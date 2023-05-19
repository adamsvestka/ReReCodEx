package com.adamsvestka.pijl.rerecodex.Panels;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.adamsvestka.pijl.rerecodex.Components.AssignmentBody;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.VerticalScrollPanel;

public class AssignmentPanel extends JPanel {
    private JPanel container;
    private JScrollPane scrollPane;
    private AssignmentBody body;

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

        body = new AssignmentBody();

        container.add(body);
    }

    public void update(Assignment assignment) {
        body.update(assignment);
    }
}
