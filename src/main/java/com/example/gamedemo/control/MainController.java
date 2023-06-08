package com.example.gamedemo.control;

import com.example.gamedemo.model.*;
import com.example.gamedemo.screens.BaseScreen;
import javafx.application.Platform;
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

public class MainController implements Initializable {

    @FXML
    private Canvas canvas;

    private boolean isRunning;

    private int actualScreen;

    private int animationFrame;

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
        actualScreen = 1;
        screens = new ArrayList<>(4);
        screenImages= new ArrayList<>(3);
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

        initEvents();
    }

    public Vector getRandomPosition() {
        double x = random.nextInt((int) canvas.getWidth() - 100) + 50; // Genera una coordenada X aleatoria

        double y = random.nextInt((int) canvas.getHeight() - 100) + 50; // Genera una coordenada Y aleatoria

        return new Vector(x, y);
    }

    public void addImagesScreen() {
        for (int i = 1; i <= 4; i++) {
            screenImages.add(new Image(
                    getClass().getResourceAsStream("/scenarios/scenario_" + i + ".png")));
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
        for (int index = 0; index < 2; index++) {
            BoxWeapon boxWeapon = new BoxWeapon(canvas, getRandomPosition(), index, 50);
            boxWeapons.add(boxWeapon);
            new Thread(boxWeapon).start();

        }
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

    public void paint() {

        // Acomodar el contexto grafico (Screen)
        graphicsContext.drawImage( screenImages.get(0), 0,0,canvas.getWidth(),canvas.getHeight());

        // Insertar al avatar
        avatar.paint();

        // Seguir avatar
        for (Enemy enemy : enemies) {
            enemy.follow();
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
        graphicsContext.setFill(Color.WHITE);
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

            double distanceColision = Math.sqrt(
                    Math.pow(avatar.getPosition().getX() - actualBoxWeapon.getPositionX(), 2) +
                            Math.pow(avatar.getPosition().getY() - actualBoxWeapon.getPositionY(), 2));

            if (distanceColision <= 60) {

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
                } else {
                    boxWeapons.remove(i);
                    i--;
                    avatar.setWeapon(1);
                    currentBullets = 30;
                    avatar.paint();
                }

                break;

            }

        }

    }

    public void paintColisionEnemy() {

        // Bucle para la colision con el enemigo
        for (int i = 0; i < enemies.size(); i++) {

            Enemy actualEnemy = enemies.get(i);

            double distanceColision = Math.sqrt(
                    Math.pow(avatar.getPosition().getX() - actualEnemy.getPosition().getX(), 2) +
                            Math.pow(avatar.getPosition().getY() - actualEnemy.getPosition().getY(), 2));

            if (distanceColision <= 80) {

                avatar.setLives(avatar.getLives() - 1);
                if (avatar.getLives() == 1) {
                    avatar.setState(8);
                    avatar.paint();
                    avatar.setIsAlive(false);
                    break;
                } else {
                    int state = avatar.getState();
                    if (state == 2 || state == 0) {
                        avatar.getPosition().setX(avatar.getPosition().getX() - 300);
                        avatar.setState(0);
                        avatar.paint();
                        avatar.setState(6);
                        avatar.paint();
                    } else {
                        avatar.getPosition().setX(avatar.getPosition().getX() + 300);
                        avatar.setState(1);
                        avatar.paint();
                        avatar.setState(7);
                        avatar.paint();
                    }
                    break;
                }

            }

        }

    }

    public void paintShoot() {

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint();

            if (bullets.get(i).getPositionX() > canvas.getWidth()) {
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {

                int actualEnemyLives = enemies.get(i).getLives();

                Enemy actualEnemy = enemies.get(i);
                Bullet actualBullet = bullets.get(j);

                double distance = Math.sqrt(
                        Math.pow(actualEnemy.getPosition().getX() - actualBullet.getPositionX(), 2) +
                                Math.pow(actualEnemy.getPosition().getY() - actualBullet.getPositionY(), 2));

                if (distance <= 80) {
                    bullets.remove(j);
                    enemies.get(i).setLives(actualEnemyLives - 1);
                    if (actualEnemyLives == 1) {
                        enemies.get(i).setState(8);
                        enemies.get(i).paint();
                        enemies.get(i).setIsAlive(false);
                        enemies.remove(i);
                        i--;
                        return;
                    }

                }
            }
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
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

}