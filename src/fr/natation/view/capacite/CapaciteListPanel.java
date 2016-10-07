package fr.natation.view.capacite;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.service.GroupeService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.IVisibilityManager;
import fr.natation.view.ListPanel;
import fr.natation.view.eleve.EleveListPanel;

public class CapaciteListPanel extends ListPanel implements TableCellRenderer{

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    public CapaciteListPanel() throws Exception {
        super("Liste des capacités");
        

    }

    @Override
    public void refresh() throws Exception {
        CapaciteTableModel model = new CapaciteTableModel(GroupeService.getAll());
        this.table.setModel(model);
        this.hideColumn(CapaciteTableModel.COLUMN_ID);


        this.setColumnWidth(CapaciteTableModel.COLUMN_NOM, 60);
        // this.setColumnWidth(GroupeTableModel.COLUMN_DESCRIPTION, 500);
       // this.setColumnWidth(CapaciteTableModel.COLUMN_NB_ELEVE, 60);
        //this.setColumnWidth(CapaciteTableModel.COLUMN_ACTION, 60);

    }

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}
}
