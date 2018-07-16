package com.example.admin.knowyourweather;

/**
 * Created by Admin on 21-05-2018.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoteFetch {
    private static final String OPEN_WEATHER_MAP_API ="http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    public static JSONObject getJSON(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city)); // site containing weather info
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection(); // open connection

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id)); // particular key to get data

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);   // String to contain all data
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString()); // convert string to json object

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){           // cod is a var in data that is 200 if request sucess
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
}
