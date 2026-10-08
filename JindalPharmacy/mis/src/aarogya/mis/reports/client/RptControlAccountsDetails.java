package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptControlAccountsDetails_Srv;
import aarogya.mis.reports.shared.RptControlAccountsDetails_SrvAsync;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;

import aarogya.projectMenu.self.shared.pmOPD;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;
import aarogya.projectMenu.self.shared.pmMIS;


public class RptControlAccountsDetails extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmMIS().CONTROL_ACCOUNTS_DETAILS_REPORT.caption;
    private final ServiceInvoker<RptControlAccountsDetails_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(11, true);
    private final DmTextBox txtToDate = new DmTextBox(11, true);

  



    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHControl = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void initxtHCity() {
        txtHControl.getDmGrid().addTextColumn(txtHControl.getDmGrid().getPropertyAccess().name(),"Control Account", 1, "name");
        txtHControl.getDmGrid().initializeGrid(txtHControl.getDmGrid().getPropertyAccess().code(), 200, 0, 2);

        txtHControl.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>


 


    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<String>>() {

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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } 
        else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    //filters
    private void txtHelpEvents() {

        txtHControl.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).gettxtHControlAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHControl).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHControl.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHControl).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });
       
       
    
    }

    public RptControlAccountsDetails(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<RptControlAccountsDetails_SrvAsync>(GWT.create(RptControlAccountsDetails_Srv.class), RptControlAccountsDetails.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    // initialize method add the controls to the FlowPanel.
    // here fieldCode should match table column name.
    private void initialize(SetWidgetInvoker swi) {
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

       

        initxtHCity();
        addControl("name", "Control Account", vl.add(txtHControl));

       

       

        swi.setWidget(this);
    }

}
