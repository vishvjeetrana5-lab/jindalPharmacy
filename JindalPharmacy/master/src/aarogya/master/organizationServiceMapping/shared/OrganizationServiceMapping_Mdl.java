package aarogya.master.organizationServiceMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrganizationServiceMapping_Mdl extends KeyField implements Serializable, IsSerializable {

    private String organizationTag;
    private String organizationText;
    private String rateListCode;
    private String applicableDate;
    private String applicableTo;
    private List<GrdService_Mdl> listServiceDetail = new ArrayList<GrdService_Mdl>();
    private List<GrdRoomCategory_Mdl> listRoomCategory = new ArrayList<GrdRoomCategory_Mdl>();
    private List<GrdOrganizationDetail_Mdl> listOrganizationDetail = new ArrayList<GrdOrganizationDetail_Mdl>();

    public OrganizationServiceMapping_Mdl() {
    }

    public String getOrganizationTag() {
        return organizationTag;
    }

    public void setOrganizationTag(String organizationTag) {
        this.organizationTag = organizationTag;
    }

    public String getOrganizationText() {
        return organizationText;
    }

    public void setOrganizationText(String organizationText) {
        this.organizationText = organizationText;
    }

    public String getApplicableDate() {
        return applicableDate;
    }

    public void setApplicableDate(String applicableDate) {
        this.applicableDate = applicableDate;
    }

    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    public List<GrdService_Mdl> getListServiceDetail() {
        return listServiceDetail;
    }

    public List<GrdRoomCategory_Mdl> getListRoomCategory() {
        return listRoomCategory;
    }

    public List<GrdOrganizationDetail_Mdl> getListOrganizationDetail() {
        return listOrganizationDetail;
    }

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }

}
