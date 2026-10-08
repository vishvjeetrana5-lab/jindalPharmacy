package dataman.utility.exportProcedure.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.exportProcedure.shared.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ExportProcedure_SrvImpl extends DmRSSPrivate implements ExportProcedure_Srv {

    @Override
    public DmResultPageing<List<GrdProcedure_Mdl>> getProcedureList() {
        DmResultPageing<List<GrdProcedure_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdProcedure_Mdl> lstTemp = new ArrayList<>();
        GrdProcedure_Mdl node = null;
        Connection cnTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select so.name as specificName, ssm.definition as routineDefinition\n"
                    + "        from sys.objects so \n"
                    + "        left join sys.sql_modules ssm on so.object_id = ssm.object_id \n"
                    + "        where so.type = 'P' and so.name like 'osr%' \n"
                    + "        order by so.name ";
            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdProcedure_Mdl();
                node.setProcedureName(DmShared.xNull(rsTemp.getString("specificName")));
                node.setRoutineDefinition(DmShared.xNull(rsTemp.getString("routineDefinition")));
                node.setChecked(false);
                lstTemp.add(node);
            }
            rstTemp.setData(lstTemp);
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportProcedure(ExportProcedure_Mdl mrTemp) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        try {

            if (!DmSession.getSessions().containsKey(mrTemp.getSessionId())) {
                throw new DmException(0, "Invalid request.");
            }
            String directoryPath = DmServer.Path.sessions + File.separatorChar + mrTemp.getSessionId();
            List<Procedure_Mdl> lstProcedures = new ArrayList<>();

            for (int i = 0; i < mrTemp.getLstProcedure().size(); i++) {
                if (!DmShared.xNull(mrTemp.getLstProcedure().get(i).getProcedureName()).equalsIgnoreCase("")) {
                    Procedure_Mdl model = new Procedure_Mdl();
                    model.setProcedureName(mrTemp.getLstProcedure().get(i).getProcedureName());
                    model.setDropProcedure("IF OBJECT_ID ('" + DmShared.xNull(mrTemp.getLstProcedure().get(i).getProcedureName()) + "') IS NOT NULL Drop Procedure " + DmShared.xNull(mrTemp.getLstProcedure().get(i).getProcedureName()));
                    model.setCreateProcedure(mrTemp.getLstProcedure().get(i).getRoutineDefinition());
                    lstProcedures.add(model);
                }
            }

            String serverFileName = "procedures.dat";
            String fullFilePath = directoryPath + "/" + serverFileName;

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;

            try {
                fos = new FileOutputStream(fullFilePath);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(lstProcedures);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (oos != null) {
                    oos.close();
                    oos = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            }

            KeyFieldExtended field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName(serverFileName);
            dmrTemp.setData(field);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return dmrTemp;
    }

}
