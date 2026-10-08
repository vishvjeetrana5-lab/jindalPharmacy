package dataman.utility.overheadConfiguration.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.overheadConfiguration.shared.OverheadConfiguration_Srv;
import dataman.utility.overheadConfiguration.shared.OverheadConfiguration_SrvAsync;

import java.util.List;

public class ApplicableFrom extends ApplicableFrom_Dsg implements IsWidget, DmRootEvents, AttachEvent.Handler, ClickHandler, BlurHandler, ValueChangeHandler<Boolean> {

    public interface CallBack {

        public void onOk(String newDtApplicable, String oldDtApplicable);

        public void onDelete(String applicableFrom);

    }

    CallBack callBack;
    ServiceInvoker<OverheadConfiguration_SrvAsync> SrvAsync;
    String keyCode, newDtApplicable;

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDtNew) {
            txtDtNew.setText(DmClient.returnDate(txtDtNew.getText()));
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (event.getSource().equals(rbtnNewPrice)) {
            if (event.getValue()) {
                manageWorkingArea(rbtnNewPrice);
                txtDtNew.setFocus(true);
            }
        } else if (event.getSource().equals(rbtnOldPrice)) {
            if (event.getValue()) {
                manageWorkingArea(rbtnOldPrice);
            }
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textEvent() {
        txtHDtApplicable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHApplicableFrom(DmShared.xNull(keyCode), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDtApplicable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDtApplicable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDtApplicable).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public void show(UIObject showRelativeTo, boolean okNeeded, boolean deleteNeeded, String vType) {
        this.keyCode = vType;
        flwOk.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        }
        clear();
        fillApplicableDateList();

        rbtnOldPrice.setValue(true);
        rbtnNewPrice.setValue(false);
        manageWorkingArea(rbtnOldPrice);
        this.popupPanel.showRelativeTo(showRelativeTo);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public ApplicableFrom() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(OverheadConfiguration_Srv.class), OverheadConfiguration.class.getName());
        textEvent();
        gridEvent();
        handlerEvents(new RadioButton[]{rbtnNewPrice, rbtnOldPrice}, new Button[]{btnClose, btnDelete, btnOk, btnSearch});
        rbtnOldPrice.setValue(Boolean.TRUE);
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
                    txtHDtApplicable.setTag(data.getSharedKeyFieldCode());
                    txtHDtApplicable.setText(data.getSharedKeyFieldName());
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
                newDtApplicable = DmShared.xNull(dmGrdApplicableDate.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
            }
        });

        dmGrdApplicableDate.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode() && !event.getNativeEvent().getCtrlKey()) {
                    int rowIndex = dmGrdApplicableDate.getGrid().getStore().indexOf(dmGrdApplicableDate.getGrid().getSelectionModel().getSelectedItem());
                    newDtApplicable = DmShared.dNull(dmGrdApplicableDate.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode());
                    ok();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) dmGrdApplicableDate.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<PLT>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<PLT> event) {
                newDtApplicable = DmShared.xNull(dmGrdApplicableDate.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode());
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            popupPanel.hide();
        } else if (event.getSource() == btnDelete) {
            if (DmShared.dNull(newDtApplicable).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please Select Applicable From.", (DmTextBox) null).show();
            } else {
                new DmConfirmMessageBox("Are you sure? You want to delete " + DmShared.dNull(newDtApplicable) + " record.", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            SrvAsync.invokeRPC().deleteRecordApplicable(keyCode, DmShared.dNull(newDtApplicable), new AsyncCallback<DmResult<String>>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                                }

                                @Override
                                public void onSuccess(DmResult<String> result) {
                                    if (result.isStatus()) {
                                        clear();
                                        fillApplicableDateList();
                                        callBack.onDelete(DmShared.dNull(newDtApplicable));
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

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void ok() {
        String newDtApplicable = "", oldDtApplicable = "";
        try {
            if (rbtnOldPrice.getValue()) {
                if (DmShared.dNull(this.newDtApplicable).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Please Select a Date", (DmTextBox) null).show();
                    return;
                }
                newDtApplicable = this.newDtApplicable;
                oldDtApplicable = this.newDtApplicable;
            } else if (rbtnNewPrice.getValue()) {
                if (DmShared.dNull(txtDtNew.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("New Applicable Date is mandatory.", txtDtNew).show();
                    return;
                }
                newDtApplicable = DmShared.dNull(txtDtNew.getText());
                oldDtApplicable = DmShared.dNull(txtHDtApplicable.getText());
            }
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), btnOk).show();
        }
        popupPanel.hide();
        callBack.onOk(newDtApplicable, oldDtApplicable);
    }

    private void clear() {
        newDtApplicable = "";
        txtDtNew.setText("");
    }

    private void manageWorkingArea(RadioButton rBtn) {
        flw.clear();
        if (rBtn.equals(rbtnOldPrice) && rbtnOldPrice.getValue()) {
            flw.add(flwOldPriceList);
            manageBtnDelete(true);
        }
        if (rBtn.equals(rbtnNewPrice) && rbtnNewPrice.getValue()) {
            flw.add(flwNewPriceList);
            manageBtnDelete(false);
        }
    }

    private void manageBtnDelete(boolean flag) {
        flwDelete.clear();
        if (flag) {
            flwDelete.add(btnDelete);
        }
    }
}
