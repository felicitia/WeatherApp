package edu.usc.yixue.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkCityName = null;
    private CheckBox checkCityId = null;
    private EditText cityIdInput = null;
    private Spinner cityNameSpinner = null;
    private String cityName = null;
    private String cityId = null;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCityId = (CheckBox) findViewById(R.id.check_city_id);
        checkCityName = (CheckBox) findViewById(R.id.check_city_name);
        cityIdInput = (EditText) findViewById(R.id.city_id_input);
        cityNameSpinner = (Spinner) findViewById(R.id.spinner_city_name);

        cityNameSpinner.setOnItemSelectedListener(new OnCityNameSelectedListener());
        intent = new Intent(this, WeatherDisplayActivity.class);
    }

    public void onSearchClick(View view) throws IOException {
        boolean isCityNameChecked = checkCityName.isChecked();
        boolean isCityIdChecked = checkCityId.isChecked();
        if(isCityIdChecked && isCityNameChecked){
            cityId = cityIdInput.getText().toString().trim();
            SearchWeatherTask task = new SearchWeatherTask();
            String urlString1 = getString(R.string.weather_api_domain)+"weather?units=Imperial&q="+cityName+"&"+getString(R.string.weather_api_key);
            String urlString2 = getString(R.string.weather_api_domain)+"weather?units=Imperial&id="+cityId+"&"+getString(R.string.weather_api_key);
            task.execute(new URL(urlString1), new URL(urlString2));

        }else if(isCityIdChecked){
            cityId = cityIdInput.getText().toString().trim();
            String urlString = getString(R.string.weather_api_domain)+"weather?units=Imperial&id="+cityId+"&"+getString(R.string.weather_api_key);
            SearchWeatherTask task = new SearchWeatherTask();
            task.execute(null, new URL(urlString));
        }else if(isCityNameChecked){
            String urlString = getString(R.string.weather_api_domain)+"weather?units=Imperial&q="+cityName+"&"+getString(R.string.weather_api_key);
            SearchWeatherTask task = new SearchWeatherTask();
            task.execute(new URL(urlString), null);
        }
        URL favUrl = new URL("http://api.openweathermap.org/data/2.5/weather?units=Imperial&id=3882428&APPID=f46f62442611cdc087b629f6e87c7374");
        SearchFavTask favTask = new SearchFavTask();
        favTask.execute(favUrl);
    }

    private class SearchFavTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls){
            for(URL favUrl: urls) {
                try {
                    URLConnection urlConnection = favUrl.openConnection();
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                    return stringBuilder.toString().trim();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null){
                intent.putExtra("favResult", result);
                startActivity(intent);
            }
        }
    }

    private class SearchWeatherTask extends AsyncTask<URL, Void, List<String>>{
        @Override
        protected List<String> doInBackground(URL... urls){
            List<String> resultList = new ArrayList<String>();
            for(URL url: urls){
                if(url == null){
                    resultList.add(null);
                    continue;
                }
                try {
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder outString = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        outString.append(line);
                    }
                    inputStream.close();
                    resultList.add(outString.toString().trim());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return resultList;
        }

        @Override
        protected void onPostExecute(List<String> resultList){
            try {
                intent.putStringArrayListExtra("searchResult", (ArrayList<String>) resultList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class OnCityNameSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            cityName = cityNameSpinner.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
}
