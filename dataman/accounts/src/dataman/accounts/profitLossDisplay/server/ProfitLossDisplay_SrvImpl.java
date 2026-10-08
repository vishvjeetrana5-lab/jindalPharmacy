package dataman.accounts.profitLossDisplay.server;

import dataman.accounts.profitLossDisplay.shared.ProfitLossDisplay_Srv;
import dataman.accounts.profitLossDisplay.shared.ProfitLossDisplay_Mdl;
import dataman.accounts.profitLossDisplay.shared.DefaultValue_Mdl;
import dataman.accounts.acDisplaySetting.shared.*;
import dataman.accounts.acGroupDetail.server.AcGroupDetail_SrvImpl;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class ProfitLossDisplay_SrvImpl extends DmRSSPrivate implements ProfitLossDisplay_Srv {

    private double netProfit = 0;

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting) {
        return evaluate(acDisplaySetting, this);
    }

    private DmResult<DefaultValue_Mdl> evaluate(AcDisplaySetting_Mdl acDisplaySetting, DmRSS rss) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        ProfitLossDisplay_Mdl plm;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlCondition = "", sqlHaving = "", siteList = "";
        double revenueTotal = 0, expenseTotal = 0, difference = 0;
        BigDecimal amtRevenue, amtExpense;
        boolean addDetail = false;
        dmrTemp.setData(dvm);
        try {

            if (acDisplaySetting != null) {
                if (!DmShared.xNull(acDisplaySetting.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySetting.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(rss);
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

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));

            sqlQuery = "SELECT  (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  AS GroupCode,  "
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) AS GName,  "
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) ELSE 0 END) AS AmtDr,   "
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) ELSE 0 END) AS AmtCr,   "
                    + "MAX(CASE when AG1.GroupCode is null then AG.ContraGroupName ELSE AG1.ContraGroupName END) AS ContraGroupName, "
                    + "MAX(AG.GroupNature) AS GroupNature "
                    + "FROM Ledger LG "
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode    "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode    "
                    + "LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1    "
                    + "LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder    "
                    + "Where LG.isPosted = 1 And AG.GroupNature IN (2,3,4,5) AND LG.V_Prefix = " + DmServer.getSsnVoucherPrefix(rss) + " "
                    + sqlCondition
                    + "GROUP BY (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  "
                    + sqlHaving
                    + "ORDER BY MAX(CASE when AG.GroupNature IN (2,4) then 0 ELSE 1 END), "
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                psTemp.setDate("dateFrom", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateFrom())));
            }
            psTemp.setDate("dateTo", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateTo())));
            psTemp.setStringList("siteCode", siteList);
            if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", acDisplaySetting.getLedgerAcCodeList());
            }
            rsTemp = psTemp.executeQuery();
            revenueTotal = 0;
            expenseTotal = 0;
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 3
                        || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 5) {
                    rsTemp.previous();
                    break;
                }

                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                if (plm != null) {
                    amtExpense = BigDecimal.valueOf(0);
                    amtRevenue = BigDecimal.valueOf(0);
                    if (DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() > 0) {
                        amtExpense = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                        plm.setExpensesAmount(DmJasper.formatAmount(amtExpense));
                        plm.setExpensesCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                        if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 2) {

                            plm.setExpensesName(DmShared.xNull(rsTemp.getString("GName")));

                        } else {
                            plm.setExpensesName(DmShared.xNull(rsTemp.getString("ContraGroupName")));

                        }

                        if (addDetail) {
                            addDetails(acDisplaySetting, plm.getExpensesCode(), plm.getExpensesName(), dvm.getProfitLossDisplayList(), 1, cnTemp);
                        }

                    } else if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() > 0) {

                        amtRevenue = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                        plm.setRevenueAmount(DmJasper.formatAmount(amtRevenue));
                        plm.setRevenueCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                        if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 4) {
                            plm.setRevenueName(DmShared.xNull(rsTemp.getString("GName")));
                        } else {
                            plm.setRevenueName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                        }
                        if (addDetail) {
                            addDetails(acDisplaySetting, plm.getRevenueCode(), plm.getRevenueName(), dvm.getProfitLossDisplayList(), -1, cnTemp);
                        }

                    }
                    expenseTotal += amtExpense.doubleValue();
                    revenueTotal += amtRevenue.doubleValue();
                }
            }

            if (DmShared.vNull(acDisplaySetting.getClosingStock()).doubleValue() > 0) {
                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), -1);
                amtRevenue = BigDecimal.valueOf(DmShared.vNull(acDisplaySetting.getClosingStock()).doubleValue());
                plm.setRevenueCode("CS");
                plm.setRevenueName("Closing Stock");
                plm.setRevenueAmount(DmJasper.formatAmount(amtRevenue));
                revenueTotal += amtRevenue.doubleValue();
            }

            difference = revenueTotal - expenseTotal;
            if (difference != 0) {
                if (difference >= 0) {
                    plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), 1);
                    amtExpense = BigDecimal.valueOf(Math.abs(difference));
                    plm.setExpensesName("Gross Profit C/O");
                    plm.setExpensesCode("GP");
                    plm.setExpensesAmount(DmJasper.formatAmount(amtExpense));

                } else {
                    plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), -1);
                    amtRevenue = BigDecimal.valueOf(Math.abs(difference));
                    plm.setRevenueName("Gross Loss C/O");
                    plm.setRevenueCode("GL");
                    plm.setRevenueAmount(DmJasper.formatAmount(amtRevenue));
                }
            }

            if (revenueTotal > expenseTotal) {
                amtRevenue = BigDecimal.valueOf(revenueTotal);
            } else {
                amtRevenue = BigDecimal.valueOf(expenseTotal);
            }

            plm = new ProfitLossDisplay_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setRevenueCode("TT");
            plm.setRevenueName("");
            plm.setRevenueAmount(DmJasper.formatAmount(amtRevenue));

            plm.setExpensesCode("TT");
            plm.setExpensesName("");
            plm.setExpensesAmount(DmJasper.formatAmount(amtRevenue));

            if (difference != 0) {
                if (difference >= 0) {
                    plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), -1);
                    amtRevenue = BigDecimal.valueOf(Math.abs(difference));
                    plm.setRevenueName("Gross Profit B/F");
                    plm.setRevenueCode("GP");
                    plm.setRevenueAmount(DmJasper.formatAmount(amtRevenue));
                } else {
                    plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), 1);
                    amtExpense = BigDecimal.valueOf(Math.abs(difference));
                    plm.setExpensesName("Gross Loss B/F");
                    plm.setExpensesCode("GL");
                    plm.setExpensesAmount(DmJasper.formatAmount(amtExpense));
                }
            }

            revenueTotal = 0;
            expenseTotal = 0;
            if (difference > 0) {
                revenueTotal = Math.abs(difference);
            } else {
                expenseTotal = Math.abs(difference);
            }
            while (rsTemp.next()) {
                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                if (plm != null) {
                    amtExpense = BigDecimal.valueOf(0);
                    amtRevenue = BigDecimal.valueOf(0);
                    if (DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() > 0) {
                        amtExpense = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                        plm.setExpensesAmount(DmJasper.formatAmount(amtExpense));
                        plm.setExpensesCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                        if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 3) {

                            plm.setExpensesName(DmShared.xNull(rsTemp.getString("GName")));

                        } else {
                            plm.setExpensesName(DmShared.xNull(rsTemp.getString("ContraGroupName")));

                        }
                        if (addDetail) {
                            addDetails(acDisplaySetting, plm.getExpensesCode(), plm.getExpensesName(), dvm.getProfitLossDisplayList(), 1, cnTemp);
                        }

                    } else if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() > 0) {

                        amtRevenue = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                        plm.setRevenueAmount(DmJasper.formatAmount(amtRevenue));
                        plm.setRevenueCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                        if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 5) {
                            plm.setRevenueName(DmShared.xNull(rsTemp.getString("GName")));
                        } else {
                            plm.setRevenueName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                        }
                        if (addDetail) {
                            addDetails(acDisplaySetting, plm.getRevenueCode(), plm.getRevenueName(), dvm.getProfitLossDisplayList(), -1, cnTemp);
                        }

                    }
                    expenseTotal += amtExpense.doubleValue();
                    revenueTotal += amtRevenue.doubleValue();
                }
            }

            //===================== Fetching Profit & Loss ==============================
            netProfit = revenueTotal - expenseTotal;
            if (netProfit != 0) {
                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), netProfit);
                if (netProfit >= 0) {
                    amtExpense = BigDecimal.valueOf(Math.abs(netProfit));
                    plm.setExpensesName("Net Profit");
                    plm.setExpensesCode("NP");
                    plm.setExpensesAmount(DmJasper.formatAmount(amtExpense));

                } else {
                    amtRevenue = BigDecimal.valueOf(Math.abs(netProfit));
                    plm.setRevenueName("Net Loss");
                    plm.setRevenueCode("NL");
                    plm.setRevenueAmount(DmJasper.formatAmount(amtRevenue));
                }
            }
            //===========================================================================

            if (revenueTotal > expenseTotal) {
                amtRevenue = BigDecimal.valueOf(revenueTotal);
            } else {
                amtRevenue = BigDecimal.valueOf(expenseTotal);
            }
            dvm.setTotal(DmJasper.formatAmount(amtRevenue));

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

    public void addDetails(AcDisplaySetting_Mdl acDisplaySetting, String acForCode, String acForName, List<ProfitLossDisplay_Mdl> profitLossList, double amount, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        ProfitLossDisplay_Mdl plm;
        try {
            psTemp = AcGroupDetail_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySetting, DmShared.iNull(acForCode).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr"))) - DmShared.vNull(rsTemp.getString("amtCr")), "0.00"));
                plm = getEmptyRowIndex(profitLossList, amount);
                if (amount > 0) {
                    plm.setExpensesChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                    plm.setExpensesChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                    plm.setExpensesChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                    plm.setExpensesChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                    plm.setExpensesName(DmShared.xNull(acForName));
                    plm.setExpensesCode(DmShared.iNull(acForCode).toString());
                    plm.setExpensesChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing)));
                } else {
                    plm.setRevenueChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                    plm.setRevenueChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                    plm.setRevenueChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                    plm.setRevenueChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                    plm.setRevenueName(DmShared.xNull(acForName));
                    plm.setRevenueCode(DmShared.iNull(acForCode).toString());
                    plm.setRevenueChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing * -1)));
                }
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

    public double getNetProfit(AcDisplaySetting_Mdl acDisplaySetting, DmRSS rss) {
        evaluate(acDisplaySetting, rss);
        return netProfit;
    }

    private ProfitLossDisplay_Mdl getEmptyRowIndex(List<ProfitLossDisplay_Mdl> profitLossList, double amount) {
        int index = -1, i = 0;
        ProfitLossDisplay_Mdl profitLoss = null;

        if (profitLossList != null) {
            i = profitLossList.size() - 1;
            while (true) {
                if (i >= 0) {
                    profitLoss = profitLossList.get(i);
                    if (profitLoss != null) {
                        if (amount > 0) {
                            if (DmShared.xNull(profitLoss.getExpensesCode()).trim().equalsIgnoreCase("")) {
                                index = i;
                            } else {
                                break;
                            }
                        } else {
                            if (DmShared.xNull(profitLoss.getRevenueCode()).trim().equalsIgnoreCase("")) {
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
                profitLoss = new ProfitLossDisplay_Mdl();
                profitLossList.add(profitLoss);
                index = profitLossList.size() - 1;
            }

            profitLoss = profitLossList.get(index);
        }

        return profitLoss;
    }
}
