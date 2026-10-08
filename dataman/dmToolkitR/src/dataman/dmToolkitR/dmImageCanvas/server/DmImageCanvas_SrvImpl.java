package dataman.dmToolkitR.dmImageCanvas.server;

import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;
import dataman.dmToolkitR.dmImageCanvas.shared.*;
import dataman.dmToolkitR.dmStatic.server.DmServer;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.self.server.DmImageResolver;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

public class DmImageCanvas_SrvImpl extends DmRSSPrivate implements DmImageCanvas_Srv {

    @Override
    public DmResult<DmFile_Mdl> saveImage(String imgStr, DmFile_Mdl fileModel) {
        DmResult<DmFile_Mdl> result = new DmResult<>();
        try {

            System.out.println("IMage String found to save : " + imgStr.trim());

            if ((fileModel != null) && (!DmShared.xNull(fileModel.getServerFileName()).equalsIgnoreCase("")) && (!DmShared.xNull(fileModel.getServerFileName()).equalsIgnoreCase(""))) {
                File tempFile = new File(fileModel.getServerDirectory() + File.separatorChar + fileModel.getServerFileName());
                if (tempFile.exists()) {
                    boolean tempBool = tempFile.delete();
                    System.out.println("File " + ((tempBool) ? " is deleted." : " is unable to delete"));
                }
                tempFile = null;
            }

            imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
            imgStr = imgStr.replaceAll(" ", "+");
            byte[] byteBuffer = Base64.getDecoder().decode(imgStr.getBytes("UTF-8"));

            String sessionId = (String) (((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().getId());

            String directoryPath = DmServer.Path.sessions + "/" + sessionId;
            String serverFileName = UUID.randomUUID().toString() + ".jpg";
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

            DmFile_Mdl model = new DmFile_Mdl();
            model.setFileName(serverFileName);
            model.setServerFileName(model.getFileName());
            model.setServerDirectory(directoryPath);

            result.setData(model);
            result.setStatus(true);
        } catch (Exception ex) {
            result.setErrorMessage(ex.getMessage());
            result.setStatus(false);
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public DmResult<DmSize> getAspectImageSize(String ruleCode, String mongoFileLink, int expWidth,int expHeight) {
        DmResult<DmSize> result = new DmResult<>();
        try {
            DmImageResolver resolver = new DmImageResolver();

            resolver.setMongoFileLink(mongoFileLink);
            resolver.setRuleCode(ruleCode);
            
            result.setData(resolver.resolveImageForSize(expWidth, expHeight, this));            
            
            result.setStatus(true);

        } catch (Exception ex) {
            result.setErrorMessage(ex.getMessage());
            result.setStatus(false);
//            ex.printStackTrace();
        }
        return result;
    }

}
