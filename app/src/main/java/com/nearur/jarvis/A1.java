package com.nearur.jarvis;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class A1 extends AppCompatActivity {
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);
        getSupportActionBar().hide();
        mp=MediaPlayer.create(this,R.raw.he);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(A1.this,A3.class);
                startActivity(i);
                overridePendingTransition(R.anim.begin,R.anim.end);
                mp.start();
                finish();
            }
        },2000);
    }
}
