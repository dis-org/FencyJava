package com.example.claudio.fency;

import android.os.Bundle;

/**
 * Created by Francesco on 26/07/2017.
 */

public abstract class FencyModeActivity extends FencyActivity {
    protected Player user;
    protected Player opponent;
    protected SensorHandler sensorHandler;
    protected GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        user = new Player(this);
        opponent = new Player(this);
        sensorHandler = new SensorHandler(this, user);
        gameManager = new GameManager(user,opponent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorHandler.unregisterListeners();
    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorHandler.registerListeners();
    }

    public abstract void updatePlayerView(Player caller);

    public abstract void updateGameView(int gameState);
}
