package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by patrick on 24/05/14.
 */
public class RestaurantActivity extends Activity {
    private Restaurant restaurant;
    private TextView name;
    private TextView adress;
    private Button reservation;
    private TextView descriptionField;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        try {
            JSONObject json = new JSONObject(getIntent().getStringExtra("Restaurant"));
            restaurant = new Restaurant(json.getString("name"), json.getString("adress"), json.getDouble("latitude"),
                    json.getDouble("longitude"), json.getString("placesID"), json.getString("description"),
                    json.getString("referencePlaces"), json.getBoolean("isPoint"));
            name = (TextView) findViewById(R.id.textName);
            adress = (TextView) findViewById(R.id.textAdress);
            reservation = (Button) findViewById(R.id.reservationButton);
            description = (TextView) findViewById(R.id.textDescription);
            descriptionField = (TextView) findViewById(R.id.textDescriptionField);
            name.setText(restaurant.getName());
            adress.setText(restaurant.getAdress());
            if (restaurant.isPoint()) {
                descriptionField.setText(restaurant.getDescription());
                reservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(RestaurantActivity.this, ReservationActivity.class);
                        startActivity(i);
                    }
                });
            } else {
                reservation.setVisibility(View.INVISIBLE);
                descriptionField.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
