package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import com.adamsvestka.pijl.rerecodex.ColorPalette;

public class SidebarButton extends JButton implements MouseListener {
    private static final Color color_background = ColorPalette.dark_gray;
    private static final Color color_foreground = ColorPalette.light_gray;
    private static final Color color_background_hover = ColorPalette.dark_gray2;
    private static final Color color_foreground_hover = ColorPalette.white;

    public SidebarButton(String text) {
        super(text);

        setBackground(color_background);
        setForeground(color_foreground);

        setFont(getFont().deriveFont(16f));
        setHorizontalAlignment(LEFT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(this);
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
        setBackground(color_background_hover);
        setForeground(color_foreground_hover);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(color_background);
        setForeground(color_foreground);
    }
}
