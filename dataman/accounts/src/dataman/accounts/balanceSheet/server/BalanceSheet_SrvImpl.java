package dataman.accounts.balanceSheet.server;

import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acGroupDetail.server.AcGroupDetail_SrvImpl;
import dataman.dmBase.self.shared.*;
import dataman.accounts.balanceSheet.shared.*;
import dataman.accounts.profitLossDisplay.server.ProfitLossDisplay_SrvImpl;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class BalanceSheet_SrvImpl extends DmRSSPrivate implements BalanceSheet_Srv {

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        BalanceSheet_Mdl bsm = null;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlCondition = "", sqlHaving = "", siteList = "";
        double liabilityTotal = 0, assetsTotal = 0, difference = 0, netProfit = 0;
        BigDecimal liabilityAmount, assetsAmount, amtCl, amtDifference;

        boolean addDetail = false;
        dmrTemp.setData(dvm);
        try {

            if (acDisplaySetting != null) {
                dvm.setDtFrom(acDisplaySetting.getDateFrom());
                dvm.setDtTo(acDisplaySetting.getDateTo());
                if (!DmShared.xNull(acDisplaySetting.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySetting.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(this);
                }

                if (DmShared.iNull(acDisplaySetting.getShowZeroValueAc()).intValue() == 0) {
                    sqlHaving = "Having (Round(IsNull(Sum(LG.AmtDr),0),2) - Round(IsNull(Sum(LG.AmtCr),0),2)) <> 0 ";
                }

                if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                    sqlCondition = "and (lg.v_Date between :dateFrom and :dateTo) ";
                } else {
                    sqlCondition = "and (lg.v_Date <= :dateTo) ";
                }

                sqlCondition += "and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";

                if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    sqlCondition += "and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySetting.getLedgerAcCodeList()) + ") ";
                }
                if (DmShared.iNull(acDisplaySetting.getShowDetail()).intValue() == 1) {
                    addDetail = true;
                }
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  AS GroupCode,  \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) AS GName,  \n"
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) ELSE 0 END) AS AmtDr,   \n"
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) ELSE 0 END) AS AmtCr,   \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.ContraGroupName ELSE AG1.ContraGroupName END) AS ContraGroupName, \n"
                    + "MAX(AG.GroupNature) AS GroupNature \n"
                    + "FROM Ledger LG \n"
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode    \n"
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode    \n"
                    + "LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1    \n"
                    + "LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder    \n"
                    + "Where LG.isPosted = 1 And AG.GroupNature IN (0,1) "
                    + sqlCondition
                    + "GROUP BY (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  \n"
                    + sqlHaving
                    + "ORDER BY MAX(AG.GroupNature), \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) \n";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                psTemp.setDate("dateFrom", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateFrom())));
            }
            psTemp.setDate("dateTo", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateTo())));
            psTemp.setStringList("siteCode", siteList);
            if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", acDisplaySetting.getLedgerAcCodeList());
            }
            rsTemp = psTemp.executeQuery();
            liabilityTotal = 0;
            assetsTotal = 0;
            while (rsTemp.next()) {
                bsm = getEmptyRowIndex(dvm.getLstBalanceSheet(), DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                if (bsm != null) {
                    assetsAmount = BigDecimal.valueOf(0);
                    liabilityAmount = BigDecimal.valueOf(0);
                    if (DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() > 0) {
                        assetsAmount = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                        bsm.setAssetAmount(DmJasper.formatAmount(assetsAmount));
                        bsm.setAssetCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                        if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 1) {
                            bsm.setAssetName(DmShared.xNull(rsTemp.getString("GName")));
                        } else {
                            bsm.setAssetName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                        }
                        if (addDetail) {
                            addDetails(acDisplaySetting, bsm.getAssetCode(), bsm.getAssetName(), dvm.getLstBalanceSheet(), 1, cnTemp);
                        }
                    } else {
                        liabilityAmount = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("AmtCr")));
                        bsm.setLiabilityAmount(DmJasper.formatAmount(liabilityAmount));
                        bsm.setLiabilityCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                        if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 0) {
                            bsm.setLiabilityName(DmShared.xNull(rsTemp.getString("GName")));
                        } else {
                            bsm.setLiabilityName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                        }
                        if (addDetail) {
                            addDetails(acDisplaySetting, bsm.getLiabilityCode(), bsm.getLiabilityName(), dvm.getLstBalanceSheet(), -1, cnTemp);
                        }
                    }
                    assetsTotal += assetsAmount.doubleValue();
                    liabilityTotal += liabilityAmount.doubleValue();
                }
            }

            if (DmShared.vNull(acDisplaySetting.getClosingStock()).doubleValue() > 0) {
                bsm = getEmptyRowIndex(dvm.getLstBalanceSheet(), 1);
                amtCl = BigDecimal.valueOf(DmShared.vNull(acDisplaySetting.getClosingStock()).doubleValue());
                bsm.setAssetCode("CS");
                bsm.setAssetName("Closing Stock");
                bsm.setAssetAmount(DmJasper.formatAmount(amtCl));
                assetsTotal += DmShared.vNull(acDisplaySetting.getClosingStock()).doubleValue();
            }

            //===================== Fetching Profit & Loss ==============================
            acDisplaySetting.setShowDetail("0");
            netProfit = new ProfitLossDisplay_SrvImpl().getNetProfit(acDisplaySetting, this);
            if (netProfit != 0) {
                if (netProfit >= 0) {
                    bsm = getEmptyRowIndex(dvm.getLstBalanceSheet(), -1);
                    amtCl = BigDecimal.valueOf(Math.abs(netProfit));
                    bsm.setLiabilityCode("NP");
                    bsm.setLiabilityName("Net Profit");
                    bsm.setLiabilityAmount(DmJasper.formatAmount(amtCl));
                    liabilityTotal += amtCl.doubleValue();
                } else {
                    bsm = getEmptyRowIndex(dvm.getLstBalanceSheet(), 1);
                    amtCl = BigDecimal.valueOf(Math.abs(netProfit));
                    bsm.setAssetCode("NL");
                    bsm.setAssetName("Net Loss");
                    bsm.setAssetAmount(DmJasper.formatAmount(amtCl));
                    assetsTotal += amtCl.doubleValue();
                }
            }
            //===========================================================================

            difference = assetsTotal - liabilityTotal;
            assetsAmount = BigDecimal.valueOf(liabilityTotal);
            liabilityAmount = BigDecimal.valueOf(assetsTotal);
            dvm.setAmountAssests(DmJasper.formatAmount(liabilityAmount));
            dvm.setAmountLiabilities(DmJasper.formatAmount(assetsAmount));
            if (difference != 0) {
                amtDifference = BigDecimal.valueOf(Math.abs(difference));
                dvm.setAmountDifference("Difference in Trial Balance: " + DmJasper.formatAmount(amtDifference) + (difference > 0 ? " Dr" : " Cr"));
            } else {
                dvm.setAmountDifference("");
            }
            dmrTemp.setData(dvm);
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

    public void addDetails(AcDisplaySetting_Mdl acDisplaySetting, String acForCode, String acForName, List<BalanceSheet_Mdl> balanceSheetList, double amount, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        BalanceSheet_Mdl bsm = null;
        try {
            psTemp = AcGroupDetail_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySetting, DmShared.iNull(acForCode).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr"))) - DmShared.vNull(rsTemp.getString("amtCr")), "0.00"));
                bsm = getEmptyRowIndex(balanceSheetList, amount);
//                if (DmShared.iNull(rsTemp.getString("subCode")).toString().equalsIgnoreCase("0")) {
                    if (amount > 0) {
                        bsm.setAssetChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                        bsm.setAssetChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                        bsm.setAssetChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                        bsm.setAssetChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                        bsm.setAssetName(DmShared.xNull(acForName));
                        bsm.setAssetCode(DmShared.iNull(acForCode).toString());
                        bsm.setAssetChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing)));
                    } else {
                        bsm.setLiabilityChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                        bsm.setLiabilityChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                        bsm.setLiabilityChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                        bsm.setLiabilityChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                        bsm.setLiabilityName(DmShared.xNull(acForName));
                        bsm.setLiabilityCode(DmShared.iNull(acForCode).toString());
                        bsm.setLiabilityChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing * -1)));
                    }

//                }

//                if (DmShared.iNull(rsTemp.getString("subCode")).toString().equalsIgnoreCase("0")) {
//                    addDetails(acDisplaySetting, DmShared.iNull(rsTemp.getString("groupCode")).toString(),
//                            DmShared.xNull(rsTemp.getString("groupName")), balanceSheetList, amount, cnTemp);
//                }

            }

            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw e;
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
    }

    private BalanceSheet_Mdl getEmptyRowIndex(List<BalanceSheet_Mdl> balanceSheetList, double amount) {
        int index = -1, i = 0;
        BalanceSheet_Mdl balanceSheet = null;

        if (balanceSheetList != null) {
            i = balanceSheetList.size() - 1;
            while (true) {
                if (i >= 0) {
                    balanceSheet = balanceSheetList.get(i);
                    if (balanceSheet != null) {
                        if (amount > 0) {
                            if (DmShared.xNull(balanceSheet.getAssetCode()).trim().equalsIgnoreCase("")) {
                                index = i;
                            } else {
                                break;
                            }
                        } else {
                            if (DmShared.xNull(balanceSheet.getLiabilityCode()).trim().equalsIgnoreCase("")) {
                                index = i;
                            } else {
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
                i--;
            }

            if (index < 0) {
                balanceSheet = new BalanceSheet_Mdl();
                balanceSheetList.add(balanceSheet);
                index = balanceSheetList.size() - 1;
            }

            balanceSheet = balanceSheetList.get(index);
        }

        return balanceSheet;
    }
}
