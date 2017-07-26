package com.example.claudio.fency;

public class Game {

    private Player playerOne;
    private Player playerTwo;
    private int state;

    public Game(Player one, Player two){
        playerOne = one;
        playerTwo = two;
        state = R.integer.GAME_DRAW;
    }

    public void changeState(){
        int s1 = playerOne.getState();
        int s2 = playerTwo.getState();

        if(s1==R.integer.HIGH_ATTACK && s2==R.integer.LOW_STAND ||
            s1==R.integer.LOW_ATTACK && s2==R.integer.HIGH_STAND){
            state = R.integer.GAME_P1;
        }
        else if(s2==R.integer.HIGH_ATTACK && s1==R.integer.LOW_STAND ||
                s2==R.integer.LOW_ATTACK && s1==R.integer.HIGH_STAND){
            state = R.integer.GAME_P2;
        }
        else {
            state = R.integer.GAME_DRAW;
        }


    }
}
