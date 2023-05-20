package com.adamsvestka.pijl.rerecodex.Model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.adamsvestka.pijl.rerecodex.ReCodEx;

import cz.cuni.mff.recodex.api.v1.Locale;

/**
 * Represents an assignment with its properties, deadlines and other
 * constraints. The Assignment class provides methods to load data from
 * different API response payloads. Each assignment has an associated observable
 * mechanism to notify subscribers of any updates.
 * 
 * @see Observable
 */
public class Assignment extends Observable<Assignment> {
    /** The assignment's ID. */
    public UUID id;
    /** The assignment's name. */
    public String name;
    /** The assignment's description. */
    public String body;
    /** How many points the student has gained. */
    public int points;
    /** How many bonus points the student has gained. */
    public int bonusPoints;
    /** The list of deadlines for the assignment. */
    public List<Deadline> deadlines;
    /** The list of runtime environments for the assignment. */
    public List<String> runtimeEnvironments;
    /** The number of attempts the student has made. */
    public int attempts;
    /** The percentage of points required to pass the assignment. */
    public int pointsPercentualThreshold;
    /** The maximum number of submissions the student can make. */
    public int submissionsCountLimit;
    /** The maximum number of files in the solution. */
    public int solutionFilesLimit;
    /** The maximum size of all files in the solution. */
    public int solutionSizeLimit;

    /**
     * Represents a deadline for an assignment. Contains the deadline time and the
     * number of points the student can gain before the deadline.
     */
    public static class Deadline {
        /** The deadline time. */
        public LocalDateTime time;
        /** The number of points the student can gain before the deadline. */
        public int points;

        /**
         * Constructs a new Deadline object with the given deadline time and number of
         * points.
         * 
         * @param time   The deadline time in seconds since the epoch.
         * @param points The number of points the student can gain before the deadline.
         */
        public Deadline(long time, long points) {
            this.time = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
            this.points = (int) points;
        }
    }

    /**
     * Loads the assignment's data from the given response payload.
     * 
     * @param payload The response payload.
     * 
     * @see cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload
     *      /api/v1/groups/{id}/assignments
     */
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

    /**
     * Loads the assignment's data from the given response payload.
     * 
     * @param payload The response payload.
     * 
     * @see cz.cuni.mff.recodex.api.v1.groups.$id.students.stats.Response.Payload
     *      /api/v1/groups/{id}/students/stats
     */
    public void load(cz.cuni.mff.recodex.api.v1.groups.$id.students.stats.Response.Payload.Assignment payload) {
        points = (int) payload.points.gained;
        bonusPoints = (int) payload.points.bonus;

        notifySubscribers();
    }

    /**
     * Loads the assignment's data from the given response payload.
     * 
     * @param payload The response payload.
     * 
     * @see cz.cuni.mff.recodex.api.v1.exercise_assignments.$id.can_submit.Response.Payload
     *      /api/v1/exercise-assignments/{id}/can-submit
     */
    public void load(cz.cuni.mff.recodex.api.v1.exercise_assignments.$id.can_submit.Response.Payload payload) {
        attempts = (int) payload.evaluated;

        notifySubscribers();
    }

    /**
     * Builds a new Assignment object from the given response payload. Also
     * initiates loading of the assignment's statistics.
     * 
     * @param payload The response payload.
     * @return A new Assignment object.
     * 
     * @see cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload
     *      /api/v1/groups/{id}/assignments
     */
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
