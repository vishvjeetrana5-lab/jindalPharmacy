package aarogya.pharmacy.d365StockRecive.server;

import aarogya.selfToolkit.D365_To_His_DataSync.server.StockReciveSync;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import java.sql.Connection;
import java.sql.ResultSet;

public class D365ToHISGRNDataSync_Server extends AbstractEventHandler<Event> {

    public D365ToHISGRNDataSync_Server() {
        super(STEnum.ProcessEOD.D365_To_HIS_GRNDataSync.getValue());
    }

    @Override
    public void handle(Event event) {
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        Connection stagingCnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        SchedulerMast_Mdl schedulerMast_Mdl;

        try {
            schedulerMast_Mdl = event.get(DmEnum.DmEventSystemArgs.SchedulerMaster_Model.toString(), SchedulerMast_Mdl.class);
            cnTemp = DmServer.getConnection(schedulerMast_Mdl.getActiveDB());
            spBegin = new DmSavePoint(cnTemp);
            String databaseName = DmServer.executeScalar(" select bc_databaseName from enviroCompany ", cnTemp);
            stagingCnTemp = DmServer.getConnection(databaseName);
            System.out.print("Hello Grn Recive Scheduler....");
            StockReciveSync.saveStockReciveSync(cnTemp, stagingCnTemp);
            System.out.print("Complete Grn Recive Scheduler....");
            System.out.print("Hello Grn Issue Scheduler....");
            StockReciveSync.saveStockIssueSync(cnTemp, stagingCnTemp);
            System.out.print("Complete Grn Issue Scheduler....");
            spBegin.commit();
            cnTemp.close();
            stagingCnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
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
