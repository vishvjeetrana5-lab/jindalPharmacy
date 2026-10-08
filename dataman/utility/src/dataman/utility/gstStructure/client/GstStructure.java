package dataman.utility.gstStructure.client;

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
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
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
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.gstStructure.shared.GstStructure_Mdl;
import java.util.HashMap;
import java.util.List;
import dataman.utility.gstStructure.shared.GstStructure_Srv;
import dataman.utility.gstStructure.shared.GstStructure_SrvAsync;

public class GstStructure extends GstStructure_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents, BlurHandler, KeyUpHandler, KeyDownHandler {

    private ServiceInvoker<GstStructure_SrvAsync> SrvAsync;
    private GstStructure_Mdl its_Mdl = new GstStructure_Mdl();

    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private final GstStructureGridEdit gridEdit = new GstStructureGridEdit();

    private SetWidgetInvoker swi;

    private void gridEvents() {
        setHeaderValue(1);
        dmGrd.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        dmGrd.addCallBack(new DmGridPaging.CallBack<GstStructure_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, GstStructure_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GstStructure_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GstStructure_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                            dmGrd.getDmGrid().commitChanges();
                        }
                    }
                });
            }

            @Override
            public GstStructure_Mdl getParam() {
                return its_Mdl;
            }
        });
        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });
        dmGrd.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (DmShared.iNull(event.getColumnIndex()).intValue() > -1) {

                    gridEdit(event.getRowIndex());
                }

            }
        });
        dmGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    if (dmGrd.getDmGrid().getGrid().getStore().indexOf(dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem()) > 1) {

                    }
                }
            }
        }, KeyDownEvent.getType());

    }

    private void textHelpEvents() {

        gridEdit.addCallBack(new GstStructureGridEdit.CallBack() {
            @Override
            public void onOk(GstStructure_Mdl itemTaxStructure_Mdl) {
                dmGrd.getDmGrid().commitChanges();
//                dmGrd.reQuery();
                reQuery(dmGrd.getHelper().getRowsStartFrom());
                gridEdit.hide();
            }

            @Override
            public void onDelete() {

            }
        });

    }

    private void gridEdit(int currentRowIndex) {
        GstStructure_Mdl grdItemStructure = dmGrd.getDmGrid().getGrid().getStore().get(currentRowIndex);
        gridEdit.show(this, grdItemStructure);
    }

    private void onLoad() {
        dmGrd.getHelper().setRowsStartFrom(0);
        dmGrd.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        dmGrd.reQuery();
    }

    public GstStructure(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<GstStructure_SrvAsync>(GWT.create(GstStructure_Srv.class), GstStructure.class.getName());
        dmGrd.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        flwForm.addAttachHandler(this);
        manageEvents();
        textHelpEvents();

        gridEvents();
        onLoad();
        swi.setWidget(this);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GstStructure.this);
                }
            }
        }).show();
    }

    @Override

    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            gridEdit.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {

            onClose();
        }
    }

    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    private void setHeaderValue(int columnIndex) {
        if ((dmGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = dmGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) dmGrd.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        dmGrd.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        dmGrd.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : dmGrd.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmGrd.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (dmGrd.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) dmGrd.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//                onOkClick();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (dmGrd.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) dmGrd.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
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
            reQuery(0);
        }
        searchPrvValue = txtSearchValue.getText();
    }

    private void reQuery(int rowStartFrom) {
        dmGrd.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        dmGrd.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        dmGrd.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        dmGrd.reQuery(rowStartFrom, its_Mdl);
    }

    private void manageEvents() {
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

}
