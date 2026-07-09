package fi.helsinki.cs.tmc.intellij.actions.buttonactions;

import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;
import fi.helsinki.cs.tmc.intellij.services.PasteService;
import fi.helsinki.cs.tmc.intellij.snapshots.ButtonInputListener;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Defined in plugin.xml on line &lt;action id="Submit to Pastebin"
 * class="fi.helsinki.cs.tmc.intellij.actions.buttonactions.SubmitPasteAction"&gt; in group actions
 *
 * <p>Submit code to TMC Pastebin.
 */
public class SubmitPasteAction extends AnAction {

    private static final Logger logger = Logger.getInstance(SubmitPasteAction.class);
    private PasteService pasteService;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        logger.info("Performing SubmitPasteAction. @SubmitPasteAction");
        paste(anActionEvent);
    }

    private void paste(AnActionEvent anActionEvent) {
        new ButtonInputListener().receivePastebin();
        logger.info("Showing paste submit form. @SubmitPasteAction");
        if (pasteService == null
                || pasteService.getWindow() == null
                || pasteService.getWindow().isClosed()) {
            Project project = anActionEvent.getProject();
            pasteService = new PasteService();
            pasteService.showSubmitForm(project, TmcCoreHolder.get());
        } else {
            pasteService.getWindow().show();
        }
    }
}
