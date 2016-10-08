package fr.natation.model;

import fr.natation.Utils;
import fr.natation.service.CapaciteService;
import fr.natation.service.NiveauService;
import fr.natation.service.TypeAptitudeService;

public class Aptitude {

    private int id;
    private int score;

    private String description;
    private int niveauId;
    private int typeId;

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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

    public TypeAptitude getType() throws Exception {
        if (this.typeId > 0) {
            return TypeAptitudeService.get(this.typeId);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return Utils.toString(this);
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

    public String getTypeNom() throws Exception {
        TypeAptitude type = this.getType();
        if (type != null) {
            return type.getNom();
        } else {
            return "";
        }
    }

}
