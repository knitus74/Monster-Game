package com.company;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class TileMap {

    private static final double imageSize = 30;
    private static double screenHeight = 0.0;
    private static double screenWidth = 0.0;
    private static Image terrainSprites = new Image("background-sheet.png");
    private static ArrayList<Sprite> grassTerrain = new ArrayList<>();


    public static ArrayList<Sprite> getGrassTerrain(){
        for(int i = 0; i < 50; i++){
            for(int j = 0; j<50; j++){
                grassTerrain.add(new Sprite(i*imageSize, j*imageSize, 16, 16,80.5, 80, terrainSprites, imageSize, imageSize));
            }
        }
        return grassTerrain;
    };

    public static ArrayList<Sprite> getGrassForeground(){
        ArrayList<Sprite> grassForeground = new ArrayList<>();
        makeLongGrass((3*imageSize), (3*imageSize), grassForeground,  10, 8);
        makeVerticalFence(imageSize, imageSize, grassForeground, 6);
        return grassForeground;
    }

    public static void moveTerrain(double dx, double dy, ArrayList<Sprite> terrain, ArrayList<Sprite> foregroundTerrain, Timeline movementAnimations){
        moveTiles(movementAnimations, dx, dy, terrain);
        moveTiles(movementAnimations, dx, dy, foregroundTerrain);
    }

    private static void moveTiles(Timeline movementAnimations, double dx, double dy, ArrayList<Sprite> terrain){
        for(Sprite s: terrain){
            if(s.getXPosition() > - 40 && s.getXPosition() < screenWidth + 40 && s.getYPosition() > -40 && s.getYPosition() < screenHeight + 40) {
                KeyValue trainerMovementX = new KeyValue(s.getXPositionProperty(), s.getXPosition() - dx);
                KeyValue trainerMovementY = new KeyValue(s.getYPositionProperty(), s.getYPosition() - dy);
                EventHandler onFinished = (t) -> {
                    s.setPosition(s.getXPosition() - dx, s.getYPosition() - dy);
                };
                movementAnimations.getKeyFrames().add(new KeyFrame(Duration.millis(200), onFinished, trainerMovementY, trainerMovementX));
            }
            else{
                double newXPosition = s.getXPosition() - dx;
                double newYPosition = s.getYPosition() - dy;
                s.setPosition(newXPosition, newYPosition);
                s.getXPositionProperty().setValue(newXPosition);
                s.getYPositionProperty().setValue(newYPosition);
            }
        }
    }

    public static void setCanvasDimensions(double width, double height){
        screenWidth = width;
        screenHeight = height;
    }

    private static void makeVerticalFence(double xPosition, double yPosition, ArrayList<Sprite> terrain, int length) {
        for(int i = 0; i<(length - 1); i++) {
            Sprite fencePart = new Sprite(xPosition , yPosition + (imageSize/2 * i), 5.5, 8, 113, 163, terrainSprites, 7*imageSize/12, imageSize / 3);
            fencePart.setCannotWalkThrough();
            terrain.add(fencePart);
        }
        Sprite fencePart = new Sprite(xPosition, yPosition + (imageSize/2 * (length - 1)), 6, 13.5, 9, 98, terrainSprites, imageSize, imageSize/3);
        fencePart.setCannotWalkThrough();
        terrain.add(fencePart);
    }

    private static void makeLongGrass(double xPosition, double yPosition, ArrayList<Sprite> foregroundterrain, int width, int height){
        double imageSize = 30;
        for(int i = 0; i<width; i ++){
            for(int j = 0; j< height; j++){
                grassTerrain.add(new Sprite(xPosition + (i*imageSize), yPosition + (j* imageSize), 14.5, 14, 17, 81, terrainSprites, (imageSize * 0.95), (imageSize * 0.95)));
                //long grass that should go over the player
                Sprite grassClip = new Sprite(xPosition + (i* imageSize), ((6/14.0) *imageSize)+ yPosition + (j*imageSize), 14, 6, 17, 105, terrainSprites, (imageSize*0.95 * (6/14.0)), (imageSize*0.95));
                grassClip.setIsGrass();
                grassTerrain.add(grassClip);
            }
        }
    }

    public static void changeForeground(ArrayList<Sprite> foregroundTerrain, ArrayList<Sprite> terrain, double y){
        //to stop concurrent exception
        List<Sprite> dummyList = new ArrayList<Sprite>();
        for(Sprite s: foregroundTerrain){
            if(s.getIsGrass()){
                dummyList.add(s);
                terrain.add(s);
            }
        }
        foregroundTerrain.removeAll(dummyList);
        dummyList = new ArrayList<Sprite>();
        for(Sprite s: terrain){
            if(s.getIsGrass()){
                if(s.getYPositionProperty().get()> y && s.getYPositionProperty().get() - 25 < y){
                    foregroundTerrain.add(s);
                    dummyList.add(s);
                }
            }
        }
        terrain.removeAll(dummyList);
    }

}
