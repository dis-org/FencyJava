package com.example.claudio.fency;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Francesco on 25/07/2017.
 */

public class PracticeModeActivity extends FencyActivity {

    private Player discipulus;
    private Player magister;
    private SensorHandler discipuliArbiter;
    private DummyHandler magistriArbiter;
    private ImageView disipuliIcon;
    private ImageView magistriIcon;
    private TextView imperium;
    private TextView approbatione;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_mode);
        cntFullScreen = findViewById(R.id.container_practice);
        goFullScreen();

        //molte cose
    }
}
