package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BoxWeapon extends Drawing implements Runnable {

    // Elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    // referencias espaciales
    private Vector position;
    private int size;
    private int weapon;
    private Image typeBullet;

    public BoxWeapon(Canvas canvas, Vector position, int weapon, int size) {
        this.weapon = weapon;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.position = position;
        this.size = size;
        typeBullet = new Image(getClass().getResourceAsStream("/animations/box/" + weapon + ".png"));
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(typeBullet, position.getX(), position.getY(), size, size);
        position.setX(position.getX());
        position.setY(position.getY());

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public double getPositionX() {
        return position.getX();
    }

    public void setPositionX(double x) {
        this.position.setX(x);
    }

    public double getPositionY() {
        return position.getY();
    }

    public void setPositionY(double y) {
        this.position.setY(y);
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
     * @return int return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return int return the weapon
     */
    public int getWeapon() {
        return weapon;
    }

    /**
     * @param weapon the weapon to set
     */
    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    /**
     * @return Image return the typeBullet
     */
    public Image getTypeBullet() {
        return typeBullet;
    }

    /**
     * @param typeBullet the typeBullet to set
     */
    public void setTypeBullet(Image typeBullet) {
        this.typeBullet = typeBullet;
    }

}
