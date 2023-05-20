package com.adamsvestka.pijl.rerecodex.Model;

import java.util.List;
import java.util.UUID;

import com.adamsvestka.pijl.rerecodex.LocalStorage;
import com.adamsvestka.pijl.rerecodex.ReCodEx;

/**
 * Represents a user in the ReCodEx system with all of their relevant
 * information such as ID, name, email, avatar URL, and instances associated
 * with the user. Provides methods to load user data from external API payloads,
 * check if the user is logged in, and perform logout.
 * <p>
 * The User class extends the Observable class to enable observing changes in
 * the user's state, making it easy for components of the application to react
 * to user-related updates.
 * 
 * @see Observable
 */

public class User extends Observable<User> {
    /** The user's ID. */
    public UUID id;
    /** The user's full name. */
    public String name;
    /** The URL of the user's avatar. */
    public String avatarUrl;
    /** The user's email address. */
    public String email;
    /** The list of instances associated with the user. */
    public List<UUID> instances;

    /**
     * Constructs a blank User object.
     */
    public User() {
    }

    /**
     * Constructs a new User object with the given ID. The user's data is loaded
     * asynchronously from the ReCodEx API.
     * 
     * @param id The user's ID.
     */
    public User(UUID id) {
        this.id = id;
        ReCodEx.getUser(id)
                .thenAccept(this::load)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    /**
     * Loads the user's data from the login payload of the CAS UK API.
     * 
     * @param payload The login payload of the CAS UK API.
     * 
     * @see cz.cuni.mff.recodex.api.v1.login.cas_uk.Response.Payload
     *      /api/v1/login/cas-uk
     */
    public void load(cz.cuni.mff.recodex.api.v1.login.cas_uk.Response.Payload payload) {
        id = payload.user.id;
        name = payload.user.fullName;
        avatarUrl = payload.user.avatarUrl;
        if (payload.user.privateData != null) {
            email = payload.user.privateData.email;
            instances = payload.user.privateData.instancesIds;
        }

        notifySubscribers();
    }

    /**
     * Loads the user's data from the user details payload of the ReCodEx API.
     * 
     * @param payload The user details payload of the ReCodEx API.
     * 
     * @see cz.cuni.mff.recodex.api.v1.users.$id.Response.Payload /api/v1/users/{id}
     */
    public void load(cz.cuni.mff.recodex.api.v1.users.$id.Response.Payload payload) {
        id = payload.id;
        name = payload.fullName;
        avatarUrl = payload.avatarUrl;
        if (payload.privateData != null) {
            email = payload.privateData.email;
            instances = payload.privateData.instancesIds;
        }

        notifySubscribers();
    }

    /**
     * Whether the user is logged in.
     *
     * @return True if the user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return id != null;
    }

    /**
     * Logs the user out by clearing all of their data and removing their
     * credentials from the local storage.
     * 
     * @see LocalStorage
     */
    public void logout() {
        id = null;
        name = null;
        email = null;
        avatarUrl = null;
        instances = null;

        LocalStorage.remove("username");
        LocalStorage.remove("password");

        notifySubscribers();
    }
}
