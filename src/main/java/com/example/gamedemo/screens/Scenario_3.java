package com.example.gamedemo.screens;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Scenario_3 extends BaseScreen {
    public Scenario_3(Canvas canvas) {
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

        walls.add(new Rectangle(0, 0, canvas.getWidth() - 280, 80)); // Pared superior
        walls.add(new Rectangle(240, canvas.getHeight() - 80, canvas.getWidth(), 80));// Pared inferior
        walls.add(new Rectangle(0, 0, 80, canvas.getHeight())); // Pared izquierda
        walls.add(new Rectangle(canvas.getWidth() - 70, 0, 70, canvas.getHeight())); // Pared derecha
        walls.add(new Rectangle(710, 0, 80, 240)); // Pared superior de la mitad
        walls.add(new Rectangle(320, canvas.getHeight() - 330, 80, 360)); // Pared Vertical inferior
        walls.add(new Rectangle(700, (canvas.getHeight() / 2)+10, 80, 160)); // Pared Vertical sola de la mitad
        walls.add(new Rectangle(canvas.getWidth() - 520, 250, 80, 240)); // Pared Vertical de la mitad
        walls.add(new Rectangle(canvas.getWidth() - 440, 410, 80, 80)); // Cuadro solo
        walls.add(new Rectangle(80, canvas.getHeight() - 10, 240, 10)); //Entrada
        walls.add(new Rectangle(canvas.getWidth() - 280, 0, 240,10)); //Salida
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