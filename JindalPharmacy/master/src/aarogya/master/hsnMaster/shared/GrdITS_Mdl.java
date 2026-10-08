package aarogya.master.hsnMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdITS_Mdl extends KeyField implements Serializable, IsSerializable {

    private String itsAccountTag;
    private String itsAccountText;
    private String itsTag;
    private String itsText;
    private String sNo;

    public GrdITS_Mdl() {
    }

    public String getItsAccountTag() {
        return itsAccountTag;
    }

    public void setItsAccountTag(String itsAccountTag) {
        this.itsAccountTag = itsAccountTag;
    }

    public String getItsAccountText() {
        return itsAccountText;
    }

    public void setItsAccountText(String itsAccountText) {
        this.itsAccountText = itsAccountText;
    }

    public String getItsTag() {
        return itsTag;
    }

    public void setItsTag(String itsTag) {
        this.itsTag = itsTag;
    }

    public String getItsText() {
        return itsText;
    }

    public void setItsText(String itsText) {
        this.itsText = itsText;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

}
