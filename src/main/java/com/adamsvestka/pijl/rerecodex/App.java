package com.adamsvestka.pijl.rerecodex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.adamsvestka.pijl.rerecodex.Components.Sidebar;
import com.adamsvestka.pijl.rerecodex.Panels.LoginPanel;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.Deserializers;

import cz.cuni.mff.recodex.api.v1.ReCodExApiDeserializer;

public class App extends JFrame {
    private JPanel mainarea;
    private LoginPanel loginPanel;
    private Sidebar sidebar;

    public App() {
        super("ReReCodEx");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);

        mainarea = new JPanel();
        mainarea.setBackground(new Color(244, 246, 249));
        mainarea.setLayout(new GridBagLayout());

        loginPanel = new LoginPanel();

        mainarea.add(loginPanel);

        sidebar = new Sidebar();

        getContentPane().add(mainarea);
        getContentPane().add(sidebar, BorderLayout.WEST);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
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
                public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config,
                        BeanDescription beanDesc) throws JsonMappingException {
                    return new ReCodExApiDeserializer();
                };
            });
        }
    });
}
