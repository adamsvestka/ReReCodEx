package com.adamsvestka.pijl.rerecodex.Model;

import java.util.List;
import java.util.UUID;

import cz.cuni.mff.recodex.api.v1.Locale;

public class Group extends Observable<Group> {
    public String id;
    public String externalId;
    public String name;
    public String description;
    public List<UUID> students;
    public List<UUID> supervisors;
    public ObservableList<Assignment> assignments = new ObservableList<>();

    public void load(cz.cuni.mff.recodex.api.v1.groups.Response.Payload group) {
        id = group.id.toString();
        externalId = group.externalId;
        name = group.localizedTexts.stream().filter(t -> t.locale == Locale.en).findFirst().get().name;
        description = group.localizedTexts.stream().filter(t -> t.locale == Locale.en).findFirst().get().description;
        students = group.privateData.students;
        supervisors = group.privateData.supervisors;
        assignments.clear();
        assignments.addAll(group.privateData.assignments.stream().map(Assignment::build).toList());

        notifySubscribers();
    }

    public static Group build(cz.cuni.mff.recodex.api.v1.groups.Response.Payload group) {
        Group g = new Group();
        g.load(group);
        return g;
    }
}
