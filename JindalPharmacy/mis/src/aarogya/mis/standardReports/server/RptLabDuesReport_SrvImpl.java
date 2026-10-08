package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptLabDuesReport_Srv;
import aarogya.mis.standardReports.shared.RptLabDuesReport_Vld;
import aarogya.mis.standardReports.shared.TxtHUHIDNo_Mdl;
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

public class RptLabDuesReport_SrvImpl extends DmRSSPrivate implements RptLabDuesReport_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatLab = "'" + new ProjectVoucherCategory().LAB_REGISTRATION + "'";
    private final String vCatIPD = "'" + new ProjectVoucherCategory().IPD + "','" + new ProjectVoucherCategory().EMERGENCY + "'";
    private final String vCatOPD = "'" + new ProjectVoucherCategory().OPD + "','" + new ProjectVoucherCategory().OPD_SERVICE + "','" + new ProjectVoucherCategory().EMERGENCY_OPD + "','" + new ProjectVoucherCategory().PAY_CLINIC + "'";
    private final String vCatDAYC = "'" + new ProjectVoucherCategory().DAY_CARE + "'";
    private final String vCat = "'" + pvc.OPD_SERVICE_CANCELLATION + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, String widgetName, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, condition = "";

        try {
            RptLabDuesReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (pr.v_Date between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("subType").getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("subType").getTag()).equalsIgnoreCase("A")) {
                } else {
                    condition += " AND vtr.category IN (" + DmServer.getParamList(":subType", filter.get("subType").getTag()) + " )";
                }
            }

            if (!DmShared.xNull(filter.get("uhid").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pr.uhid in (" + DmServer.getParamList(":uhid", filter.get("uhid").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("userName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pr.preparedBy IN (" + DmServer.getParamList(":preparedBy", filter.get("userName").getText()) + " ) ";
            }

            sqlQuery = "select  pr.regDocId, pr.recId as regNo, pr.v_Date, pr1.recId as refNo, pu.recId as uhid, "
                    + "		case when pr.regDocIdOld is null then 'General' else vtr.short_Name end as regType,"
                    + "		pu.patientName, pr.dobDisplay, gen.shortName as gender, org.manualCode as organization,"
                    + "		psd.oh_Amt_Net, prc.amount, round(isnull(psd.oh_Amt_Net, 0) - isnull(prc.amount, 0), 2) as balance,"
                    + "		pr.preparedBy, ct.cityName, sm.name as stateName, co.name as country, pu.mobile,"
                    + "		case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, "
                    + "             case when isnull(pu.address2, '') = '' then '' else concat(', ', pu.address2)end) end as address "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientRegistration pr1 on pr1.regDocId = pr.regDocIdOld "
                    + "		left join voucher_Type vtr on vtr.v_Type = pr1.v_Type "
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
                    + "		where pr.site_Code = :siteCode and pr.isCancelled = 0 and vt.category in ('" + pvc.LAB_REGISTRATION + "') "
                    + "		 and round(isnull(psd.oh_Amt_Net, 0) - isnull(prc.amount, 0), 2) <> 0 " + condition
                    + "		order by pr.v_Date, vt.short_Name, pr.v_No, pr.recId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            if (!DmShared.xNull(filter.get("subType").getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("subType").getTag()).equalsIgnoreCase("A")) {
                } else {
                    psTemp.setStringList("subType", filter.get("subType").getTag());
                }
            }

            if (!DmShared.xNull(filter.get("uhid").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("uhid", filter.get("uhid").getTag());
            }
            if (!DmShared.xNull(filter.get("userName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("preparedBy", filter.get("userName").getText());
            }
            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_LabDuesReport");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatDAYC);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatIPD);
            listNode.setSharedKeyFieldName("IPD/Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(vCatOPD);
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("G");
            listNode.setSharedKeyFieldName("General");
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

            sqlWithParam.setSQL("select  pr.uhid, pu.docId, pu.recId, pu.patientName, pr.guardianName, pr.dobDisplay, gen.name as gender, pu.v_Date, "
                    + "		pu.v_No, vt.short_Name, org.name as organization"
                    + "		from patientUHID pu "
                    + "		left join voucher_Type vt on vt.v_Type = pu.v_Type "
                    + "		left join ("
                    + "			select  pr.uhid, max(pr.organizationCode) as organizationCode, max(pr.guardianName) as guardianName,"
                    + "				max(pr.dobDisplay) as dobDisplay"
                    + "				from patientRegistration pr "
                    + "				left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "				where vt.category in ('" + pvc.LAB_REGISTRATION + "') and pr.site_Code = :siteCode and pr.isCancelled = 0 "
                    + "				group by pr.uhid "
                    + "		) pr on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pr.uhid "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "		where pu.isCancelled = 0 and isnull(pu.approvedBy, '') = '' and pu.isExpired = 0 "
                    + "			and pr.uhid is not null");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUserName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL(" SELECT code, user_Name AS Name FROM usermast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
