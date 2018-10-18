package com.company;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

public class Battle {

    private boolean isTurnOnHold = false;
    private Monster monsterToChangeTo;
    GridPane AIMonsterHealthBar;
    GridPane playerMonsterHealthBar;
    HealthBar AIMonsterHealthController;
    HealthBar playerMonsterHealthController;
    Monster playerMonster;
    Monster AIMonster;
    private WalkingScene worldController;
    private final double dialogueBoxWidth;
    private final double dialogueBoxHeight;
    private StackPane world;
    private Random rand = new Random();
    private AnimationTimer battleAnimater;
    private final double battleTextHeight = 120;
    private VBox battleDialogue;
    private BattleDialogue textController = null;
    private ArrayList<Sprite> spritesToRender = new ArrayList();
    private Sprite AIMonsterSprite;
    private Sprite PlayerMonsterSprite;

    public Battle(GraphicsContext gc, StackPane world, Rectangle flash, boolean isWild, Object attacker, PlayerTrainer brendan, WalkingScene worldController){
        this.worldController = worldController;
        this.world = world;
        dialogueBoxWidth = world.getWidth() - 10;
        dialogueBoxHeight = battleTextHeight - 10;
        introAnimation(gc, flash, isWild, attacker, brendan);
    }

    public void introAnimation(GraphicsContext gc, Rectangle flash, boolean isWild, Object attacker, PlayerTrainer brendan){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("battleDialogue.fxml"));
            battleDialogue = loader.load();
            textController = (BattleDialogue)loader.getController();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        HBox innerBattleDialogue =  (HBox)battleDialogue.getChildren().get(0);
        innerBattleDialogue.setMaxSize(world.getWidth(), battleTextHeight);
        innerBattleDialogue.setMinSize(world.getWidth(), battleTextHeight);
        GridPane battleDialogueGridPane = (GridPane)innerBattleDialogue.getChildren().get(0);
        battleDialogueGridPane.setMaxSize(dialogueBoxWidth, dialogueBoxHeight);
        battleDialogueGridPane.setMinSize(dialogueBoxWidth, dialogueBoxHeight);
        //IMPORTANT!!! you have to acquire controller that is made when the fxml is loaded, not create a new one

        world.getChildren().remove(flash);

        Timeline curtainMovement = new Timeline();
        curtainMovement.setCycleCount(1);
        Rectangle curtain1 = new Rectangle(0,0, world.getWidth(), world.getHeight()/2);
        Rectangle curtain2 = new Rectangle(0,world.getHeight()/2, world.getWidth(), world.getHeight()/2);
        curtain1.setTranslateY(-curtain1.getHeight()/2);
        curtain2.setTranslateY(curtain1.getHeight()/2);
        world.getChildren().addAll(battleDialogue, curtain1, curtain2);

        KeyValue curtain1Movement = new KeyValue(curtain1.translateYProperty(), curtain1.getTranslateY() - curtain1.getHeight());
        KeyValue curtain2Movement = new KeyValue(curtain2.translateYProperty(), curtain2.getTranslateY() + curtain2.getHeight());

        gc.clearRect(0, 0, world.getWidth(), world.getHeight());
        setBattleBackground("grass", world);

        Sprite attackingPlatform = spritesToRender.get(1);
        Sprite defendingPlatform = spritesToRender.get(2);
        KeyValue attackingPlatformMovement = new KeyValue(attackingPlatform.getXPositionProperty(), world.getWidth() + attackingPlatform.getXPositionProperty().get());
        KeyValue defendingPlatformMovement = new KeyValue(defendingPlatform.getXPositionProperty(), defendingPlatform.getXPositionProperty().get() - world.getWidth());
        /*Sprite attackingPlatform = spritesToRender.get(1);
        Sprite attackingPlatformExtra = spritesToRender.get(2);
        Sprite defendingPlatform = spritesToRender.get(0);
        Sprite defendingPlatformExtra = spritesToRender.get(3);

        KeyValue attackingPlatformMovement = new KeyValue(attackingPlatform.getXPositionProperty(), 0.0);
        KeyValue attackingPlatformMovementExtra = new KeyValue(attackingPlatformExtra.getXPositionProperty(), world.getWidth());
        KeyValue defendingPlatformMovement = new KeyValue(defendingPlatform.getXPositionProperty(), 0.0);
        KeyValue defendingPlatformMovementExtra = new KeyValue(defendingPlatformExtra.getXPositionProperty(), -world.getWidth());
*/
        startBattleAnimater(gc);
        Battle thisBattle = this;

        //if wild monster, this sets the monster to be visible at the start, and move with the background
        if(isWild){
            curtainMovement.getKeyFrames().setAll(new KeyFrame(Duration.millis(2500), curtain1Movement, curtain2Movement), new KeyFrame(Duration.millis(3500), new EventHandler<ActionEvent>() {public void handle(ActionEvent event){textController.initiateBattle(1, countAvailableMonsters(brendan), isWild, attacker, brendan.getParty().get(0), thisBattle, dialogueBoxWidth, dialogueBoxHeight);}}, attackingPlatformMovement, defendingPlatformMovement));

            double monsterXPositionStart = - (0.3 * world.getWidth());
            setAttackingMonsterSprite((Monster)attacker, monsterXPositionStart);
            AIMonster = (Monster) attacker;
            KeyValue monsterIntroMovement = new KeyValue(spritesToRender.get(spritesToRender.size() - 1).getXPositionProperty(), monsterXPositionStart + world.getWidth());
            EventHandler onMonsterMoved = (t) -> {introduceHealthBar(true);};
            curtainMovement.getKeyFrames().add(new KeyFrame(Duration.millis(3500), onMonsterMoved, monsterIntroMovement));

        }

        //change this to check the monster has hp
        playerMonster = brendan.getParty().get(0);
        setDefendingMonsterSprite(playerMonster, -(10 + (playerMonster.getSpecies().getDefendingMonsterImage().get("width") * 2)) );

        setMonsterHealthBar(true, world.getWidth()/4, world.getHeight()/5);
        setMonsterHealthBar(false, world.getWidth()/4, world.getHeight()/5);

        curtainMovement.play();
        //gc.clearRect(0.0, 0.0, 512, 512);
    }


    public void setBattleBackground(String terrainType, StackPane world){
        /*Image backgroundStuff = new Image("battleBackgrounds.png");
        Sprite defendingPlatform = null;
        Sprite attackingPlatform = null;
        Sprite defendingPlatformExtra = null;
        Sprite attackingPlatformExtra = null;*/

        Sprite background = null;
        Sprite attackingPlatform = null;
        Sprite defendingPlatform = null;

        if(terrainType.equals("grass")) {
            Image backgroundStuff = new Image("battle_bg1.png");
            Image platformImage = new Image("background_island.png");
            double attackplatformOutputHeight = 56 * 1.5;
            double attackplatformOutputWidth = 96*1.5;
            double defendplatformOutputHeight = 56 * 2.0;
            double defendplatformOutputWidth = 96 * 2.0;
            background = new Sprite(0.0, 0.0, 240, 112, 0.0, 0.0, backgroundStuff, world.getHeight() - battleTextHeight, world.getWidth());
            attackingPlatform = new Sprite((- attackplatformOutputWidth)*6/7.0 - (world.getWidth()* 1/7.0), (world.getHeight() - battleTextHeight) * 1/3.0, 96, 56, 0.0, 0.0, platformImage, attackplatformOutputHeight, attackplatformOutputWidth);
            defendingPlatform = new Sprite(world.getWidth() + (world.getWidth() * 1/30.0), world.getHeight()-battleTextHeight - (defendplatformOutputHeight* 3/8), 96, 56, 0.0, 0.0, platformImage, defendplatformOutputHeight, defendplatformOutputWidth);
            /*double attackingPlatformHeightRatio = 77/(111.0);
            double defendingPlatformHeightRatio = (111-77)/111.0;
            double attackingPlatformHeight = attackingPlatformHeightRatio * (world.getHeight() - battleTextHeight);
            double defendingPlatformHeight = defendingPlatformHeightRatio * (world.getHeight() - battleTextHeight);
            System.out.println(attackingPlatformHeight);
            attackingPlatform = new Sprite(-world.getWidth(), 0.0, 240, 77, 0.0, 0.0, backgroundStuff,  attackingPlatformHeight, world.getWidth());
            defendingPlatform = new Sprite(world.getWidth(), attackingPlatformHeight, 240, 111-77, 0.0, 77, backgroundStuff, defendingPlatformHeight, world.getWidth());

            attackingPlatformExtra = new Sprite(0.0, 0.0, 112, 77, 0.0, 0.0, backgroundStuff, attackingPlatformHeight, world.getWidth());
            defendingPlatformExtra = new Sprite(0.0, attackingPlatformHeight, 240-133, 111-77, 133, 77, backgroundStuff, defendingPlatformHeight, world.getWidth());*/
        }
        spritesToRender.add(background);
        spritesToRender.add(attackingPlatform);
        spritesToRender.add(defendingPlatform);
        /*spritesToRender.add(defendingPlatform);
        spritesToRender.add(attackingPlatform);
        spritesToRender.add(attackingPlatformExtra);
        spritesToRender.add(defendingPlatformExtra);*/
    }

    private void setAttackingMonsterSprite(Monster monster, double startX){
        //Image attackerFile = new Image("generation3AttackingSprites.png");
        Image attackerFile = new Image("./monsterImages/"+ monster.getName() + ".png");
        HashMap<String, Double> attMonsterImage = monster.getSpecies().getAttackingMonsterImage();
        double attackerWidth = attMonsterImage.get("width");
        double attackerHeight = attMonsterImage.get("height");
        AIMonsterSprite = new Sprite(startX, 2* attackerHeight, attackerWidth, attackerHeight, attMonsterImage.get("x"), attMonsterImage.get("y"), attackerFile , attackerHeight * 2, attackerWidth * 2);
        spritesToRender.add(AIMonsterSprite);
    }

    public void sendOutDefendingMonsterFromTheSide(double width){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        //change this sprites to render business. monster needs to extend sprite
        KeyValue monsterMovement = new KeyValue(PlayerMonsterSprite.getXPositionProperty(), 0.2 * world.getWidth() - width/2);
        EventHandler monsterRoar = (t) -> {
            try {
                Thread.sleep(1000);
                introduceHealthBar(false);
                if(isTurnOnHold){
                    textController.doTurn();
                    isTurnOnHold = false;
                }
                else {
                    textController.setBattleOptions();
                }
                textController.finishAnimation();
            }
            catch(InterruptedException e){

            }
        };
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(700), monsterRoar, monsterMovement));
        timeline.playFromStart();
    }

    public void faintMonster(boolean isAI){
        Timeline faintAnimation = new Timeline();
        faintAnimation.setCycleCount(1);
        Sprite spriteToRemove;
        GridPane healthBar;
        if(isAI){
            spriteToRemove = AIMonsterSprite;
            healthBar = AIMonsterHealthBar;
        }
        else{
            spriteToRemove = PlayerMonsterSprite;
            healthBar = playerMonsterHealthBar;
        }
        KeyValue faintMovement = new KeyValue(spriteToRemove.getYPositionProperty(), spriteToRemove.getYPositionProperty().get() + world.getHeight());
        EventHandler onFinished = (t) -> {
            spritesToRender.remove(spriteToRemove);
            world.getChildren().remove(healthBar);
            textController.finishAnimation();
        };
        faintAnimation.getKeyFrames().setAll(new KeyFrame(Duration.millis(400), onFinished, faintMovement));
        faintAnimation.playFromStart();
    }

    private void setDefendingMonsterSprite(Monster monster, double startX){
        Image defenderFile = new Image("./monsterImages/" + monster.getName()+"_back.png");
        HashMap<String, Double> defendingMonsterImage = monster.getSpecies().getDefendingMonsterImage();
        double defenderWidth = defendingMonsterImage.get("width");
        double defenderHeight = defendingMonsterImage.get("height");
        double startY = world.getHeight() + 10 - ((defenderHeight * 2)+ battleTextHeight);
        PlayerMonsterSprite = new Sprite(startX, startY, defenderWidth, defenderHeight, defendingMonsterImage.get("x"), defendingMonsterImage.get("y"), defenderFile, defenderHeight * 2, defenderHeight * 2.5);
        spritesToRender.add(PlayerMonsterSprite);
    }

    private void startBattleAnimater(GraphicsContext gc){
        battleAnimater = new AnimationTimer(){
            public void handle(long now){
                for(Sprite sprite: spritesToRender){
                    sprite.render(gc);
                }
            }
        };
        battleAnimater.start();
    }

    public void endBattle(){
        battleAnimater.stop();
        worldController.fadeTransition(true, "");
    }

    public void setMonsterHealthBar(boolean isAI, double width, double height){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HealthBar.fxml"));
            GridPane healthBar;
            HealthBar healthBarController;
            if (isAI) {
                AIMonsterHealthBar = loader.load();
                healthBar = AIMonsterHealthBar;
                AIMonsterHealthController = loader.getController();
                healthBar.setTranslateX(-3 * world.getWidth()/4);
                healthBar.setTranslateY(-world.getHeight()/3);
                healthBarController = AIMonsterHealthController;
                AIMonsterHealthController.setHealthBarFeatures(AIMonster, width, height);
            }
            else{
                playerMonsterHealthBar = loader.load();
                healthBar = playerMonsterHealthBar;
                playerMonsterHealthController = loader.getController();
                healthBarController = playerMonsterHealthController;
                healthBar.setTranslateY(world.getHeight()/24);
                healthBar.setTranslateX(3*world.getWidth()/4);
                playerMonsterHealthController.setHealthBarFeatures(playerMonster, width, height);
            }
            healthBarController.setTextController(textController);
            world.getChildren().add(healthBar);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public HealthBar getHealthBarController(boolean isAI){
        if(isAI){
            return AIMonsterHealthController;
        }
        else{
            return playerMonsterHealthController;
        }
    }

    private void introduceHealthBar(boolean isAI){
        Timeline moveHealthBar = new Timeline();
        moveHealthBar.setCycleCount(1);
        KeyValue moveBar;
        if(isAI){
            moveBar = new KeyValue(AIMonsterHealthBar.translateXProperty(), - world.getWidth()/4);
        }
        else{
            moveBar = new KeyValue(playerMonsterHealthBar.translateXProperty(), world.getWidth()/12);
        }
        moveHealthBar.getKeyFrames().setAll(new KeyFrame(Duration.millis(500), moveBar));
        moveHealthBar.playFromStart();
    }

    private int countAvailableMonsters(Trainer player){
        ArrayList<Monster> playerParty = player.getParty();
        int count = 0;
        for(Monster monster: playerParty){
            if(monster.getHPLeft() != 0){
                count += 1;
            }
        }
        return count;
    }

    public void onMonstersClicked(){
        worldController.onMonstersClicked(true);
    }

    public void changeMonster(Monster selectedMonster){
        monsterToChangeTo = selectedMonster;
        textController.changeMonster(selectedMonster);
    }

    public void dropDefendingMonster(){
        Timeline dropMonsterTimeline = new Timeline();
        dropMonsterTimeline.setCycleCount(1);
        KeyValue monsterMovement = new KeyValue(PlayerMonsterSprite.getXPositionProperty(), PlayerMonsterSprite.getXPositionProperty().get() - 120);
        EventHandler onFinished = (t) -> {
            spritesToRender.remove(PlayerMonsterSprite);
            world.getChildren().remove(playerMonsterHealthBar);
            playerMonster = monsterToChangeTo;
            setDefendingMonsterSprite(playerMonster, -(10 + (playerMonster.getSpecies().getDefendingMonsterImage().get("width") * 2)) );
            setMonsterHealthBar(false, world.getWidth()/4, world.getHeight()/5);
            textController.setDefendingMonster(playerMonster);
            textController.finishAnimation();
        };
        dropMonsterTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), onFinished, monsterMovement));
        dropMonsterTimeline.playFromStart();
    }

    public void setTurnOnHold(){
        isTurnOnHold = true;
    }

}
