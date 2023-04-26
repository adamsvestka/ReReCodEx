package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.adamsvestka.pijl.rerecodex.ColorPalette;

public class Sidebar<E extends Enum<E>> extends JPanel {
    private static final Color color_background = ColorPalette.dark_gray;
    private static final Color color_foreground = ColorPalette.light_gray;
    private static final int padding = 10;

    private Map<SidebarButton, Enum<E>> buttons = new HashMap<>();

    private Consumer<Enum<E>> onChange;

    public Sidebar(Map<String, Enum<E>> buttons, Consumer<Enum<E>> onClick) {
        super();

        this.onChange = onClick;

        for (var entry : buttons.entrySet())
            addButton(entry.getKey(), entry.getValue());

        setBackground(color_background);
        setBorder(new EmptyBorder(padding, padding, padding, padding));

        initComponents();

        var initialButton = this.buttons.keySet().iterator().next();
        initialButton.setActive(true);
        onChange.accept(this.buttons.get(initialButton));
    }

    private void initComponents() {
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var menuLabel = new JLabel("MENU");
        menuLabel.setForeground(color_foreground);
        menuLabel.setBorder(new EmptyBorder(padding, padding, 0, 0));

        add(menuLabel, 0);
    }

    private void addButton(String text, Enum<E> value) {
        var button = new SidebarButton(text, e -> {
            var source = (SidebarButton) e.getSource();
            if (source.getActive())
                return;
            for (var entry : buttons.entrySet())
                entry.getKey().setActive(entry.getKey() == source);
            onChange.accept(buttons.get(source));
        });

        buttons.put(button, value);

        add(Box.createRigidArea(new Dimension(0, 3)));
        add(button);
    }
}
