package aarogya.pharmacy.profitSlab.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ProfitSlab_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ProfitSlab_Mdl> getMoveRec(String strKey);

    public DmResult<List<GrdProfitSlab_Mdl>> getGrdData();

    public DmResult<String> saveRecord(ProfitSlab_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();
}
