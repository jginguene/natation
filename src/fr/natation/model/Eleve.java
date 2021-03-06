
package fr.natation.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.natation.service.CapaciteService;
import fr.natation.service.ClasseService;
import fr.natation.service.CompetenceService;
import fr.natation.service.GroupeService;

public class Eleve {

    private String nom;
    private String prenom;
    private Date dateDeNaissance;

    private int classeId;
    private int groupeId;
    private int id;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

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

    public Niveau getRequiredNiveau() throws Exception {
        if (this.getClasse() != null) {
            return this.getClasse().getNiveau();
        }
        return null;
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
        return this.prenom + " " + this.nom;
    }

    public List<Competence> getCompetences(Niveau niveau, Domaine domaine) throws Exception {
        return CompetenceService.get(this, niveau, domaine);
    }

    public List<Competence> getCompetences(Niveau niveau) throws Exception {
        return CompetenceService.get(this, niveau);
    }

    public List<Competence> getCompetences() throws Exception {
        return CompetenceService.get(this);
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
        Eleve other = (Eleve) obj;
        if (this.id != other.id)
            return false;
        return true;
    }

    public Capacite getCapacite() throws Exception {

        List<Competence> competences = this.getCompetences();
        Capacite eleveCapactite = null;
        for (Capacite capacite : CapaciteService.getAll()) {
            if (!capacite.getCompetences().isEmpty() && competences.containsAll(capacite.getCompetences())) {
                if (eleveCapactite == null || eleveCapactite.getId() < capacite.getId()) {
                    eleveCapactite = capacite;
                }
            }
        }
        return eleveCapactite;
    }

    public Assn getAssn() throws Exception {

        List<Competence> competences = this.getCompetences();
        Assn assn = null;
        for (Capacite capacite : CapaciteService.getAll()) {

            if (!capacite.getCompetences().isEmpty() && competences.containsAll(capacite.getCompetences()) && capacite.getAssn() != null) {
                assn = capacite.getAssn();
            }

        }

        return assn;

    }

    public Date getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    public String getDateDeNaissanceAsString() {
        return FORMAT.format(this.dateDeNaissance);
    }

    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

}
