package aarogya.selfToolkit.itemUpdatePopup.server;

import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateHistory_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdate_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemUpdateServer {

    public static void updateItem(ItemUpdate_Mdl iuMdl, Connection cnTemp, DmRSS dmrss) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<ItemUpdateHistory_Mdl> lstIuhMdl = new ArrayList<>();
        ItemUpdateHistory_Mdl iuLogMdl = null;
        boolean isCompanyUpdateRequierd = false;
        double companyPurchaseDiscountPer = 0;
        String sqlQuery = "";
        try {
            //<editor-fold defaultstate="collapsed" desc="Save Update Log">
            sqlQuery = "SELECT mrp, saleRate, purchaseRate, purchaseDiscountPer, schemeOnQty, schemeFreeQty"
                    + " FROM itemMast im "
                    + " WHERE im.code = :itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(iuMdl.getItemCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                iuLogMdl = new ItemUpdateHistory_Mdl();
                iuLogMdl.setId(DmShared.xNull(iuMdl.getId()));
                iuLogMdl.setDocId(DmShared.xNull(iuMdl.getDocId()));
                iuLogMdl.setV_SNo(DmShared.xNull(iuMdl.getV_SNo()));
                iuLogMdl.setV_Type(DmShared.xNull(iuMdl.getV_Type()));
                iuLogMdl.setV_No(DmShared.xNull(iuMdl.getV_No()));
                iuLogMdl.setV_Date(DmShared.xNull(iuMdl.getV_Date()));
                iuLogMdl.setRecId(DmShared.xNull(iuMdl.getRecId()));
                iuLogMdl.setSubcode(DmShared.xNull(iuMdl.getSubcode()));
                iuLogMdl.setItemCode(DmShared.xNull(iuMdl.getItemCode()));
                iuLogMdl.setUnitCode(DmShared.xNull(iuMdl.getUnitCode()));

                for (ItemUpdateField_Mdl iufMdl : iuMdl.getLstItemUpdateFields()) {
                    if ((DmShared.iNull(iufMdl.getSaveToItem()).intValue() == 1 || DmShared.iNull(iufMdl.getSaveToCompany()).intValue() == 1) && iufMdl.getField() != null) {
                        switch (iufMdl.getField()) {
                            case MRP:
                                iuLogMdl.setPreviousMrp(DmShared.xNull(rsTemp.getString("mrp")));
                                iuLogMdl.setCurrentMrp(DmShared.xNull(iufMdl.getFinalValue()));
                                break;
                            case SALE_RATE:
                                iuLogMdl.setPreviousSaleRate(DmShared.xNull(rsTemp.getString("saleRate")));
                                iuLogMdl.setCurrentSaleRate(DmShared.xNull(iufMdl.getFinalValue()));
                                break;
                            case PURCHASE_RATE:
                                iuLogMdl.setPreviousPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));
                                iuLogMdl.setCurrentPurchaseRate(DmShared.xNull(iufMdl.getFinalValue()));
                                break;
                            case SCHEME:
                                String[] finalScheme = DmShared.xNull(iufMdl.getFinalValue()).split("[+]");

                                iuLogMdl.setPreviousSchemeOnQty(DmShared.xNull(rsTemp.getString("schemeOnQty")));
                                iuLogMdl.setCurrentSchemeOnQty(DmShared.xNull(finalScheme[0]).trim());

                                iuLogMdl.setPreviousSchemeFreeQty(DmShared.xNull(rsTemp.getString("schemeFreeQty")));
                                iuLogMdl.setCurrentSchemeFreeQty(DmShared.xNull(finalScheme[1]).trim());
                                break;
                            case PURCHASE_DISCOUNT_PERCENTAGE:
                                iuLogMdl.setPreviousPurchaseDiscountPer(DmShared.xNull(rsTemp.getString("purchaseDiscountPer")));
                                iuLogMdl.setCurrentPurchaseDiscountPer(DmShared.xNull(iufMdl.getFinalValue()));
                                break;
                            default:
                                break;
                        }
                    }
                }

                lstIuhMdl.add(iuLogMdl);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            sqlQuery = " UPDATE itemMast SET isActive = isActive";
            for (ItemUpdateField_Mdl iufMdl : iuMdl.getLstItemUpdateFields()) {
                if (DmShared.iNull(iufMdl.getSaveToItem()).intValue() == 1 && iufMdl.getField() != null) {
                    switch (iufMdl.getField()) {
                        case MRP:
                            sqlQuery += " , mrp = " + DmShared.vNull(iufMdl.getFinalValue());
                            break;
                        case SALE_RATE:
                            sqlQuery += " , saleRate = " + DmShared.vNull(iufMdl.getFinalValue());
                            break;
                        case PURCHASE_RATE:
                            sqlQuery += " , purchaseRate = " + DmShared.vNull(iufMdl.getFinalValue());
                            break;
                        case SCHEME:
                            String[] finalScheme = DmShared.xNull(iufMdl.getFinalValue()).split("[+]");
                            sqlQuery += " , schemeOnQty = " + DmShared.xNull(finalScheme[0]).trim();
                            sqlQuery += " , schemeFreeQty = " + DmShared.xNull(finalScheme[1]).trim();
                            break;
                        case PURCHASE_DISCOUNT_PERCENTAGE:
                            sqlQuery += " , purchaseDiscountPer = " + DmShared.vNull(iufMdl.getFinalValue());
                            break;
                        default:
                            break;
                    }
                } else if (DmShared.iNull(iufMdl.getSaveToCompany()).intValue() == 1 && iufMdl.getField() != null) {
                    switch (iufMdl.getField()) {
                        case PURCHASE_DISCOUNT_PERCENTAGE:
                            isCompanyUpdateRequierd = true;
                            companyPurchaseDiscountPer = DmShared.vNull(iufMdl.getFinalValue());
                            sqlQuery += " , purchaseDiscountPer = " + DmShared.vNull(iufMdl.getFinalValue());
                            break;
                    }
                }
            }
            sqlQuery += " WHERE code = :itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(iuMdl.getItemCode()));
            psTemp.executeUpdate();

            if (isCompanyUpdateRequierd) {
                String itemMakeCode = DmServer.executeScalar("SELECT itemMakeCode FROM itemMast WHERE code = " + DmShared.xNull(iuMdl.getItemCode()), cnTemp);

                //<editor-fold defaultstate="collapsed" desc="Save Update Log">
                sqlQuery = "SELECT im.code, im.purchaseDiscountPer "
                        + " FROM itemMast im "
                        + " WHERE im.itemMakeCode = :itemMakeCode"
                        + " AND im.code <> :itemCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemMakeCode", DmShared.xNull(itemMakeCode));
                psTemp.setString("itemCode", DmShared.xNull(iuMdl.getItemCode()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    iuLogMdl = new ItemUpdateHistory_Mdl();
                    iuLogMdl.setId(DmShared.xNull(iuMdl.getId()));
                    iuLogMdl.setDocId(DmShared.xNull(iuMdl.getDocId()));
                    iuLogMdl.setV_SNo(DmShared.xNull(iuMdl.getV_SNo()));
                    iuLogMdl.setV_Type(DmShared.xNull(iuMdl.getV_Type()));
                    iuLogMdl.setV_No(DmShared.xNull(iuMdl.getV_No()));
                    iuLogMdl.setV_Date(DmShared.xNull(iuMdl.getV_Date()));
                    iuLogMdl.setRecId(DmShared.xNull(iuMdl.getRecId()));
                    iuLogMdl.setSubcode(DmShared.xNull(iuMdl.getSubcode()));
                    iuLogMdl.setItemCode(DmShared.xNull(rsTemp.getString("code")));
                    iuLogMdl.setUnitCode(DmShared.xNull(iuMdl.getUnitCode()));
                    iuLogMdl.setPreviousPurchaseDiscountPer(DmShared.xNull(rsTemp.getString("purchaseDiscountPer")));
                    iuLogMdl.setCurrentPurchaseDiscountPer(DmServer.vNull(companyPurchaseDiscountPer));
                    lstIuhMdl.add(iuLogMdl);
                }
                rsTemp.close();
                psTemp.close();
                //</editor-fold>

                sqlQuery = "UPDATE itemMast SET"
                        + " purchaseDiscountPer = :purchaseDiscountPer"
                        + " WHERE itemMakeCode = :itemMakeCode"
                        + " AND code <> :itemCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("purchaseDiscountPer", DmServer.vNull(companyPurchaseDiscountPer));
                psTemp.setString("itemMakeCode", DmShared.xNull(itemMakeCode));
                psTemp.setString("itemCode", DmShared.xNull(iuMdl.getItemCode()));
                psTemp.executeUpdate();

                sqlQuery = "UPDATE itemMakeMast SET"
                        + " purchaseDiscountPer = :purchaseDiscountPer"
                        + " WHERE code = :itemMakeCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("purchaseDiscountPer", DmServer.vNull(companyPurchaseDiscountPer));
                psTemp.setString("itemMakeCode", DmShared.xNull(itemMakeCode));
                psTemp.executeUpdate();
            }
            saveUpdateLog(lstIuhMdl, cnTemp, dmrss);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void saveUpdateLog(List<ItemUpdateHistory_Mdl> lstIuLogMdl, Connection cnTemp, DmRSS dmrss) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        int sno = 0;

        try {

            for (ItemUpdateHistory_Mdl iuLogMdl : lstIuLogMdl) {
                sqlQuery = "INSERT INTO itemUpdateHistory (id, docId, v_SNo, sno, v_Type, v_No, v_Date, recId, site_Code, subcode, itemCode, unitCode,"
                        + "     previousMrp, currentMrp, previousSaleRate, currentSaleRate, previousPurchaseRate, currentPurchaseRate, previousPurchaseDiscountPer,"
                        + "     currentPurchaseDiscountPer, previousSchemeOnQty, currentSchemeOnQty, previousSchemeFreeQty,"
                        + "     currentSchemeFreeQty, u_Name, u_EntDt)"
                        + " VALUES (:id, :docId, :v_SNo, :sno, :v_Type, :v_No, :v_Date, :recId, :site_Code, :subcode, :itemCode, :unitCode,"
                        + "     :previousMrp, :currentMrp, :previousSaleRate, :currentSaleRate, :previousPurchaseRate, :currentPurchaseRate, :previousPurchaseDiscountPer,"
                        + "     :currentPurchaseDiscountPer, :previousSchemeOnQty, :currentSchemeOnQty, :previousSchemeFreeQty,"
                        + "     :currentSchemeFreeQty, :u_Name, :u_EntDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("id", DmShared.foriegnKey(iuLogMdl.getId()));
                psTemp.setString("docId", DmShared.foriegnKey(iuLogMdl.getDocId()));
                psTemp.setString("v_SNo", DmShared.foriegnKey(iuLogMdl.getV_SNo()));
                psTemp.setString("sno", DmShared.iNull(++sno).toString());
                psTemp.setString("v_Type", DmShared.xNull(iuLogMdl.getV_Type()));
                psTemp.setString("v_No", DmShared.foriegnKey(iuLogMdl.getV_No()));
                psTemp.setDate("v_Date", DmServer.convertDate(iuLogMdl.getV_Date()));
                psTemp.setString("recId", DmShared.xNull(iuLogMdl.getRecId()));
                psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(dmrss)));
                psTemp.setString("subcode", DmShared.foriegnKey(iuLogMdl.getSubcode()));
                psTemp.setString("itemCode", DmShared.foriegnKey(iuLogMdl.getItemCode()));
                psTemp.setString("unitCode", DmShared.foriegnKey(iuLogMdl.getUnitCode()));
                psTemp.setString("previousMrp", DmServer.vNull(iuLogMdl.getPreviousMrp()));
                psTemp.setString("currentMrp", DmServer.vNull(iuLogMdl.getCurrentMrp()));
                psTemp.setString("previousSaleRate", DmServer.vNull(iuLogMdl.getPreviousSaleRate()));
                psTemp.setString("currentSaleRate", DmServer.vNull(iuLogMdl.getCurrentSaleRate()));
                psTemp.setString("previousPurchaseRate", DmServer.vNull(iuLogMdl.getPreviousPurchaseRate()));
                psTemp.setString("currentPurchaseRate", DmServer.vNull(iuLogMdl.getCurrentPurchaseRate()));
                psTemp.setString("previousPurchaseDiscountPer", DmServer.vNull(iuLogMdl.getPreviousPurchaseDiscountPer()));
                psTemp.setString("currentPurchaseDiscountPer", DmServer.vNull(iuLogMdl.getCurrentPurchaseDiscountPer()));
                psTemp.setString("previousSchemeOnQty", DmServer.vNull(iuLogMdl.getPreviousSchemeOnQty()));
                psTemp.setString("currentSchemeOnQty", DmServer.vNull(iuLogMdl.getCurrentSchemeOnQty()));
                psTemp.setString("previousSchemeFreeQty", DmServer.vNull(iuLogMdl.getPreviousSchemeFreeQty()));
                psTemp.setString("currentSchemeFreeQty", DmServer.vNull(iuLogMdl.getCurrentSchemeFreeQty()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(dmrss));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                try {
                    if (psTemp != null) {
                        psTemp.close();
                    }
                } catch (Exception e) {

                }
            } catch (Exception e) {

            }
        }

    }

    public static void deleteItemUpdateHistory(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {
            sqlQuery = " DELETE FROM itemUpdateHistory WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

}
