package com.example.gamedemo.screens;

import com.example.gamedemo.model.Enemy;
import com.example.gamedemo.model.BoxWeapon;
import com.example.gamedemo.model.Bullet;
import com.example.gamedemo.model.Vector;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class Scenario1 extends BaseScreen {

    private Enemy enemy;

    private ArrayList<Enemy> enemies;

    private ArrayList<Bullet> bullets;

    private int currentBullets;

    private ArrayList<BoxWeapon> boxWeapons;
    private ArrayList<Image> lifes;

    private BoxWeapon boxWeapon1;
    private BoxWeapon boxWeapon2;

    private Random random;

    private double mouseX;
    private double mouseY;

    private Image pointerImage;
    private Font font;

    public Scenario1(Canvas canvas) {
        super(canvas);
        random = new Random();
        enemy = new Enemy(canvas);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        currentBullets = 0;
        boxWeapons = new ArrayList<>();
        boxWeapon1 = new BoxWeapon(canvas, getRandomPosition(), 0, 45);
        boxWeapon2 = new BoxWeapon(canvas, getRandomPosition(), 1, 45);
        boxWeapons.add(boxWeapon1);
        boxWeapons.add(boxWeapon2);

        Enemy enemy = new Enemy(canvas, getRandomPosition());
        enemy.start();
        enemies.add(enemy);

        pointerImage = new Image(getClass().getResourceAsStream("/animations/pointer/pointer.png"));
        font = Font.loadFont(getClass().getResourceAsStream("/fonts/Super Mario Bros. 2.ttf"), 20);

        lifes = new ArrayList<>();

        // Cargar imagenes de la vida
        addImagesLifes();

        canvas.setOnMouseMoved(event -> onMouseMoved(event));

    }

    public void addImagesLifes() {
        for (int i = 0; i <= 7; i++) {
            lifes.add(new Image(getClass().getResourceAsStream("/animations/lifes/" + i + ".png")));
        }
    }

    public void onMouseMoved(MouseEvent event) {
        mouseX = (event.getX());
        mouseY = (event.getY());
    }

    public Vector getRandomPosition() {
        double x = random.nextInt((int) canvas.getWidth() - 100) + 50; // Genera una coordenada X aleatoria

        double y = random.nextInt((int) canvas.getHeight() - 100) + 50; // Genera una coordenada Y aleatoria

        return new Vector(x, y);
    }

    @Override
    public void paint() {

        // Acomoddar el contexto grafico (Screen)
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Insertar al avatar
        enemy.paint();

        // Insertar cajas de armas en el screen
        for (BoxWeapon bW : boxWeapons) {
            bW.paint();
        }

        // Insertar enemigos en el screen
        for (Enemy b : enemies) {
            b.paint();
        }

        // Bucle para la colision con el enemigo
        paintColisionEnemy();

        // Bucle para la colision con las cajas de armas
        paintColisionBoxWeapon();

        // Bucle para disparar
        paintShoot();

        // Bucle para las vidas
        paintLifes();

        // Dibujo del cursor
        paintPointer();

        // Dibujar informacion del arma
        paintWeaponInfo();

    }

    private void paintWeaponInfo() {
        if(enemy.getWeapon() == 0){

        }else {
            String weaponInfo = "Bullets:" + currentBullets;
            double infoX = 10;
            double infoY = canvas.getHeight() - 30;

            graphicsContext.setFont(font);
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.fillText(weaponInfo, infoX, infoY);

            Image weaponImage = new Image(
                    getClass().getResourceAsStream("/animations/weapons/" + enemy.getWeapon() + ".png"));

            Text text = new Text(weaponInfo);
            text.setFont(graphicsContext.getFont());
            double textWidth = text.getLayoutBounds().getWidth();

            double weaponImageX = infoX + textWidth + 10;
            double weaponImageY = infoY - weaponImage.getHeight();

            graphicsContext.drawImage(weaponImage, weaponImageX, weaponImageY);
        }
    }

    private void paintPointer() {
        graphicsContext.drawImage(pointerImage, mouseX, mouseY);
    }

    private void paintLifes() {

        int frame = 0;

        int heartSize = 20;
        int heartSpacing = 10;
        double heartsX = 10;
        double heartsY = 10;

        for (int i = 0; i < enemy.getLives(); i++) {
            double heartPosX = heartsX + (i * (heartSize + heartSpacing));
            double heartPosY = heartsY;
            for (int j = 0; j < lifes.size(); j++) {
                graphicsContext.drawImage(lifes.get(frame % 7), heartPosX, heartPosY, heartSize, heartSize);
                frame++;
            }
        }
    }

    public void paintColisionBoxWeapon() {
        // Bucle para la colision con las cajas de armas
        for (int i = 0; i < boxWeapons.size(); i++) {

            BoxWeapon actualBoxWeapon = boxWeapons.get(i);

            double distanceColision = Math.sqrt(
                    Math.pow(enemy.getPosition().getX() - actualBoxWeapon.getPositionX(), 2) +
                            Math.pow(enemy.getPosition().getY() - actualBoxWeapon.getPositionY(), 2));

            if (distanceColision <= 45) {

                if (actualBoxWeapon.getWeapon() == 0) {
                    boxWeapons.remove(i);
                    i--;
                    enemy.setWeapon(1);
                    currentBullets += 30;
                    enemy.paint();
                } else if (actualBoxWeapon.getWeapon() == 1) {
                    boxWeapons.remove(i);
                    i--;
                    enemy.setWeapon(2);
                    currentBullets += 30;
                    enemy.paint();
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
                    Math.pow(enemy.getPosition().getX() - actualEnemy.getPosition().getX(), 2) +
                            Math.pow(enemy.getPosition().getY() - actualEnemy.getPosition().getY(), 2));

            if (distanceColision <= 60) {
                enemy.getPosition().setX(enemy.getPosition().getX() + 70);
                int state = enemy.getState();
                if (state == 2 || state == 0) {
                    enemy.setState(0);
                    enemy.paint();
                    enemy.setState(6);
                    enemy.paint();
                } else {
                    enemy.setState(1);
                    enemy.paint();
                    enemy.setState(7);
                    enemy.paint();
                }
                break;
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

                Enemy actualEnemy = enemies.get(i);
                Bullet actualBullet = bullets.get(j);

                double distance = Math.sqrt(
                        Math.pow(actualEnemy.getPosition().getX() - actualBullet.getPositionX(), 2) +
                                Math.pow(actualEnemy.getPosition().getY() - actualBullet.getPositionY(), 2));

                if (distance <= 60) {
                    Enemy deletedEnemy = enemies.remove(i);

                    deletedEnemy.setAlive(false);
                    bullets.remove(j);
                    return;
                }
            }
        }
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        enemy.onKeyPressed(event);

        // Verificar si se presionó la tecla de recarga (por ejemplo, la tecla "R")
        if (event.getCode() == KeyCode.R) {
            reloadWeapon();
        }
    }

    private void reloadWeapon() {
        // Verificar si el arma actual del avatar es recargable
        int currentWeapon = enemy.getWeapon();
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

    @Override
    public void onKeyReleased(KeyEvent event) {
        enemy.onKeyReleased(event);

    }

    @Override
    public void onMousePressed(MouseEvent event) {
        if (enemy.getWeapon() != 0) {
            if (currentBullets > 0) {
                int weapon = enemy.getWeapon();
                double diffX = event.getX() - enemy.getPosition().getX();
                double diffY = event.getY() - enemy.getPosition().getY();

                Vector diff = new Vector(diffX, diffY);
                diff.normalize();
                diff.setSpeed(40);

                enemy.onMousePressed(event);

                int pos = enemy.getState();

                if (pos == 4) {
                    bullets.add(
                            new Bullet(canvas,
                                    new Vector(enemy.getPosition().getX() + 120, enemy.getPosition().getY() + 50),
                                    diff, weapon));
                } else {
                    bullets.add(
                            new Bullet(canvas,
                                    new Vector(enemy.getPosition().getX() - 30, enemy.getPosition().getY() + 50),
                                    diff, weapon));
                }

                currentBullets--;
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        if (enemy.getWeapon() != 0) {
            if (currentBullets > 0) {
                int weapon = enemy.getWeapon();
                double diffX = event.getX() - enemy.getPosition().getX();
                double diffY = event.getY() - enemy.getPosition().getY();

                Vector diff = new Vector(diffX, diffY);
                diff.normalize();
                diff.setSpeed(40);

                enemy.onMousePressed(event);

                int pos = enemy.getState();

                if (pos == 4) {
                    bullets.add(
                            new Bullet(canvas,
                                    new Vector(enemy.getPosition().getX() + 120, enemy.getPosition().getY() + 50),
                                    diff, weapon));
                } else {
                    bullets.add(
                            new Bullet(canvas,
                                    new Vector(enemy.getPosition().getX() - 30, enemy.getPosition().getY() + 50),
                                    diff, weapon));
                }
                currentBullets--;
            }
        }

    }

    @Override
    public void onMouseReleased(MouseEvent event) {
        enemy.onMouseReleased(event);
    }

    /**
     * @return Avatar return the avatar
     */
    public Enemy getAvatar() {
        return enemy;
    }

    /**
     * @param enemy the avatar to set
     */
    public void setAvatar(Enemy enemy) {
        this.enemy = enemy;
    }

    /**
     * @return ArrayList<Box> return the boxes
     */
    public ArrayList<Enemy> getBoxes() {
        return enemies;
    }

    /**
     * @param enemies the boxes to set
     */
    public void setBoxes(ArrayList<Enemy> enemies) {
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
     * @return BoxWeapon return the boxWeapon1
     */
    public BoxWeapon getBoxWeapon1() {
        return boxWeapon1;
    }

    /**
     * @param boxWeapon1 the boxWeapon1 to set
     */
    public void setBoxWeapon1(BoxWeapon boxWeapon1) {
        this.boxWeapon1 = boxWeapon1;
    }

    /**
     * @return BoxWeapon return the boxWeapon2
     */
    public BoxWeapon getBoxWeapon2() {
        return boxWeapon2;
    }

    /**
     * @param boxWeapon2 the boxWeapon2 to set
     */
    public void setBoxWeapon2(BoxWeapon boxWeapon2) {
        this.boxWeapon2 = boxWeapon2;
    }

    /**
     * @return Random return the randdom
     */
    public Random getRandom() {
        return random;
    }

    /**
     * @param randdom the randdom to set
     */
    public void setRandom(Random randdom) {
        this.random = randdom;
    }

}
