package aarogya.selfToolkit.pharmacyDiscountPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PharmacyDiscountPopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(String voucherDate,String vType, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroupName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile);
}
