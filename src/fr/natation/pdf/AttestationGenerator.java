package fr.natation.pdf;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import fr.natation.Utils;
import fr.natation.model.Eleve;
import fr.natation.model.Etape;
import fr.natation.service.EcoleService;
import fr.natation.service.EtapeService;

public class AttestationGenerator {

    private static PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private static PDFont FONT = PDType1Font.HELVETICA;

    private final static int START_Y = 700;

    private final static PdfTextConf BIG_TITLE = new PdfTextConf().setFont(FONT_BOLD).setBackgroundColor(Color.LIGHT_GRAY).setFontSize(14).setCenter(true);

    private final static PdfTextConf PHOTO = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true).setBorderColor(Color.BLACK);

    private final static PdfTextConf TEXT = new PdfTextConf().setFont(FONT).setFontSize(11).setCenter(false).setLineHeight(10);
    private final static PdfTextConf TITLE = new PdfTextConf().setFont(FONT_BOLD).setBorderColor(Color.WHITE).setFontSize(10).setCenter(false);
    private final static PdfTextConf CENTER_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true);

    private final static PdfTextConf SMALL_CENTER_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(10).setCenter(true).setLineHeight(12);

    private final PDDocument doc = new PDDocument();

    public AttestationGenerator() {

    }

    public void addPage(Eleve eleve) throws Exception {

        //La capacite 8 est la loutre
        List<Etape> etapes = EtapeService.get(8);

        PDPage page = new PDPage();
        this.doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(this.doc, page, AppendMode.APPEND, true);

        int y = START_Y;
        int x = 20;

        PDImageXObject logoSqy = PDImageXObject.createFromFile("img/logo-sqy.png", this.doc);
        contentStream.drawImage(logoSqy, x, y, logoSqy.getWidth() * 80 / logoSqy.getHeight(), 80);
        y -= 30;

        PdfUtils.writeText(contentStream, x, y, 550, 20, "ATTESTATON SCOLAIRE \"SAVOIR NAGER\"           ", BIG_TITLE);
        PdfUtils.writeText(contentStream, x + 450, y - 100, 100, 100, "PHOTO", PHOTO);
        y -= 40;

        PdfUtils.writeText(contentStream, x, y, 150, 20, "NOM Prénom:", CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 150, y, 300, 20, eleve.getNom() + " " + eleve.getPrenom(), CENTER_TITLE);
        y -= 20;

        PdfUtils.writeText(contentStream, x, y, 150, 20, "Né le", CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 150, y, 300, 20, eleve.getDateDeNaissanceAsString(), CENTER_TITLE);
        y -= 20;

        PdfUtils.writeText(contentStream, x, y, 150, 20, "Ecole:", CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 150, y, 300, 20, EcoleService.get().getNom(), CENTER_TITLE);
        y -= 20;

        y -= 30;

        PdfUtils.writeText(contentStream, x + 10, y, 500, 20, "Parours à réaliser en continuité, sans reprise d'appuis au bord du bassin et sans lunettes:", TITLE);

        y -= 20;
        PdfUtils.writeText(contentStream, x + 450, y, 100, 20, "DATE", CENTER_TITLE);

        for (Etape etape : etapes) {
            String text = Utils.cutString(etape.getNum() + " - " + etape.getDescription(), 90);
            int height = 15 * text.split("\n").length;
            y -= height;
            PdfUtils.writeText(contentStream, x, y, 450, height, text, TEXT);
            PdfUtils.writeText(contentStream, x + 450, y, 100, height, "", TEXT);

        }

        y -= 20;
        PdfUtils.writeText(contentStream, x, y, 550, 20, "COMPETENCES VALIDEES PAR:", SMALL_CENTER_TITLE);

        y -= 20;
        PdfUtils.writeText(contentStream, x, y, 250, 20, "LE MNS (1 à 10)", SMALL_CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 250, y, 150, 20, "L'enseignant (11 à 13)", SMALL_CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 400, y, 150, 20, "Le Directeur d'école", SMALL_CENTER_TITLE);

        y -= 20;
        PdfUtils.writeText(contentStream, x, y, 250, 20, "Cachet et signature", SMALL_CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 250, y, 150, 20, "Nom et signature", SMALL_CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 400, y, 150, 20, "Cachet et signature", SMALL_CENTER_TITLE);

        y -= 100;
        PdfUtils.writeText(contentStream, x, y, 250, 100, "", SMALL_CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 250, y, 150, 100, "", SMALL_CENTER_TITLE);
        PdfUtils.writeText(contentStream, x + 400, y, 150, 100, "", SMALL_CENTER_TITLE);

        contentStream.close();

    }

    public void generate(String filename) throws IOException {

        this.doc.save(filename);
        this.doc.close();

    }

}
