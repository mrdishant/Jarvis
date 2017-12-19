package com.nearur.jarvis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AboutUs extends AppCompatActivity implements SensorEventListener {

    Sensor sensor;
    SensorManager sensorManager;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().hide();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        textView=(TextView)findViewById(R.id.abouttext);
        textView.setTypeface(EasyFonts.captureIt(AboutUs.this));
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        new SweetAlertDialog(this,SweetAlertDialog.CUSTOM_IMAGE_TYPE).setCustomImage(R.drawable.about)
                .setTitleText("Developer").setContentText("Dishant Mahajan\nD3CSEA1\nRoll:1507567\nPhone:9023074222").show();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor s=event.sensor;
        if(s==sensor){
            float[] val = event.values;

            if (val[0] == 0) {
                String phone = "+919023074222";
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(AboutUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AboutUs.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                    return;
                }

                startActivity(i);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(AboutUs.this,sensor);
        super.onDestroy();
    }
}
