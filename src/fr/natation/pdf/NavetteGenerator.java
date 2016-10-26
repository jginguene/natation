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
    private final static int ELEVE_HEIGHT = 14;

    private final static int MAX_ELEVE_PER_PAGE = 25;

    private final static PdfTextConf SMALL_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(10).setCenter(true);
    private final static PdfTextConf TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true);
    private final static PdfTextConf SMALL_TEXT = new PdfTextConf().setFont(FONT).setFontSize(10).setCenter(true);
    private final static PdfTextConf LEFT_SMALL_TEXT = new PdfTextConf().setFont(FONT).setFontSize(10).setCenter(false);
    private final static PdfTextConf VERTICAL_TEXT = new PdfTextConf().setFont(FONT).setFontSize(8).setCenter(false).setVertical(true);

    private final PDDocument doc = new PDDocument();

    private List<Eleve> eleves = new ArrayList<>();
    private List<Niveau> niveaux = new ArrayList<>();

    public NavetteGenerator(List<Niveau> niveaux, List<Eleve> eleves) {
        this.eleves = eleves;
        this.niveaux = niveaux;

    }

    private void addDoc(Niveau niveau, List<Eleve> elevesForPage, String title) throws Exception {
        try {

            PDPage page = PdfUtils.createLandscapePage();
            this.doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(this.doc, page, AppendMode.APPEND, true);

            int y = START_Y;
            int x = 200;

            int competenceNiveauCount = CompetenceService.get(niveau).size();
            int domaineX = x;
            PdfUtils.writeText(contentStream, x, y, COMPETENCE_WIDTH * competenceNiveauCount, TITLE_HEIGHT, "Niveau " + niveau.getNom() + " - " + title, TITLE);
            x += COMPETENCE_WIDTH * competenceNiveauCount;

            for (Domaine domaine : DomaineService.getAll()) {
                List<Competence> competences = CompetenceService.get(niveau, domaine);

                int competenceNiveauDomaineCount = competences.size();
                PdfUtils.writeText(contentStream, domaineX, y - 20, COMPETENCE_WIDTH * competenceNiveauDomaineCount, TITLE_HEIGHT, domaine.getNom(), SMALL_TEXT);

                int competenceX = domaineX;
                for (Competence competence : competences) {
                    String str = Utils.cutString(competence.getDescription(), 45);
                    PdfUtils.writeText(contentStream, competenceX, y - 20 - COMPETENCE_HEIGHT, COMPETENCE_WIDTH, COMPETENCE_HEIGHT,
                            str, VERTICAL_TEXT);
                    competenceX += COMPETENCE_WIDTH;
                }

                domaineX += COMPETENCE_WIDTH * competenceNiveauDomaineCount;
            }
            this.addEleves(contentStream, niveau, elevesForPage);

            contentStream.close();
        } finally {

        }
    }

    public void addEleves(PDPageContentStream contentStream, Niveau niveau, List<Eleve> eleves) throws Exception {

        int y = START_Y - COMPETENCE_HEIGHT - TITLE_HEIGHT;
        int x = 10;

        int eleveTitleWidth = 140;
        int eleveGroupeWidth = 50;

        PdfUtils.writeText(contentStream, x, y, eleveTitleWidth, TITLE_HEIGHT, "Nom de l'elève", SMALL_TITLE);
        PdfUtils.writeText(contentStream, x + eleveTitleWidth, y, eleveGroupeWidth, TITLE_HEIGHT, "Groupe", SMALL_TITLE);
        y -= ELEVE_HEIGHT;

        int i = 0;
        for (Eleve eleve : eleves) {
            Color color = Color.lightGray;
            if (i % 2 == 0) {
                color = Color.WHITE;
            }

            PdfUtils.writeText(contentStream, x, y, eleveTitleWidth, ELEVE_HEIGHT, eleve.toString(), LEFT_SMALL_TEXT);
            PdfUtils.writeText(contentStream, x + eleveTitleWidth, y, eleveGroupeWidth, ELEVE_HEIGHT, eleve.getGroupeNom(), SMALL_TEXT);

            int competenceX = x + eleveTitleWidth + eleveGroupeWidth;

            for (Domaine domaine : DomaineService.getAll()) {
                for (Competence competence : CompetenceService.get(niveau, domaine)) {

                    String checkCompetence = " ";
                    for (Competence eleveCompetence : eleve.getCompetences(niveau, domaine)) {
                        if (eleveCompetence.getId() == competence.getId()) {
                            checkCompetence = "X";
                        }
                    }

                    PdfUtils.writeText(contentStream, competenceX, y, COMPETENCE_WIDTH, ELEVE_HEIGHT, checkCompetence, SMALL_TEXT);

                    competenceX += COMPETENCE_WIDTH;
                }

            }

            y -= ELEVE_HEIGHT;
            i++;
        }
    }

    public void generate(String filename) throws Exception {

        for (Niveau niveau : this.niveaux) {
            if (this.eleves.size() < MAX_ELEVE_PER_PAGE) {
                this.addDoc(niveau, this.eleves, " 1/1");
            } else {
                int pageCount = 1 + this.eleves.size() / MAX_ELEVE_PER_PAGE;
                for (int i = 1; i <= pageCount; i++) {
                    int startIndex = (i - 1) * MAX_ELEVE_PER_PAGE;
                    int stopIndex = Math.min(i * MAX_ELEVE_PER_PAGE, this.eleves.size());

                    this.addDoc(niveau, this.eleves.subList(startIndex, stopIndex), " " + i + "/" + pageCount);

                }

            }

        }

        this.doc.save(filename);
        this.doc.close();

    }

}
