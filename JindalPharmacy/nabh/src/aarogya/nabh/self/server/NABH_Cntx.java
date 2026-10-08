package aarogya.nabh.self.server;

import aarogya.nabh.roomHelp.client.RoomHelp;
import aarogya.nabh.roomHelp.server.RoomHelp_SrvImpl;
import aarogya.nabh.nabhNavigation.client.NABHNavigation;
import aarogya.nabh.nabhNavigation.server.NABHNavigation_SrvImpl;
import aarogya.nabh.patientInfectionEntry.client.PatientInfectionEntry;
import aarogya.nabh.patientInfectionEntry.server.PatientInfectionEntry_SrvImpl;
import aarogya.nabh.reports.client.*;
import aarogya.nabh.reports.server.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class NABH_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = NABHNavigation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NABHNavigation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientInfectionEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientInfectionEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = NABHIndicatorsSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NABHIndicatorsSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RoomHelp.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RoomHelp_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator10.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator10_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator13.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator13_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator27.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator27_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator28.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator28_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator29.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator29_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator30.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator30_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator6.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator6_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator61.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator61_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndicator62.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndicator62_SrvImpl.class);
            dyn.addMapping("/" + strPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
