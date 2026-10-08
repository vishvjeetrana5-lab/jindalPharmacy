package dataman.payroll.leaveMaster.shared;

import dataman.dmBase.self.shared.KeyField;
import dataman.payroll.self.shared.PREnum.LEAVE_NATURE;
import dataman.payroll.self.shared.PREnum.LEAVE_TYPE;
import java.util.ArrayList;

public class LeaveMaster_Mdl extends KeyField {

    private String shortName; 
    private LEAVE_NATURE leave_nature;
    private LEAVE_TYPE leave_type;
    private String isActive;
    private String isActiveName;
    private String plusMinus;
    private ArrayList<GrdDate_Mdl> grdLst = new ArrayList<GrdDate_Mdl>();

    public LeaveMaster_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
   
    public ArrayList<GrdDate_Mdl> getGrdLst() {
        return grdLst;
    }

    public void setGrdLst(ArrayList<GrdDate_Mdl> grdLst) {
        this.grdLst = grdLst;
    }

    public LEAVE_NATURE getLeave_nature() {
        return leave_nature;
    }

    public void setLeave_nature(LEAVE_NATURE leave_nature) {
        this.leave_nature = leave_nature;
    }

    public LEAVE_TYPE getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(LEAVE_TYPE leave_type) {
        this.leave_type = leave_type;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsActiveName() {
        return isActiveName;
    }

    public void setIsActiveName(String isActiveName) {
        this.isActiveName = isActiveName;
    }

    public String getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(String plusMinus) {
        this.plusMinus = plusMinus;
    }
    

}
