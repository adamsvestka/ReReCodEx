package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.adamsvestka.pijl.rerecodex.ColorPalette;

/**
 * A custom JPanel representing a sidebar with selectable buttons for different
 * sections. The sidebar provides options to add and remove buttons with text
 * and icons, manages button states, and provides a callback that returns the
 * associated value of the selected button. It also includes a user card at the
 * top of the sidebar with a profile picture, greeting and image.
 *
 * @param <T> The type of value associated with each SidebarButton, passed to
 *            the callback when a button is selected
 * 
 * @see SidebarButton
 */
public class Sidebar<T> extends JPanel {
    private static final Color color_background = ColorPalette.dark_gray;
    private static final Color color_foreground = ColorPalette.light_gray;
    private static final int padding = 10;
    private static final int width = 200;

    private JPanel container;

    private List<Entry<SidebarButton, T>> buttons = new ArrayList<>();
    private Consumer<T> onChange;

    /**
     * Constructs a new Sidebar component with the given callback. The callback is
     * invoked whenever a button is selected, and is passed the value associated
     * with the button.
     * 
     * @param onChange The callback invoked when a button is selected
     */
    public Sidebar(Consumer<T> onChange) {
        super();

        this.onChange = onChange;

        setBackground(color_background);

        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(width, Integer.MAX_VALUE));

        var userCard = new UserCard();
        container = new JPanel();

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(width, Integer.MAX_VALUE));
        container.setBorder(new EmptyBorder(padding, padding, padding, padding));
        container.setBackground(color_background);

        add(userCard);
        add(container);

        var menuLabel = new JLabel("MENU");

        menuLabel.setForeground(color_foreground);
        menuLabel.setBorder(new EmptyBorder(0, padding, 0, 0));

        container.add(menuLabel);
    }

    /**
     * Removes all buttons from the sidebar.
     */
    public void clearButtons() {
        buttons.clear();
        for (var component : container.getComponents())
            if (component instanceof SidebarButton || component instanceof Box)
                container.remove(component);
    }

    /**
     * Deselects all buttons in the sidebar. This method is useful when navigating
     * to a page that is not represented by a button in the sidebar.
     */
    public void deselectButtons() {
        for (var entry : buttons)
            entry.getKey().setActive(false);
    }

    /**
     * Adds a new button to the sidebar with the given icon, text and value. The
     * value is passed to the callback when the button is selected.
     * 
     * @param icon  The icon to display on the button
     * @param text  The text to display on the button
     * @param value The value associated with the button
     * 
     * @see SidebarButton
     */
    public void addButton(String icon, String text, T value) {
        var button = new SidebarButton(icon, text, e -> {
            var source = (SidebarButton) e.getSource();
            if (source.getActive())
                return;
            for (var entry : buttons) {
                if (entry.getKey() == source)
                    onChange.accept(entry.getValue());
                entry.getKey().setActive(entry.getKey() == source);
            }
        });

        buttons.add(new SimpleEntry<>(button, value));

        container.add(Box.createRigidArea(new Dimension(0, 3)));
        container.add(button);

        if (buttons.size() == 1) {
            onChange.accept(value);
            button.setActive(true);
        }
    }
}
