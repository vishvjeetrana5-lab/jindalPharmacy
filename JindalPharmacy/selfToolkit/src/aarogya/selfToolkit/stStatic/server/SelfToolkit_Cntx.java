package aarogya.selfToolkit.stStatic.server;

import aarogya.projectMenu.servlet.shared.MachineInterfaceConfig_SrvAsync;
import aarogya.projectMenu.servlet.shared.MachineInterfaceData_SrvAsync;
import aarogya.projectMenu.servlet.shared.RadiologyDocumentsRequest_SrvAsync;
import aarogya.projectMenu.servlet.shared.RadiologyDocumentsUpload_SrvAsync;
import aarogya.selfToolkit.allergy.client.Allergy;
import aarogya.selfToolkit.allergy.server.Allergy_SrvImpl;
import aarogya.selfToolkit.appointmentSearch.client.AppointmentSearch;
import aarogya.selfToolkit.appointmentSearch.server.AppointmentSearch_SrvImpl;
import aarogya.selfToolkit.cardPaymentPopup.client.CardPaymentPopup;
import aarogya.selfToolkit.cardPaymentPopup.server.CardPaymentPopup_SrvImpl;
import aarogya.selfToolkit.claimIdPopup.client.ClaimIdPopup;
import aarogya.selfToolkit.claimIdPopup.server.ClaimIdPopup_SrvImpl;
import aarogya.selfToolkit.customMultiLine.client.CustomMultiLine;
import aarogya.selfToolkit.customMultiLine.server.CustomMultiLine_SrvImpl;
import aarogya.selfToolkit.discountPopup.client.DiscountPopup;
import aarogya.selfToolkit.discountPopup.server.DiscountPopup_SrvImpl;
import aarogya.selfToolkit.fifoPopUp.client.FifoPopUp;
import aarogya.selfToolkit.fifoPopUp.server.FifoPopUp_SrvImpl;
import aarogya.selfToolkit.investigationAdvise.client.InvestigationAdvise;
import aarogya.selfToolkit.investigationAdvise.server.InvestigationAdvise_SrvImpl;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.server.ItemPropertyPopup_SrvImpl;
import aarogya.selfToolkit.labRequisition.client.LabRequisition;
import aarogya.selfToolkit.labRequisition.server.LabRequisition_SrvImpl;
import aarogya.selfToolkit.medicineAdvise.client.MedicineAdvise;
import aarogya.selfToolkit.medicineAdvise.server.MedicineAdvise_SrvImpl;
import aarogya.selfToolkit.medicineSubstituteSearch.client.MedicineSubstituteSearch;
import aarogya.selfToolkit.medicineSubstituteSearch.server.MedicineSubstituteSearch_SrvImpl;
import aarogya.selfToolkit.mi.server.MachineInterfaceConfiguration_Srvlt;
import aarogya.selfToolkit.mi.server.MachineInterfaceData_Srvlt;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_SrvImpl;
import aarogya.selfToolkit.opdQueueDisplay.client.OPDQueueDisplay;
import aarogya.selfToolkit.opdQueueDisplay.server.OPDQueueDisplay_SrvImpl;
import aarogya.selfToolkit.opdSearch.client.OPDSearch;
import aarogya.selfToolkit.opdSearch.server.OPDSearch_SrvImpl;
import aarogya.selfToolkit.organizationPopup.client.OrganizationPopup;
import aarogya.selfToolkit.organizationPopup.server.OrganizationPopup_SrvImpl;
import aarogya.selfToolkit.patientRequisition.client.PatientRequisition;
import aarogya.selfToolkit.patientRequisition.server.PatientRequisition_SrvImpl;
import aarogya.selfToolkit.patientSchedule.client.PatientSchedule;
import aarogya.selfToolkit.patientSchedule.server.PatientSchedule_SrvImpl;
import aarogya.selfToolkit.patientSearch.client.PatientSearch;
import aarogya.selfToolkit.patientSearch.server.PatientSearch_SrvImpl;
import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.server.PatientSearchRoomWise_SrvImpl;
import aarogya.selfToolkit.pharmacyDiscountPopup.client.PharmacyDiscountPopup;
import aarogya.selfToolkit.pharmacyDiscountPopup.server.PharmacyDiscountPopup_SrvImpl;
import aarogya.selfToolkit.prescriptionCustomItem.client.PrescriptionCustomItem;
import aarogya.selfToolkit.prescriptionCustomItem.server.PrescriptionCustomItem_SrvImpl;
import aarogya.selfToolkit.radiologyDocuments.server.RadiologyDocumentsDownload_Srvlt;
import aarogya.selfToolkit.radiologyDocuments.server.RadiologyDocumentsUpload_Srvlt;
import aarogya.selfToolkit.patientSchedulePopup.client.PatientSchedulePopup;
import aarogya.selfToolkit.patientSchedulePopup.server.PatientSchedulePopup_SrvImpl;
import aarogya.selfToolkit.permanentAddressPopup.client.PermanentAddressPopup;
import aarogya.selfToolkit.permanentAddressPopup.server.PermanentAddressPopup_SrvImpl;
import aarogya.selfToolkit.splApplicableFrom.client.SPLApplicableFrom;
import aarogya.selfToolkit.splApplicableFrom.server.SPLApplicableFrom_SrvImpl;
import aarogya.selfToolkit.splApplicableWithOption.client.SPLApplicableWithOption;
import aarogya.selfToolkit.splApplicableWithOption.server.SPLApplicableWithOption_SrvImpl;
import aarogya.selfToolkit.iplApplicableWithOption.client.IPLApplicableWithOption;
import aarogya.selfToolkit.iplApplicableWithOption.server.IPLApplicableWithOption_SrvImpl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.uhidDuplicacy.client.UHIDDuplicacy;
import aarogya.selfToolkit.uhidDuplicacy.server.UHIDDuplicacy_SrvImpl;
import aarogya.selfToolkit.uhidPopup.client.UhidPopup;
import aarogya.selfToolkit.uhidPopup.server.UhidPopup_SrvImpl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.serviceGrid.server.ServiceGrid_SrvImpl;
import aarogya.selfToolkit.stockDetailPopup.client.StockDetailPopup;
import aarogya.selfToolkit.stockDetailPopup.server.StockDetailPopup_SrvImpl;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class SelfToolkit_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = AppointmentSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AppointmentSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientSchedule.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientSchedule_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CardPaymentPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CardPaymentPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientSearchRoomWise.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientSearchRoomWise_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SPLApplicableFrom.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SPLApplicableFrom_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SPLApplicableWithOption.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SPLApplicableWithOption_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IPLApplicableWithOption.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IPLApplicableWithOption_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = ModifyReason.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ModifyReason_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OPDQueueDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OPDQueueDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CustomMultiLine.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CustomMultiLine_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OrganizationPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OrganizationPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = UHIDDuplicacy.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, UHIDDuplicacy_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientRequisition.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientRequisition_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = UhidPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, UhidPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LabRequisition.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LabRequisition_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OPDSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OPDSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DiscountPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DiscountPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PharmacyDiscountPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PharmacyDiscountPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = STClient.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, STClient_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemPropertyPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemPropertyPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineAdvise.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineAdvise_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Allergy.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Allergy_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = InvestigationAdvise.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, InvestigationAdvise_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ClaimIdPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ClaimIdPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PrescriptionCustomItem.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PrescriptionCustomItem_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MachineInterfaceData_SrvAsync.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MachineInterfaceData_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = MachineInterfaceConfig_SrvAsync.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MachineInterfaceConfiguration_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = FifoPopUp.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FifoPopUp_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RadiologyDocumentsRequest_SrvAsync.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RadiologyDocumentsDownload_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = RadiologyDocumentsUpload_SrvAsync.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RadiologyDocumentsUpload_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientSchedulePopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientSchedulePopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineSubstituteSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineSubstituteSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PermanentAddressPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PermanentAddressPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = ServiceGrid.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ServiceGrid_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = StockDetailPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StockDetailPopup_SrvImpl.class);
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
