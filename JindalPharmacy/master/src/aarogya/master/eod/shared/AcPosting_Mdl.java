package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AcPosting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String forDate;
    private RecId objRecId = new RecId();
    private List<GrdAcPosting_Mdl> listAcPostingDetail = new ArrayList<GrdAcPosting_Mdl>();
    private List<GrdAcPostedDetail_Mdl> listAcPostedDetail = new ArrayList<GrdAcPostedDetail_Mdl>();

    public AcPosting_Mdl() {
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public List<GrdAcPosting_Mdl> getListAcPostingDetail() {
        return listAcPostingDetail;
    }

    public List<GrdAcPostedDetail_Mdl> getListAcPostedDetail() {
        return listAcPostedDetail;
    }

}
