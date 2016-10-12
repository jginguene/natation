package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;

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

    private final List<IEleveSelectListener> listeners = new ArrayList<IEleveSelectListener>();

    private JButton pdfButton;
    private JButton exportButton;
    private Action viewAction;

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

        this.initViewAction();
    }

    private void initViewAction() {
        if (this.viewAction == null) {
            this.viewAction = new AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    EleveTableModel model = (EleveTableModel) EleveListPanel.this.table.getModel();
                    Integer id = (Integer) (model).getValueAt(modelRow, EleveTableModel.COLUMN_ID);
                    Eleve eleve = null;
                    try {
                        eleve = EleveService.get(id.intValue());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "La visualisation de l'eleve " + id + " a echoué", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La visualisation de l'eleve " + id + " a echoué", ex);
                    }

                    for (IEleveSelectListener listener : EleveListPanel.this.listeners) {
                        try {
                            listener.onChange(eleve, this);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "La visualisation de l'eleve " + eleve + " a echoué", "Erreur", JOptionPane.ERROR_MESSAGE);
                            LOGGER.error("La visualisation de l'eleve " + eleve + " a echoué", ex);
                        }
                    }
                }
            };
        }

    }

    @Override
    public void refresh() throws Exception {

        this.initViewAction();

        EleveTableModel model = new EleveTableModel(EleveService.getAll());
        this.table.setModel(model);

        new ButtonColumn(this.table, this.viewAction, EleveTableModel.COLUMN_ACTION, Icon.View.getImage(), null);

        this.setColumnWidth(EleveTableModel.COLUMN_ID, 60);
        this.setColumnWidth(EleveTableModel.COLUMN_GROUPE, 60);
        this.setColumnWidth(EleveTableModel.COLUMN_ACTION, 60);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(JLabel.CENTER);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_ID).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_GROUPE).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_NOM).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_PRENOM).setCellRenderer(renderer);
        this.setRowHeight(28);

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

    public void addListener(IEleveSelectListener listener) {
        this.listeners.add(listener);
    }
}
