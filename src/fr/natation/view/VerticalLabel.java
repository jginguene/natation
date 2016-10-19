package fr.natation.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class VerticalLabel extends JLabel {
    public final static int ROTATE_RIGHT = 1;

    public final static int DONT_ROTATE = 0;

    public final static int ROTATE_LEFT = -1;

    private int rotation = DONT_ROTATE;

    private boolean painting = false;

    public VerticalLabel() {
        super();
    }

    public VerticalLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public VerticalLabel(Icon image) {
        super(image);
    }

    public VerticalLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public VerticalLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    public VerticalLabel(String text) {
        super(text);
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public boolean isRotated() {
        return this.rotation != DONT_ROTATE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (this.isRotated())
            g2d.rotate(Math.toRadians(90 * this.rotation));
        if (this.rotation == ROTATE_RIGHT)
            g2d.translate(0, -this.getWidth());
        else if (this.rotation == ROTATE_LEFT)
            g2d.translate(-this.getHeight(), 0);
        this.painting = true;

        super.paintComponent(g2d);

        this.painting = false;
        if (this.isRotated())
            g2d.rotate(-Math.toRadians(90 * this.rotation));
        if (this.rotation == ROTATE_RIGHT)
            g2d.translate(-this.getWidth(), 0);
        else if (this.rotation == ROTATE_LEFT)
            g2d.translate(0, -this.getHeight());
    }

    @Override
    public Insets getInsets(Insets insets) {
        insets = super.getInsets(insets);
        if (this.painting) {
            if (this.rotation == ROTATE_LEFT) {
                int temp = insets.bottom;
                insets.bottom = insets.left;
                insets.left = insets.top;
                insets.top = insets.right;
                insets.right = temp;
            } else if (this.rotation == ROTATE_RIGHT) {
                int temp = insets.bottom;
                insets.bottom = insets.right;
                insets.right = insets.top;
                insets.top = insets.left;
                insets.left = temp;
            }
        }
        return insets;
    }

    @Override
    public Insets getInsets() {
        Insets insets = super.getInsets();
        if (this.painting) {
            if (this.rotation == ROTATE_LEFT) {
                int temp = insets.bottom;
                insets.bottom = insets.left;
                insets.left = insets.top;
                insets.top = insets.right;
                insets.right = temp;
            } else if (this.rotation == ROTATE_RIGHT) {
                int temp = insets.bottom;
                insets.bottom = insets.right;
                insets.right = insets.top;
                insets.top = insets.left;
                insets.left = temp;
            }
        }
        return insets;
    }

    @Override
    public int getWidth() {
        if ((this.painting) && (this.isRotated()))
            return super.getHeight();
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        if ((this.painting) && (this.isRotated()))
            return super.getWidth();
        return super.getHeight();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (this.isRotated()) {
            int width = d.width;
            d.width = d.height;
            d.height = width;
        }
        return d;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        if (this.isRotated()) {
            int width = d.width;
            d.width = d.height;
            d.height = width;
        }
        return d;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        if (this.isRotated()) {
            int width = d.width;
            d.width = d.height + 10;
            d.height = width + 10;
        }
        return d;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());
        VerticalLabel label = new VerticalLabel("Testing something");
        VerticalLabel label2 = new VerticalLabel("Testing something");
        VerticalLabel label3 = new VerticalLabel("Testing something");
        label.setIcon(new ImageIcon("shortcut.png"));
        label2.setIcon(new ImageIcon("shortcut.png"));
        label3.setIcon(new ImageIcon("shortcut.png"));
        label.setRotation(VerticalLabel.ROTATE_LEFT);
        label2.setRotation(VerticalLabel.DONT_ROTATE);
        label3.setRotation(VerticalLabel.ROTATE_RIGHT);
        frame.getContentPane().add(label);
        frame.getContentPane().add(label2);
        frame.getContentPane().add(label3);
        frame.pack();
        frame.setVisible(true);
    }
}