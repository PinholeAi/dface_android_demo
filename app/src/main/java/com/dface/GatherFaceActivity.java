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
import com.dface.dto.FaceLandmark;
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
import static com.dface.MainActivity.dfaceG;

//import static com.dface.MainActivity.rgz;

public class GatherFaceActivity extends Activity {

    private static final int SELECT_IMAGE = 1;

    private TextView infoResult;
    private ImageView imageView;
    private Bitmap yourSelectedImage = null;

    AppCompatEditText etMinFaceSize,etThreadsNumber;
    private int minFaceSize = 40;
    private int testTimeCount = 1;
    private int threadsNumber = 4;

    private boolean maxFaceSetting = false;

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
        setContentView(R.layout.activity_gather_face);

        infoResult = (TextView) findViewById(R.id.gatherInfoResult);
        imageView = (ImageView) findViewById(R.id.gatherImageView);

        etMinFaceSize = (AppCompatEditText) findViewById(R.id.gatherEtMinFaceSize);
//        etTestTimeCount = (AppCompatEditText) findViewById(R.id.gatherEtTestTimeCount);
        etThreadsNumber = (AppCompatEditText) findViewById(R.id.gatherEtThreadsNumber);


        Button buttonImage = (Button) findViewById(R.id.buttonImageGather);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_IMAGE);
            }
        });

        Button buttonDetect = (Button) findViewById(R.id.buttonGather);
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
                    infoResult.setText("线程数限制（1，2，4，8）之一");
                    return;
                }

                Log.i(TAG, "最小人脸："+minFaceSize);
                dfaceD.SetMinFace(minFaceSize);
                dfaceD.SetNumThreads(threadsNumber);
                dfaceG.SetMinFace(minFaceSize);

                //检测流程
                int width = yourSelectedImage.getWidth();
                int height = yourSelectedImage.getHeight();
                byte[] imageDate = getPixelsRGBA(yourSelectedImage);
                int faceInfo[] = null;
                DFaceMat imgMat = new DFaceMat(imageDate, width, height, 4);
                List<Bbox> faceBboxs;

                long timeDetectFace = System.currentTimeMillis();
                //检测人脸
                faceBboxs = dfaceD.detection(imgMat, true);
                if(faceBboxs.isEmpty()){
                    infoResult.setText("未检测到人脸");
                    return;
                }

                timeDetectFace = System.currentTimeMillis() - timeDetectFace;
                Log.i(TAG, "人脸检测耗时："+timeDetectFace);

                //Bbox转Rect
                List<Rect> faceRects = new ArrayList<>();
                if(!faceBboxs.isEmpty()){
                    for(Bbox box : faceBboxs){
                        faceRects.add(box.toRect());
                    }
                }

                //测试人脸质量可打开注释
                //人脸光照质量判断
//              int[] light_quality = dfaceG.predictLight(imgMat, faceRects);
                //人脸清晰度判断
//                double[] dfaceG.predictBlur(imgMat, faceRects);

                long timeLandmarksFace = System.currentTimeMillis();
                //3D姿态估计和68关键点
                List<FaceLandmark> faceLandmarks = dfaceG.predictPose(imgMat, faceRects);
                timeLandmarksFace = System.currentTimeMillis() - timeLandmarksFace;
                Log.i(TAG, "人脸采集耗时："+timeLandmarksFace);

                Bitmap drawBitmap = yourSelectedImage.copy(Bitmap.Config.ARGB_8888, true);
                //描绘3D姿态和68关键点
                if(!faceLandmarks.isEmpty()){
                    Canvas canvas = new Canvas(drawBitmap);
                    String poseInfo = new String();
                    int idx = 1;
                    for(FaceLandmark fk : faceLandmarks){
                        int left, top, right, bottom;
                        Paint paint = new Paint();
                        left = fk.getBbox().getX();
                        top = fk.getBbox().getY();
                        right = fk.getBbox().getX()+fk.getBbox().getWidth();
                        bottom = fk.getBbox().getY()+fk.getBbox().getHeight();
                        paint.setColor(Color.RED);
                        paint.setStyle(Paint.Style.STROKE);//不填充
                        paint.setStrokeWidth(5);  //线的宽度
                        canvas.drawRect(left, top, right, bottom, paint);
                        //人脸68特征点信息
                        int[] Ppoint = fk.getLandmarks2D();
                        float[] fPpoint = new float[136];
                        for(int i=0; i<Ppoint.length; ++i){
                            fPpoint[i] = (float)Ppoint[i];
                        }
                        canvas.drawPoints(fPpoint, paint);//画多个点
                        //人脸3D姿态信息
                        double[] poses = fk.getPose3D();
                        String strInfo = String.format("人脸%d姿态: yaw=%.2f, pitch=%.2f, roll=%.2f %n X=%.4f, Y=%.4f, Z=%.4f %n",idx,poses[0],poses[1],poses[2],poses[3],poses[4],poses[5]);
                        poseInfo = poseInfo+strInfo;
                        idx++;
                    }
                    poseInfo = poseInfo+"人脸采集耗时:"+timeLandmarksFace+"ms \n";
                    imageView.setImageBitmap(drawBitmap);
                    infoResult.setText(poseInfo);
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
