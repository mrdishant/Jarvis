package com.nearur.jarvis;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.os.Environment;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecognitionListener {

    boolean b = false;
    HashMap<String, String> a;
    SpeechRecognizer speechRecognizer;
    TextToSpeech ts;
    TextView t;
    String f = "";
    AlarmManager alarmManager;
    ProgressDialog p;
    TimePickerDialog timePickerDialog;
    MediaPlayer mp = new MediaPlayer();
    String name = "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<String> poem = new ArrayList<>(), greet = new ArrayList<>();
    ContentResolver resolver;
    int scorej = 0, scoreu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        a = new HashMap<>();
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
        a.put("shutdown", "Okay " + name);
        a.put("who is your father", "Mr Tony Stark");
        a.put("who are you", "I am Jarvis, Personal Assistant of Mr Dishant Mahajan");
        a.put("what this means", "It stands for Just A Rather Very Intelligent System");

        greet.add("Hi ");
        greet.add("Hello ");
        greet.add("Hey ");
        greet.add("Namaste ");
        greet.add("Sat Shri Akaal ");
        greet.add("Jai Shri Krishna ");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        poem.add("Twinkle, twinkle, little star,\n" +
                "How I wonder what you are!\n" +
                "Up above the world so high,\n" +
                "Like a diamond in the sky.\n");

        poem.add("Humpty Dumpty sat on a wall,\n" +
                "Humpty Dumpty had a great fall;\n" +
                "All the king's horses and all the king's men\n" +
                "Couldn't put Humpty together again.");

        poem.add("Ring-a-ring-a roses, \n" +
                "\n" +
                "A pocket full of posies, \n" +
                "\n" +
                "Ashes! Ashes! \n" +
                "\n" +
                "We all fall down. \n" +
                "\n" +
                "Ring-a-ring-a roses, \n" +
                "\n" +
                "A pocket full of posies, \n" +
                "\n" +
                "A-tishoo! A-tishoo! \n" +
                "\n" +
                "We all fall down.");
        poem.add("Teri Aankhon Ki, Namkeen Mastiyaan....\n" +
                "Teri Hansi Ki, Beparwaah Gustakhiyaan....\n" +
                "Teri Zulfon Ki, Lehraati Angdaiyaan....\n" +
                "Nahi Bhoolunga Main....\n" +
                "Jab Tak Hai Jaan !!!\n" +
                "Jab Tak Hai Jaan !!!");
        poem.add("Rain rain go away,\n" +
                "Come again another day.\n" +
                "Little Johnny wants to play;\n" +
                "Rain, rain, go to Spain,\n" +
                "Never show your face again!\n");
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            speak("");
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
            i = new Intent(this, A2.class);
        } else if (id == R.id.location) {
            i = new Intent(this, A3.class);
        } else if (id == R.id.social) {
            i = new Intent(this, Social.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(i);

        return true;
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        p.show();
    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        p.dismiss();
        speechRecognizer.stopListening();
    }

    @Override
    public void onError(int i) {
        speak("");
        speechRecognizer.stopListening();
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> re = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (re != null && re.size() > 0) {
            String se = re.get(0);
            if (se.contains("location")) {
                t.setText(se.toString());
                speak("Okay Let me Fetch it");
                final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Please Give Permission", Toast.LENGTH_LONG).show();
                }
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        StringBuffer buffer = new StringBuffer();
                        Geocoder gc = new Geocoder(MainActivity.this);

                        List<Address> a = null;
                        try {
                            a = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < a.get(0).getMaxAddressLineIndex(); i++) {
                            buffer.append(a.get(0).getAddressLine(i) + ",");
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
                }, Looper.myLooper());
            } else {
                new task().execute(bundle);
            }
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            ts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    void name(){
        final Dialog d=new Dialog(this);
        d.setContentView(R.layout.dialog);
        Button b=(Button)d.findViewById(R.id.button);
        final EditText editText=(EditText)d.findViewById(R.id.editText);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("name",editText.getText().toString());
                editor.commit();
                d.dismiss();
            }
        });
        d.setCancelable(false);
        d.show();
    }


    void insertdata(String s){
        Date d=new Date();
        SimpleDateFormat frt=new SimpleDateFormat("dd/MM/yyyy");
        ContentValues values=new ContentValues();
        values.put(Util.date,frt.format(d).toString());
        values.put(Util.thing,s);
        Uri x=resolver.insert(Util.u,values);
        Toast.makeText(MainActivity.this,"Done"+x.getLastPathSegment(),Toast.LENGTH_LONG).show();
    }


    String read(String s){
        StringBuffer buffer=new StringBuffer();
        boolean found=false;
        String[] p={"Date","Thing"};
        Cursor c=resolver.query(Util.u,p,null,null,null);
        if(c!=null){
        while (c.moveToNext()){
            String x=c.getString(1);
            if(x.contains(s.substring(s.indexOf("is")+2))){
                buffer.append (c.getString(0)+"\n"+c.getString(1)+"\n");
                found=true;
                }
            if(x.contains(s.substring(s.indexOf("are")+3))){
                buffer.append (c.getString(0)+"\n"+c.getString(1)+"\n");
                found=true;
            }
            }
        }c.close();
        if (!found){
            buffer.append("Sorry , i don't know about that");
        }
        speak(buffer.toString());
        return buffer.toString();
    }


    void delete(String s){
        String[] p={"Date","Thing"};
        int cr=0;
        Cursor c=resolver.query(Util.u,p,null,null,null);
        if(c!=null){
            while (c.moveToNext()){
                String x=c.getString(1);
                if(x.contains(s.substring(s.indexOf("about")+6))){
                    String w=Util.thing+" = '"+x+"'";
                    cr=resolver.delete(Util.u,w,null);
                    break;
                }
                }
            }
            c.close();
        if(cr>0){
            speak("Okay "+name+" I'll "+s);
        }
        else{
            speak("I guess you haven't told me "+s.substring(s.indexOf("forget")+7));
        }
    }

    String  play(String s){
            ArrayList<String> a = new ArrayList<>();
            a.add("Stone");
            a.add("Paper");
            a.add("Scissors");
            Collections.shuffle(a);
            Collections.shuffle(a);
            Collections.shuffle(a);

            String j=a.get(0);
        if(s.equalsIgnoreCase("Stone")&& j.equalsIgnoreCase("Paper")){
            scorej++;
        }
        else if(s.equalsIgnoreCase("Stone")&&j.equalsIgnoreCase("scissors")){
            scoreu++;
        }

        else if(s.equalsIgnoreCase("Stone")&&j.equalsIgnoreCase("stone")){
            scoreu++;
            scorej++;
        }

        else if(s.equalsIgnoreCase("paper")&&j.equalsIgnoreCase("scissors")){
            scorej++;
        }

        else if(s.equalsIgnoreCase("paper")&&j.equalsIgnoreCase("stone")){
            scoreu++;
        }
        else if(s.equalsIgnoreCase("paper")&&j.equalsIgnoreCase("paper")){
            scoreu++;
            scorej++;
        }
        else if(s.equalsIgnoreCase("scissors")&&j.equalsIgnoreCase("scissors")){
            scoreu++;
            scorej++;
        }

        else if(s.equalsIgnoreCase("scissors")&&j.equalsIgnoreCase("stone")){
            scorej++;
        }
        else if(s.equalsIgnoreCase("scissors")&&j.equalsIgnoreCase("paper")){
            scoreu++;
        }
        return (j+"\nScore is :"+scorej+" / "+scoreu);
    }

    void selfie(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"helloJarvis.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        startActivityForResult(intent, 7623);
    }



    class task extends AsyncTask<Bundle,Integer,Void>{
        String se,reminder;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Bundle... bundle) {
            if(!b){
                Looper.prepare();
                b=true;
            }

           try{ ArrayList<String> re = bundle[0].getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (re != null  && re.size()>0) {
               se = re.get(0);

                if (a.containsKey(se.toLowerCase())) {
                    speak(a.get(se));
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
                                Toast.makeText(MainActivity.this, "Please Give Permission", Toast.LENGTH_SHORT).show();
                            }
                            speak("Okay " + name);
                            found=true;
                            startActivity(i);
                        }
                    }
                    if(!found){
                        speak("No Contact Found Can U please Specify");
                    }

                }


                else if(se.equalsIgnoreCase("jarvis")){
                    speak("Yes "+name+",How can i help U ?");
                }

                else if(se.toLowerCase().contains("nice jarvis")){
                    speak("Thank You "+name+"!!");
                }

                else if(se.toLowerCase().contains("table of")){
                    int num=Integer.parseInt(se.substring(se.indexOf("of")+3,se.length()));
                    StringBuffer buffer=new StringBuffer();
                    for(int i=1;i<=10;i++){
                        buffer.append(num+" "+i+" za "+(num*i)+"\n");
                    }
                    speak(buffer.toString());
                }

                else if(se.contains("poem")){
                    Collections.shuffle(poem);
                    if(poem.get(0).contains("Teri Aankhon Ki, Namkeen Mastiyaan")){
                        mp=MediaPlayer.create(MainActivity.this,R.raw.poem);
                        mp.start();
                    }else{
                        speak(poem.get(0));
                    }
                }

                else if(se.equalsIgnoreCase("English Alphabets")){
                    speak("A for Apple\n"+"B for Ball\n"+"C for Cat\n"+"D for Dog\n");

                }
                else if(se.toLowerCase().contains("remember that")){
                    insertdata(se);
                    speak("Okay i'll remember that");
                }

                else if(se.toLowerCase().contains("forget about")){
                    delete(se);
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
                else if(se.toLowerCase().contains("sat sri akal")||se.toLowerCase().contains("hi")||se.toLowerCase().contains("namaste")||se.toLowerCase().contains("hello")||se.toLowerCase().contains("hey")||se.toLowerCase().contains("jai shri krishna")){
                    Collections.shuffle(greet);
                    speak(greet.get(0)+name);
                }
                else if(se.contains("date")){
                    Calendar c=Calendar.getInstance();
                    SimpleDateFormat frmt=new SimpleDateFormat("dd/MM/yyyy");
                    speak(frmt.format(c.getTime()).toString());
                }
                else if(se.contains("time")) {
                    Calendar d =Calendar.getInstance();
                    SimpleDateFormat frmt = new SimpleDateFormat("hh:mm:ss a");
                    speak(frmt.format(d.getTime()).toString());
                }
                else if(se.contains("+")){
                    int a=Integer.parseInt(se.substring(0,se.indexOf('+')).trim());
                    int b=Integer.parseInt(se.substring(se.indexOf('+')+1,se.length()).trim());
                    String ans= String.valueOf(a+b);
                    speak("The answer is "+ans);
                }
                else if(se.contains("-")){
                    int a=Integer.parseInt(se.substring(0,se.indexOf('-')).trim());
                    int b=Integer.parseInt(se.substring(se.indexOf('-')+1,se.length()).trim());
                    int ans=a-b;
                    speak("The answer is "+ans);
                }
                else if(se.contains("into")){
                    int a=Integer.parseInt(se.substring(0,se.indexOf("into")).trim());
                    int b=Integer.parseInt(se.substring(se.indexOf("into")+5,se.length()).trim());
                    String ans= String.valueOf(a*b);
                    speak("The answer is "+ans);
                }
                else if(se.contains("/")){
                    int a=Integer.parseInt(se.substring(0,se.indexOf("/")).trim());
                    int b=Integer.parseInt(se.substring(se.indexOf("/")+1,se.length()).trim());
                    String ans= String.valueOf(a/b);
                    speak("The answer is "+ans);
                }

                else if(se.toLowerCase().contains("sing")&&se.toLowerCase().contains("song")){
                    mp=MediaPlayer.create(MainActivity.this,R.raw.poem);
                    mp.start();
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
                            Intent i=new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("sms:"+phone));
                            i.putExtra("sms_body",se.substring(se.indexOf("that")+4,se.length()));
                            speak("Okay " + name);
                            found=true;
                            startActivity(i);
                            break;
                        }
                        if ((c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE))).toLowerCase().contains(namep)) {
                            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Intent i=new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("sms:"+phone));
                            i.putExtra("sms_body",se.substring(se.indexOf("that")+4,se.length()));
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

                else if(se.toLowerCase().contains("where is")||se.toLowerCase().contains("what is")||se.toLowerCase().contains("where are")||se.toLowerCase().contains("what are")){
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
                        speak("Sorry Can U please Enter?");
                        publishProgress(15);
                    }

                }
                else if(se.toLowerCase().contains("remind me to")){

                    reminder=se.substring(se.toLowerCase().indexOf("to")+2);
                    speak("When Do u want me to remind you ?");
                    publishProgress(10);
                }
                else if(se.toLowerCase().contains("open")){
                    String app=se.substring(se.toLowerCase().indexOf("open")+4);

                }
                else if(se.toLowerCase().contains("take a")&&se.toLowerCase().contains("selfie")){
                    selfie();
                }

                else{
                    speak("Here What I Found on Internet");
                    Intent i=new Intent(Intent.ACTION_WEB_SEARCH);
                    i.putExtra(SearchManager.QUERY,se);
                    startActivity(i);
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
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]==10){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Please Select");
                String [] strings={"EveryDay","Specific Days"};
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
                                Dialog d=new Dialog(MainActivity.this);
                                d.setContentView(R.layout.specifiv);
                                Button b=(Button)d.findViewById(R.id.button2);
                                d.show();
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
                        if(i<h){
                            c1.set(Calendar.DATE,c1.get(Calendar.DATE)+1);
                            c1.set(Calendar.HOUR_OF_DAY, i);
                            c1.set(Calendar.MINUTE, i1);
                            c1.set(Calendar.SECOND, 00);
                            i=i+(23-h);
                            if(i1>=m){
                                i1=m-i1;
                            }
                            else{
                                i=i-1;
                                i1=m-(i1-m);
                            }
                        }else {
                            c1.set(Calendar.HOUR_OF_DAY, i);
                            c1.set(Calendar.MINUTE, i1);
                            c1.set(Calendar.SECOND, 00);
                            i=i-h;
                            if(i1>=m){
                                i1=m-i1;
                            }
                            else{
                                i=i-1;
                                i1=m-(i1-m);
                            }
                        }
                        Intent intent=new Intent(MainActivity.this,AlramRing.class);
                        intent.putExtra("message","Wake Up "+name);
                        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),(int)(Math.random()*100),intent,0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,c1.getTimeInMillis(),pendingIntent);
                        speak("Alarm set for "+i+" Hours and "+i1+" minutes from now");
                    }
                };

                timePickerDialog = new TimePickerDialog(MainActivity.this,t,hh,mm,true);
                timePickerDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            b=false;
            t.setText(se.toUpperCase());
        }
    }

}
