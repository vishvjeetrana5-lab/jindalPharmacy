package aarogya.pharmacy.medicineIndentDisplay.server;

import aarogya.pharmacy.medicineIndentDisplay.shared.MedicineIndentDisplay_Mdl;
import aarogya.pharmacy.medicineIndentDisplay.shared.SearchValue_Mdl;
import aarogya.pharmacy.medicineIndentDisplay.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
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
import aarogya.pharmacy.medicineIndentDisplay.shared.MedicineIndentDisplay_Srv;
import java.util.Calendar;

public class MedicineIndentDisplay_SrvImpl extends DmRSSPrivate implements MedicineIndentDisplay_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<List<MedicineIndentDisplay_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl) {
        DmResultPageing<List<MedicineIndentDisplay_Mdl>> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", conditionItmMst = "";
        MedicineIndentDisplay_Mdl node;
        List<MedicineIndentDisplay_Mdl> lstTemp = new ArrayList<>();
                        Integer intForDays=0;
                        long intDateDiff=0;
        
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            
            if (!DmShared.xNull(searchValueMdl.getDepartmentCode()).trim().equals("")){
                condition+=" AND  trn.departmentCode IN (" + DmServer.getParamList(":departmentCode", searchValueMdl.getDepartmentCode()) + " ) ";
            } else if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).trim().equalsIgnoreCase("")) {
                condition += "and trn.departmentCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
//                conditionItmStk = "and departmentCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
            }
            
            if (!DmShared.xNull(searchValueMdl.getCompanyCode()).trim().equals("")) {
                conditionItmMst += " AND im.itemmakeCode IN (" + DmServer.getParamList(":medMake", searchValueMdl.getCompanyCode()) + " )";
            }
            
            if (!DmShared.xNull(searchValueMdl.getGroupCode()).trim().equals("")) {
                conditionItmMst += " AND im.itemGroup IN (" + DmServer.getParamList(":medGroup", searchValueMdl.getGroupCode()) + " )";
            }
            
            if (!DmShared.xNull(searchValueMdl.getCategoryCode()).trim().equals("")) {
                conditionItmMst += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":medCat", searchValueMdl.getCategoryCode()) + " )";
            }
            
            if (!DmShared.xNull(searchValueMdl.getMedicineCode()).trim().equals("")) {
                conditionItmMst += " AND im.code IN (" + DmServer.getParamList(":medicineCode", searchValueMdl.getMedicineCode()) + " )";
            }
            intDateDiff=(DmShared.convertDate(searchValueMdl.getAsOnDate()).getTime()-DmShared.convertDate(searchValueMdl.getMovementFrom()).getTime())/(24*60*60*1000);
            intForDays= DmShared.iNull(searchValueMdl.getForDays()).intValue();
            
            sqlQuery = "select max(im.name) as itemName,max(dept.deptName) as deptName,trn.itemcode,trn.departmentcode, "
                    + "         sum(trn.requisitionToQty) as requisitionToQty,sum(trn.requisitionFromQty) requisitionFromQty, "
                    + "		sum(trn.pendingIndentQty) as pendingIndentQty,sum(trn.consumeQty) as consumeQty,sum(trn.currStk) as currStk, "
                    + "		max(im.unitDecimalPlaces) as unitDecimalPlaces,max(trn.minStock) as minStock,max(im.sku) as skuCode, "
                    + "		max(im.skuName) as skuName, "
                    + "		max(trn.minStock)+(sum(trn.requisitionToQty)+sum(trn.requisitionFromQty)-sum(trn.pendingIndentQty)-sum(trn.currStk))+ "
                    + "		((sum(trn.consumeQty)/"+intDateDiff+") *"+intForDays+") as NetReqQty "
                    + "		from ("
                    + "               SELECT rq.departmentToCode as departmentCode ,rqd.itemCode,  rqd.reqQty-isNUll(rqd.indentQty,0) as requisitionToQty, "
                    + "                     0 as requisitionFromQty,0 as pendingIndentQty,0 as consumeQty,0 as currStk,0 as minStock "
                    + "                     FROM requisition as rq  "
                    + "                     left join requisitionDetail rqd on rq.docid=rqd.docid "
                    + "                     WHERE  rqd.isStatus=0 and rqd.reqQty-isNUll(rqd.indentQty,0)>0  and  rq.site_Code=:site_Code "
                    + "               union All		"
                    + "               SELECT rq.departmentCode ,rqd.itemCode,  0 as requisitionToQty,rqd.reqQty-isNUll(rqd.indentQty,0) as requisitionFromQty, "
                    + "                     0 as pendingIndentQty,0 as consumeQty,0 as currStk ,0 as minStock "
                    + "                     FROM requisition as rq  "
                    + "                     left join requisitionDetail rqd on rq.docid=rqd.docid "
                    + "                     WHERE rqd.isStatus=0 and rqd.reqQty-isNUll(rqd.indentQty,0)>0  and  rq.site_Code=:site_Code "
                    + "               union All		"
                    + "		      SELECT rq.departmentCode ,rqd.itemCode,  0 as requisitionToQty,0 as requisitionFromQty, "
                    + "                     isnull(rqd.aprQty,0)-isNUll(rqd.orderedQty,0) as pendingIndentQty,0 as consumeQty,0 as currStk,0 as minStock "
                    + "                     FROM indent as rq "
                    + "                     left join indentDetail rqd on rq.docid=rqd.docid "
                    + "                     WHERE rqd.isStatus=0 and isnull(rqd.aprQty,0)-isNUll(rqd.orderedQty,0)>0  and  rq.site_Code=:site_Code "
                    + "               union All		"
                    + "               select rq.departmentCode,rq.itemCode,  0 as requisitionToQty,0 as requisitionFromQty, "
                    + "                     0 as pendingIndentQty,isuQty as consumeQty,0 as currStk,0 as minStock "
                    + "                     from stock rq  "
                    + "                     left join voucher_Type vt on rq.v_Type=vt.v_Type " 
                    + "                     where rq.v_Date between :movementFrom and :asOnDate and isnull(rq.isuQty,0)<>0 and  rq.site_Code=:site_Code  "
                    + "               union All		"
                    + "               select rq.departmentCode,rq.itemCode,0 as requisitionToQty,0 as requisitionFromQty,0 as pendingIndentQty, "
                    + "               0 as consumeQty,rq.balanceQty as currStk,0 as minStock "
                    + "                 from itemStock rq   "
                    + "                 where rq.site_Code=:site_Code "
                    + "               union All		"
                    + "               select idi.departmentCode,rq.code as itemCode,0 as requisitionToQty,0 as requisitionFromQty,0 as pendingIndentQty,  "
                    + "               0 as consumeQty,0 as currStk , isnull(idi.minStock,0) as minStock "
                    + "                 from itemMast rq   "
                    + "                 left join itemDepartmentInfo idi on rq.code=idi.itemcode  "
                    + "	        ) as trn "
                    + "		left Join ( "
                    + "                 select im.code,im.Name,um.decimalPlaces as unitDecimalPlaces,um.unit as skuName,im.sku,im.itemMakeCode, "
                    + "                 im.itemGroup,im.itemCategoryCode "
                    + "                     from itemmast im "
                    + "                     left join unitMast um on im.sku=um.code "
                    + "                     where im.isActive=1 "
                    + "                     ) as im on trn.itemCode=im.code  "
                    + "         left Join ( "
                    + "                     select sg.name as deptName,sg.subcode from subgroup sg "
                    + "                     ) as dept on trn.departmentCode=dept.subCode "
                    + "         where isnull(im.code,'')<>'' "+conditionItmMst+condition
                    + "         group by trn.itemcode,trn.departmentcode "
                    + "         having round(max(trn.minStock)+(sum(trn.requisitionToQty)+sum(trn.requisitionFromQty)-sum(trn.pendingIndentQty)-sum(trn.currStk))+ "
                    + "		((sum(trn.consumeQty)/"+intDateDiff+") *"+intForDays+"),max(im.unitDecimalPlaces))>0 "
                    + "		order by itemName,deptName";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            
            if (!DmShared.dNull(searchValueMdl.getAsOnDate()).trim().equals("")) {
                psTemp.setDate("asOnDate", DmServer.convertDate(searchValueMdl.getAsOnDate()));
            }
            
            if (!DmShared.dNull(searchValueMdl.getMovementFrom()).trim().equals("")) {
                psTemp.setDate("movementFrom", DmServer.convertDate(searchValueMdl.getMovementFrom()));
            }
            
            if (!DmShared.xNull(searchValueMdl.getDepartmentCode()).trim().equals("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(DmShared.xNull(searchValueMdl.getDepartmentCode())));
            }
            
            if (!DmShared.xNull(searchValueMdl.getCompanyCode()).trim().equals("")) {
                psTemp.setStringList("medMake",DmShared.xNull(DmShared.xNull(searchValueMdl.getCompanyCode())) );
            }
            
            if (!DmShared.xNull(searchValueMdl.getGroupCode()).trim().equals("")) {
                psTemp.setStringList("medGroup",DmShared.xNull(DmShared.xNull(searchValueMdl.getGroupCode())) );
            }
            
            if (!DmShared.xNull(searchValueMdl.getCategoryCode()).trim().equals("")) {
                psTemp.setStringList("medCat",DmShared.xNull(DmShared.xNull(searchValueMdl.getCategoryCode())) );
            }
            
            if (!DmShared.xNull(searchValueMdl.getMedicineCode()).trim().equals("")) {
                psTemp.setStringList("medicineCode",DmShared.xNull(DmShared.xNull(searchValueMdl.getMedicineCode())) );
            }
            
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new MedicineIndentDisplay_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("deptName")));
                node.setCurrentStk(DmServer.vNull(rsTemp.getString("currStk"), "0.00"));
                node.setRequisitionToDeptQty(DmServer.vNull(rsTemp.getString("requisitionToQty"), "0.00"));
                node.setRequisitionFromDeptQty(DmServer.vNull(rsTemp.getString("requisitionFromQty"), "0.00"));
                node.setPendingIndentQty(DmServer.vNull(rsTemp.getString("pendingIndentQty"), "0.00"));
                node.setMinQty(DmServer.vNull(rsTemp.getString("minStock"), "0.00"));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setNetReqQty(DmServer.vNull(rsTemp.getString("NetReqQty"), "0.00"));
                node.setUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("unitDecimalPlaces")));
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
    public DmResult<MedicineIndentDisplay_Mdl> getRequisitionDetail(String department) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
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
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
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
}
