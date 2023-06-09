package com.example.gamedemo.screens;

import java.util.ArrayList;

import com.example.gamedemo.model.Vector;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public abstract class BaseScreen {

    protected Canvas canvas;
    protected GraphicsContext graphicsContext;
    private ArrayList<Rectangle> walls;

    public BaseScreen(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    /**
     * @return ArrayList<Rectangle> return the walls
     */
    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    /**
     * @param walls the walls to set
     */
    public void setWalls(ArrayList<Rectangle> walls) {
        this.walls = walls;
    }

}
