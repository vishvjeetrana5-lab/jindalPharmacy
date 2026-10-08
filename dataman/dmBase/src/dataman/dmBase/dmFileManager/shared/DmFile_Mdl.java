package dataman.dmBase.dmFileManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmFile_Mdl implements Serializable, IsSerializable, DmIFile_Mdl {

    private String fileName;
    private String fileSize;
    private String serverFileName;
    private String serverDirectory;

    public DmFile_Mdl() {
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileSize() {
        return fileSize;
    }

    @Override
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String getServerFileName() {
        return serverFileName;
    }

    @Override
    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    @Override
    public String getServerDirectory() {
        return serverDirectory;
    }

    @Override
    public void setServerDirectory(String serverDirectory) {
        this.serverDirectory = serverDirectory;
    }
}
