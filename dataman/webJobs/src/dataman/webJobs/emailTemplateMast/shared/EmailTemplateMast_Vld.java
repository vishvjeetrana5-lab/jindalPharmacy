package dataman.webJobs.emailTemplateMast.shared;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EmailTemplateMast_Vld {

    public static void chkSave(EmailTemplateMast_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getTempName(), "Template Name", 100);
        DmShared.isValidText(mrTemp.getTempGroupName(), "Template Group Name", 200);
        DmShared.isValidText(mrTemp.getDataSource().getSourceName(), "Data Source Name", 300);
        if (DmShared.iNull(mrTemp.getDataSource().getSourceType()).intValue() == DmWebJobDataSources.CSV.getSourceType()) {
            DmShared.isValidText(mrTemp.getFunctionText(), "File Name", 400);
        } else {
            DmShared.isValidText(mrTemp.getFunctionText(), "Function Name", 400);
            DmShared.isValidText(mrTemp.getSubject(), "Subject Name", 500);
        }
        if (mrTemp.isStaffInfo() && mrTemp.getLstStaffInfo().size() == 0) {
            throw new DmException(600, "Staff Info is mandatory.");
        }

    }

}
