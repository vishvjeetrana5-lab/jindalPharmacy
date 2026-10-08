package aarogya.selfToolkit.fifoPopUp.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface FifoPopUp_SrvAsync {

    public void getTxtHCat(String moduleType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGroup(String moduleType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(String moduleType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemName(String moduleType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void startFifo(FifoSetting_Mdl mdl, AsyncCallback<DmResult<String>> result);

    public void readLogFile(String filePath, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
