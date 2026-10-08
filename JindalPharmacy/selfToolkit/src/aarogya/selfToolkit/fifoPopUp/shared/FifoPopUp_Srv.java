package aarogya.selfToolkit.fifoPopUp.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface FifoPopUp_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCat(String moduleType, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(String moduleType, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(String moduleType, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemName(String moduleType, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> startFifo(FifoSetting_Mdl mdl);

    public DmResult<KeyFieldExtended> readLogFile(String filePath);

}
