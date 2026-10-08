package aarogya.dashBoard.medicineStockOut.shared;

 
 
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;

public interface MedicineStockOut_SrvAsync {
 
    public void getCardDetail(String daysCount,AsyncCallback<DmResult<CardDetail_Mdl>> result);
    
    
}
