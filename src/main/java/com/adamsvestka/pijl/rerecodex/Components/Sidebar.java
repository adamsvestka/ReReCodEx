package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Sidebar extends JPanel {
    private List<SidebarButton> buttons;

    private static final int padding = 10;
    private static final Color background = Color.decode("#343a40");

    public Sidebar() {
        super();

        setBackground(background);
        setBorder(new EmptyBorder(padding, padding, padding, padding));

        buttons = List.of(
                new SidebarButton("🏠 Login"),
                new SidebarButton("􀻶 Logout"),
                new SidebarButton("􀍟 Register"));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var menuLabel = new JLabel("MENU");
        menuLabel.setForeground(Color.decode("#c2c7d0"));
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
