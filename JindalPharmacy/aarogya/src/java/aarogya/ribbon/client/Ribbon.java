package aarogya.ribbon.client;

import aarogya.common.client.Common;
import aarogya.common.shared.Common_Srv;
import aarogya.common.shared.Common_SrvAsync;
import aarogya.initializer.client.Initializer;
import aarogya.localSetting.client.LocalSetting;
import dataman.dmToolkit.dmNotifications.client.DmNotificationPopup;
import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.ribbon.shared.Ribbon_Srv;
import aarogya.ribbon.shared.Ribbon_SrvAsync;
import aarogya.ribbon.shared.UserInfo_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmNotifications.client.DmNotificationReceiver;
import dataman.dmToolkit.dmNotifications.shared.DmNotification_Mdl;
import dataman.dmToolkit.dmNotifications.shared.IReceiver;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmWebSocket.shared.CloseEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmInputBox.client.DmInputBox;
import dataman.libraryMenu.self.shared.lmUtility;

public class Ribbon extends Ribbon_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, KeyDownHandler, KeyPressHandler, BlurHandler {

    private ServiceInvoker<Ribbon_SrvAsync> srvAsync;
    private ServiceInvoker<Common_SrvAsync> srvAsyncCommon;
    private String pltName = "";
    private String companyName = "";
    private String companyCode = "";
    private String branchName = "";
    private String moduleName = "";
    private Initializer initializer = new Initializer();
    private DmNotificationReceiver notificationReceiver;
    private Timer timer;
    private RepairDB repairDBPopUp = new RepairDB();

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_M) {
            setModuleOpen();
        }
    }
    private final com.sencha.gxt.widget.core.client.Window window = new com.sencha.gxt.widget.core.client.Window();
    private ScriptElement scriptElement = Document.get().createScriptElement();

    public static native void skypeConnect(String skypeName)/*-{
        $wnd.SkypeWebControl.SDK.Chat.startChat({
            ConversationId: skypeName,
            ConversationType: 'agent'
        });
    }-*/;
    private DmInputBox inputBox = new DmInputBox();
    private String skypeName = "";
    private DmInputBox.CallBack inputBoxCallBack = new DmInputBox.CallBack() {
        @Override
        public void onOk(String inputValue, String parentName) {
            skypeName = inputValue;
//            skypeConnect(skypeName);
            window.hide();
            window.clear();
            window.clearSizeCache();
            Frame skypeFrame = new Frame(GWT.getHostPageBaseURL() + "aarogya/ribbon/server/DmSkypeIni_Srvlt?user=" + skypeName);
            window.add(skypeFrame);
            window.show();
        }
    };

    private void addSkype() {
        inputBox.addCallBack(inputBoxCallBack);
//        FlowPanel flw = new FlowPanel();
        scriptElement.setSrc("https://swc.cdn.skype.com/sdk/v1/sdk.min.js");
        flwSkypeContainer.getElement().appendChild(scriptElement);
//        flw.setStyleName("skype-button");
//        flw.getElement().setAttribute("data-contact-id", "datamankanpur");
//        flwSkypeContainer.add(flw);

        inputBox.show(aSkype, "Connect Skype", skypeName, 100, "Skype Name", "250px");
    }

    @Override
    public void onBtnClick(ClickEvent event) {
        Object aSource = event.getSource();

        if (aSource.equals(aMoudelName)) {
            setModuleOpen();
        } else if (aSource.equals(ilblLogo)) {
            DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(DmEnum.PCSEnum.DASHBOARD);
        } else if (aSource.equals(aLock)) {
            srvAsyncCommon.invokeRPC(true).reInitialize(DmEnum.PCSEnum.LOCK, new AsyncCallback<DmResult<PCSData>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<PCSData> result) {
                    if (result.isStatus()) {
                        DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(DmEnum.PCSEnum.LOCK);
                    }
                }
            });
        } else if (aSource.equals(aAlert) || aSource.equals(aAlertCount)) {
            setAlertOpen(event.getClientX());
        } else if (aSource.equals(aLogoutIcon)) {
            setLoginUserOpen(event.getClientX());
        } else if (aSource.equals(aLogout)) {
            if (Window.confirm("Are you sure? You want to change logout.")) {
                DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.Login);
            };
        } else if (aSource.equals(aPassChange)) {
            flwLoginUserBox.hide();
            DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").setWidget(new lmUtility().moduleName, new lmUtility().CHANGE_PASSWORD.caption, DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent"));

        } else if (aSource.equals(aPLT)) {
            if (Window.confirm("Are you sure? You want to change company.")) {
                DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.PLT);
            }
        } else if (aSource.equals(aCompany)) {
            if (Window.confirm("Are you sure? You want to change financial year.")) {
                DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.Company);
            }
        } else if (aSource.equals(aBranchName)) {
            if (Window.confirm("Are you sure? You want to change site / branch.")) {
                DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.Site);
            }
        } else if (aSource.equals(repairTransactionDBButton)) {
            repairDBPopUp.show("Transaction");

        } else if (aSource.equals(repairCompanyDBButton)) {
            repairDBPopUp.show("Company");

        } else if (aSource.equals(initializeDefaults)) {
            initializer.show();
        } else if (aSource.equals(repairMenu)) {
            srvAsync.invokeRPC(true).repairMenu(new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {

                    } else {
                        Window.alert(result.getErrorMessage());
                    }
                }
            });
        } else if (aSource.equals(pacHandShakeBtn)) {
            pacHandShake();
        } else if (aSource.equals(localSetting)) {
            localSetting();
        } else if (aSource.equals(aSearch)) {
            aSearch.removeFromParent();
            txtMenuShortHand.setFocus(true);
        } else if (aSource.equals(aSetting)) {
            setRightSliderOpen(event.getClientX());
        } else if (aSource.equals(aSkype)) {
            addSkype();
        }
    }

    private void setRightSliderOpen(int intLeftX) {
        if (flwSetting.isVisible() && flwSetting.isAttached()) {
            flwSetting.hide();
        } else {
            flwSetting.show();
            flwSetting.getElement().setAttribute("Style", "width:20%;background:#fff; right:0px;position:fixed;bottom:0px; z-index:10000000000;top:35px;display:block; border: none;padding:0;box-shadow: 0px 0px 5px #ccc;border-left:1px solid #ccc;");
            DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").showCurrentList(flwSetting, flwCurrentList);
        }
    }

    private void localSetting() {
        try {
            new LocalSetting(DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent"));
        } catch (Exception e) {
            Window.alert(e.getMessage());
        }
    }

    public void setMenu(IsWidget mnbTemp, String moduleName) {
//        flwDownArrow.hide();
        flwMenu.clear();
        flwMenu.add(mnbTemp);
        flwtmpHeader.setHeight("0px");
        txtMenuShortHand.setVisible(true);
        aAlert.setVisible(true);
        aAlertCount.setVisible(true);
        aSkype.setVisible(true);
        flwMenuShortHand.setVisible(true);
        aLock.setVisible(true);

        this.moduleName = moduleName;
        aMoudelName.setText(this.moduleName);
        aMoudelName.setTitle(this.moduleName);

        flwMoudelNameContainer.clear();
        if (!this.moduleName.trim().equalsIgnoreCase("")) {
            flwMoudelNameContainer.add(flwMoudelName);
        }
        flwMoludePopup.hide();

    }

    private void setModuleOpen() {
        if (flwMoludePopup.isVisible() && flwMoludePopup.isAttached()) {
            flwMoludePopup.hide();
        } else {
            flwMoludePopup.show();
            flwMoludePopup.getElement().setAttribute("Style", " z-index:10000000000;top:25px;display:block;");
        }
    }

    private void setAlertOpen(int intLeftX) {
        if (flwAlertBox.isVisible() && flwAlertBox.isAttached()) {
            flwAlertBox.hide();
        } else {
            flwAlertBox.show();
            flwAlertBox.getElement().setAttribute("Style", "left:" + (intLeftX - 240) + "px;  z-index:10000000000; top:25px;display:block; overFlow:auto; padding:0;");
            flwAlertBox.getElement().getStyle().setPosition(Style.Position.FIXED);
        }
    }

    public void showTempAlert(DmNotification_Mdl notification_Mdl) {
        flwShowTempPopup.clear();
        flwShowTempPopup.add(new DmNotificationPopup(notification_Mdl));
        flwShowTempPopup.show();
        flwShowTempPopup.getElement().setAttribute("Style", "right:154px; min-height:40px; z-index:10000000000; top:25px;display:block; padding:0;");
        flwShowTempPopup.getElement().getStyle().setPosition(Style.Position.FIXED);
        flwShowTempPopup.addStyleName("notificationBox");
        timer.schedule(2000);
    }

    private void setLoginUserOpen(final int intLeftX) {
        if (flwLoginUserBox.isVisible() && flwLoginUserBox.isAttached()) {
            flwLoginUserBox.hide();
        } else {
            srvAsync.invokeRPC(true).getUserInfo(DmClient.getLoginUserName(), new AsyncCallback<DmResult<UserInfo_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<UserInfo_Mdl> result) {
                    UserInfo_Mdl userInfo_Mdl = result.getData();
                    if (result.isStatus()) {
                        userNameLabel.setText(DmShared.xNull(userInfo_Mdl.getUserName()));
                        userDescriptionLabel.setText(DmShared.xNull(userInfo_Mdl.getUserDescription()));
                        emailLabel.setText(DmShared.xNull(userInfo_Mdl.getVersionDetail()));
                        buildDetail.setText(DmShared.xNull(userInfo_Mdl.getBuidDetail()));

                        flwLoginUserBox.show();
                        flwLoginUserBox.getElement().setAttribute("Style", "left:" + (intLeftX - 100) + "px;  z-index:10000000000; top:25px;display:block;");
                        flwLoginUserBox.getElement().getStyle().setPosition(Style.Position.FIXED);
                    } else {
                        Window.alert(result.getErrorMessage());
                    }
                }
            });
        }
    }

    public void setOtherVisible(boolean blnVisible) {
        flwInnerForm.removeFromParent();
        if (blnVisible) {
            flwForm.add(flwInnerForm);
            userNameLabel.setText(DmClient.getLoginUserName());
            GWT.runAsync(new RunAsyncCallback() {

                @Override
                public void onFailure(Throwable reason) {
                }

                @Override
                public void onSuccess() {
                    DmBaseURL baseURL = DmClient.getHostPageBaseURL("ws");
                    notificationReceiver = new DmNotificationReceiver(baseURL.getProtocol() + "://" + baseURL.getUrl() + "receiver", new IReceiver() {

                        @Override
                        public void onMessageReceive(String msg) {
                            DmNotification_Mdl notification = null;
                            if (notification != null) {
                                showTempAlert(notification);
                                flwAlertBoxContainer.insert(new DmNotificationPopup(notification), 0);
                            }
                        }

                        @Override
                        public void onClosed(CloseEvent event) {
                        }

                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onOpened() {
                        }
                    });
                }
            });
            if (DmShared.xNull(DmClient.getLoginUserName()).equalsIgnoreCase("SA")
                    || DmShared.xNull(DmClient.getLoginUserName()).equalsIgnoreCase("SUPER")
                    || DmShared.xNull(DmClient.getLoginUserName()).equalsIgnoreCase("")) {

            } else {
                repairTransactionDBButton.removeFromParent();
                repairCompanyDBButton.removeFromParent();
                repairMenu.removeFromParent();
                initializeDefaults.removeFromParent();
            }
        } else {
            userNameLabel.setText("");
        }
        this.flwOther.setVisible(blnVisible);
    }

    public String getPLT() {
        return this.pltName;
    }

    public void setPLT(String strPLT) {
        this.pltName = strPLT;
        aPLT.setText(this.pltName);
        aPLT.setTitle(this.pltName);

        flwPLTContainer.clear();
        if (strPLT.trim().equalsIgnoreCase("")) {
            //flwaPLT.setVisible(false);
        } else {
            //flwaPLT.setVisible(true);
            flwPLTContainer.add(flwaPLT);
        }
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public String getCompanyCode() {
        return this.companyCode;
    }

    public void setCompany(String companyName, String companyCode) {
        this.companyName = companyName;
        this.companyCode = companyCode;
        aCompany.setText(this.companyName);
        aCompany.setTitle(this.companyName);

        flwCompanyContainer.clear();
        if (this.companyName.trim().equalsIgnoreCase("")) {
            //flwaCompany.setVisible(false);
        } else {
            //flwaCompany.setVisible(true);
            flwCompanyContainer.add(flwaCompany);
        }
    }

    public String getBranchName() {
        return this.branchName;
    }

    public void setBranchName(String strBranchName) {
        this.branchName = strBranchName;
        aBranchName.setText(this.branchName);
        aBranchName.setTitle(this.branchName);

        flwBranchNameContainer.clear();
        if (strBranchName.trim().equalsIgnoreCase("")) {
        } else {
            flwBranchNameContainer.add(flwaBranchName);
        }
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public Ribbon(boolean blnWithOtherVisible) {
        super();
        srvAsync = new ServiceInvoker<Ribbon_SrvAsync>(GWT.create(Ribbon_Srv.class), Ribbon.class.getName());
        srvAsyncCommon = new ServiceInvoker<Common_SrvAsync>(GWT.create(Common_Srv.class), Common.class.getName());
        setOtherVisible(blnWithOtherVisible);
        txtMenuShortHand.addKeyDownHandler(this);
        txtMenuShortHand.addKeyPressHandler(this);
        txtMenuShortHand.addBlurHandler(this);
        txtMenuShortHand.setVisible(false);
        aAlert.setVisible(false);
        aSkype.setVisible(false);
        flwMenuShortHand.setVisible(false);
        aAlertCount.setVisible(false);
        aLock.setVisible(false);

        flwMoudelName.addAttachHandler(this);
        viewReportInNewWindow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                DmClient.setViewReportInNewWindow(event.getValue());
            }
        });
        timer = new Timer() {
            public void run() {
                flwShowTempPopup.hide();
            }
        };
        window.setHeight("525px");
        repairDBCallBack();
    }

    private void repairDBCallBack() {
        repairDBPopUp.addCallBack(new RepairDB.CallBack() {

            @Override
            public void onOk(String repairDB, boolean isExhaustiveRepair) {
                if (repairDB.equalsIgnoreCase("Transaction")) {
                    srvAsync.invokeRPC(true).rdsTransaction(getCompanyCode(), isExhaustiveRepair, new AsyncCallback<DmResult<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(DmResult<String> result) {
                            if (result.isStatus()) {

                            } else {
                                Window.alert(result.getErrorMessage());
                            }
                        }
                    });
                } else if (repairDB.equalsIgnoreCase("Company")) {
                    srvAsync.invokeRPC(true).rdsCompany(isExhaustiveRepair, new AsyncCallback<DmResult<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(DmResult<String> result) {
                            if (result.isStatus()) {

                            } else {
                                Window.alert(result.getErrorMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onClose() {

            }
        }
        );
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

//    private void showCamera() {
//        GWT.runAsync(new RunAsyncCallback() {
//
//            @Override
//            public void onFailure(Throwable reason) {
//                new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//            }
//
//            @Override
//            public void onSuccess() {
//                DmClient.setWebCamera(new DmWebCamera());
//                ((DmWebCamera) DmClient.getWebCamera()).setSize("200", "150");
//                ((DmWebCamera) DmClient.getWebCamera()).show();
//            }
//        });
//    }
    private void pacHandShake() {
        STClient.pacHandShake(new STClient.IPACHandShake() {
            @Override
            public void onSuccess() {
            }
        });
//        try {
//            RequestBuilder rBuilder = new RequestBuilder(RequestBuilder.GET, "http://localhost:" + String.valueOf(DmClient.getPACWebPort()) + "/queryClient");
//            rBuilder.sendRequest(null, new RequestCallback() {
//                @Override
//                public void onResponseReceived(Request request, Response response) {
//                    if ((!DmShared.xNull(response.getText()).equalsIgnoreCase("")) && (!DmShared.xNull(response.getText()).equalsIgnoreCase("test"))) {
//                        DmClient.setTcpSessionId(response.getText().toLowerCase().trim());
//                        Window.alert("PAC Handshaked successfully.");
//                    }
//                }
//
//                @Override
//                public void onError(Request request, Throwable exception) {
//                    Window.alert(exception.getMessage());
//                }
//            });
//        } catch (Exception e) {
//            Window.alert(e.getMessage());
//        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (!txtMenuShortHand.getText().equalsIgnoreCase("")) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                getMenuInfo(DmShared.xNull(txtMenuShortHand.getText()));
            }
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();

        if ((charCode > 31) && (charCode < 48)) {
            txtMenuShortHand.cancelKey();
        } else if (charCode > 57 && charCode < 65) {
            txtMenuShortHand.cancelKey();
        } else if (charCode > 90 && charCode < 97) {
            txtMenuShortHand.cancelKey();
        } else if (charCode > 121) {
            txtMenuShortHand.cancelKey();
        }
    }

    private void getMenuInfo(String menuCode) {
        srvAsync.invokeRPC(true).getMenuInfo(menuCode, new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    if (DmShared.xNull(field.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Code doesn't exist.", txtMenuShortHand).show();
                    } else {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(DmShared.xNull(field.getSharedKeyFieldCode()), DmShared.xNull(field.getSharedKeyFieldName()), DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        flwMenuIconStyle.add(aSearch);
        txtMenuShortHand.setText("");
    }

}
