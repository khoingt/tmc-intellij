package fi.helsinki.cs.tmc.intellij.io;

import fi.helsinki.cs.tmc.core.domain.ProgressObserver;

import com.intellij.openapi.progress.ProgressIndicator;

import com.intellij.openapi.diagnostic.Logger;

public class CoreProgressObserver extends ProgressObserver {

    private final ProgressIndicator progressWindow;
    private static final Logger logger = Logger.getInstance(CoreProgressObserver.class);

    public CoreProgressObserver(ProgressIndicator progressWindow) {
        this.progressWindow = progressWindow;
    }

    @Override
    public void progress(long mysteryLong, String status) {
        logger.info("Setting progress status. @CoreProgressObserver");
        progressWindow.setText2(status);
        progressWindow.checkCanceled();
    }

    @Override
    public void progress(long mysteryLong, Double progress, String status) {
        logger.info("Setting progress status. @CoreProgressObserver");
        progressWindow.setText2(status);
        progressWindow.setFraction(progress);
        progressWindow.checkCanceled();
    }

    @Override
    public void start(long mysteryLong) {
        logger.info("Opening progress window. @CoreProgressObserver");
        progressWindow.start();
    }

    @Override
    public void end(long mysteryLong) {
        logger.info("Closing progress window. @CoreProgressObserver");
        progressWindow.stop();
    }
}
