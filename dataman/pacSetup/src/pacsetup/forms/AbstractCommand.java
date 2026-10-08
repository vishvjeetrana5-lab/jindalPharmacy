package pacsetup.forms;

import pacsetup.actions.AbstractAction;
import pacsetup.actions.EnumStatus;
import java.util.List;
import java.util.ArrayList;

public abstract class AbstractCommand implements Runnable {

    private String command;
    private String displayCommand;
    private String urlString;
    private AbstractAction currentExecutingAction;

    public AbstractCommand(String urlString, String commandString, String displayCommand) {
        this.urlString = urlString.trim();
        this.command = commandString.trim();
        this.displayCommand = displayCommand.trim();
    }

    public abstract List<? extends AbstractAction> getActions();

    public String getDisplayCommand() {
        return displayCommand;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getStatusString() {
        EnumStatus status = getCurrentStatus();
        return (status != null ? status.toString() : "");
    }

    public String getCurrentExecutingActionString() {
        String value = "";
        if (this.currentExecutingAction != null) {
            value = this.currentExecutingAction.toString();
        }
        return value;
    }

    public EnumStatus getCurrentStatus() {
        return (currentExecutingAction != null ? currentExecutingAction.getCurrentStatus() : null);
    }

    public AbstractAction getCurrentExecutingAction() {
        return currentExecutingAction;
    }

    public void setCurrentExecutingAction(AbstractAction currentExecutingAction) {
        this.currentExecutingAction = currentExecutingAction;
    }
}
