package dataman.dmToolkit.dmFileManager.shared;

import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.HashMap;
import java.util.List;

public interface DmFileManager_SrvAsync {

    public void getFileType(String keyCode, String tempKeyCode, String sNo, String ruleCode, String fileTypeName, HashMap<String, String> multiDocIdWithRuleCode, AsyncCallback<DmResult<List<DmFileRuleType>>> result);

    public void allocateResource(List<DmAttachment_Mdl> attachment, AsyncCallback<DmResult<List<DmAttachment_Mdl>>> callback);

    public void deleteFile(DmAttachment_Mdl fileAttachment, AsyncCallback<DmResult<Boolean>> callback);

    public void saveEditedFile(DmAttachment_Mdl fileAttachment, DmFile_Mdl fileModel, AsyncCallback<DmResult<DmAttachment_Mdl>> callback);

    public void getActualImageSize(DmAttachment_Mdl fileAttachment, AsyncCallback<DmResult<DmSize>> callback);

    public void updateRemarks(DmAttachment_Mdl fileAttachment, String remarks, AsyncCallback<DmResult<String>> result);

}
