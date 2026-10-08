package dataman.dmToolkit.dmFileManager.shared;

import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.HashMap;
import java.util.List;

public interface DmFileManager_Srv extends RemoteService {

    public DmResult<List<DmFileRuleType>> getFileType(String keyCode, String tempKeyCode, String sNo, String ruleCode, String fileTypeName, HashMap<String, String> multiDocIdWithRuleCode);

    public DmResult<List<DmAttachment_Mdl>> allocateResource(List<DmAttachment_Mdl> attachment);

    public DmResult<Boolean> deleteFile(DmAttachment_Mdl fileAttachment);

    public DmResult<DmAttachment_Mdl> saveEditedFile(DmAttachment_Mdl fileAttachment, DmFile_Mdl fileModel);

    public DmResult<DmSize> getActualImageSize(DmAttachment_Mdl fileAttachment);

    public DmResult<String> updateRemarks(DmAttachment_Mdl fileAttachment, String remarks);

}
