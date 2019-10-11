package com.example.ex.bakingapp.utils;

import android.os.AsyncTask;


import com.example.ex.bakingapp.MainActivity;
import com.example.ex.bakingapp.RecipesRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonDownload extends AsyncTask<String, Void, JSONArray> {

    public JsonDownload() {

    }

    protected JSONArray doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(4000);
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            for(int data = reader.read(); data != -1; data = reader.read()) {
                char ch = (char)data;
                content.append(ch);
            }

            JSONArray jsonArray = new JSONArray(content.toString());

            return jsonArray;
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }
}
