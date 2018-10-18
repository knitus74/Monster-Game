package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Sprite {
    private double xPosition;
    private double yPosition;
    private DoubleProperty xPositionProperty;
    private DoubleProperty yPositionProperty;
    private Image image;
    private double imageSourceWidth;
    private double imageSourceHeight;
    private double imageSourceXPosition;
    private double imageSourceYPosition;
    private boolean canWalkThrough;
    private double imageOutputWidth;
    private double imageOutputHeight;
    private boolean isGrass = false;


    public Sprite(double xPosition, double yPosition, double imageSourceWidth, double imageSourceHeight, double imageSourceXPosition, double imageSourceYPosition, Image image, double imageOutputHeight, double imageOutputWidth){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.image = image;
        xPositionProperty = new SimpleDoubleProperty(xPosition);
        yPositionProperty = new SimpleDoubleProperty(yPosition);
        this.imageSourceWidth = imageSourceWidth;
        this.imageSourceHeight = imageSourceHeight;
        this.imageSourceXPosition = imageSourceXPosition;
        this.imageSourceYPosition = imageSourceYPosition;
        this.imageOutputWidth = imageOutputWidth;
        this.imageOutputHeight = imageOutputHeight;
        canWalkThrough = true;
    }

    public double getImageWidth(){
        return imageOutputWidth;
    }

    public double getImageHeight(){
        return imageOutputHeight;
    }

    public boolean getCanWalkThrough(){
        return canWalkThrough;
    }

    public void setCannotWalkThrough(){ canWalkThrough = false;}

    public boolean getIsGrass(){ return isGrass;}

    public void setIsGrass(){ isGrass = true;}


    public DoubleProperty getXPositionProperty(){
        return xPositionProperty;
    }

    public DoubleProperty getYPositionProperty() {
        return yPositionProperty;
    }

    public void render(GraphicsContext gc){
        gc.drawImage( image, imageSourceXPosition, imageSourceYPosition, imageSourceWidth, imageSourceHeight, xPositionProperty.get(), yPositionProperty.get(), imageOutputWidth, imageOutputHeight);
    }

    public double getXPosition(){
        return xPosition;
    }

    public double getYPosition(){
        return yPosition;
    }

    public void setPosition(double xPosition, double yPosition){
        this.yPosition = yPosition;
        this.xPosition = xPosition;
    }

    public void setImageSourcePosition(double x, double y){
        imageSourceXPosition = x;
        imageSourceYPosition = y;
    }

    public void setImageSourceImage(Image image){
        this.image = image;
    }

}