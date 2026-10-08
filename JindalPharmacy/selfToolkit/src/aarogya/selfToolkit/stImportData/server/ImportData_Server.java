package aarogya.selfToolkit.stImportData.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmBase.self.server.DmHandelFile;
import java.sql.Connection;
import java.sql.ResultSet;

public class ImportData_Server extends DmImportData_Server {

    public static String getITSCode(String lineNoDisplay, String itsName, Connection cnTemp, DmRSS rss, DmHandelFile handelFile) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, itsCode = "";

        try {
            itsName = DmShared.removeSpace(itsName);
            itsCode = DmServer.getCodeViaManualCode(itsName, "name", "its", "code", cnTemp);

            if (DmShared.iNull(itsCode).longValue() == 0) {
                itsCode = DmServer.getSerialNoInteger(rss, "itemCat", cnTemp);
                if (DmShared.iNull(itsCode).longValue() == 0) {
                    throw new UnsupportedOperationException("Invalid key.");
                }
                sqlQuery = "Select Count(*) As Cnt From its Where name =:name AND code <> :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(itsName));
                psTemp.setString("code", DmShared.xNull(itsCode));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("ITS Name already exists.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Insert Into its(code,name,U_EntDt,transferDt,preparedBy) Values  "
                        + "(:code ,:name, getdate(),:transferDt,:preparedBy) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(itsCode));
                psTemp.setString("name", DmShared.xNull(itsName));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
                psTemp.executeUpdate();
                psTemp.close();

                handelFile.writeLine(lineNoDisplay + itsName + " ITS is created.");
            }

        } catch (Exception e) {
            try {
                handelFile.writeLine(lineNoDisplay + e.getMessage());
            } catch (Exception ex) {
            }
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return itsCode;
    }

}
