package com.dface.dto;

/**
 * @brief DFace图片数据封装类
 */
public class DFaceMat {
    /**
     * @brief 图片数据byte数组,只支持RGBA或者RGB格式的数据，YUV格式需要自己转成以上两张图片格式
     */
    public byte[] data;
    /**
     * @brief 图片宽
     */
    public int width;
    /**
     * @brief 图片高
     */
    public int height;
    /**
     * @brief 图片通道数
     */
    public int channel;

    public DFaceMat(){

    }

    public DFaceMat(byte[] data, int width, int height, int channel){
        this.data = data;
        this.width = width;
        this.height = height;
        this.channel = channel;
    }


    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
