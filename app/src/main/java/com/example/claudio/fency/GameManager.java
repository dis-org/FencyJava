package com.example.claudio.fency;

/**
 * Created by Francesco on 26/07/2017.
 */

public class GameManager {

    private Player playerOne;
    private Player playerTwo;
    private int gameState;

    public GameManager(Player one, Player two){
        playerOne = one;
        playerTwo = two;
        gameState = R.integer.GAME_DRAW;
    }

    public void updateGameState(){
        //TODO
    }
}
