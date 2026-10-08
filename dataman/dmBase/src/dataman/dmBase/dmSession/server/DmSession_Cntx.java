package dataman.dmBase.dmSession.server;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DmSession_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sc) {

        try {

            FilterRegistration.Dynamic df = sc.getServletContext().addFilter("DmBase/DmSession", DmRequestFilter.class);
            df.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
            sc.getServletContext().addListener(DmSessionListener.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
