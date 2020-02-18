package com.hoangnguyen.news.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hoangnguyen.news.R;
import com.hoangnguyen.news.adapter.WeatherAdapter;
import com.hoangnguyen.news.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NextDayWeatherActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView tvCityName;
    private ListView lvWeather;
    private WeatherAdapter weatherAdapter;
    private ArrayList<Weather> weatherArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_day_weather);

//        imgBack = findViewById(R.id.img_back);
        tvCityName = findViewById(R.id.tv_city_name);
        lvWeather = findViewById(R.id.listview);

        weatherArrayList = new ArrayList<Weather>();
        weatherAdapter = new WeatherAdapter(NextDayWeatherActivity.this, weatherArrayList);
        lvWeather.setAdapter(weatherAdapter);

        Intent intent = getIntent();
        String city = intent.getStringExtra("cityName");
        if (city.equals("")) {
            get7DaysData("Hanoi");
        } else {
            get7DaysData(city);
        }

//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    private void get7DaysData(String cityName) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="
                + cityName + "&units=metric&cnt=7&appid=056cdf2cb0e197454382cd127ec61b84";
        RequestQueue requestQueue = Volley.newRequestQueue(NextDayWeatherActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            tvCityName.setText(name);

                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectList = jsonArray.getJSONObject(i);

                                Long dt = Long.valueOf(jsonObjectList.getString("dt"));
                                Date date = new Date(dt * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                                String day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");

                                long a = Math.round(Double.valueOf(jsonObjectTemp.getString("temp")));
                                String avrTemp= String.valueOf(a);


                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                weatherArrayList.add(new Weather(day, status, icon,avrTemp ));
                            }

                            weatherAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);

    }
}
