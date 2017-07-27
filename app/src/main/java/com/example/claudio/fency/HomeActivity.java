package com.example.claudio.fency;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
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

        audioPlayer01 = MediaPlayer.create(this, R.raw.menu_theme);
        audioPlayer01.setLooping(true);

        audioPlayer02 = MediaPlayer.create(this, R.raw.turn_page);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.btnPractice:
                if (audioPlayer02!=null)
                    audioPlayer02.start();
                switchActivity(PracticeModeActivity.class);
                break;
        }
    }

    @Override
    protected void onPause(){
        audioPlayer01.pause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        audioPlayer01.start();
    }
}
