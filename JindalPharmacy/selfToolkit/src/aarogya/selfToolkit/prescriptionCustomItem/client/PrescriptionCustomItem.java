package aarogya.selfToolkit.prescriptionCustomItem.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.prescriptionCustomItem.shared.*;
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
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmOperand;
import dataman.dmBase.self.shared.DmEnum.DmOperator;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomWidgetNature;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;

public class PrescriptionCustomItem extends PrescriptionCustomItem_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void onOk(List<GrdPrescriptionCustomItem_Mdl> listDiagnosis);

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<PrescriptionCustomItem_SrvAsync> srvAsync;
    private String nature = "";
    private String consultantCode;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "", captionName;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHGroupName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPrescriptionCustomGroup(nature, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroupName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroupName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroupName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHName.setTag("");
                txtHName.setText("");
                grdFavorite.reQuery();
            }
        });
        txtHName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPrescriptionCustomItem(nature, DmShared.xNull(txtHGroupName.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHName_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHGroupName.setTag(DmShared.xNull(t1Row.getGroupCode()));
                    txtHGroupName.setText(DmShared.xNull(t1Row.getGroupName()));
                } else {
                    txtHGroupName.setTag("");
                    txtHGroupName.setText("");
                }
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        grdFavorite.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdFavorite.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getFavoriteDetail(nature, helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdFavorite_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdFavorite_Mdl>> result) {
                        if (result.isStatus()) {
                            grdFavorite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchFavriote).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize(true);
            }
        });
        grdFavorite.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == colRemoveFav) {
                        removeFromFavorite(grdFavorite.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    } else {
                        setFavoriteData(grdFavorite.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdFavorite.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    CellSelectionModel cellModel = (CellSelectionModel) grdFavorite.getDmGrid().getGrid().getSelectionModel();
                    int row = grdFavorite.getDmGrid().getGrid().getStore().indexOf(grdFavorite.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    if (cellModel.getSelectCell().getCell() == colRemoveFav) {
                        removeFromFavorite(grdFavorite.getDmGrid().getGrid().getStore().get(row));
                    } else {
                        setFavoriteData(grdFavorite.getDmGrid().getGrid().getStore().get(row));
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
                        getAllergyViaTemplate(grdFavoriteTemplate.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
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
                        getAllergyViaTemplate(grdFavoriteTemplate.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode());
                    } catch (Exception ex) {
                        new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                    }
                }
            }
        }, KeyDownEvent.getType());
        grdFavoriteTemplate.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getTemplateFavorite(nature, helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        grid.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                removeFromGrid(event.getCellIndex(), event.getRowIndex());
            }
        });
        grid.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    CellSelectionModel cellModel = (CellSelectionModel) grid.getGrid().getSelectionModel();
                    int row = grid.getGrid().getStore().indexOf(grid.getGrid().getSelectionModel().getSelectedItem());

                    if (cellModel.getSelectCell().getCell() == 3 || cellModel.getSelectCell().getCell() == 4) {
                        removeFromGrid(cellModel.getSelectCell().getCell(), row);
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    public void show(String nature, String captionName, String consultantCode, boolean okNeeded, List<GrdPrescriptionCustomItem_Mdl> listPrescriptionCustomItem) {
        this.nature = nature;
        this.captionName = captionName;
        this.consultantCode = consultantCode;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        //<editor-fold defaultstate="collapsed" desc="Set Custom Item Name">
        if (DmShared.xNull(captionName).trim().equalsIgnoreCase("")) {
            this.captionName = CustomWidgetNature.getKeyOf(nature).getValue();
        } else {
            this.captionName = DmShared.xNull(captionName).trim();
        }
        toolContainer.setTitle(this.captionName);
        lblName.setText(this.captionName);
        grid.getGrid().getColumnModel().getColumn(colArrowUp + 1).setHeader(this.captionName);
        grid.getGrid().getView().getHeader().refresh();
        grid.getGrid().getView().refresh(true);
        grid.getGrid().getStore().commitChanges();

        if (DmShared.xNull(captionName).trim().length() > 40) {
            lblTabCaption.setText(captionName.substring(0, 35) + "...");
        } else {
            lblTabCaption.setText(captionName);
        }

        //</editor-fold>
        flwOk.clear();
        flwCancel.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        } else {
            flwCancel.add(btnCancel);
        }
        clear();

        grid.getGrid().getStore().clear();
        grid.getGrid().getStore().addAll(listPrescriptionCustomItem);
        reQuery();
        reQueryTemplate();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHGroupName.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean callBackNeeded) {
        if (callBackNeeded) {
            if (btnOk.isAttached() && btnOk.isEnabled() && btnOk.isVisible()) {
                this.callBack.onOk(grid.getGrid().getStore().getAll());
            } else if (btnCancel.isAttached() && btnCancel.isEnabled() && btnCancel.isVisible()) {
                this.callBack.onClose();
            }
        }
        this.popupPanel.hide();
    }

    private void setFavoriteData(GrdFavorite_Mdl node) {
        if (node != null) {
            txtHGroupName.setTag(DmShared.xNull(node.getGroupCode()));
            txtHGroupName.setText(DmShared.xNull(node.getGroupName()));
            txtHName.setTag(DmShared.xNull(node.getSharedKeyFieldCode()));
            txtHName.setText(DmShared.xNull(node.getSharedKeyFieldName()));
            txtRemark.setFocus(true);
        }
    }

    private void removeFromFavorite(final GrdFavorite_Mdl node) {
        new DmConfirmMessageBox("Are you sure? You want to remove from Favorites.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    removeFavorites(DmShared.xNull(node.getSharedKeyFieldCode()));
                }
            }
        }).show();
    }

    private void fillData(GrdPrescriptionCustomItem_Mdl mrTemp, int rowIndex) {
        if (mrTemp != null) {
            txtHGroupName.setTag(DmShared.xNull(mrTemp.getGroupCode()));
            txtHGroupName.setText(DmShared.xNull(mrTemp.getGroupName()));
            txtHName.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtHName.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtRemark.setTag(DmShared.xNull(mrTemp.getRemark()));
            txtRemark.setText(DmShared.xNull(mrTemp.getRemark()));
            txtSNo.setText(DmShared.iNull(rowIndex).toString());

            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtHName.setFocus(true);
                }
            });
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel || event.getSource() == btnOk) {
            hide(true);
        } else if (event.getSource() == btnAddToGrid) {
            addToGrid();
        } else if (event.getSource() == lblName) {
            if (btnOk.isAttached() && btnOk.isEnabled() && btnOk.isVisible()) {
                showPrescriptionCustomItemMast();
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchFavriote)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdFavorite.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdFavorite.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        } else if (event.getSource().equals(txtSearchTemplate)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdFavoriteTemplate.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdFavoriteTemplate.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchFavriote)) {
            if (!searchPrvValue.equalsIgnoreCase(txtSearchFavriote.getText())) {
                reQuery();
            }
            searchPrvValue = txtSearchFavriote.getText();
        } else if (event.getSource().equals(txtSearchTemplate)) {
            if (!searchPrvValue.equalsIgnoreCase(txtSearchTemplate.getText())) {
                reQueryTemplate();
            }
            searchPrvValue = txtSearchTemplate.getText();
        }
    }

    private void addToGrid() {
        boolean flag = false;
        GrdPrescriptionCustomItem_Mdl ourRow = new GrdPrescriptionCustomItem_Mdl();
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHName.getText()));
        ourRow.setGroupCode(DmShared.xNull(txtHGroupName.getTag()));
        ourRow.setGroupName(DmShared.xNull(txtHGroupName.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));

        int lastIndex = DmShared.iNull(grid.getGrid().getStore().size()).intValue();
        if (DmShared.iNull(txtSNo.getText()).intValue() > 0 && DmShared.iNull(txtSNo.getText()).intValue() <= lastIndex) {
            lastIndex = DmShared.iNull(txtSNo.getText()).intValue() - 1;
        }

        if ((DmShared.iNull(txtHName.getTag()).longValue() == 0) && (DmShared.xNull(txtRemark.getText()).equalsIgnoreCase(""))) {
            new DmAlertMessageBox(captionName + " is mandatory.", txtHName).show();
            return;
        }
        for (int i = 0; i < grid.getGrid().getStore().size(); i++) {
            GrdPrescriptionCustomItem_Mdl grd = grid.getGrid().getStore().get(i);
            if ((DmShared.iNull(grd.getSharedKeyFieldCode()).longValue() == DmShared.iNull(ourRow.getSharedKeyFieldCode()).longValue())
                    && (DmShared.xNull(grd.getRemark()).trim().equalsIgnoreCase(DmShared.xNull(ourRow.getRemark()).trim()))) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            grid.getGrid().getStore().add(lastIndex, ourRow);
            grid.commitChanges();
            clear();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtHGroupName.setFocus(true);
                }
            });
        } else {
            new DmAlertMessageBox(captionName + " already exists.", txtHName).show();
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

    public PrescriptionCustomItem() {
        super();
        srvAsync = new ServiceInvoker<PrescriptionCustomItem_SrvAsync>(GWT.create(PrescriptionCustomItem_Srv.class), PrescriptionCustomItem.class.getName());
        toolContainer.setTitle(widgetName);
        manageEvents();
        textHelpEvents();
        grdFavorite.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        grdFavoriteTemplate.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
    }

    private void reQuery() {
        grdFavorite.getHelper().setRowsStartFrom(0);
        grdFavorite.getHelper().getFilter().get(0).setValue1(txtSearchFavriote.getText());
        grdFavorite.reQuery();
    }

    private void reQueryTemplate() {
        grdFavoriteTemplate.getHelper().setRowsStartFrom(0);
        grdFavoriteTemplate.getHelper().getFilter().get(0).setValue1(txtSearchTemplate.getText());
        grdFavoriteTemplate.reQuery();
    }

    private SearchValue_Mdl valueIntialize(boolean isFavorite) {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();

        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldValue(isFavorite ? DmShared.xNull(txtSearchFavriote.getText()) : DmShared.xNull(txtSearchTemplate.getText()));
        value_Mdl.setConsultantCode(DmShared.xNull(consultantCode));

        return value_Mdl;
    }

    private void removeFromGrid(int cellIndex, int rowIndex) {
        if (cellIndex == colArrowUp) {
            fillData(grid.getGrid().getStore().get(rowIndex), rowIndex + 1);
            grid.getGrid().getStore().remove(rowIndex);
            grid.commitChanges();
            txtHName.setFocus(true);
        } else if (cellIndex == colArrowLeft) {
            GrdPrescriptionCustomItem_Mdl mrTemp = new GrdPrescriptionCustomItem_Mdl();
            try {
                mrTemp = grid.getGrid().getSelectionModel().getSelectedItem();
                if (DmShared.iNull(consultantCode).longValue() == 0) {
                    new DmAlertMessageBox("Consultant is mandatory.", (DmTextBox) null).show();
                    return;
                }
                if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                    new DmAlertMessageBox(captionName + " is mandatory.", txtHName).show();
                    return;
                }
                srvAsync.invokeRPC().addFavorite(nature, mrTemp, DmShared.iNull(consultantCode).toString(), new AsyncCallback<DmResult<String>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            reQuery();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchFavriote).show();
                        }
                    }
                });
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnCancel).show();
            }
        }
    }

    private void removeFavorites(String detailCode) {
        if (DmShared.iNull(consultantCode).longValue() == 0) {
            DmShared.isValidAmount(DmShared.iNull(consultantCode).longValue(), "Consultant", 100);
        } else {
            srvAsync.invokeRPC().removeFavorite(nature, detailCode, consultantCode, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        reQuery();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtSearchFavriote).show();
                    }
                }
            });
        }
    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnAddToGrid.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchFavriote.addKeyUpHandler(this);
        txtSearchFavriote.addKeyDownHandler(this);
        txtSearchTemplate.addKeyUpHandler(this);
        txtSearchTemplate.addKeyDownHandler(this);
        lblName.addClickHandler(this);
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdFavorite.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdFavorite.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdFavorite.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdFavorite.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdFavorite.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchFavriote.setText("");
        txtSearchFavriote.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdFavorite.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdFavorite.getDmGrid().getGrid().getView().refresh(true);
        txtSearchFavriote.setFocus(true);
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
        txtSearchTemplate.setText("");
        txtSearchTemplate.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : grdFavoriteTemplate.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdFavoriteTemplate.getDmGrid().getGrid().getView().refresh(true);
        txtSearchTemplate.setFocus(true);
    }

    private void getAllergyViaTemplate(String docId) {
        srvAsync.invokeRPC().getItemViaTemplate(docId, nature, new AsyncCallback<DmResult<List<GrdPrescriptionCustomItem_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdPrescriptionCustomItem_Mdl>> result) {
                if (result.isStatus()) {
                    grid.getGrid().getStore().clear();
                    grid.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setWidget(final boolean openInPopup, String moduleName, String menuName, final String docId, final String firstAction, final String secondAction, final boolean isCallBack, final HashMap paramHashMap) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuName, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                IsWidgetExtended widget = ((IsWidgetExtended) wgtObject);
                if (!isCallBack && !DmShared.xNull(firstAction).trim().equalsIgnoreCase("")) {
                    widget.action(firstAction, DmShared.xNull(docId));
                    if (!openInPopup) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    }
                } else if (!DmShared.xNull(firstAction).equalsIgnoreCase("") && !DmShared.xNull(secondAction).equalsIgnoreCase("")) {
                    widget.action(firstAction, DmShared.xNull(docId), new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    if (!openInPopup) {
                                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                    }
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

    private void showPrescriptionCustomItemMast() {
        if (!DmShared.xNull(nature).trim().equalsIgnoreCase("")) {
            HashMap hm = new HashMap();
            hm.put("flagOpenAsPopup", true);
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmMaster().moduleName, new pmMaster().PRESCRIPTION_CUSTOM_ITEM_MASTER.caption, hm, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    ((IsWidgetExtended) wgtObject).action("ADD_ITEM", DmShared.xNull(nature).trim(), new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    ((IsWidgetExtended) wgtObject).action("FOCUS_ON_ADD", null, null);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void clear() {
        txtHGroupName.setTag("");
        txtHGroupName.setText("");
        txtHName.setTag("");
        txtHName.setText("");
        txtRemark.setTag("");
        txtRemark.setText("");
        txtSNo.setText(DmShared.iNull(grid.getGrid().getStore().getAll().size() + 1).toString());
    }
}
