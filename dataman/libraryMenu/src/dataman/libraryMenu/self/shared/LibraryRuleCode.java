package dataman.libraryMenu.self.shared;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;

public enum LibraryRuleCode {

    GUI("GUI", "GUI Configuration" , "Images" , 5120 ,"jpg,bmp,gif,jpeg"),
    CAMERA("SSHOT", "Donor Register,Billing Forecast,CPOE,Current Occupancy,"
            + "Day Care Schedule,Dietitian,Day Care Case Summary,Day Care Registration,Discharge Card, "
            + "Emergency,Admission,Employee Master,Point Of Care,Consultation,Patient File,Prescription,UHID Modification",
            "Snapshot", 5120, "jpg,bmp,jpeg"),
    EMPLOYEE("EMPFI", "Employee Master", "Documents", 5120, "docx"),
    EMAIL("EMT", "Email Template Master", "Documents" , 5120 ,"docx");

    private String ruleCode, description , fileType ,  filters;
    private int size;

    private LibraryRuleCode(String ruleCode, String description , String fileType , int size , String filters ) {
        this.ruleCode = ruleCode;
        this.description = description;
        this.fileType = fileType;
        this.size = size;
        this.filters = filters;
    }


    
    public String getRuleCode() {
        return ruleCode;
    }

    public String getDescription() {
        return description;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFilters() {
        return filters;
    }

    public int getSize() {
        return size;
    }
    
    
    public static LibraryRuleCode getLibraryRuleCode(String ruleCode) {
        LibraryRuleCode rtnValue = null;

        if (!DmBaseShared.xNull(ruleCode).equalsIgnoreCase("")) {
            ruleCode = DmBaseShared.removeSpace(ruleCode).replace(" ", "");
            if (ruleCode.equalsIgnoreCase(GUI.getRuleCode())) {
                rtnValue = GUI;
            } else if (ruleCode.equalsIgnoreCase(EMAIL.getRuleCode())) {
                rtnValue = EMAIL;
            }
        }
        return rtnValue;
    }
}
