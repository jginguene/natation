package fr.natation.model;

import fr.natation.service.AssnService;
import fr.natation.service.NiveauService;

public class Classe {

    private int id;
    private int niveauId;
    private String nom;

    public Niveau getNiveau() throws Exception {
        return NiveauService.get(this.niveauId);
    }

    public int getNiveauId() {
        return this.niveauId;
    }

    public void setNiveauId(int niveauId) {
        this.niveauId = niveauId;
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



    @Override
    public String toString() {
        return this.nom;
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
        Classe other = (Classe) obj;
        if (this.id != other.id)
            return false;
        return true;
    }

}
