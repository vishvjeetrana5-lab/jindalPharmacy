package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptDischargePatientDues_Srv;
import aarogya.mis.standardReports.shared.RptDischargePatientDues_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
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

public class RptDischargePatientDues_SrvImpl extends DmRSSPrivate implements RptDischargePatientDues_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCatBill = "'" + pvc.IPD_BILL + "'";
    private final String vrCatReg = "'" + pvc.IPD + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ";
        String condition1 = " ", condition2 = " ";

        try {
            RptDischargePatientDues_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition1 += " and (pd.dischargeDate between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("payRecUpTo").getText()).trim().equalsIgnoreCase("")) {
                condition2 += " and prc.v_Date <= :v_Date ";
            }
            if (DmShared.iNull(filter.get("reportFor").getTag()).intValue() != 2) {
                condition1 += " and om.isTPA  = :isTPA ";
            }

            sqlQuery = "select  pr.v_Date, pr.recId as regNo, vt.description, pu.patientName, pu.recId as uhidNo, org.manualCode, "
                    + "		pd.dischargeDate, pd.preparedBy, sb.billDate, sb.billNo, sb.billAmount, prc.adjustedAmount,"
                    + "		round(isnull(sb.billAmount, 0) - isnull(prc.adjustedAmount, 0),2) as balance "
                    + "		from patientDischarge pd "
                    + "		left join patientRegistration pr on pr.regDocId = pd.regDocId "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "         left join organizationMast as om on om.subCode=pr.organizationCode"
                    + "		left join ( "
                    + "                   select  sb.v_Date as billDate, sb.recId as billNo, sb.regDocId, sb.oh_Amt_Net as billAmount, sb.v_No"
                    + "                           from saleBill sb "
                    + "                           left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "                           where isnull(sb.approvedBy, '') <> '' and vt.category in (" + vrCatBill + ") "
                    + "					and sb.site_Code = :siteCode and sb.regDocId <> sb.docId "
                    + "		) sb on sb.regDocId = pd.regDocId "
                    + "		left join ( "
                    + "                     select  prc.regDocId, sum(prc.amount) as adjustedAmount "
                    + "                             from patientReceipt prc "
                    + "                             where prc.isCancelled = 0 and prc.site_Code = :siteCode " + condition2
                    + "                             group by prc.regDocId "
                    + "		) prc on prc.regDocId = pd.regDocId "
                    + "		where pr.site_Code = :siteCode and vt.category in (" + vrCatReg + ") "
                    + "             and round(isnull(sb.billAmount, 0) - isnull(prc.adjustedAmount, 0),2) > 0 " + condition1
                    + "		order by pd.dischargeDate, vt.short_Name, sb.v_No, sb.billNo, pr.v_Date, pr.v_No, pr.recId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            if (!DmShared.xNull(filter.get("payRecUpTo").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(filter.get("payRecUpTo").getText()), true);
            }
            if (DmShared.iNull(filter.get("reportFor").getTag()).intValue() != 2) {
                psTemp.setString("isTPA", DmShared.xNull(filter.get("reportFor").getTag()));
            }

            rsTemp = psTemp.executeQuery();
            HashMap hm = new HashMap();

            jasperReport = DmServer.compileJasper("MIS_DischargePatientDuesReport");
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportForType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Cash");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Organisation");
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

}
