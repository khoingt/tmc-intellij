package fi.helsinki.cs.tmc.intellij.importexercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;

public class JavaProjectDescriptor {
    private static final Logger logger = LoggerFactory.getLogger(JavaProjectDescriptor.class);

    public static ProjectDescriptor create(String path, Set<String> ignoredNames) {
        logger.info("Starting to create Project Descriptor in JavaProjectDescriptor.");
        File contentFile = new File(path);
        ProjectDescriptor projectDescriptor = new ProjectDescriptor();
        projectDescriptor.setContentRoot(contentFile);
        logger.info("Ending to create Project Descriptor in JavaProjectDescriptor.");
        return projectDescriptor;
    }

    public static class ProjectDescriptor {
        private File contentRoot;

        public void setContentRoot(File contentRoot) {
            this.contentRoot = contentRoot;
        }

        public File getContentRoot() {
            return contentRoot;
        }
    }
}
