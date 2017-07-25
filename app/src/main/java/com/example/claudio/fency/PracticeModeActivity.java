package com.example.claudio.fency;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Francesco on 25/07/2017.
 */

public class PracticeModeActivity extends FencyActivity {

    private static final long ATTACK_ANIMATION_DELAY = 500; //milliseconds
    private final int vibrationLength = 100;

    private Player discipulus;
    private Player magister;
    private SensorHandler discipuliArbiter;
    private DummyHandler magistriArbiter;
    private ImageView disipuliIcon;
    private ImageView magistriIcon;
    private TextView imperium;
    private TextView approbatione;
    private boolean attackAnimationIsOn = false;
    private Handler handler;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_mode);
        cntFullScreen = findViewById(R.id.container_practice);
        goFullScreen();

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        handler = new Handler();
        discipulus = new Player(this);
        magister = new Player(this);
        disipuliIcon = (ImageView)findViewById(R.id.ivPlayerState);
        magistriIcon = (ImageView)findViewById(R.id.ivOpponentState);
        imperium = (TextView)findViewById(R.id.tvCommand);
        approbatione = (TextView)findViewById(R.id.tvCheck);

        discipuliArbiter = new SensorHandler(this, discipulus);
        discipuliArbiter.registerListeners();
        magistriArbiter = new DummyHandler(this, magister);
        magistriArbiter.step();
    }

    public void updateView(Player caller) {
        int state = caller.getState();
        ImageView icon = null;

        if(caller.equals(discipulus)){
            icon = disipuliIcon;
        }
        else if(caller.equals(magister)){
            icon = magistriIcon;
        }

        icon.setImageAlpha(255);

        if(!attackAnimationIsOn) {
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
                vibrator.vibrate(vibrationLength);

                attackAnimationIsOn = true;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Allow player image change only after delay
                        attackAnimationIsOn = false;
                    }
                }, ATTACK_ANIMATION_DELAY);
            }
        }
    }
}
