package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptAreaWiseLabRegistrationReport_Srv;
import aarogya.mis.standardReports.shared.RptAreaWiseLabRegistrationReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
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

public class RptAreaWiseLabRegistrationReport_SrvImpl extends DmRSSPrivate implements RptAreaWiseLabRegistrationReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String category = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", mainQuery, reportName = "", condition = "";
        try {
            RptAreaWiseLabRegistrationReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (pr.v_Date between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("areaType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND zm.code IN (" + DmServer.getParamList(":areaType", filter.get("areaType").getTag()) + " )";
            }

            mainQuery = "select pr.regDocId, pr.recId as regNo, pr.v_Date, pr1.recId as refNo, pu.recId as uhid, "
                    + "		case when pr.regDocIdOld is null then 'General' else vt.short_Name end as regType,"
                    + "		pu.patientName, pr.dobDisplay, gen.shortName as gender, org.manualCode as organization,"
                    + "		psd.oh_Amt_Net, prc.amount, round(isnull(psd.oh_Amt_Net, 0) - isnull(prc.amount, 0), 2) as balance,"
                    + "		pr.preparedBy, ct.cityName, sm.name as stateName, co.name, pu.zoneCode, zm.name as zone, pu.mobile, "
                    + "		case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, "
                    + "             case when isnull(pu.address2, '') = '' then '' else concat(', ', pu.address2)end) end as address "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientRegistration pr1 on pr1.regDocId = pr.regDocIdOld "
                    + "		left join voucher_Type vtr on vtr.v_Type = pr.v_Type "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join city ct on ct.cityCode = pu.cityCode "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode "
                    + "		left join zoneMast zm on zm.code = pu.zoneCode "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "		left join ("
                    + "			select  psd.regDocId, sum(psd.oh_Amt_Net) as oh_Amt_Net "
                    + "				from patientServiceDetail psd "
                    + "				where psd.site_Code = :siteCode and psd.isCancelled = 0 "
                    + "				group by psd.regDocId"
                    + "		) psd on psd.regDocId = pr.regDocId "
                    + "		left join ("
                    + "			select  prc.regDocId, sum(prc.amount) as amount"
                    + "				from patientReceipt prc "
                    + "				where prc.site_Code = :siteCode and prc.isCancelled = 0 "
                    + "				group by prc.regDocId "
                    + "		) prc on prc.regDocId = pr.regDocId "
                    + "		where pr.site_Code = :siteCode and pr.isCancelled = 0 and vt.category in ('" + pvc.LAB_REGISTRATION + "') " + condition;

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("s")) {
                sqlQuery = "select  cityName, zone, count(regDocId) as regCount, sum(isnull(oh_Amt_Net, 0)) as oh_Amt_Net,"
                        + "         sum(isnull(amount, 0)) as amount, sum(balance) as balance "
                        + "         from (" + mainQuery
                        + "         ) as tbl "
                        + "         group by cityName, zone "
                        + "         order by cityName, zone ";

            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("d")) {
                sqlQuery = mainQuery
                        + "	order by zm.name, pr.v_Date, pr.regTime, vt.short_Name, pr.v_No, pr.recId ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            if (!DmShared.xNull(filter.get("areaType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("areaType", DmShared.xNull(filter.get("areaType").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("s")) {
                reportName = "MIS_AreaWiseLabRegistrationSummary";
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("d")) {
                reportName = "MIS_AreaWiseLabRegistrationDetail";
            }

            HashMap hm = new HashMap();
            hm.put("displayBalance", DmShared.iNull(filter.get("balance").getTag()).intValue() == 1);
            jasperReport = DmServer.compileJasper(reportName);
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("s");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("d");
            listNode.setSharedKeyFieldName("Detail");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHArea(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT CODE, NAME FROM zonemast ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBalance(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("YES");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("NO");
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

}
