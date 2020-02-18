package com.hoangnguyen.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoangnguyen.news.R;
import com.hoangnguyen.news.model.Weather;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Weather> weatherArrayList;

    public WeatherAdapter(Context context, ArrayList<Weather> weatherArrayList) {
        this.context = context;
        this.weatherArrayList = weatherArrayList;
    }

    @Override
    public int getCount() {
        return weatherArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_weather_listview,null);

        Weather weather = weatherArrayList.get(position);
        TextView tvDay = convertView.findViewById(R.id.tv_day);
        TextView tvStatus = convertView.findViewById(R.id.tv_status);
        TextView tvAvrTemp = convertView.findViewById(R.id.tv_avr_temp);
        ImageView imgStatus = convertView.findViewById(R.id.img_status);

        tvDay.setText(weather.getDay());
        tvStatus.setText(weather.getStatus());
        tvAvrTemp.setText(weather.getAvrTemp()+"°C");


        Picasso.get().load("http://openweathermap.org/img/wn/" +
                weather.getImage() + ".png").into(imgStatus);
        return convertView;
    }
}
