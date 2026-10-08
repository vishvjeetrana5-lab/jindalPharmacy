package dataman.dmToolkit.eGstPopup.shared;

import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import java.sql.Connection;

public interface IEGstTransaction {
    public void processPostApi(MasterEGst_Mdl previousMasterEgst_Mdl, MasterEGst_Mdl currentMasterEgst_Mdl,int actionCode, Connection cnTemp, DmRSSPrivate dmRss) throws Exception;
}
