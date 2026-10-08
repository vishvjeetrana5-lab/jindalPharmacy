package aarogya.dashBoard.requisitionPharmacy.server;

import aarogya.dashBoard.requisitionPharmacy.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RequisitionPharmacy_SrvImpl extends DmRSSPrivate implements RequisitionPharmacy_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<GrdRequisitionPharmacy_Mdl>> getFillRecords(String departmentCode, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdRequisitionPharmacy_Mdl>> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<GrdRequisitionPharmacy_Mdl> listNode = new ArrayList<GrdRequisitionPharmacy_Mdl>();
        GrdRequisitionPharmacy_Mdl requisition;
        dmrTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition = " and rs.departmentToCode = :departmentCode ";
            } else if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).trim().equalsIgnoreCase("")) {
                condition += "and rs.departmentToCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
            }
            sqlQuery = "select  rs.docId, rs.recId, rs.requestedByCode, req.name requestedBy, rs.departmentCode, depby.name departmentBy, "
                    + "         rs.departmentToCode, depTo.name departmentToName, rs.v_Date, reqDateTime, vt.short_Name, vt.category, "
                    + "		rs.v_No, hl7.colorName, hl7.priorityIndex, format(reqd.reqDateTime,'dd-MMM-yyyy HH.mm') reqDT, rs.v_Type,pr.recid as regNo,pu.patientName,pu.recid as uhid "
                    + "         from requisition rs "
                    + "         inner join ( "
                    + "                 select  docId, MIN(reqDateTIme) AS reqDateTIme "
                    + "                         from ( "
                    + "                             select  rd.Docid, CONVERT(DATETIME, (CONVERT(VARCHAR (100), reqDate) + ' ' +  "
                    + "                                     REPLACE((CONVERT(VARCHAR (100), CAST(reqTime AS DECIMAL (38, 2)))),'.',':') + ':00'),120) AS reqDateTIme  "
                    + "                                     from requisitionDetail rd  "
                    + "                                     where rd.isStatus = 0 "
                    + "                         ) AS maintab  "
                    + "				GROUP BY docId "
                    + "         ) AS reqd ON rs.docId = reqd.docid "
                    + "	        left join patientRegistration pr on pr.regdocid=rs.regDocid "
                    + "		left join patientUhid pu on pu.docid = pr.uhid"
                    + "         left join subGroup req on req.subCode = rs.requestedByCode "
                    + "         left join subGroup depby on depby.subCode = rs.departmentCode "
                    + "         left join subGroup depTo on depTo.subCode = rs.departmentToCode "
                    + "         LEFT JOIN voucher_type AS vt ON rs.v_Type = vt.v_Type "
                    + "         left join hl7Mast  as hl7 on hl7.code  = rs.priorityTypeCode "
                    + "         where rs.site_Code_To = :site_Code and isnull(rs.approvedBy,'') <> '' "
                    + "             and vt.category in ('" + pvc.PHARMACY_REQUISITION + "','" + pvc.NURSING_MEDICINE_REQUISTION + "') " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                requisition = new GrdRequisitionPharmacy_Mdl();
                requisition.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                requisition.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                requisition.setV_No(DmShared.xNull(rsTemp.getString("v_No")));
                requisition.setV_Type(DmShared.xNull(rsTemp.getString("v_Type")));
                requisition.setDate(DmShared.dNull(rsTemp.getDate("v_date")));
                requisition.setReqDateTime(DmShared.xNull(rsTemp.getString("reqDT")));
                requisition.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                requisition.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentBy")));
                requisition.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                requisition.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentToName")));
                requisition.setRequisitionByCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
                requisition.setRequisitionByName(DmShared.xNull(rsTemp.getString("requestedBy")));
                requisition.setPriorityColor(DmShared.xNull(rsTemp.getString("colorName")));
                requisition.setV_TypeCat(DmShared.xNull(rsTemp.getString("category")));
                requisition.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                requisition.setUhid(DmShared.xNull(rsTemp.getString("uhid")));
                requisition.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.add(requisition);
            }
            psTemp.close();
            rsTemp.close();
            helper.positioning(listNode.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());

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
    public DmResult<RequisitionPharmacy_Mdl> getRequisitionDetail(String docId) {
        DmResult<RequisitionPharmacy_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, code = "", name = "";
        RequisitionPharmacy_Mdl mrTemp = new RequisitionPharmacy_Mdl();
        List<GrdRequisitionDetailPharmacy_Mdl> lstTemp = new ArrayList<>();
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT rq.docId, rq.recId, rq.v_Date,sg.subCode,sg.name AS subgroup, "
                    + " sdp.subcode AS departmentCode,sdp.name departmentName,rq.preparedBy,"
                    + " rq.u_Name,rq.u_EntDt_LatestLine,rq.departmentToCode,sdt.name departmentToName,vt.category "
                    + " from requisition rq "
                    + " LEFT JOIN subGroup sg ON sg.subCode = rq.requestedByCode "
                    + " LEFT JOIN subgroup sdp ON sdp.subcode = rq.departmentCode "
                    + " LEFT JOIN subgroup sdt ON sdt.subcode = rq.departmentToCode "
                    + " LEFT JOIN voucher_type AS VT ON rq.v_Type = VT.v_Type "
                    + " where rq.docid=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", docId);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                mrTemp.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentToName")));
                mrTemp.setRequisitionByName(DmShared.xNull(rsTemp.getString("subgroup")));
                mrTemp.setRequisitionByCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setvCat(DmShared.xNull(rsTemp.getString("category")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT rqd.docId, rqd.v_SNo,rqd.reqQty,rqd.reqDate,rqd.remark,rqd.isStatus,"
                    + " rqd.itemcode as code,im.name,um.unit AS sku,um.code skuCode,rqd.reqTime "
                    + " from requisitiondetail rqd  "
                    + " LEFT JOIN itemMast im ON im.code = rqd.itemCode "
                    + " LEFT JOIN unitMast um ON um.code = im.sku "
                    + " where docid=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", docId);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdRequisitionDetailPharmacy_Mdl node = new GrdRequisitionDetailPharmacy_Mdl();

                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));

                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setReqTime(DmServer.vNull(rsTemp.getString("reqTime"), "00.00"));
                node.setItemQty(DmServer.vNull(rsTemp.getString("reqQty"), "0.00"));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setUnitCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setUnitName(DmShared.xNull(rsTemp.getString("sku")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                if (!DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("0")) {
                    node.setCheck("0");
                } else {
                    node.setCheck("1");
                }
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                node.setAction("0");
                lstTemp.add(node);
            }

            mrTemp.getObjRequistionDetail().addAll(lstTemp);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition += " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlWithParam.setSQL("select	dm.subCode, sg.name"
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "		where sg.isActive = 1 and sgn.manualCode in ('CM','PH')" + condition);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
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
    public DmResult<CardDetail_Mdl> getCardDetail() {
        DmResult<CardDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	count(req.docId) as cnt "
                    + "		from requisition as req "
                    + "		left join voucher_type as vt on req.v_Type=vt.v_Type "
                    + "		left join ( "
                    + "                 select	reqd.docId "
                    + "                         from requisitionDetail reqd "
                    + "				where reqd.isStatus = 0 "
                    + "				group by reqd.docId "
                    + "		) as reqd on  reqd.docId = req.docId "
                    + "		where reqd.docId is not null and isNull(req.approvedBy,'') <> '' and vt.category = ('" + pvc.PHARMACY_REQUISITION + "') "
                    + "         and req.site_Code = :site_Code and req.v_date= :v_date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("v_date", DmServer.convertDate(DmServer.getServerDateTime()[0]));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCount(DmShared.iNull(rsTemp.getString("cnt")).toString());
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Fill Chart Detail >====================================*/
            node = new DmChart_Mdl();
            node.setLabelName("00:30");
            node.setLabelCode("00:30");
            node.setDataValue(0);
            listNode.add(node);

            node = new DmChart_Mdl();
            node.setLabelName("01:00");
            node.setLabelCode("01:00");
            node.setDataValue(0);
            listNode.add(node);

            node = new DmChart_Mdl();
            node.setLabelName("02:00");
            node.setLabelCode("02:00");
            node.setDataValue(0);
            listNode.add(node);

            node = new DmChart_Mdl();
            node.setLabelName("04:00");
            node.setLabelCode("04:00");
            node.setDataValue(0);
            listNode.add(node);

            node = new DmChart_Mdl();
            node.setLabelName("08:00");
            node.setLabelCode("08:00");
            node.setDataValue(0);
            listNode.add(node);

            node = new DmChart_Mdl();
            node.setLabelName("Above");
            node.setLabelCode("Above");
            node.setDataValue(0);
            listNode.add(node);

            /*====================================< Get Chart Detail >====================================*/
            sqlQuery = "Select	sum(case when temp.reqInMin <= 30 then 1 else 0 end) as '30m', "
                    + "		sum(case when (temp.reqInMin > 30 and temp.reqInMin <= 60) then 1 else 0 end) as '60m', "
                    + "		sum(case when (temp.reqInMin > 60 and temp.reqInMin <= 120) then 1 else 0 end) as '120m', "
                    + "		sum(case when (temp.reqInMin > 120 and temp.reqInMin <= 240) then 1 else 0 end) as '240m', "
                    + "		sum(case when (temp.reqInMin > 240 and temp.reqInMin <= 480) then 1 else 0 end) as '480m', "
                    + "		sum(case when temp.reqInMin > 480 then 1 else 0 end) as 'Above480m' "
                    + "		from ( "
                    + "                 Select	req.v_date, datediff(minute, format(req.v_date, 'dd-MMM-yyyy') + ' ' + replace(format(req.v_time,'00.00'),'.',':'), :uEntDt) as reqInMin "
                    + "                         from requisition req "
                    + "				left join voucher_type as vt on req.v_Type = vt.v_Type "
                    + "				left join ( "
                    + "                                 select	reqd.docId "
                    + "                                         from requisitionDetail as reqd "
                    + "						where reqd.isStatus = 0 "
                    + "						group by reqd.docId "
                    + "				) as reqd on reqd.docId = req.docId "
                    + "				where reqd.docId is not null and isNull(req.approvedBy,'') <> '' and vt.category = ('" + pvc.PHARMACY_REQUISITION + "') "
                    + "				and req.site_Code = :site_Code and req.v_date = :v_date "
                    + "		) as temp "
                    + "		group by temp.v_date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("v_date", DmServer.convertDate(DmServer.getServerDateTime()[0]));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.get(0).setDataValue(DmShared.iNull(rsTemp.getString("30m")).intValue());
                listNode.get(1).setDataValue(DmShared.iNull(rsTemp.getString("60m")).intValue());
                listNode.get(2).setDataValue(DmShared.iNull(rsTemp.getString("120m")).intValue());
                listNode.get(3).setDataValue(DmShared.iNull(rsTemp.getString("240m")).intValue());
                listNode.get(4).setDataValue(DmShared.iNull(rsTemp.getString("480m")).intValue());
                listNode.get(5).setDataValue(DmShared.iNull(rsTemp.getString("Above480m")).intValue());
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListChardData().addAll(listNode);

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

}
