package no.lambda.model;

import java.util.ArrayList;
import java.util.Date;

public class Rute {

    private int id;
    private String navn;
    private Date startDatoTid;
    private Date sluttDatoTid;

    public Rute(int id, String navn, Date startDatoTid, Date sluttDatoTid) {
        this.id = id;
        this.navn = navn;
        this.startDatoTid = startDatoTid;
        this.sluttDatoTid = sluttDatoTid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Date getStartDatoTid() {
        return startDatoTid;
    }

    public void setStartDatoTid(Date startDatoTid) {
        this.startDatoTid = startDatoTid;
    }

    public Date getSluttDatoTid() {
        return sluttDatoTid;
    }

    public void setSluttDatoTid(Date sluttDatoTid) {
        this.sluttDatoTid = sluttDatoTid;
    }
}
