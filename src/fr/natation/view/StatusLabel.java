package fr.natation.view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.natation.model.Status;

public class StatusLabel extends JLabel {

    private static final long serialVersionUID = 1L;
    private final ImageIcon icon;
    private final String text;

    public StatusLabel(Status status, String text) {
        super();
        this.text = text;
        this.icon = this.getStatusIcon(status);

        this.setSize(new Dimension(this.icon.getIconWidth(), this.icon.getIconHeight()));
        this.setPreferredSize(new Dimension(this.icon.getIconWidth(), this.icon.getIconHeight()));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.icon.getImage(), 0, 0, null);

        int x = this.getWidth() / 2 - (5 * this.text.length());
        int y = 5 + this.getHeight() / 2;

        g.drawString(this.text, x, y);

    }

    private ImageIcon getStatusIcon(Status status) {

        switch (status) {

        case Green:
            return Icon.Green.getImage();

        case Blue:
            return Icon.Blue.getImage();

        case Orange:
            return Icon.Orange.getImage();

        default:
            return Icon.Red.getImage();

        }
    }

}
