package dataman.dmBase.dmSession.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import java.io.File;
import java.util.TimerTask;

public class DmGarbageCleaner extends TimerTask {

    @Override
    public void run() {
        try {

            cleanInactiveSessionDirectories();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cleanInactiveSessionDirectories() {
        String tempSessionPath, tempPath;
        String sessionId;
        File sessionDir;
        String[] sessionDirs;

        try {
            tempSessionPath = DmBaseServer.Path.sessions;
            sessionDir = new File(tempSessionPath);

            if (sessionDir.exists()) {
                sessionDirs = sessionDir.list();

                for (int i = 0; i < sessionDirs.length; i++) {
                    sessionId = sessionDirs[i];
                    if (!DmSession.getSessions().containsKey(sessionId)) {
                        tempPath = tempSessionPath + "/" + sessionId;
                        DmBaseServer.deleteFilesAndDirectories(tempPath);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
