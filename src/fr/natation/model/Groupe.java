package fr.natation.model;

import fr.natation.service.EleveService;

public class Groupe {

    private String nom;
    private String description;
    private int id;

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbEleve() throws Exception {
        return EleveService.getNbEleve(this.id);
    }

    @Override
    public String toString() {
        return this.nom;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.nom == null || obj == null) {
            return false;
        }
        return this.nom.equals(((Groupe) obj).getNom());
    }

    @Override
    public int hashCode() {
        return this.nom.hashCode();
    }

}
