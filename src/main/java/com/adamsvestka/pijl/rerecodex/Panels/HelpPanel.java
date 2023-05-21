package com.adamsvestka.pijl.rerecodex.Panels;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.adamsvestka.pijl.rerecodex.ColorPalette;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.CustomHTMLEditorKit;
import com.adamsvestka.pijl.rerecodex.SwingExtensions.VerticalScrollPanel;

/**
 * HelpPanel is a custom JPanel that displays the help page in a vertically
 * scrollable view. It consists of a JEditorPane component, displaying the help
 * page.
 */
public class HelpPanel extends JPanel {
    private JPanel container;
    private JScrollPane scrollPane;
    private JEditorPane content;

    /**
     * Constructs a new HelpPanel.
     */
    public HelpPanel() {
        super();

        setBackground(ColorPalette.white);

        initComponents();

        loadContent();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        container = new VerticalScrollPanel();
        scrollPane = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(LEFT_ALIGNMENT);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);

        content = new JEditorPane();

        content.setContentType("text/html");
        content.setEditorKit(new CustomHTMLEditorKit());
        content.setEditable(false);
        content.setOpaque(false);
        content.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        container.add(content);
    }

    private void loadContent() {
        try {
            var file = getClass().getResourceAsStream("/docs/help.md");
            var markdown = new String(file.readAllBytes());

            Parser parser = Parser.builder().build();
            Node document = parser.parse(markdown);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String html = renderer.render(document);

            content.setText(html);
        } catch (Exception e) {
            content.setText("<html><body><h1>Failed to load help</h1></body></html>");
        }
    }
}
