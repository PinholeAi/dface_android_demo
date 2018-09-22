package com.dface.dto;
/**
 * @brief 检测回归边框封装类
 */
public class Bbox {
    /**
     * @brief 判断为人脸的分值
     */
    public float score;
    /**
     * @brief 边框左上角x坐标
     */
    public int x1;
    /**
     * @brief 边框左上角y坐标
     */
    public int y1;
    /**
     * @brief 边框右下角x坐标
     */
    public int x2;
    /**
     * @brief 边框右下角y坐标
     */
    public int y2;
    /**
     * @brief 边框面积
     */
    public float area;
    /**
     * @brief 人脸五官的定位
     *
     * 前5个(ppoint[0]~ppoint[4])依次为 左眼，右眼，鼻子，左嘴角，右嘴角的x坐标值，后5个(ppoint[5]~ppoint[9])依次为 左眼，右眼，鼻子，左嘴角，右嘴角的y坐标值
     */
    public float[]ppoint;
    /**
     * @brief 人脸边框回归信息
     */
    public float[]regreCoord;

    public Bbox(){

    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float[] getPpoint() {
        return ppoint;
    }

    public void setPpoint(float[] ppoint) {
        this.ppoint = ppoint;
    }

    public float[] getRegreCoord() {
        return regreCoord;
    }

    public void setRegreCoord(float[] regreCoord) {
        this.regreCoord = regreCoord;
    }

    public Rect toRect(){
        Rect rect = new Rect();
        rect.setX(this.x1);
        rect.setY(this.y1);
        rect.setWidth(this.x2 - this.x1);
        rect.setHeight(this.y2 - this.y1);
        return rect;
    }


}
