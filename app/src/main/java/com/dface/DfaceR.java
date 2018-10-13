package com.dface;

import com.dface.dto.DFaceMat;

public class DfaceR {
    public native int identify();

    /**
     * 通道初始化
     * @param model_path 模型目录
     * @param accuracy 精度模式 0:普通模式 1:高精度模式 2:实时模式
     * @return 初始化成功标志
     **/
    public native boolean initLoad(String model_path, int accuracy);

    /**
     * 卸载通道
     * @return 卸载化成功标志
     **/
    public native boolean uninitLoad();

    /**
     * 比较2个人脸相似度(1:1)
     * @param face1 第1个人脸Mat数据(已被检测和裁剪的人脸，参考DfaceD::cropFace), 以DFaceMat格式输入
     * @param face2 第2个人脸Mat数据(已被检测和裁剪的人脸，参考DfaceD::cropFace), 以以DFaceMat格式输入格式输入
     * @return 返回相似度,一般大于0.9表示同一个人
     * @note  (相似度范围(0.0~1.0),值越大越相似,一般大于0.9表示同一个人)
     */
    public native float similarity1V1ByFace(DFaceMat face1, DFaceMat face2);


    /**
     * 比较2个人脸相似度(1:1)
     * @param face1_path 第1个人脸地址
     * @param face2_path 第2个人脸地址
     * @return 返回相似度,一般大于0.9表示同一个人
     * @note  (相似度范围(0.0~1.0),值越大越相似,一般大于0.9表示同一个人)
     */
    public native float similarity1V1ByFace(String face1_path, String face2_path);


    /**
     * 比较2个人脸距离(1:1)
     * @param face1 第1个人脸Mat数据(已被检测和裁剪的人脸，参考DfaceD::cropFace), 以DFaceMat格式输入
     * @param face2 第2个人脸Mat数据(已被检测和裁剪的人脸，参考DfaceD::cropFace), 以DFaceMat格式输入
     * @return 返回两个人脸的特征差异值
     * @note  (差异值范围(0.0~1.0), 值越大差异越小)
     */
    public native float diff1V1ByFace(DFaceMat face1, DFaceMat face2);


    /**
     * 比较2个人脸距离(1:1)
     * @param face1_path 第1个人脸地址
     * @param face2_path 第2个人脸地址
     * @return 返回两个人脸的特征差异值
     * @note  (差异值范围(0.0~1.0), 值越大差异越小)
     */
    public native float diff1V1ByFace(String face1_path, String face2_path);


    /**
     * 抽取人脸特征(用于比对识别)
     * @param face 人脸Mat数据(已被检测和裁剪的人脸，参考DfaceD::cropFace)，以DFaceMat格式输入
     * @return 返回人脸特征(512个float浮点数，实时模式则返回128个float浮点数)
     * @note  ()
     */
    public native float[] extractFaceFeatureByFace(DFaceMat face);


    /**
     * 抽取人脸特征(用于比对识别)
     * @param face_path 人脸图片地址(已被检测和裁剪的人脸，参考DfaceD::cropFace)，以string格式输入
     * @return 返回人脸特征(512个float浮点数，实时模式则返回128个float浮点数)
     * @note  ()
     */
    public native float[] extractFaceFeatureByFace(String face_path);


    /**
     * 根据两个人脸特征数组，判断相似度
     * @param feature_1 第一个人脸特征(512或者128个浮点数)
     * @param feature_2 第二个人脸特征(512或者128个浮点数)
     * @return 返回相似度 一般大于0.9表示同一个人
     * @note  (相似度范围(0.0~1.0),值越大越相似,一般大于0.9表示同一个人)
     */
    public native float similarityByFeature(float[] feature_1, float[] feature_2);


    /**
     * 调整并行线程数量，可根据目标机器cpu内核数手动调整
     * @param numThreads 并行线程数量
     * @note (默认单线程)
     */
    public native void SetNumThreads(int numThreads);



}
