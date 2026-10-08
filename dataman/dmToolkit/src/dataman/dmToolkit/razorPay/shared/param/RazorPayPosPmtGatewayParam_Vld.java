package dataman.dmToolkit.razorPay.shared.param;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.util.List;

public class RazorPayPosPmtGatewayParam_Vld {

    public static void validateParamData(RazorPayPosPmtGatewayParam param) throws Exception {
        if (DmBaseShared.xNull(param.getAppKey()).trim().equals("")) {
            throw new Exception("App_Key is mandatory.");
        }
        if (DmBaseShared.xNull(param.getAppKey()).trim().length() > 50) {
            throw new Exception("App_Key length can not be greater than 50.");
        }
        if (DmBaseShared.xNull(param.getUsername()).trim().equals("")) {
            throw new Exception("User_Name is mandatory.");
        }
        if (DmBaseShared.xNull(param.getUsername()).trim().length() > 20) {
            throw new Exception("User_Name length can not be greater than 20.");
        }
        String mode = DmBaseShared.xNull(param.getMode()).trim();
        if (mode.equals("")) {
            throw new Exception("Mode is mandatory.");
        }
        if (!(mode.equalsIgnoreCase("ALL") || mode.equalsIgnoreCase("CARD") || mode.equalsIgnoreCase("WALLET")
                || mode.equalsIgnoreCase("CASH") || mode.equalsIgnoreCase("UPI"))) {
            throw new Exception("Invalid mode.");
        }
        if (DmBaseShared.xNull(param.getUrlInitiate()).trim().equals("")) {
            throw new Exception("URL Initiate is mandatory.");
        }
        if (DmBaseShared.xNull(param.getUrlStatus()).trim().equals("")) {
            throw new Exception("URL Status is mandatory.");
        }
        if (DmBaseShared.xNull(param.getUrlCancel()).trim().equals("")) {
            throw new Exception("URL Cancel is mandatory.");
        }

//        String defaultDeviceId = DmBaseShared.xNull(param.getDefaultDeviceId()).trim();
//        List<RazorPayPosPGDeviceId_Mdl> availableDeviceIds = param.getAvailableDeviceIds();
//        if (availableDeviceIds == null || availableDeviceIds.isEmpty()) {
//            throw new Exception("Available device list is mandatory.");
//        }
//        boolean isDefaultIdFound = false;
//        for (RazorPayPosPGDeviceId_Mdl deviceIdMdl : availableDeviceIds) {
//            if (defaultDeviceId.equalsIgnoreCase(DmBaseShared.xNull(deviceIdMdl.getDeviceId()).trim())) {
//                isDefaultIdFound = true;
//                break;
//            }
//        }
        if (DmBaseShared.xNull(param.getDefaultDeviceId()).trim().equalsIgnoreCase("")) {
            throw new Exception("Default Device Id must be in available devices.");
        }
    }
}
