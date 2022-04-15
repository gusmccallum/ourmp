package com.example.ourmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefConfig {
    private  static final String LIST_KEY_MP = "list_key_mp";
    private  static final String LIST_KEY_BILL = "list_key_bill";


    public static void writeListInPref(Context context, ArrayList<MP> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY_MP, jsonString);
        editor.apply();
    }

    public static ArrayList<MP> readListFromPref(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY_MP, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MP>>() {}.getType();

        return gson.fromJson(jsonString, type);
    }

    public static void writeListInPrefBills(Context context, ArrayList<Activity> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY_BILL, jsonString);
        editor.apply();
    }

    public static ArrayList<Activity> readListFromPrefBills(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY_BILL, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Activity>>() {}.getType();

        return gson.fromJson(jsonString, type);
    }


    public static void deleteListInPref(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(LIST_KEY_MP);
        editor.remove(LIST_KEY_BILL);
        editor.apply();
    }
}
