package aarogya.selfToolkit.opdSearch.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.opdSearch.shared.*;
import aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import java.util.List;

public class OPDSearch extends OPDSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void ok(String recIdReg, String docIdReg);

        public void close();

    }

    private CallBack callBack;
    private final ServiceInvoker<OPDSearch_SrvAsync> srvAsync;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private OPDFilterType[] filterType = null;
    private DefaultValue_Mdl defValue_Mdl;

    public void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    public void show(String widgetName, OPDFilterType... filterTypes) {
        lblWidgetName.setText(widgetName.trim() + " Search");
        toolContainer.setTitle(lblWidgetName.getText());
        this.filterType = filterTypes;
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
            callBack.close();
        }
    }

    private void findEvents() {
        setHeaderValue(0);
        registrationSearch.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        registrationSearch.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });
        registrationSearch.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
            }
        }, KeyDownEvent.getType());
        registrationSearch.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(false).getFind(DmShared.iNull(defValue_Mdl.getCancelDaysOPD()).intValue(), DmShared.iNull(defValue_Mdl.getServiceDaysOPD()).intValue(), helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdRegistration_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdRegistration_Mdl>> result) {
                        if (result.isStatus()) {
                            registrationSearch.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchValue).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return setParam();
            }
        });
    }

    private void setHeaderValue(int columnIndex) {
        if ((registrationSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = registrationSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) registrationSearch.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        registrationSearch.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        registrationSearch.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : registrationSearch.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        registrationSearch.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
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
            hide(true);
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (registrationSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) registrationSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (registrationSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    if (registrationSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) registrationSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
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
        registrationSearch.getHelper().setRowsStartFrom(0);
        registrationSearch.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        registrationSearch.reQuery();
    }

    private void onSelection() {
        GrdRegistration_Mdl grdPatientDetails = registrationSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String RecId = "", DocId = "";
        int index = -1;

        if (grdPatientDetails != null) {
            index = registrationSearch.getDmGrid().getGrid().getStore().indexOf(grdPatientDetails);
            if (index >= 0) {
                RecId = registrationSearch.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldName();
                DocId = registrationSearch.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode();
            }
        }
        callBack.ok(RecId, DocId);
        hide(false);
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

    public OPDSearch() {
        super();
        srvAsync = new ServiceInvoker<OPDSearch_SrvAsync>(GWT.create(OPDSearch_Srv.class), OPDSearch.class.getName());
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
        registrationSearch.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        iniDefaultValues();
    }

    private SearchValue_Mdl setParam() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(txtSearchValue.getText());
        value_Mdl.setFilterType(filterType);
        return value_Mdl;
    }

    private void iniDefaultValues() {
        srvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtSearchValue).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defValue_Mdl = result.getData();
                    findEvents();

                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtSearchValue.setFocus(true);
                        }
                    });
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtSearchValue).show();
                }
            }
        });
    }
}
