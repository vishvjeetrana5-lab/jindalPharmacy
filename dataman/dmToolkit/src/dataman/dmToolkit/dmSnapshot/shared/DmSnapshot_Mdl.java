package dataman.dmToolkit.dmSnapshot.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import java.io.Serializable;

public class DmSnapshot_Mdl implements Serializable, IsSerializable {

    private String mongoFileLink;
    DmFile_Mdl file = new DmFile_Mdl();

    public DmSnapshot_Mdl() {
    }

    public String getMongoFileLink() {
        return mongoFileLink;
    }

    public void setMongoFileLink(String mongoFileLink) {
        this.mongoFileLink = mongoFileLink;
    }

    public DmFile_Mdl getFile() {
        return file;
    }

    public void setFile(DmFile_Mdl file) {
        this.file = file;
    }
}
