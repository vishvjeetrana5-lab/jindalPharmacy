package aarogya.inventory.itemGroupMaster.server;

import aarogya.inventory.itemGroupMaster.shared.Find_Mdl;
import aarogya.inventory.itemGroupMaster.shared.GrdDepartmentConsumptionAc_Mdl;
import aarogya.inventory.itemGroupMaster.shared.GrdRestrictedDept_Mdl;
import aarogya.inventory.itemGroupMaster.shared.ItemGroupMaster_Mdl;
import aarogya.inventory.itemGroupMaster.shared.ItemGroupMaster_Srv;
import aarogya.inventory.itemGroupMaster.shared.ItemGroupMaster_Vld;
import aarogya.inventory.itemGroupMaster.shared.TxtHGroup_Mdl;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stImportData.server.ItemGroup;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemGroupMaster_SrvImpl extends DmRSSPrivate implements ItemGroupMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT CODE FROM itemGroup WHERE filterItemType = :filterItemType ORDER BY NAME ");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<String>> getSuggestionGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Name From itemGroup");
            helper.addSort("Name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResult<ItemGroupMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ItemGroupMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ItemGroupMaster_Mdl mrTemp = new ItemGroupMaster_Mdl();
        dmrTemp.setData(mrTemp);

        List<GrdRestrictedDept_Mdl> listRestrictedDept = new ArrayList<>();
        GrdRestrictedDept_Mdl RestrictedDept;
        List<GrdDepartmentConsumptionAc_Mdl> listDeptWiseConsumptionAc = new ArrayList<>();
        GrdDepartmentConsumptionAc_Mdl DeptWiseConsumptionnAc;
        
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select ig.code, ig.name,ig.itemCatgory,ig.GroupUnder, ig.preparedBy, ig.u_name,igu.name as underGroupName ,ic.name as underCategoryName,"
                    + " ig.purchaseAc AS purchaseAcCode ,ig.purchaseReturnAc AS purchaseReturnAcCode ,ig.salesAc as salesAcCode, ig.salesReturnAc as salesReturnAcCode,\n"
                    + " ig.subCode as inventoryAc,sgp.name as purchaseAcName, sgpr.name as purchaseReturnAcName,sgs.name as salesAcName, sgsr.name as salesReturnAcName,sgi.name as inventoryAcName,\n"
                    + " ig.grIrAc AS grIrAcCode ,ig.consumptionAc AS consumptionAcCode ,ig.cogsAc as cogsAcCode, ig.pidAc as pidAcCode,ig.inTransitAc as inTransitAcCode,\n"
                    + " sgIrGr.name as irGrAcName, sgC.name as consumptionAcName,sgCOG.name as cogsAcName, sgPID.name as pidAcName,sgTrn.name as inTransitAcName \n"
                    + " From itemgroup  as ig "
                    + " left join itemtype as it on ig.filterItemType=it.code "
                    + " Left Join itemGroup igu On ig.GroupUnder = igu.code "
                    + " Left Join itemcat ic On ic.code = ig.itemCatgory "
                    + " left join subgroup AS sgp ON ig.purchaseAc = sgp.subCode"
                    + " left join subgroup AS sgpr ON ig.purchaseReturnAc = sgpr.subCode"
                    + " left join subgroup AS sgs ON ig.salesAc = sgs.subCode"
                    + " left join subgroup AS sgsr ON ig.salesReturnAc = sgsr.subCode"
                    + " left join subgroup AS sgi ON ig.subCode = sgi.subCode"
                    + " left join subgroup AS sgIrGr ON ig.grIrAc = sgIrGr.subCode"
                    + " left join subgroup AS sgC ON ig.consumptionAc = sgC.subCode"
                    + " left join subgroup AS sgCOG ON ig.cogsAc = sgCOG.subCode"
                    + " left join subgroup AS sgPID ON ig.pidAc = sgPID.subCode"
                    + " left join subgroup AS sgTrn ON ig.inTransitAc = sgTrn.subCode"
                    
                    + " Where ig.code= :code and ig.filterItemType = :filterItemType ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setUnderCateCode(DmShared.xNull(rsTemp.getString("itemCatgory")));
                mrTemp.setUnderCateName(DmShared.xNull(rsTemp.getString("underCategoryName")));
                mrTemp.setGroupUnder(DmShared.xNull(rsTemp.getString("GroupUnder")));
                mrTemp.setInventoryAcTag(DmShared.xNull(rsTemp.getString("inventoryAc")));
                mrTemp.setInventoryAcText(DmShared.xNull(rsTemp.getString("inventoryAcName")));
                mrTemp.setPurchaseAcTag(DmShared.xNull(rsTemp.getString("purchaseAcCode")));
                mrTemp.setPurchaseAcText(DmShared.xNull(rsTemp.getString("purchaseAcName")));
                mrTemp.setSalesAcTag(DmShared.xNull(rsTemp.getString("salesAcCode")));
                mrTemp.setSalesAcText(DmShared.xNull(rsTemp.getString("salesAcName")));
                mrTemp.setPurchaseReturnAcTag(DmShared.xNull(rsTemp.getString("purchaseReturnAcCode")));
                mrTemp.setPurchaseReturnAcText(DmShared.xNull(rsTemp.getString("purchaseReturnAcName")));
                mrTemp.setSalesReturnAcTag(DmShared.xNull(rsTemp.getString("salesReturnAcCode")));
                mrTemp.setSalesReturnAcText(DmShared.xNull(rsTemp.getString("salesReturnAcName")));
                
                mrTemp.setGrIrAcTag(DmShared.xNull(rsTemp.getString("grIrAcCode")));
                mrTemp.setGrIrAcText(DmShared.xNull(rsTemp.getString("irGrAcName")));
                mrTemp.setConsumptionAcTag(DmShared.xNull(rsTemp.getString("consumptionAcCode")));
                mrTemp.setConsumptionAcText(DmShared.xNull(rsTemp.getString("consumptionAcName")));
                mrTemp.setCogsAcTag(DmShared.xNull(rsTemp.getString("cogsAcCode")));
                mrTemp.setCogsAcText(DmShared.xNull(rsTemp.getString("cogsAcName")));
                mrTemp.setPidAcTag(DmShared.xNull(rsTemp.getString("pidAcCode")));
                mrTemp.setPidAcText(DmShared.xNull(rsTemp.getString("pidAcName")));
                mrTemp.setInTransitAcTag(DmShared.xNull(rsTemp.getString("inTransitAcCode")));
                mrTemp.setInTransitAcText(DmShared.xNull(rsTemp.getString("inTransitAcName")));
                
                mrTemp.setGroupUnderName(DmShared.xNull(rsTemp.getString("underGroupName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
            }
            psTemp.close();
            rsTemp.close();
            
            //========================== Restricted Department grid=======================
            sqlQuery = "  select sNo,departmentCode,sg.name as departmentName "
                    + "     from itemGroupRestrictedToDepartment as rd "
                    + "     LEFT JOIN  subGroup AS sg ON rd.departmentCode = sg.subCode "
                    + "     where code = :code ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                RestrictedDept = new GrdRestrictedDept_Mdl();
                RestrictedDept.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("departmentCode")).toString());
                RestrictedDept.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")).toString());
                RestrictedDept.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                listRestrictedDept.add(RestrictedDept);

            }
            mrTemp.getGrdRestrictedDept_Mdls().addAll(listRestrictedDept);
            rsTemp.close();
            psTemp.close();
            
            //========================== Department Wise Consumption Ac grid=======================
            sqlQuery = "  select igdc.departmentCode,sg.name as departmentName,sgc.Name as consumptionAcName,igdc.consumptionAc "
                    + "     from itemGroupDepartmentConsumptionAc as igdc "
                    + "     LEFT JOIN  subGroup AS sg ON igdc.departmentCode = sg.subCode "
                    + "     LEFT JOIN  subGroup AS sgc ON igdc.consumptionAc = sgc.subCode "
                    + "     where code = :code ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                DeptWiseConsumptionnAc = new GrdDepartmentConsumptionAc_Mdl();
                DeptWiseConsumptionnAc.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("departmentCode")).toString());
                DeptWiseConsumptionnAc.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")).toString());
                DeptWiseConsumptionnAc.setConsumptionAcCode(DmShared.iNull(rsTemp.getString("consumptionAc")).toString());
                DeptWiseConsumptionnAc.setConsumptionAcName(DmShared.xNull(rsTemp.getString("consumptionAcName")).toString());
                listDeptWiseConsumptionAc.add(DeptWiseConsumptionnAc);

            }
            mrTemp.getGrdDepartmentConsumptionAc().addAll(listDeptWiseConsumptionAc);
            rsTemp.close();
            psTemp.close();
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From itemGroup Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From ItemGroupPath Where GroupCode = :groupcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();
            
            sqlQuery = "Delete From itemGroupDepartmentConsumptionAc Where Code = :groupcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From itemGroupRestrictedToDepartment Where Code = :groupcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();
            
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    public DmResult<String> saveRecord(ItemGroupMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null, psTemp2 = null, psTemp3 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery,sqlQryRestrictedDept="" ;
//        ResultSet rsTemp = null;
        ResultSet rsTemp = null;
        ResultSet rsTemp1 = null;
        ResultSet rsTemp2 = null;
        ResultSet rsTemp3 = null;
        int intSNo;

        dmrTemp.setData("");

        try {
            ItemGroupMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "itemGroup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            //* Fetching Group Path Of Particular Group Under For Later Use ********************
            sqlQuery = "Select GroupCode,SNo,GroupUnder From ItemGroupPath Where GroupCode = :groupCode Order By SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("groupCode", mrTemp.getGroupUnder());
            rsTemp1 = psTemp1.executeQuery();

            sqlQuery = "Delete From ItemGroupPath Where GroupCode = :groupCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "SELECT COUNT(name) AS cnt FROM itemGroup WHERE LOWER(name) = :name AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Item Group already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "Insert Into itemGroup(code ,name,filterItemType,itemcatgory ,GroupUnder , "
                        + " preparedBy, u_EntDt,purchaseAc,salesAc,subCode, purchaseReturnAc , salesReturnAc, "
                        + " grIrAc,consumptionAc,cogsAc,pidAc,inTransitAc) Values  "
                        + "(:code ,:name,:filterItemType,:itemcatgory ,:GroupUnder ,:preparedBy, :uEntDt "
                        + " ,:purchaseAc,:salesAc,:subCode, :purchaseReturnAc,:salesReturnAc "
                        + " ,:grIrAc,:consumptionAc,:cogsAc,:pidAc,:inTransitAc) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupUnder", DmShared.foriegnKey(mrTemp.getGroupUnder()));
                psTemp.setString("itemcatgory", DmShared.foriegnKey(mrTemp.getUnderCateCode()));
                psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));
                
                
                psTemp.setString("grIrAc", DmShared.foriegnKey(mrTemp.getGrIrAcTag()));
                psTemp.setString("consumptionAc", DmShared.foriegnKey(mrTemp.getConsumptionAcTag()));
                psTemp.setString("cogsAc", DmShared.foriegnKey(mrTemp.getCogsAcTag()));
                psTemp.setString("pidAc", DmShared.foriegnKey(mrTemp.getPidAcTag()));
                psTemp.setString("inTransitAc", DmShared.foriegnKey(mrTemp.getInTransitAcTag()));
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "Update itemGroup Set "
                        + " name = :name, "
                        + " itemcatgory = :itemcatgory, "
                        + " GroupUnder = :GroupUnder, "
                        + " filterItemType = :filterItemType, "
                        + " purchaseAc = :purchaseAc, "
                        + " salesAc = :salesAc, "
                        + " subCode = :subCode, "
                        + " purchaseReturnAc= :purchaseReturnAc, "
                        + " salesReturnAc = :salesReturnAc, "
                         + " grIrAc = :grIrAc, "
                        + " consumptionAc = :consumptionAc, "
                        + " cogsAc = :cogsAc, "
                        + " pidAc= :pidAc, "
                        + " inTransitAc = :inTransitAc, "
                         + " U_Name = :u_Name, "
                        + " U_EntDt = :uEntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupUnder", DmShared.foriegnKey(mrTemp.getGroupUnder()));
                psTemp.setString("itemcatgory", DmShared.foriegnKey(mrTemp.getUnderCateCode()));
                psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));
                
                psTemp.setString("grIrAc", DmShared.foriegnKey(mrTemp.getGrIrAcTag()));
                psTemp.setString("consumptionAc", DmShared.foriegnKey(mrTemp.getConsumptionAcTag()));
                psTemp.setString("cogsAc", DmShared.foriegnKey(mrTemp.getCogsAcTag()));
                psTemp.setString("pidAc", DmShared.foriegnKey(mrTemp.getPidAcTag()));
                psTemp.setString("inTransitAc", DmShared.foriegnKey(mrTemp.getInTransitAcTag()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            intSNo = 0;
            //* Inserting Path For Particular Group Under Which Is Already Fetched Above ************
            if (!DmShared.xNull(mrTemp.getGroupUnder()).equalsIgnoreCase("")) {
                while (rsTemp1.next()) {
                    intSNo++;
                    sqlQuery = "Insert Into ItemGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("groupUnder", rsTemp1.getString("GroupUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                //* Inserting Path For Particular Group Under *******************
                intSNo++;
                sqlQuery = "Insert Into ItemGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setInt("sno", intSNo);
                psTemp.setString("groupUnder", mrTemp.getGroupUnder());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp1.close();
            psTemp1.close();

            //* Updating Child Records Also [Begin] **********************************
            //* Fetching Group Path Of Particular Group For Later Use ****************
            sqlQuery = "Select GroupCode,SNo,GroupUnder From ItemGroupPath Where GroupCode = :groupCode Order By SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            rsTemp1 = psTemp1.executeQuery();

            //* Fetching All Child Groups Of Particular Record *********************
            sqlQuery = "Select GroupCode,SNo,GroupUnder From ItemGroupPath Where GroupUnder = :groupCode Order By SNo ";
            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp2.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            rsTemp2 = psTemp2.executeQuery();

            while (rsTemp2.next()) {
                //* Fetching Group Path Whose Level Is Greater Than Or Equal To *********************
                sqlQuery = "Select GroupCode,SNo,GroupUnder From ItemGroupPath Where GroupCode = :groupCode And SNo >= :sno Order By SNo ";
                psTemp3 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp3.setString("groupCode", rsTemp2.getString("GroupCode"));
                psTemp3.setInt("sno", rsTemp2.getInt("SNo"));
                rsTemp3 = psTemp3.executeQuery();

                //* Deleting Particular Group Path ***************************
                sqlQuery = "Delete From ItemGroupPath Where GroupCode = :groupCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("groupCode", rsTemp2.getString("GroupCode"));
                psTemp.executeUpdate();
                psTemp.close();
                
                //* Updating Group Path Of Child Record ****************************
                intSNo = 0;
                while (rsTemp1.next()) {
                    intSNo++;
                    sqlQuery = "Insert Into ItemGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("groupCode", rsTemp2.getString("GroupCode"));
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("groupUnder", rsTemp1.getString("GroupUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                while (rsTemp3.next()) {
                    intSNo++;
                    sqlQuery = "Insert Into ItemGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("groupCode", rsTemp2.getString("GroupCode"));
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("groupUnder", rsTemp3.getString("GroupUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
                psTemp3.close();
                rsTemp3.close();

            }
            psTemp1.close();
            rsTemp1.close();
            psTemp2.close();
            rsTemp2.close();

            //============================= Restricted department Entry =================================
            sqlQuery = " delete from itemGroupRestrictedToDepartment where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdRestrictedDept_Mdls() != null) {
                int sNo = 0;
                for (GrdRestrictedDept_Mdl grdRestrictedDept : mrTemp.getGrdRestrictedDept_Mdls()) {
                    if (DmShared.vNull(grdRestrictedDept.getSharedKeyFieldCode()).doubleValue() != 0) {
                        sNo++;
                        sqlQuery = "INSERT INTO itemGroupRestrictedToDepartment (code,sNo,departmentCode) "
                                + "VALUES (:code,:sNo,:departmentCode); ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", sNo);
                        psTemp.setString("departmentCode", DmShared.foriegnKey(grdRestrictedDept.getSharedKeyFieldCode()));

                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            //============================= Update Restricted department in Item MAster link with current Group Entry =================================
            //* Deletion & Insertion of Item of Current Group Restricted Department ****************************
                sqlQuery = "Delete From itemRestrictedToDepartment Where code in(select code from itemMast where itemGroup= :groupCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("groupCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();
                
                if (!DmShared.xNull(mrTemp.getGrdRestrictedDept_Mdls().get(0).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "insert into itemRestrictedToDepartment(code,sNo,departmentCode) \n"
                        + "select im.code,igrd.sNo,igrd.departmentCode  \n"
                        + "from itemGroup ig  \n"
                        + "left join itemMAst im on im.itemGroup=ig.code  \n"
                        + "left join itemGroupRestrictedToDepartment igrd on ig.code=igrd.code  \n"
                        + "where ig.code=:groupCode  and im.code is not Null \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("groupCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();
                }
                
//                //* Deletion & Insertion of Item having Group under Current Group Restricted Department ****************************
//                sqlQuery = "Delete From itemRestrictedToDepartment Where itemGroup in(select code from ItemGroupPath where groupUnder=:groupCode ) ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("groupCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                psTemp.executeUpdate();
//                psTemp.close();
//                
//                sqlQuery = "insert into itemRestrictedToDepartment(code,sNo,departmentCode) \n"
//                        + "select im.code,igrd.sNo,igrd.departmentCode  \n"
//                        + "from itemGroup ig  \n"
//                        + "left join itemMAst im on im.itemGroup=ig.code  \n"
//                        + "left join itemGroupRestrictedToDepartment igrd on ig.code=igrd.code  \n"
//                        + "where ig.code in(select code from ItemGroupPath where groupUnder=:groupCode ) and igrd.departmentCode is not null  \n";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("groupCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                psTemp.executeUpdate();
//                psTemp.close();


//============================= Restricted department Entry =================================
            sqlQuery = " delete from itemGroupDepartmentConsumptionAc where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdDepartmentConsumptionAc()!= null) {
                for (GrdDepartmentConsumptionAc_Mdl grdDeptWiseConsumptionAc : mrTemp.getGrdDepartmentConsumptionAc()) {
                    if (DmShared.vNull(grdDeptWiseConsumptionAc.getSharedKeyFieldCode()).doubleValue() != 0) {
                        sqlQuery = "INSERT INTO itemGroupDepartmentConsumptionAc (code,departmentCode,consumptionAc) "
                                + "VALUES (:code,:departmentCode,:consumptionAc); ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("departmentCode", DmShared.foriegnKey(grdDeptWiseConsumptionAc.getSharedKeyFieldCode()));
                        psTemp.setString("consumptionAc", DmShared.foriegnKey(grdDeptWiseConsumptionAc.getConsumptionAcCode()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select ig.code, ig.name,ig.itemCatgory,ig.GroupUnder,igu.name as underGroupName"
                    + " ,ic.name as underCategoryName from itemgroup ig\n"
                    + " Left Join itemGroup igu On ig.GroupUnder = igu.code  \n"
                    + " Left Join itemcat ic On ic.code = ig.itemCatgory  \n"
                    + " left join itemtype as it on ig.filterItemType=it.code  "
                    + " where ig.filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setGrpName(DmShared.xNull(rsTemp.getString("underGroupName")));
                listNode.setCatName(DmShared.xNull(rsTemp.getString("underCategoryName")));
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = ImportData_Server.getCSVFormats(new ItemGroup().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "Item Group Master";
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

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select ig.code, ig.name,ig.itemCatgory,ig.GroupUnder,igu.name as underGroupName "
                    + " ,ic.name as underCategoryName from itemgroup ig\n"
                    + " Left Join itemGroup igu On ig.GroupUnder = igu.code  \n"
                    + " Left Join itemcat ic On ic.code = ig.itemCatgory  \n"
                    + " left join itemtype as it on ig.filterItemType=it.code   "
                    + " WHERE ig.filterItemType = :filterItemType "
                    + " order by ig.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
//                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
//                lineData += commaDelimiter;

                ////
                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("underGroupName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("underCategoryName")) + "\"";
//                lineData += commaDelimiter;

                ///
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	pscm.code as categoryCode, pscm.name as categoryName "
                    + "		from itemCat as pscm ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("categoryName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<TxtHGroup_Mdl>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile,
            String strCode) {
        DmResultPageing<List<TxtHGroup_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGroup_Mdl> lstTemp = new ArrayList<>();
        TxtHGroup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT sg.code, sg.name ,sg.itemcatgory ,ic.name as itemcatgoryName FROM itemGroup sg "
                    + " left join itemcat ic on ic.code=sg.itemcatgory "
                    + " where sg.code <> :groupCode "
                    + " And sg.code Not In (Select GroupCode From ItemGroupPath Where GroupUnder = :groupCode) ");
            sqlWithParam.setString("groupCode", DmShared.iNull(strCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHGroup_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCategoryCode(DmShared.xNull(rsTemp.getString("itemcatgory")));
                listNode.setCategoryName(DmShared.xNull(rsTemp.getString("itemcatgoryName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCode(String subgroupNature, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String Condition = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                Condition = " and sg.nature = :nature ";
            }

            sqlWithParam.setSQL(" SELECT sg.subCode, sg.name, sg.manualCode "
                    + " FROM subGroup sg where sg.isActive = 1 "
                    + Condition);
            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("nature", DmShared.xNull(DmServer.getCodeViaManualCode(subgroupNature, "manualCode", "subGroupNature", "code", cnTemp)));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode, sg.name"
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','DP') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
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
}
