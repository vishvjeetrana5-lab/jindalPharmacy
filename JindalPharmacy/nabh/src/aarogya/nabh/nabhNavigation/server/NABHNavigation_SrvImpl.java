package aarogya.nabh.nabhNavigation.server;

import aarogya.nabh.nabhNavigation.shared.NABHNavigation_Mdl;

import aarogya.nabh.nabhNavigation.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import aarogya.nabh.nabhNavigation.shared.NABHNavigation_Srv;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.ArrayList;
import java.util.List;

public class NABHNavigation_SrvImpl extends DmRSSPrivate implements NABHNavigation_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

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
    public DmResult<NABHNavigation_Mdl> getPatientDetail(String regDocId, String regRecID, String voucherCat) {
        DmResult<NABHNavigation_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        NABHNavigation_Mdl mrTemp = new NABHNavigation_Mdl();
        String[] currentDateTime;
        String sqlQuery, condition = "";
        dmrTemp.setData(mrTemp);

        try {
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(regDocId).longValue() != 0l) {
                condition = " and pr.regDocId = :regDocId ";
            } else if (!DmShared.xNull(regRecID).trim().equalsIgnoreCase("")) {
                condition = " and pr.recId = :regRecID ";
            }
            sqlQuery = "Select  pr.regDocId, pr.recId as regNo,vt.category "
                    + "         from patientRegistration as pr "
                    + "         left join Voucher_Type vt on pr.v_Type = vt.v_Type"
                    + "         left join patientUHID AS pu ON pu.docId = pr.uhid"
                    + "         where (pr.isCancelled = 0) AND (pu.isCancelled <> 1) and isNull(pu.approvedBy,'') <> '' "
                    + "         and pr.site_code = :site_Code and vt.category in (:category)  " + condition
                    + "         order by pr.v_Date DESC, pr.regTime DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("category", DmShared.xNull(voucherCat));
            if (DmShared.iNull(regDocId).longValue() != 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            } else if (!DmShared.xNull(regRecID).trim().equalsIgnoreCase("")) {
                psTemp.setString("regRecID", DmShared.xNull(regRecID));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setTypeTag(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setTypeText(DmShared.xNull(rsTemp.getString("category")));
            } else {
                throw new DmException(0, "No record found. Try Again.");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0l) {
                throw new DmException(0, "No record found. Try Again.");
            }

            getPatientDetail(mrTemp, cnTemp);

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

    private void getPatientDetail(NABHNavigation_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = " SELECT "
                    + "         pu.recId AS uhidRecID,pu.docId AS uhidDocID,CONCAT(pu.patientInitial,' ',pu.patientName) AS patientName, "
                    + "         pr.v_date as admittingDate,pr.regTime as admittingTime, "
                    + "         CONCAT(pu.guardianInitial,' ',pu.guardianName) AS guardianName, "
                    + "         hl7.name AS gender,pu.address1,pu.address2,pu.dobDisplay,pr.recId AS opdRecid, "
                    + "         pr.regDocId AS OPDDocID,pr.organizationCode,sg.name AS organizationName, "
                    + "         CASE WHEN ISNULL(sgcmc.initial, '') <> '' THEN CONCAT(sgcmc.initial, ' ', sgcmc.name) ELSE sgcmc.name END AS consultantName, "
                    + "         pu.mobile,pu.dobDisplay AS age,pr.v_Date AS regDate,pr.regTime, "
                    + "         DATEDIFF(DAY, pr.v_Date, getDate ()) AS noOfDays,vt.category "
                    + "         FROM patientUHID AS pu "
                    + "         LEFT JOIN patientRegistration AS pr ON pr.uhid = pu.docId "
                    + "          Left JOin Voucher_Type vt on pr.v_Type = vt.v_Type"
                    + "		LEFT JOIN patientUHID2 pu2 ON pu2.uhid= pu.docId"
                    + "         LEFT JOIN hl7Mast hl7 ON pu2.genderCode= hl7.code "
                    + "         LEFT JOIN caseMast ON pr.caseCode = caseMast.code "
                    + "         LEFT JOIN roomMast AS rm ON pr.roomCode = rm.code "
                    + "         LEFT JOIN organizationMast AS om ON pr.organizationCode = om.subcode "
                    + "         LEFT JOIN subGroup sg ON om.subCode = sg.subCode "
                    + "		LEFT join subGroup as sgcmc ON pr.consultantCode = sgcmc.subCode  "
                    + "         WHERE pu.isCancelled = 0 AND pr.isCancelled = 0 AND vt.category= :category "
                    + "         AND pr.regDocId = :regDocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("category", DmShared.xNull(mrTemp.getTypeTag()).toString());
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
                mrTemp.setOrganizationText(DmShared.xNull(rsTemp.getString("organizationname")));
                mrTemp.setConsultantText(DmShared.xNull(rsTemp.getString("consultantName")));
                mrTemp.setAge(DmShared.xNull(rsTemp.getString("age")));
                mrTemp.setRegDate(DmShared.dNull(rsTemp.getDate("regDate")));
                mrTemp.setRegTime(DmShared.xNull(rsTemp.getString("regTime")));
                mrTemp.setDaysOfOPD(DmShared.iNull(rsTemp.getString("noOfDays")).toString() + " Days");
                mrTemp.setTypeTag(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setTypeText(DmShared.xNull(rsTemp.getString("category")));
            }
            psTemp.close();
            rsTemp.close();

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.EMERGENCY_OPD + "'");
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.IPD + "'");
            listNode.setSharedKeyFieldName("IPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.LAB_REGISTRATION + "'");
            listNode.setSharedKeyFieldName("LAB");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.DAY_CARE + "'");
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("'" + pvc.EMERGENCY + "'");
            listNode.setSharedKeyFieldName("EMRG");
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
