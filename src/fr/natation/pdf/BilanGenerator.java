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

import fr.natation.model.Bilan;
import fr.natation.model.Capacite;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.model.Status;
import fr.natation.service.DomaineService;

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

    private final static PdfTextConf SMALL_TEXT = new PdfTextConf().setFont(FONT).setFontSize(10).setCenter(false).setLineHeight(12);

    private final static PdfTextConf GREEN = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true).setBackgroundColor(Color.GREEN);
    private final static PdfTextConf RED = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true).setBackgroundColor(Color.RED);
    private final static PdfTextConf VERTICAL_TEXT = new PdfTextConf().setFont(FONT).setFontSize(8).setCenter(false).setVertical(true);

    private final static int IMG_HEIGHT = 100;

    private final PDDocument doc = new PDDocument();

    public BilanGenerator() {

    }

    public void addPage(Eleve eleve) throws Exception {

        Bilan bilan = new Bilan(eleve);

        PDPage page = new PDPage();
        this.doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(this.doc, page, AppendMode.APPEND, true);

        int y = START_Y;
        int x = 20;

        PdfUtils.writeText(contentStream, x, y, 500, 20, "Evaluation des compétences", BIG_TITLE);
        y -= 40;

        PdfUtils.writeText(contentStream, x, y, 500, 20, eleve.toString(), ELEVE_TITLE);
        y -= 20;
        PdfUtils.writeText(contentStream, x, y, 500, 20, eleve.getClasseNom(), ELEVE_TITLE);

        y -= (IMG_HEIGHT + 20);

        Capacite capactite = eleve.getCapacite();
        if (capactite != null) {
            PDImageXObject pdImage = PDImageXObject.createFromFile("img/" + capactite.getNom() + ".png", this.doc);
            contentStream.drawImage(pdImage, 220, y, pdImage.getWidth() * IMG_HEIGHT / pdImage.getHeight(), IMG_HEIGHT);
        }
        y -= 80;

        String capaciteDesc = bilan.getCapaciteFullDesc();

        int height = (1 + capaciteDesc.split("\n").length) * SMALL_TEXT.getLineHeight();

        PdfUtils.writeText(contentStream, x + 100, y, 330, height, capaciteDesc, SMALL_TEXT);
        y -= height;

        String assn = bilan.getAssnAsStr();
        if (assn != null) {
            PdfUtils.writeText(contentStream, x + 150, y, 200, 20, bilan.getAssnAsStr(), CENTER_TITLE);
            y -= 40;
        }

        Niveau requiredNiveau = eleve.getRequiredNiveau();
        for (Domaine domaine : DomaineService.getAll()) {
            Status status = bilan.getStatus(requiredNiveau, domaine);
            PdfUtils.writeText(contentStream, x + 150, y, 190, 20, domaine.getNom(), TITLE);
            PdfUtils.writeText(contentStream, x + 320, y, 30, 20, "", TITLE);
            PDImageXObject pdImage = this.getImage(status);
            contentStream.drawImage(pdImage, x + 330, y + 5, pdImage.getWidth() * 10 / pdImage.getHeight(), 10);

            y -= 20;
        }

        y -= (IMG_HEIGHT + 40);

        PdfUtils.writeText(contentStream, x + 100, y, 300, 50, "Adapter ses déplacements à différents\ntypes d'environnements", SMALL_TITLE);

        Status niveauStatus = bilan.getStatus(requiredNiveau);
        PDImageXObject niveauStatusImg = this.getImage(niveauStatus);

        PdfUtils.writeText(contentStream, x + 400, y, 30, 50, "", TITLE);
        contentStream.drawImage(niveauStatusImg, x + 410, y + 20, niveauStatusImg.getWidth() * 10 / niveauStatusImg.getHeight(), 10);

        contentStream.close();

    }

    public PDImageXObject getImage(Status status) throws IOException {
        switch (status) {
        case Green:
            return PDImageXObject.createFromFile("img/Green.png", this.doc);

        case Blue:
            return PDImageXObject.createFromFile("img/Blue.png", this.doc);

        case Orange:
            return PDImageXObject.createFromFile("img/Orange.png", this.doc);

        default:
            return PDImageXObject.createFromFile("img/Red.png", this.doc);

        }
    }

    public void generate(String filename) throws IOException {

        this.doc.save(filename);
        this.doc.close();

    }

}
