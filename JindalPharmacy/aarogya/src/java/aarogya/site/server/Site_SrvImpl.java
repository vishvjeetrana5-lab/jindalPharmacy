package aarogya.site.server;

import dataman.dmBase.self.shared.*;
import aarogya.site.shared.Site_Srv;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.PCSData;

public class Site_SrvImpl extends DmRSSPrivate implements Site_Srv {

    @Override
    public DmResult<PCSData> validateSite(byte[] bytSiteCode) {
        DmResult<PCSData> dmrTemp = new DmResult();
        PCSData pcsMain = new PCSData();
        PCSData pcsSite = new PCSData();
        dmrTemp.setData(pcsMain);

        try {
            pcsMain.lstPLTs.clear();
            pcsMain.lstCompanys.clear();
            pcsSite = DmServer.getSites(this, new String(bytSiteCode));
            pcsMain.setRegionalLanguage(pcsSite.getRegionalLanguage());
            pcsMain.setLocaleCountry(pcsSite.getLocaleCountry());
            pcsMain.setSite(pcsSite);
            if (pcsMain.sSelected != null) {
                pcsMain.lstSites.clear();
                dmrTemp.setStatus(true);
            }
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

}
