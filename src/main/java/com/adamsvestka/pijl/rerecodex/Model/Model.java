package com.adamsvestka.pijl.rerecodex.Model;

import java.util.List;

import cz.cuni.mff.recodex.api.v1.ILocalizedText;
import cz.cuni.mff.recodex.api.v1.Locale;

/**
 * Model is a singleton class that represents the main data model for the
 * application. It contains the access token, a user object and a list of groups
 * the user is a part of. The class provides a single entry point for accessing
 * and modifying the data model and utility methods for handling localized
 * texts.
 * 
 * @see Observable
 * @see ObservableList
 */
public class Model {
    /** The access token used to authenticate the user. */
    public String accessToken;
    /** The user object representing the currently logged in user. */
    public final User user = new User();
    /** The list of groups the user is a part of. */
    public final ObservableList<Group> groups = new ObservableList<>();

    private static final Model instance = new Model();

    private Model() {
    }

    /**
     * Returns the singleton instance of the Model class.
     * 
     * @return The singleton instance of the Model class.
     */
    public static Model getInstance() {
        return instance;
    }

    /**
     * Returns the localized text for the given list of texts and the preferred
     * locale. If the preferred locale is not available, the first text in the list
     * is returned.
     * 
     * @param <T>             The type of the localized text.
     * @param texts           The list of localized texts.
     * @param preferredLocale The preferred locale.
     * @return The localized text for the given list of texts and the preferred
     *         locale.
     */
    public static <T extends ILocalizedText> T getLocalizedText(List<T> texts, Locale preferredLocale) {
        return texts.stream().filter(t -> t.locale == preferredLocale).findFirst().orElse(texts.get(0));
    }
}
