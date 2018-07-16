package com.example.admin.knowyourweather;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    TextView cityField;
    TextView updatedField;
    TextView humidity;
    TextView pressure;
    TextView currentTemperatureField;
    TextView weathercondition;
    LinearLayout background;
TextView weatherIcon;
Typeface weatherFont;
//private static final String OPEN_WEATHER_MAP_URL =
  //      "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
//    private static final String OPEN_WEATHER_MAP_API = "92cba32359290c222aca7aa3f22d0166";

    Handler handler;
    public WeatherFragment() {
        handler = new Handler();
    }

    // Required empty public constructor
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWeatherData(new PastData(getActivity()).getCity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city);
        updatedField = (TextView)rootView.findViewById(R.id.updated);
        humidity = (TextView)rootView.findViewById(R.id.humidity);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.temprature);
        weatherFont = Typeface.createFromAsset(getContext().getAssets(), "font/weather.ttf");
         weatherIcon = (TextView)rootView.findViewById(R.id.weathericon);
        weatherIcon.setTypeface(weatherFont);
        pressure = (TextView)rootView.findViewById(R.id.pressure);
        background = (LinearLayout)rootView.findViewById(R.id.background);
        weathercondition=(TextView)rootView.findViewById(R.id.weathercondition);
        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"font/sans.ttf");
        updatedField.setTypeface(type);
        cityField.setTypeface(type);
        currentTemperatureField.setTypeface(type);
        weathercondition.setTypeface(type);
        humidity.setTypeface(type);
        pressure.setTypeface(type);
        return rootView;
    }
    private void updateWeatherData(final String city){         // update weather acc to city
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void renderWeather(JSONObject json){    // get info acc to city
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            humidity.setText(
                   // details.getString("description").toUpperCase(Locale.US) +
                             "Humidity: " + main.getString("humidity") + "%"
                           );
            weathercondition.setText(details.getString("description").toUpperCase(Locale.US));
             pressure.setText( "\n" + "Pressure: " + main.getString("pressure") + " hPa");
            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last Update: "+updatedOn);

            setWeatherIcon(details.getInt("id"),
                   json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setWeatherIcon(int actualId, long sunrise, long sunset){     // get weather conditions
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
                background.setBackground(getResources()
                        .getDrawable(R.drawable.sunny));


            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
                background.setBackground(getResources()
                        .getDrawable(R.drawable.night));
                updatedField.setTextColor(getResources().getColor(R.color.color2));
                humidity.setTextColor(getResources().getColor(R.color.color2));
                currentTemperatureField.setTextColor(getResources().getColor(R.color.color2));
                pressure.setTextColor(getResources().getColor(R.color.color2));
                weathercondition.setTextColor(getResources().getColor(R.color.color2));
              cityField.setTextColor(getResources().getColor(R.color.color2));
                weatherIcon.setTextColor(getResources().getColor(R.color.color2));

            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    background.setBackground(getResources()
                            .getDrawable(R.drawable.thunder));
                    updatedField.setTextColor(getResources().getColor(R.color.thunder));
                    humidity.setTextColor(getResources().getColor(R.color.thunder));
                    currentTemperatureField.setTextColor(getResources().getColor(R.color.thunder));
                    pressure.setTextColor(getResources().getColor(R.color.thunder));
                    weathercondition.setTextColor(getResources().getColor(R.color.thunder));
                    cityField.setTextColor(getResources().getColor(R.color.thunder));
                    weatherIcon.setTextColor(getResources().getColor(R.color.thunder));
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    background.setBackground(getResources()
                            .getDrawable(R.drawable.drizzle));
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    background.setBackground(getResources()
                            .getDrawable(R.drawable.foggy));
                    updatedField.setTextColor(getResources().getColor(R.color.black));
                    humidity.setTextColor(getResources().getColor(R.color.black));
                    currentTemperatureField.setTextColor(getResources().getColor(R.color.black));
                    pressure.setTextColor(getResources().getColor(R.color.black));
                    weathercondition.setTextColor(getResources().getColor(R.color.black));
                    cityField.setTextColor(getResources().getColor(R.color.black));
                    weatherIcon.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    background.setBackground(getResources()
                            .getDrawable(R.drawable.cloudy));
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    background.setBackground(getResources()
                            .getDrawable(R.drawable.snowy));
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    background.setBackground(getResources()
                            .getDrawable(R.drawable.rain));
                    updatedField.setTextColor(getResources().getColor(R.color.rain));
                    humidity.setTextColor(getResources().getColor(R.color.rain));
                    currentTemperatureField.setTextColor(getResources().getColor(R.color.rain));
                    pressure.setTextColor(getResources().getColor(R.color.rain));
                    weathercondition.setTextColor(getResources().getColor(R.color.rain));
                    cityField.setTextColor(getResources().getColor(R.color.rain));
                    weatherIcon.setTextColor(getResources().getColor(R.color.rain));
                    break;
            }
        }
        weatherIcon.setText(icon);
    }
    public void changeCity(String city){
        updateWeatherData(city);
    }

}