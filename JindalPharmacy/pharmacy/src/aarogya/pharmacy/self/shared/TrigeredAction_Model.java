package aarogya.pharmacy.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

//This model is used for Refresh of grid in Requisition Display after saving of Indent/Requisition
public class TrigeredAction_Model implements Serializable, IsSerializable{
    private String startupAction;
    private String performedAction;

    public String getStartupAction() {
        return startupAction;
    }

    public void setStartupAction(String startupAction) {
        this.startupAction = startupAction;
    }

    public String getPerformedAction() {
        return performedAction;
    }

    public void setPerformedAction(String performedAction) {
        this.performedAction = performedAction;
    }
    
    
}
