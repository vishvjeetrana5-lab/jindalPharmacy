package dataman.accounts.acLedgerDetail.server;

import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.acLedgerDetail.shared.*;
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

public class AcLedgerDetail_SrvImpl extends DmRSSPrivate implements AcLedgerDetail_Srv {

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting, List<DisplayRoute_Mdl> displayRouteList) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery, siteList = "", sqlCondition = "", sqlConditionOP = "";
        double debit = 0, credit = 0, difference = 0, runningBalance = 0;
        BigDecimal amtCr, amtDr, balance;
        long orderIndex;
        int contraTextStartIndex = 0;
        String[] contraTextLines = null;
        String tableName;

        try {
            if (displayRouteList != null) {
                if (DmShared.iNull(displayRouteList.get(displayRouteList.size() - 1).getCode()).longValue() > 0) {
                    dvm.setAcForCode(displayRouteList.get(displayRouteList.size() - 1).getCode());
                    dvm.setAcName(displayRouteList.get(displayRouteList.size() - 1).getName());
                }
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            tableName = getTable(this);
            if (acDisplaySetting != null) {
                if (!DmShared.xNull(acDisplaySetting.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySetting.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(this);
                }

                sqlConditionOP = "and (lg.v_Date < :dateFrom) and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";
                sqlCondition = "and (lg.v_Date between :dateFrom and :dateTo) and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";

                sqlQuery = "Select Count(ag.GroupNature) As Cnt "
                        + "From SubGroup SG "
                        + "left join acGroup ag on ag.groupCode = sg.GroupCode "
                        + "Where SG.SubCode = :subCode and ag.GroupNature in (2,3,4,5) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.iNull(dvm.getAcForCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("Cnt")).intValue() > 0) {
                        sqlConditionOP += "and lg.v_Prefix = " + DmServer.getSsnVoucherPrefix(this) + " ";
                        sqlCondition += "and lg.v_Prefix = " + DmServer.getSsnVoucherPrefix(this) + " ";
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            // Temporary Table _AccountLedgerDisplay created
            sqlQuery = "IF OBJECT_ID('tempdb.." + tableName + "') IS NOT NULL "
                    + " DROP TABLE " + tableName + " ;"
                    + " create  table " + tableName + " (orderIndex bigint, docId bigInt, recId varchar(50), narration varchar(max), amtDr float, amtCr float, runningBalance float,\n"
                    + " v_Type smallInt, v_Desc varchar(50), v_date date, sno smallInt, contraText varchar(Max), serialNo smallInt, contraName varchar(max),\n"
                    + " refNo varchar(30), refDate date,mnuAttachedInModule varchar(100),mnuText varchar(100),v_Prefix tinyInt,site_Code smallint)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                sqlQuery = "";
            } else {
                sqlQuery = "SELECT '-1' as DocId, '' as Narration,\n"
                        + " (CASE WHEN (ISNULL(SUM(round(AmtDr,2)), 0) - ISNULL(SUM(round(LG.AmtCr,2)), 0)) > 0 THEN\n"
                        + " (ISNULL(SUM(round(AmtDr,2)), 0) - ISNULL(SUM(round(LG.AmtCr,2)), 0)) ELSE 0 END) AS AmtDr,\n"
                        + " (CASE WHEN (ISNULL(SUM(round(AmtCr,2)), 0) - ISNULL(SUM(round(LG.AmtDr,2)), 0)) > 0 THEN\n"
                        + " (ISNULL(SUM(round(AmtCr,2)), 0) - ISNULL(SUM(round(LG.AmtDr,2)), 0)) ELSE 0 END) AS AmtCr,\n"
                        + " NULL AS V_No, NULL AS V_Type, NULL as v_Desc, NULL AS V_Date, 0 AS SNo, '' AS ContraText,\n"
                        + " 0 AS SerialNo, 'Opening' AS ContraName, NULL AS RefNo, NULL AS RefDate,\n"
                        + " null as mnuAttachedInModule,null as mnuText,null as v_Prefix,null as site_Code\n"
                        + " FROM Ledger LG\n"
                        + " WHERE LG.isPosted = 1 and lg.subCode = :subCode\n"
                        + sqlConditionOP
                        + " HAVING (ISNULL(SUM(round(AmtDr,2)), 0) - ISNULL(SUM(round(LG.AmtCr,2)), 0)) <> 0\n"
                        + "UNION ALL\n";
            }

            sqlQuery += "SELECT LG.DocId, \n"
                    + " (case when isNull(refNo,'')<>'' then 'Ref No. '+refNo+'    ' else '' end)\n"
                    + " +(case when refDate is not Null then 'Ref Dt. '+FORMAT (refDate, 'dd-MMM-yyyy ')+'    ' else '' end) +isNull(LG.Narration,'') as Narration, \n"
                    + " round(LG.AmtDr,2) as amtDr, round(LG.AmtCr,2) as amtCr, LG.RecId AS V_No,\n"
                    + " LG.V_Type, VT.short_Name as v_Desc, LG.V_Date, 1 AS SNo, LG.ContraText, VT.SerialNo, SG.Name AS ContraName,\n"
                    + " LG.RefNo, LG.RefDate,vt.mnuAttachedInModule,vt.mnuText,lg.v_Prefix,lg.site_Code\n"
                    + " FROM Ledger LG\n"
                    + " LEFT JOIN Voucher_Type VT ON LG.V_Type = VT.V_Type\n"
                    + " LEFT JOIN SubGroup SG ON LG.ContraSub = SG.SubCode\n"
                    + " WHERE LG.isPosted = 1 and lg.subCode = :subCode\n"
                    + sqlCondition
                    + " ORDER BY SNo, V_Date, SerialNo, V_No ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("dateFrom", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateFrom())));
            psTemp.setDate("dateTo", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateTo())));
            psTemp.setStringList("siteCode", siteList);
            if (!DmShared.xNull(dvm.getAcForCode()).trim().equalsIgnoreCase("")) {
                psTemp.setString("subCode", DmShared.iNull(dvm.getAcForCode()).toString());
            } else {
                if (acDisplaySetting.getLedgerAcCodeList().indexOf(",") > 0) {
                    throw new UnsupportedOperationException("Multiple account selection not allowed!");
                } else {
                    psTemp.setString("subCode", DmShared.iNull(acDisplaySetting.getLedgerAcCodeList()).toString());
                }
            }

            rsTemp = psTemp.executeQuery();
            orderIndex = 0;
            runningBalance = 0;
            while (rsTemp.next()) {
                orderIndex++;
                runningBalance = runningBalance + (DmShared.vNull(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00")).doubleValue() - DmShared.vNull(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00")).doubleValue());
                contraTextLines = null;
                contraTextStartIndex = 0;
                if (DmShared.iNull(acDisplaySetting.getShowDetail()).intValue() == 1) {
                    contraTextLines = DmShared.xNull(rsTemp.getString("contraText")).split("[\n]");
                }

                sqlQuery = "insert into " + tableName + " (orderIndex, docId, recId, narration, amtDr, amtCr, runningBalance, v_Type, v_Desc, v_date, sno, contraText,"
                        + " serialNo, contraName, refNo, refDate,mnuAttachedInModule,mnuText,v_Prefix,site_Code) "
                        + "values (:orderIndex, :docId, :recId, :narration, :amtDr, :amtCr, :runningBalance, :v_Type, :v_Desc, :v_date, :sno,"
                        + " :contraText, :serialNo, :contraName, :refNo, :refDate,:mnuAttachedInModule,:mnuText,:v_Prefix,:site_Code)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("orderIndex", String.valueOf(orderIndex));
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("docId")).toString());
                psTemp1.setString("recId", DmShared.xNull(rsTemp.getString("v_No")));
                psTemp1.setString("narration", DmShared.xNull(rsTemp.getString("narration")));
                psTemp1.setString("v_Type", DmShared.iNull(rsTemp.getString("v_Type")).toString());
                psTemp1.setString("v_desc", DmShared.xNull(rsTemp.getString("v_desc")));
                psTemp1.setDate("v_date", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("v_date"))));
                psTemp1.setString("sno", DmShared.iNull(rsTemp.getString("sno")).toString());
                psTemp1.setString("contraText", DmShared.xNull(rsTemp.getString("contraText")));
                psTemp1.setString("serialNo", DmShared.iNull(rsTemp.getString("serialNo")).toString());
                if (DmShared.iNull(acDisplaySetting.getShowDetail()).intValue() == 1) {
                    if (DmShared.iNull(acDisplaySetting.getShowNarration()).intValue() == 1
                            && !DmShared.xNull(rsTemp.getString("narration")).trim().equals("")) {
                        psTemp1.setString("contraName", DmShared.xNull(rsTemp.getString("narration")));
                        contraTextStartIndex = 0;
                    } else {
                        psTemp1.setString("contraName", DmShared.xNull(contraTextLines[0]));
                        contraTextStartIndex = 1;
                    }
                } else {
                    psTemp1.setString("contraName", DmShared.xNull(rsTemp.getString("contraName")));
                }
                psTemp1.setString("refNo", DmShared.xNull(rsTemp.getString("refNo")));
                psTemp1.setDate("refDate", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("refDate"))));
                psTemp1.setString("amtDr", DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                psTemp1.setString("amtCr", DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                psTemp1.setString("runningBalance", DmServer.vNull(runningBalance, "0.00"));
                psTemp1.setString("mnuAttachedInModule", DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                psTemp1.setString("mnuText", DmShared.xNull(rsTemp.getString("mnuText")));
                psTemp1.setString("v_Prefix", DmShared.iNull(rsTemp.getString("v_Prefix")).toString());
                psTemp1.setString("site_Code", DmShared.iNull(rsTemp.getString("site_Code")).toString());
                psTemp1.executeUpdate();
                psTemp1.close();
                debit = DmShared.vNull(DmServer.vNull(debit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00")).doubleValue());
                credit = DmShared.vNull(DmServer.vNull(credit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00")).doubleValue());

                if (DmShared.iNull(acDisplaySetting.getShowDetail()).intValue() != 1
                        && DmShared.iNull(acDisplaySetting.getShowNarration()).intValue() == 1
                        && !DmShared.xNull(rsTemp.getString("narration")).trim().equals("")) {
                    orderIndex++;
                    sqlQuery = "insert into " + tableName + " (orderIndex, docId,recId, contraName, mnuAttachedInModule, mnuText, v_Prefix, site_Code)"
                            + " values (:orderIndex, :docId, :recId, :contraName, :mnuAttachedInModule,:mnuText,:v_Prefix,:site_Code)";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("orderIndex", String.valueOf(orderIndex));
                    psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("docId")).toString());
                    psTemp1.setString("recId", DmShared.xNull(rsTemp.getString("v_No")));
                    psTemp1.setString("contraName", DmShared.xNull(DmShared.xNull(rsTemp.getString("narration"))));
                    psTemp1.setString("mnuAttachedInModule", DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                    psTemp1.setString("mnuText", DmShared.xNull(rsTemp.getString("mnuText")));
                    psTemp1.setString("v_Prefix", DmShared.iNull(rsTemp.getString("v_Prefix")).toString());
                    psTemp1.setString("site_Code", DmShared.iNull(rsTemp.getString("site_Code")).toString());
                    psTemp1.executeUpdate();
                    psTemp1.close();
                } else {
                    if (contraTextLines != null) {
                        for (int i = contraTextStartIndex; i < contraTextLines.length; i++) {
                            orderIndex++;
                            sqlQuery = "insert into " + tableName + " (orderIndex, docId, contraName, mnuAttachedInModule, mnuText, v_Prefix, site_Code)"
                                    + " values (:orderIndex, :docId, :contraName, :mnuAttachedInModule,:mnuText,:v_Prefix,:site_Code)";
                            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp1.setString("orderIndex", String.valueOf(orderIndex));
                            psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("docId")).toString());
                            psTemp1.setString("contraName", DmShared.xNull(contraTextLines[i]));
                            psTemp1.setString("mnuAttachedInModule", DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                            psTemp1.setString("mnuText", DmShared.xNull(rsTemp.getString("mnuText")));
                            psTemp1.setString("v_Prefix", DmShared.iNull(rsTemp.getString("v_Prefix")).toString());
                            psTemp1.setString("site_Code", DmShared.iNull(rsTemp.getString("site_Code")).toString());
                            psTemp1.executeUpdate();
                            psTemp1.close();
                        }
                    }
                }

            }
            rsTemp.close();
            psTemp.close();

            amtDr = BigDecimal.valueOf(debit);
            amtCr = BigDecimal.valueOf(credit);

            dvm.setTotalDr(DmJasper.formatAmount(amtDr));
            dvm.setTotalCr(DmJasper.formatAmount(amtCr));
            if ((debit - credit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(debit, "0.00")) - DmShared.vNull(DmServer.vNull(credit, "0.00"));

                balance = BigDecimal.valueOf(difference);

                dvm.setBalance((DmJasper.formatAmount(balance)));
                dvm.setIsDrCr("Dr");
                //dvm.setIsDrCr("Cr");
            } else if ((credit - debit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(credit, "0.00")) - DmShared.vNull(DmServer.vNull(debit, "0.00"));

                balance = BigDecimal.valueOf(difference);
                dvm.setBalance((DmJasper.formatAmount(balance)));
                // dvm.setIsDrCr("Dr");
                dvm.setIsDrCr("Cr");
            }

            dvm.setDtFrom(DmShared.dNull(acDisplaySetting.getDateFrom()));
            dvm.setDtTo(DmShared.dNull(acDisplaySetting.getDateTo()));
            dvm.setSiteCode(DmServer.getSsnSiteCode(this));
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
    public DmResultPageing<List<AcLedgerDetail_Mdl>> getAcLedgerDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField) {
        DmResultPageing<List<AcLedgerDetail_Mdl>> rstTemp = new DmResultPageing<>();
        List<AcLedgerDetail_Mdl> lstTemp = new ArrayList<AcLedgerDetail_Mdl>();
        AcLedgerDetail_Mdl listNode;
        DefaultValue_Mdl dvm;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
//        double credit = 0, debit = 0, difference = 0.00, opBal = 0, totalBal = 0.00;
//        BigDecimal amtCl, amtBal, totalBal1;
        double runningBalance = 0;
        String tableName;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            tableName = getTable(this);
            sqlWithParam.setSQL("select orderIndex, docId, ISNULL(recId, '') AS recId, narration, amtDr, amtCr, runningBalance, "
                    + "v_Type, v_Desc, v_date, sno, contraText,"
                    + "serialNo, contraName, refNo, refDate,mnuAttachedInModule,mnuText,v_Prefix,site_Code "
                    + "from " + tableName + " ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
//                credit = 0;
//                debit = 0;
//                difference = 0;

                listNode = new AcLedgerDetail_Mdl();
                if (DmShared.iNull(rsTemp.getString("sno")).intValue() != 0) {

                    listNode.setAmtCr(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue())));
                    listNode.setAmtDr(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue())));
                } else {
//                    opBal = DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() > 0
//                            ? DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue() : (0 - DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
//                    listNode.setAmtBalance(DmShared.vNull(opBal).toString());
                }

//                difference = DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue() - DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue();
//                totalBal = DmShared.vNull(DmServer.vNull(totalBal, "0.00")) + DmShared.vNull(DmServer.vNull(difference, "0.00"));
//                amtCl = BigDecimal.valueOf(DmShared.vNull(totalBal));
                runningBalance = DmShared.vNull(DmServer.vNull(rsTemp.getString("runningBalance"), "0.00")).doubleValue();
                listNode.setDrCr(runningBalance > 0 ? "Dr" : runningBalance < 0 ? "Cr" : "");
                listNode.setAmtBalance(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(Math.abs(runningBalance)))));

                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setParticulars(DmShared.xNull(rsTemp.getString("contraName")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("v_Desc")));
                listNode.setDtVoucher(DmShared.dNull(rsTemp.getDate("v_date")));
                listNode.setModuleName(DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                listNode.setMenuText(DmShared.xNull(rsTemp.getString("mnuText")));
                listNode.setV_Prefix(DmShared.xNull(rsTemp.getString("v_Prefix")));
                listNode.setSite_Code(DmShared.xNull(rsTemp.getString("site_Code")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
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

    private String getTable(DmRSSPrivate rssPrivate) {
        String sessionName;

        sessionName = (rssPrivate.getLocalRequest().getSession().getId()).replaceAll("-", "_");

        return "##t" + sessionName + "_" + "AcLedgerDetailDisplay";
    }
}
