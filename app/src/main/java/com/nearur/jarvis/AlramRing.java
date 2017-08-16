package com.nearur.jarvis;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlramRing extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Button stop;
    TextView textView;
    TextToSpeech ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alram_ring);
        textView=(TextView)findViewById(R.id.textView4);
        stop=(Button)findViewById(R.id.buttonStop);
        ts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        textView.setText(getIntent().getStringExtra("message").toString().toUpperCase());
        if(textView.getText().toString().toLowerCase().contains("wake up")){
            mediaPlayer=MediaPlayer.create(this,R.raw.alarm);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();


            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.stop();
                    finish();
                }
            });
        }else{
            speak(textView.getText().toString());
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }


    }


    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            ts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
