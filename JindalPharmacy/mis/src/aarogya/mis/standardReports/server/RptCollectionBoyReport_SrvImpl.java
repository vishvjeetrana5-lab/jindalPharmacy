package aarogya.mis.standardReports.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.mis.standardReports.shared.RptCollectionBoyReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import aarogya.mis.standardReports.shared.TxtHUHIDNo_Mdl;
import aarogya.mis.standardReports.shared.RptCollectionBoyReport_Srv;
import java.util.HashMap;

public class RptCollectionBoyReport_SrvImpl extends DmRSSPrivate implements RptCollectionBoyReport_Srv {

    private final String vCat = "'" + new ProjectVoucherCategory().LAB_REGISTRATION + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = " ", mainQuery, reportName = "";

        try {
            RptCollectionBoyReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (pr.v_Date between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("uhid").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pu.recId in (" + DmServer.getParamList(":uhid", filter.get("uhid").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("collectionBoy").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and cb.subCode in (" + DmServer.getParamList(":collectionBoy", filter.get("collectionBoy").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("area").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and zm.code in (" + DmServer.getParamList(":area", filter.get("area").getTag()) + " )";
            }

            mainQuery = " select  pr.regDocId, pr.recId as regNo, pr.dobDisplay, pu.recId as uhidNo, pu.patientName,pr.v_Date,ref.name as referConsultant, "
                    + "		psd.oh_Amt_Net, ser.colectionAmount, isnull(cb.name,'') as collectionBoy,isnull(zm.name,'') as area"
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "		left join subGroup cb on cb.subCode = psd.collectionBoyCode "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join subGroup ref on ref.subCode = pr.referredBy "
                    + "         left join zoneMast zm on zm.code = pu.zoneCode "
                    + "		left join ("
                    + "			select  psd.regDocId, sum(psd.oh_Amt_Net) as colectionAmount"
                    + "					from patientServiceDetail psd"
                    + "					left join voucher_Type vt on vt.v_Type = psd.v_Type "
                    + "					where psd.serviceCode = 100010005 and vt.category in ('LAB', 'LABCN')"
                    + "						and psd.site_Code = 1 "
                    + "					group by psd.regDocId"
                    + "		) ser on ser.regDocId = pr.regDocId"
                    + "		where vt.category in ('LAB') and pr.isCancelled = 0 and pr.site_Code = 1 "
                    + "		and psd.collectionBoyCode is not null " + condition;

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("s")) {
                sqlQuery = "select max( t.collectionBoy) as collectionBoy, count(t.regDocId) as regCount,max(t.oh_Amt_Net) as totalAmount "
                        + "	from (" + mainQuery + "     ) as t "
                        + "     group by t.collectionBoy ";

            } else {
                sqlQuery = mainQuery + " order by pr.v_Date, pr.regTime, vt.short_Name, pr.v_No, pr.recId ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()));

            if (!DmShared.xNull(filter.get("uhid").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("uhid", DmShared.xNull(filter.get("uhid").getTag()));
            }

            if (!DmShared.xNull(filter.get("collectionBoy").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("collectionBoy", DmShared.xNull(filter.get("collectionBoy").getTag()));
            }

            if (!DmShared.xNull(filter.get("area").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("area", DmShared.xNull(filter.get("area").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("s")) {
                reportName = "Diagnostic_LabCollectionBoySummary";
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("d")) {
                reportName = "Diagnostic_LabCollectionBoyDetail";
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
    public DmResultPageing<List<KeyFieldExtended>> getReportType(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<TxtHUHIDNo_Mdl>> getUHIDNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHUHIDNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHUHIDNo_Mdl> lstTemp = new ArrayList<>();
        TxtHUHIDNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select pu.docId, pu.recId, pu.patientName, pu.guardianName, pu.dobDisplay, gen.name as gender, "
                    + "		case when isnull(org.manualCode, 'CP') = 'CP' then '' else org.manualCode end as organization, "
                    + "		pu.v_Date, vt.short_Name, pu.v_No "
                    + "	from patientUHID pu "
                    + "	left join voucher_Type vt on vt.v_Type = pu.v_Type "
                    + "	left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                    + "	left join subGroup org on org.subCode = pu.organizationCode "
                    + "	left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "	where pu.isExpired = 0 and isnull(pu.approvedBy, '') <> ''");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHUHIDNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                listNode.setDobDisplay(DmShared.xNull(rsTemp.getString("dobDisplay")));
                listNode.setGender(DmShared.xNull(rsTemp.getString("gender")));
                listNode.setOrganization(DmShared.xNull(rsTemp.getString("organization")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getCollectionBoy(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select sg.subCode,sg.name  from subgroup as sg where nature = 18");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getArea(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select code,name from zoneMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

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
