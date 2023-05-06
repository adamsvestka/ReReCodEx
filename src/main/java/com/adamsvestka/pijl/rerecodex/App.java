package com.adamsvestka.pijl.rerecodex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.adamsvestka.pijl.rerecodex.Components.Sidebar;
import com.adamsvestka.pijl.rerecodex.Model.Model;
import com.adamsvestka.pijl.rerecodex.Model.User;
import com.adamsvestka.pijl.rerecodex.Panels.DashboardPanel;
import com.adamsvestka.pijl.rerecodex.Panels.LoginPanel;

public class App extends JFrame {
    private JPanel mainarea = new JPanel();
    private Sidebar<JPanel> sidebar;

    public App() {
        super("ReReCodEx");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);

        mainarea = new JPanel();
        sidebar = new Sidebar<>(this::navigate);

        mainarea.setBackground(ColorPalette.light_gray2);
        mainarea.setLayout(new GridBagLayout());

        Model.getInstance().user.subscribe(this::update);
        update(Model.getInstance().user);

        getContentPane().add(mainarea);
        getContentPane().add(sidebar, BorderLayout.WEST);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void navigate(JPanel panel) {
        mainarea.removeAll();
        mainarea.add(panel);
        mainarea.revalidate();
        mainarea.repaint();
    }

    private void update(User user) {
        sidebar.clearButtons();

        if (user.id == null) {
            sidebar.addButton("􀻶 Login", new LoginPanel());
        } else {
            sidebar.addButton("􀍾 Dashboard", new DashboardPanel());
            sidebar.addButton("􀈕 Homeworks", new JPanel());
            sidebar.addButton("􀁜 Help", new JPanel());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
