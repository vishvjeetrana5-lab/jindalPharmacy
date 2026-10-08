package dataman.accounts.billHandle.server;

import dataman.accounts.billHandle.shared.DefaultValue_Mdl;
import dataman.accounts.billHandle.shared.GrdBillDetail_Mdl;
import dataman.accounts.billHandle.shared.Find_Mdl;
import dataman.accounts.billHandle.shared.BillHandle_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.accounts.billHandle.shared.BillHandle_Srv;
import dataman.accounts.billHandle.shared.BillHandle_Vld;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BillHandle_SrvImpl extends DmRSSPrivate implements BillHandle_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private String v_Category = pvc.BILL_HANDLE;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        String sqlQuery = "", sqlCondition = "";
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT docid as code "
                    + "   FROM billHandle bh "
                    + "   LEFT JOIN voucher_Type vt ON vt.v_Type=bh.v_Type"
                    + "   where vt.category='" + v_Category + "' and bh.site_Code = :site_Code "
                    + "   and bh.v_prefix = :vPrefix "
                    + " Order By bh.v_Date desc , vt.short_Name, bh.v_No desc, bh.recId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstKeyField.add(lstKeyFieldTmp);
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
    public DmResult<BillHandle_Mdl> getMoveRec(String strKey) {
        DmResult<BillHandle_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        BillHandle_Mdl mrTemp = new BillHandle_Mdl();
        List<GrdBillDetail_Mdl> lstTemp = new ArrayList<GrdBillDetail_Mdl>();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  bh.docId,vt.v_Type,bh.module, bh.recId,bh.wtaCode,bh.approvedBy, bh.v_Date,bh.fromLocCode,"
                    + "     bh.toLocCode ,vt.description, bhlm.name as fromLocationName,bhlmt.name as toLocatioName,"
                    + "     bh.preparedBy,"
                    + "     bh.u_Name,bh.billFromDate,bh.billToDate ,bh.recIdPrefix,bh.v_No"
                    + "     from billHandle bh "
                    + "     LEFT JOIN billHandleLocationMaster bhlm ON bhlm.code = bh.fromLocCode "
                    + "     LEFT JOIN billHandleLocationMaster bhlmt ON bhlmt.code = bh.toLocCode "
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type=bh.v_Type "
                    + " where bh.docid=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                mrTemp.getRecid().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecid().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecid().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setFromBilldate(DmShared.dNull(rsTemp.getDate("billFromDate")));
                mrTemp.setToBilldate(DmShared.dNull(rsTemp.getDate("billToDate")));
                mrTemp.setFromLocationName(DmShared.xNull(rsTemp.getString("fromLocationName")));
                mrTemp.setFromLocationCode(DmShared.xNull(rsTemp.getString("fromLocCode")));
                mrTemp.setToLocationName(DmShared.xNull(rsTemp.getString("toLocatioName")));
                mrTemp.setToLocationCode(DmShared.xNull(rsTemp.getString("toLocCode")));
                mrTemp.setBillTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setBillTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setModule(DmShared.xNull(rsTemp.getString("module")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT sgs.dueDays,bhd.docId,bhd.v_SNo , bhd.v_No , bhd.modDocId ,"
                    + "         convert(varchar,bhd.lastStatusDt,22)lastStatusDt ,bhd.remark,"
                    + "         vt.description,pb.v_date,pb.paymentDueDate as paymentDueDate,pb.partyBillNo,"
                    + "         pb.partyBillDate,sgs.name as partyName , "
                    + "         pb.oh_Amt_Net,pb.v_date,pb.v_date "
                    + " from billHandledetail bhd"
                    + "         LEFT join pBill pb on pb.docId= bhd.modDocId "
                    + "         LEFT JOIN voucher_Type vt ON vt.v_Type=pb.v_Type "
                    + "         LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode"
                    + "         where bhd.docid = :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdBillDetail_Mdl node = new GrdBillDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("modDocId")));// pBDOcID
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setVoucherNo(DmShared.xNull(rsTemp.getString("v_No")));
                node.setType(DmShared.xNull(rsTemp.getString("description")));
                node.setDate(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setPartyDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                node.setPartyName(DmShared.xNull(rsTemp.getString("partyName")));
                node.setNetAmt(DmShared.xNull(rsTemp.getString("oh_Amt_Net")));
                node.setPaymentDueDt(calendar(DmShared.iNull(rsTemp.getString("dueDays")).intValue(), DmShared.dNull(rsTemp.getDate("paymentDueDate"))));
                node.setLastStatusDt(DmShared.xNull(rsTemp.getString("lastStatusDt")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setCheck(true);
                lstTemp.add(node);
            }

            mrTemp.getGrdData().addAll(lstTemp);
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
    public DmResult<String> saveRecord(BillHandle_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, voucherType = "", vNo = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            BillHandle_Vld.chkSave(mrTemp);

            boolean isBillHandelExist = false;

            for (GrdBillDetail_Mdl row : mrTemp.getGrdData()) {

                if (row != null && row.isCheck()) {
                    isBillHandelExist = true;
                    break;
                }
            }

            if (!isBillHandelExist) {
                throw new DmException(0, "No any bill selectted.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            voucherType = DmShared.xNull(mrTemp.getBillTypeCode());

            spBegin = new DmSavePoint(cnTemp);

            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {

                sqlQuery = "select count(*) as cnt from  billHandle where docid = :docId and ISNULL(approvedBy,'') <> '' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                        throw new DmException(0, "Record is approved. You can not save this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(voucherType), voucherPrefix, cnTemp, this));
                mrTemp.setRecid(DmServer.getRecId("billHandle", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDate(), DmShared.xNull(voucherType), voucherPrefix, cnTemp, this, true));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), mrTemp.getBillTypeCode(), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("billHandle", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDate(), DmShared.xNull(voucherType), voucherPrefix, cnTemp, this, true);
            }
            vNo = DmShared.iNull(mrTemp.getRecid().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "INSERT INTO billHandle (docId, v_Type,v_No, recId, v_Date, v_Prefix, fromLocCode, toLocCode, site_Code,module,"
                        + " preparedBy,  u_EntDt,u_EntDt_LatestLine,billFromDate,billToDate,recIdPrefix,wtaCode)"
                        + " VALUES (:docid, :v_type,:v_No,  :recid, :v_date, :v_prefix, :fromLocCode, :toLocCode, :site_code,:module, :preparedby,"
                        + " :uEntDt,:uEntDt,:billFromDate,:billToDate,:recIdPrefix, :wtaCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_type", DmShared.xNull(voucherType));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recid", DmShared.xNull(mrTemp.getRecid().getRecIdValue()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecid().getPrefix()));
                psTemp.setDate("v_date", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("v_prefix", DmShared.xNull(voucherPrefix));
                psTemp.setString("fromLocCode", DmShared.foriegnKey(mrTemp.getFromLocationCode()));
                psTemp.setString("module", DmShared.xNull(mrTemp.getModule()));
                psTemp.setString("toLocCode", DmShared.foriegnKey(mrTemp.getToLocationCode()));
                psTemp.setDate("billFromDate", DmServer.convertDate(mrTemp.getFromBilldate()));
                psTemp.setDate("billToDate", DmServer.convertDate(mrTemp.getToBilldate()));
                psTemp.setString("site_code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.executeUpdate();
                psTemp.close();

            }

            //==================================================================
            int vSNo = 0;

            for (GrdBillDetail_Mdl row : mrTemp.getGrdData()) {
                if (!blnAdd) {
                    sqlQuery = "UPDATE billHandleDetail set "
                            + " remark= :remark "
                            + " where docid=:docid and v_SNo=:v_SNo and v_No=:v_No and modDocId=:modDocId ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(++vSNo).toString());
                    psTemp.setString("v_No", DmShared.xNull(row.getVoucherNo()));// purchase Bill voucher_No
                    psTemp.setString("modDocId", DmShared.xNull(row.getSharedKeyFieldCode()));// pBill docid
                    psTemp.setString("remark", DmShared.xNull(row.getRemark()));
                    psTemp.executeUpdate();
                    psTemp.close();
                    continue;
                }

                if (!DmShared.xNull(row.getPartyBillNo()).trim().equals("")) {
                    if (row.isCheck()
                            && !DmShared.xNull(row.getIsStatus()).equalsIgnoreCase("1")
                            && !DmShared.xNull(row.getIsStatus()).equalsIgnoreCase("2")) {
                        sqlQuery = "INSERT INTO billHandleDetail ("
                                + "     docId, v_SNo,v_No , modDocId ,isStatus ,lastStatusDt ,remark)"
                                + " Values ("
                                + "  :docId, :v_SNo,:v_No , :modDocId ,0 ,:lastStatusDt ,:remark)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("v_SNo", DmShared.iNull(++vSNo).toString());
                        psTemp.setString("v_No", DmShared.xNull(row.getVoucherNo()));// purchase Bill voucher_No
                        psTemp.setString("modDocId", DmShared.xNull(row.getSharedKeyFieldCode()));// pBill docid
                        psTemp.setTimestamp("lastStatusDt", DmServer.getServerTimestamp());
                        psTemp.setString("remark", DmShared.xNull(row.getRemark()));
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = "UPDATE pBill set "
                                + " lastLoc = :lastLoc "
                                + " where docid=:docid ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docid", row.getSharedKeyFieldCode());
                        psTemp.setString("lastLoc", DmShared.foriegnKey(mrTemp.getToLocationCode()));
                        psTemp.executeUpdate();
                        psTemp.close();

                        // to put status of pbill previously transfer to 1 
                        sqlQuery = "    update billHandleDetail set reTransfer =1 \n"
                                + "         where modDocId=:modDocId "
                                + "         and docid<>:docid ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docid", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("modDocId", DmShared.xNull(row.getSharedKeyFieldCode()));// pBill docid
                        psTemp.executeUpdate();
                        psTemp.close();

                    }

                }
            }

            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
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
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT bh.docId,vt.short_Name,bh.module, bh.recId, bh.v_Date,bh.fromLocCode,"
                    + " bh.toLocCode, vt.description, bh.approvedBy, "
                    + " bhlm.name as fromLocationName,bhlmt.name as toLocatioName,"
                    + " bh.preparedBy,"
                    + " bh.u_Name,bh.billFromDate,bh.billToDate ,bh.recIdPrefix,bh.v_No"
                    + " from billHandle bh "
                    + " LEFT JOIN billHandleLocationMaster bhlm ON bhlm.code = bh.fromLocCode "
                    + " LEFT JOIN billHandleLocationMaster bhlmt ON bhlmt.code = bh.toLocCode "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=bh.v_Type "
                    + " where bh.site_Code = :site_Code and bh.v_prefix = :vPrefix ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setFromBilldate(DmShared.dNull(rsTemp.getDate("billFromDate")));
                listNode.setToBilldate(DmShared.dNull(rsTemp.getDate("billToDate")));
                listNode.setFromLocationName(DmShared.xNull(rsTemp.getString("fromLocationName")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setToLocationName(DmShared.xNull(rsTemp.getString("toLocatioName")));
                listNode.setModule(DmShared.xNull(rsTemp.getString("module")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHToLocation(DmHelper helper, boolean blnToEndOfFile, String location
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {

            if (DmShared.iNull(location).longValue() > 0) {
                condition += " where dhlm.code != " + location;
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select	dhlm.code, dhlm.name ,dhlm.mappMod "
                    + "		from billHandleLocationMaster as dhlm " + condition);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("mappMod")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHFromLocation(DmHelper helper, boolean blnToEndOfFile, String location
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {

            if (!DmBaseServer.getSsnLocationCodeList(this).equalsIgnoreCase("")) {
                condition = " AND bhlm.code in (" + DmServer.getParamList(":code", DmBaseServer.getSsnLocationCodeList(this)) + ")";

            }
            if (DmShared.iNull(location).longValue() > 0) {
                condition += " AND bhlm.code != " + location;
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select	bhlm.code, bhlm.name ,bhlm.mappMod "
                    + "		from billHandleLocationMaster as bhlm where 1=1 " + condition);
            if (!DmBaseServer.getSsnLocationCodeList(this).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("code", DmBaseServer.getSsnLocationCodeList(this));
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("mappMod")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT V_type as code, Description as name FROM Voucher_Type VT WHERE VT.IsActive=1 And Category = '" + v_Category + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setBillHandleTypeCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setBillHandleTypeName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.setCurrentDate(DmServer.getServerDateTime()[0]);
            mrTemp.setCurrentTime(DmServer.getServerDateTime()[1]);

            dmrTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRequisitiontype(DmHelper helper, boolean blnToEndOfFile
    ) {
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
            sqlWithParam.setSQL("SELECT V_type, Description FROM Voucher_Type VT WHERE Category = '" + v_Category + "'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_type")));
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

    private String calendar(int value, String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        Date dtSchedule = sdf.parse(date);

        cal.setTime(dtSchedule);
        cal.add(Calendar.DATE, value);

        String newDate = sdf.format(cal.getTime());

        return newDate;

    }

    @Override
    public DmResult<List<GrdBillDetail_Mdl>> getPBillByModuleLocation(String fromLocationModule, String fromDate, String toDate, String fromLocation) {
        DmResult<List<GrdBillDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        GrdBillDetail_Mdl node;
        List<GrdBillDetail_Mdl> lstNode = new ArrayList<>();
        dmrTemp.setData(lstNode);

        boolean isFromLocationIsAccount = false;

        try {
            if (DmShared.xNull(fromLocationModule).equalsIgnoreCase("Purchase")) {
                condition = " and vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', "
                        + "     '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "',"
                        + "     '" + pvc.PURCHASE_BILL + "', "
                        + "     '" + pvc.PURCHASE_BILL_UNREGISTERED + "')";
            } else if (DmShared.xNull(fromLocationModule).equalsIgnoreCase("Pharmacy")) {
                condition = " and vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', "
                        + "     '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "',"
                        + "     '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', "
                        + "     '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            } else if (DmShared.xNull(fromLocationModule).equalsIgnoreCase("Service")) {
                condition = " and vt.category IN ('" + pvc.SERVICE_BILL_UNREGISTERED + "', "
                        + "     '" + pvc.SERVICE_BILL + "')";
            } else if (DmShared.xNull(fromLocationModule).equalsIgnoreCase("Accounts")) {
//                condition = " and 1!=1 ";
                condition = "";
                isFromLocationIsAccount = true;
            }

            if (DmShared.iNull(fromLocation).longValue() > 0) {
                if (isFromLocationIsAccount) {
                    condition += "  AND  ( pb.lastLoc =" + fromLocation + " and isNull(bhd.approvedBy,'')<>'' )";
                } else {
                    condition += "  AND isNull(pb.lastLoc,0) =0  OR ( pb.lastLoc =" + fromLocation + " and isNull(bhd.approvedBy,'')<>'' )";
                }

            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "    SELECT  "
                    + "         max(pb.lastLoc) as lastLoc,   "
                    + "         max(sgs.dueDays) as dueDays,                                 "
                    + "         max(pb.docId) as docId ,max(vt.description) as voucherType , "
                    + "         max(bhd.lastStatusDt) as lastStatusDt,max(pb.v_No) as v_No,  "
                    + "         max(pb.v_Date) as v_Date,                                   "
                    + "         max(pb.partyBillNo) as partyBillNo ,                        "
                    + "         max(pb.partyBillDate) as partyBillDate,max(pb.consignee) consignee, "
                    + "         max(sgs.name) AS partyName,max(pb.oh_Amt_Net) as oh_Amt_Net ,           "
                    + "         max(pb.paymentDueDate) as paymentDueDate,                                   "
                    + "         max(pb.remark) as remark, max (pb.lastloc)lastloc   "
                    + "    FROM pBill pb    "
                    + "           LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type        "
                    + "           LEFT JOIN subGroup sgd ON pb.departmentCode = sgd.subCode "
                    + "           LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode            "
                    + "           LEFT JOIN city c ON c.cityCode = sgs.cityCode             "
                    + "          LEFT JOIN (\n"
                    + "                 select bh.approvedBy, bhd.modDocId, bhd.lastStatusDt "
                    + "                 from pBill pb           \n"
                    + "                 LEFT JOIN billhandleDetail bhd ON bhd.modDocId = pb.docId    \n"
                    + "                 left join billhandle bh ON bh.docId = bhd.docId         \n"
                    + "                     where pb.site_Code =:site_Code  and pb.lastLoc =:fromLocation     "
                    + "                      and bh.toLocCode =:fromLocation "
                    + "                      and isNUll(bhd.retransfer,0)<>1"
                    + "		)    bhd  on bhd.modDocId = pb.docId"
                    + "    where pb.site_Code =:site_Code and pb.v_Date  between :fromDate and :toDate  " + condition
                    + "    Group BY pb.DocId \n"
                    + "    order BY pb.DocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(fromDate));
            psTemp.setDate("todate", DmServer.convertDate(toDate));
            psTemp.setString("fromLocation", DmShared.iNull(fromLocation).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdBillDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("voucherType")));
                node.setType(DmShared.xNull(rsTemp.getString("voucherType")));
                node.setVoucherNo(DmShared.xNull(rsTemp.getString("v_No")));
                node.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setPartyDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                node.setPartyName(DmShared.xNull(rsTemp.getString("partyName")));
                node.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                node.setPaymentDueDt(calendar(DmShared.iNull(rsTemp.getString("dueDays")).intValue(), DmShared.dNull(rsTemp.getDate("paymentDueDate"))));
                {
                    node.setLastStatusDt(DmShared.xNull(rsTemp.getString("lastStatusDt")));
                }
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setLastLoc(DmShared.xNull(rsTemp.getString("lastLoc")));
                lstNode.add(node);
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
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
            }
            dmrTemp.setData(docId);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from billHandle where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = " Update billHandle set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update billHandle set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    documentApproval(walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    private WalkThroughDefault_Mdl getWalkThroughFor(BillHandle_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getBillTypeCode());

        return walkThroughDefault;
    }
//   
}
