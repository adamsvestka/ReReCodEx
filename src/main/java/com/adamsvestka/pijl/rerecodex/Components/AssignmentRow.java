package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.adamsvestka.pijl.rerecodex.App;
import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;
import com.adamsvestka.pijl.rerecodex.Panels.AssignmentPanel;

/**
 * AssignmentRow is a custom JPanel component that represents a single row in
 * the list of assignments. It displays relevant information about an
 * Assignment, such as its name, points, max points, and deadline. The row is
 * interactive and allows the user to click on it to navigate to the detailed
 * AssignmentPanel view. This class subscribes to updates on the associated
 * Assignment model and updates the displayed information accordingly.
 * 
 * @see Assignment
 * @see AssignmentPanel
 */
public class AssignmentRow extends JPanel implements MouseListener {
    /** The height of the row in pixels. */
    public static final int height = 40;

    private JLabel name;
    private JLabel points;
    private JLabel maxPoints;
    private JLabel deadline;

    private AssignmentPanel assignmentPanel;

    /**
     * Constructs a new AssignmentRow component for the given assignment. The
     * component displays information about the assignment and allows the user to
     * navigate to the detailed AssignmentPanel view.
     * 
     * @param assignment The assignment to display information about
     */
    public AssignmentRow(Assignment assignment) {
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
        deadline.setText(App.dateTimeFormatter.format(assignment.deadlines.get(0).time));
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
