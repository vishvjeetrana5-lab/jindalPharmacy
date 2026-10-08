package aarogya.pharmacy.medicineTransferApproval.server;

import aarogya.pharmacy.medicineTransferApproval.server.*;
import aarogya.pharmacy.medicineTransferApproval.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
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

public class MedicineTransferApproval_SrvImpl extends DmRSSPrivate implements MedicineTransferApproval_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatTRF = "'" + pvc.MEDICINE_TRANSFER + "'";

    @Override
    public DmResultPageing<List<GrdMedicineTransferApproval_Mdl>> getFillRecords(MedicineTransferApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdMedicineTransferApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdMedicineTransferApproval_Mdl> lstTemp = new ArrayList<>();
        GrdMedicineTransferApproval_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join, middCondition, itemCondtion;;
        rstTemp.setData(lstTemp);

        try {
            condition = " ";
            join = " ";
            middCondition = " ";
            itemCondtion = "";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  mid.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  mid.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getTransferCode()).equalsIgnoreCase("")) {
                middCondition += " AND midd.subCode IN(" + DmServer.getParamList(":subCode", param.getTransferCode()) + " )";
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                middCondition += " AND midd.departmentToCode IN(" + DmServer.getParamList(":Department_Code", param.getDepartmentCode()) + " )";
            } else {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    middCondition += " AND midd.departmentToCode IN (" + DmServer.getParamList(":Department_Code", STServer.getSsnDepartmentCodeList(this)) + ")";
                }
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                middCondition += " AND midd.itemCode IN(" + DmServer.getParamList(":itemCode", param.getItemCode()) + " )";
            }

            if (!DmShared.xNull(param.getTransferDocIds()).equalsIgnoreCase("")) {
                condition += " AND mid.docid IN(" + DmServer.getParamList(":transferDocIds", param.getTransferDocIds()) + " )";
            }

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(mid.approvedBy,'') = '' ";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = mid.wtaCode and wtfp.isStatus = 0 \n";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(mid.approvedBy,'') <> '' ";
            }

            condition += " AND mid.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";

            sqlQuery = "  select midd.departmentCode, midd.departmentToCode, sgdm.name AS depName, sgdtm.name AS depToName, \n"
                    + "     sg.name AS transferToName,mid.recId,mid.docId,mid.v_Date,                   \n"
                    + "     mid.remark,mid.site_Code,sm.name AS siteName,mid.approvedBy,            \n"
                    + "     FORMAT(mid.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime, mid.wtaCode,              \n"
                    + "     mid.v_Type,vt.short_Name AS trfType, mid.u_EntDt_LatestLine     \n"
                    + "     from stockHead AS mid           \n"
                    + "     LEFT JOIN(          \n"
                    + "   select midd.departmentCode, midd.departmentToCode, midd.docid, midd.subCode           \n"
                    + "             from stockHeadDetail AS midd  where 1=1  \n" + middCondition
                    + "             group by   midd.departmentCode, midd.departmentToCode, midd.docid, midd.subCode                      \n"
                    + "         ) midd ON midd.docId = mid.docId             \n"
                    + "     LEFT JOIN subGroup AS sg on sg.subCode=midd.subCode             \n"
                    + "     LEFT JOIN siteMast AS sm ON sm.code=mid.site_Code           \n"
                    + "     LEFT JOIN departmentMast AS dm on dm.subCode=midd.departmentCode            \n"
                    + "     LEFT JOIN departmentMast AS dtm on dtm.subCode=midd.departmentToCode            \n"
                    + "     LEFT JOIN subGroup AS sgdm ON sgdm.subCode = dm.subCode                 \n"
                    + "     LEFT JOIN subGroup AS sgdtm ON sgdtm.subCode = dtm.subCode                  "
                    + "     LEFT JOIN voucher_Type AS vt on vt.v_Type=mid.v_Type                    \n" + join
                    + "  where vt.category in (" + vCatTRF + ")  and isnull(midd.docId,0)<>0 \n" + condition;
            sqlWithParam.setSQL(sqlQuery);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getTransferCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("subCode", DmShared.xNull(param.getTransferCode()));
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemCode", DmShared.xNull(param.getItemCode()));
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("Department_Code", DmShared.xNull(param.getDepartmentCode()));            //Department
            } else {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    sqlWithParam.setStringList("Department_Code", DmShared.xNull(STServer.getSsnDepartmentCodeList(this)));            //Department
                }
            }
            if (!DmShared.xNull(param.getTransferDocIds()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("transferDocIds", DmShared.xNull(param.getTransferDocIds()));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineTransferApproval_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                node.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                node.setDepartmentToName(DmShared.xNull(rsTemp.getString("depToName")));
                node.setTrfDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));

                node.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    node.setIsChecked("1");
                    node.setIsStatus("0");
                } else {
                    node.setIsChecked("0");
                    node.setIsStatus("0");
                }
                lstTemp.add(node);
//                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
//                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
//                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
//                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
//                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
//                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
//
//                node.setDisplayProperties(ipm.getPropertiesCsv());

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
    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicineName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicineName_Mdl> lstTemp = new ArrayList<TxtHMedicineName_Mdl>();
        TxtHMedicineName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT im.code, im.name, im.manualCode, um.unit AS skuName"
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND im.isActive = 1"
                    + " AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicineName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
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
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransferTo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        String v_Type = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            v_Type = DmShared.xNull(DmServer.getCodeViaManualCode(pvc.MEDICINE_TRANSFER, "short_Name", "voucher_Type", "v_Type", cnTemp));
            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , c.cityName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo) {
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

            sqlQuery = "select approvedBy from stockHead where docId = :docId";
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

//            sqlQuery = " select Count(isStatus) as cnt from indentDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(docId).toString());
//            psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
//                    throw new DmException(0, "Record is completed,Please refresh record.");
//                }
//            }
//            rsTemp.close();
//            psTemp.close();
//            if (isOpen) {
//                sqlQuery = " Update indentDetail set "
//                        + " isStatus=:isStatus "
//                        + " where docId=:docId AND v_SNo=:vSNo";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(docId).toString());
//                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
//                psTemp.setString("isStatus", "0");
//                psTemp.executeUpdate();
//
//                psTemp.close();
//
//            } else {
//                sqlQuery = " select Count(isStatus) as cnt from indentDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 1";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(docId).toString());
//                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
//                rsTemp = psTemp.executeQuery();
//                if (rsTemp.next()) {
//                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
//                        throw new DmException(0, "Record is already cancelled,Please refresh record.");
//                    }
//                }
//                rsTemp.close();
//                psTemp.close();
//
//                sqlQuery = " select Count(isStatus) as cnt from indentDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(docId).toString());
//                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
//                rsTemp = psTemp.executeQuery();
//                if (rsTemp.next()) {
//                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
//                        throw new DmException(0, "Record is completed,Please refresh record.");
//                    }
//                }
//                rsTemp.close();
//                psTemp.close();
//
//                sqlQuery = " UPDATE indentDetail SET "
//                        + " isStatus = 1 "
//                        + " WHERE docId = :docId AND v_SNo =:vSNo";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(docId).toString());
//                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
//                psTemp.executeUpdate();
//                psTemp.close();
//            }
            sqlQuery = " Update indent set "
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

//                sqlQuery = "select isStatus from indentDetail where docId = :docId AND v_SNo=:vSNo";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
//                psTemp.setString("vSNo", DmShared.iNull(mdl.getvSNo()).toString());
//                rsTemp = psTemp.executeQuery();
//                if (rsTemp.next()) {
//                    if (DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("1")) {
//                        throw new DmException(0, "Record is cancelled.So can't update data.");
//                    } else if (DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("2")) {
//                        throw new DmException(0, "Record is completed.So can't update data.");
//                    }
//                }
//                rsTemp.close();
//                psTemp.close();
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

//                sqlQuery = "select orderedQty FROM indentdetail WHERE  docId = :docId AND v_SNo=:vSNo ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
//                psTemp.setString("vSNo", DmShared.iNull(mdl.getvSNo()).toString());
//                rsTemp = psTemp.executeQuery();
//                while (rsTemp.next()) {
//                    if (DmShared.vNull(rsTemp.getString("orderedQty")).doubleValue() > DmShared.vNull(mdl.getApprovedQty()).doubleValue()) {
//                        throw new DmException(0, "Approved Qty should be greater than or equal to Order Qty.");
//                    }
//                }
//                rsTemp.close();
//                psTemp.close();
//                sqlQuery = " Update indentDetail set "
//                        + " aprQty=:aprQty "
//                        + " where docId=:docId AND v_SNo=:v_SNo ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docId", DmShared.iNull(mdl.getIndDocId()).toString());
//                psTemp.setString("aprQty", DmShared.xNull(mdl.getApprovedQty()));
//                psTemp.setString("v_SNo", DmShared.iNull(mdl.getvSNo()).toString());
//                psTemp.executeUpdate();
//                psTemp.close();
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
    public DmResult<MedicineTransferApproval_Mdl> fillItemGrid(String code) {
        DmResult<MedicineTransferApproval_Mdl> dmrTemp = new DmResult<>();
        GrdMedicineDetail_Mdl node;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<GrdMedicineDetail_Mdl>();
        MedicineTransferApproval_Mdl mrTemp = new MedicineTransferApproval_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  midd.departmentCode,sdm.name AS depName,sdtm.name AS depToName,"
                    + "         mid.recId,mid.docId,mid.v_Date,"
                    + "         sm.name AS siteName,mid.approvedBy,FORMAT(mid.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,"
                    + "         mid.site_Code,mid.v_Type,vt.description AS trfType,mid.wtaCode,departmentToCode "
                    + "         FROM stockHead AS mid "
                    + "         left join (select top 1 departmentCode,departmentToCode,docid from stockHeadDetail ) midd on mid.docid=midd.docId "
                    + "         LEFT JOIN siteMast AS sm ON sm.code = mid.site_Code "
                    + "         LEFT JOIN subgroup AS sdm ON sdm.subcode = midd.departmentCode "
                    + "         LEFT JOIN subgroup AS sdtm ON sdtm.subcode = midd.departmentToCode "
                    + "         LEFT JOIN voucher_Type AS vt ON vt.v_Type = mid.v_Type "
                    + "         where mid.docId=:docId  And vt.category in (" + vCatTRF + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(code));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new MedicineTransferApproval_Mdl();
                mrTemp.setTrfDocId(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setTrfReccId(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                mrTemp.setToDepartmentCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                mrTemp.setToDepartmentName(DmShared.xNull(rsTemp.getString("depToName")));
                mrTemp.setTrfDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setTrfType(DmShared.xNull(rsTemp.getString("trfType")));
                mrTemp.setApprovedDateTime(DmShared.xNull(rsTemp.getString("approvalDateTime")));
                mrTemp.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT  mid.docId, mid.itemCode, mid.isStatus, mid.reqDate, mid.reqQty, ump.unit as skuName, mi.approvedBy, "
                    + "         mid.isStatus, mid.v_SNo, im.name AS itemName, ig.name AS imGroup, imm.companyName AS brandName,IsNull(ump.decimalPlaces,0) as decimalPlaces ,"
                    + "         mid.colourCode,icm.name as colourName,"
                    + "         mid.sizeCode as sizeCode,ism.name sizeName,mid.colourCode,icm.name as colourName,"
                    + "         mid.sizeCode as sizeCode,ism.name sizeName,im.propertyList "
                    + "         FROM stockHeadDetail AS mid "
                    + "         LEFT JOIN stockHead AS mi ON mid.docId = mi.docId "
                    + "         LEFT JOIN itemMast AS im ON mid.itemCode = im.code "
                    + "         LEFT JOIN itemGroup AS ig ON ig.Code = im.itemGroup "
                    + "         LEFT JOIN unitMast AS ump ON im.sku = ump.Code "
                    + "         LEFT JOIN itemMakeMast AS imm ON imm.code = im.itemMakeCode "
                    + "         LEFT JOIN voucher_Type AS vt ON vt.v_Type = mi.v_Type "
                    + "         LEFT JOIN itemColourMast icm ON icm.code = mid.colourCode "
                    + "         LEFT JOIN itemSizeMast ism ON ism.code = mid.sizeCode "
                    + "         where mid.docId=:docId  And vt.category in (" + vCatTRF + ") "
                    + "         order by itemName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(code));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIndQty(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setApprovedQty(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setIsChecked(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setIndDocId(DmShared.xNull(rsTemp.getString("docId")));
                node.setDecimalPlace(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));

                node.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColor(DmShared.xNull(rsTemp.getString("colourName")));

                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSize(DmShared.xNull(rsTemp.getString("sizeName")));

                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));

                node.setDisplayProperties(ipm.getPropertiesCsv());

                listNode.add(node);
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getGrdMedicineDetail_Mdl().addAll(listNode);

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
    public DmResultPageing<List<TxtHTransferNo_Mdl>> getTxtHTransferNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTransferNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTransferNo_Mdl> lstTemp = new ArrayList<>();
        TxtHTransferNo_Mdl listNode;
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
                    + " FROM stockHead AS ind  "
                    + " left join voucher_Type as vt on vt.v_Type = ind.v_Type"
                    + " where ind.site_Code = '" + DmServer.getSsnSiteCode(this) + "' AND vt.category in (" + vCatTRF + ") ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHTransferNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indRecId")));
                listNode.setTransferType(DmShared.xNull(rsTemp.getString("category")));
                listNode.setTransferDate(DmShared.dNull(rsTemp.getDate("indDate")));
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
