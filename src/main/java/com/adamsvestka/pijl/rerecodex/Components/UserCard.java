package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Model;
import com.adamsvestka.pijl.rerecodex.Model.User;

/**
 * UserCard is a custom JPanel component that displays the User's details, such
 * as their username and avatar image. It also contains a logout button for the
 * user to log out of their account. The component is designed to be displayed
 * in the application's user interface and automatically updates its contents
 * whenever the user's information changes. The class makes use of the Singleton
 * Model object to obtain the latest information about the User and updates
 * itself by subscribing to changes in the User object. The background color of
 * the component and various other stylistic aspects are also defined within
 * this class.
 * 
 * @see Model
 * @see User
 */
public class UserCard extends JPanel {
    private static final Color color_background = ColorPalette.dark_gray;
    private static final int image_size = 32;

    private JLabel usernameLabel;

    /**
     * Constructs a new UserCard component. The component is initialized with the
     * latest information about the User and automatically updates itself whenever
     * the User object changes.
     */
    public UserCard() {
        super();

        setBackground(color_background);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();

        Model.getInstance().user.subscribe(this::update);
        update(Model.getInstance().user);
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        usernameLabel = new JLabel("Username");
        var container = new JPanel();

        usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
        usernameLabel.setForeground(ColorPalette.white);
        container.setAlignmentX(CENTER_ALIGNMENT);
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setBackground(color_background);

        add(usernameLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(container);

        var logout = new JButton("Logout");

        logout.addActionListener(e -> Model.getInstance().user.logout());

        container.add(logout);
    }

    @Override
    public Dimension getPreferredSize() {
        var size = super.getPreferredSize();
        size.width = 200;
        return size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(ColorPalette.dark_gray2);
        g.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
    }

    private void update(User user) {
        setVisible(user.isLoggedIn());

        usernameLabel.setText(user.name);

        revalidate();
        repaint();

        if (user.avatarUrl == null) {
            usernameLabel.setIcon(null);
            return;
        }

        try {
            URL url = new URI("http:" + user.avatarUrl).toURL();
            Image image = ImageIO.read(url).getScaledInstance(image_size, image_size, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(image);
            usernameLabel.setIcon(icon);
        } catch (Exception ex) {
            usernameLabel.setIcon(null);
        }
    }
}
