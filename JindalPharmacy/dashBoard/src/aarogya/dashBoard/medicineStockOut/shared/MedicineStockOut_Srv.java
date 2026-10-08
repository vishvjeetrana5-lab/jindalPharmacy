package aarogya.dashBoard.medicineStockOut.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;

public interface MedicineStockOut_Srv extends RemoteService {

    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount);

}
