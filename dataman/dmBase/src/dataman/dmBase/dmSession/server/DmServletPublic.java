package dataman.dmBase.dmSession.server;

import dataman.dmBase.jvmSerialization.shared.DmJsonResponse_Mdl;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

public abstract class DmServletPublic extends DmServlet {

    public abstract void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (validate(request, response)) {
            processRequest(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (validate(request, response)) {
            processRequest(request, response);
        }
    }

    private boolean validate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DmSession session;
        boolean rtn = true;

        try {

            if (request instanceof DmRequestWrapper) {
                session = (DmSession) request.getSession();
            } else {
                session = DmSession.getSessions().get(request.getParameter("u"));
            }

            if (session == null) {
                ((DmResponseWrapper) response).setDatamanSessionExpired(true);
                rtn = false;
            } else if (!DmSession.validateSession(session.getId(), request)) {
                ((DmResponseWrapper) response).setDatamanSessionExpired(true);
                rtn = false;
            }

        } catch (Exception ex) {
            rtn = false;
            ex.printStackTrace();
        }

        if (!rtn) {
            response.setStatus(HttpStatus.OK_200);
        }

        return rtn;
    }
}
