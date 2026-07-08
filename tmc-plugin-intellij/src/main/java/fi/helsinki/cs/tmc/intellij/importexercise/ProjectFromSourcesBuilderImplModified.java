package fi.helsinki.cs.tmc.intellij.importexercise;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectFromSourcesBuilderImplModified {
    private static final Logger logger =
            LoggerFactory.getLogger(ProjectFromSourcesBuilderImplModified.class);

    public static void commit(@NotNull final Project project, String path) {
        logger.info("Commit no longer needed — IDE handles project structure automatically.");
    }
}
