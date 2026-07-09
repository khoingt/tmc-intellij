package fi.helsinki.cs.tmc.intellij.actions.buttonactions;

import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;

import fi.helsinki.cs.tmc.intellij.services.ObjectFinder;
import fi.helsinki.cs.tmc.intellij.services.exercises.CheckForExistingExercises;
import fi.helsinki.cs.tmc.intellij.services.exercises.ExerciseDownloadingService;
import fi.helsinki.cs.tmc.intellij.snapshots.ButtonInputListener;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Defined in plugin.xml on line &lt;action id="Download Exercises"
 * class="fi.helsinki.cs.tmc.intellij.actions.buttonactions.DownloadExerciseAction"&gt; in group
 * actions
 *
 * <p>Downloads exercises from the course selected in settings, uses CheckForExistingExercises to
 * check already downloaded ones, updates exercise lists with CourseAndExerciseManager
 */
public class DownloadExerciseAction extends AnAction {

    private static final Logger logger = Logger.getInstance(DownloadExerciseAction.class);

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        new ButtonInputListener().receiveDownloadExercise();
        downloadExercises(project, false);
    }

    public void downloadExercises(Project project, boolean downloadAll) {
        logger.info("Performing DownloadExerciseAction. @DownloadExerciseAction");
        try {
            startDownloadExercise(project, downloadAll);
        } catch (Exception exception) {
            logger.warn("Downloading failed. @DownloadExerciseAction", exception);
            Messages.showMessageDialog(
                    project,
                    "Downloading failed \n"
                            + "Are your account details correct?\n"
                            + exception.getMessage(),
                    "Result",
                    Messages.getErrorIcon());
        }
    }

    private void startDownloadExercise(Project project, boolean downloadAll) throws Exception {
        ExerciseDownloadingService
                .startDownloadExercise(
                        TmcCoreHolder.get(),
                        TmcSettingsManager.get(),
                        new CheckForExistingExercises(),
                        new ObjectFinder(),
                        project,
                        downloadAll);
    }
}
