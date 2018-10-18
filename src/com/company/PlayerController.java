package com.company;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.EventHandler;
import javafx.util.Duration;
import java.util.ArrayList;

import static com.company.WalkingScene.getWorldWidth;

public class PlayerController {

    private double dx;
    private double dy;
    private String moveDirection;
    //whether the next foot should be the right foot
    private boolean isLeftFoot;
    //whether you are mid walk
    private boolean isMidWalk;
    private static final double movementStep = 30;

    public PlayerController(){
        dx = 0;
        dy = 0;
        moveDirection = "";
        isLeftFoot = false;
        isMidWalk = false;
    }

    public void movePlayer(String code, boolean firstPress, PlayerTrainer brendan, WalkingScene scene, ArrayList<Sprite> terrain, ArrayList<Sprite> foregroundTerrain){
        if(firstPress) {
            switch(code) {
                case "RIGHT":   scene.setKeyboardInput("RIGHT");
                                //brendan.setFacingRight();
                                brendan.setPlayerImage("_right","");
                                dx = movementStep;
                                dy = 0;
                                moveDirection = "RIGHT";
                                break;
                case "LEFT":    scene.setKeyboardInput("LEFT");
                                //brendan.setFacingLeft();
                                brendan.setPlayerImage("_left", "");
                                dx =  - movementStep;
                                dy = 0;
                                moveDirection = "LEFT";
                                break;
                case "UP":      scene.setKeyboardInput("UP");
                                //brendan.setFacingBack();
                                brendan.setPlayerImage("_back", "");
                                dx = 0;
                                dy = - movementStep;
                                moveDirection = "UP";
                                break;
                case "DOWN":    scene.setKeyboardInput("DOWN");
                                //brendan.setFacingForward();
                                brendan.setPlayerImage("_front", "");
                                dx = 0;
                                dy =  movementStep;
                                moveDirection = "DOWN";
                                break;
            }
        }

        if(scene.getKeyboardInput().equals("DOWN")){
            //to stop the grass clipping issue when walking down from a previous tile
            TileMap.changeForeground(foregroundTerrain, terrain, brendan.getYPositionProperty().get() + movementStep);
        }

        if(scene.getKeyboardInput().equals("RIGHT") || scene.getKeyboardInput().equals("LEFT") || scene.getKeyboardInput().equals("DOWN") || scene.getKeyboardInput().equals("UP")){
            //KeyValue trainerMovementX = new KeyValue(brendan.getXPositionProperty(), brendan.getXPosition() + dx);
            //KeyValue trainerMovementY = new KeyValue(brendan.getYPositionProperty(), brendan.getYPosition() + dy);
            /*EventHandler onFinished = (t) -> {
                brendan.setPosition(brendan.getXPosition() + dx, brendan.getYPosition() + dy);
                if(scene.getKeyboardInput().equals("RIGHT") || scene.getKeyboardInput().equals("LEFT") || scene.getKeyboardInput().equals("DOWN") || scene.getKeyboardInput().equals("UP")){
                    movePlayer(moveDirection, false, brendan, scene, terrain);
                }
            };*/

            EventHandler onFinished = (t) -> {
                if(moveDirection.equals("UP")) {
                    TileMap.changeForeground(foregroundTerrain, terrain, brendan.getYPositionProperty().get());
                }
                if(scene.getKeyboardInput().equals("RIGHT") || scene.getKeyboardInput().equals("LEFT") || scene.getKeyboardInput().equals("DOWN") || scene.getKeyboardInput().equals("UP")){
                    movePlayer(moveDirection, false, brendan, scene, terrain, foregroundTerrain);
                }
            };

            EventHandler changeWalkAnimation = (t) -> {
                switch(code){
                    case "RIGHT":
                                    if(! isMidWalk){
                                        isMidWalk = true;
                                        if(isLeftFoot){
                                            //brendan.setFacingRightLeftFoot();
                                            brendan.setPlayerImage("_right", "_left");
                                            isLeftFoot = false;
                                        }
                                        else{
                                            //brendan.setFacingRightRightFoot();
                                            brendan.setPlayerImage("_right", "_right");
                                            isLeftFoot = true;
                                        }
                                    }
                                    else{
                                        //brendan.setFacingRight();
                                        brendan.setPlayerImage("_right", "");
                                        isMidWalk = false;
                                    }
                                    break;
                    case "LEFT":
                                    if(! isMidWalk){
                                        isMidWalk = true;
                                        if(isLeftFoot){
                                            //brendan.setFacingLeftLeftFoot();
                                            brendan.setPlayerImage("_left", "_left");
                                            isLeftFoot = false;
                                        }
                                        else{
                                            //brendan.setFacingLeftRightFoot();
                                            brendan.setPlayerImage("_left", "_right");
                                            isLeftFoot = true;
                                        }
                                    }
                                    else{
                                        //brendan.setFacingLeft();
                                        brendan.setPlayerImage("_left", "");
                                        isMidWalk = false;
                                    }
                                    break;
                    case "UP":
                                    if(! isMidWalk){
                                        isMidWalk = true;
                                        if(isLeftFoot){
                                            //brendan.setFacingBackLeftFoot();
                                            brendan.setPlayerImage("_back", "_left");
                                            isLeftFoot = false;
                                        }
                                        else{
                                            //brendan.setFacingBackRightFoot();
                                            brendan.setPlayerImage("_back", "_right");
                                            isLeftFoot = true;
                                        }
                                    }
                                    else{
                                        //brendan.setFacingBack();
                                        brendan.setPlayerImage("_back", "");
                                        isMidWalk = false;
                                    }
                                    break;
                    case "DOWN":
                                    if(! isMidWalk){
                                        isMidWalk = true;
                                        if(isLeftFoot){
                                            //brendan.setFacingForwardLeftFoot();
                                            brendan.setPlayerImage("_front", "_left");
                                            isLeftFoot = false;
                                        }
                                        else{
                                            //brendan.setFacingForwardRightFoot();
                                            brendan.setPlayerImage("_front", "_right");
                                            isLeftFoot = true;
                                        }
                                    }
                                    else{
                                        //brendan.setFacingForward();
                                        brendan.setPlayerImage("_front", "");
                                        isMidWalk = false;
                                    }
                                    break;
                }
            };

            scene.getGameLoop().getKeyFrames().setAll(new KeyFrame(Duration.millis(100), changeWalkAnimation), new KeyFrame(Duration.millis(200), changeWalkAnimation));

            //scene.getGameLoop().getKeyFrames().setAll(new KeyFrame(Duration.millis(200), trainerMovementX), new KeyFrame(Duration.millis(200), onFinished, trainerMovementY), new KeyFrame(Duration.millis(100), changeWalkAnimation), new KeyFrame(Duration.millis(200), changeWalkAnimation));
            if(checkCanMove(foregroundTerrain)) {
                TileMap.moveTerrain(dx, dy, terrain, foregroundTerrain, scene.getGameLoop());
            }

            scene.getGameLoop().getKeyFrames().add(new KeyFrame(Duration.millis(200), onFinished));

            scene.getGameLoop().playFromStart();
        }
    }

    private boolean checkCanMove(ArrayList<Sprite> terrain){
        double originalX = WalkingScene.getWorldWidth()/2 - 17;
        double originalY = WalkingScene.getWorldHeight()/2 - 17;
        double finalX = originalX;
        double finalY = originalY;
        switch(moveDirection){
            case "RIGHT":
                            originalX += 30;
                            finalX = originalX + (movementStep);
                            break;
            case "LEFT":
                            finalX = originalX - (movementStep +5);
                            break;
            case "UP":
                            finalY = originalY - (movementStep + 5);
                            break;
            case "DOWN":
                            originalY += 30;
                            finalY = originalY + (movementStep + 5);
                            break;
        }
        //System.out.println("original x " + originalX);
        //System.out.println("final x" + finalX);
        for(Sprite tile: terrain){
            if(!tile.getCanWalkThrough()) {
                double objectX = tile.getXPositionProperty().get();
                double objectY = tile.getYPositionProperty().get();
               // System.out.println("object x" + objectX);

                if( (((originalX< objectX && finalX > objectX) || (originalX > objectX && finalX < objectX)) && (originalY + 5 < (objectY + tile.getImageHeight())) && (originalY + 20 - 5 > objectY)  )

                        ){
                    System.out.println("Object x" + (objectY + tile.getImageHeight()));
                    System.out.println("Original x" + originalY);
                    System.out.println("final x" + finalX);
                    return false;
                }
            }
        }
        return true;
    }

}
