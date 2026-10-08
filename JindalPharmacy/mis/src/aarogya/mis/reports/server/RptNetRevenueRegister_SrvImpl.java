package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptNetRevenueRegister_Srv;
import aarogya.mis.reports.shared.RptNetRevenueRegister_Vld;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.BillingNature;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RptNetRevenueRegister_SrvImpl extends DmRSSPrivate implements RptNetRevenueRegister_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatOPD = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "','" + pvc.UHID + "','" + pvc.PAY_CLINIC + "'";
    private final String vCatIPD = "'" + pvc.IPD + "'";
    private final String vCatEmergency = "'" + pvc.EMERGENCY + "'";
    private final String vCatDAYC = "'" + pvc.DAY_CARE + "'";
    private final String vCatLAB = "'" + pvc.LAB_REGISTRATION + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String session, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        String condition = "", groupBy = "", orderBy = "", field = "";
        String sessionId = "", fileName = "RevenueReport_", COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";
        FileWriter fileWriter = null;
        double lastTotal = 0, rowWiseTotal = 0, gdPharmacyCredit = 0d, gdGrandToral = 0, gdPharmacyCash = 0d,
                gdWholesaleCashAmt = 0d, gdWholesaleCredditAmt = 0d;
        String conditionQuery = "";

        try {
            RptNetRevenueRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==========================CSV=================================================*/
            sessionId = session;

            if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("D")) {
                groupBy += "GROUP BY temp.v_Date \n ORDER BY temp.v_Date ";
                conditionQuery += "FORMAT(temp.v_Date, 'dd-MMM-yyyy') AS [Date], \n";
                fileName += filter.get("groupBy").getText();
            } else {
                groupBy += "GROUP BY temp.consultantName\n ORDER BY temp.consultantName";
                conditionQuery += "temp.consultantName      AS consultantName,\n";
                fileName += filter.get("groupBy").getText();
            }

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + "/" + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + "/" + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            File nFile = new File(fullFilePath);
            if (nFile.exists()) {
                nFile.delete();
            }
            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);
            fileWriter = new FileWriter(fullFilePath, true);

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " Where temp.v_Date >= :fromDate ";
            }

            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and temp.v_Date <= :toDate \n";
            }

            sqlQuery = "SELECT \n"
                    + conditionQuery
                    + "    SUM(temp.pharmacyCash)          AS pharmacyCash,\n"
                    + "    SUM(temp.pharmacyCredit)          AS pharmacyCredit,\n"
                    + "    SUM(temp.pharmacyReturn)           as pharmacyReturn,\n"
                    + "    SUM(temp.wholesaleCashAmount)        AS wholesaleCashAmt,\n"
                    + "    SUM(temp.wholesaleCreditAmount)         AS wholesaleCredditAmt\n"
                    + "FROM (\n"
                    + "\n"
                    + "SELECT \n"
                    + "        sg.name as consultantName,\n"
                    + "        sb.v_Date,\n"
                    + "        sb.oh_Amt_Net as pharmacyCash,\n"
                    + "        0 as pharmacyCredit,\n"
                    + "        0 as pharmacyReturn,\n"
                    + "        0 as wholesaleCashAmount,\n"
                    + "        0 as wholesaleCreditAmount\n"
                    + "    FROM salebill sb\n"
                    + "    left join subgroup sg on sg.subCode = sb.consultantCode\n"
                    + "    WHERE sb.v_Type in ('300','341','342','343')\n"
                    + "    \n"
                    + "      union all\n"
                    + "      \n"
                    + "      select \n"
                    + "      sg.name as consultantName,\n"
                    + "       sb.v_Date,\n"
                    + "        0 as pharmacyCash,\n"
                    + "        sb.oh_Amt_Net as pharmacyCredit,\n"
                    + "        0 as pharmacyReturn,\n"
                    + "        0 as wholesaleCashAmount,\n"
                    + "        0 as wholesaleCreditAmount\n"
                    + "    FROM salebill sb\n"
                    + "    left join subgroup sg on sg.subCode = sb.consultantCode\n"
                    + "    WHERE sb.v_Type = '346'\n"
                    + "    \n"
                    + "      union all \n"
                    + "\n"
                    + "   SELECT  \n"
                    + "   sg.name as consultantName,\n"
                    + "    sb.v_Date,\n"
                    + "    0 as pharmacyCash ,\n"
                    + "    0 as pharmacyCredit,\n"
                    + "    isNULL(sb.oh_Amt_Net,0) as pharmacyReturn,\n"
                    + "    0 as wholesaleCashAmount,\n"
                    + "    0 as wholesaleCreditAmount\n"
                    + "   FROM salebill sb\n"
                    + "   left join subgroup sg on sg.subCode = sb.consultantCode\n"
                    + "   LEFT JOIN voucher_Type vt ON sb.v_Type   = vt.v_Type\n"
                    + "   WHERE vt.category in ( '" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "')"
                    + " \n   "
                    + "union all \n  "
                    + "SELECT  \n"
                    + "      sg.name as consultantName,\n"
                    + "        sb.v_Date,\n"
                    + "        0 as pharmacyCash ,\n"
                    + "        0 as pharmacyCredit,\n"
                    + "        0 as pharmacyReturn,\n"
                    + "        0 as wholesaleCreditAmount,\n"
                    + "        sb.oh_Amt_Net as wholesaleCashAmount\n"
                    + "    FROM salebill sb\n"
                    + "    left join subgroup sg on sg.subCode = sb.consultantCode\n"
                    + "    LEFT JOIN voucher_Type vt ON sb.v_Type   = vt.v_Type\n"
                    + "     WHERE vt.category = '" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "'\n"
                    + "\n"
                    + "      union all\n"
                    + "      \n"
                    + "      SELECT  \n"
                    + "      sg.name as consultantName,\n"
                    + "        sb.v_Date,\n"
                    + "         0 as pharmacyCash ,\n"
                    + "        0 as pharmacyCredit,\n"
                    + "        0 as pharmacyReturn,\n"
                    + "        0 as wholesaleCashAmount,\n"
                    + "        sb.oh_Amt_Net as wholesaleCreditAmount\n"
                    + "    FROM salebill sb\n"
                    + "    left join subgroup sg on sg.subCode = sb.consultantCode\n"
                    + "    LEFT JOIN voucher_Type vt ON sb.v_Type   = vt.v_Type\n"
                    + "    WHERE vt.category = '" + pvc.PHARMACY_BILL_WHOLESALE_CREDIT + "'"
                    + ") as temp"
                    + "\n" + condition
                    + "\n" + groupBy;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getQuery(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            String FILE_HEADER = "";
            if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("D")) {
                FILE_HEADER += "Date,";
            } else {
                FILE_HEADER += "ConsultantName,";
            }
            FILE_HEADER += "Pharmacy Cash,Pharmacy Credit,Pharmacy Return,wholeSale Cash ,wholesale Credit,Total";

            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

            while (rsTemp.next()) {

                if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("D")) {
                    lineData = "\"" + DmShared.dNull(rsTemp.getString("Date")) + "\"" + COMMA_DELIMITER;
                } else {
                    lineData = "\"" + DmShared.xNull(rsTemp.getString("consultantName")) + "\"" + COMMA_DELIMITER;
                }
                lineData += "\"" + DmServer.vNull(rsTemp.getString("pharmacyCash"), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("pharmacyCredit"), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("pharmacyReturn"), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("wholesaleCashAmt"), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("wholesaleCredditAmt"), "0.00") + "\"" + COMMA_DELIMITER;

                rowWiseTotal = DmShared.vNull(DmServer.vNull(rsTemp.getString("pharmacyCash"), "0.00")).doubleValue()
                        + DmShared.vNull(DmServer.vNull(rsTemp.getString("pharmacyCredit"), "0.00")).doubleValue()
                        - DmShared.vNull(DmServer.vNull(rsTemp.getString("pharmacyReturn"), "0.00")).doubleValue()
                        + DmShared.vNull(DmServer.vNull(rsTemp.getString("wholesaleCashAmt"), "0.00")).doubleValue()
                        + DmShared.vNull(DmServer.vNull(rsTemp.getString("wholesaleCredditAmt"), "0.00")).doubleValue();
                        

                gdPharmacyCash += DmShared.vNull(DmServer.vNull(rsTemp.getString("pharmacyCash"), "0.00")).doubleValue();
                gdPharmacyCredit += DmShared.vNull(DmServer.vNull(rsTemp.getString("pharmacyCredit"), "0.00")).doubleValue();
                gdWholesaleCashAmt += DmShared.vNull(DmServer.vNull(rsTemp.getString("wholesaleCashAmt"), "0.00")).doubleValue();
                gdWholesaleCredditAmt += DmShared.vNull(DmServer.vNull(rsTemp.getString("wholesaleCredditAmt"), "0.00")).doubleValue();
                gdGrandToral += DmShared.vNull(rowWiseTotal).doubleValue();

                lastTotal += DmShared.vNull(DmServer.vNull(rowWiseTotal, "0.00"));
                lineData += "\"" + DmServer.vNull(rowWiseTotal, "0.00") + "\"" + COMMA_DELIMITER;
                fileWriter.append(lineData);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            rsTemp.close();
            psTemp.close();

            //append grand total
            lineData = "\"" + "Total" + "\"" + COMMA_DELIMITER;
            lineData += "\"" + DmServer.vNull(gdPharmacyCash, "0.00") + "\"" + COMMA_DELIMITER;
            lineData += "\"" + DmServer.vNull(gdPharmacyCredit, "0.00") + "\"" + COMMA_DELIMITER;
            lineData += "\"" + DmServer.vNull(gdWholesaleCashAmt, "0.00") + "\"" + COMMA_DELIMITER;
            lineData += "\"" + DmServer.vNull(gdWholesaleCredditAmt, "0.00") + "\"" + COMMA_DELIMITER;
            lineData += "\"" + DmServer.vNull(gdGrandToral, "0.00") + "\"" + COMMA_DELIMITER;

//            for (String head : grandTotal.keySet()) {
//                lineData += "\"" + DmServer.vNull(grandTotal.get(head), "0.00") + "\"" + COMMA_DELIMITER;
//            }
//            lineData += "\"" + DmServer.vNull(lastTotal, "0.00") + "\"" + COMMA_DELIMITER;
            fileWriter.append(lineData);
            fileWriter.append(NEW_LINE_SEPARATOR);

            fileWriter.flush();
            fileWriter.close();

            rstTemp.setData(keyField);
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

    public DmPreparedStatement getQuery(HashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
        } catch (Exception e) {
        }

        return psTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroupBy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Consultant Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Date Wise");
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
