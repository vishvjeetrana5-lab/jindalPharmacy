package dataman.dmBase.dmSession.server;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionContext;
import javax.websocket.Session;

public class DmSession implements HttpSession {

    private String sessionId = "";
    private HttpSession hsession;
    private boolean boolIsNew = false;
    private Hashtable<String, Object> table = new Hashtable<String, Object>();
    private static final Hashtable<String, DmSession> sessions = new Hashtable<String, DmSession>();
//    private static final Hashtable<String, Session> webSocketSessions = new Hashtable<>();
    private DmSessionPropertyListener propListener = new DmSessionPropertyListener();
    private LocalDateTime cDate = null;
    private LocalDateTime aDate = null;
    private int lockTimeOut = 0;
    private String remoteAddress;
    private String browserAgent;
    private boolean isMobileCall;
    private Session receiverSession = null;
    private DmEnum.DmSessionStatus sessionStatus;

    public DmSession(String reqSessionId, HttpSession sess, String remoteAddress, String browAgent, boolean isNew) {
        this.hsession = sess;
        this.sessionId = reqSessionId;
        this.boolIsNew = isNew;
        this.remoteAddress = remoteAddress.trim();
        this.browserAgent = browAgent.trim();
        this.isMobileCall = ((this.browserAgent.indexOf("Mobile") >= 0) || (this.browserAgent.indexOf("mobile") >= 0));
        this.cDate = LocalDateTime.now();
        this.aDate = LocalDateTime.now();
        if (sessions.containsKey(this.sessionId) == false) {
            sessions.put(sessionId, this);
        }
    }

    public DmEnum.DmSessionStatus getSessionStatus() {
        return this.sessionStatus;
    }

    public void setSessionStatus(DmEnum.DmSessionStatus sessStatus) {
        this.sessionStatus = sessStatus;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    public String getBrowserAgent() {
        return this.browserAgent;
    }

    public boolean getIsMobileSession() {
        return this.isMobileCall;
    }

    public static Hashtable<String, DmSession> getSessions() {
        return sessions;
    }

    public synchronized long getLastAccessDurationInSeconds() {
        long durSecs = 0;
        try {
            LocalDateTime currTime = LocalDateTime.now();
            LocalDateTime tempTime = LocalDateTime.from(this.aDate);

            durSecs = tempTime.until(currTime, ChronoUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return durSecs;
    }

    public HttpSession getBrowserSession() {
        return this.hsession;
    }

    @Override
    public long getCreationTime() {
        return new Date(this.cDate.getYear(), this.cDate.getMonthValue(), this.cDate.getDayOfMonth(), this.cDate.getHour(), this.cDate.getMinute(), this.cDate.getSecond()).getTime();
    }

    @Override
    public String getId() {
        return this.sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return new Date(this.aDate.getYear(), this.aDate.getMonthValue(), this.aDate.getDayOfMonth(), this.aDate.getHour(), this.aDate.getMinute(), this.aDate.getSecond()).getTime();
    }

    @Override
    public ServletContext getServletContext() {
        return this.hsession.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        this.hsession.setMaxInactiveInterval(i);
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.hsession.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return this.hsession.getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        Object obj = null;

        name = name.toLowerCase();
        if (this.table.containsKey(name) == true) {
            obj = this.table.get(name);
        }

        this.aDate = LocalDateTime.now();
        return obj;
    }

    @Override
    public Object getValue(String string) {
        return getAttribute(string);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        this.aDate = LocalDateTime.now();
        return this.table.keys();
    }

    public boolean constainsValueName(String name) {
        this.aDate = LocalDateTime.now();
        return this.table.containsKey(name);
    }

    @Override
    public String[] getValueNames() {
        String[] vnames = new String[this.table.size()];
        Enumeration<String> enumstr = getAttributeNames();
        int i = 0;
        while (enumstr.hasMoreElements()) {
            vnames[i] = enumstr.nextElement();
            i++;
        }
        this.aDate = LocalDateTime.now();
        return vnames;
    }

    @Override
    public void setAttribute(String string, Object o) {
        string = string.toLowerCase();

        if (this.table.containsKey(string) == false) {
            this.table.put(string, o);
            this.propListener.attributeAdded(new HttpSessionBindingEvent(this, string, o));
        } else if (this.table.containsKey(string) == true) {
            this.table.replace(string, o);
            this.propListener.attributeReplaced(new HttpSessionBindingEvent(this, string, o));
        }
        this.aDate = LocalDateTime.now();
    }

    @Override
    public void putValue(String string, Object o) {
        setAttribute(string, o);
    }

    @Override
    public void removeAttribute(String string) {
        string = string.toLowerCase();
        if (this.table.containsKey(string) == true) {
            this.table.remove(string);
            this.propListener.attributeRemoved(new HttpSessionBindingEvent(hsession, string));
        }
        this.aDate = LocalDateTime.now();
    }

    @Override
    public void removeValue(String string) {
        removeAttribute(string);
    }

    @Override
    public void invalidate() {
        this.hsession.invalidate();
    }

    public static boolean validateSession(String sessionId, HttpServletRequest req) {
        boolean status = false;

        try {
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new Exception("session does not exist.");
            }

            DmSession session = DmSession.getSessions().get(sessionId);

//            if (!req.getRemoteAddr().equalsIgnoreCase("0:0:0:0:0:0:0:1") && !req.getRemoteAddr().equalsIgnoreCase("127.0.0.1")) {
//                if (!req.getRemoteAddr().equalsIgnoreCase(session.getRemoteAddress())) {
//                    throw new Exception("session not validated.");
//                }
//            }
            if (!req.getHeader("user-agent").equalsIgnoreCase(session.getBrowserAgent())) {
                throw new Exception("session not validated.");
            }
            status = true;
        } catch (Exception ex) {
            status = false;
        }

        return status;
    }

    @Override
    public boolean isNew() {
        return this.boolIsNew;
    }

    public void setReceiverSession(Session session) {
        this.receiverSession = session;
    }

    public Session getReceiverSession() {
        return this.receiverSession;
    }

    public int getLockTimeOut() {
        return lockTimeOut;
    }

    public void setLockTimeOut(int lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
    }
}
