package com.adamsvestka.pijl.rerecodex;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

public class LocalStorage {
    private static final String storageLocation = "ReReCodEx.properties";
    private static final EncryptableProperties properties;
    private static final StandardPBEStringEncryptor encryptor;

    static {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("haRCJbcpWv");
        properties = new EncryptableProperties(encryptor);

        try (FileInputStream input = new FileInputStream(storageLocation)) {
            properties.load(input);
        } catch (Exception e) {
            System.out.println("Failed to load local storage.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (FileOutputStream output = new FileOutputStream(storageLocation)) {
                properties.store(output, null);
            } catch (Exception e) {
                System.out.println("Failed to save local storage.");
            }
        }));
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        set(key, value, false);
    }

    public static void set(String key, String value, boolean encrypt) {
        properties.setProperty(key, encrypt ? "ENC(" + encryptor.encrypt(value) + ")" : value);
    }

    public static boolean remove(String key) {
        return properties.remove(key) != null;
    }

    public static void clear() {
        properties.clear();
    }
}
