package com.company;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class BackgroundMusic implements Runnable {

    private String soundtrack;
    private Media hit;
    private MediaPlayer mediaPlayer;

    public BackgroundMusic(String soundtrack) {
        changeTrack(soundtrack);
    }

    public void run() {
        mediaPlayer.play();
        mediaPlayer.setOnError(() -> System.out.println("Error : " + mediaPlayer.getError().toString()));
    }

    public void changeTrack(String soundtrack){
        try{
            mediaPlayer.stop();
        }
        catch(NullPointerException e){
            //do nothing
        }
        this.soundtrack = soundtrack;
        hit = new Media(new File(soundtrack).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
    }
}