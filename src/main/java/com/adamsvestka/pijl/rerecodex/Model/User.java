package com.adamsvestka.pijl.rerecodex.Model;

import com.adamsvestka.pijl.rerecodex.LocalStorage;

public class User extends Observable<User> {
    public String id;
    public String name;
    public String email;
    public String avatarUrl;

    public void load(cz.cuni.mff.recodex.api.v1.login.cas_uk.Payload payload) {
        id = payload.user.id;
        name = payload.user.fullName;
        email = payload.user.privateData.email;
        avatarUrl = payload.user.avatarUrl;

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

        LocalStorage.remove("username");
        LocalStorage.remove("password");

        notifySubscribers();
    }
}
