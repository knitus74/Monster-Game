package com.company;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class HealthBar {

    BattleDialogue textController;
    private Monster monster;
    private double healthBarInnerWidth;
    private double totalHP;
    @FXML
    Text monsterNameText;
    @FXML
    Text monsterLevelText;
    @FXML
    Text healthFractionText;
    @FXML
    GridPane wholeHealthBar;
    @FXML
    Rectangle healthBarOuter;
    @FXML
    Rectangle healthBarInner;

    public void setHealthBarFeatures(Monster monster, double healthWidth, double healthHeight){
        this.monster = monster;
        monsterNameText.setText(monster.getName());
        monsterLevelText.setText(Integer.toString(monster.getLevel()));
        int hpLeft = monster.getHPLeft();
        totalHP = monster.getMonsterStats().get("hp");
        double hpFraction = hpLeft/totalHP * 1.0;
        healthFractionText.setText(hpLeft + "/" + (int)totalHP);
        healthBarInnerWidth = healthWidth * 0.75;

        wholeHealthBar.setMaxSize(healthWidth, healthHeight);
        wholeHealthBar.setMaxSize(healthWidth, healthHeight);
        healthBarOuter.setWidth(healthBarInnerWidth + 6);
        healthBarInner.setWidth(healthBarInnerWidth * hpFraction);
        healthBarInner.setFill(Color.web(changeHealthBarColour(hpFraction)));
    }

    private String changeHealthBarColour(double lengthRatio){
        if(lengthRatio <= 0.2){
            return "red";
        }
        else if(lengthRatio <= 0.5){
            return "yellow";
        }
        else{
            return "green";
        }
    }

    public void changeHealthAmount(){
        Timeline healthChangeTimeline = new Timeline();
        healthChangeTimeline.setCycleCount(1);
        int newHP = monster.getHPLeft();
        double newFraction = newHP/totalHP * 1.0;
        healthChange.start();
        KeyValue healthBarLength = new KeyValue(healthBarInner.widthProperty(), healthBarInnerWidth*newFraction);
        EventHandler finishAnimation = (t) -> {textController.finishAnimation(); healthChange.stop();};
        healthChangeTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(1000), finishAnimation, healthBarLength));
        healthChangeTimeline.playFromStart();
    }

    public void setTextController(BattleDialogue textController){
        this.textController = textController;
    }

    private AnimationTimer healthChange = new AnimationTimer() {
        @Override
        public void handle(long now) {
            double currentFraction = healthBarInner.getWidth()/healthBarInnerWidth;
            healthBarInner.setFill(Color.web(changeHealthBarColour(currentFraction)));
            int currentHP = (int)Math.round(currentFraction * totalHP);
            healthFractionText.setText(Integer.toString(currentHP) + "/" + (int)totalHP);
        }
    };
}
