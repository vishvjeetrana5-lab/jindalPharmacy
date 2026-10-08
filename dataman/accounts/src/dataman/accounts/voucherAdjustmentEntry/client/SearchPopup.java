package dataman.accounts.voucherAdjustmentEntry.client;

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
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.voucherAdjustmentEntry.shared.Filter_Mdl;
import dataman.accounts.voucherAdjustmentEntry.shared.GrdLedger_Mdl;
import dataman.accounts.voucherAdjustmentEntry.shared.VoucherAdjustmentEntry_SrvAsync;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class SearchPopup extends SearchPopup_Dsg implements IsWidget, ClickHandler, KeyUpHandler, AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void select(String voucherDocId);

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    protected ServiceInvoker<VoucherAdjustmentEntry_SrvAsync> srvAsync;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private String searchDbFieldName;
    private int searchColumnIndex;
    private SearchFilterPopup filterPopup = new SearchFilterPopup();
    protected Filter_Mdl filter = new Filter_Mdl();

    public SearchPopup() {
        super();
        grdSearch.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        manageEvents();
        manageCallback();
    }

    public void show(VoucherAdjustmentEntry voucherAdjustmentEntry) {
        srvAsync = voucherAdjustmentEntry.srvAsync;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSearchValue.setFocus(true);
        reQuery();
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCallback) {
        popupPanel.hide();
        if (withCallback) {
            callBack.close();
        }
    }

    private void manageEvents() {
        gridEvents();
        txtSearchValue.addKeyUpHandler(this);
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnFilter.addClickHandler(this);
    }

    private void manageCallback() {
        filterPopup.addCallBack(new SearchFilterPopup.CallBack() {
            @Override
            public void select(Filter_Mdl filter) {
                SearchPopup.this.filter = filter;
                reQuery();
                txtSearchValue.setFocus(true);
            }

            @Override
            public void close() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);

        grdSearch.addCallBack(new DmGridPaging.CallBack<Filter_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, Filter_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getLedger(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdLedger_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdLedger_Mdl>> result) {
                        if (result.isStatus()) {
                            grdSearch.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public Filter_Mdl getParam() {
                return filter;
            }
        });

        grdSearch.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        grdSearch.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                select();
            }
        });

        grdSearch.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    select();
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                    if (grdSearch.getDmGrid().getGrid().getStore().indexOf(grdSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem()) == 0) {
                        txtSearchValue.setFocus(true);
                    }
                }
            }
        }, KeyDownEvent.getType());

    }

    private void select() {
        GrdLedger_Mdl gvMdl = grdSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        if (gvMdl != null) {
            callBack.select(gvMdl.getSharedKeyFieldCode());
            hide(false);
        }
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdSearch.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdSearch.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdSearch.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : grdSearch.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdSearch.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        grdSearch.getHelper().setRowsStartFrom(0);
        grdSearch.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdSearch.reQuery();
    }

    private void showFilterPopup() {
        filterPopup.show(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
        } else if (event.getSource() == btnFilter) {
            showFilterPopup();
        }
    }

    private void focusGrid() {
        ((CellSelectionModel) grdSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource() == txtSearchValue) {
            if (!event.isAltKeyDown()) {
                switch (event.getNativeKeyCode()) {
                    case KeyCodes.KEY_DOWN:
                        focusGrid();
                        break;
                    case KeyCodes.KEY_UP:
                        break;
                    default:
                        reQuery();
                        break;
                }
            }
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F) {
            showFilterPopup();
        }
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

}
