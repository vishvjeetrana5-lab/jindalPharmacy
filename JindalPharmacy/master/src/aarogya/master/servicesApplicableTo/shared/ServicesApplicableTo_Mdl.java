package aarogya.master.servicesApplicableTo.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServicesApplicableTo_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<KeyFieldExtended> grdData = new ArrayList<KeyFieldExtended>();

    private String acknowledgeCode;
    private String acknowledgeName;

    public ServicesApplicableTo_Mdl() {
    }

    public List<KeyFieldExtended> getGrdData() {
        return grdData;
    }

    public String getAcknowledgeCode() {
        return acknowledgeCode;
    }

    public void setAcknowledgeCode(String acknowledgeCode) {
        this.acknowledgeCode = acknowledgeCode;
    }

    public String getAcknowledgeName() {
        return acknowledgeName;
    }

    public void setAcknowledgeName(String acknowledgeName) {
        this.acknowledgeName = acknowledgeName;
    }

}
