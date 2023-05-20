package com.adamsvestka.pijl.rerecodex.Model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.adamsvestka.pijl.rerecodex.ReCodEx;

import cz.cuni.mff.recodex.api.v1.Locale;

public class Assignment extends Observable<Assignment> {
    public UUID id;
    public String name;
    public String body;
    public int points;
    public int bonusPoints;
    public List<Deadline> deadlines;
    public List<String> runtimeEnvironments;
    public int attempts;
    public int pointsPercentualThreshold;
    public int submissionsCountLimit;
    public int solutionFilesLimit;
    public int solutionSizeLimit;

    public static class Deadline {
        public LocalDateTime time;
        public int points;

        public Deadline(long time, long points) {
            this.time = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
            this.points = (int) points;
        }
    }

    public void load(cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload payload) {
        id = payload.id;
        name = Model.getLocalizedText(payload.localizedTexts, Locale.en).name;
        body = Model.getLocalizedText(payload.localizedTexts, Locale.en).text;
        deadlines = new ArrayList<>(2);
        deadlines.add(new Deadline(payload.firstDeadline, payload.maxPointsBeforeFirstDeadline));
        if (payload.allowSecondDeadline) {
            deadlines.add(new Deadline(payload.secondDeadline, payload.maxPointsBeforeSecondDeadline));
        }
        runtimeEnvironments = payload.runtimeEnvironmentIds;
        attempts = (int) payload.submissionsCountLimit;
        pointsPercentualThreshold = (int) payload.pointsPercentualThreshold;
        submissionsCountLimit = (int) payload.submissionsCountLimit;
        solutionFilesLimit = (int) payload.solutionFilesLimit;
        solutionSizeLimit = (int) payload.solutionSizeLimit;

        notifySubscribers();
    }

    public void load(cz.cuni.mff.recodex.api.v1.groups.$id.students.stats.Response.Payload.Assignment payload) {
        points = (int) payload.points.gained;
        bonusPoints = (int) payload.points.bonus;

        notifySubscribers();
    }

    public void load(cz.cuni.mff.recodex.api.v1.exercise_assignments.$id.can_submit.Response.Payload payload) {
        attempts = (int) payload.evaluated;

        notifySubscribers();
    }

    public static Assignment build(cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload payload) {
        Assignment a = new Assignment();
        a.load(payload);
        ReCodEx.getCanSubmit(payload.id)
                .thenAccept(a::load)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
        return a;
    }
}
