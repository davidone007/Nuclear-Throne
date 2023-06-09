package com.example.gamedemo.screens;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Scenario_1 extends BaseScreen {
    public Scenario_1(Canvas canvas) {
        super(canvas);
        walls = new ArrayList<>();
        addWalls();
    }

    private ArrayList<Rectangle> walls;

    public void addWalls() {

        //Coordenada 0,0 es la esquina superior izquierda, y la altura va desde arriba hacia abajo, es decir el plano
        //tiene un efecto espejo horizontal el punto más alto seria la esquina inferior izquierda
        //los rectangulos se crean desde su esquina izquierda, width=Ancho height=Alto

        walls.add(new Rectangle(0, 0, canvas.getWidth(), 70)); // Pared superior
        walls.add(new Rectangle(0, canvas.getHeight() - 70, canvas.getWidth(), 70)); // Pared inferior
        walls.add(new Rectangle(canvas.getWidth() - 70, 0, 70, canvas.getHeight())); // Pared derecha
        walls.add(new Rectangle(0, 0, 60, canvas.getHeight()-650)); // Pared izquierda Superior
        walls.add(new Rectangle(0, canvas.getHeight()-300, 70, 300)); // Pared izquierda inferior
        walls.add(new Rectangle(canvas.getWidth()-910,canvas.getHeight()-800,70,160)); //Primera Pared Vertical Superior 
        walls.add(new Rectangle(canvas.getWidth()-440,canvas.getHeight()-800,70,160)); //Segunda Pared Vertical Superior 
        walls.add(new Rectangle(canvas.getWidth()-440,canvas.getHeight()-330,70,360)); //Pared Vertical inferior 
        walls.add(new Rectangle(canvas.getWidth()-1135,canvas.getHeight()-390,310,75)); //Pared horizontal mitad 

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