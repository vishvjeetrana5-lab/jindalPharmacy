package dataman.company.self.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class Company_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {

//            strPath = FrequencyMaster.class.getName().trim().replace(".", "/");
//            dyn = sce.getServletContext().addServlet(strPath, FrequencyMaster_SrvImpl.class);
//            dyn.addMapping("/" + strPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
