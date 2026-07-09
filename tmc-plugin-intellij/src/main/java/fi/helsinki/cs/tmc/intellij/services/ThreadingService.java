package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;

public class ThreadingService {

    private static final Logger logger = Logger.getInstance(ThreadingService.class);

    public void runWithNotification(
            final Runnable run, Project project, ProgressIndicator progressWindow) {
        logger.info("Processing runWithNotification. @ThreadingService");

        ApplicationManager.getApplication()
                .executeOnPooledThread(
                        () -> ProgressManager.getInstance().runProcess(run, progressWindow));
    }
}
