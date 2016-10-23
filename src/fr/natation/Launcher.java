package fr.natation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import fr.natation.view.Icon;
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
        splash();

    }

    public static void splash() throws Exception {
        int width = 500;
        int height = 400;

        JWindow window = new JWindow();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel title = new JLabel("Passeport pour la natation");
        title.setPreferredSize(new Dimension(width, 50));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(65, 135, 215));

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JLabel(Icon.Application.getImage()), BorderLayout.CENTER);
        panel.add(new JLabel(Icon.Splash.getImage()), BorderLayout.SOUTH);
        panel.setBackground(Color.white);

        window.getContentPane().add(panel, SwingConstants.CENTER);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - width) / 2);
        int y = (int) ((dimension.getHeight() - height) / 2);

        window.setBounds(x, y, width, height);
        window.setVisible(true);

        NatationFrame frame = new NatationFrame();
        try {
            while (!frame.isInitialized()) {
                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);

        frame.setVisible(true);
        //window.dispose();
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
