package aarogya.master.cardDeactive.server;

import aarogya.master.cardDeactive.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardDeactivate_SrvImpl extends DmRSSPrivate implements CardDeactivate_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private final String vCat = "'" + pvc.CARD_DEACTIVE + "'";
    private final String voucherTypefieldName = "gr.v_Type", voucherDateFieldName = "gr.v_Date";

    @Override
    public DmResult<String> saveRecord(CardDeactivate_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            CardDeactivate_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVrTypeCode()), voucherPrefix, cnTemp, this));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (DmShared.xNull(mrTemp.getRecIdDeactive().getRecIdValue()).equalsIgnoreCase("")) {
                mrTemp.setRecIdDeactive(DmServer.getRecId("generalReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdDeactive(), mrTemp.getVrDate(), DmShared.xNull(mrTemp.getVrTypeCode()), voucherPrefix, cnTemp, this, true));
            }

            if (STServer.getCardRegistrationBalance(mrTemp.getCardRegMdl().getSharedKeyFieldCode(), cnTemp) != 0) {
                throw new DmException(0, "Card have balance. It can not be deactive.");
            }
            sqlQuery = "select  count(cl.cardRegDocId) as cnt "
                    + "         from generalReceipt gr "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
                    + "         left join cardLedger cl on cl.docId = gr.docId and v_Sno = 1 "
                    + "         where vt.category in (" + vCat + ") and cl.cardRegDocId = :cardDocId and gr.docId <> :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("cardDocId", mrTemp.getCardRegMdl().getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).longValue() > 0) {
                    throw new DmException(0, "Wallet/Card No. already deactive.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "insert into generalReceipt (docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, remark, u_EntDt, preparedBy, site_Code) values "
                    + " (:docId, :vType, :vNo, :recIdPrefix, :recId, :vPrefix, :vDate, :remark, :uEntDt, :userName, :siteCode) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("vType", mrTemp.getVrTypeCode());
            psTemp.setString("vNo", DmShared.iNull(mrTemp.getVrNo()).toString());
            psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdDeactive().getRecIdValue()));
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdDeactive().getPrefix()));
            psTemp.setString("vPrefix", voucherPrefix);
            psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVrDate()));
            psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("userName", userName);
            psTemp.setString("siteCode", siteCode);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into cardLedger(docId, v_SNo, v_Type, recId, v_Date, subCode, amtDr, amtCr, cardTypeCode, cardNo, cardHolderName, "
                    + "         cardAuthorization, site_Code, u_Name, u_EntDt, cardRegDocId) "
                    + " values (:docId, 1, :vType, :recId, :vDate, :subCode, 0, 0, :cardTypeCode, :cardNo, :contactPerson, :cardRegNo, :siteCode, "
                    + "         :userName, :uEntDt, :cardRegDocId)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("cardRegDocId", DmShared.foriegnKey(mrTemp.getCardRegMdl().getSharedKeyFieldCode()));
            psTemp.setString("vType", mrTemp.getVrTypeCode());
            psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdDeactive().getRecIdValue()));
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCardRegMdl().getLinkAccountCode()));
            psTemp.setString("cardTypeCode", DmShared.foriegnKey(mrTemp.getCardRegMdl().getCardTypeCode()));
            psTemp.setString("cardNo", DmShared.xNull(mrTemp.getCardRegMdl().getCardNo()));
            psTemp.setString("contactPerson", DmShared.xNull(mrTemp.getCardRegMdl().getContactPerson()));
            psTemp.setString("cardRegNo", DmShared.xNull(mrTemp.getCardRegMdl().getSharedKeyFieldName()));
            psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVrDate()));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("userName", userName);
            psTemp.setString("siteCode", siteCode);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update cardStock set cardRegDocId = null where cardRegDocId = :cardRegDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cardRegDocId", DmShared.foriegnKey(mrTemp.getCardRegMdl().getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT  vt.v_Type, vt.description"
                    + "         FROM voucher_Type vt "
                    + "         WHERE vt.short_Name = " + vCat + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherText(DmShared.xNull(rsTemp.getString("description")));
                defaultValue.setVoucherTag(DmShared.xNull(rsTemp.getString("v_Type")));

            }
            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
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
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstTmp;
        String sqlQuery = "", sqlCondition = "";
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  docId "
                    + "         from generalReceipt gr "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
                    + "         where gr.v_Prefix = :vPrefix and gr.site_Code = :siteCode and vt.category in (" + vCat + ") ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlQuery += " order by gr.v_Date desc, vt.short_Name, gr.v_no desc, gr.recId ";
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
    public DmResult<CardDeactivate_Mdl> getMoveRec(String keyCode) {
        DmResult<CardDeactivate_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDeactivate_Mdl mrTemp = new CardDeactivate_Mdl();
        RegisteredWallet_Mdl cardMdl;

        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  gr.docId, gr.v_Type, gr.v_No, gr.recIdPrefix, gr.recId, gr.v_Date, gr.remark, gr.u_Name, gr.preparedBy, gr.isCancelled, "
                    + "		cl.cardRegDocId, cr.recId as cardRegNo, cr.v_Date as allotedDate, cr.expiryDate, cl.subCode, sg.name linkAccount, "
                    + "         cl.cardNo, cl.cardHolderName, cl.amtCr, vt.description, ctm.name as cardTypeName, cr.mobile, cl.cardTypeCode,"
                    + "         cl.physicalId "
                    + "		from generalReceipt AS gr "
                    + "		left join cardLedger cl on cl.docId = gr.docId and cl.v_SNo = 1 "
                    + "		left join cardRegistration cr on cr.docId = cl.cardRegDocId "
                    + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                    + "		left join subGroup sg on sg.subCode = cl.subCode "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
                    + "         where gr.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setVrDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVrTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVrTypeName(DmShared.xNull(rsTemp.getString("description")));
                cardMdl = new RegisteredWallet_Mdl();
                cardMdl.setAllotmentDate(DmShared.dNull(rsTemp.getDate("allotedDate")));
                cardMdl.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                cardMdl.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                cardMdl.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                cardMdl.setContactPerson(DmShared.xNull(rsTemp.getString("cardHolderName")));
                cardMdl.setLinkAccountCode(DmShared.xNull(rsTemp.getString("subCode")));
                cardMdl.setLinkAccountName(DmShared.xNull(rsTemp.getString("linkAccount")));
                cardMdl.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                cardMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardRegDocId")));
                cardMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardRegNo")));
                cardMdl.setPhysicalId(DmShared.xNull(rsTemp.getString("physicalId")));
                mrTemp.setCardRegMdl(cardMdl);
            }
            psTemp.close();
            rsTemp.close();

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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition, condition = " ";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  gr.docId, gr.v_Type, gr.v_No, gr.recIdPrefix, gr.recId, gr.v_Date, gr.remark, gr.u_Name, gr.preparedBy, "
                    + "		cl.cardRegDocId, cr.recId as cardRegNo, cr.v_Date as allotedDate, cr.expiryDate, cl.subCode, sg.name linkAccount, "
                    + "         cl.cardHolderName, cl.amtDr, vt.description, ctm.name as cardTypeName, cr.mobile, cl.cardTypeCode, cr.cardNo, cr.personName "
                    + "		from generalReceipt AS gr "
                    + "		left join cardLedger cl on cl.docId = gr.docId and cl.v_SNo = 1 "
                    + "		left join cardRegistration cr on cr.docId = cl.cardRegDocId "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		left join subGroup sg on sg.subCode = cr.subCode "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
                    + "         where gr.site_Code = :site_Code and gr.v_prefix = :v_prefix and vt.category in (" + vCat + ") " + condition;
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_prefix", DmServer.getSsnVoucherPrefix(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVrDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setCardNoName(DmShared.xNull(rsTemp.getString("cardRegNo")));
                listNode.setCardNumber(DmShared.xNull(rsTemp.getString("cardNo")));
                listNode.setContactPerson(DmShared.xNull(rsTemp.getString("personName")));
                listNode.setLinkedACName(DmShared.xNull(rsTemp.getString("linkAccount")));
                listNode.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVrType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  vt.v_Type, vt.description "
                    + "         from voucher_Type vt "
                    + "         where vt.category in ('" + pvc.CARD_DEACTIVE + "') and vt.isActive = 1 ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResult<RegisteredWallet_Mdl> getCardDetail(String cardNo, String physicalId, String docId) {
        DmResult<RegisteredWallet_Mdl> dmrTemp = new DmResult<RegisteredWallet_Mdl>();
        Connection cnTemp = null;
        RegisteredWallet_Mdl mrTemp;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.iNull(docId).longValue() > 0) {
                cardNo = DmServer.getCodeViaManualCode(docId, "docId", "cardRegistration", "cardNo", cnTemp);
            }
            mrTemp = STServer.getRegisteredWallet(cardNo, physicalId, "", cnTemp);

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setStatus(false);
            dmrTemp.setErrorMessage(e.getMessage());
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
    public DmResultPageing<List<RegisteredWallet_Mdl>> getTxtHCardNo(String voucherDate, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<RegisteredWallet_Mdl>> rstTemp = new DmResultPageing<>();
        List<RegisteredWallet_Mdl> lstTemp = new ArrayList<>();
        RegisteredWallet_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  cr.docId, cr.recId, cr.v_Date, cr.v_No, vt.short_Name, cr.personName, cs.cardNo, "
                    + "		cr.mobile, ctm.name as cardTypeName "
                    + "		from cardStock cs "
                    + "		left join cardRegistration cr on cr.docId = cs.cardRegDocId "
                    + "		left join voucher_Type vt on vt.v_Type = cr.v_Type "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		where cs.cardRegDocId is not null and isnull(cr.expiryDate, :voucherDate) >= :voucherDate ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setDate("voucherDate", DmServer.convertDate(voucherDate));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new RegisteredWallet_Mdl();
                node.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                node.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setContactPerson(DmShared.xNull(rsTemp.getString("personName")));
                node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                lstTemp.add(node);
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

}
