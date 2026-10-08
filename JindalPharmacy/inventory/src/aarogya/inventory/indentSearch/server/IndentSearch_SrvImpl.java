package aarogya.inventory.indentSearch.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.indentSearch.shared.GrdIndentDetailsSearch_Mdl;
import aarogya.inventory.indentSearch.shared.IndentSearchValue_Mdl;
import aarogya.inventory.indentSearch.shared.IndentSearch_Srv;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class IndentSearch_SrvImpl extends DmRSSPrivate implements IndentSearch_Srv {

    @Override
    public DmResultPageing<List<GrdIndentDetailsSearch_Mdl>> getIndentSearchDetail(String v_Type, ItemProperty_Mdl ipm, String v_Date, String voucherCategory, String supplierCode, IndentSearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdIndentDetailsSearch_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", join = "", sqlQuery, itemCatFilteration = "";
        List<GrdIndentDetailsSearch_Mdl> lstTemp = new ArrayList<GrdIndentDetailsSearch_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().INVENTORY_ENQUIRY)) {
                join = "         left join ("
                        + "             SELECT eid.indentDocId, eid.indentV_SNo "
                        + "             FROM enquiryIndentDetail eid "
                        + "             group by eid.indentDocId, eid.indentV_SNo "
                        + "         ) vEid on vEid.indentDocId = id.docid  and vEid.indentV_SNo = id.v_SNo ";

                condition += " and vEid.indentDocId is null ";

            } else if (voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().QUOTATION) || voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().QUOTATION_UNREGISTERED)) {
                if (!DmShared.xNull(supplierCode).trim().equalsIgnoreCase("")) {
                    join = " LEFT JOIN ( "
                            + "     select qid.indentDocId, qid.indentV_SNo "
                            + "     from quotation q "
                            + "     INNER JOIN quotationIndentDetail qid on qid.docId = q.docId "
                            + "     where q.subCode = " + supplierCode + " "
                            + " ) vq on vq.indentDocId=id.docId and vq.indentV_SNo=id.v_SNo ";
                    condition += " and vq.indentDocId IS NULL ";
                }
            }

            if (voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().INVENTORY_ENQUIRY)
                    || voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().QUOTATION)
                    || voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().QUOTATION_UNREGISTERED)) {
                condition += " and IsNull(id.orderedQty,0) = 0 ";
            }

            if (!voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().SERVICE_ORDER)
                    && !voucherCategory.equalsIgnoreCase(new ProjectVoucherCategory().SERVICE_ORDER_UNREGISTERED)) {
                condition += " and vt.category = '" + new ProjectVoucherCategory().INVENTORY_INDENT + "' ";
            } else {/*++++++++++++++++++++++++++Indents are not required in Service Order++++++++++++++++++++++++++*/
                condition += " and 1=2 ";
            }

            condition += " and id.isStatus = 0 and isNull(i.approvedBy,'') <> ''  ";

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            /*=================================  Set Conditions ===============================================*/
            if (DmShared.iNull(ipm.getItemCode()).longValue() != 0) {
                condition += " and id.itemCode =:itemCode ";
            }
            if (DmShared.iNull(ipm.getColourCode()).intValue() != 0) {
                condition += " and id.colourCode =:colourCode ";
            }
            if (DmShared.iNull(ipm.getSizeCode()).intValue() != 0) {
                condition += " and id.sizeCode =:sizeCode ";
            }
            if (DmShared.iNull(ipm.getItemMakeCode()).intValue() != 0) {
                condition += " and id.itemMakeCode =:itemMakeCode ";
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND i.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }
            if (!DmShared.dNull(searchValueMdl.getFromDate()).equalsIgnoreCase("")) {
                condition += " and i.v_Date >= :fromDate ";
            }
            if (!DmShared.dNull(searchValueMdl.getToDate()).equalsIgnoreCase("")) {
                condition += " and i.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(searchValueMdl.getDepartmentTag()).equalsIgnoreCase("")) {
                condition += " and i.departmentCode in (" + DmServer.getParamList(":departmentCode", searchValueMdl.getDepartmentTag()) + " )";
            }
            if (!DmShared.xNull(searchValueMdl.getMarketingCompanyTag()).equalsIgnoreCase("")) {
                condition += " and im.marketingCompany in (" + DmServer.getParamList(":marketingCompany", searchValueMdl.getMarketingCompanyTag()) + " )";
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineCategoryTag()).equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode in (" + DmServer.getParamList(":itemCategoryCode", searchValueMdl.getMedicineCategoryTag()) + " )";
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineDivisionTag()).equalsIgnoreCase("")) {
                condition += " and im.divisionCode in (" + DmServer.getParamList(":divisionCode", searchValueMdl.getMedicineDivisionTag()) + " )";
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineGroupTag()).equalsIgnoreCase("")) {
                condition += " and im.itemGroup in (" + DmServer.getParamList(":itemGroup", searchValueMdl.getMedicineGroupTag()) + " )";
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineMakeTag()).equalsIgnoreCase("")) {
                condition += " and im.itemMakeCode in (" + DmServer.getParamList(":itemMakeCode", searchValueMdl.getMedicineMakeTag()) + " )";
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineTag()).equalsIgnoreCase("")) {
                condition += " and im.Code in (" + DmServer.getParamList(":Code", searchValueMdl.getMedicineTag()) + " )";
            }

            sqlQuery = "select  id.docId as indentDocId, id.v_SNo, i.recId as indentNo, i.v_Date as indentDate, i.departmentCode, sgd.name as departmentName, "
                    + "		id.itemCode, im.name as itemName, id.aprQty as quantity, id.reqDate, im.sku, um.unit, id.remark,"
                    + "         id.isStatus, i.approvedBy,id.colourCode,icm.name as colourName,id.itemMakeCode,"
                    + "         id.sizeCode as sizeCode,ism.name sizeName,im.propertyList, "
                    + "         imm.companyName as itemMakeName "
                    + "		from indent as i "
                    + "		left join indentDetail as id on i.docId = id.docId "
                    + "		left join subGroup as sgd on i.departmentCode = sgd.subCode "
                    + "		left join itemMast as im on id.itemCode = im.code "
                    + "         LEFT JOIN itemColourMast icm ON icm.code = id.colourCode "
                    + "         LEFT JOIN itemSizeMast ism ON ism.code = id.sizeCode "
                    + "         LEFT join voucher_Type as vt on vt.v_Type = i.V_type "
                    + "         LEFT join itemMakeMast as imm on imm.code = im.itemMakeCode "
                    + "		left join unitMast as um on im.sku = um.code "
                    + join
                    + "         where i.site_Code = :site_Code  "
                    + "" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (DmShared.iNull(ipm.getItemCode()).longValue() != 0) {
                sqlWithParam.setString("itemCode", DmShared.iNull(ipm.getItemCode()).toString());
            }
            if (DmShared.iNull(ipm.getColourCode()).intValue() != 0) {
                sqlWithParam.setString("colourCode", DmShared.iNull(ipm.getColourCode()).toString());
            }
            if (DmShared.iNull(ipm.getSizeCode()).intValue() != 0) {
                sqlWithParam.setString("sizeCode", DmShared.iNull(ipm.getSizeCode()).toString());
            }
            if (DmShared.iNull(ipm.getItemMakeCode()).intValue() != 0) {
                sqlWithParam.setString("itemMakeCode", DmShared.iNull(ipm.getItemMakeCode()).toString());
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            if (!DmShared.dNull(searchValueMdl.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromDate", DmServer.convertDate(searchValueMdl.getFromDate()));
            }
            if (!DmShared.dNull(searchValueMdl.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("toDate", DmServer.convertDate(searchValueMdl.getToDate()));
            }
            if (!DmShared.xNull(searchValueMdl.getDepartmentTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("departmentCode", DmShared.xNull(searchValueMdl.getDepartmentTag()));
            }
            if (!DmShared.xNull(searchValueMdl.getMarketingCompanyTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("marketingCompany", DmShared.xNull(searchValueMdl.getMarketingCompanyTag()));
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineCategoryTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemCategoryCode", DmShared.xNull(searchValueMdl.getMedicineCategoryTag()));
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineDivisionTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("divisionCode", DmShared.xNull(searchValueMdl.getMedicineDivisionTag()));
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineGroupTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemGroup", DmShared.xNull(searchValueMdl.getMedicineGroupTag()));
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineMakeTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemMakeCode", DmShared.xNull(searchValueMdl.getMedicineMakeTag()));
            }
            if (!DmShared.xNull(searchValueMdl.getMedicineTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("Code", DmShared.xNull(searchValueMdl.getMedicineTag()));
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdIndentDetailsSearch_Mdl objListTemp = new GrdIndentDetailsSearch_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("indentDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indentNo")));
                objListTemp.setVoucherSNo(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
                objListTemp.setIndentDate(DmShared.dNull(rsTemp.getDate("indentDate")));
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                objListTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setSku(DmShared.xNull(rsTemp.getString("unit")));
                objListTemp.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                objListTemp.setRequiredDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                objListTemp.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                ItemProperty_Mdl ipmTemp = new ItemProperty_Mdl();
                ipmTemp.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipmTemp.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipmTemp.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                objListTemp.setPropertyValueCsv(ipmTemp.getPropertiesCsv());

                objListTemp.setIsChecked("0");
                lstTemp.add(objListTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PR') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCategoryIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select imc.code , imc.name "
                    + " from itemcat imc"
                    + " where (filterItemType = :filterItemType OR imc.isCommon = 1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroupIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT ig.code, ig.name"
                    + " FROM itemGroup ig "
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMakeIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT CODE, companyName as name, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "   WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT mm.code, mm.name "
                    + " FROM itemMast mm"
                    + " LEFT JOIN itemSiteList sl ON sl.code = mm.code"
                    + " WHERE (mm.filterItemType = :filterItemType OR mm.isCommon = 1)"
                    + " AND mm.isActive = 1"
                    + " AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDivisionIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select sg.subCode as code,sg.name from subGroup as sg "
                    + " where filterNature = :filterNature ");
            sqlWithParam.setString("filterNature", DmServer.getCodeViaManualCode("DV", "manualCode", "subgroupNature", "code", cnTemp));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMarketingIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT imm.code, imm.companyName as name "
                    + " FROM itemMakeMast imm"
                    + " WHERE filterItemType = :filterItemType and entryType=:entryType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("entryType", DmShared.iNull(STEnum.ITEM_MAKE_ENTRY_TYPE.MARKETING.getId()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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
