package aarogya.pharmacy.medicineDepartmentInfo.client;

import aarogya.pharmacy.medicineDepartmentInfo.shared.*;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class DepartmentInfoDetailsGridEdit<T1, T2> extends DepartmentInfoDetailsGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdMedicineDepartmentInfo_Mdl gridItem_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<MedicineDepartmentInfo_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    private MedicineDepartmentInfo medicineDepartmentInfo;
    private String isHoldReq;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineDepartmentInfo obj) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.medicineDepartmentInfo = obj;

    }

    private void textHelpEvents() {
        txtHMedicine.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicine_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(medicineDepartmentInfo.txtHSiteName.getTag(), medicineDepartmentInfo.txtHDepartment.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicine_Mdl>> result) {

                        if (result.isStatus()) {
                            txtHMedicine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicine).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicine_Mdl t1Row) {

                if (blnStatus && t1Row != null) {
                    isHoldReq = t1Row.getIsHoldRequirement();
                    txtUnit.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtUnit.setTag(DmShared.xNull(t1Row.getAllowDecimalPlaces()));
                    calculate();
                } else {
                    txtUnit.setText("");
                    txtUnit.setTag("");
                }
            }

        });
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        GrdMedicineDepartmentInfo_Mdl ourRow = (GrdMedicineDepartmentInfo_Mdl) row;

        txtMinLvl.setText(DmShared.xNull(ourRow.getMinStock()));
        txtMaxLvl.setText(DmShared.xNull(ourRow.getMaxStock()));
        txtReOrder.setText(DmShared.xNull(ourRow.getReOrder()));
        txtBin.setText(DmShared.xNull(ourRow.getBinName()));
        txtHMedicine.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHMedicine.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtUnit.setText(DmShared.xNull(ourRow.getSkuName()));
        txtUnit.setTag(DmShared.xNull(ourRow.getAllowDecimalPlace()));
        calculate();
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHMedicine.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDepartmentInfo_Mdl();
    }

    @Override
    public void onOk() {

        try {
            calculate();

            if (DmShared.xNull(medicineDepartmentInfo.txtHSiteName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Site Name is mandatory.", medicineDepartmentInfo.txtHSiteName).show();
                return;
            }

            if (DmShared.xNull(medicineDepartmentInfo.txtHDepartment.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Department is mandatory.", medicineDepartmentInfo.txtHDepartment).show();
                return;
            }

            if (DmShared.xNull(txtHMedicine.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine is mandatory.", txtHMedicine).show();
                return;
            }

            if (DmShared.vNull(txtMaxLvl.getText()).doubleValue() < DmShared.vNull(txtMinLvl.getText()).doubleValue()) {
                new DmAlertMessageBox("Min Level value must be less than or equal to Max Level value.", txtMinLvl).show();
                return;
            }

            if ((DmShared.vNull(txtReOrder.getText()).doubleValue() < DmShared.vNull(txtMinLvl.getText()).doubleValue()) || (DmShared.vNull(txtReOrder.getText()).doubleValue() > DmShared.vNull(txtMaxLvl.getText()).doubleValue())) {
                throw new DmException(600, "Re-Order Level cannot is greater than  Max. Level or less than Min. Level");
            }

            for (GrdMedicineDepartmentInfo_Mdl config_Mdl : (List<GrdMedicineDepartmentInfo_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHMedicine.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getSharedKeyFieldCode()))) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(config_Mdl.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Medicine already exist.", txtHMedicine).show();
                        return;
                    }
                }

            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHMedicine).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineDepartmentInfo_Mdl ourRow = (GrdMedicineDepartmentInfo_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicine.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicine.getText()));
        ourRow.setMinStock(DmShared.xNull(txtMinLvl.getText()));
        ourRow.setMaxStock(DmShared.xNull(txtMaxLvl.getText()));
        ourRow.setReOrder(DmShared.xNull(txtReOrder.getText()));
        ourRow.setBinName(DmShared.xNull(txtBin.getText()));
        ourRow.setIsHoldRequirement(DmShared.xNull(isHoldReq));

        callBack.onOk(ourRow);
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
    public void manageDelete() {
        callBack.onDelete();
    }

    public DepartmentInfoDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Grid Edit", "600px", "127px");
        SrvAsync = new ServiceInvoker<MedicineDepartmentInfo_SrvAsync>(GWT.create(MedicineDepartmentInfo_Srv.class), MedicineDepartmentInfo.class.getName());
        textHelpEvents();
        handlerEvents();

    }

    private void handlerEvents() {
        txtMinLvl.addBlurHandler(this);
        txtMaxLvl.addBlurHandler(this);
        txtReOrder.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtMinLvl) || event.getSource().equals(txtMaxLvl) || event.getSource().equals(txtReOrder)) {
            calculate();
        }
    }

    private void calculate() {
        txtMinLvl.setText(DmClient.vNull(DmShared.vNull(txtMinLvl.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
        txtMaxLvl.setText(DmClient.vNull(DmShared.vNull(txtMaxLvl.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
        txtReOrder.setText(DmClient.vNull(DmShared.vNull(txtReOrder.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
    }

    private void clear() {
        txtHMedicine.setTag("");
        txtHMedicine.setText("");
        txtMinLvl.setText("");
        txtMaxLvl.setText("");
        txtReOrder.setText("");
        txtBin.setText("");
        txtUnit.setText("");

    }
}
