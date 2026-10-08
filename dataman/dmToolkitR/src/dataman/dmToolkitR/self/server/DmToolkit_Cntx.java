package dataman.dmToolkitR.self.server;

import dataman.dmToolkitR.dmFileManager.client.DmFileManager;
import dataman.dmToolkitR.dmFileManager.server.DmFileManager_SrvImpl;
import dataman.dmToolkitR.dmFileServlets.DmFileDownload_Srvlt;
import dataman.dmToolkitR.dmFileServlets.DmFileUpload_Srvlt;
import dataman.dmToolkitR.dmFileServlets.DmImageFileThumbnail_Srvlt;
import dataman.dmToolkitR.dmImageCanvas.client.DmImageCanvas;
import dataman.dmToolkitR.dmImageCanvas.server.DmImageCanvas_SrvImpl;
import dataman.dmToolkitR.dmReportLayout.server.DmReportLayout_Srvlt;
import dataman.dmToolkitR.dmReportLayout.shared.DmReportLayout_SrvAsync;
import dataman.dmToolkitR.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkitR.openSourceReport.server.OpenSourceReport_SrvImpl;
import dataman.dmToolkitR.printerMapping.client.PrinterMapping;
import dataman.dmToolkitR.printerMapping.server.PrinterMapping_SrvImpl;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class DmToolkit_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {

            strPath = OpenSourceReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OpenSourceReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DmReportLayout_SrvAsync.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmReportLayout_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = DmFileManager.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmFileManager_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DmImageCanvas.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmImageCanvas_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkitR/dmFileServlets/DmFileUpload_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFileUpload_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkitR/dmFileServlets/DmFileDownload_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFileDownload_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkitR/dmFileServlets/DmImageFileThumbnail_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmImageFileThumbnail_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = PrinterMapping.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PrinterMapping_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkitR/self/server/DmPACSetup_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmPACSetup_Srvlt.class);
            dyn.addMapping("/" + strPath);
            
            strPath = "dataman/dmToolkitR/self/server/DmPlugin_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmPlugin_Srvlt.class);
            dyn.addMapping("/" + strPath);
            
             strPath = "dataman/dmToolkitR/self/server/DmPACUpdateSetup_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmPACUpdateSetup_Srvlt.class);
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
