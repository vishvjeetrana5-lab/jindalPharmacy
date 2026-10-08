package aarogya.master.cardReceive.server;

import aarogya.master.cardReceive.shared.*;
import aarogya.selfToolkit.walletSystem.server.WalletSystem_Server;
import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardReceive_SrvImpl extends DmRSSPrivate implements CardReceive_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult<List<KeyFieldExtended>>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  distinct recDate "
                    + "         from cardStock "
                    + "         order by recDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstTmp = new KeyFieldExtended();
                lstTmp.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("recDate")));
                lstNavigator.add(lstTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
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
    public DmResult<CardReceive_Mdl> getMoveRec(String keyCode) {
        DmResult<CardReceive_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardReceive_Mdl mrTemp = new CardReceive_Mdl();

        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  cs.cardNo, cs.recDate, cs.u_Name"
                    + "		from cardStock cs "
                    + "		where cs.recDate = :recDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("recDate", DmServer.convertDate(keyCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("recDate")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
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
    public DmResult<String> deleteRecord(String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, useCards = "", recDate = "", cardRegDocId = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(recDate);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select  cardNo, recDate, cardRegDocId "
                    + "         from cardStock cs "
                    + "         where cardNo in (" + DmServer.getParamList(":cardNo", keyCode) + ") and cs.cardRegDocId is not null "
                    + "         order by cardNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("cardNo", keyCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                useCards += DmShared.xNull(rsTemp.getString("cardNo")) + " <br/>";
                cardRegDocId += "," + DmShared.iNull(rsTemp.getString("cardRegDocId")).toString();
                recDate = DmShared.dNull(rsTemp.getDate("recDate"));
            }
            rsTemp.close();
            psTemp.close();

            if (!useCards.equalsIgnoreCase("")) {
                throw new DmException(0, useCards + " are alloted. You can not delete it.");
            }

            if (!cardRegDocId.equalsIgnoreCase("")) {
                cardRegDocId = cardRegDocId.substring(1);

                sqlQuery = "select  cardNo "
                        + "         from cardLedger cl "
                        + "         where cardRegDocId in (" + DmServer.getParamList(":cardRegDocId", cardRegDocId) + ")"
                        + "         order by cardNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setStringList("cardRegDocId", cardRegDocId);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    useCards += DmShared.xNull(rsTemp.getString("cardNo")) + " <br/>";
                }
                rsTemp.close();
                psTemp.close();

                if (!useCards.equalsIgnoreCase("")) {
                    throw new DmException(0, useCards + " are in use. You can not delete it.");
                }
            }

            sqlQuery = "delete from cardStock where cardNo in (" + DmServer.getParamList(":cardNo", keyCode) + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("cardNo", keyCode);
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResult<String> saveRecord(CardReceive_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        CardRegistration_Mdl wallet = new CardRegistration_Mdl();
        String sqlQuery, cardNo = "";
        ResultSet rsTemp = null;
        int maxNo = 0, noOfCard = 0;
        dmrTemp.setData("");
        try {
            CardReceive_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            maxNo = DmShared.iNull(mrTemp.getCardNoFrom()).intValue();

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            noOfCard = DmShared.iNull(mrTemp.getCardNoTo()).intValue() - DmShared.iNull(mrTemp.getCardNoFrom()).intValue();
            for (int i = 1; i <= (noOfCard + 1); i++) {
                if (DmShared.iNull(mrTemp.getCardNoTo()).intValue() == 0 && DmShared.iNull(mrTemp.getCardNoFrom()).intValue() == 0) {
                    cardNo = mrTemp.getSharedKeyFieldName();
                } else {
                    cardNo = DmShared.xNull(mrTemp.getSharedKeyFieldName()) + DmServer.vNull(maxNo, mrTemp.getCounterFormat());
                    maxNo++;
                }
                cardNo = DmShared.removeSpace(cardNo.toUpperCase());

                if (DmShared.iNull(DmServer.getCodeViaManualCode(cardNo, "cardNo", "cardStock", "count(cardNo)", cnTemp)).intValue() > 0) {
                    throw new DmException(0, cardNo + " card is already generated.");
                }

                wallet.setCardNo(cardNo);
                wallet.setVoucherDate(DmShared.dNull(mrTemp.getSharedKeyFieldCode()));

                new WalletSystem_Server(this).generateCard(wallet, cnTemp);

            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select  cs.cardNo, cs.recDate, cr.v_date as allotmentDate, cr.personName "
                    + "		from cardStock cs "
                    + "         left join cardRegistration cr on cr.docId = cs.cardRegDocId "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		where cs.site_Code = :siteCode ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("recDate")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardNo")));
                listNode.setAllotmentDate(DmShared.dNull(rsTemp.getDate("allotmentDate")));
                listNode.setPerson(DmShared.xNull(rsTemp.getString("personName")));
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
    public DmResultPageing<List<KeyFieldExtended>> fillDeleteGrd(DmHelper pageing, boolean blnToEndOfFile, String recDate) {
        DmResultPageing<List<KeyFieldExtended>> dmrTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> mrTemp = new ArrayList<>();
        KeyFieldExtended node;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        dmrTemp.setData(mrTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  cardNo, recDate "
                    + "         from cardStock "
                    + "         where recDate = :recDate ");
            sqlWithParam.setDate("recDate", DmServer.convertDate(recDate));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CardNo")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CardNo")));
                node.setSharedModifiedBy(DmShared.dNull(rsTemp.getDate("RecDate")));
                mrTemp.add(node);
            }
            pageing.positioning(mrTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<GrdCardReceive_Mdl>> getReceiveCards(DmHelper pageing, boolean blnToEndOfFile, String recDate) {
        DmResultPageing<List<GrdCardReceive_Mdl>> dmrTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdCardReceive_Mdl> mrTemp = new ArrayList<>();
        GrdCardReceive_Mdl node;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        dmrTemp.setData(mrTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  cs.cardNo, cs.recDate, cr.v_Date as allotmentDate, cr.personName, ctm.name cardName "
                    + "		from cardStock cs "
                    + "         left join cardRegistration cr on cr.cardNo = cs.cardNo "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		where cs.recDate = :recDate ");
            sqlWithParam.setDate("recDate", DmServer.convertDate(recDate));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdCardReceive_Mdl();
                node.setCardName(DmShared.xNull(rsTemp.getString("cardName")));
                node.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                node.setAllotmentDate(DmShared.dNull(rsTemp.getDate("allotmentdate")));
                node.setAllotedPerson(DmShared.xNull(rsTemp.getString("personName")));
                mrTemp.add(node);
            }
            pageing.positioning(mrTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(pageing.getPaging());
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

}
