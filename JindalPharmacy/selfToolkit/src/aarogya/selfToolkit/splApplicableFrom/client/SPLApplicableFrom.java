package aarogya.selfToolkit.splApplicableFrom.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.splApplicableFrom.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
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
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class SPLApplicableFrom extends SPLApplicableFrom_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, ValueChangeHandler<Boolean> {

    public interface CallBack {

        public void onOk(String newDtApplicable, String oldDtApplicable, String siteCode);

        public void onDelete(String applicableFrom);
    }

    CallBack callBack;
    ServiceInvoker<SPLApplicableFrom_SrvAsync> SrvAsync;
    private SearchValue_Mdl searchValue;

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
            }
        } else if (event.getSource().equals(rbtnOldPrice)) {
            if (event.getValue()) {
                manageWorkingArea(rbtnOldPrice);
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            rootEvent.stopPropagation();
            hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide();
        } else if (event.getSource() == btnOk) {
            ok();
        } else if (event.getSource() == btnDelete) {
            if (DmShared.dNull(searchValue.getDtApplicable()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Invalid Applicable From.", (DmTextBox) null).show();
            } else {
                new DmConfirmMessageBox("Are you sure? You want to delete " + DmShared.dNull(searchValue.getDtApplicable()) + " record.", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            SrvAsync.invokeRPC().deleteApplicableDate(DmShared.dNull(searchValue.getDtApplicable()), searchValue.getPriceListEntryFrom(), searchValue.getKeyCode(), searchValue.getOrganizationTag(), DmShared.xNull(txtHSite.getTag2nd()), new AsyncCallback<DmResult<String>>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                                }

                                @Override
                                public void onSuccess(DmResult<String> result) {
                                    if (result.isStatus()) {
                                        fillApplicableDateList();
                                        callBack.onDelete(DmShared.dNull(searchValue.getDtApplicable()));
                                    } else {
                                        new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), (DmTextBox) null).show();
                                    }
                                }
                            });
                        }
                    }
                }).show();
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            popupPanel.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public SPLApplicableFrom() {
        super();
        SrvAsync = new ServiceInvoker<SPLApplicableFrom_SrvAsync>(GWT.create(SPLApplicableFrom_Srv.class), SPLApplicableFrom.class.getName());
        textHelpEvent();
        gridEvents();
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

    private void gridEvents() {
        grdDtApplicable.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                searchValue.setDtApplicable(DmShared.dNull(grdDtApplicable.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
            }
        });

        grdDtApplicable.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode() && !event.getNativeEvent().getCtrlKey()) {
                    int rowIndex = grdDtApplicable.getGrid().getStore().indexOf(grdDtApplicable.getGrid().getSelectionModel().getSelectedItem());
                    searchValue.setDtApplicable(DmShared.dNull(grdDtApplicable.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()));
                    ok();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) grdDtApplicable.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<PLT>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<PLT> event) {
                searchValue.setDtApplicable(DmShared.xNull(grdDtApplicable.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode()));
            }
        });
        grdDtApplicable.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                searchValue.setDtApplicable(DmShared.dNull(grdDtApplicable.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvent() {
        txtHDtApplicable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDtApplicable(helper, blnToEndOfFile, searchValue, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, searchValue, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    txtHDtApplicable.setText("");
                    txtHDtApplicable.setTag("");
                    if (t1Row != null) {
                        searchValue.setSiteCode(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                    } else {
                        searchValue.setSiteCode(DmShared.xNull(txtHSite.getTag2nd()));
                    }

                }
            }
        });
    }

    protected void fillApplicableDateList() {
        SrvAsync.invokeRPC().getApplicableDateList(searchValue.getPriceListEntryFrom(), searchValue.getKeyCode(), searchValue.getOrganizationTag(), searchValue.getSiteCode(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    grdDtApplicable.getGrid().getStore().clear();
                    for (int i = 0; i < result.getData().size(); i++) {
                        grdDtApplicable.getGrid().getStore().add(i, result.getData().get(i));
                    }
                    getMaxDateApplicable();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    public void show(UIObject showRelativeTo, boolean okNeeded, boolean deleteNeeded, PriceListEntryFrom priceListEntryFrom,
            String keyCode, String organizationCode, String site_Code) {
        this.searchValue = new SearchValue_Mdl();
        searchValue.setKeyCode(DmShared.iNull(keyCode).toString());
        searchValue.setOrganizationTag(DmShared.iNull(organizationCode).toString());
        searchValue.setPriceListEntryFrom(priceListEntryFrom);
        searchValue.setSiteCode(DmShared.iNull(site_Code).toString());
        searchValue.setDtApplicable("");
        txtDtNew.setTag("");
        txtDtNew.setText("");
        txtDtNew.setTag2nd("");
        txtHSite.setText("");
        txtHSite.setTag("");
        txtHSite.setTag2nd(DmShared.iNull(site_Code).toString());
        flwOk.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        }
        fillApplicableDateList();
        popupPanel.showRelativeTo(showRelativeTo);
        if (!rbtnNewPrice.getValue() && !rbtnOldPrice.getValue()) {
            rbtnOldPrice.setValue(true);
        } else if (rbtnNewPrice.getValue() && !rbtnOldPrice.getValue()) {
            rbtnOldPrice.setValue(true);
        }
        manageWorkingArea(rbtnOldPrice);
        this.popupPanel.showRelativeTo(showRelativeTo);
    }

    private void ok() {
        String newDtApplicable = "", oldDtApplicable = "", siteCode = "";
        try {
            if (rbtnOldPrice.getValue()) {
                if (DmShared.dNull(searchValue.getDtApplicable()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Please Select a Date", (DmTextBox) null).show();
                    return;
                }
                newDtApplicable = searchValue.getDtApplicable();
                oldDtApplicable = searchValue.getDtApplicable();
                siteCode = DmShared.xNull(txtHSite.getTag2nd());
            } else if (rbtnNewPrice.getValue()) {

//                if (DmShared.xNull(txtHSite.getTag()).equalsIgnoreCase("") && !DmShared.dNull(txtHDtApplicable.getText()).equalsIgnoreCase("")) {
//                    new DmAlertMessageBox("Site is mandatory.", txtHSite).show();
//                    return;
//                }
                if (DmShared.dNull(txtDtNew.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Please give new Date.", txtDtNew).show();
                    return;
                }
                if (DmShared.dNull(txtHDtApplicable.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtHSite.getTag()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Copy Applicable Date is mandatory.", txtHDtApplicable).show();
                    return;
                }
                newDtApplicable = DmShared.dNull(txtDtNew.getText());
                oldDtApplicable = DmShared.dNull(txtHDtApplicable.getText());
                siteCode = DmShared.xNull(txtHSite.getTag());
            }
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), btnOk).show();
        }
        popupPanel.hide();
        callBack.onOk(newDtApplicable, oldDtApplicable, siteCode);
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

    private void getMaxDateApplicable() {
        SrvAsync.invokeRPC().getMaxApplicableDate(searchValue, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
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
}
