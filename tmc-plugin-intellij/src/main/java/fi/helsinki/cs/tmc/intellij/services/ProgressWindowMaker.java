package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.progress.util.ProgressIndicatorBase;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;

public class ProgressWindowMaker {

    private static final Logger logger = Logger.getInstance(ProgressWindowMaker.class);

    public static ProgressIndicator make(
            String title,
            Project project,
            boolean cancelable,
            boolean hidable,
            boolean indeterminate) {
        logger.debug("Creating progress window. @ProgressWindowMaker");
        ProgressIndicatorBase progressWindow = new ProgressIndicatorBase();
        progressWindow.setIndeterminate(indeterminate);
        progressWindow.setText(title);

        return progressWindow;
    }
}
