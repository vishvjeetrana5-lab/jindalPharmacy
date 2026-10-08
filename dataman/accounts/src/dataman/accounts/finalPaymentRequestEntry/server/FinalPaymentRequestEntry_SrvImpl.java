package dataman.accounts.finalPaymentRequestEntry.server;

import dataman.accounts.finalPaymentRequestEntry.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FinalPaymentRequestEntry_SrvImpl extends DmRSSPrivate implements FinalPaymentRequestEntry_Srv {

    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    private final String vCat = "'" + pvc.FINAL_PAYMENT_REQUEST_ENTRY + "'";

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

            sqlQuery = "SELECT  vt.v_Type, vt.description, vt.guiTemplateCode "
                    + "         FROM voucher_Type vt "
                    + "         WHERE vt.short_Name = " + vCat + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.iNull(rsTemp.getString("v_Type")).toString());
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));

            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
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
    public String[] getServerDateTime() {
        String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.PURCHASE_ORDER);
            listNode.setSharedKeyFieldName("Purchase");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.PHARMACY_PURCHASE_ORDER);
            listNode.setSharedKeyFieldName("Pharmacy");
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
    public DmResultPageing<List<GrdPBillSearchDetail_Mdl>> getPBSearch(String supplierTag, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdPBillSearchDetail_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery = "", voucherType = "";
        List<GrdPBillSearchDetail_Mdl> lstTemp = new ArrayList<GrdPBillSearchDetail_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.iNull(supplierTag).longValue() != 0) {
                condition = " AND pb.consignee = :consignee";
            } else {
                condition = "and 1!=1";

            }
            if (DmShared.xNull(param.getModuleType()).equalsIgnoreCase(pvc.PURCHASE_ORDER)) {
                voucherType = " AND vt.category IN ('" + pvc.PURCHASE_BILL + "', '" + pvc.PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', '" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "')";

            } else {
                voucherType = " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";

            }

            sqlQuery = "select tmp.docid,tmp.pbNo,tmp.pbDate, tmp.supplierCode,\n"
                    + "tmp.supplierName,tmp.pbAmt ,\n"
                    + "tmp.approvalDate,tmp.category, tmp.billType \n"
                    + "from (\n"
                    + "\n"
                    + "Select pb.docid,pb.recId as pbNo,pb.v_Date as pbDate,"
                    + " pb.consignee as supplierCode,sg.name as supplierName,pb.oh_Amt_Net as pbAmt ,"
                    + " format(pb.approvalDateTime,'dd-MMM-yyyy') AS approvalDate,vt.category,"
                    + " vt.description  as billType  "
                    + " from pBill pb "
                    + " left join subgroup sg on sg.subcode=pb.consignee "
                    + " left join voucher_Type as vt on vt.v_Type = pb.V_type "
                    + "      left join (\n"
                    + " \n"
                    + "     select  (sum(fpre.finDuePay) + sum( fpre.advanceSettled) ) as previousAmountPaid, "
                    + "          fpe.supplierCode,fpre.pbDocid      \n"
                    + "		 from finPayEnt fpe\n"
                    + "		 left join finPayEntDetail fped on fped.docid=fpe.docid     \n"
                    + "		 left join finPayReqEnt fpre on fpre.docId=fped.payReqDocId \n"
                    + "		 where isNUll(fped.approvedby,'')<>''   \n"
                    + "   group by fpe.supplierCode,fpre.pbDocid        \n"
                    + "         	\n"
                    + " ) fp on fp.supplierCode = pb.consignee and fp.pbDocId = pb.docId"
                    + " where pb.site_Code = :site_Code and isNUll(pb.approvedBy,'')<>'' "
                    + " and (pb.oh_Amt_Net -pb.tdsAmt -isNull(fp.previousAmountPaid,0) )>0 " + voucherType
                    + "" + condition
                    + "\n"
                    + " union all\n"
                    + "\n"
                    + " Select  pb.docid,pb.recId as pbNo,pb.v_Date as pbDate, pb.consignee as supplierCode,\n"
                    + "sg.name as supplierName,pb.oh_Amt_Net as pbAmt ,\n"
                    + "format(pb.approvalDateTime,'dd-MMM-yyyy') AS approvalDate,vt.category, vt.description  as billType \n"
                    + "from pBill pb \n"
                    + "left join subgroup sg on sg.subcode=pb.consignee \n"
                    + "left join voucher_Type as vt on vt.v_Type = pb.V_type"
                    + " where pb.site_Code = :site_Code and isNUll(pb.approvedBy,'')<>''"
                    + " and pb.isoh_amt_netzero = 1 and pb.isSettled = 0 " + voucherType
                    + "" + condition
                    + ") tmp";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(supplierTag).longValue() != 0) {
                sqlWithParam.setString("consignee", supplierTag);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdPBillSearchDetail_Mdl objTemp = new GrdPBillSearchDetail_Mdl();
                objTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                objTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("pbNo")));
                objTemp.setBillType(DmShared.xNull(rsTemp.getString("billType")));
                objTemp.setPbDate(DmShared.dNull(rsTemp.getDate("pbDate")));
                objTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                objTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                objTemp.setPbAmount(DmServer.vNull(rsTemp.getString("pbAmt")));
                objTemp.setApproveDt(DmShared.xNull(rsTemp.getString("approvalDate")));

                lstTemp.add(objTemp);
            }

//            double amt = -1;
//            for (GrdPBillSearchDetail_Mdl node : lstTemp) {
//                amt = DmShared.vNull(getTotalBalaceSettel(DmShared.xNull(node.getSharedKeyFieldCode()), node.getSupplierTag(), cnTemp)).doubleValue();
//                if (DmShared.vNull(node.getPbAmount()) == DmShared.vNull(amt).doubleValue() || amt == -4) {
//
//                    node.setRemove(true);
//                }
//            }
//
//            Iterator<GrdPBillSearchDetail_Mdl> i = lstTemp.iterator();
//
//            while (i.hasNext()) {
//                GrdPBillSearchDetail_Mdl e = i.next();
//                if (e.isRemove()) {
//                    i.remove();
//                }
//            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
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

    private double getTotalBalaceSettel(String pbDocId, String supplierCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", cond = "";
        double reqAmt = 0d, totalRequestAmt = 0d, diff = -1;
        try {

            if (DmShared.iNull(supplierCode).longValue() != 0 && DmShared.iNull(pbDocId).longValue() != 0) {
                cond = " and fpe.supplierCode=:supplierCode and fpre.pbDocid=:pbDocid";
            } else {
                cond = "and 1!=1";
            }
            sqlQuery = " select sum(fpe.originalAmt)as finAmt,sum(fpre.finDuePay) as ReqAmt,"
                    + "             sum( fpre.advanceSettled) advanceSettled, "
                    + "           max(fpe.approvedBy) approvedBy \n"
                    + "		 from finPayEnt fpe\n"
                    + "		 left join finPayEntDetail fped on fped.docid=fpe.docid\n"
                    + "		 left join finPayReqEnt fpre on fpre.docId=fped.payReqDocId\n"
                    + "		 where fped.approvedby<>''\n " + cond
                    + "		 group by fpe.supplierCode,fpre.pbDocid ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            if (DmShared.iNull(supplierCode).longValue() != 0 && DmShared.iNull(pbDocId).longValue() != 0) {
                psTemp.setLong("supplierCode", DmShared.iNull(supplierCode).longValue());
                psTemp.setLong("pbDocid", DmShared.iNull(pbDocId).longValue());
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                totalRequestAmt = DmShared.vNull(rsTemp.getString("ReqAmt")).doubleValue() + DmShared.vNull(rsTemp.getString("advanceSettled")).doubleValue();
//                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")
//                        && totalRequestAmt == 0) {
//                    totalRequestAmt = -4;/* we set -4 to show that that pbill is setteled against  advance
//                                 /*without any payment at final payment entry point so bill is setteled */
//                }

            }
            rsTemp.close();
            psTemp.close();

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
        return totalRequestAmt;
    }

    @Override
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate "
                    + " FROM subGroup sg "
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode "
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode "
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature "
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type) "
                    + " AND sg.isActive = 1 "
                    + " AND sgsl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
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
    public DmResult<String> saveRecord(FinalPaymentRequestEntry_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, vNo = "", userName = "", voucherPrefix;
        boolean blnAdd = false;
        double totalpBillPaid = 0.0;
        double totalpBillAdvance = 0.0;
        double totalpBillAmt = 0.0;
        try {
            FinalPaymentRequestEntry_Vld.chkSave(mrTemp, DmServer.getServerDateTime()[0]);
            userName = DmServer.getSsnUserName(this);
            voucherPrefix = DmServer.getSsnVoucherPrefix(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("FinPayReqEnt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));

            }

            vNo = DmShared.iNull(mrTemp.getRecId().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            /*====================================< Check wether record is approved or not >====================================*/
            sqlQuery = "Select fpe.approvedBy "
                    + " from FinPayEnt fpe "
                    + " left join FinPayEntDetail fped on fped.docId=fpe.docId"
                    + " where fped.payReqDocId = :payReqDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("payReqDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("")) {
                    throw new DmException(0, "Final Payment Entry Record has been approved. You can't modify this record.");
                }

            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getHeaderSupplierText()).equalsIgnoreCase(DmShared.xNull(mrTemp.getpBillSelectedSupplierText()))) {
                throw new DmException(0, "Supplier names are different.");
            }

            /*=====================< Check wether pBill Request record is pending or not at Final Paymeny Entry >====================================*/
//            if (blnAdd) {
            sqlQuery = " select fped.docid ,fpe.approvedBy"
                    + " from FinPayReqEnt fpr"
                    + " left join FinPayEntDetail fped on fped.payReqDocId=fpr.docId"
                    + " left join FinPayEnt fpe on fpe.docId=fped.docId"
                    + " where fpr.pbDocid=:pBDocid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("docid")).trim().equals("") && blnAdd) {
                    throw new DmException(0, "Can not select some Final Payment Request Entry of this P.Bill is still pending first settle it.");
                }
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("") && blnAdd) {
                    throw new DmException(0, "Can't select some Final Payment Request Entry for this P.Bill is still un-approved first approved it.");
                }
                if (!DmShared.xNull(rsTemp.getString("docid")).trim().equals("") && DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("") && !blnAdd) {
                    throw new DmException(0, "Payment for this request has been done, pending for approval, so can't edit now.");
                }

            }

            rsTemp.close();
            psTemp.close();
//            }

            // ========================================================================
            sqlQuery = "  select sum(fpre.advanceSettled) TotalAdvSett, \n"
                    + "   sum(fpre.finReqAmt) TotalPbillPaidAmt,     \n"
                    + "    sum(pb.oh_Amt_net) pbillAmount\n"
                    + "\n"
                    + "     from pbill pb \n"
                    + "     left join \n"
                    + "		(   select sum(fpre.advanceSettled)advanceSettled, sum(fped.finReqAmt) finReqAmt, "
                    + "                 fpre.pbdocid\n"
                    + "			from finPayReqEnt fpre                                                  \n"
                    + "			left join finPayEntDetail fped on fped.payReqDocId = fpre.docId     \n"
                    + "			group by fpre.pbdocid                                               \n"
                    + "         ) fpre on fpre.pbdocid = pb.docid                                           \n"
                    + "     where pb.DocId =:pBDocid            "
                    + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                totalpBillPaid = rsTemp.getDouble("TotalPbillPaidAmt");
                totalpBillAdvance = rsTemp.getDouble("TotalAdvSett");
                totalpBillAmt = rsTemp.getDouble("pbillAmount");
            }
            double demo = totalpBillAmt - (totalpBillPaid + totalpBillAdvance + DmShared.vNull(mrTemp.getpBillAdvanceSetteled()) + DmShared.vNull(mrTemp.getFinalDuePayment()));
            demo = DmShared.vNull(DmServer.vNull(demo, "0.00"));
            if (demo < 0) {
                throw new DmException(0, " Final Due Amount Can't be greater than Total PBill.");
            }
            //===========================================================================

            if (blnAdd) {
                sqlQuery = "Insert Into FinPayReqEnt(docId, recId,v_No,v_Type, v_Prefix,v_Date,module,supplierCode,pbDocId,finDuePay,paymDt,remark,preparedBy,"
                        + " preparedDate,site_Code,partPay, advanceSettled)"
                        + " values (:docId, :recId,:v_No,:v_Type, :v_Prefix,:v_Date,:module,:supplierCode,:pbDocId,:finDuePay,:paymDt,:remark,:preparedBy,"
                        + " :preparedDate,:site_Code,:partPay, :advanceSettled) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeTag()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("module", DmShared.xNull(mrTemp.getModuleTag()));
                psTemp.setString("supplierCode", DmShared.xNull(mrTemp.getSupplierTag()));
                psTemp.setString("pbDocId", DmShared.iNull(mrTemp.getpBillDocId()).toString());
                psTemp.setDouble("finDuePay", DmShared.vNull(mrTemp.getFinalDuePayment()));
                psTemp.setDate("paymDt", DmServer.convertDate(mrTemp.getPaymentDate()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("partPay", mrTemp.getPartPay());
                psTemp.setDouble("advanceSettled", DmShared.vNull(mrTemp.getpBillAdvanceSetteled()));
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "Update FinPayReqEnt set "
                        + " remark=:remark,"
                        + " partPay=:partPay,"
                        + " U_EntDt= :U_EntDt,"
                        + " finDuePay=:finDuePay ,"
                        + " u_Name=:u_Name "
                        + " where docId=:docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("partPay", mrTemp.getPartPay());
                psTemp.setDouble("finDuePay", DmShared.vNull(mrTemp.getFinalDuePayment()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }
            if (DmShared.iNull(mrTemp.getpBillAdvanceSetteled()).longValue() >= 0) {
                for (GrdAdvPaymentDetail_Mdl mdl : mrTemp.getLstGrdAdvPaymentDetails()) {
                    if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                        sqlQuery = "    Update advanceEntry set "
                                + "     finPayReqDocId=:finPayReqDocId"
                                + "     where docId=:docId and isNull(finPayReqDocId,0)=0";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mdl.getSharedKeyFieldCode()).toString());
                        psTemp.setString("finPayReqDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

                        psTemp.executeUpdate();
                        psTemp.close();
                    }

                }
            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<FinalPaymentRequestEntry_Mdl> getMoveRec(String strKey) {
        DmResult<FinalPaymentRequestEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, poDocid = "";
        FinalPaymentRequestEntry_Mdl mrTemp = new FinalPaymentRequestEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " Select fpr.docid as docId, fpr.recId as recId,"
                    + "     fpr.v_date as v_date ,fpr.module,fpr.supplierCode,sg.name as supplierName,fpr.pbDocId,"
                    + "     fpr.finDuePay ,"
                    + "     fpr.remark,fpr.paymDt,pb.recId as pbRecId ,"
                    + "     fpr.preparedBy,fpr.u_Name,fpr.partPay, fpr.advanceSettled,"
                    + "     fped.finreqAmt, pb.oh_Amt_Net as pbAmt "
                    + "     from FinPayReqEnt fpr "
                    + "     left join pBill pb on pb.docid=fpr.pbDocId "
                    + "     left join subGroup sg on sg.subcode=fpr.supplierCode  "
                    + "     left join finPayEntDetail fped on fped.payReqDocid = fpr.docid  "
                    + "     where fpr.docId= :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_date")));
                mrTemp.setpBillDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                mrTemp.setpBillRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                mrTemp.setModuleTag(DmShared.xNull(rsTemp.getString("module")).trim());
                mrTemp.setModuleText(DmShared.xNull(mrTemp.getModuleTag()).equalsIgnoreCase("PO") ? "Purchase" : "Pharmacy");
                mrTemp.setPartPay(DmShared.xNull(rsTemp.getString("partPay")).trim());
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setPaymentDate(DmShared.dNull(rsTemp.getDate("paymDt")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setFinalDuePayment(DmShared.xNull(rsTemp.getString("finDuePay")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setpBillAmount(DmServer.vNull(rsTemp.getString("pbAmt")));
            }

            psTemp.close();
            rsTemp.close();

            getPBTotalPayment(mrTemp, cnTemp);

            sqlQuery = " select pb.PaymentDueDate,pb.docid \n"
                    + " from pBill pb\n"
                    + " where pb.docid=:pBDocid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setPayDueDate(DmShared.dNull(rsTemp.getDate("PaymentDueDate")));

            }
            rsTemp.close();
            psTemp.close();

            //============ <find adjustment  settelment for this PO>
            sqlQuery = "  select top 1 poDocId from pBillDetail pbd  \n"
                    + "     where pbd.docid = :pBDocid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                poDocid = (DmShared.iNull(rsTemp.getString("poDocId")).toString());

            }
            rsTemp.close();
            psTemp.close();

            getPOAdvSetteled(poDocid, mrTemp, cnTemp);

            //========================== check final payement approved or not
            sqlQuery = "    select fpe.approvedBy    \n"
                    + "         from FinPayReqEnt fpr   \n"
                    + "         left join FinPayEntDetail  fped on fped.payReqDocId = fpr.docid \n"
                    + "         left join FinPayEnt fpe on fpe.docid = fped.docid \n"
                    + "     where fpr.docid =:docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));

            }
            rsTemp.close();
            psTemp.close();

            getPBillDetail(mrTemp, cnTemp);
            mrTemp.getLstGrdPoDetail().addAll(getGrdPoDetail(poDocid, cnTemp));
            mrTemp.getLstGrdMrnDetail().addAll(getGrdMrnDetail(mrTemp.getpBillDocId(), cnTemp));
            mrTemp.getLstGrdBillDetail().addAll(getGrdBillDetail(mrTemp.getpBillDocId(), cnTemp));
            mrTemp.getLstGrdAdvPaymentDetails().addAll(getGrdAdvPaymnetDetail(poDocid, cnTemp));;
            mrTemp.getLstGrdFinalPaymentDetails().addAll(getGrdFinalPaymentDetail(mrTemp.getpBillDocId(), cnTemp));

            cnTemp.close();
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

    private List<GrdFinalPaymentDetail_Mdl> getGrdFinalPaymentDetail(String docId, Connection cnTemp) {
        List<GrdFinalPaymentDetail_Mdl> nodeList = new ArrayList<GrdFinalPaymentDetail_Mdl>();
        GrdFinalPaymentDetail_Mdl node = new GrdFinalPaymentDetail_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            /*====================================< Get Final Pay Detail >====================================*/
            sqlQuery = "Select (fpe.docid) as docid ,max(fpe.recid) as recid ,max(fpe.v_No) as v_No,max(fpe.v_date) as date ,"
                    + "max(fpe.module) as module,max(fpe.supplierCode)as supplierCode,max(sg1.name) as supplierName,\n"
                    + " max(fpe.remark) as remark ,max(cm.code) as cardTypeCode, max(cm.name) As cardTypeName, "
                    + "max(cm.cardType) as cardType, max(cm.shortName) as cardShortName, max(cl.amtDr) as amtDr\n"
                    + " , max(cl.amtCr) as amtCr, \n"
                    + " max(sg.subcode) as subcode, max(sg.name) as acName,"
                    + " max(LGTDS.AmtDr) As TDSDeducted,"
                    + " max(lg.tdsOnAmt) as tdsOnAmt,max(LG.OrignalAmt) as finalAmt , \n"
                    + " max(sg.nature) as acNature, max(cm.nature) as cardTypeNature,max(cl.cardNo) as insNo,"
                    + " max(cl.cardHolderName) as remak,max(cl.cardDate) as insDate\n"
                    + " from FinPayEnt fpe  \n"
                    + " LEFT JOIN FinPayEntDetail AS fped ON fped.docid = fpe.docid \n"
                    + " LEFT JOIN FinPayReqEnt AS fpre ON fpre.docid = fped.payReqDocId  \n"
                    + " LEFT JOIN pBill AS pbd ON pbd.docid = fpre.pbDocId \n"
                    + " LEFT JOIN advanceEntry AS ad ON ad.finPayReqDocId = fpre.docid \n"
                    + " LEFT JOIN advanceReqEntry AS adre ON adre.docid = ad.docId\n"
                    + " LEFT JOIN voucher_Type AS vt ON fpe.v_Type = vt.v_Type \n"
                    + " left join cardLedger cl  On fpe.docId  = cl.docid    \n"
                    + " left join ledger lg  On fpe.docId = lg.docid And cl.V_SNo=LG.V_SNo \n"
                    + " Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                    + " left join cardTypeMast cm On cl.cardTypeCode = cm.code \n"
                    + " left join subGroup sg On cl.subcode = sg.subCode \n"
                    + " left join subGroup sg1 On fpe.supplierCode = sg1.subCode\n"
                    + " WHERE fpre.pbDocid=:docId \n"
                    + " and lg.isSystem_Generated=0 \n"
                    + "group by fpe.docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdFinalPaymentDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("date")));
                node.setNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setMode(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setChqNo(DmShared.xNull(rsTemp.getString("insNo")));
                node.setChqDate(DmShared.dNull(rsTemp.getDate("insDate")));
                node.setBank(DmShared.xNull(rsTemp.getString("subcode")));
                node.setBank(DmShared.xNull(rsTemp.getString("acName")));
                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                node.setAmt(DmServer.vNull(rsTemp.getString("finalAmt"), "0.00"));
                node.setTds(DmServer.vNull(rsTemp.getString("tdsOnAmt"), "0.00"));
                node.setTdsDeduction(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select fpr.docId "
                    + "                        from FinPayReqEnt fpr "
                    + "                   	left join voucher_Type as vt on fpr.v_Type = vt.v_Type "
                    + "                         where  vt.category = " + vCat + ""
                    + "                   	Order by  fpr.v_Date,  fpr.v_No, fpr.recId ");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select fpr.docid ,fpr.recid as payReqNo,fpr.v_No,fpr.v_date as date,"
                    + "fpr.module,fpr.supplierCode,sg.name as supplierName,"
                    + "fpr.pbDocId,pb.recId as pbNo ,fpr.finDuePay,"
                    + " fpr.paymDt, fpr.preparedBy,fpr.u_Name "
                    + " from FinPayReqEnt fpr "
                    + " left join pBill pb on pb.docid=fpr.pbDocId "
                    + " left join subGroup sg on sg.subcode=fpr.supplierCode "
                    + " LEFT JOIN voucher_Type AS vt ON fpr.v_Type = vt.v_Type "
                    + "	 WHERE vt.category in (" + vCat + ") "
                    + "  AND fpr.site_Code = :site_Code "
                    + "  AND fpr.v_prefix = :vPrefix";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("payReqNo")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("date")));
                listNode.setModuleTag(DmShared.xNull(rsTemp.getString("module").trim()));
                listNode.setModuleText(DmShared.xNull(listNode.getModuleTag()).equalsIgnoreCase("PO") ? "Purchase" : "Pharmacy");
                listNode.setSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                listNode.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setPaymnetDt(DmShared.dNull(rsTemp.getDate("paymDt")));
                listNode.setPbNo(DmShared.xNull(rsTemp.getString("pbNo")));
                listNode.setFinalDuePay(DmServer.vNull(rsTemp.getString("finDuePay"), "0.00"));
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

            /*====================================< Check wether record is approved or not >====================================*/
            sqlQuery = "  Select fped.approvedBy ,fped.payReqDocId \n"
                    + "   from FinPayEntDetail  fped\n"
                    + "  where fped.payReqDocId = :payReqDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("payReqDocId", DmShared.iNull(strKey).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("")) {
                    throw new DmException(0, "Final Payment  has been approved. You can't delete this record.");
                } else if (!DmShared.xNull(rsTemp.getString("reqDocId")).trim().equals("")) {
                    throw new DmException(0, " You can't delete this record final payment done.");
                }

            }
            rsTemp.close();
            psTemp.close();

            //======== update advanceEntry finpayRequestDocid to null============
            sqlQuery = "    Update advanceEntry set "
                    + "     finPayReqDocId=NUll"
                    + "     where finPayReqDocId=:finPayReqDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("finPayReqDocId", DmShared.xNull(strKey));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from advanceReqEntry >>===============================*/
            sqlQuery = " Delete From FinPayReqEnt Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
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
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
                }
            } catch (Exception ex) {
                dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
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
    public DmResult<FinalPaymentRequestEntry_Mdl> getPurchaseBillDetail(String pbNo, String pbDocId, String type, String supplierType) {
        DmResult<FinalPaymentRequestEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Purchase Bill detail >>===============================*/
            DmResult<FinalPaymentRequestEntry_Mdl> resultPurchaseBillDetail = getPurchaseBillDetail(pbNo, pbDocId, type, supplierType, cnTemp);
            dmrTemp.setData(resultPurchaseBillDetail.getData());

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
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

    private void getPBillDetail(FinalPaymentRequestEntry_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";

        try {
            if (DmShared.iNull(mrTemp.getpBillDocId()).longValue() != 0) {
                condition = " AND pb.docid = :pbDocId";
            } else {
                condition = "and 1!=1";
            }
            sqlQuery = "Select pb.docid as pBDocId ,pb.v_Date as pBillDate,pb.recId as pbNo, "
                    + "  pb.consignee as supplierCode,sg.name as supplierName,pb.oh_Amt_Net as pbAmt, "
                    + "  format(pb.approvalDateTime,'dd-MMM-yyyy') AS approvalDate,pb.approvedBy,pb.remark, pb.tdsAmt, (pb.oh_Amt_Net- pb.tdsAmt) as pNetAmt "
                    + "  from pBill pb "
                    + "  left join subgroup sg on sg.subcode=pb.consignee    "
                    + "  left join voucher_Type as vt on vt.v_Type = pb.V_type   "
                    + "  where pb.site_Code = :siteCode and pb.approvedBy is not null "
                    //                    + "	 AND vt.category IN ('PBIL','PBILU','PBILD','PBLDU') "
                    + condition
                    + "  order by pb.v_Date desc,  pb.v_No desc, pb.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(mrTemp.getpBillDocId()).longValue() != 0) {
                psTemp.setString("pbDocId", DmShared.iNull(mrTemp.getpBillDocId()).toString());
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                mrTemp.setpBillDocId(DmShared.iNull(rsTemp.getString("pBDocId")).toString());
                mrTemp.setpBillRecId(DmShared.xNull(rsTemp.getString("pbNo")));
                mrTemp.setpBillDate(DmShared.dNull(rsTemp.getDate("pBillDate")));
                mrTemp.setpBillSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                mrTemp.setpBillSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setpBillAmount(DmServer.vNull(rsTemp.getString("pbAmt")));
                mrTemp.setpBillTDS(DmServer.vNull(rsTemp.getString("tdsAmt")));
                mrTemp.setpBillNET(DmServer.vNull(rsTemp.getString("pNetAmt")));
                mrTemp.setpBillApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setpBillRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setpBillApprovedDt(DmShared.xNull(rsTemp.getString("approvalDate")));
            }
            rsTemp.close();
            psTemp.close();
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

    public DmResult<FinalPaymentRequestEntry_Mdl> getPurchaseBillDetail(String pbNo, String pbDocId, String type, String supplierType, Connection cnTemp) throws DmException, SQLException {
        DmResult<FinalPaymentRequestEntry_Mdl> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "";

        String poDocid = "";
        FinalPaymentRequestEntry_Mdl mrTemp = new FinalPaymentRequestEntry_Mdl();
        dmrTemp.setData(mrTemp);
        double temp = 0.0;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(pbDocId).longValue() != 0) {
                condition += " and pb.docId = :pbDocId";
            } else if (!DmShared.xNull(pbNo).trim().equalsIgnoreCase("")) {
                condition += " and pb.recId = :pbNo";
            }
            // ============================ << To get pBill details >>=======================================
            sqlQuery = "Select pb.docid as pBDocId ,pb.v_Date as pBillDate,pb.recId as pbNo, "
                    + " pb.consignee as supplierCode,sg.name as supplierName,pb.oh_Amt_Net as pbAmt, "
                    + " format(pb.approvalDateTime,'dd-MMM-yyyy') AS approvalDate,pb.approvedBy,pb.remark, pb.tdsAmt, (pb.oh_Amt_Net- pb.tdsAmt) as pNetAmt "
                    + " from pBill pb "
                    + " left join subgroup sg on sg.subcode=pb.consignee"
                    + " left join voucher_Type as vt on vt.v_Type = pb.V_type"
                    + " where pb.site_Code = :siteCode and pb.approvedBy is not null"
                    + condition
                    + " order by pb.v_Date desc,  pb.v_No desc, pb.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(pbDocId).longValue() != 0) {
                psTemp.setString("pbDocId", DmShared.iNull(pbDocId).toString());
            } else if (!DmShared.xNull(pbNo).trim().equalsIgnoreCase("")) {
                psTemp.setString("pbNo", DmShared.xNull(pbNo));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                if (DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record has not been approved.");
                }
                mrTemp.setpBillDocId(DmShared.iNull(rsTemp.getString("pBDocId")).toString());
                mrTemp.setpBillRecId(DmShared.xNull(rsTemp.getString("pbNo")));
                mrTemp.setpBillDate(DmShared.dNull(rsTemp.getDate("pBillDate")));
                mrTemp.setpBillSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                mrTemp.setpBillSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setpBillAmount(DmServer.vNull(rsTemp.getString("pbAmt")));
                mrTemp.setpBillTDS(DmServer.vNull(rsTemp.getString("tdsAmt")));
                mrTemp.setpBillNET(DmServer.vNull(rsTemp.getString("pNetAmt")));
                mrTemp.setpBillApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setpBillRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setpBillApprovedDt(DmShared.xNull(rsTemp.getString("approvalDate")));
            } else {
                throw new DmException(0, "No record found. Try Again.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " select fped.docid ,fpe.approvedBy"
                    + " from FinPayReqEnt fpr"
                    + " left join FinPayEntDetail fped on fped.payReqDocId=fpr.docId"
                    + " left join FinPayEnt fpe on fpe.docId=fped.docId"
                    + " where fpr.pbDocid=:pBDocid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("docid")).trim().equals("")) {
                    throw new DmException(0, "Previous Payment request of this P.Bill is still pending first settle it.");
                }
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("")) {
                    throw new DmException(0, "Previous Payment request for this P.Bill is still un-approved first approved it.");
                }

            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " select pb.PaymentDueDate,pb.docid \n"
                    + " from pBill pb\n"
                    + " where pb.docid=:pBDocid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setPayDueDate(DmShared.dNull(rsTemp.getDate("PaymentDueDate")));
            }
            rsTemp.close();
            psTemp.close();

            //============ <find adjustment  settelment for this PO>
            sqlQuery = "  select top 1 poDocId from pBillDetail pbd  \n"
                    + "     where pbd.docid = :pBDocid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                poDocid = (DmShared.iNull(rsTemp.getString("poDocId")).toString());

            }
            rsTemp.close();
            psTemp.close();

            getPOAdvSetteled(poDocid, mrTemp, cnTemp);
            getPBTotalPayment(mrTemp, cnTemp);
            mrTemp.getLstGrdBillDetail().addAll(getGrdBillDetail(mrTemp.getpBillDocId(), cnTemp));
            mrTemp.getLstGrdPoDetail().addAll(getGrdPoDetail(poDocid, cnTemp));
            mrTemp.getLstGrdMrnDetail().addAll(getGrdMrnDetail(mrTemp.getpBillDocId(), cnTemp));
            mrTemp.getLstGrdAdvPaymentDetails().addAll(getGrdAdvPaymnetDetail(poDocid, cnTemp));
            mrTemp.getLstGrdFinalPaymentDetails().addAll(getGrdFinalPaymentDetail(mrTemp.getpBillDocId(), cnTemp));

            dmrTemp.setStatus(true);
            cnTemp.close();
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

    private List<GrdBillDetail_Mdl> getGrdBillDetail(String pbDocId, Connection cnTemp) {
        List<GrdBillDetail_Mdl> nodeList = new ArrayList<GrdBillDetail_Mdl>();
        GrdBillDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get PO Detail >====================================*/
            sqlQuery = "Select pbd.docid,pbd.itemcode,im.name as itemName,pbd.acceptQty as quantity ,"
                    + ""
                    + "        pbd.rate,pbd.oh_amt_net as amt "
                    + "        from pBillDetail pbd "
                    + "        LEFT JOIN itemMast im ON pbd.itemcode = im.code "
                    + "        where pbd.docid = :docid  "
                    + "        order by pbd.v_sno ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(pbDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdBillDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setQty(DmServer.vNull(rsTemp.getString("quantity"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("amt"), "0.00"));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private List<GrdPODetail_Mdl> getGrdPoDetail(String poDocId, Connection cnTemp) {
        List<GrdPODetail_Mdl> nodeList = new ArrayList<GrdPODetail_Mdl>();
        GrdPODetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get PO Detail >====================================*/
            sqlQuery = " select po.DocId docid,  po.recId as orderNo,po.v_date as orderDate,     \n"
                    + "         im.name as itemName,      \n"
                    + "         pod.oh_amt_net as value,po.subCode ,sg.name as supplierName,     \n"
                    + "         po.preparedBy as entryBy  \n"
                    + "     from pOrder po  \n"
                    + "     LEFT JOIN pOrderDetail pod ON pod.docId = po.DocId    \n"
                    + "     LEFT JOIN itemMast im ON pod.itemcode = im.code \n"
                    + "     LEFT join subgroup sg on sg.subcode=po.subcode  \n"
                    + "   where po.docid =:docId  \n"
                    + "   order by pod.v_sno ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(poDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPODetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("orderNo")));
                node.setSupplier(DmShared.xNull(rsTemp.getString("supplierName")));
                node.setDate(DmShared.dNull(rsTemp.getDate("orderDate")));
                node.setValue(DmServer.vNull(rsTemp.getString("value"), "0.00"));
                node.setEntryBy(DmShared.xNull(rsTemp.getString("entryBy")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));

                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private List<GrdGRNDetail_Mdl> getGrdMrnDetail(String pbDocId, Connection cnTemp) {
        List<GrdGRNDetail_Mdl> nodeList = new ArrayList<GrdGRNDetail_Mdl>();
        GrdGRNDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get GRN Detail >====================================*/
            sqlQuery = " Select pbd.pcDocId, pc.v_date as grnDate,pc.recid as grnNo,"
                    + " pc.pChallanNo,pc.pChallanDate,pcd.oh_amt_net as value,"
                    + " pcd.podocid,pc.preparedBy,pcd.isStatus, im.name as itemName "
                    + " from pChallan pc "
                    + " LEFT JOIN pBillDetail pbd  on pc.docId= pbd.pcDocId "
                    + " LEFT JOIN pChallanDetail pcd  ON pbd.pcDocId = pcd.docid  and  pcd.v_SNo=pbd.pcV_SNo"
                    + "  LEFT JOIN itemMast im ON pcd.itemcode = im.code "
                    + " where pbd.docid = :docId and   pc.approvedBy is not null"
                    + " order by pbd.v_sno";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(pbDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdGRNDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("grnDate")));
                node.setGrnNo(DmShared.xNull(rsTemp.getString("grnNo")));
                node.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                node.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));
                node.setValue(DmServer.vNull(rsTemp.getString("value"), "0.00"));
                node.setEntryBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 0) {
                    node.setStatus("Pending");
                } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 1) {
                    node.setStatus("Cancelled");
                } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 2) {
                    node.setStatus("Completed");
                }

                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private List<GrdAdvPaymentDetail_Mdl> getGrdAdvPaymnetDetail(String podocId, Connection cnTemp) {
        List<GrdAdvPaymentDetail_Mdl> nodeList = new ArrayList<GrdAdvPaymentDetail_Mdl>();
        GrdAdvPaymentDetail_Mdl node = new GrdAdvPaymentDetail_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            /*====================================< Get advanceEntry Detail >====================================*/
            sqlQuery = "   Select ad.docId,ad.v_date,ad.recId,cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, cm.shortName as cardShortName, "
                    + " sg.subcode, sg.name as acName,  ad.tdsDeduction As TDSDeducted, lg.tdsOnAmt, ad.OriginalAmt, cl.amtCr, cl.amtDr,   "
                    + " sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,cl.cardHolderName as remak,cl.cardDate as insDate,ad.approvedBy   "
                    + " from advanceEntry ad    "
                    + " left join advanceReqEntry adr on adr.docid=ad.reqDocId  "
                    + " left join cardLedger cl  On ad.docId = cl.docid   "
                    + " left join ledger lg  On ad.docId = lg.docid And cl.V_SNo=LG.V_SNo  "
                    + " Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')=''  "
                    + " left join cardTypeMast cm On cl.cardTypeCode = cm.code  "
                    + " left join subGroup sg On cl.subcode = sg.subCode    "
                    + "  where adr.podocid=:docId  and lg.isSystem_Generated=0 "
                    + "  order by ad.v_no ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(podocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAdvPaymentDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setMode(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setChqNo(DmShared.xNull(rsTemp.getString("insNo")));
                node.setChqDate(DmShared.dNull(rsTemp.getDate("insDate")));
                node.setBank(DmShared.xNull(rsTemp.getString("subcode")));
                node.setBank(DmShared.xNull(rsTemp.getString("acName")));
                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                node.setAmt(DmServer.vNull(rsTemp.getString("OriginalAmt"), "0.00"));
                node.setTdsDeduction(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));

                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private void getPOAdvSetteled(String poDocid, FinalPaymentRequestEntry_Mdl mrTemp, Connection cnTemp) {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            /*====================================< Get POAdvSetteled  >====================================*/
            sqlQuery = " select sum(fpr.advanceSettled) as advSettled      \n"
                    + "         from  pORder po \n      "
                    + "          left join              "
                    + "         ( select docid, max(poDocId)poDocId \n"
                    + "                 from pBillDetail pbd\n"
                    + "                 group by docid ) pbd on po.docid = pbd.poDocId  \n"
                    + "         left join FinPayReqEnt fpr on fpr.pbdocid = pbd.docId   \n"
                    + "         where po.docid = :poDocid       ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("poDocid", DmShared.iNull(poDocid).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setPoAdvanceSetteled(DmShared.vNull(rsTemp.getString("advSettled")).toString());
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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

    private void getPBTotalPayment(FinalPaymentRequestEntry_Mdl mrTemp, Connection cnTemp) {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double totalPayment = 0.0, totalpBillAdvance = 0.0;
        try {

            /*====================================< Get POAdvSetteled  >====================================*/
            sqlQuery = "   select fpre.advanceSettled TotalAdvSett, fped.finReqAmt TotalPbillPaidAmt, fped.approvedBy "
                    + "     \n"
                    + "     from pbill pb \n"
                    + "     left join finPayReqEnt fpre on fpre.pbdocid = pb.docid\n"
                    + "     left join finPayEntDetail fped on fped.payReqDocId = fpre.docId\n"
                    + "     where pb.DocId = :pBDocid   ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("pBDocid", DmShared.iNull(mrTemp.getpBillDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {

                    totalPayment += DmShared.vNull(rsTemp.getDouble("TotalPbillPaidAmt"));
                }
                totalpBillAdvance += DmShared.vNull(rsTemp.getDouble("TotalAdvSett"));

            }
            rsTemp.close();
            psTemp.close();
            mrTemp.setTotalBillPaymnet(DmShared.vNull(totalPayment).toString());
            mrTemp.setpBillAdvanceSetteled(DmShared.vNull(totalpBillAdvance).toString());

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
}
