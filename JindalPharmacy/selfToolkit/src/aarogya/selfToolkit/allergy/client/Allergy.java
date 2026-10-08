package aarogya.selfToolkit.allergy.client;

import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.allergy.shared.*;
import aarogya.selfToolkit.medicineAdvise.shared.GrdMedicineDetail_Mdl;
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
import dataman.dmBase.self.shared.DmEnum.DmOperand;
import dataman.dmBase.self.shared.DmEnum.DmOperator;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.dmToolkit.self.shared.*;
import java.util.HashMap;
import java.util.List;

public class Allergy extends Allergy_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    public interface CallBack {

        public void onOk(List<GrdAllergy_Mdl> listAllergy);

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<Allergy_SrvAsync> srvAsync;
    private String consultantCode;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "", ptntPrscrptnDocId = "";
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private List<GrdMedicineDetail_Mdl> listMedicine;
    boolean isAllergic = false, addAllergy = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHAllergyNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAllergyNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAllergyNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAllergyNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAllergyNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHAllergy.setTag("");
                txtHAllergy.setText("");
                grdFavoriteAllergy.reQuery();
            }
        });
        txtHAllergy.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAllergy_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAllergy(DmShared.xNull(txtHAllergyNature.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAllergy_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAllergy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAllergy_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAllergy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAllergy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAllergy_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    chkPatientMedicineAllergy(t1Row.getSharedKeyFieldCode(), t1Row.getSharedKeyFieldName());
                    txtHAllergyNature.setTag(DmShared.xNull(t1Row.getNatureCode()));
                    txtHAllergyNature.setText(DmShared.xNull(t1Row.getNatureName()));
                } else {
                    txtHAllergyNature.setTag("");
                    txtHAllergyNature.setText("");
                }
            }
        });
        txtHRemark.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRemark(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRemark).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRemark.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRemark).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        setTemplateHeaderValue(0);
        grdFavoriteAllergy.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdFavoriteAllergy.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

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
                            grdFavoriteAllergy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchAllergy).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize(true);
            }
        });
        grdFavoriteAllergy.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == colRemoveFav) {
                        removeFromFavorite(grdFavoriteAllergy.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    } else {
                        setFavoriteData(grdFavoriteAllergy.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdFavoriteAllergy.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    CellSelectionModel cellModel = (CellSelectionModel) grdFavoriteAllergy.getDmGrid().getGrid().getSelectionModel();
                    int row = grdFavoriteAllergy.getDmGrid().getGrid().getStore().indexOf(grdFavoriteAllergy.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    if (cellModel.getSelectCell().getCell() == colRemoveFav) {
                        removeFromFavorite(grdFavoriteAllergy.getDmGrid().getGrid().getStore().get(row));
                    } else {
                        setFavoriteData(grdFavoriteAllergy.getDmGrid().getGrid().getStore().get(row));
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

    private void chkPatientMedicineAllergy(final String compostionCode, final String componentName) {
        srvAsync.invokeRPC().getIsPatientPrescbToAllergicMedicine(DmShared.iNull(ptntPrscrptnDocId).toString(), DmShared.iNull(compostionCode).toString(), DmShared.iNull(consultantCode).toString(), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    if (!result.getData().equalsIgnoreCase("")) {

                        String message = "Patient is prescribed with " + result.getData() + " medicine that contains " + componentName + " Kindly unprescribed them.";
                        new DmAlertMessageBox(message, txtHAllergy).show();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHAllergy).show();
                }
            }
        });
    }

    public void show(String ptntPrscrptnDocId, String consultantCode, boolean okNeeded, boolean addAllergy, List<GrdAllergy_Mdl> listAllergy, List<GrdMedicineDetail_Mdl> listMedicine) {
        this.addAllergy = addAllergy;
        this.listMedicine = listMedicine;
        this.consultantCode = consultantCode;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        this.ptntPrscrptnDocId = DmShared.iNull(ptntPrscrptnDocId).toString();
        flwOk.clear();
        flwCancel.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
        } else {
            flwCancel.add(btnCancel);
        }

        clear();
        grdDetail.getGrid().getStore().clear();
        grdDetail.getGrid().getStore().addAll(listAllergy);
        reQuery();
        reQueryTemplate();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHAllergy.setFocus(true);
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

    private void fillData(GrdAllergy_Mdl mrTemp) {
        if (mrTemp != null) {
            txtHAllergy.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtHAllergy.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtHAllergyNature.setTag(DmShared.xNull(mrTemp.getNatureCode()));
            txtHAllergyNature.setText(DmShared.xNull(mrTemp.getNatureName()));
            txtHRemark.setTag(DmShared.xNull(mrTemp.getRemark()));
            txtHRemark.setText(DmShared.xNull(mrTemp.getRemark()));
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtHAllergy.setFocus(true);
                }
            });
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel || event.getSource() == btnOk) {
            hide(true);
        } else if (event.getSource() == btnAddToGrid) {
            checkAllergy();
        } else if (event.getSource() == lblAllergy) {
            if (btnOk.isAttached() && btnOk.isEnabled() && btnOk.isVisible() && addAllergy) {
                HashMap hm = new HashMap();
                hm.put("flagOpenAsPopup", true);
                setWidget(true, new pmMaster().moduleName, new pmMaster().ALLERGY_MASTER.caption, "", "", "", false, hm);
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchAllergy)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdFavoriteAllergy.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdFavoriteAllergy.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
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
        if (event.getSource().equals(txtSearchAllergy)) {
            if (!searchPrvValue.equalsIgnoreCase(txtSearchAllergy.getText())) {
                reQuery();
            }
            searchPrvValue = txtSearchAllergy.getText();
        } else if (event.getSource().equals(txtSearchTemplate)) {
            if (!searchPrvValue.equalsIgnoreCase(txtSearchTemplate.getText())) {
                reQueryTemplate();
            }
            searchPrvValue = txtSearchTemplate.getText();
        }
    }

    private void setFavoriteData(GrdFavorite_Mdl node) {
        if (node != null) {
            txtHAllergyNature.setTag(DmShared.xNull(node.getNatureCode()));
            txtHAllergyNature.setText(DmShared.xNull(node.getNatureName()));
            txtHAllergy.setTag(DmShared.xNull(node.getSharedKeyFieldCode()));
            txtHAllergy.setText(DmShared.xNull(node.getSharedKeyFieldName()));
            txtHRemark.setFocus(true);
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

    private void checkAllergy() {
        if (listMedicine != null && listMedicine.size() > 0) {
            isAllergic = false;
            for (GrdMedicineDetail_Mdl node : listMedicine) {
                if (DmShared.iNull(node.getCompositionCode()).longValue() != 0) {
                    if (DmShared.iNull(node.getCompositionCode()).longValue() == DmShared.iNull(txtHAllergy.getTag()).longValue()) {
                        isAllergic = true;
                        new DmConfirmMessageBox("Allergic medicine found in Medicine Advice, Do you want to continue?.", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    addToGrid();
                                }
                            }
                        }).show();
                    }
                }
                if (isAllergic) {
                    break;
                }
            }
            if (!isAllergic) {
                addToGrid();
            }
        } else {
            addToGrid();
        }
    }

    private void addToGrid() {
        boolean flag = false;
        GrdAllergy_Mdl ourRow = new GrdAllergy_Mdl();
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHAllergy.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHAllergy.getText()));
        ourRow.setNatureCode(DmShared.xNull(txtHAllergyNature.getTag()));
        ourRow.setNatureName(DmShared.xNull(txtHAllergyNature.getText()));
        ourRow.setRemark(DmShared.xNull(txtHRemark.getText()));

        int lastIndex = DmShared.iNull(grdDetail.getGrid().getStore().size()).intValue();
        lastIndex = ((lastIndex - 1) <= 0 ? 0 : (lastIndex - 1));
        if (DmShared.xNull(ourRow.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Allergy is mandatory.", txtHAllergy).show();
            return;
        }
        for (int i = 0; i < grdDetail.getGrid().getStore().size(); i++) {
            GrdAllergy_Mdl grd = grdDetail.getGrid().getStore().get(i);
            if (DmShared.xNull(grd.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(ourRow.getSharedKeyFieldName()).trim())) {
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
                    txtHAllergy.setFocus(true);
                }
            });
        } else {
            new DmAlertMessageBox("Allergy already exists.", txtHAllergy).show();
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

    public Allergy() {
        super();
        srvAsync = new ServiceInvoker<Allergy_SrvAsync>(GWT.create(Allergy_Srv.class), Allergy.class.getName());
        manageEvents();
        textHelpEvents();
        grdFavoriteAllergy.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        grdFavoriteTemplate.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
    }

    private void reQuery() {
        grdFavoriteAllergy.getHelper().setRowsStartFrom(0);
        grdFavoriteAllergy.getHelper().getFilter().get(0).setValue1(txtSearchAllergy.getText());
        grdFavoriteAllergy.reQuery();
    }

    private void reQueryTemplate() {
        grdFavoriteTemplate.getHelper().setRowsStartFrom(0);
        grdFavoriteTemplate.getHelper().getFilter().get(0).setValue1(txtSearchTemplate.getText());
        grdFavoriteTemplate.reQuery();
    }

    private SearchValue_Mdl valueIntialize(boolean isAllergy) {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();

        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldValue(isAllergy ? DmShared.xNull(txtSearchAllergy.getText()) : DmShared.xNull(txtSearchTemplate.getText()));
        value_Mdl.setNatureCode(DmShared.xNull(txtHAllergyNature.getTag()));
        value_Mdl.setConsultantCode(DmShared.xNull(consultantCode));

        return value_Mdl;
    }

    private void removeFromGrid(int cellIndex, int rowIndex) {
        if (cellIndex == colArrowUp) {
            fillData(grdDetail.getGrid().getStore().get(rowIndex));
            grdDetail.getGrid().getStore().remove(rowIndex);
            txtHAllergy.setFocus(true);
        } else if (cellIndex == colArrowLeft) {
            GrdAllergy_Mdl mrTemp = new GrdAllergy_Mdl();
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
                            reQuery();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchAllergy).show();
                        }
                    }
                });
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnCancel).show();
            }
        }
    }

    private void removeFavorites(String detailCode) {
        srvAsync.invokeRPC().removeFavorite(detailCode, consultantCode, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtSearchAllergy).show();
                }
            }
        });
    }

    private void manageEvents() {
        lblAllergy.addClickHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnAddToGrid.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchAllergy.addKeyUpHandler(this);
        txtSearchAllergy.addKeyDownHandler(this);
        txtSearchTemplate.addKeyUpHandler(this);
        txtSearchTemplate.addKeyDownHandler(this);
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdFavoriteAllergy.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdFavoriteAllergy.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdFavoriteAllergy.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdFavoriteAllergy.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdFavoriteAllergy.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchAllergy.setText("");
        txtSearchAllergy.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdFavoriteAllergy.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdFavoriteAllergy.getDmGrid().getGrid().getView().refresh(true);
        txtSearchAllergy.setFocus(true);
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

    private void clear() {
        txtHAllergy.setTag("");
        txtHAllergy.setText("");
        txtHAllergyNature.setTag("");
        txtHAllergyNature.setText("");
        txtHRemark.setTag("");
        txtHRemark.setText("");
    }

    private void getAllergyViaTemplate(String docId) {
        srvAsync.invokeRPC().getAllergyViaTemplate(docId, new AsyncCallback<DmResult<List<GrdAllergy_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdAllergy_Mdl>> result) {
                if (result.isStatus()) {
                    grdDetail.getGrid().getStore().clear();
                    grdDetail.getGrid().getStore().addAll(result.getData());
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
}
