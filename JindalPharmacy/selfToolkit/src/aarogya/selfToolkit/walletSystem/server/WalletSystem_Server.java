package aarogya.selfToolkit.walletSystem.server;

import aarogya.selfToolkit.walletSystem.shared.CardRegistration_Mdl;
import aarogya.selfToolkit.walletSystem.shared.PersonDetail_Mdl;
import aarogya.selfToolkit.walletSystem.shared.TxtHCardType_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;

public class WalletSystem_Server {

    private DmRSSPrivate rss;
    private final LibraryVoucherCategory lvc = new LibraryVoucherCategory();

    public WalletSystem_Server(DmRSSPrivate rss) {
        this.rss = rss;
    }

    public void createWallet(CardRegistration_Mdl mrTemp, String voucherPrefix, boolean blnAdd, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TxtHCardType_Mdl card = null;
        String sqlQuery;
        try {
            if (!DmShared.xNull(mrTemp.getCardNo()).equalsIgnoreCase("")) {
                sqlQuery = "select cardNo from cardStock where cardNo = :cardNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("cardNo", mrTemp.getCardNo());
                rsTemp = psTemp.executeQuery();
                if (!rsTemp.next()) {
                    generateCard(mrTemp, cnTemp);
                    card = getCardModel(mrTemp.getCardMdl(), mrTemp.getVoucherDate(), cnTemp);
                    mrTemp.setCardMdl(card);
                    mrTemp.setExpiryDate(card.getExpiryDate());
                }
                rsTemp.close();
                psTemp.close();
                walletRegistration(mrTemp, voucherPrefix, blnAdd, cnTemp);
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
    }

    public void generateCard(CardRegistration_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "insert into cardStock (cardNo, recDate, site_Code, u_Name, u_EntDt) "
                    + "values (:cardNo, :recDate, :siteCode, :userName, :uEntDt)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cardNo", mrTemp.getCardNo());
            psTemp.setDate("recDate", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
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
    }

    public void walletRegistration(CardRegistration_Mdl mrTemp, String voucherPrefix, boolean blnAdd, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PersonDetail_Mdl personModel = mrTemp.getPersonMdl();
        String sqlQuery;

        try {

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new DmException(0, "Invalid key.");
            }

            blnAdd = DmShared.iNull(DmServer.getCodeViaManualCode(mrTemp.getRecIdReg().getRecIdValue(), "recId", "cardRegistration", "docId", cnTemp)).longValue() == 0;
            sqlQuery = " select  count(recId) as cnt "
                    + "         from cardRegistration cr "
                    + "         where cr.docId <> :docId and cr.recId = :recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("recId", mrTemp.getRecIdReg().getRecIdValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Card Reg No. is already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  count(cardRegDocId) as cnt "
                    + "         from cardStock cs "
                    + "         where cs.cardRegDocId <> :docId and cardNo = :cardNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("cardNo", mrTemp.getCardNo());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Card No. already alloted.");
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
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "This card registration has been deactive. you can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(DmServer.getCodeViaManualCode(mrTemp.getCardMdl().getSharedKeyFieldCode(), "code", "cardTypeMast", "isActive", cnTemp)).intValue() == 0) {
                throw new DmException(0, "Card Type is not active.");
            }

            /*====================================< Check whether patient Alive/ Approved or not>====================================*/
            sqlQuery = "Select approvedBy, isExpired, isCancelled from patientUHID where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(personModel.getDocIdUHID()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                    throw new DmException(0, "UHID has been discarded. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("isExpired")).intValue() == 1) {
                    throw new DmException(0, "Patient has been expired. You can not modify this record.");
                }
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Patient is not Approved. You can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "insert into cardRegistration (docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, uhid, cardNo, cardTypeCode, "
                        + "             subCode, expiryDate, initialPrefix, personName, address, cityCode, mobile, eMail, dob, doa, remark, site_Code, "
                        + "             preparedBy, u_EntDt, physicalId)"
                        + "	values (:docId, :vType, :vNo, :recIdPrefix, :recId, :vPrefix, :vDate, :uhid, :cardNo, :cardTypeCode, :subCode, "
                        + "             :expiryDate, :initialPrefix, :person, :address, :cityCode, :mobile, :eMail, :dob, :doa, :remark, :siteCode, "
                        + "             :userName, :uEntDt, :physicalId)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("vType", mrTemp.getVoucherType());
                psTemp.setLong("vNo", mrTemp.getRecIdReg().getCounter());
                psTemp.setString("recIdPrefix", mrTemp.getRecIdReg().getPrefix());
                psTemp.setString("recId", mrTemp.getRecIdReg().getRecIdValue());
                psTemp.setString("vPrefix", voucherPrefix);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setDate("expiryDate", DmServer.convertDate(mrTemp.getExpiryDate()));
                psTemp.setString("cardNo", mrTemp.getCardNo());
                psTemp.setString("physicalId", DmShared.foriegnKey(mrTemp.getPhysicalId()));
                psTemp.setString("cardTypeCode", DmShared.foriegnKey(mrTemp.getCardMdl().getSharedKeyFieldCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCardMdl().getSubCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("uhid", DmShared.foriegnKey(personModel.getDocIdUHID()));
                psTemp.setString("initialPrefix", DmShared.xNull(personModel.getInitial()));
                psTemp.setString("person", DmShared.xNull(personModel.getPerson()));
                psTemp.setString("address", DmShared.xNull(personModel.getAddress()));
                psTemp.setString("cityCode", DmShared.foriegnKey(personModel.getCityCode()));
                psTemp.setString("mobile", DmShared.foriegnKey(personModel.getMobile()));
                psTemp.setString("eMail", DmShared.foriegnKey(personModel.getEmail()));
                psTemp.setDate("dob", DmServer.convertDate(personModel.getDateDOB()));
                psTemp.setDate("doa", DmServer.convertDate(personModel.getDateDOA()));
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            } else {
                sqlQuery = "update  cardRegistration set "
                        + "         expiryDate = :expiryDate, "
                        + "         initialPrefix = :initialPrefix, "
                        + "         personName = :person, "
                        + "         address = :address, "
                        + "         cityCode = :cityCode, "
                        + "         mobile = :mobile, "
                        + "         eMail = :eMail, "
                        + "         dob = :dob, "
                        + "         doa = :doa, "
                        + "         remark = :remark, "
                        + "         u_Name = :userName, "
                        + "         u_EntDt = :uEntDt "
                        + "  where  docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setDate("expiryDate", DmServer.convertDate(mrTemp.getExpiryDate()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("initialPrefix", DmShared.xNull(personModel.getInitial()));
                psTemp.setString("person", DmShared.xNull(personModel.getPerson()));
                psTemp.setString("address", DmShared.xNull(personModel.getAddress()));
                psTemp.setString("cityCode", DmShared.foriegnKey(personModel.getCityCode()));
                psTemp.setString("mobile", DmShared.foriegnKey(personModel.getMobile()));
                psTemp.setString("eMail", DmShared.foriegnKey(personModel.getEmail()));
                psTemp.setDate("dob", DmServer.convertDate(personModel.getDateDOB()));
                psTemp.setDate("doa", DmServer.convertDate(personModel.getDateDOA()));

            }

            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update cardStock set "
                    + " cardRegDocId = :docId, "
                    + " u_Name = :userName, "
                    + " u_EntDt = :uEntDt "
                    + " where cardNo = :cardNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("cardNo", mrTemp.getCardNo());
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
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
    }

    private TxtHCardType_Mdl getCardModel(TxtHCardType_Mdl mrTemp, String voucherDate, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Calendar cal = null;
        try {
            sqlQuery = "select  ctm.code, ctm.name, ctm.shortName, ctm.onAmount, ctm.points, ctm.valuePerPoint, "
                    + "         case ctm.nature when 'V' then 'Value' else ctm.nature end as nature, sg.name as sgName, "
                    + "         ctm.subCode, ctm.validDays, ctm.isPhysicallyRequired "
                    + "		from cardTypeMast ctm "
                    + "		left join subGroup sg on sg.subCode = ctm.subCode "
                    + "		where ctm.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setLinkAccount(DmShared.xNull(rsTemp.getString("sgName")));
                mrTemp.setNature(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setOnAmount(DmServer.vNull(rsTemp.getString("onAmount"), "0.00"));
                mrTemp.setPoints(DmServer.vNull(rsTemp.getString("points"), "0"));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setValuePerPoint(DmServer.vNull(rsTemp.getString("valuePerPoint"), "0"));
                mrTemp.setPhysicallyRequired(DmShared.iNull(rsTemp.getString("isPhysicallyRequired")).intValue() == 1);
                cal = Calendar.getInstance();
                cal.setTime(DmShared.convertDate(voucherDate));
                cal.add(Calendar.DATE, DmShared.iNull(rsTemp.getString("validDays")).intValue());
                mrTemp.setExpiryDate(DmShared.dNull(cal.getTime()));
            }
            rsTemp.close();
        } catch (Exception e) {
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

        return mrTemp;
    }
}
