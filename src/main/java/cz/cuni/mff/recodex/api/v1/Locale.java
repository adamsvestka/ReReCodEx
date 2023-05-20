package cz.cuni.mff.recodex.api.v1;

import java.io.IOException;

/**
 * Known locales used in the API.
 * 
 * @see ILocalizedText
 */
public enum Locale {
    cs, en;

    /**
     * Convert the enum value to a string.
     * 
     * @return The string representation of the enum value.
     */
    public String toValue() {
        switch (this) {
            case cs:
                return "cs";
            case en:
                return "en";
        }
        return null;
    }

    /**
     * Convert a string to an enum value.
     * 
     * @param value The string representation of the enum value.
     * @return The enum value.
     * @throws IOException If the string does not represent a valid enum value.
     */
    public static Locale forValue(String value) throws IOException {
        if (value.equals("cs"))
            return cs;
        if (value.equals("en"))
            return en;
        throw new IOException("Cannot deserialize Locale");
    }
}
