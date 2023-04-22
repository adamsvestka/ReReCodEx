package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBorder;

public class PasswordField extends JPasswordField implements FocusListener {
    private static final Color color_border = Color.gray;
    private static final Color color_border_focus = ColorPalette.blue;

    private RoundedBorder border;

    public PasswordField() {
        super();

        setBorder(border = new RoundedBorder(color_border, 1f, 10));
        setOpaque(false);

        addFocusListener(this);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 15, 0, 15);
    }

    @Override
    public void focusGained(FocusEvent e) {
        border.setBorderColor(color_border_focus);
    }

    @Override
    public void focusLost(FocusEvent e) {
        border.setBorderColor(color_border);
    }
}
