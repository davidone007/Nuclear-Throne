package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Box extends Thread{
    private Canvas canvas;

    private GraphicsContext graphicsContext;

    private int lifes;

    private Vector position;

    private double y;

    private boolean isAlive;

    public Box(Canvas canvas, Vector position ){
        this.lifes = 3;
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
        graphicsContext.fillRect(position.getX(), position.getY(), 70, 70);
    }

    

   

    /**
     * @return Canvas return the canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * @param canvas the canvas to set
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * @param graphicsContext the graphicsContext to set
     */
    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    /**
     * @return int return the lifes
     */
    public int getLifes() {
        return lifes;
    }

    /**
     * @param lifes the lifes to set
     */
    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vector position) {
        this.position = position;
    }

    /**
     * @return boolean return the isAlive
     */
    public boolean isIsAlive() {
        return isAlive;
    }

    /**
     * @param isAlive the isAlive to set
     */
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

}
