package com.nearur.jarvis;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class Contacts extends AppCompatActivity {

    ListView listView;
    ContentResolver resolver;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList,arrayList1;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        listView=(ListView)findViewById(R.id.listview);
        arrayList=new ArrayList<>();
        resolver=getContentResolver();
        editText=(EditText)findViewById(R.id.search);
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor c=resolver.query(uri,null,null,null,null);

        while(c.moveToNext()){
            StringBuffer o=new StringBuffer();
           // o.append(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))+"\n");
            o.append(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME))+"\n");
            o.append(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            if(!arrayList.contains(o.toString()))
            arrayList.add(o.toString());
        }


       arrayList1=new ArrayList<String>();
        arrayList1.addAll(arrayList);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.length()>=0){
                            arrayList.clear();
                           for(String s: arrayList1){
                               if(s.toLowerCase().contains(charSequence.toString())||s.contains(charSequence.toString())){
                                   arrayList.add(s);
                               }
                           }
                        }
                  Collections.sort(arrayList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
       Collections.sort(arrayList);
        adapter=new ArrayAdapter<String>(this,R.layout.contact,R.id.textcontact,arrayList);
         listView.setAdapter(adapter);
    }
}
