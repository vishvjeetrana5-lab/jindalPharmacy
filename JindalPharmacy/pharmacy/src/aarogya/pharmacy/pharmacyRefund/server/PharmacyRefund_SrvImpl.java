package aarogya.pharmacy.pharmacyRefund.server;

import aarogya.pharmacy.ledgerMaster.server.LedgerMasterServer;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.partySearch.server.PartySearch_SrvImpl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.pharmacyRefund.shared.*;
import aarogya.selfToolkit.cardPaymentPopup.server.CardPaymentPopup_Server;
import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PharmacyRefund_SrvImpl extends DmRSSPrivate implements PharmacyRefund_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.PHARMACY_REFUND + "'";
    private final String voucherTypefieldName = "pr.v_Type", voucherDateFieldName = "pr.v_Date";

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            if (ipdAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.IPD);
                listNode.setSharedKeyFieldName(pvc.IPD);
                lstTemp.add(listNode);
            }

            if (opdAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.OPD);
                listNode.setSharedKeyFieldName(pvc.OPD);
                lstTemp.add(listNode);
            }
            if (dayCareAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
                listNode.setSharedKeyFieldName("Day Care");
                lstTemp.add(listNode);
            }
            if (emergencyAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
                listNode.setSharedKeyFieldName("Emergency");
                lstTemp.add(listNode);
            }
            if (ledgerAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Ledger");
                listNode.setSharedKeyFieldName("Ledger");
                lstTemp.add(listNode);
            }
            if (walkInAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Walk-In");
                listNode.setSharedKeyFieldName("Walk-In");
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
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT pr.docId"
                    + " FROM patientReceipt pr"
                    + " LEFT JOIN voucher_type vt ON vt.v_type = pr.v_type"
                    + " WHERE pr.site_Code = :site_Code"
                    + " AND vt.category IN (" + vCat + ")"
                    + " AND pr.v_prefix = :v_prefix";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + sqlCondition;
            }
            sqlQuery += " ORDER BY pr.v_Date DESC, vt.short_Name, pr.v_no DESC, pr.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("v_prefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();
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
    public DmResult<PharmacyRefund_Mdl> getMoveRec(String strKey) {
        DmResult<PharmacyRefund_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        CardPaymentPopup_Mdl cardPaymentPopup_Mdl = new CardPaymentPopup_Mdl();
        ResultSet rsTemp = null;
        String sqlQuery;
        PharmacyRefund_Mdl mrTemp = new PharmacyRefund_Mdl();
        dmrTemp.setData(mrTemp);
        double balance = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT COUNT(pr.docId) AS cnt"
                    + " FROM patientReceipt pr"
                    + " LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type"
                    + " WHERE pr.docId = :docId"
                    + " AND vt.category NOT IN (" + vCat + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next() && rsTemp.getLong("cnt") > 0) {
                throw new DmException(0, "Record is not generated from here. Thus can not be display.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT"
                    + "   pr.docId"
                    + " , pr.recId"
                    + " , pr.recIdPrefix"
                    + " , pr.v_No"
                    + " , pr.v_Date"
                    + " , pr.isCancelled"
                    + " , pr.amount"
                    + " , pr.remark"
                    + " , pr.u_Name"
                    + " , pr.preparedBy"
                    + " , vt.v_Type"
                    + " , vt.description"
                    + " , vt.attachPaymentMode"
                    + " , lm.code AS lmCode"
                    + " , lm.name AS lmName"
                    + " , lm.displayName"
                    + " , lm.manualCode AS lmManualCode"
                    + " , lm.mobile"
                    + " , lm.address"
                    + " , ct.cityCode"
                    + " , ct.cityName"
                    + " , hm.code AS idTypeCode"
                    + " , hm.name AS idTypeName"
                    + " , lm.idNo"
                    + " , lm.subcode"
                    + " , sg.manualCode AS sgManualCode"
                    + " , lm.regDocId"
                    + " , prg.recId AS regRecId"
                    + " , prgvt.category AS regVoucherCategory"
                    + " FROM patientReceipt pr"
                    + " LEFT JOIN voucher_Type AS vt ON vt.v_Type = pr.v_Type"
                    + " LEFT JOIN ledgerMast lm ON lm.code = pr.lmCode"
                    + " LEFT JOIN city ct ON ct.cityCode = lm.cityCode"
                    + " LEFT JOIN hl7Mast hm ON hm.code = lm.idTypeCode"
                    + " LEFT JOIN subGroup sg ON sg.subCode = lm.subCode"
                    + " LEFT JOIN patientRegistration prg ON prg.regDocId = lm.regDocId"
                    + " LEFT JOIN voucher_Type AS prgvt ON prgvt.v_Type = prg.v_Type"
                    + " WHERE pr.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.getRecIdRefund().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdRefund().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecIdRefund().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setIsCancel(DmShared.iNull(rsTemp.getString("isCancelled")).toString());
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setRecVDescription(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setRecVType(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setRecAttachPaymentMode(DmShared.xNull(rsTemp.getString("attachPaymentMode")));

                if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() != 0) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("regRecId")));
                    mrTemp.setCustomerTypeCode(DmShared.xNull(rsTemp.getString("regVoucherCategory")));
                    mrTemp.setCustomerTypeName(DmShared.xNull(rsTemp.getString("regVoucherCategory")));
                } else {
                    if (DmShared.iNull(rsTemp.getString("subCode")).longValue() != 0) {
                        mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("sgManualCode")));
                        mrTemp.setCustomerTypeCode("Ledger");
                        mrTemp.setCustomerTypeName("Ledger");
                    } else if (DmShared.iNull(rsTemp.getString("lmCode")).longValue() != 0) {
                        mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                        mrTemp.setCustomerTypeCode("Walk-In");
                        mrTemp.setCustomerTypeName("Walk-In");
                    }
                }

                mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                balance = STServer.getBalanceLedgerMast(DmShared.xNull(rsTemp.getString("lmCode")), DmServer.getSsnSiteCode(this), cnTemp);
                mrTemp.setPharmacyBalance(DmServer.vNull(balance, "0.00"));

                balance += DmShared.vNull(rsTemp.getString("amount"));
                mrTemp.setEntryBalance(DmServer.vNull(balance, "0.00"));
            }
            rsTemp.close();
            psTemp.close();

            cardPaymentPopup_Mdl = CardPaymentPopup_Server.getCardListViaDocId(strKey, cnTemp);
            mrTemp.getCardPayment().getGrdCardPaymentList().addAll(cardPaymentPopup_Mdl.getGrdCardPaymentList());
            mrTemp.setCardPayment(cardPaymentPopup_Mdl);

            cnTemp.close();
            dmrTemp.setData(mrTemp);
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
    public DmResult<String> deleteRecord(UserPermission userPermission, String keyCode, ModifyReason_Mdl modifyReasonMdl) {
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
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "SELECT pr.isCancelled, pr.site_Code, pr.v_Prefix"
                    + " FROM patientReceipt pr"
                    + " WHERE pr.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                    throw new DmException(0, "Record already discarded.");
                }
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(DmServer.getSsnSiteCode(this)).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not discard this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(DmServer.getSsnVoucherPrefix(this)).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not discard this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*============================< Check whether record generated here or not >=============================*/
            sqlQuery = "SELECT pr.docId"
                    + " FROM patientReceipt pr"
                    + " LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type"
                    + " WHERE pr.docId = :docId"
                    + " AND vt.category NOT IN (" + vCat + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Record is not generated from here. You can not discard this record.");
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Check wether record posted or not >====================================*/
            STServer.checkEOD("patientReceipt", "docId", keyCode, cnTemp);

            CardPaymentPopup_Server.cancel(keyCode, cnTemp);

            /*====================================< Update patientReceipt >====================================*/
            sqlQuery = "UPDATE patientReceipt SET isCancelled = 1 WHERE docId= :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (modifyReasonMdl != null) {
                ModifyReason_Server.save(modifyReasonMdl, keyCode, DmServer.getSsnUserName(this), cnTemp);
            }

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
    public DmResult<String> saveRecord(UserPermission userPermission, PharmacyRefund_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            PharmacyRefund_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            //<editor-fold defaultstate="collapsed" desc="Create Ledger If Required">
            if (DmShared.iNull(mrTemp.getLmCode()).longValue() == 0) {
                LedgerMaster_Mdl ledgerMdl = new LedgerMaster_Mdl();
                ledgerMdl.setManualCode(mrTemp.getManualCode());
                ledgerMdl.setDisplayName(mrTemp.getDisplayName());
                ledgerMdl.setAccountCode(mrTemp.getSubCode());
                ledgerMdl.setRegDocId(mrTemp.getRegDocId());
                ledgerMdl.setRegType(mrTemp.getCustomerTypeCode());
                ledgerMdl.setAddress(mrTemp.getAddress());
                ledgerMdl.setCityCode(mrTemp.getCityCode());
                ledgerMdl.setMobile(mrTemp.getMobile());
                ledgerMdl.setIdTypeCode(mrTemp.getIdTypeCode());
                ledgerMdl.setIdNo(mrTemp.getIdNo());
                LedgerMasterServer.saveRecord(ledgerMdl, cnTemp, this);
                mrTemp.setLmCode(ledgerMdl.getSharedKeyFieldCode());
            }
            //</editor-fold>

            if (mrTemp.getCardPayment().getPaymentSource() != STEnum.PaymentSource.PAYMENT) {
                throw new DmException(0, "Only receipt payment type is allow.");
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(DmShared.xNull(mrTemp.getCardPayment().getReceiptTypeCode())), voucherPrefix, cnTemp, this));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            if (DmShared.xNull(mrTemp.getRecIdRefund().getRecIdValue()).equalsIgnoreCase("")) {
                mrTemp.setRecIdRefund(DmServer.getRecId("patientReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdRefund(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getCardPayment().getReceiptTypeCode()), voucherPrefix, cnTemp, this, true));
            } else {
                DmServer.getRecId("patientReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdRefund(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getCardPayment().getReceiptTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            /*============================< Check whether record generated here or not >=============================*/
            sqlQuery = "SELECT pr.docId"
                    + " FROM patientReceipt pr"
                    + " LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type"
                    + " WHERE pr.docId = :docId"
                    + " AND vt.category NOT IN (" + vCat + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Record is not generated from here. You can not modify this record.");
            }
            rsTemp.close();
            psTemp.close();

            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "SELECT pr.isCancelled, pr.site_Code, pr.v_Prefix, pr.v_Type "
                    + " FROM patientReceipt as pr "
                    + " WHERE pr.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                    throw new DmException(0, "Record is discarded. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(siteCode).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(voucherPrefix).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Type")).intValue() != DmShared.iNull(mrTemp.getCardPayment().getReceiptTypeCode()).intValue()) {
                    throw new DmException(0, "Record is not generated from here. You can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Check wether record posted or not >====================================*/
            STServer.checkEOD("patientReceipt", "docId", mrTemp.getSharedKeyFieldCode(), cnTemp);

            CardPaymentPopup_Server.chkSave(true, mrTemp.getAmount(), mrTemp.getSharedKeyFieldCode(), mrTemp.getCardPayment(), cnTemp, false, false, false);
            CardPaymentPopup_Server.delete(mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*====================================< Insert/Update into patientReceipt >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert into patientReceipt(docId, v_Type, recId, v_No, recIdPrefix, v_Date, v_Prefix, site_Code, preparedBy, u_EntDt, "
                        + " lmCode, amount, remark) "
                        + " values (:docId, :v_Type, :recID, :v_No, :recIdPrefix, :v_Date, :v_Prefix, :siteCode, :preparedBy, :u_EntDt, "
                        + " :lmCode, :amount, :remark) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", mrTemp.getCardPayment().getReceiptTypeCode());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdRefund().getRecIdValue()).trim());
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdRefund().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdRefund().getPrefix()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("siteCode", siteCode);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                psTemp.setString("amount", DmServer.vNull(0 - (Math.abs(DmShared.vNull(mrTemp.getAmount()))), "0.00"));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()).trim());
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                mrTemp.setSharedPreparedBy(userName);
            } else {
                sqlQuery = "Update patientReceipt set"
                        + " u_EntDt = :u_EntDt,"
                        + " u_Name = :userName,"
                        + " transferDt = NULL,"
                        + " amount =:amount ,"
                        + " remark =:remark "
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("userName", userName);
                psTemp.setString("amount", DmServer.vNull(0 - (Math.abs(DmShared.vNull(mrTemp.getAmount()).doubleValue())), "0.00"));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()).trim());
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Save Payment >====================================*/
            CardPaymentPopup_Server.save(true, mrTemp.getCardPayment(), "", mrTemp.getSharedKeyFieldCode(),
                    mrTemp.getRecIdRefund().getRecIdValue(), mrTemp.getCardPayment().getReceiptTypeCode(), siteCode, mrTemp.getVoucherDate(), mrTemp.getSharedPreparedBy(), "", cnTemp);

            /*====================================< Check for refund Extra amount >====================================*/
            double balance = STServer.getBalanceLedgerMast(DmShared.xNull(mrTemp.getLmCode()), DmServer.getSsnSiteCode(this), cnTemp);
            if (DmShared.vNull(DmServer.vNull(balance, "0.00")) > 0) {
                throw new DmException(0, "You can not refund excess amount.");
            }

            /*====================================< Save Modify Reason >====================================*/
            if (mrTemp.getModifyReason() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReason(), mrTemp.getSharedKeyFieldCode(), userName, cnTemp);
            }

            spBegin.commit();
            cnTemp.close();

            /*====================================< Send Mail >====================================*/
            STServer.sendMail(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), mrTemp.getCardPayment().getReceiptTypeCode(), this);

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        String sqlQuery, sqlCondition;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   pr.docId"
                    + " , pr.recId"
                    + " , pr.v_Date"
                    + " , vt.short_Name"
                    + " , pr.amount"
                    + " , pr.v_no"
                    + " , lm.name AS lmName"
                    + " , lm.displayName"
                    + " , lm.manualCode"
                    + " , lm.mobile"
                    + " FROM patientreceipt pr"
                    + " LEFT JOIN voucher_type vt ON pr.v_Type = vt.v_Type"
                    + " LEFT JOIN ledgerMast lm ON lm.code = pr.lmCode"
                    + " WHERE pr.site_Code = :site_Code"
                    + " AND vt.category IN (" + vCat + ")"
                    + " AND pr.v_prefix = :v_prefix";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + sqlCondition;
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
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        String defaultMode;
        List<String> PayMode = new ArrayList<String>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setCardPayment(new CardPaymentPopup_Mdl());
            if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("Super"))) {
                sqlQuery = "select paymentTypeCode \n"
                        + " from " + DmServer.dbCompany + ".dbo.userPayModePermission upm \n"
                        + "left join voucher_Type as vt on vt.v_Type =  upm.v_Type\n"
                        + "where  isnull(upm.isDefault,0)=1 and  upm.userName = '" + DmServer.getSsnUserName(this) + "' \n"
                        + "And vt.category = " + vCat + "";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    String code = DmShared.xNull(rsTemp.getString("paymentTypeCode"));
                    PayMode.add(code);
                }
                rsTemp.close();
                psTemp.close();
            }
            /*====================================< Get Recipt type for Payment Type >====================================*/
            sqlQuery = "SELECT vt.v_Type, vt.description, vt.attachPaymentMode, vt.isSystemDefine"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN (" + vCat + ")"
                    + " AND isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                TxtHReceiptType_VList_Mdl rtvMdl = new TxtHReceiptType_VList_Mdl();
                rtvMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                rtvMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                rtvMdl.setV_type(DmShared.xNull(rsTemp.getString("v_Type")));
                rtvMdl.setDescription(DmShared.xNull(rsTemp.getString("description")));
                rtvMdl.setPaymentMode(DmShared.xNull(rsTemp.getString("attachPaymentMode")));
                rtvMdl.setIsDefault(DmShared.xNull(rsTemp.getString("isSystemDefine")));
                if (!DmShared.xNull(rtvMdl.getPaymentMode()).equalsIgnoreCase("")) {
                    String[] pmArray = DmShared.xNull(rtvMdl.getPaymentMode()).split("\\|");

                    if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("Super"))) {

                        defaultMode = pmArray[0];

                        if (PayMode != null && PayMode.size() > 0) {

                            for (String str : pmArray) {
                                if (DmShared.xNull(PayMode.get(0)).equalsIgnoreCase(str)) {
                                    rtvMdl.setCardPaymentDefaultMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(str)}, cnTemp).get(0));
                                    break;
                                }
                            }

                        } else {
                            rtvMdl.setCardPaymentDefaultMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(pmArray[0])}, cnTemp).get(0));
                        }

                    } else {
                        rtvMdl.setCardPaymentDefaultMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(pmArray[0])}, cnTemp).get(0));
                    }
                }
                mrTemp.getCardPayment().getTxtHReceiptTypeVList().add(rtvMdl);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setCurrentDate(DmServer.getServerDateTime()[0]);
            dmrTemp.setData(mrTemp);
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
    public DmResult<LedgerMaster_Mdl> getCustomerDetails(String customerType, String uniqueId) {
        DmResult<LedgerMaster_Mdl> dmrTemp = new DmResult<LedgerMaster_Mdl>();
        LedgerMaster_Mdl lmMdl = new LedgerMaster_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(customerType).equalsIgnoreCase("Walk-In")) {
                lmMdl = LedgerMasterServer.getRecordByManualCode(uniqueId, DmServer.getSsnSiteCode(this), "", cnTemp);
            } else if (DmShared.xNull(customerType).equalsIgnoreCase("Ledger")) {
                PartySearch_Mdl psMdl = PartySearch_SrvImpl.getRecord(uniqueId, "", this, cnTemp);
                lmMdl.setSharedKeyFieldCode(DmShared.xNull(psMdl.getLmCode()));
                lmMdl.setAccountCode(DmShared.xNull(psMdl.getSharedKeyFieldCode()));
                lmMdl.setManualCode(DmShared.xNull(psMdl.getManualCode()));
                lmMdl.setDisplayName(DmShared.xNull(psMdl.getSharedKeyFieldName()));
                lmMdl.setMobile(DmShared.xNull(psMdl.getMobile()));
                lmMdl.setAddress(DmShared.xNull(psMdl.getAddress()));
                lmMdl.setCityCode(DmShared.xNull(psMdl.getCityCode()));
                lmMdl.setCityName(DmShared.xNull(psMdl.getCityName()));
                lmMdl.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(lmMdl.getSharedKeyFieldCode(), DmServer.getSsnSiteCode(this), cnTemp), "0.00"));
            } else {
                sqlQuery = "SELECT"
                        + "   pr.regDocId"
                        + " , pr.recId AS regRecId"
                        + " , pu.patientName"
                        + " , pu.address1"
                        + " , pu.address2"
                        + " , ct.cityCode"
                        + " , ct.cityName"
                        + " , pu.mobile"
                        + " , pu.idNo"
                        + " , hl.code AS idTypeCode"
                        + " , hl.name AS idTypeName"
                        + " , lm.code AS lmCode"
                        + " FROM patientRegistration pr"
                        + " LEFT JOIN patientUHID pu ON pu.docId = pr.uhid"
                        + " LEFT JOIN patientUHID2 pu2 ON pu2.uhid = pu.docId"
                        + " LEFT JOIN city ct ON ct.cityCode = pu.cityCode"
                        + " LEFT JOIN hl7Mast hl ON hl.code = pu2.idTypeCode"
                        + " LEFT JOIN ledgerMast lm ON lm.regDocId = pr.regDocId"
                        + " WHERE pr.isCancelled = 0"
                        + " AND pr.recId = :regRecId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regRecId", DmShared.xNull(uniqueId));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    lmMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("lmCode")));
                    lmMdl.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                    lmMdl.setManualCode(DmShared.xNull(rsTemp.getString("regRecId")));
                    lmMdl.setDisplayName(DmShared.xNull(rsTemp.getString("patientName")));
                    lmMdl.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                    lmMdl.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                    lmMdl.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                    lmMdl.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                    lmMdl.setAddress(DmShared.xNull(rsTemp.getString("address1")) + " " + DmShared.xNull(rsTemp.getString("address2")));
                    lmMdl.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                    lmMdl.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                    lmMdl.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(lmMdl.getSharedKeyFieldCode(), DmServer.getSsnSiteCode(this), cnTemp), "0.00"));
                }
                psTemp.close();
                rsTemp.close();
            }
            dmrTemp.setData(lmMdl);

            if (dmrTemp.getData() == null || DmShared.xNull(dmrTemp.getData().getDisplayName()).trim().equals("")) {
                throw new Exception("No Record Found. Try Again.");
            }

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
}
