package no.lambda.client.entur.dto;

import java.util.List;

 public class TripPattern {
    public String expectedStartTime;
    public String expectedEndTime;
    public long duration;
    public double walkDistance;
    public List<Leg> legs;
    public double FromtoLatitude;
    public double FromtoLongitude;
    public double toLatitude;
    public double toLongitude;


     @Override
     public String toString() {
         return "TripPattern{" +
                 "expectedStartTime='" + expectedStartTime + '\'' +
                 ", expectedEndTime='" + expectedEndTime + '\'' +
                 ", duration=" + duration +
                 ", walkDistance=" + walkDistance +
                 ", legs=" + legs +
                 '}';
     }
}

