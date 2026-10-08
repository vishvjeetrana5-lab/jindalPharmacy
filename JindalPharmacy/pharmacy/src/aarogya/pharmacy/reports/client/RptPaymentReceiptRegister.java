/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.RptPaymentReceiptRegister_Srv;
import aarogya.pharmacy.reports.shared.RptPaymentReceiptRegister_SrvAsync;
import aarogya.pharmacy.reports.shared.TxtPartyName_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptPaymentReceiptRegister extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().PAYMENT_RECIPT_REGISTER.caption;
    ServiceInvoker<RptPaymentReceiptRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    interface TxtHSite_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }

    DmTextHelpMulti<KeyFieldExtended, TxtHSite_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(TxtHSite_PrpAcs.class), 5, false);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site", 200, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPartyName">
    interface TxtHPartyName_PrpAcs extends KeyField_T_PrpAcs<TxtPartyName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtPartyName_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtPartyName_Mdl, String> cityName();
    }

    final DmTextHelpMulti<TxtPartyName_Mdl, TxtHPartyName_PrpAcs> txtHPartyName = new DmTextHelpMulti(GWT.create(TxtHPartyName_PrpAcs.class), 6, false);

    private void iniTxtHPartyName() {
        txtHPartyName.getDmGrid().addTextColumn(txtHPartyName.getDmGrid().getPropertyAccess().name(), "Name", 0, "partyName");
        txtHPartyName.getDmGrid().addTextColumn(txtHPartyName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHPartyName.getDmGrid().addTextColumn(txtHPartyName.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHPartyName.getDmGrid().initializeGrid(txtHPartyName.getDmGrid().getPropertyAccess().code(), 550, 0, 2);
        txtHPartyName.getHelper().addSort("partyName", DmEnum.DmSortType.ASC);
        txtHPartyName.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUserName">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHUserName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHUserName() {
        txtHUserName.getDmGrid().addTextColumn(txtHUserName.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        txtHUserName.getDmGrid().initializeGrid(txtHUserName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHUserName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void txtHelpEvents() {
        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHPartyName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtPartyName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPartyName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtPartyName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPartyName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtPartyName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPartyName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPartyName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

    }

    public RptPaymentReceiptRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptPaymentReceiptRegister_Srv.class), RptPaymentReceiptRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefaultValue();
        initialize(swi);
    }

    private void initialize(SetWidgetInvoker swi) {

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHSite();
        addControl("site", "Site", vl.add(txtHSite));

        iniTxtHPartyName();
        addControl("partyName", "Party Name", vl.add(txtHPartyName));

        swi.setWidget(this);

    }

    private void setDefaultValue() {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended keyField = result.getData();
                    txtHSite.setTag(keyField.getSharedKeyFieldCode());
                    txtHSite.setText(keyField.getSharedKeyFieldName());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    getReport(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public Widget asWidget() {
        return this.form;
    }

}
