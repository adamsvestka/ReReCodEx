package com.adamsvestka.pijl.rerecodex.Model;

import java.util.List;
import java.util.UUID;

import com.adamsvestka.pijl.rerecodex.ReCodEx;

import cz.cuni.mff.recodex.api.v1.Locale;

/**
 * A Group can represent many different structures in ReCodEx, however here it
 * is distilled to only a study group. Each Group has a list of students, a list
 * of primary administrators (teachers), and a list of assignments.
 * The Group class extends <code>Observable&lt;Group&gt;</code> to provide
 * update notifications to subscribers. This class handles loading data from
 * ReCodEx API response payloads and provides methods to build and manage a
 * Group object.
 * <p>
 * Key data attributes for a Group include its ID, name, description, and
 * participating students, primary administrators, and assignments.
 * <p>
 * The class provides additional utility methods for loading data from response
 * payload objects and handling API requests to load statistics related to the
 * content of the group, such as assignment details.
 * 
 * @see Observable
 * @see Assignment
 */
public class Group extends Observable<Group> {
    /** The group's ID. */
    public UUID id;
    /** The group's external ID. (Usually matching the course code.) */
    public String externalId;
    /** The group's full name. */
    public String name;
    /** The group's description. */
    public String description;
    /** The list of students in the group. */
    public List<UUID> students;
    /** The list of teachers in the group. */
    public List<User> primaryAdmins = new ObservableList<>();
    /** The list of assignments in the group. */
    public ObservableList<Assignment> assignments = new ObservableList<>();

    /**
     * Loads the group's data from the given response payload. Also initiates
     * loading of the group's assignments and statistics.
     * 
     * @param payload The response payload.
     * 
     * @see cz.cuni.mff.recodex.api.v1.groups.Response.Payload /api/v1/groups/{id}
     */
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

    /**
     * Builds a Group object from the given response payload.
     * 
     * @param payload The response payload.
     * @return The Group object.
     * 
     * @see cz.cuni.mff.recodex.api.v1.groups.Response.Payload /api/v1/groups/{id}
     */
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
