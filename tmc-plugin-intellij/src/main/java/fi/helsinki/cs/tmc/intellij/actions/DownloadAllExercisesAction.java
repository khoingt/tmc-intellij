package fi.helsinki.cs.tmc.intellij.actions;

import fi.helsinki.cs.tmc.intellij.actions.buttonactions.DownloadExerciseAction;
import fi.helsinki.cs.tmc.intellij.snapshots.ButtonInputListener;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import com.intellij.openapi.diagnostic.Logger;

public class DownloadAllExercisesAction extends AnAction {

    private static final Logger logger = Logger.getInstance(DownloadAllExercisesAction.class);

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        logger.info("Starting to download all courses exercises. @DownloadAllExercisesAction");
        Project project = anActionEvent.getProject();
        new ButtonInputListener().receiveDownloadExercise();
        new DownloadExerciseAction().downloadExercises(project, true);
    }
}
