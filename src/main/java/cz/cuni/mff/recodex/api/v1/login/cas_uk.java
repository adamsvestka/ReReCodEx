package cz.cuni.mff.recodex.api.v1.login;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class cas_uk {
    public boolean success;
    public long code;
    public Payload payload;

    public static class Payload {
        public String accessToken;
        public User user;
        public Error error;

        public static class User {
            public String id;
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
                public List<String> instancesIds;
                public String role;
                public boolean emptyLocalPassword;
                public boolean isLocal;
                public boolean isExternal;
                public boolean isAllowed;
                public ExternalIDS externalIds;
                public UIData uiData;
                public Settings settings;

                public static class ExternalIDS {
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
