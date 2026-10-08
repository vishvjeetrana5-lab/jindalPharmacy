package dataman.dmBase.dmBaseStatic.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RootPanel;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrinter_Mdl;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DmBaseClient {

    protected static HashMap<String, Object> store;
    protected static RootPanel root = null;
    protected static HashMap<Integer, List<DmRootEvents>> rootEvents = null;
    protected static HashMap<String, String> voucherPrintSetup = null;
    protected static HashMap<String, String> voucherPrintSetup_PageSize = null;
    protected static LinkedHashMap<String, List<UserMenu>> userPermission = new LinkedHashMap();
    protected static List<VoucherPermission> voucherPermission = new ArrayList<>();
    protected static DmRootEventsListener rootEventsListener = null;
    protected static List<DmLocalSettingPrintMap_Mdl> localSettingPrintMaps = new ArrayList<>();
    protected static List<DmLocalSettingPrinter_Mdl> localSettingPrinters = new ArrayList<>();
    protected static String regionalLanguage = null;
    protected static String loginUserName = null;
    protected static String loginDate = null;
    protected static String localeCountry = null;
    protected static String voucherPrefix = null;
    protected static String startDate = null;
    protected static String endDate = null;
    protected static boolean viewReportInNewWindow = false;
    protected static int pacWebPort = -1;
    protected static DmEnum.ScreenDisplay screenDisplay = DmEnum.ScreenDisplay.DESKTOP;

    public interface IDirectPrintingCallback {

        public void onSuccess(String reportUrl, String printerName);

        public void onFailure(Exception ex);
    }

    public static FlowPanel createProgressBar() {
        FlowPanel flwMain;
        flwMain = new FlowPanel();
        flwMain.getElement().setAttribute("style", "width:100px; font-size:25px;");

        InlineLabel lblProgressbar = new InlineLabel();
        lblProgressbar.getElement().setAttribute("style", "width:100px;text-align:center;color:rgb(240, 98, 146); ");
        //lblProgressbar.setStyleName("fa fa-spinner fa-pulse fa-3x fa-fw");
        lblProgressbar.setStyleName("fa fa-loader ");

        InlineLabel lblmsg = new InlineLabel("Please wait ..");
        lblmsg.getElement().setAttribute("style", "width:100px;text-align:center;font-size:12px;margin-left: 11px;font-weight: bold;color:rgb(59, 153, 217);");

        flwMain.add(lblProgressbar);

        return flwMain;
    }

    public static <T> T getStoreItem(String strKey) {
        return (T) store.get(strKey.trim().toLowerCase());
    }

    public static <T> void setStoreItem(String strKey, T tObject) {
        if (store.containsKey(strKey.trim().toLowerCase())) {
            store.remove(strKey.trim().toLowerCase());
        }
        store.put(strKey.trim().toLowerCase(), tObject);
    }

    public static void initializeVariables(boolean callPageUnload) {
        if (store != null) {
            store.clear();
            store = null;
        }
        store = new HashMap<String, Object>();

        if (rootEvents != null) {
            rootEvents.clear();
            rootEvents = null;
        }
        rootEvents = new HashMap<>();

        root = RootPanel.get();
        rootEventsListener = new DmRootEventsListener(callPageUnload);
        Event.addNativePreviewHandler(rootEventsListener);

        if (userPermission != null) {
            userPermission.clear();
            userPermission = new LinkedHashMap();
        }

        if (voucherPermission != null) {
            voucherPermission.clear();
            voucherPermission = new ArrayList<>();
        }

        voucherPrintSetup = new HashMap<>();
        voucherPrintSetup_PageSize = new HashMap<>();

        localSettingPrintMaps = new ArrayList<>();

        localSettingPrinters = new ArrayList<>();

        screenDisplay = DmEnum.ScreenDisplay.DESKTOP;
    }

    public static void initializeVariables(boolean allVariable, boolean callPageUnload) {
        initializeVariables(callPageUnload);

        if (allVariable) {
            regionalLanguage = null;
            loginUserName = null;
            loginDate = null;
            localeCountry = null;
            voucherPrefix = null;
            startDate = null;
            endDate = null;
            viewReportInNewWindow = false;
            DmRequestBuilder.flushSessionId();
        }
    }

    public static HashMap<String, String> getVoucherPrintSetup() {
        return voucherPrintSetup;
    }

    public static HashMap<String, String> getVoucherPrintSetup_PaperSize() {
        return voucherPrintSetup_PageSize;
    }

    public static boolean isViewReportInNewWindow() {
        return viewReportInNewWindow;
    }

    public static void setViewReportInNewWindow(boolean viewReportInNewWindow) {
        DmBaseClient.viewReportInNewWindow = viewReportInNewWindow;
    }

    public static HashMap<Integer, List<DmRootEvents>> getRootKeyEvents() {
        return rootEvents;
    }

    public static void printReportToPrinterDirectly(DmReportProperty_Mdl reportProperty, final String reportUrl, final IDirectPrintingCallback callBack) {
        try {
            if (reportProperty.isPrintToPrinter()) {
                DmLocalSettingPrinter_Mdl printerModel = new DmLocalSettingPrinter_Mdl();
                printerModel.setPrinterCode(reportProperty.getPrinterCode());
                if (localSettingPrinters.contains(printerModel)) {
                    printerModel = localSettingPrinters.get(localSettingPrinters.indexOf(printerModel));
                    final String printerName = printerModel.getPrinterName();

                    Map<String, String> urlParams = new HashMap<>();

                    urlParams.put("cmd", "print");
                    urlParams.put("reportUrl", reportUrl);
                    urlParams.put("printerName", printerName);
                    urlParams.put("copies", String.valueOf(reportProperty.getNoOfPrint()));
                    urlParams.put("printerType", String.valueOf(printerModel.getPrinterImageType().toInt()));

                    String printRequestUrl = createPrintRequestUrl("printCommands", urlParams);
                    RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, printRequestUrl);
                    requestBuilder.setCallback(new RequestCallback() {
                        @Override
                        public void onResponseReceived(Request request, Response response) {
                            if (response.getStatusCode() == 200) {
                                if (callBack != null) {
                                    callBack.onSuccess(reportUrl, printerName);
                                }
                            }
                        }

                        @Override
                        public void onError(Request request, Throwable exception) {
                            if (callBack != null) {
                                callBack.onFailure(new Exception(exception.getMessage()));
                            }
                        }
                    });

                    requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
                    requestBuilder.send();

                }
            }
        } catch (Exception ex) {
        }
    }
 public static void printReportToPrinterDirectlyNew(final String sharedPrinterName, DmReportProperty_Mdl reportProperty, final String reportUrl, final IDirectPrintingCallback callBack) {
        try {
            if (reportProperty.isPrintToPrinter()) {

                Map<String, String> urlParams = new HashMap<>();

                urlParams.put("cmd", "print");
                urlParams.put("reportUrl", reportUrl);
//                    urlParams.put("printerName", "\\\\CPU-282\\HP LaserJet Tank MFP 1005");
                urlParams.put("printerName", sharedPrinterName);
                urlParams.put("copies", "1");
                urlParams.put("printerType", "0");

                String printRequestUrl = createPrintRequestUrl("printCommands", urlParams);

                RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, printRequestUrl);
                requestBuilder.setCallback(new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        if (response.getStatusCode() == 200) {
                            if (callBack != null) {

                                callBack.onSuccess(reportUrl, sharedPrinterName);
                            }
                        }
                    }

                    @Override
                    public void onError(Request request, Throwable exception) {
                        if (callBack != null) {
                            callBack.onFailure(new Exception(exception.getMessage()));
                        }
                    }
                });

                requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
                requestBuilder.send();

            }
        } catch (Exception ex) {
        }
    }
    public static String createRequestUrl(String host, String protocol, int port, String relativeUrlPath, Map<String, String> urlParams) {
        String urlForPrintRequest = "";

        UrlBuilder urlBuilder = new UrlBuilder();
        urlBuilder.setHost(host);
        urlBuilder.setProtocol(protocol);
        urlBuilder.setPort(port);

        if (!DmBaseShared.xNull(relativeUrlPath).trim().equalsIgnoreCase("")) {
            urlBuilder.setPath(relativeUrlPath);
            if (urlParams != null) {
                Iterator<String> iterator = urlParams.keySet().iterator();
                while (iterator.hasNext()) {
                    String urlKey = DmBaseShared.xNull(iterator.next());
                    String urlValue = DmBaseShared.xNull(urlParams.get(urlKey));
                    urlBuilder.setParameter(urlKey, urlValue);
                }
            }
            urlForPrintRequest = urlBuilder.buildString().replace("///", "//");
        }
        return urlForPrintRequest;
    }

    public static String createPrintRequestUrl(String relativeUrlPath, Map<String, String> urlParams) {
        return createRequestUrl("localhost", "http", pacWebPort, relativeUrlPath, urlParams);
    }

    public static boolean regExMatches(String string, String javaScriptRegEx) {
        return RegExp.compile(javaScriptRegEx).test(string);
    }

    public static DmEnum.ScreenDisplay getScreenDisplay() {
        return DmBaseClient.screenDisplay;
    }

    public static void setScreenDisplay(int clientWidth) {
        if (clientWidth < 768) {
            DmBaseClient.screenDisplay = DmEnum.ScreenDisplay.MOBILE;
        } else if (clientWidth < 992) {
            DmBaseClient.screenDisplay = DmEnum.ScreenDisplay.TABLET;
        } else {
            DmBaseClient.screenDisplay = DmEnum.ScreenDisplay.DESKTOP;
        }
    }
}
