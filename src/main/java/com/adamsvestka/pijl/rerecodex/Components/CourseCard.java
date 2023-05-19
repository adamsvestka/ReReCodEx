package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Group;
import com.adamsvestka.pijl.rerecodex.Model.User;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;

public class CourseCard extends JPanel {
    private JLabel fullNameLabel;
    private JLabel teachersLabel;

    private Group course;

    public CourseCard(Group course) {
        super();

        this.course = course;

        setBackground(ColorPalette.white);
        setBorder(BorderFactory.createCompoundBorder(
                new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 5),
                new RoundedBox(5)));

        initComponents();

        for (User teacher : course.primaryAdmins) {
            teacher.subscribe(this::updateTeacher);
            updateTeacher(teacher);
        }
    }

    private void initComponents() {
        setLayout(null);

        fullNameLabel = new JLabel(course.name);
        teachersLabel = new JLabel();

        fullNameLabel.setFont(getFont().deriveFont(16f));
        fullNameLabel.setForeground(ColorPalette.dark_gray);
        teachersLabel.setFont(getFont().deriveFont(12f));
        teachersLabel.setForeground(ColorPalette.dark_gray2);

        add(fullNameLabel);
        add(teachersLabel);

        fullNameLabel.setBounds(20, 15, 500, 20);
        teachersLabel.setBounds(30, 35, 500, 20);
    }

    private void updateTeacher(User teacher) {
        teachersLabel.setText("(" + String.join(", ", course.primaryAdmins.stream().map(t -> t.name).toList()) + ")");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 66);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, 66);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, 66, getWidth() - getInsets().right, 66);
    }
}