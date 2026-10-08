package dataman.webJobs.notificationEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class NotificationEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    
    private String entryDate;
    private String expiryDate;
  

    public NotificationEntry_Mdl() {
    }

  

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    
}
