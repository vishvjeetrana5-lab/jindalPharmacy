package dataman.utility.dataBackup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface DataBackup_SrvAsync {

    public void backupElement(List<GrdDataBackup_Mdl> mrTemp, AsyncCallback<DmResult<List<GrdDataBackup_Mdl>>> result);

    public void saveRecord(GrdDataBackup_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void deleteRecord(String elementName, String elementType, AsyncCallback<DmResult<String>> result);

    public void getDefaultValue(AsyncCallback<DmResult<List<GrdDataBackup_Mdl>>> result);

    public void getElementType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDatabaseName(DmHelper helper, boolean blnToEndOfFile, String elementType, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
