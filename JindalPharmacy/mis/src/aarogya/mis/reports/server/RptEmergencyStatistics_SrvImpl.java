package aarogya.mis.reports.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.mis.reports.shared.RptEmergencyStatistics_Srv;
import aarogya.mis.reports.shared.TxtHOPDType_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptEmergencyStatistics_SrvImpl extends DmRSSPrivate implements RptEmergencyStatistics_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String filterCat = "'" + pvc.EMERGENCY_OPD + "','" + pvc.OPD + "','" + pvc.PAY_CLINIC + "'";
    private final String ipdCat = "'" + pvc.IPD + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " and psd.v_Date between :fromDate and :toDate ";

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pr.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultant").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  pr.regTime >= :fromTime ";
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  pr.regTime <= :toTime ";
            }
            if (!DmShared.xNull(filter.get("opdType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND vt.v_Type IN(" + DmServer.getParamList(":short_name", filter.get("opdType").getTag()) + " )";
            } else {
                condition += " and vt.category in (" + filterCat + ") ";
            }
//            sqlQuery = "select	pr.consultantCode, max(con.name) as consultantName, count(pr.regDocId) as noOfSlip, count(pra.regDocId) as noOfAdmit "
//                    + "		from patientRegistration as pr "
//                    + "		left join subGroup as con on pr.consultantCode = con.subCode "
//                    + "		left join voucher_Type as vt on pr.v_Type = vt.v_Type "
//                    + "		left join patientRegistration as pra on pr.regDocId = pra.regDocIdOld and pra.v_Type = (select v_Type from voucher_Type where category = " + ipdCat + ") "
//                    + "		where pr.isCancelled = 0 and pr.site_Code = '" + DmServer.getSsnSiteCode(this) + "' " + condition
//                    + "		group by pr.consultantCode "
//                    + "		order by max(con.name)";
            
            
            sqlQuery = "select	pr.consultantCode, max(con.name) as consultantName, sum(psd.quantity) as noOfSlip, count(ipd.regDocIdOld) as noOfAdmit 		 "
		+ "from patientRegistration as pr 		 "
		+ "left join patientServiceDetail as psd on psd.regDocId = pr.regDocId "
		+ "left join patientServiceMast psm on psm.code = psd.serviceCode "
		+ "left join subGroup as con on pr.consultantCode = con.subCode 		 "
		+ "left join voucher_Type as vt on pr.v_Type = vt.v_Type 		 "
		+ "left join ( "
		+ "	select	pri.regDocIdOld "
		+ "			from patientRegistration as pri "
		+ "			left join voucher_Type vt on pri.v_Type = vt.v_type "
		+ "			where vt.category in ("+ ipdCat +") and pri.isCancelled = 0 and pri.regDocIdOld is not null "
		+ "			group by pri.regDocIdOld "
		+ ") as ipd on ipd.regDocIdOld = pr.regDocId  "
		+ "where psd.isCancelled = 0 and psd.site_Code = '" + DmServer.getSsnSiteCode(this) + "' and psm.shortName = 'CF' " + condition
		+ "group by pr.consultantCode " 		
		+ "order by max(con.name)";
                
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()));

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultantCode", DmShared.xNull(filter.get("consultant").getTag()));
            }
            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("fromTime", DmShared.xNull(filter.get("fromTime").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("toTime", DmShared.xNull(filter.get("toTime").getText()), true);
            }
            if (!DmShared.xNull(filter.get("opdType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("short_name", DmShared.xNull(filter.get("opdType").getTag()), true);
            }
            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();
            jasperReport = DmServer.compileJasper("MIS_EmergencyStatisticsReport");
            jasperPrint = DmServer.fillJasper(this, "Emergency Statistics Report ", jasperReport, hm, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  cm.subcode as code,sg.name "
                    + "         FROM consultantmast cm "
                    + "         LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + "         LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + "         LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + "         WHERE CSL.site_Code = :site_Code AND sg.isActive = '1' AND sgn.manualCode = 'CN' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
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
    public DmResultPageing<List<TxtHOPDType_Mdl>> getTxtHOPDType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHOPDType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHOPDType_Mdl> lstTemp = new ArrayList<>();
        TxtHOPDType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT vt.v_type, vt.description, vt.short_Name FROM voucher_type AS "
                    + " vt WHERE vt.category in (" + filterCat + ") ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHOPDType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("description")));
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
