package aarogya.pharmacy.enquiry.client;

import aarogya.pharmacy.enquiry.shared.Enquiry_Srv;
import aarogya.pharmacy.enquiry.shared.Enquiry_SrvAsync;
import aarogya.pharmacy.enquiry.shared.GrdVendor_Mdl;
import aarogya.pharmacy.enquiry.shared.TxtHSupplier_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class VendorsGridEdit<T1, T2> extends VendorsGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdVendor_Mdl gridVendor_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<Enquiry_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    private Enquiry enquiry;

    private void textHelpEvents() {

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(false, enquiry.txtHEnquiryType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSupplier_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSupplier_Mdl t1Row) {
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, Enquiry enquiry) {
        this.enquiry = enquiry;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        GrdVendor_Mdl ourRow = (GrdVendor_Mdl) row;
        txtHSupplier.setTag2nd(DmShared.xNull(ourRow.getIsStatus()));
        txtHSupplier.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSupplier.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHSupplier.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdVendor_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHSupplier.getTag2nd()).equalsIgnoreCase("1")) {
                new DmAlertMessageBox("Supplier is cancelled,cannot modify record.", txtHSupplier).show();
                return;
            }
            if (DmShared.xNull(txtHSupplier.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Supplier is mandatory.", txtHSupplier).show();
                return;
            }

            for (GrdVendor_Mdl detail_Mdl : (List<GrdVendor_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHSupplier.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHSupplier.getText()) + " already exists.", txtHSupplier).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHSupplier).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdVendor_Mdl ourRow = (GrdVendor_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHSupplier.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSupplier.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(txtHSupplier.getTag2nd()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Supplier is cancelled,cannot modify record.", txtHSupplier).show();
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

    public VendorsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Suppliers", "300px", "55px");
        srvAsync = new ServiceInvoker<Enquiry_SrvAsync>(GWT.create(Enquiry_Srv.class), Enquiry.class.getName());
        textHelpEvents();
    }

}
