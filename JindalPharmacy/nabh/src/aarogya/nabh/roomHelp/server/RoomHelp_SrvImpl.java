package aarogya.nabh.roomHelp.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.nabh.roomHelp.shared.Rd_Mdl;
import aarogya.nabh.roomHelp.shared.PatientHistorySearch_Mdl;
import aarogya.nabh.roomHelp.shared.DefaultValue_Mdl;
import aarogya.nabh.roomHelp.shared.GrdRoomHelp_Mdl;
import aarogya.nabh.roomHelp.shared.RoomHelp_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import aarogya.nabh.roomHelp.shared.RoomHelp_Srv;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;

public class RoomHelp_SrvImpl extends DmRSSPrivate implements RoomHelp_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.NABH + "'";

    @Override
    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl) {
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

            sqlQuery = " select Count(isCancelled) as cnt from nabhIndicator where docId = :docId AND isCancelled = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                    throw new DmException(0, "Record is already discarded.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " UPDATE nabhIndicator SET "
                    + " isCancelled = 1 "
                    + " WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            psTemp.executeUpdate();
            psTemp.close();

            if (modifyReasonMdl != null) {
                ModifyReason_Server.save(modifyReasonMdl, strKey, DmServer.getSsnUserName(this), cnTemp);
            }

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
    public DmResult<String> saveGUIRecord(Rd_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, userName = "";
        boolean blnAdd = false;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            userName = DmServer.getSsnUserName(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("nabhIndicator", "docId", mrTemp.getSharedKeyFieldCode(), "recId", mrTemp.getRecId(), mrTemp.getV_Date(), mrTemp.getVoucherTypeTag(), voucherPrefix, cnTemp, this, true));
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiCode(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmServer.getSsnSiteCode(this), cnTemp);
            //Insert Record into NABH_Indeicator Table
            if (blnAdd) {
                if (DmShared.iNull(mrTemp.getGuiCode()).longValue() == 0) {
                    throw new DmException(0, "Please select a GUI.");
                }

                sqlQuery = "insert into nabhIndicator (docId, v_Type, v_No, recId, v_Prefix, v_Date, recIdPrefix, v_Time, guiConfigCode, u_EntDt, "
                        + "         preparedBy, site_Code, regDocId,natureCode) "
                        + " values (:docId,:vType,:vNo,:recId,:vPrefix,:vDate,:recIdPrefix,:vTime,:guiConfigCode, :uEntDt,:userName,:siteCode, "
                        + "         :regDocId,:natureCode)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("vType", mrTemp.getVoucherTypeTag());
                psTemp.setString("vNo", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
                psTemp.setString("vPrefix", DmShared.xNull(voucherPrefix));
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getV_Date()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("vTime", DmShared.xNull(mrTemp.getV_Time()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiCode()));
                psTemp.setString("userName", userName);
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocId()).toString());
                psTemp.setString("natureCode", DmShared.iNull(mrTemp.getMappingNature().getId()).toString());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            DmGUI_Server.saveViaConfigCode(mrTemp.getGui_Mdl().getGuiRowList(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmServer.getSsnSiteCode(this), cnTemp);
            if (mrTemp.getModifyReason() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReason(), mrTemp.getSharedKeyFieldCode(), userName, cnTemp);
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
    public DmResult<RoomHelp_Mdl> getPatientDetail(String regDocId, String regRecID) {
        DmResult<RoomHelp_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        RoomHelp_Mdl mrTemp = new RoomHelp_Mdl();
        String[] currentDateTime;
        String sqlQuery, condition = "";
        dmrTemp.setData(mrTemp);

        try {
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(regDocId).longValue() != 0) {
                condition = " and pr.regDocId = :regDocId ";
            } else if (!DmShared.xNull(regRecID).trim().equalsIgnoreCase("")) {
                condition = " and pr.recId = :regRecID ";
            } else {
                condition += " and 1 = 0 ";
            }

            sqlQuery = "Select  TOP (1) pr.regDocId, pr.recId as regNo, pr.roomCode "
                    + "         from patientRegistration as pr "
                    + "         left join patientUHID AS pu ON pu.docId = pr.uhid"
                    + "         where (pr.isCancelled = 0) AND (pu.isCancelled <> 1) and isNull(pu.approvedBy,'') <> '' "
                    + "         and pr.site_code = :site_Code " + condition
                    + "         order by pr.v_Date DESC, pr.regTime DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(regDocId).longValue() != 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            } else if (!DmShared.xNull(regRecID).trim().equalsIgnoreCase("")) {
                psTemp.setString("regRecID", DmShared.xNull(regRecID));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setRoomCode(DmShared.iNull(rsTemp.getString("roomCode")).toString());
            } else {
                throw new DmException(0, "No record found. Try Again.");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new DmException(0, "No record found. Try Again.");
            } else {
                getNABHRecord(regDocId, cnTemp);
            }

            getPatientDetail(mrTemp, cnTemp);

            mrTemp.getGrdGUIEntry_Mdl().addAll(getNABHRecord(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), cnTemp));

            dmrTemp.setValue1(currentDateTime[0]);
            cnTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (SQLException | DmException e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void getPatientDetail(RoomHelp_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = " SELECT "
                    + "         pu.recId AS uhidRecID,pu.docId AS uhidDocID,CONCAT(pu.patientInitial,' ',pu.patientName) AS patientName, "
                    + "         pr.v_date as admittingDate,pr.regTime as admittingTime, "
                    + "         CONCAT(pu.guardianInitial,' ',pu.guardianName) AS guardianName, "
                    + "         hl7.name AS gender,pu.address1,pu.address2,pu.dobDisplay,pr.recId AS opdRecid, "
                    + "         pr.regDocId AS OPDDocID, "
                    + "         pr.organizationCode,caseMast.name AS casemastName, "
                    + "         sg.name AS organizationName, "
                    + "         rm.code AS roomCode, rm.roomDescription AS roomName, "
                    + "         CASE WHEN ISNULL(sgcmc.initial, '') <> '' THEN CONCAT(sgcmc.initial, ' ', sgcmc.name) ELSE sgcmc.name END AS consultantName, "
                    + "         pu.mobile,pu.dobDisplay AS age,pr.v_Date AS regDate,pr.regTime, "
                    + "         DATEDIFF(DAY, pr.v_Date, getDate ()) AS noOfDays "
                    + "         FROM patientUHID AS pu "
                    + "         LEFT JOIN patientRegistration AS pr ON pr.uhid = pu.docId "
                    + "		LEFT JOIN patientUHID2 pu2 ON pu2.uhid= pu.docId"
                    + "         LEFT JOIN hl7Mast hl7 ON pu2.genderCode= hl7.code "
                    + "         LEFT JOIN caseMast ON pr.caseCode = caseMast.code "
                    + "         LEFT JOIN roomMast AS rm ON pr.roomCode = rm.code "
                    + "         LEFT JOIN organizationMast AS om ON pr.organizationCode = om.subcode "
                    + "         LEFT JOIN subGroup sg ON om.subCode = sg.subCode "
                    + "		LEFT join subGroup as sgcmc ON pr.consultantCode = sgcmc.subCode  "
                    + "  WHERE (pu.isCancelled = 0) AND (pr.isCancelled = 0)  AND (pr.regDocId = :regDocId)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("uhidDocID")));
                mrTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhidRecID")));
                mrTemp.setDisplayDOB(DmShared.xNull(rsTemp.getString("dobDisplay")));
                mrTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address1")) + DmShared.xNull(rsTemp.getString("address2")));
                mrTemp.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                mrTemp.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setCaseText(DmShared.xNull(rsTemp.getString("casemastName")));
                mrTemp.setOrganizationText(DmShared.xNull(rsTemp.getString("organizationname")));
                mrTemp.setConsultantText(DmShared.xNull(rsTemp.getString("consultantName")));
                mrTemp.setAge(DmShared.xNull(rsTemp.getString("age")));
                mrTemp.setRegDate(DmShared.dNull(rsTemp.getDate("regDate")));
                mrTemp.setRegTime(DmShared.xNull(rsTemp.getString("regTime")));
                mrTemp.setRoomNo(DmShared.xNull(rsTemp.getString("roomName")));
                mrTemp.setDaysOfOPD(DmShared.iNull(rsTemp.getString("noOfDays")).toString() + " Days");
            }
            psTemp.close();
            rsTemp.close();

            mrTemp.setVoucherTypeTag(DmServer.getVType(pvc.NABH, cnTemp));

        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
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
    }

    private List<GrdRoomHelp_Mdl> getNABHRecord(String regDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdRoomHelp_Mdl> listNode = new ArrayList<GrdRoomHelp_Mdl>();
        GrdRoomHelp_Mdl node;

        try {
            sqlQuery = " SELECT   gc.name AS guiName,gtm.name AS templateName,gc.code AS guiCode,gtm.code AS templateCode,ni.docId, "
                    + " ni.v_Type,ni.v_Date,ni.v_Time,ni.regDocId,  ni.preparedBy,ni.site_Code, FORMAT(ni.u_EntDt, 'dd-MMM-yyyy HH.mm') AS lastModified,  "
                    + " ni.guiConfigCode,ni.guiConfigCode,ni.isCancelled,  ni.u_Name, ni.recId  "
                    + " FROM nabhIndicator ni "
                    + " LEFT JOIN guiConfig AS gc ON gc.code = ni.guiConfigCode "
                    + " LEFT JOIN guiConfigApplicableTo AS gca ON gca.code = gc.code "
                    + " AND gca.site_Code='" + DmServer.getSsnSiteCode(this) + "'"
                    + " LEFT JOIN guiTemplateMast AS gtm ON gtm.code = gca.guiTemplateCode "
                    + "  LEFT JOIN patientPrescriptionGUIMapping as ppgm on gca.guiTemplateCode = ppgm.guiTemplateCode"
                    + " LEFT JOIN voucher_Type AS vt ON vt.v_Type = ni.v_Type "
                    + " where ni.regDocId=:regDocId "
                    + " AND vt.category in (" + vCat + ") "
                    + " order by ni.v_Date Desc,ni.v_Time Desc";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdRoomHelp_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setRefRecId(DmShared.xNull(rsTemp.getString("recId")).trim());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("v_Type")));
                node.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setTime(DmShared.xNull(rsTemp.getString("v_Time")));
                node.setTemplateCode(DmShared.xNull(rsTemp.getString("templateCode")));
                node.setTemplateName(DmShared.xNull(rsTemp.getString("templateName")));
                node.setGuiName(DmShared.xNull(rsTemp.getString("guiName")));
                node.setGuiCode(DmShared.xNull(rsTemp.getString("guiCode")));
                node.setLastModifiedBy(DmShared.xNull(rsTemp.getString("lastModified")));
                node.setU_Name(DmShared.xNull(rsTemp.getString("u_Name")));
                node.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                if (DmShared.xNull(rsTemp.getString("isCancelled")).equalsIgnoreCase("0")) {
                    node.setColorStatus("0");
                } else {
                    node.setColorStatus("1");
                }
                listNode.add(node);
            }

            rsTemp.close();
            psTemp.close();
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
            } catch (Exception e) {
            }
        }
        return listNode;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues(String templateCode) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            defaultValue.setVoucherCategory(vCat);

            sqlQuery = "select gc.name as guiName from guiConfig gc"
                    + "		LEFT JOIN guiConfigApplicableTo AS gca ON gca.code = gc.code  "
                    + "         LEFT JOIN patientPrescriptionGUIMapping AS gppm ON gppm.guiTemplateCode = gca.guiTemplateCode "
                    + "		where gppm.guiTemplateCode = :guiTemplateCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("guiTemplateCode", DmShared.xNull(templateCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setGuiName(DmShared.xNull(rsTemp.getString("guiName")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT v_Type, description FROM voucher_Type "
                    + "WHERE (category = " + vCat + ") AND (isActive <> 0) "
                    + "ORDER BY description OFFSET 0 ROWS FETCH NEXT 1 ROWS only ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeTag(DmShared.iNull(rsTemp.getString("v_Type")).toString());
                defaultValue.setVoucherTypeText(DmShared.xNull(rsTemp.getString("description")));

            }
            rsTemp.close();
            psTemp.close();

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setCurrentTime(DmServer.getServerDateTime()[1]);

            dmrTemp.setData(defaultValue);

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
    public DmResult<Rd_Mdl> getGUIData(String regDocId, String docId, String templateCode, MappingNature mappingNature) {
        DmResult<Rd_Mdl> dmrTemp = new DmResult();
        Rd_Mdl mrTemp = new Rd_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(regDocId).longValue() != 0) {
                condition = " ni.regDocId = :regDocId and gppm.guiTemplateCode = :templateCode  ";
            } else if (DmShared.iNull(docId).longValue() != 0) {
                condition = " ni.docId = :docId ";
            }

            sqlQuery = "SELECT  ni.docId AS docId, ni.recId, ni.v_Type AS v_Type,ni.v_Date AS v_Date,"
                    + "                   ni.v_Time AS v_Time,ni.regDocId AS regDocId,ni.u_EntDt AS lastModifiedBy,"
                    + "                   ni.u_Name AS userName,ni.site_Code AS sitecode,ni.guiConfigCode AS guiConfigCode,gc.name AS guiName,gppm.guiTemplateCode, "
                    + "                   gtm.name AS templateName,ni.preparedBy,ni.isCancelled,gppm.reportNamePrefix  "
                    + "                   FROM nabhIndicator ni  "
                    + "                   LEFT JOIN guiConfig AS gc ON gc.code = ni.guiConfigCode "
                    + "                   LEFT JOIN guiConfigApplicableTo AS gca ON gca.code = gc.code  "
                    + "                   LEFT JOIN patientPrescriptionGUIMapping AS gppm ON gppm.guiTemplateCode = gca.guiTemplateCode "
                    + "			  LEFT JOIN guiTemplateMast gtm on gppm.guiTemplateCode = gtm.code"
                    + "                   LEFT JOIN voucher_Type AS vt on vt.v_Type=ni.v_Type "
                    + "                   where ni.natureCode = :natureCode and " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("natureCode", DmShared.iNull(mappingNature.getId()).toString());
            if (DmShared.iNull(regDocId).longValue() != 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
                psTemp.setString("templateCode", DmShared.xNull(templateCode));
            } else if (DmShared.iNull(docId).longValue() != 0) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                if (DmShared.iNull(docId).longValue() != 0) {
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")).trim());
                }

                mrTemp.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setV_Time(DmShared.xNull(rsTemp.getString("v_Time")));
                mrTemp.setVoucherTypeTag(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setGuiCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setGuiName(DmShared.xNull(rsTemp.getString("guiName")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("userName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setIsCancelled(DmShared.xNull(rsTemp.getString("isCancelled")));
                mrTemp.setReportNamePrefix(DmShared.xNull(rsTemp.getString("reportNamePrefix")));
                mrTemp.setTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));

            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(regDocId).longValue() != 0) {
                mrTemp.setGui_Mdl(DmGUI_Server.getGUIViaConfigCode(DmShared.xNull(mrTemp.getGuiCode()), regDocId, DmServer.getSsnSiteCode(this), cnTemp));
            } else if (DmShared.iNull(docId).longValue() != 0) {
                mrTemp.setGui_Mdl(DmGUI_Server.getGUIViaConfigCode(DmShared.xNull(mrTemp.getGuiCode()), docId, DmServer.getSsnSiteCode(this), cnTemp));
            }

            //mrTemp.getGuiRowList().addAll(DmGUI_Server.getGUIViaConfigCode(DmShared.xNull(mrTemp.getGuiCode()), regDocId, DmServer.getSsnSiteCode(this), cnTemp).getGuiRowList());
            dmrTemp.setData(mrTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTemplateName(DmHelper helper, boolean blnToEndOfFile, MappingNature mappingNature) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select max(guimapping.guiTemplateCode) as code,Max(gui.name) as name "
                    + "		from  patientPrescriptionGUIMapping as guimapping "
                    + "		left join guiTemplateMast as gui on gui.code=guimapping.guiTemplateCode "
                    + "		where natureCode = :natureCode "
                    + "		group by gui.code ");
            sqlWithParam.setString("natureCode", DmShared.iNull(mappingNature.getId()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<GrdRoomHelp_Mdl>> getPatientHistory(DmHelper helper, boolean blnToEndOfFile, PatientHistorySearch_Mdl svm) {
        DmResultPageing<List<GrdRoomHelp_Mdl>> rstTemp = new DmResultPageing<List<GrdRoomHelp_Mdl>>();
        List<GrdRoomHelp_Mdl> lstTemp = new ArrayList<GrdRoomHelp_Mdl>();
        GrdRoomHelp_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  ni.docId AS docId, ni.recId, ni.regDocId AS regDocId,ni.u_EntDt AS lastModifiedBy, "
                    + "         ni.u_Name AS userName,ni.site_Code AS sitecode,ni.guiConfigCode AS guiConfigCode, "
                    + "         gtm.name AS templateName,ni.preparedBy,ni.isCancelled "
                    + "         FROM nabhIndicator ni  "
                    + "         LEFT JOIN guiConfig AS gc ON gc.code = ni.guiConfigCode "
                    + "         LEFT JOIN guiConfigApplicableTo AS gca ON gca.code = gc.code  "
                    + "         LEFT JOIN guiTemplateMast AS gtm ON gtm.code = gca.guiTemplateCode "
                    + "         LEFT JOIN voucher_Type AS vt on vt.v_Type=ni.v_Type "
                    + "         where  ni.regDocId = :regDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(svm.getRegDocId()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdRoomHelp_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")).trim());
                node.setTemplateName(DmShared.xNull(rsTemp.getString("templateName")));
                node.setTemplateCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                node.setTemplateCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                node.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("userName")));
                node.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                node.setIsCancelled(DmShared.xNull(rsTemp.getString("isCancelled")));
                node.setColorStatus("0");

                lstTemp.add(node);
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

}
