package dataman.accounts.balanceSheetCompare.server;

import dataman.accounts.balanceSheetCompare.shared.BalanceSheetDisplayCompare_Srv;
import dataman.accounts.balanceSheetCompare.shared.BalanceSheetDisplayCompare_Mdl;
import dataman.accounts.balanceSheetCompare.shared.DefaultValue_Mdl;
import dataman.accounts.acDisplaySetting.shared.*;
import dataman.accounts.acGroupDetailCompare.server.AcGroupDetailCompare_SrvImpl;
import dataman.accounts.profitLossDisplayCompare.server.ProfitLossDisplayCompare_SrvImpl;
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

public class BalanceSheetDisplayCompare_SrvImpl extends DmRSSPrivate implements BalanceSheetDisplayCompare_Srv {

    private double netProfitLoss = 0;
    private double[] netProfit = new double[2];

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting) {
        return evaluate(acDisplaySetting, this);
    }

    private DmResult<DefaultValue_Mdl> evaluate(AcDisplaySetting_Mdl acDisplaySetting, DmRSS rss) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        BalanceSheetDisplayCompare_Mdl plm;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlCondition = "", sqlCmnCondition = "", sqlCondition1 = "", sqlHaving = "", siteList = "";
        double assetTotal = 0, liabilityTotal = 0, difference = 0;
        double lstAssetTotal = 0, lstLiabilityTotal = 0, lstDifference = 0;
        BigDecimal amtTransaction, amtHead, headBalance, lstHeadBalance;
        boolean addDetail = false;
        Integer intLiabilityIndex = 0, intAssetIndex = 0;
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
                    sqlCondition = " and (lg.v_Date <= :dateTo) ";
                    sqlCondition1 = " and (lg.v_Date <= :dateTo2) ";
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
                    + "Where LG.isPosted = 1  And AG.GroupNature IN (0,1) "
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
                    + "Where LG.isPosted = 1 And AG.GroupNature IN (0,1) "
                    + sqlCondition1
                    + sqlCmnCondition
                    + "GROUP BY (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  \n"
                    + sqlHaving
                    //                    + "ORDER BY MAX(CASE when AG.GroupNature IN (2,4) then 0 ELSE 1 END), "
                    //                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) "
                    + " ) As Main \n"
                    + " Group By GroupCode "
                    + "ORDER BY MAX(GroupNature),GName ";

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
//            if (!DmShared.xNull(acDisplaySetting.getYearPrefix()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("yearPrefix", acDisplaySetting.getYearPrefix());
//            }
//            if (!DmShared.xNull(acDisplaySetting.getYearPrefix2()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("yearPrefix2", acDisplaySetting.getYearPrefix2());
//            }
            rsTemp = psTemp.executeQuery();
            assetTotal = 0;
            liabilityTotal = 0;

            plm = new BalanceSheetDisplayCompare_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setA_LHeadCode("DF");
            plm.setA_LHeadNameName("DIFFERENCE IN TRIAL");
            plm.setA_LAmount(DmJasper.formatAmount(BigDecimal.valueOf(0)));

            plm = new BalanceSheetDisplayCompare_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setA_LHeadCode("EL");
            plm.setA_LHeadNameName("EQUITY AND LIABILITIES");
            plm.setA_LAmount(DmJasper.formatAmount(BigDecimal.valueOf(0)));
            intLiabilityIndex = dvm.getProfitLossDisplayList().size();

            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 1) {
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
                        plm.setA_LDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                        plm.setA_LDrCr("Cr");
                    }
                    headBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("headBalance")).doubleValue());

                    plm.setA_LAmount(DmJasper.formatAmount(amtHead));
                    plm.setA_LHeadCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 0) {
                        plm.setA_LHeadNameName(DmShared.xNull(rsTemp.getString("GName")));
                    } else {
                        plm.setA_LHeadNameName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 0) {
                        liabilityTotal += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00")).doubleValue();
                    } else {
                        assetTotal += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00")).doubleValue();
                    }

//                        /*/*/*/*/**/*/*/*/*/*/*/*/**//*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/***/*/*/*/*/*/*/**/*/*/**//
                    lstHeadBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastHeadBalance")).doubleValue());

                    if (DmShared.vNull(rsTemp.getString("LastAmtDr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtDr")).doubleValue());
                        plm.setLstA_LDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("LastAmtCr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtCr")).doubleValue());
                        plm.setLstA_LDrCr("Cr");
                    }
                    lstHeadBalance = lstHeadBalance;
                    plm.setLstA_LAmount(DmJasper.formatAmount(amtHead));

                    if (addDetail) {
                        addDetails(acDisplaySetting, plm.getA_LHeadCode(), plm.getA_LHeadNameName(), dvm.getProfitLossDisplayList(), 1, cnTemp);
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 0) {
                        lstLiabilityTotal += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00")).doubleValue();
                    } else {
                        lstAssetTotal += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00")).doubleValue();
                    }

                    difference += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00")).doubleValue();
                    lstDifference += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00")).doubleValue();
                }
            }

            if (DmShared.vNull(acDisplaySetting.getClosingStock()).doubleValue() > 0) {
                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), -1);
                amtTransaction = BigDecimal.valueOf(DmShared.vNull(acDisplaySetting.getClosingStock()).doubleValue());
                plm.setA_LHeadCode("CS");
                plm.setA_LHeadNameName("Closing Stock");
                plm.setA_LAmount(DmJasper.formatAmount(amtTransaction));
                assetTotal += amtTransaction.doubleValue();
            }

            if (assetTotal > liabilityTotal) {
                amtTransaction = BigDecimal.valueOf(assetTotal);
            } else {
                amtTransaction = BigDecimal.valueOf(liabilityTotal);
            }

            plm = new BalanceSheetDisplayCompare_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setA_LHeadCode("TT");
            plm.setA_LHeadNameName("");
//            plm.setA_LAmount(DmJasper.formatAmount(amtTransaction));

            plm = new BalanceSheetDisplayCompare_Mdl();
            dvm.getProfitLossDisplayList().add(plm);
            plm.setA_LHeadCode("AS");
            plm.setA_LHeadNameName("ASSET");
            plm.setA_LAmount(DmJasper.formatAmount(amtTransaction));
            intAssetIndex = dvm.getProfitLossDisplayList().size();

            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 0) {
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
                        plm.setA_LDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                        plm.setA_LDrCr("Cr");
                    }
                    headBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("headBalance")).doubleValue());

                    plm.setA_LAmount(DmJasper.formatAmount(amtHead));
                    plm.setA_LHeadCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 1) {
                        plm.setA_LHeadNameName(DmShared.xNull(rsTemp.getString("GName")));
                    } else {
                        plm.setA_LHeadNameName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 1) {
                        assetTotal += headBalance.doubleValue();
                    } else {
                        liabilityTotal += headBalance.doubleValue();
                    }

//                        /*/*/*/*/**/*/*/*/*/*/*/*/**//*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/***/*/*/*/*/*/*/**/*/*/**//
                    lstHeadBalance = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastHeadBalance")).doubleValue());

                    if (DmShared.vNull(rsTemp.getString("LastAmtDr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtDr")).doubleValue());
                        plm.setLstA_LDrCr("Dr");
                    } else if (DmShared.vNull(rsTemp.getString("LastAmtCr")).doubleValue() > 0) {
                        amtHead = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("LastAmtCr")).doubleValue());
                        plm.setLstA_LDrCr("Cr");
                    }
                    lstHeadBalance = lstHeadBalance;
                    plm.setLstA_LAmount(DmJasper.formatAmount(amtHead));

                    if (addDetail) {
                        addDetails(acDisplaySetting, plm.getA_LHeadCode(), plm.getA_LHeadNameName(), dvm.getProfitLossDisplayList(), 1, cnTemp);
                    }

                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 0) {
                        lstLiabilityTotal += lstHeadBalance.doubleValue();
                    } else {
                        lstAssetTotal += lstHeadBalance.doubleValue();
                    }

                    difference += DmShared.vNull(DmServer.vNull(headBalance.doubleValue(), "0.00")).doubleValue();
                    lstDifference += DmShared.vNull(DmServer.vNull(lstHeadBalance.doubleValue(), "0.00")).doubleValue();
                }
            }

            //===================== Total of Expenses and Revenue ==============================
            if (assetTotal < 0) {
                dvm.getProfitLossDisplayList().get(intLiabilityIndex - 1).setA_LDrCr("Cr");
            } else if (assetTotal > 0) {
                dvm.getProfitLossDisplayList().get(intLiabilityIndex - 1).setA_LDrCr("Dr");
            }
            assetTotal = Math.abs(assetTotal);
            amtTransaction = BigDecimal.valueOf(assetTotal);
            dvm.getProfitLossDisplayList().get(intLiabilityIndex - 1).setA_LAmount(DmJasper.formatAmount(amtTransaction));

            if (liabilityTotal < 0) {
                dvm.getProfitLossDisplayList().get(intAssetIndex - 1).setA_LDrCr("Cr");
            } else if (liabilityTotal > 0) {
                dvm.getProfitLossDisplayList().get(intAssetIndex - 1).setA_LDrCr("Dr");
            }
            liabilityTotal = Math.abs(liabilityTotal);
            amtTransaction = BigDecimal.valueOf(liabilityTotal);
            dvm.getProfitLossDisplayList().get(intAssetIndex - 1).setA_LAmount(DmJasper.formatAmount(amtTransaction));

            if (lstAssetTotal < 0) {
                dvm.getProfitLossDisplayList().get(intLiabilityIndex - 1).setA_LDrCr("Cr");
            } else if (lstAssetTotal > 0) {
                dvm.getProfitLossDisplayList().get(intLiabilityIndex - 1).setA_LDrCr("Dr");
            }
            lstAssetTotal = Math.abs(lstAssetTotal);
            amtTransaction = BigDecimal.valueOf(lstAssetTotal);
            dvm.getProfitLossDisplayList().get(intLiabilityIndex - 1).setLstA_LAmount(DmJasper.formatAmount(amtTransaction));

            if (lstLiabilityTotal < 0) {
                dvm.getProfitLossDisplayList().get(intAssetIndex - 1).setA_LDrCr("Cr");
            } else if (lstLiabilityTotal > 0) {
                dvm.getProfitLossDisplayList().get(intAssetIndex - 1).setA_LDrCr("Dr");
            }
            lstLiabilityTotal = Math.abs(lstLiabilityTotal);
            amtTransaction = BigDecimal.valueOf(lstLiabilityTotal);
            dvm.getProfitLossDisplayList().get(intAssetIndex - 1).setLstA_LAmount(DmJasper.formatAmount(amtTransaction));

//===================== Fetching Profit & Loss ==============================
            acDisplaySetting.setShowDetail("0");
            netProfit = new ProfitLossDisplayCompare_SrvImpl().getNetProfit(acDisplaySetting, this);
            if (netProfit[0] != 0) {
                plm = getEmptyRowIndex(dvm.getProfitLossDisplayList(), netProfitLoss);
                plm.setA_LHeadNameName("Net Profit/Loss");
                if (netProfit[0] >= 0) {
                    amtHead = BigDecimal.valueOf(Math.abs(netProfit[0]));
                    plm.setA_LHeadCode("NL");
                    plm.setA_LDrCr("NL");
                    plm.setA_LAmount(DmJasper.formatAmount(amtHead));
                    assetTotal = Math.abs(assetTotal + Math.abs(netProfit[0]));
                } else {
                    amtTransaction = BigDecimal.valueOf(Math.abs(netProfit[0]));
                    plm.setA_LHeadCode("NP");
                    plm.setA_LDrCr("NP");
                    plm.setA_LAmount(DmJasper.formatAmount(amtTransaction));
                    liabilityTotal = Math.abs(liabilityTotal + Math.abs(netProfit[0]));
                }
            }

            if (netProfit[1] != 0) {
                if (netProfit[1] >= 0) {
                    plm.setLstA_LDrCr("NL");
                    amtHead = BigDecimal.valueOf(Math.abs(netProfit[1]));
                    plm.setLstA_LAmount(DmJasper.formatAmount(amtHead));
                    lstAssetTotal = Math.abs(lstAssetTotal + Math.abs(netProfit[1]));
                } else {
                    plm.setLstA_LDrCr("NP");
                    amtHead = BigDecimal.valueOf(Math.abs(netProfit[1]));
                    plm.setLstA_LAmount(DmJasper.formatAmount(amtHead));
                    lstLiabilityTotal = Math.abs(lstLiabilityTotal + Math.abs(netProfit[1]));
                }
            }

            //===========================================================================
            amtTransaction = BigDecimal.valueOf(Math.abs(assetTotal - liabilityTotal));
            if (assetTotal > liabilityTotal) {
                dvm.getProfitLossDisplayList().get(0).setA_LAmount(DmJasper.formatAmount(amtTransaction));
                dvm.getProfitLossDisplayList().get(0).setA_LDrCr("Dr");
            } else {
                dvm.getProfitLossDisplayList().get(0).setA_LAmount(DmJasper.formatAmount(amtTransaction));
                dvm.getProfitLossDisplayList().get(0).setA_LDrCr("Cr");
            }

            amtTransaction = BigDecimal.valueOf(Math.abs(lstAssetTotal - lstLiabilityTotal));
            if (lstAssetTotal > lstLiabilityTotal) {
                dvm.getProfitLossDisplayList().get(0).setLstA_LAmount(DmJasper.formatAmount(amtTransaction));
                dvm.getProfitLossDisplayList().get(0).setLstA_LDrCr("Dr");
            } else {
                dvm.getProfitLossDisplayList().get(0).setLstA_LAmount(DmJasper.formatAmount(amtTransaction));
                dvm.getProfitLossDisplayList().get(0).setLstA_LDrCr("Cr");
            }

            dvm.setTotal(DmJasper.formatAmount(amtTransaction));

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

    public void addDetails(AcDisplaySetting_Mdl acDisplaySetting, String acForCode, String acForName, List<BalanceSheetDisplayCompare_Mdl> profitLossList, double amount, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        BalanceSheetDisplayCompare_Mdl plm;
        try {
            psTemp = AcGroupDetailCompare_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySetting, DmShared.iNull(acForCode).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr"))) - DmShared.vNull(rsTemp.getString("amtCr")), "0.00"));
                plm = getEmptyRowIndex(profitLossList, amount);

                plm.setA_LChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                plm.setA_LChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                plm.setA_LChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                plm.setA_LChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                plm.setA_LHeadNameName(DmShared.xNull(acForName));
                plm.setA_LHeadCode(DmShared.iNull(acForCode).toString());
                plm.setA_LChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing)));

                double lstAmtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("LastAmtDr"))) - DmShared.vNull(rsTemp.getString("LastAmtCr")), "0.00"));

                plm.setLstA_LChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(lstAmtClosing)));
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
        return netProfitLoss;
    }

    private BalanceSheetDisplayCompare_Mdl getEmptyRowIndex(List<BalanceSheetDisplayCompare_Mdl> profitLossList, double amount) {
        int index = -1, i = 0;
        BalanceSheetDisplayCompare_Mdl profitLoss = null;

        if (profitLossList != null) {
            i = profitLossList.size() - 1;
            while (true) {
                if (i >= 0) {
                    profitLoss = profitLossList.get(i);
                    if (profitLoss != null) {
                        if (amount > 0) {
                            if (DmShared.xNull(profitLoss.getA_LHeadCode()).trim().equalsIgnoreCase("")) {
                                index = i;
                            } else {
                                break;
                            }
                        } else {
                            if (DmShared.xNull(profitLoss.getA_LHeadCode()).trim().equalsIgnoreCase("")) {
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
                profitLoss = new BalanceSheetDisplayCompare_Mdl();
                profitLossList.add(profitLoss);
                index = profitLossList.size() - 1;
            }

            profitLoss = profitLossList.get(index);
        }

        return profitLoss;
    }
}
