package aarogya.inventory.itemDepartmentInfo.server;

import dataman.dmBase.self.shared.*;
import aarogya.inventory.itemDepartmentInfo.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemDepartmentInfo_SrvImpl extends DmRSSPrivate implements ItemDepartmentInfo_Srv {

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
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT concat(idi.site_Code,'|', idi.departmentCode) as code "
                    + " FROM itemDepartmentInfo idi"
                    + " LEFT JOIN itemMast im ON im.code = idi.itemCode   "
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) "
                    + " group By idi.site_Code, idi.departmentCode ");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResult<ItemDepartmentInfo_Mdl> getMoveRec(String strKey) {
        DmResult<ItemDepartmentInfo_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, department = "", site = "";
        String[] tmpKey;
        ItemDepartmentInfo_Mdl mrTemp = new ItemDepartmentInfo_Mdl();
        GrdItemDepartmentInfo_Mdl listNode;
        dmrTemp.setData(mrTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp.setSharedKeyFieldCode(DmShared.xNull(strKey));

            tmpKey = strKey.split("\\|");
            if (!DmShared.xNull(strKey).equalsIgnoreCase("")) {
                site = tmpKey[0];
                department = tmpKey[1];
            }
            sqlQuery = " Select idi.site_Code ,sm.name as siteName"
                    + ", idi.departmentCode , sg.name as departmentName, idi.itemCode  , im.name as itemName"
                    + ", idi.minStock  "
                    + ", idi.maxStock  "
                    + ", idi.reOrder  "
                    + ", idi.binName "
                    + ", idi.isHoldRequirement  , ump.decimalPlaces as decimalPlaces ,ump.unit as skuName from itemDepartmentInfo idi "
                    + " left join subGroup sg on sg.subCode = idi.departmentCode "
                    + " left join siteMast sm on sm.code = idi.site_Code"
                    + " left join itemMast im on im.code = idi.itemCode"
                    + "  LEFT JOIN unitMast ump ON ump.code = im.sku"
                    + " Where idi.site_Code= :siteCode and idi.departmentCode = :department"
                    + " and (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " ORDER BY im.name ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(site).toString());
            psTemp.setString("department", DmShared.iNull(department).toString());
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new GrdItemDepartmentInfo_Mdl();
                mrTemp.setDepartmentCode(DmShared.iNull(rsTemp.getString("departmentCode")).toString());
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setSiteCode(DmShared.iNull(rsTemp.getString("site_Code")).toString());
                mrTemp.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setMinStock(DmServer.vNull(rsTemp.getString("minStock")));
                listNode.setMaxStock(DmServer.vNull(rsTemp.getString("maxStock")));
                listNode.setReOrder(DmServer.vNull(rsTemp.getString("reOrder")));
                listNode.setBinName(DmShared.xNull(rsTemp.getString("binName")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setAllowDecimalPlace(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                mrTemp.getGrdMed_Mdl().add(listNode);
            }
            psTemp.close();
            rsTemp.close();
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
        String sqlQuery, department, site;
        ResultSet rsTemp = null;
        String[] tmpKey;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            tmpKey = strKey.split("\\|");
            site = tmpKey[0];
            department = tmpKey[1];

            sqlQuery = "DELETE idi "
                    + " FROM itemDepartmentInfo idi "
                    + " LEFT JOIN itemMast im ON im.code = idi.itemCode"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) "
                    + " AND idi.site_Code=:siteCode and idi.departmentCode= :departmentCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("siteCode", DmShared.iNull(site).toString());
            psTemp.setString("departmentCode", DmShared.iNull(department).toString());
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
    public DmResult<String> saveRecord(ItemDepartmentInfo_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterItemType = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ItemDepartmentInfo_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "DELETE idi "
                    + " FROM itemDepartmentInfo idi "
                    + " LEFT JOIN itemMast im ON im.code = idi.itemCode"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) "
                    + " AND idi.site_Code=:siteCode and idi.departmentCode= :departmentCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("siteCode", mrTemp.getSiteCode());
            psTemp.setString("departmentCode", mrTemp.getDepartmentCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdItemDepartmentInfo_Mdl med_Mdl : mrTemp.getGrdMed_Mdl()) {
                if (DmShared.iNull(med_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                    sqlQuery = "insert into itemDepartmentInfo(departmentCode,itemCode, minStock, maxStock, reOrder, binName,site_Code , isHoldRequirement)  "
                            + " VALUES (:departmentCode,:itemCode, :minStock, :maxStock, :reOrder,:binName, :siteCode , :isHoldRequirement) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(med_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("minStock", DmServer.vNull(med_Mdl.getMinStock()));
                    psTemp.setString("maxStock", DmServer.vNull(med_Mdl.getMaxStock()));
                    psTemp.setString("reOrder", DmServer.vNull(med_Mdl.getReOrder()));
                    psTemp.setString("binName", DmShared.xNull(med_Mdl.getBinName()));
                    psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSiteCode()));
                    psTemp.setString("isHoldRequirement", DmShared.iNull(med_Mdl.getIsHoldRequirement()).toString());

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getSiteCode() + "|" + mrTemp.getDepartmentCode());

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
            sqlWithParam.setSQL("SELECT concat(idi.site_Code,'|',idi.departmentCode) as code , max(sg.name)  as departmentName, max(sm.name) as siteName    "
                    + " FROM itemDepartmentInfo  as idi "
                    + " LEFT JOIN itemMast im ON im.code = idi.itemCode"
                    + " left join siteMast sm on sm.code = idi.site_Code "
                    + " left join subGroup sg on sg.subCode = idi.departmentCode "
                    + " where (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " group By idi.site_Code, idi.departmentCode ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code, name FROM siteMast ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlQuery = "select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PR') and sg.isActive = 1 " + condition;
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
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItemName(String siteCode, String department, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT mm.code"
                    + " ,mm.name as itemName"
                    + " , mm.manualCode, ump.decimalPlaces as decimalPlaces, mg.name as itemGroup,"
                    + " mc.name as itemCategory, idi.isHoldRequirement ,"
                    + " ump.unit as skuName "
                    + " FROM itemMast mm "
                    + " LEFT JOIN itemSiteList sl ON sl.code = mm.code"
                    + " LEFT JOIN itemGroup mg ON mg.code = mm.itemGroup "
                    + " LEFT JOIN itemCat mc ON mc.code = mm.itemCategoryCode "
                    + " LEFT JOIN ( "
                    + "                 SELECT irtd.code "
                    + "                 FROM itemRestrictedToDepartment irtd "
                    + "                 WHERE irtd.departmentCode = :department"
                    + "		) virtd ON virtd.code = mm.code "
                    + " left join ("
                    + "		select  idi.itemCode, max(idi.isHoldRequirement) as isHoldRequirement"
                    + "			from itemDepartmentInfo idi "
                    + "			where idi.departmentCode = :department and idi.site_Code = :siteCode "
                    + "			group by itemCode"
                    + "	) idi on idi.itemCode = mm.code"
                    + " LEFT JOIN unitMast ump ON ump.code = mm.sku "
                    + " WHERE (mm.filterItemType = :filterItemType OR mm.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " AND virtd.code IS NULL ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("siteCode", siteCode);
            sqlWithParam.setString("department", department);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setItemCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                listNode.setItemGroup(DmShared.xNull(rsTemp.getString("itemGroup")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setAllowDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                listNode.setIsHoldRequirement(DmShared.iNull(rsTemp.getString("isHoldRequirement")).toString());
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
