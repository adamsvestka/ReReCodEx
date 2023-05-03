package com.adamsvestka.pijl.rerecodex.SwingExtensions;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.Border;

public class RoundedBox implements Border {
    private int radius;
    private boolean visible = true;

    public RoundedBox(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (!visible)
            return;

        var g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(c.getBackground());
        g.fillRoundRect(x, y, width, height, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
