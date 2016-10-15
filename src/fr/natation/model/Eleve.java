
package fr.natation.model;

import fr.natation.service.ClasseService;
import fr.natation.service.CompetenceService;
import fr.natation.service.GroupeService;

public class Eleve {

    private String nom;
    private String prenom;
    private int classeId;

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

    public Classe getClasse() throws Exception {
        if (this.classeId > 0) {
            return ClasseService.get(this.classeId);
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

    public int getClasseId() {
        return this.classeId;
    }

    public void setClasseId(int classeId) {
        this.classeId = classeId;
    }

    public String getClasseNom() throws Exception {
        Classe classe = this.getClasse();
        if (classe != null) {
            return classe.getNom();
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
        return this.prenom + " " + this.nom;// Utils.toString(this);
    }

    public Competence getCompetence(Niveau niveau, Domaine type) throws Exception {
        return CompetenceService.get(this, niveau, type);
    }

}
