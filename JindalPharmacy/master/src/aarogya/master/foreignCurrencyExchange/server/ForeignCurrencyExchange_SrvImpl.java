package aarogya.master.foreignCurrencyExchange.server;

import aarogya.master.foreignCurrencyExchange.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ForeignCurrencyExchange_SrvImpl extends DmRSSPrivate implements ForeignCurrencyExchange_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "	Select   fce.exchangeDate as ExchangeDate "
                    + "                 		from currencyExchangeMast  fce"
                    + "	group by  fce.exchangeDate "
                    + "  order by fce.exchangeDate asc ");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("ExchangeDate")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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

    @Override
    public DmResult<ForeignCurrencyExchange_Mdl> getMoveRec(String strKey) {
        DmResult<ForeignCurrencyExchange_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ForeignCurrencyExchange_Mdl mrTemp = new ForeignCurrencyExchange_Mdl();
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	cm.exchangeDate,cm.currencyCode,cm.conversionRate, cr.name as currencyName,"
                    + "		concat(cm.u_Name ,' ', format(cm.u_EntDt,'dd-MMM-yyyy HH:mm')) as modifiedBy ,cr.countryLocale"
                    + "		from currencyExchangeMast cm "
                    + "		left join currency cr on cr.code = cm.currencyCode"
                    + "         where cm.exchangeDate =:exchangeDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("exchangeDate", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("exchangeDate")));
                mrTemp.setExchangeDate(DmShared.dNull(rsTemp.getDate("exchangeDate")));

                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("modifiedBy")));

                GrdForeignCurrencyExchange_Mdl node = new GrdForeignCurrencyExchange_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("currencyCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("currencyName")));
//                node.setBase(DmShared.xNull("= 1 " + DmShared.xNull(DmEnum.DmLocaleCountry.getLocaleCountry("countryLocale").getCurrencyShortName()) + ""));
                node.setBase(DmShared.xNull("= 1 " + DmShared.xNull(DmServer.baseDmLocaleCountry.getCurrencyShortName()) + ""));

                node.setRate(DmServer.vNull(rsTemp.getString("conversionRate"), "0.0000000"));
                mrTemp.getLstCurrencyExchange().add(node);
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
            dmrTemp.setStatus(true);
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

    @Override
    public DmResult<String> saveRecord(ForeignCurrencyExchange_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, userName;
        Timestamp timestamp;
        dmrTemp.setData("");
        ResultSet rsTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(this);
            timestamp = DmServer.getServerTimestamp();
            ForeignCurrencyExchange_Vld.chkSave(mrTemp, DmServer.getServerDateTime()[0]);
            if (mrTemp.getExchangeDate().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*============================<Check Duplicate data>=================================*/
            if (mrTemp.isNewMode()) {
                sqlQuery = "Select exchangeDate from currencyExchangeMast where exchangeDate = :exchangeDate ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("exchangeDate", DmShared.foriegnKey(mrTemp.getExchangeDate()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Exchange date already exists. ");
                }
                rsTemp.close();
                psTemp.close();
            }

            /*============================< Delete records from currencyExchangeMast >==============================*/
            sqlQuery = "Delete From currencyExchangeMast Where exchangeDate = :exchangeDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("exchangeDate", DmShared.dNull(mrTemp.getExchangeDate()));
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdForeignCurrencyExchange_Mdl node : mrTemp.getLstCurrencyExchange()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    /*====================================< Insert into currencyExchangeMast >====================================*/
                    sqlQuery = "Insert into currencyExchangeMast(exchangeDate, currencyCode, conversionRate ,u_Name, u_EntDt) "
                            + " values (:exchangeDate, :currencyCode, :conversionRate, :u_Name,:u_EntDt)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("exchangeDate", DmShared.dNull(mrTemp.getExchangeDate()));
                    psTemp.setString("currencyCode", DmShared.iNull(node.getSharedKeyFieldCode()).toString());

                    psTemp.setString("conversionRate", DmShared.vNull(node.getRate()).toString());
                    psTemp.setString("u_Name", userName);

                    psTemp.setTimestamp("u_EntDt", timestamp);
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getExchangeDate());
            spBegin.commit();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
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

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.convertDate(strKey).getTime() <= DmShared.convertDate(DmServer.getServerDateTime()[0]).getTime()) {
                throw new UnsupportedOperationException(" For Delete Exchange Date should be greater than  Current Date.");
            }

            sqlQuery = "Delete From currencyExchangeMast Where exchangeDate=:exchangeDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("exchangeDate", DmShared.xNull(strKey));
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        String sqlQuery;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  fce.exchangeDate  "
                    + "         from currencyExchangeMast fce"
                    + "         group by  fce.exchangeDate ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("exchangeDate")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("exchangeDate")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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

    @Override
    public DmResult<List<GrdForeignCurrencyExchange_Mdl>> getCurrencyExchangeList() {
        DmResult<List<GrdForeignCurrencyExchange_Mdl>> rstTemp = new DmResult<>();
        List<GrdForeignCurrencyExchange_Mdl> lstTemp = new ArrayList<GrdForeignCurrencyExchange_Mdl>();
        Connection cnTemp = null;
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp.addAll(fillForeignCurrencyList(cnTemp));

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return rstTemp;
    }

    private List<GrdForeignCurrencyExchange_Mdl> fillForeignCurrencyList(Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        GrdForeignCurrencyExchange_Mdl lstNode;
        List<GrdForeignCurrencyExchange_Mdl> lstTemp = new ArrayList<GrdForeignCurrencyExchange_Mdl>();

        try {

            sqlQuery = "Select	ctm.currencyCode, max(cm.name) as currencyName,max(cm.countryLocale)"
                    + "		from cardTypeMast as ctm "
                    + "		left join currency as cm on ctm.currencyCode = cm.code"
                    + "		where ctm.cardType = :cardType and ctm.isActive = 1"
                    + "		group by ctm.currencyCode"
                    + "		order by max(cm.name) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cardType", DmShared.iNull(CardType.FOREIGN_CURRENCY.getEnumValue()).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdForeignCurrencyExchange_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("currencyCode")).toString());
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("currencyName")));
                String conversionRate = getBestFitRate(DmShared.iNull(rsTemp.getString("currencyCode")).toString(), cnTemp);
                lstNode.setRate(DmShared.xNull(conversionRate));

//                lstNode.setBase(DmShared.xNull("= 1 " + DmShared.xNull(DmEnum.DmLocaleCountry.getLocaleCountry("countryLocale").getCurrencyShortName())+ ""));
                lstNode.setBase(DmShared.xNull("= 1 " + DmShared.xNull(DmServer.baseDmLocaleCountry.getCurrencyShortName()) + ""));

                lstTemp.add(lstNode);

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return lstTemp;
    }

    private String getBestFitRate(String currencyCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, conversionRate = "";

        try {
            sqlQuery = "Select	top 1 cem.currencyCode, cem.conversionRate"
                    + "		from currencyExchangeMast as cem"
                    + "		where   cem.currencyCode = :currencyCode"
                    + "		order by cem.exchangeDate desc";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("currencyCode", DmShared.iNull(currencyCode).toString());

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                conversionRate = DmServer.vNull(rsTemp.getString("conversionRate"), "0.0000000");
                currencyCode = DmShared.xNull(rsTemp.getString("currencyCode"));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return conversionRate;
    }

}
