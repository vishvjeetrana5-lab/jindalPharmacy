package aarogya.selfToolkit.patientSearch.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.patientSearch.shared.*;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl.FilterType;
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
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class PatientSearch extends PatientSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void ok(String uhidRecId, String uhidDocId);

        public void close();
    }

    CallBack callBack;
    ServiceInvoker<PatientSearch_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private FilterType[] filterType;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(boolean editable, FilterType... filterType) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        this.filterType = filterType;
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
        dmPatientSearch.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        dmPatientSearch.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });

        dmPatientSearch.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                }
            }
        }, KeyDownEvent.getType());

        dmPatientSearch.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                SrvAsync.invokeRPC(false).getFind(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdPatientDetails_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdPatientDetails_Mdl>> result) {
                        if (result.isStatus()) {
                            dmPatientSearch.setData(result.getData(), result.getPaging());
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
        if ((dmPatientSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = dmPatientSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) dmPatientSearch.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        dmPatientSearch.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        dmPatientSearch.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : dmPatientSearch.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmPatientSearch.getDmGrid().getGrid().getView().refresh(true);
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
                if (dmPatientSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) dmPatientSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (dmPatientSearch.getDmGrid().getGrid().getStore().size() > 0) {
                    if (dmPatientSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) dmPatientSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
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
        dmPatientSearch.getHelper().setRowsStartFrom(0);
        dmPatientSearch.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        dmPatientSearch.reQuery();
    }

    private void onSelection() {
        GrdPatientDetails_Mdl grdPatientDetails = dmPatientSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String uhidRecId = "", uhidDocId = "";
        int index = -1;

        if (grdPatientDetails != null) {
            index = dmPatientSearch.getDmGrid().getGrid().getStore().indexOf(grdPatientDetails);
            if (index >= 0) {
                uhidRecId = dmPatientSearch.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldName();
                uhidDocId = dmPatientSearch.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode();
            }
        }
        callBack.ok(uhidRecId, uhidDocId);
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

    public PatientSearch() {
        super();
        SrvAsync = new ServiceInvoker<PatientSearch_SrvAsync>(GWT.create(PatientSearch_Srv.class), PatientSearch.class.getName());
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
        dmPatientSearch.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        findEvents();
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();

        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(txtSearchValue.getText());
        value_Mdl.setFilterType(filterType);

        return value_Mdl;
    }
}
