package cz.cuni.mff.recodex.api.v1.groups.$id.students.stats;

import java.util.List;
import java.util.UUID;

public class Response {
    public boolean success;
    public long code;
    public List<Payload> payload;

    public static class Payload {
        public UUID userId;
        public UUID groupId;
        public PayloadPoints points;
        public boolean hasLimit;
        public boolean passesLimit;
        public List<Assignment> assignments;
        public List<ShadowAssignment> shadowAssignments;

        public static class PayloadPoints {
            public long total;
            public long gained;
        }

        public static class Assignment {
            public UUID id;
            public String status;
            public AssignmentPoints points;
            public UUID bestSolutionId;
            public boolean accepted;

            public static class AssignmentPoints {
                public long total;
                public long gained;
                public long bonus;
            }
        }

        public static class ShadowAssignment {
            public UUID id;
            public ShadowAssignmentPoints points;

            public static class ShadowAssignmentPoints {
                public long total;
                public Long gained;
            }
        }
    }
}
