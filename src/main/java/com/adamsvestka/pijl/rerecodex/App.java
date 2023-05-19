package com.adamsvestka.pijl.rerecodex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.adamsvestka.pijl.rerecodex.Components.Sidebar;
import com.adamsvestka.pijl.rerecodex.Model.Group;
import com.adamsvestka.pijl.rerecodex.Model.Model;
import com.adamsvestka.pijl.rerecodex.Model.User;
import com.adamsvestka.pijl.rerecodex.Panels.CoursePanel;
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
        var c = new GridBagConstraints();
        c.fill = panel.getClass() == LoginPanel.class ? GridBagConstraints.NONE : GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        mainarea.add(panel, c);
        mainarea.revalidate();
        mainarea.repaint();
    }

    private void update(User user) {
        sidebar.clearButtons();

        if (!user.isLoggedIn()) {
            sidebar.addButton("􀻶 Login", new LoginPanel());
        } else {
            sidebar.addButton("􀍾 Courses", new CoursePanel());
            sidebar.addButton("􀈕 Assignments", new JPanel());
            sidebar.addButton("􀁜 Help", new JPanel());

            var mffInstanceId = user.instances.get(0);
            ReCodEx.getInstances(user.id)
                    .thenAcceptBoth(ReCodEx.getGroups(false, mffInstanceId), (instances, groups) -> {
                        Model.getInstance().groups.clear();
                        Model.getInstance().groups.addAll(groups.payload.stream().map(Group::build).toList());
                    }).exceptionally(e -> {
                        e.printStackTrace();
                        return null;
                    });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
