package com.nearur.jarvis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm set for minutes from now", Toast.LENGTH_SHORT).show();
        mp=MediaPlayer.create(context,R.raw.jarvis_at_service);
        mp.start();
    }
}
