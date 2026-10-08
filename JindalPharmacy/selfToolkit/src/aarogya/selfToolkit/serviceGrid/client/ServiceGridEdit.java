package aarogya.selfToolkit.serviceGrid.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.GSTS_Head;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.List;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.TxtHServiceDetail_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Srv;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_SrvAsync;
import aarogya.selfToolkit.stStatic.client.STClient;

public class ServiceGridEdit<T1, T2> extends ServiceGridEdit_Dsg<T1, T2> implements BlurHandler, ClickHandler {

    private ServiceInvoker<ServiceGrid_SrvAsync> srvAsync;
    private int hashCode = 0;
    String skuCode = "", skuName = "",
            itsBasedOn = "", v_SNo = "", itemGSTNature = "", gstUnitCode = "", unitCode = "";
    String selectedItemCode = "", iSubCode = "";
    List<GSTS_Mdl> lstITS;
    private ServiceGrid serviceGrid = null;
    private String gstRate = "", quantity = "";

    private void manageEvent() {
        txtServiceEvalRate.addBlurHandler(this);
        txtAmount.addBlurHandler(this);
        txtSgstUgstPer.addBlurHandler(this);
        txtSgstUgst.addBlurHandler(this);
        txtCGSTPer.addBlurHandler(this);
        txtCGST.addBlurHandler(this);
        txtIGSTPer.addBlurHandler(this);
        txtIGST.addBlurHandler(this);

        cbIsManualGstType.addClickHandler(this);
        cbIsManualTax.addClickHandler(this);
    }

    @Override
    public void onBlur(com.google.gwt.event.dom.client.BlurEvent event) {
        if (event.getSource().equals(txtAmount)
                || event.getSource() == txtSgstUgstPer
                || event.getSource() == txtSgstUgst
                || event.getSource() == txtCGSTPer
                || event.getSource() == txtCGST
                || event.getSource() == txtIGSTPer
                || event.getSource() == txtIGST
                || event.getSource() == txtServiceEvalRate) {
            calculate();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Callback">
    interface CallBack {

        public void onOk(ServiceDetail_Mdl serviceDetail_Mdl);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    //</editor-fold>
    public ServiceGridEdit(DmGrid<T1, T2> grid, ServiceGrid serviceGrid) {
        super(grid, "Service Detail", "267px", "230px");
        this.serviceGrid = serviceGrid;
        srvAsync = new ServiceInvoker<>(GWT.create(ServiceGrid_Srv.class), ServiceGrid.class.getName());
        textHelpEvents();
        manageDisplay();
        manageEvent();
    }

    private void textHelpEvents() {
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHServiceDetail_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHServiceDetail_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHServiceDetail_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHServiceDetail_Mdl t1Row) {
                if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                    resetItem();
                    selectedItemCode = DmShared.xNull(txtHItemName.getTag());
                }

                if (blnStatus && t1Row != null) {
                    txtHItemName.setTag2nd(t1Row.getSmfCode());
                    txtHServiceEvalType.setText(t1Row.getServiceEvalTypeName());
                    txtHServiceEvalType.setTag(t1Row.getServiceEvalTypeCode());
                    txtServiceEvalRate.setText(DmShared.xNull(t1Row.getServiceEvalRate()));
                    txtHsn.setTag(DmShared.xNull(t1Row.getHsnCode()));
                    txtHsn.setText(DmShared.xNull(t1Row.getHsn()));
                    iSubCode = DmShared.xNull(t1Row.getIsubCode());
                    itemGSTNature = DmShared.xNull(t1Row.getItemGSTNature());
                    gstUnitCode = DmShared.xNull(t1Row.getGstUnitCode());
                    itsBasedOn = DmShared.iNull(t1Row.getItsBasedOn()).toString();

                    unitCode = DmShared.xNull(t1Row.getSkuCode());
                    getITS(t1Row.getSharedKeyFieldCode(), txtAmount.getText(), DmShared.iNull(t1Row.getItsBasedOn()).toString(), serviceGrid.location, "", cbIsManualGstType.getValue());
                }
                calculate();
            }
        });

        txtHGstType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGstType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGstType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHGstType.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHGstType.getTag()))) {
                    resetItemType();
                    txtHGstType.setTag2nd(DmShared.xNull(txtHGstType.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    //getITS
                    getITS(t1Row.getSharedKeyFieldCode(), txtAmount.getText(), itsBasedOn, serviceGrid.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
                }
            }
        });

        txtHServiceEvalType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHValuationType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceEvalType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceEvalType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceEvalType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                calculate();
            }
        });

    }

    private void getITS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, final boolean isManualITS) {
        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, itsBasedOn, locationCode, gstTypeCode, isManualITS, new AsyncCallback<DmResult<List<GSTS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GSTS_Mdl>> result) {
                if (result.isStatus() && result.getData() != null) {
                    if (!isManualITS) {
                        txtHGstType.setText(result.getValue2());
                        txtHGstType.setTag(result.getValue1());
                        txtHGstType.setTag2nd(result.getValue1());
                    }
                    lstITS = result.getData();
                    setITS(lstITS, serviceGrid.isInGSTS);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                }
            }
        });
    }

    private void setITS(List<GSTS_Mdl> lstITS, boolean isInITS) {
        String taxPer = "", taxAcCode = "", rcmAcCode = "";
        resetTaxDetail();
        if (lstITS != null && lstITS.size() > 0) {
            for (GSTS_Mdl iTS_Mdl : lstITS) {
                taxPer = "";
                taxAcCode = "";
                rcmAcCode = "";

                if (serviceGrid.isIncludeTaxInCost) {
                    if (!serviceGrid.isRcmApplicable) {
                        taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");
                        if (!serviceGrid.isInventoryAccountingActive) {
                            taxAcCode = DmShared.xNull(iSubCode);
                        }
                    }
                } else {
                    taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");
                    rcmAcCode = DmShared.xNull(iTS_Mdl.getRcmAcCode());
                    if (isInITS) {
                        taxAcCode = DmShared.xNull(iTS_Mdl.getInputTaxAcCode());
                    } else {
                        taxAcCode = DmShared.xNull(iTS_Mdl.getOutputTaxAcCode());
                    }

                }

                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.SGST.getShortName()) && !serviceGrid.isUnionTerritory) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtSGSTPer.setText(taxPer);
                    txtSGSTPer.setTag(rcmAcCode);
                    txtSGST.setTag(taxAcCode);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.UGST.getShortName()) && serviceGrid.isUnionTerritory) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtUGSTPer.setText(taxPer);
                    txtUGSTPer.setTag(rcmAcCode);
                    txtUGST.setTag(taxAcCode);
                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.CGST.getShortName())) {
                    txtCGSTPer.setText(taxPer);
                    txtCGSTPer.setTag(rcmAcCode);
                    txtCGST.setTag(taxAcCode);
                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.IGST.getShortName())) {
                    txtIGSTPer.setText(taxPer);
                    txtIGSTPer.setTag(rcmAcCode);
                    txtIGST.setTag(taxAcCode);
                }
            }
        }
        calculate();
    }

    private void calculate() {
        //Reset Values
        txtSGSTPer.setText("");
        txtSGST.setText("");
        txtUGSTPer.setText("");
        txtUGST.setText("");
        txtAmount.setText("");
        txtSgstUgst.setTag2nd("");
        txtCGST.setTag2nd("");
        txtIGST.setTag2nd("");

        if (cbIsManualTax.getValue()) {
            txtSgstUgst.setTag2nd(txtSgstUgst.getText());
            txtCGST.setTag2nd(txtCGST.getText());
            txtIGST.setTag2nd(txtIGST.getText());
        }

        //Initialize New Model for Calculation
        ServiceDetail_Mdl ourRow = new ServiceDetail_Mdl();
        ourRow.setServiceEvalTypeCode(DmShared.xNull(txtHServiceEvalType.getTag()));
        ourRow.setServiceEvalRate(DmShared.xNull(txtServiceEvalRate.getText()));
        ourRow.setManualTax(cbIsManualTax.getValue());
        ourRow.setSgstOrUgstRate(DmShared.xNull(txtSgstUgstPer.getText()));
        ourRow.setSgstRate(DmShared.xNull(txtSGSTPer.getText()));
        ourRow.setUgstRate(txtUGSTPer.getText());
        ourRow.setCgstRate(DmShared.xNull(txtCGSTPer.getText()));
        ourRow.setIgstRate(DmShared.xNull(txtIGSTPer.getText()));

        //calculate values
        serviceGrid.calculate(ourRow);

        //Amount
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        //Set Rate & Qty
        txtAmount.setTag2nd(DmClient.vNull(ourRow.getRate(), "0.00"));
        quantity = DmShared.xNull(ourRow.getQuantity());

        //Taxable Value
        txtAmount.setTag(DmClient.vNull(ourRow.getTaxableAmt(), "0.00"));

        //GST Rate
        gstRate = DmClient.vNull(ourRow.getGstRate(), "0.00");

        //Tax Values
        if (!cbIsManualTax.getValue()) {
            txtSgstUgst.setText(DmClient.vNull(ourRow.getSgstOrUgstAmt(), "0.00"));
        } else {
            txtSgstUgst.setText(txtSgstUgst.getTag2nd());
        }
        if (!serviceGrid.isUnionTerritory) {
            txtSGSTPer.setText(txtSgstUgstPer.getText());
            txtSGST.setText(txtSgstUgst.getText());
        } else if (serviceGrid.isUnionTerritory) {
            txtUGSTPer.setText(txtSgstUgstPer.getText());
            txtUGST.setText(txtSgstUgst.getText());
        }
        if (!cbIsManualTax.getValue()) {
            txtCGST.setText(DmClient.vNull(ourRow.getCgstAmt(), "0.00"));
            txtIGST.setText(DmClient.vNull(ourRow.getIgstAmt(), "0.00"));
        } else {
            txtCGST.setText(txtCGST.getTag2nd());
            txtIGST.setText(txtIGST.getTag2nd());
        }

        //SubTotal
        txtNetAmount.setTag(DmClient.vNull(ourRow.getSubTotal(), "0.00"));

        //Net Amount
        txtNetAmount.setText(DmClient.vNull(ourRow.getNetAmt(), "0.00"));
    }

    private void manageDisplay() {
        txtAmount.setEnabled(false);
        txtNetAmount.setEnabled(false);

        txtSgstUgstPer.setEnabled(false);
        txtSGSTPer.setEnabled(false);
        txtUGSTPer.setEnabled(false);
        txtCGSTPer.setEnabled(false);
        txtIGSTPer.setEnabled(false);
        txtSGST.setEnabled(false);
        txtUGST.setEnabled(false);
        manageTaxControls();
    }

    private void manageTaxControls() {
        FlwManualGstType.clear();
        if (serviceGrid.isManualGstRequired) {
            FlwManualGstType.add(cbIsManualGstType);
        }
        FlwManualTax.clear();
        lblManualtax.setText("");
        if (serviceGrid.isManualTaxRequired) {
            FlwManualTax.add(cbIsManualTax);
            lblManualtax.setText("Manual Tax");
        }
        txtHGstType.setEnabled(cbIsManualGstType.getValue());

        txtIGST.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtCGST.setEnabled(false);

        if (cbIsManualTax.getValue()) {
            if (DmShared.xNull(serviceGrid.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.InterState.getId()))) {
                txtIGST.setEnabled(true);
            } else if (DmShared.xNull(serviceGrid.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.IntraState.getId()))) {
                txtSgstUgst.setEnabled(true);
                txtCGST.setEnabled(true);
            }
        }
    }

    @Override
    public void onOk() {

        calculate();
        if (DmShared.iNull(txtHItemName.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Service is Mandatory.", txtHItemName).show();
            return;
        }

        if (DmShared.iNull(txtHItemName.getTag2nd()).longValue() == 0) {
            new DmAlertMessageBox("Service is not valid.", txtHItemName).show();
            return;
        }

        if (DmShared.iNull(txtAmount.getText()).longValue() <= 0) {
            new DmAlertMessageBox("Invalid amount.", txtServiceEvalRate).show();
            return;
        }

        if (DmShared.xNull(txtHServiceEvalType.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Valuation is mandetory.", txtHServiceEvalType).show();
            return;
        }
        if (DmShared.xNull(txtServiceEvalRate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Value is mandetory.", txtServiceEvalRate).show();
            return;
        }

        if (cbIsManualTax.getValue()) {
            if (txtSgstUgst.isEnabled()) {
                if (Math.abs(DmShared.vNull(txtSgstUgst.getTag2nd()).doubleValue() - DmShared.vNull(txtSgstUgst.getText()).doubleValue()) >= 1.0) {
                    new DmAlertMessageBox("(" + DmShared.xNull(lblSgstUgst.getText()) + ") is not valid", txtSgstUgst).show();
                    return;
                }
            }
            if (txtCGST.isEnabled()) {
                if (Math.abs(DmShared.vNull(txtCGST.getTag2nd()).doubleValue()) - (DmShared.vNull(txtCGST.getText()).doubleValue()) >= 1.0) {
                    new DmAlertMessageBox("CGST is not valid", txtCGST).show();
                    return;
                }
            }
            if (txtIGST.isEnabled()) {
                if (Math.abs(DmShared.vNull(txtIGST.getTag2nd()).doubleValue()) - (DmShared.vNull(txtIGST.getText()).doubleValue()) >= 1.0) {
                    new DmAlertMessageBox("IGST is not valid", txtIGST).show();
                    return;
                }
            }
        }

        if (serviceGrid.isServiceAcMandatory) {
            if (DmShared.iNull(iSubCode).longValue() == 0) {
                new DmAlertMessageBox("Service a/c is required.", txtHItemName).show();
                return;
            }
        }

        if (serviceGrid.isTaxAcMandatory) {
            if (!serviceGrid.isIncludeTaxInCost) {
                if (DmShared.vNull(txtSGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtSGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("SGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (serviceGrid.isRcmApplicable) {
                        if (DmShared.iNull(txtSGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("SGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
                if (DmShared.vNull(txtUGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtUGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("UGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (serviceGrid.isRcmApplicable) {
                        if (DmShared.iNull(txtUGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("UGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
                if (DmShared.vNull(txtCGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtCGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("CGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (serviceGrid.isRcmApplicable) {
                        if (DmShared.iNull(txtCGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("CGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
                if (DmShared.vNull(txtIGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtIGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("IGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (serviceGrid.isRcmApplicable) {
                        if (DmShared.iNull(txtIGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("IGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
            }
        }

        for (ServiceDetail_Mdl detail_Mdl : (List<ServiceDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox("Service already exists.", txtHItemName).show();
                    return;
                }
            }
        }

//===============================================================================================================================
        proceedOk();
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageOk(T1 row) {
        ServiceDetail_Mdl ourRow = (ServiceDetail_Mdl) row;

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setSmfCode(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setServiceEvalTypeName(DmShared.xNull(txtHServiceEvalType.getText()));
        ourRow.setServiceEvalTypeCode(DmShared.xNull(txtHServiceEvalType.getTag()));
        ourRow.setServiceEvalRate(DmShared.xNull(txtServiceEvalRate.getText()));

        //Item Details
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setHsnCode(DmShared.xNull(txtHsn.getTag()));
        ourRow.setHsn(DmShared.xNull(txtHsn.getText()));
        ourRow.setItemGSTNature(itemGSTNature);
        ourRow.setGstUnitCode(gstUnitCode);
        ourRow.setUnitCode(unitCode);

        ourRow.setManualTax(cbIsManualTax.getValue());
        ourRow.setManualGstType(cbIsManualGstType.getValue());
        ourRow.setGstTypeCode(DmShared.xNull(txtHGstType.getTag()));
        ourRow.setGstTypeName(DmShared.xNull(txtHGstType.getText()));

        ourRow.setSgstOrUgstRate(DmShared.xNull(txtSgstUgstPer.getText()));
        ourRow.setSgstOrUgstAmt(DmShared.xNull(txtSgstUgst.getText()));

        ourRow.setSgstRate(DmShared.xNull(txtSGSTPer.getText()));
        ourRow.setSgstRcmAcCode(DmShared.xNull(txtSGSTPer.getTag()));
        ourRow.setSgstAmt(DmShared.xNull(txtSGST.getText()));
        ourRow.setSgstAcCode(DmShared.xNull(txtSGST.getTag()));

        ourRow.setUgstRate(DmShared.xNull(txtUGSTPer.getText()));
        ourRow.setUgstRcmAcCode(DmShared.xNull(txtUGSTPer.getTag()));
        ourRow.setUgstAmt(DmShared.xNull(txtUGST.getText()));
        ourRow.setUgstAcCode(DmShared.xNull(txtUGST.getTag()));

        ourRow.setCgstRate(DmShared.xNull(txtCGSTPer.getText()));
        ourRow.setCgstRcmAcCode(DmShared.xNull(txtCGSTPer.getTag()));
        ourRow.setCgstAmt(DmShared.xNull(txtCGST.getText()));
        ourRow.setCgstAcCode(DmShared.xNull(txtCGST.getTag()));

        ourRow.setIgstRate(DmShared.xNull(txtIGSTPer.getText()));
        ourRow.setIgstRcmAcCode(DmShared.xNull(txtIGSTPer.getTag()));
        ourRow.setIgstAmt(DmShared.xNull(txtIGST.getText()));
        ourRow.setIgstAcCode(DmShared.xNull(txtIGST.getTag()));

        ourRow.setGstRate(DmClient.vNull(gstRate, "0.00"));
        ourRow.setQuantity(DmShared.xNull(quantity));
        ourRow.setRate(DmClient.vNull(txtAmount.getTag2nd(), "0.00"));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setTaxableAmt(DmClient.vNull(txtAmount.getTag(), "0.00"));
        ourRow.setSubTotal(DmClient.vNull(txtNetAmount.getTag(), "0.00"));
        ourRow.setNetAmt(DmClient.vNull(txtNetAmount.getText(), "0.00"));

        //IAP
        ourRow.setIsubCode(iSubCode);

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new ServiceDetail_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        ServiceDetail_Mdl ourRow = (ServiceDetail_Mdl) row;

        //Item
        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtHItemName.setTag(ourRow.getSharedKeyFieldCode());
        txtHItemName.setText(ourRow.getSharedKeyFieldName());
        txtHItemName.setTag2nd(ourRow.getSmfCode());
        txtHServiceEvalType.setText(ourRow.getServiceEvalTypeName());
        txtHServiceEvalType.setTag(ourRow.getServiceEvalTypeCode());
        txtServiceEvalRate.setText(ourRow.getServiceEvalRate());

        //Item Details
        itsBasedOn = ourRow.getItsBasedOn();
        txtHsn.setTag(ourRow.getHsnCode());
        txtHsn.setText(ourRow.getHsn());
        itemGSTNature = ourRow.getItemGSTNature();
        gstUnitCode = ourRow.getGstUnitCode();
        unitCode = ourRow.getUnitCode();

        cbIsManualTax.setValue(ourRow.isManualTax());
        cbIsManualGstType.setValue(ourRow.isManualGstType());
        txtHGstType.setText(DmShared.xNull(ourRow.getGstTypeName()));
        txtHGstType.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHGstType.setTag2nd(DmShared.xNull(ourRow.getGstTypeCode()));

        txtSgstUgst.setText(DmShared.xNull(ourRow.getSgstOrUgstAmt()));
        txtSgstUgstPer.setText(ourRow.getSgstOrUgstRate());

        txtSGST.setText(DmShared.xNull(ourRow.getSgstAmt()));
        txtSGST.setTag(DmShared.xNull(ourRow.getSgstAcCode()));
        txtSGSTPer.setText(DmShared.xNull(ourRow.getSgstRate()));
        txtSGSTPer.setTag(DmShared.xNull(ourRow.getSgstRcmAcCode()));

        txtUGST.setText(DmShared.xNull(ourRow.getUgstAmt()));
        txtUGST.setTag(DmShared.xNull(ourRow.getUgstAcCode()));
        txtUGSTPer.setText(DmShared.xNull(ourRow.getUgstRate()));
        txtUGSTPer.setTag(DmShared.xNull(ourRow.getUgstRcmAcCode()));

        txtCGST.setText(DmShared.xNull(ourRow.getCgstAmt()));
        txtCGST.setTag(DmShared.xNull(ourRow.getCgstAcCode()));
        txtCGSTPer.setText(DmShared.xNull(ourRow.getCgstRate()));
        txtCGSTPer.setTag(DmShared.xNull(ourRow.getCgstRcmAcCode()));

        txtIGST.setText(DmShared.xNull(ourRow.getIgstAmt()));
        txtIGST.setTag(DmShared.xNull(ourRow.getIgstAcCode()));
        txtIGSTPer.setText(DmShared.xNull(ourRow.getIgstRate()));
        txtIGSTPer.setTag(DmShared.xNull(ourRow.getIgstRcmAcCode()));

        //set temp value for validate check
        if (cbIsManualTax.getValue()) {
            txtSgstUgst.setTag2nd(ourRow.getSgstOrUgstAmt());
            txtCGST.setTag2nd(ourRow.getCgstAmt());
            txtIGST.setTag2nd(ourRow.getIgstAmt());
        }

        gstRate = DmShared.xNull(ourRow.getGstRate());
        quantity = DmShared.xNull(ourRow.getQuantity());
        txtAmount.setTag2nd(DmShared.xNull(ourRow.getRate()));
        txtAmount.setText(DmShared.xNull(ourRow.getAmount()));
        txtAmount.setTag(DmShared.xNull(ourRow.getTaxableAmt()));
        txtNetAmount.setTag(DmShared.xNull(ourRow.getSubTotal()));
        txtNetAmount.setText(DmShared.xNull(ourRow.getNetAmt()));

        //IAP
        iSubCode = DmShared.xNull(ourRow.getIsubCode());

        v_SNo = ourRow.getV_Sno();
        hashCode = ourRow.getSharedKeyFieldHashCode();

        manageDisplay();
        txtHItemName.setFocus(true);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        reset();
        lblSgstUgst.setText(serviceGrid.sgstUgstCaption);
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

    }

    private void reset() {
        txtHItemName.setText("");
        txtHItemName.setTag("");
        quantity = "";

        resetItem();
    }

    private void resetItem() {
        txtHItemName.setTag2nd("");
        txtHServiceEvalType.setText("");
        txtHServiceEvalType.setTag("");
        txtHServiceEvalType.setTag2nd("");

        txtServiceEvalRate.setText("");
        txtServiceEvalRate.setTag("");
        txtServiceEvalRate.setTag2nd("");

        txtHsn.setText("");
        txtHsn.setTag("");
        txtHsn.setTag2nd("");

        iSubCode = "";
        itsBasedOn = "";
        itemGSTNature = "";
        gstUnitCode = "";
        unitCode = "";

        txtHGstType.setText("");
        txtHGstType.setTag("");

        resetItemType();
    }

    private void resetItemType() {
        txtHGstType.setTag2nd("");

        txtNetAmount.setText("");
        txtNetAmount.setTag("");
        txtNetAmount.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        resetTaxDetail();
    }

    private void resetTaxDetail() {
        gstRate = "";

        //SGST or UGST
        txtSgstUgstPer.setText("");
        txtSgstUgstPer.setTag("");
        txtSgstUgstPer.setTag2nd("");

        txtSgstUgst.setText("");
        txtSgstUgst.setTag("");
        txtSgstUgst.setTag2nd("");

        //SGST
        txtSGSTPer.setText("");
        txtSGSTPer.setTag("");
        txtSGSTPer.setTag2nd("");

        txtSGST.setText("");
        txtSGST.setTag("");
        txtSGST.setTag2nd("");

        //UGST
        txtUGSTPer.setText("");
        txtUGSTPer.setTag("");
        txtUGSTPer.setTag2nd("");

        txtUGST.setText("");
        txtUGST.setTag("");
        txtUGST.setTag2nd("");

        //CGST
        txtCGSTPer.setText("");
        txtCGSTPer.setTag("");
        txtCGSTPer.setTag2nd("");

        txtCGST.setText("");
        txtCGST.setTag("");
        txtCGST.setTag2nd("");

        //IGST
        txtIGSTPer.setText("");
        txtIGSTPer.setTag("");
        txtIGSTPer.setTag2nd("");

        txtIGST.setText("");
        txtIGST.setTag("");
        txtIGST.setTag2nd("");
    }

    //</editor-fold>
    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == cbIsManualGstType) {
            manageTaxControls();
            if (!cbIsManualGstType.getValue()) {
                //getITS
                getITS(txtHItemName.getTag(), txtAmount.getText(), itsBasedOn, serviceGrid.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
            }
        } else if (event.getSource() == cbIsManualTax) {
            manageTaxControls();
            calculate();
        } else {
            super.onClick(event);
        }
    }
}
