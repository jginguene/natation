package fr.natation.view.aptitude;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.service.AptitudeService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.ListPanel;

public class AptitudeListPanel extends ListPanel {

    private static final long serialVersionUID = 1L;

    public static int ROW_HEIGHT = 25;

    private final static Logger LOGGER = Logger.getLogger(AptitudeListPanel.class.getName());

    public AptitudeListPanel() throws Exception {
        super("Liste des aptitudes");
    }

    @Override
    public void refresh() throws Exception {

        AptitudeTableModel model = new AptitudeTableModel(AptitudeService.getAll());
        this.table.setModel(model);

        Action delete = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                table.getSelectedRow();

                int modelRow = Integer.valueOf(e.getActionCommand());
                AptitudeTableModel model = (AptitudeTableModel) table.getModel();
                Integer id = (Integer) (model).getValueAt(modelRow, AptitudeTableModel.COLUMN_ID);

                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez vous supprimer l'aptitude "
                                + model.getValueAt(modelRow, AptitudeTableModel.COLUMN_DESC)
                                + " de l'application?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        AptitudeService.delete(id.intValue());
                        AptitudeListPanel.this.refresh();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La suppression a échouée", e1);
                    }
                }
            }
        };

        new ButtonColumn(this.table, delete, AptitudeTableModel.COLUMN_ACTION, Utils.getImage("delete.png"), null);

        this.setColumnWidth(AptitudeTableModel.COLUMN_SCORE, 50);
        this.setColumnWidth(AptitudeTableModel.COLUMN_DESC, 480);
        this.setColumnWidth(AptitudeTableModel.COLUMN_NIVEAU, 50);
        this.setColumnWidth(AptitudeTableModel.COLUMN_TYPE, 100);
        this.setColumnWidth(AptitudeTableModel.COLUMN_CAPACITE, 50);
        this.setColumnWidth(AptitudeTableModel.COLUMN_ACTION, 60);

        this.table.setRowHeight(AptitudeTableModel.ROW_HEIGHT);
        this.hideColumn(AptitudeTableModel.COLUMN_ID);

    }
}
