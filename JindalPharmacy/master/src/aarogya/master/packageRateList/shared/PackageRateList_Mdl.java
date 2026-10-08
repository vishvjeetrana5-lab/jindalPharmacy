package aarogya.master.packageRateList.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PackageRateList_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private List<GrdPackageDetail_Mdl> listPackageDetail = new ArrayList<GrdPackageDetail_Mdl>();

    public PackageRateList_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<GrdPackageDetail_Mdl> getListPackageDetail() {
        return listPackageDetail;
    }

}
