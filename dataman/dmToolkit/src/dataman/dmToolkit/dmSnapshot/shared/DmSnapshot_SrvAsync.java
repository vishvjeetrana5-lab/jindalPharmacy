package dataman.dmToolkit.dmSnapshot.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.self.shared.*;

public interface DmSnapshot_SrvAsync {

    public void uploadSnapshot(String Image, AsyncCallback<DmResult<DmIFile_Mdl>> result);

    public void getImageSize(int width, int height, String ruleCode, DmSnapshot_Mdl snapshotModel, AsyncCallback<DmResult<DmSize>> result);
}
