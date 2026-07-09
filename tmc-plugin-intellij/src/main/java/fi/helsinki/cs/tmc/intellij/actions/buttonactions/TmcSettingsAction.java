package fi.helsinki.cs.tmc.intellij.actions.buttonactions;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.ui.exerciselist.ExerciseListDialog;
import fi.helsinki.cs.tmc.intellij.ui.login.LoginDialog;
import fi.helsinki.cs.tmc.intellij.ui.settings.SettingsWindow;

public class TmcSettingsAction extends AnAction {

    private static final Logger logger = Logger.getInstance(TmcSettingsAction.class);
    private SettingsWindow window;

    @Override
    public void update(AnActionEvent e) {
        if (ActionPlaces.WELCOME_SCREEN.equals(e.getPlace())) {
            Presentation p = e.getPresentation();
            boolean loggedIn = TmcSettingsManager.get().getToken().isPresent();
            if (loggedIn) {
                p.setText("Open TMC exercises");
                p.setDescription("Browse and download TMC coursework exercises");
            } else {
                p.setText("Get started with TMC");
                p.setDescription("Set up TMC to start working on coursework");
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        logger.info("Performing TmcSettingsAction. @TmcSettingsAction");
        if (ActionPlaces.WELCOME_SCREEN.equals(anActionEvent.getPlace())) {
            if (TmcSettingsManager.get().getToken().isPresent()) {
                ExerciseListDialog.display();
            } else {
                LoginDialog.display();
            }
        } else {
            showSettings();
        }
    }

    public void showSettings() {
        logger.info("Opening TMC setting window. @TmcSettingsAction");
        if (TmcSettingsManager.get().getToken().isPresent()) {
            if (window == null || window.isClosed()) {
                window = new SettingsWindow();
            } else {
                window.show();
            }
        } else {
            LoginDialog.display();
        }
    }
}
