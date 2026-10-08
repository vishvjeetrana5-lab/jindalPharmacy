package aarogya.pharmacy.indent.client;

import aarogya.pharmacy.indent.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.indent.shared.TxtHMedicineName_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.pharmacy.indent.shared.Indent_Srv;
import aarogya.pharmacy.indent.shared.Indent_SrvAsync;
import aarogya.selfToolkit.toolTip.client.ToolTip;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import java.util.ArrayList;

public class IndentGridEdit<T1, T2> extends IndentGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl grdIndentEntry);

        public void onDelete();
    }

    ServiceInvoker<Indent_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdMedicineDetail_Mdl parentRow;
    private String dtIndent;
    private int hashCode;
    String itemSku = "";
    ToolTip toolTip = new ToolTip();
    List<ToolTip_Mdl> data = new ArrayList<>();
    String isBestBefore = "", reqV_SNo = "", reqDocId = "", requisitionDate = "", previousItemCode = "";

    private void textHelpEvents() {

        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicineName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(itemSku, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicineName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicineName_Mdl t1Row) {

                if (DmShared.iNull(previousItemCode).longValue() != DmShared.iNull(txtHMedicineName.getTag()).longValue()) {
                    resetControls();
                    previousItemCode = DmShared.xNull(txtHMedicineName.getTag());
                }
                if (blnStatus && t1Row != null) {
                    txtUnit.setTag2nd(t1Row.getDecimalPlace());
                    txtUnit.setText(t1Row.getSkuName());
                    isBestBefore = t1Row.getIsBestBefore();
                }
            }
        });

    }

    private void resetControls() {
        txtUnit.setTag2nd("");
        txtUnit.setTag("");
        txtUnit.setText("");
        isBestBefore = "";
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtReqDate)) {
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
        if (event.getSource().equals(txtQty)) {
            calculate();
        }
    }

    private void calculate() {
        txtQty.setText(DmClient.vNull(txtQty.getText(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String loginDate, int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        dtIndent = loginDate;
        txtHMedicineName.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;
        parentRow = ourRow;
        if (DmShared.iNull(ourRow.getRequisitionVsNo()).intValue() != 0) {
            itemSku = DmShared.xNull(ourRow.getUnitName());
            flwLblReqisitionNoOuter.clear();
            flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
            txtHMedicineName.setEnabled(false);
            txtQty.setFocus(true);
        } else {
            itemSku = "";
            flwLblReqisitionNoOuter.clear();
            txtHMedicineName.setEnabled(true);
        }
        hashCode = ourRow.getSharedKeyFieldHashCode();
        lblReqisitionNoValue.setText(ourRow.getRequisitionNo());
        requisitionDate = DmShared.dNull(ourRow.getRequisitionDate());

        previousItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtHMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtReqDate.setText(ourRow.getReqDate());
        txtRemarks.setText(ourRow.getRemarks());
        txtQty.setText(DmShared.xNull(ourRow.getQty()));
        txtUnit.setText(DmShared.xNull(ourRow.getUnitName()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlace()));
        txtRequisitionNo.setTag(DmShared.xNull(ourRow.getRequisitionDocId()));
        txtRequisitionNo.setText(DmShared.xNull(ourRow.getRequisitionNo()));
        reqDocId = DmShared.xNull(ourRow.getRequisitionDocId());
        reqV_SNo = DmShared.xNull(ourRow.getRequisitionVsNo());
        txtMrp.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("cancelled")) {
            new DmAlertMessageBox("Record has been canceled, can't change record.", txtHMedicineName).show();
            return;
        }
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("completed")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtHMedicineName).show();
            return;
        }

        if (DmShared.xNull(txtHMedicineName.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine Name is mandatory", txtHMedicineName).show();
            return;
        }
        if (DmShared.vNull(txtQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQty).show();
            return;
        }

        if (dtIndent != null || dtIndent != "") {
            if (!DmShared.dNull(txtReqDate.getText()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtReqDate.getText()).getTime() < DmShared.convertDate(dtIndent).getTime()) {
                    new DmAlertMessageBox("Require Date Can't be Less Then Indent Date.", txtReqDate).show();
                    return;
                }
            }
            if (!DmShared.dNull(requisitionDate).equalsIgnoreCase("")) {
                if (DmShared.convertDate(requisitionDate).getTime() > DmShared.convertDate(dtIndent).getTime()) {
                    new DmAlertMessageBox("Requisition Date Can't be greater Then Indent Date.", txtHMedicineName).show();
                    return;
                }
            }
        }

        List<GrdMedicineDetail_Mdl> lstTemp = (List<GrdMedicineDetail_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdMedicineDetail_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHMedicineName.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))
                    && DmShared.xNull(reqDocId).equalsIgnoreCase(DmShared.xNull(get.getRequisitionDocId()))
                    && DmShared.xNull(reqV_SNo).equalsIgnoreCase(DmShared.xNull(get.getRequisitionVsNo()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHMedicineName.getText() + " already exists.", txtHMedicineName).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        ourRow = parentRow;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineName.getText()));
        ourRow.setUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setDecimalPlace(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setQty(DmShared.xNull(txtQty.getText()));
        ourRow.setRemarks(DmShared.xNull(txtRemarks.getText()));
        ourRow.setReqDate(DmShared.dNull(txtReqDate.getText()));
        ourRow.setRequisitionDocId(DmShared.xNull(txtRequisitionNo.getTag()));
        ourRow.setRequisitionNo(DmShared.xNull(txtRequisitionNo.getText()));
        ourRow.setRequisitionDate(DmShared.dNull(requisitionDate));
        ourRow.setMrp(DmClient.vNull(txtMrp.getText(), "0.00"));
        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("cancelled")) {
            new DmAlertMessageBox("Record has been canceled, can't delete record.", txtHMedicineName).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("completed")) {
            new DmAlertMessageBox("Record has been completed, can't delete record.", txtHMedicineName).show();
            return;
        } else {
            proceedDelete();
        }
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public IndentGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Indent Detail", "400px", "150px");
        SrvAsync = new ServiceInvoker<>(GWT.create(Indent_Srv.class), Indent.class.getName());
        textHelpEvents();
        manageDisplay();
        manageEvent();
    }

    private void manageEvent() {
        txtReqDate.addBlurHandler(this);
        txtQty.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtUnit.setEnabled(false);
    }

}
