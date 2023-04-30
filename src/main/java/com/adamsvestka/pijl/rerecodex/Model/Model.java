package com.adamsvestka.pijl.rerecodex.Model;

public class Model {
    public String accessToken;
    public final User user = new User();

    private static final Model instance = new Model();

    private Model() {
    }

    public static Model getInstance() {
        return instance;
    }
}
