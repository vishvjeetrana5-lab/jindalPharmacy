package dataman.dmBase.dmSession.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class DmRSS extends DmRemoteServiceServlet {

    public abstract HttpServletRequest getLocalRequest();

}
