package dataman.accounts.trialBalanceCompare.server;

import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acGroupDetailCompare.server.AcGroupDetailCompare_SrvImpl;
import dataman.dmBase.self.shared.*;
import dataman.accounts.trialBalanceCompare.shared.*;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class TrialBalanceCompare_SrvImpl extends DmRSSPrivate implements TrialBalanceCompare_Srv {

//    private String codifyAlgorithm = "AES";
//    private String codifyKey1 = "~!â‰¡s5*.d", codifyKey2 = "Tâ†�mÃ„nâ•¤sY";  
//    private String codifyKey1 = "aaa", codifyKey2 = "bbb";
    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySettingComparison) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        TrialBalanceCompare_Mdl tbm;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlHaving = "", sqlCondition1 = "", sqlCondition2 = "", siteList = "", sqlGenCondition = "";
        double debit = 0, credit = 0, differenceDr = 0, differenceCr = 0, lstDebit = 0, lstCredit = 0, lstDifferenceDr = 0, lstDifferenceCr = 0;
        BigDecimal amtCr, amtDr;
        dmrTemp.setData(dvm);
        boolean addDetail = false;

        try {
//            test();
            if (acDisplaySettingComparison != null) {
                if (!DmShared.xNull(acDisplaySettingComparison.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySettingComparison.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(this);
                }

                if (DmShared.iNull(acDisplaySettingComparison.getShowZeroValueAc()).intValue() == 0) {
                    sqlHaving = "Having (Round(IsNull(Sum(LG.AmtDr),0),2) - Round(IsNull(Sum(LG.AmtCr),0),2)) <> 0 \n";
                }

                if (DmShared.iNull(acDisplaySettingComparison.getIncludeOpening()).intValue() == 0) {
                    sqlCondition1 = "and (lg.v_Date between :dateFrom and :dateTo) \n";
                } else {
                    sqlCondition1 = "and (lg.v_Date <= :dateTo) \n";
                }

                if (DmShared.iNull(acDisplaySettingComparison.getIncludeOpening()).intValue() == 0) {
                    sqlCondition2 = "and (lg.v_Date between :dateFrom2 and :dateTo2) \n";
                } else {
                    sqlCondition2 = "and (lg.v_Date <= :dateTo2) \n";
                }

                sqlGenCondition += "and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") \n";

                if (!DmShared.xNull(acDisplaySettingComparison.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    sqlGenCondition += "and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySettingComparison.getLedgerAcCodeList()) + ") \n";
                }

                if (DmShared.iNull(acDisplaySettingComparison.getShowDetail()).intValue() == 1) {
                    addDetail = true;
                }

                //========== For Picking Only Current Year Data Of Expense / Revenue ==============
                sqlCondition1 += "and (Case When AG.GroupNature In (2,3,4,5) Then lg.v_Prefix else \n";
//                sqlCondition1 += "" + DmServer.getSsnVoucherPrefix(this) + " End) = " + DmServer.getSsnVoucherPrefix(this) + " \n";
                sqlCondition1 += "" + acDisplaySettingComparison.getYearPrefix() + " End) = " + acDisplaySettingComparison.getYearPrefix() + " \n";

                sqlCondition2 += "and (Case When AG.GroupNature In (2,3,4,5) Then lg.v_Prefix else \n";
//                sqlCondition2 += "" + DmServer.getSsnVoucherPrefix(this) + " End) = " + DmServer.getSsnVoucherPrefix(this) + " \n";
                sqlCondition2 += "" + acDisplaySettingComparison.getYearPrefix2() + " End) = " + acDisplaySettingComparison.getYearPrefix2() + " \n";
                //=================================================================================
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select GroupCode,max(GName) as GName,Sum(AmtDr) As AmtDr,Sum(AmtCr) as AmtCr,Sum(lastAmtDr) As lastAmtDr,Sum(lastAmtCr) as lastAmtCr, "
                    + "max(ContraGroupName) as ContraGroupName,GroupNature "
                    + "From ("
                    + "SELECT  (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  AS GroupCode,  \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) AS GName,  \n"
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) ELSE 0 END) AS AmtDr,   \n"
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) ELSE 0 END) AS AmtCr,   \n"
                    + "0 as lastAmtDr,0 as lastAmtCr, "
                    + "MAX(CASE when AG1.GroupCode is null then AG.ContraGroupName ELSE AG1.ContraGroupName END) AS ContraGroupName, \n"
                    + "MAX(AG.GroupNature) AS GroupNature \n"
                    + "FROM Ledger LG \n"
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode    \n"
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode   \n "
                    + "LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1    \n"
                    + "LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder    \n"
                    + "Where LG.isPosted = 1 \n"
                    + sqlCondition1
                    + sqlGenCondition
                    + "GROUP BY (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  \n"
                    + sqlHaving
                    //                    + "ORDER BY MAX(AG.GroupNature), \n"
                    //                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) \n";
                    + "Union All "
                    + "SELECT  (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  AS GroupCode,  \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.GroupName ELSE AG1.GroupName END) AS GName,  \n"
                    + "0 as AmtDr,0 as AmtCr, "
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) ELSE 0 END) AS lastAmtDr,   \n"
                    + "(CASE WHEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) > 0 THEN (ISNULL(SUM(LG.AmtCr), 0) - ISNULL(SUM(LG.AmtDr), 0)) ELSE 0 END) AS lastAmtCr,   \n"
                    + "MAX(CASE when AG1.GroupCode is null then AG.ContraGroupName ELSE AG1.ContraGroupName END) AS ContraGroupName, \n"
                    + "MAX(AG.GroupNature) AS GroupNature \n"
                    + "FROM Ledger LG \n"
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode    \n"
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode   \n "
                    + "LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1    \n"
                    + "LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder    \n"
                    + "Where LG.isPosted = 1 \n"
                    + sqlCondition2
                    + sqlGenCondition
                    + "GROUP BY (CASE when AG1.GroupCode is null THEN AG.GroupCode ELSE AG1.GroupCode END)  \n"
                    + sqlHaving
                    + ") As Main "
                    + "Group By GroupCode,GroupNature  ORDER BY MAX(GroupNature),GName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(acDisplaySettingComparison.getIncludeOpening()).intValue() == 0) {
                psTemp.setDate("dateFrom", DmServer.convertDate(DmShared.dNull(acDisplaySettingComparison.getDateFrom())));
                psTemp.setDate("dateFrom2", DmServer.convertDate(DmShared.dNull(acDisplaySettingComparison.getLstDateFrom())));
            }
            psTemp.setDate("dateTo", DmServer.convertDate(DmShared.dNull(acDisplaySettingComparison.getDateTo())));
            psTemp.setDate("dateTo2", DmServer.convertDate(DmShared.dNull(acDisplaySettingComparison.getLstDateTo())));

            psTemp.setStringList("siteCode", siteList);
            if (!DmShared.xNull(acDisplaySettingComparison.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", acDisplaySettingComparison.getLedgerAcCodeList());
            }
            rsTemp = psTemp.executeQuery();
            debit = 0;
            credit = 0;
            while (rsTemp.next()) {

                tbm = new TrialBalanceCompare_Mdl();
                amtCr = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                amtDr = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                tbm.setCredit(DmJasper.formatAmount(amtCr));
                tbm.setDebit(DmJasper.formatAmount(amtDr));
                tbm.setLastCredit(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("lastAmtCr")).doubleValue())));
                tbm.setLastDebit(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("lastAmtDr")).doubleValue())));
                tbm.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("GroupCode")).toString());
                tbm.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GName")));

                if (amtDr.doubleValue() > 0) {
                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 1
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 2
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 4) {
                        tbm.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GName")));

                    } else {
                        tbm.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                    }

                } else if (amtCr.doubleValue() > 0) {
                    if (DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 0
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 3
                            || DmShared.iNull(rsTemp.getString("groupNature")).intValue() == 5) {
                        tbm.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GName")));

                    } else {
                        tbm.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                    }
                }

                debit = DmShared.vNull(DmServer.vNull(debit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue());
                credit = DmShared.vNull(DmServer.vNull(credit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
                lstDebit = DmShared.vNull(DmServer.vNull(lstDebit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtDr"), "0.00")).doubleValue());
                lstCredit = DmShared.vNull(DmServer.vNull(lstCredit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtCr"), "0.00")).doubleValue());
                dvm.getLstTrialBalane().add(tbm);
                if (addDetail) {
                    addDetails(acDisplaySettingComparison, tbm.getSharedKeyFieldCode(), tbm.getSharedKeyFieldName(), dvm.getLstTrialBalane(), cnTemp);
                }
            }
            rsTemp.close();
            psTemp.close();

//            tbm = new TrialBalanceCompare_Mdl();
//            tbm.setSharedKeyFieldName("Difference In Trial Balance");
            if ((debit - credit) > 0) {
                differenceDr = DmShared.vNull(DmServer.vNull(debit, "0.00")) - DmShared.vNull(DmServer.vNull(credit, "0.00"));
//                tbm.setDebit(DmJasper.formatAmount(BigDecimal.valueOf(difference)));

            } else if ((credit - debit) > 0) {
                differenceCr = DmShared.vNull(DmServer.vNull(credit, "0.00")) - DmShared.vNull(DmServer.vNull(debit, "0.00"));
//                tbm.setCredit(DmJasper.formatAmount(BigDecimal.valueOf(difference)));
            } else {
//                tbm.setDebit("-");
//                tbm.setCredit("-");
            }

            if ((lstDebit - lstCredit) > 0) {
                lstDifferenceDr = DmShared.vNull(DmServer.vNull(lstDebit, "0.00")) - DmShared.vNull(DmServer.vNull(lstCredit, "0.00"));
//                tbm.setLastDebit(DmJasper.formatAmount(BigDecimal.valueOf(difference)));
            } else if ((lstCredit - lstDebit) > 0) {
                lstDifferenceCr = DmShared.vNull(DmServer.vNull(lstCredit, "0.00")) - DmShared.vNull(DmServer.vNull(lstDebit, "0.00"));
//                tbm.setLastCredit(DmJasper.formatAmount(BigDecimal.valueOf(difference)));
            } else {
//                tbm.setLastDebit("");
//                tbm.setLastCredit("");
            }
//            dvm.getLstTrialBalane().add(tbm);

            updateTotal_Difference(dvm,true, debit, credit, lstDebit, lstCredit);
            updateTotal_Difference(dvm,false, differenceDr, differenceCr, lstDifferenceDr, lstDifferenceCr);
            
            
            dvm.setDtTo(DmShared.dNull(acDisplaySettingComparison.getDateTo()));
            dvm.setDtFrom(DmShared.dNull(acDisplaySettingComparison.getDateFrom()));
//            dvm.setAmtCr(DmJasper.formatAmount(BigDecimal.valueOf(credit)));
//            dvm.setAmtDr(DmJasper.formatAmount(BigDecimal.valueOf(debit)));
            dvm.setLstDtTo(DmShared.dNull(acDisplaySettingComparison.getLstDateTo()));
            dvm.setLstDtFrom(DmShared.dNull(acDisplaySettingComparison.getLstDateFrom()));
//            dvm.setLstAmtCr(DmJasper.formatAmount(BigDecimal.valueOf(lstCredit)));
//            dvm.setLstAmtDr(DmJasper.formatAmount(BigDecimal.valueOf(lstDebit)));

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

    private void updateTotal_Difference(DefaultValue_Mdl dvm ,Boolean blnTot_Diff, Double amtDebit, Double amtCredit, Double amtLstDebit, Double amtLstCredit) {
        TrialBalanceCompare_Mdl tbm;
               tbm = new TrialBalanceCompare_Mdl();
        if (blnTot_Diff) {
            tbm.setSharedKeyFieldName("Total ");
            tbm.setSharedKeyFieldCode("TT");
            tbm.setDebit(DmJasper.formatAmount(BigDecimal.valueOf(amtDebit)));
            tbm.setCredit(DmJasper.formatAmount(BigDecimal.valueOf(amtCredit)));
            tbm.setLastDebit(DmJasper.formatAmount(BigDecimal.valueOf(amtLstDebit)));
            tbm.setLastCredit(DmJasper.formatAmount(BigDecimal.valueOf(amtLstCredit)));
        } else {
            tbm.setSharedKeyFieldName("Difference In Trial Balance ");
            tbm.setSharedKeyFieldCode("DF");
            tbm.setDebit(DmJasper.formatAmount(BigDecimal.valueOf(amtDebit)));
            tbm.setCredit(DmJasper.formatAmount(BigDecimal.valueOf(amtCredit)));
            tbm.setLastDebit(DmJasper.formatAmount(BigDecimal.valueOf(amtLstDebit)));
            tbm.setLastCredit(DmJasper.formatAmount(BigDecimal.valueOf(amtLstCredit)));
        }
        dvm.getLstTrialBalane().add(tbm);
    }

    public void addDetails(AcDisplaySetting_Mdl acDisplaySettingComparison, String acForCode, String acForName, List<TrialBalanceCompare_Mdl> trialBalanceList, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TrialBalanceCompare_Mdl tbm = null;
        try {
            psTemp = AcGroupDetailCompare_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySettingComparison, DmShared.iNull(acForCode).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr"))) - DmShared.vNull(rsTemp.getString("amtCr")), "0.00"));
                double lstAmtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("lastAmtDr"))) - DmShared.vNull(rsTemp.getString("lastAmtCr")), "0.00"));
                tbm = new TrialBalanceCompare_Mdl();
                tbm.setChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                tbm.setChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                tbm.setChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                tbm.setChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                tbm.setChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing)));
                tbm.setLastChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(lstAmtClosing)));
                tbm.setSharedKeyFieldName(DmShared.xNull(acForName));
                tbm.setSharedKeyFieldCode(DmShared.iNull(acForCode).toString());
                trialBalanceList.add(tbm);
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

//    public void test() {
//        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//
//        try {
//            cnTemp = DmServer.getConnection("sun_company");
//            sqlQuery = "select passwd from userMast where user_name = 'amit'";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                String str = dCodify(DmShared.xNull(rsTemp.getString("passwd")));
//                System.out.println(str);
//            }
//            cnTemp.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
//    public String dCodify(String stringToDCodify) throws Exception {
//        byte[] keyBytes = convertStringToBytes(codifyKey1 + codifyKey2);
//        Key key = new SecretKeySpec(keyBytes, codifyAlgorithm);
//        Cipher cipher = Cipher.getInstance(codifyAlgorithm);
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] decryptedValue64 = java.util.Base64.getDecoder().decode(stringToDCodify);
//        byte[] decryptedValue = cipher.doFinal(decryptedValue64);
//        String decryptedString = new String(decryptedValue, "utf-8");
//        return decryptedString;
//    }
//
//    private byte[] convertStringToBytes(String str) {
//        char[] charArray = str.toCharArray();
//        byte[] bytes = new byte[str.toCharArray().length];
//        for (int i = 0; i < charArray.length; i++) {
//            bytes[i] = (byte) charArray[i];
//        }
//        return bytes;
//    }
}
