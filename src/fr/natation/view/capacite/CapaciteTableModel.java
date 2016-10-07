package fr.natation.view.capacite;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import fr.natation.model.Groupe;
import fr.natation.service.GroupeService;

public class CapaciteTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(CapaciteTableModel.class.getName());
    private final List<Groupe> list;

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_LOGO = 1;
    public static final int COLUMN_NOM = 2;
    public static final int COLUMN_DESCRIPTION = 3;
    public static final int COLUMN_APTITUDE = 1;

    private final String[] columnNames = new String[] { "Id", "Logo", "Nom", "Description", "Aptitudes requise" };

    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[] { Integer.class, String.class, String.class, String.class, String.class };

    public CapaciteTableModel(List<Groupe> list) {
        this.list = list;
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.columnClass[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public int getRowCount() {
        return this.list.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Groupe groupe = this.list.get(rowIndex);

            switch (columnIndex) {
            case COLUMN_ID:
                return groupe.getId();

            case COLUMN_NOM:
                return groupe.getNom();

            case COLUMN_DESCRIPTION:
                return groupe.getDescription();

            case COLUMN_APTITUDE:
                return "- apt1\n- apt2";

            default:
                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException("getValueAt(" + rowIndex + ", " + columnIndex + ")", e);
        }
    }

   

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
