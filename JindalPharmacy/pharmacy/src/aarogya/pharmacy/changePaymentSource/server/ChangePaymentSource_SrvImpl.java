package aarogya.pharmacy.changePaymentSource.server;

import aarogya.pharmacy.changePaymentSource.shared.*;
import aarogya.pharmacy.changePaymentSource.shared.ChangePaymentSource_Mdl.SaleBillSearchType;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChangePaymentSource_SrvImpl extends DmRSSPrivate implements ChangePaymentSource_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = pvc.PHARMACY_CHANGE_PAYMENT_SOURCE;
//    private final String vCatOpd = pvc.OPD;
//    private final String filterCategory = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.LAB_REGISTRATION + "'";
    private final String filterCategory = "'" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "'";
    private final String voucherTypefieldName = "pla.v_Type", voucherDateFieldName = "pla.v_Date";

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

//            sqlQuery = "Select  pla.docId "
//                    + "         from patientLimitApproval as pla "
//                    + "         left join voucher_type vt on vt.v_type = pla.v_type "
//                    + "         where pla.site_Code = :site_Code and vt.category in ('" + vCat + "') and pla.v_prefix = :v_prefix and pla.sbDocId IS NOT NULL ";
            sqlQuery = "Select  pla.docId "
                    + "         from patientLimitApproval as pla "
                    + "         left join voucher_type vt on vt.v_type = pla.v_type "
                    + "         where pla.site_Code = :site_Code "
                    + "         and vt.category in ('" + vCat + "') "
                    + "         and pla.v_prefix = :v_prefix "
                    + "         and LEFT(CAST(pla.docid AS varchar), 1) = '1' ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlQuery += " order by pla.v_Date desc, vt.short_Name, pla.v_no desc, pla.recId ";
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
    public DmResult<ChangePaymentSource_Mdl> getMoveRec(String strKey) {
        DmResult<ChangePaymentSource_Mdl> dmrTemp = new DmResult();
        ChangePaymentSource_Mdl mrTemp = new ChangePaymentSource_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  pla.docId, pla.v_Type, pla.v_No, pla.recId, pla.recIdPrefix, pla.v_Prefix, pla.v_Date, pla.amount, "
                    + "         pla.nature as changePaymentSource, vt.category as regType, pla.u_EntDt, pla.u_Name, pla.preparedBy, "
                    + "         lm.regDocId, lm.subCode, sb.lmCode, pla.sbDocId , sb.recId  as billNo"
                    + "         from patientLimitApproval as pla "
                    + "         left join saleBill as sb on sb.docid = pla.sbDocId  "
                    + "         left join ledgerMast as lm on lm.code = sb.lmCode "
                    + "         left join patientRegistration as pr on lm.regDocId = pr.regDocId "
                    + "         left join voucher_Type as vt on vt.v_Type = sb.v_Type "
                    + "         Where pla.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.getPatientDetail().setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.getPatientDetail().setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.getPatientDetail().setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                mrTemp.getPatientDetail().setBillDocId(DmShared.xNull(rsTemp.getString("sbDocId")));
                mrTemp.getPatientDetail().setBillNo(DmShared.xNull(rsTemp.getString("billNo")));
                mrTemp.setChangePaymentSource(DmShared.xNull(rsTemp.getString("changePaymentSource")));

                if (DmShared.xNull(rsTemp.getString("regType")).trim().equalsIgnoreCase(pvc.PHARMACY_BILL)) {
                    mrTemp.setRegType(DmShared.xNull(SaleBillSearchType.SALE_BILL.getValue()));
                } else if (DmShared.xNull(rsTemp.getString("regType")).trim().equalsIgnoreCase(pvc.PHARMACY_BILL_RETURN)) {
                    mrTemp.setRegType(DmShared.xNull(SaleBillSearchType.SALE_BILL_RETURN.getValue()));
                }

                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Get Patient Detail >====================================*/
            getPatientDetail(mrTemp.getPatientDetail(), cnTemp);

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
    public DmResult<String> deleteRecord(UserPermission userPermission, String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, billDocId = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select  pu.approvedBy, pu.isExpired, pu.isCancelled as uhidIsCancelled, pr.isCancelled as regIsCancelled, "
                    + "         pla.isCancelled, pla.site_Code, pla.v_Prefix, pla.regDocId  , sb.isCancelled as isSaleBillCancelled,"
                    + "         pla.sbDocId as billDocId"
                    + "         from patientLimitApproval as pla "
                    + "         left join patientRegistration as pr on pla.regDocId = pr.regDocId "
                    + "         left join saleBill sb on sb.docId = pla.sbDocId"
                    + "         left join patientUHID as pu on sb.uhid = pu.docId "
                    + "         where pla.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                billDocId = DmShared.xNull(rsTemp.getString("billDocId"));
                if (DmShared.iNull(rsTemp.getString("uhidIsCancelled")).intValue() != 0) {
                    throw new DmException(0, "UHID discarded. You can not discard this record.");
                }
                if (DmShared.iNull(rsTemp.getString("regIsCancelled")).intValue() != 0) {
                    throw new DmException(0, "Registration discarded. You can not discard this record.");
                }
                if (DmShared.iNull(rsTemp.getString("isExpired")).intValue() == 1) {
                    throw new DmException(0, "Patient has been expired. You can not discard this record.");
                }
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Patient is not Approved. You can not discard this record.");
                }
                if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                    throw new DmException(0, "Record already discarded.");
                }
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(DmServer.getSsnSiteCode(this)).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not discard this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(DmServer.getSsnVoucherPrefix(this)).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not discard this record.");
                }
                if (DmShared.iNull(rsTemp.getString("isSaleBillCancelled")).intValue() == 1) {
                    throw new DmException(0, "Bill already discarded.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Check wether record posted or not >====================================*/
            STServer.checkEOD("saleBillDetail", "docId", billDocId, cnTemp);

            /*====================================< Delete records from patientLimitApproval >====================================*/
            sqlQuery = "Delete from patientLimitApproval Where docId = :docId or docId = :newDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("newDocId", DmShared.iNull(DmShared.xNull(keyCode).replaceFirst("1", "2")).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Update paymentSource into patientRegistration >====================================*/
            sqlQuery = "Update	sb set "
                    + "		sb.paymentSource =  CASE WHEN (prc.docId is not null and pla.docId is null) then " + PaymentSource.PAYMENT.getEnumValue() + " "
                    + "		when (prc.docId is not null and pla.docId is not null) then " + PaymentSource.PARTIAL_PAYMENT.getEnumValue() + " "
                    + "		when (prc.docId is null and pla.docId is not null) then pla.nature  "
                    + "		when (prc.docId is null and pla.docId is null) then " + PaymentSource.NO_PAYMENT.getEnumValue() + " end "
                    + "		from saleBill as sb "
                    + "		left join patientLimitApproval as pla on sb.docId = pla.docId "
                    + "		left join patientReceipt as prc on sb.docId = prc.docId "
                    + "		where sb.docId = :billDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("billDocId", DmShared.iNull(billDocId).toString());
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
    public DmResult<String> saveRecord(UserPermission userPermission, ChangePaymentSource_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        boolean blnAdd = false, CTR_To_CTP = false;
        String sqlQuery, userName, siteCode, patientType = "";
        double amount;
        dmrTemp.setData("");

        try {
            ChangePaymentSource_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(mrTemp.getPatientDetail().getRegDocId()).longValue() > 0) {
                sqlQuery = "Select  vt.category "
                        + "         from patientRegistration as pr "
                        + "         left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                        + "         where pr.regDocId = :regDocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getPatientDetail().getRegDocId()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    patientType = DmShared.xNull(rsTemp.getString("category"));
                }
                rsTemp.close();
                psTemp.close();

                if (patientType.equalsIgnoreCase(pvc.OPD)) {
                    if (DmShared.iNull(mrTemp.getChangePaymentSource()).intValue() == PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) {
                        throw new DmException(0, "OPD registration can not be change to Credit To Registration.");
                    }
                } else if (DmShared.xNull(patientType).trim().equalsIgnoreCase(pvc.IPD)
                        || DmShared.xNull(patientType).trim().equalsIgnoreCase(pvc.DAY_CARE)
                        || DmShared.xNull(patientType).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    if (DmShared.iNull(mrTemp.getChangePaymentSource()).intValue() == PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) {
                        throw new DmException(0, "IPD / Emergency / Day Care Patient can not be CTO.");
                    }
                }
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(DmShared.xNull(mrTemp.getVoucherTypeCode())), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("patientLimitApproval", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(),
                        DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=========================< Check whether Registration No. is allowed or not >===========================*/
            if (DmShared.iNull(mrTemp.getPatientDetail().getBillDocId()).longValue() != 0) {
                sqlQuery = "Select  sb.regDocId "
                        + "         from saleBill as sb "
                        + "         left join voucher_Type as vt on sb.v_Type = vt.v_Type "
                        + "         where sb.docId = :docId and vt.category in (" + filterCategory + ") "
                        + "         AND sb.isCancelled = 0";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getPatientDetail().getBillDocId()).toString());
                rsTemp = psTemp.executeQuery();
                if (!rsTemp.next()) {
                    throw new DmException(0, "This bill is is not Exist.");
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==========================< Check whether already change or not >==========================*/
//            sqlQuery = "Select  pla.docId "
//                    + "         from patientLimitApproval as pla "
//                    + "         left join voucher_Type as vt on pla.v_Type = vt.v_Type "
//                    + "         where vt.category = '" + vCat + "' and pla.sbDocId = :sbDocId  and pla.docId <> :docId ";
            sqlQuery = "Select  pla.docId "
                    + "         from patientLimitApproval as pla "
                    + "         left join voucher_Type as vt on pla.v_Type = vt.v_Type "
                    + "         where vt.category = '" + vCat + "' "
                    + "         and pla.sbDocId = :sbDocId  "
                    + "         and LEFT(CAST(pla.docid AS varchar), 1) = '1' "
                    + "         and pla.docId <> :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("sbDocId", DmShared.iNull(mrTemp.getPatientDetail().getBillDocId()).toString());
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "You have already changed payment source of this Bill. No..");
            }
            rsTemp.close();
            psTemp.close();

            String plaDocId = null, plaNature = null, plaAmount = null, prcDocId = null, prcAmount = null;

            /*====================================< Check whether patient Alive or not>====================================*/  //tbc
            sqlQuery = " select pu.approvedBy, pu.isExpired, pu.isCancelled as uhidIsCancelled, sb.isCancelled as billIsCancelled ,"
                    + "  pla.docId as plaDocId,pla.nature as plaNature, pla.amount as plaAmount, "
                    + "  prc.docId as prcDocId, prc.amount as prcAmount, sb.paymentSource"
                    + "  from saleBill sb "
                    + "         left join patientLimitApproval as pla on sb.docId  = pla.docid "
                    + "         left join patientUHID as pu on sb.uhid = pu.docId "
                    + "         left join patientReceipt as prc on sb.docId = prc.docId "
                    + "  where sb.docId=  :billDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("billDocId", DmShared.iNull(mrTemp.getPatientDetail().getBillDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                plaDocId = DmShared.xNull(rsTemp.getString("plaDocId"));
                plaNature = DmShared.xNull(rsTemp.getString("plaNature"));
                plaAmount = DmShared.xNull(rsTemp.getString("plaAmount"));
                prcDocId = DmShared.xNull(rsTemp.getString("prcDocId"));
                prcAmount = DmShared.xNull(rsTemp.getString("prcAmount"));

                if (blnAdd) {
                    if (DmShared.iNull(rsTemp.getString("paymentSource")).intValue() == DmShared.iNull(mrTemp.getChangePaymentSource()).intValue()) {
                        throw new DmException(0, "Payment source is already " + PaymentSource.getValueOf(DmShared.iNull(mrTemp.getChangePaymentSource()).intValue()).getEnumName() + ". You can not modify this record.");
                    }
                }

                if (DmShared.iNull(rsTemp.getString("uhidIsCancelled")).intValue() != 0) {
                    throw new DmException(0, "UHID discarded. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("billIsCancelled")).intValue() != 0) {
                    throw new DmException(0, "Bill discarded. You can not modify this record.");
                }
//                if (DmShared.iNull(rsTemp.getString("isExpired")).intValue() == 1) {        // TBD
//                    throw new DmException(0, "Patient has been expired. You can not modify this record.");
//                }
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Patient is not Approved. You can not modify this record.");
                }

            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getPatientDetail().getRegDocId()).longValue() != 0) {
                sqlQuery = "Select  pr.isCancelled as regIsCancelled, "
                        + "         pr.organizationCode, om.isTPA, pr.orgPolicyNo, pr.orgHolderName, pr.corporateEmployeeCode  , sb.paymentSource"
                        + "         from patientRegistration as pr "
                        + "         left join saleBill sb on sb.regDocId = pr.regDocId"
                        + "         left join organizationMast as om on pr.organizationCode = om.subCode "
                        + "         where pr.regDocId = :regDocId and sb.docId = :billDocId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getPatientDetail().getRegDocId()).toString());
                psTemp.setString("billDocId", DmShared.iNull(mrTemp.getPatientDetail().getBillDocId()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {

                    if (PaymentSource.getValueOf(DmShared.iNull(mrTemp.getChangePaymentSource()).intValue()) == PaymentSource.CREDIT_TO_ORGANIZATION) {
                        if (DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 0) {
                            throw new DmException(0, "In case of CTO you have to be TPA patient.");
                        }
                        if (DmShared.iNull(rsTemp.getString("corporateEmployeeCode")).longValue() == 0
                                || DmShared.xNull(rsTemp.getString("orgPolicyNo")).trim().equalsIgnoreCase("")
                                || DmShared.xNull(rsTemp.getString("orgHolderName")).trim().equalsIgnoreCase("")) {
                            throw new DmException(0, "In case of CTO you have to fill all organization details.");
                        }
                    }
                    if (PaymentSource.getValueOf(DmShared.iNull(rsTemp.getString("paymentSource")).intValue()) == PaymentSource.CREDIT_TO_REGISTRATION) {
//                        throw new DmException(0, "CTR can not be changed.");
                        CTR_To_CTP = true;
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            /*=============================== check for walkin customer can't do ctr ==================*/
            if (DmShared.iNull(mrTemp.getPatientDetail().getRegDocId()).longValue() == 0) {
                if (DmShared.iNull(mrTemp.getPatientDetail().getSubCode()).longValue() == 0) {
                    if (PaymentSource.getValueOf(DmShared.iNull(mrTemp.getChangePaymentSource()).intValue()) == PaymentSource.CREDIT_TO_ORGANIZATION) {
                        throw new DmException(0, "In case of Walk-In CTO is not allowed.");
                    } else if (PaymentSource.getValueOf(DmShared.iNull(mrTemp.getChangePaymentSource()).intValue()) == PaymentSource.CREDIT_TO_REGISTRATION) {
                        throw new DmException(0, "In case of Walk-In CTR is not allowed.");
                    }
                } else {
                    if (PaymentSource.getValueOf(DmShared.iNull(mrTemp.getChangePaymentSource()).intValue()) == PaymentSource.CREDIT_TO_ORGANIZATION) {
                        throw new DmException(0, "In case of Ledger CTO is not allowed.");
                    } else if (PaymentSource.getValueOf(DmShared.iNull(mrTemp.getChangePaymentSource()).intValue()) == PaymentSource.CREDIT_TO_REGISTRATION) {
                        throw new DmException(0, "In case of Ledger CTR is not allowed.");
                    }
                }
            }

            if (DmShared.iNull(plaDocId).longValue() != 0) {
                amount = DmShared.vNull(plaAmount);
            } else {
                amount = DmShared.vNull(prcAmount);
            }

            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select  pla.isCancelled, pla.site_Code, pla.v_Prefix "
                    + "         from patientLimitApproval as pla "
                    + "         where pla.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(siteCode).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(voucherPrefix).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            STServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");

            /*====================================< Check wether record posted or not >====================================*/
            STServer.checkEOD("saleBillDetail", "docId", mrTemp.getPatientDetail().getBillDocId(), cnTemp);

            /*====================================< Insert/Update into patientLimitApproval >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert into patientLimitApproval(docId, v_Type, v_No, recIdPrefix, recId, v_Date, v_Prefix, sbDocId, regDocId , "
                        + " amount, nature, site_Code, preparedBy, u_EntDt) "
                        + " values (:docId, :v_Type, :v_No, :recIdPrefix, :recId, :v_Date, :v_Prefix, :sbDocId, :regDocId ,"
                        + " :amount, :nature, :site_Code, :preparedBy, :u_EntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getRecId().getRecIdValue()).trim());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("sbDocId", DmShared.foriegnKey(mrTemp.getPatientDetail().getBillDocId()));
                psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getPatientDetail().getRegDocId()));
                psTemp.setString("nature", DmShared.iNull(mrTemp.getChangePaymentSource()).toString());
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("amount", DmServer.vNull(amount, "0.00"));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                mrTemp.setSharedPreparedBy(userName);
            } else {
                sqlQuery = "Update patientLimitApproval set "
                        + " amount = :amount, "
                        + " nature = :nature, "
                        + " u_EntDt = :u_EntDt, "
                        + " u_Name = :u_Name, "
                        + " transferDt = null "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("nature", DmShared.iNull(mrTemp.getChangePaymentSource()).toString());
                psTemp.setString("amount", DmServer.vNull(amount, "0.00"));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from patientLimitApproval >====================================*/
            sqlQuery = "Delete from patientLimitApproval Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode().replaceFirst("1", "2")).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (DmShared.iNull(plaDocId).longValue() != 0) {
                /*====================================< Insert record into patientLimitApproval >====================================*/
                sqlQuery = "Insert  into patientLimitApproval (docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, sbDocId, regDocId, amount, authLetterNo, "
                        + "         remark, nature, u_EntDt, u_Name, preparedBy, site_Code, transferDt, isCancelled) "
                        + " Select  :newDocId as docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, sbDocId, regDocId, :amount as amount, authLetterNo, "
                        + "         remark, :nature as nature, u_EntDt, u_Name, preparedBy, site_Code, transferDt, isCancelled "
                        + "         from patientLimitApproval "
                        + "         where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("newDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode().replaceFirst("1", "2")).toString());
                psTemp.setString("amount", DmServer.vNull(0 - DmShared.vNull(amount), "0.00"));
                psTemp.setString("nature", DmShared.iNull(plaNature).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*====================================< Update paymentSource into patientRegistration >====================================*/
            sqlQuery = "Update saleBill set paymentSource = :paymentSource where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getPatientDetail().getBillDocId()).toString());
            psTemp.setString("paymentSource", DmShared.iNull(mrTemp.getChangePaymentSource()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (CTR_To_CTP) {
                sqlQuery = " update patientService set isCancelled = 1 where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getPatientDetail().getBillDocId());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = " update patientServiceDetail set isCancelled = 1 where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getPatientDetail().getBillDocId());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = " update patientLimitApproval set isCancelled = 1 where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getPatientDetail().getBillDocId());
                psTemp.executeUpdate();
                psTemp.close();
            }

            spBegin.commit();
            cnTemp.close();
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
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  pla.docId, pla.recId, pla.v_Date, sb.recId AS regNo, pla.v_no, vt.short_Name ,   "
                    + "          lm.displayName  as patientName ,            "
                    //                    + "          CASE WHEN (sb.regDocId is  not null) then  uhid.patientName     WHEN  (sb.subCode is  not null) then  sg.name       ELSE lm.displayName  end as patientName ,            "
                    + "          CASE WHEN (sb.regDocId is  not null) then  uhid.guardianName              WHEN  (sb.subCode is  not null) then  ''  ELSE uhid.guardianName   end as guardianName ,           "
                    + "          CASE WHEN (sb.regDocId is  not null) then  uhid.dobDisplay              WHEN  (sb.subCode is  not null) then  ''    ELSE ''   end as dobdisplay , "
                    + "          CASE WHEN (sb.regDocId is  not null) then  gn.name              WHEN  (sb.subCode is  not null) then  gn.name       ELSE ''   end as gender , "
                    + "          CASE WHEN (sb.regDocId is  not null) then  uhid.mobile              WHEN  (sb.subCode is  not null) then  sg.mobile ELSE lm.mobile   end as mobile           "
                    + "         FROM patientLimitApproval as pla "
                    + "         LEFT JOIN saleBill sb on sb.docId = pla.sbDocId"
                    + "         LEFT JOIN patientRegistration pr ON pr.regdocID = pla.regDocId"
                    + "         LEFT JOIN subGroup sg on sg.subCode= sb.subCode"
                    + "         LEFT JOIN ledgerMast lm on lm.code = sb.lmCode "
                    + "         LEFT JOIN patientUHID AS uhid ON uhid.docid = sb.uhid  "
                    + "         LEFT JOIN patientUHID2 AS pu2 ON uhid.docId = pu2.uhid  "
                    + "         LEFT JOIN voucher_type AS vt ON pla.v_Type = vt.v_Type  "
                    + "         LEFT JOIN hl7Mast AS gn ON gn.code = pu2.genderCode "
                    + "         WHERE pla.site_Code = :site_Code "
                    + "         and vt.category in ('" + vCat + "') "
                    + "         and pla.v_prefix = :v_prefix "
                    + "         and LEFT(CAST(pla.docid AS varchar), 1) = '1' ";
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
                node = new Find_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                node.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                node.setAge(DmShared.xNull(rsTemp.getString("dobdisplay")));
                node.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                node.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                node.setGender(DmShared.xNull(rsTemp.getString("gender")));

                lstTemp.add(node);
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
    public DmResult<PatientDetail_Mdl> getBillDetails(String billDocId, String billRecId, String regType) {
        DmResult<PatientDetail_Mdl> dmrTemp = new DmResult();
        PatientDetail_Mdl mrTemp = new PatientDetail_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", vCat = "";
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(regType).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Type is mandatory.");
            } else if (regType.equalsIgnoreCase(DmShared.iNull(SaleBillSearchType.SALE_BILL.getId()).toString())) {
                vCat = pvc.PHARMACY_BILL;
            } else if (regType.equalsIgnoreCase(DmShared.iNull(SaleBillSearchType.SALE_BILL_RETURN.getId()).toString())) {
                vCat = pvc.PHARMACY_BILL_RETURN;
            }

            /*======================<check for Bill docId or recid =======================================================*/
            if (!DmShared.xNull(billDocId).equals("")) {
                condition += " AND sb.docId = :docId";
            } else if (!DmShared.xNull(billRecId).equals("")) {
                condition += " AND sb.recId = :recId";
            }

            /*====================================< Check whether valid reg no. or not >====================================*/
            sqlQuery = " Select  pr.regDocId , sb.docId, sb.recId as billNo,  sb.lmCode, sb.subCode, sb.v_Type, "
                    + "     pr.isCancelled as regCancelled, pu.isCancelled as uhidCancelled, pu.approvedBy as  uhidApprovedBy "
                    + "     FROM saleBill sb "
                    + "     LEFT JOIN voucher_type AS pbvt ON sb.v_Type = pbvt.v_Type "
                    + "     LEFT JOIN patientregistration pr ON sb.regDocid = pr.regDocid "
                    + "     LEFT JOIN patientUHID as pu on pu.docId = pr.uhid "
                    + "     where sb.isCancelled = 0 "
                    + "     AND pbvt.category in (" + "'" + vCat + "'" + ") "
                    + "     and sb.site_Code = :site_Code  " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (DmShared.iNull(billDocId).longValue() != 0) {
                psTemp.setString("docId", DmShared.xNull(billDocId));
            } else if (!DmShared.xNull(billRecId).equals("")) {
                psTemp.setString("recId", DmShared.xNull(billRecId).trim());
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRegDocId(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                mrTemp.setBillDocId(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setBillNo(DmShared.xNull(rsTemp.getString("billNo")));
                mrTemp.setLmCode(DmShared.iNull(rsTemp.getString("lmCode")).toString());
                mrTemp.setSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                mrTemp.setSubCode(DmShared.iNull(rsTemp.getString("subCode")).toString());

                if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() > 0) {
                    if (DmShared.iNull(rsTemp.getString("regCancelled")).intValue() == 1) {
                        throw new DmException(0, "Patient registration is discarded.");
                    } else if (DmShared.iNull(rsTemp.getString("uhidCancelled")).intValue() == 1) {
                        throw new DmException(0, "Patient UHID is discarded.");
                    } else if (DmShared.xNull(rsTemp.getString("uhidApprovedBy")).trim().equals("")) {
                        throw new DmException(0, "Patient UHID is not approved.");
                    }
                }
            } else {
                throw new DmException(0, "No record found. Try Again.");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getBillDocId()).longValue() == 0) {
                throw new DmException(0, "No record found. Try Again.");
            }
            /*====================================< Get Patient Detail >====================================*/
            getPatientDetail(mrTemp, cnTemp);

            dmrTemp.setValue1(DmServer.getServerDateTime()[0]);
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

//    private void getPatientDetail(PatientDetail_Mdl mrTemp, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//
//        try {
//
//            if (DmShared.iNull(mrTemp.getRegDocId()).longValue() > 0) {
//
//                sqlQuery = "Select  pr.regDocId, pr.recId as regNo, pr.v_Date as regDate, pr.uhid, pu.recId as uhidNo, "
//                        + "         pu.patientName, pu.guardianName, gn.name as gender, pu.address1, pu.address2, pu.dobDisplay as age, pu.mobile, "
//                        + "         pr.organizationCode, org.name as organizationName, cm.name as caseName, con.name AS consultantName, "
//                        + "          CASE WHEN (prc.docId is not null and pla.docId is null) then " + PaymentSource.PAYMENT.getEnumValue() + " "
//                        + "         when (prc.docId is not null and pla.docId is not null) then " + PaymentSource.PARTIAL_PAYMENT.getEnumValue() + " "
//                        + "         when (prc.docId is null and pla.docId is not null) then pla.nature "
//                        + "         when (prc.docId is null and pla.docId is null) then " + PaymentSource.NO_PAYMENT.getEnumValue() + " end as paymentSource "
//                        + "         from patientRegistration as pr "
//                        + "         left join patientLimitApproval as pla on pr.regDocId = pla.docId "
//                        + "	    left join patientReceipt as prc on pr.regDocId = prc.docId "
//                        + "         left join patientUHID as pu on pr.uhid = pu.docId "
//                        + "         left join caseMast as cm on pr.caseCode = cm.code "
//                        + "         left join subGroup as org on pr.organizationCode = org.subCode "
//                        + "         left join subGroup as con on pr.consultantCode = con.subCode "
//                        + "         left join patientUHID2 as pu2 on pu.docId = pu2.uhid  "
//                        + "         left join hl7Mast as gn on gn.code = pu2.genderCode "
//                        + "         where pr.regDocId = :regDocId ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocId()).toString());
//
//            } else if (DmShared.iNull(mrTemp.getSubCode()).longValue() > 0) {
//                //lm.displayName, lm.cityCode, lm.address, sb.consultantCode
//                sqlQuery = "   SELECT  '' as regDocId  "
//                        + "   ,  ''  as regNo   "
//                        + "   ,  '' as uhid , '' as uhidNo , '' as guardianName"
//                        + "   ,  sg.name as patientName , '' as age,'' as gender"
//                        + "   ,  sg.add1 as address1 ,  sg.add2 as address2 ,  sg.mobile  "
//                        + "   ,  sg.manualCode,  '' as organizationCode, '' as organizationName"
//                        + "   , con.name as consultantName,  '' as caseName, sb.paymentSource "
//                        + " FROM subGroup sg "
//                        + " LEFT JOIN saleBill sb ON sg.subCode = lm.subCode AND sb.docid = :docId "
//                        + " LEFT JOIN subGroup as con on sb.consultantCode = con.subCode "
//                        + " WHERE sg.subCode = :subCode";
//
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSubCode()).toString());
//                psTemp.setString("docId", DmShared.iNull(mrTemp.getBillDocId()).toString());
//
//            } else {
//                sqlQuery = "Select sb.regDocId , '' as regNo, sb.v_Date as regDate, sb.uhid, pu.recId as uhidNo, pu.guardianName as guardianName,"
//                        + " lm.displayName as patientName, '' as age,'' as gender, lm.mobile as mobile ,"
//                        + " lm.address as address1 , '' as address2, '' as organizationCode, '' as organizationName, "
//                        + " con.name as consultantName , '' as caseName, sb.paymentSource "
//                        + " FROM LedgerMast AS lm "
//                        + " LEFT JOIN saleBill sb ON lm.code = sb.lmCode  AND sb.docid = :docId "
//                        + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode "
//                        + " LEFT JOIN subGroup AS con ON con.subCode = con.subCode  "
//                        + " LEFT JOIN patientUHID as pu on sb.uhid = pu.docId "
//                        + " WHERE lm.code = :lmCode ";
//
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(mrTemp.getBillDocId()).toString());
//                psTemp.setString("lmCode", DmShared.iNull(mrTemp.getLmCode()).toString());
//            }
//
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//
//                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
//
//                mrTemp.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
//                mrTemp.setRegDate(DmShared.dNull(rsTemp.getDate("regDate")));
//                mrTemp.setUhidDocId(DmShared.xNull(rsTemp.getString("uhid")));
//                mrTemp.setUhidNo(DmShared.xNull(rsTemp.getString("uhidNo")));
//                mrTemp.setGuardianName(DmShared.xNull(rsTemp.getString("guardianName")));
//                mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
//                mrTemp.setAge(DmShared.xNull(rsTemp.getString("age")));
//                mrTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));
//                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
//                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address1")) + DmShared.xNull(rsTemp.getString("address2")));
//                mrTemp.setOrganizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
//                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
//                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
//                mrTemp.setCaseName(DmShared.xNull(rsTemp.getString("caseName")));
//                mrTemp.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));
//
//            }
//            psTemp.close();
//            rsTemp.close();
//
//            String[] amount = getAmount(DmShared.iNull(mrTemp.getBillDocId()).toString(), cnTemp);
//            mrTemp.setServiceAmount(amount[1]);
//            mrTemp.setReceiptAmount(amount[2]);
//        } catch (SQLException | DmException e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
    private void getPatientDetail(PatientDetail_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            sqlQuery = "Select sb.regDocId , pr.recId as regNo, pr.v_Date as regDate, sb.uhid, pu.recId as uhidNo, pu.guardianName as guardianName,"
                    + " lm.displayName as patientName, pu.dobDisplay as age, gn.name as gender, lm.mobile as mobile ,"
                    + " lm.address as address1 , '' as address2, pr.organizationCode, org.name as organizationName, "
                    + " con.name as consultantName , cm.name as caseName, "
                    + " CASE WHEN (prc.docId is not null and pla.docId is null) then " + PaymentSource.PAYMENT.getEnumValue() + " "
                    + "         when (prc.docId is not null and pla.docId is not null) then " + PaymentSource.PARTIAL_PAYMENT.getEnumValue() + " "
                    + "         when (prc.docId is null and pla.docId is not null) then pla.nature "
                    + "         when (prc.docId is null and pla.docId is null) then " + PaymentSource.NO_PAYMENT.getEnumValue() + " end as paymentSource "
                    + " FROM saleBill sb "
                    + " left join patientLimitApproval as pla on sb.docid = pla.docId "
                    + "	left join patientReceipt as prc on sb.docId = prc.docId "
                    + " LEFT JOIN LedgerMast AS lm ON lm.code = sb.lmCode "
                    + " LEFT JOIN subGroup AS con ON con.subCode = con.subCode  "
                    + " LEFT JOIN patientUHID as pu on sb.uhid = pu.docId "
                    + " left join patientUHID2 as pu2 on pu.docId = pu2.uhid  "
                    + " left join hl7Mast as gn on gn.code = pu2.genderCode "
                    + " LEFT JOIN patientRegistration as pr on sb.regDocId = pr.regDocId "
                    + " left join subGroup as org on pr.organizationCode = org.subCode "
                    + " left join caseMast as cm on pr.caseCode = cm.code "
                    + " WHERE sb.docid = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getBillDocId()).toString());

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setRegDate(DmShared.dNull(rsTemp.getDate("regDate")));
                mrTemp.setUhidDocId(DmShared.xNull(rsTemp.getString("uhid")));
                mrTemp.setUhidNo(DmShared.xNull(rsTemp.getString("uhidNo")));
                mrTemp.setGuardianName(DmShared.xNull(rsTemp.getString("guardianName")));
                mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setAge(DmShared.xNull(rsTemp.getString("age")));
                mrTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address1")) + DmShared.xNull(rsTemp.getString("address2")));
                mrTemp.setOrganizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                mrTemp.setCaseName(DmShared.xNull(rsTemp.getString("caseName")));
                mrTemp.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));
            }
            psTemp.close();
            rsTemp.close();

            String[] amount = getAmount(DmShared.iNull(mrTemp.getBillDocId()).toString(), cnTemp);
            mrTemp.setServiceAmount(amount[1]);
            mrTemp.setReceiptAmount(amount[2]);
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select v_Type, description from voucher_Type where short_Name = '" + vCat + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
            } else {
                throw new DmException(0, "Voucher Type not found.");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (int i = 0; i < SaleBillSearchType.values().length; i++) {
                SaleBillSearchType cdn = SaleBillSearchType.values()[i];
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(cdn.getId()).toString());
                listNode.setSharedKeyFieldName(cdn.getValue());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentSource(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
            listNode.setSharedKeyFieldName(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumName());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PaymentSource.CREDIT_TO_PATIENT.getEnumValue()).toString());
            listNode.setSharedKeyFieldName(PaymentSource.CREDIT_TO_PATIENT.getEnumName());
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return rstTemp;
    }

    private String[] getAmount(String billDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String[] rtnVal = new String[3];

        try {
            sqlQuery = "   Select sb.docId, prec.recAmt, sbd.srvAmt, (ISNULL(sbd.srvAmt,0) - ISNULL(prec.recAmt,0)) as balAmt  "
                    + "    from saleBill as sb "
                    + "    left join  "
                    + "		 (  select  prc.docId, sum(isNull(prc.amount,0)) as recAmt "
                    + "		       from patientReceipt prc "
                    + "		       where prc.isCancelled <> 1  "
                    + "                group by prc.docId  "
                    + "            ) as prec on sb.docId= prec.docId "
                    + "	    left join   "
                    + "	          ( "
                    + "             select  sbds.docId, "
                    + "             SUM(CASE WHEN vt.category = :sbrCategory THEN -ISNULL(sbds.oh_Amt_Net, 0) ELSE ISNULL(sbds.oh_Amt_Net, 0) END) as srvAmt  "
                    + "             from saleBillDetail sbds "
                    + "             LEFT JOIN saleBill sb ON sb.docid = sbds.docid "
                    + "             LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type "
                    + "             group by sbds.docId "
                    + "	          ) as sbd on sb.docId = sbd.docId "
                    + "   where sb.docId = :docId and sb.isCancelled <> 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(billDocId).toString());
            psTemp.setString("sbrCategory", pvc.PHARMACY_BILL_RETURN);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                rtnVal[0] = DmServer.vNull(rsTemp.getString("balAmt"), "0.00");
                rtnVal[1] = DmServer.vNull(rsTemp.getString("srvAmt"), "0.00");
                rtnVal[2] = DmServer.vNull(rsTemp.getString("recAmt"), "0.00");
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
        return rtnVal;
    }
}
