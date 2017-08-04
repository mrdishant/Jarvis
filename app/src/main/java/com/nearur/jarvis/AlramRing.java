package com.nearur.jarvis;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AlramRing extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alram_ring);
        mediaPlayer=MediaPlayer.create(this,R.raw.jarvis_at_service);
        mediaPlayer.start();
        //mediaPlayer.setLooping(true);
        stop=(Button)findViewById(R.id.buttonStop);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
            }
        });

    }
}
