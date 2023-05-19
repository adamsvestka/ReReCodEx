package com.adamsvestka.pijl.rerecodex.Panels;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.adamsvestka.pijl.rerecodex.Components.CourseCard;
import com.adamsvestka.pijl.rerecodex.Model.Group;
import com.adamsvestka.pijl.rerecodex.Model.Model;

public class CoursePanel extends JPanel {
    private JPanel container;
    private JScrollPane scrollPane;

    private List<CourseCard> courses;

    public CoursePanel() {
        super();

        courses = List.of();

        setOpaque(false);

        initComponents();

        Model.getInstance().groups.subscribe(this::update);
        update(Model.getInstance().groups);
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        container = new JPanel();
        scrollPane = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
    }

    private void update(List<Group> groups) {
        try {
            container.removeAll();
            courses = groups.stream().map(CourseCard::new).toList();
            for (int i = 0; i < courses.size(); i++) {
                container.add(courses.get(i));
                if (i < courses.size() - 1)
                    container.add(Box.createRigidArea(new Dimension(0, 20)));
            }

            container.revalidate();
            container.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
