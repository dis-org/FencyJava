package com.example.claudio.fency;

/**
 * Created by Francesco on 25/07/2017.
 */

public abstract class FencyHandler {

    protected Player player;
    protected FencyModeActivity context;

    public FencyHandler(FencyModeActivity context, Player player){
        this.context = context;
        this.player = player;
    }
}
