package com.adamsvestka.pijl.rerecodex.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import cz.cuni.mff.recodex.api.v1.Locale;

public class Assignment extends Observable<Assignment> {
    public UUID id;
    public String name;
    public String body;
    public List<Deadline> deadlines;
    public List<String> runtimeEnvironments;

    public static class Deadline {
        public LocalDateTime time;
        public int points;

        public Deadline(long time, long points) {
            this.time = LocalDateTime.ofEpochSecond(time, 0, null);
            this.points = (int) points;
        }
    }

    public void load(cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload assignment) {
        id = assignment.id;
        name = Model.getLocalizedText(assignment.localizedTexts, Locale.en).name;
        body = Model.getLocalizedText(assignment.localizedTexts, Locale.en).text;
        deadlines = List.of(new Deadline(assignment.firstDeadline, assignment.maxPointsBeforeFirstDeadline));
        if (assignment.allowSecondDeadline) {
            deadlines.add(new Deadline(assignment.secondDeadline, assignment.maxPointsBeforeSecondDeadline));
        }

        notifySubscribers();
    }

    public static Assignment build(cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload assignment) {
        Assignment a = new Assignment();
        a.load(assignment);
        return a;
    }
}
