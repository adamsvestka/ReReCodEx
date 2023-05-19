package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.adamsvestka.pijl.rerecodex.App;
import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;
import com.adamsvestka.pijl.rerecodex.Panels.AssignmentPanel;

public class AssignmentCard extends JPanel implements MouseListener {
    public static final int height = 40;

    private JLabel name;
    private JLabel points;
    private JLabel maxPoints;
    private JLabel deadline;

    private AssignmentPanel assignmentPanel;

    public AssignmentCard(Assignment assignment) {
        super();

        assignmentPanel = new AssignmentPanel(assignment);

        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        initComponents();

        addMouseListener(this);

        assignment.subscribe(this::update);
        update(assignment);
    }

    private void initComponents() {
        setPreferredSize(new Dimension(0, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        name = new JLabel();
        points = new JLabel();
        maxPoints = new JLabel();
        deadline = new JLabel();

        name.setForeground(ColorPalette.blue);
        points.setPreferredSize(new Dimension(50, 0));
        maxPoints.setPreferredSize(new Dimension(50, 0));
        deadline.setPreferredSize(new Dimension(150, 0));
        deadline.setHorizontalAlignment(SwingConstants.RIGHT);

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

    private void update(Assignment assignment) {
        name.setText(assignment.name);
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(assignment.deadlines.get(0).points));
        if (assignment.bonusPoints != 0) {
            sb.append(assignment.bonusPoints > 0 ? " + " : " - ");
            sb.append(Math.abs(assignment.bonusPoints));
        }
        points.setText(sb.toString());
        maxPoints.setText(Integer.toString(assignment.deadlines.get(0).points));
        deadline.setText(
                DateTimeFormatter.ofPattern("dd. MM. yyyy  HH:mm").format(assignment.deadlines.get(0).time));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, height, getWidth() - getInsets().right, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        App.navigate(assignmentPanel);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override

    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
