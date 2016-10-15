package fr.natation.model;

import java.util.List;

import fr.natation.service.CompetenceService;

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

    public List<Competence> getAptitudes() throws Exception {
        return CompetenceService.get(this);
    }

}
