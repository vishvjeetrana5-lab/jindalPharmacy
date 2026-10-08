package dataman.dmToolkit.eGstPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.EWAY_GenerationMode;
import dataman.dmBase.self.shared.DmEnum.TransportMode;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.eGstPopup.shared.*;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import dataman.dmToolkit.gst.shared.EGstToken_Mdl;
import dataman.dmToolkit.gst.shared.EGst_Mdl;
import dataman.dmToolkit.gst.shared.EwayCancelRequest_Mdl;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.Gst_Shared;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.gst.shared.Party_Mdl;
import java.util.HashMap;
import java.util.List;

public class EGstPopup extends EGstPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {


    public interface CallBack {

        public void onOk(EGst_Mdl values);
    }
    CallBack callBack;

    private EGst_Mdl egst_Mdl = null;
    private GstDocument_Mdl gst_Mdl = null;
    private EGstToken_Mdl token_Mdl = null;

    private final ServiceInvoker<EGstPopup_SrvAsync> srvAsync;
    private final ServiceInvoker<? extends IEGstPopup_SrvAsync> srvEGstPopupAsync;
    private String transporterGstin = "", actFromStateNumericCode = "", actToStateNumericCode = "";
    private boolean isTransporterEditable = false, isTransporterDetailEditable = false, isEwayDocTypeEditable = false, isEwaySubSupplyTypeEditable = false, isEinvoiceSupplyTypeEditable = false, isEInvoiceDocTypeEditable = false, isBuyerAddressEditable = false, isConsigneeAddressEditable = false, isShipToAddressEditable = false, isGstDocumentRequired = false, isIntraState = false, isJobWork = false, isEwayTransactionTypeEditable = false, isDispatcherAddressEditable = false;
    private final EGstCancelPopup egstCancelPopup = new EGstCancelPopup();
    private boolean isEWayBillRequired = false, isEInvoiceRequired = false, isTPBillGenrated = false;
    private String eWay_GenerationMode = "";
    final Setting settingPopup = new Setting();

    public EGstPopup(ServiceInvoker<? extends IEGstPopup_SrvAsync> srvEGstPopupAsync) {
        super();
        this.srvEGstPopupAsync = srvEGstPopupAsync;
        srvAsync = new ServiceInvoker<EGstPopup_SrvAsync>(GWT.create(EGstPopup_Srv.class), EGstPopup.class.getName());
        toolContainer.setTitle("GST Detail");
        toolContainer.addWidget(flwCancel);
        toolContainer.addWidget(flwSetting);
        toolContainer.addGap();
        textHelpEvents();
        handlerEvents();
    }

    private void manageDisplay() {
        boolean isEwayGenrated = false, isEinvGenrated = false;

        if (isEWayBillRequired) {
            if (!DmShared.xNull(txtEWayBillNo.getText()).trim().equalsIgnoreCase("")) {
                isEwayGenrated = true;
            }
        }
        if (isEInvoiceRequired) {
            if (!DmShared.xNull(txtEInvIrnNo.getText()).trim().equalsIgnoreCase("")) {
                isEinvGenrated = true;
            }
        }

        txtEWayBillNo.setEnabled(false);
        txtEWayDate.setEnabled(false);
        txtEWayValidUpto.setEnabled(false);
        txtEWayMsg.setEnabled(false);
        txtEInvStatus.setEnabled(false);
        txtSubSupplyDesc.setEnabled(false);
        txtHEwaySubSupplyType.setEnabled(false);
        txtHEwayDocumentType.setEnabled(false);
        txtHEinvoiceSupplyType.setEnabled(false);
        txtHEinvoiceDocType.setEnabled(false);
        txtEInvAckNo.setEnabled(false);
        txtEInvAckDate.setEnabled(false);
        txtEInvIrnNo.setEnabled(false);
        txtHGstDocumentType.setEnabled(false);
        txtHGstDocumentNature.setEnabled(false);
        txtHEwayTransactionType.setEnabled(false);

        //<editor-fold defaultstate="collapsed" desc="Token">
        txtAuthToken.setEnabled(false);
        txtTokenExpiryAt.setEnabled(false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="txtBuyer">
        txtBuyerName.setEnabled(false);
        txtBuyerGstin.setEnabled(false);
        txtBuyerGstinDate.setEnabled(false);
        txtBuyerLegalName.setEnabled(false);
        txtBuyerTradeName.setEnabled(false);
        txtBuyerState.setEnabled(false);
        txtHBuyerCity.setEnabled(false);
        txtBuyerAddress1.setEnabled(false);
        txtBuyerAddress2.setEnabled(false);
        txtBuyerPinCode.setEnabled(false);
        txtBuyerPhone.setEnabled(false);
        txtBuyerEmail.setEnabled(false);
        txtBuyerCountry.setEnabled(false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="txtShipTo">
        txtShipToPartyName.setEnabled(false);
        txtShipToPartyGstin.setEnabled(false);
        txtShipToPartyGstinDate.setEnabled(false);
        txtShipToPartyLegalName.setEnabled(false);
        txtShipToPartyTradeName.setEnabled(false);
        txtShipToPartyState.setEnabled(false);
        txtHShipToPartyCity.setEnabled(false);
        txtShipToPartyAddress1.setEnabled(false);
        txtShipToPartyAddress2.setEnabled(false);
        txtShipToPartyPinCode.setEnabled(false);
        txtShipToPartyPhone.setEnabled(false);
        txtShipToPartyEmail.setEnabled(false);
        txtShipToPartyCountry.setEnabled(false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Consigner">
        txtConsignerName.setEnabled(false);
        txtConsignerGstin.setEnabled(false);
        txtConsignerGstinDate.setEnabled(false);
        txtConsignerLegalName.setEnabled(false);
        txtConsignerTradeName.setEnabled(false);
        txtHConsignerCity.setEnabled(false);
        txtConsignerState.setEnabled(false);
        txtConsignerPhone.setEnabled(false);
        txtConsignerAddress1.setEnabled(false);
        txtConsignerAddress2.setEnabled(false);
        txtConsignerEmail.setEnabled(false);
        txtConsignerPinCode.setEnabled(false);
        txtConsignerCountry.setEnabled(false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Dispatcher">
        txtDispatcherName.setEnabled(false);
        txtDispatcherGstin.setEnabled(false);
        txtDispatcherGstinDate.setEnabled(false);
        txtDispatcherLegalName.setEnabled(false);
        txtDispatcherTradeName.setEnabled(false);
        txtHDispatcherCity.setEnabled(false);
        txtDispatcherState.setEnabled(false);
        txtDispatcherPhone.setEnabled(false);
        txtDispatcherAddress1.setEnabled(false);
        txtDispatcherAddress2.setEnabled(false);
        txtDispatcherEmail.setEnabled(false);
        txtDispatcherPinCode.setEnabled(false);
        txtDispatcherCountry.setEnabled(false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Consignee">
        txtConsigneeName.setEnabled(false);
        txtConsigneeGstin.setEnabled(false);
        txtConsigneeGstinDate.setEnabled(false);
        txtConsigneeLegalName.setEnabled(false);
        txtConsigneeTradeName.setEnabled(false);
        txtHConsigneeCity.setEnabled(false);
        txtConsigneeState.setEnabled(false);

        txtConsigneeAddress1.setEnabled(false);
        txtConsigneeAddress2.setEnabled(false);
        txtConsigneePhone.setEnabled(false);
        txtConsigneeEmail.setEnabled(false);
        txtConsigneePinCode.setEnabled(false);
        txtConsigneeCountry.setEnabled(false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Transporter Details">
        txtHTransporter.setEnabled(false);
        txtTransporterId.setEnabled(false);
        txtTransporterGstinDate.setEnabled(false);
        txtLrNo.setEnabled(false);
        txtLrDate.setEnabled(false);
        txtVehicleNo.setEnabled(false);
        txtHTransportMode.setEnabled(false);
        txtHVehicleType.setEnabled(false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="GST DOCUMENT">
        txtHGstDocumentType.setEnabled(isGstDocumentRequired);
        txtHGstDocumentNature.setEnabled(isGstDocumentRequired);
        //</editor-fold>

        if (!isEwayGenrated && isEWayBillRequired) {
            txtHEwaySubSupplyType.setEnabled(isEwaySubSupplyTypeEditable);
            if (DmShared.iNull(txtHEwaySubSupplyType.getTag()).intValue() == DmEnum.Eway_SubSupplyType.OTHERS.getCode()) {
                txtSubSupplyDesc.setEnabled(isEwaySubSupplyTypeEditable);
            } else {
                txtSubSupplyDesc.setEnabled(false);
            }
            txtHEwayDocumentType.setEnabled(isEwayDocTypeEditable);
            txtHEwayTransactionType.setEnabled(isEwayTransactionTypeEditable);
        }
        if (!isEinvGenrated && isEInvoiceRequired) {
            txtHEinvoiceSupplyType.setEnabled(isEinvoiceSupplyTypeEditable);
            txtHEinvoiceDocType.setEnabled(isEInvoiceDocTypeEditable);
        }
        if (isEwayGenrated == false && isTPBillGenrated == false) {
            //<editor-fold defaultstate="collapsed" desc="Transporter Details">
            txtHTransporter.setEnabled(isTransporterEditable);
            txtHTransportMode.setEnabled(isTransporterDetailEditable);
            txtVehicleNo.setEnabled(isTransporterDetailEditable);
            txtHVehicleType.setEnabled(isTransporterDetailEditable);
            txtLrNo.setEnabled(isTransporterDetailEditable);
            txtLrDate.setEnabled(isTransporterDetailEditable);
            //</editor-fold>
        }
        if ((isEwayGenrated == false) && (isEinvGenrated == false)) {
            //<editor-fold defaultstate="collapsed" desc="txtBuyer">
            txtDispatcherAddress1.setEnabled(isDispatcherAddressEditable);
            txtDispatcherAddress2.setEnabled(isDispatcherAddressEditable);
            txtDispatcherPinCode.setEnabled(isDispatcherAddressEditable);
            txtDispatcherPhone.setEnabled(isDispatcherAddressEditable);
            txtDispatcherEmail.setEnabled(isDispatcherAddressEditable);
            txtHDispatcherCity.setEnabled(isDispatcherAddressEditable);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Dispatcher">
            txtBuyerAddress1.setEnabled(isBuyerAddressEditable);
            txtBuyerAddress2.setEnabled(isBuyerAddressEditable);
            txtBuyerPinCode.setEnabled(isBuyerAddressEditable);
            txtBuyerPhone.setEnabled(isBuyerAddressEditable);
            txtBuyerEmail.setEnabled(isBuyerAddressEditable);
            txtHBuyerCity.setEnabled(isBuyerAddressEditable);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="txtShipTo">
            txtShipToPartyAddress1.setEnabled(isShipToAddressEditable);
            txtShipToPartyAddress2.setEnabled(isShipToAddressEditable);
            txtShipToPartyPinCode.setEnabled(isShipToAddressEditable);
            txtShipToPartyPhone.setEnabled(isShipToAddressEditable);
            txtShipToPartyEmail.setEnabled(isShipToAddressEditable);
            txtHShipToPartyCity.setEnabled(isShipToAddressEditable);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Consignee">
            txtConsigneeAddress1.setEnabled(isConsigneeAddressEditable);
            txtConsigneeAddress2.setEnabled(isConsigneeAddressEditable);
            txtConsigneePhone.setEnabled(isConsigneeAddressEditable);
            txtConsigneeEmail.setEnabled(isConsigneeAddressEditable);
            txtConsigneePinCode.setEnabled(isConsigneeAddressEditable);
            txtHConsigneeCity.setEnabled(isConsigneeAddressEditable);
            //</editor-fold>
        }
        //<editor-fold defaultstate="collapsed" desc="Manage Action">
        manageAction();
        //</editor-fold>
    }

    private void displayData() {
        clear();
        //<editor-fold defaultstate="collapsed" desc="DISPLAY EGST">
        isEInvoiceRequired = egst_Mdl.isEinvoiceRequired();
        isEWayBillRequired = egst_Mdl.isEwayBillRequired();
        eWay_GenerationMode = DmShared.xNull(egst_Mdl.getEway_GenerationMode());
        txtEWayBillNo.setText(DmShared.xNull(egst_Mdl.getEway_BillNo()));
        txtEWayDate.setText(DmShared.xNull(egst_Mdl.getEway_BillDate()));
        txtEWayValidUpto.setText(DmShared.xNull(egst_Mdl.getEway_validUpto()));
        txtEWayMsg.setText(DmShared.xNull(egst_Mdl.getEway_alert()));
        txtEInvAckNo.setText(DmShared.xNull(egst_Mdl.getEinv_ackNo()));
        txtEInvAckDate.setText(DmShared.xNull(egst_Mdl.getEinv_ackDt()));
        txtEInvIrnNo.setText(DmShared.xNull(egst_Mdl.getEinv_IrnNo()));
        //<editor-fold defaultstate="collapsed" desc="Einv Status">
        if (!DmShared.xNull(egst_Mdl.getEinv_Status()).trim().equalsIgnoreCase("")) {
            String einvStatus = DmEnum.Egst_Status.getViaCode(egst_Mdl.getEinv_Status());
            if (!DmShared.xNull(einvStatus).trim().equalsIgnoreCase("")) {
                txtEInvStatus.setText(DmShared.xNull(einvStatus));
                txtEInvStatus.setTag(DmShared.xNull(egst_Mdl.getEinv_Status()));
            }
        }
        //</editor-fold>
        txtHEwaySubSupplyType.setTag(DmShared.xNull(egst_Mdl.getEwaySubSupplyTypeCode()));
        txtHEwaySubSupplyType.setTag2nd(DmShared.xNull(egst_Mdl.getEwaySubSupplyTypeCode()));
        txtHEwaySubSupplyType.setText(DmShared.xNull(egst_Mdl.getEwaySubSupplyTypeName()));
        txtHEwayDocumentType.setTag(DmShared.xNull(egst_Mdl.getEwayDocTypeCode()));
        txtHEwayDocumentType.setText(DmShared.xNull(egst_Mdl.getEwayDocTypeName()));
        txtHEinvoiceSupplyType.setTag(DmShared.xNull(egst_Mdl.getEinvSupplyTypeCode()));
        txtHEinvoiceSupplyType.setText(DmShared.xNull(egst_Mdl.getEinvSupplyTypeName()));
        txtHEinvoiceDocType.setTag(DmShared.xNull(egst_Mdl.getEinvDocTypeCode()));
        txtHEinvoiceDocType.setText(DmShared.xNull(egst_Mdl.getEinvDocTypeName()));
        txtHEwayTransactionType.setTag(DmShared.xNull(egst_Mdl.getEwayTransTypeCode()));
        txtHEwayTransactionType.setText(DmShared.xNull(egst_Mdl.getEwayTransTypeName()));
        txtSubSupplyDesc.setText(DmShared.xNull(egst_Mdl.getEwaySubSupplyDesc()));
        txtDistance.setText(DmShared.xNull(egst_Mdl.getTransDistance()));
        //<editor-fold defaultstate="collapsed" desc="ActionStateCode">
        actFromStateNumericCode = DmShared.xNull(egst_Mdl.getActFromStateNumericCode());
        actToStateNumericCode = DmShared.xNull(egst_Mdl.getActToStateNumericCode());
        //</editor-fold>
        ////<editor-fold defaultstate="collapsed" desc="Consigner">
        txtConsignerName.setTag(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getSharedKeyFieldCode()));
        txtConsignerName.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getSharedKeyFieldName()));
        txtConsignerName.setTag2nd(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getSiteCode()));
        txtConsignerGstin.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getGstin()));
        txtConsignerGstinDate.setText(DmShared.dNull(egst_Mdl.getConsigner_Mdl().getGstinDate()));
        txtConsignerLegalName.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getLegalName()));
        txtConsignerTradeName.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getTradeName()));
        txtConsignerAddress1.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getAddress1()));
        txtConsignerAddress2.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getAddress2()));
        txtConsignerState.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getStateName()));
        txtConsignerState.setTag(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getStateNumericCode()));
        txtConsignerPhone.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getPhone()));
        txtHConsignerCity.setTag(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getCityCode()));
        txtHConsignerCity.setTag2nd(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getCityCode()));
        txtHConsignerCity.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getCityName()));
        txtConsignerEmail.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getEmail()));
        txtConsignerPinCode.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getPinCode()));
        txtConsignerCountry.setTag(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getGstCountryCode()));
        txtConsignerCountry.setText(DmShared.xNull(egst_Mdl.getConsigner_Mdl().getCountryName()));
        ////</editor-fold>
        ////<editor-fold defaultstate="collapsed" desc="Dispatcher">
        txtDispatcherName.setTag(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getSharedKeyFieldCode()));
        txtDispatcherName.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getSharedKeyFieldName()));
        txtDispatcherName.setTag2nd(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getSiteCode()));
        txtDispatcherGstin.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getGstin()));
        txtDispatcherGstinDate.setText(DmShared.dNull(egst_Mdl.getDispatchFrom_Mdl().getGstinDate()));
        txtDispatcherLegalName.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getLegalName()));
        txtDispatcherTradeName.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getTradeName()));
        txtDispatcherAddress1.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getAddress1()));
        txtDispatcherAddress2.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getAddress2()));
        txtDispatcherState.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getStateName()));
        txtDispatcherState.setTag(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getStateNumericCode()));
        txtDispatcherPhone.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getPhone()));
        txtHDispatcherCity.setTag(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getCityCode()));
        txtHDispatcherCity.setTag2nd(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getCityCode()));
        txtHDispatcherCity.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getCityName()));
        txtDispatcherEmail.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getEmail()));
        txtDispatcherPinCode.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getPinCode()));
        txtDispatcherCountry.setTag(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getGstCountryCode()));
        txtDispatcherCountry.setText(DmShared.xNull(egst_Mdl.getDispatchFrom_Mdl().getCountryName()));

        ////</editor-fold>
        ////<editor-fold defaultstate="collapsed" desc="BillTo">
        txtBuyerName.setTag(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getSharedKeyFieldCode()));
        txtBuyerName.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getSharedKeyFieldName()));
        txtBuyerName.setTag2nd(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getSiteCode()));
        txtBuyerGstin.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getGstin()));
        txtBuyerGstinDate.setText(DmShared.dNull(egst_Mdl.getBillTo_Mdl().getGstinDate()));
        txtBuyerLegalName.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getLegalName()));
        txtBuyerTradeName.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getTradeName()));
        txtBuyerAddress1.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getAddress1()));
        txtBuyerAddress2.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getAddress2()));
        txtBuyerState.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getStateName()));
        txtBuyerState.setTag(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getStateNumericCode()));
        txtBuyerState.setTag2nd(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getPlaceOfSupplyCode()));
        txtBuyerPhone.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getPhone()));
        txtHBuyerCity.setTag(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getCityCode()));
        txtHBuyerCity.setTag2nd(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getCityCode()));
        txtHBuyerCity.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getCityName()));
        txtBuyerEmail.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getEmail()));
        txtBuyerPinCode.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getPinCode()));
        txtBuyerCountry.setTag(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getGstCountryCode()));
        txtBuyerCountry.setText(DmShared.xNull(egst_Mdl.getBillTo_Mdl().getCountryName()));
        ////</editor-fold>
        ////<editor-fold defaultstate="collapsed" desc="Consignee">
        txtConsigneeName.setTag(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getSharedKeyFieldCode()));
        txtConsigneeName.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getSharedKeyFieldName()));
        txtConsigneeName.setTag2nd(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getSiteCode()));
        txtConsigneeGstin.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getGstin()));
        txtConsigneeGstinDate.setText(DmShared.dNull(egst_Mdl.getConsignee_Mdl().getGstinDate()));
        txtConsigneeLegalName.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getLegalName()));
        txtConsigneeTradeName.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getTradeName()));
        txtConsigneeAddress1.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getAddress1()));
        txtConsigneeAddress2.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getAddress2()));
        txtConsigneeState.setTag(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getStateNumericCode()));
        txtConsigneeState.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getStateName()));
        txtConsigneePhone.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getPhone()));
        txtHConsigneeCity.setTag(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getCityCode()));
        txtHConsigneeCity.setTag2nd(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getCityCode()));
        txtHConsigneeCity.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getCityName()));
        txtConsigneeEmail.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getEmail()));
        txtConsigneePinCode.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getPinCode()));
        txtConsigneeCountry.setTag(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getGstCountryCode()));
        txtConsigneeCountry.setText(DmShared.xNull(egst_Mdl.getConsignee_Mdl().getCountryName()));
        ////</editor-fold>
        //    //<editor-fold defaultstate="collapsed" desc="ShipTo">
        txtShipToPartyName.setTag(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getSharedKeyFieldCode()));
        txtShipToPartyName.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getSharedKeyFieldName()));
        txtShipToPartyName.setTag2nd(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getSiteCode()));
        txtShipToPartyGstin.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getGstin()));
        txtShipToPartyGstinDate.setText(DmShared.dNull(egst_Mdl.getShipTo_Mdl().getGstinDate()));
        txtShipToPartyLegalName.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getLegalName()));
        txtShipToPartyTradeName.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getTradeName()));
        txtShipToPartyAddress1.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getAddress1()));
        txtShipToPartyAddress2.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getAddress2()));
        txtShipToPartyState.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getStateName()));
        txtShipToPartyState.setTag(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getStateNumericCode()));
        txtShipToPartyPhone.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getPhone()));
        txtHShipToPartyCity.setTag(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getCityCode()));
        txtHShipToPartyCity.setTag2nd(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getCityCode()));
        txtHShipToPartyCity.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getCityName()));
        txtShipToPartyEmail.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getEmail()));
        txtShipToPartyPinCode.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getPinCode()));
        txtShipToPartyCountry.setTag(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getGstCountryCode()));
        txtShipToPartyCountry.setText(DmShared.xNull(egst_Mdl.getShipTo_Mdl().getCountryName()));
        ////</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Transporter Details">
        txtLrNo.setText(DmShared.xNull(egst_Mdl.getTransportLrNo()));
        txtLrDate.setText(DmShared.dNull(egst_Mdl.getTransportLrDate()));
        txtHTransporter.setTag(DmShared.xNull(egst_Mdl.getTransporter_Mdl().getSharedKeyFieldCode()));
        txtHTransporter.setTag2nd(DmShared.xNull(egst_Mdl.getTransporter_Mdl().getSharedKeyFieldCode()));
        txtHTransporter.setText(DmShared.xNull(egst_Mdl.getTransporter_Mdl().getSharedKeyFieldName()));
        txtTransporterId.setText(DmShared.xNull(egst_Mdl.getTransporter_Mdl().getTransporterId()));
        txtTransporterGstinDate.setText(DmShared.dNull(egst_Mdl.getTransporter_Mdl().getGstinDate()));
        transporterGstin = DmShared.xNull(egst_Mdl.getTransporter_Mdl().getGstin());
        txtVehicleNo.setText(DmShared.xNull(egst_Mdl.getVehicleNo()));
        txtHTransportMode.setTag(DmShared.xNull(egst_Mdl.getTransportModeCode()));
        txtHTransportMode.setText(DmShared.xNull(egst_Mdl.getTransportModeName()));
        txtHVehicleType.setTag(DmShared.xNull(egst_Mdl.getVehicleTypeCode()));
        txtHVehicleType.setText(DmShared.xNull(egst_Mdl.getVehicleTypeName()));
        txtHVehicleType.setTag2nd(DmShared.xNull(egst_Mdl.getGstVehicleType()));
        txtDistance.setText(DmShared.xNull(egst_Mdl.getTransDistance()));
        isTPBillGenrated = egst_Mdl.isTpBillGenrated();
        //</editor-fold>
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="DISPLAY GST">
        txtHGstDocumentType.setTag(DmShared.xNull(gst_Mdl.getGstDocTypeCode()));
        txtHGstDocumentType.setText(DmShared.xNull(gst_Mdl.getGstDocTypeName()));
        txtHGstDocumentNature.setTag(DmShared.xNull(gst_Mdl.getGstDocNatureCode()));
        txtHGstDocumentNature.setText(DmShared.xNull(gst_Mdl.getGstDocNatureName()));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="DISPLAY Token">
        if (token_Mdl != null) {
            txtAuthToken.setText(DmShared.xNull(token_Mdl.getAuthtoken()));
            txtTokenExpiryAt.setText(DmShared.xNull(token_Mdl.getTokenExpiry()));
        }
        //</editor-fold>
        manageDisplay();
    }

    private void clear() {
        //<editor-fold defaultstate="collapsed" desc="EGST">

        txtHEwaySubSupplyType.setTag("");
        txtHEwaySubSupplyType.setText("");

        resetEwaySubSupplyType();

        txtHEwayDocumentType.setTag("");
        txtHEwayDocumentType.setText("");
        txtHEwayDocumentType.setTag2nd("");

        txtHEwayTransactionType.setTag("");
        txtHEwayTransactionType.setTag2nd("");
        txtHEwayTransactionType.setText("");

        txtHEinvoiceDocType.setTag("");
        txtHEinvoiceDocType.setTag2nd("");
        txtHEinvoiceDocType.setText("");

        txtHEinvoiceSupplyType.setTag("");
        txtHEinvoiceSupplyType.setTag2nd("");
        txtHEinvoiceSupplyType.setText("");

        txtEWayBillNo.setText("");
        txtEWayBillNo.setTag("");
        txtEWayBillNo.setTag2nd("");

        txtEWayDate.setText("");
        txtEWayDate.setTag("");
        txtEWayDate.setTag2nd("");

        txtEInvAckNo.setTag("");
        txtEInvAckNo.setTag2nd("");
        txtEInvAckNo.setText("");

        txtEInvAckDate.setTag("");
        txtEInvAckDate.setTag2nd("");
        txtEInvAckDate.setText("");

        txtEInvIrnNo.setTag("");
        txtEInvIrnNo.setTag2nd("");
        txtEInvIrnNo.setText("");

        txtEInvStatus.setTag("");
        txtEInvStatus.setTag2nd("");
        txtEInvStatus.setText("");

        txtLrNo.setText("");
        txtLrNo.setTag("");
        txtLrNo.setTag2nd("");

        txtLrDate.setText("");
        txtLrDate.setText("");
        txtLrDate.setTag2nd("");

        txtHTransporter.setTag("");
        txtHTransporter.setText("");

        resetTransporter();

        eWay_GenerationMode = "";
        isEWayBillRequired = false;
        isTPBillGenrated = false;

        txtVehicleNo.setText("");
        txtVehicleNo.setTag("");
        txtVehicleNo.setTag2nd("");

        txtHTransportMode.setTag("");
        txtHTransportMode.setText("");
        txtHTransportMode.setTag2nd("");

        txtHVehicleType.setTag("");
        txtHVehicleType.setText("");
        txtHVehicleType.setTag2nd("");

        txtDistance.setText("");
        txtDistance.setTag("");
        txtDistance.setTag2nd("");

        //<editor-fold defaultstate="collapsed" desc="ActionStateCode">
        actFromStateNumericCode = "";
        actToStateNumericCode = "";
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Consigner">
        txtConsignerName.setTag("");
        txtConsignerName.setText("");
        txtConsignerName.setTag2nd("");

        txtConsignerGstin.setText("");
        txtConsignerGstin.setTag("");
        txtConsignerGstin.setTag2nd("");

        txtConsignerGstinDate.setText("");
        txtConsignerGstinDate.setTag("");
        txtConsignerGstinDate.setTag2nd("");

        txtConsignerLegalName.setText("");
        txtConsignerLegalName.setTag("");
        txtConsignerLegalName.setTag2nd("");

        txtConsignerTradeName.setText("");
        txtConsignerTradeName.setTag("");
        txtConsignerTradeName.setTag2nd("");

        txtConsignerAddress1.setText("");
        txtConsignerAddress1.setTag("");
        txtConsignerAddress1.setTag2nd("");

        txtConsignerAddress2.setText("");
        txtConsignerAddress2.setTag("");
        txtConsignerAddress2.setTag2nd("");

        txtConsignerState.setTag("");
        txtConsignerState.setText("");
        txtConsignerState.setTag2nd("");

        txtConsignerCountry.setTag("");
        txtConsignerCountry.setText("");
        txtConsignerCountry.setTag2nd("");

        txtConsignerPhone.setText("");
        txtConsignerPhone.setTag("");
        txtConsignerPhone.setTag2nd("");

        txtHConsignerCity.setTag("");
        txtHConsignerCity.setTag2nd("");
        txtHConsignerCity.setText("");

        txtConsignerEmail.setText("");
        txtConsignerEmail.setTag("");
        txtConsignerEmail.setTag2nd("");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Dispatcher">
        txtDispatcherName.setTag("");
        txtDispatcherName.setText("");
        txtDispatcherName.setTag2nd("");

        txtDispatcherGstin.setText("");
        txtDispatcherGstin.setTag("");
        txtDispatcherGstin.setTag2nd("");

        txtDispatcherGstinDate.setText("");
        txtDispatcherGstinDate.setTag("");
        txtDispatcherGstinDate.setTag2nd("");

        txtDispatcherLegalName.setText("");
        txtDispatcherLegalName.setTag("");
        txtDispatcherLegalName.setTag2nd("");

        txtDispatcherTradeName.setText("");
        txtDispatcherTradeName.setTag("");
        txtDispatcherTradeName.setTag2nd("");

        txtDispatcherAddress1.setText("");
        txtDispatcherAddress1.setTag("");
        txtDispatcherAddress1.setTag2nd("");

        txtDispatcherAddress2.setText("");
        txtDispatcherAddress2.setTag("");
        txtDispatcherAddress2.setTag2nd("");

        txtDispatcherState.setTag("");
        txtDispatcherState.setText("");
        txtDispatcherState.setTag2nd("");

        txtDispatcherCountry.setTag("");
        txtDispatcherCountry.setText("");
        txtDispatcherCountry.setTag2nd("");

        txtDispatcherPhone.setText("");
        txtDispatcherPhone.setTag("");
        txtDispatcherPhone.setTag2nd("");

        txtHDispatcherCity.setTag("");
        txtHDispatcherCity.setTag2nd("");
        txtHDispatcherCity.setText("");

        txtDispatcherEmail.setText("");
        txtDispatcherEmail.setTag("");
        txtDispatcherEmail.setTag2nd("");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="BillTo">
        txtBuyerName.setTag("");
        txtBuyerName.setText("");
        txtBuyerName.setTag2nd("");

        txtBuyerGstin.setText("");
        txtBuyerGstin.setTag("");
        txtBuyerGstin.setTag2nd("");

        txtBuyerGstinDate.setText("");
        txtBuyerGstinDate.setTag("");
        txtBuyerGstinDate.setTag2nd("");

        txtBuyerLegalName.setText("");
        txtBuyerLegalName.setTag("");
        txtBuyerLegalName.setTag2nd("");

        txtBuyerTradeName.setText("");
        txtBuyerTradeName.setTag("");
        txtBuyerTradeName.setTag2nd("");

        txtBuyerAddress1.setText("");
        txtBuyerAddress1.setTag("");
        txtBuyerAddress1.setTag2nd("");

        txtBuyerAddress2.setText("");
        txtBuyerAddress2.setTag("");
        txtBuyerAddress2.setTag2nd("");

        txtBuyerState.setTag("");
        txtBuyerState.setText("");
        txtBuyerState.setTag2nd("");

        txtBuyerCountry.setTag("");
        txtBuyerCountry.setText("");
        txtBuyerCountry.setTag2nd("");

        txtBuyerPhone.setText("");
        txtBuyerPhone.setTag("");
        txtBuyerPhone.setTag2nd("");

        txtHBuyerCity.setTag("");
        txtHBuyerCity.setTag2nd("");
        txtHBuyerCity.setText("");

        txtBuyerEmail.setText("");
        txtBuyerEmail.setTag("");
        txtBuyerEmail.setTag2nd("");

        ////</editor-fold>
        ////<editor-fold defaultstate="collapsed" desc="Consignee">
        txtConsigneeName.setTag("");
        txtConsigneeName.setText("");
        txtConsigneeName.setTag2nd("");

        txtConsigneeGstin.setText("");
        txtConsigneeGstin.setTag("");
        txtConsigneeGstin.setTag2nd("");

        txtConsigneeGstinDate.setText("");
        txtConsigneeGstinDate.setTag("");
        txtConsigneeGstinDate.setTag2nd("");

        txtConsigneeLegalName.setText("");
        txtConsigneeLegalName.setTag("");
        txtConsigneeLegalName.setTag2nd("");

        txtConsigneeTradeName.setText("");
        txtConsigneeTradeName.setTag("");
        txtConsigneeTradeName.setTag2nd("");

        txtConsigneeAddress1.setText("");
        txtConsigneeAddress1.setTag("");
        txtConsigneeAddress1.setTag2nd("");

        txtConsigneeAddress2.setText("");
        txtConsigneeAddress2.setTag("");
        txtConsigneeAddress2.setTag2nd("");

        txtConsigneeState.setTag("");
        txtConsigneeState.setText("");
        txtConsigneeState.setTag2nd("");

        txtConsigneeCountry.setTag("");
        txtConsigneeCountry.setText("");
        txtConsigneeCountry.setTag2nd("");

        txtConsigneePhone.setText("");
        txtConsigneePhone.setTag("");
        txtConsigneePhone.setTag2nd("");

        txtHConsigneeCity.setTag("");
        txtHConsigneeCity.setTag2nd("");
        txtHConsigneeCity.setText("");

        txtConsigneeEmail.setText("");
        txtConsigneeEmail.setTag("");
        txtConsigneeEmail.setTag2nd("");

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="ShipTo">
        txtShipToPartyName.setTag("");
        txtShipToPartyName.setText("");
        txtShipToPartyName.setTag2nd("");

        txtShipToPartyGstin.setText("");
        txtShipToPartyGstin.setTag("");
        txtShipToPartyGstin.setTag2nd("");

        txtShipToPartyGstinDate.setText("");
        txtShipToPartyGstinDate.setTag("");
        txtShipToPartyGstinDate.setTag2nd("");

        txtShipToPartyLegalName.setText("");
        txtShipToPartyLegalName.setTag("");
        txtShipToPartyLegalName.setTag2nd("");

        txtShipToPartyTradeName.setText("");
        txtShipToPartyTradeName.setTag("");
        txtShipToPartyTradeName.setTag2nd("");

        txtShipToPartyAddress1.setText("");
        txtShipToPartyAddress1.setTag("");
        txtShipToPartyAddress1.setTag2nd("");

        txtShipToPartyAddress2.setText("");
        txtShipToPartyAddress2.setTag("");
        txtShipToPartyAddress2.setTag2nd("");

        txtShipToPartyState.setTag("");
        txtShipToPartyState.setText("");
        txtShipToPartyState.setTag2nd("");

        txtShipToPartyCountry.setTag("");
        txtShipToPartyCountry.setText("");
        txtShipToPartyCountry.setTag2nd("");

        txtShipToPartyPhone.setText("");
        txtShipToPartyPhone.setTag("");
        txtShipToPartyPhone.setTag2nd("");

        txtHShipToPartyCity.setTag("");
        txtHShipToPartyCity.setTag2nd("");
        txtHShipToPartyCity.setText("");

        txtShipToPartyEmail.setText("");
        txtShipToPartyEmail.setTag("");
        txtShipToPartyEmail.setTag2nd("");

        //</editor-fold>
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="GST">
        txtHGstDocumentType.setTag("");
        txtHGstDocumentType.setTag2nd("");
        txtHGstDocumentType.setText("");

        txtHGstDocumentNature.setTag("");
        txtHGstDocumentNature.setTag2nd("");
        txtHGstDocumentNature.setText("");
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Token">
        txtAuthToken.setText("");
        txtAuthToken.setTag("");
        txtAuthToken.setTag2nd("");

        txtTokenExpiryAt.setText("");
        txtTokenExpiryAt.setTag("");
        txtTokenExpiryAt.setTag2nd("");
        //</editor-fold>
    }

    private void resetEwaySubSupplyType() {
        txtHEwaySubSupplyType.setTag2nd("");

        txtSubSupplyDesc.setTag("");
        txtSubSupplyDesc.setTag2nd("");
        txtSubSupplyDesc.setText("");
    }

    private void resetTransporter() {
        txtHTransporter.setTag2nd("");

        transporterGstin = "";

        txtTransporterId.setTag("");
        txtTransporterId.setTag2nd("");
        txtTransporterId.setText("");

        txtTransporterGstinDate.setTag("");
        txtTransporterGstinDate.setTag2nd("");
        txtTransporterGstinDate.setText("");
    }

    private void textHelpEvents() {

        txtHTransportMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransportMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransportMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTransportMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransportMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }

        });
        txtHTransporter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTranporter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransporter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTranporter_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransporter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTranporter_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTransporter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransporter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTranporter_Mdl tRow) {
                if (!DmShared.xNull(txtHTransporter.getTag()).equalsIgnoreCase(DmShared.xNull(txtHTransporter.getTag2nd()))) {
                    resetTransporter();
                    txtHTransporter.setTag2nd(txtHTransporter.getTag());
                }
                if (blnStatus && tRow != null) {
                    transporterGstin = DmShared.xNull(tRow.getGstin());
                    txtTransporterId.setText(DmShared.xNull(tRow.getTransporterId()));
                    txtTransporterGstinDate.setText(DmShared.dNull(tRow.getGstinDate()));
                }

            }
        });
        txtHVehicleType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                srvAsync.invokeRPC(blnCallBackWait).getHVehicleType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVehicleType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVehicleType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVehicleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHVehicleType.setTag2nd(t1Row.getSharedModifiedBy());
                }
            }
        });
        txtHEwaySubSupplyType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                srvAsync.invokeRPC(blnCallBackWait).getHEwaySubSupplyType(DmShared.xNull(egst_Mdl.getEwaySupplyTypeCode()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEwaySubSupplyType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEwaySubSupplyType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEwaySubSupplyType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHEwaySubSupplyType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHEwaySubSupplyType.getTag2nd()))) {
                    resetEwaySubSupplyType();
                    txtHEwaySubSupplyType.setTag2nd(DmShared.xNull(txtHEwaySubSupplyType.getTag()));
                    txtSubSupplyDesc.setEnabled(false);
                    if (blnStatus && t1Row != null) {
                        if (DmShared.iNull(txtHEwaySubSupplyType.getTag()).intValue() == DmEnum.Eway_SubSupplyType.OTHERS.getCode()) {
                            txtSubSupplyDesc.setEnabled(isEwaySubSupplyTypeEditable);
                        }
                    }
                }
            }
        });
        txtHEwayDocumentType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                srvAsync.invokeRPC(blnCallBackWait).getHEwayDocumentType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEwayDocumentType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEwayDocumentType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEwayDocumentType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row
            ) {
            }
        });
        txtHEwayTransactionType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHEwayTransactionType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEwayTransactionType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEwayTransactionType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEwayTransactionType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHEinvoiceSupplyType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                srvAsync.invokeRPC(blnCallBackWait).getHEinvSupplyType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEinvoiceSupplyType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEinvoiceSupplyType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEinvoiceSupplyType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row
            ) {
            }
        });
        txtHEinvoiceDocType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                srvAsync.invokeRPC(blnCallBackWait).getHEinvDocumentType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEinvoiceDocType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEinvoiceDocType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEinvoiceDocType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row
            ) {
            }
        });
        txtHGstDocumentType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDocumentType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstDocumentType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstDocumentType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHGstDocumentType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHGstDocumentNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDocumentNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstDocumentNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstDocumentNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHGstDocumentNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        egstCancelPopup.addCallBack(new EGstCancelPopup.CallBack() {
            @Override
            public void onOk(EwayCancelRequest_Mdl cancelRequest_Mdl, int actionCode) {
                //<editor-fold defaultstate="collapsed" desc="FILL CancelRequest_Mdl">
                if (cancelRequest_Mdl != null) {
                    if (actionCode == DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getCode()) {
                        egst_Mdl.setEway_cancelRsnCode(DmShared.xNull(cancelRequest_Mdl.getCancelRsnCode()));
                        egst_Mdl.setEway_cancelRmrk(DmShared.xNull(cancelRequest_Mdl.getCancelRmrk()));
                    } else if (actionCode == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
                        egst_Mdl.setEinv_cancelRsnCode(DmShared.xNull(cancelRequest_Mdl.getCancelRsnCode()));
                        egst_Mdl.setEinv_cancelRmrk(DmShared.xNull(cancelRequest_Mdl.getCancelRmrk()));
                    }
                }
                //</editor-fold>
                processEgst(actionCode);
            }
        });
        settingPopup.addCallBack(new Setting.CallBack() {
            @Override
            public void onOk(EGstParam_Mdl eGstParam_Mdl) {
                srvAsync.invokeRPC(true).setEgstParamSessionDetail(eGstParam_Mdl, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus() && result.getData() != null) {
                            new DmAlertMessageBox("Setting updated successfully!", (DmTextBox) null).show();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }
        });

        txtHConsignerCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(DmShared.xNull(txtConsignerGstin.getText()), DmShared.xNull(txtConsignerState.getTag()), txtConsignerName.getTag(), DmShared.xNull(txtConsignerName.getTag2nd()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsignerCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHConsignerCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsignerCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
                if (!DmShared.xNull(txtHConsignerCity.getTag()).equalsIgnoreCase(DmShared.xNull(txtHConsignerCity.getTag2nd()))) {
                    txtConsignerState.setText("");
                    txtConsignerState.setTag("");
                    txtConsignerCountry.setTag("");
                    txtConsignerCountry.setText("");
                    txtHConsignerCity.setTag2nd(DmShared.xNull(txtHConsignerCity.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    txtConsignerState.setText(DmShared.xNull(t1Row.getStateName()));
                    txtConsignerState.setTag(DmShared.xNull(t1Row.getStateNumericCode()));
                    txtConsignerCountry.setTag(DmShared.xNull(t1Row.getGstCountryCode()));
                    txtConsignerCountry.setText(DmShared.xNull(t1Row.getCountryName()));
                }
                manageActionStateCode();
            }
        });
        txtHDispatcherCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(DmShared.xNull(txtDispatcherGstin.getText()), DmShared.xNull(txtDispatcherState.getTag()), DmShared.xNull(txtDispatcherName.getTag()), DmShared.xNull(txtDispatcherName.getTag2nd()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDispatcherCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHDispatcherCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDispatcherCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
                if (!DmShared.xNull(txtHDispatcherCity.getTag()).equalsIgnoreCase(DmShared.xNull(txtHDispatcherCity.getTag2nd()))) {
                    txtDispatcherState.setText("");
                    txtDispatcherState.setTag("");
                    txtDispatcherCountry.setTag("");
                    txtDispatcherCountry.setText("");
                    txtHDispatcherCity.setTag2nd(DmShared.xNull(txtHDispatcherCity.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    txtDispatcherState.setText(DmShared.xNull(t1Row.getStateName()));
                    txtDispatcherState.setTag(DmShared.xNull(t1Row.getStateNumericCode()));
                    txtDispatcherCountry.setTag(DmShared.xNull(t1Row.getGstCountryCode()));
                    txtDispatcherCountry.setText(DmShared.xNull(t1Row.getCountryName()));
                }
                manageActionStateCode();
            }
        });
        txtHBuyerCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(DmShared.xNull(txtBuyerGstin.getText()), DmShared.xNull(txtBuyerState.getTag()), DmShared.xNull(txtBuyerName.getTag()), DmShared.xNull(txtBuyerName.getTag2nd()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBuyerCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBuyerCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBuyerCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
                if (!DmShared.xNull(txtHBuyerCity.getTag()).equalsIgnoreCase(DmShared.xNull(txtHBuyerCity.getTag2nd()))) {
                    txtBuyerState.setText("");
                    txtBuyerState.setTag("");
                    txtBuyerCountry.setTag("");
                    txtBuyerCountry.setText("");
                    txtHBuyerCity.setTag2nd(DmShared.xNull(txtHBuyerCity.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    txtBuyerState.setText(DmShared.xNull(t1Row.getStateName()));
                    txtBuyerState.setTag(DmShared.xNull(t1Row.getStateNumericCode()));
                    txtBuyerCountry.setTag(DmShared.xNull(t1Row.getGstCountryCode()));
                    txtBuyerCountry.setText(DmShared.xNull(t1Row.getCountryName()));
                }
                manageActionStateCode();
            }
        });
        txtHConsigneeCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(DmShared.xNull(txtConsigneeGstin.getText()), DmShared.xNull(txtConsigneeState.getTag()), DmShared.xNull(txtConsigneeName.getTag()), DmShared.xNull(txtConsigneeName.getTag2nd()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsigneeCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHConsigneeCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsigneeCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
                if (!DmShared.xNull(txtHConsigneeCity.getTag()).equalsIgnoreCase(DmShared.xNull(txtHConsigneeCity.getTag2nd()))) {
                    txtConsigneeState.setText("");
                    txtConsigneeState.setTag("");
                    txtConsigneeCountry.setTag("");
                    txtConsigneeCountry.setText("");
                    txtHConsigneeCity.setTag2nd(DmShared.xNull(txtHConsigneeCity.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    txtConsigneeState.setText(DmShared.xNull(t1Row.getStateName()));
                    txtConsigneeState.setTag(DmShared.xNull(t1Row.getStateNumericCode()));
                    txtConsigneeCountry.setTag(DmShared.xNull(t1Row.getGstCountryCode()));
                    txtConsigneeCountry.setText(DmShared.xNull(t1Row.getCountryName()));
                }
                manageActionStateCode();
            }
        });
        txtHShipToPartyCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(DmShared.xNull(txtShipToPartyGstin.getText()), DmShared.xNull(txtShipToPartyState.getTag()), DmShared.xNull(txtShipToPartyName.getTag()), DmShared.xNull(txtShipToPartyName.getTag2nd()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShipToPartyCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHShipToPartyCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShipToPartyCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
                if (!DmShared.xNull(txtHShipToPartyCity.getTag()).equalsIgnoreCase(DmShared.xNull(txtHShipToPartyCity.getTag2nd()))) {
                    txtShipToPartyState.setText("");
                    txtShipToPartyState.setTag("");
                    txtShipToPartyCountry.setTag("");
                    txtShipToPartyCountry.setText("");
                    txtHShipToPartyCity.setTag2nd(DmShared.xNull(txtHShipToPartyCity.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    txtShipToPartyState.setText(DmShared.xNull(t1Row.getStateName()));
                    txtShipToPartyState.setTag(DmShared.xNull(t1Row.getStateNumericCode()));
                    txtShipToPartyCountry.setTag(DmShared.xNull(t1Row.getGstCountryCode()));
                    txtShipToPartyCountry.setText(DmShared.xNull(t1Row.getCountryName()));
                }
                manageActionStateCode();
            }
        });
    }

    private void manageActionStateCode() {
        String tempActFromStateNumericCode = "", tempActToStateNumericCode = "";
        String billToInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtBuyerGstin.getText()), DmShared.xNull(txtBuyerTradeName.getText()), DmShared.xNull(txtBuyerAddress1.getText()), DmShared.xNull(txtBuyerAddress2.getText()), DmShared.xNull(txtHBuyerCity.getText()), DmShared.xNull(txtBuyerPinCode.getText()), DmShared.xNull(txtBuyerState.getText()), DmShared.xNull(txtBuyerCountry.getText())), shipToInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtShipToPartyGstin.getText()), DmShared.xNull(txtShipToPartyTradeName.getText()), DmShared.xNull(txtShipToPartyAddress1.getText()), DmShared.xNull(txtShipToPartyAddress2.getText()), DmShared.xNull(txtHShipToPartyCity.getText()), DmShared.xNull(txtShipToPartyPinCode.getText()), DmShared.xNull(txtShipToPartyState.getText()), DmShared.xNull(txtShipToPartyCountry.getText()));
        String consignerInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtConsignerGstin.getText()), DmShared.xNull(txtConsignerTradeName.getText()), DmShared.xNull(txtConsignerAddress1.getText()), DmShared.xNull(txtConsignerAddress2.getText()), DmShared.xNull(txtHConsignerCity.getText()), DmShared.xNull(txtConsignerPinCode.getText()), DmShared.xNull(txtConsignerState.getText()), DmShared.xNull(txtConsignerCountry.getText())), dispatchFromInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtDispatcherGstin.getText()), DmShared.xNull(txtDispatcherTradeName.getText()), DmShared.xNull(txtDispatcherAddress1.getText()), DmShared.xNull(txtDispatcherAddress2.getText()), DmShared.xNull(txtHDispatcherCity.getText()), DmShared.xNull(txtDispatcherPinCode.getText()), DmShared.xNull(txtDispatcherState.getText()), DmShared.xNull(txtDispatcherCountry.getText()));
        tempActFromStateNumericCode = DmShared.xNull(txtConsignerState.getTag());
        tempActToStateNumericCode = DmShared.xNull(txtBuyerState.getTag());
        if (!DmShared.xNull(dispatchFromInfo).trim().equalsIgnoreCase("")) {
            if (!DmShared.xNull(dispatchFromInfo).equalsIgnoreCase(consignerInfo)) {
                tempActFromStateNumericCode = DmShared.xNull(txtDispatcherState.getTag());
            }
        }
        if (!DmShared.xNull(shipToInfo).trim().equalsIgnoreCase("")) {
            if (!DmShared.xNull(shipToInfo).equalsIgnoreCase(billToInfo)) {
                tempActToStateNumericCode = DmShared.xNull(txtShipToPartyState.getTag());
            }
        }
        actFromStateNumericCode = DmShared.xNull(tempActFromStateNumericCode);
        actToStateNumericCode = DmShared.xNull(tempActToStateNumericCode);
    }

    private void showSetting() {
        srvAsync.invokeRPC(true).getEgstParamSessionDetail(new AsyncCallback<DmResult<EGstParam_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EGstParam_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    settingPopup.show(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void handlerEvents() {

        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtLrDate.addBlurHandler(this);
        btnAuthentication.addClickHandler(this);
        btnGenrateEway.addClickHandler(this);
        btnCancelEway.addClickHandler(this);
        btnPrintEwb.addClickHandler(this);
        btnPrintDeatiledEwb.addClickHandler(this);
        btnGenrateEinv.addClickHandler(this);
        btnCancelEinv.addClickHandler(this);
        btnSaveGst.addClickHandler(this);
        btnGenrateEwayViaIrn.addClickHandler(this);
        btnSetting.addClickHandler(this);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtLrDate) {
            txtLrDate.setText(DmClient.returnDate(txtLrDate.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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
            this.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        eWay_GenerationMode = "";
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnAuthentication) {
            onAuthToken(DmEnum.EGST_ACTION.E_WAY_AUTHENTICATION.getCode());
        } else if (event.getSource() == btnGenrateEway) {
            eWay_GenerationMode = String.valueOf(EWAY_GenerationMode.E_WAY_GENERATED_DIRECTLY.getCode());
            processEgst(DmEnum.EGST_ACTION.E_WAY_GENRATE_BILL.getCode());
        } else if (event.getSource() == btnGenrateEinv) {
            if (isEWayBillRequired && DmShared.xNull(txtEWayBillNo.getText()).trim().equalsIgnoreCase("") && (isIntraState == false) && (isJobWork == false)) {
                new DmConfirmMessageBox("Do you want to create E-Way bill with E-Invoice?", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            eWay_GenerationMode = String.valueOf(EWAY_GenerationMode.E_WAY_GENERATED_WITH_E_INVOICE.getCode());
                            processEgst(DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode());
                        } else if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            processEgst(DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode());
                        }
                    }
                }).show();
            } else {
                processEgst(DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode());
            }
        } else if (event.getSource() == btnGenrateEwayViaIrn) {
            eWay_GenerationMode = String.valueOf(EWAY_GenerationMode.E_WAY_GENERATED_VIA_IRN.getCode());
            processEgst(DmEnum.EGST_ACTION.E_WAY_GENERATE_VIA_IRN.getCode());
        } else if (event.getSource() == btnCancelEway) {
            onCancel(DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getCode());
        } else if (event.getSource() == btnCancelEinv) {
            onCancel(DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode());
        } else if (event.getSource() == btnPrintEwb) {
            onPrint(DmEnum.EGST_ACTION.E_WAY_PRINT_BILL.getCode());
        } else if (event.getSource() == btnPrintDeatiledEwb) {
            onPrint(DmEnum.EGST_ACTION.E_WAY_PRINT_DETAILED_BILL.getCode());
        } else if (event.getSource() == btnSaveGst) {
            processEgst(DmEnum.EGST_ACTION.GST_UPDATE_DOCUMENTS.getCode());
        } else if (event.getSource() == btnSetting) {
            showSetting();
        }
    }

    private MasterEGst_Mdl fillRecords() {
        MasterEGst_Mdl masterEGst_Mdl = new MasterEGst_Mdl();
        manageActionStateCode();
        //<editor-fold defaultstate="collapsed" desc="EGST">
        egst_Mdl.setEwaySubSupplyTypeCode(DmShared.xNull(txtHEwaySubSupplyType.getTag()));
        egst_Mdl.setEwaySubSupplyTypeName(DmShared.xNull(txtHEwaySubSupplyType.getText()));
        egst_Mdl.setEwaySubSupplyDesc(DmShared.xNull(txtSubSupplyDesc.getText()));
        egst_Mdl.setEwayDocTypeCode(DmShared.xNull(txtHEwayDocumentType.getTag()));
        egst_Mdl.setEwayDocTypeName(DmShared.xNull(txtHEwayDocumentType.getText()));
        egst_Mdl.setEwayTransTypeCode(DmShared.xNull(txtHEwayTransactionType.getTag()));
        egst_Mdl.setEwayTransTypeName(DmShared.xNull(txtHEwayTransactionType.getText()));
        egst_Mdl.setEinvDocTypeCode(DmShared.xNull(txtHEinvoiceDocType.getTag()));
        egst_Mdl.setEinvDocTypeName(DmShared.xNull(txtHEinvoiceDocType.getText()));
        egst_Mdl.setEinvSupplyTypeCode(DmShared.xNull(txtHEinvoiceSupplyType.getTag()));
        egst_Mdl.setEinvSupplyTypeName(DmShared.xNull(txtHEinvoiceSupplyType.getText()));
        egst_Mdl.setEway_GenerationMode(eWay_GenerationMode);
        //<editor-fold defaultstate="collapsed" desc="ActionStateCode">
        egst_Mdl.setActFromStateNumericCode(actFromStateNumericCode);
        egst_Mdl.setActToStateNumericCode(actToStateNumericCode);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Transporter Details">
        egst_Mdl.setTransportLrNo(DmShared.xNull(txtLrNo.getText()));
        egst_Mdl.setTransportLrDate(DmShared.dNull(txtLrDate.getText()));
        Party_Mdl transporterInfo = new Party_Mdl();
        transporterInfo.setSharedKeyFieldCode(txtHTransporter.getTag());
        transporterInfo.setSharedKeyFieldName(txtHTransporter.getText());
        transporterInfo.setGstin(DmShared.xNull(transporterGstin));
        transporterInfo.setGstinDate(DmShared.dNull(txtTransporterGstinDate.getText()));
        transporterInfo.setTransporterId(DmShared.xNull(txtTransporterId.getText()));
        egst_Mdl.setTransporter_Mdl(transporterInfo);
        egst_Mdl.setVehicleNo(DmShared.xNull(txtVehicleNo.getText()));
        egst_Mdl.setTransportModeCode(DmShared.xNull(txtHTransportMode.getTag()));
        egst_Mdl.setTransportModeName(DmShared.xNull(txtHTransportMode.getText()));
        egst_Mdl.setVehicleTypeCode(DmShared.xNull(txtHVehicleType.getTag()));
        egst_Mdl.setVehicleTypeName(DmShared.xNull(txtHVehicleType.getText()));
        egst_Mdl.setGstVehicleType(DmShared.xNull(txtHVehicleType.getTag2nd()));
        egst_Mdl.setTransDistance(DmShared.xNull(txtDistance.getText()));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Consigner">
        Party_Mdl consigner = new Party_Mdl();
        consigner.setSharedKeyFieldCode(DmShared.xNull(txtConsignerName.getTag()));
        consigner.setSharedKeyFieldName(DmShared.xNull(txtConsignerName.getText()));
        consigner.setName(DmShared.xNull(txtConsignerName.getText()));
        consigner.setSiteCode(DmShared.xNull(txtConsignerName.getTag2nd()));
        consigner.setGstin(DmShared.xNull(txtConsignerGstin.getText()));
        consigner.setGstinDate(DmShared.dNull(txtConsignerGstinDate.getText()));
        consigner.setLegalName(DmShared.xNull(txtConsignerLegalName.getText()));
        consigner.setTradeName(DmShared.xNull(txtConsignerTradeName.getText()));
        consigner.setAddress1(DmShared.xNull(txtConsignerAddress1.getText()));
        consigner.setAddress2(DmShared.xNull(txtConsignerAddress2.getText()));
        consigner.setPinCode(DmShared.xNull(txtConsignerPinCode.getText()));
        consigner.setStateNumericCode(DmShared.xNull(txtConsignerState.getTag()));
        consigner.setStateName(DmShared.xNull(txtConsignerState.getText()));
        consigner.setPlaceOfSupplyCode(DmShared.xNull(txtConsignerState.getTag2nd()));
        consigner.setCityCode(DmShared.xNull(txtHConsignerCity.getTag()));
        consigner.setCityName(DmShared.xNull(txtHConsignerCity.getText()));
        consigner.setPhone(DmShared.xNull(txtConsignerPhone.getText()));
        consigner.setEmail(DmShared.xNull(txtConsignerEmail.getText()));
        consigner.setGstCountryCode(DmShared.xNull(txtConsignerCountry.getTag()));
        consigner.setCountryName(DmShared.xNull(txtConsignerCountry.getText()));
        egst_Mdl.setConsigner_Mdl(consigner);
        ////</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Dispatcher">
        Party_Mdl dispatcher = new Party_Mdl();
        dispatcher.setSharedKeyFieldCode(DmShared.xNull(txtDispatcherName.getTag()));
        dispatcher.setSharedKeyFieldName(DmShared.xNull(txtDispatcherName.getText()));
        dispatcher.setName(DmShared.xNull(txtDispatcherName.getText()));
        dispatcher.setSiteCode(DmShared.xNull(txtDispatcherName.getTag2nd()));
        dispatcher.setGstin(DmShared.xNull(txtDispatcherGstin.getText()));
        dispatcher.setGstinDate(DmShared.dNull(txtDispatcherGstinDate.getText()));
        dispatcher.setLegalName(DmShared.xNull(txtDispatcherLegalName.getText()));
        dispatcher.setTradeName(DmShared.xNull(txtDispatcherTradeName.getText()));
        dispatcher.setAddress1(DmShared.xNull(txtDispatcherAddress1.getText()));
        dispatcher.setAddress2(DmShared.xNull(txtDispatcherAddress2.getText()));
        dispatcher.setPinCode(DmShared.xNull(txtDispatcherPinCode.getText()));
        dispatcher.setStateNumericCode(DmShared.xNull(txtDispatcherState.getTag()));
        dispatcher.setStateName(DmShared.xNull(txtDispatcherState.getText()));
        dispatcher.setPlaceOfSupplyCode(DmShared.xNull(txtDispatcherState.getTag2nd()));
        dispatcher.setCityCode(DmShared.xNull(txtHDispatcherCity.getTag()));
        dispatcher.setCityName(DmShared.xNull(txtHDispatcherCity.getText()));
        dispatcher.setPhone(DmShared.xNull(txtDispatcherPhone.getText()));
        dispatcher.setEmail(DmShared.xNull(txtDispatcherEmail.getText()));
        dispatcher.setGstCountryCode(DmShared.xNull(txtDispatcherCountry.getTag()));
        dispatcher.setCountryName(DmShared.xNull(txtDispatcherCountry.getText()));
        egst_Mdl.setDispatchFrom_Mdl(dispatcher);
        ////</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="BillTo">
        Party_Mdl buyer = new Party_Mdl();
        buyer.setSharedKeyFieldCode(DmShared.xNull(txtBuyerName.getTag()));
        buyer.setSharedKeyFieldName(DmShared.xNull(txtBuyerName.getText()));
        buyer.setName(DmShared.xNull(txtBuyerName.getText()));
        buyer.setSiteCode(DmShared.xNull(txtBuyerName.getTag2nd()));
        buyer.setGstin(DmShared.xNull(txtBuyerGstin.getText()));
        buyer.setGstinDate(DmShared.dNull(txtBuyerGstinDate.getText()));
        buyer.setLegalName(DmShared.xNull(txtBuyerLegalName.getText()));
        buyer.setTradeName(DmShared.xNull(txtBuyerTradeName.getText()));
        buyer.setAddress1(DmShared.xNull(txtBuyerAddress1.getText()));
        buyer.setAddress2(DmShared.xNull(txtBuyerAddress2.getText()));
        buyer.setPinCode(DmShared.xNull(txtBuyerPinCode.getText()));
        buyer.setStateNumericCode(DmShared.xNull(txtBuyerState.getTag()));
        buyer.setStateName(DmShared.xNull(txtBuyerState.getText()));
        buyer.setPlaceOfSupplyCode(DmShared.xNull(txtBuyerState.getTag2nd()));
        buyer.setCityCode(DmShared.xNull(txtHBuyerCity.getTag()));
        buyer.setCityName(DmShared.xNull(txtHBuyerCity.getText()));
        buyer.setPhone(DmShared.xNull(txtBuyerPhone.getText()));
        buyer.setEmail(DmShared.xNull(txtBuyerEmail.getText()));
        buyer.setGstCountryCode(DmShared.xNull(txtBuyerCountry.getTag()));
        buyer.setCountryName(DmShared.xNull(txtBuyerCountry.getText()));
        egst_Mdl.setBillTo_Mdl(buyer);
        ////</editor-fold>
        ////<editor-fold defaultstate="collapsed" desc="Consignee">
        Party_Mdl consignee = new Party_Mdl();
        consignee.setSharedKeyFieldCode(DmShared.xNull(txtConsigneeName.getTag()));
        consignee.setSharedKeyFieldName(DmShared.xNull(txtConsigneeName.getText()));
        consignee.setName(DmShared.xNull(txtConsigneeName.getText()));
        consignee.setSiteCode(DmShared.xNull(txtConsigneeName.getTag2nd()));
        consignee.setGstin(DmShared.xNull(txtConsigneeGstin.getText()));
        consignee.setGstinDate(DmShared.dNull(txtConsigneeGstinDate.getText()));
        consignee.setLegalName(DmShared.xNull(txtConsigneeLegalName.getText()));
        consignee.setTradeName(DmShared.xNull(txtConsigneeTradeName.getText()));
        consignee.setAddress1(DmShared.xNull(txtConsigneeAddress1.getText()));
        consignee.setAddress2(DmShared.xNull(txtConsigneeAddress2.getText()));
        consignee.setPinCode(DmShared.xNull(txtConsigneePinCode.getText()));
        consignee.setStateNumericCode(DmShared.xNull(txtConsigneeState.getTag()));
        consignee.setStateName(DmShared.xNull(txtConsigneeState.getText()));
        consignee.setPlaceOfSupplyCode(DmShared.xNull(txtConsigneeState.getTag2nd()));
        consignee.setCityCode(DmShared.xNull(txtHConsigneeCity.getTag()));
        consignee.setCityName(DmShared.xNull(txtHConsigneeCity.getText()));
        consignee.setPhone(DmShared.xNull(txtConsigneePhone.getText()));
        consignee.setEmail(DmShared.xNull(txtConsigneeEmail.getText()));
        consignee.setGstCountryCode(DmShared.xNull(txtConsigneeCountry.getTag()));
        consignee.setCountryName(DmShared.xNull(txtConsigneeCountry.getText()));
        egst_Mdl.setConsignee_Mdl(consignee);
        ////</editor-fold>
        ////<editor-fold defaultstate="collapsed" desc="ShipTo">
        Party_Mdl shipTo = new Party_Mdl();
        shipTo.setSharedKeyFieldCode(DmShared.xNull(txtShipToPartyName.getTag()));
        shipTo.setSharedKeyFieldName(DmShared.xNull(txtShipToPartyName.getText()));
        shipTo.setName(DmShared.xNull(txtShipToPartyName.getText()));
        shipTo.setSiteCode(DmShared.xNull(txtShipToPartyName.getTag2nd()));
        shipTo.setGstin(DmShared.xNull(txtShipToPartyGstin.getText()));
        shipTo.setGstinDate(DmShared.dNull(txtShipToPartyGstinDate.getText()));
        shipTo.setLegalName(DmShared.xNull(txtShipToPartyLegalName.getText()));
        shipTo.setTradeName(DmShared.xNull(txtShipToPartyTradeName.getText()));
        shipTo.setAddress1(DmShared.xNull(txtShipToPartyAddress1.getText()));
        shipTo.setAddress2(DmShared.xNull(txtShipToPartyAddress2.getText()));
        shipTo.setPinCode(DmShared.xNull(txtShipToPartyPinCode.getText()));
        shipTo.setStateNumericCode(DmShared.xNull(txtShipToPartyState.getTag()));
        shipTo.setStateName(DmShared.xNull(txtShipToPartyState.getText()));
        shipTo.setCityCode(DmShared.xNull(txtHShipToPartyCity.getTag()));
        shipTo.setCityName(DmShared.xNull(txtHShipToPartyCity.getText()));
        shipTo.setPhone(DmShared.xNull(txtShipToPartyPhone.getText()));
        shipTo.setEmail(DmShared.xNull(txtShipToPartyEmail.getText()));
        shipTo.setGstCountryCode(DmShared.xNull(txtShipToPartyCountry.getTag()));
        shipTo.setCountryName(DmShared.xNull(txtShipToPartyCountry.getText()));
        egst_Mdl.setShipTo_Mdl(shipTo);
        //</editor-fold>
        masterEGst_Mdl.seteGst_Mdl(egst_Mdl);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="GST">
        gst_Mdl.setGstDocTypeCode(DmShared.xNull(txtHGstDocumentType.getTag()));
        gst_Mdl.setGstDocTypeName(DmShared.xNull(txtHGstDocumentType.getText()));
        gst_Mdl.setGstDocNatureCode(DmShared.xNull(txtHGstDocumentNature.getTag()));
        gst_Mdl.setGstDocNatureName(DmShared.xNull(txtHGstDocumentNature.getText()));
        masterEGst_Mdl.setGst_Mdl(gst_Mdl);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="TOKEN">
        masterEGst_Mdl.seteGstToken_Mdl(token_Mdl);
        //</editor-fold>

        return masterEGst_Mdl;
    }

    public void show(MasterEGst_Mdl masterEgstMdl, HashMap paramHashMap) {
        if (paramHashMap != null) {
            initializeControl(paramHashMap);
        }
        if (masterEgstMdl != null) {
            if (masterEgstMdl.geteGst_Mdl() != null) {
                this.egst_Mdl = new EGst_Mdl();
                this.egst_Mdl = masterEgstMdl.geteGst_Mdl();
            }
            if (masterEgstMdl.getGst_Mdl() != null) {
                this.gst_Mdl = new GstDocument_Mdl();
                this.gst_Mdl = masterEgstMdl.getGst_Mdl();
            }
            if (masterEgstMdl.geteGstToken_Mdl() != null) {
                this.token_Mdl = new EGstToken_Mdl();
                this.token_Mdl = masterEgstMdl.geteGstToken_Mdl();
            }
            displayData();
        } else {
            clear();
        }

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtEWayBillNo.setFocus(true);
            }
        });
    }

    public void hide() {
        if (callBack != null) {
            this.popupPanel.hide();
        }
    }

    private void manageAction() {
        flwQuickLink.clear();
        if (egst_Mdl != null) {
            if (!DmShared.xNull(txtEInvStatus.getTag()).trim().equalsIgnoreCase(DmEnum.Egst_Status.Cancelled.getCode())) {
                flwQuickLink.add(btnAuthentication);
                if (isEWayBillRequired) {
                    if (DmShared.xNull(egst_Mdl.getEway_BillNo()).trim().equalsIgnoreCase("")) {
                        flwQuickLink.add(btnGenrateEway);
                    } else {
                        flwQuickLink.add(btnCancelEway);
                        flwQuickLink.add(btnPrintEwb);
                        flwQuickLink.add(btnPrintDeatiledEwb);
                    }
                }
                if (isEInvoiceRequired) {
                    if (DmShared.xNull(egst_Mdl.getEinv_IrnNo()).trim().equalsIgnoreCase("")) {
                        flwQuickLink.add(btnGenrateEinv);
                    } else {
                        if ((isIntraState == false) && (isJobWork == false)) {
                            if (isEWayBillRequired && DmShared.xNull(egst_Mdl.getEway_BillNo()).trim().equalsIgnoreCase("")) {
                                flwQuickLink.add(btnGenrateEwayViaIrn);
                            }
                        }
                        flwQuickLink.add(btnCancelEinv);
                    }
                }
                if (isGstDocumentRequired) {
                    flwQuickLink.add(btnSaveGst);
                }
            }
        }
    }

    private void validateEgst(final int actionCode) throws DmException {
        try {
            boolean isConfirmBox = false;
            String confirmBoxMessage = "";
            if (isEWayBillRequired && !DmShared.xNull(eWay_GenerationMode).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(txtHEwaySubSupplyType.getTag()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("E-way Sub Supply Type is mandatory!", txtHEwaySubSupplyType).show();
                    return;
                }
                if (DmShared.iNull(txtHEwaySubSupplyType.getTag2nd()).intValue() == DmEnum.Eway_SubSupplyType.OTHERS.getCode()) {
                    if (DmShared.xNull(txtSubSupplyDesc.getText()).trim().equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Sub Supply Type is mentioned as Others, the description for that is mandatory!", txtSubSupplyDesc).show();
                        return;
                    }
                }
                if (DmShared.xNull(txtHEwayDocumentType.getTag()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("E-way Doc. Type is mandatory!", txtHEwayDocumentType).show();
                    return;
                }
                if (DmShared.xNull(txtHEwayTransactionType.getTag()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("E-way Transaction Type is mandatory!", txtHEwayTransactionType).show();
                    return;
                }
                if (DmShared.xNull(txtHTransporter.getTag()).trim().equalsIgnoreCase("")) {
                    tabPanel.selectTab(1);
                    new DmAlertMessageBox("Transporter is mandatory !", txtHTransporter).show();
                    return;
                }
                if (DmShared.iNull(txtHTransportMode.getTag()).intValue() == TransportMode.BY_AIR.getId()
                        || DmShared.iNull(txtHTransportMode.getTag()).intValue() == TransportMode.BY_RAIL.getId()
                        || DmShared.iNull(txtHTransportMode.getTag()).intValue() == TransportMode.BY_SHIP.getId()) {
                    if (DmShared.xNull(txtLrNo.getText()).trim().equalsIgnoreCase("")) {
                        tabPanel.selectTab(1);
                        new DmAlertMessageBox("LR No. is mandatory !", txtLrNo).show();
                        return;
                    } else if (DmShared.dNull(txtLrDate.getText()).trim().equalsIgnoreCase("")) {
                        tabPanel.selectTab(1);
                        new DmAlertMessageBox("LR Date is mandatory !", txtLrDate).show();
                        return;
                    }
                }
                int tempTransactionTypeCode = DmEnum.Eway_TransactionType.Regular.getCode();
                String consignerInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtConsignerGstin.getText()), DmShared.xNull(txtConsignerTradeName.getText()), DmShared.xNull(txtConsignerAddress1.getText()), DmShared.xNull(txtConsignerAddress2.getText()), DmShared.xNull(txtHConsignerCity.getText()), DmShared.xNull(txtConsignerPinCode.getText()), DmShared.xNull(txtConsignerState.getText()), DmShared.xNull(txtConsignerCountry.getText())), dispatchFromInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtDispatcherGstin.getText()), DmShared.xNull(txtDispatcherTradeName.getText()), DmShared.xNull(txtDispatcherAddress1.getText()), DmShared.xNull(txtDispatcherAddress2.getText()), DmShared.xNull(txtHDispatcherCity.getText()), DmShared.xNull(txtDispatcherPinCode.getText()), DmShared.xNull(txtDispatcherState.getText()), DmShared.xNull(txtDispatcherCountry.getText()));
                if (!DmShared.xNull(dispatchFromInfo).equalsIgnoreCase(consignerInfo)) {
                    tempTransactionTypeCode = DmEnum.Eway_TransactionType.Bill_From_Ship_From.getCode();
                }
                String billToInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtBuyerGstin.getText()), DmShared.xNull(txtBuyerTradeName.getText()), DmShared.xNull(txtBuyerAddress1.getText()), DmShared.xNull(txtBuyerAddress2.getText()), DmShared.xNull(txtHBuyerCity.getText()), DmShared.xNull(txtBuyerPinCode.getText()), DmShared.xNull(txtBuyerState.getText()), DmShared.xNull(txtBuyerCountry.getText())), shipToInfo = Gst_Shared.concatPartyInfo(DmShared.xNull(txtShipToPartyGstin.getText()), DmShared.xNull(txtShipToPartyTradeName.getText()), DmShared.xNull(txtShipToPartyAddress1.getText()), DmShared.xNull(txtShipToPartyAddress2.getText()), DmShared.xNull(txtHShipToPartyCity.getText()), DmShared.xNull(txtShipToPartyPinCode.getText()), DmShared.xNull(txtShipToPartyState.getText()), DmShared.xNull(txtShipToPartyCountry.getText()));
                if (!DmShared.xNull(billToInfo).equalsIgnoreCase(shipToInfo)) {
                    if (tempTransactionTypeCode == DmEnum.Eway_TransactionType.Regular.getCode()) {
                        tempTransactionTypeCode = DmEnum.Eway_TransactionType.Bill_To_Ship_To.getCode();
                    } else {
                        tempTransactionTypeCode = DmEnum.Eway_TransactionType.Combination_OF_2_And_3.getCode();
                    }
                }
                if (DmShared.iNull(txtHEwayTransactionType.getTag()).intValue() != tempTransactionTypeCode) {
                    isConfirmBox = true;
                    confirmBoxMessage += "<br><br><br>" + Gst_Shared.concatPartyInfoForView(DmShared.xNull("Consigner To"), DmShared.xNull(txtConsignerGstin.getText()), DmShared.xNull(txtConsignerName.getText()), DmShared.xNull(txtConsignerAddress1.getText()), DmShared.xNull(txtConsignerAddress2.getText()), DmShared.xNull(txtHConsignerCity.getText()), DmShared.xNull(txtConsignerPinCode.getText()), DmShared.xNull(txtConsignerState.getText()), DmShared.xNull(txtConsignerCountry.getText()));
                    confirmBoxMessage += "<br><br>" + Gst_Shared.concatPartyInfoForView(DmShared.xNull("Dispatch From"), DmShared.xNull(txtDispatcherGstin.getText()), DmShared.xNull(txtDispatcherName.getText()), DmShared.xNull(txtDispatcherAddress1.getText()), DmShared.xNull(txtDispatcherAddress2.getText()), DmShared.xNull(txtHDispatcherCity.getText()), DmShared.xNull(txtDispatcherPinCode.getText()), DmShared.xNull(txtDispatcherState.getText()), DmShared.xNull(txtDispatcherCountry.getText()));
                    confirmBoxMessage += "<br><br>" + Gst_Shared.concatPartyInfoForView(DmShared.xNull("Bill To"), DmShared.xNull(txtBuyerGstin.getText()), DmShared.xNull(txtBuyerName.getText()), DmShared.xNull(txtBuyerAddress1.getText()), DmShared.xNull(txtBuyerAddress2.getText()), DmShared.xNull(txtHBuyerCity.getText()), DmShared.xNull(txtBuyerPinCode.getText()), DmShared.xNull(txtBuyerState.getText()), DmShared.xNull(txtBuyerCountry.getText()));
                    confirmBoxMessage += "<br><br>" + Gst_Shared.concatPartyInfoForView(DmShared.xNull("Ship To"), DmShared.xNull(txtShipToPartyGstin.getText()), DmShared.xNull(txtShipToPartyName.getText()), DmShared.xNull(txtShipToPartyAddress1.getText()), DmShared.xNull(txtShipToPartyAddress2.getText()), DmShared.xNull(txtHShipToPartyCity.getText()), DmShared.xNull(txtShipToPartyPinCode.getText()), DmShared.xNull(txtShipToPartyState.getText()), DmShared.xNull(txtShipToPartyCountry.getText()));
                    confirmBoxMessage += "<br>E-way Transaction type should be <b>" + DmEnum.Eway_TransactionType.getViaCode(tempTransactionTypeCode)
                            + "</b> <br> Are You sure want to continue?";
                }
            }

            if (isConfirmBox) {
                new DmConfirmMessageBox(confirmBoxMessage, new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            MasterEGst_Mdl masterEGst_Mdl = fillRecords();
                            processEgst(masterEGst_Mdl, actionCode);
                        } else if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            txtHEwayTransactionType.setFocus(true);
                        }
                    }
                }).show();
            } else {
                MasterEGst_Mdl masterEGst_Mdl = fillRecords();
                processEgst(masterEGst_Mdl, actionCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void onAuthToken(int actionCode) {

        fillRecords();
        srvAsync.invokeRPC().getAuthToken(actionCode, new AsyncCallback<DmResult<EGstToken_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EGstToken_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    token_Mdl = result.getData();
                    displayData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void processEgst(final int actionCode) {
        if (egst_Mdl != null) {
            //<editor-fold defaultstate="collapsed" desc="Validate Egst">
            if (actionCode == DmEnum.EGST_ACTION.E_WAY_GENRATE_BILL.getCode()
                    || actionCode == DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode()
                    || actionCode == DmEnum.EGST_ACTION.E_WAY_GENERATE_VIA_IRN.getCode()) {
                validateEgst(actionCode);
            } else {
                MasterEGst_Mdl masterEGst_Mdl = fillRecords();
                processEgst(masterEGst_Mdl, actionCode);
            }
            //</editor-fold>
        } else {
            new DmAlertMessageBox("E-GST record not found!", (DmTextBox) null).show();
            hide();
        }
    }

    private void processEgst(final MasterEGst_Mdl masterEGst_Mdl, final int actionCode) {
        srvEGstPopupAsync.invokeRPC().processEGst(masterEGst_Mdl, actionCode, new AsyncCallback<DmResult<MasterEGst_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MasterEGst_Mdl> result) {
                MasterEGst_Mdl masterEGst_Mdl = result.getData();
                String message = "";
                if (masterEGst_Mdl != null && result.isStatus()) {
                    if (masterEGst_Mdl.geteGst_Mdl() != null) {
                        egst_Mdl = masterEGst_Mdl.geteGst_Mdl();
                    }
                    if (masterEGst_Mdl.getGst_Mdl() != null) {
                        gst_Mdl = masterEGst_Mdl.getGst_Mdl();
                    }
                    if (masterEGst_Mdl.geteGstToken_Mdl() != null) {
                        token_Mdl = masterEGst_Mdl.geteGstToken_Mdl();
                    }
                    displayData();
                    callBack.onOk(masterEGst_Mdl.geteGst_Mdl());
                    if (actionCode == DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode()) {
                        message = "E-invoice bill generated successfully!";
                    } else if (actionCode == DmEnum.EGST_ACTION.E_WAY_GENRATE_BILL.getCode()) {
                        message = "E-way bill generated successfully!";
                    } else if (actionCode == DmEnum.EGST_ACTION.E_WAY_GENERATE_VIA_IRN.getCode()) {
                        message = "E-way bill via IRN generated successfully!";
                    } else if (actionCode == DmEnum.EGST_ACTION.GST_UPDATE_DOCUMENTS.getCode()) {
                        message = "GST record save successfully!";
                    } else if (actionCode == DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getCode()) {
                        message = "E-Way cancelled successfully.";
                    } else if (actionCode == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
                        message = "E-Invoice cancelled successfully.";
                    }
                    new DmAlertMessageBox(message, (DmTextBox) null).show();
                    hide();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onCancel(final int actionCode) {
        egstCancelPopup.show(DmShared.iNull(actionCode).intValue());
    }

    private void onPrint(final int actionCode) {

        srvAsync.invokeRPC().print(DmShared.xNull(txtEWayBillNo.getText()), actionCode, new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    try {
                        KeyFieldExtended field = result.getData();
                        startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void startDownloading(String fileName, String filePath) throws Exception {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void initializeControl(HashMap paramHashMap) {
        if (paramHashMap.containsKey("TRANSPORTER")) {
            isTransporterEditable = (Boolean) paramHashMap.get("TRANSPORTER");
        }
        if (paramHashMap.containsKey("IS_INTRA_STATE")) {
            isIntraState = (Boolean) paramHashMap.get("IS_INTRA_STATE");
        }
        if (paramHashMap.containsKey("IS_JOB_WORK")) {
            isJobWork = (Boolean) paramHashMap.get("IS_JOB_WORK");
        }
        if (paramHashMap.containsKey("TRANSPORTER_DETAIL")) {
            isTransporterDetailEditable = (Boolean) paramHashMap.get("TRANSPORTER_DETAIL");
        }
        if (paramHashMap.containsKey("EWAY_DOCTYPE")) {
            isEwayDocTypeEditable = (Boolean) paramHashMap.get("EWAY_DOCTYPE");
        }
        if (paramHashMap.containsKey("EWAY_SUBSUPPLYTYPE")) {
            isEwaySubSupplyTypeEditable = (Boolean) paramHashMap.get("EWAY_SUBSUPPLYTYPE");
        }
        if (paramHashMap.containsKey("IS_EWAY_TRANSACTION_TYPE")) {
            isEwayTransactionTypeEditable = (Boolean) paramHashMap.get("IS_EWAY_TRANSACTION_TYPE");
        }
        if (paramHashMap.containsKey("EINV_SUPPLYTYPE")) {
            isEinvoiceSupplyTypeEditable = (Boolean) paramHashMap.get("EINV_SUPPLYTYPE");
        }
        if (paramHashMap.containsKey("EINV_DOCTYPE")) {
            isEInvoiceDocTypeEditable = (Boolean) paramHashMap.get("EINV_DOCTYPE");
        }
        if (paramHashMap.containsKey("GST_DOCUMENT")) {
            isGstDocumentRequired = (Boolean) paramHashMap.get("GST_DOCUMENT");
        }
        if (paramHashMap.containsKey("IS_DISPATCHER_ADDRESS_EDITABLE")) {
            isDispatcherAddressEditable = (Boolean) paramHashMap.get("IS_DISPATCHER_ADDRESS_EDITABLE");
        }
        if (paramHashMap.containsKey("IS_BUYER_ADDRESS_EDITABLE")) {
            isBuyerAddressEditable = (Boolean) paramHashMap.get("IS_BUYER_ADDRESS_EDITABLE");
        }
        if (paramHashMap.containsKey("IS_CONSIGNEE_ADDRESS_EDITABLE")) {
            isConsigneeAddressEditable = (Boolean) paramHashMap.get("IS_CONSIGNEE_ADDRESS_EDITABLE");
        }
        if (paramHashMap.containsKey("IS_SHIP_TO_ADDRESS_EDITABLE")) {
            isShipToAddressEditable = (Boolean) paramHashMap.get("IS_SHIP_TO_ADDRESS_EDITABLE");
        }
    }

}
