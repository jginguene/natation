package fr.natation.view.eleve;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagLayout;
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
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.IRefreshListener;

public class ElevePanel extends JPanel implements IRefreshListener, IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final EleveSelectPanel selectPanel = new EleveSelectPanel();
    private final EleveInfoEditPanel editPanel = new EleveInfoEditPanel();
    private final EleveAptitudeAssociationPanel aptitudePanel = new EleveAptitudeAssociationPanel();

    private final JButton updateButton = ButtonFactory.createUpdateButton();
    private final JButton pdfButton = ButtonFactory.createPdfButton("Créer le diplome de l'élève");
    private final JButton cancelButton = ButtonFactory.createCancelButton();

    private Eleve eleve;

    public ElevePanel() throws Exception {
        this.setLayout(new GridBagLayout());

        this.selectPanel.addListener(this.editPanel);
        this.selectPanel.addListener(this.aptitudePanel);
        this.selectPanel.addListener(this);
        JPanel buttonPanel = new JPanel();

        this.editPanel.setPreferredSize(new Dimension(1000, 100));
        this.aptitudePanel.setPreferredSize(new Dimension(1000, 500));

        this.add(this.selectPanel, GridBagConstraintsFactory.create(0, 1, 1, 1));
        this.add(this.editPanel, GridBagConstraintsFactory.create(0, 2, 1, 1));
        this.add(this.aptitudePanel, GridBagConstraintsFactory.create(0, 3, 1, 1));
        this.add(buttonPanel, GridBagConstraintsFactory.create(0, 4, 1, 1));

        buttonPanel.setLayout(new GridLayout(1, 5));

        buttonPanel.add(this.cancelButton);
        buttonPanel.add(this.updateButton);
        buttonPanel.add(this.pdfButton);

        this.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onCancelButton();
            }
        });

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
            this.editPanel.updateEleve(this.eleve);
            this.aptitudePanel.updateEleve(this.eleve);
            EleveService.update(this.eleve);

            this.refresh();

            JOptionPane.showMessageDialog(null, "La mise à jour de " + this.eleve.toString() + " est terminée ", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("L'ajout a échoué", e);
        }

    }

    private void onCancelButton() {
        this.editPanel.onChange(this.eleve);
        this.aptitudePanel.onChange(this.eleve);
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
