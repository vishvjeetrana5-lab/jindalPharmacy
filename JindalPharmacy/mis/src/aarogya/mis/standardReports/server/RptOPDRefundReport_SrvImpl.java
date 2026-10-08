package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptOPDRefundReport_Srv;
import aarogya.mis.standardReports.shared.RptOPDRefundReport_Vld;
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
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptOPDRefundReport_SrvImpl extends DmRSSPrivate implements RptOPDRefundReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCat = "'" + pvc.OPD_REFUND_RECEIPT + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        String sqlQuery, condition;
        try {
            RptOPDRefundReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " and (prc.v_Date between  :fromDate  and  :toDate) ";

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pr.consultantCode in(" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + " )";
            }

            sqlQuery = " select  prc.docId, prc.recId as recNo, prc.v_Date, cl.amtDr, ctm.name, pu.patientName, gen.shortName as gender,  "
                    + "		pr.dobDisplay, pr.recId as regNo, prc.preparedBy, prc.remark, pu.recId as uhid,  "
                    + "		case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end as consultant "
                    + "		from patientReceipt prc  "
                    + "		left join voucher_Type vt on vt.v_Type = prc.v_Type  "
                    + "		left join patientRegistration pr on pr.regDocId = prc.regDocId  "
                    + "		left join patientUHID pu on pu.docId = pr.uhid  "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId  "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode  "
                    + "		left join cardLedger cl on cl.docId = prc.docId  "
                    + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode  "
                    + "		left join subGroup con on con.subCode = pr.consultantCode  "
                    + "		where vt.category in (  " + vrCat + " ) and prc.site_Code = :siteCode  "
                    + "			and prc.isCancelled = 0   " + condition
                    + "		order by prc.v_Date, vt.short_Name, prc.v_No, prc.recId, cl.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()), true);
            }

            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("MIS_OPDRefundReport");

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

}
