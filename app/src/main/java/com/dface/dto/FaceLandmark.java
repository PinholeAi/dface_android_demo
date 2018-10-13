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
     * (yaw,pitch,roll,t_x,t_y,t_z) 详情查看《DFace用户手册.pdf》
     * yaw(偏航角): 人脸朝向视角，向右转脸为正角度，向左转脸为负角度(单位角度数)
     * pitch(俯仰角): 人脸朝向视角, 向下俯为正角度，向上仰为负角度(单位角度数)
     * roll(滚转角): 人脸朝向视角，向右滚头为正，向左滚头为负(单位角度数)
     * t_x: 摄像头为视角，向摄像头右方偏离为正，左方为负(单位米)
     * t_y： 摄像头为视角，向摄像头上方偏离为正，下方为负(单位米)
     * t_z： 摄像头为视角，偏离摄像头越远值越大，该值和摄像头焦距关系很大，实际生成环境需要设置好焦距参数
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
