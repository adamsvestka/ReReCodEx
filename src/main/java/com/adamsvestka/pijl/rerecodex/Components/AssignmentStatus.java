package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.adamsvestka.pijl.rerecodex.App;
import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.VerticalScrollPanel;

/**
 * AssignmentStatus is a custom Swing panel component that displays the detailed
 * status of a specific assignment. This component inherits from
 * VerticalScrollPanel and displays assignment details like deadline, points
 * limit, correctness threshold, allowed environments, submission attempts, and
 * solution file restrictions. It supports updating data with a provided
 * Assignment object and handles the layout and styling of its child components,
 * including JLabels for the assignment's attribute titles and values, and
 * graphic lines for separating content.
 * 
 * @see VerticalScrollPanel
 * @see Assignment
 */
public class AssignmentStatus extends VerticalScrollPanel {
    private JLabel nameLabel;
    private List<Entry<String, Function<Assignment, String>>> data = new ArrayList<>();
    private Map<String, JLabel> labels = new HashMap<>();

    /**
     * Constructs a new AssignmentStatus component. The component displays
     * information about the assignment, such as its deadline, points limit,
     * correctness threshold, allowed environments, submission attempts, and
     * solution file restrictions.
     */
    public AssignmentStatus() {
        super();

        data.add(new SimpleEntry<>("Deadline:",
                a -> App.dateTimeFormatter.format(a.deadlines.get(0).time)));
        data.add(new SimpleEntry<>("Points limit:",
                a -> Integer.toString(a.deadlines.get(0).points)));
        data.add(new SimpleEntry<>("Correctness threshold:",
                a -> Integer.toString(100 * a.pointsPercentualThreshold) + " %"));
        data.add(new SimpleEntry<>("Allowed environments:",
                a -> String.join(", ", a.runtimeEnvironments)));
        data.add(new SimpleEntry<>("Submission attempts:",
                a -> String.format("%s / %s", a.attempts, a.submissionsCountLimit)));
        data.add(new SimpleEntry<>("Solution file restrictions:",
                a -> String.format("%s files, %s total", a.solutionFilesLimit,
                        App.bytesToHumanReadable(a.solutionSizeLimit))));

        setBackground(ColorPalette.white);
        setBorder(BorderFactory.createCompoundBorder(
                new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 10),
                new RoundedBox(10)));

        initComponents();
    }

    private void initComponents() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        setAlignmentX(LEFT_ALIGNMENT);

        nameLabel = new JLabel();
        nameLabel.setFont(nameLabel.getFont().deriveFont(20f));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        SequentialGroup vsGroup = layout.createSequentialGroup()
                .addComponent(nameLabel)
                .addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        ParallelGroup hgGroupLabels = layout.createParallelGroup();
        ParallelGroup hgGroupValues = layout.createParallelGroup();

        for (var entry : data) {
            var label = new JLabel(entry.getKey());
            var value = new JLabel();

            label.setFont(label.getFont().deriveFont(Font.BOLD));
            label.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            value.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            label.setMinimumSize(new Dimension(300, 30));
            value.setMinimumSize(new Dimension(300, 30));

            hgGroupLabels.addComponent(label);
            hgGroupValues.addComponent(value);

            vsGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(value));

            labels.put(entry.getKey(), value);
        }

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(nameLabel)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(hgGroupLabels)
                        .addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addGroup(hgGroupValues)));

        layout.setVerticalGroup(vsGroup);
    }

    /**
     * Update the component with the given assignment data.
     * 
     * @param assignment The assignment to display information about.
     */
    public void update(Assignment assignment) {
        if (assignment.name == null || assignment.deadlines == null)
            return;

        nameLabel.setText(assignment.name);

        for (var entry : data)
            labels.get(entry.getKey()).setText(entry.getValue().apply(assignment));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, 51, getWidth() - getInsets().right, 51);

        g.setColor(ColorPalette.light_gray2);
        for (int i = 1; i < labels.size(); i++)
            g.drawLine(getInsets().left, 52 + 30 * i, getWidth() - getInsets().right, 52 + 30 * i);
    }
}
