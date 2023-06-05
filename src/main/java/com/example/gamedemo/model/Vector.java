package com.example.gamedemo.model;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void normalize(){
        double magnitude = Math.sqrt(x * x + y * y);
        if (magnitude != 0) {
            x /= magnitude;
            y /= magnitude;
        }
    }

    public void setSpeed(int speed){
        x*=speed;
        y*=speed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
