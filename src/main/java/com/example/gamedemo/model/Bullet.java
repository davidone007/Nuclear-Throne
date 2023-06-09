package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bullet {

    // Elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    // referencias espaciales
    private Vector position;
    private Vector direction;
    private int size;
    private int speed;
    private int weapon;
    private Image typeBullet;

    public Bullet(Canvas canvas, Vector position, Vector direction, int weapon) {
        this.weapon = weapon;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.position = position;
        this.direction = direction;
        this.size = 20;
        this.speed = 40;
        typeBullet = new Image(getClass().getResourceAsStream("/animations/bullets/" + weapon + ".png"));
    }


    public void paint() {
        graphicsContext.drawImage(typeBullet, position.getX(), position.getY(), size, size);
        position.setX(position.getX() + direction.getX());
        position.setY(position.getY() + direction.getY());

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
}
