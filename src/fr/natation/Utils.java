package fr.natation;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import javax.swing.ImageIcon;

import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlWriter;

public class Utils {

    public static String toString(Object o) {
        StringWriter stringWriter = new StringWriter();
        YamlWriter writer = new YamlWriter(stringWriter);

        try {
            writer.write(o);
            writer.close();
        } catch (YamlException e) {
            throw new RuntimeException("toString() failed", e);
        }

        return stringWriter.toString();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String capitalize(String str) {
        if (isBlank(str)) {
            return str;
        } else if (str.length() == 1) {
            return str.toUpperCase();
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }

    public static ImageIcon getImage(String img) {
        return new ImageIcon(Utils.class.getResource("/" + img));
    }

    public static ImageIcon getExternalImage(String img) {
        return new ImageIcon(img);
    }

    public static ImageIcon getExternalImage(String img, int height) {
        ImageIcon icon = new ImageIcon(img);
        int initHeight = icon.getIconHeight();
        int initWidth = icon.getIconWidth();
        return new ImageIcon(icon.getImage().getScaledInstance(initWidth * height / initHeight, height, java.awt.Image.SCALE_SMOOTH));

    }

    public static ImageIcon getImage(String img, int height) {
        ImageIcon icon = new ImageIcon(Utils.class.getResource("/" + img));
        int initHeight = icon.getIconHeight();
        int initWidth = icon.getIconWidth();
        return new ImageIcon(icon.getImage().getScaledInstance(initWidth * height / initHeight, height, java.awt.Image.SCALE_SMOOTH));

    }

    public static String cutStringHtml(String str, int lineLength) {
        if (str.length() < lineLength) {
            return str;
        } else {
            String line = "";
            String ret = "";
            for (String word : str.split(" ")) {
                String newLine = line + " " + word;
                if (newLine.length() < lineLength) {
                    line = newLine;
                } else {
                    ret = ret + " " + line + "<br/>";
                    line = word;
                }
            }
            ret = ret + line;
            return "<html>" + ret;
        }
    }

    public static String cutString(String str, int lineLength) {
        if (str.length() < lineLength) {
            return str;
        } else {
            String line = "";
            String ret = "";
            for (String word : str.split(" ")) {
                String newLine = line + " " + word;
                if (newLine.length() < lineLength) {
                    line = newLine;
                } else {
                    ret = ret + " " + line + "\n";
                    line = word;
                }
            }
            ret = ret + line;
            return ret;
        }
    }

    public static boolean isLock(String fileName) throws Exception {
        File file = new File(fileName);
        FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
        // Get an exclusive lock on the whole file
        FileLock lock = channel.lock();
        boolean ret = false;
        try {
            lock = channel.tryLock();
            return true;
        } catch (OverlappingFileLockException e) {
            // File is open by someone else
        } finally {
            lock.release();
        }
        return ret;
    }

}
