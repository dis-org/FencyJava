package com.example.claudio.fency;

import android.os.Bundle;
import android.view.View;

public class HomeActivity extends FencyActivity implements View.OnClickListener{

    private View toPracticeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cntFullScreen = findViewById(R.id.container_home);
        goFullScreen();

        toPracticeMode = findViewById(R.id.btnPractice);
        toPracticeMode.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.btnPractice:
                switchActivity(PracticeModeActivity.class);
                break;
        }
    }


}
