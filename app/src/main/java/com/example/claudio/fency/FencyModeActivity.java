package com.example.claudio.fency;

import android.os.Bundle;
import android.os.Vibrator;

/**
 * Created by Francesco on 26/07/2017.
 */

public abstract class FencyModeActivity extends FencyActivity {
    private final int vibrationLength = 100;

    protected Player user;
    protected Player opponent;
    protected SensorHandler sensorHandler;
    protected Game game;
    protected Vibrator vibrator;
    protected boolean userAttacking = false;
    protected boolean opponentAttacking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        user = new Player(this);
        opponent = new Player(this);
        sensorHandler = new SensorHandler(this, user);
        sensorHandler.registerListeners();
        game = new Game(this,user,opponent);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
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

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    public abstract void updatePlayerView(Player caller);

    public void updateGameView(int gameState){
        if (gameState == R.integer.GAME_DRAW)
            vibrator.vibrate(vibrationLength);
    }
}
