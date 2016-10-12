package fr.natation.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class NatationMenu extends JMenuBar {

    private final INatationMenuListener listener;

    private final JMenuItem saveMenu = new JMenuItem("Sauvegarder", Icon.SaveBackup.getImage());
    private final JMenuItem loadMenu = new JMenuItem("Charger", Icon.LoadBackup.getImage());
    private final JMenuItem exitMenu = new JMenuItem("Quitter", Icon.Exit.getImage());

    public NatationMenu(INatationMenuListener listener) {
        this.listener = listener;

        JMenu menu = new JMenu("Fichier");
        menu.setMnemonic(KeyEvent.VK_F);
        this.add(menu);

        menu.add(this.saveMenu);
        this.saveMenu.setHorizontalTextPosition(JMenuItem.RIGHT);
        this.saveMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NatationMenu.this.listener.onSave();
            }
        });

        menu.add(this.loadMenu);
        this.loadMenu.setHorizontalTextPosition(JMenuItem.RIGHT);
        this.loadMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NatationMenu.this.listener.onLoad();
            }
        });

        menu.addSeparator();
        menu.add(this.exitMenu);
        this.exitMenu.setHorizontalTextPosition(JMenuItem.RIGHT);
        this.exitMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NatationMenu.this.listener.onExit();
            }
        });

    }

}