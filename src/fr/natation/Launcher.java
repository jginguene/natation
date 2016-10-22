package fr.natation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

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

        backup();

        new NatationFrame().setVisible(true);

    }

    public static void backup() throws IOException {
        File backupDir = new File("backup/");
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        String backupFilneName = "backup/" + new SimpleDateFormat("yyyy-MM-dd-HH").format(new Date()) + "_" + "natation.db";
        if (!new File(backupFilneName).exists()) {
            //Files.copy(Paths.get("natation.db"), Paths.get(backupFilneName));

            DiskTools.threadCopy(Paths.get("natation.db"), Paths.get(backupFilneName));
            new File(backupFilneName).setLastModified(new Date().getTime());
            LOGGER.debug("Create backup " + backupFilneName);
        }

        // clean old backup
        File[] files = backupDir.listFiles();

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });

        int maxBackup = 100;
        int currentBackup = 0;
        for (File f : files) {
            if (currentBackup > maxBackup) {
                LOGGER.debug("Delete backup " + backupFilneName);
                f.delete();
            }
            currentBackup++;
        }
    }

}
