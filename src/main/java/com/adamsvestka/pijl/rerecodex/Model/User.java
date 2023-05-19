package com.adamsvestka.pijl.rerecodex.Model;

import java.util.List;
import java.util.UUID;

import com.adamsvestka.pijl.rerecodex.LocalStorage;
import com.adamsvestka.pijl.rerecodex.ReCodEx;

public class User extends Observable<User> {
    public UUID id;
    public String name;
    public String avatarUrl;
    public String email;
    public List<UUID> instances;

    public User() {
    }

    public User(UUID id) {
        this.id = id;
        ReCodEx.getUser(id)
                .thenAccept(this::load)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

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

    public boolean isLoggedIn() {
        return id != null;
    }

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
