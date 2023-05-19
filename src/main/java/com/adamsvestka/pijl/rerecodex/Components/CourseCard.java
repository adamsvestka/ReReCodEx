package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;
import com.adamsvestka.pijl.rerecodex.Model.Group;
import com.adamsvestka.pijl.rerecodex.Model.User;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;

public class CourseCard extends JPanel implements MouseListener, ComponentListener {
    private JPanel header;
    private JLabel fullNameLabel;
    private JLabel teachersLabel;
    private JLabel arrowLabel;
    private JPanel container;
    private List<AssignmentCard> assignments;

    private Group course;
    private boolean isExpanded = false;

    public CourseCard(Group course) {
        super();

        this.course = course;

        setBackground(ColorPalette.white);
        setBorder(BorderFactory.createCompoundBorder(
                new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 5),
                new RoundedBox(5)));

        initComponents();

        addMouseListener(this);
        addComponentListener(this);

        for (User teacher : course.primaryAdmins) {
            teacher.subscribe(this::updateTeacher);
            updateTeacher(teacher);
        }

        course.assignments.subscribe(this::updateAssignments);
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        header = new JPanel();
        container = new JPanel();

        header.setLayout(null);
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(500, 66));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(LEFT_ALIGNMENT);
        container.setOpaque(false);
        container.setVisible(isExpanded);

        add(header);
        add(container);

        fullNameLabel = new JLabel(course.name);
        teachersLabel = new JLabel();
        arrowLabel = new JLabel("+");

        fullNameLabel.setFont(getFont().deriveFont(16f));
        fullNameLabel.setForeground(ColorPalette.dark_gray);
        teachersLabel.setFont(getFont().deriveFont(12f));
        teachersLabel.setForeground(ColorPalette.dark_gray2);
        arrowLabel.setFont(getFont().deriveFont(24f));
        arrowLabel.setForeground(ColorPalette.dark_gray2);

        header.add(fullNameLabel);
        header.add(teachersLabel);
        header.add(arrowLabel);

        fullNameLabel.setBounds(20, 15, 500, 20);
        teachersLabel.setBounds(30, 35, 500, 20);
        arrowLabel.setBounds(header.getWidth() - 30, 15, 20, 20);

        assignments = course.assignments.stream().map(AssignmentCard::new).toList();

        assignments.forEach(container::add);
    }

    private void updateTeacher(User teacher) {
        teachersLabel.setText("(" + String.join(", ", course.primaryAdmins.stream().map(t -> t.name).toList()) + ")");
    }

    private void updateAssignments(List<Assignment> assignments) {
        this.assignments.forEach(container::remove);
        this.assignments = assignments.stream()
                .sorted((a, b) -> b.deadlines.get(0).time.compareTo(a.deadlines.get(0).time))
                .map(AssignmentCard::new).toList();
        this.assignments.forEach(container::add);

        revalidate();
        repaint();
    }

    private int getExtraHeight() {
        return isExpanded ? assignments.size() * AssignmentCard.height : 0;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, 66 + getExtraHeight());
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, 66 + getExtraHeight());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, 66, getWidth() - getInsets().right, 66);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        isExpanded = !isExpanded;

        arrowLabel.setText(isExpanded ? "-" : "+");
        container.setVisible(isExpanded);

        revalidate();
        getParent().repaint();
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

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        arrowLabel.setBounds(header.getWidth() - 30, 15, 20, 20);
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }
}