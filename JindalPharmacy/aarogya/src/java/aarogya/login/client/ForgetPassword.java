package aarogya.login.client;

import aarogya.login.shared.ForgetPassword_Mdl;
import aarogya.login.shared.Login_Srv;
import aarogya.login.shared.Login_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class ForgetPassword extends ForgetPassword_Dsg implements DmRootEvents, AttachEvent.Handler, ClickHandler {

    interface CallBack {

        public void onClose();
    }

    private final ServiceInvoker<Login_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void txtHelpEvents() {
        txtHPltCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPltCode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPltCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPltCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPltCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                txtHFinancialYear.setTag("");
                txtHFinancialYear.setText("");
            }
        });
        txtHFinancialYear.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFinancialYear(DmShared.xNull(txtHPltCode.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFinancialYear).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFinancialYear.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFinancialYear).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    public void show() {
        popupPanel.setGlassEnabled(true);
        popupPanel.show();
        popupPanel.center();

        clear();
        defaultValues();
        txtUserName.setFocus(true);
    }

    public ForgetPassword() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(Login_Srv.class), Login.class.getName());
        txtHelpEvents();
        manageEvent();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);

        } else if (event.getSource() == btnSubmit) {
            check();
        }
    }

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
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            check();
        }
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean isCallbackNeeded) {
        if (isCallbackNeeded) {
            callBack.onClose();
        }
        this.popupPanel.hide();
    }

    private void check() {
        try {
            if (DmShared.iNull(txtHPltCode.getTag()).intValue() == 0) {
                new DmAlertMessageBox("Product Licesed To is mandatory.", txtHPltCode).show();
                return;
            } else if (DmShared.iNull(txtHFinancialYear.getTag()).intValue() == 0) {
                new DmAlertMessageBox("Financial Year is mandatory.", txtHFinancialYear).show();
                return;
            } else if (DmShared.xNull(txtUserName.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("User Name is mandatory.", txtUserName).show();
                return;
            } else if (DmShared.xNull(txtMobile.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Mobile is mandatory.", txtMobile).show();
                return;
            } else if (DmShared.xNull(txtEmail.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Email is mandatory.", txtEmail).show();
                return;
            } else if (!DmShared.xNull(txtEmail.getText().trim()).equals("")) {
                DmShared.isValidEMail(txtEmail.getText(), 500);
            }

            ForgetPassword_Mdl mrTemp = new ForgetPassword_Mdl();
            mrTemp.setPltCode(DmShared.xNull(txtHPltCode.getTag()));
            mrTemp.setPltName(DmShared.xNull(txtHPltCode.getText()));
            mrTemp.setCompanyCode(DmShared.xNull(txtHFinancialYear.getTag()));
            mrTemp.setFyName(DmShared.xNull(txtHFinancialYear.getText()));
            mrTemp.setUserName(DmShared.xNull(txtUserName.getText()).trim());
            mrTemp.setEmail(DmShared.xNull(txtEmail.getText()).trim());
            mrTemp.setMobile(DmShared.xNull(txtMobile.getText()).trim());

            srvAsync.invokeRPC().forgetPassword(mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Reset password link has been sent to your registered email Id.", txtUserName).show();
                        hide(true);
                    } else {
                        saveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            saveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void saveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHPltCode).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHFinancialYear).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtUserName).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtUserName).show();
                break;
        }
    }

    private void defaultValues() {
        try {
            srvAsync.invokeRPC().getDefaultValues(new AsyncCallback<DmResult<ForgetPassword_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtUserName).show();
                }

                @Override
                public void onSuccess(DmResult<ForgetPassword_Mdl> result) {
                    if (result.isStatus()) {
                        clearDefault();
                        ForgetPassword_Mdl data = result.getData();
                        txtHPltCode.setTag(DmShared.xNull(data.getPltCode()));
                        txtHPltCode.setText(DmShared.xNull(data.getPltName()));
                        txtHFinancialYear.setTag(DmShared.xNull(data.getCompanyCode()));
                        txtHFinancialYear.setText(DmShared.xNull(data.getFyName()));
                        txtUserName.setFocus(true);
                    }
                }
            });

        } catch (DmException e) {
            new DmAlertMessageBox(e.getErrorSource(), (DmTextBox) null).show();
        }

    }

    private void manageEvent() {
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnSubmit.addClickHandler(this);
    }

    private void clearDefault() {
        txtHPltCode.setTag("");
        txtHPltCode.setText("");
        txtHFinancialYear.setTag("");
        txtHFinancialYear.setText("");
        clear();
    }

    private void clear() {
        txtUserName.setText("");
        txtMobile.setText("");
        txtEmail.setText("");
    }
}
