package fi.helsinki.cs.tmc.intellij.runners;

import com.intellij.execution.RunManager;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.diagnostic.Logger;

public class RunProject {

    private static final Logger logger = Logger.getInstance(RunProject.class);

    private final RunConfigurationFactory factory;

    public RunProject(RunManager runManager, Module module, String configurationType) {
        logger.info("Creating RunConfigurationFactory.");
        factory = new RunConfigurationFactory(runManager, module, configurationType);
        if (makeSureConfigurationIsCorrectType(runManager)) {
            return;
        }
        factory.createRunner();
    }

    private boolean makeSureConfigurationIsCorrectType(RunManager runManager) {
        if (runManager.getSelectedConfiguration() == null || factory.checkConfigurationType()) {
            logger.info("Prompting user to choose main class with Chooser.");
            TreeClassChooser chooser = factory.chooseMainClassForProject();
            if (chooser.getSelected() == null) {
                logger.warn("Choosing main class returned null and running is cancelled.");
                return true;
            }
            logger.info("Creating configurations.");
            factory.createConfiguration();
            factory.configApplicationConfiguration(chooser);
        }
        return false;
    }
}
