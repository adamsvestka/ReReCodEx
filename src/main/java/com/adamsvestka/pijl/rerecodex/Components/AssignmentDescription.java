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
import com.adamsvestka.pijl.rerecodex.SwingExtensions.CustomHTMLEditorKit;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.RoundedBox;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.VerticalScrollPanel;

/**
 * This class represents a custom graphical component, specifically designed to
 * display assignment description details in a visually appealing and organized
 * manner. The AssignmentDescription extends VerticalScrollPanel and implements
 * HyperlinkListener to provide hyperlink handling. The component consists of a
 * title label and a JEditorPane which renders HTML content using a
 * CustomHTMLEditorKit. The assignment's body is parsed and rendered as an HTML
 * string.
 * 
 * @see VerticalScrollPanel
 * @see HyperlinkListener
 * @see CustomHTMLEditorKit
 * @see Assignment
 * @see JEditorPane
 */
public class AssignmentDescription extends VerticalScrollPanel implements HyperlinkListener {
    private JEditorPane bodyPane;

    /**
     * Constructs a new AssignmentDescription component.
     */
    public AssignmentDescription() {
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

        var titleLabel = new JLabel("Assignment description");
        bodyPane = new JEditorPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };

        titleLabel.setFont(titleLabel.getFont().deriveFont(20f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bodyPane.setContentType("text/html");
        bodyPane.setEditorKit(new CustomHTMLEditorKit());
        bodyPane.setEditable(false);
        bodyPane.setOpaque(false);
        bodyPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        bodyPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        bodyPane.addHyperlinkListener(this);

        add(titleLabel);
        add(bodyPane);
    }

    /**
     * Updates the component with the given assignment.
     * 
     * @param assignment The assignment to update the component with
     */
    public void update(Assignment assignment) {
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
        g.drawLine(getInsets().left, 46, getWidth() - getInsets().right, 46);
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
