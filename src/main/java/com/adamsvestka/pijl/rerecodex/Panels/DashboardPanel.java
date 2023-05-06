package com.adamsvestka.pijl.rerecodex.Panels;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.adamsvestka.pijl.rerecodex.Model.Model;
import com.adamsvestka.pijl.rerecodex.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DashboardPanel extends JPanel {
    private JTextArea textArea;

    public DashboardPanel() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);

        add(textArea);

        Model.getInstance().user.subscribe(this::update);
        update(Model.getInstance().user);
    }

    private void update(User user) {
        try {
            String str = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(user);
            textArea.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
