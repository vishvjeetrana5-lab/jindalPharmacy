package aarogya.company.server;

import dataman.dmBase.self.shared.*;
import aarogya.company.shared.Company_Srv;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.PCSData;

public class Company_SrvImpl extends DmRSSPrivate implements Company_Srv {

    @Override
    public DmResult<PCSData> getSites(byte[] bytCompanyCode) {
        DmResult<PCSData> dmrTemp = new DmResult();
        PCSData pcsMain = new PCSData();
        PCSData pcsSite = new PCSData();
        dmrTemp.setData(pcsMain);
        try {
            pcsMain.lstPLTs.clear();
            pcsMain.setCompany(DmServer.getCompanys(this, new String(bytCompanyCode)));
            if (pcsMain.cSelected != null) {
                pcsMain.lstCompanys.clear();
                pcsMain.cSelected = null;

                pcsSite = DmServer.getSites(this, null);
                pcsMain.setRegionalLanguage(pcsSite.getRegionalLanguage());
                pcsMain.setSite(pcsSite);
            }
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }

}
