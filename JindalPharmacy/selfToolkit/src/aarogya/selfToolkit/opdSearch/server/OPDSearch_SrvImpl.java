package aarogya.selfToolkit.opdSearch.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.opdSearch.shared.*;
import aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.OPDFilterType;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OPDSearch_SrvImpl extends DmRSSPrivate implements OPDSearch_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String opdCategory = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";
    private final String dayCareCategory = "'" + pvc.DAY_CARE + "'";

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get Default values from enviro >====================================*/
            sqlQuery = "Select cancelDaysOPD, serviceDaysOPD from enviro Where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setCancelDaysOPD(DmShared.iNull(rsTemp.getString("cancelDaysOPD")).toString());
                defaultValue.setServiceDaysOPD(DmShared.iNull(rsTemp.getString("serviceDaysOPD")).toString());
            } else {
                throw new DmException(0, "Please set default values in Environment Setting for current Site.");
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            dmrTemp.setData(defaultValue);
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
    public DmResultPageing<List<GrdRegistration_Mdl>> getFind(int cancelDaysOPD, int serviceDaysOPD, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdRegistration_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdRegistration_Mdl> lstTemp = new ArrayList<>();
        GrdRegistration_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "", filterCategory = opdCategory + "," + dayCareCategory, saleBillQuery = "";
        boolean isAllowSiteApplicable = false;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (param.getFilterType() != null) {
                Calendar cal;
                cal = Calendar.getInstance();
                cal.setTime(new Date());

                for (OPDFilterType filterType : param.getFilterType()) {
                    if (filterType == OPDFilterType.TPA_ONLY) {
                        condition += " and (om.isTPA = 1) ";
                    } else if (filterType == OPDFilterType.NON_TPA_ONLY) {
                        condition += " and (om.isTPA = 0) ";
                    }
                    if (filterType == OPDFilterType.CANCEL_DAYS) {
                        cal.add(Calendar.DATE, 0 - cancelDaysOPD);
                        condition += " and (pr.v_Date >= '" + DmShared.dNull(cal.getTime()) + "') ";
                    } else if (filterType == OPDFilterType.SERVICE_DAYS) {
                        cal.add(Calendar.DATE, 0 - serviceDaysOPD);
                        condition += " and (pr.v_Date >= '" + DmShared.dNull(cal.getTime()) + "') ";
                    }

                    if (filterType == OPDFilterType.OPD_ONLY) {
                        filterCategory = opdCategory;
                    } else if (filterType == OPDFilterType.DAYCARE_ONLY) {
                        filterCategory = dayCareCategory;

                        saleBillQuery = "         left join ("
                                + "		select  sb.docId, sb.regDocId, sb.approvedBy "
                                + "			from saleBill sb "
                                + "			left join voucher_Type vt on vt.v_Type = sb.v_Type "
                                + "			where sb.regDocId <> sb.docId and sb.isCancelled = 0 and isnull(sb.approvedBy,'') <> '' and vt.category in ( '" + new ProjectVoucherCategory().DAY_CARE_BILL + "' )   "
                                + "         ) sb on sb.regDocId = pr.regDocId  ";

                        condition += " and sb.docId is null ";
                    }

                    if (filterType == OPDFilterType.ALLOW_SITE_FOR_DIAGNOSTIC) {
                        if (!isAllowSiteApplicable) {
                            condition += " and (pr.site_Code in (" + STServer.getSsnDiagnosticAllowPatientForSite(this) + ")) ";
                        }
                        isAllowSiteApplicable = true;
                    }
                    if (filterType == OPDFilterType.ALLOW_SITE_FOR_PHARMACY) {
                        if (!isAllowSiteApplicable) {
                            condition += " and (pr.site_Code in (" + STServer.getSsnPharmacyAllowPatientForSite(this) + ")) ";
                        }
                        isAllowSiteApplicable = true;
                    }
                    if (filterType == OPDFilterType.ALLOW_SITE_FOR_OPD) {
                        if (!isAllowSiteApplicable) {
                            condition += " and (pr.site_Code in (" + STServer.getSsnOpdAllowPatientForSite(this) + ")) ";
                        }
                        isAllowSiteApplicable = true;
                    }
                    if (filterType == OPDFilterType.ALLOW_SITE_FOR_IPD) {
                        if (!isAllowSiteApplicable) {
                            condition += " and (pr.site_Code in (" + STServer.getSsnIpdAllowPatientForSite(this) + ")) ";
                        }
                        isAllowSiteApplicable = true;
                    }
                    if (filterType == OPDFilterType.ALLOW_SITE_FOR_DAYCARE) {
                        if (!isAllowSiteApplicable) {
                            condition += " and (pr.site_Code in (" + STServer.getSsnDayCareAllowPatientForSite(this) + ")) ";
                        }
                        isAllowSiteApplicable = true;
                    }
                }
            }
            if (!isAllowSiteApplicable) {
                condition += " and (pr.site_Code in (" + DmServer.getSsnSiteCode(this) + ")) ";
            }

            sqlQuery = "Select	pr.regDocId, pr.v_Date, pr.regTime, pr.v_No, vt.short_Name, pr.recID as opdNo, "
                    + "		pu.patientName, pr.guardianName, pu.mobile, hl7.name as gender, pu.dobDisplay as age, "
                    + "		pu.recId as uhidNo, org.name as organizationName, sg.name as consultantName "
                    + "		from patientRegistration as pr  with(nolock) "
                    + saleBillQuery
                    + "		left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "		left join patientUHID as pu on pr.uhid = pu.docId "
                    + "		left join subGroup as sg on pr.consultantCode = sg.subCode "
                    + "		left join subGroup as org on pr.organizationCode = org.subCode "
                    + "         left join organizationMast om on om.subCode = org.subCode "
                    + "		left join patientUHID2 as pu2 on pu.docId = pu2.uhid "
                    + "		left join hl7Mast as hl7 on pu2.genderCode = hl7.code "
                    + "		Where (pr.isCancelled = 0) and (vt.category in (" + filterCategory + ")) "
                    + "		and (pu.isCancelled = 0) and (pu.isExpired = 0) and (isNull(pu.approvedBy,'') <> '') " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdRegistration_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("regDocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("opdNo")));
                listNode.setUhidNo(DmShared.xNull(rsTemp.getString("uhidNo")));
                listNode.setRegDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRegTime(DmShared.xNull(rsTemp.getString("regTime")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setGuardianName(DmShared.xNull(rsTemp.getString("guardianName")));
                listNode.setAge(DmShared.xNull(rsTemp.getString("age")));
                listNode.setGender(DmShared.xNull(rsTemp.getString("gender")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                listNode.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
                lstTemp.add(listNode);
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
