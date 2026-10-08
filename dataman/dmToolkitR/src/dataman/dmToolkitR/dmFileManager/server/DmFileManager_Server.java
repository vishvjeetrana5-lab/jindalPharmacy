package dataman.dmToolkitR.dmFileManager.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import java.sql.Connection;

public class DmFileManager_Server {

    public static void manageLinkFileCollection(String docId, String tempDocId, DmRSS rss, Connection cnTemp) throws Exception {
        manageLinkFileCollection(docId, 1, tempDocId, rss, cnTemp);
    }

    public static void manageLinkFileCollection(String docId, long sNo, String tempDocId, DmRSS rss, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery, sessionId;

        try {
            sessionId = (rss.getLocalRequest().getSession().getId());
            sqlQuery = "Update linkFileCollection set docId = :docId, sNo = :sNo, tempDocId = null, sessionId = null where tempDocId = :tempDocId and sessionId = :sessionId ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("docId", DmShared.iNull(docId).longValue());
            psTemp.setLong("sNo", sNo);
            psTemp.setString("tempDocId", tempDocId);
            psTemp.setString("sessionId", sessionId);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }
    }
}
