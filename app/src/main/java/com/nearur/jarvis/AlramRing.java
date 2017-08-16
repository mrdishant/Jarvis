package com.nearur.jarvis;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlramRing extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Button stop;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alram_ring);
        textView=(TextView)findViewById(R.id.textView4);
        stop=(Button)findViewById(R.id.buttonStop);

        textView.setText(getIntent().getStringExtra("message").toString().toUpperCase());
        if(textView.getText().toString().toLowerCase().contains("wake up")){
            mediaPlayer=MediaPlayer.create(this,R.raw.alarm);
        }else{
            mediaPlayer=MediaPlayer.create(this,R.raw.alarm2);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                finish();
            }
        });

    }


}
