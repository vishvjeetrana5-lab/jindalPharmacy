package aarogya.mis.reports.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.mis.reports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptConsultantSharing_SrvImpl extends DmRSSPrivate implements RptConsultantSharing_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
//    String opdCategory = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.DAY_CARE + "','" + pvc.OPD_SERVICE + "','" + pvc.LAB_REGISTRATION + "'";
//    String ipdCategory = "'" + pvc.IPD + "','" + pvc.EMERGENCY + "','" + pvc.IPD_SERVICE + "'";

    enum ReportType {
        DETAIL(0, "Detail"), CATEGORY_WISE_SUMMARY(1, "Category Wise Summary"), CATEGORY_ORGANIZATION_WISE_SUMMARY(2, "Category / Organization Wise Summary"),
        SERVICE_WISE_SUMMARY(3, "Service Wise Summary"), SERVICE_ORGANIZATION_WISE_SUMMARY(4, "Service / Organization Wise Summary");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ReportType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ReportType valueOf(int id) {
            if (id == 0) {
                return DETAIL;
            } else if (id == 1) {
                return CATEGORY_WISE_SUMMARY;
            } else if (id == 2) {
                return CATEGORY_ORGANIZATION_WISE_SUMMARY;
            } else if (id == 3) {
                return SERVICE_WISE_SUMMARY;
            } else if (id == 4) {
                return SERVICE_ORGANIZATION_WISE_SUMMARY;
            } else {
                return CATEGORY_WISE_SUMMARY;
            }
        }
    }

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, amountType = "";
        String conditionPsdDate = "", conditionSbDate = "", conditionMain = "";
        List<ConsultantSharing_Mdl> listSharing = new ArrayList<ConsultantSharing_Mdl>();
        List<ConsultantSharingFilter_Mdl> listFilter = null, listTemp = null;
        ConsultantSharingFilter_Mdl sharingMdl = null;
        boolean chkPaymentStatus = false;

        try {
            RptConsultantSharing_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(filter.get("share").getTag()).intValue() == 0) {
                amountType = "psd.grossAmount";
            } else {
                amountType = "psd.oh_Amt_Net";
            }

            conditionPsdDate = "and psd.v_Date between :fromDate and :toDate ";
            conditionSbDate = "and sb.v_Date between :fromDate and :toDate ";

            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                conditionPsdDate += " and (pr.organizationCode in (" + DmServer.getParamList(":organizationCode", filter.get("organization").getTag()) + "))";
                conditionSbDate += " and (pr.organizationCode in (" + DmServer.getParamList(":organizationCode", filter.get("organization").getTag()) + "))";
            }
            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                conditionMain += "and (temp.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultant").getTag()) + ")) ";
            }
            if (!DmShared.xNull(filter.get("roomCategory").getTag()).trim().equalsIgnoreCase("")) {
                conditionMain += "and (rm.roomCategoryCode in (" + DmServer.getParamList(":roomCategory", filter.get("roomCategory").getTag()) + ")) ";
            }
            if (!DmShared.xNull(filter.get("serviceCategory").getTag()).trim().equalsIgnoreCase("")) {
                conditionMain += "and (psm.categoryCode in (" + DmServer.getParamList(":serviceCategory", filter.get("serviceCategory").getTag()) + ")) ";
            }
            if (!DmShared.xNull(filter.get("serviceGroup").getTag()).trim().equalsIgnoreCase("")) {
                conditionMain += "and (psm.groupCode in (" + DmServer.getParamList(":serviceGroup", filter.get("serviceGroup").getTag()) + ")) ";
            }
            if (!DmShared.xNull(filter.get("serviceName").getTag()).trim().equalsIgnoreCase("")) {
                conditionMain += "and (psm.code in (" + DmServer.getParamList(":serviceName", filter.get("serviceName").getTag()) + ")) ";
            }

            if (!DmShared.xNull(filter.get("nature").getTag()).trim().equalsIgnoreCase("")) {
                conditionMain += "and temp.nature in (" + DmServer.getParamList(":nature", "2," + filter.get("nature").getTag()) + ") ";
            }

            if (!DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                conditionMain += "and temp.moduleName in (" + DmServer.getParamList(":reportFor", "Discount," + filter.get("reportFor").getTag()) + ") ";
            }
            sqlQuery = "";

            if (DmShared.iNull(filter.get("paymentStatus").getTag()).intValue() != 0) {
                chkPaymentStatus = true;
                sqlQuery = "declare @t table (regDocId bigint primary key) "
                        + "INSERT INTO @t (regDocId) "
                        + "select  t.regDocId "
                        + "from ( "
                        + "	select	psd.regDocId "
                        + "		from patientServiceDetail psd "
                        + "		left join patientRegistration as pr on psd.regDocId = pr.regDocId  "
                        + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                        + "             where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + "','" + pvc.LAB_REGISTRATION + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and  "
                        + "             pr.site_code = :siteCode " + conditionPsdDate
                        + "	union all "
                        + "	select	sb.regDocId "
                        + "         from saleBill sb "
                        + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId "
                        + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                        + "         where vt.category in ('" + pvc.IPD_BILL + "') and  "
                        + "         pr.site_code = :siteCode " + conditionSbDate
                        + ") as t "
                        + "group by t.regDocId; ";
                if (DmShared.iNull(filter.get("paymentStatus").getTag()).intValue() == 1) {
                    conditionMain += "and isNull(t.bal,0) <= 0  ";
                } else if (DmShared.iNull(filter.get("paymentStatus").getTag()).intValue() == 2) {
                    conditionMain += "and isNull(t.bal,0) > 0 ";
                }
            }

            sqlQuery += "Select	temp.moduleName,temp.nature as tempNature, temp.isHeader, "
                    + "         case when temp.nature = 0 and temp.isHeader = 1 then 'Header Consultation' when temp.nature = 0 and temp.isHeader = 0 then 'Performing Consultant' "
                    + "         when temp.nature = 1 and temp.isHeader = 1 then 'Header Referred' when temp.nature = 1 and temp.isHeader = 0 then 'Performing Referred Con.' "
                    + "         when temp.nature = 2 then 'Discount' end as nature, "
                    + "		temp.regDocId, temp.regNo, temp.regDate, temp.organizationCode, isNull(rm.roomCategoryCode,-1) as roomCategoryCode, "
                    + "         rcm.name as roomCategoryName, temp.dobDisplay as age, org.manualCode as organizationName,  "
                    + "		temp.consultantCode, concat(con.initial, ' ',con.name) as consultantName, pu.recId as uhidNo, "
                    + "		case when isNull(pu.patientInitial,'') <> '' then concat(pu.patientInitial,' ', pu.patientName) else pu.patientName end as patientName, "
                    + "		gen.shortName as gender, temp.serviceDate, temp.serviceDocId, temp.billNo, temp.serviceDate as billDate, "
                    + "		temp.serviceCode, psm.name as serviceName, psm.groupCode, psm.categoryCode, pscm.name as categoryName, "
                    + "         temp.quantity, temp.amount as srvAmt, temp.discount "
                    + "		from ( "
                    //detail starts 
                    //<editor-fold defaultstate="collapsed" desc="OPD module">
                    + "select	'OPD' as moduleName,0 as nature, 0 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo,psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid,  "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientService as ps  "
                    + "         left join patientServiceDetail as psd on ps.docId = psd.docId  "
                    + "         left join patientRegistration as pr on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and  "
                    + "         pr.site_code = :siteCode and psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionPsdDate
                    + "union all "
                    + "select	'OPD' as moduleName,0 as nature, 1 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo, psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and  "
                    + "         pr.site_code = :siteCode and pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionPsdDate
                    + "union all "
                    + "select	'OPD' as moduleName,1 as nature, 1 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo, psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "') and "
                    + "         pr.site_code = :siteCode and pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0 " + conditionPsdDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="IPD module">
                    + "union all "
                    + "select	'IPD' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'IPD' as moduleName,0 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'IPD' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0 " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Emergency Module">
                    + "union all "
                    + "select	'Emergency' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.EMERGENCY_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Emergency' as moduleName,0 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.EMERGENCY_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Emergency' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.EMERGENCY_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0 " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Day Care Module">
                    + "union all "
                    + "select	'Day Care' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.DAY_CARE_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Day Care' as moduleName,0 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.DAY_CARE_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "union all "
                    + "select	'Day Care' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo, sb.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.DAY_CARE_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0 " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Diagnostic detail of Credit To Registration Performing">
                    + "union all  "
                    + "select	'Diagnostic' as moduleName,0 as nature, 0 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join ( "
                    + "             select  prl.regDocId,prl.regDocIdOld  "
                    + "                     from patientRegistration as prl  "
                    //                    + "                     left join patientLimitApproval pla on pla.docId = prl.regDocId "
                    + "                     left join voucher_type vt on vt.v_Type = prl.v_Type "
                    + "                     where vt.category in ('" + pvc.LAB_REGISTRATION + "') and prl.isCancelled = 0 and "
                    //                    + "                     pla.nature = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     prl.paymentSource = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     group by prl.regDocId,prl.regDocIdOld  "
                    + "         ) as lab on lab.regDocIdOld = sb.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = lab.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1  " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Diagnostic detail of Credit To Registration Referred By">
                    + "union all  "
                    + "select	'Diagnostic' as moduleName,1 as nature, 1 as isHeader, sb.docId as serviceDocId, sb.v_type, sb.recId as billNo,sb.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, lab.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from saleBill sb "
                    + "         left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "         left join ( "
                    + "             select  prl.regDocId,prl.regDocIdOld,max(prl.referredBy) as referredBy "
                    + "                     from patientRegistration as prl  "
                    //                    + "                     left join patientLimitApproval pla on pla.docId = prl.regDocId "
                    + "                     left join voucher_type vt on vt.v_Type = prl.v_Type "
                    + "                     where vt.category in ('" + pvc.LAB_REGISTRATION + "') and prl.isCancelled = 0 and "
                    //                    + "                     pla.nature = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     prl.paymentSource = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "   "
                    + "                     group by prl.regDocId,prl.regDocIdOld  "
                    + "         ) as lab on lab.regDocIdOld = sb.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = lab.regDocId  "
                    + "         left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "         where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "         lab.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1  " + conditionSbDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Diagnostic Module">
                    + "union all "
                    + "select	'Diagnostic' as moduleName,0 as nature, 0 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo,psd.v_Date as serviceDate, "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, psd.consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr "
                    //                    + "         left join patientLimitApproval pla on pla.docId = pr.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId  "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.LAB_REGISTRATION + "') and pr.site_code = :siteCode and "
                    //                    + "         isNull(pla.nature,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         isNull(pr.paymentSource,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         psd.consultantCode is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0  " + conditionPsdDate
                    + "union all "
                    + "select	'Diagnostic' as moduleName,1 as nature, 1 as isHeader, ps.docId as serviceDocId, ps.v_type, ps.recId as billNo, psd.v_Date as serviceDate,  "
                    + "         pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.dobDisplay, pr.uhid, "
                    + "         pr.organizationCode, psd.roomCode, pr.referredBy as consultantCode, psd.serviceCode, " + amountType + " as amount, psd.quantity, 0 as discount  "
                    + "         from patientRegistration as pr "
                    //                    + "         left join patientLimitApproval pla on pla.docId = pr.regDocId "
                    + "         left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "         left join patientService as ps on ps.docId = psd.docId  "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         where vt.category in ('" + pvc.LAB_REGISTRATION + "') and pr.site_code = :siteCode and "
                    //                    + "         isNull(pla.nature,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         isNull(pr.paymentSource,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and "
                    + "         pr.referredBy is not null and psd.isCancelled = 0 and psd.billingNature <> 1 and "
                    + "         pr.isCancelled = 0 " + conditionPsdDate
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Discount Summary">
                    + "union all "
                    + "select   'Discount' as moduleName,2 as nature, 2 as isHeader, null as serviceDocId, null as v_type, null as billNo,null as serviceDate, "
                    + "         null as regDocId, null as regNo, null as regDate, null as dobDisplay, null as uhid, "
                    + "         null as organizationCode,null as roomCode, d.consultantCode, null as serviceCode, null as amount, null as quantity, sum(d.discount) as discount  "
                    + "         from ( "
                    + "             select  psd.discountGivenBy as consultantCode, psd.discount "
                    + "                     from patientService as ps  "
                    + "                     left join patientServiceDetail as psd on ps.docId = psd.docId  "
                    + "                     left join patientRegistration as pr on psd.regDocId = pr.regDocId  "
                    //                    + "                     left join patientLimitApproval pla on pla.docId = pr.regDocId "
                    + "                     left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "                     where vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.LAB_REGISTRATION + "','" + pvc.PAY_CLINIC + "') and "
                    //                    + "                     isNull(pla.nature,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and  "
                    + "                     isNull(pr.paymentSource,0) <> " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + " and  "
                    + "                     pr.site_code = :siteCode and psd.discountGivenBy is not null and psd.isCancelled = 0 and "
                    + "                     psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionPsdDate
                    + "             union all " // ipd
                    + "             select  psd.discountGivenBy as consultantCode, psd.discount "
                    + "                     from saleBill sb "
                    + "                     left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "                     left join patientServiceDetail as psd on psd.regDocId = pr.regDocId  "
                    + "                     left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "                     where vt.category in ('" + pvc.IPD_BILL + "','" + pvc.DAY_CARE_BILL + "','" + pvc.EMERGENCY_BILL + "') and "
                    + "                     isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and psd.discountGivenBy is not null and "
                    + "                     psd.isCancelled = 0 and psd.billingNature <> 1 and pr.isCancelled = 0  " + conditionSbDate
                    + "             union all "//Lab of ipd bill
                    + "             select  psd.discountGivenBy as consultantCode, psd.discount  "
                    + "                     from saleBill sb "
                    + "                     left join patientRegistration as pr on sb.regDocId = pr.regDocId  "
                    + "                     left join ( "
                    + "                         select  prl.regDocId,prl.regDocIdOld  "
                    + "                                 from patientRegistration as prl  "
                    //                    + "                                 left join patientLimitApproval pla on pla.docId = prl.regDocId "
                    + "                                 left join voucher_type vt on vt.v_Type = prl.v_Type "
                    + "                                 where vt.category in ('" + pvc.LAB_REGISTRATION + "') and prl.isCancelled = 0 and "
                    //                    + "                                 pla.nature = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "  "
                    + "                                 prl.paymentSource = " + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + "  "
                    + "                                 group by prl.regDocId,prl.regDocIdOld  "
                    + "                     ) as lab on lab.regDocIdOld = sb.regDocId "
                    + "                     left join patientServiceDetail as psd on psd.regDocId = lab.regDocId  "
                    + "                     left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "                     where vt.category in ('" + pvc.IPD_BILL + "') and isNull(sb.approvedBy,'') <> '' and pr.site_code = :siteCode and "
                    + "                     psd.discountGivenBy is not null and psd.billingNature <> 1 and "
                    + "                     psd.isCancelled = 0 " + conditionSbDate
                    + "         ) as d "
                    + "         group by d.consultantCode "
                    //</editor-fold>
                    //detail end 
                    + "		) as temp "
                    + "		left join subGroup as org on temp.organizationCode = org.subCode "
                    + "		left join subGroup as con on temp.consultantCode = con.subCode "
                    + "		left join patientServiceMast as psm on temp.serviceCode = psm.code "
                    + "		left join roomMast as rm on temp.roomCode = rm.code "
                    + "		left join roomCatMast as rcm on rcm.code = rm.roomCategoryCode "
                    + "		left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
                    + "		left join patientUHID as pu on temp.uhid = pu.docId "
                    + "		left join patientUHID2 as pu2 on temp.uhid = pu2.uhid "
                    + "		left join hl7Mast as gen on pu2.genderCode = gen.code ";
            if (chkPaymentStatus) {
                sqlQuery = sqlQuery
                        + "left join ( "
                        + "     select  t.regDocId, round(isNull(sum(t.oh_Amt_Net),0) - isNull(sum(t.amount),0),2) as bal "
                        + "     from ( "
                        + "         select	t.regDocId, sum(psd.oh_Amt_Net) as oh_Amt_Net, 0 as amount "
                        + "			from @t as t "
                        + "			left join patientServiceDetail psd on t.regDocId = psd.regDocId "
                        + "			group by t.regDocId "
                        + "         union all "
                        + "         select      t.regDocId, 0 as oh_Amt_Net, sum(prc.amount) as amount "
                        + "			from @t t "
                        + "			left join patientReceipt prc on t.regDocId = prc.regDocId "
                        + "			group by t.regDocId "
                        + "     ) as t "
                        + "     group by t.regDocId "
                        + ") as t on t.regDocId = temp.regDocId ";

            }
            sqlQuery = sqlQuery + "where 1 = 1 " + conditionMain;

            if (DmShared.iNull(filter.get("type").getTag()).intValue() == ReportType.DETAIL.getId()) {
//                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, temp.nature, temp.isHeader desc, temp.serviceDate, temp.billNo "; //, temp.serviceDocId, pr.v_Date, pr.recId, temp.regDocId
                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, psm.name, psm.code, org.manualCode, temp.organizationCode, temp.nature, temp.isHeader desc, temp.serviceDate, temp.billNo "; //, temp.serviceDocId, pr.v_Date, pr.recId, temp.regDocId

            } else if (DmShared.iNull(filter.get("type").getTag()).intValue() == ReportType.SERVICE_WISE_SUMMARY.getId()
                    || DmShared.iNull(filter.get("type").getTag()).intValue() == ReportType.SERVICE_ORGANIZATION_WISE_SUMMARY.getId()) {
                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, psm.name, psm.code, org.manualCode, temp.organizationCode ";

            } else {
                sqlQuery += "order by con.name, temp.consultantCode, temp.moduleName, pscm.name, psm.categoryCode, org.manualCode, temp.organizationCode ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (!DmShared.xNull(filter.get("nature").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("nature", DmShared.xNull("2," + filter.get("nature").getTag()));
            }
            if (!DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("reportFor", DmShared.xNull("Discount," + filter.get("reportFor").getTag()));
            }
            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultantCode", DmShared.xNull(filter.get("consultant").getTag()));
            }
            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("organizationCode", DmShared.xNull(filter.get("organization").getTag()));
            }
            if (!DmShared.xNull(filter.get("roomCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("roomCategory", DmShared.xNull(filter.get("roomCategory").getTag()));
            }
            if (!DmShared.xNull(filter.get("serviceCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("serviceCategory", DmShared.xNull(filter.get("serviceCategory").getTag()));
            }
            if (!DmShared.xNull(filter.get("serviceGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("serviceGroup", DmShared.xNull(filter.get("serviceGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("serviceName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("serviceName", DmShared.xNull(filter.get("serviceName").getTag()));
            }
            psTemp.execute();
            if (chkPaymentStatus) {
                psTemp.getMoreResults();
                rsTemp = psTemp.getResultSet();
            } else {
                rsTemp = psTemp.getResultSet();
            }
            listFilter = null;
            long consultantCode = 0;
            while (rsTemp.next()) {
                listTemp = null;
                sharingMdl = null;
                if (consultantCode != DmShared.iNull(rsTemp.getString("consultantCode")).longValue()) {
                    listFilter = getSharingRules(DmShared.iNull(rsTemp.getString("consultantCode")).toString(), cnTemp);
                }

                if (listFilter != null && listFilter.size() > 0) {
                    if (DmShared.iNull(rsTemp.getString("tempNature")).longValue() == 2) {
                        fillRecord(DmShared.iNull(filter.get("type").getTag()).intValue() == ReportType.DETAIL.getId() ? false : true, null, rsTemp, listSharing);

                    } else {
                        listTemp = getBestFit(listFilter, rsTemp);
                        if (listTemp != null && listTemp.size() > 0) {
                            sharingMdl = listTemp.get(0);
                            fillRecord(DmShared.iNull(filter.get("type").getTag()).intValue() == ReportType.DETAIL.getId() ? false : true, sharingMdl, rsTemp, listSharing);
                        }
                    }
                }
                consultantCode = DmShared.iNull(rsTemp.getString("consultantCode")).longValue();
            }
            rsTemp.close();
            psTemp.close();

            if (listSharing != null && listSharing.size() > 0) {
                HashMap hm = new HashMap();
                hm.put("isAmountNeeded", DmShared.iNull(filter.get("amountNeeded").getTag()).intValue() == 1 ? true : false);
                if (DmShared.iNull(filter.get("type").getTag()).intValue() == ReportType.DETAIL.getId()) {
                    jasperReport = DmServer.compileJasper("MIS_ConsultantSharingDetail");
                    jasperPrint = DmServer.<ConsultantSharing_Mdl>fillJasper(this, "Consultant Sharing (Detail)", jasperReport, hm, filter, listSharing);

                } else {
                    jasperReport = DmServer.compileJasper("MIS_ConsultantSharingSummary");
                    jasperPrint = DmServer.<ConsultantSharing_Mdl>fillJasper(this, "Consultant Sharing (Summary)", jasperReport, hm, filter, getSummary(listSharing, ReportType.valueOf(DmShared.iNull(filter.get("type").getTag()).intValue())));
                }
                rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            } else {
                throw new DmException(100, "No record found.");
            }

            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
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

    private void fillRecord(boolean isSummary, ConsultantSharingFilter_Mdl sharingMdl, ResultSet rsTemp, List<ConsultantSharing_Mdl> listSharing) {
        ConsultantSharing_Mdl node;
        double amount = 0d, quantity = 0d, share = 0d;

        try {
            if (sharingMdl != null
                    || DmShared.iNull(rsTemp.getString("tempNature")).longValue() == 2) {
                node = new ConsultantSharing_Mdl();
                node.setConsultantCode(DmShared.iNull(rsTemp.getString("consultantCode")).longValue());
                node.setOrganizationCode(DmShared.iNull(rsTemp.getString("organizationCode")).longValue());
                node.setRegDocId(DmShared.iNull(rsTemp.getString("regDocId")).longValue());
                node.setServiceDocId(DmShared.iNull(rsTemp.getString("serviceDocId")).longValue());
                node.setTempNature(DmShared.iNull(rsTemp.getString("tempNature")).intValue());
                node.setIsHeader(DmShared.iNull(rsTemp.getString("isHeader")).intValue());
                node.setServiceCode(DmShared.iNull(rsTemp.getString("serviceCode")).intValue());
                node.setGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).intValue());
                node.setCategoryCode(DmShared.iNull(rsTemp.getString("categoryCode")).intValue());
                node.setRoomCategoryCode(DmShared.iNull(rsTemp.getString("roomCategoryCode")).intValue());
                node.setRegDate(rsTemp.getDate("regDate"));
                node.setBillDate(rsTemp.getDate("billDate"));
                node.setNature(DmShared.xNull(rsTemp.getString("nature")).trim());
                node.setRegNo(DmShared.xNull(rsTemp.getString("regNo")).trim());
                node.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")).trim());
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")).trim());
                node.setUhidNo(DmShared.xNull(rsTemp.getString("uhidNo")).trim());
                node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")).trim());
                node.setAge(DmShared.xNull(rsTemp.getString("age")).trim());
                node.setGendor(DmShared.xNull(rsTemp.getString("gender")).trim());
                node.setBillNo(DmShared.xNull(rsTemp.getString("billNo")).trim());
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")).trim());
                node.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")).trim());
                node.setRoomCategoryName(DmShared.xNull(rsTemp.getString("roomCategoryName")).trim());
                node.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")).trim());

                amount = DmShared.vNull(DmServer.vNull(rsTemp.getString("srvAmt"), "0.00")).doubleValue();
                quantity = DmShared.vNull(DmServer.vNull(rsTemp.getString("quantity"), "0.00")).doubleValue();
                node.setSrvAmt(amount);
                node.setQuantity(quantity);

                if (DmShared.iNull(rsTemp.getString("tempNature")).intValue() == 0) {
                    if (isSummary) {
                        node.setConAmt(amount);
                        node.setConUnit(quantity);
                        node.setRefAmt(0d);
                        node.setRefUnit(0d);
                        node.setDisAmt(0d);
                        node.setDiscountUnit(0d);
                    }
                    /*=================< For Consultant >=================*/
                    if (DmShared.iNull(rsTemp.getString("isHeader")).intValue() == 1) {
                        /*=================< For Header >=================*/
                        if (sharingMdl.getHeaderConsultantIsAmount() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getHeaderConsultantAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderConsultantAmount(), "0.00") + "%");
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            if (isSummary) {
                                share = DmShared.vNull(DmServer.vNull((sharingMdl.getHeaderConsultantAmount() * quantity), "0.00")).doubleValue();
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderConsultantAmount(), "0.00"));
                            }
                        }
                    } else {
                        /*=================< For Line >=================*/
                        if (sharingMdl.getLineConsultantIsAmount() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getLineConsultantAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineConsultantAmount(), "0.00") + "%");
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            share = DmShared.vNull(DmServer.vNull((sharingMdl.getLineConsultantAmount() * quantity), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingConAmt(share);
                                node.setSharingRefAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineConsultantAmount(), "0.00"));
                            }
                        }
                    }
                } else if (DmShared.iNull(rsTemp.getString("tempNature")).intValue() == 1) {
                    /*=================< For Referred >=================*/
                    if (isSummary) {
                        node.setRefAmt(amount);
                        node.setRefUnit(quantity);
                        node.setConAmt(0d);
                        node.setConUnit(0d);
                        node.setDisAmt(0d);
                        node.setDiscountUnit(0d);
                    }
                    if (DmShared.iNull(rsTemp.getString("isHeader")).intValue() == 1) {
                        /*=================< For Header >=================*/
                        if (sharingMdl.getHeaderReferredIsAmount() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getHeaderReferredAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderReferredAmount(), "0.00") + "%");
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            share = DmShared.vNull(DmServer.vNull((sharingMdl.getHeaderReferredAmount() * quantity), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getHeaderReferredAmount(), "0.00"));
                            }
                        }
                    } else {
                        /*=================< For Line >=================*/
                        if (DmShared.iNull(rsTemp.getString("lineReferredIsAmount")).intValue() == 0) {
                            /*=================< For % On Amount >=================*/
                            share = DmShared.vNull(DmServer.vNull(((amount * sharingMdl.getLineReferredAmount()) / 100.0), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineReferredAmount(), "0.00"));
                            }
                        } else {
                            /*=================< For Amount Per Unit >=================*/
                            share = DmShared.vNull(DmServer.vNull((sharingMdl.getLineReferredAmount() * quantity), "0.00")).doubleValue();
                            if (isSummary) {
                                node.setSharingRefAmt(share);
                                node.setSharingConAmt(0.0d);
                                node.setDiscount(0.0d);
                            } else {
                                node.setSharingSrvAmt(share);
                                node.setSharing(DmServer.vNull(sharingMdl.getLineReferredAmount(), "0.00"));
                            }
                        }
                    }
                } else {
                    /*=================< For Discount >=================*/
                    if (isSummary) {
                        node.setDisAmt(amount);
                        node.setDiscountUnit(quantity);
                        node.setConAmt(0d);
                        node.setConUnit(0d);
                        node.setRefAmt(0d);
                        node.setRefUnit(0d);
                        node.setDiscount(DmShared.vNull(DmServer.vNull(rsTemp.getString("discount"), "0.00")).doubleValue());
                        node.setSharingConAmt(0.0d);
                        node.setSharingRefAmt(0.0d);
                    } else {
                        node.setSharing("");
//                        node.setSharingSrvAmt(DmShared.vNull(DmServer.vNull(rsTemp.getString("discount"), "0.00")).doubleValue());
                        node.setDiscount(DmShared.vNull(DmServer.vNull(rsTemp.getString("discount"), "0.00")).doubleValue());
                    }
                }

                if (DmShared.vNull(node.getSharingSrvAmt()).doubleValue() != 0.0
                        || DmShared.vNull(node.getSharingConAmt()).doubleValue() != 0.0
                        || DmShared.vNull(node.getSharingRefAmt()).doubleValue() != 0.0
                        || DmShared.vNull(node.getDiscount()).doubleValue() != 0.0) {
                    listSharing.add(node);
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    private List<ConsultantSharingFilter_Mdl> getSharingRules(String consultantCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<ConsultantSharingFilter_Mdl> listNode = new ArrayList<ConsultantSharingFilter_Mdl>();
        ConsultantSharingFilter_Mdl node;
        String sqlQuery;

        try {
            sqlQuery = "Select	csm.applicableFrom, csm.applicableTo, csm.organizationCode, csm.roomCategoryCode, csm.moduleName, "
                    + "         csm.serviceCode, csm.serviceGroupCode, csm.serviceCategoryCode, "
                    + "         csm.headerConsultantIsAmount, csm.headerConsultantAmount, csm.headerReferredIsAmount, csm.headerReferredAmount, "
                    + "         csm.lineConsultantIsAmount, csm.lineConsultantAmount, csm.lineReferredIsAmount, csm.lineReferredAmount "
                    + "		from consultantSharingMast as csm "
                    + "		where csm.consultantCode = :consultantCode "
                    + "         order by csm.organizationCode, csm.roomCategoryCode, csm.moduleName, csm.serviceCategoryCode, csm.serviceGroupCode, csm.serviceCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new ConsultantSharingFilter_Mdl();
                node.setApplicableFrom(rsTemp.getDate("applicableFrom"));
                node.setApplicableTo(rsTemp.getDate("applicableTo"));
                node.setOrganizationCode(DmShared.iNull(rsTemp.getString("organizationCode")).longValue());
                node.setRoomCategoryCode(DmShared.iNull(rsTemp.getString("roomCategoryCode")).intValue());
                node.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")).trim());
                node.setServiceCode(DmShared.iNull(rsTemp.getString("serviceCode")).intValue());
                node.setServiceGroupCode(DmShared.iNull(rsTemp.getString("serviceGroupCode")).intValue());
                node.setServiceCategoryCode(DmShared.iNull(rsTemp.getString("serviceCategoryCode")).intValue());
                node.setHeaderConsultantIsAmount(DmShared.iNull(rsTemp.getString("headerConsultantIsAmount")).intValue());
                node.setHeaderReferredIsAmount(DmShared.iNull(rsTemp.getString("headerReferredIsAmount")).intValue());
                node.setLineConsultantIsAmount(DmShared.iNull(rsTemp.getString("lineConsultantIsAmount")).intValue());
                node.setLineReferredIsAmount(DmShared.iNull(rsTemp.getString("lineReferredIsAmount")).intValue());
                node.setHeaderConsultantAmount(DmShared.vNull(rsTemp.getString("headerConsultantAmount")).doubleValue());
                node.setHeaderReferredAmount(DmShared.vNull(rsTemp.getString("headerReferredAmount")).doubleValue());
                node.setLineConsultantAmount(DmShared.vNull(rsTemp.getString("lineConsultantAmount")).doubleValue());
                node.setLineReferredAmount(DmShared.vNull(rsTemp.getString("lineReferredAmount")).doubleValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
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

    private List<ConsultantSharingFilter_Mdl> getBestFitService(List<ConsultantSharingFilter_Mdl> filterList,
            Integer categoryCode, Integer groupCode, Integer serviceCode) {
        List<ConsultantSharingFilter_Mdl> listTemp = null;

        listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).intValue() == serviceCode
                && DmShared.iNull(x.getServiceGroupCode()).intValue() == 0
                && DmShared.iNull(x.getServiceCategoryCode()).intValue() == 0)
        ).collect(Collectors.toList());
        if (listTemp == null || listTemp.size() == 0) {

            if (listTemp == null || listTemp.size() == 0 && groupCode > 0) {
                listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).intValue() == 0
                        && DmShared.iNull(x.getServiceGroupCode()).intValue() == groupCode
                        && DmShared.iNull(x.getServiceCategoryCode()).intValue() == 0)
                ).collect(Collectors.toList());
            }

            if (listTemp == null || listTemp.size() == 0) {

                listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).intValue() == 0
                        && DmShared.iNull(x.getServiceGroupCode()).intValue() == 0
                        && DmShared.iNull(x.getServiceCategoryCode()).intValue() == categoryCode)
                ).collect(Collectors.toList());

            }
        }

        return listTemp;
    }

    private List<ConsultantSharingFilter_Mdl> getBestFit(List<ConsultantSharingFilter_Mdl> filterList, ResultSet rsTemp) {
        List<ConsultantSharingFilter_Mdl> listTemp = null;
        List<ConsultantSharingFilter_Mdl> listWorking = null;
        List<ConsultantSharingFilter_Mdl> listService = null;

        if (filterList != null) {
            try {
                Date serviceDate = rsTemp.getDate("serviceDate");
                if (serviceDate != null) {
                    /*=================< 1 >=================*/
                    listTemp = filterList.stream().filter(x -> (x.getApplicableFrom().getTime() <= serviceDate.getTime()
                            && x.getApplicableTo().getTime() >= serviceDate.getTime())).collect(Collectors.toList());

                    if (listTemp != null && listTemp.size() > 0) {
                        Long organizationCode = DmShared.iNull(rsTemp.getString("organizationCode")).longValue();
                        Integer serviceCode = DmShared.iNull(rsTemp.getString("serviceCode")).intValue();
                        Integer categoryCode = DmShared.iNull(rsTemp.getString("categoryCode")).intValue();
                        Integer groupCode = DmShared.iNull(rsTemp.getString("groupCode")).intValue();
                        String moduleName = DmShared.xNull(rsTemp.getString("moduleName")).trim();
                        Integer roomCategoryCode = DmShared.iNull(rsTemp.getString("roomCategoryCode")).intValue();

                        listService = cloneConsultantSharingFilter_Mdl(getBestFitService(filterList, categoryCode, groupCode, serviceCode));
                        listWorking = listService;
                        if (listWorking == null || listWorking.size() == 0) {
                            listWorking = filterList.stream().filter(x -> (DmShared.iNull(x.getServiceCode()).longValue() == 0)
                                    && (DmShared.iNull(x.getServiceGroupCode()).longValue() == 0)
                                    && (DmShared.iNull(x.getServiceCategoryCode()).longValue() == 0)
                            ).collect(Collectors.toList());
                        }

                        listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                        ).collect(Collectors.toList());

                        if (listTemp == null || listTemp.size() == 0) {
                            listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                    && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                    && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                            ).collect(Collectors.toList());

                            if (listTemp == null || listTemp.size() == 0) {
                                listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                        && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                ).collect(Collectors.toList());

                                if (listTemp == null || listTemp.size() == 0) {
                                    listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                            && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                            && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                    ).collect(Collectors.toList());

                                    if (listTemp == null || listTemp.size() == 0) {
                                        listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                                && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                        ).collect(Collectors.toList());

                                        if (listTemp == null || listTemp.size() == 0) {
                                            listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                    && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                                    && (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                            ).collect(Collectors.toList());

                                            if (listTemp == null || listTemp.size() == 0) {
                                                listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                        && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == roomCategoryCode)
                                                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                ).collect(Collectors.toList());

                                                if (listTemp == null || listTemp.size() == 0) {
                                                    listTemp = listWorking.stream().filter(x -> (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                            && (DmShared.iNull(x.getRoomCategoryCode()).longValue() == 0)
                                                            && (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                    ).collect(Collectors.toList());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new DmException(0, e.getMessage());
            }
        }

//        return listTemp == null || listTemp.size() == 0 ? listService : listTemp;
        return listTemp;
    }

    private List<ConsultantSharingFilter_Mdl> cloneConsultantSharingFilter_Mdl(List<ConsultantSharingFilter_Mdl> consultantSharingFilter_Mdls) {
        List<ConsultantSharingFilter_Mdl> newConsultantSharingFilter_Mdls1 = new ArrayList<>();

        if (consultantSharingFilter_Mdls != null && consultantSharingFilter_Mdls.size() > 0) {
            for (ConsultantSharingFilter_Mdl consultantSharingFilter_Mdl : consultantSharingFilter_Mdls) {
                newConsultantSharingFilter_Mdls1.add(consultantSharingFilter_Mdl.clone());
            }
        }

        return newConsultantSharingFilter_Mdls1;
    }

    private List<ConsultantSharing_Mdl> getSummary(List<ConsultantSharing_Mdl> listTemp, ReportType reportType) {
        List<ConsultantSharing_Mdl> listSummary = new ArrayList<ConsultantSharing_Mdl>();
        ConsultantSharing_Mdl node;

        while (listTemp.size() > 0) {
            node = new ConsultantSharing_Mdl();

            List<ConsultantSharing_Mdl> listNode = getSameRecord(listTemp, reportType,
                    DmShared.xNull(listTemp.get(0).getModuleName()),
                    DmShared.iNull(listTemp.get(0).getOrganizationCode()).longValue(),
                    DmShared.iNull(listTemp.get(0).getConsultantCode()).longValue(),
                    DmShared.iNull(listTemp.get(0).getCategoryCode()).intValue(),
                    DmShared.iNull(listTemp.get(0).getServiceCode()).intValue());

            while (listNode.size() > 0) {
                if (DmShared.iNull(listNode.get(0).getConsultantCode()).longValue() != 0) {
                    node.setConsultantCode(listNode.get(0).getConsultantCode());
                    node.setConsultantName(listNode.get(0).getConsultantName());
                    node.setModuleName(listNode.get(0).getModuleName());

                    if (reportType == ReportType.SERVICE_ORGANIZATION_WISE_SUMMARY) {
                        node.setCategoryCode(listNode.get(0).getServiceCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getServiceName()) + " - " + DmShared.xNull(listNode.get(0).getOrganizationName()));

                    } else if (reportType == ReportType.SERVICE_WISE_SUMMARY) {
                        node.setCategoryCode(listNode.get(0).getServiceCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getServiceName()));

                    } else if (reportType == ReportType.CATEGORY_ORGANIZATION_WISE_SUMMARY) {
                        node.setCategoryCode(listNode.get(0).getCategoryCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getCategoryName()) + " - " + DmShared.xNull(listNode.get(0).getOrganizationName()));

                    } else {
                        node.setCategoryCode(listNode.get(0).getCategoryCode());
                        node.setCategoryName(DmShared.xNull(listNode.get(0).getCategoryName()));
                    }

                    node.setConAmt(DmShared.vNull(node.getConAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getConAmt()).doubleValue());
                    node.setConUnit(DmShared.vNull(node.getConUnit()).doubleValue() + DmShared.vNull(listNode.get(0).getConUnit()).doubleValue());
                    node.setSharingConAmt(DmShared.vNull(node.getSharingConAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getSharingConAmt()).doubleValue());
                    node.setRefAmt(DmShared.vNull(node.getRefAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getRefAmt()).doubleValue());
                    node.setRefUnit(DmShared.vNull(node.getRefUnit()).doubleValue() + DmShared.vNull(listNode.get(0).getRefUnit()).doubleValue());
                    node.setSharingRefAmt(DmShared.vNull(node.getSharingRefAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getSharingRefAmt()).doubleValue());
                    node.setDisAmt(DmShared.vNull(node.getDisAmt()).doubleValue() + DmShared.vNull(listNode.get(0).getDisAmt()).doubleValue());
                    node.setDiscountUnit(DmShared.vNull(node.getDiscountUnit()).doubleValue() + DmShared.vNull(listNode.get(0).getDiscountUnit()).doubleValue());
                    node.setDiscount(DmShared.vNull(node.getDiscount()).doubleValue() + DmShared.vNull(listNode.get(0).getDiscount()).doubleValue());
                    listTemp.remove(listNode.get(0));
                    listNode.remove(0);
                }
            }
            listSummary.add(node);
        }

        return listSummary;
    }

    private List<ConsultantSharing_Mdl> getSameRecord(List<ConsultantSharing_Mdl> filterList, ReportType reportType,
            final String moduleName, final Long organizationCode, final Long consultantCode, final int categoryCode, final int serviceCode) {
        if (filterList != null) {
            if (reportType == ReportType.SERVICE_ORGANIZATION_WISE_SUMMARY) {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == DmShared.iNull(organizationCode).longValue())
                        && (DmShared.iNull(x.getServiceCode()).intValue() == DmShared.iNull(serviceCode).intValue())).collect(Collectors.toList());

            } else if (reportType == ReportType.SERVICE_WISE_SUMMARY) {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getServiceCode()).intValue() == DmShared.iNull(serviceCode).intValue())).collect(Collectors.toList());

            } else if (reportType == ReportType.CATEGORY_ORGANIZATION_WISE_SUMMARY) {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getOrganizationCode()).longValue() == DmShared.iNull(organizationCode).longValue())
                        && (DmShared.iNull(x.getCategoryCode()).intValue() == DmShared.iNull(categoryCode).intValue())).collect(Collectors.toList());

            } else {
                return filterList.stream().filter(x -> (DmShared.iNull(x.getConsultantCode()).longValue() == DmShared.iNull(consultantCode).longValue())
                        && (DmShared.xNull(x.getModuleName()).equalsIgnoreCase(DmShared.xNull(moduleName)))
                        && (DmShared.iNull(x.getCategoryCode()).intValue() == DmShared.iNull(categoryCode).intValue())).collect(Collectors.toList());
            }
        } else {
            return null;
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(String.valueOf(ReportType.CATEGORY_ORGANIZATION_WISE_SUMMARY.getId()));
            listNode.setSharedKeyFieldName(ReportType.CATEGORY_ORGANIZATION_WISE_SUMMARY.getValue());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(String.valueOf(ReportType.CATEGORY_WISE_SUMMARY.getId()));
            listNode.setSharedKeyFieldName(ReportType.CATEGORY_WISE_SUMMARY.getValue());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(String.valueOf(ReportType.DETAIL.getId()));
            listNode.setSharedKeyFieldName(ReportType.DETAIL.getValue());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(String.valueOf(ReportType.SERVICE_ORGANIZATION_WISE_SUMMARY.getId()));
            listNode.setSharedKeyFieldName(ReportType.SERVICE_ORGANIZATION_WISE_SUMMARY.getValue());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(String.valueOf(ReportType.SERVICE_WISE_SUMMARY.getId()));
            listNode.setSharedKeyFieldName(ReportType.SERVICE_WISE_SUMMARY.getValue());
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Consultant");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Referred");
            lstTemp.add(listNode);

//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("2");
//            listNode.setSharedKeyFieldName("Discount");
//            lstTemp.add(listNode);
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSharing(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Before Discount");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("After Discount");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getPaymentStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Not Applicable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Paid");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Un-Paid");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
//        Connection cnTemp = null;
//        ResultSet rsTemp = null;
//        DmPreparedStatement psTemp = null;
//        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
//        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//
//            sqlQuery = "select	category as code, max(description) as name "
//                    + "		from voucher_Type "
//                    + "		where category in (" + opdCategory + "," + ipdCategory + ") "
//                    + "		group by category ";
//            sqlWithParam.setSQL(sqlQuery);
//            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                listNode = new KeyFieldExtended();
//                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
//                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
//                lstTemp.add(listNode);
//            }
//            rsTemp.close();
//            psTemp.close();
//            cnTemp.close();
//
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
//            rstTemp.setStatus(true);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("OPD");
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("IPD");
            listNode.setSharedKeyFieldName("IPD");
            lstTemp.add(listNode);

//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("Day Care");
//            listNode.setSharedKeyFieldName("Day Care");
//            lstTemp.add(listNode);
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("Emergency");
//            listNode.setSharedKeyFieldName("Emergency");
//            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Diagnostic");
            listNode.setSharedKeyFieldName("Diagnostic");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
        }

        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrganization(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  om.subCode, sg.name "
                    + "         from organizationMast as om "
                    + "         left Join subGroup as sg on om.subCode = sg.subCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  rcm.code, rcm.name "
                    + "         from roomCatMast as rcm ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  pscm.code, pscm.name "
                    + "         from patientServiceCatMast as pscm ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  sg.code, sg.name "
                    + "         from serviceGroup as sg ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  psm.code, psm.name "
                    + "         from patientServiceMast as psm ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  cm.subcode as code, sg.name, sgn.name AS nature "
                    + "         FROM consultantmast cm "
                    + "         LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + "         LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + "         WHERE sg.isActive = 1 AND sgn.manualCode in ('CN','RC') ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("nature")));
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
