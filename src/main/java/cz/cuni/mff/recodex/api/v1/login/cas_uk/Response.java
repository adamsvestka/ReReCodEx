package cz.cuni.mff.recodex.api.v1.login.cas_uk;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    public boolean success;
    public long code;
    public Payload payload;

    public static class Payload {
        public String accessToken;
        public User user;
        public Error error;

        public static class User {
            public UUID id;
            public String fullName;
            public Name name;
            public String avatarUrl;
            public boolean isVerified;
            public PrivateData privateData;

            public static class Name {
                public String titlesBeforeName;
                public String firstName;
                public String lastName;
                public String titlesAfterName;
            }

            public static class PrivateData {
                public String email;
                public long createdAt;
                public long lastAuthenticationAt;
                public List<UUID> instancesIds;
                public String role;
                public boolean emptyLocalPassword;
                public boolean isLocal;
                public boolean isExternal;
                public boolean isAllowed;
                public ExternalIds externalIds;
                public UIData uiData;
                public Settings settings;

                public static class ExternalIds {
                    @JsonProperty("cas-uk")
                    public String casUk;
                }

                public static class UIData {
                    public long systemMessagesAccepted;
                    public boolean darkTheme;
                    public boolean vimMode;
                    public boolean openedSidebar;
                    public boolean useGravatar;
                    public String defaultPage;
                    public long editorFontSize;
                    public boolean lastNameFirst;
                    public String dateFormatOverride;
                }

                public static class Settings {
                    public String defaultLanguage;
                    public boolean newAssignmentEmails;
                    public boolean assignmentDeadlineEmails;
                    public boolean submissionEvaluatedEmails;
                    public boolean solutionCommentsEmails;
                    public boolean solutionReviewsEmails;
                    public boolean assignmentCommentsEmails;
                    public boolean pointsChangedEmails;
                    public boolean assignmentSubmitAfterAcceptedEmails;
                    public boolean assignmentSubmitAfterReviewedEmails;
                }
            }
        }
    }
}
