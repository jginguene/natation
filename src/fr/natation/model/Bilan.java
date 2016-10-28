package fr.natation.model;

import fr.natation.service.CompetenceService;
import fr.natation.service.NiveauService;

public class Bilan {

    private final Eleve eleve;

    public Bilan(Eleve eleve) {
        this.eleve = eleve;
    }

    public Niveau getNiveau() throws Exception {
        Niveau eleveNiveau = null;
        for (Niveau niveau : NiveauService.getAll()) {
            int niveauTotalCompetenceCount = niveau.getCompetencesCount();
            int niveauEleveCompetenceCount = this.eleve.getCompetences(niveau).size();

            float pct = (100 * niveauEleveCompetenceCount) / (niveauTotalCompetenceCount);
            if (pct > 80) {
                eleveNiveau = niveau;
            }

        }
        return eleveNiveau;
    }

    public String getNiveauAsStr() throws Exception {
        Niveau niveau = this.getNiveau();
        if (niveau == null) {
            return "-";
        } else {
            return niveau.getNom();
        }
    }

    public Status getStatus(Niveau niveau, Domaine domaine) throws Exception {

        int eleveCompetenceCount = this.eleve.getCompetences(niveau, domaine).size();
        int totalCompetenceCount = CompetenceService.get(niveau, domaine).size();

        float pct = eleveCompetenceCount / totalCompetenceCount;

        if (pct >= 0.8) {
            return Status.Green;
        } else if (pct >= 0.6) {
            return Status.Blue;
        } else {
            return Status.Red;
        }
    }

    public Status getStatus(Niveau niveau) throws Exception {

        int eleveCompetenceCount = this.eleve.getCompetences(niveau).size();
        int totalCompetenceCount = CompetenceService.get(niveau).size();

        float pct = eleveCompetenceCount / totalCompetenceCount;

        if (pct >= 0.8) {
            return Status.Green;
        } else if (pct >= 0.6) {
            return Status.Blue;
        } else {
            return Status.Red;
        }
    }

}
