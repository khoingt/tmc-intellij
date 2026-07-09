package fi.helsinki.cs.tmc.intellij.ui.submissionresult.feedback;

import fi.helsinki.cs.tmc.core.domain.submission.FeedbackQuestion;

import com.intellij.openapi.diagnostic.Logger;

public class FeedbackQuestionPanelFactory {

    private static final Logger logger =
            Logger.getInstance(FeedbackQuestionPanelFactory.class);

    public static FeedbackQuestionPanel getPanelForQuestion(FeedbackQuestion question) {
        logger.info(
                "Checking if FeedbackQuestion " + question + " is int range question or text question.");
        if (question.isIntRange()) {
            return new IntRangeQuestionPanel(question);
        } else if (question.isText()) {
            return new TextQuestionPanel(question);
        }

        throw new IllegalArgumentException("Unknown feedback question type: " + question.getKind());
    }
}
