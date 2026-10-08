package dataman.dmBase.dmSession.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmSession.server.DmSession;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;
import javax.websocket.CloseReason;

public class DmSessionCleaner extends TimerTask {

    @Override
    public void run() {
        try {
 
            cleanExpiredSessions();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cleanExpiredSessions() {
        String sessionId;
        try {
            List<DmSession> sessionList = DmSession.getSessions().values().stream().filter(s -> s.getLastAccessDurationInSeconds() > DmBaseServer.sessionTimeOut).collect(Collectors.toList());
            for (int i = 0; i < sessionList.size(); i++) {
                sessionId = sessionList.get(i).getId();
                if (sessionList.get(i).getReceiverSession() != null) {
                    if (sessionList.get(i).getReceiverSession().isOpen()) {
                        sessionList.get(i).getReceiverSession().close(new CloseReason(CloseReason.CloseCodes.RESERVED, "Session get Expired."));
                    }
                }
                DmBaseServer.deleteFilesAndDirectories(sessionId);
                DmSession.getSessions().remove(sessionId); 
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
