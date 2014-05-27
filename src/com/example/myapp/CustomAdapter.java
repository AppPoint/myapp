package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by patrick on 24/05/14.
 */
public class CustomAdapter extends BaseAdapter {

    private ArrayList<Restaurant> restaurants;
    private final Activity context;
    private static LayoutInflater inflater=null;
    public Resources res;

    public CustomAdapter(Activity context, ArrayList<Restaurant> restaurants, Resources res) {
        this.restaurants = restaurants;
        this.context = context;
        this.res = res;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_row, null);
        TextView name = (TextView) convertView.findViewById(R.id.textRowName);
        TextView adress = (TextView) convertView.findViewById(R.id.textRowAdress);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        Restaurant r = this.restaurants.get(position);
        if (r.isPoint()){
            imageView.setImageResource(R.drawable.restaurant_point);
        } else{
            imageView.setImageResource(R.drawable.restaurant);
        }
        name.setText(r.getName());
        adress.setText(r.getAdress());
        return convertView;
    }
}
