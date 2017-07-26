package com.example.claudio.fency;

/**
 * Created by Francesco on 25/07/2017.
 */

public class Player {
    private FencyModeActivity activity;
    private int state;

    public Player(FencyModeActivity activity){
        this.activity = activity;
    }

    public void changeState(int to){

        state = to;
        activity.updatePlayerView(this);

        if (state==R.integer.HIGH_ATTACK || state==R.integer.LOW_ATTACK) {
            activity.game.changeState();
        }

    }

    public String toString(){
        String str = "Stato = "+ state;
        return str;
    }

    public int getState() {
        return state;
    }
}
