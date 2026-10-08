package aarogya.pharmacy.medicineDepartmentInfo.client;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.medicineDepartmentInfo.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public final class MedicineDepartmentInfo extends MedicineDepartmentInfo_Dsg {

    final DepartmentInfoDetailsGridEdit<GrdMedicineDepartmentInfo_Mdl, KeyFieldHashCode_T_PrpAcs> medGridEdit = new DepartmentInfoDetailsGridEdit(grdMedDetails);

    ServiceInvoker<MedicineDepartmentInfo_SrvAsync> SrvAsync;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();

                txtHSiteName.setFocus(true);
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                grdMedDetails.getGrid().getStore().add(new GrdMedicineDepartmentInfo_Mdl());
                ((CellSelectionModel) grdMedDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineDepartmentInfo.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void gridEvents() {
        medGridEdit.addCallBack(new DepartmentInfoDetailsGridEdit.CallBack() {
            @Override
            public void onOk(GrdMedicineDepartmentInfo_Mdl gridItem_Mdl) {
                grdMedDetails.commitChanges();
            }

            @Override
            public void onDelete() {
                grdMedDetails.commitChanges();
            }
        });

        grdMedDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {

                if (DmShared.xNull(txtHSiteName.getTag()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Site Name is mandatory.", txtHSiteName).show();
                    return;
                }

                if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Department is mandatory.", txtHDepartment).show();
                    return;
                }
                showItemGridEdit(event.getRowIndex());
            }
        });
        grdMedDetails.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    if (DmShared.xNull(txtHSiteName.getTag()).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Site Name is mandatory.", txtHSiteName).show();
                        return;
                    }

                    if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Department is mandatory.", txtHDepartment).show();
                        return;
                    }
                    showItemGridEdit(grdMedDetails.getGrid().getStore().indexOf(grdMedDetails.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showItemGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHSiteName.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Site Name is mandatory.", txtHSiteName).show();
            return;
        }
        if (DmShared.xNull(txtHDepartment.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Department Name is mandatory.", txtHDepartment).show();
            return;
        }

        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            medGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            medGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void textHelpEvents() {

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
                    if (!DmShared.xNull(txtHSiteName.getTag()).trim().equals("")) {
                        fillMedicineGrid(DmShared.xNull(txtHSiteName.getTag()), DmShared.xNull(txtHDepartment.getTag()));
                    }
                }
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
                if (blnStatus && tRow != null) {
                    if (!DmShared.xNull(txtHDepartment.getTag()).trim().equals("")) {
                        fillMedicineGrid(DmShared.xNull(txtHSiteName.getTag()), DmShared.xNull(txtHDepartment.getTag()));
                    }

                }
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    public MedicineDepartmentInfo(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(MedicineDepartmentInfo_Srv.class), MedicineDepartmentInfo.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        swi.setWidget(this);

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private MedicineDepartmentInfo_Mdl topControlSaveInitialize() {
        MedicineDepartmentInfo_Mdl mrTemp = new MedicineDepartmentInfo_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setSiteCode(DmShared.xNull(txtHSiteName.getTag()));
        mrTemp.setSiteName(DmShared.xNull(txtHSiteName.getText()));
        mrTemp.getGrdMed_Mdl().clear();
        mrTemp.getGrdMed_Mdl().addAll(grdMedDetails.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        MedicineDepartmentInfo_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            MedicineDepartmentInfo_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), "", result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHSiteName).show();
                break;

            case 200:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;

            case 600:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdMedDetails.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtHSiteName).show();
                break;
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<MedicineDepartmentInfo_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineDepartmentInfo_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<MedicineDepartmentInfo_Mdl> result) {
        MedicineDepartmentInfo_Mdl data;

        clear();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtHDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtHDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            txtHSiteName.setTag(DmShared.xNull(data.getSiteCode()));
            txtHSiteName.setText(DmShared.xNull(data.getSiteName()));
            grdMedDetails.getGrid().getStore().clear();
            grdMedDetails.getGrid().getStore().addAll(data.getGrdMed_Mdl());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        manageDisplay(false);
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        grdMedDetails.getGrid().getStore().clear();
        grdMedDetails.getGrid().getStore().add(new GrdMedicineDepartmentInfo_Mdl());
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
    }

    private void fillMedicineGrid(String site, String department) {
        String code = "";
        code = site + "|" + department;

        SrvAsync.invokeRPC().getMoveRec(code, new AsyncCallback<DmResult<MedicineDepartmentInfo_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineDepartmentInfo_Mdl> result) {
                gridFillSuccess(result);
            }
        });
    }

    private void gridFillSuccess(DmResult<MedicineDepartmentInfo_Mdl> result) {
        MedicineDepartmentInfo_Mdl data;

        grdMedDetails.getGrid().getStore().clear();
        if (result.isStatus()) {
            data = result.getData();
            if (!data.getGrdMed_Mdl().isEmpty()) {
                grdMedDetails.getGrid().getStore().addAll(data.getGrdMed_Mdl());
            }
            txtHSiteName.setEnabled(false);
            txtHDepartment.setEnabled(false);
            grdMedDetails.getGrid().getStore().add(new GrdMedicineDepartmentInfo_Mdl());
            ((CellSelectionModel) grdMedDetails.getGrid().getSelectionModel()).selectCell(0, 1);

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

    }

    private void manageDisplay(boolean flag) {
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHSiteName.setEnabled(false);
            txtHDepartment.setEnabled(false);
        }
    }
}
