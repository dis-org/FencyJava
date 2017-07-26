package com.example.claudio.fency;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Francesco on 25/07/2017.
 */

public class PracticeModeActivity extends FencyModeActivity {

    private static final long ATTACK_ANIMATION_DELAY = 500; //milliseconds


    private DummyHandler arbiter;
    private ImageView discipuli;
    private ImageView magistri;
    private TextView imperium;
    private TextView approbatio;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_mode);
        cntFullScreen = findViewById(R.id.container_practice);
        handler = new Handler();

        discipuli = (ImageView)findViewById(R.id.ivPlayerState);
        magistri = (ImageView)findViewById(R.id.ivOpponentState);
        imperium = (TextView)findViewById(R.id.tvCommand);
        approbatio = (TextView)findViewById(R.id.tvCheck);
        arbiter = new DummyHandler(this, opponent);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // starting delay
                arbiter.step();
            }
        }, 3000);

    }

    @Override
    public void updatePlayerView(Player caller) {

        int state = caller.getState();
        ImageView icon = null;
        boolean attackOn = true;

        if(caller.equals(user)){
            icon = discipuli;
            attackOn = userAttacking;
        }
        else if(caller.equals(opponent)){
            icon = magistri;
            attackOn = opponentAttacking;
        }

        icon.setImageAlpha(255);

        if(!attackOn) {
            //change player img
            switch (state) {
                case R.integer.HIGH_STAND:
                    icon.setImageResource(R.mipmap.fency_high_stand);
                    break;
                case R.integer.LOW_STAND:
                    icon.setImageResource(R.mipmap.fency_low_stand);
                    break;
                case R.integer.HIGH_ATTACK:
                    icon.setImageResource(R.mipmap.fency_high_attack);
                    break;
                case R.integer.LOW_ATTACK:
                    icon.setImageResource(R.mipmap.fency_low_attack);
                    break;
                case R.integer.INVALID:
                    icon.setImageAlpha(80);
                    break;
            }
            if (state==R.integer.HIGH_ATTACK || state==R.integer.LOW_ATTACK){

                if(caller.equals(user)){
                    userAttacking = true;
                }
                else if(caller.equals(opponent)){
                    opponentAttacking = true;
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Allow players image change only after delay
                        userAttacking = false;
                        opponentAttacking = false;
                    }
                }, ATTACK_ANIMATION_DELAY);
            }
        }
    }


    @Override
    public void updateGameView(int gameState){
        super.updateGameView(gameState);

        if(gameState == 0){

        }
    }

    public void impera(int actum){
    }
}
