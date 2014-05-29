package com.example.myapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by patrick on 24/05/14.
 */
public class Restaurant {
    private int id;
    private String name;
    private String adress;
    private Double latitude;
    private Double longitude;
    private String placesID;
    private String description;
    private String referencePlaces;
    private boolean isPoint;


    public Restaurant(int id, String name, String adress, Double latitude, Double longitude, String placesID, String description, String referencePlaces, boolean isPoint) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placesID = placesID;
        this.description = description;
        this.referencePlaces = referencePlaces;
        this.isPoint = isPoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlacesID() {
        return placesID;
    }

    public void setPlacesID(String placesID) {
        this.placesID = placesID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferencePlaces() {
        return referencePlaces;
    }

    public void setReferencePlaces(String referencePlaces) {
        this.referencePlaces = referencePlaces;
    }

    public boolean isPoint() {
        return isPoint;
    }

    public void setPoint(boolean isPoint) {
        this.isPoint = isPoint;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adress='" + adress + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", placesID='" + placesID + '\'' +
                ", description='" + description + '\'' +
                ", referencePlaces='" + referencePlaces + '\'' +
                ", isPoint=" + isPoint +
                '}';
    }

    public String toJSON(){
        try {
            return new JSONObject().put("id", id)
                    .put("name", this.name)
                    .put("adress", this.adress)
                    .put("latitude", this.latitude)
                    .put("longitude", this.longitude)
                    .put("longitude", this.longitude)
                    .put("placesID", this.placesID)
                    .put("description", this.description)
                    .put("referencePlaces", this.referencePlaces)
                    .put("isPoint", this.isPoint)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
