package edu.usc.yixue.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usc.yixue.Proxy;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkCityName = null;
    private CheckBox checkCityId = null;
    private EditText cityIdInput = null;
    private Spinner cityNameSpinner = null;
    private Button searchBtn = null;

    private String cityName = null;
    private String cityId = null;
    private Intent intent = null;
    private String favCityId = null;
    private Long before;
    private Long after;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCityId = (CheckBox) findViewById(R.id.check_city_id);
        checkCityName = (CheckBox) findViewById(R.id.check_city_name);
        cityIdInput = (EditText) findViewById(R.id.city_id_input);
        cityNameSpinner = (Spinner) findViewById(R.id.cityname_spinner);
        searchBtn = (Button) findViewById(R.id.search_btn);

        cityNameSpinner.setOnItemSelectedListener(new OnCityNameSelectedListener());
        intent = new Intent(this, DisplayActivity.class);

        favCityId = readFavCityId();
//        Proxy.sendDef(favCityId, 275, 1, "edu.usc.yixue.weatherapp");

        searchBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                boolean isCityNameChecked = checkCityName.isChecked();
                boolean isCityIdChecked = checkCityId.isChecked();
                String urlById = null;
                String urlByName = null;
                if(isCityIdChecked && isCityNameChecked){
                    cityId = cityIdInput.getText().toString().trim();
//                    Proxy.sendDef("edu.usc.yixue.weatherapp", 272, 1, cityId);
                    urlByName = getString(R.string.weather_api_domain)+"weather?units=Imperial&q="+cityName+"&"+getString(R.string.weather_api_key);
                    urlById = getString(R.string.weather_api_domain)+"weather?units=Imperial&id="+cityId+"&"+getString(R.string.weather_api_key);

                }else if(isCityIdChecked){
                    cityId = cityIdInput.getText().toString().trim();
//                    Proxy.sendDef("edu.usc.yixue.weatherapp", 272, 1, cityId);
                    urlById = getString(R.string.weather_api_domain)+"weather?units=Imperial&id="+cityId+"&"+getString(R.string.weather_api_key);
                }else if(isCityNameChecked){
                    urlByName = getString(R.string.weather_api_domain)+"weather?units=Imperial&q="+cityName+"&"+getString(R.string.weather_api_key);
                }
                String favUrl = "http://api.openweathermap.org/data/2.5/weather?units=Imperial&id="+favCityId+"&"+getString(R.string.weather_api_key);

//                before = System.currentTimeMillis();

                SearchWeatherTask task = new SearchWeatherTask();
                URL name = null;
                URL id = null;
                URL fav = null;
                try{
                if(urlByName != null){
                    name = new URL(urlByName);
                }
                if(urlById!=null){
                    id = new URL(urlById);
                }
                if(favUrl != null){
                    fav = new URL(favUrl);
                }}catch (Exception e){
                    e.printStackTrace();
                }
                task.execute(name, id, fav);
                    }
                });
    }

    //assume reading the fav id from user's setting
    public String readFavCityId(){
        return "3882428";
    }

//    public void onSearchClick(View view) throws IOException {
//        boolean isCityNameChecked = checkCityName.isChecked();
//        boolean isCityIdChecked = checkCityId.isChecked();
//        String urlById = null;
//        String urlByName = null;
//        if(isCityIdChecked && isCityNameChecked){
//            cityId = cityIdInput.getText().toString().trim();
//            Proxy.sendDef("edu.usc.yixue.weatherapp", 272, 1, cityId);
//            urlByName = getString(R.string.weather_api_domain)+"weather?units=Imperial&q="+cityName+"&"+getString(R.string.weather_api_key);
//            urlById = getString(R.string.weather_api_domain)+"weather?units=Imperial&id="+cityId+"&"+getString(R.string.weather_api_key);
//
//        }else if(isCityIdChecked){
//            cityId = cityIdInput.getText().toString().trim();
//            Proxy.sendDef("edu.usc.yixue.weatherapp", 272, 1, cityId);
//            urlById = getString(R.string.weather_api_domain)+"weather?units=Imperial&id="+cityId+"&"+getString(R.string.weather_api_key);
//        }else if(isCityNameChecked){
//            urlByName = getString(R.string.weather_api_domain)+"weather?units=Imperial&q="+cityName+"&"+getString(R.string.weather_api_key);
//        }
//        String favUrl = "http://api.openweathermap.org/data/2.5/weather?units=Imperial&id="+favCityId+"&"+getString(R.string.weather_api_key);
//        before = System.currentTimeMillis();
//        SearchFavTask favTask = new SearchFavTask();
//        favTask.execute(favUrl);

//        SearchWeatherTask task = new SearchWeatherTask();
//        URL name = null;
//        URL id = null;
//        URL fav = null;
//        if(urlByName != null){
//            name = new URL(urlByName);
//        }
//        if(urlById!=null){
//            id = new URL(urlById);
//        }
//        if(favUrl != null){
//            fav = new URL(favUrl);
//        }
//        task.execute(name, id, fav);
//        Map<String, String> result = new HashMap<String, String>();
//        if(urlByName != null){
//            before = System.currentTimeMillis();
//            result.put("name", Proxy.getResult(urlByName));
//            after = System.currentTimeMillis();
//            Log.e("urlByName", ""+(after-before));
//        }
//        if(urlById != null){
//            before = System.currentTimeMillis();
//            result.put("id", Proxy.getResult(urlById));
//            after = System.currentTimeMillis();
//            Log.e("urlById", ""+(after-before));
//        }
//        if(favUrl != null){
//            before = System.currentTimeMillis();
//            result.put("fav", Proxy.getResult(favUrl));
//            after = System.currentTimeMillis();
//            Log.e("favUrl", ""+(after-before));
//        }
//
//        try {
//            if(result.containsKey("name")){
//                intent.putExtra("name", result.get("name"));
//            }
//            if(result.containsKey("id")){
//                intent.putExtra("id", result.get("id"));
//            }
//            if(result.containsKey("fav")){
//                intent.putExtra("fav", result.get("fav"));
//            }
//            startActivity(intent);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

//    private class SearchFavTask extends AsyncTask<URL, Void, String>{
//
//        @Override
//        protected String doInBackground(URL... urls){
//            for(URL favUrl: urls) {
//                try {
//                    URLConnection urlConnection = favUrl.openConnection();
//                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder stringBuilder = new StringBuilder();
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        stringBuilder.append(line);
//                    }
//                    inputStream.close();
//                    return stringBuilder.toString().trim();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            after = System.currentTimeMillis();
//            Log.e("TIME BEFORE", ""+before);
//            Log.e("TIME AFTER", ""+after);
//            Log.e("TIME DIFF", ""+(after-before));
//            if(result != null){
//                intent.putExtra("favResult", result);
//                startActivity(intent);
//            }
//        }
//    }

    private class SearchWeatherTask extends AsyncTask<URL, Void, Map<String, String>>{
        @Override
        protected Map<String, String> doInBackground(URL... urls){
            Map<String, String> result = new HashMap<String, String>();
            for(int i=0; i<urls.length; i++){
                URL url = urls[i];
                if(url != null){
                    Log.e("url", url.toString());
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
                        switch (i){
                            case 0:
                                result.put("name", outString.toString().trim());
                                break;
                            case 1:
                                result.put("id", outString.toString().trim());
                                break;
                            case 2:
                                result.put("fav", outString.toString().trim());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Map<String, String> result){
            try {
                if(result.containsKey("name")){
                    intent.putExtra("name", result.get("name"));
                }
                if(result.containsKey("id")){
                    intent.putExtra("id", result.get("id"));
                }
                if(result.containsKey("fav")){
                    intent.putExtra("fav", result.get("fav"));
                }
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class OnCityNameSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Log.e("spinner", "onItemSelected");
            cityName = cityNameSpinner.getSelectedItem().toString();
//            Proxy.sendDef("edu.usc.yixue.weatherapp", 269, 1, cityName);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            Log.e("spinner", "onNothingSelected");
        }

    }
}
