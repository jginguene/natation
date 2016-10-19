package fr.natation.pdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class PdfUtils {

    public static void createRectangle(PDPageContentStream contentStream, int x, int y, int width, int height, String text, PDFont font, int fontSize, boolean center)
            throws IOException {
        createRectangle(contentStream, x, y, width, height, text, font, fontSize, center, false);
    }

    public static void createRectangle(PDPageContentStream contentStream, int x, int y, int width, int height, String text, PDFont font, int fontSize, boolean center,
            boolean isVertical) throws IOException {
        createRectangle(contentStream, x, y, width, height, text, font, fontSize, center, isVertical, Color.white);
    }

    public static void createRectangle(PDPageContentStream contentStream, int x, int y, int width, int height, String text, PDFont font, int fontSize, boolean center,
            boolean isVertical, Color backgroundColor)
            throws IOException {
        PdfUtils.createRectangle(contentStream, x, y, width, height, backgroundColor);
        int charWidth = 5;

        contentStream.beginText();
        contentStream.setFont(font, fontSize);

        String[] lines = text.split("\n");

        int startY = 0;

        if (!isVertical) {
            startY = height / 3;
        } else {
            startY = 20;
        }

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineX;
            int lineY = y + startY;

            if (!center) {
                lineX = x + 10;
            } else {
                int centerX = (width - charWidth * line.length()) / 2;
                lineX = x + centerX;
            }

            //Calcul empirique avec la lib
            //Sans ça il y a un décalage à partir de la 2eme ligne en vertical
            if (isVertical && i > 0) {
                lineY += 4;
            }

            lineX += 7 * i;
            contentStream.newLineAtOffset(lineX, lineY);

            if (isVertical) {
                contentStream.setTextRotation(Math.PI / 2, lineX, lineY);
            }
            contentStream.showText(line);
        }

        contentStream.endText();

    }

    public static void createRectangle(PDPageContentStream contentStream, int x, int y, int width, int height, Color backgroundColor) throws IOException {
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.addRect(x, y, width, height);
        contentStream.fill();

        contentStream.setNonStrokingColor(backgroundColor);
        contentStream.addRect(x + 1, y + 1, width - 2, height - 2);
        contentStream.fill();
        contentStream.setNonStrokingColor(Color.BLACK);
    }

    public static void createRectangle(PDPageContentStream contentStream, int x, int y, int width, int height) throws IOException {
        createRectangle(contentStream, x, y, width, height, Color.WHITE);
    }

    public static PDPage createLandscapePage() {
        float POINTS_PER_INCH = 72;
        float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
        return new PDPage(new PDRectangle(297 * POINTS_PER_MM, 210 * POINTS_PER_MM));
    }

}
