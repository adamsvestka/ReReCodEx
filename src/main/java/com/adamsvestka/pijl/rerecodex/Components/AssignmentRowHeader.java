package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.adamsvestka.pijl.rerecodex.ColorPalette;

/**
 * AssignmentRowHeader represents the header row for a table listing
 * assignments. This custom JPanel displays the assignment's name, points, max
 * points, and deadline column headers.
 * 
 * @see AssignmentRow
 */
public class AssignmentRowHeader extends JPanel {
    /**
     * The height of the component.
     * 
     * @see AssignmentRow#height
     */
    public static final int height = AssignmentRow.height;

    private JLabel name;
    private JLabel points;
    private JLabel maxPoints;
    private JLabel deadline;

    /**
     * Constructs a new AssignmentRowHeader component. The component displays the
     * assignment's name, points, max points, and deadline column headers.
     */
    public AssignmentRowHeader() {
        super();

        setOpaque(false);

        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(0, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        name = new JLabel("Name");
        points = new JLabel("Points");
        maxPoints = new JLabel("Max points");
        deadline = new JLabel("Deadline", SwingConstants.CENTER);

        name.setFont(name.getFont().deriveFont(Font.BOLD));
        points.setFont(points.getFont().deriveFont(Font.BOLD));
        maxPoints.setFont(maxPoints.getFont().deriveFont(Font.BOLD));
        deadline.setFont(deadline.getFont().deriveFont(Font.BOLD));

        points.setPreferredSize(new Dimension(100, 0));
        maxPoints.setPreferredSize(new Dimension(100, 0));
        deadline.setPreferredSize(new Dimension(150, 0));

        add(Box.createRigidArea(new Dimension(20, 0)));
        add(name);
        add(Box.createHorizontalGlue());
        add(points);
        add(Box.createRigidArea(new Dimension(50, 0)));
        add(maxPoints);
        add(Box.createRigidArea(new Dimension(50, 0)));
        add(deadline);
        add(Box.createRigidArea(new Dimension(40, 0)));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, height - 1, getWidth() - getInsets().right, height - 1);
    }
}
