package fr.natation.view.groupe;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import fr.natation.service.GroupeService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.IRefreshListener;
import fr.natation.view.IVisibilityManager;
import fr.natation.view.eleve.EleveListPanel;

public class GroupeListPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    private final JTable table = new JTable();

    public GroupeListPanel() throws Exception {
        this.setBorder(BorderFactory.createTitledBorder("Liste des groupes"));
        this.add(new JScrollPane(this.table));
        this.refresh();
    }

    private void hideColumn(int column) {
        this.table.getColumnModel().getColumn(column).setWidth(0);
        this.table.getColumnModel().getColumn(column).setMinWidth(0);
        this.table.getColumnModel().getColumn(column).setMaxWidth(0);
        this.table.getColumnModel().getColumn(column).setHeaderValue("");

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

        new ButtonColumn(this.table, delete, GroupeTableModel.COLUMN_ACTION, new ImageIcon("delete.png"), new IVisibilityManager() {
            @Override
            public boolean isVisible(int row) {
                GroupeTableModel model = (GroupeTableModel) GroupeListPanel.this.table.getModel();
                Integer nbEleve = (Integer) (model).getValueAt(row, GroupeTableModel.COLUMN_NB_ELEVE);
                return nbEleve == 0;
            }
        });

    }
}
