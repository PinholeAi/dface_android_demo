package com.dface;
import com.dface.dto.Bbox;
import com.dface.dto.DFaceMat;
import com.dface.dto.FaceMatBbox;
import com.dface.dto.FaceMatBbox;
import com.dface.dto.Rect;
import java.util.List;

public class DfaceD {

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
     * 检测图片所有的人脸参数，包括边框，五官定位，可信度
     * @param img 图片数据,以Mat格式输入
     * @return 返回所有人脸边框信息数组
     * @note (当未检测到人脸，finalBbox为空List)
     **/
    public native List<Bbox> detection(DFaceMat img, boolean square);

    /**
     * 检测图片所有的人脸参数，包括边框，五官定位，可信度
     * @param img_path 图片地址
     * @return 返回所有n个人脸边框信息数组
     * @note (当未检测到人脸，finalBbox为空列表)
     */
    public native List<Bbox> detection(String img_path, boolean square);

//    /**
//     * 被检测到的人脸数据，高精度调整人脸信息
//     * @param img 已经被检测到的人脸图片数据(图片只包含一个人脸，且已经被裁剪对齐)
//     * @param finalBbox 返回高精度人脸边框信息
//     * @return 参考参考finalBbox
//     * @note (当未检测到人脸，finalBbox为空vector)
//     */
//    public native void detectionLimited(byte[] img, List<Bbox> finalBbox);


    /**
     * 检测图片的最大人脸参数，包括边框，五官定位，可信度
     * @param img 图片数据,以Mat格式输入
     * @return 参考finalBbox
     * @note (当未检测到人脸，finalBbox为空列表)
     */
    public native List<Bbox> detectionMaxFace(DFaceMat img, boolean square);


    /**
     * 输入1张图片地址, 返回被检测到的最大1个人脸参数，包括边框，五官定位，可信度
     * @param img_path 图片地址
     * @return 返回人脸边框信息数组(size=1)
     * @note (当未检测到人脸，finalBbox为空列表)
     */
    public native List<Bbox> detectionMaxFace(String img_path, boolean square);


    /**
     * 裁剪对齐人脸
     * @param face 被裁剪的人脸数据，人脸Size可任意
     * @return 参照out_face
     * @note (返回人脸 Size(112x112), 一般用于人脸识别通道特征提取和比对输入)
     */
    public native DFaceMat alignFace(DFaceMat face);


    /**
     * 检测最大人脸并裁剪对齐
     * @param img 图片数据,以Mat格式输入
     * @return 返回人脸数据，包括回归框，关键点信息
     * @note (返回人脸 Size(112x112), 一般用于人脸识别通道的人脸特征提取和人脸比对输入)
     */
    public native FaceMatBbox detectionMaxFaceWithAlign(DFaceMat img);


    /**
     * 检测人脸并裁剪对齐
     * @param img 图片数据,以Mat格式输入
     * @return 返回人脸数据(输出人脸尺寸 112*112)，包括回归框，关键点信息
     * @note (返回人脸 Size(112x112), 一般用于人脸识别通道的人脸特征提取和人脸比对输入)
     */
    public native List<FaceMatBbox> detectionWithAlign(DFaceMat img);


    /**
     * 裁剪人脸矩形框
     * @param img 被裁剪的图片
     * @param rect 需要被裁剪的矩形区域
     * @return 返回的被裁剪人脸数据
     * @note (被裁减返回的人脸Size依据输入的rect)
     */
    public native DFaceMat cropFace(DFaceMat img, Rect rect);


    /**
     * 裁剪人脸矩形框
     * @param img 被裁剪的图片
     * @param bbox 被检测到的人脸边框信息
     * @return 返回的被裁剪人脸数据
     * @note (被裁减返回的人脸Size依据输入的rect)
     */
    public native DFaceMat cropFace(DFaceMat img, Bbox bbox);


    /**
     * 调整并行线程数量，可根据目标机器cpu内核数手动调整
     * @param numThreads 并行线程数量
     * @note (默认单线程)
     */
    public native void SetNumThreads(int numThreads);

//    /**
//     * 保留
//     */
//    public native void SetTimeCount(int timeCount);

    /**
     * 调整需要被检测的最小人脸Size，该值越大，往往检测速度越快，但会漏检小于该尺寸的人脸
     * @param minSize 最小人脸尺寸
     * @note (默认60，可按输入的图片分辨率适当调整，640*480P建议40-60 1280*960P建议100-160)
     */
    public native void SetMinFace(int minSize);

}
