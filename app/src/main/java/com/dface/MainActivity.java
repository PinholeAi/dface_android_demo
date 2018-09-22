package com.dface;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.dface.R;
import com.dface.DfaceD;
//import com.dface.Recognizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.PublicKey;
import com.Aladdin.HaspUsbHandler;


import static android.content.ContentValues.TAG;


public class MainActivity extends Activity {
    private static final int SELECT_IMAGE = 1;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static DfaceD dfaceD = new DfaceD();
    public static DfaceG dfaceG = new DfaceG();
    public static DfaceR dfaceR = new DfaceR();
    public static DfaceC dfaceC = new DfaceC();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    TextView textView;

    private BroadcastReceiver usbReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if ( HaspUsbHandler.ACTION_USB_PERMISSION.equals(action) )
            {
                synchronized (this)
                {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if ( intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) )
                    {
                        if ( device != null )
                        {
                            System.out.println("Permissions granted for USB device" + device);
                        }
                    }
                    else
                    {
                        System.out.println("Permission denied for USB device " + device);
                    }
                }
            }
        }
    };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Context context = getApplicationContext();
        IntentFilter filter = new IntentFilter(HaspUsbHandler.ACTION_USB_PERMISSION);
        filter.addAction(HaspUsbHandler.ACTION_USB_PERMISSION);
        context.registerReceiver(usbReceiver, filter);

        textView = (TextView)findViewById(R.id.tv);
        textView.setMovementMethod(new ScrollingMovementMethod());

        if ( ! "goldfish".equals(Build.HARDWARE) )
        {
            /*
             * Requests permission at the user to access USB devices.
             */
            int messages = HaspUsbHandler.getPermission(getApplicationContext());
            if ( messages != 0 )
            {
                textView.setText("Sent " + messages + " request(s) for USB access permission\n");
            }
        }

        System.loadLibrary("dfacepro");

        verifyStoragePermissions(this);
        copyFileOrDir("normal_binary");

        //模型初始化
        File sdDir = Environment.getExternalStorageDirectory();//获取SD卡根目录
        String sdPath = sdDir.toString() + "/dface/normal_binary/";
        dfaceD.initLoad(sdPath);
        dfaceG.initLoad(sdPath);
        dfaceR.initLoad(sdPath, 2);
        dfaceC.initLoad(sdPath,2);
        Button buttonDetect = (Button) findViewById(R.id.detect_demo_bt);
        buttonDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent =new Intent(MainActivity.this,DetectFaceActivity.class);
                startActivity(intent);
            }
        });


        Button buttonRgz = (Button) findViewById(R.id.recog_demo_bt);
        buttonRgz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent =new Intent(MainActivity.this,RecognizeFaceActivity.class);
                startActivity(intent);
            }
        });


        Button buttonGather = (Button) findViewById(R.id.gather_demo_bt);
        buttonGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent =new Intent(MainActivity.this,GatherFaceActivity.class);
                startActivity(intent);
            }
        });
    }


    private void copyBigDataToSD(String strOutFileName) throws IOException {
        Log.i(TAG, "start copy file " + strOutFileName);
        File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        File file = new File(sdDir.toString()+"/dface/");
        if (!file.exists()) {
            file.mkdir();
        }

        String tmpFile = sdDir.toString()+"/dface/" + strOutFileName;
        File f = new File(tmpFile);
        if (f.exists()) {
            Log.i(TAG, "file exists " + strOutFileName);
            return;
        }
        InputStream myInput;
        java.io.OutputStream myOutput = new FileOutputStream(sdDir.toString()+"/dface/"+ strOutFileName);
        myInput = this.getAssets().open(strOutFileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
        Log.i(TAG, "end copy file " + strOutFileName);

    }


    private void copyFileOrDir(String path) {
        File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        File file = new File(sdDir.toString()+"/dface/");
        if (!file.exists()) {
            file.mkdir();
        }

        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath =  file.toString() + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    dir.mkdir();
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i]);
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        File file = new File(sdDir.toString()+"/dface/");
        if (!file.exists()) {
            file.mkdir();
        }

        AssetManager assetManager = this.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = file.toString() + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    static {
//        System.loadLibrary("dfacepro");
    }
}
