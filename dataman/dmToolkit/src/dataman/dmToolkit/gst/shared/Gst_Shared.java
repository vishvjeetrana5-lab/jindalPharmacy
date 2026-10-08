package dataman.dmToolkit.gst.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

/**
 *
 * @author Lenovo
 */
public class Gst_Shared {

    public static String concatPartyInfo(String gstin, String name, String add1, String add2, String cityName, String pincode, String stateName, String countryName) {
        return DmShared.removeSpace(DmShared.xNull(gstin).trim()
                + DmShared.xNull(name).trim()
                + DmShared.xNull(add1).trim()
                + DmShared.xNull(add2).trim()
                + DmShared.xNull(cityName).trim()
                + DmShared.xNull(pincode).trim()
                + DmShared.xNull(stateName).trim()
                + DmShared.xNull(countryName).trim());
    }

    public static String concatPartyInfoForView(String header, String gstin, String name, String add1, String add2, String cityName, String pincode, String stateName, String countryName) {
        return DmShared.removeSpace("<b style=\"color:rgb(21, 66, 139);\">" + DmShared.xNull(header).trim() + "</b> <b>( " + DmShared.xNull(gstin).trim() + " ) : </b><br>"
                +"<strong><i><u>"+ DmShared.xNull(name).trim() + "</strong></i></u> <br>"
                + DmShared.xNull(add1).trim() + ", " + DmShared.xNull(add2).trim() + "<br>"
                + DmShared.xNull(cityName).trim()
                + "-" + DmShared.xNull(pincode).trim()
                + ", " + DmShared.xNull(stateName).trim()
                + ", " + DmShared.xNull(countryName).trim()
                + "<br>");
    }

}
