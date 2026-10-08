package dataman.utility.companyMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class CompanyMaster_Vld {

    public static void chkSave(CompanyMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getProductLicenseKyCode(), "Product License", 100);
        DmShared.isValidText(mrTemp.getCurrentYear(), "Current Year", 200);
        DmShared.isValidText(mrTemp.getPreviousYear(), "Previous Year", 300);
        DmShared.isValidText(mrTemp.getYearStartDate(), "Year Start Date", 400);
        DmShared.isValidText(mrTemp.getYearEndDate(), "Year End Date", 500);
        if (DmShared.convertDate(mrTemp.getYearEndDate()).getTime() <= DmShared.convertDate(mrTemp.getYearStartDate()).getTime()) {
            throw new DmException(600, "Year End Date must be greater than Year Start Date.");
        }
        DmShared.isValidText(mrTemp.getVoucherPrefix().trim(), "Voucher Prefix", 700);
        DmShared.isValidText(mrTemp.getDataBaseName().trim(), "Database Name", 800);
        DmShared.isValidText(mrTemp.getFileDataBaseName().trim(), "File Database Name", 900);
        DmShared.isValidText(mrTemp.getFinacialYearTag().trim(), "Latest Financial Year", 1000);
    }
}
