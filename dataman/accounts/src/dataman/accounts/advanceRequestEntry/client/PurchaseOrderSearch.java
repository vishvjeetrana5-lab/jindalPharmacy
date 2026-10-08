package dataman.accounts.advanceRequestEntry.client;

import dataman.accounts.advanceRequestEntry.shared.AdvanceRequestEntry_Srv;
import dataman.accounts.advanceRequestEntry.shared.AdvanceRequestEntry_SrvAsync;
import dataman.accounts.advanceRequestEntry.shared.GrdPurchaseOrderSearchDetail_Mdl;
import dataman.accounts.advanceRequestEntry.shared.SearchValue_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class PurchaseOrderSearch extends PurchaseOrderSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler, KeyPressHandler {

    public interface CallBack {

        public void onOk(String poNo, String poDocid);

        public void onClose();

    }
    private CallBack callBack;
    private final ServiceInvoker<AdvanceRequestEntry_SrvAsync> srvAsync;
    private SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private String voucherType = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String voucherType) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        this.voucherType = voucherType;
        reQuery();
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            callBack.onClose();
        }
    }

    private void gridEvents() {
        setHeaderValue(0);
        gridPoSearchDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        gridPoSearchDetail.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });
        gridPoSearchDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
            }
        }, KeyDownEvent.getType());

        gridPoSearchDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(false).getPoSearch(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdPurchaseOrderSearchDetail_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdPurchaseOrderSearchDetail_Mdl>> result) {
                        if (result.isStatus()) {
                            gridPoSearchDetail.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });
    }

    private void setHeaderValue(int columnIndex) {
        if ((gridPoSearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridPoSearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridPoSearchDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridPoSearchDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridPoSearchDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridPoSearchDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridPoSearchDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridPoSearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridPoSearchDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridPoSearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    if (gridPoSearchDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) gridPoSearchDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                        onSelection();
                    }
                }
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    private void reQuery() {
        gridPoSearchDetail.getHelper().setRowsStartFrom(0);
        gridPoSearchDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridPoSearchDetail.reQuery();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnRefresh) {
            txtSearchValue.setText("");
            reQuery();
        }
    }

    private void onSelection() {
        GrdPurchaseOrderSearchDetail_Mdl grdPoDetails_Mdl = gridPoSearchDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String poDocId = "", poNo = "";
        int index = -1;

        if (grdPoDetails_Mdl != null) {
            index = gridPoSearchDetail.getDmGrid().getGrid().getStore().indexOf(grdPoDetails_Mdl);
            if (index >= 0) {
                poDocId = DmShared.xNull(gridPoSearchDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode());

                poNo = DmShared.xNull(gridPoSearchDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldName());
            }
        }
        popupPanel.hide();
        callBack.onOk(poNo, poDocId);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public PurchaseOrderSearch() {
        super();
        srvAsync = new ServiceInvoker<AdvanceRequestEntry_SrvAsync>(GWT.create(AdvanceRequestEntry_Srv.class), AdvanceRequestEntry.class.getName());
        gridPoSearchDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        gridEvents();
        manageEvent();
    }

    private void manageEvent() {
        btnCancel.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        popupPanel.addAttachHandler(this);

        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl = searchMdl;
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(txtSearchValue.getText());
        value_Mdl.setVoucherType(voucherType);
        return value_Mdl;
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {

    }
}
