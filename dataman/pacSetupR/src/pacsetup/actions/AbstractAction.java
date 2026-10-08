package pacsetup.actions;

import java.util.HashMap;
import java.util.Map;
import pacsetup.forms.AbstractCommand;

public abstract class AbstractAction {

    private AbstractCommand parentCommand;

    private EnumStatus currentStatus = EnumStatus.Pending;
    private Map<String, String> preExecutionParams = new HashMap<>();

    public AbstractAction(AbstractCommand command) {
        this.parentCommand = command;
    }

    public AbstractCommand getParentCommand() {
        return this.parentCommand;
    }

    public Map<String, String> getPreExecutionParams() {
        return preExecutionParams;
    }
    
    public abstract void run();
    
    public abstract Map<String,String> getPostExecutionParams();

    public EnumStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EnumStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
