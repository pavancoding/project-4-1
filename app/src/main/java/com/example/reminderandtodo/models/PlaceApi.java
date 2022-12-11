package com.example.reminderandtodo.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceApi {

    public ArrayList<String> autoComplete(String input){
        ArrayList<String> arrayList=new ArrayList();
        HttpURLConnection connection=null;
        StringBuilder jsonResult=new StringBuilder();
        try {
            StringBuilder sb=new StringBuilder("https://api.foursquare.com/v3/autocomplete?");
            sb.append("query="+input);
            sb.append("&ll=15.9129%2C79.7400&radius=100000");
            URL url=new URL(sb.toString().replaceAll(" ", "%20"));
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Authorization", "fsq3g0i4ICeaGrzwbWZLj8qV6iewgqqqid4bLsPBaqAfyT8=");
            connection.setRequestProperty("accept","application/json");
            InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff=new char[1024];
            while ((read=inputStreamReader.read(buff))!=-1){
                jsonResult.append(buff,0,read);
            }

            Log.d("JSon",jsonResult.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(connection!=null){
                connection.disconnect();
            }
        }

        try {
            JSONObject jsonObject=new JSONObject(jsonResult.toString());
            JSONArray prediction=jsonObject.getJSONArray("results");
            for(int i=0;i<prediction.length();i++){
                JSONObject obj=prediction.getJSONObject(i).getJSONObject("text");
                        Log.d("data",obj.toString()+"is the object");
                arrayList.add(obj.getString("primary")+" "+obj.getString("secondary"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return arrayList;
    }


}
