package com.hoangnguyen.news.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hoangnguyen.news.R;
import com.hoangnguyen.news.activity.NextDayWeatherActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherFragment extends Fragment {

    private TextView tvCityName, tvTemp, tvStatus, tvHumidity, tvCloud, tvWind, tvDay;
    private ImageView imgIcon;
    private Button btnForwardActivity;
    private String city;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        setHasOptionsMenu(true);
        tvCityName = view.findViewById(R.id.tv_city_name);
        tvTemp = view.findViewById(R.id.tv_temp);
        tvStatus = view.findViewById(R.id.tv_status);
        tvHumidity = view.findViewById(R.id.tv_humidity);
        tvCloud = view.findViewById(R.id.tv_cloud);
        tvWind = view.findViewById(R.id.tv_wind);
        tvDay = view.findViewById(R.id.tv_day);
        imgIcon = view.findViewById(R.id.img_icon);
        btnForwardActivity = view.findViewById(R.id.btn_forward_activity);
        if (isOnline()==false){
            btnForwardActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        getCurrentWeatherData("Hanoi");


        btnForwardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NextDayWeatherActivity.class);
                intent.putExtra("cityName", city);
                startActivity(intent);

            }
        });
        return view;
    }

    private void getCurrentWeatherData(String cityName) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName
                + "&units=metric&appid=056cdf2cb0e197454382cd127ec61b84";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            city = jsonObject.getString("name");
                            //city name
                            tvCityName.setText(jsonObject.getString("name") + ", " +
                                    jsonObject.getJSONObject("sys")
                                            .getString("country"));

                            //image
                            JSONObject jsonObjectWeather = jsonObject.getJSONArray("weather")
                                    .getJSONObject(0);
                            Picasso.get().load("http://openweathermap.org/img/wn/" +
                                    jsonObjectWeather.getString("icon") + ".png").into(imgIcon);

                            //temperature
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            Double t = Double.valueOf(jsonObjectMain.getString("temp"));
                            tvTemp.setText(String.valueOf(t.intValue()) + "°C");

                            //status
                            tvStatus.setText(jsonObjectWeather.getString("main"));

                            //humidity
                            tvHumidity.setText(jsonObjectMain.getString("humidity") + "%");

                            //cloud
                            tvCloud.setText(jsonObject.getJSONObject("clouds").getString("all") + "%");

                            //wind
                            tvWind.setText(jsonObject.getJSONObject("wind").getString("speed") + "m/s");

                            //day update
                            long dt = Long.valueOf(jsonObject.getString("dt"));
                            Date date = new Date(dt * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                            tvDay.setText(simpleDateFormat.format(date));

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search city name...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getCurrentWeatherData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchItem.getIcon().setVisible(false, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
