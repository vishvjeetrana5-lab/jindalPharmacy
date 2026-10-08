package aarogya.pharmacy.walkInSearch.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;
import aarogya.pharmacy.walkInSearch.shared.SearchValue_Mdl;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import aarogya.pharmacy.walkInSearch.shared.WalkInSearch_Srv;
import aarogya.pharmacy.walkInSearch.shared.WalkInSearch_SrvAsync;

public class WalkInSearch extends WalkInSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, BlurHandler, KeyDownHandler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    public interface CallBack {

        public void ok(String lmCode, String lmManualCode);

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private final ServiceInvoker<WalkInSearch_SrvAsync> srvAsync;
    boolean gridFocusFlag = true;
    private int searchColumnIndex;
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private String searchDbFieldName = "", searchPrvValue = "";

    public WalkInSearch() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(WalkInSearch_Srv.class), WalkInSearch.class.getName());
        grdWalkInSearch.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        manageEvents();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdWalkInSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdWalkInSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (grdWalkInSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    select(grdWalkInSearch.getDmGrid().getGrid().getStore().get(0));
                }
            }
        }
    }

    public void show() {
        clear();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSearchValue.setText("");
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
        reQuery();
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.close();
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdWalkInSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdWalkInSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdWalkInSearch.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdWalkInSearch.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdWalkInSearch.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : grdWalkInSearch.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdWalkInSearch.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void manageEvents() {
        gridEvents();
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        txtSearchValue.addKeyUpHandler(this);
    }

    private void gridEvents() {

        setHeaderValue(0);

        grdWalkInSearch.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        grdWalkInSearch.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                select(grdWalkInSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
            }
        });

        grdWalkInSearch.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (!event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    select(grdWalkInSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                }
            }
        }, KeyDownEvent.getType());

        grdWalkInSearch.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(false).getSearchRecord(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<LedgerMaster_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<LedgerMaster_Mdl>> result) {
                        if (result.isStatus()) {
                            grdWalkInSearch.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchValue).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return new SearchValue_Mdl();
            }
        });

    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            hide();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        gridFocusFlag = true;
    }

    private void reQuery() {
        grdWalkInSearch.getHelper().setRowsStartFrom(0);
        grdWalkInSearch.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdWalkInSearch.reQuery();
    }

    private void select(LedgerMaster_Mdl lmMdl) {
        if (lmMdl != null) {
            popupPanel.hide();
            callBack.ok(lmMdl.getSharedKeyFieldCode(), lmMdl.getManualCode());
        } else {
            txtSearchValue.setFocus(true);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            if (gridFocusFlag) {
                ((CellSelectionModel) grdWalkInSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                gridFocusFlag = false;
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    private void clear() {
        txtSearchValue.setText("");
        txtSearchValue.setTag("");
        txtSearchValue.setTag2nd("");
        grdWalkInSearch.getDmGrid().getGrid().getStore().clear();
    }
}
