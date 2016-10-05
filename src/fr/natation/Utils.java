package fr.natation;

import java.io.StringWriter;

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

}
