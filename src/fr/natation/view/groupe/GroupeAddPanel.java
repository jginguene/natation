package fr.natation.view.groupe;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Groupe;
import fr.natation.service.GroupeService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.IRefreshListener;

public class GroupeAddPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(GroupeAddPanel.class.getName());

    private final JLabel labelNom = new JLabel("Nom:");
    private final JLabel labelDescription = new JLabel("Description:");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputDescription = new JTextField(40);

    private final JButton addButton = ButtonFactory.createCreateButton();;

    private IRefreshListener listener;

    public GroupeAddPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Ajouter un groupe"));
        this.refreshGroupes();

        JPanel panel = new JPanel();
        this.setLayout(new BorderLayout());

        this.add(panel, BorderLayout.WEST);

        panel.setLayout(new GridBagLayout());

        int y = 0;
        panel.add(this.labelNom, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputNom, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.labelDescription, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputDescription, GridBagConstraintsFactory.create(1, y, 2, 1));
        y++;

        panel.add(this.addButton, GridBagConstraintsFactory.create(0, y, 2, 1));

        this.addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                GroupeAddPanel.this.onAddButton();
            }
        });

        this.setVisible(true);

    }

    public void onAddButton() {
        if (this.listener != null) {
            try {
                String error = "";
                if (Utils.isBlank(this.inputNom.getText())) {
                    error += "La saisie du nom est obligatoire.\n";
                }

                if (Utils.isBlank(this.inputDescription.getText())) {
                    error += "La saisie du description est obligatoire.\n";
                }

                if (!Utils.isBlank(error)) {
                    JOptionPane.showMessageDialog(null, error, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Groupe groupe = new Groupe();
                groupe.setNom(this.inputNom.getText());
                groupe.setDescription(this.inputDescription.getText());

                GroupeService.create(groupe);

                this.listener.refresh();

                this.inputNom.setText("");
                this.inputDescription.setText("");

                this.refreshGroupes();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("L'ajout a échoué", e);
            }
        }
    }

    private void refreshGroupes() throws Exception {
        // List<Groupe> groupes = GroupeService.getAll();
        // GroupeComboboxModel model = new GroupeComboboxModel(groupes);
        // this.inputGroupe.setModel(model);
    }

    public void addListener(IRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void refresh() throws Exception {
        // TODO Auto-generated method stub

    }

}
