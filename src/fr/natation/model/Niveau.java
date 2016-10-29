package fr.natation.model;

import java.util.List;

import fr.natation.service.AssnService;
import fr.natation.service.CompetenceService;

public class Niveau {

    private int id;
    private String nom;
    private int assnId;

    public int getAssnId() {
        return this.assnId;
    }

    public void setAssnId(int assnId) {
        this.assnId = assnId;
    }

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

    public Assn getAssn() throws Exception {
        return AssnService.get(this.assnId);
    }

    @Override
    public String toString() {
        return "Niveau " + this.nom;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Niveau other = (Niveau) obj;
        if (this.id != other.id)
            return false;
        return true;
    }

    public List<Competence> getCompetences() throws Exception {
        return CompetenceService.get(this);
    }

    public int getCompetencesCount() throws Exception {
        return this.getCompetences().size();
    }

}
