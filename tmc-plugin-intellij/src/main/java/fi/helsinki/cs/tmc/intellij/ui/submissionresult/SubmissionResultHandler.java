package fi.helsinki.cs.tmc.intellij.ui.submissionresult;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;

import com.intellij.openapi.project.Project;

import com.intellij.openapi.diagnostic.Logger;

public class SubmissionResultHandler {

    private static final Logger logger = Logger.getInstance(SubmissionResultHandler.class);

    public void showResultMessage(Exercise exercise, SubmissionResult result, Project project) {
        logger.info("Showing submission result message. @SubmissionResultHandler");

        if (result.isAllTestsPassed()) {
            new SuccessfulSubmissionDialog(exercise, result, project);
        } else {
            new FailedSubmissionDialog(result, project);
        }
    }
}
