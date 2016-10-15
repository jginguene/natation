package fr.natation.model;

public class Classe {

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

    public int getAssnId() {
        return this.assnId;
    }

    public void setAssnId(int assnId) {
        this.assnId = assnId;
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
