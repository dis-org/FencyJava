package com.example.claudio.fency;

import android.os.Handler;
import android.widget.Toast;

public class DummyHandler extends FencyHandler{

    private long WAITING_TIME = 2500;

    private int state;
    private Handler handler;

    public DummyHandler(PracticeModeActivity context, Player player) {
        super(context, player);
        this.context = context;
        handler = new Handler();
        state = 0;
    }

    public void step( boolean success){

        if(success) state= (state+1)%4;

        ((PracticeModeActivity)context).impera(toImperium());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Allow player state change only after delay
                player.changeState(toAction());
            }
        }, WAITING_TIME);

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
                action = R.integer.HIGH_STAND;
                break;
            case 3:
                action = R.integer.LOW_STAND;
                break;
        }
        return action;
    }

    public int toImperium(){
        int action = -1;
        switch (this.state){
            case 0:
                action = R.integer.HIGH_STAND;
                break;
            case 1:
                action = R.integer.LOW_STAND;
                break;
            case 2:
                action = R.integer.LOW_ATTACK;
                break;
            case 3:
                action = R.integer.HIGH_ATTACK;
                break;
        }
        return action;
    }


}
