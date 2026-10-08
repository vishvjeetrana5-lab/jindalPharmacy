package aarogya.pharmacy.medicineModification.server;

import aarogya.pharmacy.medicineModification.shared.*;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineModification_SrvImpl extends DmRSSPrivate implements MedicineModification_Srv {

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
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select code from itemMast where filterItemType = :filterItemType order by name");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult< MedicineModification_Mdl> getMoveRec(String strKey) {
        DmResult< MedicineModification_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        MedicineModification_Mdl mrTemp = new MedicineModification_Mdl();
        GrdComposition_Mdl node;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  im.name as itemName, im.code, im.itemMakeCode, imm.companyName, im.itemCategoryCode, ic.name as itemCategory, "
                    + "         im.itemGroup, ig.name as groupName, imm.shortName, im.displayName, im.itemPacking "
                    + "		from itemMast im "
                    + "		left join itemMakeMast imm on imm.code = im.itemMakeCode "
                    + "		left join itemCat ic on ic.code = im.itemCategoryCode "
                    + "		left join itemGroup ig on ig.code = im.itemGroup "
                    + "		where im.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setItemPacking(DmShared.xNull(rsTemp.getString("itemPacking")));
                mrTemp.setCategoryTag(DmShared.xNull(rsTemp.getString("itemCategoryCode")));
                mrTemp.setCategoryText(DmShared.xNull(rsTemp.getString("itemCategory")));
                mrTemp.setCompanyTag(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                mrTemp.setCompanyText(DmShared.xNull(rsTemp.getString("companyName")));
                mrTemp.setCompanyShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setGroupTag(DmShared.xNull(rsTemp.getString("itemGroup")));
                mrTemp.setGroupText(DmShared.xNull(rsTemp.getString("groupName")));
            }
            psTemp.close();
            rsTemp.close();

            //==========================Composition grid=======================
            sqlQuery = "select  mc.sNo, mc.compositionCode, mcm.name as compositionName, mc.power, mc.isBase, mc.unitCode, "
                    + "         um.unit as unitText, im.name as itemName, mcm.restrictGroupCode, rgm.name as restrictedGroupName  "
                    + "         from medicineComposition as mc "
                    + "         left join itemMast im on mc.code = im.code  "
                    + "         left join unitmast um on mc.unitCode = um.code  "
                    + "         left join medicinecompomast mcm on mc.compositionCode = mcm.code  "
                    + "         left join restrictedGrpMast rgm on rgm.code =  mcm.restrictGroupCode "
                    + "         where mc.code = :code "
                    + "         order by sNo ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdComposition_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("compositionCode")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("compositionName")));
                node.setPower(DmServer.vNull(rsTemp.getString("power"), "0.00"));
                node.setBaseTag(DmShared.iNull(rsTemp.getString("isBase")).toString());
                node.setBaseText(DmShared.iNull(rsTemp.getString("isBase")).intValue() == 1 ? "Yes" : "No");
                node.setUnitTag(DmShared.iNull(rsTemp.getString("unitCode")).toString());
                node.setUnitText(DmShared.xNull(rsTemp.getString("unitText")));
                node.setV_SNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                node.setRestrictGroupCode(DmShared.xNull(rsTemp.getString("restrictGroupCode")).toString());
                node.setRestrictGroupName(DmShared.xNull(rsTemp.getString("restrictedGroupName")));
                mrTemp.getLstComposition().add(node);
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
    public DmResult<String> saveRecord(MedicineModification_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        int sNo = 0;
        try {
            MedicineModification_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "UPDATE itemmast SET "
                    + "  displayName = :displayName, "
                    + "  itemGroup = :itemGroup, "
                    + "  itemMakeCode = :itemMakeCode, "
                    + "  itemCategoryCode = :itemCategoryCode,  "
                    + "  itemPacking = :itemPacking, "
                    + "  u_Name = :userName, "
                    + "  u_EntDt = :uEntDt "
                    + " WHERE code = :code ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
            psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getGroupTag()));
            psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getCompanyTag()));
            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
            psTemp.setString("itemPacking", DmShared.xNull(mrTemp.getItemPacking()));
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "UPDATE itemMast "
                    + " SET itemMast.name = vItem.itemName "
                    + " FROM ( "
                    + " 	SELECT im.code as itemCode "
                    + " 	, im.displayName + '-' + ic.name + '-' + comp.shortName + ' (Packing : ' + im.itemPacking + ')' AS itemName "
                    + " 	FROM itemMast im  "
                    + " 	INNER JOIN itemCat ic ON im.itemCategoryCode = ic.code "
                    + "         INNER JOIN itemmakemast AS comp ON im.itemMakeCode = comp.code  "
                    + " 	WHERE im.code = :code "
                    + " 	) vItem "
                    + " WHERE itemMast.code = vItem.itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            //=============================Composition Grid Entry =================================
            sqlQuery = " delete from medicineComposition where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sNo = 0;
            for (GrdComposition_Mdl grdComposition : mrTemp.getLstComposition()) {
                if (DmShared.iNull(grdComposition.getSharedKeyFieldCode()).longValue() > 0) {
                    sqlQuery = "INSERT INTO medicineComposition (code, sNo, compositionCode, power, isBase, unitCode) "
                            + "VALUES (:code, :sNo, :compositionCode, :power,:isBase,:unitCode);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setInt("sNo", ++sNo);
                    psTemp.setString("compositionCode", DmShared.foriegnKey(grdComposition.getSharedKeyFieldCode()));
                    psTemp.setString("power", DmServer.vNull(grdComposition.getPower()));
                    psTemp.setString("isBase", grdComposition.getBaseTag());
                    psTemp.setString("unitCode", DmShared.foriegnKey(grdComposition.getUnitTag()));
                    psTemp.executeUpdate();
                    psTemp.close();
                    if (DmShared.iNull(grdComposition.getBaseTag()).longValue() == 1) {
                        sqlQuery = "UPDATE itemMast "
                                + " set itemMast.compositionCode=:compositionCode "
                                + " where itemMast.code=:code";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("compositionCode", DmShared.foriegnKey(grdComposition.getSharedKeyFieldCode()));
                        psTemp.executeUpdate();
                        psTemp.close();

                    }
                }
            }

            //<editor-fold defaultstate="collapsed" desc="Update Item Mast">
            sqlQuery = "update itemMast set "
                    + " itemMast.restrictGroup = vMcm.restrictGroupCode "
                    + " from ( "
                    + "     select im.code as itemCode, max(mcm.restrictGroupCode) as restrictGroupCode "
                    + "     from itemMast im "
                    + "     left join medicineComposition mc on mc.code=im.code "
                    + "     left join medicineCompoMast mcm on mcm.code=mc.compositionCode "
                    + "     left join restrictedGrpMast rgm on rgm.code=mcm.restrictGroupCode "
                    + "     where im.code=:code "
                    + "     group by im.code "
                    + "	) vMcm "
                    + " where itemMast.code = vMcm.itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Chk of wrong restrict group update">
            sqlQuery = "select  count(*) cnt, im.code, max(im.name) "
                    + "         from itemMast im "
                    + "         left join medicineComposition mc on mc.code=im.code "
                    + "         left join medicineCompoMast mcm on mcm.code=mc.compositionCode "
                    + "         where im.code = :code "
                    + "         group by im.code "
                    + "         having sum(case when isnull(mcm.restrictGroupCode,isnull(im.restrictGroup,0)) = isnull(im.restrictGroup,0) then 1 else 0 end)  <> COUNT(im.code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Restricted Group changed for compositions in composition Master");
                }
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            if (mrTemp.getModifyReason() != null) {
                ModifyReason_Server.save(mrTemp.getModifyReason(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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
            sqlWithParam.setSQL(" SELECT im.code, im.name , mg.name AS groupName, um.unit AS sku,  "
                    + " comp.companyName, im.isActive,  mcm.name AS compositionName, "
                    + "  mc.name AS categoryName, rgm.name AS resName,it.code as typeCode,it.name as typename,im.itemPacking ,im.manualCode,"
                    + "  case im.isActive when 0  then 'InActive' when 1 then 'Active' when 2 then 'Hold' end  as activeText,  "
                    + "  CAST(im.u_EntDt as date) as modifiedDate "
                    + "  FROM itemmast AS im "
                    + "  LEFT JOIN itemCat AS mc ON im.itemCategoryCode = mc.code "
                    + "  LEFT JOIN itemGroup AS mg ON im.itemGroup = mg.code "
                    + "  LEFT JOIN itemMakemast AS comp ON im.itemMakeCode = comp.code "
                    + "  LEFT JOIN medicinecompomast AS mcm ON im.compositionCode = mcm.code "
                    + "  LEFT JOIN restrictedgrpmast AS rgm ON im.restrictGroup = rgm.code "
                    + "  LEFT JOIN itemType AS it ON im.itemType = it.code  "
                    + "  LEFT JOIN unitmast AS um ON im.sku = um.code "
                    + " WHERE  im.filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setActive(DmShared.xNull(rsTemp.getString("activeText")));
                listNode.setModifiedOn(DmShared.dNull(rsTemp.getDate("modifiedDate")));

                listNode.setCategory(DmShared.xNull(rsTemp.getString("categoryName")));
                listNode.setCompany(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setComposition(DmShared.xNull(rsTemp.getString("compositionName")));
                listNode.setMedGroup(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setRestrictGroup(DmShared.xNull(rsTemp.getString("resName")));
                listNode.setSku(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setMedicinePacking(DmShared.xNull(rsTemp.getString("itemPacking")));
                listNode.setMedicineTypeTag(DmShared.xNull(rsTemp.getString("typeCode")));
                listNode.setMedicineTypeText(DmShared.xNull(rsTemp.getString("typename")));

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
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select imc.code as categoryCode, imc.name as categoryName "
                    + " from itemcat imc"
                    + " where (filterItemType = :filterItemType OR imc.isCommon = 1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("categoryName")));

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
    public DmResultPageing<List<TxtHMedGrp_Mdl>> getTxtHMedGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedGrp_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedGrp_Mdl> lstTemp = new ArrayList<>();
        TxtHMedGrp_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT ig.code, ig.name, ig.saleDiscount "
                    + " FROM itemGroup ig "
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedGrp_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setSaleDisPer(DmShared.iNull(rsTemp.getString("saleDiscount")).toString());
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
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT CODE, companyName, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "   WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPurchaseDisPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer"), "0.00"));
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
    public DmResultPageing<List<TxtHComposition_Mdl>> getTxtHComposition(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHComposition_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHComposition_Mdl> lstTemp = new ArrayList<>();
        TxtHComposition_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT "
                    + "     mcm.CODE "
                    + "   , mcm.NAME "
                    + "   , rgm.code as restrictGroupCode  "
                    + "   , rgm.name as restrictGroupName  "
                    + "     FROM medicinecompomast mcm "
                    + "     LEFT JOIN restrictedgrpmast rgm ON rgm.code=mcm.restrictGroupCode "
                    + "     WHERE mcm.isActive = 1 "
                    + "     AND ISNULL(mcm.nature,0) = 1");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHComposition_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setRestrictGroupCode(DmShared.xNull(rsTemp.getString("restrictGroupCode")));
                listNode.setRestrictGroupName(DmShared.xNull(rsTemp.getString("restrictGroupName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            if (isActiveCall) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("Inactive");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Active");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("2");
                listNode.setSharedKeyFieldName("Hold");
                lstTemp.add(listNode);
            } else {

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("No");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Yes");
                lstTemp.add(listNode);
            }
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<Sku_Mdl>> getSKUDetails(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Sku_Mdl>> rstTemp = new DmResultPageing<>();
        List<Sku_Mdl> lstTemp = new ArrayList<>();
        Sku_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select code, unit, decimalPlaces from unitmast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Sku_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setAllowedDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT  "
                    + "    env.alphaCase_Medicine "
                    + "  FROM enviro as env "
                    + "  WHERE env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setMedicineNameAlphabetCase(DmShared.xNull(rsTemp.getString("alphaCase_Medicine")));
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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
    public DmResultPageing<List<String>> getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT NAME FROM itemMast  ");
            helper.addSort("NAME", DmEnum.DmSortType.ASC);
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
}
