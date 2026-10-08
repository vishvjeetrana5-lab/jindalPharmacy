package aarogya.pharmacy.profitSlab.client;

import aarogya.pharmacy.profitSlab.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.ArrayList;
import java.util.List;

public final class ProfitSlab extends ProfitSlab_Dsg implements IsWidget, AttachEvent.Handler, BlurHandler {

    ServiceInvoker<ProfitSlab_SrvAsync> SrvAsync;
    DefaultValue_Mdl dvm = new DefaultValue_Mdl();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                fillGrdDetail();
                txtMarginFrom.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                dmGrd.getGrid().getStore().add(new GrdProfitSlab_Mdl());
//                txtHService.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ProfitSlab.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

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

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
        } else {
//            gridEdit.hide();
        }
    }

    public ProfitSlab(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ProfitSlab_Srv.class), ProfitSlab.class.getName());
        this.asWidget().setTitle("Profit Slab Master");
        flwForm.addAttachHandler(this);
        txtMarginFrom.addBlurHandler(this);
        txtMarginTo.addBlurHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        iniDefaultValues(swi);

    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    if (dvm.getLocaleCountry() == DmEnum.DmLocaleCountry.CAMBODIA_USA) {
                        colFindMarginPerFrom.setHeader("Rate");
                        colGrdMarginPer.setHeader("Rate");
                        lblMarginPer.setText("Rate");
                    } else {
                        colFindMarginPerFrom.setHeader("Profit %");
                        colGrdMarginPer.setHeader("Profit %");
                        lblMarginPer.setText("Profit %");
                    }
                    if (dmFind.getDmGrid().getGrid().getView().getHeader() != null) {
                        dmFind.getDmGrid().getGrid().getView().getHeader().refresh();
                    }
                    if (dmGrd.getGrid().getView().getHeader() != null) {
                        dmGrd.getGrid().getView().getHeader().refresh();
                    }
                    swi.setWidget(ProfitSlab.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ProfitSlab_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ProfitSlab_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ProfitSlab_Mdl> result) {
        ProfitSlab_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            if (DmShared.xNull(data.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                clear();
            }
            topControl.setDocId(data.getSharedKeyFieldCode());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            txtMarginFrom.setText(DmShared.xNull(data.getMarginFrom()));
            txtMarginTo.setText(DmShared.xNull(data.getMarginTo()));
            txtProfitPercent.setText(DmShared.xNull(data.getProfitPercent()));
            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(data.getGrdData());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        ProfitSlab_Mdl mrTemp;

        try {
            calculate();
            mrTemp = topControlSaveInitialize();
            ProfitSlab_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtMarginFrom).show();
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
                new DmAlertMessageBox(errorMessage, txtMarginTo).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtMarginFrom).show();
                break;
        }
    }

    private ProfitSlab_Mdl topControlSaveInitialize() {
        ProfitSlab_Mdl mrTemp = new ProfitSlab_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        dmGrd.getGrid().getStore().commitChanges();

        mrTemp.setMarginFrom(DmShared.xNull(txtMarginFrom.getText()));
        mrTemp.setMarginTo(DmShared.xNull(txtMarginTo.getText()));
        mrTemp.setProfitPercent(DmClient.vNull(txtProfitPercent.getText(), "0.00"));
        List<GrdProfitSlab_Mdl> OBJList = new ArrayList<>();
        OBJList.addAll(dmGrd.getGrid().getStore().getAll());

        mrTemp.getGrdData().addAll(OBJList);
        return mrTemp;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        dmGrd.getGrid().getStore().clear();
    }

    private void fillGrdDetail() {
        SrvAsync.invokeRPC().getGrdData(new AsyncCallback<DmResult<List<GrdProfitSlab_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdProfitSlab_Mdl>> result) {
                dmGrd.getGrid().getStore().clear();
                dmGrd.getGrid().getStore().addAll(result.getData());
            }
        });
    }

    private void calculate() {
        txtMarginFrom.setText(DmClient.vNull(txtMarginFrom.getText(), DmShared.getNumberFormat(DmShared.iNull(dvm.getRateDecimalPlaces()).intValue())));
        txtMarginTo.setText(DmClient.vNull(txtMarginTo.getText(), DmShared.getNumberFormat(DmShared.iNull(dvm.getRateDecimalPlaces()).intValue())));
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtMarginFrom) || event.getSource().equals(txtMarginTo)) {
            calculate();
        }
    }
}
