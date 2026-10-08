package aarogya.master.cardTypeMaster.server;

import dataman.dmBase.self.shared.*;
import aarogya.master.cardTypeMaster.shared.*;
import aarogya.selfToolkit.stImportData.server.ImportCardType;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardTypeMaster_SrvImpl extends DmRSSPrivate implements CardTypeMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("Select code From cardTypeMast Order By name;");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Name From cardTypeMast ");
            helper.addSort("Name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResult<CardTypeMaster_Mdl> getMoveRec(String strKey) {
        DmResult<CardTypeMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardTypeMaster_Mdl mrTemp = new CardTypeMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  cm.code, cm.name, cm.shortName, cm.nature, cm.subCode, sg.name as subCodeName, cm.onAmount, cm.valuePerPoint, "
                    + "         cm.points,cm.isSmsAlert,cm.isEmailAlert, cm.isActive, cm.isPhysicallyRequired, cm.preparedBy, cm.u_Name, cm.cardType,"
                    + "         cm.validDays,cm.currencyCode,cr.name as currencyName,cm.isAccountEditable , "
                    + "         cm.isPOSIntegrationCode ,cm.isPOSIntegrationTypeCode ,cm.integrationParams "
                    + "         from cardTypeMast cm "
                    + "         left join subGroup sg on cm.subCode = sg.subCode "
                    + "         left join currency cr on cr.code = cm.currencyCode "
                    + "         where cm.code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setLinkedAccountCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setLinkedAccountName(DmShared.xNull(rsTemp.getString("SubCodeName")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("Nature")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("Nature")).trim().equalsIgnoreCase("V") ? "Value" : "Point");
                mrTemp.setActiveCode(DmShared.vNull(rsTemp.getString("isActive")).toString());
                mrTemp.setActiveName(DmShared.vNull(mrTemp.getActiveCode()) == 0 ? "No" : "Yes");
                mrTemp.setPhysicalIdReqCode(DmShared.vNull(rsTemp.getString("isPhysicallyRequired")).toString());
                mrTemp.setPhysicalIdReqName(DmShared.vNull(mrTemp.getPhysicalIdReqCode()) == 0 ? "No" : "Yes");
                mrTemp.setOnAmount(DmServer.vNull(rsTemp.getString("OnAmount"), "0.00"));
                mrTemp.setValuePerPoint(DmServer.vNull(rsTemp.getString("ValuePerPoint"), "0.00"));
                mrTemp.setPoints(DmServer.vNull(rsTemp.getString("Points"), "0.00"));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardType")));
                mrTemp.setCardTypeName(CardType.getValueOf(DmShared.iNull(mrTemp.getCardTypeCode()).intValue()).getEnumName());
                mrTemp.setSmsAlertCode(DmShared.xNull(rsTemp.getString("isSmsAlert")));
                mrTemp.setSmsAlertName(DmShared.iNull(rsTemp.getString("isSmsAlert")).intValue() == 1 ? "Yes" : "No");
                mrTemp.setEmailAlertCode(DmShared.xNull(rsTemp.getString("isEmailAlert")));
                mrTemp.setEmailAlertName(DmShared.iNull(rsTemp.getString("isEmailAlert")).intValue() == 1 ? "Yes" : "No");
                mrTemp.setValidDays(DmShared.xNull(rsTemp.getString("validDays")));

                mrTemp.setCurrencyCode(DmShared.xNull(rsTemp.getString("currencyCode")));
                mrTemp.setCurrencyName(DmShared.xNull(rsTemp.getString("currencyName")));
                mrTemp.setIsAccountEditableCode(DmShared.xNull(rsTemp.getString("isAccountEditable")));
                mrTemp.setIsAccountEditableName(DmShared.iNull(rsTemp.getString("isAccountEditable")).intValue() == 1 ? "Yes" : "No");
                //===============================<MJ>===================================================//
                mrTemp.setIsPOSIntegrationCode(DmShared.xNull(rsTemp.getString("isPOSIntegrationCode")));
                mrTemp.setIsPOSIntegrationName(DmShared.vNull(mrTemp.getIsPOSIntegrationCode()) == 0 ? "No" : "Yes");

                if (DmShared.iNull(rsTemp.getString("isPOSIntegrationTypeCode")).intValue() > 0) {
                    mrTemp.setIsPOSIntegrationTypeCode(DmShared.xNull(rsTemp.getString("isPOSIntegrationTypeCode")));
                    mrTemp.setIsPOSIntegrationTypeName(STEnum.POSIntegrationType.getViaKey(DmShared.iNull(mrTemp.getIsPOSIntegrationTypeCode()).intValue()).getName());
                }
                mrTemp.setIntegrationParams(DmShared.xNull(rsTemp.getString("integrationParams")));
                //===============================<******>===================================================//

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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, vShortName = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(DmShared.iNull(strKey).toString());
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select code from cardTypeMast where code = :code and shortName in ('CS', 'CTO', 'CC', 'CTR', 'ADJ')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                throw new UnsupportedOperationException("System define [Card Type]. You can not delete it.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  v_Type, short_Name "
                    + "		from voucher_Type vt "
                    + "		where concat(attachPaymentMode, '|') like :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", "%" + DmShared.iNull(strKey).toString() + "|%");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vShortName += "<li><b>" + DmShared.xNull(rsTemp.getString("short_Name")) + "</b></li>";
            }
            rsTemp.close();
            psTemp.close();

            if (!vShortName.equalsIgnoreCase("")) {
                throw new DmException(0, "Card Type is in use for payment mode in following voucher: <br/><br/> <ol>" + vShortName + "</ol>");
            }

            sqlQuery = "Select cardTypeCode From CardRegistration cs where cs.cardTypeCode = :code;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Card Type is in use. You can not delete it.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select cardTypeCode From CardLedger cl where cl.cardTypeCode = :code;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Card Type is in use. You can not delete it.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Delete From cardTypeMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

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
    public DmResult<String> saveRecord(CardTypeMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        try {
            CardTypeMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setShortName(DmShared.xNull(mrTemp.getShortName()).trim().replace(" ", ""));
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "cardTypeMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select code From cardTypeMast CC Where CC.Name=:name And CC.Code<>:code;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("name", mrTemp.getSharedKeyFieldName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Card Type already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select code From cardTypeMast CC Where CC.shortName=:shortName  And CC.Code<>:code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("shortName", mrTemp.getShortName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = " Insert Into cardTypeMast(Code,Name,shortName,Nature,isActive,isPhysicallyRequired,isSmsAlert,isEmailAlert, "
                        + " SubCode,preparedBy,U_EntDt,OnAmount,ValuePerPoint,Points,cardType,validDays,currencyCode,isAccountEditable ,"
                        + "isPOSIntegrationCode ,isPOSIntegrationTypeCode,integrationParams) "
                        + " Values(:Code,:Name,:shortName,:Nature,:isActive,:isPhysicallyRequired,:isSmsAlert,:isEmailAlert, "
                        + " :SubCode,:U_Name,:uEntDt,:OnAmount,:ValuePerPoint,:Points,:cardType,:validDays,:currencyCode, :isAccountEditable,"
                        + " :isPOSIntegrationCode ,:isPOSIntegrationTypeCode,:integrationParams) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("Code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("Nature", DmShared.xNull(mrTemp.getNatureCode()));
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveCode()) == 0 ? "0" : "1");
                psTemp.setString("isPhysicallyRequired", DmShared.vNull(mrTemp.getPhysicalIdReqCode()) == 0 ? "0" : "1");
                psTemp.setString("SubCode", DmShared.xNull(mrTemp.getLinkedAccountCode()));
                psTemp.setString("OnAmount", DmServer.vNull(mrTemp.getOnAmount(), "0.00"));
                psTemp.setString("ValuePerPoint", DmServer.vNull(mrTemp.getValuePerPoint(), "0.00"));
                psTemp.setString("Points", DmServer.vNull(mrTemp.getPoints(), "0.00"));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("cardType", DmShared.iNull(mrTemp.getCardTypeCode()).toString());
                psTemp.setString("isSmsAlert", DmShared.xNull(mrTemp.getSmsAlertCode()));
                psTemp.setString("isEmailAlert", DmShared.xNull(mrTemp.getEmailAlertCode()));
                psTemp.setString("validDays", DmShared.xNull(mrTemp.getValidDays()));
                psTemp.setString("currencyCode", DmShared.foriegnKey(mrTemp.getCurrencyCode()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("isAccountEditable", DmShared.xNull(mrTemp.getIsAccountEditableCode()));

                psTemp.setString("isPOSIntegrationCode", DmShared.xNull(mrTemp.getIsPOSIntegrationCode()));
                psTemp.setString("isPOSIntegrationTypeCode", DmShared.xNull(mrTemp.getIsPOSIntegrationTypeCode()));
                psTemp.setString("integrationParams", DmShared.xNull(mrTemp.getIntegrationParams()));

            } else {
                sqlQuery = "Update cardTypeMast Set  "
                        + "name = :name, "
                        + "shortName = :shortName, "
                        + "Nature = :Nature, "
                        + "isActive = :isActive, "
                        + "isPhysicallyRequired = :isPhysicallyRequired, "
                        + "SubCode = :SubCode,"
                        + "OnAmount = :OnAmount,"
                        + "ValuePerPoint = :ValuePerPoint,"
                        + "Points = :Points,"
                        + "U_Name = :U_Name,"
                        + "U_EntDt = :uEntDt,"
                        + " cardType = :cardType, "
                        + " isSmsAlert = :isSmsAlert, "
                        + " isEmailAlert = :isEmailAlert,"
                        + " validDays= :validDays, "
                        + " isAccountEditable = :isAccountEditable, "
                        + " currencyCode= :currencyCode , "
                        + " isPOSIntegrationCode =:isPOSIntegrationCode,"
                        + " isPOSIntegrationTypeCode =:isPOSIntegrationTypeCode,"
                        + " integrationParams =:integrationParams "
                        + "Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("Nature", DmShared.xNull(mrTemp.getNatureCode()));
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveCode()) == 0 ? "0" : "1");
                psTemp.setString("isPhysicallyRequired", DmShared.vNull(mrTemp.getPhysicalIdReqCode()) == 0 ? "0" : "1");
                psTemp.setString("SubCode", DmShared.xNull(mrTemp.getLinkedAccountCode()));
                psTemp.setString("OnAmount", DmServer.vNull(mrTemp.getOnAmount(), "0.00"));
                psTemp.setString("ValuePerPoint", DmServer.vNull(mrTemp.getValuePerPoint(), "0.00"));
                psTemp.setString("Points", DmServer.vNull(mrTemp.getPoints(), "0.00"));
                psTemp.setString("U_Name", DmServer.getSsnUserName(this));
                psTemp.setString("cardType", DmShared.iNull(mrTemp.getCardTypeCode()).toString());
                psTemp.setString("isSmsAlert", DmShared.xNull(mrTemp.getSmsAlertCode()));
                psTemp.setString("isEmailAlert", DmShared.xNull(mrTemp.getEmailAlertCode()));
                psTemp.setString("validDays", DmShared.xNull(mrTemp.getValidDays()));

                psTemp.setString("isPOSIntegrationCode", DmShared.xNull(mrTemp.getIsPOSIntegrationCode()));
                psTemp.setString("isPOSIntegrationTypeCode", DmShared.xNull(mrTemp.getIsPOSIntegrationTypeCode()));
                psTemp.setString("integrationParams", DmShared.xNull(mrTemp.getIntegrationParams()));

                psTemp.setString("isAccountEditable", DmShared.xNull(mrTemp.getIsAccountEditableCode()));
                psTemp.setString("currencyCode", DmShared.foriegnKey(mrTemp.getCurrencyCode()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

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

            sqlWithParam.setSQL("select  ctm.code, ctm.name, ctm.shortName, ctm.nature, ctm.onAmount, ctm.points, "
                    + "		ctm.valuePerPoint, ctm.subCode, sg.name as linkAccount,ctm.validDays, "
                    + "		case ctm.isActive when 1 then 'Yes' when 0 then 'No' end as isActive, "
                    + "		case ctm.isSmsAlert when 1 then 'Yes' when 0 then 'No' end as isSmsAlert, "
                    + "		case ctm.isEmailAlert when 1 then 'Yes' when 0 then 'No' end as eMailAlert, "
                    + "		case ctm.isPhysicallyRequired when 1 then 'Yes' when 0 then 'No' end as isPhysicallyRequired, "
                    + "		case ctm.cardType "
                    + "             when " + CardType.ADJUST.getEnumValue() + " then '" + CardType.ADJUST.getEnumName() + "' "
                    + "             when " + CardType.BANK.getEnumValue() + " then '" + CardType.BANK.getEnumName() + "' "
                    + "             when " + CardType.CASH.getEnumValue() + " then '" + CardType.CASH.getEnumName() + "' "
                    + "             when " + CardType.CREDIT_CARD.getEnumValue() + " then '" + CardType.CREDIT_CARD.getEnumName() + "' "
                    + "             when " + CardType.DIGITAL.getEnumValue() + " then '" + CardType.DIGITAL.getEnumName() + "' "
                    + "             when " + CardType.WALLET.getEnumValue() + " then '" + CardType.WALLET.getEnumName() + "' "
                    + "             when " + CardType.FOREIGN_CURRENCY.getEnumValue() + " then '" + CardType.FOREIGN_CURRENCY.getEnumName() + "' "
                    + "             end cardType "
                    + "		from cardTypeMast ctm "
                    + "		left join subGroup sg on sg.subCode = ctm.subCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setActive(DmShared.xNull(rsTemp.getString("isActive")));
                listNode.setPhysicallyRequired(DmShared.xNull(rsTemp.getString("isPhysicallyRequired")));
                listNode.setLinkedAccount(DmShared.xNull(rsTemp.getString("linkAccount")));
                listNode.setOnAmount(DmShared.xNull(rsTemp.getString("onAmount")));
                listNode.setValuePerPoint(DmShared.xNull(rsTemp.getString("valuePerPoint")));
                listNode.setCardTypeName(DmShared.xNull(rsTemp.getString("cardType")));
                listNode.setPoints(DmShared.xNull(rsTemp.getString("points")));
                listNode.setValidDays(DmShared.xNull(rsTemp.getString("validDays")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<LinkedAccount_Mdl>> getTxtHLinkedAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<LinkedAccount_Mdl>> rstTemp = new DmResultPageing<>();
        List<LinkedAccount_Mdl> lstTemp = new ArrayList<>();
        LinkedAccount_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select  sg.subCode, sg.name, ag.groupName, case when sg.cityCode is null then '' else "
                    + "		concat(ct.cityName, ', ', sm.name, ', ', co.name) end as cityName "
                    + "		from subGroup sg "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		left join acGroup ag on ag.groupCode = sg.groupCode "
                    + "		left join city ct on ct.cityCode = sg.cityCode "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :siteCode ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new LinkedAccount_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setGroup(DmShared.xNull(rsTemp.getString("groupName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Value");
            listNode.setSharedKeyFieldName("Value");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Point");
            listNode.setSharedKeyFieldName("Point");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select shortName From cardTypeMast ");
            helper.addSort("shortName", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCardType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        try {
            for (int i = 0; i < CardType.values().length; i++) {
                CardType cdn = CardType.values()[i];
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(cdn.getEnumValue()).toString());
                listNode.setSharedKeyFieldName(cdn.getEnumName());
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getCurrencyType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select cr.code ,cr.name from currency cr ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getHPOSIntegrationType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        try {
            for (STEnum.POSIntegrationType posIntegrationType : STEnum.POSIntegrationType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(posIntegrationType.getCode())));
                listNode.setSharedKeyFieldName(DmShared.xNull(posIntegrationType.getName()));
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = ImportData_Server.getCSVFormats(new ImportCardType().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "cardTypeMaster";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  ctm.code, ctm.name, ctm.shortName, ctm.onAmount, ctm.points, ctm.valuePerPoint, ctm.validDays, sg.name as linkedAc,"
                    + "         cur.name as currency,  "
                    + "		case ctm.nature when 'V' then 'Value' else ctm.nature end as nature,  "
                    + "		case ctm.isEmailAlert when 0 then 'No' else 'Yes' end isEmailAlert, "
                    + "		case ctm.isSmsAlert when 0 then 'No' else 'Yes' end isSmsAlert, "
                    + "		case ctm.isAccountEditable when 0 then 'No' else 'Yes' end isAccountEditable, "
                    + "		case ctm.isPhysicallyRequired when 0 then 'No' else 'Yes' end isPhysicallyRequired, "
                    + "		case ctm.isActive when 0 then 'No' else 'Yes' end isActive, "
                    + "		case ctm.cardType "
                    + "             when " + CardType.ADJUST.getEnumValue() + " then '" + CardType.ADJUST.getEnumName() + "' "
                    + "             when " + CardType.BANK.getEnumValue() + " then '" + CardType.BANK.getEnumName() + "' "
                    + "             when " + CardType.CASH.getEnumValue() + " then '" + CardType.CASH.getEnumName() + "' "
                    + "             when " + CardType.CREDIT_CARD.getEnumValue() + " then '" + CardType.CREDIT_CARD.getEnumName() + "' "
                    + "             when " + CardType.DIGITAL.getEnumValue() + " then '" + CardType.DIGITAL.getEnumName() + "' "
                    + "             when " + CardType.WALLET.getEnumValue() + " then '" + CardType.WALLET.getEnumName() + "' "
                    + "             when " + CardType.FOREIGN_CURRENCY.getEnumValue() + " then '" + CardType.FOREIGN_CURRENCY.getEnumName() + "' "
                    + "             end cardType "
                    + "		from cardTypeMast ctm "
                    + "		left join subGroup sg on sg.subCode = ctm.subCode "
                    + "         left join currency as cur on ctm.currencyCode =  cur.code"
                    + "		order by ctm.cardType, ctm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("shortName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("cardType")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("nature")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("linkedAc")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("onAmount")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("points")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("valuePerPoint")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("validDays")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isEmailAlert")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isSmsAlert")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isAccountEditable")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isPhysicallyRequired")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("currency")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
