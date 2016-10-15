package fr.natation;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import fr.natation.model.Eleve;

public class PdfGenerator {

    private final PDDocument doc = new PDDocument();

    public PdfGenerator() {

    }

    public void addPage(Eleve eleve) throws IOException {

        try {

            PDPage page = new PDPage();
            this.doc.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromFile("img/Canard.jpg", this.doc);
            PDPageContentStream contentStream = new PDPageContentStream(this.doc, page, AppendMode.APPEND, true);

            PDFont fontBold = PDType1Font.HELVETICA_BOLD;
            PDFont font = PDType1Font.HELVETICA;

            int y = 500;
            int x = 100;

            contentStream.beginText();
            contentStream.setFont(fontBold, 12);
            contentStream.newLineAtOffset(100, y);
            contentStream.showText("Prénom:");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(150, y);
            contentStream.showText(eleve.getPrenom());
            contentStream.endText();

            y -= 20;
            contentStream.beginText();
            contentStream.setFont(fontBold, 12);
            contentStream.newLineAtOffset(100, y);
            contentStream.showText("Nom:");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(150, y);
            contentStream.showText(eleve.getNom());
            contentStream.endText();

            float height = 100;

            contentStream.drawImage(pdImage, 200, 600, pdImage.getWidth() * height / pdImage.getHeight(), height);

            y -= 40;
            createReact(contentStream, x, y, 200, 20);

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(x + 10, y + 5);
            contentStream.showText("Compétence");
            contentStream.endText();

            x = x + 200;
            createReact(contentStream, x, y, 200, 20);

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(x + 10, y + 5);
            contentStream.showText("Niveau");
            contentStream.endText();

            y -= 20;
            x = 100;
            createReact(contentStream, x, y, 200, 20);
            x = x + 200;
            createReact(contentStream, x, y, 200, 20);

            contentStream.close();
        } finally {

        }
    }

    public void generate(String filename) throws IOException {
        this.doc.save(filename);
        this.doc.close();

    }

    private static void createReact(PDPageContentStream contentStream, int x, int y, int width, int height) throws IOException {
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.addRect(x, y, width, height);
        contentStream.fill();

        contentStream.setNonStrokingColor(Color.WHITE);
        contentStream.addRect(x + 1, y + 1, width - 2, height - 2);
        contentStream.fill();

        contentStream.setNonStrokingColor(Color.BLACK);
    }

}
