package aarogya.mis.graphicalDashboard.server;

import aarogya.mis.graphicalDashboard.shared.*;
import aarogya.mis.graphicalDashboard.shared.GraphicalDashboar_Vld.*;
import aarogya.mis.graphicalDashboard.shared.MainChart_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import java.sql.SQLException;
import java.util.Calendar;

public class GraphicalDashBoard_SrvImpl extends DmRSSPrivate implements GraphicalDashBoard_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String recOPD = "'" + pvc.OPD_RECEIPT + "','" + pvc.OPD_REFUND_RECEIPT + "','" + pvc.OPD_REGISTRATION_RECEIPT
            + "','" + pvc.OPD_SERVICE_RECEIPT + "','" + pvc.OPD_APPOINTMENT_RECEIPT + "','" + pvc.UHID_REGISTRATION_RECEIPT + "'";

    private final String mdlOPD = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.OPD_APPOINTMENT + "','" + pvc.PAY_CLINIC + "'";

    private final String recIPD = "'" + pvc.IPD_RECEIPT + "','" + pvc.IPD_REFUND_RECEIPT + "','" + pvc.ADVANCE_BOOKING_RECEIPT
            + "','" + pvc.ADVANCE_BOOKING_REFUND + "','" + pvc.ADVANCE_ADJUSTMENT + "','" + pvc.CREDIT_TO_PATIENT + "'";

    private final String recLAB = "'" + pvc.LAB_RECEIPT + "','" + pvc.LAB_REFUND_RECEIPT + "','" + pvc.LAB_REGISTRATION_RECEIPT + "'";

    private final String recPHARMACY = "'" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT
            + "','" + pvc.PHARMACY_REFUND + "'";

    private final String salePHARMACY = "'" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "'";

    private final String challanPHARMACY = "'" + pvc.PHARMACY_PURCHASE_CHALLAN + "','" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "'";

    private final String purchasePHARMACY = "'" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT
            + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_RETURN
            + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "'";

    private MainChart_Mdl getOPDTabData(MainChart_Mdl mrTemp, Connection cnTemp, Filter_Mdl filterMdl) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null; 
        String sqlQuery;
        DmChart_Mdl node;
        try {

            //<editor-fold defaultstate="collapsed" desc="Consultant Wise OPD"> 
            rsTemp = graphViewOPDQuery(GraphView.CONSULTANT_WISE, filterMdl, cnTemp).executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("groupName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getLstOPDConsultantWise().add(node);
            }
            rsTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="OPD Collection">
            mrTemp.getLstOPDCollection().addAll(graphCollectionQuery(TabType.TAB_OPD, filterMdl, cnTemp));
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Department Wise OPD">
            sqlQuery = "select  count(pr.regDocId) as regCount, max(dep.name) as departmentExpand, "
                    + "         max(case when len(dep.name) > 20 then  substring(dep.name,1,15)+'...' else dep.name end) as department "
                    + "         from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join caseMast cm on cm.code = pr.caseCode "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join zoneMast zm on zm.code = pu.zoneCode "
                    + "		left join subGroup dep on dep.subCode = pr.departmentCode "
                    + "		left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "		where pr.isCancelled = 0 and psm.shortName = 'CF' and vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "')"
                    + "                 and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "                 and (pr.v_Date between :fromDate and :toDate) "
                    + "		group by pr.departmentCode "
                    + "		order by count(pr.regDocId) desc, max(dep.name)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("siteCode", filterMdl.getSiteCodeOPD());
            psTemp.setDate("fromDate", DmServer.convertDate(filterMdl.getDateFrom()));
            psTemp.setDate("toDate", DmServer.convertDate(filterMdl.getDateto()));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("department")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("department")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getLstOPDDepartment().add(node);

                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("departmentExpand")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("departmentExpand")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getLstOPDDepartmentExpand().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Area Wise OPD">
            rsTemp = graphViewOPDQuery(GraphView.AREA_WISE, filterMdl, cnTemp).executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("groupName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListOPDChart4().add(node);
            }
            rsTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Case Wise OPD">
            rsTemp = graphViewOPDQuery(GraphView.CASE_WISE, filterMdl, cnTemp).executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("groupName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getLstOPDCaseWise().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Date Wise OPD">
            sqlQuery = "select   regCount, category, regDay, v_Date "
                    + "		from ("
                    + "             select  sum(psd.quantity) as regCount, vt.category, max(DATEPART(dd, pr.v_Date)) as regDay, pr.v_Date"
                    + "                     from patientRegistration pr "
                    + "                     left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "                     left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "                     left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "                     where pr.isCancelled = 0 and psm.shortName = 'CF' and vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "')"
                    + "                         and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "                         and (pr.v_Date between :fromDate and :toDate) "
                    + "                     group by pr.v_Date, vt.category"
                    + "             union all"
                    + "             select 0 as regCount, '' as category, 0 as regDay, null as v_Date"
                    + "		) as tbl "
                    + "		order by v_Date, case when category = '" + pvc.EMERGENCY_OPD + "' then 0 else 1 end";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("siteCode", filterMdl.getSiteCodeOPD());
            psTemp.setDate("fromDate", DmServer.convertDate(filterMdl.getDateFrom()));
            psTemp.setDate("toDate", DmServer.convertDate(filterMdl.getDateto()));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setGroupCode(DmShared.xNull(rsTemp.getString("category")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("category")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("regDay")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("regDay")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListOPDChart6().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Billed/Unbilled">
            sqlQuery = "select  sum(unBilled) as unBilled, sum(billed) as billed, sum(medCount) as medCount"
                    + "		from ("
                    + "		select  0 as unBilled, 0 as billed, count(pmc.docId) as medCount"
                    + "                 from patientMedicalCertificate pmc "
                    + "			left join voucher_Type vt on vt.v_Type = pmc.v_Type "
                    + "			where pmc.isCancelled = 0 and vt.category in ('" + pvc.MEDICAL_CERTIFICATE + "') "
                    + "                 and pmc.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "                 and (pmc.v_Date between :fromDate and :toDate) "
                    + "		union all "
                    + "		select  sum(unBill) as unBilled, sum(bill) as billed, 0 as medCount"
                    + "			from ( "
                    + "			select  max(case when psd.sbDocId is null then 1 else 0 end) as unBill,"
                    + "				max(case when psd.sbDocId is not null then 1 else 0 end) as bill"
                    + "				from ("
                    + "				select  pla.regDocId "
                    + "					from patientLimitApproval pla "
                    + "					left join voucher_Type vt on vt.v_Type = pla.v_Type "
                    + "					where pla.isCancelled = 0 and vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "', '" + pvc.OPD_SERVICE + "','" + pvc.PAY_CLINIC + "')"
                    + "                                 and pla.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "                                 and (pla.v_Date between :fromDate and :toDate) and pla.nature = " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ""
                    + "					group by pla.regDocId "
                    + "				) pla "
                    + "				left join patientServiceDetail psd on psd.regDocId = pla.regDocId "
                    + "				group by psd.regDocId "
                    + "			) as tbl "
                    + "		) as tblMain ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("siteCode", filterMdl.getSiteCodeOPD());
            psTemp.setDate("fromDate", DmServer.convertDate(filterMdl.getDateFrom()));
            psTemp.setDate("toDate", DmServer.convertDate(filterMdl.getDateto()));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                mrTemp.setLblOPDUnBilled(DmShared.iNull(rsTemp.getString("unBilled")).toString());
                mrTemp.setLblOPDBilled(DmShared.iNull(rsTemp.getString("billed")).toString());
                mrTemp.setLblOPDMedCount(DmShared.iNull(rsTemp.getString("medCount")).toString());
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

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

        return mrTemp;
    }

    private MainChart_Mdl getIPDTabData(MainChart_Mdl mrTemp, Connection cnTemp, Filter_Mdl filterMdl) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmChart_Mdl node;
        try {

            //<editor-fold defaultstate="collapsed" desc="Consultant Wise">
            psTemp = graphViewIPDQuery(GraphView.CONSULTANT_WISE, filterMdl, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("groupName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListIPDChart1().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="IPD Collection">
            mrTemp.getListIPDChart2().addAll(graphCollectionQuery(TabType.TAB_IPD, filterMdl, cnTemp));
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Department Wise">
            sqlQuery = "select  count(pr.regDocId) as regCount, max(pr.departmentCode) as departmentCode, max(dt.name) as departmentExpand, "
                    + "         max(case when len(dt.name) > 20 then substring(dt.name,1,20)+'...' else dt.name end) as department "
                    + "         from patientRegistration pr "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join subGroup dt on dt.subCode=pr.departmentCode "
                    + "         where pr.isCancelled = 0 and vt.category in ('" + pvc.IPD + "') "
                    + "               and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "               and (pr.v_Date between :fromDate and :toDate) "
                    + "         group by dt.subCode   "
                    + "         order by count(pr.regDocId) desc, max(dt.name)";

            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_IPD, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("department")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListIPDChart3().add(node);

                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("departmentExpand")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListExpandIPDChart3().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Room Category Wise">
            psTemp = graphViewIPDQuery(GraphView.ROOM_CATEGORY_WISE, filterMdl, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("groupName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListIPDChart4().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Case Wise">
            psTemp = graphViewIPDQuery(GraphView.CASE_WISE, filterMdl, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("groupName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListIPDChart5().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Day Wise Bed Occupancy">
            sqlQuery = "select  occupied as occupied, regDay as regDay, v_Date, viewType "
                    + "		from ("
                    + "		select  count(rm.code) as occupied, max(datepart(dd, pr.v_Date)) as regDay, pr.v_Date, 'Occupied' as viewType "
                    + "			from roomMast rm "
                    + "			left join patientRegistration pr on pr.roomCode = rm.code "
                    + "			left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "			where rcm.roomType = 'Room' and pr.isCancelled = 0 and rm.isActive = 1 "
                    + "                         and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "                         and (pr.v_Date between :fromDate and :toDate)"
                    + "			group by pr.v_Date"
                    + "		union all"
                    + "		select  (max(tbl.roomCount) - count(rm.code)) as occupied, max(datepart(dd, pr.v_Date)) as regDay, pr.v_Date, 'Vacant' as viewType "
                    + "			from roomMast rm "
                    + "			left join patientRegistration pr on pr.roomCode = rm.code "
                    + "			left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "			left join ("
                    + "				select  count(rm.code) as roomCount, rcm.roomType"
                    + "					from roomMast rm "
                    + "					left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "					where rcm.roomType = 'Room' and rm.isActive = 1 "
                    + "                                     and rm.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "					group by rcm.roomType"
                    + "			) tbl on tbl.roomType = rcm.roomType"
                    + "			where rcm.roomType = 'Room' and pr.isCancelled = 0 and rm.isActive = 1 "
                    + "                         and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "                         and (pr.v_Date between :fromDate and :toDate)"
                    + "			group by pr.v_Date"
                    + "		) as tbl"
                    + "		order by v_Date, viewType ";
            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_IPD, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setGroupCode(DmShared.xNull(rsTemp.getString("viewType")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("viewType")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("regDay")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("regDay")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("occupied")).intValue());
                mrTemp.getListIPDChart6().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

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

        return mrTemp;
    }

    private MainChart_Mdl getDiagnosticsTabData(MainChart_Mdl mrTemp, Connection cnTemp, Filter_Mdl filterMdl) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmChart_Mdl node;
        try {
            //<editor-fold defaultstate="collapsed" desc="Diagnostic Category Wise">
            sqlQuery = "select  sum(psd.quantity) as regCount, max(pscm.name) as category "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "		left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "		where vt.category in ('LAB') and psd.isCancelled = 0 "
                    + "             and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeDiagnostic())) + ") "
                    + "             and (pr.v_Date between :fromDate and :toDate) "
                    + "		group by psm.categoryCode "
                    + "		order by count(pr.regDocId) desc ";

            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_DIAGNOSTIC, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("category")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("category")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListDIAGChart1().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Diagnostic Collection">
            mrTemp.getListDIAGChart2().addAll(graphCollectionQuery(TabType.TAB_DIAGNOSTIC, filterMdl, cnTemp));
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Area Wise Lab">
            sqlQuery = "select  sum(regCount) as regCount, max(zm.name) as zone"
                    + "		from ("
                    + "		select  max(pu.zoneCode) as zoneCode, max(psd.quantity) as regCount"
                    + "			from patientRegistration pr "
                    + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "			left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "			left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "			left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "			left join patientUHID pu on pu.docId = pr.uhid "
                    + "			where vt.category in ('" + pvc.LAB_REGISTRATION + "') and psd.isCancelled = 0 and pu.zoneCode is not null"
                    + "                     and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeDiagnostic())) + ") "
                    + "                     and (pr.v_Date between :fromDate and :toDate) "
                    + "			group by psd.regDocId"
                    + "			having sum(psd.quantity) > 0"
                    + "		) as tbl"
                    + "		left join zoneMast zm on zm.code = tbl.zoneCode "
                    + "		group by tbl.zoneCode"
                    + "		order by max(zm.name)";
            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_DIAGNOSTIC, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("zone")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("zone")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                mrTemp.getListDIAGChart3().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Day Wise Investigation">
            if (DmShared.iNull(filterMdl.getServiceCode()).intValue() > 0) {
                sqlQuery = "select  sum(psd.quantity) as regCount, max(psm.name) as serviceName, max(datepart(dd, pr.v_Date)) as regDay"
                        + "         from patientRegistration pr"
                        + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                        + "         left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                        + "         left join patientServiceMast psm on psm.code = psd.serviceCode "
                        + "         where vt.category in ('" + pvc.LAB_REGISTRATION + "') and psd.isCancelled = 0 and psd.serviceCode = :serviceCode "
                        + "             and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodeDiagnostic())) + ") "
                        + "             and (pr.v_Date between :fromDate and :toDate) "
                        + "         group by pr.v_Date"
                        + "         having sum(psd.quantity) > 0 "
                        + "         order by pr.v_Date";
                psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_DIAGNOSTIC, cnTemp);
                psTemp.setString("serviceCode", DmShared.iNull(filterMdl.getServiceCode()).toString());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    node = new DmChart_Mdl();
                    node.setLabelCode(DmShared.xNull(rsTemp.getString("regDay")));
                    node.setLabelName(DmShared.xNull(rsTemp.getString("regDay")));
                    node.setDataValue(DmShared.iNull(rsTemp.getString("regCount")).intValue());
                    mrTemp.getListDIAGChart6().add(node);
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

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

        return mrTemp;
    }

    private MainChart_Mdl getPHARMTabData(MainChart_Mdl mrTemp, Connection cnTemp, Filter_Mdl filterMdl) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmChart_Mdl node;
        try {

            //<editor-fold defaultstate="collapsed" desc="Company Wise Sale">
            sqlQuery = "select  top " + filterMdl.getTopCompanyCount() + " im.itemMakeCode, round(sum(sbd.oh_Amt_Net), 2) as companySale, "
                    + "         max(tbl.oh_Amt_Net) as totalSale, max(imm.companyName) as companyName, "
                    + "         round(sum(sbd.oh_Amt_Net)/max(tbl.oh_Amt_Net)*100,2) as perSale "
                    + "		from saleBill sb "
                    + "		left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "		left join saleBillDetail sbd on sbd.docId = sb.docId "
                    + "		left join itemMast im on im.code = sbd.itemCode "
                    + "		left join itemMakeMast imm on imm.code = im.itemMakeCode "
                    + "		left join ("
                    + "			select  sum(sb.oh_Amt_Net) as oh_Amt_Net"
                    + "				from saleBill sb "
                    + "				left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "				where sb.isCancelled = 0 and vt.category in (" + salePHARMACY + ") "
                    + "                             and sb.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "                             and (sb.v_Date between :fromDate and :toDate) "
                    + "		) as tbl on 1 = 1 "
                    + "		where sb.isCancelled = 0 and vt.category in (" + salePHARMACY + ") and sbd.isFooter = 0 "
                    + "             and sb.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "             and (sb.v_Date between :fromDate and :toDate) "
                    + "		group by im.itemMakeCode"
                    + "		having sum(sbd.quantity) > 0"
                    + "		order by sum(sbd.oh_Amt_Net) desc, max(imm.companyName)";
            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_PHARMACY, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("companyName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("companyName")));
                node.setDataValue(DmShared.vNull(rsTemp.getString("perSale")).doubleValue());
                mrTemp.getListPHARMChart1().add(node);
                mrTemp.setTotalSale(DmServer.vNull(rsTemp.getString("totalSale"), "00.00"));
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Sale Revenue">
            sqlQuery = "select  sum(prc.amount) as amount, prc.cardType"
                    + "		from saleBill sb"
                    + "		left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "		left join ("
                    + "			select  prc.docId, (cl.amtDr - amtCr) as amount, ctm.name as cardType "
                    + "				from patientReceipt prc "
                    + "				left join voucher_Type vt on vt.v_Type = prc.v_Type "
                    + "				left join cardLedger cl on cl.docId = prc.docId "
                    + "				left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                    + "				where prc.isCancelled = 0 and vt.category in ('" + pvc.PHARMACY_BILL_RECEIPT + "', '" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "') "
                    + "                             and prc.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "                             and (prc.v_Date between :fromDate and :toDate) "
                    + "			union all "
                    + "			select  pla.docId, pla.amount, case pla.nature when " + PaymentSource.PARTIAL_PAYMENT.getEnumValue() + ""
                    + "                         then '" + PaymentSource.PARTIAL_PAYMENT.getEnumName() + "' "
                    + "                         when " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " "
                    + "                         then '" + PaymentSource.CREDIT_TO_PATIENT.getEnumName() + "' "
                    + "                         when " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " "
                    + "                         then '" + PaymentSource.CREDIT_TO_REGISTRATION.getEnumName() + "' "
                    + "                         when " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " "
                    + "                         then '" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumName() + "' end as cardType"
                    + "				from patientLimitApproval pla "
                    + "				left join voucher_Type vt on vt.v_Type = pla.v_Type "
                    + "				where pla.isCancelled = 0 and vt.category in (" + salePHARMACY + ")"
                    + "                             and pla.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "                             and (pla.v_Date between :fromDate and :toDate) "
                    + "		) prc on prc.docId = sb.docId "
                    + "		where sb.isCancelled = 0 "
                    + "         and vt.category in ('" + pvc.PHARMACY_BILL_CASH + "') "
                    + "             and sb.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "             and (sb.v_Date between :fromDate and :toDate) "
                    + "		group by prc.cardType"
                    + "		order by sum(prc.amount) desc, prc.cardType";
            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_PHARMACY, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("cardType")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("cardType")) + "(" + DmServer.vNull(rsTemp.getString("amount"), "0.00") + ")");
                node.setDataValue(DmShared.vNull(rsTemp.getString("amount")).doubleValue());
                mrTemp.getListPHARMChart2().add(node);
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Day Wise Purchase/Sale">
            sqlQuery = "select  sum(oh_Amt_Net) as oh_Amt_Net, max(datepart(dd, v_Date)) as onDay, max(viewType) as viewType"
                    + "		from ("
                    + "			select  pb.oh_Amt_Net, pb.v_Date, 'Purchase' as viewType"
                    + "				from pBill pb"
                    + "				left join voucher_Type vt on vt.v_Type = pb.v_Type "
                    + "				where isnull(pb.approvedBy, '') <> '' and vt.category in (" + purchasePHARMACY + ") "
                    + "                             and pb.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "                             and (pb.v_Date between :fromDate and :toDate) "
                    + "			union all"
                    + "			select  pc.oh_Amt_Net, pc.v_Date, 'Purchase' as viewType"
                    + "				from pChallan pc "
                    + "				left join voucher_Type vt on vt.v_Type = pc.v_Type "
                    + "				where vt.category in (" + challanPHARMACY + ") "
                    + "                             and pc.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "                             and (pc.v_Date between :fromDate and :toDate) "
                    + "			union all "
                    + "			select  sb.oh_Amt_Net, sb.v_Date, 'Sale' as viewType"
                    + "				from saleBill sb"
                    + "				left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "				where sb.isCancelled = 0 and vt.category in (" + salePHARMACY + ") "
                    + "                             and sb.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "                             and (sb.v_Date between :fromDate and :toDate) "
                    + "			union all "
                    + "			select 0 as oh_Amt_Net, null as v_Date, '' as viewType"
                    + "		) tbl"
                    + "		group by v_Date, viewType"
                    + "		order by v_Date, viewType";
            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_PHARMACY, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setGroupCode(DmShared.xNull(rsTemp.getString("viewType")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("viewType")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("onDay")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("onDay")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("oh_Amt_Net")).intValue());
                mrTemp.getListPHARMChart3().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Item Wise Sale">
            sqlQuery = "select  top " + filterMdl.getTopItemCount() + " round(sum(sbd.oh_Amt_Net), 2) as companySale, "
                    + "         max(tbl.oh_Amt_Net) as totalSale, max(im.displayName) as companyName, "
                    + "         round(sum(sb.oh_Amt_Net)/max(tbl.oh_Amt_Net)*100,2) as perSale "
                    + "		from saleBill sb "
                    + "		left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "		left join saleBillDetail sbd on sbd.docId = sb.docId "
                    + "		left join itemMast im on im.code = sbd.itemCode "
                    + "		left join itemMakeMast imm on imm.code = im.itemMakeCode "
                    + "		left join ("
                    + "			select  sum(sb.oh_Amt_Net) as oh_Amt_Net"
                    + "				from saleBill sb "
                    + "				left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "				where sb.isCancelled = 0 and vt.category in (" + salePHARMACY + ") "
                    + "                             and sb.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "                             and (sb.v_Date between :fromDate and :toDate) "
                    + "		) as tbl on 1 = 1 "
                    + "		where sb.isCancelled = 0 and vt.category in (" + salePHARMACY + ") and sbd.isFooter = 0 "
                    + "             and sb.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "             and (sb.v_Date between :fromDate and :toDate) "
                    + "		group by sbd.itemCode"
                    + "		having sum(sbd.quantity) > 0"
                    + "		order by sum(sbd.oh_Amt_Net) desc, max(im.displayName)";
            psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_PHARMACY, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("companyName")));
                node.setLabelName(DmShared.xNull(rsTemp.getString("companyName")));
                node.setDataValue(DmShared.vNull(rsTemp.getString("perSale")).doubleValue());
                mrTemp.getListPHARMChart4().add(node);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Day Wise Collection">
            if (DmShared.iNull(filterMdl.getPaymentMode()).longValue() > 0) {
                sqlQuery = "select  sum(prc.amount) as amount, max(datepart(dd, prc.v_Date)) as billDay, max(prc.cardType) as cardType "
                        + "         from ( "
                        + "		select  prc.docId, prc.v_Date, (cl.amtDr - amtCr) as amount, ctm.name as cardType "
                        + "			from patientReceipt prc "
                        + "			left join voucher_Type vt on vt.v_Type = prc.v_Type "
                        + "			left join cardLedger cl on cl.docId = prc.docId "
                        + "			left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                        + "			where prc.isCancelled = 0 and vt.category in ('" + pvc.PHARMACY_BILL_RECEIPT + "', '" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "') "
                        + "                         and prc.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                        + "                         and (prc.v_Date between :fromDate and :toDate) and cl.cardTypeCode = :cardTypeCode "
                        + "		union all "
                        + "		select  pla.docId, pla.v_Date, pla.amount, case pla.nature when " + PaymentSource.PARTIAL_PAYMENT.getEnumValue() + ""
                        + "                     then '" + PaymentSource.PARTIAL_PAYMENT.getEnumName() + "' "
                        + "                     when " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " "
                        + "                     then '" + PaymentSource.CREDIT_TO_PATIENT.getEnumName() + "' "
                        + "                     when " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " "
                        + "                     then '" + PaymentSource.CREDIT_TO_REGISTRATION.getEnumName() + "' "
                        + "                     when " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " "
                        + "                     then '" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumName() + "' end as cardType"
                        + "                     from patientLimitApproval pla "
                        + "			left join voucher_Type vt on vt.v_Type = pla.v_Type "
                        + "			where pla.isCancelled = 0 and vt.category in (" + salePHARMACY + ")"
                        + "                         and pla.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                        + "                         and (pla.v_Date between :fromDate and :toDate) and cast(pla.nature as int) = :cardTypeCode "
                        + "		) prc "
                        + "		group by prc.v_Date, prc.cardType "
                        + "		order by prc.v_Date, prc.cardType";
                psTemp = setFilter(filterMdl, sqlQuery, TabType.TAB_PHARMACY, cnTemp);
                psTemp.setString("cardTypeCode", filterMdl.getPaymentMode());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    node = new DmChart_Mdl();
                    node.setLabelCode(DmShared.xNull(rsTemp.getString("billDay")));
                    node.setLabelName(DmShared.xNull(rsTemp.getString("billDay")));
                    node.setDataValue(DmShared.iNull(rsTemp.getString("amount")).intValue());
                    mrTemp.getListPHARMChart6().add(node);
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

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

        return mrTemp;
    }

    private MainChart_Mdl getNetRevenueTabData(MainChart_Mdl mrTemp, Connection cnTemp, Filter_Mdl filterMdl) {

        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        //========================NET Revenue Charts List=========================
        List<DmChart_Mdl> listNETChart1 = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;

        try {
            //31=============================<NET REVENUE Chart1>==================================
            sqlQuery = "select  sum(psd.oh_Amt_Net) as amount, 'OPD' as revenueType "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "		where pr.isCancelled = 0 and vt.category in (" + mdlOPD + ") and pr.uhid is not null "
                    + "             and pr.site_Code in (" + DmServer.getParamList(":siteCodeOPD", DmShared.xNull(filterMdl.getSiteCodeOPD())) + ") "
                    + "             and (pr.v_Date between :fromDate and :toDate) "
                    + "union all "
                    + "select   sum(psd.oh_Amt_Net) as amount, 'IPD' as revenueType"
                    + "		from saleBill sb "
                    + "		left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "		left join patientServiceDetail psd on psd.sbDocId = sb.docId "
                    + "		where isnull(sb.approvedBy, '') <> '' and vt.category in ('" + pvc.IPD_BILL + "') and sb.docId <> sb.regDocId "
                    + "             and sb.site_Code in (" + DmServer.getParamList(":siteCodeIPD", DmShared.xNull(filterMdl.getSiteCodeIPD())) + ") "
                    + "             and (sb.v_Date between :fromDate and :toDate) and psd.billingNature  <> 2 and psd.isFooter = 0 "
                    + "union all "
                    + "select   sum(psd.oh_Amt_Net) as amount, 'Diagnostic' as revenueType"
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "		where pr.isCancelled = 0 and vt.category in ('" + pvc.LAB_REGISTRATION + "')"
                    + "             and pr.site_Code in (" + DmServer.getParamList(":siteCodeLAB", DmShared.xNull(filterMdl.getSiteCodeDiagnostic())) + ") "
                    + "             and (pr.v_Date between :fromDate and :toDate) "
                    + "union all "
                    + "select  round(sum(sb.oh_Amt_Net), 2) as amount, 'Pharmacy' as revenueType"
                    + "		from saleBill sb "
                    + "		left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "		where sb.isCancelled = 0 and vt.category in ('" + pvc.PHARMACY_BILL + "', '" + pvc.PHARMACY_BILL_RETURN + "') "
                    + "          and sb.site_Code in (" + DmServer.getParamList(":siteCodePharmacy", DmShared.xNull(filterMdl.getSiteCodePharmacy())) + ") "
                    + "          and (sb.v_Date between :fromDate and :toDate) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filterMdl.getDateFrom()));
            psTemp.setDate("toDate", DmServer.convertDate(filterMdl.getDateto()));
            psTemp.setStringList("siteCodeLAB", filterMdl.getSiteCodeDiagnostic());
            psTemp.setStringList("siteCodeIPD", filterMdl.getSiteCodeIPD());
            psTemp.setStringList("siteCodeOPD", filterMdl.getSiteCodeOPD());
            psTemp.setStringList("siteCodePharmacy", filterMdl.getSiteCodePharmacy());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("revenueType") + " (" + DmShared.iNull(rsTemp.getString("amount")).toString() + ")"));
                node.setLabelName(DmShared.xNull(rsTemp.getString("revenueType") + " (" + DmShared.iNull(rsTemp.getString("amount")).toString() + ")"));
                node.setDataValue(DmShared.iNull(rsTemp.getString("amount")).intValue());
                mrTemp.getListNETChart1().add(node);
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

        return mrTemp;
    }

    private DmPreparedStatement graphViewIPDQuery(GraphView graphView, Filter_Mdl filter, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery, groupBy = "", orderBy = "", groupName = "";
        try {
            switch (graphView) {
                case CONSULTANT_WISE:
                    groupName = "max(case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end)";
                    groupBy = "pr.consultantCode";
                    orderBy = "max(con.name)";
                    break;
                case CASE_WISE:
                    groupName = "max(cm.name)";
                    groupBy = "pr.caseCode";
                    orderBy = "max(cm.name)";
                    break;
                case AREA_WISE:
                    groupName = "max(zm.name)";
                    groupBy = "pu.zoneCode";
                    orderBy = "max(zm.name)";
                    break;
                case ROOM_CATEGORY_WISE:
                    groupName = "max(rcm.name)";
                    groupBy = "rm.roomCategoryCode";
                    orderBy = "max(rcm.name)";
                    break;
            }

            sqlQuery = "select  count(pr.regDocId) as regCount, " + groupName + " as groupName "
                    + "		from roomMast rm "
                    + "		left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "		left join patientRegistration pr on pr.roomCode = rm.code "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join caseMast cm on cm.code = pr.caseCode "
                    + "		where rcm.roomType = 'Room' and rm.isActive = 1 and vt.category in ('IPD') and pr.isCancelled = 0 "
                    + "                 and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filter.getSiteCodeOPD())) + ") "
                    + "                 and (pr.v_Date between :fromDate and :toDate) "
                    + "		group by " + groupBy
                    + "		order by count(pr.regDocId) desc, " + orderBy;
            psTemp = setFilter(filter, sqlQuery, TabType.TAB_IPD, cnTemp);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return psTemp;
    }

    private DmPreparedStatement graphViewOPDQuery(GraphView graphView, Filter_Mdl filter, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery, groupBy = "", orderBy = "", groupName = "";
        try {
            switch (graphView) {
                case CONSULTANT_WISE:
                    groupName = "max(case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end)";
                    groupBy = "pr.consultantCode";
                    orderBy = "max(con.name)";
                    break;
                case CASE_WISE:
                    groupName = "max(cm.name)";
                    groupBy = "pr.caseCode";
                    orderBy = "max(cm.name)";
                    break;
                case AREA_WISE:
                    groupName = "max(zm.name)";
                    groupBy = "pu.zoneCode";
                    orderBy = "max(zm.name)";
                    break;
            }

            sqlQuery = "select  sum(psd.quantity) as regCount, " + groupName + " as groupName "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join caseMast cm on cm.code = pr.caseCode "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join zoneMast zm on zm.code = pu.zoneCode "
                    + "		left join subGroup dep on dep.subCode = pr.departmentCode "
                    + "		left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "		where pr.isCancelled = 0 and psm.shortName = 'CF' and vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "')"
                    + "                 and pr.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filter.getSiteCodeOPD())) + ") "
                    + "                 and (pr.v_Date between :fromDate and :toDate) "
                    + "		group by " + groupBy
                    + "         having sum(psd.quantity) > 0 "
                    + "		order by count(pr.regDocId) desc, " + orderBy;
            psTemp = setFilter(filter, sqlQuery, TabType.TAB_OPD, cnTemp);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return psTemp;
    }

    private List<DmChart_Mdl> graphCollectionQuery(TabType tabType, Filter_Mdl filter, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, entryType = "", recCategory = "";
        List<DmChart_Mdl> lstNode = new ArrayList<>();
        DmChart_Mdl node = null;
        try {
            switch (tabType) {
                case TAB_OPD:
                    recCategory = recOPD;
                    entryType = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";
                    break;
                case TAB_IPD:
                    recCategory = recIPD;
                    entryType = "'" + pvc.IPD + "'";
                    break;
                case TAB_DIAGNOSTIC:
                    recCategory = recLAB;
                    entryType = "'" + pvc.LAB_REGISTRATION + "'";
                    break;
                case TAB_PHARMACY:
                    recCategory = recPHARMACY;
                    entryType = "'" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_REFUND + "'";
                    break;
            }
            sqlQuery = "select  round(sum(prc.amount), 2) as amount, max(prc.cardType) as cardType"
                    + "		from ("
                    + "             select  prc.regDocId, sum(prc.amount) as amount, max(ctm.shortName) as shortName, max(ctm.name) as cardType"
                    + "                     from patientReceipt prc "
                    + "                     left join voucher_Type vt on vt.v_Type = prc.v_Type "
                    + "                     left join cardLedger cl on cl.docId = prc.docId "
                    + "                     left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                    + "                     where prc.isCancelled = 0 and vt.category in (" + recCategory + ") "
                    + "                         and prc.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filter.getSiteCodeOPD())) + ") "
                    + "                         and (prc.v_Date between :fromDate and :toDate)"
                    + "                     group by prc.regDocId, cl.cardTypeCode "
                    + "             union all"
                    + "             select  pla.regDocId, sum(pla.amount) as amount, '' as shortName, "
                    + "                     max(case pla.nature when " + PaymentSource.PARTIAL_PAYMENT.getEnumValue() + ""
                    + "                         then '" + PaymentSource.PARTIAL_PAYMENT.getEnumName() + "' "
                    + "                         when " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " "
                    + "                         then '" + PaymentSource.CREDIT_TO_PATIENT.getEnumName() + "' "
                    + "                         when " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " "
                    + "                         then '" + PaymentSource.CREDIT_TO_REGISTRATION.getEnumName() + "' "
                    + "                         when " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " "
                    + "                         then '" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumName() + "' end) as cardType"
                    + "                     from patientLimitApproval pla "
                    + "                     left join voucher_Type vt on vt.v_Type = pla.v_Type "
                    + "                     where pla.isCancelled = 0 "
                    + "                         and vt.category in (" + entryType + ")"
                    + "                         and pla.site_Code in (" + DmServer.getParamList(":siteCode", DmShared.xNull(filter.getSiteCodeOPD())) + ") "
                    + "                         and (pla.v_Date between :fromDate and :toDate)"
                    + "                     group by pla.regDocId, pla.nature"
                    + "		) prc "
                    + "		left join patientRegistration pr on pr.regDocId = prc.regDocId"
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		where pr.isCancelled = 0 and vt.category in (" + entryType + ") "
                    + "             and prc.regDocId is not null "
                    + "		group by prc.cardType "
                    + "		order by max(case when prc.shortName <> 'ADJ' then 0 else 1 end), max(prc.cardType)";
            psTemp = setFilter(filter, sqlQuery, tabType, cnTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmChart_Mdl();
                node.setLabelCode(DmShared.xNull(rsTemp.getString("cardType") + " (" + DmServer.vNull(rsTemp.getString("amount"), "00") + ")"));
                node.setLabelName(DmShared.xNull(rsTemp.getString("cardType") + " (" + DmServer.vNull(rsTemp.getString("amount"), "00") + ")"));
                node.setDataValue(DmShared.vNull(rsTemp.getString("amount")).doubleValue());
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return lstNode;
    }

    private DmPreparedStatement setFilter(Filter_Mdl filterMdl, String sqlQuery, TabType tabType, Connection cnTemp) throws SQLException {
        DmPreparedStatement psTemp = null;
        String siteList = "";

        switch (tabType) {
            case TAB_OPD:
                siteList = filterMdl.getSiteCodeOPD();
                break;
            case TAB_IPD:
                siteList = filterMdl.getSiteCodeIPD();
                break;
            case TAB_DIAGNOSTIC:
                siteList = filterMdl.getSiteCodeDiagnostic();
                break;
            case TAB_PHARMACY:
                siteList = filterMdl.getSiteCodePharmacy();
                break;
        }
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setDate("fromDate", DmServer.convertDate(filterMdl.getDateFrom()));
        psTemp.setDate("toDate", DmServer.convertDate(filterMdl.getDateto()));
        psTemp.setStringList("siteCode", siteList);
        return psTemp;
    }

    @Override
    public DmResult<MainChart_Mdl> getChartData(Filter_Mdl filterMdl) {
        DmResult<MainChart_Mdl> dmrTemp = new DmResult();
        MainChart_Mdl mrTemp = new MainChart_Mdl();

        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            //===================================Date validation=====================================
            sqlQuery = "select case when dateName(MM,:fromDate) <>dateName(MM,:toDate) then 1 else 0 end as monthNotEqual,"
                    + "case when DATEDIFF(DAY,:fromDate,:toDate)<0 then 1 else 0 end as startDateOk ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filterMdl.getDateFrom()));
            psTemp.setDate("toDate", DmServer.convertDate(filterMdl.getDateto()));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("monthNotEqual")).intValue() == 1) {
                    throw new UnsupportedOperationException("Months are not same");

                } else if (DmShared.iNull(rsTemp.getString("startDateOk")).intValue() == 1) {
                    throw new UnsupportedOperationException("From Date should be less than To Date");

                } else if (DmShared.xNull(filterMdl.getDateFrom()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("From Date is mandatory");

                } else if (DmShared.xNull(filterMdl.getDateto()).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("To Date is mandatory");

                }

            }
            rsTemp.close();
            psTemp.close();

            //=================================OPD===========================
            getOPDTabData(mrTemp, cnTemp, filterMdl);
            //=================================IPD===========================
            getIPDTabData(mrTemp, cnTemp, filterMdl);
            //=================================Diagnostics===================
            getDiagnosticsTabData(mrTemp, cnTemp, filterMdl);
            //=================================Pharmacy======================
            getPHARMTabData(mrTemp, cnTemp, filterMdl);
            //=================================Net Revenue===================
            getNetRevenueTabData(mrTemp, cnTemp, filterMdl);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInvestigation(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select psm.code,psm.name from patientServiceMast  psm  "
                    + "left join patientServiceCatMast as pscm on pscm.code=psm.categoryCode  "
                    + "where pscm.filterNature=1 and psm.isActive=1  ");

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
    public String[] getServerDateTime(String fromDate) {
        String[] dateFilter = new String[2];
        String sqlQuery;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {
            dateFilter[0] = fromDate;
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT EOMONTH( :fromDate ) AS toDate; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(fromDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dateFilter[1] = DmShared.dNull(rsTemp.getDate("toDate"));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception e) {

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

        return dateFilter;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentMode(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" select code, name "
                    + "from ( "
                    + "select ctm.code, ctm.name from cardTypeMast as ctm "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " as code , 'CTR' as name "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " as code , 'CTO' as name "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " as code , 'CTP' as name "
                    + ") as ctm ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select sm.code, sm.name "
                    + "         from siteMast sm"
                    + "         where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        Calendar cal;
        try {
            cal = Calendar.getInstance();

            cal.setTime(DmShared.convertDate(DmServer.getServerDateTime()[0]));
            mrTemp.setCurrentDate(DmShared.dNull(cal.getTime()));

            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
            mrTemp.setMonthLastDate(DmShared.dNull(cal.getTime()));

            mrTemp.setSiteCode(DmServer.getSsnSiteCode(this));
            mrTemp.setSiteName(DmServer.getSsnSiteName(this));

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }

}
