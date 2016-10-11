package fr.natation.view.groupe;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.service.GroupeService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.IVisibilityManager;
import fr.natation.view.ListPanel;
import fr.natation.view.eleve.EleveListPanel;

public class GroupeListPanel extends ListPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    public GroupeListPanel() throws Exception {
        super("Liste des groupes");

    }

    @Override
    public void refresh() throws Exception {
        GroupeTableModel model = new GroupeTableModel(GroupeService.getAll());
        this.table.setModel(model);
        this.hideColumn(GroupeTableModel.COLUMN_ID);

        Action delete = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                table.getSelectedRow();

                int modelRow = Integer.valueOf(e.getActionCommand());
                GroupeTableModel model = (GroupeTableModel) table.getModel();
                Integer id = (Integer) (model).getValueAt(modelRow, GroupeTableModel.COLUMN_ID);

                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez vous supprimer le groupe "
                                + model.getValueAt(modelRow, GroupeTableModel.COLUMN_NOM)
                                + " de l'application?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        GroupeService.delete(id.intValue());
                        GroupeListPanel.this.refresh();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La suppression a échouée", e1);
                    }
                }
            }
        };

        new ButtonColumn(this.table, delete, GroupeTableModel.COLUMN_ACTION, Utils.getExternalImage("img/delete.png"), new IVisibilityManager() {
            @Override
            public boolean isVisible(int row) {
                GroupeTableModel model = (GroupeTableModel) GroupeListPanel.this.table.getModel();
                Integer nbEleve = (Integer) (model).getValueAt(row, GroupeTableModel.COLUMN_NB_ELEVE);
                return nbEleve == 0;
            }
        });

        this.setColumnWidth(GroupeTableModel.COLUMN_NOM, 60);
        this.setColumnWidth(GroupeTableModel.COLUMN_NB_ELEVE, 60);
        this.setColumnWidth(GroupeTableModel.COLUMN_ACTION, 60);

    }
}
