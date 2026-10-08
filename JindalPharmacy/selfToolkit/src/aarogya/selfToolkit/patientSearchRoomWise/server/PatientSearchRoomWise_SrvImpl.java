package aarogya.selfToolkit.patientSearchRoomWise.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.patientSearchRoomWise.shared.*;
import aarogya.selfToolkit.patientSearchRoomWise.shared.SearchValueRoomWise_Mdl.FilterType;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientSearchRoomWise_SrvImpl extends DmRSSPrivate implements PatientSearchRoomWise_Srv {

    @Override
    public DmResultPageing<List<GrdPatientDetailsRoomWise_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, SearchValueRoomWise_Mdl param) {
        DmResultPageing<List<GrdPatientDetailsRoomWise_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdPatientDetailsRoomWise_Mdl> lstTemp = new ArrayList<>();
        GrdPatientDetailsRoomWise_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        FilterType filterType;
        String sqlQuery = "", condition = "", sb_Vcat = "", conditionRoomConfirm = "";
        boolean inPatient = false, isAllowSiteApplicable = false, isEmrOrIpd = true, aLLOW_WITHOUT_ROOM_CONFIRMATION=false;
        rstTemp.setData(lstTemp);

        try {
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
    cnTemp = DmServer.getConnection(STServer.getSsnParentDateBase(this));
            sb_Vcat = " '" + new ProjectVoucherCategory().IPD_BILL + "' , '" + new ProjectVoucherCategory().EMERGENCY_BILL + "' ";

            if (param.getFilterType() != null && param.getFilterType().length != 0) {
                for (int i = 0; i < param.getFilterType().length; i++) {
                    filterType = param.getFilterType()[i];
                    if (null != filterType) {
                        switch (filterType) {
                            case NOT_DISCHARGED:
                                inPatient = true;
                                break;
                            case DISCHARGED:
                                inPatient = false;
                                condition += " and pd.regDocId is not null ";
                                break;
                            case NOT_BILLED:
                                inPatient = false;
                                condition += " and (sb.docId is null or sb.docId = sb.regDocId) ";
                                break;
                            case NOT_BILLED_APPROVED:
                                inPatient = false;
                                condition += " and isnull(sb.approvedBy, '') = '' ";
                                break;
                            case EMERGENCY_ONLY:
                                condition += " and vt.category = '" + new ProjectVoucherCategory().EMERGENCY + "' ";

                                isEmrOrIpd = false;
                                break;
                            case IPD_ONLY:
                                condition += " and vt.category = '" + new ProjectVoucherCategory().IPD + "' ";
                                isEmrOrIpd = false;
                                break;
                            case FEMALE_ONLY:
                                condition += " and gen.name = 'female'";
                                break;
                            case TPA:
                                condition += " and om.isTPA = 1 ";
                                break;
                            case NON_TPA:
                                condition += " and om.isTPA = 0 ";
                                break;
                            case ALLOW_SITE_FOR_DIAGNOSTIC:
                                if (!isAllowSiteApplicable) {
//                                    condition += " and pr.site_Code in (" + STServer.getSsnDiagnosticAllowPatientForSite(this) + ") ";
                                }
                                isAllowSiteApplicable = true;
                                break;
                            case ALLOW_SITE_FOR_PHARMACY:
                                if (!isAllowSiteApplicable) {
//                                    condition += " and pr.site_Code in (" + STServer.getSsnPharmacyAllowPatientForSite(this) + ") ";
                                }
                                isAllowSiteApplicable = true;
                                break;
                            case ALLOW_SITE_FOR_OPD:
                                if (!isAllowSiteApplicable) {
//                                    condition += " and pr.site_Code in (" + STServer.getSsnOpdAllowPatientForSite(this) + ") ";
                                }
                                isAllowSiteApplicable = true;
                                break;
                            case ALLOW_SITE_FOR_IPD:
                                if (!isAllowSiteApplicable) {
//                                    condition += " and pr.site_Code in (" + STServer.getSsnIpdAllowPatientForSite(this) + ") ";
                                }
                                isAllowSiteApplicable = true;
                                break;
                            case ALLOW_SITE_FOR_DAYCARE:
                                if (!isAllowSiteApplicable) {
//                                    condition += " and pr.site_Code in (" + STServer.getSsnDayCareAllowPatientForSite(this) + ") ";
                                }
                                isAllowSiteApplicable = true;
                                break;
                            case ALLOW_WITHOUT_ROOM_CONFIRMATION:
                                aLLOW_WITHOUT_ROOM_CONFIRMATION = true;
                                break;
                        }
                    }
                }
            }
            if (!isAllowSiteApplicable) {
                condition += " and pr.site_Code in (" + DmServer.getSsnSiteCode(this) + ") ";
            }

            if (!DmShared.xNull(STServer.getSsnNursingStationCode(this)).equalsIgnoreCase("")) {
                condition += " and rm.nursingStationCode in (" + DmServer.getParamList(":prmNursingStationCode", STServer.getSsnNursingStationCode(this)) + " )";
            }
            if (isEmrOrIpd) {
                condition += " and vt.category in ('" + new ProjectVoucherCategory().IPD + "','" + new ProjectVoucherCategory().EMERGENCY + "')  ";
            }
            if (!aLLOW_WITHOUT_ROOM_CONFIRMATION) {
                conditionRoomConfirm = " and isnull(rm.ConfirmedBy,'')<>'' ";
            }
            if (inPatient) {
                sqlQuery = "Select  rm.roomNo, rm.code, pu.docId, pu.recId recIdUHID, pr.v_Date, pr.regDocId, pr.recId recIdReg, "
                        + "         pu.patientAliasName patient, pu.guardianName guardian, pu.mobile, pu.idNo, pu.v_No, "
                        + "         dep.name department, org.displayName organization, vt.short_Name, pr.regTime  "
                        + "         from patientRegistration pr  with(nolock) "
                        + "         left join roomMast rm  ON rm.regDocId = pr.regDocId "
                        + "         left join patientDischarge pd on pd.regDocId = pr.regDocId "
                        + "         left join voucher_Type vt ON vt.v_Type = pr.v_Type "
                        + "         left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                        + "         left join patientUHID pu ON pu.docId = pr.uhid "
                        + "         left join patientUHID2 pu2 ON pu2.uhid = pu.docId "
                        + "         left join hl7Mast gen ON gen.code = pu2.genderCode "
                        + "         left join organizationMast as om on pr.organizationCode = om.subCode "
                        + "         left join subGroup org ON org.subCode = pr.organizationCode "
                        + "         left join subGroup dep ON dep.subCode = pr.departmentCode"
                        + "         where isnull(pu.approvedBy,'') <> '' and pu.isCancelled = 0 and pr.isCancelled = 0 "
                        + "         and pd.regDocId is null" + condition + conditionRoomConfirm;//and rm.regDocId is not null 
            } else {
                sqlQuery = "Select  rm.roomNo, rm.code, pu.docId, pu.recId recIdUHID, pd.dischargeDate v_Date, pr.regDocId, pr.recId recIdReg,  "
                        + "         pu.patientAliasName patient, pu.guardianName guardian, pu.mobile, pu.v_No,  "
                        + "         dep.name department, org.displayName organization, vt.short_Name, pd.dischargeTime regTime "
                        + "         from roomMast rm  with(nolock) "
                        + "         left join patientRegistration pr on rm.code = pr.roomCode "
                        + "         left join ("
                        + "		select  sb.docId, sb.regDocId, sb.approvedBy "
                        + "			from saleBill sb "
                        + "			left join voucher_Type vt on vt.v_Type = sb.v_Type "
                        + "			where sb.regDocId <> sb.docId and sb.isCancelled = 0 and vt.category in (" + sb_Vcat + ") "
                        + "         ) sb on sb.regDocId = pr.regDocId  "
                        + "         left join patientDischarge pd on pd.regDocId = pr.regDocId "
                        + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                        + "         left join patientUHID pu on pu.docId = pr.uhid "
                        + "         left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                        + "         left join hl7mast gen on gen.code = pu2.genderCode "
                        + "         left join organizationMast as om on pr.organizationCode = om.subCode "
                        + "         left join subGroup org on org.subCode = pr.organizationCode "
                        + "         left join subGroup dep on dep.subCode = pr.departmentCode "
                        + "         where rm.isActive = 1 and isnull(pu.approvedBy,'') <> '' "
                        + "         and pu.isCancelled = 0 and  pr.isCancelled = 0 " + condition + conditionRoomConfirm;
            }
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnNursingStationCode(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("prmNursingStationCode", STServer.getSsnNursingStationCode(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdPatientDetailsRoomWise_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomNo")));
                listNode.setDocIdIPD(DmShared.xNull(rsTemp.getString("regDocid")));
                listNode.setRecIdIPD(DmShared.xNull(rsTemp.getString("recIdReg")));
                listNode.setDocIdUHID(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setRecIdUHID(DmShared.xNull(rsTemp.getString("recIdUHID")));
                listNode.setDtIPD(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setPatient(DmShared.xNull(rsTemp.getString("patient")));
                listNode.setGuardian(DmShared.xNull(rsTemp.getString("guardian")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setOrganizationName(DmShared.xNull(rsTemp.getString("organization")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
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
