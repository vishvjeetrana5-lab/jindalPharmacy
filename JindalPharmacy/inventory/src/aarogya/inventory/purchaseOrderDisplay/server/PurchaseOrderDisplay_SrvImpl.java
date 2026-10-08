package aarogya.inventory.purchaseOrderDisplay.server;

import aarogya.inventory.purchaseOrderDisplay.shared.ApprovedVendors_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.IndentDetail_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.SearchValue_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.TxtHItem_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.GrdGrnPurchaseHistory_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.TxtHSupplier_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Srv;
import aarogya.inventory.purchaseOrderDisplay.shared.TxtHIndent_Mdl;
import java.util.Collections;

public class PurchaseOrderDisplay_SrvImpl extends DmRSSPrivate implements PurchaseOrderDisplay_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<List<PurchaseOrderDisplay_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl) {
        DmResultPageing<List<PurchaseOrderDisplay_Mdl>> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        PurchaseOrderDisplay_Mdl node;
        List<PurchaseOrderDisplay_Mdl> lstTemp = new ArrayList<>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(searchValueMdl.getDepartmentCode()).trim().equals("")) {
                condition += " AND  ind.toDepartmentCode IN (" + DmServer.getParamList(":toDepartmentCode", searchValueMdl.getDepartmentCode()) + " ) ";
            } else if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).trim().equalsIgnoreCase("")) {
                condition += "and ind.toDepartmentCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
            }

            if (!DmShared.xNull(searchValueMdl.getCompanyCode()).trim().equals("")) {
                condition += " AND im.itemmakeCode IN (" + DmServer.getParamList(":medMake", searchValueMdl.getCompanyCode()) + " )";
            }

            if (!DmShared.xNull(searchValueMdl.getGroupCode()).trim().equals("")) {
                condition += " AND im.itemGroup IN (" + DmServer.getParamList(":medGroup", searchValueMdl.getGroupCode()) + " )";
            }

            if (!DmShared.xNull(searchValueMdl.getCategoryCode()).trim().equals("")) {
                condition += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":medCat", searchValueMdl.getCategoryCode()) + " )";
            }

            if (!DmShared.xNull(searchValueMdl.getMedicineCode()).trim().equals("")) {
                condition += " AND im.code IN (" + DmServer.getParamList(":medicineCode", searchValueMdl.getMedicineCode()) + " )";
            }

            if (!DmShared.xNull(searchValueMdl.getIndent()).trim().equals("")) {
                condition += " AND ind.docid IN (" + DmServer.getParamList(":indent", searchValueMdl.getIndent()) + " )";
            }

            sqlQuery = "select ind.docid as code,ind.recid as name,sgtd.name as toDepartmentName,indd.v_Sno,indd.itemCode,indd.aprQty, \n"
                    + "         indd.orderedQty,indd.aprQty-isNull(indd.orderedQty,0) as pendingQty,im.name as itemName,um.unit as skuName,  \n "
                    + "		sg.name as lastSupplerName,lst.subCode as lastSupplierCode,ind.toDepartmentCode,im.sku as skuCode, \n "
                    + "		um.decimalPlaces as  unitDecimalPlaces,lst.rate as lastOrderRate,im.purchaseUnit as purchaseUnitCode, \n"
                    + "		im.purchaseUnitConversionRate,ump.unit as purchaseUnitName \n"
                    + "             from indent ind \n "
                    + "             left join indentDetail indd on ind.docid=indd.docid \n "
                    + "             left join subGroup sgtd on ind.toDepartmentCode=sgtd.subCode \n "
                    + "             left join itemMast im on indd.itemCode=im.code \n "
                    + "             left join unitMast um on im.sku=um.code \n "
                    + "             left join unitMast ump on im.purchaseUnit=ump.code \n "
                    + "             left join ( \n "
                    + "                         select po.v_Date,po.docid,v_Sno,itemCode,po.subcode,pod.rate , \n "
                    + "                             row_Number() over(partition by pod.itemcode order by po.v_Date desc) as lastiteminfo \n "
                    + "                                 FROM pOrder po \n "
                    + "                                 left join pOrderDetail pod on po.docid=pod.docid \n "
                    + "                         ) as lst on indd.itemCode=lst.itemCode and lst.lastiteminfo=1 \n "
                    + "             left join subGroup sg on sg.subCode=lst.subCode \n "
                    + "             where indd.site_Code=:site_Code and  ind.v_Date between :movementFrom and :asOnDate and indd.isStatus=0 \n "
                    + "              and indd.aprQty-isnull(orderedQty,0)>0 " + condition
                    + "             order by im.name,ind.v_Date,indd.DocId,indd.v_Sno  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.dNull(searchValueMdl.getAsOnDate()).trim().equals("")) {
                psTemp.setDate("asOnDate", DmServer.convertDate(searchValueMdl.getAsOnDate()));
            }

            if (!DmShared.dNull(searchValueMdl.getMovementFrom()).trim().equals("")) {
                psTemp.setDate("movementFrom", DmServer.convertDate(searchValueMdl.getMovementFrom()));
            }

            if (!DmShared.xNull(searchValueMdl.getDepartmentCode()).trim().equals("")) {
                psTemp.setStringList("toDepartmentCode", DmShared.xNull(DmShared.xNull(searchValueMdl.getDepartmentCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getCompanyCode()).trim().equals("")) {
                psTemp.setStringList("medMake", DmShared.xNull(DmShared.xNull(searchValueMdl.getCompanyCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getGroupCode()).trim().equals("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(DmShared.xNull(searchValueMdl.getGroupCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getCategoryCode()).trim().equals("")) {
                psTemp.setStringList("medCat", DmShared.xNull(DmShared.xNull(searchValueMdl.getCategoryCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getMedicineCode()).trim().equals("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(DmShared.xNull(searchValueMdl.getMedicineCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getIndent()).trim().equals("")) {
                psTemp.setStringList("indent", DmShared.xNull(DmShared.xNull(searchValueMdl.getIndent())));
            }

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new PurchaseOrderDisplay_Mdl();
                IndentDetail_Mdl nodeLine;

                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("toDepartmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("toDepartmentName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("unitDecimalPlaces")));
                node.setLastOrderRate(DmShared.xNull(rsTemp.getString("lastOrderRate")));
                node.setLastOrderSupplier(DmShared.xNull(rsTemp.getString("lastSupplerName")));
                node.setLastOrderSupplierCode(DmShared.xNull(rsTemp.getString("lastSupplierCode")));
                node.setPurchaseUnitConversionRate(DmShared.xNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setItemPurchaseunitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setItemPurchaseunitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));

                nodeLine = new IndentDetail_Mdl();

                if (lstTemp.contains(node)) {
                    node = lstTemp.get(lstTemp.indexOf(node));
//                    node.setApprovedQty(DmServer.vNull((DmShared.vNull(node.getApprovedQty()).doubleValue()
//                            + DmShared.vNull(rsTemp.getString("aprQty")).doubleValue()),
//                            DmShared.getNumberFormat(DmShared.iNull(node.getUnitDecimalPlaces()).intValue())));
                    node.setApprovedQty(DmServer.vNull((DmShared.vNull(node.getApprovedQty()).doubleValue()
                            + DmShared.vNull(rsTemp.getString("aprQty")).doubleValue()), "0.00"));
//                    node.setPendingOrderQty(DmServer.vNull((DmShared.vNull(node.getPendingOrderQty()).doubleValue()
//                            + DmShared.vNull(rsTemp.getString("pendingQty")).doubleValue()),
//                            DmShared.getNumberFormat((DmShared.iNull(node.getUnitDecimalPlaces()).intValue()))));
                    node.setPendingOrderQty(DmServer.vNull((DmShared.vNull(node.getPendingOrderQty()).doubleValue()
                            + DmShared.vNull(rsTemp.getString("pendingQty")).doubleValue()), "0.00"));
                } else {
                    node.setApprovedQty(DmServer.vNull(rsTemp.getString("aprQty"), "0.00"));
                    node.setPendingOrderQty(DmServer.vNull(rsTemp.getString("pendingQty"), "0.00"));
                    lstTemp.add(node);
                }

                nodeLine.setIndentDocID(DmShared.xNull(rsTemp.getString("code")));
                nodeLine.setIndentV_Sno(DmShared.xNull(rsTemp.getString("v_Sno")));

                node.getGrdItemDetailList().add(nodeLine);
            }

//            Collections.sort(lstTemp, new ShortBySupplier());
            psTemp.close();
            rsTemp.close();
            dmrTemp.setData(lstTemp);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition += " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlWithParam.setSQL("select	dm.subCode, sg.name"
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "		where sg.isActive = 1 and sgn.manualCode in ('CM','PH')" + condition);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
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
    public DmResult<KeyFieldExtended> getDefaultValues() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended lstTemp = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstTemp);

            cnTemp.close();
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
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT imm.code, imm.companyName AS name"
                    + " FROM itemMakeMast imm"
                    + " WHERE imm.filterItemType = :filterItemType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name"
                    + " FROM itemgroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ic.code, ic.name "
                    + " FROM itemcat ic "
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT im.code, im.name, im.manualCode "
                    + " FROM itemMast im"
                    + " LEFT JOIN unitmast um ON im.sku = um.code "
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<TxtHIndent_Mdl>> getTxtHIndent(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHIndent_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHIndent_Mdl> lstTemp = new ArrayList<>();
        TxtHIndent_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select ind.docid as code,MAX(ind.recid) as name,MAX(ind.v_Date) as entryDate, \n"
                    + " MAX(sgfd.name) as fromDepartmentname,MAX(sgtd.name) as toDepartmentName,MAX(emp.name) as indentorName \n"
                    + "     from indent ind \n"
                    + "     left join indentDetail indd on ind.docid=indd.docid \n"
                    + "     left join subGroup sgfd on ind.departmentCode=sgfd.subCode \n"
                    + "     left join subGroup sgtd on ind.toDepartmentCode=sgtd.subCode \n"
                    + "     left join subGroup emp on ind.indentorCode=emp.subCode \n"
                    + "     WHERE ind.site_Code = :site_Code and  indd.isStatus=0 \n"
                    + "     GROUP BY ind.docid \n"
                    + "     HAVING SUM(indd.aprQty)-sum(isnull(orderedQty,0))>0 ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHIndent_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setEntryDate(DmShared.dNull(rsTemp.getDate("entryDate")));
                listNode.setFromDepartmentName(DmShared.xNull(rsTemp.getString("fromDepartmentname")));
                listNode.setToDepartmentName(DmShared.xNull(rsTemp.getString("toDepartmentName")));
                listNode.setIndentorName(DmShared.xNull(rsTemp.getString("indentorName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResult<List<PurchaseOrderDisplay_Mdl>> getShortOnSupplier(List<PurchaseOrderDisplay_Mdl> lstTemp) {
        DmResultPageing<List<PurchaseOrderDisplay_Mdl>> dmrTemp = new DmResultPageing();
        Collections.sort(lstTemp, new ShortBySupplier());
        dmrTemp.setData(lstTemp);
        dmrTemp.setStatus(true);
        return dmrTemp;
    }

    @Override
    public DmResult<List<ApprovedVendors_Mdl>> getApprovedVendors(String strItemCode) {
        DmResultPageing<List<ApprovedVendors_Mdl>> dmrTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ApprovedVendors_Mdl node;
        List<ApprovedVendors_Mdl> lstTemp = new ArrayList<>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select apv.code as itemCode,apv.sNo,sg.name as approvedVendorName,lstrn.v_date,isnull(lstrn.lastRate,0) as lastRate,lstrn.rankNum, \n"
                    + "apv.approvedVendorCode         \n"
                    + "         from approvedVendors apv \n "
                    + "		left join subgroup sg on apv.approvedVendorCode=sg.subCode \n "
                    + "         left Join \n "
                    + "         ( \n "
                    + "         select a.itemCode,a.subcode,rank() over (partition by a.subcode order by a.v_date desc) as rankNum,a.v_Date,isnull(a.rate,0) as lastRate \n "
                    + "             from \n "
                    + "                 ( \n "
                    + "                 select pb.subcode,pb.v_Date,pbd.itemCode,pbd.rate \n "
                    + "                         from pBill pb \n "
                    + "                         left join pBillDetail pbd on pb.docid=pbd.docid \n "
                    + "         		left join voucher_Type vt on pb.v_Type=vt.v_Type \n "
                    + "                         where vt.category in('" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "') \n "
                    + "                 union all \n "
                    + "                 select pc.subcode,pc.v_Date,pcd.itemCode,pcd.rate \n "
                    + "                         from pChallan pc \n "
                    + "                         left join pChallanDetail pcd on pc.docid=pcd.docid \n "
                    + "                         left join voucher_Type vt on pc.v_Type=vt.v_Type \n "
                    + "                         where vt.category in('" + pvc.PURCHASE_CHALLAN + "','" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "') \n "
                    + "                 ) As A \n "
                    + "         ) as lstrn on apv.code=lstrn.itemCode and apv.approvedVendorCode=lstrn.subcode \n "
                    + "         where apv.code=:itemCode  and (lstrn.rankNum=1 or lstrn.rankNum is null) \n ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", strItemCode);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new ApprovedVendors_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("approvedVendorCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("approvedVendorName")));
                node.setRate(DmServer.vNull(rsTemp.getString("lastRate"), "0.00"));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("sNo")));

                lstTemp.add(node);
            }

            psTemp.close();
            rsTemp.close();
            dmrTemp.setData(lstTemp);

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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplierPT(DmHelper pageing, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, filtration = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            filtration = " SELECT acf.Nature "
                    + " FROM AcFilteration acf "
                    + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_ORDER + "')";

            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , c.cityName,sg.guiConfigCode "
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
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

    public class ShortBySupplier implements Comparator<PurchaseOrderDisplay_Mdl> {

        @Override
        public int compare(PurchaseOrderDisplay_Mdl o1, PurchaseOrderDisplay_Mdl o2) {
            return o1.getLastOrderSupplier().compareTo(o2.getLastOrderSupplier());
        }

    }

    @Override
    public DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> getPrevGrn(String itemCode, DmHelper helper, boolean blnToEndOfFile, String param) {
        DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> dmrTemp = new DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>();
        List<GrdGrnPurchaseHistory_Mdl> lstTemp = new ArrayList<GrdGrnPurchaseHistory_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        GrdGrnPurchaseHistory_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "", supplierCondition = "";
        dmrTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Previous Grn detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   pc.pChallanDate AS refDate"
                    + " , pc.pChallanNo AS refNo"
                    + " , sgs.name AS supplierName,sgs.subcode "
                    + " , ct.cityName"
                    + " , um.unit AS purchaseUnitName"
                    + " , pcd.acceptQty"
                    + " , pcd.freeQty"
                    + " , pcd.rate"
                    + " , pcd.oh_At_Discount"
                    + " , pcd.saleRate"
                    + " , pcd.mrp"
                    + " , pcd.batchNo"
                    + " , pcd.expiryDate"
                    + " , ums.unit AS skuName"
                    + " , pcd.skuQty"
                    + " , pcd.skuRate"
                    + " , pcd.landedSkuValue"
                    + " , pcd.v_SNo"
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN subGroup sgs ON pc.subcode = sgs.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sgs.cityCode"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + " AND isNull(pc.approvedBy,'') <> ''"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + " AND pcd.itemCode = :itemCode" + supplierCondition + condition;
            sqlQuery += " UNION ALL ";
            sqlQuery += "SELECT"
                    + "   pc.partyBillDate AS refDate"
                    + " , pc.partyBillNo AS refNo"
                    + " , sgs.name AS supplierName,sgs.subcode "
                    + " , ct.cityName"
                    + " , um.unit AS purchaseUnitName"
                    + " , pcd.acceptQty"
                    + " , pcd.freeQty"
                    + " , pcd.rate"
                    + " , pcd.oh_At_Discount"
                    + " , pcd.saleRate"
                    + " , pcd.mrp"
                    + " , pcd.batchNo"
                    + " , pcd.expiryDate"
                    + " , ums.unit AS skuName"
                    + " , pcd.skuQty"
                    + " , pcd.skuRate"
                    + " , pcd.landedSkuValue"
                    + " , pcd.v_SNo"
                    + " FROM pBillDetail pcd"
                    + " LEFT JOIN pBill pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN subGroup sgs ON pc.subcode = sgs.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sgs.cityCode"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " AND isNull(pc.approvedBy,'') <> ''"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + " AND pcd.itemCode = :itemCode" + supplierCondition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("itemCode", DmShared.xNull(itemCode));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdGrnPurchaseHistory_Mdl();
                node.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                node.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                node.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                node.setSupplierCode(DmShared.xNull(rsTemp.getString("subCode")));
                if (!DmShared.xNull(rsTemp.getString("cityName")).trim().equals("")) {
                    node.setSupplierName(node.getSupplierName() + ", " + DmShared.xNull(rsTemp.getString("cityName")));
                }
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("PurchaseUnitName")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_Discount")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                if (DmShared.vNull(node.getRate()).doubleValue() > 0) {
                    node.setProfitPer(DmServer.vNull(((DmShared.vNull(node.getSaleRate()) - DmShared.vNull(node.getRate())) / DmShared.vNull(node.getRate())) * 100, "0.00"));
                }
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());

            cnTemp.close();
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
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    //</editor-fold>
}
