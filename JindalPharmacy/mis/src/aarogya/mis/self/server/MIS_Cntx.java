package aarogya.mis.self.server;

import aarogya.mis.graphicalDashboard.client.GraphicalDashBoard;
import aarogya.mis.graphicalDashboard.server.GraphicalDashBoard_SrvImpl;
import aarogya.mis.reports.client.*;
import aarogya.mis.reports.server.*;
import aarogya.mis.standardReports.client.*;
import aarogya.mis.standardReports.server.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class MIS_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {

            strPath = RptDateWiseCollection.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDateWiseCollection_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptConsultantSharing.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptConsultantSharing_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIPD_UnbilledServiceReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIPD_UnbilledServiceReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptNetRevenueRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptNetRevenueRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptCollectionRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptCollectionRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptEmergencyStatistics.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptEmergencyStatistics_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptBirthdayAnniv.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBirthdayAnniv_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GraphicalDashBoard.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GraphicalDashBoard_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPatientLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPatientLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPatientSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPatientSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            //===============================<Standard Reports>==============================================   
            strPath = RptAreaWiseConsultation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptAreaWiseConsultation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptCaseWiseConsultation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptCaseWiseConsultation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptCasteWiseConsultation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptCasteWiseConsultation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptCategoryWiseServiceReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptCategoryWiseServiceReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptDepartmentWiseConsultation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDepartmentWiseConsultation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptConsultationReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptConsultationReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptOPDServiceCancellation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptOPDServiceCancellation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptOPDServicesReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptOPDServicesReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptLabCancellationReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLabCancellationReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptRoomChange.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptRoomChange_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptDischargePatientDues.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDischargePatientDues_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIPDServicesReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIPDServicesReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIPDRefundReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIPDRefundReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptDischargeReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDischargeReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptLabDiscountReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLabDiscountReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptLabDuesReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLabDuesReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptLabRefundReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLabRefundReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptAreaWiseLabRegistrationReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptAreaWiseLabRegistrationReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptOpdDiscountReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptOpdDiscountReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptOPDRefundReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptOPDRefundReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptCollectionBoyReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptCollectionBoyReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptConsultantWiseConsultationReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptConsultantWiseConsultationReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPackageWiseInvestigationReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPackageWiseInvestigationReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptTaskExecutionReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptTaskExecutionReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptUnBilledOPDOrganizationReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptUnBilledOPDOrganizationReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptRoomCategoryWiseIPDReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptRoomCategoryWiseIPDReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIPDDiscountReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIPDDiscountReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptModificationHistory.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptModificationHistory_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptLogSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLogSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptUHIDRegistration.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptUHIDRegistration_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPatientWalletLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPatientWalletLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIPDServiceCancellation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIPDServiceCancellation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptControlAccountsDetails.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptControlAccountsDetails_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = RptPOSPaymentCollection.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPOSPaymentCollection_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptCancelServiceRefundReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptCancelServiceRefundReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
