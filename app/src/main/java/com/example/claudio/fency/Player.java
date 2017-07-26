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
        //if(state != to){
            state = to;

          //  activity.updateIcon(this);
        //}


    }

    public String toString(){
        String str = "Stato = "+ state;
        return str;
    }

    public int getState() {
        return state;
    }
}
