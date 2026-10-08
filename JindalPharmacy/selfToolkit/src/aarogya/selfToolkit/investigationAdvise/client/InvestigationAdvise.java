package aarogya.selfToolkit.investigationAdvise.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.investigationAdvise.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.dmToolkit.self.shared.*;
import java.util.HashMap;
import java.util.List;

public class InvestigationAdvise extends InvestigationAdvise_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void onOk(List<GrdInvestigation_Mdl> listInvestigation);

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<InvestigationAdvise_SrvAsync> srvAsync;
    private String consultantCode;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private boolean isPackage;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHInvestigation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<GrdInvestigation_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInvestigation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdInvestigation_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInvestigation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdInvestigation_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHInvestigation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInvestigation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, GrdInvestigation_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHInvestigation.setTag2nd(DmShared.xNull(t1Row.getCategoryName()));
                    isPackage = t1Row.isPkg();
                } else {
                    txtHInvestigation.setTag2nd("");
                    isPackage = false;
                }
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        setTemplateHeaderValue(0);
        grdFavoriteTest.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdFavoriteTest.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getFavoriteDetail(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdFavorite_Mdl>> result) {
                        if (result.isStatus()) {
                            grdFavoriteTest.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtTestSearch).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize(true);
            }
        });
        grdFavoriteTest.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == colRemoveFav) {
                        removeFromFavorite(grdFavoriteTest.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    } else {
                        GrdFavorite_Mdl gfm = grdFavoriteTest.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                        isPackage = gfm.isPkg();
                        addToGrid(DmShared.iNull(gfm.getSharedKeyFieldCode()).toString(), DmShared.xNull(gfm.getSharedKeyFieldName()), DmShared.xNull(gfm.getCategoryName()));
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdFavoriteTest.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    CellSelectionModel cellModel = (CellSelectionModel) grdFavoriteTest.getDmGrid().getGrid().getSelectionModel();
                    int row = grdFavoriteTest.getDmGrid().getGrid().getStore().indexOf(grdFavoriteTest.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    if (cellModel.getSelectCell().getCell() == colRemoveFav) {
                        removeFromFavorite(grdFavoriteTest.getDmGrid().getGrid().getStore().get(row));
                    } else {
                        GrdFavorite_Mdl gfm = grdFavoriteTest.getDmGrid().getGrid().getStore().get(row);
                        isPackage = gfm.isPkg();
                        addToGrid(DmShared.iNull(gfm.getSharedKeyFieldCode()).toString(), DmShared.xNull(gfm.getSharedKeyFieldName()), DmShared.xNull(gfm.getCategoryName()));
                    }
                }
            }
        }, KeyDownEvent.getType());

        grdFavoriteTemplate.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setTemplateHeaderValue(event.getColumnIndex());
            }
        });
        grdFavoriteTemplate.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                try {
                    if (event.getColumnIndex() == 1) {
                        setWidget(false, new pmMaster().moduleName, new pmMaster().PRESCRIPTION_VALUE_TEMPLATE.caption, grdFavoriteTemplate.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode(), "", "", false, null);
                    } else {
                        getInvestigationViaPackageTemplate("", grdFavoriteTemplate.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdFavoriteTemplate.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    try {
                        getInvestigationViaPackageTemplate("", grdFavoriteTemplate.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode());
                    } catch (Exception ex) {
                        new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                    }
                }
            }
        }, KeyDownEvent.getType());
        grdFavoriteTemplate.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getTemplateFavorite(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            grdFavoriteTemplate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize(false);
            }
        });

        grdDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                removeFromGrid(event.getCellIndex(), event.getRowIndex());
            }
        });
        grdDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    CellSelectionModel cellModel = (CellSelectionModel) grdDetail.getGrid().getSelectionModel();
                    int row = grdDetail.getGrid().getStore().indexOf(grdDetail.getGrid().getSelectionModel().getSelectedItem());
                    if (cellModel.getSelectCell().getCell() == 3 || cellModel.getSelectCell().getCell() == 4) {
                        removeFromGrid(cellModel.getSelectCell().getCell(), row);
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    public void show(String consultantCode, boolean okNeeded, List<GrdInvestigation_Mdl> listInvestigation) {
        this.consultantCode = consultantCode;
        this.isPackage = false;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        flwOk.clear();
        flwCancel.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        } else {
            flwCancel.add(btnCancel);
        }

        clear();
        grdDetail.getGrid().getStore().clear();
        grdDetail.getGrid().getStore().addAll(listInvestigation);
        reQueryTemplate();
        reQueryTest();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHInvestigation.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean callBackNeeded) {
        if (callBackNeeded) {
            if (btnOk.isAttached() && btnOk.isEnabled() && btnOk.isVisible()) {
                this.callBack.onOk(grdDetail.getGrid().getStore().getAll());
            } else if (btnCancel.isAttached() && btnCancel.isEnabled() && btnCancel.isVisible()) {
                this.callBack.onClose();
            }
        }
        this.popupPanel.hide();
    }

    private void fillData(GrdInvestigation_Mdl mrTemp) {
        if (mrTemp != null) {
            txtHInvestigation.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtHInvestigation.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtHInvestigation.setTag2nd(DmShared.xNull(mrTemp.getCategoryName()));
            isPackage = mrTemp.isPkg();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtHInvestigation.setFocus(true);
                }
            });
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel || event.getSource() == btnOk) {
            hide(true);
        } else if (event.getSource() == btnAddToGrid) {
            addToGrid(txtHInvestigation.getTag(), txtHInvestigation.getText(), txtHInvestigation.getTag2nd());
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtTestSearch)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdFavoriteTest.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdFavoriteTest.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        } else if (event.getSource().equals(txtTemplateSearch)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdFavoriteTemplate.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdFavoriteTemplate.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtTestSearch)) {
            if (!searchPrvValue.equalsIgnoreCase(txtTestSearch.getText())) {
                reQueryTest();
            }
            searchPrvValue = txtTestSearch.getText();
        } else if (event.getSource().equals(txtTemplateSearch)) {
            if (!searchPrvValue.equalsIgnoreCase(txtTemplateSearch.getText())) {
                reQueryTest();
            }
            searchPrvValue = txtTemplateSearch.getText();
        }
    }

    private void addToGrid(String code, String name, String categoryName) {
        boolean flag = false;
        GrdInvestigation_Mdl ourRow = new GrdInvestigation_Mdl();
        ourRow.setSharedKeyFieldCode(DmShared.xNull(code));
        ourRow.setSharedKeyFieldName(DmShared.xNull(name));
        ourRow.setCategoryName(DmShared.xNull(categoryName));
        ourRow.setPkg(isPackage);

        int lastIndex = DmShared.iNull(grdDetail.getGrid().getStore().size()).intValue();
        if (DmShared.xNull(name).trim().equalsIgnoreCase("") || DmShared.iNull(code).longValue() == 0) {
            new DmAlertMessageBox("Investigation / Package / Panel is mandatory.", txtHInvestigation).show();
            return;
        }
        for (int i = 0; i < grdDetail.getGrid().getStore().size(); i++) {
            GrdInvestigation_Mdl grd = grdDetail.getGrid().getStore().get(i);
            if ((DmShared.iNull(grd.getSharedKeyFieldCode()).longValue() == DmShared.iNull(ourRow.getSharedKeyFieldCode()).longValue())
                    && (grd.isPkg() == ourRow.isPkg())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            grdDetail.getGrid().getStore().add(lastIndex, ourRow);
            grdDetail.commitChanges();
            clear();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtHInvestigation.setFocus(true);
                }
            });
        } else {
            new DmAlertMessageBox("Investigation / Package / Panel already exists.", txtHInvestigation).show();
        }
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
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public InvestigationAdvise() {
        super();
        srvAsync = new ServiceInvoker<InvestigationAdvise_SrvAsync>(GWT.create(InvestigationAdvise_Srv.class), InvestigationAdvise.class.getName());
        manageEvents();
        textHelpEvents();
        grdFavoriteTest.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        grdFavoriteTemplate.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
    }

    private void reQueryTest() {
        grdFavoriteTest.getHelper().setRowsStartFrom(0);
        grdFavoriteTest.getHelper().getFilter().get(0).setValue1(txtTestSearch.getText());
        grdFavoriteTest.reQuery();
    }

    private void reQueryTemplate() {
        grdFavoriteTemplate.getHelper().setRowsStartFrom(0);
        grdFavoriteTemplate.getHelper().getFilter().get(0).setValue1(txtTemplateSearch.getText());
        grdFavoriteTemplate.reQuery();
    }

    private SearchValue_Mdl valueIntialize(boolean isInvestigation) {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldValue(isInvestigation ? DmShared.xNull(txtTestSearch.getText()) : DmShared.xNull(txtTemplateSearch.getText()));
        value_Mdl.setConsultantCode(DmShared.xNull(consultantCode));

        return value_Mdl;
    }

    private void removeFromGrid(int cellIndex, int rowIndex) {
        if (cellIndex == colArrowUp) {
            fillData(grdDetail.getGrid().getStore().get(rowIndex));
            grdDetail.getGrid().getStore().remove(rowIndex);
            txtHInvestigation.setFocus(true);
        } else if (cellIndex == colArrowLeft) {
            GrdInvestigation_Mdl mrTemp = new GrdInvestigation_Mdl();
            try {
                mrTemp = grdDetail.getGrid().getSelectionModel().getSelectedItem();
                if (DmShared.iNull(consultantCode).longValue() == 0) {
                    DmShared.isValidAmount(DmShared.iNull(consultantCode).longValue(), "Consultant", 100);
                }
                srvAsync.invokeRPC().addFavorite(mrTemp, DmShared.iNull(consultantCode).toString(), new AsyncCallback<DmResult<String>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            reQueryTest();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtTestSearch).show();
                        }
                    }
                });
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnCancel).show();
            }
        }
    }

    private void removeFromFavorite(final GrdFavorite_Mdl node) {
        new DmConfirmMessageBox("Are you sure? You want to remove from Favorites.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    removeFavorites(DmShared.xNull(node.getSharedKeyFieldCode()), node.isPkg());
                }
            }
        }).show();
    }

    private void removeFavorites(String detailCode, boolean isPkg) {
        srvAsync.invokeRPC().removeFavorite(detailCode, consultantCode, isPkg, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    reQueryTest();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtTestSearch).show();
                }
            }
        });
    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnAddToGrid.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtTestSearch.addKeyUpHandler(this);
        txtTestSearch.addKeyDownHandler(this);
        txtTemplateSearch.addKeyUpHandler(this);
        txtTemplateSearch.addKeyDownHandler(this);
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdFavoriteTest.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdFavoriteTest.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdFavoriteTest.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdFavoriteTest.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdFavoriteTest.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtTestSearch.setText("");
        txtTestSearch.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdFavoriteTest.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdFavoriteTest.getDmGrid().getGrid().getView().refresh(true);
        txtTestSearch.setFocus(true);
    }

    private void setTemplateHeaderValue(int columnIndex) {
        if ((grdFavoriteTemplate.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdFavoriteTemplate.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdFavoriteTemplate.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdFavoriteTemplate.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdFavoriteTemplate.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtTemplateSearch.setText("");
        txtTemplateSearch.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : grdFavoriteTemplate.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdFavoriteTemplate.getDmGrid().getGrid().getView().refresh(true);
        txtTemplateSearch.setFocus(true);
    }

    private void clear() {
        isPackage = false;
        txtHInvestigation.setTag("");
        txtHInvestigation.setTag2nd("");
        txtHInvestigation.setText("");
    }

    private void setWidget(final boolean openInPopup, String moduleName, String menuName, final String docId, final String firstAction, final String secondAction, final boolean isCallBack, final HashMap paramHashMap) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuName, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                IsWidgetExtended widget = ((IsWidgetExtended) wgtObject);
                if (!isCallBack && !DmShared.xNull(firstAction).trim().equalsIgnoreCase("")) {
                    widget.action(firstAction, DmShared.iNull(docId).toString());
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                } else if (!DmShared.xNull(firstAction).equalsIgnoreCase("") && !DmShared.xNull(secondAction).equalsIgnoreCase("")) {
                    widget.action(firstAction, DmShared.iNull(docId).toString(), new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                    ((IsWidgetExtended) wgtObject).action(secondAction, null, null);
                                }
                            }
                        }
                    });
                } else {
                    widget.searchRecord(docId);
                    if (!openInPopup) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                }
            }
        });
    }

    private void getInvestigationViaPackageTemplate(String packageCode, String templateDocId) {
        srvAsync.invokeRPC().getInvestigationViaTemplate(templateDocId, new AsyncCallback<DmResult<List<GrdInvestigation_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdInvestigation_Mdl>> result) {
                if (result.isStatus()) {
                    grdDetail.getGrid().getStore().clear();
                    grdDetail.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
