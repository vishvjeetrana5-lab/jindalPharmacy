package aarogya.dashBoard.fastMovingMedicine.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.fastMovingMedicine.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmDashBoard;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FastMovingMedicine_SrvImpl extends DmRSSPrivate implements FastMovingMedicine_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<FastMovingMedicine_Mdl>> getData(String daysCount, String lastDays, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue) {
        DmResultPageing<List<FastMovingMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<FastMovingMedicine_Mdl> lstTemp = new ArrayList<FastMovingMedicine_Mdl>();
        FastMovingMedicine_Mdl listNode;
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sql = "", toDate = "", currentdate = "", lastthrityDays = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            int daysToSubtract = DmShared.iNull(lastDays).intValue();
            currentdate = DmServer.getServerDateTime()[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
            LocalDate currentDate = LocalDate.parse(currentdate, formatter);
            LocalDate resultDate = currentDate.minusDays(daysToSubtract);

            lastthrityDays = DmShared.dNull(resultDate.format(formatter));
            sql = "  	select tmp.* from \n"
                    + "(\n"
                    + "	SELECT sbd.itemCode, im.name as medicineName, max(ic.name) as medicineCategory,"
                    + " 	CAST(\n"
                    + "    SUM(\n"
                    + "        CASE \n"
                    + "            WHEN vt.parentVoucher IN ('Sale', 'Sale Return') \n"
                    + "                THEN ISNULL(sbd.isuQty, 0)\n"
                    + "            ELSE 0 \n"
                    + "        END\n"
                    + "    ) AS BIGINT\n"
                    + ") AS totalSoldQty , \n"
                    + "		RANK() OVER (ORDER BY SUM(sbd.isuQty) DESC) AS salesRank \n"
                    + "		FROM stock sbd\n"
                    + "		left join itemMast im on im.code=sbd.itemCode\n"
                    + "		left join itemCat ic on ic.code=im.itemCategoryCode\n "
                    + "         left join saleBill sb on sb.docid=sbd.docId \n"
                    + "         left join voucher_Type as vt on sb.v_Type=vt.v_Type \n"
                    + "\n"
                    + "WHERE  sbd.v_Date >='" + lastthrityDays + "' and sbd.v_Date <= '" + currentdate + " ' \n"
                    + "               and sbd.departmentCode <> 1000100000000000267\n"
                    + "               AND CASE\n"
                    + "                     WHEN vt.parentvoucher IN ( 'Sale', 'Sale Return' )\n"
                    + "                   THEN 1\n"
                    + "                     WHEN vt.parentvoucher IN (\n"
                    + "                          'Purchase', 'Purchase Return' )\n"
                    + "                   THEN 0\n"
                    + "                     ELSE\n"
                    + "                       CASE\n"
                    + "                         WHEN ( Isnull(sbd.isuqty, 0) > 0\n"
                    + "                                 OR Isnull(sbd.recqty, 0) < 0 ) THEN 1\n"
                    + "                         ELSE 0\n"
                    + "                       END\n"
                    + "                   END = 1           \n"
                    + "GROUP BY sbd.itemCode, im.name \n"
                    + ") as tmp "
                    + "		where SalesRank <= :daysCount  ";

            sqlWithParam.setSQL(sql);
            sqlWithParam.setString("daysCount", DmShared.iNull(daysCount).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                listNode = new FastMovingMedicine_Mdl();

                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
                listNode.setMedicineCategory(DmShared.xNull(rsTemp.getString("medicineCategory")));
                listNode.setTotalSoldQty(DmShared.xNull(rsTemp.getString("totalSoldQty")));
                listNode.setSalesRank(DmShared.xNull(rsTemp.getString("salesRank")));

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
    public DmResult<String> getNewDate(int value, String date) {
        DmResult<String> rstTemp = new DmResult<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar;
        Date dt;
        try {
            calendar = Calendar.getInstance();
            dt = sdf.parse(date);
            calendar.setTime(dt);
            calendar.add(Calendar.DATE, value);
            rstTemp.setStatus(true);
            rstTemp.setData(sdf.format(calendar.getTime()));
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        Connection cnTempCmp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);
        String sql = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select count(tmp.medicineName) as cnt from \n"
                    + "(\n"
                    + "	SELECT sbd.itemCode, im.name as medicineName, max(ic.name) as medicineCategory,cast(SUM(sbd.quantity) as bigint) AS totalSoldQty, \n"
                    + "		RANK() OVER (ORDER BY SUM(sbd.quantity) DESC) AS salesRank \n"
                    + "		FROM saleBillDetail sbd\n"
                    + "		left join itemMast im on im.code=sbd.itemCode\n"
                    + "		left join itemCat ic on ic.code=im.itemCategoryCode\n "
                    + "         left join saleBill sb on sb.docid=sbd.docId  "
                    + "         left join voucher_Type as vt on sb.v_Type=vt.v_Type \n"
                    + "\n"
                    + "		where im.filterItemType=11\n and sb.site_Code=:siteCode "
                    + "         and vt.category=(  'PHWBC' ) \n"
                    + "		GROUP BY sbd.itemCode, im.name\n"
                    + "	) as tmp\n"
                    + "\n"
                    + "	 where SalesRank <= :daysCount \n "
                    + "	  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("daysCount", DmShared.iNull(daysCount).toString());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCount(DmShared.iNull(rsTemp.getString("cnt")).toString());

                node = new DmChart_Mdl();
                node.setLabelName(DmShared.xNull(rsTemp.getString("cnt")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("cnt")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("cnt")).intValue());
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysUpto, String lastDays) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        String[] currentDateTime;
        char separator = File.separatorChar;
        String csvHeader = "", condition = "", currentdate = "", lastthrityDays = "";
        dmrTemp.setData(keyField);
        try {
            csvHeader = "Medicine Name,Medicine Category,Total Sold Quantity,Sale Rank";
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            int daysToSubtract = DmShared.iNull(lastDays).intValue();
            currentdate = DmServer.getServerDateTime()[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
            LocalDate currentDate = LocalDate.parse(currentdate, formatter);
            LocalDate resultDate = currentDate.minusDays(daysToSubtract);
            lastthrityDays = DmShared.dNull(resultDate.format(formatter));

            fileName = new pmDashBoard().FAST_MOVING_MEDICINE.caption + String.valueOf(new java.util.Date().getTime()).trim();
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

            sqlQuery = " select    tmp.*      from \n"
                    + "(\n"
                    + "		SELECT sbd.itemCode, im.name as medicineName, max(ic.name) as medicineCategory,\n"
                    + "    CAST(\n"
                    + "    SUM(\n"
                    + "        CASE \n"
                    + "            WHEN vt.parentVoucher IN ('Sale', 'Sale Return') \n"
                    + "                THEN ISNULL(sbd.isuQty, 0)\n"
                    + "            ELSE 0 \n"
                    + "        END\n"
                    + "    ) AS BIGINT\n"
                    + ") AS totalSoldQty , \n"
                    + "		RANK() OVER (ORDER BY SUM(sbd.isuQty) DESC) AS salesRank \n"
                    + "		FROM stock sbd\n"
                    + "		left join itemMast im on im.code=sbd.itemCode\n"
                    + "		left join itemCat ic on ic.code=im.itemCategoryCode\n"
                    + "          left join saleBill sb on sb.docid=sbd.docId \n"
                    + "         left join voucher_Type as vt on sb.v_Type=vt.v_Type \n"
                    + "\n"
                    + "WHERE  sbd.v_Date >='" + lastthrityDays + "' and sbd.v_Date <= '" + currentdate + " ' \n"
                    + "               and sbd.departmentCode <> 1000100000000000267\n"
                    + "               AND CASE\n"
                    + "                     WHEN vt.parentvoucher IN ( 'Sale', 'Sale Return' )\n"
                    + "                   THEN 1\n"
                    + "                     WHEN vt.parentvoucher IN (\n"
                    + "                          'Purchase', 'Purchase Return' )\n"
                    + "                   THEN 0\n"
                    + "                     ELSE\n"
                    + "                       CASE\n"
                    + "                         WHEN ( Isnull(sbd.isuqty, 0) > 0\n"
                    + "                                 OR Isnull(sbd.recqty, 0) < 0 ) THEN 1\n"
                    + "                         ELSE 0\n"
                    + "                       END\n"
                    + "                   END = 1           \n"
                    + "         GROUP BY sbd.itemCode, im.name \n"
                    + "		) as tmp\n"
                    + "\n"
                    + "		where SalesRank <= :daysCount \n "
                    + " order by salesRank ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setInt("daysCount", DmShared.iNull(daysUpto).intValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("medicineName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("medicineCategory")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("totalSoldQty")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("salesRank")) + "\"";

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
