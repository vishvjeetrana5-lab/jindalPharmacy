package dataman.accounts.acGroupDetailCompare.server;

import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.acGroupDetailCompare.shared.*;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AcGroupDetailCompare_SrvImpl extends DmRSSPrivate implements AcGroupDetailCompare_Srv {

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySettingComparison, List<DisplayRoute_Mdl> displayRouteList) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery;
        double debit = 0, credit = 0, difference = 0, closing = 0;
        double lstDebit = 0, lstCredit = 0, lstDifference = 0, lstClosing = 0;
        BigDecimal amtCr, amtDr, balance, lstAmtCr, lstAmtDr, lstBalance;
        String tableName;

        try {
            dmrTemp.setData(dvm);
            if (displayRouteList != null) {
                if (DmShared.iNull(displayRouteList.get(displayRouteList.size() - 1).getCode()).longValue() > 0) {
                    dvm.setAcForCode(displayRouteList.get(displayRouteList.size() - 1).getCode());
                    dvm.setAcName(displayRouteList.get(displayRouteList.size() - 1).getName());
                }
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //Create Temporary Session Wise
            tableName = getTable(this);
            sqlQuery = "IF OBJECT_ID('tempdb.." + tableName + "') IS NOT NULL "
                    + " DROP TABLE " + tableName + " ;"
                    + " create  table " + tableName + " (groupCode bigInt, groupName varchar(50), subCode bigInt, subName varchar(50), amtDr float, amtCr float, amtClosing float, lastAmtDr float, lastAmtCr float, lastAmtClosing float)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            //Get AcGroupDetail PreparedStatement
            psTemp = AcGroupDetailCompare_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySettingComparison, DmShared.iNull(dvm.getAcForCode()).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr")).doubleValue()) - DmShared.vNull(rsTemp.getString("amtCr")).doubleValue(), "0.00"));
                double lstAmtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("lastAmtDr")).doubleValue()) - DmShared.vNull(rsTemp.getString("lastAmtCr")).doubleValue(), "0.00"));
                sqlQuery = "insert into " + tableName + " (groupCode, groupName, subCode, subName, amtDr, amtCr,amtClosing,lastAmtDr, lastAmtCr, lastAmtClosing) "
                        + " values (:groupCode, :groupName, :subCode, :subName, :amtDr, :amtCr,:amtClosing,:lastAmtDr, :lastAmtCr, :lastAmtClosing) ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("groupCode", DmShared.iNull(rsTemp.getString("GroupCode")).toString());
                psTemp1.setString("groupName", DmShared.xNull(rsTemp.getString("groupName")));
                psTemp1.setString("subCode", DmShared.iNull(rsTemp.getString("subCode")).toString());
                psTemp1.setString("subName", DmShared.xNull(rsTemp.getString("subName")));
                psTemp1.setString("amtDr", DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                psTemp1.setString("amtCr", DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                psTemp1.setString("amtClosing", DmServer.vNull(amtClosing, "0.00"));

                psTemp1.setString("lastAmtDr", DmServer.vNull(rsTemp.getString("lastAmtDr"), "0.00"));
                psTemp1.setString("lastAmtCr", DmServer.vNull(rsTemp.getString("lastAmtCr"), "0.00"));
                psTemp1.setString("lastAmtClosing", DmServer.vNull(lstAmtClosing, "0.00"));
                psTemp1.executeUpdate();
                psTemp1.close();
//                debit = DmShared.vNull(DmServer.vNull(debit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue());
//                credit = DmShared.vNull(DmServer.vNull(credit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
//                closing = DmShared.vNull(DmServer.vNull(closing, "0.00")) + (DmShared.vNull(amtClosing).doubleValue());
//
//                lstDebit = DmShared.vNull(DmServer.vNull(lstDebit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtDr"), "0.00")).doubleValue());
//                lstCredit = DmShared.vNull(DmServer.vNull(lstCredit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtCr"), "0.00")).doubleValue());
//                lstClosing = DmShared.vNull(DmServer.vNull(lstClosing, "0.00")) + (DmShared.vNull(lstAmtClosing).doubleValue());
            }
            rsTemp.close();
            psTemp.close();
            amtDr = BigDecimal.valueOf(debit);
            amtCr = BigDecimal.valueOf(credit);

//            lstAmtDr = BigDecimal.valueOf(lstDebit);
//            lstAmtCr = BigDecimal.valueOf(lstCredit);
//            dvm.setTotalDr(DmJasper.formatAmount(amtDr));
//            dvm.setTotalCr(DmJasper.formatAmount(amtCr));
//            dvm.setLstTotalDr(DmJasper.formatAmount(lstAmtDr));
//            dvm.setLstTotalCr(DmJasper.formatAmount(lstAmtCr));
//
//            dvm.setAmtCl(DmJasper.formatAmount(BigDecimal.valueOf(closing)));
//            dvm.setClDrCr("Dr");
//            if ((debit - credit) > 0) {
//                difference = DmShared.vNull(DmServer.vNull(debit, "0.00")) - DmShared.vNull(DmServer.vNull(credit, "0.00"));
//                balance = BigDecimal.valueOf(difference);
//                dvm.setBalance((DmJasper.formatAmount(balance)));
//                dvm.setIsDrCr("Dr");
//            } else if ((credit - debit) > 0) {
//                difference = DmShared.vNull(DmServer.vNull(credit, "0.00")) - DmShared.vNull(DmServer.vNull(debit, "0.00"));
//                balance = BigDecimal.valueOf(difference);
//                dvm.setBalance((DmJasper.formatAmount(balance)));
//                dvm.setIsDrCr("Cr");
//            }
//
//            if ((lstDebit - lstCredit) > 0) {
//                lstDifference = DmShared.vNull(DmServer.vNull(lstDebit, "0.00")) - DmShared.vNull(DmServer.vNull(lstCredit, "0.00"));
//                lstBalance = BigDecimal.valueOf(lstDifference);
//                dvm.setLstBalance((DmJasper.formatAmount(lstBalance)));
//                dvm.setLstIsDrCr("Dr");
//            } else if ((lstCredit - lstDebit) > 0) {
//                lstDifference = DmShared.vNull(DmServer.vNull(lstCredit, "0.00")) - DmShared.vNull(DmServer.vNull(lstDebit, "0.00"));
//                lstBalance = BigDecimal.valueOf(lstDifference);
//                dvm.setLstBalance((DmJasper.formatAmount(lstBalance)));
//                dvm.setLstIsDrCr("Cr");
//            }
            dvm.setDtTo(DmShared.dNull(acDisplaySettingComparison.getDateTo()));
            dvm.setDtFrom(DmShared.dNull(acDisplaySettingComparison.getDateFrom()));

            dvm.setLstDtTo(DmShared.dNull(acDisplaySettingComparison.getLstDateTo()));
            dvm.setLstDtFrom(DmShared.dNull(acDisplaySettingComparison.getLstDateFrom()));

            cnTemp.close();
            dmrTemp.setData(dvm);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setStatus(false);
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
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
    public DmResultPageing<List<AcGroupDetailCompare_Mdl>> getAcGroupDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField) {
        DmResultPageing<List<AcGroupDetailCompare_Mdl>> rstTemp = new DmResultPageing<>();
        List<AcGroupDetailCompare_Mdl> lstTemp = new ArrayList<AcGroupDetailCompare_Mdl>();
        AcGroupDetailCompare_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        BigDecimal amtCr, amtDr, amtCl, lstAmtCr, lstAmtDr, lstAmtCl;
        double debitTot = 0, creditTot = 0, closingTot = 0;
        double lstDebitTot = 0, lstCreditTot = 0, lstClosingTot = 0;
        String tableName;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            tableName = getTable(this);
            sqlWithParam.setSQL("SELECT groupCode, groupName, subCode, subName, amtDr, amtCr,amtClosing, lastAmtDr, lastAmtCr,lastAmtClosing FROM " + tableName + " ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AcGroupDetailCompare_Mdl();
                amtCr = BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
                amtDr = BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue());
                lstAmtCr = BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtCr"), "0.00")).doubleValue());
                lstAmtDr = BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtDr"), "0.00")).doubleValue());
                listNode.setCredit(DmJasper.formatAmount(amtCr));
                listNode.setDebit(DmJasper.formatAmount(amtDr));
                listNode.setLstCredit(DmJasper.formatAmount(lstAmtCr));
                listNode.setLstDebit(DmJasper.formatAmount(lstAmtDr));
                amtCl = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtClosing")).doubleValue());
                lstAmtCl = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("lastAmtClosing")).doubleValue());
                listNode.setClosingDrCr(amtCl.doubleValue() > 0 ? "Dr" : amtCl.doubleValue() < 0 ? "Cr" : "");
                listNode.setLstClosingDrCr(lstAmtCl.doubleValue() > 0 ? "Dr" : lstAmtCl.doubleValue() < 0 ? "Cr" : "");
                amtCl = BigDecimal.valueOf(Math.abs(amtCl.doubleValue()));
                lstAmtCl = BigDecimal.valueOf(Math.abs(lstAmtCl.doubleValue()));
                listNode.setClosing(DmJasper.formatAmount(amtCl));
                listNode.setLstClosing(DmJasper.formatAmount(lstAmtCl));
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("GroupCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                listNode.setSubName(DmShared.xNull(rsTemp.getString("subName")));

                debitTot = DmShared.vNull(DmServer.vNull(debitTot, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue());
                creditTot = DmShared.vNull(DmServer.vNull(creditTot, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
                closingTot = DmShared.vNull(DmServer.vNull(closingTot, "0.00")) + DmShared.vNull(DmServer.vNull(rsTemp.getString("amtClosing"), "0.00")).doubleValue();

                lstDebitTot = DmShared.vNull(DmServer.vNull(lstDebitTot, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtDr"), "0.00")).doubleValue());
                lstCreditTot = DmShared.vNull(DmServer.vNull(lstCreditTot, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtCr"), "0.00")).doubleValue());
                lstClosingTot = DmShared.vNull(DmServer.vNull(lstClosingTot, "0.00")) + DmShared.vNull(DmServer.vNull(rsTemp.getString("lastAmtClosing"), "0.00")).doubleValue();

                lstTemp.add(listNode);
            }

            rsTemp.close();
            psTemp.close();

            listNode = new AcGroupDetailCompare_Mdl();

            amtCr = BigDecimal.valueOf(creditTot);
            amtDr = BigDecimal.valueOf(debitTot);
            lstAmtCr = BigDecimal.valueOf(lstCreditTot);
            lstAmtDr = BigDecimal.valueOf(lstDebitTot);

            amtCl = BigDecimal.valueOf(closingTot);
            lstAmtCl = BigDecimal.valueOf(lstClosingTot);
            listNode.setDebit(DmJasper.formatAmount(amtDr));
            listNode.setCredit(DmJasper.formatAmount(amtCr));
            listNode.setLstDebit(DmJasper.formatAmount(lstAmtDr));
            listNode.setLstCredit(DmJasper.formatAmount(lstAmtCr));
            listNode.setClosingDrCr(amtCl.doubleValue() > 0 ? "Dr" : amtCl.doubleValue() < 0 ? "Cr" : "");
            listNode.setLstClosingDrCr(lstAmtCl.doubleValue() > 0 ? "Dr" : lstAmtCl.doubleValue() < 0 ? "Cr" : "");
            amtCl = BigDecimal.valueOf(Math.abs(amtCl.doubleValue()));
            lstAmtCl = BigDecimal.valueOf(Math.abs(lstAmtCl.doubleValue()));
            listNode.setClosing(DmJasper.formatAmount(amtCl));
            listNode.setLstClosing(DmJasper.formatAmount(lstAmtCl));
            listNode.setSharedKeyFieldCode("TT");
            listNode.setSharedKeyFieldName("Total");
            listNode.setSubCode("TT");
            listNode.setSubName("Total");
            lstTemp.add(listNode);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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

    public static DmPreparedStatement getAcGroupDetailPreparedStatement(AcDisplaySetting_Mdl acDisplaySettingComparison, String acForCode, Connection cnTemp, DmRSSPrivate rssPrivate) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", cmnSqlQuery = "", siteList = "", sqlHaving = "", sqlCondition1 = "", sqlCondition2 = "", sqlGenCondition = "";

        try {
            if (acDisplaySettingComparison != null) {
                if (!DmShared.xNull(acDisplaySettingComparison.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySettingComparison.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(rssPrivate);
                }

                if (DmShared.iNull(acDisplaySettingComparison.getShowZeroValueAc()).intValue() == 0) {
                    sqlHaving = " Having (Round(IsNull(Sum(LG.AmtDr),0),2) - Round(IsNull(Sum(LG.AmtCr),0),2)) <> 0 ";
                }

                if (DmShared.iNull(acDisplaySettingComparison.getIncludeOpening()).intValue() == 0) {
                    sqlCondition1 = " and (lg.v_Date between :dateFrom and :dateTo) ";
                } else {
                    sqlCondition1 = " and (lg.v_Date <= :dateTo) ";
                }

                if (DmShared.iNull(acDisplaySettingComparison.getIncludeOpening()).intValue() == 0) {
                    sqlCondition2 = " and (lg.v_Date between :dateFrom2 and :dateTo2) ";
                } else {
                    sqlCondition2 = " and (lg.v_Date <= :dateTo2) ";
                }

                sqlGenCondition += " and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";

                if (!DmShared.xNull(acDisplaySettingComparison.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    sqlGenCondition += " and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySettingComparison.getLedgerAcCodeList()) + ") ";
                }

                //========== For Picking Only Current Year Data Of Expense / Revenue ==============
                if (DmShared.iNull(acDisplaySettingComparison.getIncludeOpening()).intValue() == 1) {
                    sqlCondition1 += " and (Case When agp.GroupNature In (2,3,4,5) Then lg.v_Prefix else ";
                    sqlCondition1 += " " + acDisplaySettingComparison.getYearPrefix() + " End) = " + acDisplaySettingComparison.getYearPrefix() + " ";

                    sqlCondition2 += " and (Case When agp.GroupNature In (2,3,4,5) Then lg.v_Prefix else ";
                    sqlCondition2 += " " + acDisplaySettingComparison.getYearPrefix2() + " End) = " + acDisplaySettingComparison.getYearPrefix2() + " ";
                    //=================================================================================
                }
            }

            cmnSqlQuery = "SELECT MAX(agp.groupCode) AS groupCode, MAX(agp.groupName) AS groupName \n"
                    + "		, (CASE WHEN agp.lookUpCode IS NULL THEN agp.groupCode ELSE agp.lookUpCode END) lookUpCode \n"
                    + "		, MAX(agp.GroupNature) AS GroupNature \n"
                    + "		FROM ( \n"
                    + "				SELECT ag.groupCode, ag.groupName, NULL AS lookUpCode, ag.GroupNature \n"
                    + "				FROM acGroup ag \n"
                    + "				WHERE :groupCode IN (ag.groupCode, ag.groupUnder) \n"
                    + ""
                    + "				UNION ALL \n"
                    + ""
                    + "				SELECT ag.groupCode, ag.groupName, agp.groupCode AS lookUpCode, ag.GroupNature \n"
                    + "				FROM acGroup ag \n"
                    + "				LEFT JOIN ( \n"
                    + "					SELECT agp.groupCode, agp.groupUnder \n"
                    + "					FROM acGroupPath agp \n"
                    + "				) AS agp ON agp.groupUnder = ag.groupCode \n"
                    + "				WHERE :groupCode = ag.groupUnder ";

            sqlQuery = "Select groupCode,max(groupName) as groupName,max(subCode) as subCode,max(subName) as subName \n"
                    + " ,sum(amtDr) as amtDr,sum(amtCr) as amtCr,sum(lastAmtDr) as lastAmtDr,sum(lastAmtCr) as lastAmtCr \n"
                    + " From ( "
                    + "SELECT agp.groupCode, MAX(agp.groupName) AS groupName \n"
                    + " , MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.subCode ELSE NULL END) AS subCode \n"
                    + " , MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.Name ELSE agp.groupName END) AS subName \n"
                    + " , SUM(lg.amtDr) AS amtDr, SUM(lg.amtCr) AS amtCr,0 as lastAmtDr,0 as lastAmtCr \n"
                    + " FROM ("
                    + cmnSqlQuery
                    + "		) AS agp \n"
                    + "		GROUP BY (CASE WHEN agp.lookUpCode IS NULL THEN agp.groupCode ELSE agp.lookUpCode END) \n"
                    + " ) AS agp \n"
                    + " LEFT JOIN subgroup sg ON sg.groupCode = agp.lookUpCode \n"
                    + " LEFT JOIN ledger lg ON lg.subCode = sg.subCode \n"
                    + " WHERE lg.isPosted = 1 \n"
                    + sqlCondition1 + sqlGenCondition
                    + " GROUP BY agp.groupCode, (CASE WHEN agp.groupCode = :groupCode THEN sg.subCode ELSE NULL END) \n"
                    + sqlHaving
                    //                    + " ORDER BY MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.Name ELSE agp.groupName END) \n";
                    + " Union All "
                    + "SELECT agp.groupCode, MAX(agp.groupName) AS groupName \n"
                    + " , MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.subCode ELSE NULL END) AS subCode \n"
                    + " , MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.Name ELSE agp.groupName END) AS subName \n"
                    + " ,0 as amtDr,0 as amtCr, SUM(lg.amtDr) AS lastAmtDr, SUM(lg.amtCr) AS lastAmtCr \n"
                    + " FROM ("
                    + cmnSqlQuery
                    + "		) AS agp \n"
                    + "		GROUP BY (CASE WHEN agp.lookUpCode IS NULL THEN agp.groupCode ELSE agp.lookUpCode END) \n"
                    + " ) AS agp \n"
                    + " LEFT JOIN subgroup sg ON sg.groupCode = agp.lookUpCode \n"
                    + " LEFT JOIN ledger lg ON lg.subCode = sg.subCode \n"
                    + " WHERE lg.isPosted = 1 \n"
                    + sqlCondition2 + sqlGenCondition
                    + " GROUP BY agp.groupCode, (CASE WHEN agp.groupCode = :groupCode THEN sg.subCode ELSE NULL END) \n"
                    + sqlHaving
                    + " ) as Main "
                    + " GROUP BY groupCode, (CASE WHEN groupCode = :groupCode THEN subCode ELSE NULL END) \n"
                    + " ORDER BY MAX(CASE WHEN groupCode = :groupCode THEN subName ELSE groupName END) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (acDisplaySettingComparison != null) {
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
            }
            psTemp.setString("groupCode", DmShared.iNull(acForCode).toString());

        } catch (Exception e) {
            throw e;
        }

        return psTemp;
    }

    public String getTable(DmRSSPrivate rssPrivate) {
        String sessionName;

        sessionName = (rssPrivate.getLocalRequest().getSession().getId()).replaceAll("-", "_");

        return "##t" + sessionName + "_" + "AcGroupDetailDisplay";
    }

}
