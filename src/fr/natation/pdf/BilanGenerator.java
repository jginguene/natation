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

    private final static PdfTextConf GREEN = new PdfTextConf().setFont(FONT_BOLD).setFontSize(12).setCenter(true).setBackgroundColor(Color.GREEN);
    private final static PdfTextConf VERTICAL_TEXT = new PdfTextConf().setFont(FONT).setFontSize(8).setCenter(false).setVertical(true);

    private final static int IMG_HEIGHT = 100;

    private final PDDocument doc = new PDDocument();

    public BilanGenerator() {

    }

    public void addPage(Eleve eleve) throws Exception {

        try {

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
            PdfUtils.writeText(contentStream, x + 250, y, 100, 20, "1", TEXT);

            y -= 20;
            PdfUtils.writeText(contentStream, x + 150, y, 200, 20, "Savoir nager 1", CENTER_TITLE);

            y -= 40;
            for (Domaine domaine : DomaineService.getAll()) {
                PdfUtils.writeText(contentStream, x + 150, y, 170, 20, domaine.getNom(), TITLE);
                PdfUtils.writeText(contentStream, x + 320, y, 30, 20, "", GREEN);
                y -= 20;
            }

            y -= (IMG_HEIGHT + 40);

            PdfUtils.writeText(contentStream, x + 100, y, 300, 50, "Adapter ses déplacements à différents\ntypes d'environnements", SMALL_TITLE);
            ;

            /* contentStream.beginText();
            contentStream.setFont(FONT_BOLD, 12);
            contentStream.newLineAtOffset(100, y);
            contentStream.showText("Prénom:");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(FONT, 12);
            contentStream.newLineAtOffset(150, y);
            contentStream.showText(eleve.getPrenom());
            contentStream.endText();
            
            y -= 20;
            contentStream.beginText();
            contentStream.setFont(FONT_BOLD, 12);
            contentStream.newLineAtOffset(100, y);
            contentStream.showText("Nom:");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(FONT, 12);
            contentStream.newLineAtOffset(150, y);
            contentStream.showText(eleve.getNom());
            contentStream.endText();
            
            float height = 100;
            
            y -= 40;
            PdfUtils.createRectangle(contentStream, x, y, 200, 20);
            
            contentStream.beginText();
            contentStream.setFont(FONT, 12);
            contentStream.newLineAtOffset(x + 10, y + 5);
            contentStream.showText("Compétence");
            contentStream.endText();
            
            x = x + 200;
            PdfUtils.createRectangle(contentStream, x, y, 200, 20);
            
            contentStream.beginText();
            contentStream.setFont(FONT, 12);
            contentStream.newLineAtOffset(x + 10, y + 5);
            contentStream.showText("Niveau");
            contentStream.endText();
            
            y -= 20;
            x = 100;
            PdfUtils.createRectangle(contentStream, x, y, 200, 20);
            x = x + 200;
            PdfUtils.createRectangle(contentStream, x, y, 200, 20);*/

            contentStream.close();
        } finally {

        }
    }

    public void generate(String filename) throws IOException {

        this.doc.save(filename);
        this.doc.close();

    }

}
