package dataman.accounts.defineCostCenter.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface DefineCostCenter_Srv extends RemoteService {

    public DmResultPageing<List<String>> getSuggestionCostCenter(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<DefineCostCenter_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(DefineCostCenter_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHUnderCostCenter_Mdl>> getTxtHUnderCostCenter(DmHelper pageing, boolean blnToEndOfFile, String strCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getGrdUnderCostCenter(String strCode);
}
