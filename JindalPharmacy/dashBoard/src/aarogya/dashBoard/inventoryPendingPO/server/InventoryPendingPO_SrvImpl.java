package aarogya.dashBoard.inventoryPendingPO.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.inventoryPendingPO.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
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
import java.util.*;

public class InventoryPendingPO_SrvImpl extends DmRSSPrivate implements InventoryPendingPO_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<InventoryPendingPO_Mdl>> getData(String daysCount, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue) {
        DmResultPageing<List<InventoryPendingPO_Mdl>> rstTemp = new DmResultPageing<>();
        List<InventoryPendingPO_Mdl> lstTemp = new ArrayList<InventoryPendingPO_Mdl>();
        InventoryPendingPO_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT po.docId, pod.v_Sno as VSNo,  po.approvedBy, po.recId AS orderNo , vt.short_Name AS vTypeShortName , \n"
                    + "po.v_Date AS orderDate ,  sg.name as supplierName   ,CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE ''\n"
                    + "END  as supCity , im.name AS itemName , icm.name AS colour , ism.name AS size , pod.requiredDate , pod.quantity AS orderQty ,\n"
                    + "um.unit AS unit , pod.rate , pod.oh_Amt_Gross , pod.oh_Amt_Discount , pod.oh_Amt_Net , pod.challanedQty AS recQty , \n"
                    + "ISNULL(pod.quantity, 0) - ISNULL(pod.challanedQty, 0) AS pendingQty  , pod.tl_ExcessQty , pod.tl_ShortageQty  ,\n"
                    + "CASE WHEN pod.isStatus = 1 THEN 'Cancelled' WHEN pod.isStatus = 2 THEN 'Completed' ELSE 'Open' END AS status ,\n"
                    + "sm.code AS siteCode , sm.name AS siteName ,REPLACE(CONVERT(VARCHAR(11), approvalDateTime, 106), ' ', '-') \n"
                    + "+' '+STUFF(RIGHT( CONVERT(VARCHAR,approvalDateTime,100 ) ,7), 6, 0, ' ') as approvedDateTime ,\n"
                    + "isNull(pod.statusRemark,'') as statusRemark \n"
                    + "\n"
                    + "FROM pOrder po  \n"
                    + "LEFT JOIN pOrderDetail pod ON pod.docId = po.docId \n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = po.v_Type \n"
                    + "LEFT JOIN subgroup sg ON sg.subCode = po.subCode \n"
                    + "LEFT JOIN itemMast im ON im.code = pod.itemCode \n"
                    + "LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode \n"
                    + "LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode \n"
                    + "LEFT JOIN unitMast um ON um.code = pod.unitCode \n"
                    + "LEFT JOIN siteMast sm ON sm.code = po.site_Code \n"
                    + "left join city as c on c.cityCode = sg.cityCode  \n"
                    + "\n"
                    + "WHERE vt.category in( '" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "','" + pvc.SERVICE_ORDER + "','" + pvc.SERVICE_ORDER_UNREGISTERED + "') "
                    + " AND po.v_Date >= cast(dateadd(dd,- :daysCount ,getdate()) as date)  \n"
                    + "  AND  po.site_Code   = :siteCode \n"
                    + "and pod.isStatus not in (1,2)\n"
                    + "  ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setInteger("daysCount", DmShared.iNull(daysCount).intValue());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                listNode = new InventoryPendingPO_Mdl();

                listNode.setPoNumber(DmShared.xNull(rsTemp.getString("orderNo")));
                listNode.setPoDate(DmShared.dNull(rsTemp.getDate("orderDate")));
                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setOrderQty(DmShared.xNull(rsTemp.getString("orderQty")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setReceiveQty(DmShared.xNull(rsTemp.getString("recQty")));
                listNode.setRate(DmShared.xNull(rsTemp.getString("rate")));
                listNode.setAmount(DmShared.xNull(rsTemp.getString("oh_Amt_Gross")));

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
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT count(po.docid) as cnt\n"
                    + "FROM pOrder po  \n"
                    + "LEFT JOIN pOrderDetail pod ON pod.docId = po.docId \n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = po.v_Type \n"
                    + "WHERE vt.category in( '" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "','" + pvc.SERVICE_ORDER + "','" + pvc.SERVICE_ORDER_UNREGISTERED + "') AND po.v_Date >= cast(dateadd(dd,- :daysCount ,getdate()) as date)  \n"
                    + "  AND  po.site_Code   = :siteCode \n"
                    + "and pod.isStatus not in (1,2)\n"
                    + "  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setInt("daysCount", DmShared.iNull(daysCount).intValue());
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
        String csvHeader = "", condition = "";
        dmrTemp.setData(keyField);
        try {
            csvHeader = "PO No.,PO Date,Supplier Name,Item Name,Order Qty.,Unit,Receive Qty.,Amount";
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            fileName = "Inventory_pending_PO_" + String.valueOf(new java.util.Date().getTime()).trim();
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

            sqlQuery = "SELECT po.docId, pod.v_Sno as VSNo,  po.approvedBy, po.recId AS orderNo , vt.short_Name AS vTypeShortName , \n"
                    + "po.v_Date AS orderDate ,  sg.name as supplierName   ,CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE ''\n"
                    + "END  as supCity , im.name AS itemName , icm.name AS colour , ism.name AS size , pod.requiredDate , pod.quantity AS orderQty ,\n"
                    + "um.unit AS unit , pod.rate , pod.oh_Amt_Gross , pod.oh_Amt_Discount , pod.oh_Amt_Net , pod.challanedQty AS recQty , \n"
                    + "ISNULL(pod.quantity, 0) - ISNULL(pod.challanedQty, 0) AS pendingQty  , pod.tl_ExcessQty , pod.tl_ShortageQty  ,\n"
                    + "CASE WHEN pod.isStatus = 1 THEN 'Cancelled' WHEN pod.isStatus = 2 THEN 'Completed' ELSE 'Open' END AS status ,\n"
                    + "sm.code AS siteCode , sm.name AS siteName ,REPLACE(CONVERT(VARCHAR(11), approvalDateTime, 106), ' ', '-') \n"
                    + "+' '+STUFF(RIGHT( CONVERT(VARCHAR,approvalDateTime,100 ) ,7), 6, 0, ' ') as approvedDateTime ,\n"
                    + "isNull(pod.statusRemark,'') as statusRemark \n"
                    + "\n"
                    + "FROM pOrder po  \n"
                    + "LEFT JOIN pOrderDetail pod ON pod.docId = po.docId \n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = po.v_Type \n"
                    + "LEFT JOIN subgroup sg ON sg.subCode = po.subCode \n"
                    + "LEFT JOIN itemMast im ON im.code = pod.itemCode \n"
                    + "LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode \n"
                    + "LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode \n"
                    + "LEFT JOIN unitMast um ON um.code = pod.unitCode \n"
                    + "LEFT JOIN siteMast sm ON sm.code = po.site_Code \n"
                    + "left join city as c on c.cityCode = sg.cityCode  \n"
                    + "\n"
                    + "WHERE vt.category in( '" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "','" + pvc.SERVICE_ORDER + "','" + pvc.SERVICE_ORDER_UNREGISTERED + "') AND po.v_Date >= cast(dateadd(dd,- :daysCount ,getdate()) as date)  \n"
                    + "  AND  po.site_Code   = :siteCode \n"
                    + "and pod.isStatus not in (1,2)\n"
                    + "  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setInt("daysCount", DmShared.iNull(daysUpto).intValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("orderNo")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.dNull(rsTemp.getDate("orderDate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("supplierName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("orderQty")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("unit")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("recQty")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("rate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("oh_Amt_Gross")) + "\"";

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
