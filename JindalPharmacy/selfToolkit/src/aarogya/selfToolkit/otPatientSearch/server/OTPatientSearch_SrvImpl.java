package aarogya.selfToolkit.otPatientSearch.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.selfToolkit.otPatientSearch.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.DiaryStatus;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OTPatientSearch_SrvImpl extends DmRSSPrivate implements OTPatientSearch_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String filterCategory = pvc.OT_SURGICAL_REGISTRATION;

    @Override
    public DmResultPageing<List<GrdOTPatientSearch_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, OTSearchValue_Mdl param) {
        DmResultPageing<List<GrdOTPatientSearch_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdOTPatientSearch_Mdl> lstTemp = new ArrayList<>();
        GrdOTPatientSearch_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select ppr.recId AS surgicalNo ,ppr.docId as surgicalDocId, "
                    + "  rm.roomNo, rm.code, pu.docId, pu.recId recIdUHID,"
                    + "  pr.regDocId, pr.recId recIdReg, "
                    + "  pu.patientAliasName patient, pu.guardianName guardian, pu.mobile, pu.v_No, "
                    + "  dep.name department, org.displayName organization, vt.short_Name "
                    + "  FROM patientPrescriptionRef AS ppr  "
                    + "  left JOIN patientRegistration AS pr ON pr.regDocId = ppr.regDocId  "
                    + "  left join roomMast as rm on  rm.code = pr.roomCode  "
                    + "  left join voucher_Type vt on vt.v_Type = ppr.v_Type  "
                    + "  left join patientUHID pu on pu.docId = pr.uhid  "
                    + "  left join subGroup org on org.subCode = pr.organizationCode  "
                    + "  left join subGroup dep on dep.subCode = pr.departmentCode"
                    + "  where   vt.category in ('" + filterCategory + "') and ppr.isStatus = :isStatus"
                    + "  and ppr.site_Code = :siteCode ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("isStatus", DmShared.iNull(DiaryStatus.Confirmed.getOptionValue()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdOTPatientSearch_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomNo")));
                listNode.setRecIdIPD(DmShared.xNull(rsTemp.getString("recIdReg")));
                listNode.setDocIdUHID(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setRecIdUHID(DmShared.xNull(rsTemp.getString("recIdUHID")));

                listNode.setPatient(DmShared.xNull(rsTemp.getString("patient")));
                listNode.setGuardian(DmShared.xNull(rsTemp.getString("guardian")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setOrganizationName(DmShared.xNull(rsTemp.getString("organization")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setDocIdIPD(DmShared.xNull(rsTemp.getString("regDocid")));
                listNode.setSurgicalNo(DmShared.xNull(rsTemp.getString("surgicalNo")));
                listNode.setSurgicalDocId(DmShared.xNull(rsTemp.getString("surgicalDocId")));
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
}
