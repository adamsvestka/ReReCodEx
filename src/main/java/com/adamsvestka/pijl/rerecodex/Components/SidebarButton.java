package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class SidebarButton extends JButton implements MouseListener {
    private static final Color background = Color.decode("#343a40");
    private static final Color foreground = Color.decode("#c2c7d0");
    private static final Color hoverBackground = Color.decode("#484f58");
    private static final Color hoverForeground = Color.decode("#ffffff");
    private static final Color activeBackground = Color.decode("#28a745");
    private static final Color activeForeground = Color.decode("#ffffff");

    private boolean isActive = false;

    public SidebarButton(String text) {
        super(text);

        initComponents();
    }

    private void initComponents() {
        setBackground(background);
        setForeground(foreground);

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
        if (isActive = !isActive) {
            setBackground(activeBackground);
            setForeground(activeForeground);
        } else {
            setBackground(hoverBackground);
            setForeground(hoverForeground);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!isActive) {
            setBackground(hoverBackground);
            setForeground(hoverForeground);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!isActive) {
            setBackground(background);
            setForeground(foreground);
        }
    }
}
