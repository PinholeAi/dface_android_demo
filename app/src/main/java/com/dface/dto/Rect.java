package com.dface.dto;

/**
 * @brief DFace矩形封装类
 */
public class Rect {
    /**
     * @brief 矩形x轴坐标
     */
    public int x;
    /**
     * @brief 矩形y轴坐标
     */
    public int y;
    /**
     * @brief 矩形宽
     */
    public int width;
    /**
     * @brief 矩形高
     */
    public int height;

    public Rect(){

    }

    public Rect(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
}
