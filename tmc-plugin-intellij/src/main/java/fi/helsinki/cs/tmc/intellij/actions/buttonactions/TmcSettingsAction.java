package fi.helsinki.cs.tmc.intellij.actions.buttonactions;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.DialogWrapper;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.ui.login.LoginDialog;
import fi.helsinki.cs.tmc.intellij.ui.projectlist.ProjectListWindow;
import fi.helsinki.cs.tmc.intellij.ui.settings.SettingsWindow;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class TmcSettingsAction extends AnAction {

    private static final Logger logger = Logger.getInstance(TmcSettingsAction.class);
    private SettingsWindow window;

    @Override
    public void update(AnActionEvent e) {
        if (ActionPlaces.WELCOME_SCREEN.equals(e.getPlace())) {
            if (TmcSettingsManager.get().getToken().isPresent()) {
                e.getPresentation().setText("Open TMC exercises");
                e.getPresentation().setDescription("Browse and download TMC coursework exercises");
            } else {
                e.getPresentation().setText("Get started with TMC");
                e.getPresentation().setDescription("Set up TMC to start working on coursework");
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        logger.info("Performing TmcSettingsAction. @TmcSettingsAction");
        if (ActionPlaces.WELCOME_SCREEN.equals(anActionEvent.getPlace())) {
            if (TmcSettingsManager.get().getToken().isPresent()) {
                DialogWrapper dialog = new DialogWrapper(true) {
                    {
                        init();
                    }

                    @Nullable
                    @Override
                    protected JComponent createCenterPanel() {
                        return new ProjectListWindow().getBasePanel();
                    }
                };
                dialog.setTitle("TMC Exercises");
                dialog.show();
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
