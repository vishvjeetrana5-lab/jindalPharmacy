package dataman.dmToolkitR.dmImageCanvas.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;

public interface DmImageCanvas_SrvAsync {

    public void saveImage(String imgStr, DmFile_Mdl fileModel, AsyncCallback<DmResult<DmFile_Mdl>> callback);

    public void getAspectImageSize(String ruleCode, String mongoFileLink, int expWidth,int expHeight, AsyncCallback<DmResult<DmSize>> cakkback);
}
