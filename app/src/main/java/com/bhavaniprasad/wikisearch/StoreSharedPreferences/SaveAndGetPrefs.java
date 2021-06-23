package com.bhavaniprasad.wikisearch.StoreSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bhavaniprasad.wikisearch.model.wikiUsersList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/***
 * SaveAndGetPrefs class stores network data which can be useful in Offline Mode
 */
public class SaveAndGetPrefs {

    public void saveArrayList(ArrayList<wikiUsersList> list, String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<wikiUsersList> getArrayList(String key, Context yourMain){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(yourMain);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<wikiUsersList>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
