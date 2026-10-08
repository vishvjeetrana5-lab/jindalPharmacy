package dataman.dmToolkit.accountAdjustment.client;

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
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchPopup extends SearchPopup_Dsg implements IsWidget, ClickHandler, KeyUpHandler, AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void select(List<GrdAdjustment_Mdl> selections);

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    protected ServiceInvoker<AccountAdjustment_SrvAsync> srvAsync;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private String searchDbFieldName;
    private int searchColumnIndex;
    private SearchFilterPopup filterPopup = new SearchFilterPopup();
    private List<GrdAdjustment_Mdl> lstSelections = new ArrayList<>();
    protected Filter_Mdl filter = new Filter_Mdl();
    protected boolean isPartyCredit = false;

    public SearchPopup() {
        super();
        grdSearch.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        manageEvents();
        manageCallback();
    }

    public void show(AccountAdjustment voucherAdjustmentPopup) {
        this.srvAsync = voucherAdjustmentPopup.srvAsync;
        this.isPartyCredit = voucherAdjustmentPopup.isPartyCredit;

        lstSelections.clear();
        for (GrdAdjustment_Mdl gaMdl : voucherAdjustmentPopup.grdAdjustment.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(gaMdl.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                lstSelections.add(gaMdl);
            }
        }

        this.filter.setShowDebit(isPartyCredit);
        if (!DmShared.xNull(filter.getAcCode()).contains(",")) {
            this.filter.setAcCode(voucherAdjustmentPopup.lblAcName.getElement().getId());
            this.filter.setAcName(voucherAdjustmentPopup.lblAcName.getText());
        }

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

    private void focusGrid() {
        ((CellSelectionModel) grdSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
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

    private void manageEvents() {
        gridEvents();
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        btnClose.addClickHandler(this);
        btnFilter.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void gridEvents() {
        setHeaderValue(1);

        grdSearch.addCallBack(new DmGridPaging.CallBack<Filter_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, Filter_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getLedger(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdAdjustment_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdAdjustment_Mdl>> result) {
                        if (result.isStatus()) {
                            grdSearch.setData(result.getData(), result.getPaging());
                            setTicks();
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
                if (event.getCellIndex() == 0) {
                    setTick(event.getRowIndex());
                }
            }
        });

        grdSearch.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    if (grdSearch.getDmGrid().getSelectedCell().getCell() == 0) {
                        setTick(grdSearch.getDmGrid().getSelectedCell().getRow());
                    }
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                    if (grdSearch.getDmGrid().getGrid().getStore().indexOf(grdSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem()) == 0) {
                        txtSearchValue.setFocus(true);
                    }
                }
            }
        }, KeyDownEvent.getType());

    }

    private void setTick(int rowIndex) {
        GrdAdjustment_Mdl selectedRow = grdSearch.getDmGrid().getGrid().getStore().get(rowIndex);
        int val = DmShared.vNull(selectedRow.getIsSelected()).intValue();
        boolean contains = false;

        if (val == 0) {
            selectedRow.setIsSelected("1");
            selectedRow.setAmountDrCr(isPartyCredit ? "Dr" : "Cr");
            selectedRow.setAdjustedDrCr(isPartyCredit ? "Cr" : "Dr");
            selectedRow.setAdjustmentDrCr(isPartyCredit ? "Cr" : "Dr");

            for (Iterator<GrdAdjustment_Mdl> iterator = lstSelections.iterator(); iterator.hasNext();) {
                GrdAdjustment_Mdl next = iterator.next();
                if (DmShared.iNull(selectedRow.getSharedKeyFieldCode()).longValue() == DmShared.iNull(next.getSharedKeyFieldCode()).longValue()) {
                    if (DmShared.iNull(selectedRow.getV_SNo()).intValue() == DmShared.iNull(next.getV_SNo()).intValue()) {
                        contains = true;
                        break;
                    }
                }
            }

            if (!contains) {
                lstSelections.add(selectedRow);
            }
        } else if (val == 1) {
            selectedRow.setIsSelected("0");
            for (Iterator<GrdAdjustment_Mdl> iterator = lstSelections.iterator(); iterator.hasNext();) {
                GrdAdjustment_Mdl next = iterator.next();
                if (DmShared.iNull(selectedRow.getSharedKeyFieldCode()).longValue() == DmShared.iNull(next.getSharedKeyFieldCode()).longValue()) {
                    if (DmShared.iNull(selectedRow.getV_SNo()).intValue() == DmShared.iNull(next.getV_SNo()).intValue()) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        grdSearch.getDmGrid().commitChanges();
    }

    private void setTicks() {
        for (GrdAdjustment_Mdl untickedRow : grdSearch.getDmGrid().getGrid().getStore().getAll()) {
            for (GrdAdjustment_Mdl tickedRow : lstSelections) {
                if (DmShared.iNull(untickedRow.getSharedKeyFieldCode()).longValue() == DmShared.iNull(tickedRow.getSharedKeyFieldCode()).longValue()) {
                    if (DmShared.iNull(untickedRow.getV_SNo()).intValue() == DmShared.iNull(tickedRow.getV_SNo()).intValue()) {
                        untickedRow.setIsSelected("1");
                    }
                }
            }
        }
        grdSearch.getDmGrid().commitChanges();
    }

    private void onOk() {
        if (lstSelections.size() > 0) {
            List<GrdAdjustment_Mdl> clonedSelections = new ArrayList<>();
            GrdAdjustment_Mdl clone = null;
            for (GrdAdjustment_Mdl gaMdl : lstSelections) {
                clone = gaMdl.clone();
                clone.setAmount(DmClient.vNull(Math.abs(DmShared.vNull(clone.getAmount())), "0.00"));
                clone.setAdjusted(DmClient.vNull(Math.abs(DmShared.vNull(clone.getAdjusted())), "0.00"));
                clone.setAdjustment(DmClient.vNull(Math.abs(DmShared.vNull(clone.getAdjustment())), "0.00"));
                clonedSelections.add(clone);
            }
            callBack.select(clonedSelections);
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
        } else if (event.getSource() == btnOk) {
            onOk();
        }
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
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            onOk();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F) {
            showFilterPopup();
        }
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

}
