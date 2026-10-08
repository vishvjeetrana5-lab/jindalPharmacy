package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptLabCancellationReport_Srv;
import aarogya.mis.standardReports.shared.RptLabCancellationReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
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

public class RptLabCancellationReport_SrvImpl extends DmRSSPrivate implements RptLabCancellationReport_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.LAB_CANCELATION + "'";

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
            RptLabCancellationReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (psd.v_Date  between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("test").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND psd.serviceCode IN (" + DmServer.getParamList(":test", filter.get("test").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("testCategory").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND psm.categoryCode IN (" + DmServer.getParamList(":testCategory", filter.get("testCategory").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("referred").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pr.referredBy in (" + DmServer.getParamList(":referred", filter.get("referred").getTag()) + " )";
            }

            sqlQuery = " Select	psd.consultantCode as groupCode,  concat(con.initial, ' ', con.name) as groupName, ps.docId, ps.recId, ps.v_Date, vt.description,  "
                    + "		psd.v_Type, pr.recId as regNo, pu.recId as uhid, psd.discountReason, psd.oh_Amt_Net,  "
                    + "		case when isnull(pu.patientInitial,'') = '' then pu.patientName else concat(pu.patientInitial,' ', pu.patientName) end as patientName,  "
                    + "		case when isnull(dis.initial,'') = '' then dis.name else concat(dis.initial,' ', dis.name) end  as discountBy,  "
                    + "		case when org.manualCode = 'CP' then '' else org.manualCode end as orgShortName, psd.serviceCode,  "
                    + "		concat(con.initial, ' ', con.name) consultant, ps.preparedBy, psm.name as serviceName, psd.grossAmount, psd.discount,  "
                    + "		pr.dobDisplay, psd.rate, psd.quantity, gen.shortName gender, psd.consultantCode as consultantCode,psd.remark  "
                    + "		from patientService ps  "
                    + "		left join voucher_Type vt on vt.v_Type = ps.v_Type  "
                    + "		left join patientServiceDetail psd on psd.docId = ps.docId  "
                    + "		left join patientRegistration pr on pr.regDocId = psd.regDocId  "
                    + "		left join patientUHID pu on pu.docId = pr.uhid  "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pr.uhid  "
                    + "		left join subGroup org on org.subCode = pr.organizationCode  "
                    + "		left join subGroup dis on dis.subCode = psd.discountGivenBy  "
                    + "		left join subGroup con on con.subCode = pr.referredBy  "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode  "
                    + "		left join patientServiceCatMast pscm on pscm.code = psm.categoryCode  "
                    + "		left join hl7mast gen on gen.code = pu2.genderCode  "
                    + "		where vt.category in (" + vCat + ") and psd.isCancelled = 0 and ps.site_Code = :siteCode   " + condition
                    + "		order by groupName, ps.v_Date, vt.description, ps.v_No, ps.recId, psm.name ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("test").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("test", DmShared.xNull(filter.get("test").getTag()));
            }
            if (!DmShared.xNull(filter.get("testCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("testCategory", DmShared.xNull(filter.get("testCategory").getTag()));
            }
            if (!DmShared.xNull(filter.get("referred").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("referred", DmShared.xNull(filter.get("referred").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_LabCancellationReport");

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select code, name FROM patientservicecatmast where nature = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTest(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        String sqlQuery = "";
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select 	pcm.CODE,pcm.NAME   "
                    + " from patientServiceMast pcm   "
                    + " left join patientServiceCatMast pscm on pcm.categoryCode = pscm.code   "
                    + " where pcm.isActive = 1  and pcm.serviceNature in (" + ServiceNature.PATHOLOGY_TEST.getId() + "," + ServiceNature.RADIOLOGY_TEST.getId() + ") and pcm.serviceRoutine = 0 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subcode as code,sg.name "
                    + " FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " WHERE CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' AND sgn.manualCode in ('CN') ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
