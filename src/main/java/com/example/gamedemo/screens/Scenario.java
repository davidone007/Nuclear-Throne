package com.example.gamedemo.screens;

import java.util.ArrayList;

import com.example.gamedemo.model.BoxWeapon;
import com.example.gamedemo.model.Enemy;
import com.example.gamedemo.model.Vector;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Scenario {

    protected Canvas canvas;
    protected GraphicsContext graphicsContext;
    private Color color;
    private ArrayList<Enemy> enemies;
    private ArrayList<BoxWeapon> boxWeapons;
    private int scenario;

    public Scenario(Canvas canvas, int scenario) {
        this.canvas = canvas;
        this.scenario= scenario;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    /**
     * @return Color return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return ArrayList<Enemy> return the enemies
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * @param enemies the enemies to set
     */
    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     * @return ArrayList<BoxWeapon> return the boxWeapons
     */
    public ArrayList<BoxWeapon> getBoxWeapons() {
        return boxWeapons;
    }

    /**
     * @param boxWeapons the boxWeapons to set
     */
    public void setBoxWeapons(ArrayList<BoxWeapon> boxWeapons) {
        this.boxWeapons = boxWeapons;
    }

}
