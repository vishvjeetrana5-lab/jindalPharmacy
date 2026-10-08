package dataman.utility.walkThroughMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.walkThroughMaster.shared.GrdDefaultDetail_Mdl;
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
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_Srv;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_SrvAsync;

public class DefaultDetailGridEdit<T1, T2> extends DefaultDetailGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdDefaultDetail_Mdl gridDefault_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<WalkThroughMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    private void textHelpEvents() {
        txtHUserName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUserName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUserName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUserName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHVType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHSiteName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSiteName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

                if (blnStatus && t1Row != null) {

                } else {

                }
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

    }

    @Override
    public void displayRow(T1 row) {
        GrdDefaultDetail_Mdl ourRow = (GrdDefaultDetail_Mdl) row;

        txtHUserName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHUserName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHVType.setText(DmShared.xNull(ourRow.getV_TypeName()));
        txtHVType.setTag(DmShared.xNull(ourRow.getV_TypeCode()));
        txtHSiteName.setText(DmShared.xNull(ourRow.getSiteName()));
        txtHSiteName.setTag(DmShared.xNull(ourRow.getSiteCode()));
        txtHDepartment.setText(DmShared.xNull(ourRow.getDepartmentName()));
        txtHDepartment.setTag(DmShared.xNull(ourRow.getDepartmentCode()));
        txtValue.setText(DmClient.vNull(ourRow.getValueUpto(), "0.00"));

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHUserName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdDefaultDetail_Mdl();
    }

    @Override
    public void onOk() {
        GrdDefaultDetail_Mdl mrTemp;
        mrTemp = new GrdDefaultDetail_Mdl();
        try {

//
//            for (GrdDefaultDetail_Mdl default_Mdl : (List<GrdDefaultDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
//                if (DmShared.xNull(default_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHUserName.getTag()))) {
//                    if (!DmClient.vNull(default_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
//                        new DmAlertMessageBox(DmShared.xNull(txtHUserName.getText()) + " already exists.", txtHUserName).show();
//                        return;
//                    }
//                }
//            }
            for (GrdDefaultDetail_Mdl config_Mdl : (List<GrdDefaultDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHUserName.getText()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getSharedKeyFieldName()))
                        && DmShared.xNull(txtHVType.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getV_TypeCode()))
                        && DmShared.xNull(txtHSiteName.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getSiteCode()))
                        && DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getDepartmentCode()))
                        //&& DmShared.xNull(txtValue.getText()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getValueUpto()))&&
                        && DmShared.vNull(txtValue.getText()).doubleValue() == DmShared.vNull(config_Mdl.getValueUpto()).doubleValue()) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(config_Mdl.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Combination already exist.", txtHUserName).show();
                        return;
                    }
                }
            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHUserName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdDefaultDetail_Mdl ourRow = (GrdDefaultDetail_Mdl) row;
        ourRow.setValueUpto(DmClient.vNull(txtValue.getText(), "0.00"));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHUserName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHUserName.getText()));
        ourRow.setV_TypeCode(DmShared.xNull(txtHVType.getTag()));
        ourRow.setV_TypeName(DmShared.xNull(txtHVType.getText()));
        ourRow.setSiteCode(DmShared.xNull(txtHSiteName.getTag()));
        ourRow.setSiteName(DmShared.xNull(txtHSiteName.getText()));
        ourRow.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        ourRow.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));

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

    public DefaultDetailGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Default Approval", "400px", "136px");
        SrvAsync = new ServiceInvoker<WalkThroughMaster_SrvAsync>(GWT.create(WalkThroughMaster_Srv.class), WalkThroughMaster.class.getName());
        textHelpEvents();

    }

}
