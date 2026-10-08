package aarogya.master.consultantSharingMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.consultantSharingMaster.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class SharingDetailGridEdit<T1, T2> extends SharingDetailGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdSharingDetail_Mdl grdSharingDetail_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<ConsultantSharingMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    private void textHelpEvents() {
        txtHEmpanelment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEmpanelment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmpanelment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEmpanelment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmpanelment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHModule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHServiceCat.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceCat).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                manageCategoryDisplay();
            }
        });
        txtHServiceGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                manageGroupDisplay();
            }
        });
        txtHService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHService(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                manageServiceDisplay();
            }
        });
        txtHRoomCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRoomCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoomCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoomCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoomCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHHeaderConsType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHeaderConsType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHeaderConsType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHeaderConsType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHHeaderRefType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHeaderRefType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHeaderRefType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHeaderRefType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHLineConsType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLineConsType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLineConsType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLineConsType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHLineRefType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLineRefType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLineRefType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLineRefType).show();
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
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHEmpanelment.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdSharingDetail_Mdl ourRow = (GrdSharingDetail_Mdl) row;

        txtHEmpanelment.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHEmpanelment.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHRoomCategory.setTag(DmShared.xNull(ourRow.getRoomCategoryCode()));
        txtHRoomCategory.setText(DmShared.xNull(ourRow.getRoomCategoryName()));
        txtHModule.setTag(DmShared.xNull(ourRow.getModuleName()));
        txtHModule.setText(DmShared.xNull(ourRow.getModuleName()));
        txtHServiceCat.setTag(DmShared.xNull(ourRow.getCategoryCode()));
        txtHServiceCat.setText(DmShared.xNull(ourRow.getCategoryName()));
        txtHServiceGroup.setTag(DmShared.xNull(ourRow.getGroupCode()));
        txtHServiceGroup.setText(DmShared.xNull(ourRow.getGroupName()));
        txtHService.setTag(DmShared.xNull(ourRow.getServiceCode()));
        txtHService.setText(DmShared.xNull(ourRow.getServiceName()));
        txtHHeaderConsType.setTag(DmShared.xNull(ourRow.getHeaderConTypeCode()));
        txtHHeaderConsType.setText(DmShared.xNull(ourRow.getHeaderConTypeName()));
        txtHHeaderRefType.setTag(DmShared.xNull(ourRow.getHeaderRefTypeCode()));
        txtHHeaderRefType.setText(DmShared.xNull(ourRow.getHeaderRefTypeName()));
        txtHLineConsType.setTag(DmShared.xNull(ourRow.getLineConTypeCode()));
        txtHLineConsType.setText(DmShared.xNull(ourRow.getLineConTypeName()));
        txtHLineRefType.setTag(DmShared.xNull(ourRow.getLineRefTypeCode()));
        txtHLineRefType.setText(DmShared.xNull(ourRow.getLineRefTypeName()));

        if (DmShared.xNull(ourRow.getHeaderConSharingDisplay()).trim().equalsIgnoreCase("")) {
            txtHHeaderConsType.setTag("0");
            txtHHeaderConsType.setText("% On Amount");
        }
        if (DmShared.xNull(ourRow.getHeaderRefSharingDisplay()).equalsIgnoreCase("")) {
            txtHHeaderRefType.setTag("0");
            txtHHeaderRefType.setText("% On Amount");
        }
        if (DmShared.xNull(ourRow.getLineConSharingDisplay()).trim().equalsIgnoreCase("")) {
            txtHLineConsType.setTag("0");
            txtHLineConsType.setText("% On Amount");
        }
        if (DmShared.xNull(ourRow.getLineRefSharingDisplay()).equalsIgnoreCase("")) {
            txtHLineRefType.setTag("0");
            txtHLineRefType.setText("% On Amount");
        }

        txtHeaderRefAmount.setText(DmClient.vNull(ourRow.getHeaderRefAmount(), "0.00"));
        txtHeaderConsAmount.setText(DmClient.vNull(ourRow.getHeaderConAmount(), "0.00"));
        txtLineRefAmount.setText(DmClient.vNull(ourRow.getLineRefAmount(), "0.00"));
        txtLineConsAmount.setText(DmClient.vNull(ourRow.getLineConAmount(), "0.00"));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        if (DmShared.iNull(txtHService.getTag()).longValue() != 0) {
            manageServiceDisplay();
        } else if (DmShared.iNull(txtHServiceCat.getTag()).longValue() != 0) {
            manageCategoryDisplay();
        } else if (DmShared.iNull(txtHServiceGroup.getTag()).longValue() != 0) {
            manageGroupDisplay();
        } else {
            txtHService.setEnabled(true);
            txtHServiceCat.setEnabled(true);
            txtHServiceGroup.setEnabled(true);
        }

        txtHEmpanelment.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdSharingDetail_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if ((DmShared.iNull(txtHServiceCat.getTag()).longValue() == 0) && (DmShared.iNull(txtHServiceGroup.getTag()).longValue() == 0)
                    && (DmShared.xNull(txtHModule.getTag()).trim().equalsIgnoreCase("")) && (DmShared.iNull(txtHService.getTag()).longValue() == 0)) {
                new DmAlertMessageBox("Module Name / Service Category / Service Group / Service is mandatory.", txtHModule).show();
                return;
            }
            if (DmShared.xNull(txtHHeaderConsType.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Header Consultant Type is mandatory.", txtHHeaderConsType).show();
                return;
            }
            if (DmShared.xNull(txtHHeaderRefType.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Header Referred Cons.Type is mandatory.", txtHHeaderRefType).show();
                return;
            }
            if (DmShared.xNull(txtHLineConsType.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Performing Consultant Type is mandatory.", txtHLineConsType).show();
                return;
            }
            if (DmShared.xNull(txtHLineRefType.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Performing Referred Cons.Type is mandatory.", txtHLineRefType).show();
                return;
            }

            for (GrdSharingDetail_Mdl detail_Mdl : (List<GrdSharingDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.iNull(txtHService.getTag()).longValue() != 0) {
                    if ((DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(txtHEmpanelment.getTag()).longValue())
                            && (DmShared.xNull(detail_Mdl.getModuleName()).trim().equalsIgnoreCase(DmShared.xNull(txtHModule.getTag()).trim()))
                            && (DmShared.iNull(detail_Mdl.getRoomCategoryCode()).longValue() == DmShared.iNull(txtHRoomCategory.getTag()).longValue())
                            && (DmShared.iNull(detail_Mdl.getServiceCode()).longValue() == DmShared.iNull(txtHService.getTag()).longValue())) {
                        if (detail_Mdl.getSharedKeyFieldHashCode() != hashCode) {
                            new DmAlertMessageBox(DmShared.xNull(txtHService.getText()) + " already exists.", txtHService).show();
                            return;
                        }
                    }
                }
                if (DmShared.iNull(txtHServiceCat.getTag()).longValue() != 0) {
                    if ((DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(txtHEmpanelment.getTag()).longValue())
                            && (DmShared.xNull(detail_Mdl.getModuleName()).trim().equalsIgnoreCase(DmShared.xNull(txtHModule.getTag()).trim()))
                            && (DmShared.iNull(detail_Mdl.getRoomCategoryCode()).longValue() == DmShared.iNull(txtHRoomCategory.getTag()).longValue())
                            && (DmShared.iNull(detail_Mdl.getCategoryCode()).longValue() == DmShared.iNull(txtHServiceCat.getTag()).longValue())) {
                        if (detail_Mdl.getSharedKeyFieldHashCode() != hashCode) {
                            new DmAlertMessageBox(DmShared.xNull(txtHServiceCat.getText()) + " already exists.", txtHServiceCat).show();
                            return;
                        }
                    }
                }
                if (DmShared.iNull(txtHServiceGroup.getTag()).longValue() != 0) {
                    if ((DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(txtHEmpanelment.getTag()).longValue())
                            && (DmShared.xNull(detail_Mdl.getModuleName()).trim().equalsIgnoreCase(DmShared.xNull(txtHModule.getTag()).trim()))
                            && (DmShared.iNull(detail_Mdl.getRoomCategoryCode()).longValue() == DmShared.iNull(txtHRoomCategory.getTag()).longValue())
                            && (DmShared.iNull(detail_Mdl.getGroupCode()).longValue() == DmShared.iNull(txtHServiceGroup.getTag()).longValue())) {
                        if (detail_Mdl.getSharedKeyFieldHashCode() != hashCode) {
                            new DmAlertMessageBox(DmShared.xNull(txtHServiceGroup.getText()) + " already exists.", txtHServiceGroup).show();
                            return;
                        }
                    }
                }
//                if (DmShared.iNull(txtHRoomCategory.getTag()).longValue() != 0) {
//                    if ((DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(txtHEmpanelment.getTag()).longValue())
//                            && (DmShared.xNull(detail_Mdl.getModuleName()).trim().equalsIgnoreCase(DmShared.xNull(txtHModule.getTag()).trim()))
//                            && (DmShared.iNull(detail_Mdl.getRoomCategoryCode()).longValue() != DmShared.iNull(txtHRoomCategory.getTag()).longValue())
//                            && (DmShared.iNull(detail_Mdl.getGroupCode()).longValue() == DmShared.iNull(txtHServiceGroup.getTag()).longValue())) {
//                        if (detail_Mdl.getSharedKeyFieldHashCode() != hashCode) {
//                            new DmAlertMessageBox(DmShared.xNull(txtHRoomCategory.getText()) + " already exists.", txtHServiceGroup).show();
//                            return;
//                        }
//                    }
//                }

                if (!DmShared.xNull(txtHModule.getTag()).trim().equalsIgnoreCase("")) {
                    if ((DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(txtHEmpanelment.getTag()).longValue())
                            && (DmShared.xNull(detail_Mdl.getModuleName()).trim().equalsIgnoreCase(DmShared.xNull(txtHModule.getTag()).trim()))
                            && (DmShared.iNull(detail_Mdl.getRoomCategoryCode()).longValue() == DmShared.iNull(txtHRoomCategory.getTag()).longValue())
                            && (DmShared.iNull(detail_Mdl.getCategoryCode()).longValue() == DmShared.iNull(txtHServiceCat.getTag()).longValue())
                            && (DmShared.iNull(detail_Mdl.getServiceCode()).longValue() == DmShared.iNull(txtHService.getTag()).longValue())
                            && (DmShared.iNull(detail_Mdl.getGroupCode()).longValue() == DmShared.iNull(txtHServiceGroup.getTag()).longValue())) {
                        if (detail_Mdl.getSharedKeyFieldHashCode() != hashCode) {
                            new DmAlertMessageBox(DmShared.xNull(txtHModule.getText()) + " already exists.", txtHModule).show();
                            return;
                        }
                    }
                }
            }

//            if ((DmShared.vNull(txtHeaderConsAmount.getText()).doubleValue() == 0) && (DmShared.vNull(txtLineConsAmount.getText()).doubleValue() == 0)
//                    && (DmShared.vNull(txtHeaderRefAmount.getText()).doubleValue() == 0) && (DmShared.vNull(txtLineRefAmount.getText()).doubleValue() == 0)) {
//                new DmAlertMessageBox("Consultant  Amount / Referred Amount is mandatory.", txtHeaderConsAmount).show();
//                return;
//            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHServiceCat).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdSharingDetail_Mdl ourRow = (GrdSharingDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHEmpanelment.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHEmpanelment.getText()));
        ourRow.setRoomCategoryCode(DmShared.xNull(txtHRoomCategory.getTag()));
        ourRow.setRoomCategoryName(DmShared.xNull(txtHRoomCategory.getText()));
        ourRow.setModuleName(DmShared.xNull(txtHModule.getTag()));
        ourRow.setCategoryCode(DmShared.xNull(txtHServiceCat.getTag()));
        ourRow.setCategoryName(DmShared.xNull(txtHServiceCat.getText()));
        ourRow.setGroupCode(DmShared.xNull(txtHServiceGroup.getTag()));
        ourRow.setGroupName(DmShared.xNull(txtHServiceGroup.getText()));
        ourRow.setServiceCode(DmShared.xNull(txtHService.getTag()));
        ourRow.setServiceName(DmShared.xNull(txtHService.getText()));
        ourRow.setHeaderConTypeCode(DmShared.xNull(txtHHeaderConsType.getTag()));
        ourRow.setHeaderConTypeName(DmShared.xNull(txtHHeaderConsType.getText()));
        ourRow.setHeaderRefTypeCode(DmShared.xNull(txtHHeaderRefType.getTag()));
        ourRow.setHeaderRefTypeName(DmShared.xNull(txtHHeaderRefType.getText()));
        ourRow.setHeaderConAmount(DmShared.vNull(txtHeaderConsAmount.getText()).doubleValue() > 0 ? DmClient.vNull(txtHeaderConsAmount.getText(), "0.00") : "");
        ourRow.setHeaderRefAmount(DmShared.vNull(txtHeaderRefAmount.getText()).doubleValue() > 0 ? DmClient.vNull(txtHeaderRefAmount.getText(), "0.00") : "");
        ourRow.setLineConTypeCode(DmShared.xNull(txtHLineConsType.getTag()));
        ourRow.setLineConTypeName(DmShared.xNull(txtHLineConsType.getText()));
        ourRow.setLineRefTypeCode(DmShared.xNull(txtHLineRefType.getTag()));
        ourRow.setLineRefTypeName(DmShared.xNull(txtHLineRefType.getText()));
        ourRow.setLineConAmount(DmShared.vNull(txtLineConsAmount.getText()).doubleValue() > 0 ? DmClient.vNull(txtLineConsAmount.getText(), "0.00") : "");
        ourRow.setLineRefAmount(DmShared.vNull(txtLineRefAmount.getText()).doubleValue() > 0 ? DmClient.vNull(txtLineRefAmount.getText(), "0.00") : "");

        if (DmShared.iNull(txtHHeaderConsType.getTag()).intValue() == 0 && DmShared.vNull(ourRow.getHeaderConAmount()).doubleValue() > 0) {
            ourRow.setHeaderConSharingDisplay(ourRow.getHeaderConAmount() + " %");
        } else {
            ourRow.setHeaderConSharingDisplay(ourRow.getHeaderConAmount());
        }
        if (DmShared.iNull(txtHHeaderRefType.getTag()).intValue() == 0 && DmShared.vNull(ourRow.getHeaderRefAmount()).doubleValue() > 0) {
            ourRow.setHeaderRefSharingDisplay(ourRow.getHeaderRefAmount() + " %");
        } else {
            ourRow.setHeaderRefSharingDisplay(ourRow.getHeaderRefAmount());
        }
        if (DmShared.iNull(txtHLineConsType.getTag()).intValue() == 0 && DmShared.vNull(ourRow.getLineConAmount()).doubleValue() > 0) {
            ourRow.setLineConSharingDisplay(ourRow.getLineConAmount() + " %");
        } else {
            ourRow.setLineConSharingDisplay(ourRow.getLineConAmount());
        }
        if (DmShared.iNull(txtHLineRefType.getTag()).intValue() == 0 && DmShared.vNull(ourRow.getLineRefAmount()).doubleValue() > 0) {
            ourRow.setLineRefSharingDisplay(ourRow.getLineRefAmount() + " %");
        } else {
            ourRow.setLineRefSharingDisplay(ourRow.getLineRefAmount());
        }

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

    public SharingDetailGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Sharing Detail", "550px", "250px");
        SrvAsync = new ServiceInvoker<ConsultantSharingMaster_SrvAsync>(GWT.create(ConsultantSharingMaster_Srv.class), ConsultantSharingMaster.class.getName());
        textHelpEvents();
    }

    private void manageServiceDisplay() {
        if (DmShared.iNull(txtHService.getTag()).longValue() != 0) {
            txtHServiceCat.setTag("");
            txtHServiceCat.setText("");
            txtHServiceCat.setEnabled(false);
            txtHServiceGroup.setTag("");
            txtHServiceGroup.setText("");
            txtHServiceGroup.setEnabled(false);
            txtHService.setEnabled(true);
        } else {
            txtHServiceCat.setEnabled(true);
            txtHServiceGroup.setEnabled(true);
        }
    }

    private void manageCategoryDisplay() {
        if (DmShared.iNull(txtHServiceCat.getTag()).longValue() != 0) {
            txtHService.setTag("");
            txtHService.setText("");
            txtHService.setEnabled(false);
            txtHServiceGroup.setTag("");
            txtHServiceGroup.setText("");
            txtHServiceGroup.setEnabled(false);
            txtHServiceCat.setEnabled(true);
        } else {
            txtHService.setEnabled(true);
            txtHServiceGroup.setEnabled(true);
        }
    }

    private void manageGroupDisplay() {
        if (DmShared.iNull(txtHServiceGroup.getTag()).longValue() != 0) {
            txtHService.setTag("");
            txtHService.setText("");
            txtHService.setEnabled(false);
            txtHServiceCat.setTag("");
            txtHServiceCat.setText("");
            txtHServiceCat.setEnabled(false);
            txtHServiceGroup.setEnabled(true);
        } else {
            txtHService.setEnabled(true);
            txtHServiceCat.setEnabled(true);
        }
    }

}
