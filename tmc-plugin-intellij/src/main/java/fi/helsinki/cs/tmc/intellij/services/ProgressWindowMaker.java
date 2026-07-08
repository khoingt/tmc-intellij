package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.progress.util.ProgressIndicatorBase;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressWindowMaker {

    private static final Logger logger = LoggerFactory.getLogger(ProgressWindowMaker.class);

    public static ProgressIndicator make(
            String title,
            Project project,
            boolean cancelable,
            boolean hidable,
            boolean indeterminate) {
        logger.info("Creating progress window. @ProgressWindowMaker");
        ProgressIndicatorBase progressWindow = new ProgressIndicatorBase();
        progressWindow.setIndeterminate(indeterminate);
        progressWindow.setText(title);

        return progressWindow;
    }
}
