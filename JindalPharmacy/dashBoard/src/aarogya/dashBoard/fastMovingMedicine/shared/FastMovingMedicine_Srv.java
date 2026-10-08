package aarogya.dashBoard.fastMovingMedicine.shared;


import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface FastMovingMedicine_Srv extends RemoteService {

    public DmResultPageing<List<FastMovingMedicine_Mdl>> getData(String daysCount,String lastDays,DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue);

    public DmResult<String> getNewDate(int value, String date);

    public DmResult<KeyFieldExtended> getDefaultValue();

    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount);
    
      public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysUpto,String lastDays );

}
