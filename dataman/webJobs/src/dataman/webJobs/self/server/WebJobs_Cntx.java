package dataman.webJobs.self.server;

import dataman.webJobs.emailManager.client.EmailManager;
import dataman.webJobs.emailManager.server.EmailManager_SrvImpl;
import dataman.webJobs.emailTemplateMast.client.EmailTemplateMast;
import dataman.webJobs.emailTemplateMast.server.EmailTemplateMast_SrvImpl;
import dataman.webJobs.environmentSetting.client.EnvironmentSetting;
import dataman.webJobs.environmentSetting.server.EnvironmentSetting_SrvImpl;
import dataman.webJobs.notificationEntry.client.NotificationEntry;
import dataman.webJobs.notificationEntry.server.NotificationEntry_SrvImpl;
import dataman.webJobs.scheduler.client.Scheduler;
import dataman.webJobs.scheduler.server.Scheduler_SrvImpl;
import dataman.webJobs.smsManager.client.SmsManager;
import dataman.webJobs.smsManager.server.SmsManager_SrvImpl;
import dataman.webJobs.smsTemplate.client.SmsTemplate;
import dataman.webJobs.smsTemplate.server.SmsTemplate_SrvImpl;
import dataman.webJobs.staffInfo.client.StaffInfo;
import dataman.webJobs.staffInfo.server.StaffInfo_SrvImpl;
import dataman.webJobs.userEmailConfiguration.client.UserEmailConfiguration;
import dataman.webJobs.userEmailConfiguration.server.UserEmailConfiguration_SrvImpl;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebJobs_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = Scheduler.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Scheduler_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EmailTemplateMast.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EmailTemplateMast_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EnvironmentSetting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnvironmentSetting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SmsTemplate.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SmsTemplate_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SmsManager.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SmsManager_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EmailManager.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EmailManager_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = UserEmailConfiguration.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, UserEmailConfiguration_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = StaffInfo.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StaffInfo_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = NotificationEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NotificationEntry_SrvImpl.class);
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
