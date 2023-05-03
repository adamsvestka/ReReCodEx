package com.adamsvestka.pijl.rerecodex.SwingExtensions;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.border.Border;

public class RoundedBorder implements Border {
    private Color borderColor;
    private Stroke stroke;
    private int thickness;
    private int radius;
    private boolean visible = true;

    public RoundedBorder(Color borderColor, float thickness, int radius) {
        this.borderColor = borderColor;
        this.radius = radius;
        this.thickness = (int) thickness;

        this.stroke = new BasicStroke(thickness);
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (!visible)
            return;

        var g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(stroke);

        g.setColor(borderColor);
        g.drawRoundRect(x + thickness, y + thickness, width - 2 * thickness, height - 2 * thickness, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
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
