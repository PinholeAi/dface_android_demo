/*
 *  Copyright (C) 2013, SafeNet, Inc. All rights reserved.
 *  Use is subject to license terms.
 */

package com.Aladdin;

import com.Aladdin.HaspStatus;
import com.Aladdin.HaspTime;
import com.Aladdin.HaspApiVersion;

public class Hasp
{
    /**
     * handle - pointer to the resulting session handle.
     */
    private int[] handle = { 0 };

    /**
     * Unique identifier of the Feature.
     */
    private long featureid;

    /**
     * Status of the last function call.
     */
    private int status;

    /**
     * getSessionInfo() format to retrieve update info (C2V).
     */
    public static final String HASP_UPDATEINFO  = new String("<haspformat format=\"updateinfo\"/>");

    /**
     * getSessionInfo() format to retrieve session info.
     */
    public static final String HASP_SESSIONINFO = new String("<haspformat format=\"sessioninfo\"/>");

    /**
     * getSessionInfo() format to retrieve key/hardware info.
     */
    public static final String HASP_KEYINFO     = new String("<haspformat format=\"keyinfo\"/>");

    /**
     * format to retrieve host fingerprint info
     */
    public static final String HASP_FINGERPRINT= new String("<haspformat format=\"host_fingerprint\"/>");

    /**
     * format to retrieve recipient parameter for hasp_detach
     */
    public static final String HASP_RECIPIENT = new String("<haspformat root=\"location\">"+
                                                           "    <license_manager>" +
                                                           "    <attribute name=\"id\" />" +
                                                           "    <attribute name=\"time\" />" +
                                                           "    <element name=\"hostname\" />" +
                                                           "    <element name=\"version\" />" +
                                                           "    <element name=\"host_fingerprint\" />" +
                                                           "  </license_manager>" +
                                                           "</haspformat> \n");

    /**
     * AND-mask used to identify the Feature type.
     */
    public static final long HASP_FEATURETYPE_MASK       = 0xffff0000;

    /**
     * After AND-ing with HASP_FEATURETYPE_MASK, the Feature type contains
     * this value.
     */
    public static final long HASP_PROGNUM_FEATURETYPE    = 0xffff0000;

    /**
     * AND-mask used to extract program number from Feature ID
     * if program number Feature.
     */
    public static final long HASP_PROGNUM_MASK = 0x000000ff;

    /**
     * AND-mask used to identify "prognum" options.
     *
     * The following "prognum" options can be identified:
     *
     * <ul>
     *   <li>HASP_PROGNUM_OPT_NO_LOCAL
     *   <li>HASP_PROGNUM_OPT_NO_REMOTE
     *   <li>HASP_PROGNUM_OPT_PROCESS
     *   <li>HASP_PROGNUM_OPT_CLASSIC
     *   <li>HASP_PROGNUM_OPT_TS
     * </ul>
     *
     * 3 bits of the mask are reserved for future extensions and currently
     * unused. Initialize them to zero.
     */
    public static final long HASP_PROGNUM_OPT_MASK = 0x0000ff00;

    /**
     * "Prognum" option: disables local license search.
     */
    public static final long HASP_PROGNUM_OPT_NO_LOCAL = 0x00008000;

    /**
     * "Prognum" option: disables network license search.
     */
    public static final long HASP_PROGNUM_OPT_NO_REMOTE = 0x00004000;

    /**
     * "Prognum" option: sets session count of network licenses
     * to "per process".
     */
    public static final long HASP_PROGNUM_OPT_PROCESS = 0x00002000;

    /**
     * "Prognum" option: enables the API to access "classic"
     * (HASP4 or earlier) keys.
     */
    public static final long HASP_PROGNUM_OPT_CLASSIC = 0x00001000;

    /**
     * "Prognum" option: ignores Terminal Services.
     */
    public static final long HASP_PROGNUM_OPT_TS = 0x00000800;

    /**
     * HASP default Feature ID.
     * Present in every hardware key.
     */
    public static final long HASP_DEFAULT_FID = 0;

    /**
     * "Prognum" default Feature ID.
     * Present in every HASP key.
     */
    public static final long HASP_PROGNUM_DEFAULT_FID  = (HASP_DEFAULT_FID | HASP_PROGNUM_FEATURETYPE);

    /**
     * Minimal block size for hasp_encrypt() and hasp_decrypt() functions.
     */
    public static final int HASP_MIN_BLOCK_SIZE = 16;

    /**
     * Minimal block size for legacy functions hasp_legacy_encrypt()
     * and hasp_legacy_decrypt().
     */
    public static final long HASP_MIN_BLOCK_SIZE_LEGACY = 8;

    /**
     * HASP4 memory file:
     * File ID for HASP4-compatible memory contents without FAS.
     */
    public static final int HASP_FILEID_MAIN = 0xfff0;

    /**
     * HASP4 FAS memory file:
     * (Dummy) File ID for license data segment of memory contents.
     */
    public static final long HASP_FILEID_LICENSE = 0xfff2;

    /**
     * File ID for HASP secure writable memory.
     */
    public static final long HASP_FILEID_RW = 0xfff4;

    /**
     * File ID for HASP secure read only memory.
     */
    public static final long HASP_FILEID_RO = 0xfff5;

    /**
     * Returns the error that occurred in the last function call.
     */
    public int getLastError()
    {
        return status;
    }

    static
    {
        HaspStatus.Init();
    }

    /*
     * private native methods
     */
    private static native int Login(long feature_id,String vendor_code,int handle[]);
    private static native int LoginScope(long feature_id,String scope,String vendor_code,int handle[]);
    private static native int Logout(int handle);
    private static native int Encrypt(int handle, byte buffer[], int length);
    private static native int Decrypt(int handle, byte buffer[], int length);
    private static native int GetRtc(int handle, long time[]);
    private static native byte[] GetSessioninfo(int handle,String format,int status[]);
    private static native byte[] GetInfo(String scope,String format,String vendor_code,int status[]);
    private static native void Free(long info);
    private static native String Update(String update_data,int status[]);
    
    /*
     * functions to access the memory
     */
    private static native int Read(int handle, long fileid, int offset, int length, byte buffer[]);
    private static native int Write(int handle, long fileid, int offset, int length, byte buffer[]);
    private static native int GetSize(int handle, long fileid, int size[]);

    /**
     * Hasp constructor.
     *
     * For local "prognum" Features, concurrency is not handled and each
     * login performs a decrement if it is a counting license.
     * <p>
     * Network "prognum" Features only use the old HASP LM login logic,
     * with all its limitations.
     * <p>
     * Only concurrent usage of one server is supported (global server address).
     *
     * @param feature_id   Unique identifier of the Feature.
     *
     * With "prognum" Features (see HASP_FEATURETYPE_MASK),
     * 8 bits are reserved for legacy options (see
     * HASP_PROGNUM_OPT_MASK, currently 5 bits are used):
     *   <ul>
     *     <li>only local
     *     <li>only remote
     *     <li>login is counted per process ID
     *     <li>disable terminal server check
     *     <li>enable access to old (HASP3/HASP4) keys
     *   </ul>
     */
    public Hasp(long feature_id)
    {
        status = HaspStatus.HASP_STATUS_OK;
        featureid = feature_id;
        handle[0] = 0;
    }

    /*
     * Attempt to logout if object is finalized
     */
    protected void finalize()
    {
        logout();
    }

    /**
     * Logs in to a Feature.
     *
     * Establishes a session context.
     * <p>
     * If a previously established session context exists, the session
     * will be logged out.
     *
     * @param vendor_code      The Vendor Code.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #loginScope
     * @see #logout
     * @see #getLastError
     */
    public boolean login(String vendor_code)
    {
        if (vendor_code == null)
            status = HaspStatus.HASP_INV_VCODE;
        else
        {
            synchronized(this)
            {
                logout();
                status = Hasp.Login(featureid, vendor_code, handle);
            }
        }
        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Logs in to a Feature according to customizable search parameters.
     *
     * This function is an extended login function, where the search for the
     * Feature can be restricted.
     * <p>
     * If a previously established session context exists, the session
     * will be logged out.
     *
     * @param scope            The hasp_scope of the Feature search.
     * @param vendor_code      The Vendor Code.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #login
     * @see #logout
     * @see #getLastError
     */
    public boolean loginScope(String scope, String vendor_code)
    {
        if (vendor_code == null)
            status = HaspStatus.HASP_INV_VCODE;
        else if (scope == null)
            status = HaspStatus.HASP_INV_SCOPE;
        else
        {
            synchronized(this)
            {
                logout();
                status = Hasp.LoginScope(featureid, scope, vendor_code,handle);
            }
        }
        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Logs out from a session and frees all allocated resources for the session.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #login
     * @see #getLastError
     */
    public boolean logout()
    {
        if (handle[0] == 0)
        {
            status = HaspStatus.HASP_INV_HND;
            return true;
        }
        synchronized(this)
        {
            status = Hasp.Logout(handle[0]);
            if (status == HaspStatus.HASP_STATUS_OK)
                handle[0] = 0;
        }
        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Encrypts a buffer.
     *
     * This is the reverse operation of the decrypt() function.
     * <p>
     * If the encryption fails (e.g. key removed during the process) the
     * data buffer is unmodified.
     * <p>
     * This function is deprecated.
     *
     * @param buffer      The buffer to be encrypted.
     * @param length      Size in bytes of the buffer to be encrypted
     * 			(16 bytes minimum).
     *
     * @return true/false - indicates success or failure.
     *
     * @see #decrypt
     * @see #getLastError
     */
    @Deprecated
    public boolean encrypt(byte[] buffer, int length)
    {
        if (buffer == null)
          status = HaspStatus.HASP_INV_PARAM;
        else if (length > buffer.length)
          status = HaspStatus.HASP_INV_PARAM;
        else
          status = Hasp.Encrypt(handle[0], buffer, length);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Encrypts a buffer.
     *
     * This is the reverse operation of the decrypt() function.
     * <p>
     * If the encryption fails (e.g. key removed during the process) the
     * data buffer is unmodified.
     *
     * @param buffer      The buffer to be encrypted.
     * 			(16 bytes minimum).
     *
     * @return true/false - indicates success or failure.
     *
     * @see #decrypt
     * @see #getLastError
     */
    public boolean encrypt(byte[] buffer)
    {
        if (buffer == null)
            status = HaspStatus.HASP_INV_PARAM;
        else
            status = Hasp.Encrypt(handle[0], buffer, buffer.length);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Decrypts a buffer.
     *
     * This is the reverse operation of the encrypt() function.
     * <p>
     * If the decryption fails (e.g. key removed during the process) the
     * data buffer is unmodified.
     * <p>
     * This function is deprecated.
     *
     * @param buffer      The buffer to be decrypted.
     * @param length      Size in bytes of the buffer to be decrypted
     * 				(16 bytes minimum).
     *
     * @return true/false - indicates success or failure.
     *
     * @see #encrypt
     * @see #getLastError
     */
    @Deprecated
    public boolean decrypt(byte[] buffer, int length)
    {
        if (buffer == null)
          status = HaspStatus.HASP_INV_PARAM;
        else if (length > buffer.length)
          status = HaspStatus.HASP_INV_PARAM;
        else
          status = Hasp.Decrypt(handle[0], buffer, length);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Decrypts a buffer.
     *
     * This is the reverse operation of the encrypt() function.
     * <p>
     * If the decryption fails (e.g. key removed during the process) the
     * data buffer is unmodified.
     *
     * @param buffer      The buffer to be decrypted.
     * 				(16 bytes minimum).
     *
     * @return true/false - indicates success or failure.
     *
     * @see #encrypt
     * @see #getLastError
     */
    public boolean decrypt(byte[] buffer)
    {
        if (buffer == null)
            status = HaspStatus.HASP_INV_PARAM;
        else
            status = Hasp.Decrypt(handle[0], buffer, buffer.length);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Retrieves information about all system components.
     *
     * Acquires information about all system components.
     * The programmer can choose the scope and output structure of the data.
     * The function has a "scope" parameter that defines the scope using
     * XML syntax.
     * <p>
     * This function is not used in a login context, so it can be used
     * in a generic "Monitor" application.
     * <p>
     * @param      scope       XML definition of the information scope.
     * @param      format      XML definition of the output data structure.
     * @param      vendor_code The Vendor Code.
     * @return     info        The returned information (XML list).
     *
     * @see #getSessionInfo
     * @see #getLastError
     */
    public String getInfo(String scope, String format, String vendor_code)
    {
        byte[] info = { 0 };
        int[] status1 = { 0 };
        String s = null;

        status = HaspStatus.HASP_STATUS_OK;
        if (vendor_code == null)
            status = HaspStatus.HASP_INV_VCODE;
        else if (scope == null)
            status = HaspStatus.HASP_INV_SCOPE;
        else if (format == null)
            status = HaspStatus.HASP_INV_FORMAT;
        if (status != HaspStatus.HASP_STATUS_OK)
            return null;

        info = Hasp.GetInfo(scope, format, vendor_code, status1);

        status = status1[0];
        if( status == HaspStatus.HASP_STATUS_OK)
          s = new String(info);

        return s;
    }

    /**
     * Retrieves information regarding a session context.
     *
     * @param      format       XML definition of the output data structure.
     * @return     info         The returned information (XML list).
     *
     * @see #getLastError
     */
    public String getSessionInfo(String format)
    {
        byte[] info = { 0 };
        int[] status1 = { 0 };
        String s = null;

        if (format == null)
        {
            status = HaspStatus.HASP_INV_FORMAT;
            return null;
        }

        info = Hasp.GetSessioninfo(handle[0], format, status1);

        status = status1[0];
        if( status == HaspStatus.HASP_STATUS_OK)
            s = new String(info);

        return s;
    }

    /**
     * Reads from the HASP key memory.
     *
     * This function is deprecated.
     *
     * @param fileid       ID of the file to read (memory descriptor).
     * @param offset       Position in the file.
     * @param length       Number of bytes to be read from the file.
     * @param buffer       The retrieved data.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #getLastError
     * @see #write
     * @see #getSize
     */
    @Deprecated
    public boolean read(long fileid, int offset, int length, byte[] buffer)
    {
        if (buffer == null)
          status = HaspStatus.HASP_INV_PARAM;
        else if (offset < 0)
          status = HaspStatus.HASP_INV_PARAM;
        else if (length > buffer.length)
          status = HaspStatus.HASP_INV_PARAM;
        else
          status = Hasp.Read(handle[0], fileid, offset, length, buffer);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Reads from the HASP key memory.
     *
     * @param fileid       ID of the file to read (memory descriptor).
     * @param offset       Position in the file.
     * @param buffer       Buffer for the retrieved data.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #getLastError
     * @see #write
     * @see #getSize
     */
    public boolean read(long fileid, int offset, byte[] buffer)
    {
        if (buffer == null)
          status = HaspStatus.HASP_INV_PARAM;
        else if (offset < 0)
          status = HaspStatus.HASP_INV_PARAM;
        else
          status = Hasp.Read(handle[0], fileid, offset, buffer.length, buffer);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Writes to the HASP key memory.
     *
     * Depending on the provided session handle (either logged into the
     * default Feature or any other Feature), write access to the FAS
     * memory (HASP_FILEID_LICENSE) is not permitted.
     * <p>
     * This function is deprecated.
     *
     * @param fileid       ID of the file to write (memory descriptor).
     * @param offset       Position in the file.
     * @param length       Number of bytes to write to the file.
     * @param buffer       The data to write.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #getLastError
     * @see #read
     * @see #getSize
     */
    @Deprecated
    public boolean write(long fileid, int offset, int length, byte[] buffer)
    {
        if (buffer == null)
            status = HaspStatus.HASP_INV_PARAM;
        else if (offset < 0)
            status = HaspStatus.HASP_INV_PARAM;
        else if (length > buffer.length)
            status = HaspStatus.HASP_INV_PARAM;
        else
            status = Hasp.Write(handle[0], fileid, offset, length, buffer);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Writes to the HASP key memory.
     *
     * Depending on the provided session handle (either logged into the
     * default Feature or any other Feature), write access to the FAS
     * memory (HASP_FILEID_LICENSE) is not permitted.
     *
     * @param fileid       ID of the file to write (memory descriptor).
     * @param offset       Position in the file.
     * @param buffer       The data to write.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #getLastError
     * @see #read
     * @see #getSize
     */
    public boolean write(long fileid, int offset, byte[] buffer)
    {
        if (buffer == null)
            status = HaspStatus.HASP_INV_PARAM;
        else if (offset < 0)
            status = HaspStatus.HASP_INV_PARAM;
        else
            status = Hasp.Write(handle[0], fileid, offset, buffer.length, buffer);

        return (status == HaspStatus.HASP_STATUS_OK);
    }

    /**
     * Retrieves the byte size of a memory file from a HASP key.
     *
     * @param  fileid       ID of the file to be queried.
     *
     * @return Size of the file.
     *
     * @see #getLastError
     * @see #read
     * @see #write
     */
    public int getSize(long fileid)
    {
        int[] size = { 0 };
        status = Hasp.GetSize(handle[0], fileid, size);
        return size[0];
    }

    /**
     * Writes update information to a HASP key.
     *
     * The update BLOB contains all necessary data to perform the update:
     * Where to write (to which HASP key), the necessary
     * access data (Vendor Code) and the update itself.
     * <p>
     * If requested by the update BLOB, the function returns an Acknowledge BLOB,
     * which is signed/encrypted by the updated instance and contains
     * proof that this update was successfully installed.
     *
     * @param      update_data      The complete update data.
     *
     * @return     ack_data         The acknowledged data (if requested).
     *
     * @see #getLastError
     */
    public String update(String update_data)
    {
        int[] dll_status = {0};
        String s = null;

        if (update_data == null) {
            status = HaspStatus.HASP_INV_PARAM;
            return null;
        }

        s = Hasp.Update(update_data, dll_status);
        status = dll_status[0];

        return s;
    }

    /**
     * Reads the current time from a HASP Time key.
     *
     * Time values are returned as the number of seconds that have elapsed
     * since Jan-01-1970 0:00:00 UTC.
     * <p>
     * The general purpose of this function is to
     * obtain reliable timestamps that are independent from the system clock.
     *
     * @return A HaspTime object.
     */
    public HaspTime getRealTimeClock()
    {
        long[] time = { 0 };
        HaspTime rtcTime;
        status = Hasp.GetRtc(handle[0], time);
        rtcTime = new HaspTime(time[0]);

        if (status == HaspStatus.HASP_STATUS_OK)
          status = rtcTime.getLastError();

        return rtcTime;
    }

    /**
     * Sets the HASP License Manager idle time.
     *
     * @param  idle_time    Idle time in minutes. Set to 0 for default value.
     *
     * @return true/false - indicates success or failure.
     *
     * @see #getLastError
     */
    /*
      public boolean setIdletime(short idle_time)
      {
          status = Hasp.SetIdletime(handle[0], idle_time);
          if (status == HaspStatus.HASP_STATUS_OK)
              return true;

          return false;
      }
    */

    /**
     * Reads the HASP API Version.
     *
     * @param vendor_code      The Vendor Code.
     *
     * @return A HaspApiVersion object.
     *
     * @see #getLastError
     */
    public HaspApiVersion getVersion(String vendor_code)
    {
        HaspApiVersion version;
        version = new HaspApiVersion(vendor_code);
        status = version.getLastError();

        return version;
    }
}

