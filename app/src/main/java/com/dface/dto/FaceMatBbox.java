package com.dface.dto;

/**
 * @brief 人脸数据接对应的边框封装类
 */
public class FaceMatBbox {
    /**
     * @brief 人脸Mat数据
     */
    public DFaceMat faceMat;
    /**
     * @brief 人脸边框信息
     */
    public Bbox bbox;

    public FaceMatBbox(){

    }

    public FaceMatBbox(DFaceMat faceMat, Bbox faceBox){
        this.faceMat = faceMat;
        this.bbox = faceBox;
    }

    public DFaceMat getFaceMat() {
        return faceMat;
    }

    public void setFaceMat(DFaceMat faceMat) {
        this.faceMat = faceMat;
    }

    public Bbox getBbox() {
        return bbox;
    }

    public void setBbox(Bbox bbox) {
        this.bbox = bbox;
    }
}
