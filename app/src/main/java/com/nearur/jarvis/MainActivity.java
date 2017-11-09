package com.nearur.jarvis;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AIListener, SensorEventListener {


    TextToSpeech ts;
    SensorManager sc;
    Sensor sensor;
    Vibrator vib;
    TextView t;
    String f = "";
    AlarmManager alarmManager;
    ProgressDialog p;
    TimePickerDialog timePickerDialog;
    MediaPlayer mp = new MediaPlayer();
    String name = "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ContentResolver resolver;
    int scorej = 0, scoreu = 0;
    private AIService aiService;
    String parameterString = "Hi";
    LocationManager locationManager;
    Result result;
    final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration("a3527aa431634f25a930d84b00156f82",
            AIConfiguration.SupportedLanguages.English,
            ai.api.android.AIConfiguration.RecognitionEngine.System);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sc = (SensorManager) getSystemService(SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sensor = sc.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sc.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        t = (TextView) findViewById(R.id.txt);
        p = new ProgressDialog(this);
        resolver = getContentResolver();
        p.setMessage("Listening....");

        preferences = getSharedPreferences("name", MODE_PRIVATE);
        editor = preferences.edit();

        if (!(preferences.contains("name"))) {
            name();
        }
        name = preferences.getString("name", "Sir");
        ts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                mp = MediaPlayer.create(MainActivity.this, R.raw.jarvis_at_service);
                mp.start();
            }
        });




        aiService = AIService.getService(MainActivity.this, config);
        aiService.setListener(MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{aiService.startListening();}catch (Exception e){

                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        if (preferences.contains("Silent")) {
            if (preferences.getString("Silent", "").equals("true")) {
                menu.getItem(0).setIcon(android.R.drawable.ic_lock_silent_mode);
                menu.getItem(0).setTitle("Silent On");
            } else {
                menu.getItem(0).setIcon(android.R.drawable.ic_lock_silent_mode_off);
                menu.getItem(0).setTitle("Silent Off");
            }
        } else {
            menu.getItem(0).setIcon(android.R.drawable.ic_lock_silent_mode_off);
            menu.getItem(0).setTitle("Silent Off");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            speak("");
        } else if (id == R.id.silent) {
            if (item.getTitle() == "Silent Off") {
                PackageManager pm = MainActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(MainActivity.this, Ai.class);
                pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                item.setTitle("Silent On");
                editor.putString("Silent", "true");
                editor.commit();
                item.setIcon(android.R.drawable.ic_lock_silent_mode);
            } else {
                PackageManager pm = MainActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(MainActivity.this, Ai.class);
                pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                item.setTitle("Silent Off");
                editor.putString("Silent", "false");
                editor.commit();
                item.setIcon(android.R.drawable.ic_lock_silent_mode_off);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = null;
        if (id == R.id.home) {
            i = new Intent(this, MainActivity.class);
            finish();
        } else if (id == R.id.music) {
            i = new Intent(this, Contacts.class);
        } else if (id == R.id.distance) {
            i = new Intent(this, DistanceCalculation.class);
        } else if (id == R.id.location) {
            i = new Intent(this, LocationFetch.class);
        } else if (id == R.id.social) {
            i = new Intent(this, Social.class);
        } else if (id == R.id.aboutus) {
            i = new Intent(this, AboutUs.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(i);

        return true;
    }


    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            ts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    void name() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        Button b = (Button) d.findViewById(R.id.button);
        final EditText editText = (EditText) d.findViewById(R.id.editText);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("name", editText.getText().toString());
                editor.commit();
                d.dismiss();
            }
        });
        d.setCancelable(false);
        d.show();
    }


    void insertdata(String s) {
        Date d = new Date();
        SimpleDateFormat frt = new SimpleDateFormat("M-dd-yyyy hh:mm:ss");
        ContentValues values = new ContentValues();
        values.put(Util.date, frt.format(d).toString());
        values.put(Util.thing, s);
        Uri x = resolver.insert(Util.u, values);
        if (Integer.parseInt(x.getLastPathSegment()) > 0) {
            speak("Sure i ll remember that");
        } else {
            speak("Sorry Some Error Occured Please Try Again");
        }
        Toast.makeText(MainActivity.this, "Remembered " + x.getLastPathSegment(), Toast.LENGTH_LONG).show();
    }


    String read(String s) {
        StringBuffer buffer = new StringBuffer();
        boolean found = false;
        String[] p = {"Date", "Thing"};
        Cursor c = resolver.query(Util.u, p, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String x = c.getString(1);
                if (x.contains(s.substring(s.indexOf("is") + 2))) {
                    buffer.append(c.getString(0) + "\n" + c.getString(1) + "\n");
                    found = true;
                }
                if (x.contains(s.substring(s.indexOf("are") + 3))) {
                    buffer.append(c.getString(0) + "\n" + c.getString(1) + "\n");
                    found = true;
                }
            }
        }
        c.close();
        if (!found) {
            buffer.append("Sorry , i don't know about that");
        }
        speak(buffer.toString());
        return buffer.toString();
    }


    void delete(String s) {
        String[] p = {"Date", "Thing"};
        int cr = 0;
        Cursor c = resolver.query(Util.u, p, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String x = c.getString(1);
                if (x.contains(s.substring(s.indexOf("about") + 6))) {
                    String w = Util.thing + " = '" + x + "'";
                    cr = resolver.delete(Util.u, w, null);
                    break;
                }
            }
        }
        c.close();
        if (cr > 0) {
            speak("Okay " + name + " I'll " + s);
        } else {
            speak("I guess you haven't told me " + s.substring(s.indexOf("forget") + 7));
        }
    }

    String play(String s) {
        ArrayList<String> a = new ArrayList<>();
        a.add("Stone");
        a.add("Paper");
        a.add("Scissors");
        Collections.shuffle(a);
        Collections.shuffle(a);
        Collections.shuffle(a);

        String j = a.get(0);
        if (s.equalsIgnoreCase("Stone") && j.equalsIgnoreCase("Paper")) {
            scorej++;
        } else if (s.equalsIgnoreCase("Stone") && j.equalsIgnoreCase("scissors")) {
            scoreu++;
        } else if (s.equalsIgnoreCase("Stone") && j.equalsIgnoreCase("stone")) {
            scoreu++;
            scorej++;
        } else if (s.equalsIgnoreCase("paper") && j.equalsIgnoreCase("scissors")) {
            scorej++;
        } else if (s.equalsIgnoreCase("paper") && j.equalsIgnoreCase("stone")) {
            scoreu++;
        } else if (s.equalsIgnoreCase("paper") && j.equalsIgnoreCase("paper")) {
            scoreu++;
            scorej++;
        } else if (s.equalsIgnoreCase("scissors") && j.equalsIgnoreCase("scissors")) {
            scoreu++;
            scorej++;
        } else if (s.equalsIgnoreCase("scissors") && j.equalsIgnoreCase("stone")) {
            scorej++;
        } else if (s.equalsIgnoreCase("scissors") && j.equalsIgnoreCase("paper")) {
            scoreu++;
        }
        return (j + "\nScore is : \nJarvis: " + scorej + "\n" + name + " : " + scoreu);
    }

    void selfie() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        startActivityForResult(intent, 7623);
    }

    @Override
    public void onResult(AIResponse response) {
        result = response.getResult();
        parameterString = result.getFulfillment().getSpeech();
        Toast.makeText(getApplicationContext(), parameterString, Toast.LENGTH_LONG).show();
        new task().execute(result.getResolvedQuery());
    }

    @Override
    public void onError(AIError error) {
        //speak("Some Error Occured");
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        p.show();
    }

    @Override
    public void onListeningCanceled() {
        p.dismiss();
    }

    @Override
    public void onListeningFinished() {
        p.dismiss();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor s = sensorEvent.sensor;
        if (s == sensor) {
            float[] v = sensorEvent.values;
            float x = v[0];
            float y = v[1];
            float z = v[2];
            float cal = ((x * x) + (y * y) + (z * z)) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            if (cal > 3) {
                vib.vibrate(500);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                String d = formatter.format(date);
                final StringBuffer buffer = new StringBuffer();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please Give Permissions", Toast.LENGTH_SHORT).show();
                    Intent x1=new Intent();
                    x1.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    x1.setData(Uri.fromParts("package",getPackageName(),null));
                    startActivity(x1);
                }
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                            buffer.append(location.getLatitude() + "," + location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                }, Looper.myLooper());
                buffer.append(d+" I am in Danger Please Help!!! "+" From Dishant Mahajan ,Message Sent By Jarvis");
                String num="+919023074222";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(num,null,buffer.toString(),null,null);
                sc.unregisterListener(this,sensor);
            }
    }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    class task extends AsyncTask<String,Integer,String>{
        String se,reminder;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... bundle) {

           try{
            if (bundle[0] != null  && bundle[0].length()>0) {
               se = bundle[0];

                if (parameterString.equals("location")) {
                    speak("Okay Let me Fetch it");
                    publishProgress(35);
                }

                else if(parameterString.equals("selfie")){
                     speak("Smile Please");
                     publishProgress(12);
                 }

                else if(se.toLowerCase().contains("open")){
                    String app=se.substring(se.toLowerCase().indexOf("open")+4).trim();
                    app=app.replaceAll(" ","");
                    PackageManager packageManager=getPackageManager();
                    List<PackageInfo> packages=packageManager.getInstalledPackages(0);
                    boolean found=false;

                    for(PackageInfo p:packages) {
                        if (p.packageName.contains(app.toLowerCase())) {
                            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(p.packageName);
                            speak("Opening " + app);
                            found=true;
                            startActivity(LaunchIntent);
                            break;
                        }
                    }if(!found){
                        speak(app+" either not installed or name not correct");
                    }
                }

                else if (se.contains("call")) {
                    String namep = se.toLowerCase().substring(se.toLowerCase().indexOf("call")+5).trim();
                    boolean found =false;
                    ContentResolver resolver = getContentResolver();
                    Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    Cursor c=resolver.query(uri,null,null,null,null);
                    while (c.moveToNext()) {
                        if ((c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))).toLowerCase().contains(namep)) {
                            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:"+phone));
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(MainActivity.this, "Please Give Permissions", Toast.LENGTH_SHORT).show();
                                Intent x=new Intent();
                                x.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                x.setData(Uri.fromParts("package",getPackageName(),null));
                                startActivity(x);
                            }
                            speak("Okay " + name);
                            found=true;
                            startActivity(i);
                            break;
                        }
                    }
                    if(!found){
                        speak("No Contact Found Can U please Specify");
                    }

                }

                 else if(se.toLowerCase().contains("table of")){

                    try{if(se.contains(".")){
                         float num=Float.parseFloat(se.substring(se.indexOf("of")+3,se.length()));
                        StringBuffer buffer=new StringBuffer();
                        for(int i=1;i<=10;i++){
                            buffer.append(num+" "+i+" za "+(num*i)+"\n");
                        }
                        speak(buffer.toString());

                    }else{
                         int num=Integer.parseInt(se.substring(se.indexOf("of")+3,se.length()));
                        StringBuffer buffer=new StringBuffer();
                        for(int i=1;i<=10;i++){
                            buffer.append(num+" "+i+" za "+(num*i)+"\n");
                        }
                        speak(buffer.toString());
                    }
                     }catch (Exception e){
                         speak("Please Give a Valid Number");
                     }
                 }

                else if(se.toLowerCase().contains("remember that")){
                    publishProgress(45);
                    speak("Okay i'll remember that");
                }

                else if(se.toLowerCase().contains("forget about")){
                    publishProgress(55);
                }


                else if (se.contains("morning")||se.contains("night")||se.contains("afternoon")||se.contains("evening")) {
                    Date d=new Date();
                    int h=d.getHours();
                    int day=d.getDay();
                    String today="";
                    switch (day){
                        case 0:
                            today="Sunday Enjoy your Holiday";
                            break;
                        case 1:
                            today="Monday you have to go to college";
                            break;
                        case 2:
                            today="Tuesday you have to go to college";
                            break;
                        case 3:
                            today="Wednesday you have to go to college";
                            break;
                        case 4:
                            today="Thrusday you have to go to college";
                            break;
                        case 5:
                            today="Friday you have to go to college";
                            break;
                        case 6:
                            today="Saturday Enjoy your Holiday";
                            break;
                    }
                    if(h<12){
                        f="Good Morning "+name+",Today is "+today;
                    }
                    else if(h==12){
                        f="Good Afternoon "+name;
                    }
                    else{
                        f="Good Night "+name;
                    }
                    speak(f);
                }

                else if(se.contains("time")) {
                    Calendar d =Calendar.getInstance();
                    SimpleDateFormat frmt = new SimpleDateFormat("hh:mm:ss a");
                    speak(frmt.format(d.getTime()).toString());
                }
                else if(parameterString.contains("+")){
                   try{ int a=Integer.parseInt(parameterString.substring(0,parameterString.indexOf('+')).trim());
                    int b=Integer.parseInt(parameterString.substring(parameterString.indexOf('+')+1,parameterString.length()).trim());
                    String ans= String.valueOf(a+b);
                    speak("The answer is "+ans);}catch (Exception e){
                       speak("Please Enter A Valid Number");
                   }
                }
                else if(se.contains("-")){
                    try {
                        int a = Integer.parseInt(se.substring(0, se.indexOf('-')).trim());
                        int b = Integer.parseInt(se.substring(se.indexOf('-') + 1, se.length()).trim());
                        int ans = a - b;
                        speak("The answer is " + ans);
                    }catch (Exception e){
                         speak("Please Enter A Valid Number");
                     }
                }
                else if(se.contains("*")){
                    try{int a=Integer.parseInt(se.substring(0,se.indexOf("*")).trim());
                    int b=Integer.parseInt(se.substring(se.indexOf("*")+2,se.length()).trim());
                    String ans= String.valueOf(a*b);
                    speak("The answer is "+ans);}catch (Exception e){
                        speak("Please Enter A Valid Number");
                    }
                }
                else if(se.contains("/")){
                    try{int a=Integer.parseInt(se.substring(0,se.indexOf("/")).trim());
                        int b=Integer.parseInt(se.substring(se.indexOf("/")+2,se.length()).trim());
                        String ans= String.valueOf(a/b);
                        speak("The answer is "+ans);}catch (Exception e){
                        speak("Please Enter A Valid Number");
                    }
                }
                else if(parameterString.equals("music")){
                    speak("Sure "+name);
                    Cursor c=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
                    ArrayList<String> arrayList=new ArrayList<>();
                    if(c!=null){
                        while(c.moveToNext()){
                            arrayList.add(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        }
                    }c.close();
                    Intent i=new Intent(MainActivity.this,Music.class);
                    Collections.shuffle(arrayList);
                    i.putExtra("Path",arrayList);
                    startActivity(i);
                }
                else if(se.toLowerCase().contains("sms")){
                    String namep = se.toLowerCase().substring(se.toLowerCase().indexOf("sms")+4,se.toLowerCase().indexOf("that")).trim();
                    boolean found =false;
                    ContentResolver resolver = getContentResolver();
                    Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    Cursor c=resolver.query(uri,null,null,null,null);
                    while (c.moveToNext()) {
                        if ((c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))).toLowerCase().contains(namep)) {
                            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phone,null,se.substring(se.indexOf("that")+4,se.length()),null,null);
                            speak("Okay " + name);
                            found=true;
                            break;
                        }
                        if ((c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE))).toLowerCase().contains(namep)) {
                            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phone,null,se.substring(se.indexOf("that")+4,se.length()),null,null);speak("Okay " + name);
                            found=true;
                            break;
                        }
                    }
                    if(!found){
                        speak("No Contact Found Can U please Specify");
                    }

                }

                else if(se.toLowerCase().contains("where is")||se.toLowerCase().contains("where are")){
                   se= read(se);
                }

                else if(se.toLowerCase().equals("stone")||se.toLowerCase().equals("paper")||se.toLowerCase().equals("scissors")){
                    se= play(se);
                    speak(se);
                }

                else if(se.toLowerCase().contains("alarm")){
                    if(se.toLowerCase().contains("minutes")||se.toLowerCase().contains("minute")){
                        int s=Integer.parseInt(se.substring(se.toLowerCase().indexOf("for")+3,se.toLowerCase().indexOf("minu")).trim());
                        Calendar c=Calendar.getInstance();
                        if(c.get(Calendar.MINUTE)+s<60){
                            c.set(Calendar.MINUTE,s+c.get(Calendar.MINUTE));
                            c.set(Calendar.SECOND,00);
                        }else{
                            c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY)+1);
                            c.set(Calendar.MINUTE,s-(60-c.get(Calendar.MINUTE)));
                            c.set(Calendar.SECOND,00);
                        }
                        Intent intent=new Intent(MainActivity.this,AlramRing.class);
                        intent.putExtra("message","Wake Up "+name);
                        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),(int)(Math.random()*100),intent,0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
                        speak( "Alarm set for "+s+" minutes from now");
                    }

                    else{
                        speak("Can U please Enter?");
                        publishProgress(15);
                    }

                }
                else if(se.toLowerCase().contains("remind me to")){
                    reminder=se.substring(se.toLowerCase().indexOf("to")+2);
                    speak("When Do u want me to remind you ?");
                    publishProgress(10);
                }


                else if(se.toLowerCase().contains("silent mode off")) {
                    PackageManager pm  = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, Ai.class);
                    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    speak("I will be Speaking");
                }
                else if(se.toLowerCase().contains("silent mode on")) {
                    PackageManager pm  = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, Ai.class);
                    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    speak("I will be Silent");
                }
                else if(se.toLowerCase().contains ("game")) {
                   Intent i=new Intent(MainActivity.this,TicTacToe.class);
                   startActivity(i);
                }

                else{
                   speak(parameterString);
                }
            }
            else{
                speak("Kyaaaa ?");
            }
        }
        catch (Exception e){
            speak("Sorry"+name+" I didnot get that"+" beacause Error: "+e.getMessage());
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }
            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]==10){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Please Select");
                String [] strings={"EveryDay","Specific Date"};
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                speak("At What Time ?");
                                Calendar c=Calendar.getInstance();
                                int h=c.get(Calendar.HOUR_OF_DAY);
                                int m=c.get(Calendar.MINUTE);
                                TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        Intent intent=new Intent("remind");
                                        intent.putExtra("hour",i);
                                        intent.putExtra("minute",i1);
                                        intent.putExtra("message",name+" it's time to "+reminder);
                                        sendBroadcast(intent);
                                        speak("Okay "+name+" i will remind you");
                                    }
                                };
                                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this,t,h,m,false);
                                timePickerDialog.show();
                                break;
                            case 1:
                                Calendar c1=Calendar.getInstance();
                                DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, final int year, final int month, final int date) {
                                        speak("At What Time ?");
                                        Calendar c=Calendar.getInstance();
                                        int h=c.get(Calendar.HOUR_OF_DAY);
                                        int m=c.get(Calendar.MINUTE);
                                        TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                                Intent intent=new Intent("remind");
                                                intent.putExtra("hour",i);
                                                intent.putExtra("minute",i1);
                                                intent.putExtra("message",name+" it's time to "+reminder);
                                                intent.putExtra("date",date);
                                                intent.putExtra("month",month);
                                                intent.putExtra("year",year);
                                                sendBroadcast(intent);
                                                speak("Okay "+name+" i will remind you");
                                            }
                                        };
                                        TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this,t,h,m,false);
                                        timePickerDialog.show();


                                    }
                                };
                                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this,d,c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.DATE));
                                datePickerDialog.show();
                                break;
                        }
                    }
                });
                builder.create().show();
            }else if(values[0]==15){
                Calendar c=Calendar.getInstance();
                int hh=c.get(Calendar.HOUR_OF_DAY);
                int mm=c.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar c1=Calendar.getInstance();
                        int h=c1.get(Calendar.HOUR_OF_DAY);
                        int m=c1.get(Calendar.MINUTE);
                        if(i<=h){
                            c1.set(Calendar.DATE,c1.get(Calendar.DATE)+1);
                            c1.set(Calendar.HOUR_OF_DAY, i);
                            c1.set(Calendar.MINUTE, i1);
                            c1.set(Calendar.SECOND, 00);
                            i=i+(23-h);
                            if(i1>=m){
                                i1=i1-m;
                            }
                            else{
                                i=i-1;
                                i1=m-(m-i1);
                            }
                        }else {
                            c1.set(Calendar.HOUR_OF_DAY, i);
                            c1.set(Calendar.MINUTE, i1);
                            c1.set(Calendar.SECOND, 00);
                            i=i-h;
                            if(i1>=m){
                                i1=i1-m;
                            }
                            else{
                                i=i-1;
                                i1=m-(m-i1);
                            }
                        }
                        Intent intent=new Intent(MainActivity.this,AlramRing.class);
                        intent.putExtra("message","Wake Up "+name);
                        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),(int)(Math.random()*100),intent,0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,c1.getTimeInMillis(),pendingIntent);
                        speak("Alarm set for "+i+" Hours and "+i1+" minutes from now");
                        Toast.makeText(getApplicationContext(),"Alarm set for "+i+" Hours and "+i1+" minutes from now",Toast.LENGTH_LONG).show();
                    }
                };

                timePickerDialog = new TimePickerDialog(MainActivity.this,t,hh,mm,true);
                timePickerDialog.show();
            }else if(values[0]==35){
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Please Give Permissions", Toast.LENGTH_SHORT).show();
                    Intent x=new Intent();
                    x.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    x.setData(Uri.fromParts("package",getPackageName(),null));
                    startActivity(x);
                }
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        StringBuffer buffer = new StringBuffer();
                        Geocoder gc = new Geocoder(MainActivity.this);

                        List<Address> a = null;
                        try {
                            a = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                            buffer.append(location.getLatitude()+","+location.getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < a.get(0).getMaxAddressLineIndex(); i++) {
                            buffer.append(a.get(i).getAddressLine(i) + ",");
                        }
                        buffer.append("\n");
                        speak(name + " Your Location is " + buffer.toString());
                        t.setText(" Your Location is " + buffer.toString());
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                },Looper.myLooper());
            }else if(values[0]==45){
                insertdata(se);
            }else if(values[0]==55){
                delete(se);
            }else if(values[0]==12){
                selfie();
            }
        }

        @Override
        protected void onPostExecute(String aVoid) {
            t.setText(se.toUpperCase());
            super.onPostExecute(aVoid);
        }
    }

}
