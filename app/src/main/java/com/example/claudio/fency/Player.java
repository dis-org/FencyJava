package com.example.claudio.fency;

/**
 * Created by Francesco on 25/07/2017.
 */

public class Player {
    private PracticeModeActivity activity;
    private int state;

    public Player(PracticeModeActivity activity){
        this.activity = activity;
    }

    public void changeState(int to){
        state = to; //LOL
    }

    public String toString(){
        String str = "Stato = "+ state;
        return str;
    }
}
