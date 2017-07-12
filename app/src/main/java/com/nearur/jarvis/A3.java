package com.nearur.jarvis;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class A3 extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    Switch s;
    LocationManager lc;
    NotificationManager nc;
    TextView t;
    ProgressDialog pd;
    SeekBar sk;
    int count=1;

    void init() {
        s = (Switch) findViewById(R.id.switch2);
        lc = (LocationManager) getSystemService(LOCATION_SERVICE);
        nc = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        t=(TextView)findViewById(R.id.textViewAddress);
        sk=(SeekBar)findViewById(R.id.seekBar2);
        sk.setOnSeekBarChangeListener(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Fetching Location...");
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.isChecked()) {
                    if (lc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(A3.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(A3.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(A3.this,"Please Enable Permissions",Toast.LENGTH_LONG).show();
                        }
                        pd.show();
                        lc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                if(location.getSpeed()>13.889){
                                    NotificationCompat.Builder builder=new NotificationCompat.Builder(A3.this);
                                    builder.setContentTitle("Speed Limit");
                                    builder.setContentText(location.getSpeed()+"");
                                    builder.setSmallIcon(R.drawable.j5);
                                    builder.setDefaults(Notification.DEFAULT_ALL);
                                    Notification n=builder.build();
                                    nc.notify(count++,n);
                                }
                                StringBuffer buffer=new StringBuffer();
                                double lat=location.getLatitude();
                                double lon=location.getLongitude();
                                Geocoder gc=new Geocoder(A3.this);
                                try {
                                   List<Address> a=gc.getFromLocation(lat,lon,5);
                                    for(Address z:a){
                                        for(int i=0;i<z.getMaxAddressLineIndex();i++){
                                            buffer.append(z.getAddressLine(i)+",");
                                        }
                                        buffer.append("\n");
                                    }
                                    String s=buffer.toString();
                                    t.setText(s);
                                    pd.dismiss();
                                    if(s.contains("3020")&&s.contains("Ganesh Nagar")){
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(A3.this);
                                        builder.setContentText("Home Sweet Home");
                                        builder.setContentTitle("Welcome Home Sir");
                                        builder.setSmallIcon(R.drawable.j5);
                                        builder.setDefaults(Notification.DEFAULT_ALL);
                                        Notification n=builder.build();
                                        nc.notify(count++,n);
                                    }

                                    if(s.contains("Auribises")){
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(A3.this);
                                        builder.setContentText("Back To Work");
                                        builder.setContentTitle("Welcome To Auribises Sir");
                                        builder.setSmallIcon(R.drawable.j5);
                                        builder.setDefaults(Notification.DEFAULT_ALL);
                                        Notification n=builder.build();
                                        nc.notify(count++,n);
                                    }

                                    if(s.contains("Jai")&&s.contains("Durga")&&s.contains("Communication")){
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(A3.this);
                                        builder.setContentText("Back To Earn");
                                        builder.setContentTitle("Welcome To Shop Sir");
                                        builder.setSmallIcon(R.drawable.j5);
                                        builder.setDefaults(Notification.DEFAULT_ALL);
                                        Notification n=builder.build();
                                        nc.notify(count++,n);
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
                    }
                    else{
                        Toast.makeText(A3.this,"Please Enable Gps",Toast.LENGTH_LONG).show();
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
        init();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(progress==100){
            Intent i=new Intent(this,A2.class);
            startActivity(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
