package dataman.dmBase.dmSession.server;

import javax.servlet.http.HttpServletRequest;

public class DmRSSPublic extends DmRSS {

    @Override
    public HttpServletRequest getLocalRequest() {
        try {
            DmSession session = (DmSession) super.getThreadLocalRequest().getSession();

            if (session == null) {
                ((DmResponseWrapper) this.getThreadLocalResponse()).setDatamanSessionExpired(true);

            } else if (!DmSession.validateSession(session.getId(), super.getThreadLocalRequest())) {
                ((DmResponseWrapper) this.getThreadLocalResponse()).setDatamanSessionExpired(true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return super.getThreadLocalRequest();
    }

}
