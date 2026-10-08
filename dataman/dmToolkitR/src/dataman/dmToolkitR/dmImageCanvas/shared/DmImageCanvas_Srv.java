
package dataman.dmToolkitR.dmImageCanvas.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;

public interface DmImageCanvas_Srv extends RemoteService {

    public DmResult<DmFile_Mdl> saveImage(String imgStr, DmFile_Mdl fileModel);

    public DmResult<DmSize> getAspectImageSize(String ruleCode, String mongoFileLink, int expWidth,int expHeight);
}
