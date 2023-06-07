package com.example.gamedemo.screens;

import com.example.gamedemo.model.Avatar;
import com.example.gamedemo.model.Box;
import com.example.gamedemo.model.Bullet;
import com.example.gamedemo.model.Vector;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ScreeA extends BaseScreen{

    private Avatar avatar;

    private ArrayList<Box> boxes;

    private ArrayList<Bullet> bullets;

    public ScreeA(Canvas canvas) {
        super(canvas);
        avatar = new Avatar(canvas);
        boxes = new ArrayList<>();
        bullets = new ArrayList<>();

        Box box = new Box(canvas, new Vector(100, 100));
        box.start();
        boxes.add(box);
    }

    @Override
    public void paint() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());

        avatar.paint();

        for (Box b: boxes) {
           b.paint();
        }

        for (int i = 0; i< bullets.size(); i++){
            bullets.get(i).paint();

            if(bullets.get(i).getPositionX() > canvas.getWidth()){
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i< boxes.size(); i++){
            for (int j = 0; j < bullets.size(); j++){

                Box actualBox = boxes.get(i);
                Bullet actualBullet = bullets.get(j);

                double distanceBullets = Math.sqrt(
                        Math.pow(actualBox.getPosition().getX() - actualBullet.getPositionX(), 2) +
                                Math.pow(actualBox.getPosition().getY() - actualBullet.getPositionY(), 2)
                );

                if (distanceBullets <= 60) {
                    actualBox.setLifes(actualBox.getLifes() - 1);
                    if (actualBox.getLifes() == 0) {
                        boxes.get(i).setAlive(false);
                        boxes.remove(i);
                        i--;
                    }
                    bullets.remove(j);
                    break; 
                }

                System.out.println(distanceBullets);
            }
        }

        for (int i = 0; i< boxes.size(); i++) {

            Box actualBox = boxes.get(i);

             double distanceColision = Math.sqrt(
                        Math.pow(avatar.getPosition().getX() - actualBox.getPosition().getX(), 2) +
                                Math.pow(avatar.getPosition().getY() - actualBox.getPosition().getY(), 2)
                );

                if (distanceColision <= 60){
                    avatar.getPosition().setX(avatar.getPosition().getX() + 70);
                    avatar.setState(1);
                  
                    avatar.paint();
                  avatar.setState(3);    
                    avatar.paint();
                    break;
                }

                System.out.println(distanceColision);
            
        }

    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        avatar.onKeyPressed(event);
    }

    @Override
    public void onKeyReleased(KeyEvent event) {
        avatar.onKeyReleased(event);
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        if(avatar.getWeapon()!=0){
            double diffX = event.getX() - (avatar.getPosition().getX());
            double diffY = event.getY() - (avatar.getPosition().getY());
    
            Vector diff = new Vector(diffX, diffY);
    
            diff.normalize();
            diff.setSpeed(35);
            bullets.add(
                    new Bullet(canvas, new Vector( avatar.getPosition().getX(), avatar.getPosition().getY()), diff,1)
            );
            avatar.onMousePressed(event);
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event){
        if(avatar.getWeapon()!=0){
            double diffX = event.getX() - (avatar.getPosition().getX());
            double diffY = event.getY() - (avatar.getPosition().getY());
    
            Vector diff = new Vector(diffX, diffY);
    
            diff.normalize();
            diff.setSpeed(35);
    
            bullets.add(
                    new Bullet(canvas, new Vector( avatar.getPosition().getX(), avatar.getPosition().getY()), diff,1)
            );
            avatar.onMousePressed(event);
        }    
        
    }

    @Override
    public void onMouseReleased(MouseEvent event){
        avatar.onMouseReleased(event);
    }
}
