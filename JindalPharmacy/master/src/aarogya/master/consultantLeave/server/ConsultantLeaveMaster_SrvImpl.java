package aarogya.master.consultantLeave.server;

import aarogya.master.consultantLeave.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultantLeaveMaster_SrvImpl extends DmRSSPrivate implements ConsultantLeaveMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(String consultantCode) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(consultantCode).longValue() != 0) {
                condition += " where consultantcode = " + consultantCode + " ";
            }

            rsTemp = cnTemp.createStatement().executeQuery("Select code From consultantLeaveMast " + condition + " Order By fromDate desc, consultantcode ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResult<ConsultantLeaveMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ConsultantLeaveMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ConsultantLeaveMaster_Mdl mrTemp = new ConsultantLeaveMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  clm.code, clm.consultantcode, sg.name, clm.fromdate, clm.todate, clm.u_Name, clm.preparedBy, "
                    + "         clm.fromTime, clm.toTime, clm.remark, clm.isFullDay "
                    + "         from consultantLeaveMast AS clm   "
                    + "         left join consultantmast AS cm ON clm.consultantcode=cm.subcode "
                    + "         left join subGroup as sg ON cm.subCode = sg.subCode "
                    + "         where clm.code=:code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantcode")));
                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromdate")));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("todate")));
                mrTemp.setFromTime(DmServer.vNull(rsTemp.getString("fromTime"), "00.00"));
                mrTemp.setToTime(DmServer.vNull(rsTemp.getString("toTime"), "00.00"));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setFullDay(DmShared.iNull(rsTemp.getString("isFullDay")).intValue() == 1);
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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

            sqlQuery = "Delete From consultantLeaveMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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
    public DmResult<String> saveRecord(ConsultantLeaveMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ConsultantLeaveMaster_Vld.chkSave(mrTemp, DmServer.getServerDateTime()[0], DmServer.getServerDateTime()[1]);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "consultantLeaveMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = " SELECT COUNT(consultantcode) AS cnt FROM consultantLeaveMast "
                    + " WHERE consultantcode = :consultant AND code <> :docId and( "
                    + "	(fromDate <= :fromDate AND toDate >= :fromDate) OR  "
                    + "	(fromDate <= :toDate AND toDate >= :toDate) OR  "
                    + "	(fromDate >= :fromDate AND fromDate <= :toDate)) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("consultant", DmShared.iNull(mrTemp.getConsultantCode()).toString());
            psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
            psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    throw new DmException(0, "Leave has been already taken on given schedule.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "insert into consultantLeaveMast(code, consultantcode, fromdate, todate, preparedby, u_entdt, fromTime, toTime, remark, isFullDay) "
                        + " values (:code,:consultant,:fromdate,:todate,:uName,:uEntDt, :fromTime, :toTime, :remark, :isFullDay) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("consultant", DmShared.xNull(mrTemp.getConsultantCode()));
                psTemp.setDate("fromdate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setDate("todate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("uName", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("fromTime", DmShared.vNull(mrTemp.getFromTime()).toString());
                psTemp.setString("toTime", DmShared.vNull(mrTemp.getToTime()).toString());
                psTemp.setString("isFullDay", mrTemp.isFullDay() ? "1" : "0");
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {

                sqlQuery = " Update consultantLeaveMast Set "
                        + " consultantcode = :consultantcode, "
                        + " fromdate = :fromdate, "
                        + " todate = :todate, "
                        + " fromTime = :fromTime, "
                        + " toTime = :toTime, "
                        + " isFullDay = :isFullDay, "
                        + " remark = :remark, "
                        + " U_Name = :uName, "
                        + " U_EntDt = :uEntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("consultantcode", DmShared.xNull(mrTemp.getConsultantCode()));
                psTemp.setDate("fromdate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setDate("todate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("uName", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("fromTime", DmShared.vNull(mrTemp.getFromTime()).toString());
                psTemp.setString("toTime", DmShared.vNull(mrTemp.getToTime()).toString());
                psTemp.setString("isFullDay", mrTemp.isFullDay() ? "1" : "0");
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /////////////////////////////////////////////////////////////////////////////////////
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCName(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select cm.subcode as code,sg.name "
                    + " from consultantmast cm "
                    + " left join subGroup as sg ON cm.subCode = sg.subCode "
                    + " left join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " left join subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " where CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' AND sgn.manualCode='CN' ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<Find_Mdl>> getFind(String consultantCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(consultantCode).longValue() != 0) {
                condition += " where clm.consultantcode = :consultantCode ";
            }

            sqlWithParam.setSQL(" select clm.code, sg.name, clm.fromdate, clm.todate, clm.remark, "
                    + " format(clm.fromTime, '00.00') as fromTime, format(clm.toTime, '00.00') as toTime, "
                    + " case when isnull(isFullDay, 1) = 1  then 'Full Day' else 'Half Day' end as isFullDay "
                    + " from consultantLeaveMast AS clm   "
                    + " left join consultantmast AS cm ON clm.consultantcode=cm.subcode"
                    + " left join subGroup as sg ON cm.subCode = sg.subCode " + condition);
            if (DmShared.iNull(consultantCode).longValue() != 0) {
                sqlWithParam.setString("consultantCode", DmShared.xNull(consultantCode));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromdate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("todate")));
                listNode.setFromTime(DmServer.vNull(rsTemp.getString("fromTime"), "00.00"));
                listNode.setToTime(DmServer.vNull(rsTemp.getString("toTime"), "00.00"));
                listNode.setFullDay(DmShared.xNull(rsTemp.getString("isFullDay")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
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
}
