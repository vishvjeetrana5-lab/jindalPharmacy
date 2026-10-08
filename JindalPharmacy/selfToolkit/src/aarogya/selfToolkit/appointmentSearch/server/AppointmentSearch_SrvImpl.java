package aarogya.selfToolkit.appointmentSearch.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.appointmentSearch.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.DiaryStatus;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSearch_SrvImpl extends DmRSSPrivate implements AppointmentSearch_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT cm.subcode as code,sg.name "
                    + " FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " WHERE CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' AND sgn.manualCode='CN' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSitting(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<List<KeyFieldExtended>>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        KeyFieldExtended lstNode;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  hm.subCode, sg.name, dep.name as department "
                    + "		from hkLocationMast hm "
                    + "		left join subGroup sg on sg.subCode = hm.subCode "
                    + "		left join subGroup dep on dep.subCode = hm.departmentCode "
                    + "		where roomCode is null and hm.departmentCode is not null "
                    + "			and sg.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new KeyFieldExtended();
                lstNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("department")));
                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<GrdAppointmentDetails_Mdl>> getAppoinmentSearch(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdAppointmentDetails_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        List<GrdAppointmentDetails_Mdl> lstTemp = new ArrayList<GrdAppointmentDetails_Mdl>();
        GrdAppointmentDetails_Mdl node;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(param.getIsPending()).intValue() == 1) {
                condition += " AND (cd.isStatus NOT IN (1,4) AND cd.regDocId IS NULL) ";
            }
            if (!DmShared.xNull(param.getConsultantTag()).equalsIgnoreCase("")) {
                condition += " AND (cd.consultantCode = :consultantCode or cd.hkLocationCode = :consultantCode)";
            }
            if (!DmShared.xNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND cd.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND cd.v_Date <= :toDate ";
            }

            sqlQuery = "select	cd.docId as cdDocId, cd.v_SNo as cdV_SNo, concat(format(cd.v_Date, 'dd-MMM-yyyy'), ' - ' ,format(cd.timeFrom, '00.00')) as dTime, "
                    + "		pu.patientName, pu.mobile, hl7.shortName as priorityType, cd.isStatus, sgc.name as consultantName, pu.docId as uhidDocId, "
                    + "         pu.recId AS uhidNo, pr.recId AS regNo, cd.tokenNo, cd.v_Date, cd.timeFrom, pra.recId as appoinmentNo "
                    + "		from consultantDiary cd "
                    + "         left join patientRegistration AS pra ON pra.regDocId = cd.docId "
                    + "		left join patientRegistration AS pr ON pr.regDocId = cd.regDocId "
                    + "		left join patientUHID AS pu ON cd.uhid = pu.docId "
                    + "		left join hl7Mast AS hl7 ON cd.priorityTypeCode = hl7.code "
                    + "		left join subGroup as sgc on cd.consultantCode = sgc.subCode "
                    + "		left join voucher_type vt ON cd.v_Type = vt.v_type "
                    + "		where vt.category = '" + pvc.OPD_APPOINTMENT + "' and cd.site_Code = :site_Code and pra.uhid is not null " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!DmShared.xNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromDate", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.xNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("toDate", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getConsultantTag()).equalsIgnoreCase("")) {
                sqlWithParam.setString("consultantCode", DmShared.xNull(param.getConsultantTag()));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAppointmentDetails_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cdDocId")));
                node.setDiaryV_SNo(DmShared.iNull(rsTemp.getString("cdV_SNo")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("tokenNo")));
                node.setDateTime(DmShared.xNull(rsTemp.getString("dTime")));
                node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                node.setPriorityType(DmShared.xNull(rsTemp.getString("priorityType")));
                node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                node.setUhidNo(DmShared.xNull(rsTemp.getString("uhidNo")));
                node.setUhidDocId(DmShared.xNull(rsTemp.getString("uhidDocId")));
                node.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                node.setAppoinmentNo(DmShared.xNull(rsTemp.getString("appoinmentNo")));
                node.setStatus(DiaryStatus.valueOf(DmShared.iNull(rsTemp.getString("isStatus")).intValue()));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
