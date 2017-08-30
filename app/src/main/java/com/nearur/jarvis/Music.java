package com.nearur.jarvis;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class Music extends AppCompatActivity implements View.OnClickListener {

    ImageButton play,previous,next;
    LinearLayout imageView;
    int idx=1;
    ArrayList<String> path;
    boolean jplay=true;
    byte[] art;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    int total;
    MediaMetadataRetriever mediaMetadataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mediaPlayer=new MediaPlayer();
        seekBar=(SeekBar)findViewById(R.id.seekbarmusic);
        play=(ImageButton)findViewById(R.id.play);
        previous=(ImageButton)findViewById(R.id.previous);
        next=(ImageButton)findViewById(R.id.next);
        imageView=(LinearLayout) findViewById(R.id.layout);
        mediaMetadataRetriever=new MediaMetadataRetriever();
        path=getIntent().getStringArrayListExtra("Path");
        play.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        image();
        play(idx);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play.setImageResource(R.drawable.jpause);
                jplay=true;
                mediaPlayer.reset();
                play(++idx);
                image();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.seekTo(i*total);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    void play(int id){
        try {
            mediaPlayer.setDataSource(path.get(id));
            mediaPlayer.prepare();
            mediaPlayer.start();
            total=mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void image(){
        mediaMetadataRetriever.setDataSource(path.get(idx));
        art=mediaMetadataRetriever.getEmbeddedPicture();
        if(art!=null){
            imageView.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(art,0,art.length)));
        }else{
            imageView.setBackgroundResource(R.drawable.music);
        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id){
            case R.id.play:
                if(jplay){
                    play.setImageResource(R.drawable.jplay);
                    jplay=false;
                    mediaPlayer.pause();
                }else{
                    play.setImageResource(R.drawable.jpause);
                    jplay=true;
                    mediaPlayer.start();
                }
                break;

            case R.id.previous:
                if(idx-1<0){
                    Toast.makeText(this,"No Previous",Toast.LENGTH_SHORT).show();
                }else{
                    play.setImageResource(R.drawable.jpause);
                    jplay=true;
                    mediaPlayer.reset();
                   play(--idx);
                    image();
                }
                break;
            case R.id.next:
                if(idx+1>=path.size()){
                    Toast.makeText(this,"No Next",Toast.LENGTH_SHORT).show();
                }else{
                    play.setImageResource(R.drawable.jpause);
                    jplay=true;
                    mediaPlayer.reset();
                    play(++idx);
                    image();
                }
                break;
        }
    }
}
