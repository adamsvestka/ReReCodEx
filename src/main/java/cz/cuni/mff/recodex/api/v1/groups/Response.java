package cz.cuni.mff.recodex.api.v1.groups;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import cz.cuni.mff.recodex.api.v1.Locale;

public class Response {
    public boolean success;
    public long code;
    public List<Payload> payload;

    public static class Payload {
        public UUID id;
        public String externalId;
        public boolean organizational;
        public boolean archived;
        @JsonProperty("public")
        public boolean public_;
        public boolean payloadPublic;
        public boolean directlyArchived;
        public List<LocalizedText> localizedTexts;
        public List<UUID> primaryAdminsIds;
        public UUID parentGroupId;
        public List<UUID> parentGroupsIds;
        public List<Object> childGroups;
        public PrivateData privateData;
        public PermissionHints permissionHints;

        public static class LocalizedText {
            public UUID id;
            public Locale locale;
            public String name;
            public String description;
            public long createdAt;
        }

        public static class PrivateData {
            public List<UUID> admins;
            public List<UUID> supervisors;
            public List<Object> observers;
            public List<UUID> students;
            public UUID instanceId;
            public boolean hasValidLicence;
            public List<UUID> assignments;
            public List<UUID> shadowAssignments;
            public boolean publicStats;
            public boolean detaining;
            public Object threshold;
            public Bindings bindings;

            public class Bindings {
                public List<String> sis;
            }
        }

        public class PermissionHints {
            public boolean viewAssignments;
            public boolean viewDetail;
            public boolean viewSubgroups;
            public boolean viewStudents;
            public boolean viewMembers;
            public boolean inviteStudents;
            public boolean viewStats;
            public boolean addSubgroup;
            public boolean update;
            public boolean remove;
            public boolean archive;
            public boolean relocate;
            public boolean viewExercises;
            public boolean assignExercise;
            public boolean createExercise;
            public boolean createShadowAssignment;
            public boolean viewPublicDetail;
            public boolean becomeMember;
            public boolean sendEmail;
            public boolean viewInvitations;
            public boolean acceptInvitation;
            public boolean editInvitations;
        }
    }
}
