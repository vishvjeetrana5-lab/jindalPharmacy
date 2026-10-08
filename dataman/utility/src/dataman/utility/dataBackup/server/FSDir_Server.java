package dataman.utility.dataBackup.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.server.ZipUtils;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DataBackUp;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.dataBackup.shared.GrdDataBackup_Mdl;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FSDir_Server extends AbstractEventHandler<Event> {

    public FSDir_Server() {
        super(DataBackUp.FS_DIR.getValue());
    }

    @Override
    public void handle(Event event) {
        SchedulerMast_Mdl schedulerMast_Mdl;
        List<GrdDataBackup_Mdl> mrTemp = new ArrayList<>();
        GrdDataBackup_Mdl node;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        try {
            schedulerMast_Mdl = event.get(DmEnum.DmEventSystemArgs.SchedulerMaster_Model.toString(), SchedulerMast_Mdl.class);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = "select elementType, elementName, elementLocation, backupLocation "
                    + " from backupInfo "
                    + " where elementType = :elementType ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setInt("elementType", DataBackUp.FS_DIR.getId());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdDataBackup_Mdl();
                node.setChecked(true);
                node.setBackupLocation(DmShared.xNull(rsTemp.getString("backupLocation")));
                node.setElementLocation(DmShared.xNull(rsTemp.getString("elementLocation")));
                node.setElementName(DmShared.xNull(rsTemp.getString("elementName")));
                node.setElementType(DataBackUp.getKeyOf(DmShared.iNull(rsTemp.getString("elementType")).intValue()).getValue());
                node.setElementTypeCode(DmShared.iNull(rsTemp.getString("elementType")).toString());
                mrTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            if (mrTemp.size() > 0) {
                backup(mrTemp);
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    public void backup(List<GrdDataBackup_Mdl> mrTemp) {
        DmHandelFile handelFileLog = null;
        SimpleDateFormat sdf;
        String formattedDateTime, formattedDate, backupLocation, elementLocation, directoryPath;
        File tempFile = null;
        try {
            sdf = new SimpleDateFormat("yyyyMMdd");
            formattedDate = sdf.format(DmServer.getServerTimestamp().getTime());

            sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
            formattedDateTime = sdf.format(DmServer.getServerTimestamp().getTime());
            directoryPath = DmServer.Path.logs + File.separatorChar + "dbBackup";
            tempFile = new File(directoryPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            tempFile = null;
            handelFileLog = new DmHandelFile(directoryPath + File.separatorChar + "fsDIR.log", true);
            for (GrdDataBackup_Mdl dbBackup_Mdl : mrTemp) {
                if (dbBackup_Mdl.isChecked()) {
                    if (DmShared.xNull(dbBackup_Mdl.getElementName()).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Element Name not found");
                    }
                    if (DmShared.xNull(dbBackup_Mdl.getElementLocation()).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Element Location not found");
                    }
                    if (DmShared.xNull(dbBackup_Mdl.getBackupLocation()).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Backup Location not found");
                    }
                    if (DmShared.iNull(dbBackup_Mdl.getElementTypeCode()).intValue() == DataBackUp.FS_DIR.getId()) {
                        try {
                            backupLocation = dbBackup_Mdl.getBackupLocation().replaceAll("\\{date\\}", formattedDate);
                            tempFile = new File(backupLocation);
                            if (!tempFile.exists()) {
                                tempFile.mkdirs();
                            }
                            tempFile = null;

                            elementLocation = dbBackup_Mdl.getElementLocation().replaceAll("\\{date\\}", formattedDate);

                            dbBackup_Mdl.setBackupFileLocation(DmShared.xNull(backupLocation) + File.separatorChar + formattedDateTime + "_" + DmShared.xNull(dbBackup_Mdl.getElementName()));
                            ZipUtils zipUtils = new ZipUtils(DmShared.xNull(elementLocation), dbBackup_Mdl.getBackupFileLocation() + ".zip");
                            zipUtils.zipIt();
                            dbBackup_Mdl.setStatus("Backup Successfully.");
                            handelFileLog.writeLine(formattedDateTime + " " + dbBackup_Mdl.getStatus() + " " + dbBackup_Mdl.getBackupFileLocation());
                        } catch (Exception e) {
                            dbBackup_Mdl.setStatus("Unsuccessful");
                            handelFileLog.writeLine(formattedDateTime + " " + dbBackup_Mdl.getElementType() + ": " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception ee) {
            try {
                handelFileLog.writeLine("dbMongo: " + ee.getMessage());
            } catch (Exception he) {
            }
        } finally {
            try {
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception fe) {
            }
        }
    }

}
