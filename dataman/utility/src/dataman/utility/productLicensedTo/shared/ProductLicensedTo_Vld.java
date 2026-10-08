package dataman.utility.productLicensedTo.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ProductLicensedTo_Vld {

    public static void chkSave(ProductLicensedTo_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Company Name", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 200);

    }

}
