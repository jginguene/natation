package fr.natation.pdf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfUtils {
    private final static Logger LOGGER = Logger.getLogger(PdfUtils.class.getName());

    public static void save(String fileName, Component component, int x, int y, float ratio) {
        try {
            PDDocument doc = new PDDocument();
            float POINTS_PER_INCH = 72;
            float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
            PDPage page = new PDPage(new PDRectangle(297 * POINTS_PER_MM, 210 * POINTS_PER_MM));

            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true);

            BufferedImage img = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            component.paint(g);
            g.dispose();

            try {
                ImageIO.write(img, "png", new File("tmp.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            PDImageXObject image = PDImageXObject.createFromFile("tmp.png", doc);
            float width = component.getWidth() * ratio;
            float height = component.getHeight() * ratio;
            contentStream.drawImage(image, x, y, width, height);

            contentStream.close();

            doc.save(fileName);
            doc.close();

            JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a été créé", "Information", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(new File(fileName));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La génération du fichier " + fileName + "  a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La génération du fichier " + fileName + "  a échoué", e);
        }
    }

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
