package com.example.gamedemo.model;

import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class Avatar {

    // Elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private ArrayList<Image> idleImages;
    private ArrayList<Image> runImages;
    private ArrayList<Image> attackImages;
    private ArrayList<Image> deadImages;
    private ArrayList<Image> hurtImages;
    private int weapon;

    // referencias espaciales
    private double posX;
    private double posY;

    private Vector position;
    private Vector direction;

    // estado actual del personaje
    private int state;
    private int frame;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean isAttacking;

    public Avatar(Canvas canvas) {
        this.weapon = 1;
        this.state = 0;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();

        this.position = new Vector(100, 100);

        this.posX = 100;
        this.posY = 100;

        idleImages = new ArrayList<>();
        runImages = new ArrayList<>();
        attackImages = new ArrayList<>();
        hurtImages = new ArrayList<>();
        deadImages = new ArrayList<>();

        for (int i = 0; i <= 3; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream("/animations/hero/weapon_" + weapon + "/iddle/" + i + ".png"));
            idleImages.add(image);
        }

        for (int i = 0; i <= 5; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream("/animations/hero/weapon_" + weapon + "/run/" + i + ".png"));
            runImages.add(image);
        }

        for (int i = 0; i <= 5; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream("/animations/hero/weapon_" + weapon + "/attack/" + i + ".png"));
            attackImages.add(image);
        }

        for (int i = 1; i <= 1; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream("/animations/hero/weapon_" + weapon + "/hurt/" + i + ".png"));
            hurtImages.add(image);
        }

        for (int i = 0; i <= 5; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream("/animations/hero/weapon_" + weapon + "/dead/" + i + ".png"));
            deadImages.add(image);
        }
    }

    public void paint() {
        onMove();
        if (state == 0) {
            graphicsContext.drawImage(idleImages.get(frame % 3), position.getX(), position.getY());
            frame++;
        } else if (state == 1) {
            graphicsContext.drawImage(runImages.get(frame % 5), position.getX(), position.getY());
            frame++;
        } else if (state == 2) {
            graphicsContext.drawImage(attackImages.get(frame % 5), position.getX(), position.getY());
            frame++;
        } else if (state == 3) {
            graphicsContext.drawImage(hurtImages.get(frame % 1), position.getX(), position.getY());
            frame++;
        } else if (state == 4) {
            graphicsContext.drawImage(deadImages.get(frame % 4), position.getX(), position.getY());
            frame++;
        }
    }

    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                state = 1;
                upPressed = true;
                break;
            case S:
                state = 1;
                downPressed = true;
                break;
            case D:
                state = 1;
                rightPressed = true;
                break;
            case A:
                state = 1;
                leftPressed = true;
                break;
        }
    }

    public void onKeyReleased(KeyEvent event) {
        state = 0;
        switch (event.getCode()) {
            case W:
                upPressed = false;
                break;
            case S:
                downPressed = false;
                break;
            case D:
                rightPressed = false;
                break;
            case A:
                leftPressed = false;
                break;
        }
    }

    public void onMouseReleased(MouseEvent event) {
        state = 0;
    }

    public void onMousePressed(MouseEvent event) {
        state = 2;
        isAttacking = true;
    }

    public void onMove() {
        if (upPressed) {
            position.setY(position.getY() - 10);
        }
        if (downPressed) {
            position.setY(position.getY() + 10);
        }
        if (leftPressed) {
            position.setX(position.getX() - 10);
        }
        if (rightPressed) {
            position.setX(position.getX() + 10);
        }
    }

    public Vector getPosition() {
        return position;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
     * @return ArrayList<Image> return the idleImages
     */
    public ArrayList<Image> getIdleImages() {
        return idleImages;
    }

    /**
     * @param idleImages the idleImages to set
     */
    public void setIdleImages(ArrayList<Image> idleImages) {
        this.idleImages = idleImages;
    }

    /**
     * @return ArrayList<Image> return the runImages
     */
    public ArrayList<Image> getRunImages() {
        return runImages;
    }

    /**
     * @param runImages the runImages to set
     */
    public void setRunImages(ArrayList<Image> runImages) {
        this.runImages = runImages;
    }

    /**
     * @return ArrayList<Image> return the attackImages
     */
    public ArrayList<Image> getAttackImages() {
        return attackImages;
    }

    /**
     * @param attackImages the attackImages to set
     */
    public void setAttackImages(ArrayList<Image> attackImages) {
        this.attackImages = attackImages;
    }

    /**
     * @return ArrayList<Image> return the deadImages
     */
    public ArrayList<Image> getDeadImages() {
        return deadImages;
    }

    /**
     * @param deadImages the deadImages to set
     */
    public void setDeadImages(ArrayList<Image> deadImages) {
        this.deadImages = deadImages;
    }

    /**
     * @return ArrayList<Image> return the hurtImages
     */
    public ArrayList<Image> getHurtImages() {
        return hurtImages;
    }

    /**
     * @param hurtImages the hurtImages to set
     */
    public void setHurtImages(ArrayList<Image> hurtImages) {
        this.hurtImages = hurtImages;
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
     * @return double return the posX
     */
    public double getPosX() {
        return posX;
    }

    /**
     * @param posX the posX to set
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * @return double return the posY
     */
    public double getPosY() {
        return posY;
    }

    /**
     * @param posY the posY to set
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vector position) {
        this.position = position;
    }

    /**
     * @return Vector return the direction
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    /**
     * @return int return the frame
     */
    public int getFrame() {
        return frame;
    }

    /**
     * @param frame the frame to set
     */
    public void setFrame(int frame) {
        this.frame = frame;
    }

    /**
     * @return boolean return the upPressed
     */
    public boolean isUpPressed() {
        return upPressed;
    }

    /**
     * @param upPressed the upPressed to set
     */
    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    /**
     * @return boolean return the downPressed
     */
    public boolean isDownPressed() {
        return downPressed;
    }

    /**
     * @param downPressed the downPressed to set
     */
    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    /**
     * @return boolean return the leftPressed
     */
    public boolean isLeftPressed() {
        return leftPressed;
    }

    /**
     * @param leftPressed the leftPressed to set
     */
    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    /**
     * @return boolean return the rightPressed
     */
    public boolean isRightPressed() {
        return rightPressed;
    }

    /**
     * @param rightPressed the rightPressed to set
     */
    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    /**
     * @return boolean return the isAttacking
     */
    public boolean isIsAttacking() {
        return isAttacking;
    }

    /**
     * @param isAttacking the isAttacking to set
     */
    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

}