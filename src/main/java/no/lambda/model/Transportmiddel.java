package no.lambda.model;

public class Transportmiddel {
    private int ID;
    private int antallPassasjerer;
    private int antallTilgjengelig;

    public Transportmiddel(int ID, int antallPassasjerer, int antallTilgjengelig) {
        this.ID = ID;
        this.antallPassasjerer = antallPassasjerer;
        this.antallTilgjengelig = antallTilgjengelig;
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

    public int getAntallTilgjengelig() {
        return antallTilgjengelig;
    }
    public void setAntallTilgjengelig(int antallTilgjengelig) {
        this.antallTilgjengelig = antallTilgjengelig;
    }
}


