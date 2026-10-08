package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.utility.itemTaxStructure.shared.GrdItemTaxStructure_Mdl;
import dataman.utility.itemTaxStructure.shared.ItemTaxStructure_Mdl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;

public class InitializerITS implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public InitializerITS() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            Gson gson = new Gson();

            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, ItemTaxStructure_Mdl.class), model.getYearStart());
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                    br.close();
                    br = null;
                }
            }
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                throw new Exception(e);
            } catch (Exception ex) {
            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (refreshDataStructure != null) {
                    refreshDataStructure.close();
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (sessionDir != null) {
                    sessionDir = null;
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private int save(ItemTaxStructure_Mdl mrTemp, String applicableFrom) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery, maxApplicableDate = "", applicableTo = "31-Dec-9999", errorMessage = "";
        int status = 0, rtnStatus = 0;
        try {
            if (mrTemp != null) {
                mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
                mrTemp.setApplicableFrom(DmShared.dNull(applicableFrom));
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));

                if (DmShared.iNull(DmServer.getCodeViaManualCode(mrTemp.getSharedKeyFieldName(), "name", "its", "code", cnTemp)).longValue() == 0) {
                    spBegin = new DmSavePoint(cnTemp);
                    mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rssTemp, "its", cnTemp));

                    if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                        status = DmImportStatus.ERROR.getId();
                        throw new UnsupportedOperationException("Invalid key.");
                    }

                    if (!maxApplicableDate.equalsIgnoreCase("")) {
                        if (DmServer.convertDate(mrTemp.getApplicableFrom()).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                            sqlQuery = "select applicableFrom, applicableTo "
                                    + " from itsDetail "
                                    + " where code = :code "
                                    + " and applicableFrom < :applicableFromDate "
                                    + " group by applicableFrom,applicableTo "
                                    + " order by applicableFrom desc "
                                    + " offset 0 rows fetch next 1 rows only ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
                            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                                sqlQuery = " update itemCode set "
                                        + " applicableTo = :applicableToDate "
                                        + " where applicableFrom = :applicableFromDate "
                                        + " and itemCode = :code ";
                                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp1.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                                psTemp1.setDate("applicableFromDate", rsTemp.getDate("applicableFromDate"));
                                psTemp1.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                                psTemp1.executeUpdate();
                                psTemp1.close();

                            } else {
                                sqlQuery = "select applicableFrom, applicableTo "
                                        + "FROM itsDetail "
                                        + "where applicableFrom > :applicableFromDate "
                                        + "and code = :code "
                                        + "group by applicableFrom, applicableTo "
                                        + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                                // + "LIMIT 1 ";
                                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp1.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
                                psTemp1.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()).toString());
                                rsTemp1 = psTemp1.executeQuery();
                                if (rsTemp1.next()) {
                                    applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));
                                }
                                psTemp1.close();
                                rsTemp1.close();
                            }
                            psTemp.close();
                            rsTemp.close();
                        } else {
                            sqlQuery = "update itsDetail set "
                                    + " applicableTo = :applicableToDate "
                                    + " where applicableFrom = :applicableFromDate and code = :code ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setDate("applicableFromDate", DmServer.convertDate(maxApplicableDate));
                            psTemp.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }

                    sqlQuery = "Insert Into its(code, name, u_EntDt, preparedBy) values (:code, :name, :uEntDt, :preparedBy) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName().trim()));
                    psTemp.setString("preparedBy", DmServer.getSsnUserName(rssTemp));
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "Delete from itsDetail where code=:code and applicableFrom = :applicableFrom";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("applicableFrom", DmShared.xNull(mrTemp.getApplicableFrom()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    if (mrTemp.getGrdItemTaxStructure() != null) {
                        for (int i = 0; i < mrTemp.getGrdItemTaxStructure().size(); i++) {
                            GrdItemTaxStructure_Mdl node = mrTemp.getGrdItemTaxStructure().get(i);
                            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() > 0
                                    && (DmShared.iNull(node.getSiteTag()).intValue() == 1 || DmShared.iNull(node.getSiteTag()).intValue() == 0)) {
                                node.setOverHeadTag(DmServer.getCodeViaManualCode(node.getSharedKeyFieldName(), "name", "overHeadMast", "code", cnTemp));
                                if (DmShared.iNull(node.getOverHeadTag()).longValue() == 0) {
                                    status = DmImportStatus.ERROR.getId();
                                    throw new DmException(0, node.getSharedKeyFieldName() + " is not found for " + mrTemp.getSharedKeyFieldName());
                                }

                                node.setAccntAffectedTag(DmServer.getCodeViaManualCode(node.getAccntAffectedText(), "name", "subGroup", "subCode", cnTemp));
                                if (DmShared.iNull(node.getAccntAffectedTag()).longValue() == 0) {
                                    status = DmImportStatus.WARNING.getId();
                                    refreshDataStructure.writeLine(node.getAccntAffectedText() + " a/c is not found for " + node.getSharedKeyFieldName() + " in " + mrTemp.getSharedKeyFieldName());
                                }

                                sqlQuery = "Insert Into itsDetail(code,overHeadCode,locationCode,atRate,amount,subCode,applicableFrom,applicableTo) Values  "
                                        + "(:code ,:overHeadCode,:locationCode,:atRate,:amount,:subCode,:applicableFrom,:applicableTo) ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                                psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                                psTemp.setDate("applicableTo", DmServer.convertDate(applicableTo));
                                psTemp.setString("overHeadCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                                psTemp.setString("locationCode", DmShared.xNull(node.getLocationTag()));
                                psTemp.setString("atRate", DmServer.vNull(node.getAtRate(), "0.00"));
                                psTemp.setString("amount", DmServer.vNull(node.getAmount(), "0.00"));
                                psTemp.setString("subCode", DmShared.foriegnKey(node.getAccntAffectedTag()));
                                psTemp.executeUpdate();
                                psTemp.close();
                            }
                        }
                    }

                    sqlQuery = "SELECT vIts.applicableFrom  "
                            + " , vIts.locationCode  "
                            + " , MAX(om.name) overHeadName  "
                            + " FROM (  "
                            + " 	SELECT itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode,  ISNULL(itsd.site_Code,0) site_Code  "
                            + " 	, RANK() OVER (PARTITION BY itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode ORDER BY itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode, itsd.site_Code) AS sr  "
                            + " 	FROM itsDetail itsd  "
                            + " 	WHERE itsd.code = :code  "
                            + " 	AND itsd.applicableFrom = :applicableFromDate  "
                            + " 	GROUP BY itsd.applicableFrom, itsd.overHeadCode, itsd.locationCode, itsd.site_Code  "
                            + " 	) vIts  "
                            + " LEFT JOIN overHeadMast om ON om.code = vIts.overHeadCode  "
                            + " GROUP BY vIts.applicableFrom, vIts.overHeadCode, vIts.locationCode  "
                            + " HAVING  CASE WHEN count(*) > 1 AND MIN(vIts.site_Code) =  0 AND Max(vIts.sr) > 1 THEN 1 ELSE 0 END = 1 ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    rsTemp = psTemp.executeQuery();
                    errorMessage = "";
                    while (rsTemp.next()) {
                        if (!DmShared.xNull(errorMessage).trim().equals("")) {
                            errorMessage += "<br>";
                        }
                        errorMessage += "ITS "
                                + " for location: " + DmShared.xNull(DmLocation.getValueFrom(DmShared.iNull(rsTemp.getString("locationCode")).intValue()).getValue())
                                + " and overhead: " + DmShared.xNull(rsTemp.getString("overHeadName"))
                                + " is already exists for all sites.";

                    }
                    rsTemp.close();
                    psTemp.close();
                    if (!DmShared.xNull(errorMessage).trim().equals("")) {
                        status = DmImportStatus.ERROR.getId();
                        throw new UnsupportedOperationException(errorMessage);
                    }

                    spBegin.commit();
                    rtnStatus = DmImportStatus.SUCCESS.getId();
                }
            }
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                refreshDataStructure.writeLine(e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
