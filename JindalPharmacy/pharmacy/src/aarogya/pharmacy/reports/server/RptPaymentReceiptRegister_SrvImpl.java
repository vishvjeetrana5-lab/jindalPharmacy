package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptPaymentReceiptRegister_Srv;
import aarogya.pharmacy.reports.shared.RptPaymentReceiptRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtPartyName_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
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

public class RptPaymentReceiptRegister_SrvImpl extends DmRSSPrivate implements RptPaymentReceiptRegister_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String strSQL;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    public void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(10, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "To Date is mandatory.");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(80, "From Date should be less than or equal to To Date.");
            }
        }

    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "";

        try {
            RptPaymentReceiptRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.v_Date <= :toDate ";
            }

            if (!DmShared.xNull(filter.get("partyName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND cst.subCode in (" + DmServer.getParamList(":partyName", filter.get("partyName").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND   sb.site_Code IN (" + DmServer.getParamList(":site", filter.get("site").getTag()) + " )";
            } else {
                condition += " AND   sb.site_Code IN (" + DmServer.getParamList(":site", DmServer.getSsnSiteList(this)) + " )";

            }

            sqlQuery = "SELECT \n"
                    + "   sb.docId \n"
                    + " , sb.recId AS billNo \n"
                    + " , sb.v_Date AS BillDate \n"
                    + " , sb.v_Type \n"
                    + " , cst.name AS customer \n"
                    + " , pp.paymentResponceStatus AS onlinepaymentStatus \n"
                    + " , CASE  WHEN pp.paymentResponceStatus = 'SUCCESS' THEN pp.gtwAckNo \n"
                    + "    ELSE NULL \n"
                    + "   END AS onlinepaymentId \n"
                    + "  , ISNULL(sb.oh_Amt_Net ,0) AS billAmt\n"
                    + "  ,  prc.receivedAmt AS receiveAmt \n"
                    + "  ,  ISNULL(sb.oh_Amt_Net, 0) - ISNULL(prc.receivedAmt, 0) AS balance\n"
                    + " FROM saleBill sb \n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type\n"
                    + "LEFT JOIN subGroup cst ON cst.subCode = sb.partySubCode\n"
                    + " LEFT JOIN cardLedger  cl ON cl.docId = sb.docId and isnull(cl.posPaymentDocId,0)<>0 \n"
                    + " LEFT JOIN posPayment  pp ON pp.docId = cl.posPaymentDocId \n"
                    + "LEFT JOIN (\n"
                    + "    SELECT \n"
                    + "        prc.sbDocId, \n"
                    + "        SUM(prc.amount) AS receivedAmt\n"
                    + "    FROM patientReceipt prc\n"
                    + "    GROUP BY prc.sbDocId\n"
                    + ") prc ON prc.sbDocId = sb.docId\n"
                    + "WHERE vt.category = :category  \n"
                    + "  AND sb.partySubCode <> '' \n"
                    + "  AND ISNULL(prc.receivedAmt,0) >0 \n"
                    + condition
                    + "      ORDER BY sb.v_Date, sb.v_No ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("category", pvc.PHARMACY_BILL_WHOLESALE_CASH);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("Pharmacy_PaymentReceiptRegister");
            jasperPrint = DmServer.fillJasper(this, "Payment Receipt Register", jasperReport, null, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);

            cnTemp.close();
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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("partyName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("partyName", DmShared.xNull(filter.get("partyName").getTag()));
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site", DmShared.xNull(filter.get("site").getTag()));
            } else {
                psTemp.setStringList("site", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<TxtPartyName_Mdl>> getTxtHPartyName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtPartyName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtPartyName_Mdl> lstTemp = new ArrayList<TxtPartyName_Mdl>();
        TxtPartyName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            String sqlQuery = " SELECT \n"
                    + "   sg.subCode AS code\n"
                    + " , sg.name as partyName \n"
                    + " , sg.manualCode \n"
                    + " , c.cityName \n"
                    + " FROM subGroup sg \n"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode \n"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature \n"
                    + " WHERE sgn.manualCode = 'CT' ";
//                    + " WHERE sgn.code IN ( "
//                    + "             SELECT DISTINCT acf.Nature "
//                    + "             FROM AcFilteration acf "
//                    + "             LEFT JOIN voucher_Type vt ON vt.v_Type = acf.v_Type "
//                    + "             WHERE vt.category IN ('" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "') "
//                    + "             ) ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtPartyName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("partyName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
