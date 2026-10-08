package aarogya.inventory.supplierMaster.client;

import aarogya.inventory.supplierMaster.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class CDConfigurationGridEdit<T1, T2> extends CDConfigurationGridEdit_Dsg<T1, T2> implements BlurHandler, AttachEvent.Handler {

    private void resetPopUp() {
        txtHMedicineCompany.setText("");
        txtHMedicineCompany.setTag("");
        txtHMedicineCompany.setTag2nd("");

        txtDiscPer.setText("");
        txtDiscPer.setTag("");
        txtDiscPer.setTag2nd("");

        txtApplicableFrom.setText("");
        txtApplicableFrom.setTag("");
        txtApplicableFrom.setTag2nd("");
    }

    interface CallBack {

        public void onOk(GrdCDConfiguration_Mdl manf_Mdl);

        public void onDelete();

    }

    private final ServiceInvoker<SupplierMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private KeyFieldExtended parentRow;
    private int hashCode = 0;

    private void textHelpEvents() {

        txtHMedicineCompany.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }

        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        resetPopUp();
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        GrdCDConfiguration_Mdl ourRow = (GrdCDConfiguration_Mdl) row;
        parentRow = new KeyFieldExtended();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHMedicineCompany.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHMedicineCompany.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));

        txtDiscPer.setText(DmClient.vNull(DmShared.xNull(ourRow.getDiscountPer()), "0.00"));

        txtApplicableFrom.setText(DmShared.dNull(ourRow.getApplicableFrom()));

        txtHMedicineCompany.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdCDConfiguration_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHMedicineCompany.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine Company is mandatory.", txtHMedicineCompany).show();
            return;
        }

        if (DmShared.xNull(txtDiscPer.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Discount Per. is mandatory.", txtDiscPer).show();
            return;
        }
        if (DmShared.xNull(txtApplicableFrom.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Applicable From is mandatory.", txtApplicableFrom).show();
            return;
        }
        for (GrdCDConfiguration_Mdl cdConfiguration_Mdl : (List<GrdCDConfiguration_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(cdConfiguration_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHMedicineCompany.getTag()))) {

                if (!DmClient.vNull(cdConfiguration_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    if (DmShared.xNull(cdConfiguration_Mdl.getApplicableFrom()).equalsIgnoreCase(DmShared.xNull(txtApplicableFrom.getText()))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHMedicineCompany.getText()) + " is already exists.", txtHMedicineCompany).show();
                        return;
                    }

                }

            }
        }
        //============================< Applicable From Date Check >===================================================
        for (GrdCDConfiguration_Mdl cdConfiguration_Mdl : (List<GrdCDConfiguration_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (!DmShared.xNull(cdConfiguration_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(DmShared.xNull(DmShared.xNull(txtApplicableFrom.getText()))).getTime() < DmShared.convertDate(DmShared.xNull(cdConfiguration_Mdl.getApplicableFrom())).getTime()) &&
                       (DmShared.xNull(txtHMedicineCompany.getText()).equalsIgnoreCase(cdConfiguration_Mdl.getSharedKeyFieldName())))  {
                    new DmAlertMessageBox("Applicable From Date should not be less than previous From Date.", txtApplicableFrom).show();
                    return;
                }

            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdCDConfiguration_Mdl ourRow = (GrdCDConfiguration_Mdl) row;
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineCompany.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineCompany.getTag()));
        ourRow.setDiscountPer(DmClient.vNull(txtDiscPer.getText(), "0.00"));
        ourRow.setApplicableFrom(DmShared.dNull(txtApplicableFrom.getText()));
        ourRow.setApplicableTo(DmShared.xNull("31-Dec-9999"));

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

    public CDConfigurationGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "CD Configuration", "400px", "100px");
        SrvAsync = new ServiceInvoker<SupplierMaster_SrvAsync>(GWT.create(SupplierMaster_Srv.class), SupplierMaster.class.getName());
        textHelpEvents();
        handlerEvents();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if ((event.getSource() == txtDiscPer)) {
            if (!DmShared.xNull(txtDiscPer.getText()).equalsIgnoreCase("")) {
                txtDiscPer.setText(DmClient.vNull(txtDiscPer.getText(), "0.00"));
            }
        } else if (event.getSource() == txtApplicableFrom) {
            txtApplicableFrom.setText(DmClient.returnDate(txtApplicableFrom.getText()));
        }
    }

    private void handlerEvents() {
        txtDiscPer.addBlurHandler(this);
        txtApplicableFrom.addBlurHandler(this);
    }
}
