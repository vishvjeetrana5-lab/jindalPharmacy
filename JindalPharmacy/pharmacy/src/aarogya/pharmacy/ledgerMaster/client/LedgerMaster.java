package aarogya.pharmacy.ledgerMaster.client;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Srv;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_SrvAsync;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Vld;
import aarogya.pharmacy.ledgerMaster.shared.TxtHCity_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import java.util.List;

public class LedgerMaster extends LedgerMaster_Dsg implements IsWidget, KeyUpHandler {

    private final ServiceInvoker<LedgerMaster_SrvAsync> srvAsync;

    public LedgerMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("A", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(LedgerMaster_Srv.class), LedgerMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        manageEvents();
        swi.setWidget(this);
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                clear();
                manageDisplay(true);
                txtHInitial.setFocus(true);
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
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LedgerMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void manageEvents() {
        txtDisplayName.addKeyUpHandler(this);
        txtManualCode.addKeyUpHandler(this);
    }

    private void textHelpEvents() {
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
            }
        });

        txtHIdType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIdType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIdType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIdType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIdType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHInitial.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInitial(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInitial).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInitial.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInitial).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<LedgerMaster_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<LedgerMaster_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<LedgerMaster_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, LedgerMaster_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private LedgerMaster_Mdl topControlSaveInitialize() {
        LedgerMaster_Mdl mrTemp = new LedgerMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(getName());
        mrTemp.setInitial(txtHInitial.getText());
        mrTemp.setDisplayName(txtDisplayName.getText());
        mrTemp.setManualCode(txtManualCode.getText());
        mrTemp.setMobile(txtMobile.getText());
        mrTemp.setAddress(txtAddress.getText());
        mrTemp.setCityCode(txtHCity.getTag());
        mrTemp.setIdTypeCode(txtHIdType.getTag());
        mrTemp.setIdNo(txtIdNo.getText());
        mrTemp.setIsActive(txtHIsActive.getTag());

        return mrTemp;
    }

    private void topControlSave() {
        LedgerMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            LedgerMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC(true).saveRecord(mrTemp, new AsyncCallback<DmResult<LedgerMaster_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<LedgerMaster_Mdl> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData().getSharedKeyFieldCode());
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
                new DmAlertMessageBox(errorMessage, txtHInitial).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;
        }

    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC(true).getMoveRec(strKey, new AsyncCallback<DmResult<LedgerMaster_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<LedgerMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<LedgerMaster_Mdl> result) {
        clear();
        if (result.isStatus()) {
            LedgerMaster_Mdl mrTemp = result.getData();

            topControl.setDocId(mrTemp.getSharedKeyFieldCode());

            txtName.setText(mrTemp.getSharedKeyFieldName());
            txtHInitial.setTag(mrTemp.getInitial());
            txtHInitial.setText(mrTemp.getInitial());
            txtDisplayName.setText(mrTemp.getDisplayName());
            txtManualCode.setText(mrTemp.getManualCode());
            txtMobile.setText(mrTemp.getMobile());
            txtAddress.setText(mrTemp.getAddress());
            txtHCity.setTag(mrTemp.getCityCode());
            txtHCity.setText(mrTemp.getCityName());
            txtHIdType.setTag(mrTemp.getIdTypeCode());
            txtHIdType.setText(mrTemp.getIdTypeName());
            txtIdNo.setText(mrTemp.getIdNo());
            txtAccountName.setTag(mrTemp.getAccountCode());
            txtAccountName.setText(mrTemp.getAccountName());
            txtRegNo.setTag(mrTemp.getRegDocId());
            txtRegNo.setText(mrTemp.getRegRecId());
            lblPharmacyBalance.setText(DmShared.vNull(mrTemp.getPharmacyBalance()) != 0 ? "Pharmacy Balance: " + mrTemp.getPharmacyBalance() : "");

            txtHIsActive.setTag(mrTemp.getIsActive());
            txtHIsActive.setText(DmShared.iNull(mrTemp.getIsActive()).intValue() == 1 ? "Yes" : "No");

            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, mrTemp.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void topControlCard() {
        srvAsync.invokeRPC(true).getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        lblPharmacyBalance.setText("");
    }

    private void manageDisplay(boolean flag) {
        txtName.setEnabled(false);
        txtAccountName.setEnabled(false);
        txtRegNo.setEnabled(false);
    }

    private String getName() {
        return DmShared.xNull(txtDisplayName.getText()).trim() + " [" + DmShared.xNull(txtManualCode.getText()).trim() + "]";
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource() == txtDisplayName
                || event.getSource() == txtManualCode) {
            txtName.setText(getName());
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

}
