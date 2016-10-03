package fr.natation;

import org.apache.log4j.Logger;

import fr.natation.view.NatationFrame;

public class Launcher {

    private final static Logger LOGGER = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) throws Exception {
        LOGGER.debug("Start");

        try {
            // javax.swing.UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            LOGGER.error("Failed to load Look and Feel.", e);
        }

        new NatationFrame().setVisible(true);

    }
}
