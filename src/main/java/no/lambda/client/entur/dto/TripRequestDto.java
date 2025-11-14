package no.lambda.client.entur.dto;

import java.time.OffsetDateTime;

public class TripRequestDto {
    private String fromName;
    private double latitude;
    private double longitude;
    private String toName;

    private String placeId;
    private double toLatitude;
    private double toLongitude;
    private int tripPatterns;
    private OffsetDateTime dateTime;
    private boolean arriveBy;

    public TripRequestDto(String fromName, double latitude, double longitude, String toName, String placeId,
                          double toLatitude, double toLongitude, int tripPatterns, OffsetDateTime dateTime, boolean arriveBy) {
        this.fromName = fromName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.toName = toName;
        this.placeId = placeId;
        this.toLatitude = toLatitude;
        this.toLongitude = toLongitude;
        this.tripPatterns = tripPatterns;
        this.dateTime = dateTime;
        this.arriveBy = arriveBy;
    }
    public boolean isArriveBy() {
        return arriveBy;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public int getTripPatterns() {
        return tripPatterns;
    }

    public double getToLongitude() {
        return toLongitude;
    }

    public double getToLatitude() {
        return toLatitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getToName() {
        return toName;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getFromName() {
        return fromName;
    }
    public void setToLongitude(double toLongitude) {
        this.toLongitude = toLongitude;
    }

    public void setToLatitude(double toLatitude) {
        this.toLatitude = toLatitude;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setTripPatterns(int tripPatterns) {
        this.tripPatterns = tripPatterns;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setArriveBy(boolean arriveBy) {
        this.arriveBy = arriveBy;
    }

}

