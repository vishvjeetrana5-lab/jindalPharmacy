package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class ItemCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String code;
    private String his_code;
    private String name;
    private String saleRateProffitMargine;
    private String filterItemType;

    public ItemCategory_Mdl() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHis_code() {
        return his_code;
    }

    public void setHis_code(String his_code) {
        this.his_code = his_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSaleRateProffitMargine() {
        return saleRateProffitMargine;
    }

    public void setSaleRateProffitMargine(String saleRateProffitMargine) {
        this.saleRateProffitMargine = saleRateProffitMargine;
    }

    public String getFilterItemType() {
        return filterItemType;
    }

    public void setFilterItemType(String filterItemType) {
        this.filterItemType = filterItemType;
    }

}
