package aarogya.login.client;

import aarogya.login.shared.Initialze_Mdl;
import aarogya.login.shared.Login_Model;
import aarogya.login.shared.Login_Srv;
import aarogya.login.shared.Login_SrvAsync;
import aarogya.login.shared.Login_Validation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmInputBox.client.DmInputBox;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.PCSData;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class Login extends Login_Dsg implements IsWidget, AttachEvent.Handler {

    ServiceInvoker<Login_SrvAsync> SrvAsync;
    DmInputBox inputBox = new DmInputBox();
    boolean isAarogyaLite;
    private final ForgetPassword forgePass = new ForgetPassword();

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            txtUserName.setFocus(true);
        }
    }

    @Override
    public void onBtnClick(ClickEvent event) {
        Button btnClicked = (Button) event.getSource();

        if (btnClicked.equals(btnNext)) {
            validateLogin();
        }
    }

    public Login(boolean isAarogyaLite) {
        super();
        SrvAsync = new ServiceInvoker<Login_SrvAsync>(GWT.create(Login_Srv.class), Login.class.getName());
        this.isAarogyaLite = isAarogyaLite;
        flwForm.addAttachHandler(this);
        clicklbl();
//        repairCompany.setVisible(false);
//        if (!this.isAarogyaLite) {
//            repairCompany.setVisible(true);
//            repairCompany.addClickHandler(this);
//
//            inputBox.addCallBack(new DmInputBox.CallBack() {
//
//                @Override
//                public void onOk(String inputValue, String parentName) {
//                    SrvAsync.invokeRPC(true).validateDBPassword(inputValue, new AsyncCallback<DmResult<Boolean>>() {
//
//                        @Override
//                        public void onFailure(Throwable caught) {
////                            DmRequestBuilder.flushSessionId();
//                            Window.alert(caught.getMessage());
//                        }
//
//                        @Override
//                        public void onSuccess(DmResult<Boolean> result) {
//                            if (result.isStatus()) {
//                                DmClient.<aarogya.ribbon.client.Ribbon>getStoreItem("Ribbon").setOtherVisible(true);
//                            } else {
////                                DmRequestBuilder.flushSessionId();
//                                Window.alert(result.getErrorMessage());
//                            }
//                        }
//                    });
//                }
//            });
//        }

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void validateLogin() {
        Login_Model.Body bTemp;

        try {
            bTemp = initializeValidateLogin();
            Login_Validation.chkLogin(bTemp);

            if (DmShared.xNull(bTemp.getUserName()).equalsIgnoreCase("Repair Company")) {
                repairCompanyUserLogin(bTemp);
            } else {
                normalUserLogin(bTemp);
            }
        } catch (Exception e) {
            Window.alert(e.getMessage());
        }
    }

    private void repairCompanyUserLogin(Login_Model.Body bTemp) {
        SrvAsync.invokeRPC(true).validateDBPassword(bTemp.getPassword(), new AsyncCallback<DmResult<Boolean>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<Boolean> result) {
                if (result.isStatus()) {
                    DmClient.<aarogya.ribbon.client.Ribbon>getStoreItem("Ribbon").setOtherVisible(true);
                } else {
                    Window.alert(result.getErrorMessage());
                }
            }
        });
    }

    private void normalUserLogin(Login_Model.Body bTemp) {
        SrvAsync.invokeRPC(true).validateUser(bTemp, new AsyncCallback<DmResult<PCSData>>() {

            @Override
            public void onFailure(Throwable caught) {
                DmRequestBuilder.flushSessionId();
                txtUserName.setFocus(true);
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<PCSData> result) {
                if (result.isStatus()) {
                    DmClient.setLoginDate(result.getData().getLoginDate());
                    DmClient.setLoginUserName(result.getData().getUserName());

                    if (isAarogyaLite) {
//                        DmClient.<aarogyaLite.ribbon.client.Ribbon>getStoreItem("Ribbon").setOtherVisible(true);
//                        DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").next(PCSEnum.Site, result.getData());
                    } else {
                        DmClient.<aarogya.ribbon.client.Ribbon>getStoreItem("Ribbon").setOtherVisible(true);
                        DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").next(PCSEnum.PLT, result.getData());
                    }

                    initialize();

                } else {
                    DmRequestBuilder.flushSessionId();
                    txtUserName.setFocus(true);
                    Window.alert(result.getErrorMessage());
                }
            }
        });
    }

    private void initialize() {
        SrvAsync.invokeRPC(true).initialize(new AsyncCallback<DmResult<Initialze_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<Initialze_Mdl> result) {
                if (result.isStatus()) {
                    DmShared.mobileDigit = result.getData().getMobileDigit();
                }
            }
        });
    }

    private Login_Model.Body initializeValidateLogin() {
        Login_Model.Body mrTemp = new Login_Model.Body();

        mrTemp.setUserName(txtUserName.getText());
        mrTemp.setPassword(txtpass.getText());

        return mrTemp;
    }

    private void clicklbl() {
        lblForgetPassword.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                forgePass.show();
            }
        });
        forgePass.addCallBack(new ForgetPassword.CallBack() {
            @Override
            public void onClose() {
                txtUserName.setFocus(true);
            }
        });
    }
//    @Override
//    public void onClick(ClickEvent event) {
//        inputBox.show(repairCompany, "repairCompany", "", 20, "Enter Password", "");
//    }
}
