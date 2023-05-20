package com.adamsvestka.pijl.rerecodex.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.LocalStorage;
import com.adamsvestka.pijl.rerecodex.ReCodEx;
import com.adamsvestka.pijl.rerecodex.Components.Button;
import com.adamsvestka.pijl.rerecodex.Components.InputField;
import com.adamsvestka.pijl.rerecodex.Components.PasswordField;
import com.adamsvestka.pijl.rerecodex.Model.Model;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;

/**
 * A custom JPanel representing the login panel for the ReCodEx application.
 * This panel contains form elements for users to input their credentials, such
 * as username and password fields, a remember me checkbox, and a button to
 * submit the entered information for authentication. It also displays
 * appropriate feedback, such as loading progress or error messages, based on
 * the current authentication state. The panel has a rounded box appearance and
 * custom box shadow.
 * 
 * @see Model
 * @see LocalStorage
 * @see ReCodEx
 */
public class LoginPanel extends JPanel implements KeyListener {
    private JTextField usernameField;
    private JTextField passwordField;
    private JCheckBox rememberMeCheckbox;
    private JButton loginButton;
    private JLabel errorLabel;

    private enum State {
        normal, loading, error
    }

    private State state;
    private long startTime;

    /**
     * Constructs a new LoginPanel. It automatically tries to authenticate the user
     * with the credentials stored in the LocalStorage. If the authentication
     * succeeds, the user is redirected to the main application view.
     */
    public LoginPanel() {
        super();

        setBackground(ColorPalette.white);
        setBorder(BorderFactory.createCompoundBorder(
                new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 10),
                new RoundedBox(10)));

        setOpaque(false);

        initComponents();

        setState(State.normal);

        if (LocalStorage.get("username") != null) {
            usernameField.setText(LocalStorage.get("username"));
            passwordField.setText(LocalStorage.get("password"));
            rememberMeCheckbox.setSelected(true);
            loginButton.doClick();
        }
    }

    private void initComponents() {
        setPreferredSize(new Dimension(400, 320));
        setLayout(null);

        var titleLabel = new JLabel("Login with CAS account");
        var usernameLabel = new JLabel("Username:");
        usernameField = new InputField();
        var passwordLabel = new JLabel("Password:");
        passwordField = new PasswordField();
        rememberMeCheckbox = new JCheckBox("Remember me");
        loginButton = new Button("Login", event -> {
            setState(State.loading);
            ReCodEx.authenticate(usernameField.getText(), passwordField.getText())
                    .thenAccept(user -> {
                        Model.getInstance().accessToken = user.payload.accessToken;
                        Model.getInstance().user.load(user.payload);

                        if (rememberMeCheckbox.isSelected()) {
                            LocalStorage.set("username", usernameField.getText());
                            LocalStorage.set("password", passwordField.getText(), true);
                        }
                    }).exceptionally(e -> {
                        setState(LoginPanel.State.error);
                        return null;
                    });
        });
        errorLabel = new JLabel("Login failed!");

        usernameField.addKeyListener(this);
        passwordField.addKeyListener(this);
        rememberMeCheckbox.addKeyListener(this);
        loginButton.addKeyListener(this);
        errorLabel.setForeground(Color.red);

        add(titleLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(rememberMeCheckbox);
        add(loginButton);
        add(errorLabel);

        var width = getPreferredSize().width;
        var inset = 30;

        titleLabel.setBounds(20, 12, 200, 20);
        usernameLabel.setBounds(20, 60, 200, 20);
        usernameField.setBounds(inset, 90, width - 2 * inset, 35);
        passwordLabel.setBounds(20, 150, 200, 20);
        passwordField.setBounds(inset, 180, width - 2 * inset, 35);
        rememberMeCheckbox.setBounds(20, 230, 200, 20);
        loginButton.setBounds(155, 265, 90, 35);
        errorLabel.setBounds(width - errorLabel.getPreferredSize().width - inset, 12, 100, 20);
    }

    private void setState(State state) {
        switch (this.state = state) {
            case normal:
                errorLabel.setVisible(false);
                break;
            case loading:
                errorLabel.setVisible(false);
                startTime = System.currentTimeMillis();
                break;
            case error:
                errorLabel.setVisible(true);
                break;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, 41, getWidth() - getInsets().right, 41);

        if (state == State.loading) {
            g.setColor(ColorPalette.blue);
            int size = 150;
            int x = (int) ((System.currentTimeMillis() - startTime) / 5) % (getWidth() + size);
            var rect = new Rectangle(getInsets().left + Math.max(x - size, 0), 41,
                    Math.min(x < size ? x : size, getWidth() - getInsets().left - getInsets().right + size - x), 3);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
            repaint(0, rect.y, getWidth(), rect.height);
        } else if (state == State.error) {
            g.setColor(Color.red);
            g.fillRect(getInsets().left, 41, getWidth() - getInsets().left - getInsets().right, 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            loginButton.doClick();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
