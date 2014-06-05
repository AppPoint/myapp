package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class MyActivity extends Activity implements GoogleMap.OnMarkerClickListener, LocationListener{

    private GoogleMap googleMap;
    private Restaurant restaurant;
    private HashMap<String, Restaurant> hashMapRestaurant = new HashMap<String, Restaurant>();
    private HashMap<String, Marker> hashMapMarker = new HashMap<String, Marker>();
    private Button list;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        list = (Button) findViewById(R.id.button);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location mylocation = locationManager.getLastKnownLocation(provider);
        LatLng latLng;
        if (mylocation == null){
            Double longitude = -43.341259;
            Double latitude = -22.9533;
            latLng = new LatLng(latitude, longitude);
        }else{
            latLng = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ListActivity.class);
                intent.putExtra("latitude", googleMap.getCameraPosition().target.latitude);
                intent.putExtra("longitude", googleMap.getCameraPosition().target.longitude);
                startActivity(intent);
            }
        });

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();

        updateMap(latLng.latitude, latLng.longitude);

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                updateMap(cameraPosition.target.latitude, cameraPosition.target.longitude);
            }
        });

    }

    private void updateMap(Double latitude, Double longitude) {
        String urlStr = "http://" + getString(R.string.ip) + ":8080/axis2/services/controler/listRestaurants?latitude=" + latitude
                + "&longitude=" + longitude + "&response=application/json";
        Marker marker;
        Controler controler = new Controler();
        JSONArray restaurantList;

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
                Restaurant restaurant = new Restaurant(jsonRestaurant.getInt("id"), jsonRestaurant.getString("name"), jsonRestaurant.getString("adress"),
                      jsonRestaurant.getDouble("latitude"), jsonRestaurant.getDouble("longitude"), jsonRestaurant.getString("placesID"),
                      jsonRestaurant.getString("description"), jsonRestaurant.getString("referencePlaces"), jsonRestaurant.getBoolean("isPoint"));
                removeMarker(restaurant.getPlacesID());
                if (jsonRestaurant.getBoolean("isPoint")){
                    LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                    marker = googleMap.addMarker(new MarkerOptions().position(latLng)
                            .title(restaurant.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_point)));
                } else{
                    LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                    marker = googleMap.addMarker(new MarkerOptions().position(latLng)
                            .title(restaurant.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant)));
                }
                hashMapMarker.put(restaurant.getPlacesID(), marker);
                hashMapRestaurant.put(marker.getId(), restaurant);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void removeMarker(String placesID) {
        if (hashMapMarker.containsKey(placesID)){
            Marker marker = hashMapMarker.get(placesID);
            hashMapMarker.remove(placesID);
            hashMapRestaurant.remove(marker.getId());
            marker.remove();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Restaurant r = hashMapRestaurant.get(marker.getId());
        //Toast.makeText(getApplicationContext(), r.toString(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, RestaurantActivity.class);
        i.putExtra("Restaurant", r.toJSON());
        startActivity(i);
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateMap(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
