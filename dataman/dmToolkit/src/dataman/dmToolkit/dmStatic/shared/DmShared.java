package dataman.dmToolkit.dmStatic.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.UserPermission;
import java.util.ArrayList;
import java.util.List;

public class DmShared extends DmBaseShared {
    
    public static final String PRIMARY_ADDRESS_OF_BUSINESS = "Primary Address Of Business";

    public static List<KeyFieldExtended> filterKeyFieldList(DmHelper helper, List<KeyFieldExtended> keyFieldList) {
        List<KeyFieldExtended> keyFieldListReturn = new ArrayList<>();
        String searchString = "";

        if (helper.getFilter() != null) {
            if (helper.getFilter().get(0) != null) {
                searchString = helper.getFilter().get(0).getValue1();
            }
        }

        for (KeyFieldExtended keyField : keyFieldList) {
            if (DmShared.xNull(keyField.getSharedKeyFieldName()).trim().toLowerCase().startsWith(DmShared.xNull(searchString).trim().toLowerCase())) {
                keyFieldListReturn.add(new KeyFieldExtended(keyField.getSharedKeyFieldCode(), keyField.getSharedKeyFieldName()));
            }
        }

        return keyFieldListReturn;
    }

    public static boolean haveControlPermission(UserPermission userPermission, String moduleName, String menuName, String controlCaption) {
        boolean have = false;
        List<UserMenu> userMenuList;
        List<String> controlPermissionList;

        if (userPermission != null) {
            if (userPermission.getUserMenu() != null) {
                userMenuList = userPermission.getUserMenu().get(DmShared.xNull(moduleName));
                if (userMenuList != null) {
                    for (UserMenu userMenu : userMenuList) {
                        if (userMenu != null) {
                            if (DmShared.xNull(userMenu.getMenuName()).trim().equalsIgnoreCase(DmShared.xNull(menuName).trim())) {
                                controlPermissionList = userMenu.getControlPermission();
                                if (controlPermissionList != null) {
                                    if (controlPermissionList.contains(controlCaption)) {
                                        have = true;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        return have;
    }

    public static native double compute(String formula)/*-{
        return eval(formula);
    }-*/;

    public static String getNumberFormat(int decimalPlaces) {
        String returnValue = "0", numberFormat = "";

        if (decimalPlaces > 0) {
            for (int i = 0; i < decimalPlaces; i++) {
                numberFormat += "0";
            }
            returnValue += "." + numberFormat;
        }
        return returnValue;
    }

}
