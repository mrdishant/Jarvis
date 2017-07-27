package com.nearur.jarvis;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Ai extends BroadcastReceiver {

    Context cn;
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        cn=context;
        String ac=intent.getAction();
        Intent i= new Intent(context,JarvisService.class);

        if(ac.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            i.putExtra("message","Charger Plugged in");
        }
        if(ac.equals("android.intent.action.ACTION_POWER_DISCONNECTED")){
            i.putExtra("message","Charger Plugged Out");
        }
        if(ac.equals("android.intent.action.AIRPLANE_MODE")){
            if(intent.getBooleanExtra("state",false)){
                i.putExtra("message","Airplane mode On");
            }
            else
            {
                i.putExtra("message","Airplane Mode Off");
            }

        }

        if(ac.equals("android.intent.action.SCREEN_OFF")){
            i.putExtra("message","Screen Off");

        }
        if(ac.equals("android.intent.action.SCREEN_ON")){
            i.putExtra("message","Screen On");
        }
        if(ac.equals("android.intent.action.USER_UNLOCKED")){
            i.putExtra("message","User Unlocked");
        }
        if(ac.equals("android.intent.action.BATTERY_LOW")){
            i.putExtra("message","Battery Low,Please Connect Charger");
        }

        if(ac.equals("android.provider.Telephony.SMS_RECEIVED")){
            i.putExtra("message","You have a new Text Message");
        }

        if(ac.equals("android.intent.action.PHONE_STATE")){
            i.putExtra("message","You have an Incoming Call");
        }

        if(ac.equals("android.intent.action.BOOT_COMPLETED")){
            i.putExtra("message","All systems ready to Gear Up");
        }


        context.startService(i);
    }

}
