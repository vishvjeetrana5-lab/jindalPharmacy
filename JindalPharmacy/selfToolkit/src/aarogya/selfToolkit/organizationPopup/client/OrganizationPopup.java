package aarogya.selfToolkit.organizationPopup.client;

import aarogya.selfToolkit.organizationPopup.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class OrganizationPopup extends OrganizationPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void ok(Organization_Mdl ctMdl);

        public void close();
    }

    private CallBack callBack;
    private final ServiceInvoker<OrganizationPopup_SrvAsync> srvAsync;
    private boolean isTPA = false;
    private String voucherDate = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHRelationCTO.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRelation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRelationCTO).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRelationCTO.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRelationCTO).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHInsuranceCompany.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInsuranceCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInsuranceCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInsuranceCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInsuranceCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHCorporateEmployee.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCorporateEmployee_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCorporate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCorporateEmployee_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCorporateEmployee).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCorporateEmployee_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCorporateEmployee.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCorporateEmployee).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCorporateEmployee_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtCardHolderNameCTO.setText(t1Row.getCardHolderName());
                    txtCardNoCTO.setText(t1Row.getPolicyNo());
                    txtHRelationCTO.setText(t1Row.getRelationShip());
                    txtHRelationCTO.setTag(t1Row.getRelationShip());
                    txtRankNo.setTag(t1Row.getRank());
                } else {
                    txtCardHolderNameCTO.setText("");
                    txtCardNoCTO.setText("");
                    txtHRelationCTO.setText("");
                    txtHRelationCTO.setTag("");
                    txtRankNo.setTag("");
                }
            }
        });
    }

    public void show(Organization_Mdl organizationMdl, String vDate, String mode) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reset();
        this.voucherDate = vDate;
        flwOk.add(btnOk);
        flwOk.setVisible(true);
        txtClaimId.setEnabled(true);
        txtClaimRemark.setEnabled(true);
        txtClaimDate.setEnabled(true);
        if (DmShared.xNull(mode).trim().equalsIgnoreCase("Browse")) {
            flwOk.clear();
            flwOk.setVisible(false);
        } else if (DmShared.xNull(mode).trim().equalsIgnoreCase("edit")) {
            txtClaimDate.setEnabled(false);
            txtClaimId.setEnabled(false);
            txtClaimRemark.setEnabled(false);
        }

        isTPA = organizationMdl.isTpa();
        txtCardHolderNameCTO.setText(DmShared.xNull(organizationMdl.getHolderName()));
        txtCardNoCTO.setText(DmShared.xNull(organizationMdl.getPolicyNo()));
        txtHInsuranceCompany.setTag(DmShared.xNull(organizationMdl.getInsuranceCompanyCode()));
        txtHInsuranceCompany.setText(DmShared.xNull(organizationMdl.getInsuranceCompanyName()));
        txtHRelationCTO.setText(DmShared.xNull(organizationMdl.getRelationshipName()));
        txtHRelationCTO.setTag(DmShared.xNull(organizationMdl.getRelationshipCode()));
        txtCardTypeCTO.setText(DmShared.xNull(organizationMdl.getSharedKeyFieldName()));
        txtCardTypeCTO.setTag(DmShared.iNull(organizationMdl.getSharedKeyFieldCode()).toString());
        txtHCorporateEmployee.setText(DmShared.xNull(organizationMdl.getCorporateEmployeeName()));
        txtHCorporateEmployee.setTag(DmShared.iNull(organizationMdl.getCorporateEmployeeCode()).toString());
        txtCardTypeCTO.setTag2nd(DmShared.xNull(organizationMdl.getOrgShortName()));
        txtRankNo.setText(DmShared.xNull(organizationMdl.getRankNo()));
        txtReferralNo.setText(DmShared.xNull(organizationMdl.getReferralNo()));
        txtReferralNo.setTag(DmShared.xNull(organizationMdl.getRateListCode()));
        txtReferralValidUpTo.setText(DmShared.dNull(organizationMdl.getReferralValidUpTo()));
        txtClaimId.setText(DmShared.xNull(organizationMdl.getClaimId()));
        txtClaimRemark.setText(DmShared.xNull(organizationMdl.getClaimRemark()));
        txtClaimDate.setText(DmShared.dNull(organizationMdl.getClaimDate()));
        txtbillingPer.setText(DmClient.vNull(organizationMdl.getBillingPer(), "0.00"));

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHInsuranceCompany.setFocus(true);
            }
        });

    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtReferralValidUpTo)) {
            if (!DmShared.xNull(txtReferralValidUpTo.getText()).equalsIgnoreCase("")) {
                txtReferralValidUpTo.setText(DmClient.returnDate(txtReferralValidUpTo.getText()));
            }
        } else if (event.getSource().equals(txtClaimDate)) {
            if (!DmShared.xNull(txtClaimDate.getText()).equalsIgnoreCase("")) {
                txtClaimDate.setText(DmClient.returnDate(txtClaimDate.getText()));
            }

        } else if (event.getSource().equals(txtClaimId)) {
            if (!DmShared.xNull(txtClaimId.getText()).equalsIgnoreCase("")
                    && DmShared.xNull(txtClaimDate.getText()).equalsIgnoreCase("")) {
                txtClaimDate.setText(DmClient.getLoginDate());
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            callBack.close();
            hide();
        } else if (event.getSource() == btnOk) {
            if (isTPA) {
                if (DmShared.xNull(txtCardNoCTO.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Emp. Policy/Service No. is mandatory.", txtCardNoCTO).show();
                    return;
                }
                if (DmShared.xNull(txtCardHolderNameCTO.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Card Holder Name is mandatory.", txtCardHolderNameCTO).show();
                    return;
                }
                if (DmShared.xNull(txtHRelationCTO.getTag()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Relationship is mandatory.", txtHRelationCTO).show();
                    return;
                }
                if (DmShared.xNull(txtHInsuranceCompany.getTag()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Insurance Company is mandatory.", txtHInsuranceCompany).show();
                    return;
                }
//                if (!DmShared.dNull(txtReferralValidUpTo.getText()).equalsIgnoreCase("") && !DmShared.dNull(voucherDate).equalsIgnoreCase("")) {
//                    if (DmShared.convertDate(txtReferralValidUpTo.getText()).getTime() < DmShared.convertDate(voucherDate).getTime()) {
//                        new DmAlertMessageBox("Referral Valid Upto is not less than voucher date.", txtReferralValidUpTo).show();
//                        return;
//                    }
//                }
            }

            if (!DmShared.xNull(txtClaimId.getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.dNull(txtClaimDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Claim Date is mandatory.", txtClaimDate).show();
                    return;
                }
                if (DmShared.convertDate(txtClaimDate.getText()).getTime() < DmShared.convertDate(voucherDate).getTime()) {
                    new DmAlertMessageBox("Claim Date is not less than voucher date.", txtClaimDate).show();
                    return;
                }
                if (DmShared.xNull(txtClaimRemark.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Claim Remark is mandatory.", txtClaimDate).show();
                    return;
                }
            }
            if (DmShared.vNull(txtbillingPer.getText()).doubleValue() > 100) {
                new DmAlertMessageBox("Billing % should not be greater than 100%.", txtbillingPer).show();
                return;
            }

            Organization_Mdl organizationMdl = new Organization_Mdl();

            organizationMdl.setSharedKeyFieldCode(DmShared.xNull(txtCardTypeCTO.getTag()));
            organizationMdl.setSharedKeyFieldName(DmShared.xNull(txtCardTypeCTO.getText()));
            organizationMdl.setInsuranceCompanyCode(DmShared.xNull(txtHInsuranceCompany.getTag()));
            organizationMdl.setInsuranceCompanyName(DmShared.xNull(txtHInsuranceCompany.getText()));
            organizationMdl.setTpa(isTPA);
            organizationMdl.setOrgShortName(DmShared.xNull(txtCardTypeCTO.getTag2nd()));
            organizationMdl.setHolderName(DmShared.xNull(txtCardHolderNameCTO.getText()));
            organizationMdl.setPolicyNo(DmShared.xNull(txtCardNoCTO.getText()));
            organizationMdl.setRelationshipCode(DmShared.xNull(txtHRelationCTO.getTag()));
            organizationMdl.setRelationshipName(DmShared.xNull(txtHRelationCTO.getText()));
            organizationMdl.setCorporateEmployeeCode(DmShared.xNull(txtHCorporateEmployee.getTag()));
            organizationMdl.setCorporateEmployeeName(DmShared.xNull(txtHCorporateEmployee.getText()));
            organizationMdl.setRankNo(DmShared.xNull(txtRankNo.getText()));
            organizationMdl.setReferralNo(DmShared.xNull(txtReferralNo.getText()));
            organizationMdl.setRateListCode(DmShared.xNull(txtReferralNo.getTag()));
            organizationMdl.setReferralValidUpTo(DmShared.dNull(txtReferralValidUpTo.getText()));
            organizationMdl.setClaimId(DmShared.xNull(txtClaimId.getText()));
            organizationMdl.setClaimDate(DmShared.dNull(txtClaimDate.getText()));
            organizationMdl.setBillingPer(DmClient.vNull(txtbillingPer.getText(), "0.00"));
            organizationMdl.setClaimRemark(DmShared.xNull(txtClaimRemark.getText()));

            callBack.ok(organizationMdl);
            hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.close();
            hide();
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    public OrganizationPopup() {
        super();
        srvAsync = new ServiceInvoker<OrganizationPopup_SrvAsync>(GWT.create(OrganizationPopup_Srv.class), OrganizationPopup.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtReferralValidUpTo.addBlurHandler(this);
        txtClaimDate.addBlurHandler(this);
        txtClaimId.addBlurHandler(this);
        textHelpEvents();
    }

    private void reset() {
        txtCardTypeCTO.setEnabled(false);
        txtCardHolderNameCTO.setText("");
        txtHInsuranceCompany.setTag("");
        txtHInsuranceCompany.setText("");
        txtCardNoCTO.setText("");
        txtCardTypeCTO.setText("");
        txtCardTypeCTO.setTag("");
        txtCardTypeCTO.setTag2nd("");
        txtClaimId.setText("");
        txtClaimId.setTag("");
        txtClaimId.setTag2nd("");
        txtClaimDate.setText("");
        txtClaimDate.setTag("");
        txtClaimDate.setTag2nd("");
        txtClaimRemark.setText("");
        txtClaimRemark.setTag("");
        txtClaimRemark.setTag2nd("");
        txtHRelationCTO.setTag("");
        txtHRelationCTO.setText("");
        txtHCorporateEmployee.setText("");
        txtHCorporateEmployee.setTag("");
        txtRankNo.setText("");
        txtReferralNo.setText("");
        txtReferralValidUpTo.setText("");
        txtbillingPer.setText("");
        voucherDate = "";
    }

}
