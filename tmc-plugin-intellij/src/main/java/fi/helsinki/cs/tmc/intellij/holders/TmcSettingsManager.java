package fi.helsinki.cs.tmc.intellij.holders;

import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import fi.helsinki.cs.tmc.intellij.services.persistence.PersistentTmcSettings;

import com.intellij.openapi.components.ServiceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Contains the TmcSettings. */
public final class TmcSettingsManager {

    private static final Logger logger = LoggerFactory.getLogger(TmcSettingsManager.class);

    private TmcSettingsManager() {}

    private static PersistentTmcSettings persistentSettings;

    private static synchronized PersistentTmcSettings ps() {
        if (persistentSettings == null) {
            persistentSettings = ServiceManager.getService(PersistentTmcSettings.class);
        }
        return persistentSettings;
    }

    public static synchronized SettingsTmc get() {
        logger.info("Get SettingsTmc. @TmcSettingsManager.");
        PersistentTmcSettings p = ps();
        if (p.getSettingsTmc() == null) {
            p.setSettingsTmc(new SettingsTmc());
        }
        return p.getSettingsTmc();
    }

    public static synchronized void setup() {
        logger.info("Setup SettingsTmc. @TmcSettingsManager.");
        PersistentTmcSettings p = ps();
        if (p.getSettingsTmc() == null) {
            p.setSettingsTmc(new SettingsTmc());
        }
    }
}
