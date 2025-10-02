package no.lambda.model;

public abstract class Bruker {

    private int id;
    private String fornavn;
    private String etternavn;
    private String email;

    public Bruker(int id, String fornavn, String etternavn, String email){
        this.id = id;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.email = email;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getFornavn() {return fornavn;}

    public void setFornavn(String fornavn) {this.fornavn = fornavn;}

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getePost() {
        return email;
    }

    public void setePost(String ePost) {
        this.email = ePost;
    }
}
