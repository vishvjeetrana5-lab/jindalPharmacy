package aarogya.pharmacy.materialCategoryMast.shared;

import aarogya.pharmacy.materialCategoryMast.shared.*;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MaterialCategoryMast_Vld {

    public static void chkSave(MaterialCategoryMast_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Category Name", 100);
        if (!isAlpha(mrTemp.getSharedKeyFieldName().trim())){
            throw new DmException(200, "Category Name" + " must be only charecter.");
        }
    }
    

     public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }
}
