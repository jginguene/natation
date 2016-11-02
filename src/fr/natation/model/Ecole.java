package fr.natation.model;

public class Ecole {

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String nom;
    private String codePostal;
    private String ville;
    private String adresse;

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return this.ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

}
