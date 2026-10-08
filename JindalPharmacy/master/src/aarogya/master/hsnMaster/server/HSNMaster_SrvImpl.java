package aarogya.master.hsnMaster.server;

import dataman.dmBase.self.shared.*;
import aarogya.master.hsnMaster.shared.*;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.ZipUtils;
import dataman.dmBase.self.shared.DmEnum.GSTNature;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HSNMaster_SrvImpl extends DmRSSPrivate implements HSNMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("Select code From hsnMast Order By name;");

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
    public DmResult<HSNMaster_Mdl> getMoveRec(String strKey) {
        DmResult<HSNMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(moveRec(strKey, cnTemp));
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

    private HSNMaster_Mdl moveRec(String searchCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        HSNMaster_Mdl mrTemp = new HSNMaster_Mdl();
        GrdRateITS_Mdl node;
        try {

            sqlQuery = "Select hm.code, hm.name,hm.hsn,hm.preparedBy ,hm.u_Name,hm.itsBasedOn,hm.nature,hm.itemGSTNature "
                    + " From hsnMast as hm "
                    + "Where hm.code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", searchCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setHSN(DmShared.xNull(rsTemp.getString("hsn")));
                mrTemp.setTaxBasedOnTag(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                mrTemp.setTaxBasedOnText(DmShared.xNull(rsTemp.getString("itsBasedOn")).equalsIgnoreCase("1") ? "Rate" : "Fixed");
                mrTemp.setNatureTag(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setNatureText(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("1") ? "Goods" : "Service");
                mrTemp.setGstNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                if (!DmShared.xNull(rsTemp.getString("itemGSTNature")).equalsIgnoreCase("")) {
                    mrTemp.setGstNatureText(GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature")).trim().toUpperCase()).getValue());
                }
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  hid.code, hid.v_SNo, hid.rateFrom, hid.rateUpto, hid.its_Code_Purchase, hid.its_Code_PurchaseRet, hid.its_Code_Sales, "
                    + "  	hid.its_Code_SalesRet, hid.purchaseAc, hid.purchaseReturnAc, hid.salesAc, hid.salesReturnAc, itsp.name as itsPurName, "
                    + "         itspr.name as itsPurRetName, itssl.name as issalename, itsslr.name as saleRetName, sgp.name as purAcName, "
                    + "         sgpr.name as purRetAcName, sgsl.name as saleAcName, sgslr.name as saleRetAcName "
                    + "         from hsnITSDetail hid "
                    + "  	Left Join its itsp on hid.its_Code_Purchase = itsp.Code "
                    + "  	Left Join its itspr on hid.its_Code_PurchaseRet = itspr.Code "
                    + "  	Left Join its itssl on hid.its_Code_Sales = itssl.Code "
                    + "  	Left Join its itsslr on hid.its_Code_SalesRet = itsslr.Code "
                    + "         left join subgroup sgp on hid.purchaseAc=sgp.subCode"
                    + "         left join subgroup sgpr on hid.purchaseReturnAc=sgpr.subCode"
                    + "         left join subgroup sgsl on hid.salesAc=sgsl.subCode"
                    + "         left join subgroup sgslr on hid.salesReturnAc=sgslr.subCode"
                    + "         where hid.code= :code "
                    + "         order by v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", searchCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(mrTemp.getTaxBasedOnTag()).equalsIgnoreCase("1")) {
                    node = new GrdRateITS_Mdl();
                    node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                    node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("v_SNo")));
                    node.setRateFrom(DmServer.vNull(rsTemp.getString("rateFrom"), "0.00"));
                    node.setRateUpto(DmServer.vNull(rsTemp.getString("rateUpto"), "0.00"));
                    node.setItsOnPurchaseReturnTag(DmShared.xNull(rsTemp.getString("its_Code_PurchaseRet")));
                    node.setItsOnPurchaseReturnText(DmShared.xNull(rsTemp.getString("itsPurRetName")));
                    node.setItsOnPurchaseTag(DmShared.xNull(rsTemp.getString("its_Code_Purchase")));
                    node.setItsOnPurchaseText(DmShared.xNull(rsTemp.getString("itsPurName")));
                    node.setItsOnSaleTag(DmShared.xNull(rsTemp.getString("its_Code_Sales")));
                    node.setItsOnSaleText(DmShared.xNull(rsTemp.getString("issalename")));

                    node.setItsOnSaleReturnTag(DmShared.xNull(rsTemp.getString("its_Code_SalesRet")));
                    node.setItsOnSaleReturnText(DmShared.xNull(rsTemp.getString("saleRetName")));

                    node.setAcPurchaseReturnTag(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));
                    node.setAcPurchaseReturnText(DmShared.xNull(rsTemp.getString("purRetAcName")));

                    node.setAcPurchaseTag(DmShared.xNull(rsTemp.getString("purchaseAc")));
                    node.setAcPurchaseText(DmShared.xNull(rsTemp.getString("purAcName")));

                    node.setAcSaleReturnTag(DmShared.xNull(rsTemp.getString("salesReturnAc")));
                    node.setAcSaleReturnText(DmShared.xNull(rsTemp.getString("saleRetAcName")));

                    node.setAcSaleTag(DmShared.xNull(rsTemp.getString("salesAc")));
                    node.setAcSaleText(DmShared.xNull(rsTemp.getString("saleAcName")));
                    mrTemp.getGrdRateLst().add(node);
                } else if (DmShared.xNull(mrTemp.getTaxBasedOnTag()).equalsIgnoreCase("0")) {

                    mrTemp.setItsOnPurchaseReturnTag(DmShared.xNull(rsTemp.getString("its_Code_PurchaseRet")));
                    mrTemp.setItsOnPurchaseReturnText(DmShared.xNull(rsTemp.getString("itsPurRetName")));

                    mrTemp.setItsOnPurchaseTag(DmShared.xNull(rsTemp.getString("its_Code_Purchase")));
                    mrTemp.setItsOnPurchaseText(DmShared.xNull(rsTemp.getString("itsPurName")));

                    mrTemp.setItsOnSaleTag(DmShared.xNull(rsTemp.getString("its_Code_Sales")));
                    mrTemp.setItsOnSaleText(DmShared.xNull(rsTemp.getString("issalename")));

                    mrTemp.setItsOnSaleReturnTag(DmShared.xNull(rsTemp.getString("its_Code_SalesRet")));
                    mrTemp.setItsOnSaleReturnText(DmShared.xNull(rsTemp.getString("saleRetName")));

                    mrTemp.setAcPurchaseReturnTag(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));
                    mrTemp.setAcPurchaseReturnText(DmShared.xNull(rsTemp.getString("purRetAcName")));

                    mrTemp.setAcPurchaseTag(DmShared.xNull(rsTemp.getString("purchaseAc")));
                    mrTemp.setAcPurchaseText(DmShared.xNull(rsTemp.getString("purAcName")));

                    mrTemp.setAcSaleReturnTag(DmShared.xNull(rsTemp.getString("salesReturnAc")));
                    mrTemp.setAcSaleReturnText(DmShared.xNull(rsTemp.getString("saleRetAcName")));

                    mrTemp.setAcSaleTag(DmShared.xNull(rsTemp.getString("salesAc")));
                    mrTemp.setAcSaleText(DmShared.xNull(rsTemp.getString("saleAcName")));
                }
            }

            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return mrTemp;
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

            sqlQuery = "Delete From hsnITSDetail Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From hsnMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            spBegin.commit();
            dmrTemp.setStatus(true);
            psTemp.close();
            cnTemp.close();
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
    public DmResult<String> saveRecord(HSNMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            HSNMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            sqlQuery = "select count(name) as cnt from hsnMast where lower(name) = :name and code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException(" Name already exists.");

            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "hsnMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            spBegin = new DmSavePoint(cnTemp);
            if (blnAdd) {
                sqlQuery = "Insert Into hsnMast(code, name, hsn, itsBasedOn, itemGSTNature, preparedBy, u_EntDt, nature) "
                        + " values (:code, :name, :hsn, :itsBasedOn, :itemGSTNature, :u_Name, :uEntDt, :nature)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("hsn", DmShared.iNull(mrTemp.getHSN()).toString());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("itsBasedOn", DmShared.xNull(mrTemp.getTaxBasedOnTag()));
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNatureTag()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "Update hsnMast Set "
                        + "name = :name, "
                        + "hsn = :hsn, "
                        + "itemGSTNature=:itemGSTNature,"
                        + "U_Name = :u_Name, "
                        + "nature = :nature, "
                        + "U_EntDt = :uEntDt "
                        + "Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("hsn", DmShared.iNull(mrTemp.getHSN()).toString());
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNatureTag()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From hsnITSDetail Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();

            int vSNo = 0;
            if (DmShared.xNull(mrTemp.getTaxBasedOnTag()).equalsIgnoreCase("1")) {
                for (GrdRateITS_Mdl grdRate : mrTemp.getGrdRateLst()) {
                    if (DmShared.vNull(grdRate.getRateFrom()).doubleValue() > 0 && DmShared.vNull(grdRate.getRateUpto()).doubleValue() > 0) {
                        grdRate.setSharedKeyFieldName(String.valueOf(++vSNo));
                        sqlQuery = "Insert Into hsnITSDetail(code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                                + " its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) Values  "
                                + "(:code ,:v_SNo,:rateFrom,:rateUpto,:its_Code_Purchase,:its_Code_PurchaseRet,"
                                + " :its_Code_Sales,:its_Code_SalesRet,:purchaseAc,:purchaseReturnAc,:salesAc,:salesReturnAc) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.iNull(grdRate.getSharedKeyFieldName()).toString());
                        psTemp.setString("rateFrom", DmServer.vNull(grdRate.getRateFrom(), "0.00"));
                        psTemp.setString("rateUpto", DmServer.vNull(grdRate.getRateUpto(), "0.00"));
                        psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(grdRate.getItsOnPurchaseTag()));
                        psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(grdRate.getItsOnPurchaseReturnTag()));
                        psTemp.setString("its_Code_Sales", DmShared.foriegnKey(grdRate.getItsOnSaleTag()));
                        psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(grdRate.getItsOnSaleReturnTag()));
                        psTemp.setString("purchaseAc", DmShared.foriegnKey(grdRate.getAcPurchaseTag()));
                        psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(grdRate.getAcPurchaseReturnTag()));
                        psTemp.setString("salesAc", DmShared.foriegnKey(grdRate.getAcSaleTag()));
                        psTemp.setString("salesReturnAc", DmShared.foriegnKey(grdRate.getAcSaleReturnTag()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            } else {
                mrTemp.setvSNo(String.valueOf(++vSNo));
                sqlQuery = "Insert Into hsnITSDetail(code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                        + " its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) Values  "
                        + "(:code ,:v_SNo,:rateFrom,:rateUpto,:its_Code_Purchase,:its_Code_PurchaseRet,"
                        + " :its_Code_Sales,:its_Code_SalesRet,:purchaseAc,:purchaseReturnAc,:salesAc,:salesReturnAc) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_SNo", DmShared.iNull(mrTemp.getvSNo()).toString());
                psTemp.setString("rateFrom", "0.00");
                psTemp.setString("rateUpto", "0.00");
                psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(mrTemp.getItsOnPurchaseTag()));
                psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(mrTemp.getItsOnPurchaseReturnTag()));
                psTemp.setString("its_Code_Sales", DmShared.foriegnKey(mrTemp.getItsOnSaleTag()));
                psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(mrTemp.getItsOnSaleReturnTag()));
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getAcPurchaseTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getAcPurchaseReturnTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getAcSaleTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getAcSaleReturnTag()));
                psTemp.executeUpdate();
                psTemp.close();

            }
            /*===========================< Update record of itemMast >===============================*/
            sqlQuery = "Update  itemmast  set "
                    + "         itemGSTNature = :itemGSTNature"
                    + "         where hsnCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of itemITS >===============================*/
//            sqlQuery = " delete from itemITSDetail where Code In (select code from itemMast where hsnCode=:hsnCode)  ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("hsnCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            sqlQuery = "select im.code from itemMast im where im.hsnCode=:hsnCode";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("hsnCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                sqlQuery = " insert into itemITSDetail (code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
//                        + "                  its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) "
//                        + "         		(select  :code,hid.v_SNo,hid.rateFrom,hid.rateUpto,hid.its_Code_Purchase,hid.its_Code_PurchaseRet,"
//                        + "                 hid.its_Code_Sales,hid.its_Code_SalesRet,hid.purchaseAc,hid.purchaseReturnAc,hid.salesAc,hid.salesReturnAc "
//                        + "         		from hsnITSDetail as hid where hid.code = :hsnCode) ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("hsnCode", mrTemp.getSharedKeyFieldCode());
//                psTemp.setString("code", DmShared.xNull(rsTemp.getString("code")));
//                psTemp.executeUpdate();
//                psTemp.close();
//
//            }
//            rsTemp.close();
//            psTemp.close();
            sqlQuery = "DELETE itemITSDetail ";
            sqlQuery += "FROM itemMast im ";
            sqlQuery += "INNER JOIN itemItsDetail iid ON iid.code = im.code ";
            sqlQuery += "WHERE im.hsnCode = :hsnCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("hsnCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "INSERT INTO itemITSDetail ";
            sqlQuery += "           (code ";
            sqlQuery += "           ,v_SNo ";
            sqlQuery += "           ,rateFrom ";
            sqlQuery += "           ,rateUpto ";
            sqlQuery += "           ,its_Code_Purchase ";
            sqlQuery += "           ,its_Code_PurchaseRet ";
            sqlQuery += "           ,its_Code_Sales ";
            sqlQuery += "           ,its_Code_SalesRet ";
            sqlQuery += "           ,purchaseAc ";
            sqlQuery += "           ,purchaseReturnAc ";
            sqlQuery += "           ,salesAc ";
            sqlQuery += "           ,salesReturnAc) ";
            sqlQuery += "(  SELECT im.code  ";
            sqlQuery += "      , hid.v_SNo ";
            sqlQuery += "      , hid.rateFrom ";
            sqlQuery += "      , hid.rateUpto ";
            sqlQuery += "      , hid.its_Code_Purchase ";
            sqlQuery += "      , hid.its_Code_PurchaseRet ";
            sqlQuery += "      , hid.its_Code_Sales ";
            sqlQuery += "      , hid.its_Code_SalesRet ";
            sqlQuery += "      , hid.purchaseAc ";
            sqlQuery += "      , hid.purchaseReturnAc ";
            sqlQuery += "      , hid.salesAc ";
            sqlQuery += "      , hid.salesReturnAc ";
            sqlQuery += "  FROM itemMast im ";
            sqlQuery += "  INNER JOIN hsnMast hm ON hm.code = im.hsnCode ";
            sqlQuery += "  LEFT JOIN hsnITSDetail hid ON hid.code = hm.code ";
            sqlQuery += "  WHERE im.hsnCode = :hsnCode ";
            sqlQuery += "  ) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("hsnCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
            sqlWithParam.setSQL(" SELECT hm.code, hm.name, hm.hsn, "
                    + "         case hm.nature   when '1' then 'Goods' else 'Service' end as nature, "
                    + "         case hm.itsBasedOn   when '1' then 'Rate' else 'Value' end as itsBasedOn "
                    + "         FROM   hsnMast as hm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHSNCode(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setTaxBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
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
    public DmResultPageing<List<String>> getSuggestionHSN(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select hsn From hsnMast ");
            helper.addSort("hsn", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select name From hsnMast ");
            helper.addSort("name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHITS(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select Code,Name  from its ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT dm.subcode AS code, dm.name FROM subgroup AS dm  ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Goods");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Service");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTaxBasedOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Fixed");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Rate");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGSTNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (GSTNature nature : GSTNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(nature.getId());
                listNode.setSharedKeyFieldName(nature.getValue());
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
    public DmResult<KeyFieldExtended> exportJson(String session, List<String> lstVoucherType) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String sessionId = "", fileName = "", directoryPath, fullFilePath = "", serverFileName = "";
        Connection cnTemp = null;
        HSNMaster_Mdl mrTemp = null;
        KeyFieldExtended field = null;
        File sFile;
        FileWriter writer;
        try {
            sessionId = session;
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "HSN";
            sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            for (String its : lstVoucherType) {
                mrTemp = moveRec(its, cnTemp);
                fileName = mrTemp.getSharedKeyFieldName().replaceAll(" ", "_");
                fileName = fileName.replaceAll("/", "_");
                fileName = fileName.replace('\\', '_');
                fileName = fileName.replaceAll(":", "_");
                fileName = fileName.replace('*', '_');
                fileName = fileName.replace('?', '_');
                fileName = fileName.replace('"', '_');
                fileName = fileName.replaceAll("<", "_");
                fileName = fileName.replaceAll(">", "_");
                fileName = fileName.replace('|', '_');
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
                serverFileName = fileName.replace(" ", "") + ".json";
                fullFilePath = directoryPath + File.separatorChar + serverFileName;
                Gson gson = new Gson();
                String json = gson.toJson(mrTemp);
                writer = new FileWriter(fullFilePath);
                writer.write(json);
                writer.close();
            }
            ZipUtils zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "HSN.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("HSN.zip");
            rstTemp.setStatus(true);
            rstTemp.setData(field);
        } catch (IOException e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;

    }

    @Override
    public DmResult<List<GrdExport_Mdl>> getHSN() {
        DmResult<List<GrdExport_Mdl>> rstTemp = new DmResult<>();
        List<GrdExport_Mdl> lstTemp = new ArrayList<>();
        GrdExport_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  code, name, hsn, "
                    + "         case itsBasedOn when 0 then 'Fixed' when 1 then 'Rate' end as itsBasedOn"
                    + "		from hsnMast"
                    + "		order by hsn";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdExport_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setChecked(false);
                lstTemp.add(node);
            }

            rstTemp.setData(lstTemp);
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
