package fr.natation.pdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import fr.natation.model.Capacite;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;

public class BilanGenerator {

    private static PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private static PDFont FONT = PDType1Font.HELVETICA;

    private final static int START_Y = 750;

    private final static PdfTextConf BIG_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(14).setCenter(true);
    private final static PdfTextConf ELEVE_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true).setBorderColor(Color.WHITE);
    private final static PdfTextConf SMALL_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true);
    private final static PdfTextConf TEXT = new PdfTextConf().setFont(FONT).setFontSize(12).setCenter(true);
    private final static PdfTextConf TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(false);
    private final static PdfTextConf CENTER_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true);

    private final static PdfTextConf GREEN = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true).setBackgroundColor(Color.GREEN);
    private final static PdfTextConf RED = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true).setBackgroundColor(Color.RED);
    private final static PdfTextConf VERTICAL_TEXT = new PdfTextConf().setFont(FONT).setFontSize(8).setCenter(false).setVertical(true);

    private final static int IMG_HEIGHT = 100;

    private final PDDocument doc = new PDDocument();

    public BilanGenerator() {

    }

    public void addPage(Eleve eleve) throws Exception {

        PDPage page = new PDPage();
        this.doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(this.doc, page, AppendMode.APPEND, true);

        int y = START_Y;
        int x = 20;

        PdfUtils.writeText(contentStream, x, y, 500, 20, "Evaluation des compétences", BIG_TITLE);
        y -= 40;

        PdfUtils.writeText(contentStream, x, y, 500, 20, eleve.toString(), ELEVE_TITLE);
        y -= (IMG_HEIGHT + 40);

        Capacite capactite = eleve.getCapacite();
        if (capactite != null) {
            PDImageXObject pdImage = PDImageXObject.createFromFile("img/" + capactite.getNom() + ".png", this.doc);
            contentStream.drawImage(pdImage, 220, y, pdImage.getWidth() * IMG_HEIGHT / pdImage.getHeight(), IMG_HEIGHT);
        }
        y -= IMG_HEIGHT;

        PdfUtils.writeText(contentStream, x + 150, y, 100, 20, "Niveau atteint", TITLE);

        String eleveNiveau = "-";
        for (Niveau niveau : NiveauService.getAll()) {
            int niveauTotalCompetenceCount = niveau.getCompetencesCount();
            int niveauEleveCompetenceCount = eleve.getCompetences(niveau).size();

            float pct = (100 * niveauEleveCompetenceCount) / (niveauTotalCompetenceCount);
            if (pct > 80) {
                eleveNiveau = niveau.getNom();
            }

        }

        PdfUtils.writeText(contentStream, x + 250, y, 100, 20, eleveNiveau, TEXT);

        y -= 20;
        PdfUtils.writeText(contentStream, x + 150, y, 200, 20, "Savoir nager 1", CENTER_TITLE);

        y -= 40;
        for (Domaine domaine : DomaineService.getAll()) {

            Niveau requiredNiveau = eleve.getClasse().getNiveau();

            int eleveCompetenceCount = eleve.getCompetences(requiredNiveau, domaine).size();
            int totalCompetenceCount = CompetenceService.get(requiredNiveau, domaine).size();

            float pct = eleveCompetenceCount / totalCompetenceCount;

            PdfUtils.writeText(contentStream, x + 150, y, 170, 20, domaine.getNom(), TITLE);
            PdfUtils.writeText(contentStream, x + 320, y, 30, 20, "", TITLE);

            PDImageXObject pdImage = null;
            if (pct >= 0.8) {
                pdImage = PDImageXObject.createFromFile("img/Green.png", this.doc);
            } else if (pct >= 0.6) {
                pdImage = PDImageXObject.createFromFile("img/Blue.png", this.doc);
            } else {
                pdImage = PDImageXObject.createFromFile("img/Red.png", this.doc);
            }

            contentStream.drawImage(pdImage, x + 330, y + 5, pdImage.getWidth() * 10 / pdImage.getHeight(), 10);

            y -= 20;
        }

        y -= (IMG_HEIGHT + 40);

        PdfUtils.writeText(contentStream, x + 100, y, 300, 50, "Adapter ses déplacements à différents\ntypes d'environnements", SMALL_TITLE);

        contentStream.close();

    }

    public void generate(String filename) throws IOException {

        this.doc.save(filename);
        this.doc.close();

    }

}
