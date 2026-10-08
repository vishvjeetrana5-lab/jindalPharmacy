package pacsetup.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import pacsetup.actions.AbstractAction;
import pacsetup.actions.DownloadAction;
import pacsetup.actions.InstallPACAction;
import pacsetup.actions.UnzipAction;
import pacsetup.commons.Functions;
import pacsetup.forms.AbstractCommand;

public class InstallPACPlugin extends AbstractCommand {

    public InstallPACPlugin(String urlString, String commandString, String displayCommand) {
        super(urlString, commandString, displayCommand);
    }

    @Override
    public List<? extends AbstractAction> getActions() {
        List<AbstractAction> lstActions = new ArrayList<>();
        try {
            lstActions.add(new DownloadAction(this));
            lstActions.add(new UnzipAction(this));
            lstActions.add(new InstallPACAction(this));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lstActions;
    }

    @Override
    public void run() {
        try {
            List<? extends AbstractAction> actions = getActions();
            for (int i = 0; i < actions.size(); i++) {
                AbstractAction action = actions.get(i);
                setCurrentExecutingAction(action);
                if (action instanceof DownloadAction) {
                    ((DownloadAction) action).getAdditionalReqParameters().put("cPath", Functions.getConfigurationProperty("service_url"));
                } else if (action instanceof UnzipAction) {
                    ((UnzipAction) action).setZippedFilePath(((DownloadAction) actions.get(0)).getPostExecutionParams().get("downloadedfilename"));
                } else if (action instanceof InstallPACAction) {
                    ((InstallPACAction) action).setDownloadedPACPath(((UnzipAction) actions.get(1)).getPostExecutionParams().get("extractedfilename") + File.separatorChar);
                }

                action.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
