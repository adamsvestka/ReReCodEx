package cz.cuni.mff.recodex.api.v1;

import java.io.IOException;

public enum Locale {
    cs, en;

    public String toValue() {
        switch (this) {
            case cs:
                return "cs";
            case en:
                return "en";
        }
        return null;
    }

    public static Locale forValue(String value) throws IOException {
        if (value.equals("cs"))
            return cs;
        if (value.equals("en"))
            return en;
        throw new IOException("Cannot deserialize Locale");
    }
}
