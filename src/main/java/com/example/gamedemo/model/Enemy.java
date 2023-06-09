package com.example.gamedemo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Enemy implements Runnable {

    // Elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private ArrayList<Image> idleImages;
    private ArrayList<Image> runImages;
    private ArrayList<Image> attackImages;
    private ArrayList<Image> deadImages;
    private ArrayList<Image> hurtImages;
    private int scenario;
    private Avatar avatar;

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
    private int lives;
    private boolean isAlive;
    private Rectangle hitbox;

    // Variables de disparo
    private double shootRange; // Rango de disparo deseado
    private long shootDelay; // Retraso de disparo deseado (en milisegundos)
    private long lastShootTime; // Tiempo del último disparo

    public Enemy(Canvas canvas, Vector position, int scenario, Avatar avatar) {
        this.scenario = scenario;
        this.state = 0;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.avatar = avatar;
        this.hitbox = new Rectangle(posX, posY, 100, 100);

        this.position = position;

        this.posX = position.getX();
        this.posY = position.getY();
        this.isAlive = true;

        this.lives = 3;

        // Inicializar las variables de disparo
        this.shootRange = 100; // Establecer el rango de disparo deseado
        this.shootDelay = 500; // Establecer el retraso de disparo deseado (en milisegundos)
        this.lastShootTime = 0;

        idleImages = new ArrayList<>();
        runImages = new ArrayList<>();
        attackImages = new ArrayList<>();
        hurtImages = new ArrayList<>();
        deadImages = new ArrayList<>();
        chargeImages();

    }

    public boolean readyToShoot() {
        Vector diff = new Vector(avatar.getPosition().getX() - position.getX(),
                avatar.getPosition().getY() - position.getY());
        double distance = Math.sqrt(diff.getX() * diff.getX() + diff.getY() * diff.getY());

        if (distance <= shootRange) {
            // El enemigo está dentro del rango de disparo
            // Comprobar si ha pasado el tiempo suficiente desde el último disparo
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShootTime >= shootDelay) {
                // Ha pasado suficiente tiempo, el enemigo está listo para disparar
                lastShootTime = currentTime;
                return true;
            }
        }

        return false;
    }

    public void sleep() {
        try {
            paint();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isAlive) {
            // Lógica de actualización del estado del enemigo
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.out.println("");
            }
        }
    }

    public void follow(ArrayList<Rectangle> walls) {
        // Calcular la dirección hacia la cual moverse
        double diffX = avatar.getPosition().getX() - position.getX();
        double diffY = avatar.getPosition().getY() - position.getY();

        int avatarState = avatar.getState();
        if (diffX > 0) {
            if (avatarState == 0 || avatarState == 2) {
                if (avatar.getState() == 0) {
                    state = 2;
                } else {
                    state = avatarState;
                }
            } else if (avatarState == 1 || avatarState == 3) {
                if (avatar.getState() == 1) {
                    state = 2;
                } else {
                    state = avatarState - 1;
                }

            }
        } else {
            if (avatarState == 0 || avatarState == 2) {
                if (avatar.getState() == 0) {
                    state = 3;
                } else {
                    state = avatarState + 1;
                }

            } else if (avatarState == 1 || avatarState == 3) {
                if (avatar.getState() == 1) {
                    state = 3;
                } else {
                    state = avatarState;
                }
            }
        }

        if (avatar.getHitbox().intersects(hitbox.getBoundsInParent())) {
            if (state == 2) {
                state = (4);
                paint();

            } else if (state == 3) {
                state = (5);
                paint();

            }
        }

        // Normalizar la dirección
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);
        double directionX = diffX / distance;
        double directionY = diffY / distance;

        boolean collidedWithWall = false;

        for (Rectangle wall : walls) {
            if (hitbox.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                collidedWithWall = true;

                // Calcular la nueva dirección
                double wallCenterX = wall.getX() + wall.getWidth() / 2;
                double wallCenterY = wall.getY() + wall.getHeight() / 2;

                double wallDiffX = wallCenterX - position.getX();
                double wallDiffY = wallCenterY - position.getY();

                // Calcular la distancia entre el enemigo y el centro del muro
                double distanceToWallCenter = Math.sqrt(wallDiffX * wallDiffX + wallDiffY * wallDiffY);

                // Calcular la nueva dirección evitando el muro
                double newDirectionX = directionX - wallDiffX / distanceToWallCenter;
                double newDirectionY = directionY - wallDiffY / distanceToWallCenter;

                // Normalizar la nueva dirección
                double newDistance = Math.sqrt(newDirectionX * newDirectionX + newDirectionY * newDirectionY);
                if (newDistance != 0) { // Evitar la división por cero
                    directionX = newDirectionX / newDistance;
                    directionY = newDirectionY / newDistance;
                }

                // Actualizar la posición del enemigo
                double speed = 5; // Ajusta la velocidad de movimiento del enemigo si es necesario
                position.setX(position.getX() + directionX * speed);
                position.setY(position.getY() + directionY * speed);
                hitbox.setX(position.getX());
                hitbox.setY(position.getY());
            }
        }

        if (!collidedWithWall) {
            // Actualizar la posición del enemigo si no hubo colisión con una pared
            double speed = 5; // Ajusta la velocidad de movimiento del enemigo si es necesario
            position.setX(position.getX() + directionX * speed);
            position.setY(position.getY() + directionY * speed);
            hitbox.setX(position.getX());
            hitbox.setY(position.getY());
        }
    }

    public void chargeImages() {
        for (int i = 0; i <= 7; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream(
                            "/animations/enemies/enemy_scenario_" + scenario + "/iddle/" + i + ".png"));
            idleImages.add(image);
        }

        for (int i = 0; i <= 11; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream(
                            "/animations/enemies/enemy_scenario_" + scenario + "/run/" + i + ".png"));
            runImages.add(image);
        }

        for (int i = 0; i <= 11; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream(
                            "/animations/enemies/enemy_scenario_" + scenario + "/attack/" + i + ".png"));
            attackImages.add(image);
        }

        for (int i = 0; i <= 1; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream(
                            "/animations/enemies/enemy_scenario_" + scenario + "/hurt/" + i + ".png"));
            hurtImages.add(image);
        }

        for (int i = 0; i <= 5; i++) {
            Image image = new Image(
                    getClass().getResourceAsStream(
                            "/animations/enemies/enemy_scenario_" + scenario + "/die/" + i + ".png"));
            deadImages.add(image);
        }
    }

    public void paint() {
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
     * @return int return the scenario
     */
    public int getScenario() {
        return scenario;
    }

    /**
     * @param scenario the scenario to set
     */
    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    /**
     * @return boolean return the isAlive
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * @param isAlive the isAlive to set
     */
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * @return Avatar return the avatar
     */
    public Avatar getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
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
