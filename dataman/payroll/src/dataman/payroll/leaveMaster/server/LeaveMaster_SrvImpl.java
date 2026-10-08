package dataman.payroll.leaveMaster.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.leaveMaster.shared.*;
import dataman.payroll.self.shared.PREnum.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaveMaster_SrvImpl extends DmRSSPrivate implements LeaveMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery(" select code from attendenceCodeDefinitionMast where type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId() + " order by name ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<String>> getNameSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select name  From attendenceCodeDefinitionMast where type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId());
            helper.addSort("Name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<String>> getShortNameSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" Select manualcode as shortName From attendenceCodeDefinitionMast where type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId());
            helper.addSort("shortName", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResult<LeaveMaster_Mdl> getMoveRec(String strKey) {
        DmResult<LeaveMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        LeaveMaster_Mdl mrTemp = new LeaveMaster_Mdl();
        GrdDate_Mdl grdDate_Mdl;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT  code,  name,  manualCode,  typeCode ,  preparedBy,  u_Name, natureCode, isActive"
                    + " , case when isActive='1' then 'Yes' else 'No' end as isActiveName "
                    + "     FROM attendenceCodeDefinitionMast   "
                    + "     where code=:code and site_Code=:siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setLeave_nature(LEAVE_NATURE.getViaKey(DmShared.iNull(rsTemp.getString("natureCode")).intValue()));
                mrTemp.setLeave_type(LEAVE_TYPE.getViaKey(DmShared.iNull(rsTemp.getString("typeCode")).intValue()));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setIsActiveName(DmShared.xNull(rsTemp.getString("isActiveName")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "  select code, fromDate, toDate "
                    + "          from attendenceCodeDefinitionMastDetail  "
                    + "            where code=:code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                grdDate_Mdl = new GrdDate_Mdl();
                grdDate_Mdl.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                grdDate_Mdl.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                grdDate_Mdl.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                mrTemp.getGrdLst().add(grdDate_Mdl);
            }
            psTemp.close();
            rsTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From attendenceCodeDefinitionMastDetail Where Code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());

            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From attendenceCodeDefinitionMast Where Code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());

            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
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

    @Override
    public DmResult<String> saveRecord(LeaveMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());

        try {
            LeaveMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "attendenceCodeDefinitionMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select  count(name) as cnt "
                    + "         from attendenceCodeDefinitionMast  "
                    + "         where lower(name) = :name and Code <> :code and site_code=:sitecode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(100, "Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  count(manualCode) as cnt "
                    + "         from attendenceCodeDefinitionMast  "
                    + "         where lower(manualCode) = :shortName and Code <> :code and site_code=:sitecode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).toLowerCase().trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(200, "Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            
            for (GrdDate_Mdl grdMdl : mrTemp.getGrdLst()) {
                
            }

//            sqlQuery = "select  count(code) as cnt "
//                    + "         from attendenceCodeDefinitionMastDetail "
//                    + "         where lower(name) = :name and Code = :code and site_code=:sitecode";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
//            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
//            rsTemp = psTemp.executeQuery();
//            rsTemp.next();
//            if (rsTemp.getLong("cnt") > 0) {
//                throw new DmException(100, "Name already exists.");
//            }
//            rsTemp.close();
//            psTemp.close();
            if (blnAdd) {
                sqlQuery = "INSERT INTO attendenceCodeDefinitionMast (code, name, manualCode, typeCode, natureCode, u_EntDt, preparedBy, site_Code, isActive, type_Leave_Attendance, plusMinus, days)  "
                        + "     VALUES             (:code, :name, :manualCode, :typeCode, :natureCode, :u_EntDt , :preparedBy, :siteCode, :isActive, :type_Leave_Attendance, :plusMinus, :days) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("typeCode", DmShared.iNull(mrTemp.getLeave_type().getId()).toString());
                psTemp.setString("natureCode", DmShared.iNull(mrTemp.getLeave_nature().getId()).toString());
                psTemp.setString("days", "1");
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("sitecode", DmServer.getSsnSiteCode(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getIsActive()).toString());
                psTemp.setString("type_Leave_Attendance", DmServer.vNull(TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId()));
                psTemp.setString("plusMinus", DmShared.xNull(mrTemp.getPlusMinus()));
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "update attendenceCodeDefinitionMast set "
                        + " name         = :name, "
                        + " natureCode   = :natureCode, "
                        + " typeCode     = :typeCode,    "
                        + " plusMinus    = :plusMinus,   "
                        + " days         = :days,   "
                        + " u_Name       = :uName, "
                        + " u_EntDt      = :uEntDt, "
                        + " isActive      = :isActive "
                        + " Where code   = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("typeCode", DmShared.iNull(mrTemp.getLeave_type().getId()).toString());
                psTemp.setString("natureCode", DmShared.iNull(mrTemp.getLeave_nature().getId()).toString());
                psTemp.setString("days", "1");
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getIsActive()).toString());
                psTemp.setString("plusMinus", DmShared.xNull(mrTemp.getPlusMinus()));
                psTemp.executeUpdate();
                psTemp.close();

            }

            /*=================================< Delete from LVMD >=============================================================*/
            sqlQuery = "Delete From attendenceCodeDefinitionMastDetail  Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int sNo = 0;
            for (GrdDate_Mdl grdMdl : mrTemp.getGrdLst()) {
                if (DmShared.iNull(grdMdl.getDocID()).longValue() == 0) {
                    grdMdl.setDocID(DmServer.getSerialNoBigInt(this, "attendenceCodeDefinitionMastDetail", cnTemp));
                    if (!DmShared.xNull(grdMdl.getFromDate()).equalsIgnoreCase("")) {
                        sNo++;
                        sqlQuery = "insert into attendenceCodeDefinitionMastDetail  (code, docId, fromDate, toDate) "
                                + "      values(:code, :docId,:fromDate, :toDate)  ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("docID", DmShared.foriegnKey(grdMdl.getDocID()));
                        psTemp.setDate("fromDate", DmServer.convertDate(grdMdl.getFromDate()));
                        psTemp.setDate("toDate", DmServer.convertDate(grdMdl.getToDate()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }else {
                    sqlQuery = "update attendenceCodeDefinitionMastDetail set "
                                + " code=:code"
                            + " , fromDate=:fromDate" 
                            + ", toDate=:toDate  "
                            + " where docid=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("docID", DmShared.foriegnKey(grdMdl.getDocID()));
                        psTemp.setDate("fromDate", DmServer.convertDate(grdMdl.getFromDate()));
                        psTemp.setDate("toDate", DmServer.convertDate(grdMdl.getToDate()));
                        psTemp.executeUpdate();
                        psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (LEAVE_NATURE status : LEAVE_NATURE.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(status.getId()).toString());
                listNode.setSharedKeyFieldName(status.getValue());
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (LEAVE_TYPE status : LEAVE_TYPE.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(status.getId()).toString());
                listNode.setSharedKeyFieldName(status.getValue());
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("  SELECT  code,  name,  manualCode, typeCode, natureCode "
                    + "             FROM attendenceCodeDefinitionMast where site_Code=:siteCode and type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setNature(LEAVE_NATURE.getViaKey(DmShared.iNull(rsTemp.getString("natureCode")).intValue()).getValue());
                listNode.setType(LEAVE_TYPE.getViaKey(DmShared.iNull(rsTemp.getString("typeCode")).intValue()).getValue());
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
