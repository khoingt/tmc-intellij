package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ThreadingService {

    public void runWithNotification(
            final Runnable run, Project project, ProgressIndicator progressWindow) {

        new Task.Backgroundable(project, "Working...", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                run.run();
            }
        }.queue();
    }
}
