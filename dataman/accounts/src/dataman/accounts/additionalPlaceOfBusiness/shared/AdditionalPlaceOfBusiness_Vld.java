
package dataman.accounts.additionalPlaceOfBusiness.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;


public class AdditionalPlaceOfBusiness_Vld {
    
    public static void chkSave(AdditionalPlaceOfBusiness_Mdl mrTemp) {
        
        DmShared.isValidText(mrTemp.getAccountTag(), "Link With A/c", 200);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Place", 100);
        DmShared.isValidText(mrTemp.getAddress1(), "Address", 700);
        DmShared.isValidText(mrTemp.getPin(), "Pin", 300);
        DmShared.isValidMobile(mrTemp.getMobile(), 400);
        DmShared.isValidEMail(mrTemp.getEmail(), 500);
    }
}
