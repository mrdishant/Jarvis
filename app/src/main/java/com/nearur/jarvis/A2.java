
package com.nearur.jarvis;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class A2 extends AppCompatActivity implements View.OnClickListener {

    Switch s1;
    Button btn;
    CheckedTextView cstart, cend;
    LocationManager lcmanager;
    NotificationManager ncmanager;
    ProgressDialog pd;
    int count=1;
    ArrayList<Float> speed;
    String start,end;
    double userLat,userLng,venueLat,venueLng;

    void init() {
        s1 = (Switch) findViewById(R.id.switch1);
        btn = (Button) findViewById(R.id.buttonmain);
        cstart = (CheckedTextView) findViewById(R.id.checkedTextViewStart);
        cend = (CheckedTextView) findViewById(R.id.checkedTextViewEnd);
        lcmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ncmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        pd=new ProgressDialog(A2.this);
        pd.setMessage("Fetching Location...");
        s1.setOnClickListener(this);

        btn.setOnClickListener(this);
        btn.setText("Start");
        btn.setVisibility(Button.INVISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);
        init();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.switch1) {
            if( s1.isChecked()){
            if(lcmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                lcmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if(location.getSpeed()>13.889){
                            NotificationCompat.Builder builder=new NotificationCompat.Builder(A2.this);
                            builder.setContentTitle("Speed Limit");
                            builder.setContentText(location.getSpeed()+"");
                            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.j5));
                            builder.setDefaults(Notification.DEFAULT_ALL);
                            Notification n=builder.build();
                            ncmanager.notify(count++,n);
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
               btn.setVisibility(Button.VISIBLE);
            }
            else{
                Toast.makeText(this,"Please Enable Gps",Toast.LENGTH_LONG).show();
                s1.setChecked(false);
            }
        }
        else{
                cstart.setVisibility(View.INVISIBLE);
                cend.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.INVISIBLE);
            }
        }
        else if(id==R.id.buttonmain){

                if(btn.getText()=="Start"){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    else{
                        pd.show();
                        lcmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 5, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                userLat = location.getLatitude();
                                userLng = location.getLongitude();
                                StringBuffer buffer = new StringBuffer();
                                Geocoder gc = new Geocoder(A2.this);
                                try {
                                    List<Address> a = gc.getFromLocation(userLat, userLng, 5);
                                    Address z = a.get(0);
                                    for (int i = 0; i < z.getMaxAddressLineIndex(); i++) {
                                        buffer.append(z.getAddressLine(i) + ",");
                                    }
                                    buffer.append("\n");

                                    start = buffer.toString();
                                    cstart.setText(start);
                                    pd.dismiss();
                                    cend.setText("");
                                    lcmanager.removeUpdates(this);
                                    btn.setText("Stop");
                                } catch (Exception e) {

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

                }
                else if(btn.getText()=="Stop"){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    else{
                        pd.show();
                        lcmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                venueLat=location.getLatitude();
                                venueLng=location.getLongitude();
                                StringBuffer buffer = new StringBuffer();
                                Geocoder gc = new Geocoder(A2.this);
                               try {
                                    List<Address> a = gc.getFromLocation(venueLat, venueLng, 5);
                                    Address z = a.get(0);
                                    for (int i = 0; i < z.getMaxAddressLineIndex(); i++) {
                                        buffer.append(z.getAddressLine(i) + ",");
                                    }
                                    buffer.append("\n");

                                    end = buffer.toString();
                                   cend.setText(end);
                                    pd.dismiss();
                                    lcmanager.removeUpdates(this);
                                    btn.setText("Calculate");
                                } catch (Exception e) {

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

                }

                else if(btn.getText()=="Calculate"){
                    double latDistance = Math.toRadians(userLat - venueLat);
                    double lngDistance = Math.toRadians(userLng - venueLng);

                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                            + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                            * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                    float km=(float) (6371* c);
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(A2.this);
                    builder.setContentTitle("Distance");
                    builder.setContentText(km+" Kilometres");
                    builder.setSmallIcon(R.drawable.j5);
                    builder.setDefaults(Notification.DEFAULT_ALL);
                    Notification n=builder.build();
                    ncmanager.notify(count++,n);
                    btn.setText("Start");

                }
        }
    }
}
