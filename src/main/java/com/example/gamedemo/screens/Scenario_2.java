package com.example.gamedemo.screens;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Scenario_2 extends BaseScreen {
    public Scenario_2(Canvas canvas) {
        super(canvas);
        walls = new ArrayList<>();
        addWalls();
    }

    private ArrayList<Rectangle> walls;

    public void addWalls() {

        // Coordenada 0,0 es la esquina superior izquierda, y la altura va desde arriba
        // hacia abajo, es decir el plano
        // tiene un efecto espejo horizontal el punto más alto seria la esquina inferior
        // izquierda
        // los rectangulos se crean desde su esquina izquierda, width=Ancho height=Alto

        walls.add(new Rectangle(280, 0, canvas.getWidth(), 80)); // Pared superior
        walls.add(new Rectangle(0, canvas.getHeight() - 80, canvas.getWidth(), 80)); // Pared inferior
        walls.add(new Rectangle(canvas.getWidth() - 80, 0, 80, 210)); // Pared superior derecha
        walls.add(new Rectangle(canvas.getWidth() - 80, canvas.getHeight() - 280, 80, 280)); // Pared inferior derecha
        walls.add(new Rectangle(0, 0, 80, canvas.getHeight())); // Pared izquierda
        walls.add(new Rectangle(770, 0, 80, 210)); // Pared superior de la mitad
        walls.add(new Rectangle(310, canvas.getHeight() - 320, 320, 80)); // Primera Pared Horizontal
        walls.add(new Rectangle(550, 310, 80, 310)); // Primera Pared Vertical
        walls.add(new Rectangle(canvas.getWidth() - 520, canvas.getHeight() - 240, 80, 240)); // Pared Vertical

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