package dataman.dmPlugin.localSettings;

import com.google.gson.reflect.TypeToken;
import dataman.dmPlugin.server.NanoHTTPD;
import dataman.dmPlugin.server.router.RouterNanoHTTPD;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmPlugin.CFunctions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LocalSettings extends RouterNanoHTTPD.DefaultHandler {

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Response response = null;
        if (session.getParms().containsKey("cmd")) {

            if (session.getParms().get("cmd").equalsIgnoreCase("isFileExists")) {
                String settingsExistence = "false";
                try {
                    settingsExistence = String.valueOf(CFunctions.isSettingsFileExists());
                } catch (Exception ex) {
                }
                response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), settingsExistence);
            } else if (session.getParms().get("cmd").equalsIgnoreCase("getLocalSettings")) {
                String settingsJsonString = "";
                try {
                    settingsJsonString = CFunctions.getSavedSettings();
                } catch (Exception ex) {
                }
                response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), settingsJsonString);
            } else if (session.getParms().get("cmd").equalsIgnoreCase("savePrinterSettings")) {
                response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), String.valueOf(saveLocalSettingsOfPrinter(session.getParms())));
            } else if (session.getParms().get("cmd").equalsIgnoreCase("getPrinterInfo")) {
                response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), new PrinterInfo().getPrinterInfo());
            } else if (session.getParms().get("cmd").equalsIgnoreCase("assignReportMap")) {
                response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), assignReportMapToPrinter(session.getParms()));
            } else if (session.getParms().get("cmd").equalsIgnoreCase("getReportMapsUsingPrinterCode")) {
                response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), String.valueOf(assignReportMapToPrinter(session.getParms())));
            }

            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        return response;
    }

    private String assignReportMapToPrinter(Map<String, String> urlParams) {
        String responseJSonString = "";
        try {
            String jsonString = urlParams.get("data");
            DmLocalSettingPrintMap_Mdl assignmentModel = CFunctions.getObjectFromJson(jsonString, new TypeToken<DmLocalSettingPrintMap_Mdl>() {
            });
            responseJSonString = CFunctions.assignPrinterToReportMap(assignmentModel, assignmentModel.getPrinterCode(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseJSonString;
    }
    
    private boolean saveLocalSettingsOfPrinter(Map<String, String> urlParams) {
        boolean bool = false;
        try {
            String jsonString = urlParams.get("data");
            Map<String, String> additionalParams = new HashMap<>();
            Iterator<String> iterator = urlParams.keySet().iterator();
            while (iterator.hasNext()) {
                String urlKey = iterator.next();
                if ((urlKey != null) && (!urlKey.equals("")) && (!urlKey.equalsIgnoreCase("data"))) {
                    additionalParams.put(urlKey, urlParams.get(urlKey));
                }
            }
            CFunctions.savePrinterSettings(jsonString, additionalParams);
            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    @Override
    public String getText() {
        return "FILE_QUERY";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

}
