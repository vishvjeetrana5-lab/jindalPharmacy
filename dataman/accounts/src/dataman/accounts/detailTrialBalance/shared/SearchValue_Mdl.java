package dataman.accounts.detailTrialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private DefaultValue_Mdl defaultValue;
    private AcDisplaySetting_Mdl acLedgerDisplay;
    private String searchValue;

    public SearchValue_Mdl() {

    }

    public DefaultValue_Mdl getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(DefaultValue_Mdl defaultValue) {
        this.defaultValue = defaultValue;
    }

    public AcDisplaySetting_Mdl getAcLedgerDisplay() {
        return acLedgerDisplay;
    }

    public void setAcLedgerDisplay(AcDisplaySetting_Mdl acLedgerDisplay) {
        this.acLedgerDisplay = acLedgerDisplay;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
}
