
package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptUHIDRegistration_Srv;
import aarogya.mis.reports.shared.RptUHIDRegistration_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;


public class RptUHIDRegistration extends DmReportLayout implements IsWidget,BlurHandler{
    
    private final String widgetName = new pmMIS().UHID_REGISTRATION.caption;
    private final ServiceInvoker<RptUHIDRegistration_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox();
    private final DmTextBox txtToDate = new DmTextBox();
    
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHCity = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "cityName", 1, "cityName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHCity.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }
    
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHArea = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHArea() {
        txtHArea.getDmGrid().addTextColumn(txtHArea.getDmGrid().getPropertyAccess().name(), "name", 1, "name");
        txtHArea.getDmGrid().initializeGrid(txtHArea.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHArea.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    
    private void txtEvents(){
        
        txtHCity.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                
            }
        });
        
        txtHArea.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHArea(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHArea).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if(result.isStatus()){
                            txtHArea.setData(result.getData(), result.getPaging());
                        }else{
                            new DmAlertMessageBox(result.getErrorMessage(), txtHArea).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                
            }
        });
        
    }

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC().execute(getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if(result.isStatus()){
                    getReport(result.getData());
                }
                else{
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
        
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }
    
    public RptUHIDRegistration(SetWidgetInvoker swi){
        super();
        SrvAsync= new ServiceInvoker<RptUHIDRegistration_SrvAsync>(GWT.create(RptUHIDRegistration_Srv.class), RptUHIDRegistration.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
    }
    
    private void initialize(SetWidgetInvoker swi){
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getLoginDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());
        
        iniTxtHCity();
        addControl("city", "City", vl.add(txtHCity));
        
        iniTxtHArea();
        addControl("area", "Area", vl.add(txtHArea));
        
        swi.setWidget(this);
    }
    
}
