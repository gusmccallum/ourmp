package com.example.ourmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefConfig {
    private  static final String LIST_KEY = "list_key";

    public static void writeListInPref(Context context, ArrayList<MP> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
    }

    public static ArrayList<MP> readListFromPref(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MP>>() {}.getType();

        return gson.fromJson(jsonString, type);
    }

    public static void deleteListInPref(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(LIST_KEY);
        editor.apply();
    }
}
