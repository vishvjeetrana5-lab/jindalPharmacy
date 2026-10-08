package aarogya.selfToolkit.medicineAdvise.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.allergy.shared.GrdAllergy_Mdl;
import aarogya.selfToolkit.medicineAdvise.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomWidgetNature;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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
import java.util.HashMap;
import java.util.List;

public class MedicineAdvise extends MedicineAdvise_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler,
        KeyUpHandler, KeyDownHandler, KeyPressHandler {

    public interface CallBack {

        public void onOk(List<GrdMedicineDetail_Mdl> listMedicine);

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<MedicineAdvise_SrvAsync> srvAsync;
    private String consultantCode, pharmacySite = "";
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private final AddNewRecord addNewRecord = new AddNewRecord();
    private List<GrdAllergy_Mdl> listAllergy;
    private boolean isAllergic = false, addMedicine = false, addFrequency = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        addNewRecord.addCallBack(new AddNewRecord.CallBack() {
            @Override
            public void onOk(GrdMedicineDetail_Mdl node) {
                if (node != null) {
                    txtHMedicine.setTag(DmShared.xNull(node.getSharedKeyFieldCode()));
                    txtHMedicine.setText(DmShared.xNull(node.getSharedKeyFieldName()));
                    txtHMedicine.setFocus(true);
                }
            }

            @Override
            public void onClose() {
                txtHMedicine.setFocus(true);
            }
        });

        txtHMedicine.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicine_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, pharmacySite, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicine_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicine).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicine_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtComposition.setTag(DmShared.xNull(t1Row.getCompositionCode()));
                    txtComposition.setText(DmShared.xNull(t1Row.getCompositionName()));
                } else {
                    txtComposition.setTag("");
                    txtComposition.setText("");
                }
            }
        });
        txtHFrequency.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHFrequency_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFrequency(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHFrequency_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFrequency).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHFrequency_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHFrequency.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFrequency).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHFrequency_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHFrequency.setTag2nd(DmShared.xNull(t1Row.getPresentation()));
                } else {
                    txtHFrequency.setTag2nd("");
                }
            }
        });
        txtHDuration.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDuration(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDuration).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDuration.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDuration).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHInstruction.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHInstruction_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInstruction(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHInstruction_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInstruction).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHInstruction_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHInstruction.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInstruction).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHInstruction_Mdl t1Row) {
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        setTemplateHeaderValue(0);
        grdFavoriteMedicine.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdFavoriteMedicine.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

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
                            grdFavoriteMedicine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtDrugSearch).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize(true);
            }
        });
        grdFavoriteMedicine.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == colRemoveFav) {
                        removeFromFavorite(grdFavoriteMedicine.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    } else {
                        setFavoriteData(grdFavoriteMedicine.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdFavoriteMedicine.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    CellSelectionModel cellModel = (CellSelectionModel) grdFavoriteMedicine.getDmGrid().getGrid().getSelectionModel();
                    int row = grdFavoriteMedicine.getDmGrid().getGrid().getStore().indexOf(grdFavoriteMedicine.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    if (cellModel.getSelectCell().getCell() == colRemoveFav) {
                        removeFromFavorite(grdFavoriteMedicine.getDmGrid().getGrid().getStore().get(row));
                    } else {
                        setFavoriteData(grdFavoriteMedicine.getDmGrid().getGrid().getStore().get(row));
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
                        getMedicineTemplate(grdFavoriteTemplate.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
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
                        getMedicineTemplate(grdFavoriteTemplate.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode());
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

    public void show(String enviroPharmacySite, String consultantCode, boolean okNeeded, boolean addMedicine, boolean addFrequency, List<GrdMedicineDetail_Mdl> listMedicine, List<GrdAllergy_Mdl> listAllergy) {
        this.pharmacySite = enviroPharmacySite;
        this.addMedicine = addMedicine;
        this.addFrequency = addFrequency;
        this.listAllergy = listAllergy;
        this.consultantCode = consultantCode;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        clear();
        grdDetail.getGrid().getStore().clear();
        grdDetail.getGrid().getStore().addAll(listMedicine);
        reQueryMedicine();
        reQueryTemplate();

        flwOk.clear();
        flwCancel.clear();
        if (okNeeded) {
            flwOk.add(btnOk);
            onAdd();
        } else {
            flwCancel.add(btnCancel);
        }
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHMedicine.setFocus(true);
            }
        });
    }

    public void show(String consultantCode, boolean okNeeded, boolean addMedicine, boolean addFrequency, List<GrdMedicineDetail_Mdl> listMedicine, List<GrdAllergy_Mdl> listAllergy) {
        show("", consultantCode, okNeeded, addMedicine, addFrequency, listMedicine, listAllergy);
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

    private void fillData(GrdMedicineDetail_Mdl node, int rowIndex) {
        if (node != null) {
            txtHMedicine.setTag(DmShared.xNull(node.getSharedKeyFieldCode()));
            txtHMedicine.setText(DmShared.xNull(node.getSharedKeyFieldName()));
            txtComposition.setTag(DmShared.xNull(node.getCompositionCode()));
            txtComposition.setText(DmShared.xNull(node.getCompositionName()));
            txtHFrequency.setTag2nd(DmShared.xNull(node.getDosePresentation()));
            txtHFrequency.setTag(DmShared.xNull(node.getFrequencyCode()));
            txtHFrequency.setText(DmShared.xNull(node.getFrequencyName()));
            txtDoseText.setTag2nd(DmShared.xNull(node.getDoseDisplayText()));
            txtDoseText.setTag(DmShared.xNull(node.getDoseText()));
            txtDoseText.setText(getDoseValueNumeric(DmShared.xNull(node.getDoseText())));
            txtDays.setText(DmShared.iNull(node.getDays()).toString());
            txtHDuration.setTag(DmShared.xNull(node.getDurationCode()));
            txtHDuration.setText(DmShared.xNull(node.getDurationName()));
            txtRemark.setText(DmShared.xNull(node.getRemark()));
            txtHInstruction.setTag(DmShared.xNull(node.getInstructionCode()));
            txtHInstruction.setText(DmShared.xNull(node.getInstructionName()));
            txtSNo.setText(DmShared.iNull(rowIndex).toString());

            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtHMedicine.setFocus(true);
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
        } else if (event.getSource() == lblMedicine) {
            if (addMedicine) {
                addNewMedicine();
            }
        } else if (event.getSource() == lblFrequency) {
            if (addFrequency) {
                HashMap hm = new HashMap();
                hm.put("flagOpenAsPopup", true);
                setWidget(true, new pmMaster().moduleName, new pmMaster().FREQUENCY_MASTER.caption, "", "", "", false, hm);
            }
        } else if (event.getSource() == lblInstruction) {
            HashMap hm = new HashMap();
            hm.put("flagOpenAsPopup", true);
            setWidget(true, new pmMaster().moduleName, new pmMaster().PRESCRIPTION_CUSTOM_ITEM_MASTER.caption, CustomWidgetNature.MEDICINE_INSTRUCTION.getId(), "ADD_ITEM", "FOCUS_ON_ADD", true, hm);
        }
    }

    private void addNewMedicine() {
        if (btnOk.isAttached() && btnOk.isEnabled() && btnOk.isVisible()) {
            addNewRecord.show();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtDrugSearch)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdFavoriteMedicine.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdFavoriteMedicine.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
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
        if (event.getSource().equals(txtDrugSearch)) {
            if (!searchPrvValue.equalsIgnoreCase(txtDrugSearch.getText())) {
                reQueryMedicine();
            }
            searchPrvValue = txtDrugSearch.getText();
        } else if (event.getSource().equals(txtTemplateSearch)) {
            if (!searchPrvValue.equalsIgnoreCase(txtTemplateSearch.getText())) {
                reQueryTemplate();
            }
            searchPrvValue = txtTemplateSearch.getText();
        }
    }

    private void setFavoriteData(GrdFavorite_Mdl node) {
        if (node != null) {
            txtHMedicine.setTag(DmShared.xNull(node.getSharedKeyFieldCode()));
            txtHMedicine.setText(DmShared.xNull(node.getSharedKeyFieldName()));
            txtComposition.setTag(DmShared.xNull(node.getCompositionCode()));
            txtComposition.setText(DmShared.xNull(node.getCompositionName()));
            txtHFrequency.setTag2nd(DmShared.xNull(node.getDosePresentation()));
            txtHFrequency.setTag(DmShared.xNull(node.getFrequencyCode()));
            txtHFrequency.setText(DmShared.xNull(node.getFrequencyName()));
            txtHInstruction.setTag(DmShared.xNull(node.getInstructionCode()));
            txtHInstruction.setText(DmShared.xNull(node.getInstructionName()));
            txtDoseText.setTag2nd(DmShared.xNull(node.getDoseDisplayText()));
            txtDoseText.setTag(DmShared.xNull(node.getDoseText()));
            txtDoseText.setText(getDoseValueNumeric(DmShared.xNull(node.getDoseText())));
            onAdd();
            txtDays.setFocus(true);
        }
    }

    private void removeFromFavorite(final GrdFavorite_Mdl node) {
        new DmConfirmMessageBox("Are you sure? You want to remove from Favorites.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    removeFavorites(node);
                }
            }
        }).show();
    }

    private void checkAllergy() {
        if (listAllergy != null && listAllergy.size() > 0) {
            isAllergic = false;
            for (GrdAllergy_Mdl node : listAllergy) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() == DmShared.iNull(txtComposition.getTag()).longValue()) {
                        isAllergic = true;
                        new DmConfirmMessageBox("Allergic composition found in Allergy, Do you want to continue?.", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    manageDose(true);
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
                manageDose(true);
            }
        } else {
            manageDose(true);
        }
    }

    private void addToGrid() {
        boolean flag = false;
        GrdMedicineDetail_Mdl ourRow = new GrdMedicineDetail_Mdl();

        ourRow.setFieldUniqueCode(String.valueOf(grdDetail.getGrid().getStore().size() + 1));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicine.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicine.getText()));
        ourRow.setCompositionCode(DmShared.xNull(txtComposition.getTag()));
        ourRow.setCompositionName(DmShared.xNull(txtComposition.getText()));
        ourRow.setDosePresentation(DmShared.xNull(txtHFrequency.getTag2nd()));
        ourRow.setFrequencyCode(DmShared.xNull(txtHFrequency.getTag()));
        ourRow.setFrequencyName(DmShared.xNull(txtHFrequency.getText()));
        ourRow.setDays(DmClient.vNull(txtDays.getText(), "0"));
        ourRow.setDurationCode(DmShared.xNull(txtHDuration.getTag()));
        ourRow.setDurationName(DmShared.xNull(txtHDuration.getText()));
        ourRow.setDuration(ourRow.getDays() + " " + (ourRow.getDurationName().equalsIgnoreCase("Day (Next)") ? "DN"
                : ourRow.getDurationName().equalsIgnoreCase("Week") ? "W"
                : ourRow.getDurationName().equalsIgnoreCase("Week (Next)") ? "WN"
                : ourRow.getDurationName().equalsIgnoreCase("Month") ? "M"
                : ourRow.getDurationName().equalsIgnoreCase("Dose") ? "Ds"
                : ourRow.getDurationName().equalsIgnoreCase("Month (Next)") ? "MN" : "D"));

        ourRow.setRemark(DmShared.xNull(txtRemark.getText()).trim());
        ourRow.setInstructionCode(DmShared.xNull(txtHInstruction.getTag()));
        ourRow.setInstructionName(DmShared.xNull(txtHInstruction.getText()));
        ourRow.setDoseValue(DmShared.xNull(txtDoseText.getText()));
        ourRow.setDoseText(DmShared.xNull(txtDoseText.getTag()));
        ourRow.setDoseDisplayText(DmShared.xNull(txtDoseText.getTag2nd()));
        ourRow.setNetDoseValue(String.valueOf(getDoseValue(ourRow.getDoseText())));

        int lastIndex = grdDetail.getGrid().getStore().size();
        if (DmShared.iNull(txtSNo.getText()).intValue() > 0 && DmShared.iNull(txtSNo.getText()).intValue() <= lastIndex) {
            lastIndex = DmShared.iNull(txtSNo.getText()).intValue() - 1;
        }

        if (DmShared.iNull(txtHMedicine.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Medicine Name is mandatory.", txtHMedicine).show();
            return;
        }
        if (DmShared.iNull(txtHFrequency.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Frequency is mandatory.", txtHFrequency).show();
            return;
        }
        if (DmShared.xNull(txtDoseText.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Dose is mandatory.", txtDoseText).show();
            return;
        }
        if (DmShared.vNull(txtDays.getText()).doubleValue() <= 0) {
            new DmAlertMessageBox("Days is mandatory.", txtDays).show();
            return;
        }
        if (DmShared.xNull(txtHDuration.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Duration is mandatory.", txtDays).show();
            return;
        }

        for (int i = 0; i < grdDetail.getGrid().getStore().size(); i++) {
            GrdMedicineDetail_Mdl grd = grdDetail.getGrid().getStore().get(i);
            if (DmShared.iNull(grd.getSharedKeyFieldCode()).longValue() == DmShared.iNull(ourRow.getSharedKeyFieldCode()).longValue()
                    && DmShared.iNull(grd.getFrequencyCode()).longValue() == DmShared.iNull(ourRow.getFrequencyCode()).longValue()
                    && DmShared.xNull(grd.getDoseText()).trim().equalsIgnoreCase(DmShared.xNull(ourRow.getDoseText()).trim())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (!grdDetail.getGrid().getStore().getAll().contains(ourRow)) {
                grdDetail.getGrid().getStore().add(lastIndex, ourRow);
            }
            grdDetail.commitChanges();
            clear();
            onAdd();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtHMedicine.setFocus(true);
                }
            });
        } else {
            new DmAlertMessageBox("Combination of Medicine Name, Dose and Frequency already exists.", txtHMedicine).show();
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if ((charCode > 31) && (charCode < 48 || charCode > 57) && (charCode != 46) && (charCode != 43)) {
            txtDoseText.cancelKey();
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

    public MedicineAdvise() {
        super();
        srvAsync = new ServiceInvoker<MedicineAdvise_SrvAsync>(GWT.create(MedicineAdvise_Srv.class), MedicineAdvise.class.getName());
        manageEvents();
        textHelpEvents();
        grdFavoriteMedicine.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        grdFavoriteTemplate.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
    }

    private void reQueryMedicine() {
        grdFavoriteMedicine.getHelper().setRowsStartFrom(0);
        grdFavoriteMedicine.getHelper().getFilter().get(0).setValue1(txtDrugSearch.getText());
        grdFavoriteMedicine.reQuery();
    }

    private void reQueryTemplate() {
        grdFavoriteTemplate.getHelper().setRowsStartFrom(0);
        grdFavoriteTemplate.getHelper().getFilter().get(0).setValue1(txtTemplateSearch.getText());
        grdFavoriteTemplate.reQuery();
    }

    private SearchValue_Mdl valueIntialize(boolean isDrug) {
        SearchValue_Mdl value_Mdl = new SearchValue_Mdl();

        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldValue(isDrug ? DmShared.xNull(txtDrugSearch.getText()) : DmShared.xNull(txtTemplateSearch.getText()));
        value_Mdl.setConsultantCode(DmShared.xNull(consultantCode));

        return value_Mdl;
    }

    private void removeFromGrid(int cellIndex, int rowIndex) {
        if (cellIndex == colArrowUp) {
            fillData(grdDetail.getGrid().getStore().get(rowIndex), rowIndex + 1);
            grdDetail.getGrid().getStore().remove(rowIndex);
            grdDetail.commitChanges();
            txtHMedicine.setFocus(true);
        } else if (cellIndex == colArrowLeft) {
            GrdMedicineDetail_Mdl mrTemp = new GrdMedicineDetail_Mdl();
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
                            reQueryMedicine();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtDrugSearch).show();
                        }
                    }
                });
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnCancel).show();
            }
        }
    }

    private void removeFavorites(GrdFavorite_Mdl node) {
        srvAsync.invokeRPC().removeFavorite(consultantCode, DmShared.xNull(node.getSharedKeyFieldCode()), DmShared.xNull(node.getDoseText()), DmShared.xNull(node.getFrequencyCode()), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    reQueryMedicine();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtDrugSearch).show();
                }
            }
        });
    }

    private void manageEvents() {
        lblMedicine.addClickHandler(this);
        lblInstruction.addClickHandler(this);
        lblFrequency.addClickHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnAddToGrid.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtDrugSearch.addKeyUpHandler(this);
        txtDrugSearch.addKeyDownHandler(this);
        txtTemplateSearch.addKeyUpHandler(this);
        txtTemplateSearch.addKeyDownHandler(this);
        txtDoseText.addKeyPressHandler(this);
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdFavoriteMedicine.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdFavoriteMedicine.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdFavoriteMedicine.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdFavoriteMedicine.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdFavoriteMedicine.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtDrugSearch.setText("");
        txtDrugSearch.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdFavoriteMedicine.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdFavoriteMedicine.getDmGrid().getGrid().getView().refresh(true);
        txtDrugSearch.setFocus(true);
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

    private void manageDose(boolean isAddToGrid) {
        try {
            if (DmShared.iNull(txtHMedicine.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Medicine Name is mandatory.", txtHMedicine).show();
                return;
            }
            if (DmShared.iNull(txtHFrequency.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Frequency is mandatory.", txtHFrequency).show();
                return;
            }
            if (DmShared.xNull(txtHFrequency.getTag2nd()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Dose presentation is mandatory.", txtHFrequency).show();
                return;
            }
            if (DmShared.xNull(txtDoseText.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Dose is mandatory.", txtDoseText).show();
                return;
            }
            if (DmShared.xNull(txtDoseText.getText()).trim().contains("+")) {
                txtDoseText.setTag(DmShared.xNull(txtDoseText.getText()));
            } else {
                txtDoseText.setTag(DmShared.xNull(txtHFrequency.getTag2nd()).trim().replace("1", DmShared.xNull(txtDoseText.getText())));
            }

            if (isDoseCorrect(txtDoseText.getTag())) {
                String dosePresentation[] = null, doseValue[] = null;
                dosePresentation = txtHFrequency.getTag2nd().split("\\+");
                doseValue = txtDoseText.getTag().split("\\+");

                if (dosePresentation == null) {
                    new DmAlertMessageBox("Dose Presentation is mandatory.", txtDoseText).show();
                    return;
                } else if (doseValue == null) {
                    new DmAlertMessageBox("Dose is mandatory.", txtDoseText).show();
                    return;
                } else if (dosePresentation.length != doseValue.length) {
                    new DmAlertMessageBox("Dose presentation not matched with given presentation (" + DmShared.xNull(txtHFrequency.getTag2nd()) + ").", txtDoseText).show();
                    return;
                } else {
                    for (int i = 0; i < dosePresentation.length; i++) {
                        if ((DmShared.vNull(dosePresentation[i]).doubleValue() == 0.0 && DmShared.vNull(doseValue[i]).doubleValue() != 0.0)
                                || (DmShared.vNull(dosePresentation[i]).doubleValue() != 0.0 && DmShared.vNull(doseValue[i]).doubleValue() == 0.0)) {
                            new DmAlertMessageBox("Dose presentation not matched with given presentation (" + DmShared.xNull(txtHFrequency.getTag2nd()) + ").", txtDoseText).show();
                            return;
                        }
                    }
                }

                txtDoseText.setTag2nd(getDosePresentation(txtDoseText.getTag()));
            } else {
                return;
            }

            if (isAddToGrid) {
                addToGrid();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtDoseText).show();
        }
    }

    private String getDoseValueNumeric(String doseText) {
        String doseValue = "";
        boolean isDifferent = false;

        try {
            String dosePresentation[] = null;
            dosePresentation = DmShared.xNull(doseText).trim().split("\\+");
            if (dosePresentation != null && dosePresentation.length > 0) {
                /*=====================< Get first non zero value >======================*/
                for (String str : dosePresentation) {
                    if (DmShared.vNull(str).doubleValue() != 0.0) {
                        doseValue = str;
                        break;
                    }
                }
                /*=====================< Get if all values are similar >======================*/
                for (String str : dosePresentation) {
                    if (DmShared.vNull(str).doubleValue() != 0.0) {
                        if (DmShared.vNull(str).doubleValue() != DmShared.vNull(doseValue).doubleValue()) {
                            isDifferent = true;
                            break;
                        }
                    }
                }
            }
            if ((DmShared.xNull(doseValue).trim().equalsIgnoreCase("")) || isDifferent) {
                doseValue = doseText;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtDoseText).show();
        }

        return doseValue;
    }

    private String getDosePresentation(String val) {
        String returnVal = "";
        String valList[] = null;
        int integerPart = 0;
        double decimalPart = 0;

        try {
            valList = val.split("\\+");

            if (valList != null) {
                for (String str : valList) {
                    integerPart = DmShared.iNull(str).intValue();
                    decimalPart = DmShared.vNull(str).doubleValue() - integerPart;
                    decimalPart = DmShared.vNull(DmClient.vNull(decimalPart, "0.00")).doubleValue();

                    if (decimalPart == 0.0) {
                        if (returnVal.trim().equalsIgnoreCase("")) {
                            returnVal = String.valueOf(integerPart);
                        } else {
                            returnVal += " + " + String.valueOf(integerPart);
                        }
                    } else {
                        if (decimalPart == 0.25 || decimalPart == 0.5 || decimalPart == 0.33 || decimalPart == 0.75) {
                            if (returnVal.trim().equalsIgnoreCase("")) {
                                returnVal = (integerPart == 0 ? "" : String.valueOf(integerPart)) + getDecimalValueDisplay(decimalPart);
                            } else {
                                returnVal += " + " + (integerPart == 0 ? "" : String.valueOf(integerPart)) + getDecimalValueDisplay(decimalPart);
                            }
                        } else {
                            throw new UnsupportedOperationException("Wrong input value.");
                        }
                    }

                }
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException(DmShared.xNull(e.getMessage()));
        }

        return returnVal;
    }

    private String getDecimalValueDisplay(double val) throws Exception {
        String returnVal = "";
        try {
            val = DmShared.vNull(DmClient.vNull(val, "0.00")).doubleValue();
            if (val == 0.25) {
                return "¼";
            } else if (val == 0.33) {
                return "⅓";
            } else if (val == 0.5) {
                return "½";
            } else if (val == 0.75) {
                return "¾";
            } else {
                return "";
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtDoseText).show();
        }
        return returnVal;
    }

    private double getDoseValue(String expression) {
        double returnVal = 0;

        try {
            if (DmShared.xNull(expression).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Expression can't be empty.", txtDoseText).show();
            } else {
                returnVal = DmShared.compute(expression);
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtDoseText).show();
        }

        return returnVal;
    }

    private boolean isDoseCorrect(String expression) {
        boolean flag = false;

        try {
            if (DmShared.compute(expression) != 0) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtDoseText).show();
        }

        return flag;
    }

    private void clear() {
        txtHMedicine.setTag("");
        txtHMedicine.setText("");
        txtComposition.setTag("");
        txtComposition.setText("");
        txtDoseText.setTag("");
        txtDoseText.setTag2nd("");
        txtDoseText.setText("");
        txtHFrequency.setTag("");
        txtHFrequency.setTag2nd("");
        txtHFrequency.setText("");
        txtDays.setText("");
        txtHDuration.setTag("");
        txtHDuration.setText("");
        txtRemark.setText("");
        txtSNo.setText("");
        txtHInstruction.setTag("");
        txtHInstruction.setTag2nd("");
        txtHInstruction.setText("");
    }

    private void onAdd() {
        txtDays.setText("1");
        txtHDuration.setTag("0");
        txtHDuration.setText("Day");
        txtSNo.setText(DmShared.iNull(grdDetail.getGrid().getStore().getAll().size() + 1).toString());
    }

    private void getMedicineTemplate(String docId) {
        srvAsync.invokeRPC().getMedicineViaTemplate(docId, new AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicineDetail_Mdl>> result) {
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
