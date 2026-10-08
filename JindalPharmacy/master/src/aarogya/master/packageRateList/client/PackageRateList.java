package aarogya.master.packageRateList.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.packageDetail.client.PackageApplicablePopup;
import aarogya.master.packageRateList.shared.PackageRateList_Mdl;
import aarogya.master.packageRateList.shared.GrdPackageDetail_Mdl;
import aarogya.master.packageDetail.client.PackageDetail;
import aarogya.master.packageDetail.client.RoomCategoryDetail;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;
import aarogya.master.packageRateList.shared.PackageRateList_Srv;
import aarogya.master.packageRateList.shared.PackageRateList_SrvAsync;
import aarogya.master.packageRateList.shared.TxtHPackage_Mdl;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public final class PackageRateList extends PackageRateList_Dsg implements AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<PackageRateList_SrvAsync> SrvAsync;
    private PackageDetail packageDetail = new PackageDetail();
    private RoomCategoryDetail roomCategoryDetail = null;
    private RoomCategoryDetail.CallBack roomCallBack;
    private PackageApplicablePopup packageApplicablePopup = new PackageApplicablePopup();
    private String packageCode = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("A", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PackageRateList.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        packageApplicablePopup.addCallBack(new PackageApplicablePopup.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(DmShared.xNull(topControl.getDocId()));
            }
        });
        packageDetail.addCallBack(new PackageDetail.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(DmShared.xNull(topControl.getDocId()));
            }
        });
        roomCallBack = new RoomCategoryDetail.CallBack() {
            @Override
            public void onClose() {
                roomCategoryDetail = null;
                topControlMoveRec(DmShared.xNull(topControl.getDocId()));
            }
        };

        txtHPackage.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPackage_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPackage(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPackage_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPackage).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPackage_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPackage.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPackage).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPackage_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    searchPackage(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                }
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvents() {
        gridPackageDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGridEdit(event.getRowIndex(), event.getColumnIndex());
            }
        });
        gridPackageDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                try {
                    CellSelectionModel<GrdPackageDetail_Mdl> cellModel;
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                        cellModel = (CellSelectionModel<GrdPackageDetail_Mdl>) gridPackageDetail.getGrid().getSelectionModel();
                        showGridEdit(gridPackageDetail.getGrid().getStore().indexOf(gridPackageDetail.getGrid().getSelectionModel().getSelectedItem()), cellModel.getSelectCell().getCell());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGridEdit(int rowIndex, int coloumIndex) {
        GrdPackageDetail_Mdl node = gridPackageDetail.getGrid().getStore().get(rowIndex);

        if (coloumIndex == 8) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                showApplibleDate(node);
            }
        } else if (coloumIndex == 9) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                roomCategoryDetail = new RoomCategoryDetail();
                roomCategoryDetail.addCallBack(roomCallBack);
                roomCategoryDetail.show(node.getSharedKeyFieldCode(), node.getPackageCode(), txtOrganizationName.getTag(), DmShared.dNull(node.getApplicableFrom()), DmShared.dNull(node.getApplicableTo()));
            }
        } else {
            packageDetail.show(node.getSharedKeyFieldCode(), node.getPackageCode(), txtOrganizationName.getTag(), "", "", true, true);
        }
    }

    private void showApplibleDate(final GrdPackageDetail_Mdl node) {
        try {
            SrvAsync.invokeRPC().checkApplicableDate(DmShared.dNull(node.getApplicableFrom()), DmShared.dNull(node.getApplicableTo()), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        if (!DmShared.dNull(result.getData()).equalsIgnoreCase("")) {
                            packageApplicablePopup.show(DmShared.dNull(result.getData()), "31-Dec-9999",
                                    DmShared.xNull(node.getSharedKeyFieldCode()), DmShared.xNull(node.getPackageName()));
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
            e.printStackTrace();
        }
    }

    public PackageRateList(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(PackageRateList_Srv.class), PackageRateList.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        flwForm.addAttachHandler(this);
        textHelpEvents();
        findEvents();
        gridEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PackageRateList_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PackageRateList_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PackageRateList_Mdl> result) {
        PackageRateList_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtOrganizationName.setTag(data.getSharedKeyFieldCode());
            txtOrganizationName.setText(data.getSharedKeyFieldName());
            txtShortName.setText(data.getShortName());

            gridPackageDetail.getGrid().getStore().clear();
            gridPackageDetail.getGrid().getStore().addAll(data.getListPackageDetail());

            ((CellSelectionModel) gridPackageDetail.getGrid().getSelectionModel()).selectCell(0, 1);

            if (DmShared.iNull(packageCode).longValue() != 0) {
                searchPackage(packageCode);
            }
            packageCode = "";
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        txtHPackage.setEnabled(true);
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void action(String organizationCode, Object object) {
        topControlMoveRec(organizationCode);
        packageCode = (String) object;
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
            packageDetail.hide();
        }
    }

    private void searchPackage(String packageCode) {
        for (int i = 0; i < gridPackageDetail.getGrid().getStore().getAll().size(); i++) {
            GrdPackageDetail_Mdl node = gridPackageDetail.getGrid().getStore().getAll().get(i);
            if (DmShared.iNull(node.getPackageCode()).longValue() == DmShared.iNull(packageCode).longValue()) {
                ((CellSelectionModel) gridPackageDetail.getGrid().getSelectionModel()).selectCell(i, 1);
                txtHPackage.setTag("");
                txtHPackage.setText("");
                break;
            }
        }
    }

}
