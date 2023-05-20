package com.adamsvestka.pijl.rerecodex.SwingExtensions;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * VerticalScrollPanel is a JPanel class that implements the Scrollable
 * interface and provides a hotfix feature to prevent JScrollPane from scrolling
 * horizontally. It provides methods to customize scrolling preferences,
 * including unit and block increments, viewport size, and tracking viewport
 * height.
 */
public class VerticalScrollPanel extends JPanel implements Scrollable {
    /**
     * Returns the preferred size of the viewport for this component.
     *
     * @return a <code>Dimension</code> object indicating the preferred viewport
     *         size
     */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    /**
     * Returns the unit increment for scrolling in the specified direction.
     *
     * @param visibleRect a <code>Rectangle</code> object indicating the visible
     *                    area of the viewport
     * @param orientation an integer constant indicating the orientation of the
     *                    scrollbar
     * @param direction   an integer constant indicating the direction of the
     *                    scrollbar movement
     * @return an integer value indicating the unit increment for scrolling in the
     *         specified direction
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    /**
     * Returns the block increment for scrolling in the specified direction.
     *
     * @param visibleRect a <code>Rectangle</code> object indicating the visible
     *                    area of the viewport
     * @param orientation an integer constant indicating the orientation of the
     *                    scrollbar
     * @param direction   an integer constant indicating the direction of the
     *                    scrollbar movement
     * @return an integer value indicating the block increment for scrolling in the
     *         specified direction
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    /**
     * Returns whether or not this component should track the viewport's width. In
     * this implementation, it always tracks the width, so returns true.
     *
     * @return a boolean value indicating whether or not the viewport width should
     *         be tracked
     */
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    /**
     * Returns whether or not this component should track the viewport's height. In
     * this implementation, it does not track the height, so returns false.
     *
     * @return a boolean value indicating whether or not the viewport height should
     *         be tracked
     */
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}