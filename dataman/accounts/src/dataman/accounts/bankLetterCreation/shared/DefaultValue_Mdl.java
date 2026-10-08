package dataman.accounts.bankLetterCreation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String bankLetterCreationVTypeCode;
    private String bankLetterCreationVTypeName;

    private String currentDate;
    private String currentTime;

    public DefaultValue_Mdl() {
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getBankLetterCreationVTypeCode() {
        return bankLetterCreationVTypeCode;
    }

    public void setBankLetterCreationVTypeCode(String bankLetterCreationVTypeCode) {
        this.bankLetterCreationVTypeCode = bankLetterCreationVTypeCode;
    }

    public String getBankLetterCreationVTypeName() {
        return bankLetterCreationVTypeName;
    }

    public void setBankLetterCreationVTypeName(String bankLetterCreationVTypeName) {
        this.bankLetterCreationVTypeName = bankLetterCreationVTypeName;
    }

}
