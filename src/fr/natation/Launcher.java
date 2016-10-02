package fr.natation;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import fr.natation.view.NatationFrame;

public class Launcher {

    private final static Logger LOGGER = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) throws Exception {
        LOGGER.debug("Start");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.error("Failed to load Look and Feel.", e);
        }

        new NatationFrame().setVisible(true);

    }
}
