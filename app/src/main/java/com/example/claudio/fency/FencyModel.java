package com.example.claudio.fency;

/**
 * Created by Francesco on 26/07/2017.
 */

public abstract class FencyModel {

    protected FencyModeActivity activity;
    protected int state;

    public FencyModel(FencyModeActivity activity){
        this.activity = activity;
        state = 0;
    }
}
