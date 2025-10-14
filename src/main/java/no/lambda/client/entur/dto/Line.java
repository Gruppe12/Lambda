package no.lambda.client.entur.dto;

public class Line {
    public String id;
    public String publicCode;

    @Override
    public String toString() {
        return "Line{" +
                "id='" + id + '\'' +
                ", publicCode='" + publicCode + '\'' +
                '}';
    }
}
