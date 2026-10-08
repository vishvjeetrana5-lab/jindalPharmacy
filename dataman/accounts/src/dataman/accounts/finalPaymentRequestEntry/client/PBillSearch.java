package dataman.accounts.finalPaymentRequestEntry.client;

import dataman.accounts.finalPaymentRequestEntry.shared.FinalPaymentRequestEntry_Srv;
import dataman.accounts.finalPaymentRequestEntry.shared.FinalPaymentRequestEntry_SrvAsync;
import dataman.accounts.finalPaymentRequestEntry.shared.GrdPBillSearchDetail_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.SearchValue_Mdl;
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

public class PBillSearch extends PBillSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler, KeyPressHandler {

    public interface CallBack {

        public void onOk(String poNo, String poDocid, String pbSupplier);

        public void onClose();

    }
    private CallBack callBack;
    private final ServiceInvoker<FinalPaymentRequestEntry_SrvAsync> srvAsync;
    private SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private String supplierTag = "", moduleType="";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String supplierTag, String moduleType) {
        this.supplierTag = supplierTag;
        this.moduleType = moduleType;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
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
        gridPbSearchDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        gridPbSearchDetail.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });
        gridPbSearchDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
            }
        }, KeyDownEvent.getType());

        gridPbSearchDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(false).getPBSearch(supplierTag, helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdPBillSearchDetail_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdPBillSearchDetail_Mdl>> result) {
                        if (result.isStatus()) {
                            gridPbSearchDetail.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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
        if ((gridPbSearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridPbSearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridPbSearchDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridPbSearchDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridPbSearchDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridPbSearchDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridPbSearchDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridPbSearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridPbSearchDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridPbSearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    if (gridPbSearchDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) gridPbSearchDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
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
        gridPbSearchDetail.getHelper().setRowsStartFrom(0);
        gridPbSearchDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridPbSearchDetail.reQuery();
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
        GrdPBillSearchDetail_Mdl grdPoDetails_Mdl = gridPbSearchDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String pbDocId = "", pbNo = "", pbSupplier = "";
        int index = -1;

        if (grdPoDetails_Mdl != null) {
            index = gridPbSearchDetail.getDmGrid().getGrid().getStore().indexOf(grdPoDetails_Mdl);
            if (index >= 0) {
                pbDocId = DmShared.xNull(gridPbSearchDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode());

                pbNo = DmShared.xNull(gridPbSearchDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldName());
                pbSupplier = DmShared.xNull(gridPbSearchDetail.getDmGrid().getGrid().getStore().get(index).getSupplierTag());
            }
        }
        popupPanel.hide();
        callBack.onOk(pbNo, pbDocId, pbSupplier);
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

    public PBillSearch() {
        super();
        srvAsync = new ServiceInvoker<FinalPaymentRequestEntry_SrvAsync>(GWT.create(FinalPaymentRequestEntry_Srv.class), FinalPaymentRequestEntry.class.getName());
        gridPbSearchDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
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
        value_Mdl.setModuleType(moduleType);
        return value_Mdl;
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {

    }
}
