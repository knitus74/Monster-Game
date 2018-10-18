package com.company;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class BattleDialogue {

    private String actionToBeTaken = "";
    private boolean isAIToAnimate;
    ArrayList<HealthBar> healthBarControllers = new ArrayList();
    boolean isFaint = false;
    boolean isChangeHealth = false;
    boolean isAnimationTakingPlace = false;
    BattleLogic battleLogic;
    private double dialogueBoxWidth;
    private double dialogueBoxHeight;
    Monster defendingMonster;
    Monster attackingMonster;
    private Battle battle;
    private boolean isWriting = false;
    private ArrayList<String> textSequence = new ArrayList();
    private String fullFightText = "";
    private static String partialText = "";
    private static double textSpeed = 50;
    private static Timeline textAnimation = new Timeline();
    private static boolean isWild = true;
    private static String opponentName = "";
    @FXML
    private Text battleText;
    @FXML
    private HBox dialogueInner;
    private GridPane battleOptions;
    private ArrayList<String> actionSequences = new ArrayList();

    private GridPane moveDescription;

    public void initiateBattle(int numberOfAIMonsters, int numberOfPlayerMonsters, boolean isWild, Object attacker, Monster defendingMonster, Battle battle, double dialogueBoxWidth, double dialogueBoxHeight){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("battleOptions.fxml"));
            loader.setController(this);
            battleOptions = loader.load();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        this.dialogueBoxWidth = dialogueBoxWidth;
        this.dialogueBoxHeight = dialogueBoxHeight;
        this.defendingMonster = defendingMonster;
        this.battle = battle;
        this.isWild = isWild;
        textAnimation.setCycleCount(1);
        if(isWild){
            attackingMonster = (Monster) attacker;
            fullFightText = "Wild " + attackingMonster.getName() + " appeared!";
            slowText();
        }
        else{
            /*fullFightText = "TRAINER BILLY" + " challenged you to a battle!";
            slowText();
            fullFightText = "THE BIG WILLY THE FUCKBOI BILLY sent out ";*/
        }
        textSequence.add("KNOCK HIM THE FUCK OUT " + defendingMonster.getName());
        actionSequences.add(null);
        actionSequences.add("monsterFromTheSide");
        battleLogic = new BattleLogic(attackingMonster, defendingMonster, this, numberOfAIMonsters, numberOfPlayerMonsters);
    }

    private EventHandler onFinishedWritingLetter = (t) -> {
        if(fullFightText.length() != partialText.length()){
            partialText = fullFightText.substring(0, partialText.length() + 1);
            battleText.setText(partialText);
            writeDynamicText();
        }
        else{
            isWriting = false;
            try{
                //when you finish writing one sentence, set the full fight text to the next
                fullFightText = textSequence.get(0);
                textSequence.remove(0);
            }
            catch(IndexOutOfBoundsException e){
                fullFightText = "";
            }
            try{
                switch(actionSequences.get(0)){
                    case "monsterFromTheSide":  actionToBeTaken = "sendMonsterFromSide";
                                                break;
                    case "resumeWalking":
                                            battle.endBattle();
                                            break;
                    case "changeHealth":    isChangeHealth = true;
                                            break;
                    case "battleOptions":   setBattleOptions();
                                            break;
                    case "faintMonster":    isFaint = true;
                                            break;
                    case "dropDefendingMonster":
                                            actionToBeTaken = "dropDefendingMonster";
                                            break;

                }
                actionSequences.remove(0);
            }
            catch(IndexOutOfBoundsException e){
                //do nothing
            }
            catch(NullPointerException e){
                actionSequences.remove(0);
            }
        }
    };

    public void slowText(){
        isWriting = true;
        partialText = "";
        writeDynamicText();
    }

    private void writeDynamicText(){
        KeyFrame newText = new KeyFrame(Duration.millis(textSpeed), onFinishedWritingLetter);
        textAnimation.getKeyFrames().setAll(newText);
        textAnimation.playFromStart();
    }

    @FXML
    private void onMousePressed(){
        if(isWriting){
            textSpeed = 25;
        }
    }

    @FXML
    private void onMouseRelease(){
        textSpeed = 50;
        if(actionToBeTaken == "" && !isChangeHealth && !isWriting && !isFaint && !isAnimationTakingPlace){
            slowText();
        }
        else if(actionToBeTaken == "sendMonsterFromSide" && !isChangeHealth && !isWriting && !isFaint && !isAnimationTakingPlace){
            isAnimationTakingPlace = true;
            actionToBeTaken = "";
            battle.sendOutDefendingMonsterFromTheSide(defendingMonster.getSpecies().getDefendingMonsterImage().get("width") * 2);

        }
        else if(isChangeHealth && !isAnimationTakingPlace && !isFaint){
            isAnimationTakingPlace = true;
            isChangeHealth = false;
            healthBarControllers.get(0).changeHealthAmount();
            healthBarControllers.remove(0);
        }
        else if(isFaint && !isAnimationTakingPlace && !isChangeHealth){
            isAnimationTakingPlace = true;
            isFaint = false;
            battle.faintMonster(isAIToAnimate);
        }
        else if(actionToBeTaken.equals("dropDefendingMonster") && !isAnimationTakingPlace){
            isAnimationTakingPlace = true;
            actionToBeTaken = "";
            battle.dropDefendingMonster();
        }
    }

    public void setHealthBarController(boolean isAI){
        healthBarControllers.add(battle.getHealthBarController(isAI));
    }

    public void setBattleOptions(){
        dialogueInner.getChildren().remove(0);
        dialogueInner.getChildren().add(battleOptions);
    }

    public void setTextStream(){
        dialogueInner.getChildren().remove(0);
        GridPane outer = new GridPane();
        outer.setId("battleTextGridPane");
        outer.setMaxSize(dialogueBoxWidth, dialogueBoxHeight);
        outer.setMinSize(dialogueBoxWidth, dialogueBoxHeight);
        TextFlow textflowBattle = new TextFlow();
        outer.add(textflowBattle, 0, 0);
        battleText = new Text();
        battleText.setId("battleText");
        textflowBattle.getChildren().add(battleText);
        dialogueInner.getChildren().add(outer);
    }

    @FXML
    public void onFleeClicked(){
        setTextStream();
        fullFightText = "Successfully fled your bitch ass";
        actionSequences.add("resumeWalking");
        slowText();
    }

    @FXML
    public void onFightClicked(){
        dialogueInner.getChildren().remove(0);
        HBox movesAndDescriptions = new HBox();
        movesAndDescriptions.getStyleClass().add("fightOptionsHbox");
        GridPane outer = new GridPane();
        outer.getStyleClass().addAll("battleTextGridPanes", "battleMovesGridPane");
        outer.setMaxSize((2*dialogueBoxWidth)/3, dialogueBoxHeight);
        outer.setMinSize((2*dialogueBoxWidth)/3, dialogueBoxHeight);

        moveDescription = new GridPane();
        moveDescription.getStyleClass().add("battleTextGridPanes");
        moveDescription.setMaxSize(dialogueBoxWidth/3 - 5, dialogueBoxHeight);
        moveDescription.setMinSize(dialogueBoxWidth/3 - 5, dialogueBoxHeight);

        HashMap<String, MonsterMove> monsterMoveSet = defendingMonster.getMoveSet();
        int count = 0;

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        col1.setPercentWidth(50);
        col2.setPercentWidth(50);
        row1.setPercentHeight(50);
        row2.setPercentHeight(50);
        outer.getColumnConstraints().addAll(col1, col2);
        outer.getRowConstraints().addAll(row1, row2);

        for(String moveName: monsterMoveSet.keySet()){
            Label moveLabel = new Label(moveName);
            moveLabel.setUserData(monsterMoveSet.get(moveName));
            moveLabel.setOnMouseEntered(seeMoveDescription);
            moveLabel.setOnMouseClicked(makeMove);
            moveLabel.getStyleClass().add("battleMoveDescription");
            int rowIndex = 0;
            if(count >= 2){
                rowIndex = 1;
            }
            outer.add(moveLabel, (count%2), rowIndex);
            count += 1;
        }
        movesAndDescriptions.getChildren().add(outer);
        movesAndDescriptions.getChildren().add(moveDescription);
        dialogueInner.getChildren().add(movesAndDescriptions);
    }

    @FXML
    public void onMonstersClicked(){
        battle.onMonstersClicked();
    }


    EventHandler<MouseEvent> seeMoveDescription = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            MonsterMove selectedMove = (MonsterMove)((Label)event.getSource()).getUserData();
            moveDescription.getChildren().clear();
            moveDescription.add(new Label("POW: " + Integer.toString(selectedMove.getPower())), 0, 0);
            moveDescription.add(new Label("ACC: " + Integer.toString(selectedMove.getAccuracy())), 0, 1);
            moveDescription.add(new Label(selectedMove.getType()), 0, 2);
        }
    };

    EventHandler<MouseEvent> makeMove = new EventHandler<MouseEvent>(){
        @Override
        public void handle(final MouseEvent me){
            MonsterMove selectedMove = (MonsterMove)((Label)me.getSource()).getUserData();
            battleLogic.doTurn(false, selectedMove);
            //change this
            setTextStream();
            //setBattleOptions();
        }
    };

    public void addToTextAndActionSequence(String text, String action){
        textSequence.add(text);
        actionSequences.add(action);
    }

    public void doTurnDialogue(){
        fullFightText = textSequence.get(0);
        textSequence.remove(0);
    }

    public void finishAnimation(){
        isAnimationTakingPlace = false;
    }

    public void setMonsterToAnimate(boolean monsterToAnimate){
        this.isAIToAnimate = monsterToAnimate;
    }

    public void changeMonster(Monster selectedMonster){
        battleLogic.changeMonster(selectedMonster);
    }

    public void setDefendingMonster(Monster monster){
        defendingMonster = monster;
    }

    public void setTurnOnHold(){
        battle.setTurnOnHold();
    }

    public void doTurn(){
        battleLogic.doTurn(false, null);
    }
}
