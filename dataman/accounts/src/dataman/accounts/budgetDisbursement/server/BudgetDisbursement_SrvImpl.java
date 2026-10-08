package dataman.accounts.budgetDisbursement.server;

import dataman.accounts.budgetDisbursement.server.*;
import dataman.accounts.budgetDisbursement.shared.*;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public class BudgetDisbursement_SrvImpl extends DmRSSPrivate implements BudgetDisbursement_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private final String vCat = "'" + pvc.BUDGET_DISBURSEMENT + "'";
    private final String voucherTypeFieldName = "mid.V_Type", voucherDateFieldName = "mid.V_Date";

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

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select ba.docId "
                    + "                        from budgetAllocation ba "
                    + "                   	left join voucher_Type as vt on ba.v_Type = vt.v_Type "
                    + "                         where ba.site_code=:site_code and vt.category in (" + vCat + ")"
                    + "                   	Order by  ba.v_Date,  ba.v_No, ba.recId ");
            psTemp.setString("site_Code", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
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
    public DmResult<BudgetDisbursement_Mdl> getMoveRec(String strKey) {
        DmResult<BudgetDisbursement_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        BudgetDisbursement_Mdl mrTemp = new BudgetDisbursement_Mdl();
        dmrTemp.setData(mrTemp);
        GrdBudgetDisbursementDetail_Mdl node;
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select ba.docID,ba.recId,ba.v_Date,ba.v_Type,ba.v_Prefix,ba.v_No,ba.recIdPrefix,ba.budgetDescription as budgetName\n"
                    + " ,ba.u_Name,ba.u_Entdt,ba.preparedBy,ba.preparedDate,ba.approvedBy,ba.approvalDateTime,vt.description,vt.category\n"
                    + " ,bad.fromDate,bad.toDate\n"
                    + " 	from budgetAllocation ba \n"
                    + "         LEFT JOIN (\n"
                    + "                   select docid,min(fromDate) as fromDate,max(toDate) as toDate \n"
                    + "                     from budgetAllocationDetail where docID=:docID group by docID\n"
                    + "                   ) as bad on ba.docID=bad.docID\n"
                    + "         LEFT JOIN voucher_type vt ON ba.v_Type = vt.v_Type\n"
                    + " 	where ba.docID=:docID;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setBudgetNameText(DmShared.xNull(rsTemp.getString("budgetName")));
                mrTemp.setDtAllocation(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setAllocationTypeTag(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setAllocationTypeText(DmShared.xNull(rsTemp.getString("description")));

                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));

                mrTemp.setModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));

                mrTemp.getRecid().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecid().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecid().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " select bad.v_Sno,bad.headCode as budGetHeadCode,bad.costCenter,bad.plusMinus,bad.budget,bad.remark,bad.fromDate\n"
                    + " ,bad.toDate,bhm.headName as budgetHeadName,bhmu.headName as underHeadName\n"
                    + " ,ccm.name as costCenterName,case when bad.plusMinus=0 then '-' else '+' end as plusMinusText\n"
                    + " 	from budgetAllocationDetail bad\n"
                    + " 	left join budgetHeadMast bhm on bad.headCode=bhm.headCode\n"
                    + " 	left join budgetHeadMast bhmu on bhm.headUnder=bhmu.headcode\n"
                    + " 	left join costCenterMast ccm on bad.costCenter=ccm.code\n"
                    + " 	where bad.docID=:docID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new GrdBudgetDisbursementDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("budGetHeadCode")));
                node.setBudgetHeadTag(DmShared.xNull(rsTemp.getString("budGetHeadCode")));
                node.setBudgetHeadText(DmShared.xNull(rsTemp.getString("budgetHeadName")));
                node.setUnderHeadText(DmShared.xNull(rsTemp.getString("underHeadName")));
                node.setCostcenterTag(DmShared.xNull(rsTemp.getString("costCenter")));
                node.setCostcenterText(DmShared.xNull(rsTemp.getString("costCenterName")));
                node.setPlusMinus(DmShared.xNull(rsTemp.getString("plusMinus")));
                node.setPlusMinusText(DmShared.xNull(rsTemp.getString("plusMinusText")));
                node.setBudget(DmServer.vNull(rsTemp.getString("budget")));
                node.setvSno(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                node.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                node.setRemarks(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.getGrdBudgetHeadDetails().add(node);
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
    public DmResult<String> saveRecord(BudgetDisbursement_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, vNo = "";
        dmrTemp.setData("");

        try {
            BudgetDisbursement_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {

//                STServer.chkIfDocumentUsed("indentDocId", "pOrderIndentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
//                STServer.chkIfDocumentUsed("indentDocId", "enquiryIndentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
//                STServer.chkIfDocumentUsed("indentDocId", "quotationIndentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
                sqlQuery = "select count(*) as cnt from  budgetAllocation where docid = :docId and ISNULL(approvedBy,'') <> '' ";
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
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getAllocationTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecid(DmServer.getRecId("budgetAllocation", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDtAllocation(), DmShared.xNull(mrTemp.getAllocationTypeTag()), voucherPrefix, cnTemp, this, true));
            } else {
                DmServer.getRecId("budgetAllocation", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDtAllocation(), DmShared.xNull(mrTemp.getAllocationTypeTag()), voucherPrefix, cnTemp, this, true);
            }
            vNo = DmShared.iNull(mrTemp.getRecid().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< SAVE & UPDATE >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO budgetAllocation (docID,v_Type,v_No,recId,recIdPrefix,v_Prefix,v_Date,budgetDescription,u_EntDt"
                        + " ,u_Name,preparedBy,preparedDate,site_Code) "
                        + "VALUES (:docID, :v_Type, :v_No, :recId, :recIdPrefix, :v_Prefix, :v_Date, :budgetDescription, :u_EntDt"
                        + " ,:u_Name, :preparedBy, :preparedDate, :site_Code) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("v_Type", DmShared.xNull(mrTemp.getAllocationTypeTag()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recId", mrTemp.getRecid().getRecIdValue());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecid().getPrefix()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtAllocation()));
                psTemp.setString("budgetDescription", DmShared.foriegnKey(mrTemp.getBudgetNameText()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("site_code", DmServer.getSsnSiteCode(this));

            } else {
                sqlQuery = "UPDATE budgetAllocation SET"
                        + "  budgetDescription = :budgetDescription ,"
                        + "  u_EntDt = :uEntDt ,"
                        + "  u_name = :u_name, "
                        + "  v_Date = :v_Date "
                        + "  WHERE docId = :docId ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("budgetDescription", DmShared.xNull(mrTemp.getBudgetNameText()));
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtAllocation()));
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "DELETE FROM budgetAllocationDetail WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            int vSNo = 0;
            for (int i = 0; i < mrTemp.getGrdBudgetHeadDetails().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdBudgetHeadDetails().get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    GrdBudgetDisbursementDetail_Mdl detail = mrTemp.getGrdBudgetHeadDetails().get(i);
                    if (DmShared.iNull(detail.getvSno()).intValue() > vSNo) {
                        vSNo = DmShared.iNull(detail.getvSno()).intValue();
                    } else {
                        ++vSNo;
                    }

                    detail.setvSno(String.valueOf(vSNo));
                    sqlQuery = "INSERT INTO budgetAllocationDetail (docID, v_SNo, headCode, costCenter, plusMinus, budget, remark, fromDate,toDate) "
                            + "VALUES (:docID, :v_SNo, :headCode, :costCenter, :plusMinus, :budget, :remark, :fromDate, :toDate)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setInt("v_SNo", DmShared.iNull(detail.getvSno()).intValue());
                    psTemp.setString("headCode", DmShared.foriegnKey(detail.getBudgetHeadTag()));
                    psTemp.setString("costCenter", DmShared.foriegnKey(detail.getCostcenterTag()));
                    psTemp.setString("plusMinus", DmShared.xNull(detail.getPlusMinus()));
                    psTemp.setString("budget", DmServer.vNull(detail.getBudget()));
                    psTemp.setString("remark", DmShared.xNull(detail.getRemarks()));
                    psTemp.setDate("fromDate", DmServer.convertDate(detail.getFromDate()));
                    psTemp.setDate("toDate", DmServer.convertDate(detail.getToDate()));

                    psTemp.executeUpdate();
                    psTemp.close();

//                        ===============================================================================================================
                }
            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
//                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getIndentTypeTag(), this);
            }
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select approvedBy FROM budgetAllocation WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

//            STServer.chkIfDocumentUsed("indentDocId", "pOrderIndentDetail", strKey, cnTemp);
//            STServer.chkIfDocumentUsed("indentDocId", "enquiryIndentDetail", strKey, cnTemp);
//            STServer.chkIfDocumentUsed("indentDocId", "quotationIndentDetail", strKey, cnTemp);
            sqlQuery = "Delete From budgetAllocationDetail Where docid= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From budgetAllocation Where docid= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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
        String sqlQuery = "", sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT	ba.docID,ba.recId,ba.v_Date as allcationDate,ba.budgetDescription as budgetName\n"
                    + "        ,bad.fromDate,bad.toDate,ba.preparedBY,ba.approvedBy\n"
                    + "        FROM budgetAllocation ba\n"
                    + "        left join voucher_Type vt on ba.v_Type=vt.v_Type \n"
                    + "        left join (\n"
                    + "			  select docid,min(fromDate) as fromDate,max(toDate) as toDate from budgetAllocationDetail group by docid\n"
                    + "                   ) bad on ba.docID=bad.docID"
                    + "        where ba.site_Code =  :site_Code and ba.v_prefix=:v_prefix and vt.category=" + vCat + " ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setBudgetName(DmShared.xNull(rsTemp.getString("budgetName")));
                listNode.setAllcationDate(DmShared.dNull(rsTemp.getDate("allcationDate")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBY")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDisbursementType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT V_type, Description,category FROM Voucher_Type VT WHERE Category in (" + vCat + ")");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl lstTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select top 1 description,V_Type,category from voucher_Type where category in (" + vCat + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                lstTemp.setAllocationTypeTag(DmShared.xNull(rsTemp.getString("V_Type")));
                lstTemp.setAllocationTypeText(DmShared.xNull(rsTemp.getString("description")));
                lstTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstTemp);

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
            ////////////////////////////////////////////////////
//            STServer.chkIfDocumentUsed("indentDocId", "pOrderIndentDetail", keyCode, cnTemp);
//            STServer.chkIfDocumentUsed("indentDocId", "enquiryIndentDetail", keyCode, cnTemp);
//            STServer.chkIfDocumentUsed("indentDocId", "quotationIndentDetail", keyCode, cnTemp);
//////////////////////////////////////////////////////
            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from budgetAllocation where docId = :docId ";
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBudgetName(String voucherCategory, DmHelper pageing, boolean blnToEndOfFile) {
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

            sqlQuery = "select distinct ba.budgetDescription as code,ba.budgetDescription as name\n"
                    + "	from budgetAllocation ba";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPlusMinus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("-");
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
    public DmResultPageing<List<TxtHBudgetHead_Mdl>> getTxtHBudgetHeadAdjustment(String voucherCategory, String budgetDescription, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBudgetHead_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBudgetHead_Mdl> lstTemp = new ArrayList<>();
        TxtHBudgetHead_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select max(ba.docID) as docID,max(bhm.headCode) as headCode,max(bhm.headName) as budgetHeadName\n"
                    + " ,max(bhmu.headName) as underHeadName,max(bad.costCenter) as costCenter,max(ccm.name) as costCenterName\n"
                    + " ,sum(case when bad.plusMinus =0 then -bad.budget when bad.plusMinus =1 then bad.budget else 0 end ) as balanceBudget\n"
                    + " ,min(bad.fromdate) as fromdate,max(bad.toDate) as toDate\n"
                    + "		from budgetHeadMast bhm \n"
                    + "		left join budgetHeadMast bhmu on bhm.headUnder=bhmu.headCode"
                    + "		left join budgetAllocationDetail bad on bhm.headCode=bad.headCode\n"
                    + "		left join costCenterMast ccm on bad.costCenter=ccm.code\n"
                    + "		left join budgetAllocation ba on bad.docId=ba.docID\n"
                    + "		left join voucher_Type vt on ba.v_Type=vt.v_Type\n"
                    + "		where ba.budgetDescription=:budgetDescription and bad.headCode is not null \n"
                    + "		group by bhm.headName,bhm.headCode,bad.costCenter";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("budgetDescription", budgetDescription.trim());

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBudgetHead_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.vNull(listNode.getSharedKeyFieldHashCode()).toString());
                listNode.setBudgetHeadCode(DmShared.xNull(rsTemp.getString("headCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("budgetHeadName")));
                listNode.setBudgetHeadName(DmShared.xNull(rsTemp.getString("budgetHeadName")));
                listNode.setUnderHeadName(DmShared.xNull(rsTemp.getString("underHeadName")));
                listNode.setCostCenterTag(DmShared.xNull(rsTemp.getString("costCenter")));
                listNode.setCostCenterText(DmShared.xNull(rsTemp.getString("costCenterName")));
                listNode.setBalanceBudget(DmShared.vNull(rsTemp.getString("balanceBudget")).toString());
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
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
