package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.PdfGenerator;
import fr.natation.model.Eleve;
import fr.natation.service.EleveService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.IRefreshListener;

public class ElevePanel extends JPanel implements IRefreshListener, IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final EleveSelectPanel selectPanel = new EleveSelectPanel();
    private final EleveInfoEditPanel editPanel = new EleveInfoEditPanel();

    private final JButton updateButton = ButtonFactory.createUpdateButton();
    private final JButton pdfButton = ButtonFactory.createPdfButton("Créer le diplome de l'élève");

    private Eleve eleve;

    public ElevePanel() throws Exception {
        this.setLayout(new BorderLayout());

        this.selectPanel.addListener(this.editPanel);
        this.selectPanel.addListener(this);

        this.add(this.selectPanel, BorderLayout.PAGE_START);
        this.add(this.editPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));

        this.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(this.updateButton);
        buttonPanel.add(this.pdfButton);

        this.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onUpdateButton();
            }
        });

        this.pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onPdfButton();
            }
        });

        this.refresh();

    }

    @Override
    public void refresh() throws Exception {
        this.selectPanel.refresh();
        this.editPanel.refresh();
    }

    public void onUpdateButton() {
        try {
            EleveService.update(this.eleve);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("L'ajout a échoué", e);
        }

    }

    private void onPdfButton() {
        try {
            PdfGenerator generator = new PdfGenerator();
            generator.addPage(this.eleve);
            String fileName = "diplomes_" + this.eleve.toString().replaceAll(" ", "-") + ".pdf";
            generator.generate(fileName);
            JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a été créé", "Information", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(new File(fileName));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La génération des diplomes a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La génération des diplomes a échoué", e);
        }
    }

    @Override
    public void onChange(Eleve newEleve) {
        this.eleve = newEleve;
    }

}
