package aarogya.selfToolkit.wholeSaleBillSearchPopup.client;

import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.shared.PatientSearchRoomWise_Srv;
import aarogya.selfToolkit.patientSearchRoomWise.shared.PatientSearchRoomWise_SrvAsync;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.server.WholeSaleBillSearchPopup_SrvImpl;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.GrdWholeSaleBill_Mdl;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.SearchValue_Mdl;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.WholeSaleBillSearchPopup_Srv;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.WholeSaleBillSearchPopup_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmOperand;
import dataman.dmBase.self.shared.DmEnum.DmOperator;
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

public class WholeSaleBillSearchPopup extends WholeSaleBillSearchPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void ok(String regDocid, String regRecid, String roomCode);

        public void close();

    }
    private CallBack callBack;
    private ServiceInvoker<WholeSaleBillSearchPopup_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;

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
            callBack.close();
        }
    }
    
    private void findEvents() {

        setHeaderValue(0);

        grdRoomSearch.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        grdRoomSearch.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });

        grdRoomSearch.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
            }
        }, KeyDownEvent.getType());

        grdRoomSearch.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                SrvAsync.invokeRPC(false).getFind(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdWholeSaleBill_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdWholeSaleBill_Mdl>> result) {
                        if (result.isStatus()) {
                            grdRoomSearch.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), btnCancel).show();
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
        if ((grdRoomSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdRoomSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdRoomSearch.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdRoomSearch.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdRoomSearch.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdRoomSearch.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdRoomSearch.getDmGrid().getGrid().getView().refresh(true);
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
                if (grdRoomSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdRoomSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (grdRoomSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    if (grdRoomSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) grdRoomSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
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
        grdRoomSearch.getHelper().setRowsStartFrom(0);
        grdRoomSearch.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdRoomSearch.reQuery();

    }

    private void onSelection() {
        GrdWholeSaleBill_Mdl grdPatientDetails = grdRoomSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String regRecId = "", regDocId = "", roomCode = "";
        int index = -1;

        if (grdPatientDetails != null) {
            index = grdRoomSearch.getDmGrid().getGrid().getStore().indexOf(grdPatientDetails);
            if (index >= 0) {
//                regRecId = grdRoomSearch.getDmGrid().getGrid().getStore().get(index).getRecIdIPD();
//                regDocId = grdRoomSearch.getDmGrid().getGrid().getStore().get(index).getDocIdIPD();
                roomCode = grdRoomSearch.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode();
            }
        }
        hide(false);
        callBack.ok(regDocId, regRecId, roomCode);
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
    
    public WholeSaleBillSearchPopup() {
        super();
        SrvAsync = new ServiceInvoker<WholeSaleBillSearchPopup_SrvAsync>(GWT.create(WholeSaleBillSearchPopup_Srv.class), WholeSaleBillSearchPopup.class.getName());
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
//        this.filterType = filterType;
        grdRoomSearch.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        findEvents();
    }
    
     private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
//        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
//        value_Mdl.setFieldName(searchDbFieldName);
//        value_Mdl.setFieldNameValue(txtSearchValue.getText());
//        value_Mdl.setFilterType(filterType);

        return value_Mdl;
    }
}
