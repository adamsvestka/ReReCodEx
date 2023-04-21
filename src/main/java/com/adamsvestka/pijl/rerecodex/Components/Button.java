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

import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;

public class Button extends JButton implements MouseListener {
    private static final Color background1 = Color.decode("#48b461");
    private static final Color background2 = Color.decode("#28a745");
    private static final Color foreground = Color.decode("#c2c7d0");
    private static final Color hoverBackground = Color.decode("#484f58");
    private static final Color hoverForeground = Color.decode("#ffffff");
    private static final Color activeBackground = Color.decode("#28a745");
    private static final Color activeForeground = Color.decode("#ffffff");

    private Consumer<MouseEvent> onClick;

    public Button(String text, Consumer<MouseEvent> onClick) {
        super(text);

        this.onClick = onClick;

        // setBackground(background);
        setForeground(hoverForeground);

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
                new Color[] { background1, background2 });
        ((Graphics2D) g).setPaint(bg);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2d.setComposite(AlphaComposite.DstOver);

        g.setColor(Color.red);
        g.fillRect(0, 0, getWidth(), getHeight());

        g2d.setComposite(origComposite);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setBackground(hoverBackground);
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
        setBackground(hoverBackground);
        // setForeground(hoverForeground);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // setBackground(background);
        // setForeground(foreground);
    }
}
