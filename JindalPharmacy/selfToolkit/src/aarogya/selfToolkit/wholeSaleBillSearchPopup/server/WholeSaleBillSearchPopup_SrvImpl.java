
package aarogya.selfToolkit.wholeSaleBillSearchPopup.server;

import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.GrdWholeSaleBill_Mdl;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.SearchValue_Mdl;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.WholeSaleBillSearchPopup_Srv;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;


public class WholeSaleBillSearchPopup_SrvImpl extends DmRSSPrivate implements WholeSaleBillSearchPopup_Srv {

    @Override
    public DmResultPageing<List<GrdWholeSaleBill_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
