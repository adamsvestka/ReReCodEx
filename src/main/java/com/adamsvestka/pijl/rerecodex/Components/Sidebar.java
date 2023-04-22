package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.adamsvestka.pijl.rerecodex.ColorPalette;

public class Sidebar extends JPanel {
    private static final Color color_background = ColorPalette.dark_gray;
    private static final Color color_foreground = ColorPalette.light_gray;
    private static final int padding = 10;

    private List<SidebarButton> buttons;

    public Sidebar() {
        super();

        setBackground(color_background);
        setBorder(new EmptyBorder(padding, padding, padding, padding));

        buttons = List.of(
                new SidebarButton("🏠 Login"),
                new SidebarButton("􀻶 Logout"),
                new SidebarButton("􀍟 Register"));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var menuLabel = new JLabel("MENU");
        menuLabel.setForeground(color_foreground);
        menuLabel.setBorder(new EmptyBorder(padding, padding, 0, 0));

        add(menuLabel);

        for (var button : buttons) {
            add(Box.createRigidArea(new Dimension(0, 3)));
            add(button);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}
