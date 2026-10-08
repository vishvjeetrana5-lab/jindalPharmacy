package aarogya.dashBoard.nonSoldMedicine.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.nonSoldMedicine.shared.*;
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

public class NonSoldMedicine_SrvImpl extends DmRSSPrivate implements NonSoldMedicine_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<NonSoldMedicine_Mdl>> getData(String daysCount, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue) {
        DmResultPageing<List<NonSoldMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<NonSoldMedicine_Mdl> lstTemp = new ArrayList<NonSoldMedicine_Mdl>();
        NonSoldMedicine_Mdl listNode;
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sql = "", currentdate = "", fromDate = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            currentdate = DmServer.getServerDateTime()[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
            LocalDate currentDate = LocalDate.parse(currentdate, formatter);
            LocalDate resultDate = currentDate.minusDays(DmShared.iNull(daysCount).intValue());

            fromDate = DmShared.dNull(resultDate.format(formatter));

            sql = "  select max(im.name)  as medicineName,max(ic.name) as itemCategory,max(sg.name) as department,max(ib.batchNo) as batchNo, max(ib.expiryDate) as expiryDate, \n"
                    + "sum(istk.balanceQty) as balanceQty --,max(isnull(sbd.netSoldQty,0)) as netSoldQty \n"
                    + ",max(ib.skuMrp) skuMrp \n"
                    + "from itemMast im \n"
                    + "left join itemBatchStock istk on im.code=istk.itemCode\n"
                    + "left join itemBatch ib on ib.code=istk.ibCode\n"
                    + "left join subGroup sg on sg.subCode=istk.departmentCode\n"
                    + "left join itemCat ic on ic.code=im.itemCategoryCode\n"
                    + "left join \n"
                    + "( select sbd.itemCode ,sbd.ibCode,"
                    + "sum( case when vt.parentVoucher in ('Sale') then sbd.quantity when vt.parentVoucher in ('Sale Return') then -sbd.quantity end  ) as netSoldQty  "
                    + ",sb.site_Code\n"
                    + ",sb.departmentCode\n"
                    + "from saleBill sb \n"
                    + "left join voucher_Type vt on vt.v_Type =sb.v_Type\n"
                    + "left join saleBillDetail as sbd on sbd.docid=sb.docid\n"
                    + "where vt.parentVoucher in ('Sale') and sb.isCancelled=0    \n"
                    + " and sb.v_Date > = '" + fromDate + "' and sb.v_date <= '" + currentdate + "'\n"
                    + "group by sbd.itemCode,sbd.ibCode,sb.departmentCode,sb.site_Code \n"
                    + ") as sbd on sbd.itemCode=istk.itemCode "
                    + " and istk.site_Code=sbd.site_Code "
                    + "\n"
                    + " where im.filterItemType=11  and istk.balanceQty>0  AND sbd.itemCode IS NULL  \n"
                    + " and istk.site_Code= :siteCode   \n"
                    + " group by istk.itemCode,istk.ibCode,istk.departmentCode,istk.site_Code ";

            sqlWithParam.setSQL(sql);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                listNode = new NonSoldMedicine_Mdl();

                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
                listNode.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setExpiry(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setBalanceQty(DmServer.vNull(rsTemp.getString("balanceQty")));
                listNode.setSkuMRP(DmServer.vNull(rsTemp.getString("skuMrp")));

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
        String sql = "", currentdate = "", fromDate = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            currentdate = DmServer.getServerDateTime()[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
            LocalDate currentDate = LocalDate.parse(currentdate, formatter);
            LocalDate resultDate = currentDate.minusDays(DmShared.iNull(daysCount).intValue());

            fromDate = DmShared.dNull(resultDate.format(formatter));

            sqlQuery = "  select count(tmp.medicineName) as cnt from (\n"
                    + "\n"
                    + "  select max(im.name)  as medicineName,max(ic.name) as itemCategory,max(sg.name) as department,max(ib.batchNo) as batchNo, max(ib.expiryDate) as expiryDate, \n"
                    + "sum(istk.balanceQty) as balanceQty --,max(isnull(sbd.netSoldQty,0)) as netSoldQty \n"
                    + ",max(ib.skuMrp) skuMrp \n"
                    + "from itemMast im \n"
                    + "left join itemBatchStock istk on im.code=istk.itemCode\n"
                    + "left join itemBatch ib on ib.code=istk.ibCode\n"
                    + "left join subGroup sg on sg.subCode=istk.departmentCode\n"
                    + "left join itemCat ic on ic.code=im.itemCategoryCode\n"
                    + "left join \n"
                    + "( select sbd.itemCode ,sbd.ibCode,"
                    + "sum( case when vt.parentVoucher in ('Sale') then sbd.quantity when vt.parentVoucher in ('Sale Return') then -sbd.quantity end  ) as netSoldQty  "
                    + ",sb.site_Code\n"
                    + ",sb.departmentCode\n"
                    + "from saleBill sb \n"
                    + "left join voucher_Type vt on vt.v_Type =sb.v_Type\n"
                    + "left join saleBillDetail as sbd on sbd.docid=sb.docid\n"
                    + "where vt.parentVoucher in ('Sale') and sb.isCancelled=0    \n"
                    + " and sb.v_Date > = '" + fromDate + "' and sb.v_date <= '" + currentdate + "'\n"
                    + "group by sbd.itemCode,sbd.ibCode,sb.departmentCode,sb.site_Code \n"
                    + ") as sbd on sbd.itemCode=istk.itemCode "
                    + " and istk.site_Code=sbd.site_Code "
                    + "\n"
                    + " where im.filterItemType=11  and istk.balanceQty>0  AND sbd.itemCode IS NULL  \n"
                    + " and istk.site_Code= :siteCode   \n"
                    + " group by istk.itemCode,istk.ibCode,istk.departmentCode,istk.site_Code ) tmp ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysUpto) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        String[] currentDateTime;
        char separator = File.separatorChar;
        String csvHeader = "", condition = "", currentdate = "", fromDate = "";
        dmrTemp.setData(keyField);
        try {
            csvHeader = "Medicine Name,Medicine Category,Department,Batch No.,Expiry,Balance Qty.,SKU MRP";
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            currentdate = DmServer.getServerDateTime()[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
            LocalDate currentDate = LocalDate.parse(currentdate, formatter);
            LocalDate resultDate = currentDate.minusDays(DmShared.iNull(daysUpto).intValue());

            fromDate = DmShared.dNull(resultDate.format(formatter));

            fileName = new pmDashBoard().NON_SOLD_MEDICINE.caption + String.valueOf(new java.util.Date().getTime()).trim();
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

            sqlQuery = " select tmp.* from (\n"
                    + "  select max(im.name)  as medicineName,max(ic.name) as itemCategory,max(sg.name) as department,max(ib.batchNo) as batchNo, max(ib.expiryDate) as expiryDate, \n"
                    + "sum(istk.balanceQty) as balanceQty --,max(isnull(sbd.netSoldQty,0)) as netSoldQty \n"
                    + ",max(ib.skuMrp) skuMrp \n"
                    + "from itemMast im \n"
                    + "left join itemBatchStock istk on im.code=istk.itemCode\n"
                    + "left join itemBatch ib on ib.code=istk.ibCode\n"
                    + "left join subGroup sg on sg.subCode=istk.departmentCode\n"
                    + "left join itemCat ic on ic.code=im.itemCategoryCode\n"
                    + "left join \n"
                    + "( select sbd.itemCode ,sbd.ibCode,"
                    + "sum( case when vt.parentVoucher in ('Sale') then sbd.quantity when vt.parentVoucher in ('Sale Return') then -sbd.quantity end  ) as netSoldQty  "
                    + ",sb.site_Code\n"
                    + ",sb.departmentCode\n"
                    + "from saleBill sb \n"
                    + "left join voucher_Type vt on vt.v_Type =sb.v_Type\n"
                    + "left join saleBillDetail as sbd on sbd.docid=sb.docid\n"
                    + "where vt.parentVoucher in ('Sale') and sb.isCancelled=0    \n"
                    + " and sb.v_Date > = '" + fromDate + "' and sb.v_date <= '" + currentdate + "'\n"
                    + "group by sbd.itemCode,sbd.ibCode,sb.departmentCode,sb.site_Code \n"
                    + ") as sbd on sbd.itemCode=istk.itemCode "
                    + " and istk.site_Code=sbd.site_Code "
                    + "\n"
                    + " where im.filterItemType=11  and istk.balanceQty>0  AND sbd.itemCode IS NULL  \n"
                    + " and istk.site_Code= :siteCode   \n"
                    + " group by istk.itemCode,istk.ibCode,istk.departmentCode,istk.site_Code ) tmp "
                    + "  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("medicineName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemCategory")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("department")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("batchNo")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.dNull(rsTemp.getDate("expiryDate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceQty")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("skuMrp")) + "\"";

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
