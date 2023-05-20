package com.adamsvestka.pijl.rerecodex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.format.DateTimeFormatter;

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

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd. MM. yyyy  HH:mm");

    public static String bytesToHumanReadable(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    public App() {
        super("ReReCodEx");
        instance = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);

        mainarea = new JPanel();
        sidebar = new Sidebar<>(App::navigate);

        mainarea.setBackground(ColorPalette.light_gray2);
        mainarea.setLayout(new GridBagLayout());

        Model.getInstance().user.subscribe(this::update);
        update(Model.getInstance().user);

        getContentPane().add(mainarea);
        getContentPane().add(sidebar, BorderLayout.WEST);

        pack();
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static App instance;

    public static void navigate(JPanel panel) {
        instance.sidebar.deselectButtons();
        instance.mainarea.removeAll();
        var c = new GridBagConstraints();
        c.fill = panel.getClass() == LoginPanel.class ? GridBagConstraints.NONE : GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        instance.mainarea.add(panel, c);
        instance.mainarea.revalidate();
        instance.mainarea.repaint();
    }

    private void update(User user) {
        sidebar.clearButtons();

        if (!user.isLoggedIn()) {
            sidebar.addButton("/icons/login.png", "Login", new LoginPanel());
        } else {
            sidebar.addButton("/icons/dashboard.png", "Courses", new CoursePanel());
            // sidebar.addButton(null, "􀈕 Assignments", new JPanel());
            sidebar.addButton("/icons/help.png", "Help", new JPanel());

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
