package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Avatar implements Runnable {

    // Elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private ArrayList<Image> idleImages;
    private ArrayList<Image> runImages;
    private ArrayList<Image> attackImages;
    private ArrayList<Image> deadImages;
    private ArrayList<Image> hurtImages;
    private Map<String, Image> imageCache;
    private int weapon;
    private boolean isAlive;

    // referencias espaciales
    private double posX;
    private double posY;

    private Vector position;
    private Vector mapInicialPosicion;
    private Vector direction;

    // estado actual del personaje
    private int state;
    private int frame;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean isAttacking;
    private int lives;
    private Rectangle hitbox;

    public Avatar(Canvas canvas) {
        this.weapon = 0;
        this.state = 0;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.isAlive = true;
        this.hitbox = new Rectangle(1300, 500, 70, 120);
        this.position = new Vector(1300, 500);
        this.mapInicialPosicion = new Vector(1300, 500);
        this.posX = 100;
        this.posY = 100;
        this.lives = 5;

        idleImages = new ArrayList<>();
        runImages = new ArrayList<>();
        attackImages = new ArrayList<>();
        hurtImages = new ArrayList<>();
        deadImages = new ArrayList<>();
        imageCache = new HashMap<>();
        chargeImages();
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.out.println("");
            }
        }
    }

    public void clearImages() {
        idleImages.clear();
        runImages.clear();
        attackImages.clear();
        hurtImages.clear();
        deadImages.clear();
    }

    public void chargeImages() {
        clearImages();
        for (int i = 0; i <= 7; i++) {
            String imagePath = "/animations/hero/weapon_" + weapon + "/iddle/" + i + ".png";
            idleImages.add(getImageFromCache(imagePath));
        }

        for (int i = 0; i <= 11; i++) {
            String imagePath = "/animations/hero/weapon_" + weapon + "/run/" + i + ".png";
            runImages.add(getImageFromCache(imagePath));
        }

        for (int i = 0; i <= 11; i++) {
            String imagePath = "/animations/hero/weapon_" + weapon + "/attack/" + i + ".png";
            attackImages.add(getImageFromCache(imagePath));
        }

        for (int i = 0; i <= 1; i++) {
            String imagePath = "/animations/hero/weapon_" + weapon + "/hurt/" + i + ".png";
            hurtImages.add(getImageFromCache(imagePath));
        }
        

        for (int i = 0; i <= 5; i++) {
            String imagePath = "/animations/hero/weapon_" + weapon + "/dead/" + i + ".png";
            deadImages.add(getImageFromCache(imagePath));
        }
    }

    private Image getImageFromCache(String imagePath) {
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        } else {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageCache.put(imagePath, image);
            return image;
        }
    }

    public void paint() {
        chargeImages();
        onMove();
        if (state == 0) {
            graphicsContext.drawImage(idleImages.get(frame % 3), position.getX(), position.getY());
            frame++;
        } else if (state == 1) {
            // Left idle
            graphicsContext.drawImage(idleImages.get((frame % 3) + 4), position.getX(), position.getY());
            frame++;
        } else if (state == 2) {
            graphicsContext.drawImage(runImages.get(frame % 5), position.getX(), position.getY());
            frame++;
        } else if (state == 3) {
            // left run
            graphicsContext.drawImage(runImages.get((frame % 5) + 6), position.getX(), position.getY());
            frame++;
        } else if (state == 4) {
            graphicsContext.drawImage(attackImages.get(frame % 5), position.getX(), position.getY());
            frame++;
        } else if (state == 5) {
            // left attack
            graphicsContext.drawImage(attackImages.get((frame % 5) + 6), position.getX(), position.getY());
            frame++;
        } else if (state == 6) {
            graphicsContext.drawImage(hurtImages.get(0), position.getX(), position.getY());

        } else if (state == 7) {
            // left hurt
            graphicsContext.drawImage(hurtImages.get((frame % 1)), position.getX(), position.getY());
            frame++;
        } else if (state == 8) {
            graphicsContext.drawImage(deadImages.get(frame % 4), position.getX(), position.getY());
            frame++;
        }

    }

    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                if (state != 2 && state != 3) {
                    if (state == 0) {
                        state = 2;
                    } else {
                        state = 3;
                    }
                } else {
                    state = state;
                }
                upPressed = true;
                break;
            case S:
                if (state != 2 && state != 3) {
                    if (state == 0) {
                        state = 2;
                    } else {
                        state = 3;
                    }
                } else {
                    state = state;
                }
                downPressed = true;
                break;
            case D:
                state = 2;
                rightPressed = true;
                break;
            case A:
                state = 3;
                leftPressed = true;
                break;
        }
    }

    public void onKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                if (leftPressed || rightPressed || downPressed) {
                    state = state;
                    upPressed = false;
                    break;
                }
                if (state == 2) {
                    state = 0;
                } else if (state == 3) {
                    state = 1;
                }
                upPressed = false;
                break;
            case S:
                if (leftPressed || rightPressed || upPressed) {
                    state = state;
                    downPressed = false;
                    break;
                }
                if (state == 2) {
                    state = 0;
                } else if (state == 3) {
                    state = 1;
                }
                downPressed = false;
                break;
            case D:
                if (leftPressed || upPressed || downPressed) {
                    if (leftPressed) {
                        state = 3;
                    } else {
                        state = state;
                    }

                    rightPressed = false;
                    break;
                }
                state = 0;
                rightPressed = false;
                break;
            case A:
                if (rightPressed || upPressed || downPressed) {
                    if (rightPressed) {
                        state = 2;
                    } else {
                        state = state;
                    }
                    leftPressed = false;
                    break;
                }
                state = 1;
                leftPressed = false;
                break;
        }
    }

    public void onMouseReleased(MouseEvent event) {
        if (leftPressed || rightPressed) {
            state = state;
        }
        if (state == 4 || state == 5) {
            if (state == 4) {
                state = 0;
            } else {
                state = 1;
            }
        }
        isAttacking = false;
    }

    public void onMousePressed(MouseEvent event) {
        if (state == 0 || state == 1) {
            if (state == 0) {
                state = 4;
            } else {
                state = 5;
            }
        } else if (leftPressed || rightPressed) {
            state = state;
        }
        isAttacking = true;
    }

    public void onMove() {
        if (upPressed) {
            hitbox.setY(position.getY() - 7);
            position.setY(position.getY() - 10);
        }
        if (downPressed) {
            hitbox.setY(position.getY() + 7);
            position.setY(position.getY() + 10);
        }
        if (leftPressed) {
            hitbox.setX(position.getX() - 10);
            position.setX(position.getX() - 10);
        }
        if (rightPressed) {
            hitbox.setX(position.getX() + 7);
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

    /**
     * @return int return the lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * @param lives the lives to set
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * @return Map<String, Image> return the imageCache
     */
    public Map<String, Image> getImageCache() {
        return imageCache;
    }

    /**
     * @param imageCache the imageCache to set
     */
    public void setImageCache(Map<String, Image> imageCache) {
        this.imageCache = imageCache;
    }

    /**
     * @return boolean return the isAlive
     */
    public boolean isIsAlive() {
        return isAlive;
    }

    /**
     * @param isAlive the isAlive to set
     */
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
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


    /**
     * @return Vector return the mapInicialPosicion
     */
    public Vector getMapInicialPosicion() {
        return mapInicialPosicion;
    }

    /**
     * @param mapInicialPosicion the mapInicialPosicion to set
     */
    public void setMapInicialPosicion(Vector mapInicialPosicion) {
        this.mapInicialPosicion = mapInicialPosicion;
    }

}
