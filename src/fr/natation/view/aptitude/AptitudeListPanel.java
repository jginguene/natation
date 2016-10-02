package fr.natation.view.aptitude;

import java.awt.Dimension;
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

import fr.natation.service.AptitudeService;
import fr.natation.service.EleveService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.IRefreshListener;

public class AptitudeListPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(AptitudeListPanel.class.getName());

    private final JTable table = new JTable();

    public AptitudeListPanel() throws Exception {
        this.setBorder(BorderFactory.createTitledBorder("Liste des aptitudes"));
        this.add(new JScrollPane(this.table));

        JScrollPane pane = new JScrollPane(this.table);

        this.add(pane);

        pane.setPreferredSize(new Dimension(800, 140));

        this.refresh();

    }

    private void setColumnWidth(int column, int Width) {
        this.table.getColumnModel().getColumn(column).setWidth(Width);
        this.table.getColumnModel().getColumn(column).setMinWidth(Width);
        this.table.getColumnModel().getColumn(column).setMaxWidth(Width);
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
                        EleveService.delete(id.intValue());
                        AptitudeListPanel.this.refresh();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La suppression a échouée", e1);
                    }
                }
            }
        };

        new ButtonColumn(this.table, delete, AptitudeTableModel.COLUMN_ACTION, new ImageIcon("delete.png"), null);

        this.setColumnWidth(AptitudeTableModel.COLUMN_ID, 60);
        this.setColumnWidth(AptitudeTableModel.COLUMN_DESC, 500);
        this.setColumnWidth(AptitudeTableModel.COLUMN_ACTION, 60);

    }
}
