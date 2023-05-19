package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;

public class AssignmentCard extends JPanel {
    public static final int height = 40;

    private JLabel name;
    private JLabel points;
    private JLabel maxPoints;
    private JLabel deadline;

    private Assignment assignment;

    public AssignmentCard(Assignment assignment) {
        super();

        this.assignment = assignment;

        setOpaque(false);

        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(0, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        name = new JLabel(assignment.name);
        points = new JLabel("0");
        maxPoints = new JLabel(Integer.toString(assignment.deadlines.get(0).points));
        deadline = new JLabel(
                DateTimeFormatter.ofPattern("dd. MM. yyyy  HH:mm").format(assignment.deadlines.get(0).time));

        points.setPreferredSize(new Dimension(50, 0));
        maxPoints.setPreferredSize(new Dimension(50, 0));

        add(Box.createRigidArea(new Dimension(20, 0)));
        add(name);
        add(Box.createHorizontalGlue());
        add(points);
        add(Box.createRigidArea(new Dimension(100, 0)));
        add(maxPoints);
        add(Box.createRigidArea(new Dimension(100, 0)));
        add(deadline);
        add(Box.createRigidArea(new Dimension(20, 0)));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, height, getWidth() - getInsets().right, height);
    }
}
