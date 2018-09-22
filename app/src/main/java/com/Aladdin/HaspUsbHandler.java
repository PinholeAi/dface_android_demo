/*
 *  Copyright (C) 2013, SafeNet, Inc. All rights reserved.
 *  Use is subject to license terms.
 */

package com.Aladdin;

import java.util.HashMap;
import java.util.Iterator;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

public class HaspUsbHandler {
    /**
     * Event sent when requesting permission to access USB devices.
     */
    public static final String ACTION_USB_PERMISSION = "com.Aladdin.Hasp.USB_PERMISSION";

    /**
     * Request for permissions to access all the Sentinel HL connected devices.
     *
     * You must call this function to get access to USB devices.
     *
     * If the application doesn't already have permissions, it will request them,
     * and a pop-up will be displayed at the user for confirmation.
     * When the user decides, a broadcast event will be sent, and you can
     * use a BroadcastReceiver to get the result of the user confirmation.
     *
     * The returned value is the number of permission requests made,
     * and it can be used to know how many broadcast events you should expect.
     *
     * For more details see:
     * http://developer.android.com/guide/topics/connectivity/usb/host.html
     */
    public static int getPermission(Context context)
    {
        int messages = 0;

		UsbManager manager = (UsbManager)context.getSystemService(Service.USB_SERVICE);
        if ( manager == null )
            return messages;

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while ( deviceIterator.hasNext() )
        {
            UsbDevice device = deviceIterator.next();
            if ( device.getVendorId() == 1321 && device.getProductId() == 3 )
            {
                if ( !manager.hasPermission(device) )
                {
                    PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    manager.requestPermission(device, permissionIntent);
                    ++messages;
                }
            }
        }

		/* only when everything is set, save the UsbManager in the static variable */
		/* in case of an exception, we leave with the initial null value */
		global_manager = manager;

        return messages;
    }

    /**
     * Private function used by the JNI module to access USB devices.
     */
    public static UsbDeviceConnection openDevice(String path)
    {
        UsbManager manager = global_manager;

     	if ( manager == null )
    		return null;

        UsbDevice device = manager.getDeviceList().get(path);
        if ( device == null )
            return null;

        /* note that here it's too late to request permission with requestPermission() */
        /* because the openDevice() call will proceed asynchronously without waiting for the */
        /* user's answer at the pop-up */
        return manager.openDevice(device);
    }
	
    /**
     * Private function used by the JNI module to enumerate USB devices.
     */
    public static String enumerateDevice()
    {
        UsbManager manager = global_manager;
		String list = "";

     	if ( manager == null )
    		return null;
		
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while ( deviceIterator.hasNext() )
        {
            UsbDevice device = deviceIterator.next();
            if ( device.getVendorId() == 1321 && device.getProductId() == 3 ) 
            {
                if (list.length() != 0)
                    list += ":";
                list += device.getDeviceName();
            }
        }

        return list;
    }

    /**
     * Private variable to hold the UsbManager used in the USB communication.
     *
     * Use "volatile" as it could be accessed from different threads.
     */
    private static volatile UsbManager global_manager;
}

