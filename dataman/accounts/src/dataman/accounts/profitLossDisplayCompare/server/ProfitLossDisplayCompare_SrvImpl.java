package dataman.accounts.profitLossDisplayCompare.server;

import dataman.accounts.profitLossDisplayCompare.shared.ProfitLossDisplayCompare_Srv;
import dataman.accounts.profitLossDisplayCompare.shared.ProfitLossDisplayCompare_Mdl;
import dataman.accounts.profitLossDisplayCompare.shared.DefaultValue_Mdl;
import dataman.accounts.acDisplaySetting.shared.*;
import dataman.accounts.acGroupDetailCompare.server.AcGroupDetailCompare_SrvImpl;
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

public class ProfitLossDisplayCompare_SrvImpl extends DmRSSPrivate implements ProfitLossDisplayCompare_Srv {

    private double netProfitLoss = 0;
    private double lstNetProfitLoss = 0;
//===========0 index used for current year profit loss , 1 index is used for last year profit loss==============//
    private double[] arrProfitLoss = new double[2];

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting) {
        return evaluate(acDisplaySetting, this);
    }

    private DmResult<DefaultValue_Mdl> evaluate(AcDisplaySetting_Mdl acDisplaySetting, DmRSS rss) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        ProfitLossDisplayCompare_Mdl plm;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlCondition = "", sqlCmnCondition = "", sqlCondition1 = "", sqlHaving = "", siteList = "";
        double revenueTotal = 0, expenseTotal = 0, difference = 0;
        double lstRevenueTotal = 0, lstExpenseTotal = 0, lstDifference = 0;
        BigDecimal amtRevenue, amtHead, headBalance, lstHeadBalance;
        boolean addDetail = false;
        Integer intRevenueIndex = 0, intExpenseIndex = 0;
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
                    sqlCondition1 = "and (lg.v_Date between :dateFrom2 and :dateTo2) ";
                } else {
                    sqlCondition = "And AG.GroupNature IN (2,3,4,5) AND LG.V_Prefix = " + DmServer.getParamList(":yearPrefix", acDisplaySetting.getYearPrefix())
                            + " and (lg.v_Date <= :dateTo) ";
                    sqlCondition1 = "And AG.GroupNature IN (2,3,4,5) AND LG.V_Prefix = " + DmServer.getParamList(":yearPrefix2", acDisplaySetting.getYearPrefix2())
                            + " and (lg.v_Date <= :dateTo2) ";
                }

                sqlCmnCondition += "and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";

                if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    sqlCmnCondition += "and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySetting.getLedgerAcCodeList()) + ") ";
                }
                if (DmShared.iNull(acDisplaySetting.getShowDetail()).intValue() == 1) {
                    addDetail = true;
                }
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));

            sqlQuery = "Select GroupCode,Max(GName) as GName,Sum(AmtDr) as AmtDr,Sum(AmtCr) as AmtCr,Sum(LastAmtDr) as LastAmtDr, \n"
                    + " Sum(LastAmtCr) As LastAmtCr,Max(ContraGroupName) as ContraGroupName,Max(GroupNature) As GroupNature, \n"
                    + " sum(headBalance) as headBalance,sum(LastHeadBalance) as LastHeadBalance "
                    + " From ( "
                    + " SELECT  (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  AS GroupCode,  \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) AS GName,  \n"
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) ELSE 0 END) AS AmtDr,   \n"
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) ELSE 0 END) AS AmtCr,   \n"
                    + " ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0) as headBalance , 0 as LastHeadBalance, "
                    + " 0 as LastAmtDr,0 as LastAmtCr, \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.ContraGroupName ELSE AG1.ContraGroupName END) AS ContraGroupName, \n"
                    + "MAX(AG.GroupNature) AS GroupNature \n"
                    + "FROM Ledger LG \n"
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode    \n"
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode    \n"
                    + "LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1    \n"
                    + "LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder    \n"
                    //                    + "Where LG.isPosted = 1 And AG.GroupNature IN (2,3,4,5) AND LG.V_Prefix = " + DmServer.getSsnVoucherPrefix(rss) + " \n"
                    //                    + "Where LG.isPosted = 1 And AG.GroupNature IN (2,3,4,5) AND LG.V_Prefix = '21' \n"
                    + "Where LG.isPosted = 1 "
                    + sqlCondition
                    + sqlCmnCondition
                    + "GROUP BY (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  \n"
                    + sqlHaving
                    //                    + "ORDER BY MAX(CASE when AG.GroupNature IN (2,4) then 0 ELSE 1 END), "
                    //                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) "
                    + " Union All \n"
                    + "SELECT  (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  AS GroupCode,  \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) AS GName,  \n"
                    + "0 AS AmtDr, 0 AS AmtCr,   \n"
                    + " 0 as headBalance , ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0) as LastHeadBalance, "
                    + " (CASE WHEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) ELSE 0 END) as LastAmtDr, \n"
                    + " (CASE WHEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) ELSE 0 END) as LastAmtCr, \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.ContraGroupName ELSE AG1.ContraGroupName END) AS ContraGroupName, \n"
                    + "MAX(AG.GroupNature) AS GroupNature \n"
                    + "FROM Ledger LG \n"
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode    \n"
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode    \n"
                    + "LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1    \n"
                    + "LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder    \n"
                    //                    + "Where LG.isPosted = 1 And AG.GroupNature IN (2,3,4,5) AND LG.V_Prefix = " + DmServer.getSsnVoucherPrefix(rss) + " \n"
                    //                    + "Where LG.isPosted = 1 And AG.GroupNature IN (2,3,4,5) AND LG.V_Prefix = '20' \n"
                    + "Where LG.isPosted = 1 "
                    + sqlCondition1
                    + sqlCmnCondition
                    + "GROUP BY (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  \n"
                    + sqlHaving
                    //                    + "ORDER BY MAX(CASE when AG.GroupNature IN (2,4) then 0 ELSE 1 END), "
                    //                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) "
                    + " ) As Main \n"
                    + " Group By GroupCode "
                    + "ORDER BY MAX(CASE when GroupNature IN (4,5) then 0 ELSE 1 END),GName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                psTemp.setDate("dateFrom", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateFrom())));
                psTemp.setDate("dateFrom2", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getLstDateFrom())));
            }
            psTemp.setDate("dateTo", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateTo())));
            psTemp.setDate("dateTo2", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getLstDateTo())));
            psTemp.setStringList("siteCode", siteList);
            if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", acDisplaySetting.getLedgerAcCodeList());
            }

            if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 1) {
                if (!DmShared.xNull(acDisplaySetting.getYearPrefix()).trim().equalsIgnoreCase("")) {
                    psTemp.setStringList("yearPrefix", acDisplaySetting.getYearPrefix());
                }
                if (!DmShared.xNull(acDisplaySetting.getYearPrefix2()).trim().equalsIgnoreCase("")) {
                    psTemp.setStringList("yearPrefix2", acDisplaySetting.getYearPrefix2());
                }
            }
            rsTemp = psTemp.executeQuery();
            revenueTotal = 0;
            expenseTotal = 0;

            plm = new ProfitLossDisplayCompare_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setE_RHeadCode("RV");
            plm.setE_RHeadNameName("Revenue");
            plm.setE_RAmount(DmJasper.formatAmount(BigDecimal.valueOf(0)));
            intRevenueIndex = dvm.getProfitLossDisplayList().size();
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 2
                        || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 3) {
                    rsTemp.previous();
                    break;
                }

                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                if (plm != null) {
                    amtHead = BigDecimal.valueOf(0);
                    headBalance = BigDecimal.valueOf(0);
                    lstHeadBalance = BigDecimal.valueOf(0);
                    if (DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                        plm.setE_RDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                        plm.setE_RDrCr("Cr");
                    }
                    headBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("headBalance")).doubleValue());

                    plm.setE_RAmount(DmJasper.formatAmount(amtHead));
                    plm.setE_RHeadCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 2) {
                        plm.setE_RHeadNameName(DmShared.xNull(rsTemp.getString("GName")));
                    } else {
                        plm.setE_RHeadNameName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 2
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 3) {
                        expenseTotal += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00"));
                    } else {
                        revenueTotal += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00"));
                    }

//                        /*/*/*/*/**/*/*/*/*/*/*/*/**//*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/***/*/*/*/*/*/*/**/*/*/**//
                    lstHeadBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastHeadBalance")));

                    if (DmShared.vNull(rsTemp.getString("LastAmtDr")) > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtDr")));
                        plm.setLstE_RDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("LastAmtCr")) > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtCr")));
                        plm.setLstE_RDrCr("Cr");
                    }
                    lstHeadBalance = lstHeadBalance;
                    plm.setLstE_RAmount(DmJasper.formatAmount(amtHead));

                    if (addDetail) {
                        addDetails(acDisplaySetting, plm.getE_RHeadCode(), plm.getE_RHeadNameName(), dvm.getProfitLossDisplayList(), 1, cnTemp);
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 2
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 3) {
                        lstExpenseTotal += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00"));
                    } else {
                        lstRevenueTotal += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00"));
                    }

                    difference += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00"));
                    lstDifference += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00"));
                }
            }

            if (DmShared.vNull(acDisplaySetting.getClosingStock()) > 0) {
                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), -1);
                amtRevenue = BigDecimal.valueOf(DmShared.vNull(acDisplaySetting.getClosingStock()));
                plm.setE_RHeadCode("CS");
                plm.setE_RHeadNameName("Closing Stock");
                plm.setE_RAmount(DmJasper.formatAmount(amtRevenue));
                revenueTotal += amtRevenue.doubleValue();
                difference += -amtRevenue.doubleValue();
            }

            if (revenueTotal > expenseTotal) {
                amtRevenue = BigDecimal.valueOf(revenueTotal);
            } else {
                amtRevenue = BigDecimal.valueOf(expenseTotal);
            }

            plm = new ProfitLossDisplayCompare_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setE_RHeadCode("TT");
            plm.setE_RHeadNameName("");
            plm.setE_RAmount(DmJasper.formatAmount(amtRevenue));

            plm = new ProfitLossDisplayCompare_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setE_RHeadCode("EX");
            plm.setE_RHeadNameName("Expense");
            plm.setE_RAmount(DmJasper.formatAmount(amtRevenue));
            intExpenseIndex = dvm.getProfitLossDisplayList().size();

            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 4
                        || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 5) {
                    rsTemp.previous();
                    break;
                }

                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                if (plm != null) {
                    amtHead = BigDecimal.valueOf(0);
                    headBalance = BigDecimal.valueOf(0);
                    lstHeadBalance = BigDecimal.valueOf(0);
                    if (DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                        plm.setE_RDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                        plm.setE_RDrCr("Cr");
                    }
                    headBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("headBalance")).doubleValue());

                    plm.setE_RAmount(DmJasper.formatAmount(amtHead));
                    plm.setE_RHeadCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 4) {
                        plm.setE_RHeadNameName(DmShared.xNull(rsTemp.getString("GName")));
                    } else {
                        plm.setE_RHeadNameName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 4
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 5) {
                        revenueTotal += headBalance.doubleValue();
                    } else {
                        expenseTotal += headBalance.doubleValue();
                    }

//                        /*/*/*/*/**/*/*/*/*/*/*/*/**//*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/***/*/*/*/*/*/*/**/*/*/**//
                    lstHeadBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastHeadBalance")).doubleValue());

                    if (DmShared.vNull(rsTemp.getString("LastAmtDr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtDr")).doubleValue());
                        plm.setLstE_RDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("LastAmtCr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtCr")).doubleValue());
                        plm.setLstE_RDrCr("Cr");
                    }
                    lstHeadBalance = lstHeadBalance;
                    plm.setLstE_RAmount(DmJasper.formatAmount(amtHead));

                    if (addDetail) {
                        addDetails(acDisplaySetting, plm.getE_RHeadCode(), plm.getE_RHeadNameName(), dvm.getProfitLossDisplayList(), 1, cnTemp);
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 2
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 3) {
                        lstExpenseTotal += lstHeadBalance.doubleValue();
                    } else {
                        lstRevenueTotal += lstHeadBalance.doubleValue();
                    }

                    difference += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00")).doubleValue();
                    lstDifference += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00")).doubleValue();
                }
            }

            //===================== Total of Expenses and Revenue ==============================
            if (revenueTotal < 0) {
                dvm.getProfitLossDisplayList().get(intRevenueIndex - 1).setE_RDrCr("Cr");
            } else if (revenueTotal > 0) {
                dvm.getProfitLossDisplayList().get(intRevenueIndex - 1).setE_RDrCr("Dr");
            }
            revenueTotal = Math.abs(revenueTotal);
            amtRevenue = BigDecimal.valueOf(revenueTotal);
            dvm.getProfitLossDisplayList().get(intRevenueIndex - 1).setE_RAmount(DmJasper.formatAmount(amtRevenue));

            if (expenseTotal < 0) {
                dvm.getProfitLossDisplayList().get(intExpenseIndex - 1).setE_RDrCr("Cr");
            } else if (expenseTotal > 0) {
                dvm.getProfitLossDisplayList().get(intExpenseIndex - 1).setE_RDrCr("Dr");
            }
            expenseTotal = Math.abs(expenseTotal);
            amtRevenue = BigDecimal.valueOf(expenseTotal);
            dvm.getProfitLossDisplayList().get(intExpenseIndex - 1).setE_RAmount(DmJasper.formatAmount(amtRevenue));

            if (lstRevenueTotal < 0) {
                dvm.getProfitLossDisplayList().get(intRevenueIndex - 1).setE_RDrCr("Cr");
            } else if (lstRevenueTotal > 0) {
                dvm.getProfitLossDisplayList().get(intRevenueIndex - 1).setE_RDrCr("Dr");
            }
            lstRevenueTotal = Math.abs(lstRevenueTotal);
            amtRevenue = BigDecimal.valueOf(lstRevenueTotal);
            dvm.getProfitLossDisplayList().get(intRevenueIndex - 1).setLstE_RAmount(DmJasper.formatAmount(amtRevenue));

            if (lstExpenseTotal < 0) {
                dvm.getProfitLossDisplayList().get(intExpenseIndex - 1).setE_RDrCr("Cr");
            } else if (lstExpenseTotal > 0) {
                dvm.getProfitLossDisplayList().get(intExpenseIndex - 1).setE_RDrCr("Dr");
            }
            lstExpenseTotal = Math.abs(lstExpenseTotal);
            amtRevenue = BigDecimal.valueOf(lstExpenseTotal);
            dvm.getProfitLossDisplayList().get(intExpenseIndex - 1).setLstE_RAmount(DmJasper.formatAmount(amtRevenue));

//===================== Fetching Profit & Loss ==============================
//            netProfitLoss = revenueTotal + expenseTotal;
            if (difference != 0) {
                netProfitLoss = difference;
                arrProfitLoss[0] = netProfitLoss;
                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), netProfitLoss);
                plm.setE_RHeadNameName("Net Profit/Loss ");
                if (difference >= 0) {
                    amtHead = BigDecimal.valueOf(Math.abs(difference));
                    plm.setE_RHeadCode("NL");
                    plm.setE_RDrCr("NL");
                    plm.setE_RAmount(DmJasper.formatAmount(amtHead));

                } else {
                    amtRevenue = BigDecimal.valueOf(Math.abs(difference));
                    plm.setE_RHeadCode("NP");
                    plm.setE_RDrCr("NP");
                    plm.setE_RAmount(DmJasper.formatAmount(amtRevenue));
                }
            }

            if (lstDifference != 0) {
                lstNetProfitLoss = lstDifference;
                arrProfitLoss[1] = lstNetProfitLoss;
                if (lstDifference >= 0) {
                    plm.setLstE_RDrCr("NL");
                    amtHead = BigDecimal.valueOf(Math.abs(lstDifference));
                    plm.setLstE_RAmount(DmJasper.formatAmount(amtHead));
                } else {
                    plm.setLstE_RDrCr("NP");
                    amtRevenue = BigDecimal.valueOf(Math.abs(lstDifference));
                    plm.setLstE_RAmount(DmJasper.formatAmount(amtRevenue));
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

    public void addDetails(AcDisplaySetting_Mdl acDisplaySetting, String acForCode, String acForName, List<ProfitLossDisplayCompare_Mdl> profitLossList, double amount, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        ProfitLossDisplayCompare_Mdl plm;
        try {
            psTemp = AcGroupDetailCompare_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySetting, DmShared.iNull(acForCode).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr"))) - DmShared.vNull(rsTemp.getString("amtCr")), "0.00"));
                plm = getEmptyRowIndex(profitLossList, amount);

                plm.setE_RChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                plm.setE_RChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                plm.setE_RChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                plm.setE_RChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                plm.setE_RHeadNameName(DmShared.xNull(acForName));
                plm.setE_RHeadCode(DmShared.iNull(acForCode).toString());
                plm.setE_RChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing)));

                double lstAmtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("LastAmtDr"))) - DmShared.vNull(rsTemp.getString("LastAmtCr")), "0.00"));

                plm.setLstE_RChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(lstAmtClosing)));
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

    public double[] getNetProfit(AcDisplaySetting_Mdl acDisplaySetting, DmRSS rss) {
        evaluate(acDisplaySetting, rss);
//        return netProfitLoss;
        return arrProfitLoss;
    }

    private ProfitLossDisplayCompare_Mdl getEmptyRowIndex(List<ProfitLossDisplayCompare_Mdl> profitLossList, double amount) {
        int index = -1, i = 0;
        ProfitLossDisplayCompare_Mdl profitLoss = null;

        if (profitLossList != null) {
            i = profitLossList.size() - 1;
            while (true) {
                if (i >= 0) {
                    profitLoss = profitLossList.get(i);
                    if (profitLoss != null) {
                        if (amount > 0) {
                            if (DmShared.xNull(profitLoss.getE_RHeadCode()).trim().equalsIgnoreCase("")) {
                                index = i;
                            } else {
                                break;
                            }
                        } else {
                            if (DmShared.xNull(profitLoss.getE_RHeadCode()).trim().equalsIgnoreCase("")) {
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
                profitLoss = new ProfitLossDisplayCompare_Mdl();
                profitLossList.add(profitLoss);
                index = profitLossList.size() - 1;
            }

            profitLoss = profitLossList.get(index);
        }

        return profitLoss;
    }
}
