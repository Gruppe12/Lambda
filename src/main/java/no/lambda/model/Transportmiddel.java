package no.lambda.model;

public abstract class Transportmiddel {
    private int ID;
    private int antallPassasjerer;

    public Transportmiddel(int ID, int antallPassasjerer) {
        this.ID = ID;
        this.antallPassasjerer = antallPassasjerer;
    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAntallPassasjerer() {
        return antallPassasjerer;
    }
    public void setAntallPassasjerer(int antallPassasjerer) {
        this.antallPassasjerer = antallPassasjerer;
    }

    }


