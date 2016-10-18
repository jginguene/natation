package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dialog;
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
import javax.swing.JDialog;
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
import fr.natation.view.NatationFrame;

public class EleveListPanel extends ListPanel {

    private final static long serialVersionUID = 1L;
    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    private final List<IEleveSelectListener> listeners = new ArrayList<IEleveSelectListener>();

    private JButton pdfButton;
    private JButton exportButton;
    private JButton addEleveButton;
    private Action viewAction;

    private EleveAddPanel addPanel;
    private JDialog dialog;

    public EleveListPanel(boolean isSelectList) throws Exception {
        super("Liste des élèves", isSelectList);
        this.initPanelButton();

    }

    public EleveListPanel() throws Exception {
        super("Liste des élèves");
    }

    @Override
    protected void init(String title) throws Exception {
        super.init(title);
        this.configureTable();
    }

    private void configureTable() {

        this.setColumnWidth(EleveTableModel.COLUMN_ID, 60);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(JLabel.CENTER);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_NOM).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_PRENOM).setCellRenderer(renderer);

        renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(JLabel.CENTER);
        renderer.setHorizontalAlignment(JLabel.CENTER);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_ID).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_GROUPE).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(EleveTableModel.COLUMN_CLASSE).setCellRenderer(renderer);

        if (this.isSelectList) {
            this.setColumnWidth(EleveTableModel.COLUMN_SELECTED, 50);
            this.hideColumn(EleveTableModel.COLUMN_ACTION);
        } else {
            this.initViewAction();
            this.setColumnWidth(EleveTableModel.COLUMN_ACTION, 60);
            this.hideColumn(EleveTableModel.COLUMN_SELECTED);
            new ButtonColumn(this.table, this.viewAction, EleveTableModel.COLUMN_ACTION, Icon.View.getImage(), null);
            this.setRowHeight(28);
        }
    }

    private void initPanelButton() {

        if (!this.isSelectList) {

            JPanel panelButton = new JPanel();
            panelButton.setLayout(new GridLayout(1, 5));
            this.pdfButton = ButtonFactory.createPdfButton("Créer les bilans de tous les élèves");
            this.exportButton = ButtonFactory.createExcelButton();
            this.addEleveButton = ButtonFactory.createAddButton("Ajouter un élève");
            panelButton.add(this.addEleveButton);
            panelButton.add(this.pdfButton);
            panelButton.add(this.exportButton);
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

            this.addEleveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    EleveListPanel.this.onAddEleveButton();
                }
            });

        }

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
        EleveTableModel model = new EleveTableModel(this.getEleveToDisplay());
        this.table.setModel(model);
        this.configureTable();
    }

    protected List<Eleve> getEleveToDisplay() throws Exception {
        return EleveService.getAll();
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

    public void setEleveAddPanel(EleveAddPanel addPanel) {
        this.addPanel = addPanel;
    }

    private void onAddEleveButton() {
        this.getDialog().setVisible(true);
    }

    private JDialog getDialog() {
        if (this.dialog == null) {
            this.dialog = new JDialog(NatationFrame.FRAME, "", Dialog.ModalityType.DOCUMENT_MODAL);
            this.dialog.setIconImage(Icon.Add.getImage().getImage());
            this.addPanel.setDialog(this.dialog);
            this.dialog.add(this.addPanel);
            this.dialog.pack();
            this.dialog.setResizable(false);
            this.dialog.setLocationRelativeTo(NatationFrame.FRAME);
            this.dialog.setTitle("Ajouter un élève");
        }
        return this.dialog;
    }

    public void selectAll() {
        ((EleveTableModel) this.table.getModel()).selectAll();
        this.repaint();
    }

    public void unselectAll() {
        ((EleveTableModel) this.table.getModel()).unselectAll();
        this.repaint();
    }

    public List<Eleve> getSelection() {
        return ((EleveTableModel) this.table.getModel()).getSelection();
    }

}
