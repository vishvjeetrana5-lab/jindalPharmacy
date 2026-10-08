package aarogya.pharmacy.medicineSearch.client;

import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.medicineSearch.shared.DefaultValue_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Srv;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_SrvAsync;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.TxtHHsn_Mdl;
import aarogya.pharmacy.stockSummary.shared.SummarySetting_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpGrid.client.DmTextHelpGrid;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;
import java.util.List;

public class MedicineSearch extends MedicineSearch_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, KeyUpHandler, ClickHandler, FocusHandler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    public interface CallBack {

        public void ok(MedicineSearch_Mdl medSearchMdl);

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private final ServiceInvoker<MedicineSearch_SrvAsync> srvAsync;
    private final pmPharmacy pmPharmacy = new pmPharmacy();
    private final BatchNo batchPopup = new BatchNo();
    private int searchColumnIndex;
    private String searchDbFieldName;
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private MedicineSearchFilter_Mdl filter, passedFilter;
    private String selectedMedicineCode, siteCode, siteName;
    private String v_TypeValue = "";
    private boolean isOpenedAsForm = false;
    SetWidgetInvoker swi;

    public MedicineSearch(UserMenu menuPermission, final SetWidgetInvoker swi) {
        super(swi != null ? true : false);
        this.swi = swi;
        srvAsync = new ServiceInvoker<MedicineSearch_SrvAsync>(GWT.create(MedicineSearch_Srv.class), MedicineSearch.class.getName());
        grdMedicineSearch.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        manageEvents();
        textHelpEvents();
        if (swi != null) {
            lblDepartmentName.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
            lblDepartmentName.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
            lblDepartmentName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            lblDepartmentName.getElement().getStyle().setHeight(15, Style.Unit.PX);
            toolContainer.addWidget(lblDepartmentName);
            flwForm.asWidget().setTitle(widgetName);
            isOpenedAsForm = true;
            iniDefault();
        }
    }

    private void iniDefault() {
        srvAsync.invokeRPC(false).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    passedFilter = new MedicineSearchFilter_Mdl();
                    passedFilter.setDepartmentCode(DmShared.iNull(result.getData().getDepartmentCode()).toString());
                    passedFilter.setDepartmentName(DmShared.xNull(result.getData().getDepartmentName()));
                    if (!DmShared.xNull(result.getData().getDepartmentName()).equalsIgnoreCase("")) {
                        lblDepartmentName.setText("Department : " + DmShared.xNull(result.getData().getDepartmentName()));
                    } else {
                        lblDepartmentName.setText("");
                    }
                    passedFilter.setV_Date(DmShared.dNull(result.getData().getCurrentDate()));
                    clear();
                    txtSearchValue.setFocus(true);
                    txtSearchValue.setSelectionRange(DmShared.xNull(txtSearchValue.getText()).length(), 0);
                    reQuery();
                    swi.setWidget(MedicineSearch.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void show(MedicineSearchFilter_Mdl filter, final String typedChar, final String v_Type) {
        v_TypeValue = v_Type;
        this.passedFilter = filter != null ? filter : new MedicineSearchFilter_Mdl();
        isOpenedAsForm = false;
        clear();
        if (filter.getDepartmentCode() != null) {
            flwDeartmentBTNContainer.clear();
        } else if (filter.getDepartmentCode() == null) {
            flwDeartmentBTNContainer.clear();
            flwDeartmentBTNContainer.add(flwDeartmentBTNArea);
            lblDepartmentName.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
            lblDepartmentName.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
            lblDepartmentName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            lblDepartmentName.getElement().getStyle().setHeight(15, Style.Unit.PX);
            toolContainer.addWidget(lblDepartmentName);
        }
        popupPanel.clear();
        popupPanel.add(flwForm);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setText(txtSearchValue.getText() + DmShared.xNull(typedChar).trim().toLowerCase());
                reQuery();
                txtSearchValue.setFocus(true);
                txtSearchValue.setSelectionRange(DmShared.xNull(txtSearchValue.getText()).length(), 0);
            }
        });

    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallBackRequired) {
        if (!isOpenedAsForm) {
            this.popupPanel.hide();
            if (isCallBackRequired) {
                callBack.close();
            }
        } else {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineSearch.this);
        }
    }

    private void textHelpEvents() {
        txtHMedicineMake.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineMake(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineMake.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                filter.setMedicineMakeCode(keyCode);
                filter.setMedicineMakeName(keyName);
                reQuery();
            }
        });
        txtHMedicineCategory.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                filter.setMedicineCategoryCode(keyCode);
                filter.setMedicineCategoryName(keyName);
                reQuery();
            }
        });
        txtHMedicineGroup.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                filter.setMedicineGroupCode(keyCode);
                filter.setMedicineGroupName(keyName);
                reQuery();
            }
        });
        txtHDivision.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDivision(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDivision.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                filter.setDivisionCode(keyCode);
                filter.setDivisionName(keyName);
                reQuery();
            }
        });
        txtHComposition.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHComposition(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHComposition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                filter.setCompositionCode(keyCode);
                filter.setCompositionName(keyName);
                if (!DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) {
                    onSubstituteClick();
                } else {
                    resetGridHeader();
                    reQuery();
                }
            }
        });
        txtHHsn.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHHsn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHHsn_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHHsn_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHHsn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                filter.setHsnCode(keyCode);
                filter.setHsnName(keyName);
                reQuery();
            }
        });
        txtHDepartment.addHelpGridCallBack(new DmTextHelpGrid.HelpGridCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                filter.setDepartmentCode(t1Row.getSharedKeyFieldCode());
                filter.setDepartmentName(t1Row.getSharedKeyFieldName());
                lblDepartmentName.setText("Department : " + DmShared.xNull(t1Row.getSharedKeyFieldName()));
                reQuery();
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(1);

        grdMedicineSearch.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        grdMedicineSearch.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == 8) {
                    setTick(event.getRowIndex());
                    viewMedicine(grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                } else {
                    selectMedicine(grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                }
            }
        });

        grdMedicineSearch.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                MedicineSearch_Mdl msMdl = grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem();

                if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                    if (DmShared.xNull(selectedMedicineCode).equalsIgnoreCase(DmShared.xNull(msMdl.getSharedKeyFieldCode()))) {
                        txtSearchValue.setFocus(true);
                        return;
                    }
                }

                setTick(grdMedicineSearch.getDmGrid().getSelectedCell().getRow());
                viewMedicine(msMdl);

                if (!event.getNativeEvent().getCtrlKey() && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    selectMedicine(grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                }
            }
        }, KeyDownEvent.getType());

        grdMedicineSearch.addCallBack(new DmGridPaging.CallBack<MedicineSearchFilter_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, final MedicineSearchFilter_Mdl filter) {
                srvAsync.invokeRPC(false).getMedicineSearchDetail(helper, blnToEndOfFile, filter, v_TypeValue, new AsyncCallback<DmResultPageing<List<MedicineSearch_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<MedicineSearch_Mdl>> result) {
                        setBtnBackground();
                        if (result.isStatus()) {
                            grdMedicineSearch.setData(result.getData(), result.getPaging());
                            siteCode = result.getValue1();
                            siteName = result.getValue2();

                            boolean flagViewMedicine = false, flagSetSearchValue = false;

                            if (!DmShared.xNull(txtSearchValue.getText()).trim().equals("")) {
                                flagViewMedicine = true;
                            }

                            if (DmShared.iNull(filter.getMedicineCode()).longValue() != 0
                                    && DmShared.xNull(filter.getCompositionCodeCsv()).equalsIgnoreCase("")
                                    && DmShared.xNull(filter.getCompositionCodeWithPowerCsv()).equalsIgnoreCase("")) {
                                MedicineSearch.this.filter.setMedicineCode("");
                                flagViewMedicine = true;
                                flagSetSearchValue = true;
                            }

                            if (flagViewMedicine) {
                                List<MedicineSearch_Mdl> lstMsMdl = grdMedicineSearch.getDmGrid().getGrid().getStore().getAll();
                                if (lstMsMdl != null && lstMsMdl.size() > 0) {
                                    viewMedicine(lstMsMdl.get(0));
                                    if (flagSetSearchValue) {
                                        txtSearchValue.setText(lstMsMdl.get(0).getDisplayName());
                                    }
                                }
                            } else {
                                viewMedicine(null);
                            }

                            txtSearchValue.setFocus(true);
                            txtSearchValue.setSelectionRange(DmShared.xNull(txtSearchValue.getText()).length(), 0);
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSearchValue).show();
                        }
                    }
                });
            }

            @Override
            public MedicineSearchFilter_Mdl getParam() {
                return filter;
            }
        });
    }

    private void setTick(int rowIndex) {
        List<MedicineSearch_Mdl> lstTemp = grdMedicineSearch.getDmGrid().getGrid().getStore().getAll();

        for (MedicineSearch_Mdl msMdl : lstTemp) {
            if (msMdl != null) {
                msMdl.setIsSelected("0");
            }
        }
        if (lstTemp.size() > rowIndex && lstTemp.get(rowIndex) != null) {
            grdMedicineSearch.getDmGrid().getGrid().getStore().get(rowIndex).setIsSelected("1");
            grdMedicineSearch.getDmGrid().commitChanges();
        }

        ((CellSelectionModel) grdMedicineSearch.getDmGrid().getGrid().getSelectionModel()).selectCell(rowIndex, 1);
    }

    private void viewMedicine(MedicineSearch_Mdl msMdl) {
        selectedMedicineCode = null;
        resetLabels();
        if (msMdl != null) {
            selectedMedicineCode = msMdl.getSharedKeyFieldCode();

            lblHsnCode.setText(DmShared.xNull(msMdl.getHsnName()));
            lblHsnCode.setTitle(lblHsnCode.getText());

            lblGstNature.setText(DmShared.xNull(msMdl.getItemGSTNatureName()));
            lblGstNature.setTitle(lblGstNature.getText());

            lblSku.setText(DmShared.xNull(msMdl.getSkuName()));
            lblSku.setTitle(lblSku.getText());

            lblSaleUnit.setText(DmShared.xNull(msMdl.getSaleUnitName()));
            lblSaleUnit.setTitle(lblSaleUnit.getText());

            lblPurchaseUnit.setText(DmShared.xNull(msMdl.getPurchaseUnitName()));
            lblPurchaseUnit.setTitle(lblPurchaseUnit.getText());

            lblPriority.setText(DmShared.xNull(msMdl.getPriority()));
            lblPriority.setTitle(lblPriority.getText());

            lblMarginPer.setText(DmShared.xNull(msMdl.getMarginPercent()));
            lblMarginPer.setTitle(lblMarginPer.getText());

            lblStockSku.setText(DmShared.vNull(msMdl.getCurrentStock()) + " " + DmShared.xNull(msMdl.getSkuName()));
            lblStockSku.setTitle(lblStockSku.getText());

            lblStockSaleUnit.setText(DmShared.vNull(DmClient.vNull(DmShared.vNull(msMdl.getCurrentStock()) / DmShared.vNull(msMdl.getSaleUnitConversionRate()), DmShared.getNumberFormat(DmShared.iNull(msMdl.getSaleUnitDecimalPlaces()).intValue()))) + " " + DmShared.xNull(msMdl.getSaleUnitName()));
            lblStockSaleUnit.setTitle(lblStockSaleUnit.getText());
            if (DmShared.iNull(msMdl.getSaleUnitCode()).longValue() != DmShared.iNull(msMdl.getSkuCode()).longValue()) {
                flwStockSaleUnitContainer.add(flwStockSaleUnit);
            } else {
                flwStockSaleUnitContainer.clear();
            }

            lblStockPurchaseUnit.setText(DmShared.vNull(DmClient.vNull(DmShared.vNull(msMdl.getCurrentStock()) / DmShared.vNull(msMdl.getPurchaseUnitConversionRate()), DmShared.getNumberFormat(DmShared.iNull(msMdl.getPurchaseUnitDecimalPlaces()).intValue()))) + " " + DmShared.xNull(msMdl.getPurchaseUnitName()));
            lblStockPurchaseUnit.setTitle(lblStockPurchaseUnit.getText());
            if (DmShared.iNull(msMdl.getPurchaseUnitCode()).longValue() != DmShared.iNull(msMdl.getSkuCode()).longValue()
                    && DmShared.iNull(msMdl.getPurchaseUnitCode()).longValue() != DmShared.iNull(msMdl.getSaleUnitCode()).longValue()) {
                flwStockPurchaseUnitContainer.add(flwStockPurchaseUnit);
            } else {
                flwStockPurchaseUnitContainer.clear();
            }

            lblMinQty.setText(DmShared.xNull(msMdl.getMinStock()));
            lblMinQty.setTitle(lblMinQty.getText());

            lblMaxQty.setText(DmShared.xNull(msMdl.getMaxStock()));
            lblMaxQty.setTitle(lblMaxQty.getText());

            lblReOrder.setText(DmShared.xNull(msMdl.getReOrder()));
            lblReOrder.setTitle(lblReOrder.getText());

            lblBinName.setText(DmShared.xNull(msMdl.getBinName()));
            lblBinName.setTitle(lblBinName.getText());

            lblGroup.setText(DmShared.xNull(msMdl.getGroupName()));
            lblGroup.setTitle(lblGroup.getText());

            lblRestrictedGroup.setText(DmShared.xNull(msMdl.getRestrictedGroupName()));
            lblRestrictedGroup.setTitle(lblRestrictedGroup.getText());

            lblDivision.setText(DmShared.xNull(msMdl.getDivisionName()));
            lblDivision.setTitle(lblDivision.getText());

            lblMrp.setText(DmShared.xNull(msMdl.getMrp()));
            lblMrp.setTitle(lblMrp.getText());

            lblSaleRate.setText(DmShared.xNull(msMdl.getSaleRate()));
            lblSaleRate.setTitle(lblSaleRate.getText());

            lblPurchaseRate.setText(DmShared.xNull(msMdl.getPurchaseRate()));
            lblPurchaseRate.setTitle(lblPurchaseRate.getText());

            lblSaleDiscountPer.setText(DmShared.xNull(msMdl.getSaleDiscountPer()));
            lblSaleDiscountPer.setTitle(lblSaleDiscountPer.getText());

            lblPurchaseDiscountPer.setText(DmShared.xNull(msMdl.getPurchaseDiscountPer()));
            lblPurchaseDiscountPer.setTitle(lblPurchaseDiscountPer.getText());

            lblPurchaseScheme.setText(DmShared.xNull(msMdl.getSchemeOnQty()) + "+" + DmShared.xNull(msMdl.getSchemeFreeQty()));
            lblPurchaseScheme.setTitle(lblPurchaseScheme.getText());

            lblItsPurchase.setText(DmShared.xNull(msMdl.getItsPurchase()));
            lblItsPurchase.setTitle(lblItsPurchase.getText());

            lblManufacturer.setText(DmShared.xNull(msMdl.getManufacturerName()));
            lblManufacturer.setTitle(lblManufacturer.getText());

            txtComposition.setText(msMdl.getCompositionNameWithPowerCsv());
            txtInstruction.setText(msMdl.getInstruction());
            txtRegionalInstruction.setText(msMdl.getRegionalInstruction());

        }
    }

    private void selectMedicine(MedicineSearch_Mdl msMdl) {
        if (!isOpenedAsForm) {
            if (msMdl != null) {
                popupPanel.hide();
                callBack.ok(msMdl);
            } else {
                txtSearchValue.setFocus(true);
            }
        }
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdMedicineSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdMedicineSearch.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdMedicineSearch.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdMedicineSearch.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdMedicineSearch.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : grdMedicineSearch.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdMedicineSearch.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        grdMedicineSearch.getHelper().setRowsStartFrom(0);
        grdMedicineSearch.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdMedicineSearch.reQuery();
    }

    private void setBtnBackground() {
        btnSelectCompany.getElement().getStyle().setBackgroundColor(DmShared.xNull(filter.getMedicineMakeCode()).equalsIgnoreCase("") ? "#f8f8f8" : "#ffcdd2");
        btnSelectCompany.getElement().getStyle().setBorderColor(DmShared.xNull(filter.getMedicineMakeCode()).equalsIgnoreCase("") ? "#ccc" : "#f7b0b7");

        btnSelectCategory.getElement().getStyle().setBackgroundColor(DmShared.xNull(filter.getMedicineCategoryCode()).equalsIgnoreCase("") ? "#f8f8f8" : "#ffcdd2");
        btnSelectCategory.getElement().getStyle().setBorderColor(DmShared.xNull(filter.getMedicineCategoryCode()).equalsIgnoreCase("") ? "#ccc" : "#f7b0b7");

        btnSelectGroup.getElement().getStyle().setBackgroundColor(DmShared.xNull(filter.getMedicineGroupCode()).equalsIgnoreCase("") ? "#f8f8f8" : "#ffcdd2");
        btnSelectGroup.getElement().getStyle().setBorderColor(DmShared.xNull(filter.getMedicineGroupCode()).equalsIgnoreCase("") ? "#ccc" : "#f7b0b7");

        btnSelectDivision.getElement().getStyle().setBackgroundColor(DmShared.xNull(filter.getDivisionCode()).equalsIgnoreCase("") ? "#f8f8f8" : "#ffcdd2");
        btnSelectDivision.getElement().getStyle().setBorderColor(DmShared.xNull(filter.getDivisionCode()).equalsIgnoreCase("") ? "#ccc" : "#f7b0b7");

        btnSubstitute.getElement().getStyle().setBackgroundColor(DmShared.xNull(filter.getCompositionCodeCsv()).equalsIgnoreCase("") ? "#f8f8f8" : "#ffcdd2");
        btnSubstitute.getElement().getStyle().setBorderColor(DmShared.xNull(filter.getCompositionCodeCsv()).equalsIgnoreCase("") ? "#ccc" : "#f7b0b7");

        btnSelectComposition.getElement().getStyle().setBackgroundColor(DmShared.xNull(filter.getCompositionCode()).equalsIgnoreCase("") ? "#f8f8f8" : "#ffcdd2");
        btnSelectComposition.getElement().getStyle().setBorderColor(DmShared.xNull(filter.getCompositionCode()).equalsIgnoreCase("") ? "#ccc" : "#f7b0b7");

        btnSelectHsn.getElement().getStyle().setBackgroundColor(DmShared.xNull(filter.getHsnCode()).equalsIgnoreCase("") ? "#f8f8f8" : "#ffcdd2");
        btnSelectHsn.getElement().getStyle().setBorderColor(DmShared.xNull(filter.getHsnCode()).equalsIgnoreCase("") ? "#ccc" : "#f7b0b7");

        btnSelectDepartment.getElement().getStyle().setBackgroundColor(DmShared.iNull(filter.getDepartmentCode()).longValue() == 0 ? "#f8f8f8" : "#ffcdd2");
        btnSelectDepartment.getElement().getStyle().setBorderColor(DmShared.iNull(filter.getDepartmentCode()).longValue() == 0 ? "#ccc" : "#f7b0b7");
    }

    private void clear() {
        resetFilter();
        resetLabels();
        resetGridHeader();
        selectedMedicineCode = "";
        txtSearchValue.setText("");
        txtSearchValue.setTag("");
        txtSearchValue.setTag2nd("");
        grdMedicineSearch.getDmGrid().getGrid().getStore().clear();
        txtSearchValue.setFocus(true);

    }

    private void resetLabels() {
        lblHsnCode.setText("");
        lblHsnCode.setTitle("");

        lblGstNature.setText("");
        lblGstNature.setTitle("");

        lblSku.setText("");
        lblSku.setTitle("");

        lblSaleUnit.setText("");
        lblSaleUnit.setTitle("");

        lblPurchaseUnit.setText("");
        lblPurchaseUnit.setTitle("");

        lblMarginPer.setText("");
        lblMarginPer.setTitle("");

        lblPriority.setText("");
        lblPriority.setTitle("");

        lblStockSku.setText("");
        lblStockSku.setTitle("");
        flwStockSaleUnitContainer.clear();

        lblStockSaleUnit.setText("");
        lblStockSaleUnit.setTitle("");
        flwStockPurchaseUnitContainer.clear();

        lblStockPurchaseUnit.setText("");
        lblStockPurchaseUnit.setTitle("");

        lblMinQty.setText("");
        lblMinQty.setTitle("");

        lblMaxQty.setText("");
        lblMaxQty.setTitle("");

        lblReOrder.setText("");
        lblReOrder.setTitle("");

        lblBinName.setText("");
        lblBinName.setTitle("");

        lblGroup.setText("");
        lblGroup.setTitle("");

        lblRestrictedGroup.setText("");
        lblRestrictedGroup.setTitle("");

        lblDivision.setText("");
        lblDivision.setTitle("");

        lblMrp.setText("");
        lblMrp.setTitle("");

        lblSaleRate.setText("");
        lblSaleRate.setTitle("");

        lblPurchaseRate.setText("");
        lblPurchaseRate.setTitle("");

        lblSaleDiscountPer.setText("");
        lblSaleDiscountPer.setTitle("");

        lblPurchaseDiscountPer.setText("");
        lblPurchaseDiscountPer.setTitle("");

        lblPurchaseScheme.setText("");
        lblPurchaseScheme.setTitle("");

        lblPurchaseScheme.setText("");
        lblPurchaseScheme.setTitle("");

        lblItsPurchase.setText("");
        lblItsPurchase.setTitle("");

        lblManufacturer.setText("");
        lblManufacturer.setTitle("");

        txtComposition.setText("");
        txtInstruction.setText("");
        txtRegionalInstruction.setText("");
    }

    private void resetFilter() {
        filter = new MedicineSearchFilter_Mdl();
        filter.setV_Date(passedFilter.getV_Date());

        if (!DmShared.xNull(passedFilter.getDepartmentName()).equalsIgnoreCase("")) {
            lblDepartmentName.setText("Department : " + DmShared.xNull(passedFilter.getDepartmentName()));
        } else {
            lblDepartmentName.setText("");
        }

        filter.setDepartmentCode(passedFilter.getDepartmentCode());
        filter.setDepartmentName(passedFilter.getDepartmentName());
        filter.setMedicineMakeCode(passedFilter.getMedicineMakeCode());
        filter.setMedicineMakeName(passedFilter.getMedicineMakeName());
        filter.setMedicineCategoryCode(passedFilter.getMedicineCategoryCode());
        filter.setMedicineCategoryName(passedFilter.getMedicineCategoryName());
        filter.setMedicineGroupCode(passedFilter.getMedicineGroupCode());
        filter.setMedicineGroupName(passedFilter.getMedicineGroupName());
        filter.setDivisionCode(passedFilter.getDivisionCode());
        filter.setDivisionName(passedFilter.getDivisionName());
        filter.setCompositionCode(passedFilter.getCompositionCode());
        filter.setCompositionName(passedFilter.getCompositionName());
        filter.setHsnCode(passedFilter.getHsnCode());
        filter.setHsnName(passedFilter.getHsnName());
        filter.setCompositionCodeCsv(passedFilter.getCompositionCodeCsv());
        filter.setMedicineCode(passedFilter.getMedicineCode());
        passedFilter.setMedicineCode("");
        filter.setFlagInStock(cbInStock.getValue());
        filter.setItemCodeCsv(passedFilter.getItemCodeCsv());
    }

    private void resetGridHeader() {
        DmColumnConfig dmColumnConfig = (DmColumnConfig) grdMedicineSearch.getDmGrid().getGrid().getColumnModel().getColumn(0);

        if (!DmShared.xNull(filter.getCompositionCodeWithPowerCsv()).trim().equals("")
                || !DmShared.xNull(filter.getCompositionCode()).trim().equals("")) {
            dmColumnConfig.setHidden(false);
        } else {
            dmColumnConfig.setHidden(true);
        }

        if (grdMedicineSearch.getDmGrid().getGrid().getView().getHeader() != null) {
            grdMedicineSearch.getDmGrid().getGrid().getView().getHeader().refresh();
        }
    }

    private void showMedicineMaster() {
//        if (DmShared.iNull(selectedMedicineCode).longValue() != 0) {
        HashMap paramHashMap = new HashMap();
        paramHashMap.put("flagOpenAsPopup", true);
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(pmPharmacy.moduleName, pmPharmacy.MEDICINE_MASTER.caption, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                ((IsWidgetExtended) wgtObject).searchRecord(selectedMedicineCode);
            }
        });
//        }
    }

    private void showExpiryReport() {
        HashMap paramHashMap = new HashMap();
        paramHashMap.put("flagOpenAsPopup", true);
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(pmPharmacy.moduleName, pmPharmacy.MEDICINE_EXPIRY_REGISTER.caption, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {

            }
        });
    }

    private void showStockLedger() {
        if (DmShared.iNull(selectedMedicineCode).longValue() != 0) {
            HashMap paramHashMap = new HashMap();
            paramHashMap.put("flagOpenAsPopup", true);
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(pmPharmacy.moduleName, pmPharmacy.STOCK_SUMMARY_DISPLAY.caption, paramHashMap, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    MedicineSearch_Mdl msMdl = grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
                    SummarySetting_Mdl summarySetting_Mdl = new SummarySetting_Mdl();
                    summarySetting_Mdl.setFromDate(DmClient.getStartDate());
                    summarySetting_Mdl.setToDate(DmClient.getLoginDate());
                    summarySetting_Mdl.setGroupBy("Department");
                    summarySetting_Mdl.setValueOn("Landed Rate");
                    summarySetting_Mdl.setStockStatus("Negative Stock,Positive Stock");
                    summarySetting_Mdl.setMedicineMakeCode(msMdl.getMakeCode());
                    summarySetting_Mdl.setMedicineMakeName(msMdl.getMakeName());
                    summarySetting_Mdl.setMedicineCatCode(msMdl.getCategoryCode());
                    summarySetting_Mdl.setMedicineCatName(msMdl.getCategoryName());
                    summarySetting_Mdl.setMedicineGroupCode(msMdl.getGroupCode());
                    summarySetting_Mdl.setMedicineGroupName(msMdl.getGroupName());
                    summarySetting_Mdl.setMedicineCode(msMdl.getSharedKeyFieldCode());
                    summarySetting_Mdl.setMedicineName(msMdl.getSharedKeyFieldName());
                    summarySetting_Mdl.setDepartmentCode(filter.getDepartmentCode());
                    summarySetting_Mdl.setDepartmentName(filter.getDepartmentName());
                    summarySetting_Mdl.setSiteCode(DmShared.xNull(siteCode));
                    summarySetting_Mdl.setSiteName(DmShared.xNull(siteName));
                    ((IsWidgetExtended) wgtObject).action("PRE_FILTERED_SUMMARY", summarySetting_Mdl);
                }
            });
        }
    }

    private void onSubstituteClick() {
        MedicineSearch_Mdl msMdl = grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem();

        if (msMdl != null && !DmShared.xNull(msMdl.getCompositionCodeWithPowerCsv()).trim().equals("")
                && DmShared.xNull(filter.getCompositionCode()).trim().equals("")
                && DmShared.xNull(filter.getCompositionCodeWithPowerCsv()).equals("")) {
            filter.setCompositionCodeWithPowerCsv(msMdl.getCompositionCodeWithPowerCsv());
            filter.setCompositionCodeCsv(msMdl.getCompositionCodeCsv());
            filter.setMedicineCode(msMdl.getSharedKeyFieldCode());
        } else {
            filter.setCompositionCodeWithPowerCsv("");
            filter.setCompositionCodeCsv("");
            filter.setMedicineCode("");
        }

        resetGridHeader();
        reQuery();
    }

    private void onCbInStockClick() {
        filter.setFlagInStock(cbInStock.getValue());
        reQuery();
    }

    private void manageEvents() {
        gridEvents();
        popupPanel.addAttachHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addFocusHandler(this);
        btnClose.addClickHandler(this);
        btnMedicineMaster.addClickHandler(this);
        btnBatches.addClickHandler(this);
        btnStockLedger.addClickHandler(this);
        btnSelectCompany.addClickHandler(this);
        btnSelectCategory.addClickHandler(this);
        btnSelectGroup.addClickHandler(this);
        btnSelectDivision.addClickHandler(this);
        btnExpiryReport.addClickHandler(this);
        btnSubstitute.addClickHandler(this);
        btnSelectComposition.addClickHandler(this);
        btnSelectHsn.addClickHandler(this);
        btnSelectDepartment.addClickHandler(this);
        cbInStock.addClickHandler(this);
        btnClearFilters.addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnMedicineMaster)) {

            showMedicineMaster();

        } else if (event.getSource().equals(btnClose)) {

            hide(true);

        } else if (event.getSource().equals(btnStockLedger)) {

            showStockLedger();

        } else if (event.getSource().equals(btnExpiryReport)) {

            showExpiryReport();

        } else if (event.getSource().equals(btnSubstitute)) {

            onSubstituteClick();

        } else if (event.getSource().equals(btnBatches)) {

            showBatchPopUp();

        } else if (event.getSource().equals(btnSelectCompany)) {

            showMedicineCompany();

        } else if (event.getSource().equals(btnSelectCategory)) {

            showMedicineCategory();

        } else if (event.getSource().equals(btnSelectGroup)) {

            showMedicineGroup();

        } else if (event.getSource().equals(btnSelectDivision)) {

            showMedicineDivision();

        } else if (event.getSource().equals(btnSelectComposition)) {

            showMedicineComposition();

        } else if (event.getSource().equals(btnSelectHsn)) {

            showHSN();

        } else if (event.getSource().equals(btnSelectDepartment)) {
            showDepartment();

        } else if (event.getSource().equals(cbInStock)) {

            onCbInStockClick();

        } else if (event.getSource().equals(btnClearFilters)) {

            resetFilter();
            reQuery();

        }
    }

    private void showBatchPopUp() {
        if (DmShared.iNull(selectedMedicineCode).longValue() != 0) {
            batchPopup.show(selectedMedicineCode, filter.getDepartmentCode(), filter.getV_Date(), null, ItemBatchType.NON_EXPIRED_BATCH, false, "", null, "", false);
        }
    }

    private void showMedicineCompany() {
        txtHMedicineMake.show(btnSelectCompany, "", DmShared.xNull(filter.getMedicineMakeCode()), DmShared.xNull(filter.getMedicineMakeName()));
    }

    private void showMedicineCategory() {
        txtHMedicineCategory.show(btnSelectCategory, "", DmShared.xNull(filter.getMedicineCategoryCode()), DmShared.xNull(filter.getMedicineCategoryName()));
    }

    private void showMedicineGroup() {
        txtHMedicineGroup.show(btnSelectGroup, "", DmShared.xNull(filter.getMedicineGroupCode()), DmShared.xNull(filter.getMedicineGroupName()));
    }

    private void showMedicineDivision() {
        txtHDivision.show(btnSelectDivision, "", DmShared.xNull(filter.getDivisionCode()), DmShared.xNull(filter.getDivisionName()));
    }

    private void showMedicineComposition() {
        txtHComposition.show(btnSelectComposition, "", DmShared.xNull(filter.getCompositionCode()), DmShared.xNull(filter.getCompositionName()));
    }

    private void showHSN() {
        txtHHsn.show(btnSelectHsn, "", DmShared.xNull(filter.getHsnCode()), DmShared.xNull(filter.getHsnName()));
    }

    private void showDepartment() {
        txtHDepartment.show(btnSelectDepartment, "");
    }

    @Override
    public void onFocus(FocusEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            selectedMedicineCode = null;
            grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().deselectAll();
            resetLabels();
//            reQuery();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            selectedMedicineCode = null;

            switch (event.getNativeEvent().getKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    if (!DmShared.xNull(txtSearchValue.getText()).trim().equals("")) {
                        List<MedicineSearch_Mdl> lstMsMdl = grdMedicineSearch.getDmGrid().getGrid().getStore().getAll();
                        if (lstMsMdl != null && lstMsMdl.size() > 0) {
                            selectMedicine(lstMsMdl.get(0));
                        }
                    }
                    break;
                case KeyCodes.KEY_DOWN:
                    setTick(0);
                    viewMedicine(grdMedicineSearch.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    break;
                default:
                    if (event.getNativeKeyCode() == KeyCodes.KEY_INSERT || event.getNativeKeyCode() == KeyCodes.KEY_UP
                            || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_LEFT
                            || event.getNativeKeyCode() == KeyCodes.KEY_ALT || event.getNativeKeyCode() == KeyCodes.KEY_CTRL
                            || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                            || event.isAltKeyDown() || event.isControlKeyDown() || event.isShiftKeyDown()) {

                    } else {
                        reQuery();
                    }
                    break;
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
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
            onSubstituteClick();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_M) {
            if (cbInStock.getValue()) {
                cbInStock.setValue(Boolean.FALSE);
            } else {
                cbInStock.setValue(Boolean.TRUE);
            }
            onCbInStockClick();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_M) {
            showMedicineMaster();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_B) {
            showBatchPopUp();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S) {
            showStockLedger();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_E) {
            showExpiryReport();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_C) {
            showMedicineCompany();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
            showMedicineCategory();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_G) {
            showMedicineGroup();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_D) {
            showMedicineDivision();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_N) {
            showMedicineComposition();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_H) {
            showHSN();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_T) {
            showDepartment();
        }
    }

    @Override
    public Widget asWidget() {
        if (isOpenedAsForm) {
            return flwForm;
        } else {
            return popupPanel;
        }
    }

}
