package no.lambda.model;

import java.util.List;

public class SøkResultat {

    private List<Rute> ruter;

    public SøkResultat(List<Rute> ruter) {
        this.ruter = ruter;
    }

    public List<Rute> getRuter() {
        return ruter;
    }

    public void setRuter(List<Rute> ruter) {
        this.ruter = ruter;
    }
}
