package no.lambda.client.entur.dto;

public class TripRequestInputDto {
    private String from;
    private String to;
    private String time;
    private boolean arriveBy;

    public TripRequestInputDto(String from, String to, String time, boolean arriveBy) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.arriveBy = arriveBy;
    }

    public String getFrom() {
        return from;
    }

    public boolean isArriveBy() {
        return arriveBy;
    }

    public String getTime() {
        return time;
    }

    public String getTo() {
        return to;
    }
}
