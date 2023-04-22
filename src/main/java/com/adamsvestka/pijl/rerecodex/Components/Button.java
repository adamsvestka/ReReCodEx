package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import javax.swing.JButton;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;

public class Button extends JButton implements MouseListener {
    private static final Color gradient_background1 = ColorPalette.green;
    private static final Color gradient_background2 = ColorPalette.green2;
    private static final Color color_background_hover = ColorPalette.dark_gray2;
    private static final Color color_foreground_hover = ColorPalette.white;

    private Consumer<MouseEvent> onClick;

    public Button(String text, Consumer<MouseEvent> onClick) {
        super(text);

        this.onClick = onClick;

        // setBackground(background);
        setForeground(color_foreground_hover);

        setFont(getFont().deriveFont(16f));

        setBorder(new BoxShadow(0, 0, 4, 0, new Color(0x3f000000, true), 10));
        setOpaque(false);
        // setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.SrcOver);
        super.paintComponent(g);

        var origComposite = g2d.getComposite();

        var bg = new LinearGradientPaint(
                new Point(0, 0),
                new Point(0, getHeight()),
                new float[] { 0, 1 },
                new Color[] { gradient_background1, gradient_background2 });
        ((Graphics2D) g).setPaint(bg);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2d.setComposite(AlphaComposite.DstOver);

        g.setColor(Color.red);
        g.fillRect(0, 0, getWidth(), getHeight());

        g2d.setComposite(origComposite);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setBackground(color_background_hover);
        // setForeground(hoverForeground);

        onClick.accept(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(color_background_hover);
        // setForeground(hoverForeground);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // setBackground(background);
        // setForeground(foreground);
    }
}
