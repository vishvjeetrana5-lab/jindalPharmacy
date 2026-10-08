package dataman.dmToolkitR.dmStatic.server;

import dataman.dmToolkitR.dmFileServlets.DmFileDownload_Srvlt;
import dataman.dmToolkitR.dmFileServlets.DmFileUpload_Srvlt;
import dataman.dmToolkitR.dmFileServlets.DmImageFileThumbnail_Srvlt;
import dataman.dmToolkitR.dmGUI.client.DmGUI;
import dataman.dmToolkitR.dmGUI.server.DmGUI_SrvImpl;
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
//            strPath = DmGUI.class.getName().trim().replace(".", "/");
//            dyn = sce.getServletContext().addServlet(strPath, DmGUI_SrvImpl.class);
//            dyn.addMapping("/" + strPath);
//
//            strPath = "dataman/dmToolkitR/dmFileServlets/DmFileUpload_Srvlt";
//            dyn = sce.getServletContext().addServlet(strPath, DmFileUpload_Srvlt.class);
//            dyn.addMapping("/" + strPath);
//
//            strPath = "dataman/dmToolkitR/dmFileServlets/DmFileDownload_Srvlt";
//            dyn = sce.getServletContext().addServlet(strPath, DmFileDownload_Srvlt.class);
//            dyn.addMapping("/" + strPath);
//
//            strPath = "dataman/dmToolkitR/dmFileServlets/DmImageFileThumbnail_Srvlt";
//            dyn = sce.getServletContext().addServlet(strPath, DmImageFileThumbnail_Srvlt.class);
//            dyn.addMapping("/" + strPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
