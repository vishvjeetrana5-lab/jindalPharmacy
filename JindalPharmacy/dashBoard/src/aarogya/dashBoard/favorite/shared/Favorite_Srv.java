package aarogya.dashBoard.favorite.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface Favorite_Srv extends RemoteService {

    public DmResult<String> insertRecord(Favorite_Mdl mrTemp);

    public DmResult<String> deleteRecord(Favorite_Mdl mrTemp);

    public DmResult<String> setPosition(List<GrdModuleMenu_Mdl> listTemp, Favorite_Mdl mrTemp);

    public DmResult<List<GrdModuleMenu_Mdl>> getModuleList(String moduleType);

    public DmResult<List<ModuleDetail_Mdl>> getFillRecords();

    public DmResult<List<GrdModuleMenu_Mdl>> getMenuList(String menuType, String moduleName);
}
