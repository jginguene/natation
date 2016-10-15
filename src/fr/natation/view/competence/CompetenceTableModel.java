package fr.natation.view.competence;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import fr.natation.model.Competence;
import fr.natation.view.ViewUtils;

public class CompetenceTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_DESC = 1;
    public static final int COLUMN_NUM = 2;
    public static final int COLUMN_DOMAINE = 3;
    public static final int COLUMN_NIVEAU = 4;
    public static final int COLUMN_CAPACITE = 5;
    public static final int COLUMN_ACTION = 6;

    public static final int ROW_HEIGHT = 28;

    private final static Logger LOGGER = Logger.getLogger(CompetenceTableModel.class.getName());
    private final List<Competence> list;

    private final String[] columnNames = new String[] { "Id", "Description", "score", "Niveau", "Type", "Capacité", "Action" };

    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[] { Integer.class, String.class, Integer.class, String.class, String.class, ImageIcon.class, String.class };

    public CompetenceTableModel(List<Competence> list) {
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
            Competence competence = this.list.get(rowIndex);
            switch (columnIndex) {
            case COLUMN_ID:
                return competence.getId();

            case COLUMN_DESC:
                return competence.getDescription();

            case COLUMN_NIVEAU:
                return "Niveau " + competence.getNiveauNom();

            case COLUMN_CAPACITE:
                if (competence.getCapacite() != null) {
                    return ViewUtils.getCapaciteIcon(competence.getCapacite(), CompetenceListPanel.ROW_HEIGHT + 8);
                } else {
                    return new ImageIcon();
                }

            case COLUMN_NUM:
                return competence.getNum();

            case COLUMN_DOMAINE:
                return competence.getDomaineNom();

            default:
                return "";

            }
        } catch (Exception e) {
            throw new RuntimeException("getValueAt(" + rowIndex + ", " + columnIndex + ")", e);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_ACTION;
    }
}
