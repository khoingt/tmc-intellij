package fi.helsinki.cs.tmc.intellij.ui.settings;

import com.intellij.openapi.diagnostic.Logger;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Creates and controls the settings window. */
public class SettingsWindow {

    private static final Logger logger = Logger.getInstance(SettingsWindow.class);

    private final JFrame frame;

    public SettingsWindow() {
        logger.info("Building SettingsWindow. @SettingsWindow");
        frame = new JFrame();
        JPanel panel = new SettingsPanel(frame).getPanel();

        frame.add(panel);
        frame.setTitle("TMC Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setSize(new Dimension(800, 500));
        frame.setAlwaysOnTop(true);
    }

    public boolean isClosed() {
        return (frame == null || !frame.isVisible());
    }

    public void show() {
        logger.info("Showing SettingsWindow. @SettingsWindow");
        frame.setVisible(false);
        frame.setVisible(true);
    }
}
