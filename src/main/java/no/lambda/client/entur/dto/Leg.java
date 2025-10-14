package no.lambda.client.entur.dto;

public class Leg {
    public String mode;
    public double distance;
    public Line line;

    @Override
    public String toString() {
        return "Leg{" +
                "mode='" + mode + '\'' +
                ", distance=" + distance +
                ", line=" + line +
                '}';
        }
}
