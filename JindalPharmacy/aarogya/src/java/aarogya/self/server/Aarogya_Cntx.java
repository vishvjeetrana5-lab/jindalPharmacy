package aarogya.self.server;

import aarogya.common.client.Common;
import aarogya.common.server.Common_SrvImpl;
import aarogya.company.client.Company;
import aarogya.company.server.Company_SrvImpl;
import aarogya.initializer.client.Initializer;
import aarogya.initializer.server.Initializer_SrvImpl;
import aarogya.localSetting.client.LocalSetting;
import aarogya.localSetting.server.LocalSetting_SrvImpl;
import aarogya.login.client.Login;
import aarogya.login.server.Login_SrvImpl;
import aarogya.plt.client.PLT;
import aarogya.plt.server.PLT_SrvImpl;
import aarogya.ribbon.client.Ribbon;
import aarogya.ribbon.server.DmSkypeIni_Srvlt;
import aarogya.ribbon.server.Ribbon_SrvImpl;
import aarogya.site.client.Site;
import aarogya.site.server.Site_SrvImpl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import redirect.changePassword.client.ChangePassword;
import redirect.changePassword.server.ChangePassword_SrvImpl;

@WebListener
public class Aarogya_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = Common.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Common_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Login.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Login_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PLT.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PLT_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Company.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Company_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Site.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Site_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Ribbon.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Ribbon_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ChangePassword.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ChangePassword_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LocalSetting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LocalSetting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Initializer.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Initializer_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = "aarogya/ribbon/server/DmSkypeIni_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmSkypeIni_Srvlt.class);
            dyn.addMapping("/" + strPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DmServer.getInitializeINI(sce, "jimsPharmacyConfig");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
