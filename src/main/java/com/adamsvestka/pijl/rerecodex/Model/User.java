package com.adamsvestka.pijl.rerecodex.Model;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.adamsvestka.pijl.rerecodex.LocalStorage;

public class User extends Observable<User> {
    public UUID id;
    public String name;
    public String email;
    public String avatarUrl;
    public List<UUID> instances;

    public void load(cz.cuni.mff.recodex.api.v1.login.cas_uk.Response.Payload payload) {
        id = payload.user.id;
        name = payload.user.fullName;
        email = payload.user.privateData.email;
        avatarUrl = payload.user.avatarUrl;
        instances = Collections.unmodifiableList(payload.user.privateData.instancesIds);

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
