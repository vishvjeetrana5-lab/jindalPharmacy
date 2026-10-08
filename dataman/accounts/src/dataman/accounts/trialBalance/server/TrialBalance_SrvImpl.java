package dataman.accounts.trialBalance.server;

import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acGroupDetail.server.AcGroupDetail_SrvImpl;
import dataman.dmBase.self.shared.*;
import dataman.accounts.trialBalance.shared.*;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class TrialBalance_SrvImpl extends DmRSSPrivate implements TrialBalance_Srv {

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        TrialBalance_Mdl tbm;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlHaving = "", sqlCondition = "", siteList = "";
        double debit = 0, credit = 0, difference = 0;
        BigDecimal amtCr, amtDr;
        dmrTemp.setData(dvm);
        boolean addDetail = false;

        try {

            if (acDisplaySetting != null) {
                if (!DmShared.xNull(acDisplaySetting.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySetting.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(this);
                }

                if (DmShared.iNull(acDisplaySetting.getShowZeroValueAc()).intValue() == 0) {
                    sqlHaving = "Having (Round(IsNull(Sum(LG.AmtDr),0),2) - Round(IsNull(Sum(LG.AmtCr),0),2)) <> 0 \n";
                }

                if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                    sqlCondition = "and (lg.v_Date between :dateFrom and :dateTo) \n";
                } else {
                    sqlCondition = "and (lg.v_Date <= :dateTo) \n";
                }

                sqlCondition += "and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") \n";

                if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    sqlCondition += "and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySetting.getLedgerAcCodeList()) + ") \n";
                }

                if (DmShared.iNull(acDisplaySetting.getShowDetail()).intValue() == 1) {
                    addDetail = true;
                }

                //========== For Picking Only Current Year Data Of Expense / Revenue ==============
                sqlCondition += "and (Case When AG.GroupNature In (2,3,4,5) Then lg.v_Prefix else \n";
                sqlCondition += "" + DmServer.getSsnVoucherPrefix(this) + " End) = " + DmServer.getSsnVoucherPrefix(this) + " \n";
                //=================================================================================
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
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode   \n "
                    + "LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1    \n"
                    + "LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder    \n"
                    + "Where LG.isPosted = 1 \n"
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
            debit = 0;
            credit = 0;
            while (rsTemp.next()) {

                tbm = new TrialBalance_Mdl();
                amtCr = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                amtDr = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());
                tbm.setCredit(DmJasper.formatAmount(amtCr));
                tbm.setDebit(DmJasper.formatAmount(amtDr));
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
                dvm.getLstTrialBalane().add(tbm);
                if (addDetail) {
                    addDetails(acDisplaySetting, tbm.getSharedKeyFieldCode(), tbm.getSharedKeyFieldName(), dvm.getLstTrialBalane(), cnTemp);
                }
            }
            rsTemp.close();
            psTemp.close();

            dvm.setDtTo(DmShared.dNull(acDisplaySetting.getDateTo()));
            dvm.setDtFrom(DmShared.dNull(acDisplaySetting.getDateFrom()));
            dvm.setAmtCr(DmJasper.formatAmount(BigDecimal.valueOf(credit)));
            dvm.setAmtDr(DmJasper.formatAmount(BigDecimal.valueOf(debit)));
            if ((debit - credit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(debit, "0.00")) - DmShared.vNull(DmServer.vNull(credit, "0.00"));
                dvm.setIsDifference("Difference In Trial Balance");
                dvm.setAmtDiff(DmJasper.formatAmount(BigDecimal.valueOf(difference)));
                dvm.setIsDrCr("Dr");
            } else if ((credit - debit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(credit, "0.00")) - DmShared.vNull(DmServer.vNull(debit, "0.00"));
                dvm.setIsDifference("Difference In Trial Balance");
                dvm.setAmtDiff(DmJasper.formatAmount(BigDecimal.valueOf(difference)));
                dvm.setIsDrCr("Cr");
            } else {
                dvm.setIsDifference("");
                dvm.setAmtDiff("");
                dvm.setIsDrCr("");
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
    public void addDetails(AcDisplaySetting_Mdl acDisplaySetting, String acForCode, String acForName, List<TrialBalance_Mdl> trialBalanceList, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TrialBalance_Mdl tbm = null;
        try {
            psTemp = AcGroupDetail_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySetting, DmShared.iNull(acForCode).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr"))) - DmShared.vNull(rsTemp.getString("amtCr")), "0.00"));
                tbm = new TrialBalance_Mdl();
                tbm.setChildGroupCode(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                tbm.setChildGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                tbm.setChildSubName(DmShared.xNull(rsTemp.getString("subName")));
                tbm.setChildSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                tbm.setChildBalance(DmJasper.formatAmount(BigDecimal.valueOf(amtClosing)));
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

}
