package cz.cuni.mff.recodex.api.v1.exercise_assignments.$id.can_submit;

public class Response {
    public boolean success;
    public long code;
    public Payload payload;

    public static class Payload {
        public long total;
        public long evaluated;
        public long failed;
        public boolean canSubmit;
        public long submittedCount;
    }
}
