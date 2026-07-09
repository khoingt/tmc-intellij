package fi.helsinki.cs.tmc.intellij.importexercise;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

public class ProjectFromSourcesBuilderImplModified {
    private static final Logger logger =
            Logger.getInstance(ProjectFromSourcesBuilderImplModified.class);

    public static void commit(@NotNull final Project project, String path) {
        logger.info("Commit no longer needed — IDE handles project structure automatically.");
    }
}
