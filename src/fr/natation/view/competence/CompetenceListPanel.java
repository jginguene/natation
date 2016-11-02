package fr.natation.view.competence;

import java.awt.Dialog;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import fr.natation.service.CompetenceService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.Icon;
import fr.natation.view.ListPanel;
import fr.natation.view.NatationFrame;

public class CompetenceListPanel extends ListPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(CompetenceListPanel.class.getName());

    public static int ROW_HEIGHT = 25;

    private JButton addCompetenceButton;

    private CompetenceAddPanel addPanel;

    private JDialog dialog;

    public CompetenceListPanel() throws Exception {
        super("Liste des compétences");
    }

    @Override
    protected void init(String title) throws Exception {
        super.init(title);

    }

    @Override
    public void refresh() throws Exception {

        CompetenceTableModel model = new CompetenceTableModel(CompetenceService.getAll());
        this.table.setModel(model);

        Action delete = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                table.getSelectedRow();

                int modelRow = Integer.valueOf(e.getActionCommand());
                CompetenceTableModel model = (CompetenceTableModel) table.getModel();
                Integer id = (Integer) (model).getValueAt(modelRow, CompetenceTableModel.COLUMN_ID);

                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez vous supprimer l'competence "
                                + model.getValueAt(modelRow, CompetenceTableModel.COLUMN_DESC)
                                + " de l'application?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        CompetenceService.delete(id.intValue());
                        CompetenceListPanel.this.refresh();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La suppression a échouée", e1);
                    }
                }
            }
        };

        new ButtonColumn(this.table, delete, CompetenceTableModel.COLUMN_ACTION, fr.natation.view.Icon.Delete.getImage(), null);

        this.setColumnWidth(CompetenceTableModel.COLUMN_NUM, 50);
        this.setColumnWidth(CompetenceTableModel.COLUMN_NIVEAU, 100);
        this.setColumnWidth(CompetenceTableModel.COLUMN_DOMAINE, 100);
        this.setColumnWidth(CompetenceTableModel.COLUMN_CAPACITE, 80);
        this.setColumnWidth(CompetenceTableModel.COLUMN_ACTION, 60);

        this.table.setRowHeight(CompetenceTableModel.ROW_HEIGHT);

        this.hideColumn(CompetenceTableModel.COLUMN_ID);

    }

    private void onAddCompetenceButton() {
        this.getDialog().setVisible(true);
    }

    public void setCompetenceAddPanel(CompetenceAddPanel addPanel) {
        this.addPanel = addPanel;
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
            this.dialog.setTitle("Ajouter un groupe");
        }
        return this.dialog;
    }

}
