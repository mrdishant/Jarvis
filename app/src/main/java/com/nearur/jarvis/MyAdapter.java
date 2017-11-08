package com.nearur.jarvis;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mrdis on 11/7/2017.
 */

public class MyAdapter extends ArrayAdapter<String> {
    Context context; int resource;
    ArrayList<String> objects;
    public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(resource,parent,false);

        TextView textView=v.findViewById(R.id.txt123);
        if(position%2==0){
            textView.setGravity(Gravity.RIGHT);
        }else{
            textView.setGravity(Gravity.LEFT);
        }

        return v;
    }
}
