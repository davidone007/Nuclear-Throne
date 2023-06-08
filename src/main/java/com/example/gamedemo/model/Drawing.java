package com.example.gamedemo.model;

import javafx.scene.canvas.GraphicsContext;

public abstract class Drawing {

    protected Vector pos = new Vector(0,0);
    protected int speed;
    public abstract void draw(GraphicsContext gc);


}