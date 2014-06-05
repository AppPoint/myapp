package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by patrick on 24/05/14.
 */
public class ListActivity extends Activity {
    private ListView list;
    private ArrayList<Restaurant> arrayListRestaurant = new ArrayList<Restaurant>();
    private double latitude;
    private double longitude;
    private Controler controler = new Controler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        JSONArray restaurantList;
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        list = (ListView) findViewById(R.id.listView);
        String urlStr = "http://" + getString(R.string.ip) + ":8080/axis2/services/controler/listRestaurants?latitude=" + latitude
                + "&longitude=" + longitude + "&response=application/json";
        try {
            String json = controler.execute(urlStr).get();
            JSONObject jsonResult = new JSONObject(json);
            if (jsonResult.get("return") instanceof JSONObject){
                restaurantList = new JSONArray("[" + jsonResult.getJSONObject("return").toString() + "]");
            }else{
                restaurantList = jsonResult.getJSONArray("return");
            }
            for (int i = 0; i < restaurantList.length(); i++){
                JSONObject jsonRestaurant = (JSONObject) restaurantList.get(i);
                arrayListRestaurant.add(new Restaurant(jsonRestaurant.getInt("id"), jsonRestaurant.getString("name"), jsonRestaurant.getString("adress"),
                        jsonRestaurant.getDouble("latitude"), jsonRestaurant.getDouble("longitude"), jsonRestaurant.getString("placesID"),
                        jsonRestaurant.getString("description"), jsonRestaurant.getString("referencePlaces"), jsonRestaurant.getBoolean("isPoint")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        CustomAdapter adapter = new CustomAdapter(ListActivity.this, arrayListRestaurant, getResources());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant r = arrayListRestaurant.get(position);
                Intent i = new Intent(ListActivity.this, RestaurantActivity.class);
                i.putExtra("Restaurant", r.toJSON());
                startActivity(i);
            }
        });
    }
}
