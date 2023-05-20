package com.adamsvestka.pijl.rerecodex.SwingExtensions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.border.Border;

/**
 * A border that adds a shadow effect to a component. Note that This border
 * paints inwards meaning it reduces the size of the component.
 * 
 * @see javax.swing.JComponent#getInsets() JComponent.getInsets()
 */
public class BoxShadow implements Border {
    private int blur;
    private int spread2;
    private int cornerRadius;
    private Point offset;
    private Color shadowColor;
    private boolean visible = true;

    private int left, top, leftright, topbottom;

    /**
     * Constructs a new BoxShadow with the given shadow parameters.
     * 
     * @param offsetX      The horizontal offset of the shadow.
     * @param offsetY      The vertical offset of the shadow.
     * @param blur         The blur radius of the shadow effect.
     * @param spread       The spread radius of the shadow effect.
     * @param shadowColor  The color of the shadow.
     * @param cornerRadius The corner radius of the shadow.
     */
    public BoxShadow(int offsetX, int offsetY, int blur, int spread, Color shadowColor, int cornerRadius) {
        this.blur = blur;
        this.shadowColor = new Color(shadowColor.getRed(), shadowColor.getGreen(),
                shadowColor.getBlue(), shadowColor.getAlpha() / (2 * blur + 1));
        this.cornerRadius = cornerRadius;

        offset = new Point(offsetX - spread, offsetY - spread);
        spread2 = spread * 2;

        left = -Math.max(0, blur - offset.x);
        top = -Math.max(0, blur - offset.y);
        leftright = Math.max(0, blur + offset.x + spread2) - left;
        topbottom = Math.max(0, blur + offset.y + spread2) - top;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (!visible)
            return;

        var g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setClip(left, top, width + leftright, height + topbottom);
        g.setColor(shadowColor);

        for (int i = -blur; i <= blur; i++) {
            g.fillRoundRect(offset.x - i, offset.y - i, width + spread2 + 2 * i,
                    height + spread2 + 2 * i, cornerRadius + i, cornerRadius + i);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    /**
     * Sets the visibility of the shadow effect.
     * 
     * @param visible Whether the shadow effect should be visible or not.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns the current visibility of the shadow effect.
     * 
     * @return Whether the shadow effect is currently visible or not.
     */
    public boolean isVisible() {
        return visible;
    }
}