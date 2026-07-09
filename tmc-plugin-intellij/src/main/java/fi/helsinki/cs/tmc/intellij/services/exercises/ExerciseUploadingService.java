package fi.helsinki.cs.tmc.intellij.services.exercises;

import com.intellij.notification.NotificationType;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.core.exceptions.ExpiredException;
import fi.helsinki.cs.tmc.core.exceptions.ShowToUserException;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import fi.helsinki.cs.tmc.intellij.holders.ProjectListManagerHolder;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.io.CoreProgressObserver;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import fi.helsinki.cs.tmc.intellij.services.ObjectFinder;
import fi.helsinki.cs.tmc.intellij.services.PathResolver;
import fi.helsinki.cs.tmc.intellij.services.TestRunningService;
import fi.helsinki.cs.tmc.intellij.services.ThreadingService;
import fi.helsinki.cs.tmc.intellij.services.errors.ErrorMessageService;
import fi.helsinki.cs.tmc.intellij.ui.login.LoginDialog;
import fi.helsinki.cs.tmc.intellij.ui.submissionresult.SubmissionResultHandler;
import fi.helsinki.cs.tmc.intellij.ui.testresults.TestResultPanelFactory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.diagnostic.Logger;

/** Offers method to upload exercises. */
public class ExerciseUploadingService {

    private static final Logger logger = Logger.getInstance(CheckForExistingExercises.class);

    public void startUploadExercise(
            Project project,
            TmcCore core,
            ObjectFinder finder,
            CheckForExistingExercises checker,
            SubmissionResultHandler handler,
            SettingsTmc settings,
            CourseAndExerciseManager courseAndExerciseManager) {

        logger.info("Starting to upload an exercise. @ExerciseUploadingService");

        String[] exerciseCourse = PathResolver.getCourseAndExerciseName(project);
        Course course = finder.findCourse(getCourseName(exerciseCourse), "name");

        if (!courseAndExerciseManager.isCourseInDatabase(course.getTitle())) {
            Messages.showErrorDialog(project, "Project not identified as TMC exercise", "Error");
            return;
        }

        Exercise exercise =
                courseAndExerciseManager.getExercise(
                        course.getTitle(), getExerciseName(exerciseCourse));

        if (!settings.getToken().isPresent()) {
            LoginDialog.display();
        } else if (exercise == null) {
            logger.warn("Failed to submit an exercise that was null. @ExerciseUploadingService");
            ErrorMessageService error = new ErrorMessageService();
            error.showErrorMessagePopup(
                    "Failed to submit exercise.\nPlease check your internet connection.");

        } else if (exercise.hasDeadlinePassed()) {
            logger.warn("Exercise has expired. @ExerciseUploadingService");
            Messages.showErrorDialog(project, "The deadline for this exercise has passed", "Error");
        } else {
            getResults(
                    project,
                    exercise,
                    core,
                    handler,
                    finder);
            ToolWindowManager.getInstance(project).getToolWindow("TMC Test Results").show();
            ToolWindowManager.getInstance(project).getToolWindow("TMC Test Results").activate(() -> {});
            courseAndExerciseManager.updateSingleCourse(
                    course.getTitle(), checker, finder, settings);
        }
    }

    public void startUploadExercise(
            Project project,
            TmcCore core,
            ObjectFinder finder,
            CheckForExistingExercises checker,
            SubmissionResultHandler handler,
            SettingsTmc settings,
            CourseAndExerciseManager courseAndExerciseManager,
            ThreadingService threadingService,
            TestRunningService testRunningService,
            CoreProgressObserver observer,
            ProgressIndicator window) {

        logger.info("Starting to upload an exercise (full signature). @ExerciseUploadingService");

        String[] exerciseCourse = PathResolver.getCourseAndExerciseName(project);
        Course course = finder.findCourse(getCourseName(exerciseCourse), "name");

        if (!courseAndExerciseManager.isCourseInDatabase(course.getTitle())) {
            Messages.showErrorDialog(project, "Project not identified as TMC exercise", "Error");
            return;
        }

        Exercise exercise =
                courseAndExerciseManager.getExercise(
                        course.getTitle(), getExerciseName(exerciseCourse));

        if (!settings.getToken().isPresent()) {
            LoginDialog.display();
        } else if (exercise == null) {
            logger.warn("Failed to submit an exercise that was null. @ExerciseUploadingService");
            ErrorMessageService error = new ErrorMessageService();
            error.showErrorMessagePopup(
                    "Failed to submit exercise.\nPlease check your internet connection.");

        } else if (exercise.hasDeadlinePassed()) {
            logger.warn("Exercise has expired. @ExerciseUploadingService");
            Messages.showErrorDialog(project, "The deadline for this exercise has passed", "Error");
        } else {
            threadingService.runWithNotification(
                    () -> {
                        try {
                            getSubmissionResult(core, observer, exercise, handler, project);
                        } catch (TmcCoreException exception) {
                            logger.warn(
                                    "Could not getExercise submission results. "
                                            + "@ExerciseUploadingService",
                                    exception);
                            exception.printStackTrace();
                            new ErrorMessageService().showHumanReadableErrorMessage(exception, true);
                        } catch (Exception exception) {
                            logger.warn(
                                    "Could not getExercise submission results. "
                                            + "@ExerciseUploadingService",
                            exception);
                            exception.printStackTrace();
                        }
                    },
                    project,
                    window);
            testRunningService.displayTestWindow(finder);
            courseAndExerciseManager.updateSingleCourse(
                    course.getTitle(), checker, finder, settings);
        }
    }

    private void getResults(
            final Project project,
            final Exercise exercise,
            final TmcCore core,
            final SubmissionResultHandler handler,
            ObjectFinder finder) {

        logger.info("Submitting exercise via Task.Backgroundable. @ExerciseUploadingService.");

        new Task.Backgroundable(project, "Uploading exercise", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                CoreProgressObserver observer = new CoreProgressObserver(indicator);
                try {
                    getSubmissionResult(core, observer, exercise, handler, project);
                } catch (TmcCoreException exception) {
                    logger.warn(
                            "Could not getExercise submission results. "
                                    + "@ExerciseUploadingService",
                            exception);
                    exception.printStackTrace();
                    new ErrorMessageService().showHumanReadableErrorMessage(exception, true);
                } catch (Exception exception) {
                    logger.warn(
                            "Could not getExercise submission results. "
                                    + "@ExerciseUploadingService",
                            exception);
                    exception.printStackTrace();
                }
            }
        }.queue();
    }

    private void getSubmissionResult(
            TmcCore core,
            CoreProgressObserver observer,
            Exercise exercise,
            SubmissionResultHandler handler,
            Project project)
            throws Exception {
        logger.info("Getting submission results. @ExerciseUploadingService");

        final SubmissionResult result = core.submit(observer, exercise).call();
        handler.showResultMessage(exercise, result, project);
        refreshExerciseList();

        ApplicationManager.getApplication()
                .invokeLater(
                        () ->
                                TestResultPanelFactory.updateMostRecentResult(
                                        result.getTestCases(), result.getValidationResult()));
    }

    private String getCourseName(String[] courseAndExercise) {
        logger.info("Getting course name. @ExerciseUploadingService.");
        return courseAndExercise[courseAndExercise.length - 2];
    }

    private String getExerciseName(String[] courseAndExercise) {
        logger.info("Getting exercise name. @ExerciseUploadingService.");
        return courseAndExercise[courseAndExercise.length - 1];
    }

    private static void refreshExerciseList() {
        ApplicationManager.getApplication()
                .executeOnPooledThread(
                        () -> {
                            new CourseAndExerciseManager().initiateDatabase();
                            ApplicationManager.getApplication()
                                    .invokeLater(
                                            () ->
                                                    ProjectListManagerHolder.get()
                                                            .refreshAllCourses());
                        });
    }
}
