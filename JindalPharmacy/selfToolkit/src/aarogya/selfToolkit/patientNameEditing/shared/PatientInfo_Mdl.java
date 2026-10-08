package aarogya.selfToolkit.patientNameEditing.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PatientInfo_Mdl implements Serializable, IsSerializable {

    private String firstName;
    private String middleName;
    private String lastName;
    private String aliasName;

    public PatientInfo_Mdl() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

}
