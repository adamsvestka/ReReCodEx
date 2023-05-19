package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Group;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;

public class CourseCard extends JPanel {
    private JLabel shortNameLabel;
    private JLabel semesterLabel;
    private JLabel fullNameLabel;
    private JLabel teacherLabel;

    private Group course;

    public CourseCard(Group course) {
        super();

        this.course = course;

        setBackground(ColorPalette.white);
        setBorder(BorderFactory.createCompoundBorder(
                new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 10),
                new RoundedBox(10)));

        initComponents();
    }

    private void initComponents() {
        // shortNameLabel = new JLabel(course.localizedName);
        // semesterLabel = new JLabel(course.semester);
        fullNameLabel = new JLabel(course.name);
        // teacherLabel = new JLabel(course.teacher);

        // add(shortNameLabel);
        // add(semesterLabel);
        add(fullNameLabel);
        // add(teacherLabel);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, 41, getWidth() - getInsets().right, 41);
    }
}