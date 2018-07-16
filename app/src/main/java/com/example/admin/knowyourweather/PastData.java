package com.example.admin.knowyourweather;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Admin on 21-05-2018.
 */
  // This class stores last city user searched for
    public class PastData {

        SharedPreferences prefs;

        public PastData(Activity activity){
            prefs = activity.getPreferences(Activity.MODE_PRIVATE); // key-value pairs are returned by this method in SharedPreferences Class
        }

        // If the user has not chosen a city yet, return
        // Sydney as the default city
        String getCity(){
            return prefs.getString("city", "Sydney, AU");
        }

    void setCity(String city){
        prefs.edit().putString("city", city).commit(); // Data getting stored in key-value pair form
    }

}

