package aarogya.master.referredMaster.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface ReferredMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ReferredMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getTempKeyCode(AsyncCallback<String> result);

    public void saveRecord(ReferredMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSpecialization(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHActive(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHInitial(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSuggetionConsultant(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getSuggetionShortName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getTxtHCity(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> result);

    public void getTxtHTDSCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
