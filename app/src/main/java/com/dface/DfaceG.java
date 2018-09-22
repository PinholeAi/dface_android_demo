package com.dface;
import com.dface.dto.DFaceMat;
import com.dface.dto.FaceLandmark;
import com.dface.dto.Rect;
import java.util.List;

public class DfaceG {
    public native int identify();
    /**
     * 通道初始化
     * @param model_path 模型目录
     * @return 初始化成功标志
     **/
    public native boolean initLoad(String model_path);

    /**
     * 卸载通道
     * @return 卸载化成功标志
     **/
    public native boolean uninitLoad();

    /**
     * 根据一张静态图片, 定位人脸68关键点和头部3D角度
     * @param img 图片数据,以DFaceMat格式输入
     * @param bboxs 人脸在图像中对应的边框信息
     * @return 返回正脸世界坐标系的的旋转欧拉角和位置偏移量 例如(yaw,pitch,roll,t_x,t_y,t_z)
     * yaw,pitch,roll: 分别表示头部的偏航角，俯仰角，滚转角  t_x, t_y, t_z: 分别表示头部相对于正脸世界坐标系原点的偏移量(坐标值)
     * @note ()
     */
    public native List<FaceLandmark> predictPose(DFaceMat img, List<Rect> bboxs);

    /**
     * 判断人脸清晰度
     * @param img 图片数据,以DFaceMat格式输入
     * @param bboxs 人脸在图像中对应的边框信息
     * @return 返回每个人脸对应的清晰度
     * note (正常人脸清晰度500以上，较模糊的人脸3清晰度在30~300之间)
     */
    public native double[] predictBlur(DFaceMat img, List<Rect> bboxs);

    /**
     * 预测人脸年龄
     * @param img 图片数据,以DFaceMat格式输入
     * @param bboxs 人脸在图像中对应的边框信息
     * @return 返回每个人脸对应的年龄
     * note ()
     */
    public native int[] predictAge(DFaceMat img, List<Rect> bboxs);

    /**
     * 预测人脸性别
     * @param img 图片数据,以DFaceMat格式输入
     * @param bboxs 人脸在图像中对应的边框信息
     * @return 返回每个人脸对应的性别
     * note ()
     */
    public native int[] predictSex(DFaceMat img, List<Rect> bboxs);

    /**
     * 设置最小人脸尺寸
     *
     * @param size 最小人脸尺寸
     */
    public native void SetMinFace(int size);

    /**
     * 设置最大人脸尺寸
     *
     * @param size 最小人脸尺寸
     */
    public native void SetMaxFace(int size);

    /**
     * 设置摄像机参数
     *
     * @param focalLength 相机焦距，默认500
     * @param opticalCenterX 相机光轴中心X轴偏移
     * @param opticalCenterY 相机光轴中心Y轴偏移
     */
    public native boolean setCameraParameter(float focalLength, float opticalCenterX, float opticalCenterY);

}
