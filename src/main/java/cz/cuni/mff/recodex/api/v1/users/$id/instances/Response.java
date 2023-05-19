package cz.cuni.mff.recodex.api.v1.users.$id.instances;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import cz.cuni.mff.recodex.api.v1.ILocalizedText;
import cz.cuni.mff.recodex.api.v1.Locale;

public class Response {
    public boolean success;
    public long code;
    public List<Payload> payload;

    public static class Payload {
        public UUID id;
        public String name;
        public String description;
        public boolean hasValidLicence;
        public boolean isOpen;
        public boolean isAllowed;
        public long createdAt;
        public long updatedAt;
        public Object deletedAt;
        public UUID adminId;
        public RootGroup rootGroup;
        public UUID rootGroupId;

        public static class RootGroup {
            public UUID id;
            public String externalId;
            public boolean organizational;
            public boolean archived;
            @JsonProperty("public")
            public boolean public_;
            public boolean rootGroupPublic;
            public boolean directlyArchived;
            public List<LocalizedText> localizedTexts;
            public List<UUID> primaryAdminsIds;
            public Object parentGroupId;
            public List<Object> parentGroupsIds;
            public List<UUID> childGroups;
            public Object privateData;
            public PermissionHints permissionHints;

            public static class LocalizedText extends ILocalizedText {
                public UUID id;
                public Locale locale;
                public String name;
                public String description;
                public long createdAt;
            }

            public static class PermissionHints {
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
}
