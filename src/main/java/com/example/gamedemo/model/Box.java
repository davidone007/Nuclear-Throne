package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Box extends Thread{
    private Canvas canvas;

    private GraphicsContext graphicsContext;

    private Vector position;

    private double y;

    private boolean isAlive;

    public Box(Canvas canvas, Vector position ){
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.position = position;
        this.isAlive = true;
    }

    public Vector getPosition() {
        return position;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void paint(){
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(position.getX(), position.getY(), 20, 20);
    }

    @Override
    public void run(){
        while (isAlive){
            position.setY(y);
            y++;
            try {
                Thread.sleep(25);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
