package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class Main extends Application
{
    private static BackgroundMusic backgroundMusic;
    public static TypeEffectivenessTable typeEffectivenessTable = new TypeEffectivenessTable();

    public static void main(String[] args)
    {
        launch(args);
        //change the way you make the table
    }

    public static BackgroundMusic getBackgroundMusic(){
        return backgroundMusic;
    }

    public void start(Stage primaryStage){
        //gameWorld.initialize(primaryStage);

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            root = loader.load();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        primaryStage.setTitle("Monster game");
        primaryStage.setScene(scene);
        scene.getRoot().requestFocus();

        //backgroundMusic = new BackgroundMusic("traditionnelMusette.mp3");
        //Thread music = new Thread(backgroundMusic);
        //music.start();
        primaryStage.show();
    }
}