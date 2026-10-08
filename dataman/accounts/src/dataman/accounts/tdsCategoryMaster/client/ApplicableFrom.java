package dataman.accounts.tdsCategoryMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import dataman.accounts.tdsCategoryMaster.shared.TdsCategoryMaster_SrvAsync;
import dataman.accounts.tdsCategoryMaster.shared.TdsCategoryMaster_Srv;

import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.PLT;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class ApplicableFrom extends ApplicableFrom_Dsg implements IsWidget, DmRootEvents, AttachEvent.Handler, ClickHandler, BlurHandler, ValueChangeHandler<Boolean> {

    CallBack callBack;
    String keyCode, newDtApplicableFrom, newDtApplicableTo;
    boolean copyFrom = false;
    ServiceInvoker<TdsCategoryMaster_SrvAsync> SrvAsync;

    public interface CallBack {

        public void onOk(String newDtApplicable, String oldDtApplicable);

        public void onDelete(String applicableFrom);

        public void onCopyTDS(String copyTDS, String copyDtApplicable, String newDtApplicable);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textEvent() {
        txtHCopyFromTds.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCopyFromTDS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCopyFromTds).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCopyFromTds.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCopyFromTds).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtDtCopyFrom.setText(DmShared.dNull(t1Row.getSharedPreparedBy()));
                } else {
                    txtDtCopyFrom.setText("");
                }
            }
        });

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public void show(UIObject showRelativeTo, boolean okNeeded, boolean deleteNeeded, String code, boolean copyFromNeeded) {
        this.keyCode = code;
        this.copyFrom = copyFromNeeded;
        flwOk.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        }
        clear();
        fillApplicableDateList();

        flwRadioBtnsContainer.clear();
        if (copyFromNeeded) {
            rbtnOldPrice.setValue(false);
            rbtnNewPrice.setValue(false);
            manageWorkingArea((RadioButton) null);
            flwRadioBtnsContainer.getElement().getStyle().setMarginBottom(1, Style.Unit.PCT);
            flwRadioBtnsContainer.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
            flwRadioBtnsContainer.setStyleName("dm-StripLabel");
            flwRadioBtnsContainer.getElement().setAttribute("style", flwRadioBtnsContainer.getElement().getAttribute("style") + "text-align:center;");

            DmLabel lblTitle = new DmLabel("COPY ITS");
            lblTitle.getElement().setAttribute("style", " text-align: center; width: 300px !important; height: 15px !important; margin-top: 2%; font-weight: bold;");
            lblTitle.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            flwRadioBtnsContainer.add(lblTitle);

        } else {
            flwRadioBtnsContainer.add(flwRadioBtns);
            rbtnOldPrice.setValue(true);
            rbtnNewPrice.setValue(false);
            manageWorkingArea(rbtnOldPrice);
        }
        this.popupPanel.showRelativeTo(showRelativeTo);
    }

    public ApplicableFrom() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(TdsCategoryMaster_Srv.class), TdsCategoryMaster.class.getName());
        textEvent();
        gridEvent();
        handlerEvents(new RadioButton[]{rbtnNewPrice, rbtnOldPrice}, new Button[]{btnClose, btnDelete, btnOk, btnSearch});
        rbtnOldPrice.setValue(Boolean.TRUE);
    }

    protected void fillApplicableDateList() {
        SrvAsync.invokeRPC().fillGridApplicableDate(keyCode, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtDtNew).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    dmGrdApplicableDate.getGrid().getStore().clear();
                    dmGrdApplicableDate.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
                getMaxDateApplicable();
            }
        });
    }

    private void getMaxDateApplicable() {
        SrvAsync.invokeRPC().getMaxApplicableDate(keyCode, new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtDtNew).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended data = result.getData();
//                    txtHDtApplicable.setTag(data.getSharedPreparedBy());
//                    txtHDtApplicable.setText(data.getSharedPreparedBy());
                    txtHCopyFromTds.setTag(data.getSharedKeyFieldCode());
                    txtHCopyFromTds.setText(data.getSharedKeyFieldName());
                    txtDtCopyFrom.setText(DmShared.dNull(data.getSharedPreparedBy()));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void gridEvent() {

        dmGrdApplicableDate.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                newDtApplicableFrom = DmShared.xNull(dmGrdApplicableDate.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
            }
        });

        dmGrdApplicableDate.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode() && !event.getNativeEvent().getCtrlKey()) {
                    int rowIndex = dmGrdApplicableDate.getGrid().getStore().indexOf(dmGrdApplicableDate.getGrid().getSelectionModel().getSelectedItem());
                    newDtApplicableFrom = DmShared.dNull(dmGrdApplicableDate.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode());
                    newDtApplicableTo = DmShared.dNull(dmGrdApplicableDate.getGrid().getStore().get(rowIndex).getSharedKeyFieldName());
                    ok();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) dmGrdApplicableDate.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<PLT>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<PLT> event) {
                newDtApplicableFrom = DmShared.xNull(dmGrdApplicableDate.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode());
                newDtApplicableTo = DmShared.xNull(dmGrdApplicableDate.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldName());
            }
        });
    }

    private void handlerEvents(RadioButton[] rbls, Button[] btns) {
        for (RadioButton rbtn : rbls) {
            rbtn.addValueChangeHandler(this);
        }
        for (Button btn : btns) {
            btn.addClickHandler(this);
        }
        popupPanel.addAttachHandler(this);
        txtDtNew.addBlurHandler(this);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {

        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void clear() {
        newDtApplicableFrom = "";
        txtDtNew.setText("");
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            popupPanel.hide();
        } else if (event.getSource() == btnDelete) {
            if (DmShared.dNull(newDtApplicableFrom).equals("")) {
                new DmAlertMessageBox("Please Select Applicable From ", (DmTextBox) null).show();
            } else {
                new DmConfirmMessageBox("Are you sure? You want to delete " + DmShared.dNull(newDtApplicableFrom) + " record.", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            SrvAsync.invokeRPC().deleteRecordApplicable(keyCode, DmShared.dNull(newDtApplicableFrom), new AsyncCallback<DmResult<String>>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                                }

                                @Override
                                public void onSuccess(DmResult<String> result) {
                                    if (result.isStatus()) {
                                        clear();
                                        fillApplicableDateList();
                                        callBack.onDelete(DmShared.dNull(newDtApplicableFrom));
                                    } else {
                                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                                    }
                                }
                            });
                        }
                    }
                }).show();

            }

        } else if (event.getSource() == btnOk) {
            ok();
        }
    }

    private void ok() {
        String newDtApplicable = "", oldDtApplicable = "";
        String copyTDS = "", copyDtApplicable = "";
        try {
            if (this.copyFrom) {
                if (DmShared.xNull(txtHCopyFromTds.getTag()).trim().equals("")
                        || DmShared.dNull(txtDtCopyFrom.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Copy TDS is mandatory.", txtHCopyFromTds).show();
                    return;
                }
                copyTDS = DmShared.xNull(txtHCopyFromTds.getTag());
                copyDtApplicable = DmShared.dNull(txtDtCopyFrom.getText());
            } else {
                if (rbtnOldPrice.getValue()) {
                    if (DmShared.dNull(this.newDtApplicableFrom).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Please Select a Date", (DmTextBox) null).show();
                        return;
                    }
                    newDtApplicable = this.newDtApplicableFrom;
                    oldDtApplicable = this.newDtApplicableFrom;
                } else if (rbtnNewPrice.getValue()) {
                    if (DmShared.xNull(txtHCopyFromTds.getTag()).trim().equals("")
                            || DmShared.dNull(txtDtCopyFrom.getText()).trim().equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Copy TDS is mandatory.", txtHCopyFromTds).show();
                        return;
                    }

                    if (DmShared.dNull(txtDtNew.getText()).trim().equalsIgnoreCase("")) {
                        new DmAlertMessageBox("New Applicable Date is mandatory.", txtDtNew).show();
                        return;
                    }
                    copyTDS = DmShared.xNull(txtHCopyFromTds.getTag());
                    copyDtApplicable = DmShared.dNull(txtDtCopyFrom.getText());
                    newDtApplicable = DmShared.dNull(txtDtNew.getText());
                }
            }
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), btnOk).show();
        }
        popupPanel.hide();

        if (this.copyFrom || rbtnNewPrice.getValue()) {
            callBack.onCopyTDS(copyTDS, copyDtApplicable, newDtApplicable);
        } else {
            callBack.onOk(newDtApplicable, oldDtApplicable);
        }

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDtNew) {
            txtDtNew.setText(DmClient.returnDate(txtDtNew.getText()));
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (event.getSource().equals(rbtnNewPrice)) {
            if (rbtnNewPrice.getValue()) {
                manageWorkingArea(rbtnNewPrice);
                txtDtNew.setFocus(true);
            }
        }
        if (event.getSource().equals(rbtnOldPrice)) {
            if (rbtnOldPrice.getValue()) {
                manageWorkingArea(rbtnOldPrice);
            }
        }
    }

    private void manageWorkingArea(RadioButton rBtn) {
        flw.clear();
        if (rBtn != null) {
            if (rBtn.equals(rbtnOldPrice) && rbtnOldPrice.getValue()) {
                flw.add(flwOldPriceList);
                manageBtnDelete(true);
            } else if (rBtn.equals(rbtnNewPrice) && rbtnNewPrice.getValue()) {
                flwNewPriceList.clear();
                flwNewPriceList.add(newPriceList());
                flw.add(flwNewPriceList);
                manageBtnDelete(false);
            }
        } else {
            if (this.copyFrom) {
                flwCopyFromITS.clear();
                flwCopyFromITS.add(copyFromITS());
                flw.add(flwCopyFromITS);
                manageBtnDelete(false);
            }
        }
    }

    private void manageBtnDelete(boolean flag) {
        flwDelete.clear();
        if (flag) {
            flwDelete.add(btnDelete);
        }
    }

}
