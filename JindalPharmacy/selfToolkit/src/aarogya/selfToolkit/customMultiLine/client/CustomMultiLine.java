package aarogya.selfToolkit.customMultiLine.client;

import aarogya.selfToolkit.customMultiLine.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomMultiLineNature;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class CustomMultiLine extends CustomMultiLine_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void onOk(String remark, DmTextArea textArea);

        public void onClose(DmTextArea textArea);
    }

    private CallBack callBack;
    private final ServiceInvoker<CustomMultiLine_SrvAsync> srvAsync;
    private CustomMultiLineNature nature;
    private String consultantCode;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private DmTextArea textArea;
    private DmLabel label;
    private final SubjectPopup subjectPopup = new SubjectPopup();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void addCallBack(final CustomMultiLineNature nature, final String consultantCode, int maxLength, CallBack callBack) {
        this.callBack = callBack;
        this.textArea.setMaxLength(maxLength);
        this.txtRemark.setMaxLength(maxLength);
        this.label.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                CustomMultiLine.this.show(nature, consultantCode, true);
            }
        });

    }

    private void textHelpEvents() {
        subjectPopup.addCallBack(new SubjectPopup.CallBack() {
            @Override
            public void refresh() {
                reQuery();
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        grdFavorite.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdFavorite.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getFavoriteDetail(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdFavorite_Mdl>> result) {
                        if (result.isStatus()) {
                            grdFavorite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchFavriote).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize(true);
            }
        });
        grdFavorite.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == colRemoveFav) {
                        removeFromFavorite(grdFavorite.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    } else {
                        setFavoriteData(grdFavorite.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdFavorite.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    CellSelectionModel cellModel = (CellSelectionModel) grdFavorite.getDmGrid().getGrid().getSelectionModel();
                    int row = grdFavorite.getDmGrid().getGrid().getStore().indexOf(grdFavorite.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    if (cellModel.getSelectCell().getCell() == colRemoveFav) {
                        removeFromFavorite(grdFavorite.getDmGrid().getGrid().getStore().get(row));
                    } else {
                        setFavoriteData(grdFavorite.getDmGrid().getGrid().getStore().get(row));
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    public void show(CustomMultiLineNature nature, String consultantCode, boolean okNeeded) {
        clear();
        this.nature = nature;

        this.consultantCode = consultantCode;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        //<editor-fold defaultstate="collapsed" desc="Set Custom Item Name">
        if (DmShared.xNull(label.getText()).trim().equalsIgnoreCase("")) {
            label.setText(nature.getValue());
        }
        toolContainer.setTitle(label.getText());
        //</editor-fold>

        flwOk.clear();
        flwCancel.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        } else {
            flwCancel.add(btnCancel);
        }
        toolContainer.setTitle(label.getText());
        txtRemark.setText(textArea.getText());

        reQuery();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRemark.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean callBackNeeded) {
        if (callBackNeeded) {
            if (btnOk.isAttached() && btnOk.isEnabled() && btnOk.isVisible()) {
                this.callBack.onOk(txtRemark.getText(), textArea);
            } else if (btnCancel.isAttached() && btnCancel.isEnabled() && btnCancel.isVisible()) {
                this.callBack.onClose(textArea);
            }
        }
        this.popupPanel.hide();
    }

    private void setFavoriteData(GrdFavorite_Mdl node) {
        if (node != null) {
            txtRemark.setText(DmShared.xNull(node.getRemark()));
            txtRemark.setFocus(true);
        }
    }

    private void removeFromFavorite(final GrdFavorite_Mdl node) {
        new DmConfirmMessageBox("Are you sure? You want to remove from Favorites.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    removeFavorites(DmShared.xNull(node.getSharedKeyFieldCode()));
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel) || event.getSource().equals(btnOk)) {
            hide(true);
        } else if (event.getSource().equals(btnFavriot)) {
            if (DmShared.xNull(txtRemark.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Remark is mandatory.", txtRemark).show();
                return;
            }
            CustomMultiLine_Mdl mrTemp = new CustomMultiLine_Mdl();
            mrTemp.setConsultantCode(consultantCode);
            mrTemp.setNature(nature);
            mrTemp.setRemark(txtRemark.getText());
            subjectPopup.show(mrTemp, btnFavriot);
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchFavriote)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdFavorite.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdFavorite.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchFavriote)) {
            if (!searchPrvValue.equalsIgnoreCase(txtSearchFavriote.getText())) {
                reQuery();
            }
            searchPrvValue = txtSearchFavriote.getText();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide((btnCancel.isAttached() && btnCancel.isEnabled() && btnCancel.isVisible()));
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

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public CustomMultiLine(DmTextArea textArea, DmLabel label) {
        super();
        srvAsync = new ServiceInvoker<CustomMultiLine_SrvAsync>(GWT.create(CustomMultiLine_Srv.class), CustomMultiLine.class.getName());
        this.textArea = textArea;
        this.label = label;

        toolContainer.setTitle(widgetName);
        manageEvents();
        textHelpEvents();
        grdFavorite.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
    }

    private void reQuery() {
        grdFavorite.getHelper().setRowsStartFrom(0);
        grdFavorite.getHelper().getFilter().get(0).setValue1(txtSearchFavriote.getText());
        grdFavorite.reQuery();
    }

    private SearchValue_Mdl valueIntialize(boolean isFavorite) {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();

        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldValue(DmShared.xNull(txtSearchFavriote.getText()));
        value_Mdl.setConsultantCode(DmShared.xNull(consultantCode));
        value_Mdl.setNature(nature);

        return value_Mdl;
    }

    private void removeFavorites(String detailCode) {
        if (DmShared.iNull(consultantCode).longValue() == 0) {
            DmShared.isValidAmount(DmShared.iNull(consultantCode).longValue(), "Consultant", 100);
        } else {
            srvAsync.invokeRPC().removeFavorite(nature.getId(), detailCode, consultantCode, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        reQuery();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtSearchFavriote).show();
                    }
                }
            });
        }
    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
        btnFavriot.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchFavriote.addKeyUpHandler(this);
        txtSearchFavriote.addKeyDownHandler(this);
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdFavorite.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdFavorite.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdFavorite.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdFavorite.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdFavorite.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchFavriote.setText("");
        txtSearchFavriote.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdFavorite.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdFavorite.getDmGrid().getGrid().getView().refresh(true);
        txtSearchFavriote.setFocus(true);
    }

    private void clear() {
        txtRemark.setTag("");
        txtRemark.setText("");
    }
}
