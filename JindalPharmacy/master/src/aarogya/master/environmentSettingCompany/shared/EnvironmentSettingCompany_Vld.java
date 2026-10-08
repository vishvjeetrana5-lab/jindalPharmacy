package aarogya.master.environmentSettingCompany.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EnvironmentSettingCompany_Vld {

    public static void chkSave(EnvironmentSettingCompany_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getAcGroupConsCode(), "A/c Group Consultant", 100);
        DmShared.isValidText(mrTemp.getAcGroupDepartCode(), "A/c Group Department", 200);
        DmShared.isValidText(mrTemp.getAcGroupOrgCode(), "A/c Group Organization", 400);
        DmShared.isValidText(mrTemp.getAcGroupRefCode(), "A/c Group Referred", 500);
        DmShared.isValidText(mrTemp.getAcGroupEmpCode(), "A/c Group Employee", 600);
        DmShared.isValidText(mrTemp.getAcGroupPharmacySupplierCode(), "A/c Group Pharmacy Supplier", 700);
        DmShared.isValidText(mrTemp.getAcGroupInventorySupplierCode(), "A/c Group Inventory Supplier", 800);
        DmShared.isValidText(mrTemp.getAcGroupManufacturerCode(), "A/c Group Manufacturer", 900);
        DmShared.isValidText(mrTemp.getAcGroupDivisionCode(), "A/c Group Division", 1000);
        DmShared.isValidText(mrTemp.getAcGroupCustomerCode(), "A/c Group Customer", 1100);
    }

}
