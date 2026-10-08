package aarogya.selfToolkit.stockDetailPopup.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.stockDetailPopup.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;

public class StockDetailPopup_SrvImpl extends DmRSSPrivate implements StockDetailPopup_Srv {

    @Override
    public DmResult<List<StockDetailGrdEdit_Mdl>> getShowStock(String itemCode, ItemProperty_Mdl propertyPopup_Mdl) {
        DmResult<List<StockDetailGrdEdit_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        StockDetailGrdEdit_Mdl node;
        List<StockDetailGrdEdit_Mdl> lstTemp = new ArrayList<>();
        try {
            dmrTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " WHERE ibs.itemCode= :itemCode ";
            condition += " AND ibs.site_Code in (" + DmServer.getParamList(":site_Code", DmServer.getSsnSiteList(this)) + ") ";

            if (DmShared.iNull(propertyPopup_Mdl.getIbCode()).longValue() > 0) {
                condition += " AND ibs.ibCode = :ibCode ";
            } else {
                if (DmShared.iNull(propertyPopup_Mdl.getSizeCode()).longValue() > 0) {
                    condition += " AND ib.sizeCode = :sizeCode";
                }
                if (DmShared.iNull(propertyPopup_Mdl.getColourCode()).longValue() > 0) {
                    condition += " AND ib.colourCode = :colourCode";
                }
                if (DmShared.iNull(propertyPopup_Mdl.getItemMakeCode()).longValue() > 0) {
                    condition += " AND ib.itemMakeCode = :itemMakeCode";
                }
            }

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND ibs.departmentCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "Select max(im.name) as itemName, ibs.site_Code, max(sm.name) as siteName, ibs.departmentCode as godownCode, "
                    + " max(sg.name) as godown, im.sku, max(um.unit) as skuUnit, "
                    + " max(idi.minStock) as minStock, "
                    + " max(idi.maxStock) as maxStock,"
                    + " max(idi.reOrder) as reOrder,"
                    + " sum(ibs.balanceQty) as currentStock "
                    + " from itemBatchStock ibs "
                    + " LEFT JOIN itemBatch ib on ib.code=ibs.ibcode "
                    + " LEFT JOIN itemMast im on im.Code= ib.itemCode "
                    + " left join itemDepartmentInfo idi on idi.itemCode=im.code "
                    + "and idi.departmentCode = ibs.departmentCode "
                    + "and idi.site_Code= ibs.site_Code "
                    + " left join subGroup sg on sg.subCode=ibs.departmentCode "
                    + " left join siteMast sm on sm.code=ibs.site_Code "
                    + " left join unitMast um on um.code=im.sku "
                    + condition
                    + " group by ibs.itemCode,  ibs.site_Code,  ibs.departmentCode,  im.sku ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            psTemp.setStringList("site_Code", DmServer.getSsnSiteList(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                psTemp.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }

            if (DmShared.iNull(propertyPopup_Mdl.getIbCode()).longValue() > 0) {
                psTemp.setString("ibCode", DmShared.iNull(propertyPopup_Mdl.getIbCode()).toString());
            } else {
                if (DmShared.iNull(propertyPopup_Mdl.getSizeCode()).longValue() > 0) {
                    psTemp.setString("sizeCode", DmShared.iNull(propertyPopup_Mdl.getSizeCode()).toString());
                }
                if (DmShared.iNull(propertyPopup_Mdl.getColourCode()).longValue() > 0) {
                    psTemp.setString("colourCode", DmShared.iNull(propertyPopup_Mdl.getColourCode()).toString());
                }
                if (DmShared.iNull(propertyPopup_Mdl.getItemMakeCode()).longValue() > 0) {
                    psTemp.setString("itemMakeCode", DmShared.iNull(propertyPopup_Mdl.getItemMakeCode()).toString());
                }
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new StockDetailGrdEdit_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(itemCode));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setSite(DmShared.xNull(rsTemp.getString("siteName")));
                node.setGodown(DmShared.xNull(rsTemp.getString("godown")));
                node.setSku(DmShared.xNull(rsTemp.getString("skuUnit")));
                node.setMinLevel(DmServer.vNull(rsTemp.getString("minStock")));
                node.setMaxLevel(DmServer.vNull(rsTemp.getString("maxStock")));
                node.setReOrderLevel(DmServer.vNull(rsTemp.getString("reOrder")));
                node.setCurrentStock(DmServer.vNull(rsTemp.getString("currentStock")));
                lstTemp.add(node);
            }
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
                if (cnTemp != null) {
                    cnTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
