package aarogya.master.consultantSharingMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultantSharingMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String applicableFrom;
    private List<GrdSharingDetail_Mdl> listSharingDetail = new ArrayList<GrdSharingDetail_Mdl>();

    public ConsultantSharingMaster_Mdl() {
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public List<GrdSharingDetail_Mdl> getListSharingDetail() {
        return listSharingDetail;
    }

}
