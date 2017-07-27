package com.nearur.jarvis;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class A3 extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, SensorEventListener {
    Switch s;
    LocationManager lc;
    NotificationManager nc;
    SensorManager sc;
    Sensor sensor,sensor2;
    TextView t;
    ProgressDialog pd;
    SeekBar sk;
    int count = 1;
    BroadcastReceiver br;
    Vibrator vib;
    StringBuffer buffer =new StringBuffer();

    void init() {
        s = (Switch) findViewById(R.id.switch2);
        lc = (LocationManager) getSystemService(LOCATION_SERVICE);
        nc = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        t = (TextView) findViewById(R.id.textViewAddress);
        sk = (SeekBar) findViewById(R.id.seekBar2);
        sk.setOnSeekBarChangeListener(this);
        vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching Location...");
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.isChecked()) {
                    if (lc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(A3.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(A3.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(A3.this, "Please Enable Permissions", Toast.LENGTH_LONG).show();
                        }
                        pd.show();
                        lc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                if (location.getSpeed() > 13.889) {
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(A3.this);
                                    builder.setContentTitle("Speed Limit");
                                    builder.setContentText(location.getSpeed() + "");
                                    builder.setSmallIcon(R.drawable.j5);
                                    builder.setDefaults(Notification.DEFAULT_ALL);
                                    Notification n = builder.build();
                                    nc.notify(count++, n);
                                }

                                double lat = location.getLatitude();
                                double lon = location.getLongitude();
                                Geocoder gc = new Geocoder(A3.this);
                                try {
                                    List<Address> a = gc.getFromLocation(lat, lon, 5);
                                    for (Address z : a) {
                                        for (int i = 0; i < z.getMaxAddressLineIndex(); i++) {
                                            buffer.append(z.getAddressLine(i) + ",");
                                        }
                                        buffer.append("\n");
                                    }
                                    String s = buffer.toString();
                                    t.setText(s);
                                    pd.dismiss();
                                    if (s.contains("3020") && s.contains("Ganesh Nagar")) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(A3.this);
                                        builder.setContentText("Home Sweet Home");
                                        builder.setContentTitle("Welcome Home Sir");
                                        builder.setSmallIcon(R.drawable.j5);
                                        builder.setDefaults(Notification.DEFAULT_ALL);
                                        Notification n = builder.build();
                                        nc.notify(count++, n);
                                    }

                                    if (s.contains("Krishna")&&s.contains("Nagar")&&s.contains("Sardar")) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(A3.this);
                                        builder.setContentText("Back To Work");
                                        builder.setContentTitle("Welcome To Auribises Sir");
                                        builder.setSmallIcon(R.drawable.j5);
                                        builder.setDefaults(Notification.DEFAULT_ALL);
                                        Notification n = builder.build();
                                        nc.notify(count++, n);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });
                    } else {
                        Toast.makeText(A3.this, "Please Enable Gps", Toast.LENGTH_LONG).show();
                        s.setChecked(false);
                    }
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3);
        sc = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sc.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensor2=sc.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_UNLOCKED);
        BroadcastReceiver mReceiver = new Ai();
        registerReceiver(mReceiver, filter);
        sc.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        sc.registerListener(this,sensor2,SensorManager.SENSOR_DELAY_NORMAL);
        init();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress == 100) {
            Intent i = new Intent(this, A2.class);
            startActivity(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
            startActivity(i);}}
            else if(s==sensor2){
                    float[] v=event.values;
                    float x=v[0];
                    float y=v[1];
                    float z=v[2];
            float cal = ((x*x)+(y*y)+(z*z)) / (SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH);
            if(cal>3){
                vib.vibrate(500);
                            Date date=new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                            String d=formatter.format(date);
                            buffer.append(d+" From Dishant Mahajan ,Message Sent By Jarvis");
                            String num="+919023074222";
                            String msg="";
                            msg=buffer.toString();
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("sms:"+num));
                            intent.putExtra("sms_body",msg);
                            startActivity(intent);
                            sc.unregisterListener(this,sensor2);


                }
            }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sc.unregisterListener(this);
    }
}
