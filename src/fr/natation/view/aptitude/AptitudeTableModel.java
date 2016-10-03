package fr.natation.view.aptitude;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import fr.natation.model.Aptitude;
import fr.natation.service.AptitudeService;

public class AptitudeTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_DESC = 1;
    public static final int COLUMN_NIVEAU = 2;
    public static final int COLUMN_TYPE = 3;
    public static final int COLUMN_CAPACITE = 4;
    public static final int COLUMN_ACTION = 5;

    private final static Logger LOGGER = Logger.getLogger(AptitudeTableModel.class.getName());
    private final List<Aptitude> list;

    private final String[] columnNames = new String[] { "Id", "Description", "Niveau", "Type", "Capacité", "Action" };

    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class };

    public AptitudeTableModel(List<Aptitude> list) {
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
            Aptitude aptitude = this.list.get(rowIndex);
            switch (columnIndex) {
            case COLUMN_ID:
                return aptitude.getId();

            case COLUMN_DESC:
                return aptitude.getDescription();

            case COLUMN_NIVEAU:
                return aptitude.getNiveauNom();

            case COLUMN_CAPACITE:
                return aptitude.getCapaciteNom();

            case COLUMN_TYPE:
                return aptitude.getTypeNom();

            default:
                return "";

            }
        } catch (Exception e) {
            throw new RuntimeException("getValueAt(" + rowIndex + ", " + columnIndex + ")", e);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        Aptitude aptitude = this.list.get(rowIndex);

        if (COLUMN_DESC == columnIndex) {
            aptitude.setDescription((String) aValue);
        }

        try {
            AptitudeService.update(aptitude);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La mise à jour a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La mise à jour a échouée", e);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_DESC || columnIndex == COLUMN_ACTION;
    }
}