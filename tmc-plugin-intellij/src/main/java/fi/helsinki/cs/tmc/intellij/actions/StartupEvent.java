package fi.helsinki.cs.tmc.intellij.actions;

import com.google.common.base.Optional;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.core.utilities.TmcServerAddressNormalizer;
import fi.helsinki.cs.tmc.intellij.holders.ExerciseDatabaseManager;
import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.io.CoreProgressObserver;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import fi.helsinki.cs.tmc.intellij.services.errors.ErrorMessageService;
import fi.helsinki.cs.tmc.intellij.services.exercises.CheckForNewExercises;
import fi.helsinki.cs.tmc.intellij.services.exercises.CourseAndExerciseManager;
import fi.helsinki.cs.tmc.intellij.services.logging.PropertySetter;
import fi.helsinki.cs.tmc.intellij.snapshots.ActivateSnapshotsListeners;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.wm.ToolWindowManager;

import fi.helsinki.cs.tmc.intellij.ui.login.LoginDialog;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * The actions to be executed on project startup defined in plugin.xml exercises group on line
 * &lt;postStartupActivity implementation ="fi.helsinki.cs.tmc.intellij.actions.StartupEvent"&gt;
 */
public class StartupEvent implements ProjectActivity {

    private static final Logger logger = Logger.getInstance(StartupEvent.class);

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {

        logger.debug("Opening project " + project + " and running startup actions. @StartupEvent");

        ExerciseDatabaseManager.setup();

        new ErrorMessageService()
                .showInfoBalloon(
                        "The Test My Code Plugin for Intellij is in BETA and"
                                + " may not work properly. Use at your own risk. ");

        new Task.Backgroundable(project, "Running TMC startup actions", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                ProgressObserver observer = new CoreProgressObserver(indicator);
                setupLoggers(observer);
                setupTmcSettings(observer);
                CheckForOneDrive.run();
                setupCoreHolder(observer);
                setupSnapshots(observer, project);
                setupDatabase(observer);
                setupHandlersForSnapshots(observer);

                if (TmcSettingsManager.get().getFirstRun()) {
                    TmcSettingsManager.get().setFirstRun(false);
                } else {
                    sendDiagnostics(observer);
                }

                checkForNewExercises(observer);

                ApplicationManager.getApplication()
                        .invokeLater(
                                () -> {
                                    if (!project.isDisposed()) {
                                        ToolWindowManager toolWindowManager =
                                                ToolWindowManager.getInstance(project);
                                        if (toolWindowManager.getToolWindow("Project")
                                                != null) {
                                            toolWindowManager
                                                    .getToolWindow("Project")
                                                    .activate(() -> {});
                                        }
                                    }
                                });
            }
        }.queue();

        showLoginWindow();
        return Unit.INSTANCE;
    }

    private void setupLoggers(ProgressObserver observer) {
        observer.progress(0, 0.0, "Initializing loggers");
        PropertySetter propSet = new PropertySetter();
        propSet.setLog4jProperties();
    }

    private void setupTmcSettings(ProgressObserver observer) {
        observer.progress(0, 0.14, "Loading settings");
        TmcSettingsManager.setup();
    }

    private void setupCoreHolder(ProgressObserver observer) {
        observer.progress(0, 0.28, "Holding core");
        TmcCoreHolder.setup();
    }

    private void setupSnapshots(ProgressObserver observer, Project project) {
        observer.progress(0, 0.42, "Activating listeners");
        new ActivateSnapshotsListeners(project).activateListeners();
    }

    private void setupDatabase(ProgressObserver observer) {
        if (TmcSettingsManager.get().getToken().isPresent()) {
            observer.progress(0, 0.56, "Initializing database");
            new CourseAndExerciseManager().initiateDatabase();
        }
    }

    private void setupHandlersForSnapshots(ProgressObserver observer) {
        observer.progress(0, 0.70, "Setting handlers");
        final EditorActionManager actionManager = EditorActionManager.getInstance();
        final TypedAction typedAction = actionManager.getTypedAction();
        TypedActionHandler originalHandler = actionManager.getTypedAction().getHandler();
        typedAction.setupHandler(new ActivateSnapshotsAction(originalHandler));
    }

    private void checkForNewExercises(ProgressObserver observer) {
        observer.progress(0, 0.84, "Checking for new exercises");
        if (TmcSettingsManager.get().isCheckForExercises()) {
            new CheckForNewExercises().doCheck();
        }
    }

    private void sendDiagnostics(ProgressObserver observer) {
        if (TmcSettingsManager.get().getSendDiagnostics()) {
            try {
                TmcCoreHolder.get().sendDiagnostics(observer).call();
            } catch (Exception e) {
            }
        }
    }

    private void tryToMigratePasswordToOAuthToken() {
        final SettingsTmc settings = TmcSettingsManager.get();
        try {
            TmcServerAddressNormalizer normalizer = new TmcServerAddressNormalizer();
            normalizer.normalize();
            TmcCoreHolder.get()
                    .authenticate(ProgressObserver.NULL_OBSERVER, settings.getPassword().get())
                    .call();
            normalizer.selectOrganizationAndCourse();

            this.migrateCourseAndExerciseDatabase();
        } catch (Exception ex) {
            logger.info(
                    "Couldn't migrate password to OAuth token. The user will be asked to log in.");
            settings.setToken(Optional.absent());
        } finally {
            settings.setPassword(Optional.absent());
        }
    }

    private void migrateCourseAndExerciseDatabase() {
        new CourseAndExerciseManager().initiateDatabase();
    }

    private void showLoginWindow() {
        SettingsTmc settingsTmc = TmcSettingsManager.get();
        if (settingsTmc.getPassword().isPresent()) {
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                this.tryToMigratePasswordToOAuthToken();
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (!settingsTmc.getToken().isPresent()
                            || settingsTmc.getServerAddress().isEmpty()) {
                        LoginDialog.display();
                    }
                });
            });
        } else if (!settingsTmc.getToken().isPresent()
                || settingsTmc.getServerAddress().isEmpty()) {
            LoginDialog.display();
        }
    }
}
