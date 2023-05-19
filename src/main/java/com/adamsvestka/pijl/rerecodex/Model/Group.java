package com.adamsvestka.pijl.rerecodex.Model;

import java.util.List;
import java.util.UUID;

import com.adamsvestka.pijl.rerecodex.ReCodEx;

import cz.cuni.mff.recodex.api.v1.Locale;

public class Group extends Observable<Group> {
    public UUID id;
    public String externalId;
    public String name;
    public String description;
    public List<UUID> students;
    public List<User> primaryAdmins = new ObservableList<>();
    public ObservableList<Assignment> assignments = new ObservableList<>();

    public void load(cz.cuni.mff.recodex.api.v1.groups.Response.Payload group) {
        id = group.id;
        externalId = group.externalId;
        name = Model.getLocalizedText(group.localizedTexts, Locale.en).name;
        description = Model.getLocalizedText(group.localizedTexts, Locale.en).description;
        students = group.privateData.students;
        primaryAdmins.clear();
        primaryAdmins.addAll(group.primaryAdminsIds.stream().map(User::new).toList());
        assignments.clear();
        ReCodEx.getAssignments(id)
                .thenApply(e -> e.stream().map(Assignment::build).toList())
                .thenAccept(assignments::addAll);

        notifySubscribers();
    }

    public static Group build(cz.cuni.mff.recodex.api.v1.groups.Response.Payload group) {
        Group g = new Group();
        g.load(group);
        return g;
    }
}
