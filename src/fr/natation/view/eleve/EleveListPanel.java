package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import fr.natation.PdfGenerator;
import fr.natation.model.Eleve;
import fr.natation.service.EleveService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.ButtonFactory;
import fr.natation.view.Icon;
import fr.natation.view.ListPanel;

public class EleveListPanel extends ListPanel {

    private static final long serialVersionUID = 1L;

    private JButton pdfButton;
    private JButton exportButton;

    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    public EleveListPanel() throws Exception {
        super("Liste des élèves");
    }

    @Override
    protected void init(String title) throws Exception {
        super.init(title);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout(1, 5));
        this.pdfButton = ButtonFactory.createPdfButton("Créer les diplomes de tous les élèves");
        this.exportButton = ButtonFactory.createExcelButton();
        panelButton.add(this.pdfButton);
        panelButton.add(this.exportButton);
        panelButton.add(new JLabel());
        panelButton.add(new JLabel());

        this.add(panelButton, BorderLayout.SOUTH);

        this.pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                EleveListPanel.this.onPdfButton();
            }
        });

        this.exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                EleveListPanel.this.onExportButton();
            }
        });
    }

    @Override
    public void refresh() throws Exception {

        EleveTableModel model = new EleveTableModel(EleveService.getAll());
        this.table.setModel(model);

        Action delete = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                table.getSelectedRow();

                int modelRow = Integer.valueOf(e.getActionCommand());
                EleveTableModel model = (EleveTableModel) table.getModel();
                Integer id = (Integer) (model).getValueAt(modelRow, EleveTableModel.COLUMN_ID);

                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez vous supprimer l'élève "
                                + model.getValueAt(modelRow, EleveTableModel.COLUMN_PRENOM)
                                + " "
                                + model.getValueAt(modelRow, EleveTableModel.COLUMN_NOM)
                                + " de l'application?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        EleveService.delete(id.intValue());
                        EleveListPanel.this.refresh();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La suppression a échouée", e1);
                    }
                }
            }
        };

        new ButtonColumn(this.table, delete, EleveTableModel.COLUMN_ACTION, Icon.Delete.getImage(), null);

        this.setColumnWidth(EleveTableModel.COLUMN_ID, 60);
        this.setColumnWidth(EleveTableModel.COLUMN_GROUPE, 60);
        this.setColumnWidth(EleveTableModel.COLUMN_ACTION, 60);
    }

    private void onPdfButton() {
        try {
            PdfGenerator generator = new PdfGenerator();

            String fileName = "diplomes" + ".pdf";
            for (Eleve eleve : EleveService.getAll()) {
                generator.addPage(eleve);
            }
            generator.generate(fileName);
            JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a été créé", "Information", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(new File(fileName));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La génération des diplomes a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La génération des diplomes a échoué", e);
        }
    }

    private void onExportButton() {
        try {

            String fileName = "liste-eleve.csv";
            try {
                EleveListPanel.this.exportTable(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a été créé", "Information", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(new File(fileName));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "L'export a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("L'export  a échoué", e);
        }
    }
}
