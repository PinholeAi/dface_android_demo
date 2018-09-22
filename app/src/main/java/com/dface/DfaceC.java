package com.dface;

import com.dface.dto.SortSimilarity;

public class DfaceC {
    public native int identify();

    /**
     * 通道初始化
     * @param model_path 模型目录
     * @param accuracy 精度模式 0:普通模式 1:高精度模式 2:超快模式
     * @return 初始化成功标志
     **/
    public native boolean initLoad(String model_path, int accuracy);

    /**
     * 卸载通道
     * @return 卸载化成功标志
     **/
    public native boolean uninitLoad();

    /**
     * 根据两个人脸特征数组，判断相似度
     * @param feature_1 第一个特征数组
     * @param feature_2 第二个特征数组
     * @return 返回相似度
     * @note  (相似度范围(0.0~1.0),值越大越相似)
     */
    public native float similarityByFeature(float[] feature_1, float[] feature_2);


    /**
     * 比较特征值数组(1:n)，返回有序相似度(该函数支持多线程高性能并行运算)
     * @param feature_1 1个人脸人特征数组
     * @param feature_n n个人脸特征数组
     * @param top 返回前top个相似度 默认0表示返回所有
     * @param threshold 相似度阀值，只返回大于此阀值的相似度，默认0.0返回所有
     * @return 返回排序后的相似度和对应下标
     * @note  (相似度范围(0.0~1.0),值越大越相似)
     */
    public native SortSimilarity similarityByFeatureSort(float[] feature_1, float[][] feature_n, int[] idx_n, int top, float threshold);



    /**
     * 比较特征值数组(1:n)，判断最大相似度
     * @param feature_1 1个人脸人特征数组
     * @param feature_n n个人脸特征数组
     * @return 返回1:n的最大相似度
     * @note  (相似度范围(0.0~1.0),值越大越相似)
     */
    public native float similarityMaxByFeature(float[] feature_1, float[][] feature_n);



    /**
     * 比较特征值数组(1:n)，判断相似度(该函数支持多线程高性能并行运算)
     * @param feature_1 1个人脸人特征数组
     * @param feature_n n个人脸特征数组
     * @return 返回相似度数组
     * @note  (相似度范围(0.0~1.0),值越大越相似)
     */
    public native float[] similarityByFeature(float[] feature_1, float[][] feature_n);


    /**
     * 调整并行线程数量，可根据目标机器cpu内核数手动调整
     * @param numThreads 并行线程数量
     * @note (默认单线程)
     */
    public native void SetNumThreads(int numThreads);


    /**
     * 设置精度模式 (0:普通精度 1:超高精度 2:实时模式)
     * @param accuracy 精度模式
     * @note (精度越高，越耗计算量)
     */
    public native void SetAccuracy(int accuracy);

}
