package aarogya.pharmacy.ledgerPopup.client;

import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_Mdl;
import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_Srv;
import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_SrvAsync;
import aarogya.pharmacy.ledgerPopup.shared.LedgerPopup_Vld;
import aarogya.pharmacy.ledgerPopup.shared.TxtHAccountGroup_Mdl;
import aarogya.pharmacy.ledgerPopup.shared.TxtHCity_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class LedgerPopup extends LedgerPopup_Dsg implements IsWidget, ClickHandler, BlurHandler, DmRootEvents, AttachEvent.Handler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    public interface CallBack {

        public void save(LedgerPopup_Mdl lpMdl);

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private final ServiceInvoker<LedgerPopup_SrvAsync> srvAsync;

    public LedgerPopup() {
        super();
        srvAsync = new ServiceInvoker<LedgerPopup_SrvAsync>(GWT.create(LedgerPopup_Srv.class), LedgerPopup.class.getName());
        textHelpEvents();
        handlerEvents();
    }

    public void show() {
        clear();
        srvAsync.invokeRPC(true).getDefaltValues(new AsyncCallback<DmResult<LedgerPopup_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(DmResult<LedgerPopup_Mdl> result) {
                if (result.isStatus()) {
                    LedgerPopup_Mdl lpMdl = result.getData();
                    txtHAccountGroup.setText(lpMdl.getAcGroupName());
                    txtHAccountGroup.setTag(lpMdl.getAcGroupCode());
                    txtHAccountGroup.setTag2nd(lpMdl.getAcGroupCode());
                    txtHNature.setText(lpMdl.getNatureName());
                    txtHNature.setTag(lpMdl.getNatureCode());

                    popupPanel.show();
                    popupPanel.center();
                    popupPanel.setGlassEnabled(true);
                    txtName.setFocus(true);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        txtHAccountGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountGroup_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(txtHAccountGroup.getTag2nd(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountGroup_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountGroup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccountGroup_Mdl tRow) {
                if (blnStatus) {
                    txtHNature.setTag(tRow.getNatureCode());
                    txtHNature.setText(tRow.getNatureName());
                }
            }
        });
        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNature(txtHAccountGroup.getTag2nd(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHCity.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtState.setText(tRow.getStateName());
                    txtState.setTag(tRow.getStateCode());
                    txtState.setTag2nd(tRow.getStateNumericCode());
                    txtState.setEnabled(false);
                } else {
                    txtState.setText("");
                    txtState.setTag("");
                }
            }
        });
        txtHPartyType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPartyType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPartyType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHPartyType.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHPartyType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
    }

    private void handlerEvents() {
        popupPanel.addAttachHandler(this);
        txtGstinDate.addBlurHandler(this);
        btnSave.addClickHandler(this);
        btnCancel.addClickHandler(this);
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.close();
    }

    private LedgerPopup_Mdl saveInitialize() {
        LedgerPopup_Mdl mrTemp = new LedgerPopup_Mdl();
        mrTemp.setSharedKeyFieldName(txtName.getText());
        mrTemp.setAcGroupCode(txtHAccountGroup.getTag());
        mrTemp.setAcGroupName(txtHAccountGroup.getText());
        mrTemp.setNatureCode(txtHNature.getTag());
        mrTemp.setNatureName(txtHNature.getText());
        mrTemp.setAddress1(txtAddress1.getText());
        mrTemp.setAddress2(txtAddress2.getText());
        mrTemp.setCityCode(txtHCity.getTag());
        mrTemp.setCityName(txtHCity.getText());
        mrTemp.setStateCode(txtState.getTag());
        mrTemp.setStateName(txtState.getText());
        mrTemp.setStateNumericCode(txtState.getTag2nd());
        mrTemp.setMobile(txtMobileNo.getText());
        mrTemp.setPan(txtPAN.getText());
        mrTemp.setAadhaar(txtAadhar.getText());
        mrTemp.setPartyType(txtHPartyType.getTag());
        mrTemp.setGstin(txtGstin.getText());
        mrTemp.setGstinDate(txtGstinDate.getText());
        mrTemp.setCurrentDate(DmClient.getLoginDate());
        return mrTemp;
    }

    private void save() {
        try {
            LedgerPopup_Mdl mrTemp = saveInitialize();
            LedgerPopup_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC(true).saveRecord(mrTemp, new AsyncCallback<DmResult<LedgerPopup_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<LedgerPopup_Mdl> result) {
                    if (result.isStatus()) {
                        callBack.save(result.getData());
                        popupPanel.hide();
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
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHAccountGroup).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHNature).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHCity).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtGstin).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtGstinDate).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtAadhar).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtPAN).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHPartyType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }
    }

    private void clear() {
        txtName.setText("");

        txtHAccountGroup.setText("");
        txtHAccountGroup.setTag("");
        txtHAccountGroup.setTag2nd("");

        txtHNature.setText("");
        txtHNature.setTag("");
        txtHNature.setTag2nd("");

        txtAddress1.setText("");
        txtAddress1.setTag("");
        txtAddress1.setTag2nd("");

        txtAddress2.setText("");
        txtAddress2.setTag("");
        txtAddress2.setTag2nd("");

        txtHCity.setText("");
        txtHCity.setTag("");
        txtHCity.setTag2nd("");

        txtState.setText("");
        txtState.setTag("");
        txtState.setTag2nd("");

        txtMobileNo.setText("");
        txtMobileNo.setTag("");
        txtMobileNo.setTag2nd("");

        txtPAN.setText("");
        txtPAN.setTag("");
        txtPAN.setTag2nd("");

        txtGstin.setText("");
        txtGstin.setTag("");
        txtGstin.setTag2nd("");

        txtGstinDate.setText("");
        txtGstinDate.setTag("");
        txtGstinDate.setTag2nd("");

        txtAadhar.setText("");
        txtAadhar.setTag("");
        txtAadhar.setTag2nd("");
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S) {
            if (btnSave.isAttached() && btnSave.isVisible() && btnSave.isEnabled()) {
                save();
            }
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
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnSave)) {
            save();
        } else if (event.getSource().equals(btnCancel)) {
            hide();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtGstinDate) {
            txtGstinDate.setText(DmClient.returnDate(txtGstinDate.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

}
