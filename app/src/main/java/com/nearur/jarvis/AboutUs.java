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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity implements SensorEventListener {

    Sensor sensor;
    SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setTitle("About US");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
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
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please Give Permissions", Toast.LENGTH_SHORT).show();
                    Intent x=new Intent();
                    x.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    x.setData(Uri.fromParts("package",getPackageName(),null));
                    startActivity(x);
                }
                startActivity(i);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
