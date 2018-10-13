package com.dface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dface.dto.Bbox;
import com.dface.dto.DFaceMat;
import com.dface.dto.FaceMatBbox;
import com.dface.dto.Rect;
import com.dface.dto.SortSimilarity;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.dface.MainActivity.dfaceD;
import static com.dface.MainActivity.dfaceR;
import static com.dface.MainActivity.dfaceC;

public class RecognizeFaceActivity extends AppCompatActivity {

    private static final int SELECT_FACE_1 = 101;
    private static final int SELECT_FACE_2 = 102;
    private static final int COMPARE_FACE = 103;


    private TextView compareResult;
    private ImageView faceView1;
    private ImageView faceView2;

    private Bitmap yourSelectedImage1 = null;
    private Bitmap yourSelectedImage2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_face);

        compareResult = (TextView) findViewById(R.id.compareResult);
        faceView1 = (ImageView) findViewById(R.id.faceView1);
        faceView2 = (ImageView) findViewById(R.id.faceView2);


        Button buttonFace1 = (Button) findViewById(R.id.buttonFace1);
        buttonFace1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_FACE_1);
            }
        });


        Button buttonFace2 = (Button) findViewById(R.id.buttonFace2);
        buttonFace2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_FACE_2);
            }
        });


        Button buttonCompare = (Button) findViewById(R.id.buttonCompare);
        buttonCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                int width_1 = yourSelectedImage1.getWidth();
                int height_1 = yourSelectedImage1.getHeight();
                int width_2 = yourSelectedImage2.getWidth();
                int height_2 = yourSelectedImage2.getHeight();
                byte[] imageData_1 = getPixelsRGBA(yourSelectedImage1);
                byte[] imageData_2 = getPixelsRGBA(yourSelectedImage2);
                DFaceMat img1_mat = new DFaceMat();
                img1_mat.setData(imageData_1);
                img1_mat.setWidth(width_1);
                img1_mat.setHeight(height_1);
                img1_mat.setChannel(4);

                DFaceMat img2_mat = new DFaceMat();
                img2_mat.setData(imageData_2);
                img2_mat.setWidth(width_2);
                img2_mat.setHeight(height_2);
                img2_mat.setChannel(4);
                dfaceD.SetNumThreads(4);
                List<Bbox> bbox1_lst = dfaceD.detectionMaxFace(img1_mat, true);
                List<Bbox> bbox2_lst = dfaceD.detectionMaxFace(img2_mat, true);


                Bitmap drawBitmap = yourSelectedImage1.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap drawBitma2 = yourSelectedImage2.copy(Bitmap.Config.ARGB_8888, true);

                if(!bbox1_lst.isEmpty()) {
                    for (Bbox bbox : bbox1_lst) {
                        int left, top, right, bottom;
                        Canvas canvas = new Canvas(drawBitmap);
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
                    faceView1.setImageBitmap(drawBitmap);
                }else{
                    compareResult.setText("未检测到人脸");
                }


                if(!bbox2_lst.isEmpty()) {
                    for (Bbox bbox : bbox2_lst) {
                        int left, top, right, bottom;
                        Canvas canvas = new Canvas(drawBitma2);
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
                    faceView2.setImageBitmap(drawBitma2);
                }else{
                    compareResult.setText("未检测到人脸");
                }

                DFaceMat cropFace1 = dfaceD.cropFace(img1_mat, bbox1_lst.get(0));
                DFaceMat cropFace2 = dfaceD.cropFace(img2_mat, bbox2_lst.get(0));
//                DFaceMat alignFace1 = dfaceD.alignFace(cropFace1);
//                DFaceMat alignFace2 = dfaceD.alignFace(cropFace2);
                dfaceR.SetNumThreads(4);
                long timeExtractFace = System.currentTimeMillis();
                float[] feature1 = dfaceR.extractFaceFeatureByFace(cropFace1);
                float[] feature2 = dfaceR.extractFaceFeatureByFace(cropFace2);
                long durExtractTime = System.currentTimeMillis() - timeExtractFace;

//                Random random = new Random();
//                float[][] testFeatureN = new float[1000][128];
//                float[] testFeature1 = new float[128];
//                int idx[] = new int[1000];
//                for(int i=0; i<1000; ++i){
//                    for(int j=0; j<128; ++j){
//                        testFeatureN[i][j] = random.nextFloat();
//                    }
//                }
//
//                for(int i=0; i<128; ++i){
//                    testFeature1[i] = random.nextFloat();
//                }
//
//                for(int i=0; i<1000; ++i){
//                    idx[i] = i;
//                }

                long timeRecognizeFace = System.currentTimeMillis();
                float maxsimi = dfaceC.similarityByFeature(feature1, feature2);
                long durTime = System.currentTimeMillis() - timeRecognizeFace;

                compareResult.setText("相似度:"+maxsimi+" 特征提取耗时:"+durExtractTime/2 +"ms 比较耗时:"+durTime+"ms ");
                Log.i(TAG, "人脸比对运行时间:"+durTime+"ms");
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                if (requestCode == SELECT_FACE_1) {
                    Bitmap bitmap = decodeUri(selectedImage);

                    Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                    // resize to 227x227
                    //yourSelectedImage = Bitmap.createScaledBitmap(rgba, 227, 227, false);
                    yourSelectedImage1 = rgba;

                    faceView1.setImageBitmap(yourSelectedImage1);
                }else if(requestCode == SELECT_FACE_2){
                    Bitmap bitmap = decodeUri(selectedImage);

                    Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                    // resize to 227x227
                    //yourSelectedImage = Bitmap.createScaledBitmap(rgba, 227, 227, false);
                    yourSelectedImage2 = rgba;

                    faceView2.setImageBitmap(yourSelectedImage2);

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


}
