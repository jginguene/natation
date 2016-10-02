package fr.natation.view.eleve;

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

import fr.natation.service.EleveService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.IRefreshListener;

public class EleveListPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    private final JTable table = new JTable();

    public EleveListPanel() throws Exception {
        this.setBorder(BorderFactory.createTitledBorder("Liste des élèves"));
        this.add(new JScrollPane(this.table));
        this.refresh();
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

        new ButtonColumn(this.table, delete, EleveTableModel.COLUMN_ACTION, new ImageIcon("delete.png"), null);
    }
}
