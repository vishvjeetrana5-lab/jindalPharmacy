package dataman.dmBase.dmSession.server;

import java.util.Enumeration;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class DmRequestWrapper extends HttpServletRequestWrapper {

    private boolean isGwtServiceCalled = false;
    private String datamansessionid;

    private String clientIp;
    private String browserAgent;

    private boolean isNewSet = false;
    private boolean isNew = false;

    public DmRequestWrapper(HttpServletRequest request) {
        super(request);

        if (request.getHeader("DTMN_SERVICE") != null) {
            this.isGwtServiceCalled = Boolean.valueOf(request.getHeader("DTMN_SERVICE"));
        }

        if (this.isGwtServiceCalled == true) {
            this.datamansessionid = request.getHeader("DTMN_SESSIONID");
        }

        this.clientIp = request.getRemoteAddr();

        if (request.getHeader("user-agent") != null) {
            this.browserAgent = String.valueOf(request.getHeader("user-agent"));
        }
    }

    public void assignUniqueDatamanSessionId() {
        if (this.isGwtServiceCalled == true) {
            if ((this.datamansessionid == null) || (this.datamansessionid.equalsIgnoreCase("blank") == true)) {
                this.datamansessionid = UUID.randomUUID().toString();
            }
        }
    }

    public static DmRequestWrapper getDatamanRequest(HttpServletRequest instance) throws Exception {
        if ((instance instanceof DmRequestWrapper) == false) {
            throw new Exception("not a valid dataman request");
        }
        return (DmRequestWrapper) instance;
    }

    public boolean getIsGwtServiceCall() {
        return this.isGwtServiceCalled;
    }

    public String getDatamanSessionId() {
        return this.datamansessionid;
    }

    public boolean getIsNew() {

        if (!this.isNewSet) {
            if (this.isGwtServiceCalled == true) {
                if ((this.datamansessionid == null) || (this.datamansessionid.equalsIgnoreCase("blank") == true)) {
                    this.isNew = true;
                    this.isNewSet = true;
                }
            }
        }
        return this.isNew;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession sess = null;
        if (this.isGwtServiceCalled == true) {
            if (DmSession.getSessions().containsKey(this.datamansessionid) == true) {
                sess = DmSession.getSessions().get(this.datamansessionid);
            } else if (DmSession.getSessions().containsKey(this.datamansessionid) == false) {
                if (this.isNew) {
                    DmSession tmpsess = new DmSession(this.datamansessionid, super.getSession(true), this.clientIp, this.browserAgent, true);
                    DmSession.getSessions().put(this.datamansessionid, tmpsess);
                    sess = tmpsess;
                }
            }
        } else if (this.isGwtServiceCalled == false) {
            sess = super.getSession(create);
        }
        return sess;
    }

    public DmSession getDatamanSession() throws Exception {
        if (this.isGwtServiceCalled == false) {
            throw new Exception("not a valid dataman request.");
        }
        return (DmSession) getSession();
    }

}
