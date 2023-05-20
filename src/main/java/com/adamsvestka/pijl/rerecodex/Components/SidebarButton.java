package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import com.adamsvestka.pijl.rerecodex.ColorPalette;

/**
 * SidebarButton is a custom Swing JButton component specifically designed for
 * use in the sidebar of an application. It features a unique appearance, hover
 * states, and active states to indicate the user's current selection in the
 * sidebar. The component adjusts its colors and appearance according to the
 * user interactions and active state changes. This component can be used with
 * optional icons and customizable text and supports different action events.
 * 
 * @see JButton
 * @see ActionListener
 */
public class SidebarButton extends JButton implements MouseListener {
    private static final Color color_background = ColorPalette.dark_gray;
    private static final Color color_foreground = ColorPalette.light_gray;
    private static final Color color_background_hover = ColorPalette.dark_gray2;
    private static final Color color_foreground_hover = ColorPalette.white;
    private static final Color color_background_active = ColorPalette.green2;
    private static final Color color_foreground_active = ColorPalette.white;
    // private static final int image_size = 20;

    private boolean active = false;

    /**
     * Constructs a new SidebarButton with the given text and click listener.
     * 
     * <p>
     * The iconUrl parameter is currently not used.
     * </p>
     * 
     * @param iconUrl The URL of the icon to display on the button <b>[NOT USED]</b>
     * @param text    The text to display on the button
     * @param onClick The click listener to invoke when the button is clicked
     */
    public SidebarButton(String iconUrl, String text, ActionListener onClick) {
        super(text);

        // if (iconUrl != null) {
        //     try {
        //         InputStream url = getClass().getResourceAsStream(iconUrl);
        //         Image image = ImageIO.read(url).getScaledInstance(image_size, image_size,
        //                 BufferedImage.SCALE_SMOOTH);
        //         ImageIcon icon = new ImageIcon(image);
        //         setIcon(icon);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }

        setBackground(color_background);
        setForeground(color_foreground);

        setFont(getFont().deriveFont(16f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        setHorizontalAlignment(LEFT);

        setOpaque(false);
        setBorderPainted(false);
        setFocusable(false);

        addMouseListener(this);
        addActionListener(onClick);
    }

    /**
     * Gets the active state of the button.
     *
     * @return The active state of the button
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Sets the active state of the button.
     *
     * @param active The active state of the button
     */
    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            setForeground(color_foreground_active);
            setBackground(color_background_active);
        } else {
            setForeground(color_foreground);
            setBackground(color_background);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!active) {
            setForeground(color_foreground_hover);
            setBackground(color_background_hover);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!active) {
            setForeground(color_foreground);
            setBackground(color_background);
        }
    }
}
