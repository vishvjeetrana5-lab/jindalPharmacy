package aarogya.master.cardReceive.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CardReceive_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<CardReceive_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(CardReceive_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> fillDeleteGrd(DmHelper pageing, boolean blnToEndOfFile, String recDate);

    public DmResultPageing<List<GrdCardReceive_Mdl>> getReceiveCards(DmHelper pageing, boolean blnToEndOfFile, String recDate);

}
