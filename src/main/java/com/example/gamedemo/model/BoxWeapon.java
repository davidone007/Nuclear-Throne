package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class BoxWeapon implements Runnable{

    // Elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    // referencias espaciales
    private Vector position;
    private int size;
    private int weapon;
    private Image typeBullet;
    private Rectangle hitbox;

    public BoxWeapon(Canvas canvas, Vector position, int weapon, int size) {
        this.weapon = weapon;
        this.canvas = canvas;
        this.hitbox = new Rectangle(position.getX(), position.getY(),size,size);
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.position = position;
        this.size = size;
        typeBullet = new Image(getClass().getResourceAsStream("/animations/box/" + weapon + ".png"));
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.out.println("");
            }
        }
    }

    public void paint() {
        graphicsContext.drawImage(typeBullet, position.getX(), position.getY(), size, size);
        hitbox.setX(position.getX());
        hitbox.setY(position.getY());
        position.setX(position.getX());
        position.setY(position.getY());
        

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


    /**
     * @return Rectangle return the hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * @param hitbox the hitbox to set
     */
    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

}
