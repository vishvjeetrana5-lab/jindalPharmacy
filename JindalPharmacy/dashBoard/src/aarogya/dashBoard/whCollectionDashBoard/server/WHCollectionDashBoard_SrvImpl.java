package aarogya.dashBoard.whCollectionDashBoard.server;

import aarogya.dashBoard.whCollectionDashBoard.shared.CardDetail_Mdl;
import aarogya.dashBoard.whCollectionDashBoard.shared.SearchValue_Mdl;
import aarogya.dashBoard.whCollectionDashBoard.shared.WHCollectionDashBoardCard_Srv;
import aarogya.dashBoard.whCollectionDashBoard.shared.WHCollectionDashBoard_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmDashBoard;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WHCollectionDashBoard_SrvImpl extends DmRSSPrivate implements WHCollectionDashBoardCard_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<WHCollectionDashBoard_Mdl>> getData(String daysCount, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue) {
        DmResultPageing<List<WHCollectionDashBoard_Mdl>> rstTemp = new DmResultPageing<>();
        List<WHCollectionDashBoard_Mdl> lstTemp = new ArrayList<WHCollectionDashBoard_Mdl>();
        WHCollectionDashBoard_Mdl listNode;
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sql = "", startDate = "", currentDateStr = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(daysCount).equalsIgnoreCase("")) {

                currentDateStr = DmServer.getServerDateTime()[0];

                // Parse the date and number of days
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
                LocalDate currentDate = LocalDate.parse(currentDateStr, formatter);
                int daysToSubtract = DmShared.iNull(daysCount).intValue();

                // Subtract days
                LocalDate resultDate = currentDate.minusDays(daysToSubtract);

                // Optional: format result
                startDate = DmShared.dNull(resultDate.format(formatter));
            }

            sql = "SELECT \n"
                    + "    q1.SupplierCode,\n"
                    + "    q1.SupplierName,\n"
                    + "    q1.departmentCode,\n"
                    + "    q1.departmentName,\n"
                    + "    q1.totalBill,\n"
                    + "    q1.saleAmount,\n"
                    + "    q1.totalSettlementAmt,\n"
                    + "    q2.pendingToAdjust\n"
                    + "FROM (\n"
                    + "    SELECT \n"
                    + "        sb.partySubCode AS SupplierCode, \n"
                    + "        sg.name AS SupplierName,\n"
                    + "        MAX(sb.departmentCode) AS departmentCode,\n"
                    + "        MAX(sgd.name) AS departmentName,\n"
                    + "        COUNT(*) AS totalBill,\n"
                    + "        SUM(sb.oh_amt_net) AS saleAmount,\n"
                    + "        SUM(ABS(la.adjustment)) AS totalSettlementAmt,\n"
                    + "        0 AS pendingToSettlementAmt\n"
                    + "    FROM saleBill sb\n"
                    + "    LEFT JOIN voucher_Type vt ON sb.v_Type = vt.v_type \n"
                    + "    LEFT JOIN subGroup sg ON sg.subCode = sb.partySubCode\n"
                    + "    LEFT JOIN subGroup sgd ON sgd.subCode = sb.departmentCode\n"
                    + "    LEFT JOIN (\n"
                    + "         SELECT adj_DocId, SUM(adjustment) AS adjustment\n"
                    + "         FROM ledgerAdj \n"
                    + "         GROUP BY adj_DocId\n"
                    + "    ) la ON la.adj_DocId = sb.docId \n"
                    + "    WHERE sb.partySubCode IS NOT NULL  \n"
                    + "  and  sb.v_Date >='" + startDate + "' and sb.v_Date <= '" + currentDateStr + " '\n"
                    + "   AND vt.category IN ('" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "','" + pvc.PHARMACY_BILL_WHOLESALE_CREDIT + "')\n"
                    + "    GROUP BY sb.partySubCode, sg.name\n"
                    + ") q1\n"
                    + "LEFT JOIN (\n"
                    + "    SELECT \n"
                    + "       MAX(sg.subCode) AS subCode,\n"
                    + "       SUM(lg.amtDr) - ABS(SUM(ISNULL(v1.adjustment,0))) AS pendingToAdjust\n"
                    + "    FROM ledger lg \n"
                    + "    LEFT JOIN subGroup sg ON sg.subCode = lg.subcode\n"
                    + "    LEFT JOIN subGroupNature sgn ON sgn.code = sg.filterNature\n"
                    + "    LEFT JOIN (\n"
                    + "       SELECT adj.adj_DocId AS docid, SUM(adj.adjustment) AS adjustment, MAX(adj.adj_V_SNo) V_SNo\n"
                    + "       FROM ledgerAdj AS adj\n"
                    + "       GROUP BY adj.adj_DocId\n"
                    + "    ) AS v1 ON v1.docId = lg.docId AND v1.V_SNo = lg.v_sno\n"
                    + "    WHERE lg.isPosted = 1\n"
                    + "      AND lg.site_Code = '1'\n"
                    + "      AND lg.v_Prefix = 25 \n"
                    + "      AND sgn.manualCode = 'CT'\n"
                    + "      AND ISNULL(lg.amtCr,0) = 0 \n"
                    + "      AND ISNULL(lg.amtDr,0) <> 0\n"
                    + "    GROUP BY lg.subcode\n"
                    + ") q2 ON q1.SupplierCode = q2.subCode";

            sqlWithParam.setSQL(sql);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {

                listNode = new WHCollectionDashBoard_Mdl();

                listNode.setSupplierTag(DmShared.xNull(rsTemp.getString("SupplierCode")));
                listNode.setSupplierText(DmShared.xNull(rsTemp.getString("SupplierName")));
                listNode.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setTotalBill(DmShared.xNull(rsTemp.getString("totalBill")));
                listNode.setSaleAmount(DmServer.vNull(rsTemp.getString("saleAmount")));
                listNode.setTotalSettlementAmount(DmServer.vNull(rsTemp.getString("totalSettlementAmt")));
                listNode.setPendingForSettlementAmt(DmServer.vNull(rsTemp.getString("pendingToAdjust")));

                lstTemp.add(listNode);
            }

            rsTemp.close();

            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(
                    true);
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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<KeyFieldExtended>();
        KeyFieldExtended defaultValue = new KeyFieldExtended();
        try {
            defaultValue.setSharedKeyFieldCode(DmServer.getServerDateTime()[0]);
            dmrTemp.setData(defaultValue);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
        }
        return dmrTemp;
    }

    @Override
    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount) {
        DmResult<CardDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, startDate = "", currentDateStr = "";
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(daysCount).equalsIgnoreCase("")) {

                currentDateStr = DmServer.getServerDateTime()[0];

                // Parse the date and number of days
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
                LocalDate currentDate = LocalDate.parse(currentDateStr, formatter);
                int daysToSubtract = DmShared.iNull(daysCount).intValue();

                // Subtract days
                LocalDate resultDate = currentDate.minusDays(daysToSubtract);

                // Optional: format result
                startDate = DmShared.dNull(resultDate.format(formatter));
            }

            sqlQuery = "SELECT COUNT(*) AS totalRows,sum(saleAmount) AS totalSaleAmount\n"
                    + "FROM ( \n"
                    + "SELECT \n"
                    + "    sb.partySubCode, \n"
                    + "    sg.name AS partyName,\n"
                    + "	max(sb.departmentCode) as departmentCode,\n"
                    + "	max(sgd.name) as departmentName,\n"
                    + "    COUNT(*) AS cnt,\n"
                    + "	sum(sb.oh_amt_net) AS saleAmount\n"
                    + "FROM saleBill sb\n"
                    + "left join voucher_Type vt on sb.v_Type= vt.v_type \n"
                    + "LEFT JOIN subGroup sg ON sg.subCode = sb.partySubCode \n"
                    + "LEFT JOIN subGroup sgd ON sgd.subCode = sb.departmentCode \n"
                    + "WHERE sb.partySubCode IS NOT NULL \n"
                    + " and sb.v_Date >='" + startDate + "' and sb.v_Date <= '" + currentDateStr + " '\n"
                    + "AND vt.category IN ('" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "','" + pvc.PHARMACY_BILL_WHOLESALE_CREDIT + "')\n"
                    + "GROUP BY sb.partySubCode, sg.name ) AS groupedData";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCount(DmServer.vNull(rsTemp.getString("totalSaleAmount")));

                node = new DmChart_Mdl();
                node.setLabelName(DmShared.xNull(rsTemp.getString("totalRows")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("totalRows")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("totalRows")).intValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListChardData().addAll(listNode);

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

    @Override
    public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysCount) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = "", startDate = "", currentDateStr = "";
        dmrTemp.setData(keyField);
        try {

            csvHeader = "Debtor Name,No. of Bills,Department Name,Sale Amount,Total Settlement Amount,Pending Amt for Settlement";
            if (!DmShared.xNull(daysCount).equalsIgnoreCase("")) {

                currentDateStr = DmServer.getServerDateTime()[0];

                // Parse the date and number of days
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
                LocalDate currentDate = LocalDate.parse(currentDateStr, formatter);
                int daysToSubtract = DmShared.iNull(daysCount).intValue();

                // Subtract days
                LocalDate resultDate = currentDate.minusDays(daysToSubtract);

                // Optional: format result
                startDate = DmShared.dNull(resultDate.format(formatter));
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            fileName = new pmDashBoard().WHOLE_SALE_COLLECTION_DASHBOARD.caption + String.valueOf(new java.util.Date().getTime()).trim();
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            sqlQuery = "   SELECT \n"
                    + "    sb.partySubCode AS SupplierCode, \n"
                    + "    sg.name AS SupplierName,\n"
                    + "    MAX(sb.departmentCode) AS departmentCode,\n"
                    + "    MAX(sgd.name) AS departmentName,\n"
                    + "    COUNT(*) AS totalBill,\n"
                    + "    SUM(sb.oh_amt_net) AS saleAmount,\n"
                    + "    SUM(ABS(la.adjustment)) AS totalSettlementAmt,\n"
                    + "    ISNULL(SUM(sb.oh_amt_net), 0) - ISNULL(SUM(ABS(la.adjustment)), 0) AS pendingToSettlementAmt\n"
                    + "FROM saleBill sb\n"
                    + "LEFT JOIN saleBillDetail sbd ON sbd.docId = sb.docId\n"
                    + "LEFT JOIN subGroup sg ON sg.subCode = sb.partySubCode\n"
                    + "LEFT JOIN subGroup sgd ON sgd.subCode = sb.departmentCode\n"
                    + "LEFT JOIN (\n"
                    + "     SELECT adj_DocId, SUM(adjustment) AS adjustment\n"
                    + "    FROM ledgerAdj \n"
                    + "    GROUP BY adj_DocId\n"
                    + ") la ON la.adj_DocId = sbd.docId \n"
                    + " WHERE sb.partySubCode IS NOT NULL "
                    + " and sb.v_Date >='" + startDate + "' and sb.v_Date <= '" + currentDateStr + " '\n"
                    + "GROUP BY sb.partySubCode, sg.name";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("SupplierName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("totalBill")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("departmentName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmServer.vNull(rsTemp.getString("saleAmount")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmServer.vNull(rsTemp.getString("totalSettlementAmt")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmServer.vNull(rsTemp.getString("pendingToSettlementAmt")) + "\"";

                handleFile.writeLine(lineData);

            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            /////
            handleFile.close();
            dmrTemp.setData(keyField);
            dmrTemp.setStatus(true);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
