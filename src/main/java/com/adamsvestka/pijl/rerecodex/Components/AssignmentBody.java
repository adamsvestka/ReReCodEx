package com.adamsvestka.pijl.rerecodex.Components;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.Model.Assignment;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.BoxShadow;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.VerticalScrollPanel;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.CustomHTMLEditorKit;

public class AssignmentBody extends VerticalScrollPanel implements HyperlinkListener {
    private JLabel nameLabel;
    private JEditorPane bodyPane;

    public AssignmentBody() {
        super();

        setBackground(ColorPalette.white);
        setBorder(BorderFactory.createCompoundBorder(
                new BoxShadow(0, 1, 3, 0, new Color(0x3f000000, true), 10),
                new RoundedBox(10)));

        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        nameLabel = new JLabel();
        bodyPane = new JEditorPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };

        nameLabel.setFont(nameLabel.getFont().deriveFont(20f));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bodyPane.setContentType("text/html");
        bodyPane.setEditorKit(new CustomHTMLEditorKit());
        bodyPane.setEditable(false);
        bodyPane.setOpaque(false);
        bodyPane.setFont(bodyPane.getFont());
        bodyPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        bodyPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        bodyPane.addHyperlinkListener(this);

        add(nameLabel);
        add(bodyPane);
    }

    public void update(Assignment assignment) {
        nameLabel.setText(assignment.name);

        Parser parser = Parser.builder().build();
        Node document = parser.parse(assignment.body);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);

        bodyPane.setText(html);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(ColorPalette.light_gray);
        g.drawLine(getInsets().left, 51, getWidth() - getInsets().right, 51);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (IOException | URISyntaxException ex) {
            }
        }
    }
}
