package fr.natation.pdf;

import java.awt.Color;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import fr.natation.model.Domaine;
import fr.natation.model.Niveau;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;

public class CompetenceGenerator {

    private static PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private static PDFont FONT = PDType1Font.HELVETICA;

    private final static int START_Y = 750;

    private final static PdfTextConf BIG_TITLE = new PdfTextConf().setFont(FONT_BOLD).setFontSize(8).setCenter(true).setLineHeight(12);

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

    private final static int COLUMN_WIDTH = 85;

    private final PDDocument doc = new PDDocument();

    public CompetenceGenerator() {

    }

    private void createPage() throws Exception {

        List<Niveau> niveaux = NiveauService.getAll();
        List<Domaine> domaines = DomaineService.getAll();

        PDPage page = new PDPage();
        this.doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(this.doc, page, AppendMode.APPEND, true);

        int y = START_Y;
        int x = 20;

        PdfUtils.writeText(contentStream, x, y, 50, 20, "", BIG_TITLE);
        x += 50;

        for (Domaine domaine : domaines) {
            String domaineNom = domaine.getNom();
            if (domaineNom.length() > 20) {
                domaineNom = domaineNom.replaceAll(" ", "\n");
            }

            PdfUtils.writeText(contentStream, x, y, COLUMN_WIDTH, 20, domaineNom, BIG_TITLE);
            x += COLUMN_WIDTH;
        }

        int niveauY = 1;

        for (Niveau niveau : niveaux) {
            String niveauText = "";
            String s = "Niveau " + niveau.getNom();
            for (int i = 0; i < s.length(); i++) {
                niveauText += s.substring(i, i + 1) + "\n";

            }

            System.out.println(niveauText);
            x = 20;
            y -= 100;
            PdfUtils.writeText(contentStream, x, y, 20, 100, niveauText, BIG_TITLE);

            int maxNiveauCompetenceCount = 0;
            for (Domaine domaine : domaines) {

                /* int currentNiveauCompetenceCount = 0;

                y = niveauY;

                List<Competence> competences = CompetenceService.get(niveau, domaine);
                for (Competence competence : competences) {

                    Component competenceComponent = this.createCompetenceComponent(competence, niveau);
                    GridBagConstraints constraint = GridBagConstraintsFactory.create(x, y, 1, 1);
                    constraint.fill = GridBagConstraints.BOTH;
                    panel.add(competenceComponent, constraint);
                    y++;
                    currentNiveauCompetenceCount++;

                }

                if (currentNiveauCompetenceCount < maxCompetencePerNiveau.get(niveau)) {
                    GridBagConstraints constraint = GridBagConstraintsFactory.create(x, y, 1, maxCompetencePerNiveau.get(niveau) - currentNiveauCompetenceCount);
                    constraint.fill = GridBagConstraints.BOTH;
                    JLabel label = new JLabel("");
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.black));
                    label.setHorizontalAlignment(JLabel.CENTER);
                    panel.add(label, constraint);
                }

                x++;*/

            }

            /*  JLabel labelNiveau = this.createNiveauLabel(niveau);
            GridBagConstraints constraint = GridBagConstraintsFactory.create(0, niveauY, 1, maxCompetencePerNiveau.get(niveau));
            constraint.fill = GridBagConstraints.BOTH;
            panel.add(labelNiveau, constraint);

            niveauY += maxCompetencePerNiveau.get(niveau);*/
        }

        /*

        Bilan bilan = new Bilan(eleve);

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
        contentStream.drawImage(niveauStatusImg, x + 410, y + 20, niveauStatusImg.getWidth() * 10 / niveauStatusImg.getHeight(), 10);*/

        contentStream.close();

    }

    public void generate(String filename) throws Exception {
        this.createPage();

        this.doc.save(filename);
        this.doc.close();

    }

}
