package com.adamsvestka.pijl;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.Deserializers;

import cz.cuni.mff.recodex.api.v1.ReCodExApiDeserializer;

public class App extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;
    private JLabel responseLabel;

    public App() {
        initComponents();
    }

    public void initComponents() {
        var panel = new JPanel();
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        setTitle("Hello World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        usernameField = new JTextField();
        passwordField = new JTextField();
        loginButton = new JButton("Login");
        responseLabel = new JLabel("Hello, World!");

        loginButton.addActionListener(this::loginButtonActionPerformed);

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(loginButton))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup()
                        .addComponent(passwordField)
                        .addComponent(responseLabel)));

        layout.linkSize(SwingConstants.HORIZONTAL, usernameField, loginButton);
        layout.linkSize(SwingConstants.HORIZONTAL, passwordField, responseLabel);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(passwordField))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(loginButton)
                        .addComponent(responseLabel)));

        getContentPane().add(panel);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            var user = ReCodEx.authenticate(usernameField.getText(), passwordField.getText());

            responseLabel.setText(String.format("Hello, %s!", user.payload.user.name.firstName));
        } catch (Exception e) {
            responseLabel.setText("Invalid credentials");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }

    public static ObjectMapper mapper = new ObjectMapper().registerModule(new Module() {
        @Override
        public String getModuleName() {
            return "ReCodExApiModule";
        }

        @Override
        public Version version() {
            return Version.unknownVersion();
        }

        @Override
        public void setupModule(SetupContext context) {
            context.addDeserializers(new Deserializers.Base() {
                @Override
                public com.fasterxml.jackson.databind.JsonDeserializer<?> findBeanDeserializer(
                        com.fasterxml.jackson.databind.JavaType type, DeserializationConfig config,
                        BeanDescription beanDesc) throws JsonMappingException {
                    return new ReCodExApiDeserializer();
                };
            });
        }
    });
}
