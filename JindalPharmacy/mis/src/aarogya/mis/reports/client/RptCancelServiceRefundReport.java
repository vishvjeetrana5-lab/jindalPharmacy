package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptCancelServiceRefundReport_Srv;
import aarogya.mis.reports.shared.RptCancelServiceRefundReport_SrvAsync;
import aarogya.mis.reports.shared.TxtHCardReg_Mdl;
import aarogya.mis.reports.shared.TxtHReg_Mdl;
import aarogya.mis.standardReports.client.*;
import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmMIS;
import aarogya.selfToolkit.patientSearch.client.PatientSearch;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptCancelServiceRefundReport extends DmReportLayout implements IsWidget, BlurHandler {

    public final String widgetName = new pmMIS().CANCEL_SERVICE_REFUND_REPORT.caption;
    private final ServiceInvoker<RptCancelServiceRefundReport_SrvAsync> srvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();

    private final DmTextBox txtFromDate = new DmTextBox(11, true);
    private final DmTextBox txtToDate = new DmTextBox(11, true);
    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final DmTextBox txtUHID = new DmTextBox(20, true);
    private final Button btnUHID = new Button("...");
    private final PatientSearch uhidSearch = new PatientSearch();
    
    //<editor-fold defaultstate="collapsed" desc="txtHModule">
   DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Module", 1,"");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    
        //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHConsultant.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    
    interface TxtHDivision_PrpAcs extends KeyField_T_PrpAcs<TxtHReg_Mdl> {

        @Editor.Path("UhidNo")
        ValueProvider<TxtHReg_Mdl, String> uhidNo();

        @Editor.Path("Mobile")
        ValueProvider<TxtHReg_Mdl, String> mobile();

        @Editor.Path("PatientName")
        ValueProvider<TxtHReg_Mdl, String> personName();
    }
    private final DmTextHelpMulti<TxtHReg_Mdl, TxtHDivision_PrpAcs> txtHReg = new DmTextHelpMulti(GWT.create(TxtHDivision_PrpAcs.class), 10, false);

    private void iniTxtHReg() {
        txtHReg.getDmGrid().addTextColumn(txtHReg.getDmGrid().getPropertyAccess().name(), "Reg No", 100, "regNo");
        txtHReg.getDmGrid().addTextColumn(txtHReg.getDmGrid().getPropertyAccess().uhidNo(), "Uhid No", 100, "uhidName");
      //  txtHReg.getDmGrid().addTextColumn(txtHReg.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        txtHReg.getDmGrid().addTextColumn(txtHReg.getDmGrid().getPropertyAccess().personName(), "Patient Name", 100, "patientName");
        txtHReg.getDmGrid().initializeGrid(txtHReg.getDmGrid().getPropertyAccess().code(), 500,0 ,2);
       // txtHReg.getHelper().addSort("regNo", DmEnum.DmSortType.ASC);
       txtHReg.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
    }
    

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(getFilter(), reportProperty, widgetName,txtHModule.getText(), new AsyncCallback<DmResult<String>>() {

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
    
     private void txtHelpEvents() {
            txtHModule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
            
     txtHConsultant.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
     
     
            txtHReg.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHReg_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRegNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHReg_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReg).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHReg_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHReg.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReg).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }


    public RptCancelServiceRefundReport(SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<RptCancelServiceRefundReport_SrvAsync>(GWT.create(RptCancelServiceRefundReport_Srv.class), RptCancelServiceRefundReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        initialize();
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        swi.setWidget(this);
        txtHelpEvents();
        btnUHID.addClickHandler(this);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize() {
        
        FlowPanel flwTd, flwBtn;
        HorizontalPanel hrtPanel;

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());
        
//        flwTd = new FlowPanel();
//        flwBtn = new FlowPanel();
//        flwTd.getElement().setAttribute("style", "border: 1px solid lightgray; float:left; overflow: hidden; width:100%;");
//        hrtPanel = new HorizontalPanel();
//        hrtPanel.setHeight("100%");
//        hrtPanel.add(vl.add(txtUHID));
//        hrtPanel.setCellWidth(txtUHID, "100%");
//        btnUHID.getElement().setAttribute("style", "width: 20px; border:none; border-left:1px solid gray; float:left; padding-top: 0px; height:18px;");
//        flwBtn.add(btnUHID);
//        hrtPanel.add(flwBtn);
//        hrtPanel.setCellWidth(flwBtn, "1px");
//        flwTd.add(hrtPanel);
//
//        addControl("uhid", "UHID No.", flwTd, txtUHID);
        
        iniTxtHReg();
        addControl("regDocId", "Patient Registration", vl.add(txtHReg));
        
        iniTxtHModule();
        addControl("module", "Module", vl.add(txtHModule));
        txtHModule.setText("OPD");
        txtHModule.setTag(pvc.OPD+"','"+pvc.EMERGENCY_OPD+"','"+pvc.PAY_CLINIC);
        
        iniTxtHConsultant();
        addControl("consultant", "Consultant", vl.add(txtHConsultant));
        
      
     

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }
    
    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (event.getSource().equals(btnUHID)) {
            uhidSearch.show(true, SearchValue_Mdl.FilterType.WITHOUT_EXPIRED);
        }
    }

}
