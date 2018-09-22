package com.dface.dto;

/**
 * @brief 1比N返回的相似度排序信息封装
 */
public class SortSimilarity {
    /**
     * @brief 有序相似度数组
     */
    public float[] simis;
    /**
     * @brief 对应的有序下标值
     */
    public int[] idx;

    public float[] getSimis() {
        return simis;
    }

    public void setSimis(float[] simis) {
        this.simis = simis;
    }

    public int[] getIdx() {
        return idx;
    }

    public void setIdx(int[] idx) {
        this.idx = idx;
    }
}
