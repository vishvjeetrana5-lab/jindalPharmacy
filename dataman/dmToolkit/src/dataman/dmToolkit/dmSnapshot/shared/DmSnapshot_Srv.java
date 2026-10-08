package dataman.dmToolkit.dmSnapshot.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.self.shared.*;

public interface DmSnapshot_Srv extends RemoteService {

    public DmResult<DmIFile_Mdl> uploadSnapshot(String Image);

    public DmResult<DmSize> getImageSize(int width, int height, String ruleCode, DmSnapshot_Mdl snapshotModel);
}
