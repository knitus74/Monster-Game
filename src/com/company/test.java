package com.company;

import javafx.scene.input.KeyEvent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

/**
 * This application demonstrates a JavaFX 2.x Game Loop.
 * Shown below are the methods which comprise of the fundamentals to a
 * simple game loop in JavaFX:
 *
 *  <strong>initialize()</strong> - Initialize the game world.
 *  <strong>beginGameLoop()</strong> - Creates a JavaFX Timeline object containing the game life cycle.
 *  <strong>updateSprites()</strong> - Updates the sprite objects each period (per frame)
 *  <strong>checkCollisions()</strong> - Method will determine objects that collide with each other.
 *  <strong>cleanupSprites()</strong> - Any sprite objects needing to be removed from play.
 *
 * @author cdea
 */
public abstract class test {

    private String keyboardInput;
    /** The JavaFX Scene as the game surface */
    private Scene gameSurface;
    /** All nodes to be displayed in the game window. */
    private Group sceneNodes;
    /** The game loop using JavaFX's <code>Timeline</code> API.*/
    private static Timeline gameLoop;

    /** Number of frames per second. */
    private final int framesPerSecond;

    /**
     * The sprite manager.
     */
    //private final SpriteManager spriteManager = new SpriteManager();

    /**
     * Constructor that is called by the derived class. This will
     * set the frames per second, title, and setup the game loop.
     * @param fps - Frames per second.
     */
    public test(final int fps) {
        keyboardInput = "";
        framesPerSecond = fps;
    }

    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop(GraphicsContext gc, PlayerTrainer trainer) {

        final Duration oneFrameAmt = Duration.millis(1000/getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                (a) -> {
                }); // oneFrame

        // sets the game world's game loop (Timeline)
        setGameLoop(oneFrame);
    }

    public abstract void updateSprites(GraphicsContext gc, PlayerTrainer trainer, ArrayList<Sprite> terrain);

    /**
     * Initialize the game world by update the JavaFX Stage.
     * @param primaryStage
     */
    public abstract void initialize(final Stage primaryStage);

    /**Kicks off (plays) the Timeline objects containing one key frame
     * that simply runs indefinitely with each frame invoking a method
     * to update sprite objects, check for collisions, and cleanup sprite
     * objects.
     *
     */
    public void beginGameLoop() {
        getGameLoop().play();
    }


    /**
     * Returns the frames per second.
     * @return int The frames per second.
     */
    protected int getFramesPerSecond() {
        return framesPerSecond;
    }


    /**
     * The game loop (Timeline) which is used to update, check collisions, and
     * cleanup sprite objects at every interval (fps).
     * @return Timeline An animation running indefinitely representing the game
     * loop.
     */
    protected static Timeline getGameLoop() {
        return gameLoop;
    }

    /**
     * The sets the current game loop for this game world.
     * @param gameLoop Timeline object of an animation running indefinitely
     * representing the game loop.
     */
    protected static void setGameLoop(KeyFrame oneFrame) {
        test.gameLoop = new Timeline();
        Timeline gameLoop = getGameLoop();
        gameLoop.setCycleCount(1);
        gameLoop.getKeyFrames().add(oneFrame);
    }



    /**
     * Returns the JavaFX Scene. This is called the game surface to
     * allow the developer to add JavaFX Node objects onto the Scene.
     * @return
     */
    public Scene getGameSurface() {
        return gameSurface;
    }

    /**
     * Sets the JavaFX Scene. This is called the game surface to
     * allow the developer to add JavaFX Node objects onto the Scene.
     * @param gameSurface The main game surface (JavaFX Scene).
     */
    protected void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }

    /**
     * All JavaFX nodes which are rendered onto the game surface(Scene) is
     * a JavaFX Group object.
     * @return Group The root containing many child nodes to be displayed into
     * the Scene area.
     */
    public Group getSceneNodes() {
        return sceneNodes;
    }

    public String getKeyboardInput(){
        return keyboardInput;
    }

    public void setKeyboardInput(String keyboardInput){
        this.keyboardInput = keyboardInput;
    }

    /**
     * Sets the JavaFX Group that will hold all JavaFX nodes which are rendered
     * onto the game surface(Scene) is a JavaFX Group object.
     * @param sceneNodes The root container having many children nodes
     * to be displayed into the Scene area.
     */
    protected void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

}