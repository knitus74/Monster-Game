package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.util.ArrayList;

public class PlayerTrainer extends Trainer{


    public PlayerTrainer(double xPosition, double yPosition, String fileName){
        //super(xPosition, yPosition, fileName, 23, 30, 13, 7, 30, 30);
        super(xPosition, yPosition, fileName, 15, 21, 0, 11, 32, 30);
        initialiseParty();
    }

    public void initialiseParty(){
        ArrayList firstMonsterMovesSet = new ArrayList();
        firstMonsterMovesSet.add("flamer-boi");
        firstMonsterMovesSet.add("bitch-slap");
        addToParty(new Monster(100, 11, firstMonsterMovesSet));
        ArrayList secondMonsterMoveSet = new ArrayList();
        secondMonsterMoveSet.add("water-boi");
        addToParty(new Monster(35, 10, secondMonsterMoveSet));
    }

    public void setPlayerImage(String facing, String footed){
        setImageSourceImage(new Image("./playerSprites/brendan" + facing + footed + ".png" ));
    }

    /*public void setFacingBack(){
        setImageSourcePosition(13, 7);
    }

    public void setFacingForward(){
        setImageSourcePosition(13, 37);
        //setImageSourceImage(forwardImage);
    }

    public void setFacingLeft(){
        setImageSourcePosition(13, 70);
    }

    public void setFacingRight(){
        setImageSourcePosition(13, 102);
    }

    public void setFacingBackRightFoot(){
        setImageSourcePosition(45, 6);
    }

    public void setFacingBackLeftFoot(){
        setImageSourcePosition(77, 6);
    }

    public void setFacingLeftRightFoot(){
        setImageSourcePosition(45, 70);
    }

    public void setFacingLeftLeftFoot(){
        setImageSourcePosition(77, 70);
    }

    public void setFacingRightRightFoot(){
        setImageSourcePosition(45, 102);
    }

    public void setFacingRightLeftFoot(){
        setImageSourcePosition(77, 102);
    }

    public void setFacingForwardLeftFoot(){
        setImageSourcePosition(77, 37);
    }

    public void setFacingForwardRightFoot(){
        setImageSourcePosition(45, 37);
    }*/
}
