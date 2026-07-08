package fi.helsinki.cs.tmc.intellij.services.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertySetter {

    private static final Logger logger = LoggerFactory.getLogger(PropertySetter.class);

    public void setLog4jProperties() {
        logger.info("Log4j configuration not needed — IDE handles logging.");
    }
}
