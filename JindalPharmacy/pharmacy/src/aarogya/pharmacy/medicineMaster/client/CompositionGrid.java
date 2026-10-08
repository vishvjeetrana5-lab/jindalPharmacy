package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.GrdComposition_Mdl;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_Srv;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_SrvAsync;
import aarogya.pharmacy.medicineMaster.shared.Sku_Mdl;
import aarogya.pharmacy.medicineMaster.shared.TxtHComposition_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class CompositionGrid<T1, T2> extends CompositionGrid_Dsg<T1, T2> implements BlurHandler {

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtPower)) {
            if (DmShared.vNull(txtPower.getText()).doubleValue() > 0) {
                txtPower.setText(DmClient.vNull(txtPower.getText(), "0.00"));
            }
        }
    }

    interface CallBack {

        public void onOk(GrdComposition_Mdl grdComposition_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<MedicineMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    MedicineMaster itemMaster;
    private String resGrpCode = "", resGrpName = "";

    private void handlerEvents() {
        txtPower.addBlurHandler(this);
    }

    private void textHelpEvents() {
        txtHComposition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHComposition_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHComposition(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHComposition_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHComposition).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHComposition_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHComposition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHComposition).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHComposition_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    resGrpCode = t1Row.getRestrictGroupCode();
                    resGrpName = t1Row.getRestrictGroupName();
//                    if (DmShared.xNull(itemMaster.txtHResGroup.getTag()).equalsIgnoreCase("")) {
//                        itemMaster.txtHResGroup.setTag(t1Row.getRestrictGroupCode());
//                        itemMaster.txtHResGroup.setText(t1Row.getRestrictGroupName());
//                    }
                }
            }
        });

        txtHIsBase.addTextHelpCallBack(
                new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsBase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsBase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsBase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSKU.addTextHelpCallBack(
                new DmTextHelp.TextHelpCallBack<Sku_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                srvAsync.invokeRPC(blnCallBackWait).getSKUDetails(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Sku_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSKU).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Sku_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSKU.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSKU).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Sku_Mdl tRow
            ) {
                if (blnStatus && tRow != null) {
                }
            }
        }
        );

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineMaster itemMaster) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.itemMaster = itemMaster;
    }

    @Override
    public void displayRow(T1 row) {
        GrdComposition_Mdl ourRow = (GrdComposition_Mdl) row;

        txtHComposition.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHComposition.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHComposition.setTag2nd(DmShared.xNull(ourRow.getV_SNo()));
        resGrpCode = DmShared.xNull(ourRow.getRestrictGroupCode());
        resGrpName = DmShared.xNull(ourRow.getRestrictGroupName());
        txtPower.setText(DmClient.vNull(ourRow.getPower(), "0.00"));
        if (!DmShared.xNull(ourRow.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
            txtHIsBase.setText(DmShared.xNull(ourRow.getBaseText()));
            txtHIsBase.setTag(DmShared.xNull(ourRow.getBaseTag()));
        } else {
            txtHIsBase.setText("No");
            txtHIsBase.setTag("0");
        }
        txtHSKU.setText(DmShared.xNull(ourRow.getUnitText()));
        txtHSKU.setTag(DmShared.xNull(ourRow.getUnitTag()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHComposition.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdComposition_Mdl();
    }

    @Override
    public void onOk() {
        try {
            String medicine_RestrictedGroupCode = "", medicine_RestrictedGroupName = "";
            if (DmShared.iNull(txtHComposition.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Composition is mandatory.", txtHComposition).show();
                return;
            }
            if (DmShared.vNull(txtPower.getText()).doubleValue() > 0) {
                if (DmShared.iNull(txtHSKU.getTag()).intValue() == 0) {
                    new DmAlertMessageBox("Unit is mandatory.", txtHSKU).show();
                    return;
                }
            }
            if (DmShared.xNull(txtHIsBase.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Base Composition Selection is mandatory.", txtHIsBase).show();
                return;
            }

            int baseCount = 0;
            for (GrdComposition_Mdl detail_Mdl : (List<GrdComposition_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHComposition.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHComposition.getText()) + " already exists.", txtHComposition).show();
                        return;
                    }
                }
                if (DmShared.iNull(detail_Mdl.getRestrictGroupCode()).longValue() > 0 && DmShared.iNull(resGrpCode).longValue() > 0) {
                    if (!DmShared.xNull(detail_Mdl.getRestrictGroupCode()).equalsIgnoreCase(DmShared.xNull(resGrpCode))) {
                        if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                            new DmAlertMessageBox(DmShared.xNull(txtHComposition.getText()) + " has different Restrict Group than " + detail_Mdl.getSharedKeyFieldName(), txtHComposition).show();
                            return;
                        }
                    }
                }

                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        if (DmShared.iNull(detail_Mdl.getBaseTag()).longValue() > 0) {
                            ++baseCount;
                        }
                    }
                }

                if (DmShared.iNull(medicine_RestrictedGroupCode).longValue() == 0) {
                    medicine_RestrictedGroupCode = DmShared.xNull(detail_Mdl.getRestrictGroupCode());
                    medicine_RestrictedGroupName = DmShared.xNull(detail_Mdl.getRestrictGroupName());

                    itemMaster.txtResGroup.setTag(medicine_RestrictedGroupCode);
                    itemMaster.txtResGroup.setText(medicine_RestrictedGroupName);
                }
            }

            if (DmShared.iNull(txtHIsBase.getTag()).longValue() > 0) {
                ++baseCount;
            }

            if (baseCount > 1) {
                new DmAlertMessageBox("Base composition is already defined.", txtHIsBase).show();
                return;
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHComposition).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdComposition_Mdl ourRow = (GrdComposition_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHComposition.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHComposition.getText()));
        ourRow.setPower(DmClient.vNull(txtPower.getText(), "0.00"));
        ourRow.setBaseTag(DmShared.iNull(txtHIsBase.getTag()).toString());
        ourRow.setBaseText(DmShared.xNull(txtHIsBase.getText()));
        ourRow.setRestrictGroupCode(DmShared.iNull(resGrpCode).toString());
        ourRow.setRestrictGroupName(DmShared.xNull(resGrpName));
        ourRow.setUnitTag(DmShared.iNull(txtHSKU.getTag()).toString());
        ourRow.setUnitText(DmShared.xNull(txtHSKU.getText()));
        ourRow.setV_SNo(DmShared.xNull(txtHComposition.getTag2nd()));

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

    public CompositionGrid(DmGrid<T1, T2> grid) {
        super(grid, "Composition", "420px", "62px");
        srvAsync = new ServiceInvoker<MedicineMaster_SrvAsync>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        textHelpEvents();
        handlerEvents();
    }

}
