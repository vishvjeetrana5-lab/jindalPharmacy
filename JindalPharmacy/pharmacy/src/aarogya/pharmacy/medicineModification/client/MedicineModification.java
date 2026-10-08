package aarogya.pharmacy.medicineModification.client;

import aarogya.pharmacy.medicineModification.shared.*;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import java.util.List;

public final class MedicineModification extends MedicineModification_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents {

    private final ServiceInvoker<MedicineModification_SrvAsync> srvAsync;
    private ModifyReason_Mdl modifyReasonMdl;
    private final ModifyReason modifyReason = new ModifyReason();
    private final CompositionGrid<GrdComposition_Mdl, KeyFieldHashCode_T_PrpAcs> compositionGrid = new CompositionGrid(grdCompositionDetails);
    private DefaultValue_Mdl defaultValue;

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                MedicineModification.this.modifyReasonMdl = modifyReason;
                if (DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlSave();
                }
            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtDisplayName.setFocus(true);
                manageDisplay(true);
                grdCompositionDetails.getGrid().getStore().add(new GrdComposition_Mdl());

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

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                showModifyReason();
            }

        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineModification.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void gridEvents() {
        compositionGrid.addCallBack(new CompositionGrid.CallBack() {

            @Override
            public void onOk(GrdComposition_Mdl grdComposition_Mdl) {
                grdCompositionDetails.commitChanges();
            }

            @Override
            public void onDelete() {
                grdCompositionDetails.commitChanges();
            }

        });

        grdCompositionDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showCompositionDetailGrid(event.getRowIndex());
            }
        });

        grdCompositionDetails.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showCompositionDetailGrid(grdCompositionDetails.getGrid().getStore().indexOf(grdCompositionDetails.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showCompositionDetailGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            compositionGrid.show(currentRowIndex, false, false, false, this);
        } else {
            compositionGrid.show(currentRowIndex, true, true, true, this);
        }
    }

    private void textHelpEvents() {
        txtDisplayName.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                setNameText();
            }
        });

        txtDisplayName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtDisplayName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtDisplayName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtDisplayName).show();
                        }
                    }
                });
            }
        });

        txtHMedGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedGrp_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedGrp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedGrp_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedGrp_Mdl t1Row) {

            }
        });
        txtHCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    setNameText();
                }
            }
        });
        txtHCompany.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCompany_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCompany_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHCompany.setTag2nd(t1Row.getShortName());
                    setNameText();
                }
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public MedicineModification(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("A", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(MedicineModification_Srv.class), MedicineModification.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        handlerEvents();
        manageDisplay(false);
        iniDefault(swi);
    }

    private void iniDefault(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    manageDisplay(false);
                    swi.setWidget(MedicineModification.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private MedicineModification_Mdl topControlSaveInitialize() {
        MedicineModification_Mdl mrTemp = new MedicineModification_Mdl();

        setNameText();

        mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        mrTemp.setCategoryText(DmShared.xNull(txtHCategory.getText()));
        mrTemp.setCategoryTag(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setCompanyTag(DmShared.xNull(txtHCompany.getTag()));
        mrTemp.setGroupText(DmShared.xNull(txtHMedGroup.getText()));
        mrTemp.setGroupTag(DmShared.xNull(txtHMedGroup.getTag()));
        mrTemp.setItemPacking(DmShared.xNull(txtPackSize.getText()));

        mrTemp.getLstComposition().addAll(grdCompositionDetails.getGrid().getStore().getAll());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReason(modifyReasonMdl);
        }

        return mrTemp;
    }

    private void topControlSave() {
        MedicineModification_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            MedicineModification_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtDisplayName).show();
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
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHCompany).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHCategory).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHMedGroup).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtPackSize).show();
            default:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;

        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult< MedicineModification_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult< MedicineModification_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult< MedicineModification_Mdl> result) {
        MedicineModification_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtName.setTag(data.getSharedKeyFieldCode());
            txtName.setText(data.getSharedKeyFieldName());
            txtDisplayName.setText(data.getDisplayName());
            txtPackSize.setText(data.getItemPacking());
            txtHCategory.setTag(data.getCategoryTag());
            txtHCategory.setText(data.getCategoryText());
            txtHCompany.setTag(data.getCompanyTag());
            txtHCompany.setText(data.getCompanyText());
            txtHCompany.setTag2nd(data.getCompanyShortName());
            txtHMedGroup.setTag(data.getGroupTag());
            txtHMedGroup.setText(data.getGroupText());

            grdCompositionDetails.getGrid().getStore().clear();
            grdCompositionDetails.getGrid().getStore().addAll(data.getLstComposition());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        manageDisplay(false);
        topControl.setDisplay(true);
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void manageDisplay(boolean flag) {
        txtName.setEnabled(false);
    }

    private void setNameText() {
        String medicineName = DmShared.xNull(txtDisplayName.getText());
        if (defaultValue != null) {
            if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("U")) {
                medicineName = medicineName.toUpperCase();
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("L")) {
                medicineName = medicineName.toLowerCase();
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("S")) {
                medicineName = DmShared.sentenceCase(medicineName);
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("P")) {
                medicineName = DmShared.properCase(medicineName);
            }
        }
        txtDisplayName.setText(medicineName);
//        if (!DmShared.xNull(txtHCategory.getTag()).equalsIgnoreCase("")) {
//            txtName.setText(DmShared.xNull(txtDisplayName.getText()).trim());
//            medicineName += "-" + DmShared.xNull(txtHCategory.getText()).trim();
//            if (!DmShared.xNull(txtHCompany.getTag()).equalsIgnoreCase("")) {
//                medicineName += "-" + DmShared.xNull(txtHCompany.getTag2nd()).trim();
//                if (!DmShared.xNull(txtPackSize.getText()).equalsIgnoreCase("")) {
//                    medicineName += "(Packing: " + DmShared.xNull(txtPackSize.getText()).trim() + ")";
//                }
//            }
//        }
        if (!DmShared.xNull(txtHCategory.getTag()).equalsIgnoreCase("")) {
            medicineName += "-" + DmShared.xNull(txtHCategory.getText()).trim();
        }
        if (!DmShared.xNull(txtHCompany.getTag()).equalsIgnoreCase("")) {
            medicineName += "-" + DmShared.xNull(txtHCompany.getTag2nd()).trim();
        }
        if (!DmShared.xNull(txtPackSize.getText()).equalsIgnoreCase("")) {
            medicineName += "(Packing: " + DmShared.xNull(txtPackSize.getText()).trim() + ")";
        }
        txtName.setText(medicineName);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            compositionGrid.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if ((event.getSource() == txtName)) {
            if (!DmShared.xNull(txtName.getText()).equalsIgnoreCase("")) {
                setNameText();
            }
        } else if ((event.getSource() == txtPackSize)) {
            if (!DmShared.xNull(txtPackSize.getText()).equalsIgnoreCase("")) {
                setNameText();
            }
        }

    }

    private void handlerEvents() {

        txtDisplayName.addBlurHandler(this);
        txtPackSize.addBlurHandler(this);
    }
}
