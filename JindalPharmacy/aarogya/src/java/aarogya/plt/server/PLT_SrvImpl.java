package aarogya.plt.server;

import aarogya.plt.shared.PLT_Srv;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.PCSData;
import dataman.dmBase.self.shared.*;

public class PLT_SrvImpl extends DmRSSPrivate implements PLT_Srv {

    @Override
    public DmResult<PCSData> getCompanys(byte[] bytPLTCode) {
        DmResult<PCSData> dmrTemp = new DmResult();
        PCSData pcsMain = new PCSData();
        PCSData pcsSite = new PCSData();
        dmrTemp.setData(pcsMain);

        try {
            pcsMain.setPLT(DmServer.getPLTs(this, new String(bytPLTCode)));
            if (pcsMain.pltSelected != null) {
                pcsMain.lstPLTs.clear();
                pcsMain.pltSelected = null;
                pcsMain.setCompany(DmServer.getCompanys(this, null));
                if (pcsMain.cSelected != null) {
                    try {
                        pcsSite = DmServer.getSites(this, null);
                        pcsMain.setRegionalLanguage(pcsSite.getRegionalLanguage());
                        pcsMain.setSite(pcsSite);
                    } catch (Exception e) {
                        if (e instanceof DmException) {
                            if (((DmException) e).getErrorNo() == 1) {

                            } else {
                                throw new UnsupportedOperationException(e.getMessage());
                            }
                        } else {
                            throw new UnsupportedOperationException(e.getMessage());
                        }
                    }
                }
                dmrTemp.setStatus(true);
            }

        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

}
