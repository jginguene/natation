package fr.natation.view.groupe;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import fr.natation.model.Groupe;
import fr.natation.service.GroupeService;

public class GroupeTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(GroupeTableModel.class.getName());
    private final List<Groupe> list;

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NOM = 1;
    public static final int COLUMN_DESCRIPTION = 2;
    public static final int COLUMN_NB_ELEVE = 3;
    public static final int COLUMN_ACTION = 4;

    private final String[] columnNames = new String[] { "Id", "Nom", "Description", "Nombre d'élèves", "Action" };

    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[] { Integer.class, String.class, String.class, Integer.class, String.class };

    public GroupeTableModel(List<Groupe> list) {
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

            case COLUMN_NB_ELEVE:
                return groupe.getNbEleve();

            default:
                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException("getValueAt(" + rowIndex + ", " + columnIndex + ")", e);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        Groupe groupe = this.list.get(rowIndex);
        if (COLUMN_NOM == columnIndex) {
            groupe.setNom((String) aValue);
            groupe.setDescription((String) aValue);
        }

        try {
            GroupeService.update(groupe);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La mise à jour a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La mise à jour a échouée", e);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}
