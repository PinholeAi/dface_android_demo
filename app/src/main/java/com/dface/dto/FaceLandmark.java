package com.dface.dto;

import java.util.List;
import com.dface.dto.Rect;

/**
 * @brief 人脸关键点和3D角度封装类
 */
public class FaceLandmark {
    /**
     * @brief 检测到的人脸边框
     */
    public Rect bbox;
    /**
     * @brief 人脸2D关键点坐标(68个关键点，size=68x2)
     * [x1,y1,x2,y2,x3,y3...x68,y68]
     */
    public int[] Landmarks2D;
    /**
     * @brief 人脸3D关键点坐标(68个关键点，size=68x3)
     * [x1,y1,z1,x2,y2,z2,x3,y3,z3...x68,y68,z68]
     */
    public int[] Landmarks3D;
    /**
     * @brief 头部3d姿态角度信息
     * (yaw,pitch,roll,t_x,t_y,t_z)
     * yaw,pitch,roll: 分别表示头部的偏航角，俯仰角，滚转角  t_x, t_y, t_z: 分别表示头部相对于正脸世界坐标系原点的偏移量(坐标值)
     */
    public double[] Pose3D;

    public Rect getBbox() {
        return bbox;
    }

    public void setBbox(Rect bbox) {
        this.bbox = bbox;
    }

    public int[] getLandmarks2D() {
        return Landmarks2D;
    }

    public void setLandmarks2D(int[] landmarks2D) {
        Landmarks2D = landmarks2D;
    }

    public int[] getLandmarks3D() {
        return Landmarks3D;
    }

    public void setLandmarks3D(int[] landmarks3D) {
        Landmarks3D = landmarks3D;
    }

    public double[] getPose3D() {
        return Pose3D;
    }

    public void setPose3D(double[] pose3D) {
        Pose3D = pose3D;
    }
}
