package dataman.utility.dataBackup.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface DataBackup_Srv extends RemoteService {

    public DmResult<List<GrdDataBackup_Mdl>> backupElement(List<GrdDataBackup_Mdl> mrTemp);

    public DmResult<String> saveRecord(GrdDataBackup_Mdl mrTemp);

    public DmResult<String> deleteRecord(String elementName, String elementType);

    public DmResult<List<GrdDataBackup_Mdl>> getDefaultValue();

    public DmResultPageing<List<KeyFieldExtended>> getElementType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getDatabaseName(DmHelper pageing, boolean blnToEndOfFile, String elementType);
}
