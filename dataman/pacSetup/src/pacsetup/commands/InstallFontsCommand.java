package pacsetup.commands;

import java.util.ArrayList;
import java.util.List;
import pacsetup.actions.AbstractAction;
import pacsetup.actions.DownloadAction;
import pacsetup.actions.InstallFontsAction;
import pacsetup.actions.UnzipAction;
import pacsetup.forms.AbstractCommand;

public class InstallFontsCommand extends AbstractCommand {

    public InstallFontsCommand(String urlString, String commandString, String displayCommand) {
        super(urlString, commandString, displayCommand);
    }

    @Override
    public void run() {
        try {
            List<? extends AbstractAction> actions = getActions();
            for (int i = 0; i < actions.size(); i++) {
                AbstractAction action = actions.get(i);
                setCurrentExecutingAction(action);
                if (action instanceof UnzipAction) {
                    ((UnzipAction) action).setZippedFilePath(((DownloadAction) actions.get(0)).getPostExecutionParams().get("downloadedfilename"));
                } else if (action instanceof InstallFontsAction) {
                    ((InstallFontsAction) action).setFontsFullPath(((UnzipAction) actions.get(1)).getPostExecutionParams().get("extractedfilename"));
                }
                
                action.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<? extends AbstractAction> getActions() {
        List<AbstractAction> lstActions = new ArrayList<>();
        try {
            lstActions.add(new DownloadAction(this));
            lstActions.add(new UnzipAction(this));
            lstActions.add(new InstallFontsAction(this));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lstActions;
    }

}
