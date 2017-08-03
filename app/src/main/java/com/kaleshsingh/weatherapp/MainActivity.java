package com.kaleshsingh.weatherapp;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;

    // Class to download Website (API) Content

    private class DownloadTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection;
            String result  = "";
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader =  new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while(data != -1){
                    char currentCharacter = (char) data;
                    result += currentCharacter;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot find weather!", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot find weather!", Toast.LENGTH_LONG).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try{
                String message = "";
                //Log.i("Downloaded Data", result);
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");
                //Log.i("Weather Data", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(main != "" && description != ""){
                        message += main + ": " + description + "\r\n";
                    }

                    //Log.i("main", jsonPart.getString("main"));
                    //Log.i("description", jsonPart.getString("description"));
                }

                if(message != ""){
                    resultTextView.setText(message);
                }
                else{Geor
                    resultTextView.setText("City not found!");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot find weather!", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void findWeather(View view){

        try {
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="
                    + encodedCityName + ",uk&APPID=dbb3b8b089732f8b705213f52b63fc6b");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cannot find weather!", Toast.LENGTH_LONG).show();

        }

        Log.i("cityName", cityName.getText().toString());


        // Removes the keyboard from the screen when the button is pressed.
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.cityName);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

       /* DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="
                + "London" + ",uk&APPID=dbb3b8b089732f8b705213f52b63fc6b");
*/

        //Todo ************ Attempt trying to parse the city IDs JSON File

        /*InputStream inputStream = getResources().openRawResource(R.raw.cityid);
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while(scanner.hasNext()){
            stringBuilder.append(scanner.next());
        }

        String IDs = stringBuilder.toString();
        try {
            JSONArray cityIDs = new JSONArray(IDs);
            System.out.println(cityIDs.getJSONObject(0).getString("name"));
            for(int i = 0; i < cityIDs.length(); ++i){

                JSONObject jsonObj = cityIDs.getJSONObject(i);
                if(jsonObj.getString("name").equals("London")){
                    System.out.println("City: " + jsonObj.getString("name") +
                    ", Country: " + jsonObj.getString("country"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(IDs);*/


    }
}

