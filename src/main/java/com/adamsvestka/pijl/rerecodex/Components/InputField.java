package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBorder;

public class InputField extends JTextField implements FocusListener {
    private RoundedBorder border;

    public InputField() {
        super();

        setBorder(border = new RoundedBorder(Color.GRAY, 1f, 10));
        setOpaque(false);

        addFocusListener(this);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 15, 0, 15);
    }

    @Override
    public void focusGained(FocusEvent e) {
        border.setBorderColor(Color.decode("#3f51b5"));
    }

    @Override
    public void focusLost(FocusEvent e) {
        border.setBorderColor(Color.GRAY);
    }
}
