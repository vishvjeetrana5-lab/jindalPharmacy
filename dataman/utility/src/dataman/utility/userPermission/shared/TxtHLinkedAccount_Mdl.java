package dataman.utility.userPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHLinkedAccount_Mdl extends KeyField implements Serializable,IsSerializable{

    private String groupName;
    private String city;
    private String codeValue;

    public TxtHLinkedAccount_Mdl() {
    }

       public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

}
