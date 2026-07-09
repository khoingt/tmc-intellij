package fi.helsinki.cs.tmc.intellij.importexercise;

import com.intellij.ide.impl.ProjectUtil;
import com.intellij.openapi.diagnostic.Logger;

import java.nio.file.Paths;

public class NewProjectUtilModified {
    private static final Logger logger = Logger.getInstance(NewProjectUtilModified.class);

    public static void importExercise(String path) {
        logger.info("Started importing exercise at " + path);
        ProjectUtil.openOrImport(Paths.get(path));
        logger.info("Exercise import progress is finished.");
    }
}
