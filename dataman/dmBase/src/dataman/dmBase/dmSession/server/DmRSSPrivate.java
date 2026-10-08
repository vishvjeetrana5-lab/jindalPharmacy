package dataman.dmBase.dmSession.server;

import dataman.dmBase.self.shared.DmEnum;
import javax.servlet.http.HttpServletRequest;

public class DmRSSPrivate extends DmRSS {

    @Override
    public HttpServletRequest getLocalRequest() {
        HttpServletRequest httpServletRequest = null;
//        long tempDate = new Date().getTime();
        try {
            DmSession session = (DmSession) super.getThreadLocalRequest().getSession();

            if (session == null) {
                ((DmResponseWrapper) this.getThreadLocalResponse()).setDatamanSessionExpired(true);

            } else if (session.getSessionStatus() == null) {
                ((DmResponseWrapper) this.getThreadLocalResponse()).setDatamanSessionExpired(true);

            } else if (session.getSessionStatus() == DmEnum.DmSessionStatus.LOGGED_OUT) {
                ((DmResponseWrapper) this.getThreadLocalResponse()).setDatamanSessionExpired(true);

            } else if (session.getSessionStatus() == DmEnum.DmSessionStatus.LOCK) {
                ((DmResponseWrapper) this.getThreadLocalResponse()).setDatamanSessionLock(true);

            } else if (!DmSession.validateSession(session.getId(), super.getThreadLocalRequest())) {
                ((DmResponseWrapper) this.getThreadLocalResponse()).setDatamanSessionExpired(true);

            } else {
                httpServletRequest = super.getThreadLocalRequest();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            System.out.println("dataman.dmBase.dmSession.server.DmRSSPrivate.getLocalRequest(): [" + tempDate + "]");
//            System.out.println(super.getThreadLocalRequest().getRequestURL());
//            System.out.println((new Date().getTime() - tempDate));
        }

        return httpServletRequest;
    }

}
