package fi.helsinki.cs.tmc.intellij.ui.exerciselist;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.DialogWrapper;
import fi.helsinki.cs.tmc.intellij.ui.projectlist.ProjectListWindow;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class ExerciseListDialog extends DialogWrapper {

    private static final Logger logger = Logger.getInstance(ExerciseListDialog.class);
    private final JComponent panel;

    public ExerciseListDialog() {
        super(true);
        setTitle("TMC Exercises");
        ProjectListWindow window = new ProjectListWindow();
        this.panel = window.getBasePanel();
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    public static void display() {
        logger.info("Showing TMC exercise list dialog. @ExerciseListDialog");
        ExerciseListDialog dialog = new ExerciseListDialog();
        dialog.show();
    }
}
