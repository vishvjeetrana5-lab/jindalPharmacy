package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomPosting_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private List<GrdServiceDetail_Mdl> listServiceDetail = new ArrayList<GrdServiceDetail_Mdl>();
    private List<KeyFieldExtended> listRegistrationDetail = new ArrayList<KeyFieldExtended>();

    public RoomPosting_Mdl() {
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public List<GrdServiceDetail_Mdl> getListServiceDetail() {
        return listServiceDetail;
    }

    public List<KeyFieldExtended> getListRegistrationDetail() {
        return listRegistrationDetail;
    }

}
