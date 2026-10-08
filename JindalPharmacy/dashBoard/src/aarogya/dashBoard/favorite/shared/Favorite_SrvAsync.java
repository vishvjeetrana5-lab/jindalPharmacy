package aarogya.dashBoard.favorite.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface Favorite_SrvAsync {

    public void insertRecord(Favorite_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void deleteRecord(Favorite_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void setPosition(List<GrdModuleMenu_Mdl> listTemp, Favorite_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(AsyncCallback<DmResult<List<ModuleDetail_Mdl>>> result);

    public void getModuleList(String moduleType, AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>> result);

    public void getMenuList(String menuType, String moduleName, AsyncCallback<DmResult<List<GrdModuleMenu_Mdl>>> result);
}
