package dataman.dmToolkit.dmStatic.client;

import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.DmDateTimeFormat;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermission;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.overHeadLine.client.OverHeadLine;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import dataman.dmBase.dmBaseStatic.client.DmBaseClient;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmToolkit.dmDateBox.client.DmDateBox;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmMenu.client.DmMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuBarItem.client.DmMenuBarItem;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.printerMapping.client.PrinterMapping;

public class DmClient extends DmBaseClient {

    public interface IReportMapJSonInternalCallback {

        public void onJSonOfLocalSettingReportMap(String jsonOfLocalSettingReportMap);
    }

    public interface IReportMapAssignmentPrinterCodeUICallback {

        public void getJSonOfLocalSettingReportMap(DmLocalSettingPrintMap_Mdl reportMapModel, IReportMapJSonInternalCallback reportMapInternalCallback);

        public void setLocalSettings(String jsonOfJVMLocalSettings);
    }

    static int intCurrMonth = 0;

    public static List<DmLocalSettingPrintMap_Mdl> getLocalSettingPrintMap() {
        return localSettingPrintMaps;
    }

    public static List<DmLocalSettingPrinter_Mdl> getLocalSettingPrinters() {
        return localSettingPrinters;
    }

//    public static void localSettingPrintMap(DmReportProperty_Mdl reportProperty, String moduleName, String menuText, String reportName) {
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

    public static void replaceOldReportMapping(DmLocalSettingPrintMap_Mdl reportMapModel) {
        if (DmBaseClient.localSettingPrintMaps.contains(reportMapModel)) {
            DmBaseClient.localSettingPrintMaps.remove(reportMapModel);
        }

        DmBaseClient.localSettingPrintMaps.add(reportMapModel);
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

    public static boolean isReportMapExists(String moduleName, String menuText, String reportName) {
        DmLocalSettingPrintMap_Mdl reportMap = new DmLocalSettingPrintMap_Mdl();
        reportMap.setModuleName(moduleName);
        reportMap.setMenuText(menuText);
        reportMap.setReportName(reportName);

        return DmBaseClient.localSettingPrintMaps.contains(reportMap);
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
                    new DmAlertMessageBox(exception.getMessage(), (Button) null).show();
                }
            });
            requestBuilder.send();
        } catch (Exception ex) {

        }
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

//    public static String getTcpSessionId() {
//        return tcpSessionId;
//    }
//
//    public static void setTcpSessionId(String tcpSessionId) {
//        DmClient.tcpSessionId = tcpSessionId;
//    }
    public static void setPACWebPort(int webPort) {
        pacWebPort = webPort;
    }

    public static int getPACWebPort() {
        return pacWebPort;
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

    public static void setLoginDate(String value) {
        loginDate = value;
    }

    public static String getLocaleCountry() {
        return localeCountry;
    }

    public static void setLocaleCountry(String value) {
        localeCountry = value;
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

    public static class ViaList {

        private List<ViaListWidget> lstWidgets;

        public ViaList() {
            lstWidgets = new ArrayList<>();
        }

        public List<IsWidget> get() {
            ArrayList<IsWidget> arrayList = new ArrayList<>();

            for (ViaListWidget viaListWidget : lstWidgets) {
                if (viaListWidget != null) {
                    arrayList.add(viaListWidget.getIsWidget());
                }
            }

            return arrayList;
        }

        public List<ViaListWidget> getViaListWidget() {
            return lstWidgets;
        }

        public DmCheckBox add(DmCheckBox cbObject) {
            return add(cbObject, null, "");
        }

        public DmCheckBox add(DmCheckBox cbObject, String name) {
            return add(cbObject, null, name);
        }

        public DmCheckBox add(DmCheckBox cbObject, Object tabIndexAfter) {
            return add(cbObject, tabIndexAfter, "");
        }

        public DmCheckBox add(DmCheckBox cbObject, Object tabIndexAfter, String name) {
            int index = -1;
            cbObject.addKeyDownHandler(new KeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event) {
                    keyDown(event);
                }
            });

            if (tabIndexAfter != null) {
                index = lstWidgets.indexOf(tabIndexAfter);
            }

            if (index >= 0) {
                lstWidgets.add(index + 1, new ViaListWidget(name, cbObject));
            } else {
                lstWidgets.add(new ViaListWidget(name, cbObject));
            }

            return cbObject;
        }

        public IsWidget add(TextBox txtObject) {
            return add(txtObject, null, "");
        }

        public IsWidget add(TextBox txtObject, String name) {
            return add(txtObject, null, name);
        }

        public IsWidget add(TextBox txtObject, Object tabIndexAfter) {
            return add(txtObject, tabIndexAfter, "");
        }

        public IsWidget add(TextBox txtObject, Object tabIndexAfter, String name) {
            int index = -1;
            txtObject.addKeyDownHandler(new KeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event) {
                    keyDown(event);
                }
            });

            if (tabIndexAfter != null) {
                index = lstWidgets.indexOf(tabIndexAfter);
            }

            if (index >= 0) {
                lstWidgets.add(index + 1, new ViaListWidget(name, txtObject));
            } else {
                lstWidgets.add(new ViaListWidget(name, txtObject));
            }

            return txtObject;
        }

        public IsWidget add(DmDateBox txtObject, Object tabIndexAfter) {
            return add(txtObject, tabIndexAfter, "");
        }

        public IsWidget add(DmDateBox txtObject, Object tabIndexAfter, String name) {
            int index = -1;
            txtObject.getTextBox().addKeyDownHandler(new KeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event) {
                    keyDown(event);
                }
            });

            if (tabIndexAfter != null) {
                index = lstWidgets.indexOf(tabIndexAfter);
            }

            if (index >= 0) {
                lstWidgets.add(index + 1, new ViaListWidget(name, txtObject));
            } else {
                lstWidgets.add(new ViaListWidget(name, txtObject.getTextBox()));
            }

            return txtObject;
        }

        public IsWidget add(TextArea txtObject) {
            return add(txtObject, "");
        }

        public IsWidget add(TextArea txtObject, String name) {
            if (txtObject.isReadOnly()) {
                txtObject.addKeyDownHandler(new KeyDownHandler() {
                    @Override
                    public void onKeyDown(KeyDownEvent event) {
                        keyDownPreventDefaultOnEnter(event);
                    }
                });
            }

            lstWidgets.add(new ViaListWidget(name, txtObject));
            return txtObject;
        }

        public IsWidget add(TextArea txtObject, Object tabIndexAfter) {
            return add(txtObject, tabIndexAfter, "");
        }

        public IsWidget add(TextArea txtObject, Object tabIndexAfter, String name) {
            int index = -1;
            if (txtObject.isReadOnly()) {
                txtObject.addKeyDownHandler(new KeyDownHandler() {
                    @Override
                    public void onKeyDown(KeyDownEvent event) {
                        keyDown(event);
                    }
                });
            }
            if (tabIndexAfter != null) {
                index = lstWidgets.indexOf(tabIndexAfter);
            }

            if (index >= 0) {
                lstWidgets.add(index + 1, new ViaListWidget(name, txtObject));
            } else {
                lstWidgets.add(new ViaListWidget(name, txtObject));
            }

            return txtObject;
        }

        public IsWidget add(SuggestBox sgbObject) {
            return add(sgbObject, "");
        }

        public IsWidget add(SuggestBox sgbObject, String name) {
            sgbObject.addKeyDownHandler(new KeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event) {
                    keyDown(event);
                }
            });

            lstWidgets.add(new ViaListWidget(name, sgbObject));
            return sgbObject;
        }

        public IsWidget add(DmTextHelp txthObject) {
            return add(txthObject, null, "");
        }

        public IsWidget add(DmTextHelp txthObject, String name) {
            return add(txthObject, null, name);
        }

        public IsWidget add(DmTextHelp txthObject, Object tabIndexAfter) {
            return add(txthObject, tabIndexAfter, "");
        }

        public IsWidget add(DmTextHelp txthObject, Object tabIndexAfter, String name) {
            int index = -1;

            txthObject.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event, Object objSource) {
                    keyDown(event, objSource);
                }
            });

            if (tabIndexAfter != null) {
                index = lstWidgets.indexOf(tabIndexAfter);
            }

            if (index >= 0) {
                lstWidgets.add(index + 1, new ViaListWidget(name, txthObject));
            } else {
                lstWidgets.add(new ViaListWidget(name, txthObject));
            }

            return txthObject;
        }

        public IsWidget add(DmRemarkTextBox txtObject) {
            return add(txtObject, null, "");
        }

        public IsWidget add(DmRemarkTextBox txtObject, String name) {
            return add(txtObject, null, name);
        }

        public IsWidget add(DmRemarkTextBox txtObject, Object tabIndexAfter) {
            return add(txtObject, tabIndexAfter, "");
        }

        public IsWidget add(DmRemarkTextBox txtObject, Object tabIndexAfter, String name) {
            int index = -1;

            txtObject.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event, Object objSource) {
                    keyDown(event, objSource);
                }
            });

            if (tabIndexAfter != null) {
                index = lstWidgets.indexOf(tabIndexAfter);
            }

            if (index >= 0) {
                lstWidgets.add(index + 1, new ViaListWidget(name, txtObject));
            } else {
                lstWidgets.add(new ViaListWidget(name, txtObject));
            }

            return txtObject;
        }

        public IsWidget add(DmTextHelpMulti txthObject) {
            return add(txthObject, null, "");
        }

        public IsWidget add(DmTextHelpMulti txthObject, String name) {
            return add(txthObject, null, name);
        }

        public IsWidget add(DmTextHelpMulti txthObject, Object tabIndexAfter) {
            return add(txthObject, tabIndexAfter, "");
        }

        public IsWidget add(DmTextHelpMulti txthObject, Object tabIndexAfter, String name) {
            int index = -1;

            txthObject.addKeyDownHandler(new DmTextHelpMulti.TxtHelpMultiKeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event, Object objSource) {
                    keyDown(event, objSource);
                }
            });

            if (tabIndexAfter != null) {
                index = lstWidgets.indexOf(tabIndexAfter);
            }

            if (index >= 0) {
                lstWidgets.add(index + 1, new ViaListWidget(name, txthObject));
            } else {
                lstWidgets.add(new ViaListWidget(name, txthObject));
            }

            return txthObject;
        }

        public <T> IsWidget add(Grid<T> grid) {
            return add(grid, "");
        }

        public <T> IsWidget add(Grid<T> grid, String name) {
            grid.addDomHandler(new KeyDownHandler() {

                @Override
                public void onKeyDown(KeyDownEvent event) {
                    keyDownWithCtrl(event);
                }
            }, KeyDownEvent.getType());

            lstWidgets.add(new ViaListWidget(name, grid));
            return grid;
        }

        public IsWidget add(OverHead overHead) {
            return add(overHead, "");
        }

        public IsWidget add(OverHead overHead, String name) {
            overHead.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

                @Override
                public void onKeyDown(KeyDownEvent event) {
                    keyDownWithCtrl(event);
                }
            }, KeyDownEvent.getType());

            lstWidgets.add(new ViaListWidget(name, overHead.getDmGrid().getGrid()));
            return overHead;
        }

        public IsWidget add(OverHeadLine overHeadLine) {
            return add(overHeadLine, "");
        }

        public IsWidget add(OverHeadLine overHeadLine, String name) {
            overHeadLine.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

                @Override
                public void onKeyDown(KeyDownEvent event) {
                    keyDownWithCtrl(event);
                }
            }, KeyDownEvent.getType());

            lstWidgets.add(new ViaListWidget(name, overHeadLine.getDmGrid().getGrid()));
            return overHeadLine;
        }

        public IsWidget add(IsWidget wdgObject) {
            return add(wdgObject, "");
        }

        public IsWidget add(IsWidget wdgObject, String name) {
            lstWidgets.add(new ViaListWidget(name, wdgObject));
            return wdgObject;
        }

        private void keyDownWithCtrl(KeyDownEvent event) {

            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.getNativeEvent().getCtrlKey()) {
                setFocus(event, (IsWidget) event.getSource());
            }
        }

        private void keyDownPreventDefaultOnEnter(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (event.isControlKeyDown()) {
                    setFocus(event, (IsWidget) event.getSource());
                }
            }
        }

        private void keyPressPreventDefaultOnEnter(KeyPressEvent event) {

            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!event.isControlKeyDown()) {
                    event.preventDefault();
                } else {
                    TextArea textArea = (TextArea) event.getSource();
                    textArea.setText(DmShared.xNull(textArea.getText()) + "\n");
                }
            }
        }

        private void keyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                setFocus(event, (IsWidget) event.getSource());
            }
        }

        private void keyDown(KeyDownEvent event, Object objSource) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                setFocus(event, (IsWidget) objSource);
            }
        }

        private void setFocus(KeyDownEvent event, IsWidget wdgObject) {
            int intIndex, intNextIndex;
            TextBox textBox;
            DmRemarkTextBox remarkTextBox;
            DmTextHelp textHelp;
            DmTextHelpMulti textHelpMulti;
            SuggestBox suggestBox;
            DmTopControl topControl;
            Button button;
            TextArea textArea;
            Grid grid;
            DmCheckBox chkBox;

            intNextIndex = intIndex = lstWidgets.indexOf(new ViaListWidget("", wdgObject));

            do {
                if (intNextIndex >= 0 && (intNextIndex < lstWidgets.size() - 1)) {
                    intNextIndex += 1;
                } else {
                    intNextIndex = 0;
                }

                wdgObject = lstWidgets.get(intNextIndex).getIsWidget();

                if (wdgObject instanceof TextBox) {
                    textBox = (TextBox) wdgObject;
                    if (textBox.isEnabled() && textBox.isAttached() && textBox.isVisible()) {
                        if (!(textBox.getOffsetHeight() == 0 && textBox.getOffsetWidth() == 0)) {
                            textBox.setFocus(true);
                            textBox.selectAll();
                            break;
                        }
                    }
                } else if (wdgObject instanceof SuggestBox) {
                    suggestBox = (SuggestBox) wdgObject;
                    if (suggestBox.isEnabled() && suggestBox.isAttached() && suggestBox.isVisible()) {
                        if (!(suggestBox.getOffsetHeight() == 0 && suggestBox.getOffsetWidth() == 0)) {
                            suggestBox.setFocus(true);
                            //                        suggestBox.selectAll();
                            break;
                        }
                    }
                } else if (wdgObject instanceof DmTopControl) {
                    topControl = (DmTopControl) wdgObject;
                    topControl.setFocus();
                    break;
                } else if (wdgObject instanceof Button) {
                    button = (Button) wdgObject;
                    if (button.isEnabled() && button.isAttached() && button.isVisible()) {
                        if (!(button.getOffsetHeight() == 0 && button.getOffsetWidth() == 0)) {
                            button.setFocus(true);
                            break;
                        }
                    }
                } else if (wdgObject instanceof DmCheckBox) {
                    chkBox = (DmCheckBox) wdgObject;
                    if (chkBox.isEnabled() && chkBox.isAttached() && chkBox.isVisible()) {
                        if (!(chkBox.getOffsetHeight() == 0 && chkBox.getOffsetWidth() == 0)) {
                            chkBox.setFocus(true);
                            break;
                        }
                    }
                } else if (wdgObject instanceof DmTextHelp) {
                    textHelp = (DmTextHelp) wdgObject;
                    if (textHelp.isEnabled() && textHelp.asWidget().isAttached() && textHelp.asWidget().isVisible()) {
                        if (!(textHelp.getOffsetHeight() == 0 && textHelp.getOffsetWidth() == 0)) {
                            textHelp.setFocus(true);
                            break;
                        }
                    }
                } else if (wdgObject instanceof DmTextHelpMulti) {
                    textHelpMulti = (DmTextHelpMulti) wdgObject;
                    if (textHelpMulti.isEnabled() && textHelpMulti.asWidget().isAttached() && textHelpMulti.asWidget().isVisible()) {
                        if (!(textHelpMulti.getOffsetHeight() == 0 && textHelpMulti.getOffsetWidth() == 0)) {
                            textHelpMulti.setFocus(true);
                            break;
                        }
                    }
                } else if (wdgObject instanceof TextArea) {
                    textArea = (TextArea) wdgObject;
                    if (textArea.isEnabled() && textArea.isAttached() && textArea.isVisible()) {
                        if (!(textArea.getOffsetHeight() == 0 && textArea.getOffsetWidth() == 0)) {
                            textArea.setFocus(true);
                            break;
                        }
                    }
                } else if (wdgObject instanceof Grid) {
                    Integer defaultColumnIndex;
                    grid = (Grid) wdgObject;

                    if (grid.isAttached() && grid.isVisible()) {
                        if (!(grid.getOffsetHeight() == 0 && grid.getOffsetWidth() == 0)) {
                            if (grid.getSelectionModel() instanceof CellSelectionModel) {
                                defaultColumnIndex = grid.<Integer>getData(DmGrid.defaultColumnIndexLabel);
                                if (grid.getStore().size() > 0 && defaultColumnIndex != null) {
                                    ((CellSelectionModel) grid.getSelectionModel()).selectCell(0, defaultColumnIndex);
                                }
                            }
                        }
                        grid.focus();
                        break;
                    }

                } else if (wdgObject instanceof DmRemarkTextBox) {
                    remarkTextBox = (DmRemarkTextBox) wdgObject;
                    if (remarkTextBox.isEnabled()) {
                        if (!(remarkTextBox.getOffsetHeight() == 0 && remarkTextBox.getOffsetWidth() == 0)) {
                            remarkTextBox.setFocus(true);
                            break;
                        }
                    }
                } else {

                }
            } while (intNextIndex != intIndex);
        }
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

//    public static boolean isNumeric(String value) {
//        try {
//            double d = Double.parseDouble(value);
//        } catch (Exception nfe) {
//            return false;
//        }
//        return true;
//    }
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

    public static boolean isValidText(String fieldValue, String fieldName, IsWidget isWidget) {
        if (DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
            showAlertMessage(fieldName.trim() + " is mandatory.", isWidget);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidAmount(Object fieldValue, String fieldName, IsWidget isWidget) {
        if (DmShared.vNull(fieldValue) <= 0) {
            showAlertMessage(fieldName.trim() + " is mandatory.", isWidget);
            return false;
        } else {
            return true;
        }
    }

    private static void showAlertMessage(String msg, IsWidget isWidget) {
        if (isWidget instanceof DmTextBox) {
            new DmAlertMessageBox(msg, (DmTextBox) isWidget).show();
        } else if (isWidget instanceof DmTextHelp) {
            new DmAlertMessageBox(msg, (DmTextHelp) isWidget).show();
        } else if (isWidget instanceof DmTextHelpMulti) {
            new DmAlertMessageBox(msg, (DmTextHelpMulti) isWidget).show();
        } else if (isWidget instanceof DmSuggestBox) {
            new DmAlertMessageBox(msg, (DmSuggestBox) isWidget).show();
        } else if (isWidget instanceof DmTextBoxNumeric) {
            new DmAlertMessageBox(msg, (DmTextBoxNumeric) isWidget).show();
        } else if (isWidget instanceof TextArea) {
            new DmAlertMessageBox(msg, (TextArea) isWidget).show();
        } else if (isWidget instanceof Button) {
            new DmAlertMessageBox(msg, (Button) isWidget).show();
        } else if (isWidget instanceof DmRemarkTextBox) {
            new DmAlertMessageBox(msg, (DmRemarkTextBox) isWidget).show();
        } else {
            new DmAlertMessageBox(msg, (DmTextBox) null).show();
        }
    }

//    public static void addMenu(final List<UserMenu> menuList, MenuBar menuBar, MenuClick.InitializeWidgetInvoker iwi) {
//        MenuItem mnuTemp;
//        MenuBar subMenu;
//
//        for (int i = 0; i < (menuList.size()); i++) {
//            if (menuList.get(i).getUserMenu().size() > 0) {
//                subMenu = new MenuBar(true);
////                menuBar.addItem(menuList.get(i).getMenuName(), subMenu);
//                menuBar.addItem(getMenuName(menuList.get(i).getMenuName()), subMenu);
//                addSubMenu(menuList.get(i).getUserMenu(), subMenu, iwi);
//            } else {
//                mnuTemp = new MenuItem(menuList.get(i).getMenuName(), (MenuClick) null);
//                if (!DmShared.xNull(menuList.get(i).getToolTip()).trim().equalsIgnoreCase("")) {
//                    mnuTemp.setTitle("Short Hand: (" + DmShared.xNull(menuList.get(i).getToolTip()).trim() + ")");
//                    mnuTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//                }
//                mnuTemp.setScheduledCommand(new MenuClick(mnuTemp, iwi));
//                menuBar.addItem(mnuTemp);
//            }
//        }
//    }
//
//    private static void addSubMenu(List<UserMenu> menuList, MenuBar menuBar, MenuClick.InitializeWidgetInvoker iwi) {
//        MenuItem mnuTemp;
//        MenuBar subMenu;
//
//        for (int i = 0; i < (menuList.size()); i++) {
//            if (menuList.get(i).getUserMenu().size() > 0) {
//                subMenu = new MenuBar(true);
////                menuBar.addItem(menuList.get(i).getMenuName(), subMenu);
//                menuBar.addItem(getMenuName(menuList.get(i).getMenuName()), subMenu);
//                addSubMenu(menuList.get(i).getUserMenu(), subMenu, iwi);
//            } else {
//                mnuTemp = new MenuItem(menuList.get(i).getMenuName(), (MenuClick) null);
//                if (!DmShared.xNull(menuList.get(i).getToolTip()).trim().equalsIgnoreCase("")) {
//                    mnuTemp.setTitle("Short Hand: (" + DmShared.xNull(menuList.get(i).getToolTip()).trim() + ")");
//                    mnuTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//                }
//                mnuTemp.setScheduledCommand(new MenuClick(mnuTemp, iwi));
//                menuBar.addItem(mnuTemp);
//            }
//        }
//    }
//
//    private static SafeHtml getMenuName(final String menuName) {
//        SafeHtml safeHtml = new SafeHtml() {
//            @Override
//            public String asString() {
////                return menuName + "<span class='fa fa-angle-down' style='margin-left: 5px;font-size: 13px;font-weight: bold;'></span>";
//                return menuName + "<span class='fa fa-angle-down' style='margin-left: 5px;'></span>";
//            }
//        };
//        return safeHtml;
//    }
    public static void addMenuBarItem(final List<UserMenu> menuList, DmMenuBar menuBar, DmMenuItem.InitializeWidgetInvoker iwi) {
        DmMenuBarItem menuBarItem;

        for (int i = 0; i < (menuList.size()); i++) {
            if (menuList.get(i).getUserMenu().size() > 0) {
                menuBarItem = new DmMenuBarItem(menuList.get(i), menuBar);
                addMenu(menuList.get(i).getUserMenu(), menuBarItem, iwi);
            }
        }
    }

    private static void addMenu(List<UserMenu> menuList, DmMenuBarItem menuBarItem, DmMenuItem.InitializeWidgetInvoker iwi) {
        DmMenu menu;

        for (int i = 0; i < (menuList.size()); i++) {
            if (menuList.get(i).getUserMenu().size() > 0) {
                menu = new DmMenu(menuList.get(i), menuBarItem);
                addMenuItem(menuList.get(i).getUserMenu(), menu, iwi);
            }
        }
    }

    private static void addMenuItem(List<UserMenu> menuList, DmMenu menuBar, DmMenuItem.InitializeWidgetInvoker iwi) {
        DmMenuItem mnuTemp;

        for (int i = 0; i < (menuList.size()); i++) {
            if (menuList.get(i).getUserMenu().size() > 0) {
            } else {
                mnuTemp = new DmMenuItem(menuList.get(i), menuBar, iwi);
            }
        }
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

//    public static String getRegionalFontFamily() {
//        String regionalFontFamily;
//        DmRegional_Res res = DmRegional_Bnd.INSTANCE.css();
//
//        if (DmShared.xNull(getRegionalLanguage()).trim().equalsIgnoreCase(RegionalLanguage.HINDI.toString())) {
//            regionalFontFamily = res.hindiFont();
//        } else if (DmShared.xNull(getRegionalLanguage()).trim().equalsIgnoreCase(RegionalLanguage.PUNJABI.toString())) {
//            regionalFontFamily = res.punjabiFont();
//        } else if (DmShared.xNull(getRegionalLanguage()).trim().equalsIgnoreCase(RegionalLanguage.MARATHI.toString())) {
//            regionalFontFamily = res.marathiFont();
//        } else if (DmShared.xNull(getRegionalLanguage()).trim().equalsIgnoreCase(RegionalLanguage.BANGALI.toString())) {
//            regionalFontFamily = res.bangaliFont();
//        } else {
//            regionalFontFamily = res.englishFont();
//        }
//
//        return regionalFontFamily;
//    }
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

//    public static String concatURL(String classPath, HashMap<String, String> params) {
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
//    }
//    public static String concatURL(String baseProtocol, String basePath, String classPath, HashMap<String, String> params) {
//        String url = "";
////        DmBaseURL baseURl;
//
//        try {
////            baseURl = getHostPageBaseURL();
//            UrlBuilder urlBuild = new UrlBuilder();
//            urlBuild.setProtocol(baseProtocol)
//            );
//            urlBuild.setPath(baseURl.getUrl() + "/" + classPath);
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
//    }
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

    public static void reIndexViaList(ViaList viaList, List<KeyFieldExtended> listFetchRecords) {
        List<ViaListWidget> listViaListWidget = viaList.getViaListWidget();
        if (listFetchRecords.size() > 0) {
            if (listFetchRecords.size() == listViaListWidget.size()) {
                for (KeyField listMdl : listFetchRecords) {
                    if (listMdl != null) {
                        for (int i = 0; i < listViaListWidget.size(); i++) {
                            ViaListWidget tempViaListWidget = listViaListWidget.get(i);
                            if (DmShared.xNull(tempViaListWidget.getName()).equalsIgnoreCase(listMdl.getSharedKeyFieldCode())) {
                                int suggestedIndex = DmShared.iNull(listMdl.getSharedKeyFieldName()).intValue();
                                tempViaListWidget = viaList.getViaListWidget().remove(viaList.getViaListWidget().indexOf(tempViaListWidget));
                                viaList.getViaListWidget().add((suggestedIndex - 1), tempViaListWidget);
                            }
                        }
                    }
                }
            } else {
                new DmAlertMessageBox("Tab index setting mismatched. Please re-arrange it.", (DmTextBox) null).show();
            }
        }
    }
    
    public static void setStyleHyperlinkLabel(DmLabel lbl, boolean isHyperLink, String title, boolean isUnderLine) {
        lbl.setTitle(DmShared.xNull(title));
        if (isHyperLink) {
            lbl.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
            lbl.getElement().getStyle().setColor("blue");
        } else {
            lbl.getElement().getStyle().setCursor(Style.Cursor.AUTO);
            if (isUnderLine) {
                lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
            } else {
                lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);
            }
            lbl.getElement().getStyle().setColor("rgb(68, 68, 68)");
        }
    }
}
