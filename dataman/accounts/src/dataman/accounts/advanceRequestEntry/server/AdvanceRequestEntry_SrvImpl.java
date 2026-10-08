package dataman.accounts.advanceRequestEntry.server;

import dataman.accounts.advanceRequestEntry.shared.*;
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

public class AdvanceRequestEntry_SrvImpl extends DmRSSPrivate implements AdvanceRequestEntry_Srv {

    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    private final String vCat = "'" + pvc.ADVANCE_REQUEST_ENTRY + "'";

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
                defaultValue.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("v_Type")).toString());
                defaultValue.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));

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

    private String getVouchers(String module) {

        if (DmShared.xNull(module).equalsIgnoreCase(pvc.PURCHASE_ORDER)) {
            return module = "'" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "','"
                    + pvc.SERVICE_ORDER + "','" + pvc.SERVICE_ORDER_UNREGISTERED + "'";
        } else {
            return "'" + pvc.PHARMACY_PURCHASE_ORDER + "'";
        }
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
    public DmResultPageing<List<GrdPurchaseOrderSearchDetail_Mdl>> getPoSearch(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdPurchaseOrderSearchDetail_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery;
        List<GrdPurchaseOrderSearchDetail_Mdl> lstTemp = new ArrayList<GrdPurchaseOrderSearchDetail_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            condition = getVouchers(param.getVoucherType());

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select po.docid,po.v_date as poDate,po.recId as poNo,po.subcode,"
                    + " sg.name as supplierName,po.v_No,PO.oh_Amt_Net as poAmt,"
                    + " po.preparedBy as preparedBy "
                    + " from pOrder po "
                    + " left join subgroup sg on sg.subcode=po.subcode "
                    + " left join voucher_Type as vt on vt.v_Type = po.V_type "
                    + " left join ("
                    + "           select pod.docid, sum(ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0)) AS pendingQty "
                    + "                 from porderDetail pod "
                    + "                 where pod.isStatus = 0 "
                    + "                 group by pod.docid "
                    + "           ) as pod on po.docid=pod.docid "
                    + " left join (\n"
                    + "		select  sum(ad.paidAmt) as paidAmt ,po.docid podocid    \n"
                    + "               from advanceEntry  ad\n"
                    + "               left join advanceReqEntry adr on adr.docid=ad.reqDocId \n"
                    + "               left join pOrder po on po.docid=adr.poDocId \n"
                    + "               	group by po.docid \n"
                    + "         ) ad on ad.podocid = po.docid"
                    + " where po.site_Code = :site_Code and po.approvedBy is not null "
                    + " and (PO.oh_Amt_Net - isNUll(ad.paidAmt,0)) >0 "
                    + " and vt.category in(" + DmShared.xNull(condition) + ") and pod.pendingQty>0 ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdPurchaseOrderSearchDetail_Mdl objTemp = new GrdPurchaseOrderSearchDetail_Mdl();
                objTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                objTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("poNo")));
                objTemp.setV_Sno(DmServer.vNull(rsTemp.getString("v_No"), "0"));
                objTemp.setPoDate(DmShared.dNull(rsTemp.getDate("poDate")));
                objTemp.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                objTemp.setPoAmt(DmServer.vNull(rsTemp.getString("poAmt")));
                objTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));

                lstTemp.add(objTemp);
            }
            /*
            double amt = 0;
            for (GrdPurchaseOrderSearchDetail_Mdl node : lstTemp) {
                amt = DmShared.vNull(getTotalBalacePaidandPoAmt(DmShared.xNull(node.getSharedKeyFieldCode()), cnTemp)).doubleValue();
                if (DmShared.vNull(node.getPoAmt()).doubleValue() == amt) {
                    node.setFinalAmt(DmServer.vNull(amt));
                }
            }

            Iterator<GrdPurchaseOrderSearchDetail_Mdl> i = lstTemp.iterator();

            while (i.hasNext()) {
                GrdPurchaseOrderSearchDetail_Mdl e = i.next();
                if (DmShared.vNull(e.getPoAmt()).doubleValue() == DmShared.vNull(e.getFinalAmt()).doubleValue()) {
                    i.remove();
                }
            }
             */
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

//    private double getTotalBalacePaidandPoAmt(String poDocId, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery = "", cond = "";
//        double amt = 0d;
//        try {
//
//            sqlQuery = " select sum(ad.paidAmt) as paidAmt "
//                    + "	from advanceEntry  ad"
//                    + "	left join advanceReqEntry adr on adr.docid=ad.reqDocId "
//                    + " left join pOrder po on po.docid=adr.poDocId "
//                    + "	 where adr.poDocid= :docId";
//
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//
//            psTemp.setLong("docId", DmShared.iNull(poDocId).longValue());
//
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                amt = DmShared.vNull(rsTemp.getString("paidAmt")).doubleValue();
//
//            }
//            rsTemp.close();
//            psTemp.close();
//
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
//        return amt;
//    }
    private void getPoDetail(String type, AdvanceRequestEntry_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";

        try {
            if (DmShared.iNull(mrTemp.getPoDocId()).longValue() != 0) {
                condition = " AND po.docid = :poDocid";
            } else {
                condition = "and 1!=1";
            }
            sqlQuery = "Select po.docid,po.v_date as poDate,po.recId as poNo,po.subcode,sg.name as supplierName,po.v_No,PO.oh_Amt_Net as poAmt,po.preparedBy as preparedBy "
                    + " from pOrder po "
                    + " left join subgroup sg on sg.subcode=po.subcode "
                    + " left join voucher_Type as vt on vt.v_Type = po.V_type "
                    + " where po.site_Code = :site_Code and po.approvedBy is not null "
                    //                    + " and vt.category = '" + type + "' "
                    + "" + condition;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(mrTemp.getPoDocId()).longValue() != 0) {
                psTemp.setString("poDocid", DmShared.iNull(mrTemp.getPoDocId()).toString());
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setPoDocId(DmShared.xNull(rsTemp.getString("docid")));
                mrTemp.setPoRecId(DmShared.xNull(rsTemp.getString("poNo")));

                mrTemp.setPoDate(DmShared.dNull(rsTemp.getDate("poDate")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setPoAmt(DmServer.vNull(rsTemp.getString("poAmt")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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

    @Override
    public DmResult<String> saveRecord(AdvanceRequestEntry_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, vNo = "", userName = "", voucherPrefix;
        boolean blnAdd = false;

        try {
            AdvanceRequestEntry_Vld.chkSave(mrTemp, DmServer.getServerDateTime()[0]);
            userName = DmServer.getSsnUserName(this);
            voucherPrefix = DmServer.getSsnVoucherPrefix(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("advanceReqEntry", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));

            }

            vNo = DmShared.iNull(mrTemp.getRecId().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            /*====================================< Check wether record is approved or not >====================================*/
            sqlQuery = "Select approvedBy from advanceEntry where reqDocId = :reqDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("reqDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("")) {
                    throw new DmException(0, "Advace Entry Record has been approved. You can't modify this record.");
                }

            }
            rsTemp.close();
            psTemp.close();

            /*================< Check Previous requested entry is approved or not >================*/
            boolean isCheck = false;
            sqlQuery = "select count(poDocId) as cnt from advanceReqEntry  advEnt\n"
                    + "where poDocId= :poDocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("poDocId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("cnt")).equalsIgnoreCase("0")) {
                    isCheck = true;
                }

            }

            if (isCheck) {

                /*================< Previous requested entry is not punched in advanced entry. >================*/
                sqlQuery = "select  advEnt.reqDocId from   advanceReqEntry advreq\n"
                        + "left join advanceEntry  advEnt   on advEnt.reqdocid=advreq.docId\n"
                        + "where  advreq.poDocId= :poDocId and isNull(advreq.isStatus,'')='' order by advEnt.v_Date";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("poDocId", DmShared.iNull(mrTemp.getPoDocId()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.xNull(rsTemp.getString("reqDocId")).equalsIgnoreCase("")) {
                        throw new DmException(0, "Previous requested entry is not punched in advanced entry.");
                    }

                }

            }

            /*================< Check Previous advance requested entry is approved or not >================*/
            sqlQuery = "select top 1 isNull(advEnt.approvedBy,'') as appBy from advanceEntry advEnt\n"
                    + "left join advanceReqEntry advReqEnt on advReqEnt.docId=advEnt.reqDocId  \n"
                    + "where advReqEnt.poDocId= :poDocId order by advEnt.v_Date desc";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("poDocId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String appBy = DmShared.xNull(rsTemp.getString("appBy"));
                if (DmShared.xNull(appBy).equalsIgnoreCase("")) {
                    throw new DmException(0, "Previous advanced requested entry is not Approved.");
                }
            }
            /*================< Check Advance Request amount can not be greater than PO Amount >================*/
            sqlQuery = "select sum(isNull(advanceReqAmt,0)) as advanceReqAmt,max(po.oh_Amt_Total) as poTotalAmt \n"
                    + "from advanceReqEntry arq\n"
                    + "left join pOrder po on po.docId = arq.poDocId\n"
                    + "where poDocId=:poDocId and isnull(arq.isStatus,0)<>1 \n"
                    + "group by  poDocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("poDocId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                double oldAdvanceAmt = 0;
                double advanceRequestAmt = 0;
                double poAmount = 0;
                oldAdvanceAmt = DmShared.vNull(rsTemp.getString("advanceReqAmt"));
                advanceRequestAmt = DmShared.vNull(mrTemp.getAdvanceReqAmt());
                poAmount = DmShared.vNull(rsTemp.getString("poTotalAmt"));
                if ((oldAdvanceAmt + advanceRequestAmt > poAmount) && (!DmServer.vNull(poAmount - oldAdvanceAmt, "0.00").equalsIgnoreCase("0.00"))) {
                    throw new DmException(0, "Advance Request amount can not be greater than " + DmServer.vNull(poAmount - oldAdvanceAmt, "0.00"));
                }
                if (DmServer.vNull(poAmount - oldAdvanceAmt, "0.00").equalsIgnoreCase("0.00")) {
                    throw new DmException(0, "All amount already requested.");

                }
            }

            /*====================================< Check wether po record is pending or not >====================================*/
            if (blnAdd) {

                sqlQuery = " select ade.approvedBy, adr.docId from  pOrder po        \n"
                        + "     left join advanceReqEntry adr on adr.poDocId =  po.docid     \n"
                        + "     left join advanceEntry ade on ade.reqDocId = adr.docId      \n"
                        + "     where adr.isStatus<>1 and po.docid =:poDocid and ade.approvedBy is Null and adr.docid is not null ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setLong("poDocid", DmShared.iNull(mrTemp.getPoDocId()).longValue());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {

//                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("")) {
                    throw new DmException(0, "Previous Advance request for this P.O. No. is either pending or un-approved.");
//                    }

                }
                rsTemp.close();
                psTemp.close();

            }

            if (blnAdd) {
                sqlQuery = "Insert Into advanceReqEntry(docId, recId,v_No,v_Type, poDocId,module,advanceReqAmt,dueDate, remark, preparedBy,"
                        + " preparedDate,v_Prefix,v_Date,site_Code)"
                        + " values (:docId, :recId,:v_No,:v_Type, :poDocId,:module,:advanceReqAmt,:dueDate,:remark,:preparedBy,"
                        + " :preparedDate,:v_Prefix,:v_Date,:site_Code) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeTag()));
                psTemp.setString("poDocId", DmShared.iNull(mrTemp.getPoDocId()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("module", DmShared.xNull(mrTemp.getModuleTag()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setDate("dueDate", DmServer.convertDate(mrTemp.getDueDate()));
                psTemp.setDouble("advanceReqAmt", DmShared.vNull(mrTemp.getAdvanceReqAmt()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "Update advanceReqEntry set "
                        + " advanceReqAmt=:advanceReqAmt,"
                        + " dueDate=:dueDate,"
                        + " remark=:remark,"
                        + " U_EntDt= :U_EntDt,"
                        + " u_Name=:u_Name "
                        + " where docId=:docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setDate("dueDate", DmServer.convertDate(mrTemp.getDueDate()));
                psTemp.setDouble("advanceReqAmt", DmShared.vNull(mrTemp.getAdvanceReqAmt()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
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
    public DmResult<AdvanceRequestEntry_Mdl> getMoveRec(String strKey) {
        DmResult<AdvanceRequestEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, vCat = "";
        AdvanceRequestEntry_Mdl mrTemp = new AdvanceRequestEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select adr.docid as docId, adr.recId as recId,"
                    + " adr.v_date as v_date ,adr.module,adr.poDocId,adr.dueDate ,adr.remark,adr.advanceReqAmt,po.recId as poRecId ,"
                    + " adr.preparedBy,adr.u_Name,adr.isStatus, vt.category "
                    + " from advanceReqEntry adr "
                    + " left join porder po on po.docid=adr.poDocId "
                    + " left join voucher_Type as vt on vt.v_Type = po.V_type "
                    + " where adr.docId= :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));

                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_date")));

                mrTemp.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                mrTemp.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")).toString());

                mrTemp.setModuleTag(DmShared.xNull(rsTemp.getString("module")));
                mrTemp.setModuleText(DmShared.xNull(mrTemp.getModuleTag()).equalsIgnoreCase(pvc.PURCHASE_ORDER) ? "Purchase" : "Pharmacy");

                mrTemp.setDueDate(DmShared.dNull(rsTemp.getDate("dueDate")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setAdvanceReqAmt(DmShared.xNull(rsTemp.getString("advanceReqAmt")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setIsStatus((DmShared.xNull(rsTemp.getString("isStatus"))));
                vCat = DmShared.xNull(rsTemp.getString("category"));
            }

            psTemp.close();
            rsTemp.close();

            getPoDetail(vCat, mrTemp, cnTemp);
            mrTemp.getLstGrdPoDetail().addAll(getGrdPoDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdMrnDetail().addAll(getGrdMrnDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdBillDetail().addAll(getGrdBillDetail(mrTemp.getPoDocId(), cnTemp));

            sqlQuery = " Select ad.docid as docId "
                    + " from advanceEntry ad "
                    + " left join advanceReqEntry adr on adr.docid=ad.reqDocId "
                    + " left join pOrder po on po.docid=adr.poDocId "
                    + " where adr.poDocid= :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setAdvanceEntryDocId(DmShared.iNull(rsTemp.getString("docId")).toString());

            }

            psTemp.close();
            rsTemp.close();
            if (DmShared.iNull(mrTemp.getPoDocId()).longValue() != 0) {
                mrTemp.getLstGrdAdvPaymentDetails().addAll(getGrdAdvPaymnetDetail(mrTemp.getPoDocId(), cnTemp));;
            }

            sqlQuery = " select ad.docid,ad.reqDocid ,fpr.pbDocid\n"
                    + "			from advanceEntry ad\n"
                    + "			left join advanceReqEntry adr on adr.docId=ad.reqDocId\n"
                    + "			left join pBillDetail pb on pb.poDocId=adr.poDocid\n"
                    + "			left join FinPayReqEnt fpr on fpr.pbDocid=pb.docId\n"
                    + "			where adr.poDocid=:docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setpBillDocid(DmShared.iNull(rsTemp.getString("pbDocid")).toString());

            }

            psTemp.close();
            rsTemp.close();
            if (DmShared.iNull(mrTemp.getpBillDocid()).longValue() != 0) {
                mrTemp.getLstGrdFinalPaymentDetails().addAll(getGrdFinalPaymentDetail(mrTemp.getpBillDocid(), cnTemp));
            }

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
            sqlQuery = "Select (fpe.docid) as docid ,max(fpe.recid) as recid ,"
                    + " max(fpe.v_No) as v_No,max(fpe.v_date) as date ,"
                    + " max(fpe.module) as module,max(fpe.supplierCode)as supplierCode,"
                    + " max(sg1.name) as supplierName,\n"
                    + " max(fpe.remark) as remark ,max(cm.code) as cardTypeCode, "
                    + " max(cm.name) As cardTypeName, "
                    + " max(cm.cardType) as cardType, max(cm.shortName) as cardShortName,"
                    + " max(cl.amtDr) as amtDr\n"
                    + " , max(cl.amtCr) as amtCr, \n"
                    + " max(sg.subcode) as subcode, max(sg.name) as acName,"
                    + " max(LGTDS.AmtDr) As TDSDeducted,"
                    + " max(lg.tdsOnAmt) as tdsOnAmt,max(LG.OrignalAmt) as finalAmt , \n"
                    + " max(sg.nature) as acNature, max(cm.nature) as cardTypeNature,"
                    + " max(cl.cardNo) as insNo,"
                    + " max(cl.cardHolderName) as remak,max(cl.cardDate) as insDate\n"
                    + " from FinPayEnt fpe  \n"
                    + " LEFT JOIN FinPayEntDetail AS fped ON fped.docid = fpe.docid \n"
                    + " LEFT JOIN FinPayReqEnt AS fpre ON fpre.docid = fped.payReqDocId  \n"
                    + " LEFT JOIN pBill AS pbd ON pbd.docid = fpre.pbDocId \n"
                    + " LEFT JOIN advanceEntry AS ad ON ad.finPayReqDocId = fpre.docid \n"
                    + " LEFT JOIN advanceReqEntry AS adre ON adre.docid = ad.docId\n"
                    + " LEFT JOIN voucher_Type AS vt ON fpe.v_Type = vt.v_Type \n"
                    + " left join ledger lg  On fpe.docId = lg.docid \n"
                    + " Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                    + " left join cardLedger cl  On LG.docId = cl.docid  And cl.V_SNo=LG.V_SNo  \n"
                    + " left join cardTypeMast cm On cl.cardTypeCode = cm.code \n"
                    + " left join subGroup sg On cl.subcode = sg.subCode \n"
                    + " left join subGroup sg1 On fpe.supplierCode = sg1.subCode\n"
                    + " WHERE fpre.pbDocid=:docId \n"
                    + " and lg.isSystem_Generated=0 \n"
                    + " group by fpe.docid";
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

    private List<GrdAdvPaymentDetail_Mdl> getGrdAdvPaymnetDetail(String docId, Connection cnTemp) {
        List<GrdAdvPaymentDetail_Mdl> nodeList = new ArrayList<GrdAdvPaymentDetail_Mdl>();
        GrdAdvPaymentDetail_Mdl node = new GrdAdvPaymentDetail_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            /*====================================< Get advanceEntry Detail >====================================*/
            sqlQuery = "Select ad.docId,ad.v_date,ad.recId,cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, cm.shortName as cardShortName, cl.amtDr, cl.amtCr, "
                    + "     sg.subcode, sg.name as acName,LGTDS.AmtDr As TDSDeducted,lg.tdsOnAmt,LG.OrignalAmt,"
                    + "     sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,cl.cardHolderName as remak,cl.cardDate as insDate"
                    + "     from advanceEntry ad"
                    + "      left join advanceReqEntry adr on adr.docid=ad.reqDocId "
                    + "      left join pOrder po on po.docid=adr.poDocId "
                    + "	     left join ledger lg  On ad.docId = lg.docid "
                    + "      Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' "
                    + "      left join cardLedger cl  On LG.docId = cl.docid  And cl.V_SNo=LG.V_SNo "
                    + "      left join cardTypeMast cm On cl.cardTypeCode = cm.code "
                    + "      left join subGroup sg On cl.subcode = sg.subCode "
                    + "      where adr.poDocid=:docId and  ad.approvedBy is not null and lg.isSystem_Generated=0 "
                    + "      order by ad.v_no ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());

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
                node.setAmt(DmServer.vNull(rsTemp.getString("OrignalAmt"), "0.00"));
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
    public DmResult<AdvanceRequestEntry_Mdl> getPurchaseOrderDetail(String poNo, String poDocId, String type) {
        DmResult<AdvanceRequestEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Purchase Order detail >>===============================*/
            DmResult<AdvanceRequestEntry_Mdl> resultPurchaseOrderDetail = getPurchaseOrderDetail(false, poNo, poDocId, type, cnTemp);
            dmrTemp.setData(resultPurchaseOrderDetail.getData());

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

    public DmResult<AdvanceRequestEntry_Mdl> getPurchaseOrderDetail(boolean isMoveRec, String poNo, String poDocId, String type, Connection cnTemp) throws DmException, SQLException {
        DmResult<AdvanceRequestEntry_Mdl> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "";

        AdvanceRequestEntry_Mdl mrTemp = new AdvanceRequestEntry_Mdl();
        dmrTemp.setData(mrTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            condition += " and (vt.category in ('" + type + "')) ";
            if (DmShared.iNull(poDocId).longValue() != 0) {
                condition += " and po.docId = :poDocId ";
            } else if (!DmShared.xNull(poNo).trim().equalsIgnoreCase("")) {
                condition += " and po.recId = :poNo ";
            }

            sqlQuery = "Select   po.docid as poDocId , po.recId, "
                    + "		po.approvedBy "
                    + "		from pOrder po "
                    + "		left join voucher_Type vt on vt.v_Type = po.v_Type "
                    + "		where po.site_Code = :siteCode " + condition
                    + "		order by po.v_Date desc,  po.v_No desc, po.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(poDocId).longValue() != 0) {
                psTemp.setString("poDocId", DmShared.iNull(poDocId).toString());
            } else if (!DmShared.xNull(poNo).trim().equalsIgnoreCase("")) {
                psTemp.setString("poNo", DmShared.xNull(poNo));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                if (DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record has not been approved.");
                }
                mrTemp.setPoDocId(DmShared.iNull(rsTemp.getString("poDocId")).toString());
                mrTemp.setPoRecId(DmShared.xNull(rsTemp.getString("recId")).toString());
            } else {
                throw new DmException(0, "No record found. Try Again.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select ade.docid ,ade.approvedBy from advanceReqEntry adr  "
                    + " left join advanceEntry ade on adr.docid=ade.reqDocid "
                    + " where  adr.poDocid=:poDocid and adr.isStatus!=1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setLong("poDocid", DmShared.iNull(mrTemp.getPoDocId()).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("docid")).trim().equals("")) {
                    throw new DmException(0, "Can not select some req. Entry of this P.O. is still pending first settle it.");
                }
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("")) {
                    throw new DmException(0, "Can't select some advance entry for this P.O. is still un-approved first approved it.");
                }

            }
            rsTemp.close();
            psTemp.close();

            getPoDetail(type, mrTemp, cnTemp);

            mrTemp.getLstGrdPoDetail().addAll(getGrdPoDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdMrnDetail().addAll(getGrdMrnDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdBillDetail().addAll(getGrdBillDetail(mrTemp.getPoDocId(), cnTemp));

            sqlQuery = " Select ad.docid as docId "
                    + " from advanceEntry ad "
                    + " left join advanceReqEntry adr on adr.docid=ad.reqDocId "
                    + " left join pOrder po on po.docid=adr.poDocId "
                    + " where adr.poDocid= :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setAdvanceEntryDocId(DmShared.iNull(rsTemp.getString("docId")).toString());

            }

            psTemp.close();
            rsTemp.close();
            if (DmShared.iNull(mrTemp.getPoDocId()).longValue() != 0) {
                mrTemp.getLstGrdAdvPaymentDetails().addAll(getGrdAdvPaymnetDetail(mrTemp.getPoDocId(), cnTemp));;
            }

            sqlQuery = " select ad.docid,ad.reqDocid ,fpr.pbDocid\n"
                    + "			from advanceEntry ad\n"
                    + "			left join advanceReqEntry adr on adr.docId=ad.reqDocId\n"
                    + "			left join pBillDetail pb on pb.poDocId=adr.poDocid\n"
                    + "			left join FinPayReqEnt fpr on fpr.pbDocid=pb.docId\n"
                    + "			where adr.poDocid=:docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setpBillDocid(DmShared.iNull(rsTemp.getString("pbDocid")).toString());

            }

            psTemp.close();
            rsTemp.close();
            if (DmShared.iNull(mrTemp.getpBillDocid()).longValue() != 0) {
                mrTemp.getLstGrdFinalPaymentDetails().addAll(getGrdFinalPaymentDetail(mrTemp.getpBillDocid(), cnTemp));
            }
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

    private List<GrdPODetail_Mdl> getGrdPoDetail(String poDocId, Connection cnTemp) {
        List<GrdPODetail_Mdl> nodeList = new ArrayList<GrdPODetail_Mdl>();
        GrdPODetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get PO Detail >====================================*/
            sqlQuery = "Select pod.docid,pod.itemcode,im.name as itemName,pod.unitcode,um.unit as unit,pod.quantity,pod.rate,pod.oh_amt_net as amt "
                    + " from pOrderDetail pod "
                    + " LEFT JOIN itemMast im ON pod.itemcode = im.code "
                    + " LEFT JOIN unitmast um ON pod.unitcode = um.code "
                    + " where pod.docid = :docid  "
                    + " order by pod.v_sno ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(poDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPODetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                node.setQty(DmServer.vNull(rsTemp.getString("quantity"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmt(DmServer.vNull(rsTemp.getString("amt"), "0.00"));
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

    private List<GrdGRNDetail_Mdl> getGrdMrnDetail(String poDocId, Connection cnTemp) {
        List<GrdGRNDetail_Mdl> nodeList = new ArrayList<GrdGRNDetail_Mdl>();
        GrdGRNDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get GRN Detail >====================================*/
            sqlQuery = "    Select  pc.docid,pc.v_date as grnDate,pc.recid as grnNo,    "
                    + "     pc.pChallanNo,pc.pChallanDate,pcd.oh_amt_net as value,  "
                    + "     pcd.podocid,pc.preparedBy,pcd.isStatus,im.name as itemName  "
                    + "     from pChallan pc    "
                    + "     LEFT JOIN pChallanDetail pcd  ON pc.docid = pcd.docid   "
                    + "     LEFT JOIN itemMast im ON pcd.itemcode = im.code "
                    + "     where  pcd.poDocId = :docid and pcd.poDocId is not null     "
                    + "     and   pc.approvedBy is not null     "
                    + "      order by pc.v_no   ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(poDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdGRNDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("grnDate")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setGrnNo(DmShared.xNull(rsTemp.getString("grnNo")));
                node.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                node.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));
                node.setValue(DmServer.vNull(rsTemp.getString("value"), "0.00"));
                node.setEntryBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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

    private List<GrdBillDetail_Mdl> getGrdBillDetail(String poDocId, Connection cnTemp) {
        List<GrdBillDetail_Mdl> nodeList = new ArrayList<GrdBillDetail_Mdl>();
        GrdBillDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Bill Detail >====================================*/
            sqlQuery = "Select pb.docId,pb.v_date,pb.recId,pb.partyBillNo,pb.partyBillDate,"
                    + "     pbd.oh_Amt_Net as value,"
                    + "     pbd.oh_Amt_Net as netPay,pb.preparedBy ,pbd.isStatus,im.name as itemName "
                    + "     from pBill pb "
                    + "     LEFT JOIN pBillDetail pbd  ON pbd.docid = pb.docid "
                    + "     LEFT JOIN itemMast im ON pbd.itemcode = im.code "
                    + "     where  pbd.poDocId = :docid and pbd.poDocId is not null  and  "
                    + "     pb.approvedBy is not null "
                    + " order by pb.v_no ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(poDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdBillDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setBillNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setPartyNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setPartyDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                node.setValue(DmServer.vNull(rsTemp.getString("value"), "0.00"));
                node.setNetPay(DmServer.vNull(rsTemp.getString("netPay"), "0.00"));
                node.setEnterdBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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

    @Override
    public DmResult<String> deleteRecord(String strKey, String remark) {
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
            sqlQuery = "Select approvedBy ,reqDocId from advanceEntry where reqDocId = :reqDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("reqDocId", DmShared.iNull(strKey).longValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equals("")) {
                    throw new DmException(0, "Advace Entry Record has been approved. You can't delete this record.");
                } else if (!DmShared.xNull(rsTemp.getString("reqDocId")).trim().equals("")) {
                    throw new DmException(0, " You can't delete this record advace entry done.");
                }

            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Delete record from advanceReqEntry >>===============================*/
            sqlQuery = " select Count(isStatus) as cnt from advanceReqEntry where docId = :docId AND isStatus = :isStatus";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.setInt("isStatus", 1);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                    throw new DmException(0, "Record is already Discarded.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " UPDATE advanceReqEntry SET "
                    + " isStatus = :isStatus, "
                    + " discardRemark = :discardRemark "
                    + " WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            psTemp.setInt("isStatus", 1);
            psTemp.setString("discardRemark", DmShared.xNull(remark));
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

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select adr.docId "
                    + "                        from advanceReqEntry adr "
                    + "                   	left join voucher_Type as vt on adr.v_Type = vt.v_Type "
                    + "                         where  vt.category = " + vCat + ""
                    + "                   	Order by  adr.v_Date,  adr.v_No, adr.recId ");
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

            sqlQuery = " Select adr.docid ,adr.recid as adReqNo,adr.v_No,adr.v_date as adReqDate,adr.module,adr.poDocid,po.recId as poNo,adr.advanceReqAmt,"
                    + " adr.dueDate, adr.preparedBy,adr.u_Name ,adr.isStatus "
                    + " from advanceReqEntry adr "
                    + " LEFT JOIN pOrder po on po.docid=adr.podocid "
                    + " LEFT JOIN voucher_Type AS vt ON adr.v_Type = vt.v_Type "
                    + "	 WHERE vt.category in (" + vCat + ") "
                    + "  AND adr.site_Code = :site_Code "
                    + "  AND adr.v_prefix = :vPrefix";
            // condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
//            if (!condition.equalsIgnoreCase("")) {
//                sqlQuery += " AND " + condition;
//            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("adReqNo")));
                listNode.setReqDate(DmShared.dNull(rsTemp.getDate("adReqDate")));

                listNode.setModuleTag(DmShared.xNull(rsTemp.getString("module")));
                listNode.setModuleText(DmShared.xNull(listNode.getModuleTag()).equalsIgnoreCase(pvc.PURCHASE_ORDER) ? "Purchase" : "Pharmacy");

                listNode.setDueDate(DmShared.dNull(rsTemp.getDate("dueDate")));

                listNode.setPoNoTag(DmShared.xNull(rsTemp.getString("poDocid")));
                listNode.setPoNoText(DmShared.xNull(rsTemp.getString("poNo")));

                listNode.setAdvanceReqAmt(DmServer.vNull(rsTemp.getString("advanceReqAmt"), "0.00"));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                listNode.setModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                listNode.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
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
}
