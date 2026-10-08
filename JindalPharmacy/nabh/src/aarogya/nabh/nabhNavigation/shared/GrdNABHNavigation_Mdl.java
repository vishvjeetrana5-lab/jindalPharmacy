package aarogya.nabh.nabhNavigation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdNABHNavigation_Mdl extends KeyField implements Serializable, IsSerializable {

    private String menuLink;
    private String std;
    private String indicatorDescription;
    private String statusTag;
    private String viewReport;
    private String viewEntryPoint;

    public GrdNABHNavigation_Mdl() {
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getIndicatorDescription() {
        return indicatorDescription;
    }

    public void setIndicatorDescription(String indicatorDescription) {
        this.indicatorDescription = indicatorDescription;
    }

    public String getStatusTag() {
        return statusTag;
    }

    public void setStatusTag(String statusTag) {
        this.statusTag = statusTag;
    }

    public String getViewReport() {
        return viewReport;
    }

    public void setViewReport(String viewReport) {
        this.viewReport = viewReport;
    }

    public String getViewEntryPoint() {
        return viewEntryPoint;
    }

    public void setViewEntryPoint(String viewEntryPoint) {
        this.viewEntryPoint = viewEntryPoint;
    }

}
