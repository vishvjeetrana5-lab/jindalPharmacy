package dataman.dmToolkit.eGstPopup.shared;

import dataman.dmToolkit.gst.shared.EGst_Mdl;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import java.sql.Connection;

public interface IEGstPopup {

    public void updateEGstDetail(EGst_Mdl eGst_Mdl, int actionCode, Connection cnTemp) throws Exception;

    public void finalizeGstDocumentUpdate(GstDocument_Mdl gst_Mdl, int actionCode, Connection cnTemp) throws Exception; 
}
