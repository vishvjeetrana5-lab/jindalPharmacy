package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptControlAccountsDetails_Srv;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
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
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptControlAccountsDetails_SrvImpl extends DmRSSPrivate implements RptControlAccountsDetails_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.IPD_BILL + "','" + pvc.IPD_BILL_CANCELL + "'";

    @Override
    //this method returns the url of report.
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = "", reportViewType = "";

        try {
            // RptConsultationServiceDues_Vld.validateInput(filter);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "	\n"
                    + "	\n"
                    + "	select pu.patientName , pr.recId as ipdNo,  prc.recId as rectNo , '' as billNo , "
                    + "prc.amount as amt  , format(prc.v_date,'dd-MMM-yyyy') as v_Date\n"
                    + "				\n"
                    + "				--select  pu.patientName , pr.recId as IPDNo, format(prc.v_date,'dd-MMM-yyyy') as receiptDate, prc.recId as receiptNo,prc.amount \n"
                    + "				from  patientReceipt prc \n"
                    + "				left join voucher_Type vt on vt.v_Type=prc.v_Type\n"
                    + "				left join patientRegistration as pr on pr.regDocId=prc.regDocId\n"
                    + "				left join patientUHID as pu on pu.docid=pr.uhid\n"
                    + "				where \n"
                    + "				(prc.v_date between :fromDate and :toDate ) \n "
                    + "				and \n"
                    + "				vt.category in ('IPDRC','IPDRF') and prc.site_Code= :site_Code and prc.isCancelled=0 \n"
                    + "				and  prc.billDocid is  null\n"
                    + "\n"
                    + "				order by prc.v_Date desc";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("ControlAccountsDetailsReport"); //write down report file name.
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
    public DmResultPageing<List<KeyFieldExtended>> gettxtHControlAccount(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select sg.subCode as code, sg.name from subGroup as sg where sg.name in ('IPD Control A/c')");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
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

}
