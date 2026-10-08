package dataman.accounts.acGroupDetail.server;

import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.acGroupDetail.shared.*;
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

public class AcGroupDetail_SrvImpl extends DmRSSPrivate implements AcGroupDetail_Srv {

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting, List<DisplayRoute_Mdl> displayRouteList) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery;
        double debit = 0, credit = 0, difference = 0, closing = 0;
        BigDecimal amtCr, amtDr, balance;
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
                    + " create  table " + tableName + " (groupCode bigInt, groupName varchar(100), subCode bigInt, subName varchar(100), amtDr float, amtCr float, amtClosing float)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            //Get AcGroupDetail PreparedStatement
            psTemp = AcGroupDetail_SrvImpl.getAcGroupDetailPreparedStatement(acDisplaySetting, DmShared.iNull(dvm.getAcForCode()).toString(), cnTemp, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("amtDr")).doubleValue()) - DmShared.vNull(rsTemp.getString("amtCr")).doubleValue(), "0.00"));
                sqlQuery = "insert into " + tableName + " (groupCode, groupName, subCode, subName, amtDr, amtCr,amtClosing) values (:groupCode, :groupName, :subCode, :subName, :amtDr, :amtCr,:amtClosing)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("groupCode", DmShared.iNull(rsTemp.getString("GroupCode")).toString());
                psTemp1.setString("groupName", DmShared.xNull(rsTemp.getString("groupName")));
                psTemp1.setString("subCode", DmShared.iNull(rsTemp.getString("subCode")).toString());
                psTemp1.setString("subName", DmShared.xNull(rsTemp.getString("subName")));
                psTemp1.setString("amtDr", DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                psTemp1.setString("amtCr", DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                psTemp1.setString("amtClosing", DmServer.vNull(amtClosing, "0.00"));
                psTemp1.executeUpdate();
                psTemp1.close();
                debit = DmShared.vNull(DmServer.vNull(debit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue());
                credit = DmShared.vNull(DmServer.vNull(credit, "0.00")) + Math.abs(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
                closing = DmShared.vNull(DmServer.vNull(closing, "0.00")) + (DmShared.vNull(amtClosing).doubleValue());
            }
            rsTemp.close();
            psTemp.close();
            amtDr = BigDecimal.valueOf(debit);
            amtCr = BigDecimal.valueOf(credit);
            dvm.setTotalDr(DmJasper.formatAmount(amtDr));
            dvm.setTotalCr(DmJasper.formatAmount(amtCr));
            dvm.setAmtCl(DmJasper.formatAmount(BigDecimal.valueOf(closing)));
            dvm.setClDrCr("Dr");
            if ((debit - credit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(debit, "0.00")) - DmShared.vNull(DmServer.vNull(credit, "0.00"));
                balance = BigDecimal.valueOf(difference);
                dvm.setBalance((DmJasper.formatAmount(balance)));
                dvm.setIsDrCr("Dr");
            } else if ((credit - debit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(credit, "0.00")) - DmShared.vNull(DmServer.vNull(debit, "0.00"));
                balance = BigDecimal.valueOf(difference);
                dvm.setBalance((DmJasper.formatAmount(balance)));
                dvm.setIsDrCr("Cr");
            }

            dvm.setDtTo(DmShared.dNull(acDisplaySetting.getDateTo()));
            dvm.setDtFrom(DmShared.dNull(acDisplaySetting.getDateFrom()));

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
    public DmResultPageing<List<AcGroupDetail_Mdl>> getAcGroupDetial(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended keyField) {
        DmResultPageing<List<AcGroupDetail_Mdl>> rstTemp = new DmResultPageing<>();
        List<AcGroupDetail_Mdl> lstTemp = new ArrayList<AcGroupDetail_Mdl>();
        AcGroupDetail_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        BigDecimal amtCr, amtDr, amtCl;
        String tableName;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            tableName = getTable(this);
            sqlWithParam.setSQL("SELECT groupCode, groupName, subCode, subName, amtDr, amtCr,amtClosing FROM " + tableName + " ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AcGroupDetail_Mdl();
                amtCr = BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
                amtDr = BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue());
                listNode.setCredit(DmJasper.formatAmount(amtCr));
                listNode.setDebit(DmJasper.formatAmount(amtDr));
                amtCl = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtClosing")).doubleValue());
                listNode.setClosingDrCr(amtCl.doubleValue() > 0 ? "Dr" : amtCl.doubleValue() < 0 ? "Cr" : "");
                amtCl = BigDecimal.valueOf(Math.abs(amtCl.doubleValue()));
                listNode.setClosing(DmJasper.formatAmount(amtCl));
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("GroupCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                listNode.setSubName(DmShared.xNull(rsTemp.getString("subName")));
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

    public static DmPreparedStatement getAcGroupDetailPreparedStatement(AcDisplaySetting_Mdl acDisplaySetting, String acForCode, Connection cnTemp, DmRSSPrivate rssPrivate) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", siteList = "", sqlHaving = "", sqlCondition = "";

        try {
            if (acDisplaySetting != null) {
                if (!DmShared.xNull(acDisplaySetting.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySetting.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(rssPrivate);
                }

                if (DmShared.iNull(acDisplaySetting.getShowZeroValueAc()).intValue() == 0) {
                    sqlHaving = " Having (Round(IsNull(Sum(LG.AmtDr),0),2) - Round(IsNull(Sum(LG.AmtCr),0),2)) <> 0 ";
                }

                if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                    sqlCondition = " and (lg.v_Date between :dateFrom and :dateTo) ";
                } else {
                    sqlCondition = " and (lg.v_Date <= :dateTo) ";
                }

                sqlCondition += " and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";

                if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    sqlCondition += " and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySetting.getLedgerAcCodeList()) + ") ";
                }

                //========== For Picking Only Current Year Data Of Expense / Revenue ==============
                sqlCondition += " and (Case When agp.GroupNature In (2,3,4,5) Then lg.v_Prefix else ";
                sqlCondition += " " + DmServer.getSsnVoucherPrefix(rssPrivate) + " End) = " + DmServer.getSsnVoucherPrefix(rssPrivate) + " ";
                //=================================================================================
            }

            sqlQuery = "SELECT"
                    + "   agp.groupCode"
                    + " , MAX(agp.groupName) AS groupName \n"
                    + " , MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.subCode ELSE NULL END) AS subCode \n"
                    + " , MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.Name ELSE agp.groupName END) AS subName \n"
                    + " , SUM(lg.amtDr) AS amtDr"
                    + " , SUM(lg.amtCr) AS amtCr \n"
                    + " FROM ("
                    + "		SELECT"
                    + "		  MAX(agp.groupCode) AS groupCode"
                    + "		, MAX(agp.groupName) AS groupName \n"
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
                    + "				WHERE :groupCode = ag.groupUnder \n"
                    + "		) AS agp \n"
                    + "		GROUP BY (CASE WHEN agp.lookUpCode IS NULL THEN agp.groupCode ELSE agp.lookUpCode END) \n"
                    + " ) AS agp \n"
                    + " LEFT JOIN subgroup sg ON sg.groupCode = agp.lookUpCode \n"
                    + " LEFT JOIN ledger lg ON lg.subCode = sg.subCode \n"
                    + " WHERE lg.isPosted = 1 \n"
                    + sqlCondition
                    + " GROUP BY agp.groupCode, (CASE WHEN agp.groupCode = :groupCode THEN sg.subCode ELSE NULL END) \n"
                    + sqlHaving
                    + " ORDER BY MAX(CASE WHEN agp.groupCode = :groupCode THEN sg.Name ELSE agp.groupName END) \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (acDisplaySetting != null) {
                if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 0) {
                    psTemp.setDate("dateFrom", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateFrom())));
                }
                psTemp.setDate("dateTo", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateTo())));
                psTemp.setStringList("siteCode", siteList);
                if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    psTemp.setStringList("subCode", acDisplaySetting.getLedgerAcCodeList());
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
