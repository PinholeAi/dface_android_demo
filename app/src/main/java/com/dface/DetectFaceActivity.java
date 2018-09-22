package com.dface;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dface.dto.Bbox;
import com.dface.dto.DFaceMat;
import com.dface.dto.Rect;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import static android.content.ContentValues.TAG;
import static com.dface.MainActivity.dfaceD;
//import static com.dface.MainActivity.dfaceG;

//import static com.dface.MainActivity.rgz;

public class DetectFaceActivity extends Activity {

    private static final int SELECT_IMAGE = 1;

    private TextView infoResult;
    private ImageView imageView;
    private Bitmap yourSelectedImage = null;

    AppCompatEditText etMinFaceSize,etThreadsNumber;
    private int minFaceSize = 40;
    private int testTimeCount = 1;
    private int threadsNumber = 4;

    private boolean maxFaceSetting = false;

//    private Detector mtcnn = new Detector();
//
//    private Recognizer rgz = new Recognizer();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_face);

        infoResult = (TextView) findViewById(R.id.infoResult);
        imageView = (ImageView) findViewById(R.id.imageView);

        etMinFaceSize = (AppCompatEditText) findViewById(R.id.etMinFaceSize);
//        etTestTimeCount = (AppCompatEditText) findViewById(R.id.etTestTimeCount);
        etThreadsNumber = (AppCompatEditText) findViewById(R.id.etThreadsNumber);


        Button buttonImage = (Button) findViewById(R.id.buttonImage);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_IMAGE);
            }
        });

        Button buttonDetect = (Button) findViewById(R.id.buttonDetect);
        buttonDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (yourSelectedImage == null)
                    return;

                minFaceSize = Integer.valueOf(TextUtils.isEmpty(etMinFaceSize.getText().toString()) ? "40" : etMinFaceSize.getText().toString());
//                testTimeCount = Integer.valueOf(TextUtils.isEmpty(etTestTimeCount.getText().toString()) ? "10" : etTestTimeCount.getText().toString());
                threadsNumber = Integer.valueOf(TextUtils.isEmpty(etThreadsNumber.getText().toString()) ? "4" : etThreadsNumber.getText().toString());

                if (threadsNumber != 1&&threadsNumber != 2&&threadsNumber != 4&&threadsNumber != 8){
                    Log.i(TAG, "线程数："+threadsNumber);
                    infoResult.setText("线程数限制（1，2，4，8）");
                    return;
                }

                Log.i(TAG, "最小人脸："+minFaceSize);
                dfaceD.SetMinFace(minFaceSize);
//                mtcnn.SetTimeCount(testTimeCount);
                dfaceD.SetNumThreads(threadsNumber);

                //检测流程
                int width = yourSelectedImage.getWidth();
                int height = yourSelectedImage.getHeight();
                byte[] imageDate = getPixelsRGBA(yourSelectedImage);
                int faceInfo[] = null;
                DFaceMat imgMat = new DFaceMat(imageDate, width, height, 4);
                List<Bbox> faceBboxs;

                long timeDetect = System.currentTimeMillis();
                faceBboxs = dfaceD.detection(imgMat, true);
                timeDetect = System.currentTimeMillis() - timeDetect;
                Log.i(TAG, "人脸平均检测时间："+timeDetect);

                List<Rect> bboxs = new ArrayList<>();

                Bitmap drawBitmap = yourSelectedImage.copy(Bitmap.Config.ARGB_8888, true);

                if(!faceBboxs.isEmpty()) {
                    Canvas canvas = new Canvas(drawBitmap);
                    for (Bbox bbox : faceBboxs) {
                        int left, top, right, bottom;
                        Paint paint = new Paint();
                        left = bbox.getX1();
                        top = bbox.getY1();
                        right = bbox.getX2();
                        bottom = bbox.getY2();
                        paint.setColor(Color.RED);
                        paint.setStyle(Paint.Style.STROKE);//不填充
                        paint.setStrokeWidth(5);  //线的宽度
                        canvas.drawRect(left, top, right, bottom, paint);
                        //画特征点
                        float[] Ppoint = bbox.getPpoint();
                        canvas.drawPoints(new float[]{Ppoint[0], Ppoint[5],
                                Ppoint[1], Ppoint[6],
                                Ppoint[2], Ppoint[7],
                                Ppoint[3], Ppoint[8],
                                Ppoint[4], Ppoint[9]}, paint);//画多个点
                    }
                    imageView.setImageBitmap(drawBitmap);
                    infoResult.setText("检测耗时:"+timeDetect+"ms");
                }else{
                    infoResult.setText("未检测到人脸");
                }

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                if (requestCode == SELECT_IMAGE) {
                    Bitmap bitmap = decodeUri(selectedImage);

                    Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                    // resize to 227x227
                    //yourSelectedImage = Bitmap.createScaledBitmap(rgba, 227, 227, false);
                    yourSelectedImage = rgba;

                    imageView.setImageBitmap(yourSelectedImage);
                }
            } catch (FileNotFoundException e) {
                Log.e("MainActivity", "FileNotFoundException");
                return;
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    //提取像素点
    private byte[] getPixelsRGBA(Bitmap image) {
        // calculate how many bytes our image consists of
        int bytes = image.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer
        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer
        byte[] temp = buffer.array(); // Get the underlying array containing the
        return temp;
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


}
