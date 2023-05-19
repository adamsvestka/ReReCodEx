package com.adamsvestka.pijl.rerecodex.Model;

import java.util.UUID;

public class Assignment extends Observable<Assignment> {
    public UUID id;

    public void load(cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload assignment) {
        id = assignment.id;

        notifySubscribers();
    }

    public static Assignment build(UUID id) {
        Assignment a = new Assignment();
        a.id = id;
        return a;
    }
}
