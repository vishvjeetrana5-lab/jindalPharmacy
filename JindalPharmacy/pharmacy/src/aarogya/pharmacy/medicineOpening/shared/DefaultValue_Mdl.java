package aarogya.pharmacy.medicineOpening.shared;

import aarogya.selfToolkit.stStatic.shared.SaleRateRule_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String voucherTypeName;
    private String departmentTag;
    private String departmentText;
    private List<SaleRateRule_Mdl> lstSaleRateRule = new ArrayList<>();

    public DefaultValue_Mdl() {
    }

    public List<SaleRateRule_Mdl> getLstSaleRateRule() {
        return lstSaleRateRule;
    }

    public void setLstSaleRateRule(List<SaleRateRule_Mdl> lstSaleRateRule) {
        this.lstSaleRateRule = lstSaleRateRule;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

}
