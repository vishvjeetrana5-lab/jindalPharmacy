package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptOPDServiceCancellation_Vld;
import dataman.dmBase.self.shared.*;
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
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.mis.standardReports.shared.RptOPDServiceCancellation_Srv;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;

public class RptOPDServiceCancellation_SrvImpl extends DmRSSPrivate implements RptOPDServiceCancellation_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.OPD_SERVICE_CANCELLATION + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, condition = "";

        try {
            RptOPDServiceCancellation_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (isnull(psd.v_Date, :toDate) between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("service").getTag()).trim().equalsIgnoreCase("")) {
                condition += "and psd.serviceCode in(" + DmServer.getParamList(":service", filter.get("service").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                condition += "and psd.consultantCode in(" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + ")";
            }

            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("C")) {
                condition += "and psm.shortName = 'CF' ";
            }

            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("O")) {
                condition += "and psm.shortName <> 'CF' ";
            }

            sqlQuery = "select	psd.consultantCode as groupCode,  "
                    + "concat(con.initial, ' ', con.name) as groupName, ps.docId, ps.recId, ps.v_Date as cancelDate, ps.v_Time as cancelTime, vt.description, "
                    + " psd.v_Type, pr.recId as regNo, pu.recId as uhid, psd.discountReason, psd.oh_Amt_Net, "
                    + "  case when isnull(pu.patientInitial,'') = '' then pu.patientName else concat(pu.patientInitial,' ', pu.patientName) end as patientName,         "
                    + "  case when isnull(dis.initial,'') = '' then dis.name else concat(dis.initial,' ', dis.name) end  as discountBy,  "
                    + "  case when org.manualCode = 'CP' then '' else org.manualCode end as orgShortName, psd.serviceCode,  "
                    + "  concat(con.initial, ' ', con.name) consultant, ps.preparedBy, psm.name as serviceName, psd.grossAmount, psd.discount,  "
                    + "  pr.dobDisplay, psd.rate, psd.quantity, gen.shortName gender, psd.consultantCode as consultantCode,psd.remark "
                    + ", psdMain.v_Date , psdMain.v_Time  "
                    + "from patientService ps	"
                    + "left join voucher_Type vt on vt.v_Type = ps.v_Type "
                    + "left join patientServiceDetail psd on psd.docId = ps.docId "
                    + "left join patientRegistration pr on pr.regDocId = psd.regDocId 	"
                    + "left join patientUHID pu on pu.docId = pr.uhid 	"
                    + "left join patientUHID2 pu2 on pu2.uhid = pr.uhid "
                    + "left join subGroup org on org.subCode = pr.organizationCode "
                    + "left join subGroup dis on dis.subCode = psd.discountGivenBy "
                    + "left join subGroup con on con.subCode = psd.consultantCode  "
                    + "left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "left join hl7mast gen on gen.code = pu2.genderCode		"
                    + "left join patientServiceDetail psdMain on psd.serviceDocId = psdMain.docid and psd.serviceV_SNo = psdMain.v_SNo "
                    + "where vt.category in (" + vCat + ") and psd.isCancelled = 0  "
                    + " and ps.site_Code = :siteCode " + condition
                    + " order by ps.v_Date, vt.description, ps.v_No, ps.recId  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("service").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("service", DmShared.xNull(filter.get("service").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()), true);
            }

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_OPDServiceCancellationReport");

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
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Consultation");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("O");
            listNode.setSharedKeyFieldName("Other Service");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	cm.subCode as code, sg.name "
                    + "		from consultantMast as cm  "
                    + "		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
                    + "		left join subGroup as sg on cm.subCode = sg.subCode  "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code  "
                    + "		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
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
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select  psm.code, psm.name "
                    + "		from patientServiceMast psm "
                    + "		left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "		where psm.isActive = 1 and pssl.site_Code = 1 "
                    + "             and psm.serviceNature in (" + ServiceNature.OPERATION.getId() + "," + ServiceNature.SERVICE.getId() + ") ");

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
