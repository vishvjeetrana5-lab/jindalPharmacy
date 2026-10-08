package aarogya.master.D365ImportExport.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class ItemCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String printSrNo;
    private String filterItemType;
    private String isCommon;
    private String uName;
    private String preparedBy;
    private String uEntDt;
    private String transferDt;
    private float saleRateProffitMargine;
    private int allowBifurcation;

    public ItemCategory_Mdl() {
    }

    public String getPrintSrNo() {
        return printSrNo;
    }

    public void setPrintSrNo(String printSrNo) {
        this.printSrNo = printSrNo;
    }

    public String getFilterItemType() {
        return filterItemType;
    }

    public void setFilterItemType(String filterItemType) {
        this.filterItemType = filterItemType;
    }

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getuEntDt() {
        return uEntDt;
    }

    public void setuEntDt(String uEntDt) {
        this.uEntDt = uEntDt;
    }

    public String getTransferDt() {
        return transferDt;
    }

    public void setTransferDt(String transferDt) {
        this.transferDt = transferDt;
    }

    public float getSaleRateProffitMargine() {
        return saleRateProffitMargine;
    }

    public void setSaleRateProffitMargine(float saleRateProffitMargine) {
        this.saleRateProffitMargine = saleRateProffitMargine;
    }

    public int getAllowBifurcation() {
        return allowBifurcation;
    }

    public void setAllowBifurcation(int allowBifurcation) {
        this.allowBifurcation = allowBifurcation;
    }

}
