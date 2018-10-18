package com.company;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Trainer extends Sprite{

    private ArrayList<Monster> party;

    public Trainer(double xPosition, double yPosition, String fileName, double imageSourceWidth, double imageSourceHeight, double imageSourceXPosition, double imageSourceYPosition, double imageOutputHeight, double imageOutputWidth){
        super(xPosition, yPosition, imageSourceWidth, imageSourceHeight, imageSourceXPosition, imageSourceYPosition, new Image(fileName), imageOutputHeight, imageOutputWidth);
        party = new ArrayList();
    }


    public boolean addToParty(Monster monster){
        if(party.size() < 7) {
            party.add(monster);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeFromParty(int index){
        try{
            party.remove(index);
            return true;
        }
        catch(NullPointerException e){
            return false;
        }
    }

    public ArrayList<Monster> getParty(){
        return party;
    }
}
