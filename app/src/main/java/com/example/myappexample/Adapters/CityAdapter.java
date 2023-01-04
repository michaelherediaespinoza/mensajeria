package com.example.myappexample.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myappexample.R;

import com.example.myappexample.model.City;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<City> items;

    public CityAdapter(Activity activity, ArrayList<City> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_item_layout, null);
        }

        City city = items.get(position);

        ImageView image = (ImageView) vi.findViewById(R.id.cityImage);
        image.setImageBitmap(city.getPhoto());

        TextView name = (TextView) vi.findViewById(R.id.cityName);
        name.setText(city.getName());

        return vi;
    }
}