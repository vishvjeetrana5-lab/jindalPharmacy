package aarogya.dashBoard.inventoryPendingIndent.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.inventoryPendingIndent.shared.*;
import aarogya.dashBoard.inventoryPendingIndent.shared.CardDetail_Mdl;
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

public class InventoryPendingIndent_SrvImpl extends DmRSSPrivate implements InventoryPendingIndent_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<InventoryPendingIndent_Mdl>> getData(String daysCount, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue) {
        DmResultPageing<List<InventoryPendingIndent_Mdl>> rstTemp = new DmResultPageing<>();
        List<InventoryPendingIndent_Mdl> lstTemp = new ArrayList<InventoryPendingIndent_Mdl>();
        InventoryPendingIndent_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" SELECT   i.recId AS indNo, i.v_Date as indDate, \n"
                    + "sdm.name AS depName,sg.name AS indentorName,            \n"
                    + " im.name AS itemName, ind.reqQty,ind.aprQty,ind.orderedQty, um.unit, ind.reqDate,          \n"
                    + " Cast(IsNull(ind.aprQty,0)-(IsNull(ind.orderedQty,0)) as decimal(18,7)) as pendingQuantity ,icm.name as colourName,\n"
                    + " ism.name as sizeName,rq.recId as requisitionNo ,isNull(ind.statusRemark,'') as statusRemark          \n"
                    + " \n"
                    + " FROM indent i             \n"
                    + " LEFT JOIN indentDetail AS ind ON ind.docId=i.docId              \n"
                    + " LEFT JOIN subgroup sdm ON i.departmentCode = sdm.subcode              \n"
                    + " LEFT JOIN subgroup sg ON i.indentorCode = sg.subCode              \n"
                    + " LEFT JOIN voucher_type vt ON i.v_Type = vt.v_Type             \n"
                    + " LEFT JOIN itemMast AS im ON im.code=ind.itemCode            \n"
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup             \n"
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode            \n"
                    + " LEFT JOIN unitMast um ON um.code = im.sku             \n"
                    + " Left join siteMast as sm on sm.code =i.site_Code           \n"
                    + " LEFT JOIN itemColourMast icm ON icm.code =ind.colourCode      \n"
                    + " LEFT JOIN itemSizeMast ism ON ism.code =ind.sizeCode          \n"
                    + " LEFT JOIN requisition rq ON rq.docId =ind.requisitionDocId        \n"
                    + " WHERE vt.category = '" + pvc.INVENTORY_INDENT + "' AND ISNULL(i.approvedBy, '') <> ''\n"
                    + " and  i.v_Date >= CAST(DATEADD(dd, -:daysCount, GETDATE()) AS date)\n"
                    + " AND   i.site_Code =:siteCode  and ind.isStatus not in (1,2)   \n"
                    + "  \n"
                    + " ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setInteger("daysCount", DmShared.iNull(daysCount).intValue());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                listNode = new InventoryPendingIndent_Mdl();

                listNode.setIndNo(DmShared.xNull(rsTemp.getString("indNo")));
                listNode.setIndDate(DmShared.dNull(rsTemp.getDate("indDate")));
                listNode.setDepName(DmShared.xNull(rsTemp.getString("depName")));
                listNode.setIndentorName(DmShared.xNull(rsTemp.getString("indentorName")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setReqQty(DmServer.vNull(rsTemp.getString("reqQty"), "0.00"));
                listNode.setAprQty(DmServer.vNull(rsTemp.getString("aprQty"), "0.00"));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                listNode.setPendingQuantity(DmServer.vNull(rsTemp.getString("pendingQuantity"), "0.00"));
                listNode.setRequisitionNo(DmShared.xNull(rsTemp.getString("requisitionNo")));
                listNode.setStatusRemark(DmShared.xNull(rsTemp.getString("statusRemark")));

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

            sqlQuery = " SELECT count(i.docid) as cnt\n"
                    + "FROM indent i  \n"
                    + "LEFT JOIN indentDetail ind ON ind.docId = i.docId \n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = i.v_Type \n"
                    + "WHERE vt.category in( '" + pvc.INVENTORY_INDENT + "') AND i.v_Date >= cast(dateadd(dd,-:daysCount ,getdate()) as date)  \n"
                    + "  AND  i.site_Code   = :siteCode \n"
                    + "and ind.isStatus not in (1,2) AND ISNULL(i.approvedBy, '') <> '' ";
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
            csvHeader = "Indent No,Indent Date, Deptt. Name, Indentor Name,Item Name, Request Qty,Approved Qty, Unit, Request Date,Pending Quantity ,"
                    + "           Requisition No. , Status Remark";
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            fileName = "Inventory_pending_Indent_" + String.valueOf(new java.util.Date().getTime()).trim();
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

            sqlQuery = "SELECT i.docId, id.v_Sno as VSNo,  i.approvedBy, i.recId AS indNo , vt.short_Name AS vTypeShortName , \n"
                    + "i.v_Date AS indDate ,sdm.name AS depName,sg.name AS indentorName  ,CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE ''\n"
                    + "END  as supCity , im.name AS itemName  , id.reqDate , id.reqQty  ,\n"
                    + "um.unit AS unit   , id.reqDate AS reqDate ,ind.aprQty, \n"
                    + " Cast(IsNull(ind.aprQty,0)-(IsNull(ind.orderedQty,0)) as decimal(18,7)) as pendingQuantity  ,\n"
                    + "CASE WHEN id.isStatus = 1 THEN 'Cancelled' WHEN id.isStatus = 2 THEN 'Completed' ELSE 'Open' END AS status ,\n"
                    + "sm.code AS siteCode , sm.name AS siteName ,\n"
                    + "isNull(id.statusRemark,'') as statusRemark , rq.recId as requisitionNo \n"
                    + "\n"
                    + "FROM indent i  \n"
                    + " LEFT JOIN indentDetail AS ind ON ind.docId=i.docId  \n"
                    + "LEFT JOIN indentDetail id ON id.docId = i.docId \n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = i.v_Type \n"
                    + " LEFT JOIN subgroup sdm ON i.departmentCode = sdm.subcode\n"
                    + "LEFT JOIN subgroup sg ON sg.subCode = i.indentorCode \n"
                    + "LEFT JOIN itemMast im ON im.code = id.itemCode \n"
                    + "LEFT JOIN itemColourMast icm ON icm.code = id.colourCode \n"
                    + "LEFT JOIN itemSizeMast ism ON ism.code = id.sizeCode \n"
                    + "LEFT JOIN unitMast um ON um.code = im.sku \n"
                    + "LEFT JOIN siteMast sm ON sm.code = i.site_Code \n"
                    + "left join city as c on c.cityCode = sg.cityCode  \n"
                    + " LEFT JOIN requisition rq ON rq.docId =id.requisitionDocId          \n"
                    + "\n"
                    + "WHERE vt.category in( '" + pvc.INVENTORY_INDENT + "' )"
                    + "AND i.v_Date >= cast(dateadd(dd,- :daysCount ,getdate()) as date)  \n"
                    + "  AND  i.site_Code   = :siteCode \n"
                    + "and id.isStatus not in (1,2) AND ISNULL(i.approvedBy, '') <> '' \n"
                    + "  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setInt("daysCount", DmShared.iNull(daysUpto).intValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("indNo")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.dNull(rsTemp.getDate("indDate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("depName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("indentorName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmServer.vNull(rsTemp.getString("reqQty"), "0.00") + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("aprQty"), "0.00") + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("unit")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.dNull(rsTemp.getDate("reqDate")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("pendingQuantity"), "0.00") + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("requisitionNo")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("statusRemark")) + "\"";
                lineData += commaDelimiter;

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
