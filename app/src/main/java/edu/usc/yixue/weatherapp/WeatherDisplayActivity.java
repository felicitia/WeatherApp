package edu.usc.yixue.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WeatherDisplayActivity extends AppCompatActivity {

    private TextView showCityName = null;
    private TextView showCityId = null;
    private TextView showNameTemp = null;
    private TextView showIdTemp = null;
    private TextView showFavTemp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);
        showCityId = (TextView) findViewById(R.id.show_city_id);
        showCityName = (TextView) findViewById(R.id.show_city_name);
        showNameTemp = (TextView) findViewById(R.id.city_name_temp);
        showIdTemp = (TextView) findViewById(R.id.city_id_temp);
        showFavTemp = (TextView) findViewById(R.id.fav_city_temp);
        try {
            setValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setValues() throws JSONException {
        List<String> stringList = getIntent().getStringArrayListExtra("searchResult");
        if(stringList != null){
            if(stringList.get(0) !=null ){
                JSONObject jsonObject = new JSONObject(stringList.get(0));
                showCityName.setText(jsonObject.getString("name"));
                showNameTemp.setText(jsonObject.getJSONObject("main").getString("temp"));
            }
            if(stringList.get(1) != null){
                JSONObject jsonObject = new JSONObject(stringList.get(1));
                showCityId.setText(jsonObject.getString("id"));
                showIdTemp.setText(jsonObject.getJSONObject("main").getString("temp"));
            }
        }
        String favResult = getIntent().getStringExtra("favResult");
        showFavTemp.setText(new JSONObject(favResult).getJSONObject("main").getString("temp"));
    }
}
