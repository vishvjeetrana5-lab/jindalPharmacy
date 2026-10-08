package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;

public interface Initializer_Interface {

    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model);
}
