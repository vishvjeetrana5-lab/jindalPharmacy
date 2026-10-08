
package dataman.accounts.advanceEntry.client;

import dataman.accounts.advanceEntry.shared.AdvanceEntry_Srv;
import dataman.accounts.advanceEntry.shared.AdvanceEntry_SrvAsync;
import dataman.accounts.advanceEntry.shared.GrdAdvReqSearchDetail_Mdl;
import dataman.accounts.advanceEntry.shared.SearchValue_Mdl;

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

public class AdvanceReqEntrySearch extends AdvanceReqEntrySearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler, KeyPressHandler {

    public interface CallBack {

        public void onOk(String advReqNo, String advReqDocid);

        public void onClose();

    }
    private CallBack callBack;
    private final ServiceInvoker<AdvanceEntry_SrvAsync> srvAsync;
    private SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
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
        gridAdvReqSearchDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        gridAdvReqSearchDetail.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });
        gridAdvReqSearchDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
            }
        }, KeyDownEvent.getType());

        gridAdvReqSearchDetail.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(false).getAdvReqSearch(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdAdvReqSearchDetail_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtSearchValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdAdvReqSearchDetail_Mdl>> result) {
                        if (result.isStatus()) {
                            gridAdvReqSearchDetail.setData(result.getData(), result.getPaging());
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
        if ((gridAdvReqSearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridAdvReqSearchDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridAdvReqSearchDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridAdvReqSearchDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridAdvReqSearchDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridAdvReqSearchDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridAdvReqSearchDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridAdvReqSearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridAdvReqSearchDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridAdvReqSearchDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    if (gridAdvReqSearchDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) gridAdvReqSearchDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
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
        gridAdvReqSearchDetail.getHelper().setRowsStartFrom(0);
        gridAdvReqSearchDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridAdvReqSearchDetail.reQuery();
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
        GrdAdvReqSearchDetail_Mdl grdAdReqDetails_Mdl = gridAdvReqSearchDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String advReqDocId = "", advReqNo = "";
        int index = -1;

        if (grdAdReqDetails_Mdl != null) {
            index = gridAdvReqSearchDetail.getDmGrid().getGrid().getStore().indexOf(grdAdReqDetails_Mdl);
            if (index >= 0) {
                advReqDocId = DmShared.xNull(gridAdvReqSearchDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode());

                advReqNo = DmShared.xNull(gridAdvReqSearchDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldName());
            }
        }
        popupPanel.hide();
        callBack.onOk(advReqNo, advReqDocId);
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

    public AdvanceReqEntrySearch() {
        super();
        srvAsync = new ServiceInvoker<AdvanceEntry_SrvAsync>(GWT.create(AdvanceEntry_Srv.class), AdvanceEntry.class.getName());
        gridAdvReqSearchDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
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
        return value_Mdl;
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {

    }
}

