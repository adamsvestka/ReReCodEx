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

    public void load(cz.cuni.mff.recodex.api.v1.groups.Response.Payload payload) {
        id = payload.id;
        externalId = payload.externalId;
        name = Model.getLocalizedText(payload.localizedTexts, Locale.en).name;
        description = Model.getLocalizedText(payload.localizedTexts, Locale.en).description;
        students = payload.privateData.students;
        primaryAdmins.clear();
        primaryAdmins.addAll(payload.primaryAdminsIds.stream().map(User::new).toList());
        assignments.clear();
        ReCodEx.getAssignments(id)
                .thenApply(e -> e.stream().map(Assignment::build).toList())
                .thenAccept(assignments::addAll)
                .thenRun(this::loadStats)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });

        notifySubscribers();
    }

    public static Group build(cz.cuni.mff.recodex.api.v1.groups.Response.Payload payload) {
        Group g = new Group();
        g.load(payload);
        return g;
    }

    private void loadStats() {
        ReCodEx.getStats(id)
                .thenAccept(stats -> {
                    assignments.forEach(assignment -> {
                        stats.get(0).assignments.stream()
                                .filter(s -> s.id.equals(assignment.id))
                                .findFirst().ifPresent(assignment::load);
                    });
                }).exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }
}
