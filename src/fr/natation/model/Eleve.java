package fr.natation.model;

import fr.natation.Utils;
import fr.natation.service.GroupeService;

public class Eleve {

    private String nom;
    private String prenom;
    private int groupeId;
    private int id;

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getGroupeId() {
        return this.groupeId;
    }

    public void setGroupeId(int groupeId) {
        this.groupeId = groupeId;
    }

    public Groupe getGroupe() throws Exception {
        if (this.groupeId > 0) {
            return GroupeService.get(this.groupeId);
        } else {
            return null;
        }
    }

    public String getGroupeNom() throws Exception {
        Groupe groupe = this.getGroupe();
        if (groupe != null) {
            return groupe.getNom();
        } else {
            return "";
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

}
