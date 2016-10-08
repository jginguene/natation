package fr.natation.model;

import java.util.List;

import fr.natation.service.AptitudeService;

public class Capacite {

    private int id;
    private String nom;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public List<Aptitude> getAptitudes() throws Exception {
        return AptitudeService.get(this);
    }

}
