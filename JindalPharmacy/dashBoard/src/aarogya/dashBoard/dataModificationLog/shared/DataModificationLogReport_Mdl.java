package aarogya.dashBoard.dataModificationLog.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DataModificationLogReport_Mdl implements Serializable, IsSerializable {

    private String logDateTime;
    private String userNameCode;
    private String userName;
    private String financialyear;
    private String modeCode;
    private String mode;
    private String vrTypeShortName;
    private String moduleCode;
    private String module;
    private String menu;
    private String modifyReason;
    private String recordName1;
    private String recordDesc1;
    private String recordName2;
    private String recordDesc2;
    private String shortSiteName;
    private String remark;

    private String oldData;
    private String newData;

    public DataModificationLogReport_Mdl() {

    }

}
