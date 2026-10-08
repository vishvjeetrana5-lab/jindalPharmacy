package aarogya.inventory.projectIndicator.shared;

import aarogya.inventory.projectIndicator.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ProjectIndicator_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ProjectIndicator_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ProjectIndicator_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHV_Type(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<DefaultValue_Mdl> getDefaultValues();
    
    public DmResult<String[]> getServerDateTime();

}
