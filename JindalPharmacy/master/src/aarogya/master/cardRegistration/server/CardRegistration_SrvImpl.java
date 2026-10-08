package aarogya.master.cardRegistration.server;

import dataman.dmBase.self.shared.*;
import aarogya.master.cardRegistration.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.walletSystem.server.WalletSystem_Server;
import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Mdl;
import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Vld;
import aarogya.selfToolkit.walletSystem.shared.PersonDetail_Mdl;
import aarogya.selfToolkit.walletSystem.shared.TxtHCardType_Mdl;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CardRegistration_SrvImpl extends DmRSSPrivate implements CardRegistration_Srv {

    private final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherType = lvc.CARD_REGISTRATION;
    private final String voucherTypefieldName = "cr.v_Type", voucherDateFieldName = "cr.v_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstTmp;
        String sqlQuery, sqlCondition;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  cr.docId "
                    + "         from cardRegistration cr "
                    + "         left join voucher_Type vt on vt.v_Type = cr.v_Type "
                    + "         where vt.category in ('" + lvc.CARD_REGISTRATION + "') and cr.site_Code = :siteCode and cr.v_Prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlQuery += " order by cr.v_Date desc, vt.short_Name, cr.v_no desc, cr.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstTmp = new KeyFieldExtended();
                lstTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<CardRegistration_Mdl> getMoveRec(String keyCode) {
        DmResult<CardRegistration_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardRegistration_Mdl mrTemp = new CardRegistration_Mdl();
        PersonDetail_Mdl pdm;
        TxtHCardType_Mdl ctm;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  cr.docId, cr.recId, cr.v_No, cr.recIdPrefix, cr.v_Date, cr.cardNo, cr.expiryDate, cr.cardTypeCode, ctm.name as cardName, "
                    + "         ctm.onAmount, ctm.points, ctm.valuePerPoint, cr.uhid, pu.recId as uhidNo, cr.personName, cr.mobile, cr.eMail, cr.dob, "
                    + "         cr.doa, cr.remark, cr.initialPrefix, cr.address, cr.cityCode, ct.cityName, cr.u_Name, cr.preparedBy, cr.subCode, "
                    + "         sg.name as linkAccount, cr.physicalId, "
                    + "         case ctm.nature when 'V' then 'Value' else ctm.nature end as nature "
                    + "		from cardRegistration cr "
                    + "		left join patientUHID pu on pu.docId = cr.uhid "
                    + "		left join city ct on ct.cityCode = cr.cityCode "
                    + "		left join subGroup sg on sg.subCode = cr.subCode "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		where cr.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", keyCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                ctm = new TxtHCardType_Mdl();
                ctm.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                ctm.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardName")));
                ctm.setLinkAccount(DmShared.xNull(rsTemp.getString("linkAccount")));
                ctm.setOnAmount(DmServer.vNull(rsTemp.getString("onAmount"), "0.00"));
                ctm.setPoints(DmShared.xNull(rsTemp.getString("points")));
                ctm.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                ctm.setValuePerPoint(DmShared.xNull(rsTemp.getString("valuePerPoint")));
                ctm.setNature(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setCardMdl(ctm);
                pdm = new PersonDetail_Mdl();
                pdm.setAddress(DmShared.xNull(rsTemp.getString("address")));
                pdm.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                pdm.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                pdm.setDateDOA(DmShared.dNull(rsTemp.getDate("doa")));
                pdm.setDateDOB(DmShared.dNull(rsTemp.getDate("dob")));
                pdm.setDocIdUHID(DmShared.xNull(rsTemp.getString("uhid")));
                pdm.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                pdm.setInitial(DmShared.xNull(rsTemp.getString("initialPrefix")));
                pdm.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                pdm.setPerson(DmShared.xNull(rsTemp.getString("personName")));
                pdm.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhidNo")));
                mrTemp.setPersonMdl(pdm);
//                mrTemp.setBalance(STServer.getRegisteredWalletBalance(DmShared.xNull(rsTemp.getString("docId")), cnTemp));
                mrTemp.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                mrTemp.setPhysicalId(DmShared.xNull(rsTemp.getString("physicalId")));
                mrTemp.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                mrTemp.getRecIdReg().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdReg().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecIdReg().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));

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
    public DmResult<String> deleteRecord(UserPermission userPermission, String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select count(*) as cnt from cardLedger cl where cl.cardRegDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", keyCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Card is is in use. You can not delete it.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  count(cardRegDocId) as cnt "
                    + "         from generalReceipt gr "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
                    + "         left join cardLedger cl on cl.docId = gr.docId and cl.v_Sno = 1 "
                    + "         where vt.category in ('" + lvc.CARD_DEACTIVE + "') and cl.cardRegDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", keyCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "This card registration has been deactive. you can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "delete from cardRegistration where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", keyCode);
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
    public DmResult<String> saveRecord(UserPermission userPermission, CardRegistration_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;

        boolean blnAdd = false;

        dmrTemp.setData("");

        try {
            CardRegistration_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(mrTemp.getVoucherType(), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdReg(DmServer.getRecId("cardRegistration", "docId", mrTemp.getSharedKeyFieldCode(), "recId", mrTemp.getRecIdReg(),
                        mrTemp.getVoucherDate(), mrTemp.getVoucherType(), voucherPrefix, cnTemp, this, true));
            }
            new WalletSystem_Server(this).walletRegistration(mrTemp, voucherPrefix, blnAdd, cnTemp);

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, UserPermission userPermission, String vPrefix) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, sqlCondition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  cr.recId, cr.v_No, cr.v_Date, vt.short_Name, cr.cardNo, cr.personName, "
                    + "		cr.address, cr.expiryDate, ctm.name cardName, ct.cityName, cr.docId "
                    + "		from cardRegistration cr "
                    + "		left join voucher_Type vt on vt.v_Type = cr.v_Type "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		left join city ct on ct.cityCode = cr.cityCode "
                    + "		where cr.site_Code = :siteCode and cr.v_Prefix = :vPrefix and vt.category in ('" + lvc.CARD_REGISTRATION + "')";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", vPrefix);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
                listNode.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setDtAllotment(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setDtExpiry(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setPerson(DmShared.xNull(rsTemp.getString("personName")));
                listNode.setCardName(DmShared.xNull(rsTemp.getString("cardName")));
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
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  ct.cityCode, ct.cityName, sm.name as stateName, co.name as country,"
                    + "		concat(ct.cityName, ', ', sm.name, ', ', co.name) as city"
                    + "		from city ct "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("city")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setState(DmShared.xNull(rsTemp.getString("stateName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPrefix(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT NAME, CODE FROM initialmast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCardHelp(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select cs.cardNo "
                    + "		from cardStock cs "
                    + "		where cs.cardRegDocId is null and cs.site_Code = :siteCode ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardNo")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardNo")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLinkAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  sg.subCode, sg.name, ct.cityName, ag.groupName "
                    + "		from subGroup sg "
                    + "		left join acGroup ag on ag.groupCode = sg.groupCode "
                    + "		left join city ct on ct.cityCode = sg.cityCode "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		where sgsl.site_Code = :siteCode and sg.isActive = 1 ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("groupName")));
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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp.setSharedKeyFieldCode(DmServer.getVType(voucherType, cnTemp));
            mrTemp.setSharedKeyFieldName(DmServer.getServerDateTime()[0]);
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (DmException e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> getCardNo(String cardNo) {
        DmResult<String> dmrTemp = new DmResult<>();
        dmrTemp.setData("");
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  cs.cardRegDocId, cs.cardNo "
                    + "		from cardStock cs "
                    + "		where cs.cardNo = :cardNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cardNo", cardNo);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cardRegDocId")).longValue() > 0) {
                    throw new DmException(0, "<b>" + DmShared.xNull(rsTemp.getString("cardNo")) + "</b> wallet/card no. is already alloted.");
                }
                cardNo = DmShared.xNull(rsTemp.getString("cardNo"));
            } else {
                throw new DmException(0, "Invalid Wallet/Card No.");
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(cardNo);
            dmrTemp.setStatus(true);
        } catch (SQLException | DmException e) {
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
    public DmResult<PersonDetail_Mdl> getUHIDDetails(String docIdUHID, String uhidRecId) {
        DmResult<PersonDetail_Mdl> dmrTemp = new DmResult();
        PersonDetail_Mdl mrTemp = new PersonDetail_Mdl();
        String[] currentDateTime;
        dmrTemp.setData(mrTemp);
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";

        try {
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(docIdUHID).longValue() > 0) {
                condition += " and pu.docId = :docId";
            } else if (!DmShared.xNull(uhidRecId).equalsIgnoreCase("")) {
                condition += " and pu.recId = :recId";
            } else if (condition.trim().equalsIgnoreCase("")) {
                condition += " and 1 = 0 ";
            }

            sqlQuery = "select	top 1 pu.docId, pu.recId "
                    + "		from patientUHID as pu "
                    + "         left join voucher_Type vt on vt.v_Type = pu.v_Type "
                    + "		where pu.isCancelled = 0 and isNull(pu.approvedBy,'')<>'' "
                    + "             and pu.isExpired = 0 and vt.category in ('" + pvc.UHID + "')" + condition
                    + "		order by pu.v_Date desc, vt.short_Name, pu.v_No desc, pu.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(docIdUHID).longValue() > 0) {
                psTemp.setString("docId", DmShared.xNull(docIdUHID));
            } else if (!DmShared.xNull(uhidRecId).equalsIgnoreCase("")) {
                psTemp.setString("recId", DmShared.xNull(uhidRecId));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDocIdUHID(DmShared.iNull(rsTemp.getString("docId")).toString());
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getDocIdUHID()).doubleValue() == 0) {
                throw new DmException(0, "No record found. Try Again.");
            }

            getPatientDetail(mrTemp, cnTemp);

            dmrTemp.setValue1(currentDateTime[0]);
            dmrTemp.setValue2(currentDateTime[1]);
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (SQLException | DmException e) {
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

    private void getPatientDetail(PersonDetail_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        String sqlQuery;
        try {
            sqlQuery = "select  pu.docId, pu.recId, pu.patientName, pu.cityCode, pu.eMail, pu.dob, pu.mobile, "
                    + "         CONCAT( pu.address1, ' ', pu.address2) AS address, ct.cityName "
                    + "         from patientUHID AS pu "
                    + "         left join city ct on ct.cityCode = pu.cityCode "
                    + "         where pu.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdUHID()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setDateDOB(DmShared.dNull(rsTemp.getDate("dob")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setPerson(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
            }
            psTemp.close();
            rsTemp.close();
        } catch (SQLException | DmException e) {
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
    }

    @Override
    public DmResultPageing<List<TxtHCardType_Mdl>> getTxtHCardType(DmHelper pageing, boolean blnToEndOfFile, String voucherDate) {
        DmResultPageing<List<TxtHCardType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCardType_Mdl> lstTemp = new ArrayList<>();
        TxtHCardType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        Calendar cal;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  ctm.code, ctm.name, ctm.shortName, ctm.onAmount, ctm.points, ctm.valuePerPoint, "
                    + "         case ctm.nature when 'V' then 'Value' else ctm.nature end as nature, sg.name as sgName, "
                    + "         ctm.subCode, ctm.validDays, ctm.isPhysicallyRequired "
                    + "		from cardTypeMast ctm "
                    + "		left join subGroup sg on sg.subCode = ctm.subCode "
                    + "		where ctm.isActive = 1 and cardType = :cardType ");
            sqlWithParam.setInteger("cardType", CardType.WALLET.getEnumValue());
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCardType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setLinkAccount(DmShared.xNull(rsTemp.getString("sgName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setOnAmount(DmServer.vNull(rsTemp.getString("onAmount"), "0.00"));
                listNode.setPoints(DmServer.vNull(rsTemp.getString("points"), "0"));
                listNode.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setValuePerPoint(DmServer.vNull(rsTemp.getString("valuePerPoint"), "0"));
                listNode.setPhysicallyRequired(DmShared.iNull(rsTemp.getString("isPhysicallyRequired")).intValue() == 1);
                cal = Calendar.getInstance();
                cal.setTime(DmShared.convertDate(voucherDate));
                cal.add(Calendar.DATE, DmShared.iNull(rsTemp.getString("validDays")).intValue());
                listNode.setExpiryDate(DmShared.dNull(cal.getTime()));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
}
