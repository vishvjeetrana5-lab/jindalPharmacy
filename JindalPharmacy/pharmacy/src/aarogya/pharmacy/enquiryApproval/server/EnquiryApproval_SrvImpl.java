package aarogya.pharmacy.enquiryApproval.server;

import aarogya.pharmacy.enquiryApproval.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnquiryApproval_SrvImpl extends DmRSSPrivate implements EnquiryApproval_Srv {

    private String vCat = new ProjectVoucherCategory().PHARMACY_ENQUIRY;

    @Override
    public DmResultPageing<List<GrdEnquiryApproval_Mdl>> getFillRecords(EnquiryApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdEnquiryApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdEnquiryApproval_Mdl> lstTemp = new ArrayList<>();
        GrdEnquiryApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";
            join = " ";

            condition += " AND id.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            condition += " AND vt.category = '" + vCat + "'";

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  id.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  id.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getVendorCode()).equalsIgnoreCase("")) {
                condition += " AND idd.subCode IN(" + DmServer.getParamList(":subCode", param.getVendorCode()) + " )";
            }
            if (!DmShared.xNull(param.getEnquiryDocIds()).equalsIgnoreCase("")) {
                condition += " AND id.docId IN(" + DmServer.getParamList(":docId", param.getEnquiryDocIds()) + " )";
            }

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("C")) {
                condition += " AND isNull(idd.isStatus,'')=('1')";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(id.approvedBy,'') = '' AND isNull(idd.isStatus,'') = '0'";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = id.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(id.approvedBy,'') <> '' AND isNull(idd.isStatus,'') = '0'";

            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("Com")) {
                condition += "  AND   isNull(idd.isStatus,'') = '2'";
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select idd.subCode,sg.name AS VendorName,id.recId,id.docId,id.v_Date,"
                    + " id.remark,id.site_Code,sm.name AS siteName,id.approvedBy,id.referenceNo,"
                    + " FORMAT(id.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime ,id.wtaCode,"
                    + " id.v_Type,vt.short_Name AS EnqType,"
                    + " idd.isStatus "
                    + " From enquiry AS id "
                    + " INNER JOIN enquiryVendorDetail AS idd ON idd.docId=id.docId "
                    + " INNER JOIN siteMast AS sm ON sm.code=id.site_Code "
                    + " INNER JOIN subGroup AS sg on sg.subCode=idd.subCode "
                    + " INNER JOIN voucher_Type AS vt on vt.v_Type=id.v_Type " + join
                    + " where 1=1 " + condition;

            sqlWithParam.setSQL(sqlQuery);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getVendorCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("subCode", DmShared.xNull(param.getVendorCode()));
            }
            if (!DmShared.xNull(param.getEnquiryDocIds()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("docId", DmShared.xNull(param.getEnquiryDocIds()));
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdEnquiryApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setEnquiryDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("referenceNo")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");
                } else {
                    listNode.setIsChecked("0");
                }
                listNode.setEnquiryType(DmShared.xNull(rsTemp.getString("EnqType")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setVendorName(DmShared.xNull(rsTemp.getString("VendorName")));
                listNode.setVendorCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));

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
    public DmResult<EnquiryApproval_Mdl> fillVendorGrid(String code) {
        DmResult<EnquiryApproval_Mdl> dmrTemp = new DmResult<>();
        GrdVendorDetaill_Mdl grdVendorDetail_Mdl;
        List<GrdVendorDetaill_Mdl> listNode = new ArrayList<>();
        EnquiryApproval_Mdl mrTemp = new EnquiryApproval_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp1 = null;
        ResultSet rsTemp1 = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT id.recId,id.docId,id.v_Date, "
                    + "   id.remark,id.site_Code,sm.name AS siteName,id.approvedBy,id.referenceNo, "
                    + "   FORMAT(id.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime , "
                    + "   id.v_Type,vt.description AS IndType,id.wtaCode "
                    + "   FROM enquiry AS id  "
                    + "   INNER JOIN siteMast AS sm ON sm.code=id.site_Code  "
                    + "   INNER JOIN voucher_Type AS vt on vt.v_Type=id.v_Type "
                    + " WHERE id.docId=:docId ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            if (rsTemp1.next()) {
                mrTemp = new EnquiryApproval_Mdl();
                mrTemp.setEnquiryDocIds(DmShared.xNull(rsTemp1.getString("docId")));
                mrTemp.setEnquiryRecIds(DmShared.xNull(rsTemp1.getString("recId")));
                mrTemp.setEnqDate(DmShared.dNull(rsTemp1.getDate("v_Date")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp1.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp1.getString("wtaCode")));
                mrTemp.setApprovedDateTime(DmShared.xNull(rsTemp1.getString("approvalDateTime")));
                mrTemp.setRemark(DmShared.xNull(rsTemp1.getString("remark")));
                mrTemp.setSiteName(DmShared.xNull(rsTemp1.getString("siteName")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp1.getString("referenceNo")));

            }

            sqlQuery = "select idd.subCode,sg.name AS VendorName,id.docId,id.v_Date,id.approvedBy,"
                    + "   FORMAT(id.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime , idd.isStatus  "
                    + "   FROM enquiry AS id  "
                    + "   INNER JOIN enquiryVendorDetail AS idd ON idd.docId=id.docId  "
                    + "   INNER JOIN siteMast AS sm ON sm.code=id.site_Code  "
                    + "   INNER JOIN subGroup AS sg on sg.subCode=idd.subCode  "
                    + " WHERE id.docId=:docId "
                    + " ORDER BY VendorName Asc ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            while (rsTemp1.next()) {
                grdVendorDetail_Mdl = new GrdVendorDetaill_Mdl();
                grdVendorDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp1.getString("subCode")));
                grdVendorDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp1.getString("VendorName")));
                grdVendorDetail_Mdl.setIsStatus(DmShared.xNull(rsTemp1.getString("isStatus")));
                grdVendorDetail_Mdl.setIsChecked(DmShared.xNull(rsTemp1.getString("isStatus")));
                listNode.add(grdVendorDetail_Mdl);
            }
            mrTemp.getGrdVendorDetail_Mdl().addAll(listNode);
            psTemp1.close();
            rsTemp1.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select sg.subCode, sg.name, sg.manualCode, c.cityName"
                    + " from subgroup as sg "
                    + " inner join subgroupNature as sgn on sg.nature = sgn.code "
                    + " inner join subgroupSiteList as ssl on ssl.subCode = sg.subCode "
                    + " left join city as c on sg.cityCode=c.cityCode "
                    + " where sg.isActive = 1 "
                    + " and sgn.manualCode = 'SU' "
                    + " and ssl.site_Code = :site_Code");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String subCode) {
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

            sqlQuery = "select approvedBy from enquiry where docId = :docId";
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

            if (isOpen) {
                sqlQuery = " Update enquiryVendorDetail set "
                        + " isStatus=:isStatus "
                        + " where docId=:docId AND subCode=:subCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("subCode", DmShared.iNull(subCode).toString());
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();

                psTemp.close();

            } else {
                sqlQuery = " select Count(isStatus) as cnt from enquiryVendorDetail where docId = :docId  AND subCode=:subCode AND isStatus = 1";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("subCode", DmShared.iNull(subCode).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is already cancelled,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " UPDATE enquiryVendorDetail SET "
                        + " isStatus = 1 "
                        + " WHERE docId = :docId  AND subCode=:subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("subCode", DmShared.iNull(subCode).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            sqlQuery = " Update enquiry set "
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
    public DmResultPageing<List<TxtHEnquiryNo_Mdl>> getTxtHEnquiryNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHEnquiryNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHEnquiryNo_Mdl> lstTemp = new ArrayList<>();
        TxtHEnquiryNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT  "
                    + " enq.docId, enq.recId as enqRecId, enq.v_Date as enqDate, vt.category "
                    + " FROM enquiry AS enq  "
                    + " LEFT JOIN voucher_Type as vt on vt.v_Type = enq.v_Type "
                    + " WHERE enq.site_Code = '" + DmServer.getSsnSiteCode(this) + "'"
                    + " and enq.v_Prefix = :v_Prefix "
                    + " AND vt.category = '" + vCat + "'";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHEnquiryNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("enqRecId")));
                listNode.setEnquiryType(DmShared.xNull(rsTemp.getString("category")));
                listNode.setEnquiryDate(DmShared.dNull(rsTemp.getDate("enqDate")));
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
