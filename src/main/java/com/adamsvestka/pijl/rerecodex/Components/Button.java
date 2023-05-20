package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;

/**
 * Button is a custom JButton component that provides a modernized appearance
 * with smooth hover, focus and click effects. The button comes with predefined
 * colors and a box shadow border, giving it a sleek appearance consistent with
 * the ColorPalette used within the application. The focus, hover and active
 * states are automatically handled, resulting in an appealing transition
 * between these states.
 *
 * This class extends the default JButton functionality with custom paint
 * methods and event listeners to implement the desired visual effects. Button
 * can be instantiated with the same parameters as a regular JButton, and also
 * accepts an ActionListener to provide an onClick action.
 *
 * The custom event listeners and paint methods ensure that the visual
 * enhancements do not interfere with the intended functionality of the JButton
 * component.
 * 
 * @see JButton
 * @see ActionListener
 * @see MouseListener
 * @see FocusListener
 */
public class Button extends JButton implements MouseListener, FocusListener {
    private static final Color color_background1 = ColorPalette.green;
    private static final Color color_background2 = ColorPalette.green2;
    private static final Color color_foreground = ColorPalette.white;
    private static final Color color_background_hover1 = ColorPalette.dark_green;
    private static final Color color_background_hover2 = ColorPalette.dark_green2;
    private static final Color color_foreground_hover = ColorPalette.light_gray;
    private static final Color color_background_active1 = ColorPalette.darker_green;
    private static final Color color_background_active2 = ColorPalette.darker_green2;
    private static final Color color_foreground_active = ColorPalette.white;
    private static final Color color_border_focus = ColorPalette.blue;

    private boolean hovered = false;
    private boolean active = false;
    private boolean focused = false;

    /**
     * Constructs a new button with the given text and click listener
     * 
     * @param text    The text displayed on the button
     * @param onClick The action to be performed when the button is clicked
     */
    public Button(String text, ActionListener onClick) {
        super(text);

        setForeground(color_foreground);
        setFont(getFont().deriveFont(16f));
        setBorder(new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 10));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);

        addMouseListener(this);
        addFocusListener(this);
        addActionListener(e -> {
            setEnabled(false);
            onClick.actionPerformed(e);
            setEnabled(true);
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paintBorder(g);

        var g2d = (Graphics2D) g;

        Color[] color;
        if (active) {
            color = new Color[] { color_background_active1, color_background_active2 };
        } else if (focused || hovered) {
            color = new Color[] { color_background_hover1, color_background_hover2 };
        } else {
            color = new Color[] { color_background1, color_background2 };
        }

        var bg = new LinearGradientPaint(
                new Point(0, 0),
                new Point(0, getHeight()),
                new float[] { 0, 1 },
                color);

        if (focused && !hovered) {
            g.setColor(color_border_focus);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2d.setPaint(bg);
            g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
        } else {
            g2d.setPaint(bg);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        }

        super.paintComponent(g);
    }

    @Override
    public void focusGained(FocusEvent e) {
        focused = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
        focused = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        active = true;
        setForeground(color_foreground_active);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        active = false;
        setForeground(hovered ? color_foreground_hover : color_foreground);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hovered = true;
        setForeground(color_foreground_hover);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hovered = false;
        if (!active)
            setForeground(color_foreground);
    }
}
