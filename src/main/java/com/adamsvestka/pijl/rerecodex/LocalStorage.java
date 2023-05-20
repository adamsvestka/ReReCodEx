package com.adamsvestka.pijl.rerecodex;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 * Encrypted local storage for ReReCodEx. An implementation of local storage
 * which uses encrypted key-value pairs to securely store sensitive data.
 */
public class LocalStorage {
    /** File location for storage. */
    private static final String storageLocation = "ReReCodEx.properties";
    /** Encryptable properties instance to handle encrypted key-value pairs. */
    private static final EncryptableProperties properties;
    /** Encryptor used for encryption and decryption. */
    private static final StandardPBEStringEncryptor encryptor;

    /**
     * Initialize the encryptor, load the properties from the storage file,
     * and set up a shutdown hook to save changes when the application exits.
     */
    static {
        // Initialize encryptor.
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("haRCJbcpWv");

        // Load properties from storage location.
        properties = new EncryptableProperties(encryptor);

        try (FileInputStream input = new FileInputStream(storageLocation)) {
            properties.load(input);
        } catch (Exception e) {
            System.out.println("Failed to load local storage.");
        }

        // Set up shutdown hook to save changes when the application exits.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (FileOutputStream output = new FileOutputStream(storageLocation)) {
                properties.store(output, null);
            } catch (Exception e) {
                System.out.println("Failed to save local storage.");
            }
        }));
    }

    /**
     * Retrieve the value associated with the given key.
     *
     * @param key The key used to identify the value.
     * @return The value associated with the given key, or null if not found.
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Set a value associated with a key in the storage.
     *
     * @param key   The key used to identify the value.
     * @param value The value to be stored.
     */
    public static void set(String key, String value) {
        set(key, value, false);
    }

    /**
     * Set a value associated with a key in the storage, with optional encryption.
     *
     * @param key     The key used to identify the value.
     * @param value   The value to be stored.
     * @param encrypt Whether or not to encrypt the value before storing it.
     */
    public static void set(String key, String value, boolean encrypt) {
        properties.setProperty(key, encrypt ? "ENC(" + encryptor.encrypt(value) + ")" : value);
    }

    /**
     * Remove a key-value pair from the storage.
     *
     * @param key The key to be removed.
     * @return True if the key was found and removed, false otherwise.
     */
    public static boolean remove(String key) {
        return properties.remove(key) != null;
    }

    /**
     * Clear all key-value pairs from the storage.
     */
    public static void clear() {
        properties.clear();
    }
}
