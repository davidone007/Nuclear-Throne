package com.example.gamedemo.screens;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Rectangle;

public class Scenario_1 extends BaseScreen {
    public Scenario_1(Canvas canvas) {
        super(canvas);
        addWalls();
    }

    private ArrayList<Rectangle> walls;

    public void addWalls() {

        walls = new ArrayList<>();

        walls.add(new Rectangle(0, 0, canvas.getWidth(), 10)); // Pared superior
        walls.add(new Rectangle(0, canvas.getHeight() - 10, canvas.getWidth(), 10)); // Pared inferior
        walls.add(new Rectangle(0, 0, 10, canvas.getHeight())); // Pared izquierda
        walls.add(new Rectangle(canvas.getWidth() - 10, 0, 10, canvas.getHeight())); // Pared derecha
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