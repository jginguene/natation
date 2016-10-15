package fr.natation.view.capacite;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import fr.natation.model.Capacite;
import fr.natation.model.Competence;
import fr.natation.view.ViewUtils;

public class CapaciteTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(CapaciteTableModel.class.getName());
    private final List<Capacite> list;

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_LOGO = 1;
    public static final int COLUMN_NOM = 2;
    public static final int COLUMN_APTITUDE = 3;

    private final String[] columnNames = new String[] { "Id", "Logo", "Nom", "Aptitudes requise" };

    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[] { Integer.class, ImageIcon.class, String.class, String.class };

    public CapaciteTableModel(List<Capacite> list) {
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
            Capacite capacite = this.list.get(rowIndex);

            switch (columnIndex) {
            case COLUMN_ID:
                return capacite.getId();

            case COLUMN_LOGO:
                return ViewUtils.getCapaciteIcon(capacite, CapaciteListPanel.ROW_HEIGHT);

            case COLUMN_NOM:
                return capacite.getNom();

            case COLUMN_APTITUDE:
                return this.getAptitudesAsString(capacite);

            default:
                return "";
            }
        } catch (Exception e) {
            throw new RuntimeException("getValueAt(" + rowIndex + ", " + columnIndex + ")", e);
        }
    }

    private String getAptitudesAsString(Capacite Capacite) throws Exception {
        String ret = "";
        for (Competence competence : Capacite.getAptitudes()) {
            ret += "[Niveau " + competence.getNiveauNom() + "] [" + competence.getDomaineNom() + "] " + competence.getDescription() + "\n";
        }
        return ret;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
