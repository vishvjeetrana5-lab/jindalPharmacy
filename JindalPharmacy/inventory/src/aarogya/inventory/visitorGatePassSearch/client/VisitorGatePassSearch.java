package aarogya.inventory.visitorGatePassSearch.client;

import aarogya.inventory.visitorGatePassSearch.shared.*;
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
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
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


public class VisitorGatePassSearch extends VisitorGatePassSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    CallBack callBack;
    ServiceInvoker<VisitorGatePassSearch_SrvAsync> srvAsync;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private String searchDbFieldName, searchPrvValue = "";
    private int searchColumnIndex;
    private boolean gateOutFlag = false;

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (GridVisitorGatePassDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) GridVisitorGatePassDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (GridVisitorGatePassDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    onOkClick();
                }
            }
        }
    }

    public interface CallBack {

        public void ok(String strKey);

        public void close();

    }

    public VisitorGatePassSearch() {
        super();
        srvAsync = new ServiceInvoker<VisitorGatePassSearch_SrvAsync>(GWT.create(VisitorGatePassSearch_Srv.class), VisitorGatePassSearch.class.getName());
        GridVisitorGatePassDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);

        manageEvents();
    }

    private void manageEvents() {
        gridEvents();
        btnOK.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
        GridVisitorGatePassDetail.getDmGrid().getGrid().getStore().clear();
        GridVisitorGatePassDetail.getDmGrid().getGrid().getStore().add(new GrdVisitorGatePassDetailsSearch_Mdl());
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reQuery();
    }

    private void reQuery() {
        GridVisitorGatePassDetail.getHelper().setRowsStartFrom(0);
        GridVisitorGatePassDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        GridVisitorGatePassDetail.reQuery();
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void gridEvents() 
    {
        setHeaderValue(3);

        GridVisitorGatePassDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        GridVisitorGatePassDetail.addCallBack(new DmGridPaging.CallBack<VisitorGateSearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, VisitorGateSearchValue_Mdl searchValue_Mdl) {
                srvAsync.invokeRPC(true).getVisitorGateSearchDetail(topControl.getPermissions(), true, searchValue_Mdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdVisitorGatePassDetailsSearch_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdVisitorGatePassDetailsSearch_Mdl>> result) {
                        if (result.isStatus()) {
                            GridVisitorGatePassDetail.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public VisitorGateSearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        GridVisitorGatePassDetail.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        GridVisitorGatePassDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdVisitorGatePassDetailsSearch_Mdl> cellModel;
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        cellModel = (CellSelectionModel<GrdVisitorGatePassDetailsSearch_Mdl>) GridVisitorGatePassDetail.getDmGrid().getGrid().getSelectionModel();
                        if (cellModel.getSelection() != null) {
                            onOkClick();
                        }
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());
    }

    private void setHeaderValue(int columnIndex) {
        if ((GridVisitorGatePassDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = GridVisitorGatePassDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) GridVisitorGatePassDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        GridVisitorGatePassDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        GridVisitorGatePassDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : GridVisitorGatePassDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        GridVisitorGatePassDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            callBack.close();
        } else if (event.getSource() == btnRefresh) {
            txtSearchValue.setText("");
            reQuery();
        } else if (event.getSource() == btnOK) {
            onOkClick();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
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
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    private VisitorGateSearchValue_Mdl valueIntialize() {
        VisitorGateSearchValue_Mdl value_Mdl = new VisitorGateSearchValue_Mdl();

        value_Mdl.setSharedKeyFieldName(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldNameValue(DmShared.xNull(txtSearchValue.getText()));

        return value_Mdl;
    }

    private void onOkClick() {
        GrdVisitorGatePassDetailsSearch_Mdl gateDetailsSearch_Mdl = GridVisitorGatePassDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        String strKey = "";
        int index = -1;

        if (gateDetailsSearch_Mdl != null) {
            index = GridVisitorGatePassDetail.getDmGrid().getGrid().getStore().indexOf(gateDetailsSearch_Mdl);
            if (index >= 0) {
                strKey = GridVisitorGatePassDetail.getDmGrid().getGrid().getStore().get(index).getSharedKeyFieldCode();
            }
        }
        if (!DmShared.xNull(strKey).trim().equals("")) {
            popupPanel.hide();
            callBack.ok(strKey);
        } else {
            new DmAlertMessageBox("Please select gate entry.", txtSearchValue).show();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

}
