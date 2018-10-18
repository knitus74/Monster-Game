package com.company;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javafx.event.EventHandler;
import java.io.IOException;
import java.util.ArrayList;

public class WalkingScene{
    private Monster selectedMonster;
    @FXML
    private StackPane world;
    @FXML
    private Canvas canvas;
    private static double worldHeight;
    private static double worldWidth;
    private String keyboardInput = "";
    private Timeline gameLoop;
    private PlayerController playerController;
    private PlayerTrainer brendan;
    private ArrayList<Sprite> grassTerrain;
    private ArrayList<Sprite> grassForeground;
    private boolean isPaused = false;
    private VBox pauseMenu;
    private AnimationTimer walkUpdater;
    private GraphicsContext gc;
    private Battle battle;

    public static double getWorldHeight(){
        return worldHeight;
    }

    public static double getWorldWidth(){
        return worldWidth;
    }

    public void updateSprites(GraphicsContext gc, PlayerTrainer brendan, ArrayList<Sprite> terrain, ArrayList<Sprite> foreground){
        gc.clearRect(0, 0, worldWidth, worldHeight);
        for(Sprite s: terrain){
            s.render(gc);
        }
        brendan.render( gc );
        for(Sprite s: foreground){
            s.render(gc);
        }
        brendan.setPosition(brendan.getXPosition(), brendan.getYPosition());
    }

    public void initialize(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pauseMenu.fxml"));
            pauseMenu = loader.load();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        worldHeight = canvas.getHeight();
        worldWidth = canvas.getWidth();
        gc = canvas.getGraphicsContext2D();
        //!!!!!!!!!!!!!!!!!!!!!!!!!
        //change here to "brendans.png"
        brendan = new PlayerTrainer((worldWidth/2) - 17, (worldHeight/2) - 17, "./playerSprites/brendan_front.png");
        TileMap.setCanvasDimensions(canvas.getWidth(), canvas.getHeight());
        grassTerrain = TileMap.getGrassTerrain();
        grassForeground = TileMap.getGrassForeground();
        buildAndSetGameLoop();
        playerController = new PlayerController();

        walkUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateSprites(gc, brendan, grassTerrain, grassForeground);
            }
        };

        walkUpdater.start();
    }


    protected final void buildAndSetGameLoop() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(1);
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event){
        String code = event.getCode().toString();
        if(!isPaused) {
            if ((code.equals("RIGHT") && !getKeyboardInput().equals("RIGHT")) || (code.equals("LEFT") && !getKeyboardInput().equals("LEFT")) || (code.equals("UP") && !getKeyboardInput().equals("UP")) || (code.equals("DOWN") && !getKeyboardInput().equals("DOWN"))) {
                playerController.movePlayer(code, true, brendan, this, grassTerrain, grassForeground);
            }
        }
    }

    @FXML
    private void handleOnKeyReleased(KeyEvent event){
        String code = event.getCode().toString();
        System.out.println(code);
        if(code.equals("RIGHT") || code.equals("LEFT") || code.equals("UP") || code.equals("DOWN")){
            setKeyboardInput("");
        }
        else if(code.equals("DIGIT1")){
            if(!isPaused) {
                isPaused = true;
                showPauseMenu();
            }
            else{
                isPaused = false;
                hidePauseMenu();
            }
        }
        //simulates a battle
        else if(code.equals("DIGIT2")){
            isPaused = true;
            battleTransition1();

        }
    }

    public String getKeyboardInput(){
        return keyboardInput;
    }

    public void setKeyboardInput(String keyboardInput){
        this.keyboardInput = keyboardInput;
    }

    protected Timeline getGameLoop() {
        return gameLoop;
    }

    private void showPauseMenu(){
        world.getChildren().add(pauseMenu);
    }

    private void hidePauseMenu(){
        world.getChildren().remove(pauseMenu);
    }


    private void battleTransition1(){
        walkUpdater.stop();
        updateSprites(gc, brendan, grassTerrain, grassForeground);

        /*Main.getBackgroundMusic().changeTrack("");

        Thread music = new Thread(Main.getBackgroundMusic());
        music.start();*/

        System.out.println("Battle started");
        Rectangle flashing = new Rectangle(0,0, worldWidth, worldHeight);
        flashing.setOpacity(1.0);
        world.getChildren().add(flashing);

        Timeline flashingAnimation = new Timeline();
        KeyValue goToTransparent = new KeyValue(flashing.opacityProperty(), 0.0);
        flashingAnimation.setCycleCount(16);
        flashingAnimation.setAutoReverse(true);
        //change this to automate it
        ArrayList wildMonsterMoves = new ArrayList();
        wildMonsterMoves.add("water-boi");
        wildMonsterMoves.add("nibble");
        flashingAnimation.setOnFinished(e -> battle = new Battle(gc, world, flashing, true, new Monster(20, 10, wildMonsterMoves), brendan, this));
        flashingAnimation.getKeyFrames().setAll(new KeyFrame(Duration.millis(150), goToTransparent));
        flashingAnimation.play();
    }

    public void fadeTransition(boolean isResumeGame, String toDoAtFade){
        Rectangle fade = new Rectangle(0, 0, worldWidth, worldHeight);
        fade.setOpacity(0.0);
        world.getChildren().add(fade);
        Timeline fadeAnimation = new Timeline();
        KeyValue fadeValue = new KeyValue(fade.opacityProperty(), 1.0);
        fadeAnimation.setCycleCount(1);

        EventHandler deleteFade = (t) -> {
            world.getChildren().remove(fade);
            isPaused = false;
            if(toDoAtFade.equals("changeMonster")){
                //remove monster list
                battle.changeMonster(selectedMonster);
            }
        };

        EventHandler fadeIn = (t) -> {
            fade.setOpacity(1.0);
            if(toDoAtFade.equals("monsterListIsBattle")){
                makeMonsterList(true);
            }
            if(toDoAtFade.equals("changeMonster")){
                System.out.println(world.getChildren());
                world.getChildren().remove(world.getChildren().size()-3, world.getChildren().size()-2);
            }
          if(isResumeGame){
            walkUpdater.start();
            world.getChildren().remove(1, world.getChildren().size() - 1);
          }
          KeyValue fadeInValue = new KeyValue(fade.opacityProperty(), 0.0);
          fadeAnimation.getKeyFrames().setAll(new KeyFrame(Duration.millis(1000), deleteFade, fadeInValue));
          fadeAnimation.playFromStart();
        };

        fadeAnimation.getKeyFrames().setAll(new KeyFrame(Duration.millis(500), fadeIn, fadeValue));
        fadeAnimation.play();
    }

    public void makeMonsterList(boolean isBattle){
        int count = 0;
        GridPane monsterListGridPane = new GridPane();
        for(Monster monster: brendan.getParty()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("healthBar.fxml"));
            GridPane healthBar = null;
            HealthBar healthBarController = null;
            try {
                healthBar = loader.load();
                healthBarController = (HealthBar)loader.getController();
                healthBar.setUserData(monster);
                healthBar.setOnMouseClicked(onMonsterSelected);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            healthBarController.setHealthBarFeatures(monster, worldWidth/2 - 15, worldHeight/6 -10);
            monsterListGridPane.add(healthBar, (count%2), (count/2));
            count ++;
        }
        for(int i = count; i<6; i++){
            Rectangle placeHolder = new Rectangle();
            placeHolder.setWidth(worldWidth/2 -15);
            placeHolder.setHeight(worldHeight/6 - 10);
            monsterListGridPane.add(placeHolder, i%2, (i/2));
        }
        world.getChildren().add(world.getChildren().size() -2, monsterListGridPane);
    }

    public void onMonstersClicked(boolean isBattle){
        if(isBattle) {
            fadeTransition(false, "monsterListIsBattle");
        }
    }

    EventHandler<MouseEvent> onMonsterSelected = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent event){
            selectedMonster = (Monster)((GridPane)event.getSource()).getUserData();
            System.out.println(selectedMonster.getName());
            fadeTransition(false, "changeMonster");
        }
    };

}
