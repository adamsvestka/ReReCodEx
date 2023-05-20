package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBorder;

/**
 * PasswordField is a custom extension of the JPasswordField class that provides
 * a stylized, focused state appearance. It features rounded edges and a
 * changing border color when the input field gains or loses focus for enhanced
 * user experience within the application. In addition, it supports custom
 * Insets for padding within the input field to provide an appealing input
 * component in the UI.
 * 
 * @see JTextField
 * @see FocusListener
 */
public class PasswordField extends JPasswordField implements FocusListener {
    private static final Color color_border = Color.gray;
    private static final Color color_border_focus = ColorPalette.blue;

    private RoundedBorder border;

    /**
     * Constructs a new input field.
     * 
     * See {@link javax.swing.JTextField#getText() getText()} for retrieving the
     * text
     */
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
