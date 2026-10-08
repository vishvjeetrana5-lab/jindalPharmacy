
package aarogya.pharmacy.egstParam.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;


public class EgstParam_Vld {
    
    public static void chkSave(EGstParam_Mdl mrTemp) throws DmException {
        DmShared.isValidText(mrTemp.getUserGstin(), "User Gstin", 100);
        DmShared.isValidText(mrTemp.getEway_UserName(), "E-way User Name", 200);
        DmShared.isValidText(mrTemp.getEway_UserPassword(), "E-way User Password", 300);
        DmShared.isValidText(mrTemp.getEway_AspUserId(), "E-way Asp Id", 400);
        DmShared.isValidText(mrTemp.getEway_AspPassword(), "E-way Asp Password", 500);
        DmShared.isValidText(mrTemp.getEinv_UserName(), "E-invoice User Name", 600);
        DmShared.isValidText(mrTemp.getEinv_UserPassword(), "E-invoice User Password", 700);
        DmShared.isValidText(mrTemp.getEinv_ASPID(), "E-invoice Asp Id", 800);
        DmShared.isValidText(mrTemp.getEinv_ASPID_Password(), "E-invoice Asp Password", 900);
    }
    
}
