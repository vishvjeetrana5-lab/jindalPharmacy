package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LabConsumptionPosting_Mdl implements Serializable, IsSerializable {

    private List<GrdLabConsumptionDetail_Mdl> listAcPostingDetail = new ArrayList<GrdLabConsumptionDetail_Mdl>();
    private List<GrdLabConsumptionDetail_Mdl> listAcPostedDetail = new ArrayList<GrdLabConsumptionDetail_Mdl>();

    public LabConsumptionPosting_Mdl() {
    }

    public List<GrdLabConsumptionDetail_Mdl> getListAcPostingDetail() {
        return listAcPostingDetail;
    }

    public List<GrdLabConsumptionDetail_Mdl> getListAcPostedDetail() {
        return listAcPostedDetail;
    }

}
