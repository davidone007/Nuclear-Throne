package com.example.gamedemo.control;

import com.example.gamedemo.MainApplication;
import com.example.gamedemo.model.*;
import com.example.gamedemo.screens.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MainController implements Initializable {

    @FXML
    private Canvas canvas;

    private boolean isRunning;

    private int actualScreen;

    private int animationFrame;

    private Clip backgroundMusic;

    private GraphicsContext graphicsContext;
    private Avatar avatar;
    private ArrayList<BaseScreen> screens;
    private ArrayList<Image> screenImages;
    private ArrayList<Image> lifes;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<BoxWeapon> boxWeapons;
    private Random random;
    private int currentBullets;
    private Font font;
    private ArrayList<Image> weaponImage;
    private Text text;
    private String weaponInfo;
    private double mouseX;
    private double mouseY;
    private Image pointerImage;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        random = new Random();
        isRunning = true;
        screens = new ArrayList<>(3);
        screenImages = new ArrayList<>(3);
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        canvas.setWidth(screenWidth);
        canvas.setHeight(screenHeight);
        graphicsContext = canvas.getGraphicsContext2D();
        avatar = new Avatar(canvas);
        new Thread(avatar).start();
        canvas.setOnMouseMoved(event -> onMouseMoved(event));

        this.animationFrame = 0;

        screens.add(new Scenario_1(canvas));
        screens.add(new Scenario_2(canvas));
        screens.add(new Scenario_3(canvas));

        level(1);

    }

    public void level(int level) {
        isRunning = false;
        actualScreen = level;
        font = Font.loadFont(getClass().getResourceAsStream("/fonts/Super Mario Bros. 2.ttf"), 20);
        pointerImage = new Image(getClass().getResourceAsStream("/animations/pointer/pointer.png"));
        currentBullets = 0;
        boxWeapons = new ArrayList<>(3);
        bullets = new ArrayList<>(60);
        lifes = new ArrayList<>(8);
        enemies = new ArrayList<>(5);
        weaponImage = new ArrayList<>(4);
        addImagesScreen();
        addImagesLifes();
        addEnemies();
        addBoxWeapon();
        addImagesWeapon();
        canvas.setFocusTraversable(true);

        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                Platform.runLater(() -> {
                    paint();
                });
                // esta línea va acá ...
                pause(70);
            }
            // estaba acá ....
        }).start();

        new Thread(() -> {
            while (isRunning) {
                Platform.runLater(() -> {
                    initEvents();
                });
                // esta línea va acá ...
                pause(70);
            }
            // estaba acá ....
        }).start();

        // Poner musica de fondo
        playBackgroundMusic();

        // Configurar evento de cierre de la ventana
        MainApplication.getInstance().getPrimaryStage().setOnCloseRequest(event -> {
            // Detener todos los procesos y cerrar la aplicación
            isRunning = false;
            avatar.setIsAlive(false);
            killAllEnemies();
            backgroundMusic.stop();
            MainApplication mainApp = MainApplication.getInstance();
            mainApp.stopAndCloseProgram();
        });

    }

    public void playBackgroundMusic() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/scenario" + actualScreen + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void gameOver() {
        killAllEnemies();
        avatar.setIsAlive(false);
        isRunning = false;
        MainApplication mainApp = MainApplication.getInstance();
        mainApp.changeSceneGameOver();
        backgroundMusic.stop();

    }

    public void gameWin() {
        killAllEnemies();
        avatar.setIsAlive(false);
        isRunning = false;
        MainApplication mainApp = MainApplication.getInstance();
        mainApp.changeSceneGameWin();
        backgroundMusic.stop();

    }

    public Vector getRandomPosition() {
        double x;
        double y;
        boolean isInWall = true;

        do {
            if (actualScreen == 3) {
                x = random.nextInt((int) canvas.getWidth() / 2) + canvas.getWidth() / 2;
                y = random.nextInt((int) canvas.getHeight() - 200) + 50;
            } else {
                x = random.nextInt((int) canvas.getWidth()) / 2;
                y = random.nextInt((int) canvas.getHeight() - 200) + 50;
            }

            if (verifyPosWallCollision(x, y)) {
                isInWall = true;
            } else {
                isInWall = false;
            }

            // Verificar si la posición generada se intersecta con alguna hitbox de pared
            /*
             * for (Rectangle wall : screens.get(actualScreen - 1).getWalls()) {
             * if (wall.intersects(x, y, 1, 1)) {
             * isInWall = true;
             * break;
             * }
             * }
             */
        } while (isInWall);

        return new Vector(x, y);
    }

    public boolean allEnemiesKill() {

        for (Enemy enemy : enemies) {

            if (enemy.getIsAlive()) {
                return false;
            }

        }

        return true;

    }

    public void addImagesScreen() {
        for (int i = 1; i <= 3; i++) {
            screenImages.add(new Image(
                    getClass().getResourceAsStream("/scenarios/" + i + ".png")));
        }
    }

    public void addImagesWeapon() {

        for (int i = 0; i <= 2; i++) {
            weaponImage.add(new Image(
                    getClass().getResourceAsStream("/animations/weapons/" + i + ".png")));
        }

    }

    public void addImagesLifes() {
        for (int i = 0; i <= 7; i++) {
            lifes.add(new Image(getClass().getResourceAsStream("/animations/lifes/" + i + ".png")));
        }
    }

    public void addEnemies() {

        Enemy newEnemy = new Enemy(canvas, getRandomPosition(), actualScreen, avatar);
        enemies.add(newEnemy);
        new Thread(newEnemy).start();

        Enemy newEnemy2 = new Enemy(canvas, getRandomPosition(), actualScreen, avatar);
        enemies.add(newEnemy2);
        new Thread(newEnemy2).start();

        Enemy newEnemy3 = new Enemy(canvas, getRandomPosition(), actualScreen, avatar);
        enemies.add(newEnemy3);
        new Thread(newEnemy3).start();

    }

    public void addBoxWeapon() {

        BoxWeapon boxWeapon = null;
        BoxWeapon boxWeapon2 = null;
        if (actualScreen == 1) {
            boxWeapon = new BoxWeapon(canvas, new Vector(canvas.getWidth() / 2, 300), 0, 50);
            boxWeapon2 = new BoxWeapon(canvas, new Vector(canvas.getWidth() / 2, canvas.getHeight() - 200), 1, 50);

        } else if (actualScreen == 2) {
            boxWeapon = new BoxWeapon(canvas, new Vector(canvas.getWidth() / 2, 350), 0, 50);
            boxWeapon2 = new BoxWeapon(canvas, new Vector(canvas.getWidth() / 2, canvas.getHeight() - 200), 1, 50);

        } else {
            boxWeapon = new BoxWeapon(canvas, new Vector(canvas.getWidth() / 2 - 200, 100), 0, 50);
            boxWeapon2 = new BoxWeapon(canvas, new Vector(canvas.getWidth() / 2 + 240, canvas.getHeight() - 200), 1,
                    50);

        }
        boxWeapons.add(boxWeapon);
        new Thread(boxWeapon).start();
        boxWeapons.add(boxWeapon2);
        new Thread(boxWeapon2).start();
    }

    public void onMouseMoved(MouseEvent event) {
        mouseX = (event.getX());
        mouseY = (event.getY());
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

                if (pos == 4 || pos == 2) {
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

                // Disparo jugador
                playSoundShoot();

                currentBullets--;
            }
        }
    }

    public void playSoundShoot() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/shootPlayer.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            Clip disparoSound = AudioSystem.getClip();
            disparoSound.open(audioInputStream);
            disparoSound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
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
                playSoundReload();
            }
        }
    }

    public void playSoundReload() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/reloadWeapon.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            Clip disparoSound = AudioSystem.getClip();
            disparoSound.open(audioInputStream);
            disparoSound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void paint() {

        // Acomodar el contexto grafico (Screen)
        graphicsContext.drawImage(screenImages.get(actualScreen - 1), 0, 0, canvas.getWidth(), canvas.getHeight());

        verifyRunWallColision();

        // Insertar al avatar
        avatar.paint();

        // Seguir avatar
        for (Enemy enemy : enemies) {
            enemy.follow(screens.get(actualScreen - 1).getWalls());
        }

        // Insertar cajas de armas en el screen
        for (BoxWeapon bW : boxWeapons) {
            bW.paint();
        }

        // Insertar enemigos en el screen

        for (Enemy enemy : enemies) {
            enemy.paint();
        }

        // Bucle para la colision con el enemigo
        paintColisionEnemy();

        // Bucle para la colision con las cajas de armas
        paintColisionBoxWeapon();

        // Bucle para disparar
        paintShoot();

        // Bucle para las vidas
        paintLifes();

        // Dibujar informacion del arma
        paintWeaponInfo();

        // Dibuja el puntero
        paintPointer();

    }

    public void verifyRunWallColision() {
        if (!allEnemiesKill()) {
            for (int i = 0; i < screens.get(actualScreen - 1).getWalls().size(); i++) {
                Rectangle wall = screens.get(actualScreen - 1).getWalls().get(i);

                if (avatar.getHitbox().intersects(wall.getBoundsInLocal())) {
                    double diffX = avatar.getPosition().getX() - wall.getX();
                    double diffY = avatar.getPosition().getY() - wall.getY();

                    double posX = avatar.getPosition().getX();
                    double posY = avatar.getPosition().getY();

                    boolean isDown = false;
                    boolean isUp = false;
                    boolean isLeft = false;
                    boolean isRight = false;

                    if ((posX - wall.getX()) <= (wall.getWidth() + 10)
                            && (posX - wall.getX()) >= (wall.getWidth() - 10)) {
                        isLeft = true;
                    }

                    if ((posX - wall.getX()) <= 0
                            && (posX - wall.getX()) >= -150) {
                        isRight = true;
                    }

                    if ((posY - wall.getY()) <= -80
                            && (posY - wall.getY()) >= -140) {
                        isDown = true;
                    }

                    if ((posY - wall.getY()) <= (wall.getHeight() + 15)
                            && (posY - wall.getY()) >= (wall.getHeight() - 15)) {
                        isUp = true;
                    }

                    if (isDown || isUp) {
                        if (diffY < 0) {
                            if (avatar.isDownPressed()) {
                                avatar.setDownPressed(false);
                            }
                        } else {
                            if (avatar.isUpPressed()) {
                                avatar.setUpPressed(false);
                            }
                        }
                    } else {
                        if (diffX > 0) {
                            if (avatar.isLeftPressed()) {
                                avatar.setLeftPressed(false);
                            }
                        } else {
                            if (avatar.isRightPressed()) {
                                avatar.setRightPressed(false);
                            }
                        }

                    }

                }
            }
        } else {
            for (int i = 0; i < screens.get(actualScreen - 1).getWalls().size(); i++) {
                Rectangle wall = screens.get(actualScreen - 1).getWalls().get(i);

                if (wall.equals(screens.get(actualScreen - 1).getWalls()
                        .get(screens.get(actualScreen - 1).getWalls().size() - 1))) {
                    if (avatar.getHitbox().intersects(wall.getBoundsInLocal())) {
                        if (actualScreen == 3) {
                            backgroundMusic.stop();
                            gameWin();
                        } else if (actualScreen == 1) {
                            backgroundMusic.stop();
                            avatar.getHitbox().setX(canvas.getWidth() - 80);
                            avatar.getPosition().setX(canvas.getWidth() - 80);
                            avatar.getMapInicialPosicion().setX(canvas.getWidth() - 80);
                            avatar.getHitbox().setY(canvas.getHeight() / 2);
                            avatar.getPosition().setY(canvas.getHeight() / 2);
                            avatar.getMapInicialPosicion().setY(canvas.getHeight() / 2);

                            avatar.setWeapon(0);
                            level(2);
                        } else {
                            backgroundMusic.stop();
                            avatar.getHitbox().setX(130);
                            avatar.getPosition().setX(130);
                            avatar.getMapInicialPosicion().setX(130);
                            avatar.getHitbox().setY(canvas.getHeight() - 100);
                            avatar.getPosition().setY(canvas.getHeight() - 100);
                            avatar.getMapInicialPosicion().setY(canvas.getHeight() - 100);
                            avatar.setWeapon(0);
                            level(3);
                        }
                    }
                }

                if (avatar.getHitbox().intersects(wall.getBoundsInLocal())) {
                    double diffX = avatar.getPosition().getX() - wall.getX();
                    double diffY = avatar.getPosition().getY() - wall.getY();

                    double posX = avatar.getPosition().getX();
                    double posY = avatar.getPosition().getY();

                    boolean isDown = false;
                    boolean isUp = false;
                    boolean isLeft = false;
                    boolean isRight = false;

                    if ((posX - wall.getX()) <= (wall.getWidth() + 10)
                            && (posX - wall.getX()) >= (wall.getWidth() - 10)) {
                        isLeft = true;
                    }

                    if ((posX - wall.getX()) <= 0
                            && (posX - wall.getX()) >= -150) {
                        isRight = true;
                    }

                    if ((posY - wall.getY()) <= -80
                            && (posY - wall.getY()) >= -140) {
                        isDown = true;
                    }

                    if ((posY - wall.getY()) <= (wall.getHeight() + 15)
                            && (posY - wall.getY()) >= (wall.getHeight() - 15)) {
                        isUp = true;
                    }

                    if (isDown || isUp) {
                        if (diffY < 0) {
                            if (avatar.isDownPressed()) {
                                avatar.setDownPressed(false);
                            }
                        } else {
                            if (avatar.isUpPressed()) {
                                avatar.setUpPressed(false);
                            }
                        }
                    } else {
                        if (diffX > 0) {
                            if (avatar.isLeftPressed()) {
                                avatar.setLeftPressed(false);
                            }
                        } else {
                            if (avatar.isRightPressed()) {
                                avatar.setRightPressed(false);
                            }
                        }

                    }

                }
            }
        }

    }

    public void paintColisionEnemy() {

        // Bucle para la colision con el enemigo
        for (int i = 0; i < enemies.size(); i++) {

            Enemy actualEnemy = enemies.get(i);

            if (avatar.getHitbox().intersects(actualEnemy.getHitbox().getBoundsInParent())) {

                avatar.setLives(avatar.getLives() - 1);

                int enemyState = actualEnemy.getState();
                if (enemyState == 2) {
                    actualEnemy.setState(4);
                    actualEnemy.paint();

                } else if (enemyState == 3) {
                    actualEnemy.setState(5);
                    for (int j = 0; j < actualEnemy.getAttackImages().size() - 5; j++) {
                        graphicsContext.drawImage(actualEnemy.getAttackImages().get(j),
                                actualEnemy.getPosition().getX(), actualEnemy.getPosition().getY());
                    }

                }
                double inicialPositionX = avatar.getMapInicialPosicion().getX();
                double inicialPositionY = avatar.getMapInicialPosicion().getY();
                avatar.getHitbox().setX(inicialPositionX);
                avatar.getPosition().setX(inicialPositionX);
                avatar.getHitbox().setY(inicialPositionY);
                avatar.getPosition().setY(inicialPositionY);

                if (avatar.getLives() == 0) {
                    avatar.setState(8);
                    avatar.paint();
                    avatar.setIsAlive(false);
                    actualEnemy.setState(0);
                    actualEnemy.paint();

                    gameOver();

                    break;
                } else {
                    int state = avatar.getState();
                    if (state == 2 || state == 0 || state == 4) {
                        // Lo empuja a la izquierda
                        avatar.setState(0);
                        avatar.paint();
                        avatar.setState(6);
                        avatar.paint();

                    } else {
                        // Lo empuja a la derecha
                        avatar.setState(1);
                        avatar.paint();
                        avatar.setState(7);
                        avatar.paint();
                    }
                    playHurtPlayer();
                    break;
                }

            }

        }

    }

    private void paintPointer() {
        graphicsContext.drawImage(pointerImage, mouseX, mouseY);
    }

    private void paintWeaponInfo() {
        weaponInfo = "Bullets:" + currentBullets;
        double infoX = 10;
        double infoY = canvas.getHeight() - 30;
        text = new Text(weaponInfo);
        text.setFont(graphicsContext.getFont());

        graphicsContext.setFont(font);
        graphicsContext.setFill(Color.WHITE); // Color De Fondo Transparente (Color.color(0, 0, 0, 0))
        graphicsContext.fillText(weaponInfo, infoX, infoY);
        double textWidth = text.getLayoutBounds().getWidth();

        double weaponImageX = infoX + textWidth + 10;
        double weaponImageY = infoY - weaponImage.get(avatar.getWeapon()).getHeight();

        graphicsContext.drawImage(weaponImage.get(avatar.getWeapon()), weaponImageX, weaponImageY);
    }

    private void paintLifes() {
        int heartSize = 20;
        int heartSpacing = 10;
        double heartsX = 10;
        double heartsY = 10;

        for (int i = 0; i < avatar.getLives(); i++) {
            double heartPosX = heartsX + (i * (heartSize + heartSpacing));
            double heartPosY = heartsY;

            // Obtener la imagen correspondiente al frame de animación actual
            Image heartImage = lifes.get(animationFrame % 7);

            graphicsContext.drawImage(heartImage, heartPosX, heartPosY, heartSize, heartSize);
        }

        // Actualizar el frame de animación para el próximo ciclo
        animationFrame++;
    }

    public void paintColisionBoxWeapon() {
        // Bucle para la colision con las cajas de armas
        for (int i = 0; i < boxWeapons.size(); i++) {

            BoxWeapon actualBoxWeapon = boxWeapons.get(i);

            if (avatar.getHitbox().intersects(actualBoxWeapon.getHitbox().getBoundsInParent())) {

                if (actualBoxWeapon.getWeapon() == 0) {
                    boxWeapons.remove(i);
                    i--;
                    avatar.setWeapon(1);
                    currentBullets = 30;
                    avatar.paint();
                } else if (actualBoxWeapon.getWeapon() == 1) {
                    boxWeapons.remove(i);
                    i--;
                    avatar.setWeapon(2);
                    currentBullets = 30;
                    avatar.paint();
                }

                break;

            }

        }

    }

    public void killAllEnemies() {
        for (Enemy enemy : enemies) {
            enemy.setIsAlive(false);

        }
    }

    public void playHurtPlayer() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/hurtPlayer.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            Clip disparoSound = AudioSystem.getClip();
            disparoSound.open(audioInputStream);
            disparoSound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public boolean verifyPosWallCollision(double posX, double posY) {
        for (Rectangle wall : screens.get(actualScreen - 1).getWalls()) {
            Bounds hitboxBounds = wall.getBoundsInParent();
            double rectLeft = hitboxBounds.getMinX();
            double rectRight = hitboxBounds.getMaxX();
            double rectTop = hitboxBounds.getMinY();
            double rectBottom = hitboxBounds.getMaxY();

            boolean collision = posX >= rectLeft && posX <= rectRight && posY >= rectTop && posY <= rectBottom;
            if (collision) {
                return true;
            }
        }
        return false;
    }

    public void paintShoot() {

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint();
            if (verifyPosWallCollision(bullets.get(i).getPositionX(), bullets.get(i).getPositionY())) {
                bullets.remove(i);
                i--;
            }
            ;
        }

        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {

                int actualEnemyLives = enemies.get(i).getLives();

                Enemy actualEnemy = enemies.get(i);
                Bullet actualBullet = bullets.get(j);

                Rectangle hitbox = actualEnemy.getHitbox(); // Obtén la hitbox del enemigo
                double targetX = actualBullet.getPositionX(); // Coordenada X del objetivo
                double targetY = actualBullet.getPositionY(); // Coordenada Y del objetivo

                Bounds hitboxBounds = hitbox.getBoundsInParent();
                double hitboxCenterX = hitboxBounds.getMinX() + hitboxBounds.getWidth() / 2;
                double hitboxCenterY = hitboxBounds.getMinY() + hitboxBounds.getHeight() / 2;

                double distance = Math.sqrt(
                        Math.pow(hitboxCenterX - targetX, 2) +
                                Math.pow(hitboxCenterY - targetY, 2));

                if (distance <= 80) {
                    bullets.remove(j);
                    enemies.get(i).setLives(actualEnemyLives - 1);
                    int enemyState = enemies.get(i).getState();
                    if (enemyState == 3) {
                        enemies.get(i).setState(6);
                        actualEnemy.paint();
                    } else if (enemyState == 2) {
                        enemies.get(i).setState(7);
                        actualEnemy.paint();
                    }
                    playSoundHurtEnemy();
                    if (actualEnemyLives == 1) {
                        enemies.get(i).setState(8);
                        actualEnemy.paint();
                        enemies.get(i).setIsAlive(false);
                        enemies.remove(i);
                        i--;
                        return;
                    }

                }
            }
        }
    }

    public void playSoundHurtEnemy() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/hurtEnemy.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            Clip disparoSound = AudioSystem.getClip();
            disparoSound.open(audioInputStream);
            disparoSound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void setRunning(boolean running) {
        isRunning = running;

        // Detener la música de fondo
        if (!isRunning) {
            backgroundMusic.stop();
        }
    }

    public void initEvents() {

        canvas.setOnKeyPressed(this::onKeyPressed);

        canvas.setOnKeyReleased(this::onKeyReleased);

        canvas.setOnMousePressed(this::onMousePressed);

        canvas.setOnMouseReleased(this::onMouseReleased);
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

    /**
     * @return int return the actualScreen
     */
    public int getActualScreen() {
        return actualScreen;
    }

    /**
     * @param actualScreen the actualScreen to set
     */
    public void setActualScreen(int actualScreen) {
        this.actualScreen = actualScreen;
    }

    /**
     * @param graphicsContext the graphicsContext to set
     */
    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
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
     * @return ArrayList<BaseScreen> return the screens
     */
    public ArrayList<BaseScreen> getScreens() {
        return screens;
    }

    /**
     * @param screens the screens to set
     */
    public void setScreens(ArrayList<BaseScreen> screens) {
        this.screens = screens;
    }

    /**
     * @return ArrayList<Image> return the lifes
     */
    public ArrayList<Image> getLifes() {
        return lifes;
    }

    /**
     * @param lifes the lifes to set
     */
    public void setLifes(ArrayList<Image> lifes) {
        this.lifes = lifes;
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
     * @return ArrayList<Bullet> return the bullets
     */
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /**
     * @param bullets the bullets to set
     */
    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
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

    /**
     * @param random the random to set
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * @return int return the currentBullets
     */
    public int getCurrentBullets() {
        return currentBullets;
    }

    /**
     * @param currentBullets the currentBullets to set
     */
    public void setCurrentBullets(int currentBullets) {
        this.currentBullets = currentBullets;
    }

    /**
     * @return ArrayList<Image> return the weaponImage
     */
    public ArrayList<Image> getWeaponImage() {
        return weaponImage;
    }

    /**
     * @param weaponImage the weaponImage to set
     */
    public void setWeaponImage(ArrayList<Image> weaponImage) {
        this.weaponImage = weaponImage;
    }

    /**
     * @return Text return the text
     */
    public Text getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(Text text) {
        this.text = text;
    }

    /**
     * @return String return the weaponInfo
     */
    public String getWeaponInfo() {
        return weaponInfo;
    }

    /**
     * @param weaponInfo the weaponInfo to set
     */
    public void setWeaponInfo(String weaponInfo) {
        this.weaponInfo = weaponInfo;
    }

    /**
     * @return double return the mouseX
     */
    public double getMouseX() {
        return mouseX;
    }

    /**
     * @param mouseX the mouseX to set
     */
    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    /**
     * @return double return the mouseY
     */
    public double getMouseY() {
        return mouseY;
    }

    /**
     * @param mouseY the mouseY to set
     */
    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    /**
     * @return Image return the pointerImage
     */
    public Image getPointerImage() {
        return pointerImage;
    }

    /**
     * @param pointerImage the pointerImage to set
     */
    public void setPointerImage(Image pointerImage) {
        this.pointerImage = pointerImage;
    }

    /**
     * @return int return the animationFrame
     */
    public int getAnimationFrame() {
        return animationFrame;
    }

    /**
     * @param animationFrame the animationFrame to set
     */
    public void setAnimationFrame(int animationFrame) {
        this.animationFrame = animationFrame;
    }

    /**
     * @return Clip return the backgroundMusic
     */
    public Clip getBackgroundMusic() {
        return backgroundMusic;
    }

    /**
     * @param backgroundMusic the backgroundMusic to set
     */
    public void setBackgroundMusic(Clip backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    /**
     * @return ArrayList<Image> return the screenImages
     */
    public ArrayList<Image> getScreenImages() {
        return screenImages;
    }

    /**
     * @param screenImages the screenImages to set
     */
    public void setScreenImages(ArrayList<Image> screenImages) {
        this.screenImages = screenImages;
    }

}