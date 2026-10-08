package dataman.utility.fileTypeMaster.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface FileTypeMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<FileTypeMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(FileTypeMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefault(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<Boolean> syncronizeFileDB();

    public DmResult<KeyFieldExtended> exportDataCSV(String session);
}
