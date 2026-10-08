package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptPackageWiseInvestigationReport_Srv;
import aarogya.mis.standardReports.shared.RptPackageWiseInvestigationReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptPackageWiseInvestigationReport_SrvImpl extends DmRSSPrivate implements RptPackageWiseInvestigationReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
     private final String vCat = "'" + pvc.LAB_REGISTRATION + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", reportName = "", condition = "", mainQuery;

        try {
            RptPackageWiseInvestigationReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            condition = " and (pr.v_Date between :fromDate and :toDate) ";

            if (!DmShared.xNull(filter.get("package").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND psd.packageCode IN(" + DmServer.getParamList(":package", filter.get("package").getTag()) + " )";
            }

            mainQuery = "select  pr.v_Date, pr.recId as labNo, prOld.recId as refNo, pu.recId as uhid, pu.mobile, psd.packageName, gen.shortName as gender, pr.dobDisplay,"
                    + "		case when isnull(pu.patientInitial, '') = '' then pu.patientName else concat(pu.patientInitial, ' ', pu.patientName) end as patient,pu.guardianName,"
                    + "		case when isnull(ref.initial, '') = '' then ref.name else concat(ref.initial, ' ', ref.name) end as referredBy, psd.pkgAmount,"
                    + "		psd.packageCode"
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type"
                    + "		left join ("
                    + "			select  psd.regDocId, max(pm.name) as packageName, max(pd.packageCode) as packageCode, round(sum(psd.oh_Amt_Net), 2) as pkgAmount "
                    + "					from patientServiceDetail psd "
                    + "					left join packageDetail pd on pd.code = psd.packageCode "
                    + "					left join packageMast pm on pm.code = pd.packageCode "
                    + "					where psd.site_Code = 1 and psd.isCancelled = 0 and psd.packageCode is not null"
                    + "					group by psd.regDocId, psd.packageCode"
                    + "		) psd on psd.regDocId = pr.regDocId"
                    + "		left join patientRegistration prOld on prOld.regDocId = pr.regDocIdOld "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join subGroup ref on ref.subCode = pr.referredBy"
                    + "         where psd.packageCode is not null and vt.category in ("+ vCat +") and pr.isCancelled = 0  "
                    + "         and pr.site_Code = :siteCode" + condition;

            if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("s")) {
                sqlQuery = " select  max(tbl.packageName) as packageName, count(tbl.labNo) as labCount, sum(tbl.pkgAmount) as pkgAmount "
                        + " from (" + mainQuery + " ) as tbl"
                        + " group by tbl.packageCode"
                        + " order by max(tbl.packageName) ";
            } else if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("d")) {
                sqlQuery = mainQuery + " order by  psd.packageName, pr.v_Date, pr.recId ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("package").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("package", DmShared.xNull(filter.get("package").getTag()), true);
            }

            rsTemp = psTemp.executeQuery();
            if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("s")) {
                reportName = "MIS_PackageWiseInvestigationReportSummary";
            } else if (DmShared.xNull(filter.get("reportView").getTag()).trim().equalsIgnoreCase("d")) {
                reportName = "MIS_PackageWiseInvestigationReportDetail";
            }

            HashMap hm = new HashMap();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportView(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Detail");
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPackage(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select name,code from packageMast where nature = 2");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
