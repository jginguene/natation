package fr.natation.model;

import java.util.List;

import fr.natation.Utils;
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
            if (this.getStatus(niveau) == Status.Green) {
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

    public String getAssnAsStr() throws Exception {
        Assn assn = this.eleve.getAssn();
        if (assn == null) {
            return null;
        } else {
            return assn.getDescription();
        }

    }

    public String getCapaciteFullDesc() throws Exception {
        Capacite capacite = this.eleve.getCapacite();
        String desc = "";
        if (capacite != null) {
            desc += capacite.getNom() + ":\n";
            for (Competence competence : capacite.getCompetences()) {
                desc += Utils.cutString(competence.getDescription(), 70) + "\n";
            }
        }

        return desc;

    }

    public Status getStatus(Niveau niveau, Domaine domaine) throws Exception {
        float eleveCompetenceCount = this.eleve.getCompetences(niveau, domaine).size();
        return this.getStatus(niveau, domaine, eleveCompetenceCount);
    }

    public Status getStatus(Niveau niveau, Domaine domaine, float eleveCompetenceCount) throws Exception {
        float totalCompetenceCount = CompetenceService.get(niveau, domaine).size();
        float pct = eleveCompetenceCount / totalCompetenceCount;
        return this.getStatus(pct);
    }

    public Status getStatus(Niveau niveau) throws Exception {
        float eleveCompetenceCount = this.eleve.getCompetences(niveau).size();
        return this.getStatus(niveau, eleveCompetenceCount);
    }

    public Status getStatus(Niveau niveau, List<Competence> eleveCompetences) throws Exception {
        float eleveCompetenceCount = 0;
        List<Competence> niveauCompetence = niveau.getCompetences();

        for (Competence competence : eleveCompetences) {
            if (niveauCompetence.contains(competence)) {
                eleveCompetenceCount++;
            }
        }

        Status status = this.getStatus(niveau, eleveCompetenceCount);
        if (status == Status.Green) {
            if (!eleveCompetences.containsAll(niveau.getCapacite().getCompetences())) {
                status = Status.Blue;
            }
        }
        return status;
    }

    public Status getStatus(Niveau niveau, float eleveCompetenceCount) throws Exception {
        float totalCompetenceCount = CompetenceService.get(niveau).size();
        float pct = eleveCompetenceCount / totalCompetenceCount;

        return this.getStatus(pct);

    }

    public Status getStatus(float pct) {

        if (pct >= 0.8) {
            return Status.Green;
        } else if (pct >= 0.6) {
            return Status.Blue;
        } else if (pct >= 0.3) {
            return Status.Orange;
        } else {
            return Status.Red;
        }

    }

}
