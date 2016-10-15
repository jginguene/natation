package fr.natation.model;

import fr.natation.service.CapaciteService;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;

public class Competence {

    private int id;
    private int num;

    private String description;
    private int niveauId;
    private int domaineId;

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getDomaineId() {
        return this.domaineId;
    }

    public void setDomaineId(int domaineId) {
        this.domaineId = domaineId;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Niveau getNiveau() throws Exception {
        if (this.niveauId > 0) {
            return NiveauService.get(this.niveauId);
        } else {
            return null;
        }
    }

    public Capacite getCapacite() throws Exception {
        return CapaciteService.get(this);
    }

    public Domaine getDomaine() throws Exception {
        if (this.domaineId > 0) {
            return DomaineService.get(this.domaineId);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.num + ") " + this.getDescription();
    }

    public String getCapaciteNom() throws Exception {
        Capacite capacite = this.getCapacite();
        if (capacite != null) {
            return capacite.getNom();
        } else {
            return "";
        }
    }

    public String getNiveauNom() throws Exception {
        Niveau niveau = this.getNiveau();
        if (niveau != null) {
            return niveau.getNom();
        } else {
            return "";
        }
    }

    public String getDomaineNom() throws Exception {
        Domaine domaine = this.getDomaine();
        if (domaine != null) {
            return domaine.getNom();
        } else {
            return "";
        }
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
        Competence other = (Competence) obj;
        if (this.id != other.id)
            return false;
        return true;
    }

}
