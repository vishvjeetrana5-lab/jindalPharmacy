package dataman.utility.self.server;

import dataman.dmBase.self.shared.DmEnum.DataBackUp;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.utility.companyMaster.client.CompanyMaster;
import dataman.utility.companyMaster.server.CompanyMaster_SrvImpl;
import dataman.utility.siteMaster.client.SiteMaster;
import dataman.utility.siteMaster.server.SiteMaster_SrvImpl;
import dataman.utility.branchMaster.client.BranchMaster;
import dataman.utility.branchMaster.server.BranchMaster_SrvImpl;
import dataman.utility.changePassword.client.ChangePassword;
import dataman.utility.changePassword.server.ChangePassword_SrvImpl;
import dataman.utility.copyUserPermission.client.CopyUserPermission;
import dataman.utility.copyUserPermission.server.CopyUserPermission_SrvImpl;
import dataman.utility.dataBackup.client.DataBackup;
import dataman.utility.dataBackup.server.*;
import dataman.utility.docIdMaster.client.DocIdMaster;
import dataman.utility.docIdMaster.server.DocIdMaster_SrvImpl;
import dataman.utility.exportProcedure.client.ExportProcedure;
import dataman.utility.exportProcedure.server.ExportProcedure_SrvImpl;
import dataman.utility.fileTypeMaster.client.FileTypeMaster;
import dataman.utility.fileTypeMaster.server.FileTypeMaster_SrvImpl;
import dataman.utility.guiConfig.client.GUIConfig;
import dataman.utility.guiConfig.server.GUIConfig_SrvImpl;
import dataman.utility.importProcedure.client.ImportProcedure;
import dataman.utility.importProcedure.server.ImportProcedure_SrvImpl;
import dataman.utility.gstStructure.client.GstStructure;
import dataman.utility.gstStructure.server.GstStructure_SrvImpl;
import dataman.utility.itemTaxStructure.client.ItemTaxStructure;
import dataman.utility.itemTaxStructure.server.ItemTaxStructure_SrvImpl;
import dataman.utility.menuCodification.client.MenuCodification;
import dataman.utility.menuCodification.server.MenuCodification_SrvImpl;
import dataman.utility.numberSystem.client.NumberSystem;
import dataman.utility.numberSystem.server.NumberSystem_SrvImpl;
import dataman.utility.overheadConfiguration.client.OverheadConfiguration;
import dataman.utility.overheadConfiguration.server.OverheadConfiguration_SrvImpl;
import dataman.utility.overheadLineConfiguration.client.OverheadLineConfiguration;
import dataman.utility.overheadLineConfiguration.server.OverheadLineConfiguration_SrvImpl;
import dataman.utility.overheadMaster.client.OverheadMaster;
import dataman.utility.overheadMaster.server.OverheadMaster_SrvImpl;
import dataman.utility.productLicensedTo.client.ProductLicensedTo;
import dataman.utility.productLicensedTo.server.ProductLicensedTo_SrvImpl;
import dataman.utility.reports.client.RptDepartmentWiseUserReport;
import dataman.utility.reports.client.RptUserPermission;
import dataman.utility.reports.server.RptDepartmentWiseUserReport_SrvImpl;
import dataman.utility.reports.server.RptUserPermission_SrvImpl;
import dataman.utility.userMaster.client.UserMaster;
import dataman.utility.userMaster.server.UserMaster_SrvImpl;
import dataman.utility.userPermission.client.UserPermission;
import dataman.utility.userPermission.server.UserPermission_SrvImpl;
import dataman.utility.voucherType.client.VoucherType;
import dataman.utility.voucherType.server.VoucherType_SrvImpl;
import dataman.utility.walkThroughMaster.client.WalkThroughMaster;
import dataman.utility.walkThroughMaster.server.WalkThroughMaster_SrvImpl;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class Utility_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = DocIdMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DocIdMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BranchMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BranchMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CompanyMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CompanyMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = NumberSystem.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NumberSystem_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProductLicensedTo.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProductLicensedTo_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SiteMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SiteMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VoucherType.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VoucherType_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = UserMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, UserMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ChangePassword.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ChangePassword_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverheadMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverheadMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverheadConfiguration.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverheadConfiguration_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverheadLineConfiguration.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverheadLineConfiguration_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = UserPermission.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, UserPermission_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CopyUserPermission.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CopyUserPermission_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FileTypeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FileTypeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GUIConfig.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GUIConfig_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemTaxStructure.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemTaxStructure_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GstStructure.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GstStructure_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = WalkThroughMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WalkThroughMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MenuCodification.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MenuCodification_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ExportProcedure.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ExportProcedure_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ImportProcedure.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ImportProcedure_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DataBackup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DataBackup_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = RptUserPermission.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptUserPermission_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = RptDepartmentWiseUserReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDepartmentWiseUserReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            DmServer.getWebServiceSchedulars().addEventHandler(DataBackUp.DB_COMPANY.getValue(), new DBCompany_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(DataBackUp.DB_TRANSACTION.getValue(), new DBTransaction_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(DataBackUp.DB_MONGO.getValue(), new DBMongo_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(DataBackUp.FS_DIR.getValue(), new FSDir_Server());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
