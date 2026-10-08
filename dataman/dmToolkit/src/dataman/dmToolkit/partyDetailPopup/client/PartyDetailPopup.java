package dataman.dmToolkit.partyDetailPopup.client;

import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.partyDetailPopup.shared.*;
import java.util.HashMap;
import java.util.List;

public class PartyDetailPopup extends PartyDetailPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, BlurHandler, AttachEvent.Handler {

    private final ServiceInvoker<PartyDetailPopup_SrvAsync> srvAsync;
    private CallBack callBack;
    private PartyDetailPopup_Mdl partyDetailPopup_Mdl = new PartyDetailPopup_Mdl();
    private int transactionalPartyNatureCode = 0;
    private boolean isOkRequired = false, isNameEditable = false, isTradeNameEditable = false, isAdPlaceOfBusinessEditable = false, isAddressEditable = false, isGstDetailEditable, isPanTanEditable = false, isTransporterIdEditable = false, isOkEequired = false, isPrimaryAddress = false;

    private void checkIsPrimaryAddress() {
        isPrimaryAddress = false;
        if (DmShared.xNull(txtHAdPlaceOfBusiness.getText()).equalsIgnoreCase(DmShared.PRIMARY_ADDRESS_OF_BUSINESS)) {
            isPrimaryAddress = true;
        }
    }

    public interface CallBack {

        public void onOk(int itransactionalPartyNatureCode, PartyDetailPopup_Mdl partyDetailPopup);

        public void onClose(int itransactionalPartyNatureCode);

    }

    public PartyDetailPopup() {
        super();
        srvAsync = new ServiceInvoker<PartyDetailPopup_SrvAsync>(GWT.create(PartyDetailPopup_Srv.class), PartyDetailPopup.class.getName());
        manageEvents();
        textHelpEvents();
    }

    private void clear() {
        txtPartyName.setTag("");
        txtPartyName.setTag2nd("");
        txtPartyName.setText("");

        txtDisplayName.setTag("");
        txtDisplayName.setTag2nd("");
        txtDisplayName.setText("");

        txtLegalName.setTag("");
        txtLegalName.setTag2nd("");
        txtLegalName.setText("");

        txtTradeName.setTag("");
        txtTradeName.setTag2nd("");
        txtTradeName.setText("");

        txtGstin.setTag("");
        txtGstin.setTag2nd("");
        txtGstin.setText("");

        txtGstinDate.setTag("");
        txtGstinDate.setTag2nd("");
        txtGstinDate.setText("");

        txtTransporterId.setTag("");
        txtTransporterId.setTag2nd("");
        txtTransporterId.setText("");

        txtHAdPlaceOfBusiness.setTag("");
        txtHAdPlaceOfBusiness.setText("");
        resetAdPlaceOfBusinessDetail();

        txtPAN.setTag("");
        txtPAN.setTag2nd("");
        txtPAN.setText("");

        txtTAN.setTag("");
        txtTAN.setTag2nd("");
        txtTAN.setText("");

    }

    private void resetAdPlaceOfBusinessDetail() {
        txtHAdPlaceOfBusiness.setTag2nd("");

        txtAddress1.setTag("");
        txtAddress1.setTag2nd("");
        txtAddress1.setText("");

        txtAddress2.setTag("");
        txtAddress2.setTag2nd("");
        txtAddress2.setText("");

        txtHCity.setTag("");
        txtHCity.setText("");
        resetCity();

        txtMobileNo.setTag("");
        txtMobileNo.setTag2nd("");
        txtMobileNo.setText("");

        txtEMail.setTag("");
        txtEMail.setTag2nd("");
        txtEMail.setText("");
    }

    private void resetCity() {
        txtHCity.setTag2nd("");

        txtState.setText("");
        txtState.setTag("");
        txtState.setTag2nd("");

        txtStateNumericCode.setText("");
        txtStateNumericCode.setTag("");
        txtStateNumericCode.setTag2nd("");

        txtDistrict.setText("");
        txtDistrict.setTag("");
        txtDistrict.setTag2nd("");

        txtCountry.setText("");
        txtCountry.setTag("");
        txtCountry.setTag2nd("");

        txtGstCountryCode.setText("");
        txtGstCountryCode.setTag("");
        txtGstCountryCode.setTag2nd("");

        txtPinCode.setTag("");
        txtPinCode.setTag2nd("");
        txtPinCode.setText("");
    }

    private void displayData() {
        clear();
        if (partyDetailPopup_Mdl != null && !DmShared.xNull(partyDetailPopup_Mdl.getSubCode()).trim().equalsIgnoreCase("")) {

            txtPartyName.setText(DmShared.xNull(partyDetailPopup_Mdl.getName()));

            txtDisplayName.setText(DmShared.xNull(partyDetailPopup_Mdl.getDisplayName()));
            txtLegalName.setText(DmShared.xNull(partyDetailPopup_Mdl.getLegalName()));
            txtTradeName.setText(DmShared.xNull(partyDetailPopup_Mdl.getTradeName()));

            txtHAdPlaceOfBusiness.setTag(DmShared.xNull(partyDetailPopup_Mdl.getAdPlaceOfBusinessSNo()));
            txtHAdPlaceOfBusiness.setTag2nd(DmShared.xNull(partyDetailPopup_Mdl.getAdPlaceOfBusinessSNo()));
            txtHAdPlaceOfBusiness.setText(DmShared.xNull(partyDetailPopup_Mdl.getAdPlaceOfBusinessName()));

            txtAddress1.setText(DmShared.xNull(partyDetailPopup_Mdl.getAddress1()));
            txtAddress2.setText(DmShared.xNull(partyDetailPopup_Mdl.getAddress2()));

            txtHCity.setTag(DmShared.xNull(partyDetailPopup_Mdl.getCityCode()));
            txtHCity.setTag2nd(DmShared.xNull(partyDetailPopup_Mdl.getCityCode()));
            txtHCity.setText(DmShared.xNull(partyDetailPopup_Mdl.getCityName()));

            txtDistrict.setTag(DmShared.xNull(partyDetailPopup_Mdl.getDistrictCode()));
            txtDistrict.setText(DmShared.xNull(partyDetailPopup_Mdl.getDistrictName()));

            txtState.setTag(DmShared.xNull(partyDetailPopup_Mdl.getStateCode()));
            txtState.setText(DmShared.xNull(partyDetailPopup_Mdl.getStateName()));
            txtState.setTag2nd(DmShared.xNull(partyDetailPopup_Mdl.getStateNumericCode()));

            txtStateNumericCode.setText(DmShared.xNull(partyDetailPopup_Mdl.getStateNumericCode()));
            txtGstCountryCode.setText(DmShared.xNull(partyDetailPopup_Mdl.getGstCountryCode()));

            txtCountry.setTag(DmShared.xNull(partyDetailPopup_Mdl.getCountryCode()));
            txtCountry.setText(DmShared.xNull(partyDetailPopup_Mdl.getCountryName()));
            txtCountry.setTag2nd(DmShared.xNull(partyDetailPopup_Mdl.getGstCountryCode()));

            txtTransporterId.setText(DmShared.xNull(partyDetailPopup_Mdl.getTransporterId()));

            txtGstin.setText(DmShared.xNull(partyDetailPopup_Mdl.getGstin()));
            txtGstinDate.setText(DmShared.dNull(partyDetailPopup_Mdl.getGstinDate()));

            txtPinCode.setText(DmShared.xNull(partyDetailPopup_Mdl.getPinCode()));

            txtMobileNo.setText(DmShared.xNull(partyDetailPopup_Mdl.getMobile()));
            txtPhoneNo.setText(DmShared.xNull(partyDetailPopup_Mdl.getPhone()));
            txtEMail.setText(DmShared.xNull(partyDetailPopup_Mdl.getEmail()));
            txtPAN.setText(DmShared.xNull(partyDetailPopup_Mdl.getPan()));
            txtTAN.setText(DmShared.xNull(partyDetailPopup_Mdl.getTan()));

        }
        checkIsPrimaryAddress();
    }

    private void manageDisplay() {
        txtPartyName.setEnabled(isNameEditable && isOkRequired);
        txtLegalName.setEnabled(isGstDetailEditable && isOkRequired);
        txtDisplayName.setEnabled(isGstDetailEditable && isOkRequired);
        txtTradeName.setEnabled(isTradeNameEditable && isOkRequired);
        txtGstin.setEnabled(isGstDetailEditable && isOkRequired);
        txtGstinDate.setEnabled(isGstDetailEditable && isOkRequired);
        txtTransporterId.setEnabled(isTransporterIdEditable && isOkRequired);
        txtHAdPlaceOfBusiness.setEnabled(isAdPlaceOfBusinessEditable && isOkRequired);
        txtAddress1.setEnabled(isAddressEditable && isOkRequired);
        txtAddress2.setEnabled(isAddressEditable && isOkRequired);
        txtHCity.setEnabled(isAddressEditable && isOkRequired);

        txtDistrict.setEnabled(false);
        txtState.setEnabled(false);
        txtCountry.setEnabled(false);
        txtStateNumericCode.setEnabled(false);
        txtGstCountryCode.setEnabled(false);

        txtPinCode.setEnabled(isAddressEditable && isOkRequired);
        txtMobileNo.setEnabled(isAddressEditable && isOkRequired);
        txtPhoneNo.setEnabled(isAddressEditable && isOkRequired);
        txtEMail.setEnabled(isAddressEditable && isOkRequired);
        txtPAN.setEnabled(isPanTanEditable && isOkRequired);
        txtTAN.setEnabled(isPanTanEditable && isOkRequired);
    }

    private void textHelpEvents() {
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(DmShared.xNull(txtGstin.getText()), DmShared.xNull(txtStateNumericCode.getText()), DmShared.xNull(partyDetailPopup_Mdl.getSubCode()), DmShared.xNull(partyDetailPopup_Mdl.getSiteCode()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

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
                if (!DmShared.xNull(txtHCity.getTag()).equalsIgnoreCase(txtHCity.getTag2nd())) {
                    resetCity();
                    txtHCity.setTag2nd(DmShared.xNull(txtHCity.getTag()));
                }
                if (blnStatus && tRow != null) {

                    txtState.setText(DmShared.xNull(tRow.getStateName()));
                    txtState.setTag(DmShared.xNull(tRow.getStateCode()));

                    txtStateNumericCode.setText(DmShared.xNull(tRow.getStateNumericCode()));

                    txtDistrict.setTag(DmShared.xNull(tRow.getDistrictCode()));
                    txtDistrict.setText(DmShared.xNull(tRow.getDistrictName()));

                    txtCountry.setTag(DmShared.xNull(tRow.getCountryCode()));
                    txtCountry.setText(DmShared.xNull(tRow.getCountryName()));

                    txtGstCountryCode.setText(DmShared.xNull(tRow.getGstCountryCode()));
                }
            }
        });
        txtHAdPlaceOfBusiness.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAdPlaceOfBusiness_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAdPlaceOfBusiness(DmShared.xNull(partyDetailPopup_Mdl.getSubCode()), DmShared.xNull(partyDetailPopup_Mdl.getSiteCode()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAdPlaceOfBusiness_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAdPlaceOfBusiness).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAdPlaceOfBusiness_Mdl>> result) {
                        try {
                            if (result.isStatus()) {
                                txtHAdPlaceOfBusiness.setData(result.getData(), result.getPaging());
                            }
                        } catch (Exception e) {
                            new DmAlertMessageBox(e.getMessage(), txtHAdPlaceOfBusiness).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAdPlaceOfBusiness_Mdl t1Row) {
                if (!DmShared.xNull(txtHAdPlaceOfBusiness.getTag()).equalsIgnoreCase(txtHAdPlaceOfBusiness.getTag2nd())) {
                    resetAdPlaceOfBusinessDetail();
                    txtHAdPlaceOfBusiness.setTag2nd(DmShared.xNull(txtHAdPlaceOfBusiness.getTag()));
                }
                if (isPrimaryAddress) {
                    resetAdPlaceOfBusinessDetail();
                    txtHAdPlaceOfBusiness.setTag2nd(DmShared.xNull(txtHAdPlaceOfBusiness.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    txtAddress1.setText(DmShared.xNull(t1Row.getAddress1()));
                    txtAddress2.setText(DmShared.xNull(t1Row.getAddress2()));
                    txtHCity.setTag(DmShared.xNull(t1Row.getCityCode()));
                    txtHCity.setTag2nd(DmShared.xNull(t1Row.getCityCode()));
                    txtHCity.setText(DmShared.xNull(t1Row.getCityName()));
                    txtMobileNo.setText(DmShared.xNull(t1Row.getMobile()));
                    txtEMail.setText(DmShared.xNull(t1Row.getEmail()));
                    txtState.setText(DmShared.xNull(t1Row.getStateName()));
                    txtState.setTag(DmShared.xNull(t1Row.getStateCode()));
                    txtStateNumericCode.setText(DmShared.xNull(t1Row.getStateNumericCode()));
                    txtDistrict.setTag(DmShared.xNull(t1Row.getDistrictCode()));
                    txtDistrict.setText(DmShared.xNull(t1Row.getDistrictName()));
                    txtCountry.setTag(DmShared.xNull(t1Row.getCountryCode()));
                    txtCountry.setText(DmShared.xNull(t1Row.getCountryName()));
                    txtGstCountryCode.setText(DmShared.xNull(t1Row.getGstCountryCode()));
                    txtPinCode.setText(DmShared.xNull(t1Row.getPin()));
                }
                checkIsPrimaryAddress();
            }
        });
    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        pnlPopUp.addAttachHandler(this);
        txtPartyName.addBlurHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    public void show(Widget widget, final int transactionalPartyNatureCode, final PartyDetailPopup_Mdl partyDetailPopup_Mdl, final HashMap paramHashMap) {

        this.transactionalPartyNatureCode = transactionalPartyNatureCode;
        this.partyDetailPopup_Mdl = partyDetailPopup_Mdl;
        initializeControl(paramHashMap);

        manageTopControl();
        displayData();
        manageDisplay();

        pnlPopUp.setGlassEnabled(true);
        pnlPopUp.showRelativeTo(widget);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                if (isNameEditable) {
                    txtPartyName.setFocus(true);
                } else if (isGstDetailEditable) {
                    txtLegalName.setFocus(true);
                } else if (isTradeNameEditable) {
                    txtTradeName.setFocus(true);
                } else if (isTransporterIdEditable) {
                    txtTransporterId.setFocus(true);
                } else if (isAdPlaceOfBusinessEditable) {
                    txtHAdPlaceOfBusiness.setFocus(true);
                } else if (isAddressEditable) {
                    txtAddress1.setFocus(true);
                } else if (isPanTanEditable) {
                    txtPAN.setFocus(true);
                }
            }
        });
    }

    private void manageTopControl() {
        toolContainer.setTitle(DmEnum.PartyTransRecordType.getValue(transactionalPartyNatureCode) != null ? DmEnum.PartyTransRecordType.getValue(transactionalPartyNatureCode).getName() : widgetName);
        flwTopContainer.clear();
        if (isOkRequired) {
            flwTopContainer.add(flwOk);
        }
        flwTopContainer.add(flwCancel);
    }

    public void hide() {
        hide(false, null);
    }

    public void hide(boolean isCallEvent, PartyDetailPopup_Mdl mrTemp) {
        this.pnlPopUp.hide();

        if (isCallEvent) {
            this.callBack.onClose(transactionalPartyNatureCode);
        }
        if (mrTemp != null) {
            this.callBack.onOk(transactionalPartyNatureCode, mrTemp);
        }

    }

    @Override
    public Widget asWidget() {
        return pnlPopUp;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            this.hide(true, null);
        } else if (event.getSource().equals(btnOk)) {
            manageOk();
        }
    }

    private PartyDetailPopup_Mdl fillRecords(PartyDetailPopup_Mdl partyDetailPopup) {

        partyDetailPopup.setSharedKeyFieldName(DmShared.iNull(transactionalPartyNatureCode).toString());
        partyDetailPopup.setName(DmShared.xNull(txtPartyName.getText()));
        partyDetailPopup.setLegalName(DmShared.xNull(txtLegalName.getText()));
        partyDetailPopup.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        partyDetailPopup.setTradeName(DmShared.xNull(txtTradeName.getText()));
        partyDetailPopup.setAdPlaceOfBusinessSNo(DmShared.xNull(txtHAdPlaceOfBusiness.getTag()));
        partyDetailPopup.setAdPlaceOfBusinessName(DmShared.xNull(txtHAdPlaceOfBusiness.getText()));
        partyDetailPopup.setAddress1(DmShared.xNull(txtAddress1.getText()));
        partyDetailPopup.setAddress2(DmShared.xNull(txtAddress2.getText()));
        partyDetailPopup.setCityCode(DmShared.xNull(txtHCity.getTag()));
        partyDetailPopup.setCityName(DmShared.xNull(txtHCity.getText()));
        partyDetailPopup.setDistrictCode(DmShared.xNull(txtDistrict.getTag()));
        partyDetailPopup.setDistrictName(DmShared.xNull(txtDistrict.getText()));
        partyDetailPopup.setStateCode(DmShared.xNull(txtState.getTag()));
        partyDetailPopup.setStateName(DmShared.xNull(txtState.getText()));
        partyDetailPopup.setStateNumericCode(DmShared.xNull(txtStateNumericCode.getText()));
        partyDetailPopup.setCountryCode(DmShared.xNull(txtCountry.getTag()));
        partyDetailPopup.setCountryName(DmShared.xNull(txtCountry.getText()));
        partyDetailPopup.setGstCountryCode(DmShared.xNull(txtGstCountryCode.getText()));
        partyDetailPopup.setGstin(DmShared.xNull(txtGstin.getText()));
        partyDetailPopup.setGstinDate(DmShared.dNull(txtGstinDate.getText()));
        partyDetailPopup.setPinCode(DmShared.xNull(txtPinCode.getText()));
        partyDetailPopup.setMobile(DmShared.xNull(txtMobileNo.getText()));
        partyDetailPopup.setPhone(DmShared.xNull(txtPhoneNo.getText()));
        partyDetailPopup.setEmail(DmShared.xNull(txtEMail.getText()));
        partyDetailPopup.setPan(DmShared.xNull(txtPAN.getText()));
        partyDetailPopup.setTan(DmShared.xNull(txtTAN.getText()));
        partyDetailPopup.setTransporterId(DmShared.xNull(txtTransporterId.getText()));

        return partyDetailPopup;
    }

    private void manageOk() {

        if (isOkRequired) {
            final PartyDetailPopup_Mdl mrTemp = partyDetailPopup_Mdl;
            try {
                fillRecords(mrTemp);
                PartyDetailPopup_Vld.chkSave(mrTemp);
                hide(false, mrTemp);
            } catch (Exception ex) {
                if (ex instanceof DmException) {
                    saveError(DmShared.iNull(((DmException) ex).getErrorNo()).intValue(), DmShared.xNull(((DmException) ex).getMessage()));
                } else {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }

            }
        }
    }

    private void saveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtPartyName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHCity).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtMobileNo).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtEMail).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtPartyName).show();
                break;
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true, null);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            manageOk();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            this.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void initializeControl(HashMap paramHashMap) {

        if (paramHashMap != null && callBack != null) {
            if (paramHashMap.containsKey("IS_NAME_EDITABLE")) {
                isNameEditable = (Boolean) paramHashMap.get("IS_NAME_EDITABLE");
            } else {
                isNameEditable = false;
            }
            if (paramHashMap.containsKey("IS_ADDITIONAL_PLACE_OF_BUSINESS_EDITABLE")) {
                isAdPlaceOfBusinessEditable = (Boolean) paramHashMap.get("IS_ADDITIONAL_PLACE_OF_BUSINESS_EDITABLE");
            } else {
                isAdPlaceOfBusinessEditable = false;
            }
            if (paramHashMap.containsKey("IS_ADDRESS_EDITABLE")) {
                isAddressEditable = (Boolean) paramHashMap.get("IS_ADDRESS_EDITABLE");
            } else {
                isAddressEditable = false;
            }
            if (paramHashMap.containsKey("IS_GST_DETAIL_EDITABLE")) {
                isGstDetailEditable = (Boolean) paramHashMap.get("IS_GST_DETAIL_EDITABLE");
            } else {
                isGstDetailEditable = false;
            }
            if (paramHashMap.containsKey("IS_TRADE_NAME_EDITABLE")) {
                isTradeNameEditable = (Boolean) paramHashMap.get("IS_TRADE_NAME_EDITABLE");
            } else {
                isTradeNameEditable = false;
            }
            if (paramHashMap.containsKey("IS_TRANSPORTER_ID_EDITABLE")) {
                isTransporterIdEditable = (Boolean) paramHashMap.get("IS_TRANSPORTER_ID_EDITABLE");
            } else {
                isTransporterIdEditable = false;
            }
            if (paramHashMap.containsKey("IS_PAN_TAN_EDITABLE")) {
                isPanTanEditable = (Boolean) paramHashMap.get("IS_PAN_TAN_EDITABLE");
            } else {
                isPanTanEditable = false;
            }
            if (paramHashMap.containsKey("IS_OK_REQUIRED")) {
                isOkRequired = (Boolean) paramHashMap.get("IS_OK_REQUIRED");
            } else {
                isOkRequired = false;
            }
        }

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtPartyName)) {
            if (!DmShared.xNull(txtPartyName.getText()).trim().equalsIgnoreCase("") 
                    && DmShared.xNull(txtDisplayName.getText()).trim().equalsIgnoreCase("")) {
                 txtDisplayName.setText(txtPartyName.getText());
            }

        }
    }
}
