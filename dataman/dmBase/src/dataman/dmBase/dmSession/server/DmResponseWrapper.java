package dataman.dmBase.dmSession.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class DmResponseWrapper extends HttpServletResponseWrapper {

//    private String pathExtension = "";

//    public DmResponseWrapper(HttpServletResponse response) {
//        super(response);
//    }
    public DmResponseWrapper(HttpServletResponse response, String pathExtension) {
        super(response);
//        this.pathExtension = pathExtension;
        IAppServiceResponseHandler respHandler = DmBaseServer.getApplicationServiceHandler(pathExtension);
        if (respHandler != null) {
            respHandler.handleResponse(this);
        }
    }

    public void setDatamanSessionExpired(boolean isExpired) {
        if (isExpired) {
            if (!this.containsHeader("DTMN_SESSION_EXPIRED")) {
                this.addHeader("DTMN_SESSION_EXPIRED", "true");
            }
        }
    }

    public void setDatamanSessionLock(boolean isLock) {
        if (isLock) {
            if (!this.containsHeader("DTMN_SESSION_LOCK")) {
                this.addHeader("DTMN_SESSION_LOCK", "true");
            }
        }
    }

}
