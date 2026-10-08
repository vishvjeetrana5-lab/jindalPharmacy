package dataman.dmToolkit.eGstPopup.server;

import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.eGstPopup.shared.IEGstPopup;
import dataman.dmToolkit.eGstPopup.shared.IEGstTransaction;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import java.sql.Connection;

public class EGstPopup_Server {

    public static void processEGst(MasterEGst_Mdl masterEGst_Mdl, int actionCode, DmRSSPrivate rss, IEGstPopup eGstProcessed) throws Exception {
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            Gst_Server.processEGst(masterEGst_Mdl, actionCode, cnTemp, rss, new IEGstTransaction() {
                @Override
                public void processPostApi(MasterEGst_Mdl previousMasterEgst_Mdl, MasterEGst_Mdl currentMasterEgst_Mdl, int actionCode, Connection cnTemp, DmRSSPrivate dmRss) throws Exception {
                    DmSavePoint spBegin = null;
                    try {
                        spBegin = new DmSavePoint(cnTemp); 
                        if (actionCode == DmEnum.EGST_ACTION.GST_UPDATE_DOCUMENTS.getCode()) {
                            Gst_Server.updateGstDocument(previousMasterEgst_Mdl.getGst_Mdl(), currentMasterEgst_Mdl.getGst_Mdl(), actionCode, cnTemp, dmRss);
                            if (eGstProcessed != null) {
                                eGstProcessed.finalizeGstDocumentUpdate(currentMasterEgst_Mdl.getGst_Mdl(), actionCode, cnTemp);
                            }
                        } else {
                            Gst_Server.saveEGstRecord(previousMasterEgst_Mdl.geteGst_Mdl(), currentMasterEgst_Mdl.geteGst_Mdl(), actionCode, cnTemp, dmRss);
                            if (eGstProcessed != null) {
                                eGstProcessed.updateEGstDetail(currentMasterEgst_Mdl.geteGst_Mdl(), actionCode, cnTemp);
                            }
                        }
                        spBegin.commit();
                    } catch (Exception e) {
                        try {
                            if (spBegin != null) {
                                spBegin.rollBack();
                            }
                        } catch (Exception ex) {
                        }
                        e.printStackTrace();
                        throw e;
                    }
                }
            });
            cnTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

}
