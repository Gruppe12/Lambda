package no.lambda.model;

import java.util.ArrayList;
import java.util.Date;

public class Rute {

    private int favorittrute_id;
    private int bruker_id;
    private double from_longitude;
    private double from_latitude;
    private double to_longitude;
    private double to_latitude;
    private int to_place_id;

    public Rute(int favorittrute_id, int bruker_id, double from_longitude, double from_latitude, double to_longitude, double to_latitude, int to_place_id) {
        this.favorittrute_id = favorittrute_id;
        this.bruker_id = bruker_id;
        this.from_longitude = from_longitude;
        this.from_latitude = from_latitude;
        this.to_longitude = to_longitude;
        this.to_latitude = to_latitude;
        this.to_place_id = to_place_id;
    }

    public Rute() {};

    public int getFavorittrute_id() {
        return favorittrute_id;
    }
    public void setFavorittrute_id(int favorittrute_id) {
        this.favorittrute_id = favorittrute_id;
    }

    public int getBruker_id() {
        return bruker_id;
    }
    public void setBruker_id(int bruker_id) {
        this.bruker_id = bruker_id;
    }

    public double getFrom_longitude() {
        return from_longitude;
    }
    public void setFrom_longitude(double from_longitude) {
        this.from_longitude = from_longitude;
    }

    public double getFrom_latitude() {
        return from_latitude;
    }
    public void setFrom_latitude(double from_latitude) {
        this.from_latitude = from_latitude;
    }

    public double getTo_longitude() {
        return to_longitude;
    }
    public void setTo_longitude(double to_longitude) {
        this.to_longitude = to_longitude;
    }

    public double getTo_latitude() {
        return to_latitude;
    }
    public void setTo_latitude(double to_latitude) {
        this.to_latitude = to_latitude;
    }

    public int getTo_place_id() {
        return to_place_id;
    }
    public void setTo_place_id(int to_place_id) {
        this.to_place_id = to_place_id;
    }
}
