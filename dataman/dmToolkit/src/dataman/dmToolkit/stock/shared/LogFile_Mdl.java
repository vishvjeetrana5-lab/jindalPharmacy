package dataman.dmToolkit.stock.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.Serializable;

public class LogFile_Mdl implements IsSerializable, Serializable {

    private String filePath = "";
    private DmHandelFile handelFile = null;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DmHandelFile getHandelFile() {
        return handelFile;
    }

    public void setHandelFile(DmHandelFile handelFile) {
        this.handelFile = handelFile;
    }

}
