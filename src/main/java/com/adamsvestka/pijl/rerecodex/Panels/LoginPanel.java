package com.adamsvestka.pijl.rerecodex.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.ReCodEx;
import com.adamsvestka.pijl.rerecodex.Components.Button;
import com.adamsvestka.pijl.rerecodex.Components.InputField;
import com.adamsvestka.pijl.rerecodex.Components.PasswordField;
import com.adamsvestka.pijl.rerecodex.Model.Model;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;

    public LoginPanel() {
        super();

        setBackground(ColorPalette.white);
        setBorder(BorderFactory.createCompoundBorder(
                new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 10),
                new RoundedBox(10)));
        setOpaque(false);

        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(400, 300));
        setLayout(null);

        var titleLabel = new JLabel("Login with CAS account");
        var usernameLabel = new JLabel("Username:");
        usernameField = new InputField();
        var passwordLabel = new JLabel("Password:");
        passwordField = new PasswordField();
        loginButton = new Button("Login", e -> {
            try {
                var user = ReCodEx.authenticate(usernameField.getText(), passwordField.getText());

                Model.getInstance().accessToken = user.payload.accessToken;
                Model.getInstance().user.load(user.payload);

                System.out.printf("Hello, %s!%n", user.payload.user.name.firstName);
            } catch (Exception ex) {
                System.out.println("Login failed!");
            }
        });

        add(titleLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        var width = getPreferredSize().width;
        var inset = 30;

        titleLabel.setBounds(20, 12, 200, 20);
        usernameLabel.setBounds(20, 60, 200, 20);
        usernameField.setBounds(inset, 90, width - 2 * inset, 35);
        passwordLabel.setBounds(20, 150, 200, 20);
        passwordField.setBounds(inset, 180, width - 2 * inset, 35);
        loginButton.setBounds(155, 245, 90, 35);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, 41, getWidth() - getInsets().right, 41);
    }
}
