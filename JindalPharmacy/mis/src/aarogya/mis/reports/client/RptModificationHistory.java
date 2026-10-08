
package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptModificationHistory_Srv;
import aarogya.mis.reports.shared.RptModificationHistory_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
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


public class RptModificationHistory extends DmReportLayout implements IsWidget, BlurHandler{
    
    private final String widgetName = new pmMIS().MODIFICATION_HISTORY.caption;
    private final ServiceInvoker<RptModificationHistory_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);
    
    //<editor-fold defaultstate="collapsed" desc="txtHReason">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHReason = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHReason() {
        txtHReason.getDmGrid().addTextColumn(txtHReason.getDmGrid().getPropertyAccess().name(), "Reason", 1, "Name");
        txtHReason.getDmGrid().initializeGrid(txtHReason.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHReason.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHModifiedBy">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHModifiedBy = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHModifiedBy() {
        txtHModifiedBy.getDmGrid().addTextColumn(txtHModifiedBy.getDmGrid().getPropertyAccess().name(), "Modified By", 1, "Name");
        txtHModifiedBy.getDmGrid().initializeGrid(txtHModifiedBy.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHModifiedBy.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHMode">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMode = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMode() {
        txtHMode.getDmGrid().addTextColumn(txtHMode.getDmGrid().getPropertyAccess().name(), "Modified By", 1, "Name");
        txtHMode.getDmGrid().initializeGrid(txtHMode.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMode.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
         srvAsync.invokeRPC(true).execute(getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<String>>() {

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
    
    private void txtHelpEvents() {
        
         txtHReason.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
             @Override
             public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                 srvAsync.invokeRPC(blnCallBackWait).getTxtHReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                     @Override
                     public void onFailure(Throwable caught) {
                         new DmAlertMessageBox(caught.getMessage(), txtHReason).show();
                     }

                     @Override
                     public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                         if(result.isStatus()) {
                             txtHReason.setData(result.getData(), result.getPaging());
                         }
                         else {
                             new DmAlertMessageBox(result.getErrorMessage(), txtHReason).show();
                         }
                     
                     }
                 });
             }

             @Override
             public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                    
             }
         });
         txtHModifiedBy.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
             @Override
             public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                 srvAsync.invokeRPC(blnCallBackWait).getTxtHModifiedBy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                     @Override
                     public void onFailure(Throwable caught) {
                         new DmAlertMessageBox(caught.getMessage(), txtHModifiedBy).show();
                     }

                     @Override
                     public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                         if(result.isStatus()) {
                             txtHModifiedBy.setData(result.getData(), result.getPaging());
                         }
                         else {
                             new DmAlertMessageBox(result.getErrorMessage(), txtHModifiedBy).show();
                         }
                     
                     }
                 });
             }

             @Override
             public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                    
             }
         });
         txtHMode.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
             @Override
             public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                 srvAsync.invokeRPC(blnCallBackWait).getTxtHMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                     @Override
                     public void onFailure(Throwable caught) {
                         new DmAlertMessageBox(caught.getMessage(), txtHMode).show();
                     }

                     @Override
                     public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                         if(result.isStatus()) {
                             txtHMode.setData(result.getData(), result.getPaging());
                         }
                         else {
                             new DmAlertMessageBox(result.getErrorMessage(), txtHMode).show();
                         }
                     
                     }
                 });
             }

             @Override
             public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                    
             }
         });
         
    }
    
    public RptModificationHistory(SetWidgetInvoker swi){
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(RptModificationHistory_Srv.class), RptModificationHistory.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        setDefaultValue();
        txtHelpEvents();
        initialize(swi);
    }
    private void initialize(SetWidgetInvoker swi){
        
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getLoginDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());
        
        iniTxtHReason();
        addControl("reason", "Reason", vl.add(txtHReason));
        
        iniTxtHModifiedBy();
        addControl("u_Name", "Modified By", vl.add(txtHModifiedBy));
        
        iniTxtHMode();
        addControl("mode", "Mode", vl.add(txtHMode));
        
        swi.setWidget(this);
    }
    
    private void setDefaultValue(){
        txtHMode.setTag("E");
        txtHMode.setText("Edit");
    }
}
