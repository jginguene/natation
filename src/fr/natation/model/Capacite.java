package fr.natation.model;

import java.util.List;

import fr.natation.service.AssnService;
import fr.natation.service.CompetenceService;

public class Capacite {

    private int id;
    private int assnId;
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

    public List<Competence> getCompetences() throws Exception {
        return CompetenceService.get(this);
    }

    public int getAssnId() {
        return this.assnId;
    }

    public void setAssnId(int assnId) {
        this.assnId = assnId;
    }

    public Assn getAssn() throws Exception {
        return AssnService.get(this.assnId);
    }

}
