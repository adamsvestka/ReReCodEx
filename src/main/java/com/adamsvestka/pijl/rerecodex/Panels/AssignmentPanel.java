package com.adamsvestka.pijl.rerecodex.Panels;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.adamsvestka.pijl.rerecodex.Components.AssignmentDescription;
import com.adamsvestka.pijl.rerecodex.Components.AssignmentStatus;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.VerticalScrollPanel;

public class AssignmentPanel extends JPanel {
    private JPanel container;
    private JScrollPane scrollPane;
    private AssignmentStatus status;
    private AssignmentDescription body;

    public AssignmentPanel(Assignment assignment) {
        super();

        setOpaque(false);

        initComponents();

        assignment.subscribe(this::update);
        update(assignment);
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        container = new VerticalScrollPanel();
        scrollPane = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(LEFT_ALIGNMENT);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);

        status = new AssignmentStatus();
        body = new AssignmentDescription();

        container.add(status);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(body);
    }

    public void update(Assignment assignment) {
        status.update(assignment);
        body.update(assignment);
    }
}
