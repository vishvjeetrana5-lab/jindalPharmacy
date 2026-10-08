package aarogya.master.additionalUserPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageUserRole_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdManageUserRole_Mdl> listUserDetail = new ArrayList<GrdManageUserRole_Mdl>();

    public ManageUserRole_Mdl() {
    }

    public List<GrdManageUserRole_Mdl> getListUserDetail() {
        return listUserDetail;
    }

    public void setListUserDetail(List<GrdManageUserRole_Mdl> listUserDetail) {
        this.listUserDetail = listUserDetail;
    }

}
