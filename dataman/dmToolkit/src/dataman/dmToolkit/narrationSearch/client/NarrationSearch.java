package dataman.dmToolkit.narrationSearch.client;

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
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.narrationSearch.shared.NarrationSearch_Mdl;
import dataman.dmToolkit.narrationSearch.shared.NarrationSearch_Srv;
import dataman.dmToolkit.narrationSearch.shared.NarrationSearch_SrvAsync;
import java.util.List;

public class NarrationSearch extends NarrationSearch_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler {

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridNarration.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridNarration.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                onOkClick();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridNarration.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridNarration.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        }
    }

    public interface CallBack {

        public void ok(String narration);

        public void close();

    }

    CallBack callBack;
    ServiceInvoker<NarrationSearch_SrvAsync> srvAsync;
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
        gridNarration.getDmGrid().getGrid().getStore().clear();
        gridNarration.getDmGrid().commitChanges();
        reQuery();
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void gridEvents() {
        setHeaderValue(0);

        gridNarration.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridNarration.addCallBack(new DmGridPaging.CallBack<String>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String searchValue) {
                srvAsync.invokeRPC(true).getNarration(searchValue, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<NarrationSearch_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<NarrationSearch_Mdl>> result) {
                        if (result.isStatus()) {
                            gridNarration.setData(result.getData(), result.getPaging());
                            gridNarration.getDmGrid().commitChanges();
                        }
                    }
                });
            }

            @Override
            public String getParam() {
                return (String) null;
            }
        });

        gridNarration.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                CellSelectionModel<NarrationSearch_Mdl> cellModel;
                try {
                    cellModel = (CellSelectionModel<NarrationSearch_Mdl>) gridNarration.getDmGrid().getGrid().getSelectionModel();
                    if (cellModel.getSelection() != null) {
                        onOkClick();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

        gridNarration.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<NarrationSearch_Mdl> cellModel;
                try {
                    if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        cellModel = (CellSelectionModel<NarrationSearch_Mdl>) gridNarration.getDmGrid().getGrid().getSelectionModel();
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
        if ((gridNarration.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridNarration.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridNarration.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridNarration.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridNarration.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridNarration.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridNarration.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        gridNarration.getHelper().setRowsStartFrom(0);
        gridNarration.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridNarration.reQuery();
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

    private void onOkClick() {
        NarrationSearch_Mdl mrTemp = null;
        if (gridNarration.getDmGrid().getGrid().getStore().size() > 0) {
            if (gridNarration.getDmGrid().getGrid().getSelectionModel().getSelectedItem() != null) {
                mrTemp = gridNarration.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
            }
            if (mrTemp == null) {
                new DmAlertMessageBox("Please select a record first.", txtSearchValue).show();
            } else {
                if (callBack != null) {
                    callBack.ok(mrTemp.getSharedKeyFieldName());


                    hide();
                }
            }
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    public NarrationSearch() {
        super();
        srvAsync = new ServiceInvoker<NarrationSearch_SrvAsync>(GWT.create(NarrationSearch_Srv.class), NarrationSearch.class.getName());
        gridNarration.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
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

}
