package aarogya.master.D365ImportExport.server;

import aarogya.selfToolkit.D365_To_His_DataSync.server.ItemMasterSync;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;

public class D365ToHISItemSync_Server extends AbstractEventHandler<Event> {

    public D365ToHISItemSync_Server() {
        super(STEnum.ProcessEOD.D365_To_HIS_ItemSync.getValue());
    }

    @Override
    public void handle(Event event) {
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        Connection stagingCnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        DmSavePoint stagingSpBegin = null;
        SchedulerMast_Mdl schedulerMast_Mdl;

        try {
            schedulerMast_Mdl = event.get(DmEnum.DmEventSystemArgs.SchedulerMaster_Model.toString(), SchedulerMast_Mdl.class);
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            cnTemp = DmServer.getConnection(schedulerMast_Mdl.getActiveDB());
            String databaseName = DmServer.executeScalar(" select bc_databaseName from enviroCompany ", cnTemp);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc  ", cnTempCompany);
            cnTempCompany.close();
            stagingCnTemp = DmServer.getConnection(databaseName);
            spBegin = new DmSavePoint(cnTemp);
            stagingSpBegin = new DmSavePoint(stagingCnTemp);

            /*------------------------Data transfer from d365 to his-----------------*/
            ItemMasterSync.syncItemMaster(cnTemp, stagingCnTemp, transactioDB);
            cnTemp.commit();
            stagingCnTemp.commit();
            cnTemp.close();
            stagingCnTemp.close();

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (stagingSpBegin != null) {
                    stagingSpBegin.rollBack();
                }

            } catch (Exception ex) {
            }
            System.out.println("Error occurred while Executing D365 data Sync Scheduler");
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
