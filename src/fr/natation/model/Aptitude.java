package fr.natation.model;

import fr.natation.service.CapaciteService;
import fr.natation.service.NiveauService;

public class Aptitude {

    private int id;
    private String description;
    private int capaciteId;
    private int niveauId;

    public int getCapaciteId() {
        return this.capaciteId;
    }

    public void setCapaciteId(int capaciteId) {
        this.capaciteId = capaciteId;
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

    public Capacite getCapactite() throws Exception {
        if (this.capaciteId > 0) {
            return CapaciteService.get(this.capaciteId);
        } else {
            return null;
        }
    }

}
