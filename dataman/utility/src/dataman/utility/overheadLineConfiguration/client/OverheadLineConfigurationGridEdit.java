package dataman.utility.overheadLineConfiguration.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.overheadLineConfiguration.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class OverheadLineConfigurationGridEdit<T1, T2> extends OverheadLineConfigurationGridEdit_Dsg<T1, T2> implements RowClickEvent.RowClickHandler {

    interface CallBack {

        public void onOk(GrdOverheadLine_Mdl grdManageHeader);

        public void onDelete();
    }

    ServiceInvoker<OverheadLineConfiguration_SrvAsync> SrvAsync;
    CallBack callBack;
    OverheadLineConfiguration_Mdl parentRow;
    private String tableName = "", manualCode = "";
    private int hashCode;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {

        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAtRateSaveIn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAtRateSaveIn(tableName, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAtRateSaveIn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAtRateSaveIn.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAmtSaveIn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAmtSaveIn(tableName, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAmtSaveIn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAmtSaveIn.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAtRateEditable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAtRateEditable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAtRateEditable.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAmtEditable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAmtEditable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAmtEditable.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOverheadEditable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOverheadEditable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOverheadEditable.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHShowFormulaValue.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShowFormulaValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHShowFormulaValue.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHRoundOff.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRoundOff(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoundOff).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoundOff.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAtRateMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAtRateMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAtRateMandatory.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAmtMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAmtMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAmtMandatory.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHACSave.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHACSave(tableName, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHACSave).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACSave.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOverhead.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHOverhead_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOverhead(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHOverhead_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOverhead).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHOverhead_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHOverhead.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHOverhead).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHOverhead_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        manualCode = tRow.getManualCode();
                    } else {
                        manualCode = "";
                    }
                }
            }
        });

        txtHOverheadSaveIn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOverheadSaveIn(tableName, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOverheadSaveIn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOverheadSaveIn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull((result.getErrorMessage())), txtHOverheadSaveIn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHACEditable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHACEditable.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPrintable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrintable.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPostAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPostAC_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPostAC(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPostAC_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPostAC_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPostAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPostAC_Mdl tRow) {
            }
        });

        txtHAtRateAmount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtAtRateAmount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAtRateAmount.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void buttonClick() {
        grdFormula.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    String fval = txtFormula.getText() + "[" + grdFormula.getGrid().getSelectionModel().getSelectedItem().getOverheadCode() + "]";
                    txtFormula.setText(fval);
                    pnlGrid.hide();
                    txtFormula.setFocus(true);
                }
            }
        }, KeyDownEvent.getType());

        btnformula.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    parentRow = new OverheadLineConfiguration_Mdl();
                    pnlGrid.show();
                    pnlGrid.center();
                    showFormula();
                } catch (DmException e) {
                    new DmAlertMessageBox(e.toString(), txtFormula).show();
                }
            }
        });
    }

    public OverheadLineConfigurationGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Overhead Line Configuration", "500px", "330px");
        SrvAsync = new ServiceInvoker<>(GWT.create(OverheadLineConfiguration_Srv.class), OverheadLineConfiguration.class.getName());
        textHelpEvents();
        grdFormula.getGrid().addRowClickHandler(this);
        flwForm.addAttachHandler(this);
        buttonClick();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String tableName, String duplicatevalue) {
        int cellIndex = 1;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, newNeeded, deleteNeeded);
        if (DmShared.xNull(duplicatevalue).equalsIgnoreCase("")) {
            onAdd();
        }
        this.tableName = tableName;
        txtSrlNo.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdOverheadLine_Mdl ourRow = (GrdOverheadLine_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtSrlNo.setText(DmShared.xNull(ourRow.getSrlNo()));

        manualCode = DmShared.xNull(ourRow.getManualCode());
        txtHAmtEditable.setTag(DmShared.xNull(ourRow.getAmtEditableCode()));
        txtHAmtEditable.setText(DmShared.xNull(ourRow.getAmtEditable()));

        txtHAmtMandatory.setTag(DmShared.xNull(ourRow.getAmtMandatoryCode()));
        txtHAmtMandatory.setText(DmShared.xNull(ourRow.getAmtMandatory()));

        txtHAmtSaveIn.setTag(DmShared.xNull(ourRow.getAmtSaveInCode()));
        txtHAmtSaveIn.setText(DmShared.xNull(ourRow.getAmtSaveIn()));

        txtHAtRateEditable.setTag(DmShared.xNull(ourRow.getAtRateEditableCode()));
        txtHAtRateEditable.setText(DmShared.xNull(ourRow.getAtRateEditable()));

        txtHAtRateMandatory.setTag(DmShared.xNull(ourRow.getAtRateMandatoryCode()));
        txtHAtRateMandatory.setText(DmShared.xNull(ourRow.getAtRateMandatory()));

        txtHPrintable.setTag(DmShared.xNull(ourRow.getPrintableCode()));
        txtHPrintable.setText(DmShared.xNull(ourRow.getPrintable()));

        txtHAtRateSaveIn.setTag(DmShared.xNull(ourRow.getAtRateSaveInCode()));
        txtHAtRateSaveIn.setText(DmShared.xNull(ourRow.getAtRateSaveIn()));

        txtHACSave.setTag(DmShared.xNull(ourRow.getAcSave()));
        txtHACSave.setText(DmShared.xNull(ourRow.getAcSaveCode()));

        txtHNature.setTag(DmShared.xNull(ourRow.getNatureCode()));
        txtHNature.setText(DmShared.xNull(ourRow.getNature()));

        txtHOverhead.setTag(DmShared.xNull(ourRow.getOverheadCode()));
        txtHOverhead.setText(DmShared.xNull(ourRow.getOverhead()));

        if (DmShared.xNull(ourRow.getOverheadCode()).equalsIgnoreCase("")) {
            onAdd();
        }
        txtHPostAC.setTag(DmShared.xNull(ourRow.getPostACCode()));
        txtHPostAC.setText(DmShared.xNull(ourRow.getPostAC()));

        txtHOverheadSaveIn.setTag(DmShared.xNull(ourRow.getOverheadSaveInCode()));
        txtHOverheadSaveIn.setText(DmShared.xNull(ourRow.getOverheadSaveIn()));

        txtHRoundOff.setTag(DmShared.xNull(ourRow.getRoundOffCode()));
        txtHRoundOff.setText(DmShared.xNull(ourRow.getRoundOff()));

        txtHShowFormulaValue.setTag(DmShared.xNull(ourRow.getShowFormulaValueCode()));
        txtHShowFormulaValue.setText(DmShared.xNull(ourRow.getShowFormulaValue()));

        txtHOverheadEditable.setTag(DmShared.xNull(ourRow.getOverheadEditableCode()));
        txtHOverheadEditable.setText(DmShared.xNull(ourRow.getOverheadEditable()));

        txtHAtRateAmount.setTag(DmShared.xNull(ourRow.getAtRateAmountTag()));
        txtHAtRateAmount.setText(DmShared.xNull(ourRow.getAtRateAmountText()));

        txtAtRate.setText(DmClient.vNull(ourRow.getAtRate(), "0.00"));
        txtFormula.setText(DmShared.xNull(ourRow.getFormula()));
        txtUniqueID.setText(DmShared.xNull(ourRow.getUniqueID()));

        txtHACEditable.setTag(DmShared.xNull(ourRow.getaCEditableCode()));
        txtHACEditable.setText(DmShared.xNull(ourRow.getAcEditable()));
    }

    @Override
    public T1 getNewRow() {
        onAdd();
        return (T1) new GrdOverheadLine_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtSrlNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("S.No. is mandatory.", txtSrlNo).show();
            return;
        }
        if (DmShared.xNull(txtHOverhead.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Overhead is mandatory.", txtHOverhead).show();
            return;
        }
        if (DmShared.xNull(txtHAtRateEditable.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("At Rate Editable is mandatory.", txtHAtRateEditable).show();
            return;
        }
        if (DmShared.xNull(txtHAmtEditable.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Amt Editable is mandatory.", txtHAmtEditable).show();
            return;
        }
        if (DmShared.xNull(txtHOverheadEditable.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("OverHead Editable  is mandatory.", txtHOverheadEditable).show();
            return;
        }
        if (DmShared.xNull(txtHShowFormulaValue.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Show Formula Value is mandatory.", txtHShowFormulaValue).show();
            return;
        }
        if (DmShared.xNull(txtHAtRateMandatory.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("At Rate Mandatory is mandatory.", txtHAtRateMandatory).show();
            return;
        }
        if (DmShared.xNull(txtHAmtMandatory.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Amt. Mandatory is mandatory.", txtHAmtMandatory).show();
            return;
        }
        if (DmShared.xNull(txtHRoundOff.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Round Off is mandatory.", txtHRoundOff).show();
            return;
        }
        if (DmShared.xNull(txtHACEditable.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("A/c Editable is mandatory.", txtHACEditable).show();
            return;
        }
        if (DmShared.xNull(txtHAtRateAmount.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("At Rate Amount is mandatory.", txtHAtRateAmount).show();
            return;
        }
        if (!DmShared.xNull(txtUniqueID.getText()).trim().equals("")) {
            if (!DmClient.regExMatches(DmShared.xNull(txtUniqueID.getText()).trim().toUpperCase(), "^([\\[]*[A-Z0-9]+[\\]]*)$")) {
                new DmAlertMessageBox("Only A-Z, 0-9 & Square Brackets are allowed in Unique ID.", txtUniqueID).show();
                return;
            }
        }
        if ((!DmShared.xNull(txtSrlNo.getText()).equalsIgnoreCase(""))
                && (!DmShared.xNull(txtHOverhead.getTag()).equalsIgnoreCase(""))) {
            for (GrdOverheadLine_Mdl overhead : (List<GrdOverheadLine_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (!DmShared.xNull(txtUniqueID.getText()).trim().equals("") && !DmShared.xNull(txtUniqueID.getText()).trim().startsWith("[")) {
                    if (DmShared.xNull(txtUniqueID.getText()).trim().equalsIgnoreCase(DmShared.xNull(overhead.getUniqueID()))) {
                        if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(overhead.getSharedKeyFieldHashCode()).doubleValue()) {
                            new DmAlertMessageBox(txtUniqueID.getText() + " already exists.", txtUniqueID).show();
                            return;
                        }
                    }
                }

                if (DmShared.xNull(txtHOverhead.getTag()).equalsIgnoreCase(DmShared.xNull(overhead.getOverheadCode()))) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(overhead.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Overhead already exists.", txtHOverhead).show();
                        return;
                    }
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdOverheadLine_Mdl ourRow = (GrdOverheadLine_Mdl) row;

        ourRow.setSrlNo(DmShared.xNull(txtSrlNo.getText()));

        ourRow.setAmtEditable(DmShared.xNull(txtHAmtEditable.getText()));
        ourRow.setAmtEditableCode(DmShared.xNull(txtHAmtEditable.getTag()));

        ourRow.setAmtMandatory(DmShared.xNull(txtHAmtMandatory.getText()));
        ourRow.setAmtMandatoryCode(DmShared.xNull(txtHAmtMandatory.getTag()));

        ourRow.setAmtSaveIn(DmShared.xNull(txtHAmtSaveIn.getText()));
        ourRow.setAmtSaveInCode(DmShared.xNull(txtHAmtSaveIn.getTag()));

        ourRow.setAtRateEditable(DmShared.xNull(txtHAtRateEditable.getText()));
        ourRow.setAtRateEditableCode(DmShared.xNull(txtHAtRateEditable.getTag()));

        ourRow.setPrintable(DmShared.xNull(txtHPrintable.getText()));
        ourRow.setPrintableCode(DmShared.xNull(txtHPrintable.getTag()));

        ourRow.setAtRateMandatory(DmShared.xNull(txtHAtRateMandatory.getText()));
        ourRow.setAtRateMandatoryCode(DmShared.xNull(txtHAtRateMandatory.getTag()));

        ourRow.setAtRateSaveIn(DmShared.xNull(txtHAtRateSaveIn.getText()));
        ourRow.setAtRateSaveInCode(DmShared.xNull(txtHAtRateSaveIn.getTag()));

        ourRow.setAcSave(DmShared.xNull(txtHACSave.getText()));
        ourRow.setAcSaveCode(DmShared.xNull(txtHACSave.getTag()));

        ourRow.setNature(DmShared.xNull(txtHNature.getText()));
        ourRow.setNatureCode(DmShared.xNull(txtHNature.getTag()));

        ourRow.setOverhead(DmShared.xNull(txtHOverhead.getText()));
        ourRow.setOverheadCode(DmShared.xNull(txtHOverhead.getTag()));

        ourRow.setPostAC(DmShared.xNull(txtHPostAC.getText()));
        ourRow.setPostACCode(DmShared.xNull(txtHPostAC.getTag()));

        ourRow.setOverheadSaveIn(DmShared.xNull(txtHOverheadSaveIn.getText()));
        ourRow.setOverheadSaveInCode(DmShared.xNull(txtHOverheadSaveIn.getTag()));

        ourRow.setRoundOff(DmShared.xNull(txtHRoundOff.getText()));
        ourRow.setRoundOffCode(DmShared.xNull(txtHRoundOff.getTag()));

        ourRow.setShowFormulaValue(DmShared.xNull(txtHShowFormulaValue.getText()));
        ourRow.setShowFormulaValueCode(DmShared.xNull(txtHShowFormulaValue.getTag()));

        ourRow.setOverheadEditable(DmShared.xNull(txtHOverheadEditable.getText()));
        ourRow.setOverheadEditableCode(DmShared.xNull(txtHOverheadEditable.getTag()));

        ourRow.setAtRateAmountTag(DmShared.xNull(txtHAtRateAmount.getTag()));
        ourRow.setAtRateAmountText(DmShared.xNull(txtHAtRateAmount.getText()));

        ourRow.setAtRate(DmClient.vNull(txtAtRate.getText(), "0.00"));
        ourRow.setFormula(DmShared.xNull(txtFormula.getText()));
        ourRow.setUniqueID(DmShared.xNull(txtUniqueID.getText()).trim().toUpperCase());
        ourRow.setManualCode(DmShared.xNull(manualCode));

        ourRow.setAcEditable(DmShared.xNull(txtHACEditable.getText()));
        ourRow.setaCEditableCode(DmShared.xNull(txtHACEditable.getTag()));

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
        onAdd();
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

    public void showFormula() {
        SrvAsync.invokeRPC().getTxtHFormula(new AsyncCallback<DmResult<List<TxtHFormula_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<TxtHFormula_Mdl>> result) {
                if (result.isStatus()) {
                    grdFormula.getGrid().getStore().clear();
                    for (int i = 0; i < result.getData().size(); i++) {
                        grdFormula.getGrid().getStore().add(i, result.getData().get(i));
                    }
                    if (result.getData().size() > 0) {
                        ((CellSelectionModel) grdFormula.getGrid().getSelectionModel()).selectCell(0, 0);
                    }
                }
            }
        });
    }

    @Override
    public void onRowClick(RowClickEvent event) {
        String fval = txtFormula.getText() + "[" + grdFormula.getGrid().getStore().get(event.getRowIndex()).getOverheadCode() + "]";
        txtFormula.setText(fval);
        pnlGrid.hide();
        txtFormula.setFocus(true);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
            pnlGrid.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            pnlGrid.hide();
        }
    }

    private void onAdd() {
        txtHOverheadEditable.setTag("0");
        txtHOverheadEditable.setText("No");

        txtHAtRateEditable.setTag("0");
        txtHAtRateEditable.setText("No");

        txtHAmtEditable.setTag("0");
        txtHAmtEditable.setText("No");

        txtHAmtMandatory.setTag("0");
        txtHAmtMandatory.setText("No");

        txtHAtRateMandatory.setTag("0");
        txtHAtRateMandatory.setText("No");

        txtHRoundOff.setTag("N");
        txtHRoundOff.setText("No");

        txtHShowFormulaValue.setTag("1");
        txtHShowFormulaValue.setText("Yes");

        txtHACEditable.setTag("0");
        txtHACEditable.setText("No");

        txtHPrintable.setTag("1");
        txtHPrintable.setText("Yes");

        txtHAtRateAmount.setTag("0");
        txtHAtRateAmount.setText("Auto");
    }
}
