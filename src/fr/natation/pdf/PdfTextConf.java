package fr.natation.pdf;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PdfTextConf {

    private PDFont font = PDType1Font.HELVETICA;
    private int fontSize = 12;

    private boolean isVertical = false;
    private boolean isCenter = false;
    private Color borderColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;

    private int lineHeight = 20;

    public int getLineHeight() {
        return this.lineHeight;
    }

    public PdfTextConf setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public PdfTextConf setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    private Color textColor = Color.BLACK;

    public Color getBorderColor() {
        return this.borderColor;
    }

    public PdfTextConf setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public PdfTextConf setTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public PDFont getFont() {
        return this.font;
    }

    public PdfTextConf setFont(PDFont font) {
        this.font = font;
        return this;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public PdfTextConf setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public boolean isVertical() {
        return this.isVertical;
    }

    public PdfTextConf setVertical(boolean isVertical) {
        this.isVertical = isVertical;
        return this;
    }

    public boolean isCenter() {
        return this.isCenter;
    }

    public PdfTextConf setCenter(boolean isCenter) {
        this.isCenter = isCenter;
        return this;
    }

}
