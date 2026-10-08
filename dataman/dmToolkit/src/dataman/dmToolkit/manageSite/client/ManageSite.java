package dataman.dmToolkit.manageSite.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmToolkit.manageSite.shared.GridSite_Mdl;
import dataman.dmToolkit.manageSite.shared.ManageSite_Srv;
import dataman.dmToolkit.manageSite.shared.ManageSite_SrvAsync;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class ManageSite extends ManageSite_Dsg implements ClickHandler, AttachEvent.Handler, DmRootEvents {

    private final ServiceInvoker<ManageSite_SrvAsync> SrvAsync;
    private String tableName, keyFieldName, keyFieldValue, siteFieldName;

    public interface CallBack {

        public void onBeforeSaving(List<GridSite_Mdl> siteCodes);
    }

    public CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            saveSite();
        } else if (event.getSource().equals(btnClose)) {
            hide();
        }
    }

    public void hide() {
        pnl.hide();
    }

    public void show(String tableName, String keyFieldName, String keyFieldValue, String siteFieldName) {
        this.tableName = tableName;
        this.keyFieldName = keyFieldName;
        this.keyFieldValue = keyFieldValue;
        this.siteFieldName = siteFieldName;
        bindGrid();
        ((CellSelectionModel) grdSiteList.getGrid().getSelectionModel()).selectCell(0, 1);
        pnl.show();
        pnl.setGlassEnabled(true);
        pnl.center();
        txtHSiteList.getDmGrid().getGrid().getStore().clear();

    }

    private void gridEvents() {
        grdSiteList.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                try {
                    if (event.getColumnIndex() != 2) {
                        txtHSiteList.getDmGrid().getGrid().getStore().clear();
                        txtHSiteList.show(((GridView) grdSiteList.getGrid().getView()).getCell(event.getRowIndex(), event.getColumnIndex()).getAbsoluteLeft(), ((GridView) grdSiteList.getGrid().getView()).getCell(event.getRowIndex(), event.getColumnIndex()).getAbsoluteTop(), "", "", "");
                    } else if (event.getColumnIndex() == 2) {
                        delete(event.getRowIndex());
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdSiteList.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                        int rowIndex = grdSiteList.getGrid().getStore().indexOf(grdSiteList.getGrid().getSelectionModel().getSelectedItem());
                        int columnIndex = ((CellSelectionModel) grdSiteList.getGrid().getSelectionModel()).getSelectCell().getCell();
                        if (columnIndex != 2) {
                            txtHSiteList.getDmGrid().getGrid().getStore().clear();
                            txtHSiteList.show(((GridView) grdSiteList.getGrid().getView()).getCell(rowIndex, columnIndex).getAbsoluteLeft(), ((GridView) grdSiteList.getGrid().getView()).getCell(rowIndex, columnIndex).getAbsoluteTop(), "", "", "");
                        } else if (columnIndex == 2) {
                            delete(rowIndex);
                        }
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) grdSiteList.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<Company>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<Company> event) {
            }
        });
    }

    private void bindGrid() {
        SrvAsync.invokeRPC().getSiteList(DmShared.xNull(tableName), DmShared.xNull(keyFieldName), DmShared.xNull(keyFieldValue), DmShared.xNull(siteFieldName), new AsyncCallback<DmResult<List<GridSite_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GridSite_Mdl>> result) {
                if (result.isStatus()) {
                    grdSiteList.getGrid().getStore().clear();
                    grdSiteList.getGrid().getStore().addAll(result.getData());
                    grdSiteList.getGrid().getStore().add(new GridSite_Mdl());
                    ((CellSelectionModel) grdSiteList.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        });

    }

    private void saveSite() {
        List<GridSite_Mdl> grd = grdSiteList.getGrid().getStore().getAll();
        List<GridSite_Mdl> lstNew = new ArrayList<>();
        for (int i = 0; i < grd.size(); i++) {
            GridSite_Mdl tmpmdl = new GridSite_Mdl();
            if (!DmShared.xNull(grd.get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                tmpmdl.setSharedKeyFieldCode(DmShared.xNull(grd.get(i).getSharedKeyFieldCode()));
                tmpmdl.setSharedKeyFieldName(DmShared.xNull(grd.get(i).getSharedKeyFieldName()));
                lstNew.add(tmpmdl);
            }
        }
        if (callBack == null) {
            saveRecord(lstNew);
        } else {
            callBack.onBeforeSaving(lstNew);
        }

    }

    public void saveSites(List<GridSite_Mdl> lstNew) {
        saveRecord(lstNew);

    }

    private void saveRecord(List<GridSite_Mdl> lstNew) {
        SrvAsync.invokeRPC().saveRecord(lstNew, DmShared.xNull(tableName), DmShared.xNull(keyFieldName), DmShared.xNull(keyFieldValue), DmShared.xNull(siteFieldName), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    hide();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void delete(final int rowIndex) {
        String codeValue = DmShared.xNull(grdSiteList.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode());
        if (!DmShared.xNull(codeValue).equalsIgnoreCase("")) {
            grdSiteList.getGrid().getStore().remove(rowIndex);
            grdSiteList.commitChanges();
            ((CellSelectionModel) grdSiteList.getGrid().getSelectionModel()).selectCell(0, 1);
//            SrvAsync.invokeRPC().deleteSiteList(DmShared.xNull(tableName), DmShared.xNull(keyFieldName), DmShared.xNull(keyFieldValue), DmShared.xNull(siteFieldName), DmShared.xNull(grdSiteList.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode()), new AsyncCallback<DmResult<String>>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//                }
//
//                @Override
//                public void onSuccess(DmResult<String> result) {
//                    if (result.isStatus()) {
//                     
//                    } else {
//                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
//                    }
//                }
//            });
        }
    }

    private String getSiteCodeList() {
        String list = "";
        List<GridSite_Mdl> grd = grdSiteList.getGrid().getStore().getAll();
        for (int i = 0; i < grd.size(); i++) {
            if (!DmShared.xNull(grd.get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                if (list.equalsIgnoreCase("")) {
                    list = grd.get(i).getSharedKeyFieldCode();
                } else {
                    list = list + "," + grd.get(i).getSharedKeyFieldCode();
                }
            }
        }
        return list;
    }

    private boolean siteExist(String siteCode) {
        boolean val = false;
        List<GridSite_Mdl> grd = grdSiteList.getGrid().getStore().getAll();
        for (int i = 0; i < grd.size(); i++) {
            GridSite_Mdl get = grd.get(i);
            if (DmShared.xNull(siteCode).equalsIgnoreCase(DmShared.xNull(grd.get(i).getSharedKeyFieldCode()))) {
                val = true;
            }
        }
        return val;
    }

    private void txtHelpEvents() {

        txtHSiteList.addHelpGridMultiCallBack(new DmHelpGridMulti.HelpGridMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSiteList(helper, blnToEndOfFile, DmShared.xNull(getSiteCodeList()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught
                    ) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result
                    ) {
                        if (result.isStatus()) {
                            txtHSiteList.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (!DmShared.xNull(keyCode).equalsIgnoreCase("")) {
                    String[] codeList = keyCode.split(",");
                    String[] nameList = keyName.split(",");
                    List<GridSite_Mdl> grd = grdSiteList.getGrid().getStore().getAll();
                    List<GridSite_Mdl> lstNew = new ArrayList<>();
                    for (int i = 0; i < grd.size(); i++) {
                        GridSite_Mdl tmpmdl = new GridSite_Mdl();
                        if (!DmShared.xNull(grd.get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                            tmpmdl.setSharedKeyFieldCode(DmShared.xNull(grd.get(i).getSharedKeyFieldCode()));
                            tmpmdl.setSharedKeyFieldName(DmShared.xNull(grd.get(i).getSharedKeyFieldName()));
                            lstNew.add(tmpmdl);
                        }

                    }
                    for (int i = 0; i < codeList.length; i++) {
                        if (!siteExist(DmShared.xNull(codeList[i]))) {
                            GridSite_Mdl tmpmdl = new GridSite_Mdl();
                            tmpmdl.setSharedKeyFieldCode(DmShared.xNull(codeList[i]));
                            tmpmdl.setSharedKeyFieldName(DmShared.xNull(nameList[i]));
                            lstNew.add(tmpmdl);
                        }
                    }
                    grdSiteList.getGrid().getStore().clear();
                    grdSiteList.getGrid().getStore().addAll(lstNew);
                    grdSiteList.getGrid().getStore().add(new GridSite_Mdl());
                    grdSiteList.commitChanges();
                    ((CellSelectionModel) grdSiteList.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        });
    }

    public ManageSite() {
        super();
        SrvAsync = new ServiceInvoker<ManageSite_SrvAsync>(GWT.create(ManageSite_Srv.class), ManageSite.class.getName());
        gridEvents();
        txtHelpEvents();
        toolContainer.setTitle("Manage Site");
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnDelete.addClickHandler(this);
    }

}
