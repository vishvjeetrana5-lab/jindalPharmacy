package aarogya.self.client;

import aarogya.common.client.Common;
import aarogya.common.client.Common_Res;
import aarogya.common.shared.*;
import aarogya.company.client.Company;
import aarogya.dashBoard.home.client.Home;
import aarogya.dashBoard.self.client.DashBoard;
import aarogya.inventory.self.client.Inventory;
import aarogya.lock.client.Lock;
import aarogya.login.client.Login;
import aarogya.master.self.client.Master;
import aarogya.mis.self.client.MIS;
import aarogya.nabh.self.client.NABH;
import aarogya.pharmacy.self.client.Pharmacy;
import aarogya.plt.client.PLT;
import aarogya.projectMenu.self.shared.*;
import aarogya.ribbon.client.Ribbon;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.site.client.Site;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import dataman.accounts.self.client.Accounts;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.*;
import dataman.utility.self.client.Utility;
import dataman.webJobs.self.client.WebJobs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Aarogya implements EntryPoint {

    private Home home;
    private DashBoard dashboard;
    private Master master;
    private MIS mis;
    private Pharmacy pharmacy;
    private Accounts accounts;
    private WebJobs webJobs;
    private Utility utility;
    private NABH nabh;
    private Inventory inventory;
    private List<IsWidget> lstCurrentlyOpen = new ArrayList<>();
    private ServiceInvoker<Common_SrvAsync> SrvAsync;

    public Aarogya() {
        SrvAsync = new ServiceInvoker<Common_SrvAsync>(GWT.create(Common_Srv.class), Common.class.getName());
    }

    @Override
    public void onModuleLoad() {
        ini();
    }

    private void ini() {
        initializePubObject(false);
        applyWidget(new Login(false));
    }

    private void initializePubObject(boolean blnWithOtherVisible) {
        home = null;
        dashboard = null;
        master = null;
        mis = null;
        pharmacy = null;
        accounts = null;
        webJobs = null;
        utility = null;
        nabh = null;
        inventory = null;
        lstCurrentlyOpen = new ArrayList<>();

        DmClient.initializeVariables(true);
        DmClient.getRoot().clear();
        removeAllWidgetFromList();

        DmClient.setStoreItem("swiEvent", new SetWidgetInvokerExtended() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                applyWidget(wgtObject);
            }

            @Override
            public void setWidget(String moduleName, String menuText, SetWidgetInvoker swi) {
                applyWidget(moduleName, menuText, swi);
            }

            @Override
            public void setWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi) {
                applyWidget(moduleName, menuText, paramHashMap, userMenu, swi);
            }

            @Override
            public void reInitialize(PCSEnum reInitialize) {
                if (null == reInitialize) {
                    applyWidget(null);
                    SrvAsync.invokeRPC(true).reInitialize(reInitialize, new AsyncCallback<DmResult<PCSData>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(DmResult<PCSData> result) {
                            PCSEnum pcsEnumTemp = PCSEnum.PLT;

                            if (result.isStatus()) {
                                if (result.getErrorMessage().equalsIgnoreCase(PCSEnum.PLT.toString())) {
                                    pcsEnumTemp = PCSEnum.PLT;
                                } else if (result.getErrorMessage().equalsIgnoreCase(PCSEnum.Company.toString())) {
                                    pcsEnumTemp = PCSEnum.Company;
                                } else if (result.getErrorMessage().equalsIgnoreCase(PCSEnum.Site.toString())) {
                                    pcsEnumTemp = PCSEnum.Site;
                                }

                                navigate(pcsEnumTemp, result.getData(), true);
                            } else {
                                Window.alert(result.getErrorMessage());
                            }
                        }
                    });
                } else {
                    switch (reInitialize) {
                        case Login:
                            SrvAsync.invokeRPC(true).reInitialize(reInitialize, new AsyncCallback<DmResult<PCSData>>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    Window.alert(caught.getMessage());
                                }

                                @Override
                                public void onSuccess(DmResult<PCSData> result) {
                                    if (result.isStatus()) {
                                        DmClient.initializeVariables(true);
                                        initializePubObject(false);
                                        applyWidget(new Login(false));
                                    }
                                }
                            });
                            break;
                        case LOCK:
                            Lock lock = new Lock();
                            lock.show();
                            break;
                        case DASHBOARD:
                            showDashBoard();
                            break;
                        default:
                            applyWidget(null);
                            SrvAsync.invokeRPC(true).reInitialize(reInitialize, new AsyncCallback<DmResult<PCSData>>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    Window.alert(caught.getMessage());
                                }

                                @Override
                                public void onSuccess(DmResult<PCSData> result) {
                                    PCSEnum pcsEnumTemp = PCSEnum.PLT;

                                    if (result.isStatus()) {
                                        if (result.getErrorMessage().equalsIgnoreCase(PCSEnum.PLT.toString())) {
                                            pcsEnumTemp = PCSEnum.PLT;
                                        } else if (result.getErrorMessage().equalsIgnoreCase(PCSEnum.Company.toString())) {
                                            pcsEnumTemp = PCSEnum.Company;
                                        } else if (result.getErrorMessage().equalsIgnoreCase(PCSEnum.Site.toString())) {
                                            pcsEnumTemp = PCSEnum.Site;
                                        }

                                        navigate(pcsEnumTemp, result.getData(), true);
                                    } else {
                                        Window.alert(result.getErrorMessage());
                                    }
                                }
                            });
                            break;
                    }
                }
            }

            @Override
            public void next(PCSEnum iAm, PCSData pcsTemp) {
                navigate(iAm, pcsTemp, false);
            }

            private void navigate(PCSEnum iAm, PCSData pcsTemp, boolean blnViaReInitialize) {
                Object objTemp = null;

                if (iAm == PCSEnum.PLT && blnViaReInitialize) {
                    setWidget(new PLT(pcsTemp.lstPLTs));
                    return;
                } else {
                    if (pcsTemp.lstPLTs.size() > 1 && pcsTemp.pltSelected == null) {
                        setWidget(new PLT(pcsTemp.lstPLTs));
                        return;
                    } else if (pcsTemp.pltSelected != null) {
                        pcsTemp.lstPLTs.clear();
                        pcsTemp.lstPLTs.add(pcsTemp.pltSelected);
                        objTemp = new PLT(pcsTemp.lstPLTs);
                        objTemp = null;
                    }
                }

                if (iAm == PCSEnum.Company && blnViaReInitialize) {
                    setWidget(new Company(pcsTemp.lstCompanys));
                    return;
                } else {
                    if (pcsTemp.lstCompanys.size() > 1 && pcsTemp.cSelected == null) {
                        setWidget(new Company(pcsTemp.lstCompanys));
                        return;
                    } else if (pcsTemp.cSelected != null) {
                        pcsTemp.lstCompanys.clear();
                        pcsTemp.lstCompanys.add(pcsTemp.cSelected);
                        objTemp = new Company(pcsTemp.lstCompanys);
                        objTemp = null;
                    } else if (pcsTemp.cSelected == null) {
                        if (pcsTemp.lstCompanys.size() > 0) {
                            setWidget(new Company(pcsTemp.lstCompanys));
                            return;
                        }
                    }
                }

                if (iAm == PCSEnum.Site && blnViaReInitialize) {
                    setWidget(new Site(pcsTemp.lstSites, false));
                    return;
                } else {
                    if (pcsTemp.lstSites.size() > 1 && pcsTemp.sSelected == null) {
                        setWidget(new Site(pcsTemp.lstSites, false));
                        return;
                    } else if (pcsTemp.sSelected != null) {
                        DmClient.setRegionalLanguage(pcsTemp.getRegionalLanguage());
                        pcsTemp.lstSites.clear();
                        pcsTemp.lstSites.add(pcsTemp.sSelected);
                        objTemp = new Site(pcsTemp.lstSites, false);
                        objTemp = null;
                    }
                }

                initializeMenus();
            }

            @Override
            public void closeWidget(IsWidget wgtObject) {
                if (lstCurrentlyOpen.contains(wgtObject)) {
                    removeWidgetFromList(wgtObject);
                    setLastWidgetFromList();
                }
            }

            @Override
//            public void showCurrentList(final PopupPanel ppCurrentList) {
            public void showCurrentList(final PopupPanel containerPPanel, final FlowPanel currentListFPanel) {
                FlowPanel flwTemp = new FlowPanel();
                Anchor aTemp;
                FlowPanel flwTemp1;
                FlowPanel flwBttn;
                InlineLabel imgTemp;

                currentListFPanel.clear();
                flwTemp1 = new FlowPanel();
                flwTemp1.setStyleName("closeAllModules");
                flwBttn = new FlowPanel();
                flwBttn.setStyleName("dm-closeall-btn-style");
                imgTemp = new InlineLabel();
                imgTemp.getElement().getStyle().setFloat(Style.Float.LEFT);
                imgTemp.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
                imgTemp.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
                imgTemp.getElement().getStyle().setFontSize(1.1, Style.Unit.EM);
                imgTemp.getElement().getStyle().setColor("red");
                imgTemp.setStyleName("fa fa-trash-o");

                aTemp = new Anchor("Close All");
                aTemp.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
                aTemp.setStyleName("closeAllModulesA ellipsis");
//                aTemp.setWidth("180px");
                aTemp.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        removeAllWidgetFromList();
                        containerPPanel.hide();
                    }
                });

                flwBttn.add(imgTemp);
                flwBttn.add(aTemp);
                flwTemp1.add(flwBttn);
                flwTemp.add(flwTemp1);

                for (int i = 0; i < lstCurrentlyOpen.size(); i++) {
                    aTemp = new Anchor();

                    flwTemp1 = new FlowPanel();
                    flwTemp1.setStyleName("moduleOpneButtos");

                    imgTemp = new InlineLabel();
                    imgTemp.getElement().getStyle().setFloat(Style.Float.LEFT);
                    imgTemp.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
                    imgTemp.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
                    imgTemp.getElement().getStyle().setFontSize(1.1, Style.Unit.EM);
                    if (lstCurrentlyOpen.get(i) instanceof DmReportLayout) {
                        imgTemp.getElement().getStyle().setColor("maroon");
                        imgTemp.setStyleName("fa fa-file-pdf-o");
                    } else {
                        imgTemp.getElement().getStyle().setColor("black");
                        imgTemp.setStyleName("fa fa-file-text-o");
                    }

                    aTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    aTemp.setText(lstCurrentlyOpen.get(i).asWidget().getTitle());
                    aTemp.setHref("#" + String.valueOf(i));
                    aTemp.setStyleName("moduleOpneButtosA ellipsis");
                    aTemp.setWidth("180px");
                    handlerBtnClick(aTemp, containerPPanel);

                    flwTemp1.add(imgTemp);
                    flwTemp1.add(aTemp);
                    flwTemp.add(flwTemp1);
                }
                currentListFPanel.add(flwTemp);
            }

        });

        DmClient.<Ribbon>setStoreItem("Ribbon", new Ribbon(blnWithOtherVisible));
        DmClient.getRoot().add(DmClient.<Ribbon>getStoreItem("Ribbon"));
    }

    private void handlerBtnClick(Anchor btn, final PopupPanel containerPPanel) {
        btn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String[] arrayList;
                String temp;
                int i;
                IsWidget isWidget;

                temp = ((Anchor) event.getSource()).getHref();
                arrayList = temp.split("#");

                if (arrayList.length > 0) {
                    i = Integer.valueOf(arrayList[arrayList.length - 1]);
                    if (lstCurrentlyOpen.size() > 0) {
                        removeAllWidgetFromRoot();
                        isWidget = lstCurrentlyOpen.get(i);
                        lstCurrentlyOpen.remove(i);
                        lstCurrentlyOpen.add(isWidget);
                        DmClient.getRoot().add(isWidget);
                        containerPPanel.hide();
                    }
                }
            }
        });
    }

    private void initializeMenus() {
        removeAllWidgetFromList();

        SrvAsync.invokeRPC().getPACWebPort(new AsyncCallback<DmResult<Integer>>() {

            @Override
            public void onFailure(Throwable caught) {
//                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<Integer> result) {
                if (result.isStatus()) {
                    DmClient.setPACWebPort(result.getData());
                    STClient.pacHandShake(new STClient.IPACHandShake() {
                        @Override
                        public void onSuccess() {
//                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    });

//                    try {
//                        RequestBuilder rBuilder = new RequestBuilder(RequestBuilder.GET, "http://localhost:" + String.valueOf(DmClient.getPACWebPort()) + "/queryClient");
//                        rBuilder.sendRequest(null, new RequestCallback() {
//                            @Override
//                            public void onResponseReceived(Request request, Response response) {
//                                if ((!DmShared.xNull(response.getText()).equalsIgnoreCase("")) && (!DmShared.xNull(response.getText()).equalsIgnoreCase("test"))) {
//                                    DmClient.setTcpSessionId(response.getText().toLowerCase().trim());
////                                    reportProperty.setTcpSessionId(response.getText().toLowerCase().trim());
////                                    callback.processDirectPrint(reportProperty);
//                                }
//                            }
//
//                            @Override
//                            public void onError(Request request, Throwable exception) {
//                                Window.alert(exception.getMessage());
//                            }
//                        });
//                    } catch (Exception e) {
////                        Window.alert(e.getMessage());
//                    }
                }
            }
        });

        SrvAsync.invokeRPC(true).getUserPermission(new AsyncCallback<DmResult<UserPermission>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<UserPermission> result) {

                if (result.isStatus()) {
                    if (!DmShared.xNull(result.getErrorMessage()).equalsIgnoreCase("")) {
                        Window.alert(result.getErrorMessage());
                    }

                    DmClient.setUserPermission(result.getData().getUserMenu());
                    DmClient.setVoucherPermission(result.getData().getVoucherPermission());

                    home = new Home(DmClient.getUserPermission(new pmDashBoard().moduleName));
                    dashboard = new DashBoard(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    master = new Master(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    mis = new MIS(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    pharmacy = new Pharmacy(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    accounts = new Accounts(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    webJobs = new WebJobs(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    utility = new Utility(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    inventory = new Inventory(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    nabh = new NABH(DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

                    DmClient.<Ribbon>getStoreItem("Ribbon").setModuleIcon(initiateModule(), new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            setModule(((FlowPanel) event.getSource()).getTitle());
                        }
                    });
                } else {
                    Window.alert(result.getErrorMessage());
                }
            }
        });

    }

    private List<Common_Model.dmImgResourse> initiateModule() {
        String[] modulePermission = DmClient.getModulePermission();
        List<Common_Model.dmImgResourse> lstImgRes = new ArrayList();

        for (int i = 0; i < modulePermission.length; i++) {
            if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new pmDashBoard().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new pmDashBoard().moduleName, Common_Res.rsObj.imgRDashboard()));
                dashboard.addMenu(DmClient.getUserPermission(new pmDashBoard().moduleName));

            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new pmMaster().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new pmMaster().moduleName, Common_Res.rsObj.imgRMaster()));
                master.addMenu(DmClient.getUserPermission(new pmMaster().moduleName));

            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new pmMIS().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new pmMIS().moduleName, Common_Res.rsObj.imgRMIS()));
                mis.addMenu(DmClient.getUserPermission(new pmMIS().moduleName));

            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new pmPharmacy().moduleName, Common_Res.rsObj.imgRPharmacy()));
                pharmacy.addMenu(DmClient.getUserPermission(new pmPharmacy().moduleName));

            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new lmAccounts().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new lmAccounts().moduleName, Common_Res.rsObj.imgRAccounts()));
                accounts.addMenu(DmClient.getUserPermission(new lmAccounts().moduleName));

            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase("Export")) {
//                lstImgRes.add(new Aarogya_Model.dmImgResourse("Export", Aarogya_Res.rsObj.imgRExport()));
            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase("Forms")) {
//                lstImgRes.add(new Aarogya_Model.dmImgResourse("Forms", Aarogya_Res.rsObj.imgRForms()));
            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase("MLM")) {
//                lstImgRes.add(new Aarogya_Model.dmImgResourse("MLM", Aarogya_Res.rsObj.imgRMLM_Binary_Tree()));
            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new pmInventory().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new pmInventory().moduleName, Common_Res.rsObj.imgRInventory()));
                inventory.addMenu(DmClient.getUserPermission(new pmInventory().moduleName));
            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new pmNABH().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new pmNABH().moduleName, Common_Res.rsObj.imgRNABH()));
                nabh.addMenu(DmClient.getUserPermission(new pmNABH().moduleName));

            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new lmWebJobs().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new lmWebJobs().moduleName, Common_Res.rsObj.imgRWebJobs()));
                webJobs.addMenu(DmClient.getUserPermission(new lmWebJobs().moduleName));

            } else if (DmShared.xNull(modulePermission[i]).equalsIgnoreCase(new lmUtility().moduleName)) {
                lstImgRes.add(new Common_Model.dmImgResourse(new lmUtility().moduleName, Common_Res.rsObj.imgRUtility()));
                utility.addMenu(DmClient.getUserPermission(new lmUtility().moduleName));

            }
        }

        if (modulePermission.length > 0) {
            setModule(modulePermission[0]);
            showDashBoard();
        }
        return lstImgRes;
    }

    private void showDashBoard() {
        if (home != null) {
            removeAllWidgetFromRoot();
            DmClient.getRoot().add(home);
        }
    }

    private void removeDashBoard() {
        if (home != null) {
            DmClient.getRoot().remove(home);
        }
    }

    private void setModule(String moduleName) {
        if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmDashBoard().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(dashboard, new pmDashBoard().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmMaster().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(master, new pmMaster().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmMIS().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(mis, new pmMIS().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(pharmacy, new pmPharmacy().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new lmAccounts().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(accounts, new lmAccounts().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new lmWebJobs().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(webJobs, new lmWebJobs().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new lmUtility().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(utility, new lmUtility().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmInventory().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(inventory, new pmInventory().moduleName);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmNABH().moduleName)) {
            DmClient.<Ribbon>getStoreItem("Ribbon").setMenu(nabh, new pmNABH().moduleName);
        }
    }

    private void applyWidget(String moduleName, String menuText, SetWidgetInvoker swi) {
        applyWidget(moduleName, menuText, null, null, swi);
    }

    public void applyWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi) {
        if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmDashBoard().moduleName)) {
            dashboard.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmMaster().moduleName)) {
            master.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmMIS().moduleName)) {
            mis.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
            pharmacy.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new lmAccounts().moduleName)) {
            accounts.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new lmWebJobs().moduleName)) {
            webJobs.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new lmUtility().moduleName)) {
            utility.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmInventory().moduleName)) {
            inventory.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        } else if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmNABH().moduleName)) {
            nabh.invokeSetWidget(menuText, paramHashMap, userMenu, swi);

        }
    }

    private void applyWidget(IsWidget wgtObject) {
        if (wgtObject == null) {
            initializePubObject(true);
            removeAllWidgetFromList();
            return;
        } else if (wgtObject instanceof Login) {
            removeAllWidgetFromList();
        } else if (wgtObject instanceof PLT || wgtObject instanceof Company || wgtObject instanceof Site) {
            removeAllWidgetFromList();
        }

        if (chkReOpen(wgtObject)) {
            lstCurrentlyOpen.add(wgtObject);
            DmClient.getRoot().add(wgtObject);
        }
    }

    private boolean chkReOpen(IsWidget wgtObject) {
        boolean blnRtn = true;

        removeAllWidgetFromRoot();
        for (int i = 0; i < lstCurrentlyOpen.size(); i++) {
//            if (wgtObject.asWidget().getTitle().equalsIgnoreCase(lstCurrentlyOpen.get(i).asWidget().getTitle())) {
//            if (wgtObject.getClass().getSimpleName().equals(lstCurrentlyOpen.get(i).getClass().getSimpleName())) {
            if (wgtObject.getClass().toString().equals(lstCurrentlyOpen.get(i).getClass().toString())) {
                blnRtn = false;
                if (Window.confirm("Form was already opened in background. Do you want to reopen it?")) {
                    removeWidgetFromList(lstCurrentlyOpen.get(i));
                    blnRtn = true;
                }
            }
        }

        return blnRtn;
    }

    private void removeAllWidgetFromList() {
        removeDashBoard();

        for (int i = 0; i < lstCurrentlyOpen.size(); i++) {
            DmClient.getRoot().remove(lstCurrentlyOpen.get(i));
        }
        lstCurrentlyOpen.clear();
    }

    private void removeAllWidgetFromRoot() {
        removeDashBoard();

        for (int i = 0; i < lstCurrentlyOpen.size(); i++) {
            DmClient.getRoot().remove(lstCurrentlyOpen.get(i));
        }
    }

    private void removeWidgetFromList(IsWidget wgtObject) {
        if (lstCurrentlyOpen.contains(wgtObject)) {
            DmClient.getRoot().remove(wgtObject);
            lstCurrentlyOpen.remove(wgtObject);
        }
    }

    private void setLastWidgetFromList() {
        if (lstCurrentlyOpen.size() > 0) {
            DmClient.getRoot().add(lstCurrentlyOpen.get(lstCurrentlyOpen.size() - 1));
        } else {
            showDashBoard();
        }
    }
}
