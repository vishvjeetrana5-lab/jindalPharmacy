package aarogya.selfToolkit.serviceGrid.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;
import aarogya.selfToolkit.serviceGrid.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Srv;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_SrvAsync;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class ServiceGrid extends ServiceGrid_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents {

    private final ServiceGridEdit serviceDetailGridEdit = new ServiceGridEdit(grdServiceDetail, this);
    private HashMap param;
    private int serviceRowIndex = -1;
    protected String mode = "";
    protected String location = "";
    protected boolean isUnionTerritory = false;
    protected boolean isInGSTS = false;
    protected boolean isRcmApplicable = false;
    protected boolean isIncludeTaxInCost = false;
    protected boolean isInventoryAccountingActive = false;
    protected String commonUnitQty = "", goodsTaxableValue = "";
    protected DefaultValue_Mdl dvm = new DefaultValue_Mdl();
    protected boolean isManualGstRequired = false;
    protected boolean isManualTaxRequired = false;
    protected boolean isServiceAcMandatory = false;
    protected boolean isTaxAcMandatory = false;
    protected boolean isServiceGridActive = false;

    ServiceInvoker<ServiceGrid_SrvAsync> SrvAsync;

    public int getServiceRowIndex() {
        return serviceRowIndex;
    }

    public void setServiceRowIndex(int serviceRowIndex) {
        this.serviceRowIndex = serviceRowIndex;
    }

    public ServiceGrid(int width, int height) {
        super(width, height);
        SrvAsync = new ServiceInvoker<>(GWT.create(ServiceGrid_Srv.class), ServiceGrid.class.getName());
        this.isServiceGridActive=true;
        gridEvent();
        iniDefaultValues();
    }
    
    public ServiceGrid(int width, int height,boolean isServiceGridActive) {
        super(width, height);
        SrvAsync = new ServiceInvoker<>(GWT.create(ServiceGrid_Srv.class), ServiceGrid.class.getName());
        gridEvent();
        this.isServiceGridActive=isServiceGridActive;
        iniDefaultValues();
    }

    public void initialize(HashMap paramHashMap) {
        this.param = paramHashMap;
        this.setServiceRowIndex(-1);

        //Entry Mode
        if (param.containsKey("MODE")) {
            mode = (String) param.get("MODE");
        }

        //Location
        if (param.containsKey("LOCATION")) {
            location = (String) param.get("LOCATION");
        }

        //UT
        if (param.containsKey("UT")) {
            isUnionTerritory = (Boolean) param.get("UT");

        }

        //is IN GSTS
        if (param.containsKey("IS_IN_GSTS")) {
            isInGSTS = (Boolean) param.get("IS_IN_GSTS");
        }

        //is RCM Applicable
        if (param.containsKey("IS_RCM_APPLICABLE")) {
            isRcmApplicable = (Boolean) param.get("IS_RCM_APPLICABLE");
        }

        //is include tax in cost
        if (param.containsKey("IS_INCLUDE_TAX_IN_COST")) {
            isIncludeTaxInCost = (Boolean) param.get("IS_INCLUDE_TAX_IN_COST");
        }

        //is Inventroy Accounting Active
        if (param.containsKey("IS_INVENTROY_ACCOUNTING_ACTIVE")) {
            isInventoryAccountingActive = (Boolean) param.get("IS_INVENTROY_ACCOUNTING_ACTIVE");
        }

        //Goods taxable value
        if (param.containsKey("GOODS_TAXABLE_VALUE")) {
            goodsTaxableValue = (String) param.get("GOODS_TAXABLE_VALUE");
        }

        //common unit qty
        if (param.containsKey("COMMON_UNIT_QTY")) {
            commonUnitQty = (String) param.get("COMMON_UNIT_QTY");
        }
        //Manual GST Type
        if (param.containsKey("IS_MANUAL_GST_TYPE_REQUIRED")) {
            isManualGstRequired = (Boolean) param.get("IS_MANUAL_GST_TYPE_REQUIRED");
        }
        //Manual Tax Type
        if (param.containsKey("IS_MANUAL_TAX_REQUIRED")) {
            isManualTaxRequired = (Boolean) param.get("IS_MANUAL_TAX_REQUIRED");
        }
        //Is Service A/c Mandatory
        if (param.containsKey("IS_SERVICE_AC_MANDATORY")) {
            isServiceAcMandatory = (Boolean) param.get("IS_SERVICE_AC_MANDATORY");
        }
        //Is Tax A/c Mandatory
        if (param.containsKey("IS_TAX_AC_MANDATORY")) {
            isTaxAcMandatory = (Boolean) param.get("IS_TAX_AC_MANDATORY");
        }
        isUT();
        setCaption();

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();

        }
    }

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void onOk(ServiceDetail_Mdl serviceDetail_Mdl);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    //</editor-fold>    
    private void gridEvent() {
        grdServiceDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (param != null && isServiceGridActive) {
                    showServiceEntryGridEdit(event.getRowIndex());
                }
            }
        });

        grdServiceDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showServiceEntryGridEdit(grdServiceDetail.getGrid().getStore().indexOf(grdServiceDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        serviceDetailGridEdit.addCallBack(new ServiceGridEdit.CallBack() {
            @Override
            public void onOk(ServiceDetail_Mdl serviceDetail_Mdl) {

                callBack.onOk(serviceDetail_Mdl);
            }

            @Override
            public void onDelete() {

                callBack.onDelete();
            }
        });
    }

    private void showServiceEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(location).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Place of Supply is mandatory.", (DmTextBox) null).show();
            return;
        }

        if (mode.trim().equalsIgnoreCase("Browse")) {
            serviceDetailGridEdit.show(currentRowIndex, false, false, false);
        } else {
            serviceDetailGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    public DmGrid getServiceGrid() {
        return grdServiceDetail;
    }

    @Override
    public Widget asWidget() {
        return flwWgt;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            serviceDetailGridEdit.hide();
        }
    }

    private void isUT() {
        if (param.containsKey("LOCATION")) {
            if (DmShared.iNull((String) param.get("LOCATION")).intValue() == DmEnum.DmLocation.UnionTerritory.getId()) {
                sgstUgstCaption = STEnum.OHManualCode.UGST.getManualCode();
            } else {
                sgstUgstCaption = STEnum.OHManualCode.SGST.getManualCode();
            }
        }
    }

    private void setCaption() {
        DmColumnConfig col = ((DmColumnConfig) grdServiceDetail.getGrid().getColumnModel().getColumn(this.colIndex));
        col.setHeader(sgstUgstCaption);
        if (grdServiceDetail.getGrid().getView().getHeader() != null) {
            grdServiceDetail.getGrid().getView().getHeader().refresh();
        }
    }

    private void iniDefaultValues() {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    dvm = result.getData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void calculate(ServiceDetail_Mdl mrTemp) {
        //Evaluate Amount 
        if (DmShared.xNull(mrTemp.getServiceEvalTypeCode()).equalsIgnoreCase(DmShared.iNull(DmEnum.ServiceEvalType.FREE_VALUE.getCode()).toString())) {
            mrTemp.setAmount(DmShared.xNull(mrTemp.getServiceEvalRate()));
        } else if (DmShared.xNull(mrTemp.getServiceEvalTypeCode()).equalsIgnoreCase(DmShared.iNull(DmEnum.ServiceEvalType.PERCENTAGE_OF_GOODS_TAXABLE_VALUE.getCode()).toString())) {
            mrTemp.setAmount(DmClient.vNull(DmShared.vNull(goodsTaxableValue).doubleValue() * DmShared.vNull(mrTemp.getServiceEvalRate()).doubleValue() * .01, "0.00"));
        } else if (DmShared.xNull(mrTemp.getServiceEvalTypeCode()).equalsIgnoreCase(DmShared.iNull(DmEnum.ServiceEvalType.PER_UNIT.getCode()).toString())) {
            mrTemp.setAmount(DmClient.vNull(DmShared.vNull(commonUnitQty).doubleValue() * DmShared.vNull(mrTemp.getServiceEvalRate()).doubleValue(), "0.00"));
        }

        //Set Rate & Quantity
        mrTemp.setRate(DmClient.vNull(mrTemp.getAmount(), "0.00"));
        mrTemp.setQuantity("1");

        //Taxable Value
        mrTemp.setTaxableAmt(mrTemp.getAmount());

        double sgstUgstAmt = (DmShared.vNull(mrTemp.getTaxableAmt()).doubleValue() * DmShared.vNull(mrTemp.getSgstOrUgstRate()).doubleValue() * 0.01);
        double cgstAmt = (DmShared.vNull(mrTemp.getTaxableAmt()).doubleValue() * DmShared.vNull(mrTemp.getCgstRate()).doubleValue() * 0.01);
        double igstAmt = (DmShared.vNull(mrTemp.getTaxableAmt()).doubleValue() * DmShared.vNull(mrTemp.getIgstRate()).doubleValue() * 0.01);

        if (!mrTemp.isManualTax()) {
            mrTemp.setSgstOrUgstAmt(STClient.getRoundedAmt(sgstUgstAmt, dvm.getAc_RoundOffGST()));
        }
        mrTemp.setSgstRate("");
        mrTemp.setSgstAmt("");
        mrTemp.setUgstRate("");
        mrTemp.setUgstAmt("");
        if (!isUnionTerritory) {
            mrTemp.setSgstRate(DmShared.xNull(mrTemp.getSgstOrUgstRate()));
            mrTemp.setSgstAmt(DmShared.xNull(mrTemp.getSgstOrUgstAmt()));
        } else if (isUnionTerritory) {
            mrTemp.setUgstRate(DmShared.xNull(mrTemp.getSgstOrUgstRate()));
            mrTemp.setUgstAmt(DmShared.xNull(mrTemp.getSgstOrUgstAmt()));
        }
        if (!mrTemp.isManualTax()) {
            mrTemp.setCgstAmt(STClient.getRoundedAmt(cgstAmt, dvm.getAc_RoundOffGST()));
            mrTemp.setIgstAmt(STClient.getRoundedAmt(igstAmt, dvm.getAc_RoundOffGST()));
        }

        mrTemp.setNetAmt(DmClient.vNull(DmShared.vNull(mrTemp.getAmount()).doubleValue()
                + DmShared.vNull(mrTemp.getSgstOrUgstAmt()).doubleValue()
                + DmShared.vNull(mrTemp.getCgstAmt()).doubleValue()
                + DmShared.vNull(mrTemp.getIgstAmt()).doubleValue(), "0.00"));
        mrTemp.setGstRate(DmClient.vNull(DmShared.vNull(mrTemp.getSgstOrUgstRate())
                + DmShared.vNull(mrTemp.getCgstRate())
                + DmShared.vNull(mrTemp.getIgstRate()), "0.00"));

    }

}
