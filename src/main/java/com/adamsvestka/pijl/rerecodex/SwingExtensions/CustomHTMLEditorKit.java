package com.adamsvestka.pijl.rerecodex.SwingExtensions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ParagraphView;
import javax.swing.text.html.StyleSheet;

public class CustomHTMLEditorKit extends HTMLEditorKit {
    ViewFactory viewFactory = new HTMLFactory() {
        @Override
        public View create(Element elem) {
            AttributeSet attrs = elem.getAttributes();
            Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
            Object o = (elementName != null) ? elementName : attrs.getAttribute(StyleConstants.NameAttribute);

            if (o instanceof Tag) {
                if (o == Tag.IMPLIED)
                    return new ParagraphView(elem);
            }

            return super.create(elem);
        }
    };

    public CustomHTMLEditorKit() {
        super();

        StyleSheet styleSheet = getStyleSheet();
        URL styleSheetURL = getClass().getResource("/styles/assignment.css");
        try {
            URLConnection connection = styleSheetURL.openConnection();
            InputStream inputStream = connection.getInputStream();
            Reader reader = new InputStreamReader(inputStream);

            styleSheet.loadRules(reader, styleSheetURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewFactory getViewFactory() {
        return this.viewFactory;
    }
}
