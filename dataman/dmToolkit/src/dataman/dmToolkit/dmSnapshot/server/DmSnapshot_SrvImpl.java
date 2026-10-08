package dataman.dmToolkit.dmSnapshot.server;

import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Srv;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.self.server.DmImageResolver;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

public class DmSnapshot_SrvImpl extends DmRSSPrivate implements DmSnapshot_Srv {

    @Override
    public DmResult<DmIFile_Mdl> uploadSnapshot(String Image) {
        DmResult<DmIFile_Mdl> dmrTemp = new DmResult();
        DmIFile_Mdl file;
        String sessionId = "";

        try {
            Image = Image.substring(Image.indexOf(",") + 1);
            Image = Image.replaceAll(" ", "+");
            byte[] byteBuffer = Base64.getDecoder().decode(Image.getBytes("UTF-8"));

            sessionId = (String) (((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().getId());

            String directoryPath = DmServer.Path.sessions + "/" + sessionId;
            String serverFileName = UUID.randomUUID().toString() + ".png";
            String fullFilePath = directoryPath + "/" + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }

            sFile = new File(fullFilePath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(sFile);
                fos.write(byteBuffer);
            } catch (Exception exc) {
            } finally {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            }

            file = new DmFile_Mdl();
            file.setServerDirectory(directoryPath.trim());
            file.setServerFileName(serverFileName.trim());
            dmrTemp.setData(file);
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dmrTemp;
    }

    @Override
    public DmResult<DmSize> getImageSize(int width, int height, String ruleCode, DmSnapshot_Mdl snapshotModel) {
        DmResult<DmSize> result = new DmResult<>();
        try {
            DmImageResolver resolver = new DmImageResolver();
            resolver.setSessionId(((DmRequestWrapper) this.getThreadLocalRequest()).getDatamanSessionId());
            if (snapshotModel.getFile() != null) {
                resolver.setDirectoryPath(snapshotModel.getFile().getServerDirectory());
                resolver.setFileName(snapshotModel.getFile().getServerFileName());
            }
            resolver.setMongoFileLink(snapshotModel.getMongoFileLink());
            resolver.setRuleCode(ruleCode);
            result.setData(resolver.resolveImageForSize(width, height, this));
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
//            ex.printStackTrace();
        }
        return result;
    }
}
