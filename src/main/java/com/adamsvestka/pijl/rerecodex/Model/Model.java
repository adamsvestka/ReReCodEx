package com.adamsvestka.pijl.rerecodex.Model;

import java.util.List;

import cz.cuni.mff.recodex.api.v1.ILocalizedText;
import cz.cuni.mff.recodex.api.v1.Locale;

public class Model {
    public String accessToken;
    public final User user = new User();
    public final ObservableList<Group> groups = new ObservableList<>();

    private static final Model instance = new Model();

    private Model() {
    }

    public static Model getInstance() {
        return instance;
    }

    public static <T extends ILocalizedText> T getLocalizedText(List<T> texts, Locale preferredLocale) {
        return texts.stream().filter(t -> t.locale == preferredLocale).findFirst().orElse(texts.get(0));
    }
}
