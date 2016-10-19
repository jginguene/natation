package fr.natation.view.eleve;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.model.Eleve;
import fr.natation.pdf.PdfGenerator;
import fr.natation.service.EleveService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.IRefreshListener;

public class ElevePanel extends JPanel implements IRefreshListener, IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final EleveSelectPanel selectPanel = new EleveSelectPanel();
    private final EleveInfoEditPanel editPanel = new EleveInfoEditPanel();
    private final EleveCompetenceAssociationPanel competencePanel = new EleveCompetenceAssociationPanel();

    private final JButton updateButton = ButtonFactory.createUpdateButton();
    private final JButton pdfButton = ButtonFactory.createPdfButton("Créer le diplome de l'élève");
    private final JButton cancelButton = ButtonFactory.createCancelButton("Annuler les modifications");
    private final JButton deleteButton = ButtonFactory.createDeleteButton();

    private Eleve eleve;

    public ElevePanel() throws Exception {
        this.setLayout(new GridBagLayout());

        this.selectPanel.addListener(this.editPanel);
        this.selectPanel.addListener(this.competencePanel);
        this.selectPanel.addListener(this);

        this.selectPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonPanel = new JPanel();

        this.editPanel.setPreferredSize(new Dimension(1000, 100));
        this.competencePanel.setPreferredSize(new Dimension(1000, 500));

        this.add(this.selectPanel, GridBagConstraintsFactory.create(0, 1, 1, 1));
        this.add(this.editPanel, GridBagConstraintsFactory.create(0, 2, 1, 1));
        this.add(this.competencePanel, GridBagConstraintsFactory.create(0, 3, 1, 1));
        this.add(buttonPanel, GridBagConstraintsFactory.create(0, 4, 1, 1));

        buttonPanel.setLayout(new GridLayout(1, 5));

        buttonPanel.add(this.cancelButton);
        buttonPanel.add(this.updateButton);
        buttonPanel.add(this.pdfButton);
        buttonPanel.add(this.deleteButton);

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

        this.deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onDeleteButton();
            }
        });

        this.refresh();
    }

    @Override
    public void refresh() throws Exception {
        this.selectPanel.refresh();
        this.editPanel.refresh();
    }

    private void onUpdateButton() {
        try {
            this.editPanel.updateEleve(this.eleve);
            this.competencePanel.updateEleve(this.eleve);
            EleveService.update(this.eleve);

            this.refresh();

            JOptionPane.showMessageDialog(null, "La mise à jour de " + this.eleve.toString() + " est terminée ", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("L'ajout a échoué", e);
        }
    }

    private void onDeleteButton() {
        int answer = JOptionPane.showConfirmDialog(
                null,
                "Voulez vous supprimer l'élève " + this.eleve.toString() + " de l'application?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                EleveService.delete(this.eleve.getId());
                JOptionPane.showMessageDialog(null, "L'élève " + this.eleve.toString() + " a été supprimé de l'application.", "Information", JOptionPane.INFORMATION_MESSAGE);
                this.refresh();
                LOGGER.info("L'élève " + this.eleve.toString() + " a été supprimé de l'application.");
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("La suppression a échouée", e1);
            }
        }
    }

    private void onCancelButton() {
        this.editPanel.onChange(this.eleve, this);
        this.competencePanel.onChange(this.eleve, this);
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
    public void onChange(Eleve newEleve, Object source) {
        this.eleve = newEleve;
        this.selectPanel.onChange(newEleve, source);
        this.competencePanel.onChange(newEleve, source);
    }

}
