package fr.natation.pdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PdfUtils {

    public static void writeText(PDPageContentStream contentStream, int x, int y, int width, int height, String text, PdfTextConf conf)
            throws IOException {

        PdfUtils.createRectangle(contentStream, x, y, width, height, conf.getBorderColor(), conf.getBackgroundColor());

        contentStream.setNonStrokingColor(conf.getTextColor());

        int charWidth = 5;

        contentStream.setFont(conf.getFont(), conf.getFontSize());

        String[] lines = text.split("\n");

        int startY = 0;

        if (!conf.isVertical()) {
            startY = height / (lines.length + 2);
        } else {
            startY = 20;
        }

        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();
            int lineX;
            int lineY = y - 1 + startY;

            if (!conf.isCenter()) {
                lineX = x + 10;
            } else {
                int centerX = (width - charWidth * line.length()) / 2;
                lineX = x + centerX;
            }

            //Calcul empirique avec la lib
            //Sans ça il y a un décalage à partir de la 2eme ligne en vertical
            if (conf.isVertical() && i > 0) {
                lineY += 4;
            }

            //lineX += 7 * i;
            contentStream.beginText();
            contentStream.newLineAtOffset(lineX, lineY);

            if (conf.isVertical()) {
                contentStream.setTextRotation(Math.PI / 2, lineX, lineY);
            } else {
                y += conf.getLineHeight();
            }

            contentStream.showText(line);
            contentStream.endText();

        }

    }

    public static void createRectangle(PDPageContentStream contentStream, int x, int y, int width, int height, Color borderColor, Color backgroundColor) throws IOException {
        //border color
        contentStream.setNonStrokingColor(borderColor);
        contentStream.addRect(x, y, width, height);
        contentStream.fill();

        //background color
        contentStream.setNonStrokingColor(backgroundColor);
        contentStream.addRect(x + 1, y + 1, width - 2, height - 2);
        contentStream.fill();

    }

    public static PDPage createLandscapePage() {
        float POINTS_PER_INCH = 72;
        float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
        return new PDPage(new PDRectangle(297 * POINTS_PER_MM, 210 * POINTS_PER_MM));
    }

}
