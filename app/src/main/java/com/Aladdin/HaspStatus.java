/*
 *  Copyright (C) 2013, SafeNet, Inc. All rights reserved.
 *  Use is subject to license terms.
 */

package com.Aladdin;

public class HaspStatus
{
    /**
     * return codes
     */

    /**
     * No error occurred
     */
    public static final int HASP_STATUS_OK              = 0;

    /**
     * Invalid memory address
     */
    public static final int HASP_MEM_RANGE              = 1;

    /**
     * Unknown/invalid Feature ID option
     */
    public static final int HASP_INV_PROGNUM_OPT        = 2;

    /**
     * Memory allocation failed
     */
    public static final int HASP_INSUF_MEM              = 3;

    /**
     * Too many open Features
     */
    public static final int HASP_TMOF                   = 4;

    /**
     * Feature access denied
     */
    public static final int HASP_ACCESS_DENIED          = 5;

    /**
     * Incompatible Feature
     */
    public static final int HASP_INCOMPAT_FEATURE       = 6;

    /**
     * Sentinel HASP protection key not available
     */
    public static final int HASP_HASP_NOT_FOUND         = 7;

    /**
     * Deprecated - License container not found
     */
    public static final int HASP_CONTAINER_NOT_FOUND    = 7;

    /**
     * En-/decryption length too short
     */
    public static final int HASP_TOO_SHORT              = 8;

    /**
     * Invalid handle
     */
    public static final int HASP_INV_HND                = 9;

    /**
     * Invalid file ID / memory descriptor
     */
    public static final int HASP_INV_FILEID             = 10;

    /**
     * Driver or support daemon version too old
     */
    public static final int HASP_OLD_DRIVER             = 11;

    /**
     * Real time support not available
     */
    public static final int HASP_NO_TIME                = 12;

    /**
     * Generic error from host system call
     */
    public static final int HASP_SYS_ERR                = 13;

    /**
     * Hardware key driver not found
     */
    public static final int HASP_NO_DRIVER              = 14;

    /**
     * Unrecognized info format
     */
    public static final int HASP_INV_FORMAT             = 15;

    /**
     * Request not supported
     */

    public static final int HASP_REQ_NOT_SUPP           = 16;

    /**
     * Invalid update object
     */
    public static final int HASP_INV_UPDATE_OBJ         = 17;

    /**
     * Key with requested ID was not found
     */
    public static final int HASP_KEYID_NOT_FOUND        = 18;

    /**
     * Update data consistency check failed
     */
    public static final int HASP_INV_UPDATE_DATA        = 19;

    /**
     * Update not supported by this key
     */
    public static final int HASP_INV_UPDATE_NOTSUPP     = 20;

    /**
     * Update counter mismatch
     */
    public static final int HASP_INV_UPDATE_CNTR        = 21;

    /**
     * Invalid vendor code
     */
    public static final int HASP_INV_VCODE              = 22;

    /**
     * Requested encryption algorithm not supported
     */
    public static final int HASP_ENC_NOT_SUPP           = 23;

    /**
     * Invalid date / time
     */
    public static final int HASP_INV_TIME               = 24;

    /**
     * Clock has no power
     */
    public static final int HASP_NO_BATTERY_POWER       = 25;

    /**
     * Update requested acknowledgement, but no area to return it
     */
    public static final int HASP_NO_ACK_SPACE           = 26;

    /**
     * Terminal services (remote terminal) detected
     */
    public static final int HASP_TS_DETECTED            = 27;

    /**
     * Feature type not implemented
     */
    public static final int HASP_FEATURE_TYPE_NOT_IMPL  = 28;

    /**
     * Unknown algorithm
     */
    public static final int HASP_UNKNOWN_ALG            = 29;

    /**
     * Signature check failed
     */
    public static final int HASP_INV_SIG                = 30;

    /**
     * Feature not found
     */
    public static final int HASP_FEATURE_NOT_FOUND      = 31;

    /**
     * Trace log is not enabled
     */
    public static final int HASP_NO_LOG                 = 32;

    /**
     * Communication error between application and local HASP License Manager
     */
    public static final int HASP_LOCAL_COMM_ERR         = 33;

    /**
     * Vendor Code not recognized by API
     */
    public static final int HASP_UNKNOWN_VCODE          = 34;

    /**
     * Invalid XML spec
     */
    public static final int HASP_INV_SPEC               = 35;

    /**
     * Invalid XML scope
     */
    public static final int HASP_INV_SCOPE              = 36;

    /**
     * Too many Sentinel HASP protection keys currently connected
     */
    public static final int HASP_TOO_MANY_KEYS          = 37;

    /**
     * Too many concurrent user sessions currently connected
     */
    public static final int HASP_TOO_MANY_USERS         = 38;

    /**
     * Session has been interrupted
     */
    public static final int HASP_BROKEN_SESSION         = 39;

    /**
     * Communication error between local and remote HASP License Manager
     */
    public static final int HASP_REMOTE_COMM_ERR        = 40;

    /**
     * Feature expired
     */
    public static final int HASP_FEATURE_EXPIRED        = 41;

    /**
     * HASP License Manager version too old
     */
    public static final int HASP_OLD_LM                 = 42;

    /**
     * Input/Output error occurred in secure storage area of HASP SL key OR
     * a USB error occurred when communicating with a HASP HL key
     */
    public static final int HASP_DEVICE_ERR             = 43;

    /**
     * Update installation not permitted
     */
    public static final int HASP_UPDATE_BLOCKED         = 44;

    /**
     * System time has been tampered with
     */
    public static final int HASP_TIME_ERR               = 45;

    /**
     * Communication error occurred in secure channel
     */
    public static final int HASP_SCHAN_ERR              = 46;

    /**
     * Corrupt data exists in secure storage area of HASP SL protection key
     */
    public static final int HASP_STORAGE_CORRUPT        = 47;

    /**
     * Unable to find Vendor library
     */
    public static final int HASP_NO_VLIB                = 48;

    /**
     * Unable to load Vendor library
     */
    public static final int HASP_INV_VLIB               = 49;

    /**
     * Unable to locate any Features matching scope
     */
    public static final int HASP_SCOPE_RESULTS_EMPTY    = 50;

    /**
     * Program running on a virtual machine
     */
    public static final int HASP_VM_DETECTED            = 51;

    /**
     * HASP SL key incompatible with machine hardware; HASP SL key is locked
     * to different hardware. OR:
     * In the case of a V2C file, conflict between HASP SL key data and machine
     * hardware data; HASP SL key locked to different hardware
     */
    public static final int HASP_HARDWARE_MODIFIED      = 52;

    /**
     * Login denied because of user restrictions
     */
    public static final int HASP_USER_DENIED            = 53;

    /**
     * Trying to install a V2C file with an update counter that is out of
     * sequence with the update counter on Sentinel HASP protection key. The
     * update counter value in the V2C file is lower than the value in Sentinel
     * HASP protection key.
     */
    public static final int HASP_UPDATE_TOO_OLD         = 54;

    /**
     * Trying to install a V2C file with an update counter that is out of
     * sequence with update counter in Sentinel HASP protection key. The first
     * value in the V2C file is greater than the value in Sentinel HASP
     * protection key.
     */
    public static final int HASP_UPDATE_TOO_NEW         = 55;

    /**
     * Vendor library version too old
     */
    public static final int HASP_OLD_VLIB               = 56;

    /**
     * Upload via ACC failed, e.g. because of illegal format
     */
    public static final int HASP_UPLOAD_ERROR           = 57;

    /**
     * Invalid XML "recipient" parameter for hasp_detach
     */
    public static final int HASP_INV_RECIPIENT          = 58;

    /**
     * Invalid XML "detach_action" parameter for hasp_detach
     */
    public static final int HASP_INV_DETACH_ACTION      = 59;

    /**
     * Scope for hasp_detach does not select a unique Product
     */
    public static final int HASP_TOO_MANY_PRODUCTS      = 60;

    /**
     * Invalid Product information
     */
    public static final int HASP_INV_PRODUCT            = 61;

    /**
     * Unknown Recipient; update can only be applied to the
     * Recipient specified in detach(), and not to this computer
     */
    public static final int HASP_UNKNOWN_RECIPIENT      = 62;

    /**
     * Invalid duration
     */
    public static final int HASP_INV_DURATION           = 63;

    /**
     * Cloned HASP SL storage detected
     */
    public static final int HASP_CLONE_DETECTED         = 64;

    /**
     * Specified v2c update already installed in the LLM
     */
    public static final int HASP_UPDATE_ALREADY_ADDED   = 65;

    /**
     * Specified Hasp Id is in Inactive state
     */
    public static final int HASP_HASP_INACTIVE          = 66;

    /**
     * No detachable feature exists
     */
    public static final int HASP_NO_DETACHABLE_FEATURE  = 67;

    /**
     * No detachable feature exists (typo; kept for compatibility)
     */
    public static final int HASP_NO_DEATCHABLE_FEATURE  = 67;

    /**
     * scope does not specify a unique Host
     */
    public static final int HASP_TOO_MANY_HOSTS         = 68;

    /**
     * Rehost is not allowed for any license
     */
    public static final int HASP_REHOST_NOT_ALLOWED     = 69;

    /**
     * License is rehosted to other machine
     */
    public static final int HASP_LICENSE_REHOSTED       = 70;

    /**
     * Old rehost license try to apply
     */
    public static final int HASP_REHOST_ALREADY_APPLIED = 71;

    /**
     * File not found or access denied
     */
    public static final int HASP_CANNOT_READ_FILE       = 72;

    /**
     * Extension of license not allowed as number of detached
     * licenses is greater than current concurrency count
     */
    public static final int HASP_EXTENSION_NOT_ALLOWED  = 73;

    /**
     * Detach of license not allowed as product
     * contains VM disabled feature and host machine is a virtual machine
     */
    public static final int HASP_DETACH_DISABLED        = 74;

    /**
     * Rehost of license not allowed as container
     * contains VM disabled feature and host machine is a virtual machine
     */
    public static final int HASP_REHOST_DISABLED        = 75;

    /**
     * Format SL-AdminMode or migrate SL-Legacy to SL-AdminMode not allowed
     * as container has detached license
     */
    public static final int HASP_DETACHED_LICENSE_FOUND = 76;

    /**
     * Recipient of the requested operation is older than expected
     */
    public static final int HASP_RECIPIENT_OLD_LM       = 77;

    /**
     * Secure storage ID mismatch
     */
    public static final int HASP_SECURE_STORE_ID_MISMATCH = 78;

    /**
     * Secure storage ID mismatch
     */
    public static final int HASP_DUPLICATE_HOSTNAME = 79;

    /**
     * Secure storage ID mismatch
     */
    public static final int HASP_MISSING_LM = 80;


    /**
     * A required API dynamic library was not found
     */
    public static final int HASP_NO_API_DYLIB           = 400;

    /**
     * The found and assigned API dynamic library could not verified.
     */
    public static final int HASP_INV_API_DYLIB          = 401;

    /**
     * Invalid parameter
     */
    public static final int HASP_INV_PARAM              = 501;

    /*
     * Internal use: no classic memory extension block available
     */
    public static final int HASP_NO_EXTBLOCK          = 600;

    /*
     * Invalid port type
     */
    public static final int HASP_INV_PORT_TYPE          = 650;

    /*
     * Invalid port value
     */
    public static final int HASP_INV_PORT               = 651;

    /**
     * Functionality is not available
     */
    public static final int HASP_NOT_IMPL               = 698;

    /**
     * Internal API error
     */
    public static final int HASP_INT_ERR                = 699;

    /*
     * Invalid port value
     */
    public static final int HASP_FIRST_HELPER               = 2001;

    /*
     * Reserved for Sentinel Activation API
     */
    public static final int HASP_FIRST_HASP_ACT             = 3001;

    public static String runtime_library = "HASPJava";

    public static void Init()
    {
        try
        {
             System.loadLibrary(runtime_library);
        }
        catch (UnsatisfiedLinkError e)
        {
            if (e.getMessage().indexOf("already loaded in another classloader") == -1)
            {
                throw e;
            }
        }
    }
}

