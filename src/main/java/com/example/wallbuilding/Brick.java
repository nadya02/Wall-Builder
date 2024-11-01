package com.example.wallbuilding;

public class Brick {
    public int length;
    public int height;
    public int width;

    public boolean isBuilt = false;

    public Brick(int length, int height, int width) {
        this.length = length;
        this.height = height;
        this.width = width;
    }

    public double calculateCourseHeight(double bedJoint){
        return this.height + bedJoint;
    }

    public double calculateCourseLength(double headJoint){
        System.out.println("Brick length " + this.length + " HeadJoint " + headJoint);
        return this.length + headJoint;
    }

}
