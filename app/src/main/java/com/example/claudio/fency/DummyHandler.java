package com.example.claudio.fency;

import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Francesco on 25/07/2017.
 */

public class DummyHandler extends FencyHandler{

    private int state;
    private Handler handler;

    public DummyHandler(PracticeModeActivity context, Player player) {
        super(context, player);
        handler = new Handler();
        state = 0;
    }

    public void start(){
        Toast.makeText(context,"FINE TUTORIAL", Toast.LENGTH_LONG).show();
    }
    public void step(){

        //context.impera(toAction());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Allow player image change only after delay
                player.changeState(toAction());
                start();
            }
        }, 1000);


    }

    public int toAction(){
        int action = -1;
        switch (this.state){
            case 0:
                action = R.integer.HIGH_ATTACK;
                break;
            case 1:
                action = R.integer.LOW_ATTACK;
                break;
            case 2:
                action = R.integer.LOW_STAND;
                break;
            case 3:
                action = R.integer.HIGH_STAND;
                break;
        }
        return action;
    }

}
