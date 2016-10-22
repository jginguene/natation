package fr.natation.pdf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import fr.natation.Utils;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;

public class NavetteGenerator {

    private final static PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private final static PDFont FONT = PDType1Font.HELVETICA;

    private final static int START_Y = 550;
    private final static int COMPETENCE_WIDTH = 29;
    private final static int COMPETENCE_HEIGHT = 200;
    private final static int TITLE_HEIGHT = 20;
    private final static int ELEVE_HEIGHT = 12;

    private final PDDocument doc = new PDDocument();

    private List<Eleve> eleves = new ArrayList<>();
    private List<Niveau> niveaux = new ArrayList<>();

    public NavetteGenerator(List<Niveau> niveaux, List<Eleve> eleves) {
        this.eleves = eleves;
        this.niveaux = niveaux;

    }

    private void addDoc(List<Niveau> niveaux) throws Exception {
        try {

            PDPage page = PdfUtils.createLandscapePage();
            this.doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(this.doc, page, AppendMode.APPEND, true);

            int y = START_Y;
            int x = 200;

            for (Niveau niveau : niveaux) {
                int competenceNiveauCount = CompetenceService.get(niveau).size();
                int domaineX = x;
                PdfUtils.createRectangle(contentStream, x, y, COMPETENCE_WIDTH * competenceNiveauCount, TITLE_HEIGHT, "Niveau " + niveau.getNom(), FONT_BOLD, 12, true);
                x += COMPETENCE_WIDTH * competenceNiveauCount;

                for (Domaine domaine : DomaineService.getAll()) {
                    List<Competence> competences = CompetenceService.get(niveau, domaine);

                    int competenceNiveauDomaineCount = competences.size();
                    PdfUtils.createRectangle(contentStream, domaineX, y - 20, COMPETENCE_WIDTH * competenceNiveauDomaineCount, TITLE_HEIGHT, domaine.getNom(), FONT, 10, true);

                    int competenceX = domaineX;
                    for (Competence competence : competences) {
                        String str = Utils.cutString(competence.getDescription(), 45);
                        PdfUtils.createRectangle(contentStream, competenceX, y - 20 - COMPETENCE_HEIGHT, COMPETENCE_WIDTH, COMPETENCE_HEIGHT,
                                str, FONT, 8,
                                false, true);
                        competenceX += COMPETENCE_WIDTH;
                    }

                    domaineX += COMPETENCE_WIDTH * competenceNiveauDomaineCount;
                }
                this.addEleves(contentStream, niveau);

            }

            contentStream.close();
        } finally {

        }
    }

    public void addEleves(PDPageContentStream contentStream, Niveau niveau) throws Exception {

        int y = START_Y - COMPETENCE_HEIGHT - TITLE_HEIGHT;
        int x = 10;

        int eleveTitleWidth = 140;
        int eleveGroupeWidth = 50;

        PdfUtils.createRectangle(contentStream, x, y, eleveTitleWidth, TITLE_HEIGHT, "Nom de l'elève", FONT_BOLD, 10, true, false, Color.WHITE);
        PdfUtils.createRectangle(contentStream, x + eleveTitleWidth, y, eleveGroupeWidth, TITLE_HEIGHT, "Groupe", FONT_BOLD, 10, true, false, Color.WHITE);
        y -= ELEVE_HEIGHT;

        int i = 0;
        for (Eleve eleve : this.eleves) {
            Color color = Color.lightGray;
            if (i % 2 == 0) {
                color = Color.WHITE;
            }

            PdfUtils.createRectangle(contentStream, x, y, eleveTitleWidth, ELEVE_HEIGHT, eleve.toString(), FONT, 8, true, false, color);
            PdfUtils.createRectangle(contentStream, x + eleveTitleWidth, y, eleveGroupeWidth, ELEVE_HEIGHT, eleve.getGroupeNom(), FONT, 8, true, false, color);

            int competenceX = x + eleveTitleWidth + eleveGroupeWidth;

            for (Domaine domaine : DomaineService.getAll()) {
                for (Competence competence : CompetenceService.get(niveau, domaine)) {

                    String checkCompetence = " ";
                    for (Competence eleveCompetence : eleve.getCompetences(niveau, domaine)) {
                        if (eleveCompetence.getId() == competence.getId()) {
                            checkCompetence = "X";
                        }
                    }

                    PdfUtils.createRectangle(contentStream, competenceX, y, COMPETENCE_WIDTH, ELEVE_HEIGHT, checkCompetence, FONT, 8, true, false, color);

                    competenceX += COMPETENCE_WIDTH;
                }

            }

            y -= ELEVE_HEIGHT;
            i++;
        }
    }

    public void generate(String filename) throws Exception {

        List<Niveau> niveauxPage = new ArrayList<Niveau>();

        int i = 1;
        for (Niveau niveau : this.niveaux) {
            niveauxPage.add(niveau);
            if (i % 1 == 0) {
                i = 0;
                this.addDoc(niveauxPage);
                niveauxPage = new ArrayList<Niveau>();
            }
            i++;
        }
        if (!niveauxPage.isEmpty()) {
            this.addDoc(niveauxPage);
        }

        this.doc.save(filename);
        this.doc.close();

    }

}
