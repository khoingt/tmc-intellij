package fi.helsinki.cs.tmc.intellij.services.logging;

import com.intellij.openapi.diagnostic.Logger;

public class PropertySetter {

    private static final Logger logger = Logger.getInstance(PropertySetter.class);

    public void setLog4jProperties() {
        logger.info("IDE handles logging natively.");
    }
}
