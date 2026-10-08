package aarogya.pharmacy.indentApproval.server;

import aarogya.pharmacy.indentApproval.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class IndentApproval_SrvImpl extends DmRSSPrivate implements IndentApproval_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatPHIND = "'" + pvc.PHARMACY_INDENT + "'";

    @Override
    public DmResultPageing<List<GrdIndentApproval_Mdl>> getFillRecords(IndentApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdIndentApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdIndentApproval_Mdl> lstTemp = new ArrayList<>();
        GrdIndentApproval_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;
        rstTemp.setData(lstTemp);

        try {
            condition = " ";
            join = " ";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  mid.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  mid.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getIndenterCode()).equalsIgnoreCase("")) {
                condition += " AND mid.indentorCode IN(" + DmServer.getParamList(":indentorCode", param.getIndenterCode()) + " )";
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {

                condition += " AND mid.departmentCode IN(" + DmServer.getParamList(":Department_Code", param.getDepartmentCode()) + " )";
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                condition += " AND midd.itemCode IN(" + DmServer.getParamList(":itemCode", param.getItemCode()) + " )";
            }

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("C")) {
                condition += " AND isNull(midd.isStatus,'')=('1')";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(mid.approvedBy,'') = '' AND isNull(midd.isStatus,'') = '0'";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = mid.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(mid.approvedBy,'') <> '' AND isNull(midd.isStatus,'') = '0'";

            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("Com")) {
                condition += "  AND   isNull(midd.isStatus,'') = '2'";
            }

            condition += " AND mid.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";

            sqlQuery = " select mid.departmentCode, sgdm.name AS depName,mid.indentorCode,sg.name AS intendeerName,mid.recId,mid.docId,mid.v_Date,midd.itemCode,mm.name AS medicineName, "
                    + "  midd.reqDate,midd.remark,mid.site_Code,sm.name AS siteName,mid.approvedBy,IsNull(um.decimalPlaces,0) as decimalPlaces, "
                    + "  FORMAT(mid.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime, mid.wtaCode,  "
                    + "  mid.v_Type,vt.short_Name AS IndType, mm.sku as unitCode, um.unit as unitName, "
                    + "  midd.isStatus, midd.reqQty AS SKUQty, midd.v_SNo, midd.aprQty, mid.u_EntDt_LatestLine"
                    + "  from indent AS mid "
                    + "  LEFT JOIN indentDetail AS midd ON midd.docId=mid.docId "
                    + "  LEFT JOIN itemMast AS mm ON mm.code=midd.itemCode "
                    + "  LEFT JOIN unitMast AS um ON mm.sku = um.Code "
                    + "  LEFT JOIN itemMakeMast AS mmm ON mmm.code = mm.itemMakeCode "
                    + "  LEFT JOIN siteMast AS sm ON sm.code=mid.site_Code "
                    + "  LEFT JOIN subGroup AS sg on sg.subCode=mid.indentorCode"
                    + "  LEFT JOIN departmentMast AS dm on dm.subCode=mid.departmentCode"
                    + "  LEFT JOIN subGroup AS sgdm ON sgdm.subCode = dm.subCode "
                    + "  LEFT JOIN voucher_Type AS vt on vt.v_Type=mid.v_Type " + join
                    + "  where vt.short_Name= " + vCatPHIND + " " + condition;
            sqlWithParam.setSQL(sqlQuery);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getIndenterCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("indentorCode", DmShared.xNull(param.getIndenterCode()));
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemCode", DmShared.xNull(param.getItemCode()));
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("Department_Code", DmShared.xNull(param.getDepartmentCode()));            //Department
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdIndentApproval_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                node.setIndDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setIndCatCode(DmShared.xNull(rsTemp.getString("IndType")));
                node.setIndCatName(DmShared.xNull(rsTemp.getString("IndType")));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setMedicineCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setDecimalPlace(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setIndQuantity(DmServer.vNull(rsTemp.getString("SKUQty"), ""));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setApprQuantity(DmServer.vNull(rsTemp.getString("aprQty"), ""));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    node.setIsChecked("1");
                } else {
                    node.setIsChecked("0");
                }
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT im.code, im.name"
                    + " FROM itemMast im "
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND im.isActive = 1"
                    + " AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Cancelled");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Com");
            listNode.setSharedKeyFieldName("Completed");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndenter(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT sg.subCode as indentorCode,sg.name "
                    + "  FROM subgroup sg "
                    + "  LEFT JOIN subgroupsitelist SL ON Sg.subcode = SL.subcode "
                    + "  LEFT JOIN subgroupNature sgn ON sgn.code = sg.nature"
                    + "  WHERE SL.site_Code = :site_Code AND sgn.manualCode = 'EM' and sg.isActive = 1");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("indentorCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        String sqlQuery = "", condition = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PR') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo, String closedIndentApprovalRemark) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (!DmShared.xNull(closedIndentApprovalRemark).equalsIgnoreCase("")) {
                sqlQuery = " Update indentDetail set "
                        + " statusRemark=:statusRemark "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("statusRemark", DmShared.xNull(closedIndentApprovalRemark));
                psTemp.executeUpdate();
            }

            sqlQuery = "select approvedBy from indent where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase(DmServer.getSsnUserName(this))) {
                        throw new DmException(0, "Record is not approved by this user. Please refresh data.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " select Count(isStatus) as cnt from indentDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                    throw new DmException(0, "Record is completed,Please refresh record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (isOpen) {
                sqlQuery = " Update indentDetail set "
                        + " isStatus=:isStatus "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();

                psTemp.close();

            } else {
                sqlQuery = " select Count(isStatus) as cnt from indentDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 1";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is already cancelled,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " select Count(isStatus) as cnt from indentDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is completed,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " UPDATE indentDetail SET "
                        + " isStatus = 1 "
                        + " WHERE docId = :docId AND v_SNo =:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            sqlQuery = " Update indent set "
                    + " u_EntDt_LatestLine=:uEntDt"
                    + " where docId=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResult<String> save(GrdMedicineDetail_Mdl mdl) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (!DmShared.xNull(mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {

                sqlQuery = "select isStatus from indentDetail where docId = :docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
                psTemp.setString("vSNo", DmShared.iNull(mdl.getvSNo()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("1")) {
                        throw new DmException(0, "Record is cancelled.So can't update data.");
                    } else if (DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("2")) {
                        throw new DmException(0, "Record is completed.So can't update data.");
                    }
                }
                rsTemp.close();
                psTemp.close();
                sqlQuery = "select approvedBy from indent where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                        if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase(DmServer.getSsnUserName(this))) {
                            throw new DmException(0, "Record is not approved by this user.So can't update data");
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "select orderedQty FROM indentdetail WHERE  docId = :docId AND v_SNo=:vSNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
                psTemp.setString("vSNo", DmShared.iNull(mdl.getvSNo()).toString());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    if (DmShared.vNull(rsTemp.getString("orderedQty")).doubleValue() > DmShared.vNull(mdl.getApprovedQty()).doubleValue()) {
                        throw new DmException(0, "Approved Qty should be greater than or equal to Order Qty.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " Update indentDetail set "
                        + " aprQty=:aprQty "
                        + " where docId=:docId AND v_SNo=:v_SNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
                psTemp.setString("aprQty", DmServer.vNull(mdl.getApprovedQty(), ""));
                psTemp.setString("v_SNo", DmShared.iNull(mdl.getvSNo()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = " Update indent set "
                        + " u_EntDt_LatestLine=:uEntDt "
                        + " where docId=:docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }
            dmrTemp.setData("");
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
    public DmResult<IndentApproval_Mdl> fillItemGrid(String code) {
        DmResult<IndentApproval_Mdl> dmrTemp = new DmResult<>();
        GrdMedicineDetail_Mdl node;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<GrdMedicineDetail_Mdl>();
        IndentApproval_Mdl mrTemp = new IndentApproval_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  mid.departmentCode,sdm.name AS depName,"
                    + "         mid.recId,mid.docId,mid.v_Date,mid.site_Code,"
                    + "         sm.name AS siteName,mid.approvedBy,FORMAT(mid.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,"
                    + "         mid.site_Code,mid.v_Type,vt.description AS IndType,mid.wtaCode "
                    + "         FROM indent AS mid "
                    + "         LEFT JOIN siteMast AS sm ON sm.code = mid.site_Code "
                    + "         LEFT JOIN subgroup AS sdm ON sdm.subcode = mid.departmentCode "
                    + "         LEFT JOIN voucher_Type AS vt ON vt.v_Type = mid.v_Type "
                    + "         where mid.docId=:docId  And vt.category= " + vCatPHIND + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(code));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new IndentApproval_Mdl();
                mrTemp.setIndDocId(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setIndReccId(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                mrTemp.setIndDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setIndType(DmShared.xNull(rsTemp.getString("IndType")));
                mrTemp.setApprovedDateTime(DmShared.xNull(rsTemp.getString("approvalDateTime")));
                mrTemp.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT  mid.docId, mid.aprQty, mid.itemCode, mid.isStatus, mid.reqDate, mid.reqQty, ump.unit as skuName, mi.approvedBy, "
                    + "         mid.isStatus, mid.v_SNo, im.name AS medicineName, ig.name AS imGroup, imm.companyName AS brandName,IsNull(ump.decimalPlaces,0) as decimalPlaces "
                    + "         ,mid.statusRemark "
                    + "         FROM indentDetail AS mid "
                    + "         LEFT JOIN indent AS mi ON mid.docId = mi.docId "
                    + "         LEFT JOIN itemMast AS im ON mid.itemCode = im.code "
                    + "         LEFT JOIN itemGroup AS ig ON ig.Code = im.itemGroup "
                    + "         LEFT JOIN unitMast AS ump ON im.sku = ump.Code "
                    + "         LEFT JOIN itemMakeMast AS imm ON imm.code = im.itemMakeCode "
                    + "         LEFT JOIN voucher_Type AS vt ON vt.v_Type = mi.v_Type "
                    + "         where mid.docId=:docId  And vt.category= " + vCatPHIND + " "
                    + "         order by medicineName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(code));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicineName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIndQty(DmServer.vNull(rsTemp.getString("reqQty"), ""));
                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setApprovedQty(DmServer.vNull(rsTemp.getString("aprQty"), ""));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setIsChecked(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setIndDocId(DmShared.xNull(rsTemp.getString("docId")));
                node.setDecimalPlace(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setClosedIndentApprovalRemark(DmShared.xNull(rsTemp.getString("statusRemark")));

                listNode.add(node);
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getGrdItemDetail_Mdl().addAll(listNode);

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
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
    public DmResultPageing<List<TxtHIndentNo_Mdl>> getTxtHIndentNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHIndentNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHIndentNo_Mdl> lstTemp = new ArrayList<>();
        TxtHIndentNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT  "
                    + " ind.docId, ind.recId as indRecId, ind.v_Date as indDate, vt.category "
                    + " FROM indent AS ind  "
                    + " left join voucher_Type as vt on vt.v_Type = ind.v_Type"
                    + " where ind.site_Code = '" + DmServer.getSsnSiteCode(this) + "' AND vt.category= " + vCatPHIND + " ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHIndentNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indRecId")));
                listNode.setIndentType(DmShared.xNull(rsTemp.getString("category")));
                listNode.setIndentDate(DmShared.dNull(rsTemp.getDate("indDate")));
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
