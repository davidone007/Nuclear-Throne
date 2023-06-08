package com.example.gamedemo.control;

import com.example.gamedemo.model.*;
import com.example.gamedemo.screens.Scenario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable, Runnable {

    @FXML
    private Canvas canvas;

    private boolean isRunning;

    private final int FPS = 60;

    public static int SCREEN = 0;

    private ArrayList<Scenario> scenarios;

    private Thread gameThread;

    private Avatar avatar;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private int currentBullets;
    private ArrayList<BoxWeapon> boxWeapons;
    private ArrayList<Image> lifes;
    private BoxWeapon boxWeapon1;
    private BoxWeapon boxWeapon2;
    private Random random;
    private Font font;

    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isRunning = true;
        scenarios = new ArrayList<>();
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        canvas.setWidth(screenWidth);
        canvas.setHeight(screenHeight);
        gameThread = new Thread(this);
        graphicsContext = canvas.getGraphicsContext2D();
        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragReleased(this::onMouseReleased);
        canvas.setFocusTraversable(true);
        scenarios.add(new Scenario(this.canvas, 1));
        random = new Random();

        avatar = new Avatar(this.canvas);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        currentBullets = 0;
        boxWeapons = new ArrayList<>();

        font = Font.loadFont(getClass().getResourceAsStream("/fonts/Super Mario Bros. 2.ttf"), 20);

        // Nivel 1
        scenarios.get(0).setColor(Color.BLACK);
        new Thread(avatar).start();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Enemy enemy1 = new Enemy(canvas, new Vector(screenWidth - 100, screenHeight - 100), 1);
        Enemy enemy2 = new Enemy(canvas, new Vector(screenWidth - 100, screenHeight - 100), 1);
        enemies.add(enemy1);
        enemies.add(enemy2);
        new Thread(enemy1).start();
        new Thread(enemy2).start();
        boxWeapon1 = new BoxWeapon(canvas, getRandomPosition(), 0, 45);
        boxWeapon2 = new BoxWeapon(canvas, getRandomPosition(), 1, 45);
        new Thread(boxWeapon1).start();
        new Thread(boxWeapon2).start();
        boxWeapons.add(boxWeapon1);
        boxWeapons.add(boxWeapon2);

    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            // Dibujar en el lienzo
            update();
            repaint();
            graphicsContext.drawImage(new Image(
                    getClass().getResourceAsStream("/animations/weapons/" + 0 + ".png")), 100, 100);
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }

    public void update() {

        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {

                Enemy actualEnemy = enemies.get(i);
                Bullet actualBullet = bullets.get(j);

                double distance = Math.sqrt(
                        Math.pow(actualEnemy.getPosition().getX() - actualBullet.getPositionX(), 2) +
                                Math.pow(actualEnemy.getPosition().getY() - actualBullet.getPositionY(), 2));

                if (distance <= 60) {
                    enemies.remove(i);
                    bullets.remove(j);
                    return;
                }
            }
        }

        avatar.onMove();

    }

    public void repaint() {
        graphicsContext.setFill(Color.RED);

        String weaponInfo = "Bullets:" + currentBullets;
        double infoX = 10;
        double infoY = canvas.getHeight() - 30;

        graphicsContext.setFont(font);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(weaponInfo, infoX, infoY);

        Image weaponImage = new Image(
                getClass().getResourceAsStream("/animations/weapons/" + avatar.getWeapon() + ".png"));

        Text text = new Text(weaponInfo);
        text.setFont(graphicsContext.getFont());
        double textWidth = text.getLayoutBounds().getWidth();

        double weaponImageX = infoX + textWidth + 10;
        double weaponImageY = infoY - weaponImage.getHeight();

        graphicsContext.drawImage(weaponImage, weaponImageX, weaponImageY);

        int frame = 0;
        int heartSize = 20;
        int heartSpacing = 10;
        double heartsX = 10;
        double heartsY = 10;

        for (int i = 0; i < avatar.getLifes(); i++) {
            double heartPosX = heartsX + (i * (heartSize + heartSpacing));
            double heartPosY = heartsY;
            for (int j = 0; j < lifes.size(); j++) {
                graphicsContext.drawImage(lifes.get(frame % 7), heartPosX, heartPosY, heartSize, heartSize);
                frame++;
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(graphicsContext);

            if (bullets.get(i).getPositionX() > canvas.getWidth()) {
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < boxWeapons.size(); i++) {

            BoxWeapon actualBoxWeapon = boxWeapons.get(i);

            double distanceColision = Math.sqrt(
                    Math.pow(avatar.getPosition().getX() - actualBoxWeapon.getPositionX(), 2) +
                            Math.pow(avatar.getPosition().getY() - actualBoxWeapon.getPositionY(), 2));

            if (distanceColision <= 45) {

                if (actualBoxWeapon.getWeapon() == 0) {
                    boxWeapons.remove(i);
                    i--;
                    avatar.setWeapon(1);
                    currentBullets += 30;
                    avatar.draw(graphicsContext);
                } else if (actualBoxWeapon.getWeapon() == 1) {
                    boxWeapons.remove(i);
                    i--;
                    avatar.setWeapon(2);
                    currentBullets += 30;
                    avatar.draw(graphicsContext);
                }

                break;

            }

        }

        for (int i = 0; i < enemies.size(); i++) {

            Enemy actualEnemy = enemies.get(i);

            actualEnemy.draw(graphicsContext);

            double distanceColision = Math.sqrt(
                    Math.pow(avatar.getPosition().getX() - actualEnemy.getPosition().getX(), 2) +
                            Math.pow(avatar.getPosition().getY() - actualEnemy.getPosition().getY(), 2));

            if (distanceColision <= 60) {
                avatar.getPosition().setX(avatar.getPosition().getX() + 70);
                int state = avatar.getState();
                if (state == 2 || state == 0) {
                    avatar.setState(0);
                    avatar.draw(graphicsContext);
                    avatar.setState(6);
                    avatar.draw(graphicsContext);
                } else {
                    avatar.setState(1);
                    avatar.draw(graphicsContext);
                    avatar.setState(7);
                    avatar.draw(graphicsContext);
                }
                break;
            }

        }

        avatar.draw(graphicsContext);
    }

    public Vector getRandomPosition() {
        double x = random.nextInt((int) canvas.getWidth() - 100) + 50; // Genera una coordenada X aleatoria

        double y = random.nextInt((int) canvas.getHeight() - 100) + 50; // Genera una coordenada Y aleatoria

        return new Vector(x, y);
    }

    public void addImagesLifes() {
        for (int i = 0; i <= 7; i++) {
            lifes.add(new Image(getClass().getResourceAsStream("/animations/lifes/" + i + ".png")));
        }
    }

    public void onKeyPressed(KeyEvent event) {
        avatar.onKeyPressed(event);

        // Verificar si se presionó la tecla de recarga (por ejemplo, la tecla "R")
        if (event.getCode() == KeyCode.R) {
            reloadWeapon();
        }
    }

    public void onKeyReleased(KeyEvent event) {
        avatar.onKeyReleased(event);

    }

    public void onMousePressed(MouseEvent event) {
        if (avatar.getWeapon() != 0) {
            if (currentBullets > 0) {
                int weapon = avatar.getWeapon();
                double diffX = event.getX() - avatar.getPosition().getX();
                double diffY = event.getY() - avatar.getPosition().getY();

                Vector diff = new Vector(diffX, diffY);
                diff.normalize();
                diff.setSpeed(40);

                avatar.onMousePressed(event);

                int pos = avatar.getState();

                if (pos == 4) {
                    bullets.add(
                            new Bullet(canvas,
                                    new Vector(avatar.getPosition().getX() + 120, avatar.getPosition().getY() + 50),
                                    diff, weapon));
                } else {
                    bullets.add(
                            new Bullet(canvas,
                                    new Vector(avatar.getPosition().getX() - 30, avatar.getPosition().getY() + 50),
                                    diff, weapon));
                }

                currentBullets--;
            }
        }
    }

    public void onMouseReleased(MouseEvent event) {
        avatar.onMouseReleased(event);
    }

    private void reloadWeapon() {
        // Verificar si el arma actual del avatar es recargable
        int currentWeapon = avatar.getWeapon();
        if (currentWeapon != 0) {
            // Recargar el arma
            int bulletsToAdd = 30 - currentBullets;
            if (bulletsToAdd > 0) {
                currentBullets += bulletsToAdd;
                // Limitar la cantidad de balas al máximo (por ejemplo, 30)
                if (currentBullets > 30) {
                    currentBullets = 30;
                }
            }
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
     * @return boolean return the isRunning
     */
    public boolean isIsRunning() {
        return isRunning;
    }

    /**
     * @param isRunning the isRunning to set
     */
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}