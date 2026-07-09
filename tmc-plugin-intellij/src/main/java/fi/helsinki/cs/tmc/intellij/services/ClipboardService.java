package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.application.ex.ClipboardUtil;
import com.intellij.openapi.diagnostic.Logger;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/** Offers method for copying text to clip board. */
public class ClipboardService {

    private static final Logger logger = Logger.getInstance(ClipboardService.class);

    public static void copyToClipBoard(String stringToCopy) {
        logger.info("Copying " + stringToCopy + " to the clip board. @ClipboardService");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(stringToCopy);
        clipboard.setContents(selection, null);
    }

    public static String getClipBoard() {
        return ClipboardUtil.getTextInClipboard();
    }
}
