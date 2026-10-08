package aarogya.selfToolkit.serviceGrid.server;

import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.ServiceEvalType;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.serviceGrid.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.TxtHServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Srv;

public class ServiceGrid_SrvImpl extends DmRSSPrivate implements ServiceGrid_Srv {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResult<List<GSTS_Mdl>> getITS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualITS) {
        DmResult<List<GSTS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp = STServer.getGSTS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, locationCode, gstTypeCode, isManualITS, cnTemp
            );
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<TxtHServiceDetail_Mdl>> getTxtHItemName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHServiceDetail_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHServiceDetail_Mdl> lstTemp = new ArrayList<>();
        TxtHServiceDetail_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            condition = " WHERE (im.filterItemType = :filterItemType ) AND (im.ItemType = :ItemType ) \n";
            condition += " AND im.isActive = 1 \n";
            condition += " AND sl.site_Code = :site_Code \n";
            condition += " AND im.smfCode IS NOT NULL \n";

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT \n"
                    + "   im.code \n"
                    + " , im.name \n"
                    + " , im.smfCode \n"
                    + " , im.serviceEvalTypeCode \n"
                    + " , im.serviceEvalRate \n"
                    + " , im.hsnCode \n"
                    + " , im.itemGSTNature \n"
                    + " , im.itsBasedOn \n"
                    + " , im.sku \n"
                    + " , im.manualCode \n"
                    + " , hm.hsn \n"
                    + " , um.gstUnitCode \n"
                    + " , im.subCode AS iSubCode \n"
                    + " FROM itemMast im \n"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code \n"
                    + " LEFT JOIN hsnMast hm ON hm.code=im.hsnCode \n"
                    + " LEFT JOIN unitMast um ON im.sku = um.code \n";
            sqlQuery += condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("OI", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("ItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHServiceDetail_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSmfCode(DmShared.xNull(rsTemp.getString("smfCode")));
                if (DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")).equalsIgnoreCase("")) {
                    listNode.setServiceEvalTypeCode(String.valueOf(ServiceEvalType.FREE_VALUE.getCode()));
                    listNode.setServiceEvalTypeName(ServiceEvalType.FREE_VALUE.getName());
                } else {
                    listNode.setServiceEvalTypeCode(DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")));
                    listNode.setServiceEvalTypeName(DmShared.xNull(DmEnum.ServiceEvalType.getViaCode(DmShared.iNull(rsTemp.getString("serviceEvalTypeCode")).intValue()).getName()));
                }
                listNode.setServiceEvalRate(DmShared.xNull(rsTemp.getString("serviceEvalRate")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setGstUnitCode(DmShared.iNull(rsTemp.getString("gstUnitCode")).toString());
                listNode.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT it.code, it.name, it.printSrNo\n"
                    + "FROM gstType it\n"
                    + "WHERE it.isActive = 1\n";
            sqlWithParam.setSQL(sqlQuery);
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default department >>===============================*/
            sqlQuery = "SELECT e.ac_RoundOffGST \n"
                    + "FROM Enviro e \n"
                    + "WHERE e.site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setAc_RoundOffGST(DmShared.xNull(rsTemp.getString("ac_RoundOffGST")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHValuationType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (DmEnum.ServiceEvalType nature : DmEnum.ServiceEvalType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(nature.getCode()).toString());
                listNode.setSharedKeyFieldName(nature.getName());
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

    
}
