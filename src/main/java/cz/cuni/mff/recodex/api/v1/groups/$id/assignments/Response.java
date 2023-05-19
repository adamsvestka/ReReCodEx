package cz.cuni.mff.recodex.api.v1.groups.$id.assignments;

import java.util.List;
import java.util.UUID;

import cz.cuni.mff.recodex.api.v1.ILocalizedText;
import cz.cuni.mff.recodex.api.v1.Locale;

public class Response {
    public boolean success;
    public long code;
    public List<Payload> payload;

    public static class Payload {
        public UUID id;
        public long version;
        public boolean isPublic;
        public long createdAt;
        public long updatedAt;
        public List<LocalizedText> localizedTexts;
        public UUID exerciseId;
        public UUID groupId;
        public long firstDeadline;
        public long secondDeadline;
        public boolean allowSecondDeadline;
        public long maxPointsBeforeFirstDeadline;
        public long maxPointsBeforeSecondDeadline;
        public boolean maxPointsDeadlineInterpolation;
        public Object visibleFrom;
        public long submissionsCountLimit;
        public List<String> runtimeEnvironmentIds;
        public List<String> disabledRuntimeEnvironmentIds;
        public boolean canViewLimitRatios;
        public boolean canViewJudgeStdout;
        public boolean canViewJudgeStderr;
        public boolean mergeJudgeLogs;
        public boolean isBonus;
        public long pointsPercentualThreshold;
        public ExerciseSynchronizationInfo exerciseSynchronizationInfo;
        public long solutionFilesLimit;
        public long solutionSizeLimit;
        public PermissionHints permissionHints;

        public static class LocalizedText extends ILocalizedText {
            public UUID id;
            public Locale locale;
            public String name;
            public String text;
            public String link;
            public long createdAt;
            public String studentHint;
        }

        public static class ExerciseSynchronizationInfo {
            public boolean isSynchronizationPossible;
            public UpdatedAt updatedAt;
            public AttachmentFiles exerciseConfig;
            public AttachmentFiles configurationType;
            public AttachmentFiles scoreConfig;
            public AttachmentFiles exerciseEnvironmentConfigs;
            public AttachmentFiles hardwareGroups;
            public AttachmentFiles localizedTexts;
            public AttachmentFiles limits;
            public AttachmentFiles exerciseTests;
            public AttachmentFiles supplementaryFiles;
            public AttachmentFiles attachmentFiles;
            public AttachmentFiles runtimeEnvironments;
            public AttachmentFiles mergeJudgeLogs;

            public static class UpdatedAt {
                public long assignment;
                public long exercise;
            }

            public static class AttachmentFiles {
                public boolean upToDate;
            }
        }

        public static class PermissionHints {
            public boolean viewDetail;
            public boolean viewDescription;
            public boolean update;
            public boolean remove;
            public boolean resubmitSubmissions;
            public boolean viewAssignmentSolutions;
        }
    }
}
