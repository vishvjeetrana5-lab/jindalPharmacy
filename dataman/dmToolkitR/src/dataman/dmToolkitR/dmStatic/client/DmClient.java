package dataman.dmToolkitR.dmStatic.client;

import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.DmDateTimeFormat;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.VoucherPermission;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.IWebCamera;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import dataman.dmBase.dmBaseStatic.client.DmBaseClient;
import static dataman.dmBase.dmBaseStatic.client.DmBaseClient.createPrintRequestUrl;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import dataman.dmToolkitR.printerMapping.client.PrinterMapping;

public class DmClient extends DmBaseClient {

    public interface IReportMapJSonInternalCallback {

        public void onJSonOfLocalSettingReportMap(String jsonOfLocalSettingReportMap);
    }

    public interface IReportMapAssignmentPrinterCodeUICallback {

        public void getJSonOfLocalSettingReportMap(DmLocalSettingPrintMap_Mdl reportMapModel, IReportMapJSonInternalCallback reportMapInternalCallback);

        public void setLocalSettings(String jsonOfJVMLocalSettings);
    }

    private static IWebCamera webCamera = null;
    static int intCurrMonth = 0;

    public static List<DmLocalSettingPrintMap_Mdl> getLocalSettingPrintMap() {
        return localSettingPrintMaps;
    }

      public static void setPACWebPort(int webPort) {
        pacWebPort = webPort;
    }

    public static int getPACWebPort() {
        return pacWebPort;
    }
    
    public static IWebCamera getWebCamera() {
        return webCamera;
    }

    public static void setWebCamera(IWebCamera webCamera) {
        DmClient.webCamera = webCamera;
    }

    public static String[] getModulePermission() {
        return userPermission.keySet().toArray(new String[userPermission.size()]);
    }

    public static List<UserMenu> getUserPermission(String moduleName) {
        List<UserMenu> menuList;

        menuList = userPermission.get(DmShared.xNull(moduleName).toLowerCase());

        if (menuList == null) {
            menuList = new ArrayList<>();
        }

        return menuList;
    }

    public static HashMap<String, List<UserMenu>> getControlPermission() {
        HashMap<String, List<UserMenu>> controlPermission = new HashMap<String, List<UserMenu>>();
        String[] menuKeys = userPermission.keySet().toArray(new String[userPermission.size()]);
        List<UserMenu> menuList;

        for (int i = 0; i < menuKeys.length; i++) {
            menuList = userPermission.get(menuKeys[i]);
            if (menuList != null) {
                controlPermission.put(menuKeys[i], new ArrayList<UserMenu>());
//                controlPermission = getControlPermission(menuKeys[i], menuList, controlPermission);
            }
        }
        return controlPermission;
    }

    public static void setUserPermission(LinkedHashMap<String, List<UserMenu>> userPermission) {
        DmClient.userPermission = userPermission;
    }

    public static void setVoucherPermission(List<VoucherPermission> voucherPermission) {
        DmClient.voucherPermission = voucherPermission;
    }

    public static List<VoucherPermission> getVoucherPermission() {
        return DmClient.voucherPermission;
    }

    public static List<VoucherPermission> getVoucherPermission(String[] voucherCategory) {
        List<VoucherPermission> vp = new ArrayList<>();

        if (voucherCategory != null) {
            if (DmClient.voucherPermission != null) {
                for (int i = 0; i < DmClient.voucherPermission.size(); i++) {
                    for (int j = 0; j < voucherCategory.length; j++) {
                        if (DmShared.xNull(DmClient.voucherPermission.get(i).getCategory()).equalsIgnoreCase(DmShared.xNull(voucherCategory[j]))) {
                            vp.add(DmClient.voucherPermission.get(i));
                        }
                    }
                }
            }
        }

        return vp;
    }

    public static RootPanel getRoot() {
        return root;
    }

    public static String getRegionalLanguage() {
        return regionalLanguage;
    }

    public static void setRegionalLanguage(String regionalLanguage) {
        DmClient.regionalLanguage = regionalLanguage;
    }

    public static String getLoginUserName() {
        return loginUserName;
    }

    public static void setLoginUserName(String value) {
        loginUserName = value;
    }

    public static String getLoginDate() {
        return loginDate;
    }

    public static void clearLocalSettingPrintReportMap() {
        DmBaseClient.localSettingPrintMaps.clear();
    }

    public static void clearLocalSettingPrinters() {
        DmBaseClient.localSettingPrinters.clear();
    }

    public static void refreshLocalSettingPrintReportMap(List<DmLocalSettingPrintMap_Mdl> listReportMaps) {
        clearLocalSettingPrintReportMap();
        DmBaseClient.localSettingPrintMaps.addAll(listReportMaps);
    }

    public static void refreshLocalSettingPrinters(List<DmLocalSettingPrinter_Mdl> listPrinters) {
        clearLocalSettingPrinters();
        DmBaseClient.localSettingPrinters.addAll(listPrinters);
    }

    public static void setLoginDate(String value) {
        loginDate = value;
    }

    public static String getStartDate() {
        return startDate;
    }

    public static void setStartDate(String value) {
        startDate = value;
    }

    public static String getEndDate() {
        return endDate;
    }

    public static void setEndDate(String value) {
        endDate = value;
    }

    public static String getVoucherPrefix() {
        return voucherPrefix;
    }

    public static void setVoucherPrefix(String value) {
        voucherPrefix = DmShared.xNull(value).trim();
    }

    public static void addRootKeyEvent(DmRootEvents invoker, int eventType) {
        if (rootEvents.containsKey(eventType) == false) {
            rootEvents.put(eventType, new ArrayList<DmRootEvents>());
            root.sinkEvents(eventType);
        }
        if (rootEvents.get(eventType).contains(invoker) == true) {
            rootEvents.get(eventType).remove(invoker);
        }
        rootEvents.get(eventType).add(invoker);
    }

    public static void removeRootKeyEvent(DmRootEvents invoker, int eventType) {
        if (rootEvents.containsKey(eventType) == true) {
            if (rootEvents.get(eventType).contains(invoker) == true) {
                rootEvents.get(eventType).remove(invoker);
            }
        }
    }

    public static Style setStyle(Widget wgtObject, String strWidth, String strHeight, String strfloat, String strPosition, DmPosition dmPosition, boolean blnNoMargin) {
        Style stlTmp = wgtObject.getElement().getStyle();

        if (!DmShared.xNull(strWidth).equalsIgnoreCase("")) {
            wgtObject.setWidth(strWidth);
        }

        if (!DmShared.xNull(strHeight).equalsIgnoreCase("")) {
            wgtObject.setHeight(strHeight);
        }

        if (strfloat != null) {
            if (strfloat.trim().equalsIgnoreCase(Style.Float.LEFT.toString())) {
                stlTmp.setFloat(Style.Float.LEFT);
            } else if (strfloat.trim().equalsIgnoreCase(Style.Float.RIGHT.toString())) {
                stlTmp.setFloat(Style.Float.RIGHT);
            } else if (strfloat.trim().equalsIgnoreCase(Style.Float.NONE.toString())) {
                stlTmp.setFloat(Style.Float.NONE);
            }
        }

        if (dmPosition == DmPosition.CENTER) {
//            strPosition = "relative";
//            stlTmp.setLeft(50, Style.Unit.PCT);
//            stlTmp.setTop(50, Style.Unit.PCT);
//            stlTmp.setProperty("transform", "translate(-50%,-50%)");
            wgtObject.addStyleName("dm-FlwCenter");
        } else if (dmPosition == DmPosition.CENTER_HORIZONTAL) {
//            strPosition = "relative";
//            stlTmp.setLeft(50, Style.Unit.PCT);
//            stlTmp.setProperty("transform", "translateX(-50%)");
            wgtObject.addStyleName("dm-FlwCenter-Horizontal");
        } else if (dmPosition == DmPosition.CENTER_VERTICAL) {
//            strPosition = "relative";
//            stlTmp.setTop(50, Style.Unit.PCT);
//            stlTmp.setProperty("transform", "translateY(-50%)");
            wgtObject.addStyleName("dm-FlwCenter-Vertial");
        }

        if (strPosition != null) {
            if (strPosition.trim().equalsIgnoreCase(Style.Position.STATIC.toString())) {
                stlTmp.setPosition(Style.Position.STATIC);
            } else if (strPosition.trim().equalsIgnoreCase(Style.Position.ABSOLUTE.toString())) {
                stlTmp.setPosition(Style.Position.ABSOLUTE);
            } else if (strPosition.trim().equalsIgnoreCase(Style.Position.FIXED.toString())) {
                stlTmp.setPosition(Style.Position.FIXED);
            } else if (strPosition.trim().equalsIgnoreCase(Style.Position.RELATIVE.toString())) {
                stlTmp.setPosition(Style.Position.RELATIVE);
            }
        }

        if (!blnNoMargin) {
            stlTmp.setMarginBottom(1, Style.Unit.PX);
        }

        return stlTmp;
    }

    public static String returnDate(String value) {
        String returnDate = loginDate;
        String tempDate = "";
        tempDate = value;
        int mDay = 0;
        String mMonth = "";
        int mYear = 0;
        String Txt1 = "";
        int Test = 0;

        Txt1 = value.trim();

        try {
            if (value.equals("")) {
                return "";
            }

            try {
                String val = value.substring(0, 2);
                int Day = Integer.parseInt(val);
                if (Day > 0) {
                    if (Day > 31) {
                        return loginDate;
                    }
                }
            } catch (Exception ex) {
                //return "";
            }

            int len = value.length();
            if (value.length() >= 11) {
                if (value.indexOf("/") != -1) {
                    if (value.lastIndexOf("/") != -1) {
                        if (value.indexOf("/") != value.lastIndexOf("/")) {
//                            Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(value);
//                            strDate = DmDateTimeFormat.getFormat("dd/MMM/yyyy").format(date);
                            returnDate = DmShared.dNull(value);
                            return returnDate;
                        }
                    }
                }
            }

            //////////////////////For Day/////////////////////////////
            Test = Txt1.indexOf("/", 0);
            if ((Test == -1)) {
                Test = Txt1.indexOf("-", 0);
            }
            if ((Test == -1)) {
                Test = Txt1.indexOf(".", 0);
            }

            if (Test != -1) {
                if (DmShared.isNumeric((Txt1.substring(0, Test)))) {
                    String strDay = "00".substring((Txt1.substring(0, Test)).length()) + Txt1.substring(0, Test);
                    mDay = Integer.parseInt(strDay);
                } else {
                    mMonth = Txt1.substring(0, Test);
                }
            }

            if (Test == -1) {
                if (DmShared.isNumeric(Txt1)) {
                    mDay = Integer.parseInt(Txt1);
                } else {
                    mMonth = Txt1;
                }
                returnDate = exitFLAG(mDay, mMonth, mYear, tempDate);
                int daysInMonth = getNoofDayFromMonth(intCurrMonth);
                if (mDay > daysInMonth) {
                    return loginDate;
                }
                if (returnDate != "") {
                    return returnDate;
                }

            }

            ////////////For Month/////////////////////////////
            if (mMonth == "") {
                Txt1 = Txt1.substring(Test + 1);
                Test = (Txt1.indexOf("/", 0));

                if ((Test == -1)) {
                    Test = (Txt1.indexOf("-", 0));
                }

                if ((Test == -1)) {
                    Test = (Txt1.indexOf(".", 0));
                }

                if ((Test != -1)) {
                    mMonth = Txt1.substring(0, (Test));
                }

                if ((Test == -1)) {
                    mMonth = Txt1;
                    returnDate = exitFLAG(mDay, mMonth, mYear, tempDate);
                    int daysInMonth = getNoofDayFromMonth(intCurrMonth);
                    if (mDay > daysInMonth) {
                        return loginDate;
                    }
                    if (returnDate != "") {
                        return returnDate;
                    }
                }
            }

            ////////////For Year/////////////////////////////
            Txt1 = Txt1.substring(Test + 1);
            if (DmShared.isNumeric(Txt1)) {
                if (Txt1.length() < 4) {
//                    Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                    Date date = DmShared.convertDate(loginDate);
                    String strYear = DmDateTimeFormat.getFormat("yyyy").format(date).substring(0, 4 - Txt1.length()) + Txt1.trim();
                    mYear = Integer.parseInt(strYear);
                } else if (Txt1.length() > 4) {
//                    Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                    Date date = DmShared.convertDate(loginDate);
                    String strYear = DmDateTimeFormat.getFormat("yyyy").format(date);
                    mYear = Integer.parseInt(strYear);
                } else {
                    mYear = Integer.parseInt(Txt1);
                }
            } else {
//                Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                Date date = DmShared.convertDate(loginDate);
                String strYear = DmDateTimeFormat.getFormat("yyyy").format(date);
                mYear = Integer.parseInt(strYear);
            }

            String strDay = "00".substring(Integer.toString(mDay).length()) + Integer.toString(mDay);

            returnDate = strDay + "-" + mMonth.trim() + "-" + Integer.toString(mYear);
//            Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(strDate);
//            strDate = DmDateTimeFormat.getFormat("dd/MMM/yyyy").format(date);
            returnDate = DmShared.dNull(returnDate);

        } catch (Exception ex) {
        }

        return returnDate;
    }

    private static int getNoofDayFromMonth(int month) {
        int totalDays = 0;
        if (month == 1) {
            totalDays = 31;
        } else if (month == 2) {
            totalDays = 28;
        } else if (month == 3) {
            totalDays = 31;
        } else if (month == 4) {
            totalDays = 30;
        } else if (month == 5) {
            totalDays = 31;
        } else if (month == 6) {
            totalDays = 30;
        } else if (month == 7) {
            totalDays = 31;
        } else if (month == 8) {
            totalDays = 31;
        } else if (month == 9) {
            totalDays = 30;
        } else if (month == 10) {
            totalDays = 31;
        } else if (month == 11) {
            totalDays = 30;
        } else if (month == 12) {
            totalDays = 31;
        }
        return totalDays;
    }

    private static String exitFLAG(int day, String month, int year, String value) {
        String rtnDateValue = loginDate;
        String[] strTemp;
        try {
            if (year == 0) {
                Date date = DmShared.convertDate(loginDate);
                String strYear = DmDateTimeFormat.getFormat("yyyy").format(date);
                year = Integer.parseInt(strYear);
            }

            strTemp = value.split("/");
            if (strTemp.length != 3) {
                strTemp = value.split(".");
            }
            if (strTemp.length != 3) {
                strTemp = value.split("-");
            }

            if (strTemp.length == 3) {
                if (strTemp[2].length() == 4) {
                    year = Integer.parseInt(strTemp[2]);
                } else {

//                    Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                    Date date = DmShared.convertDate(loginDate);
                    String strYear = DmDateTimeFormat.getFormat("yyyy").format(date).substring(0, 4 - Integer.toString(year).trim().length()) + Integer.toString(year).trim();
                    year = Integer.parseInt(strYear);
                }
            } else {
//                Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                Date date = DmShared.convertDate(loginDate);
                String strYear = DmDateTimeFormat.getFormat("yyyy").format(date).substring(0, 4 - Integer.toString(year).trim().length()) + Integer.toString(year).trim();
                year = Integer.parseInt(strYear);
            }

            if (day < 0) {
                day = 0;
            }

            if (month.trim().equals("")) {
//                Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                Date date = DmShared.convertDate(loginDate);
                month = DmDateTimeFormat.getFormat("MMM").format(date);
                intCurrMonth = Integer.valueOf(DmDateTimeFormat.getFormat("MM").format(date));
            } else {
                if (month.trim().length() > 2) {
                    month = month.substring(0, 3);
                } else if (month.trim().length() <= 2) {
                    month = month.substring(0, month.trim().length());
                }
                switch (month.toUpperCase().trim()) {
                    case "1":
                    case "01":
                    case "J":
                    case "JA":
                    case "JAN":
                        month = "Jan";
                        intCurrMonth = 1;
                        break;
                    case "2":
                    case "02":
                    case "F":
                    case "FE":
                    case "FEB":
                        month = "Feb";
                        intCurrMonth = 2;
                        break;
                    case "3":
                    case "03":
                    case "M":
                    case "MA":
                    case "MAR":
                        month = "Mar";
                        intCurrMonth = 3;
                        break;
                    case "4":
                    case "04":
                    case "A":
                    case "AP":
                    case "APR":
                        month = "Apr";
                        intCurrMonth = 4;
                        break;
                    case "5":
                    case "05":
                    case "MAY":
                        month = "May";
                        intCurrMonth = 5;
                        break;
                    case "6":
                    case "06":
                    case "JU":
                    case "JUN":
                        month = "Jun";
                        intCurrMonth = 6;
                        break;
                    case "7":
                    case "07":
                    case "JUL":
                        month = "Jul";
                        intCurrMonth = 7;
                        break;
                    case "8":
                    case "08":
                    case "AU":
                    case "AUG":
                        month = "Aug";
                        intCurrMonth = 8;
                        break;
                    case "9":
                    case "09":
                    case "S":
                    case "SE":
                    case "SEP":
                        month = "Sep";
                        intCurrMonth = 9;
                        break;
                    case "10":
                    case "O":
                    case "OC":
                    case "OCT":
                        month = "Oct";
                        intCurrMonth = 10;
                        break;
                    case "11":
                    case "N":
                    case "NO":
                    case "NOV":
                        month = "Nov";
                        intCurrMonth = 11;
                        break;
                    case "12":
                    case "D":
                    case "DE":
                    case "DEC":
                        month = "Dec";
                        intCurrMonth = 12;
                        break;
                    default:
//                        Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                        Date date = DmShared.convertDate(loginDate);
                        String strMonth = DmDateTimeFormat.getFormat("MMM").format(date);
                        month = strMonth;
                        intCurrMonth = Integer.valueOf(DmDateTimeFormat.getFormat("MM").format(date));
                        break;
                }

                switch (month.toUpperCase().trim()) {
                    case "1":
                    case "01":
                    case "J":
                    case "JA":
                    case "JAN":
                        if ((day > 31)) {
                            day = 0;
                        }

                        break;
                    case "2":
                    case "02":
                    case "F":
                    case "FE":
                    case "FEB":
                        if ((day > (((year % 4)
                                == 0) ? 29 : 28))) {
                            day = 0;
                        }

                        break;
                    case "3":
                    case "03":
                    case "M":
                    case "MA":
                    case "MAR":
                        if ((day > 31)) {
                            day = 0;
                        }

                        break;
                    case "4":
                    case "04":
                    case "A":
                    case "AP":
                    case "APR":
                        if ((day > 30)) {
                            day = 0;
                        }

                        break;
                    case "5":
                    case "05":
                    case "MAY":
                        if ((day > 31)) {
                            day = 0;
                        }

                        break;
                    case "6":
                    case "06":
                    case "JU":
                    case "JUN":
                        if ((day > 30)) {
                            day = 0;
                        }

                        break;
                    case "7":
                    case "07":
                    case "JUL":
                        if ((day > 31)) {
                            day = 0;
                        }

                        break;
                    case "8":
                    case "08":
                    case "AU":
                    case "AUG":
                        if ((day > 31)) {
                            day = 0;
                        }

                        break;
                    case "9":
                    case "09":
                    case "S":
                    case "SE":
                    case "SEP":
                        if ((day > 30)) {
                            day = 0;
                        }

                        break;
                    case "10":
                    case "O":
                    case "OC":
                    case "OCT":
                        if ((day > 31)) {
                            day = 0;
                        }

                        break;
                    case "11":
                    case "N":
                    case "NO":
                    case "NOV":
                        if ((day > 30)) {
                            day = 0;
                        }

                        break;
                    case "12":
                    case "D":
                    case "DE":
                    case "DEC":
                        if ((day > 31)) {
                            day = 0;
                        }

                        break;
                    default:
                        day = 0;
                        break;
                }
            }
            if (day == 0) {
//                Date date = DmDateTimeFormat.getFormat("dd/MM/yyyy").parse(loginDate);
                Date date = DmShared.convertDate(loginDate);
                String strDate = DmDateTimeFormat.getFormat("dd").format(date);
                day = Integer.parseInt(strDate);

            }
            String strDay = ("00".substring(Integer.toString(day).length())) + Integer.toString(day);
            rtnDateValue = strDay + "-" + month.trim() + "-" + Integer.toString(year);

        } catch (Exception ex) {
        }
        return rtnDateValue;
    }

    public static String vNull(Object value, String pattern) {
        return NumberFormat.getFormat(pattern).format(DmShared.vNull(value));
    }

    public static double mntsToHrs(int value) {
        int hours;
        double minutes, rtnHours;

        hours = DmBaseShared.iNull(value / 60).intValue();
        minutes = DmBaseShared.vNull(vNull((value % 60), "0.00")).doubleValue() / 100;

        rtnHours = hours + minutes;

        return rtnHours;
    }

    public static int hrsToMnts(double value) {
        int minutes;

        minutes = DmBaseShared.iNull((60 * DmBaseShared.iNull(value).intValue()) + ((DmBaseShared.vNull(vNull(value - DmBaseShared.iNull(value).intValue(), "0.00"))) * 100)).intValue();

        return minutes;
    }

    public static UserMenu searchMenu(String moduleName, String menuText) {
        List<UserMenu> menuList = getUserPermission(moduleName);

        return searchMenu(menuText, menuList);
    }

    private static UserMenu searchMenu(String menuText, List<UserMenu> menuList) {
        UserMenu menu = null;

        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getMenuName().equalsIgnoreCase(menuText)) {
                menu = menuList.get(i);
                break;
            }
            if (menuList.get(i).getUserMenu().size() > 0) {
                menu = searchMenu(menuText, menuList.get(i).getUserMenu());
            }

            if (menu != null) {
                break;
            }
        }

        return menu;
    }

    public static DmBaseURL getHostPageBaseURL() {
        return getHostPageBaseURL("http");
    }

    public static DmBaseURL getHostPageBaseURL(String prefix) {
//        String url;
//        DmBaseURL baseURL = new DmBaseURL();
//        prefix = DmShared.xNull(prefix).trim().toLowerCase();
//        if (prefix.equals("")) {
//            prefix = "http";
//        }
//
//        url = GWT.getHostPageBaseURL();
//        if (url.trim().startsWith("http://")) {
//            baseURL.setProtocol(prefix);
//            baseURL.setUrl(url.replace("http://", ""));
//        } else if (url.trim().startsWith("https://")) {
//            baseURL.setProtocol(prefix + "s");
//            baseURL.setUrl(url.replace("https://", ""));
//        }
//
//        return baseURL;
        return DmShared.getHostPageBaseURL(prefix, GWT.getHostPageBaseURL());
    }

    public static void loadCss(String urlString) {
        LinkElement link = Document.get().createLinkElement();
        link.setRel("stylesheet");
        link.setHref(urlString);
        nativeAttachToHead(link);
    }

    protected static native void nativeAttachToHead(JavaScriptObject scriptElement) /*-{
        $doc.getElementsByTagName("head")[0].appendChild(scriptElement);
    }-*/;

    public static String concatURL(String classPath, HashMap<String, String> params) {
//        String url = "";
//        DmBaseURL baseURl;
//
//        try {
//            baseURl = getHostPageBaseURL();
//            UrlBuilder urlBuild = new UrlBuilder();
//            urlBuild.setProtocol(baseURl.getProtocol());
//            urlBuild.setPath(baseURl.getUrl() + classPath);
//
//            for (Map.Entry<String, String> param : params.entrySet()) {
//                urlBuild.setParameter(param.getKey(), param.getValue());
//            }
//            url = urlBuild.buildString().replace("///", "//");
//        } catch (Exception ex) {
//            url = "";
//        }
//
//        return url;

        return concatURL(getHostPageBaseURL(), classPath, params);
    }

    public static String concatURL(DmBaseURL baseURl, String classPath, HashMap<String, String> params) {
        String url = "";
//        DmBaseURL baseURl;

        try {
//            baseURl = getHostPageBaseURL();
            UrlBuilder urlBuild = new UrlBuilder();
            urlBuild.setProtocol(baseURl.getProtocol());
            urlBuild.setPath(baseURl.getUrl() + classPath);

            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    urlBuild.setParameter(param.getKey(), param.getValue());
                }
            }
            url = urlBuild.buildString().replace("///", "//");
        } catch (Exception ex) {
            url = "";
        }

        return url;
    }

    public static boolean isReportMapExists(String moduleName, String menuText, String reportName) {
        DmLocalSettingPrintMap_Mdl reportMap = new DmLocalSettingPrintMap_Mdl();
        reportMap.setModuleName(moduleName);
        reportMap.setMenuText(menuText);
        reportMap.setReportName(reportName);

        return DmBaseClient.localSettingPrintMaps.contains(reportMap);
    }

    public static boolean assignPrinterCodeToReportMap(DmReportProperty_Mdl reportProperty, String moduleName, String menuText, String reportName, String printerCode, boolean replaceOldMapping, final IReportMapAssignmentPrinterCodeUICallback uiCallback) {
        boolean bool = false;
        try {

            DmLocalSettingPrinter_Mdl printerModel = new DmLocalSettingPrinter_Mdl();
            printerModel.setPrinterCode(printerCode);

            if (!DmBaseClient.localSettingPrinters.contains(printerModel)) {
                throw new Exception("PrinterCode not found...");
            }

            DmLocalSettingPrintMap_Mdl reportMapModel = new DmLocalSettingPrintMap_Mdl();

            reportMapModel.setModuleName(moduleName);
            reportMapModel.setMenuText(menuText);
            reportMapModel.setReportName(reportName);
            reportMapModel.setPrinterCode(printerCode);

            if (replaceOldMapping) {
                replaceOldReportMapping(reportMapModel);
            }

            reportProperty.setPrinterCode(printerCode);

            sendReportMapJSonRequest(uiCallback, reportMapModel);

        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }

    private static void sendReportMapJSonRequest(final IReportMapAssignmentPrinterCodeUICallback uiCallback, final DmLocalSettingPrintMap_Mdl reportMapModel) {
        if (uiCallback != null) {
            uiCallback.getJSonOfLocalSettingReportMap(reportMapModel, new IReportMapJSonInternalCallback() {
                @Override
                public void onJSonOfLocalSettingReportMap(String jsonOfLocalSettingReportMap) {
                    saveReportMapPrinterCodeAssignment(jsonOfLocalSettingReportMap, uiCallback);
                }
            });
        }
    }

    private static void saveReportMapPrinterCodeAssignment(String jsonOfLocalSettingReportMap, final IReportMapAssignmentPrinterCodeUICallback uiCallback) {
        try {
            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("cmd", "assignReportMap");
            urlParams.put("data", jsonOfLocalSettingReportMap);
            String assignmentUrl = createPrintRequestUrl("localSettings", urlParams);

            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, assignmentUrl);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        String result = response.getText();
                        if (uiCallback != null) {
                            uiCallback.setLocalSettings(result);
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {

                    new DmAlertBox(exception.getMessage(), (DmTextBox) null).show();
                }
            });
            requestBuilder.send();
        } catch (Exception ex) {

        }
    }

    public static void replaceOldReportMapping(DmLocalSettingPrintMap_Mdl reportMapModel) {
        if (DmBaseClient.localSettingPrintMaps.contains(reportMapModel)) {
            DmBaseClient.localSettingPrintMaps.remove(reportMapModel);
        }

        DmBaseClient.localSettingPrintMaps.add(reportMapModel);
    }

    public static void localSettingPrintMap(DmReportProperty_Mdl reportProperty, String moduleName, String menuText, String reportName) {
        DmLocalSettingPrintMap_Mdl dmLocalSettingPrintMap = new DmLocalSettingPrintMap_Mdl();

        if (reportProperty == null) {
            reportProperty = new DmReportProperty_Mdl();
        }
        if (reportProperty.isPrintToPrinter()) {
            dmLocalSettingPrintMap.setModuleName(moduleName);
            dmLocalSettingPrintMap.setMenuText(menuText);
            dmLocalSettingPrintMap.setReportName(reportName);

            int index = getLocalSettingPrintMap().indexOf(dmLocalSettingPrintMap);

            if (index >= 0) {
                reportProperty.setPrinterCode(getLocalSettingPrintMap().get(index).getPrinterCode());
            } else {
                reportProperty.setPrinterCode("");
                PrinterMapping printerMappingDialog = new PrinterMapping(reportProperty, moduleName, menuText, reportName);
                printerMappingDialog.show();
            }
        }
    }

}
