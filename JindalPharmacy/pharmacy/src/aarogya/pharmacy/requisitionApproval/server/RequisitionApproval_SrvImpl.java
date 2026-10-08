package aarogya.pharmacy.requisitionApproval.server;

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
import aarogya.pharmacy.requisitionApproval.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.ConvertedQuantity;
import java.sql.SQLException;

public class RequisitionApproval_SrvImpl extends DmRSSPrivate implements RequisitionApproval_Srv {

    private String v_typeShortName = new ProjectVoucherCategory().PHARMACY_REQUISITION;

    @Override
    public DmResultPageing<List<GrdRequisitionApproval_Mdl>> getFillRecords(RequisitionApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdRequisitionApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdRequisitionApproval_Mdl> lstTemp = new ArrayList<>();
        GrdRequisitionApproval_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join, rqdCondition, itemCondtion;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";
            join = " ";
            rqdCondition = " ";
            itemCondtion = "";
            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  mr.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  mr.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getIndenterCode()).equalsIgnoreCase("")) {
                condition += " AND mr.requestedByCode IN(" + DmServer.getParamList(":requestedByCode", param.getIndenterCode()) + " )";
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {

                condition += " AND mr.departmentCode IN(" + DmServer.getParamList(":Department_Code", param.getDepartmentCode()) + " )";
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                condition += " AND mrd.itemCode IN(" + DmServer.getParamList(":medicineCode", param.getItemCode()) + " )";
            }

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("C")) {
                rqdCondition += "   AND isNull(mrd.isStatus,'')=('1') \n";
                condition += " AND isNull(mrd.isStatus,'')=('1')";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(mr.approvedBy,'') = '' AND isNull(mrd.isStatus,'') = '0'";
                rqdCondition += "    AND isNull(mrd.isStatus,'') = '0' \n";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = mr.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(mr.approvedBy,'') <> '' AND isNull(mrd.isStatus,'') = '0'";
                rqdCondition += "   AND isNull(mrd.isStatus,'') = '0' \n";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("Com")) {
                condition += "  AND   isNull(mrd.isStatus,'') = '2'";
                rqdCondition += "       AND isNull(mrd.isStatus,'') = '2' \n";
            }

            if (!DmShared.xNull(param.getRequisitionDocIds()).equalsIgnoreCase("")) {

                condition += " AND mr.docId IN(" + DmServer.getParamList(":docId", param.getRequisitionDocIds()) + " )";
            }

            condition += " AND mr.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select mr.departmentCode, sgdm.name AS depName ,mr.requestedByCode, "
                    + " sg.name AS intendeerName, mr.recId, mr.docId,\n"
                    + " mr.v_Date, mr.site_Code, sm.name AS siteName,mr.approvedBy,         \n"
                    + " FORMAT(mr.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,       \n"
                    + " mr.v_Type, vt.category AS IndType, mrd.isStatus,mr.wtaCode         \n"
                    + "	from requisition AS mr               \n"
                    + "	   left join (\n"
                    + "		   select max(isNull(mrd.isStatus,''))isStatus, "
                    + "                 max(mrd.DocId)DocId \n"
                    + "                 from requisitionDetail mrd where 1=1 " + itemCondtion + " \n" + rqdCondition
                    + "                 group by docid              \n"
                    + "		   ) mrd on mrd.docid =mr.docId      \n"
                    + "     LEFT JOIN siteMast AS sm ON sm.code=mr.site_Code \n"
                    + "	    LEFT JOIN subGroup AS sg on sg.subCode=mr.requestedByCode       \n"
                    + "	    LEFT JOIN departmentMast AS dm on dm.subCode=mr.departmentCode        \n"
                    + "     LEFT JOIN subGroup AS sgdm ON sgdm.subCode = dm.subCode  "
                    + "     LEFT JOIN voucher_Type AS vt on vt.v_Type=mr.v_Type " + join
                    + "     WHERE vt.category IN ('" + v_typeShortName + "') " + condition;
            sqlWithParam.setSQL(sqlQuery);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getIndenterCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("requestedByCode", DmShared.xNull(param.getIndenterCode()));
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("medicineCode", DmShared.xNull(param.getItemCode()));
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("Department_Code", DmShared.xNull(param.getDepartmentCode()));            //Department
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.xNull(param.getRequisitionDocIds()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(param.getRequisitionDocIds()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdRequisitionApproval_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                node.setIndDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setIndCatCode(DmShared.xNull(rsTemp.getString("IndType")));
                node.setIndCatName(DmShared.xNull(rsTemp.getString("IndType")));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                node.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));

                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));

                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    node.setIsChecked("1");
                } else {
                    node.setIsChecked("0");
                }
                //                node.setBrandCode(DmShared.xNull(rsTemp.getString("brandCode")));
//                node.setBrandName(DmShared.xNull(rsTemp.getString("brandName")));
//                node.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("medicineGroupCode")));
//                node.setMedicineGroupName(DmShared.xNull(rsTemp.getString("medicineGroup")));
//                node.setMedicineCode(DmShared.xNull(rsTemp.getString("medicineCode")));
//                node.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
//                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
//                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
//                node.setUnitCode(DmShared.xNull(rsTemp.getString("sku")));
//                node.setUnitName(DmShared.xNull(rsTemp.getString("unit")));
//                node.setSkuQty(DmServer.vNull(rsTemp.getString("SKUQty"), ""));
//                node.setConversionRate(DmShared.iNull(rsTemp.getString("conversionRate")).toString());
//
//                ConvertedQuantity convertedQuantity = STServer.getSecondryQty(node.getSkuQty(), node.getConversionRate());
//                node.setSkuDisplayQty(node.getSkuQty());

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND isActive = 1"
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
            sqlQuery = " select max(sg.name) AS Name,max(mid.requestedByCode) AS requestedByCode "
                    + " from requisition mid "
                    + " LEFT JOIN subGroup AS sg on sg.subCode=mid.requestedByCode"
                    + "   LEFT JOIN voucher_Type AS vt on vt.v_Type=mid.v_Type "
                    + "	   where vt.category='" + v_typeShortName + "'"
                    + " GROUP BY mid.requestedByCode";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
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
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition;
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBrand(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlQuery = " select max(mm.itemMakeCode) AS CODE,  max(mmm.companyName) AS NAME "
                    + "  from itemMast AS mm "
                    + "  LEFT JOIN itemMakeMast AS mmm ON mmm.code = mm.itemMakeCode "
                    + "  WHERE mm.filterItemType = :filterItemType"
                    + "  group by mm.itemMakeCode ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSKU(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlQuery = "select Code,unit AS Name from unitMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo, String closedRequisitionRemark) {
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

            if (!DmShared.xNull(closedRequisitionRemark).equalsIgnoreCase("")) {
                sqlQuery = " Update requisitionDetail set "
                        + " statusRemark=:statusRemark "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("statusRemark", DmShared.xNull(closedRequisitionRemark));
                psTemp.executeUpdate();
            }

            if (isOpen) {
                sqlQuery = " Update requisitionDetail set "
                        + " isStatus=:isStatus "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();

                psTemp.close();
            } else {

                sqlQuery = " select Count(isStatus) as cnt from requisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 1";
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

                sqlQuery = " select Count(isStatus) as cnt from requisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
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

                sqlQuery = " UPDATE requisitionDetail SET "
                        + " isStatus = 1 "
                        + " WHERE docId = :docId AND v_SNo =:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            sqlQuery = " Update requisition set "
                    + " u_EntDt_LatestLine=:uEntDt "
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
    public DmResult<RequisitionApproval_Mdl> fillItemGrid(String code) {
        DmResult<RequisitionApproval_Mdl> dmrTemp = new DmResult<>();
        GrdMedicineDetail_Mdl node;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<>();
        RequisitionApproval_Mdl mrTemp = new RequisitionApproval_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  "
                    + "  req.docId,req.recId,req.v_Date, req.approvedBy,req.departmentCode,sgd.name AS depName, "
                    + "  FORMAT(req.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,req.wtaCode, "
                    + "  req.requestedByCode AS reqByCode,sgv.name AS reqByName "
                    + "  FROM requisition AS req  "
                    + "  LEFT JOIN voucher_Type AS vt ON req.v_Type = vt.v_Type   "
                    + "  LEFT JOIN subGroup AS sgv ON req.requestedByCode = sgv.subCode "
                    + "  LEFT JOIN departmentMast AS dm on dm.subCode=req.departmentCode "
                    + "  LEFT JOIN subGroup AS sgd ON dm.subCode= sgd.subCode "
                    + " where req.docId=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(code));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new RequisitionApproval_Mdl();
                mrTemp.setRequitDocId(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setRequitRecId(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setRequitDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                mrTemp.setRequestedByCode(DmShared.xNull(rsTemp.getString("reqByCode")));
                mrTemp.setRequestedByName(DmShared.xNull(rsTemp.getString("reqByName")));
                mrTemp.setApprovedDateTime(DmShared.xNull(rsTemp.getString("approvalDateTime")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select reqd.docId, reqd.itemCode, im.name as medicineName, reqd.reqQty,"
                    + "   reqd.reqDate, reqd.remark,  reqd.v_SNo, reqd.isStatus ,reqd.statusRemark "
                    + "   from requisitionDetail as reqd "
                    + "   left join itemMast as im on reqd.itemCode = im.code  "
                    + "   where reqd.docId = :docId "
                    + "   order by medicineName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(code));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicineName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setReqQty(DmServer.vNull(rsTemp.getString("reqQty"), ""));
                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setIsChecked(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setClosedRequisitionRemark(DmShared.xNull(rsTemp.getString("statusRemark")));
//                node.setConversionRate(DmShared.iNull(rsTemp.getString("conversionRate")).toString());

//                ConvertedQuantity convertedQuantity = STServer.getSecondryQty(node.getReqQty(), node.getConversionRate());
//                node.setReqDisplayQty(convertedQuantity.getDisplayQty());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<>();
        TxtHRequisitionNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  "
                    + " mr.docId, mr.recId as requisitionNo, mr.v_Date as requisitionDate, vt.category "
                    + " FROM requisition AS mr  "
                    + " left join voucher_Type as vt on vt.v_Type = mr.v_Type "
                    + " where mr.site_Code = '" + DmServer.getSsnSiteCode(this) + "' AND vt.category='" + v_typeShortName + "' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHRequisitionNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("requisitionNo")));
                listNode.setReqDate(DmShared.dNull(rsTemp.getDate("requisitionDate")));
                listNode.setReqType(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResult<Boolean> getLinkedRecords(String docId, String vSNo) {
        DmResult<Boolean> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(true);
        boolean existRecord = true;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select count(docid) as recExist \n"
                    + " 	from (\n"
                    + " 		select docId from indentDetail where requisitionDocId=:requisitionDocId and requisitionV_SNo=:requisitionV_SNo\n"
                    + " 		union all\n"
                    + " 		select docId from stockHeadDetail where requisitionDocId=:requisitionDocId and requisitionV_SNo=:requisitionV_SNo\n"
                    + " 		) a";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("requisitionDocId", DmShared.iNull(docId).toString());
            psTemp.setString("requisitionV_SNo", DmShared.iNull(vSNo).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("recExist")).intValue() == 1) {
                    existRecord = false;
                }
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            dmrTemp.setData(existRecord);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
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
