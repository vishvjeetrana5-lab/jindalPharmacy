package aarogya.pharmacy.medicineRequisitionDisplay.server;

import aarogya.pharmacy.medicineRequisitionDisplay.shared.MedicineRequisitionDisplay_Mdl;
import aarogya.pharmacy.medicineRequisitionDisplay.shared.MedicineRequisitionDisplay_Srv;
import aarogya.pharmacy.medicineRequisitionDisplay.shared.SearchValue_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineRequisitionDisplay_SrvImpl extends DmRSSPrivate implements MedicineRequisitionDisplay_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<List<MedicineRequisitionDisplay_Mdl>> getFillRecords(String departmentCode, SearchValue_Mdl searchValueMdl) {
        DmResultPageing<List<MedicineRequisitionDisplay_Mdl>> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", conditionItmStk = "";
        MedicineRequisitionDisplay_Mdl node;
        List<MedicineRequisitionDisplay_Mdl> lstTemp = new ArrayList<>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(departmentCode).longValue() == 0) {
                departmentCode = DmShared.xNull(STServer.getSsnDepartmentCode(this));
            }

            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition = " and rs.departmentToCode = :departmentCode ";
                conditionItmStk = " and departmentCode = :departmentCode ";
            } else if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).trim().equalsIgnoreCase("")) {
                condition += "and rs.departmentToCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
                conditionItmStk = "and departmentCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
            }
            sqlQuery = "select  rs.docId, rs.recId, rs.requestedByCode, req.name requestedBy, rs.departmentCode, depby.name departmentBy, "
                    + "         rs.departmentToCode, depTo.name departmentToName, rs.v_Date, reqDateTime, vt.short_Name, vt.category, "
                    + "		rs.v_No, hl7.colorName, hl7.priorityIndex, format(reqd.reqDateTime,'dd-MMM-yyyy HH.mm') reqDT, rs.v_Type,pr.recid as regNo "
                    + "		,pu.patientName,pu.recid as uhid,im.Name as itemName,isnull(rqd.reqQty,0)-isnull(rqd.issueQty,0)-isnull(rqd.indentQty,0) as reqQty ,stk.totReqQty "
                    + "		,stk.currStk,rqd.itemCode,rqd.v_SNo,im.sku as skuCode ,um.unit as skuName,um.decimalPlaces "
                    + "         from requisition rs "
                    + "         left join requisitionDetail rqd on rs.docid=rqd.docid "
                    + "         left join itemmast im on rqd.itemCode=im.code "
                    + "         inner join ( "
                    + "                 select  docId, MIN(reqDateTIme) AS reqDateTIme "
                    + "                         from ( "
                    + "                             select  rd.Docid, CONVERT(DATETIME, (CONVERT(VARCHAR (100), reqDate) + ' ' +  "
                    + "                                     REPLACE((CONVERT(VARCHAR (100), CAST(case when not reqTime is null then reqTime else '0.0' end AS DECIMAL (38, 2)))),'.',':') + ':00'),120) AS reqDateTIme  "
                    + "                                     from requisitionDetail rd  "
                    + "                                     where rd.isStatus = 0 "
                    + "                         ) AS maintab  "
                    + "				GROUP BY docId "
                    + "         ) AS reqd ON rs.docId = reqd.docid "
                    + "	        left join ( "
                    + "                   select rqd.itemCode,sum(isnull(rqd.reqQty,0)-isnull(rqd.issueQty,0)-isnull(rqd.indentQty,0)) as totReqQty,max(curr.currStk) as currStk "
                    + "                   from requisition rs "
                    + "                   left join requisitionDetail rqd on rs.docid=rqd.docid "
                    + "                   left join ( "
                    + "                              select itemcode,sum(balanceQty) as currStk "
                    + "                              from itemStock   "
                    + "                              where site_Code = :site_Code " + conditionItmStk
                    + "                              group by itemcode "
                    + "                             ) curr on curr.itemcode=rqd.itemCode "
                    + "                   where rs.site_Code_To = :site_Code and isnull(rs.approvedBy,'') <> '' " + condition
                    + "                   group by rqd.itemcode "
                    + "         ) As stk on rqd.ItemCode=stk.itemCode "
                    + "	        left join patientRegistration pr on pr.regdocid=rs.regDocid "
                    + "		left join patientUhid pu on pu.docid = pr.uhid"
                    + "         left join subGroup req on req.subCode = rs.requestedByCode "
                    + "         left join subGroup depby on depby.subCode = rs.departmentCode "
                    + "         left join subGroup depTo on depTo.subCode = rs.departmentToCode "
                    + "         LEFT JOIN voucher_type AS vt ON rs.v_Type = vt.v_Type "
                    + "         left join hl7Mast  as hl7 on hl7.code  = rs.priorityTypeCode "
                    + "         left join unitMast um on im.sku = um.code "
                    + "         where rs.site_Code_To = :site_Code and isnull(rs.approvedBy,'') <> '' "
                    + "         and vt.category in ('" + pvc.PHARMACY_REQUISITION + "','" + pvc.NURSING_MEDICINE_REQUISTION + "') " + condition
                    + "         And isnull(rqd.reqQty,0)-isnull(rqd.issueQty,0)-isnull(rqd.indentQty,0)>0 "
                    + "         Order By rs.v_Date,rs.docID,im.name ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new MedicineRequisitionDisplay_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                node.setV_No(DmShared.xNull(rsTemp.getString("v_No")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setV_Type(DmShared.xNull(rsTemp.getString("v_Type")));
                node.setDate(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setReqDateTime(DmShared.xNull(rsTemp.getString("reqDT")));
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentBy")));
                node.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                node.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentToName")));
                node.setRequisitionByCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
                node.setRequisitionByName(DmShared.xNull(rsTemp.getString("requestedBy")));
                node.setPriorityColor(DmShared.xNull(rsTemp.getString("colorName")));
                node.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                node.setUhid(DmShared.xNull(rsTemp.getString("uhid")));
                node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setItemQty(DmServer.vNull(rsTemp.getString("reqQty"), "0.00"));
                node.setItemDocumentQty(DmServer.vNull(rsTemp.getString("reqQty"), "0.00"));
                node.setTotalReqQty(DmServer.vNull(rsTemp.getString("totReqQty"), "0.00"));
                node.setCurrentStk(DmServer.vNull(rsTemp.getString("currStk"), "0.00"));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                lstTemp.add(node);
            }
            psTemp.close();
            rsTemp.close();
            dmrTemp.setData(lstTemp);

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
    public DmResult<MedicineRequisitionDisplay_Mdl> getRequisitionDetail(String department) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public DmResult<String> saveRecord(List<MedicineRequisitionDisplay_Mdl> lstGrid) {
        DmResult<String> dmr = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = " Delete From tempRequisitionIndentDetail ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            for (MedicineRequisitionDisplay_Mdl detail_Mdl : lstGrid) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    sqlQuery = " insert into tempRequisitionIndentDetail (docId, v_SNo, itemCode, quantity) "
                            + " values (:docId, :vSno, :itemCode, :quantity)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).toString());
                    psTemp.setString("vSno", detail_Mdl.getV_SNo());
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getItemCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getItemQty()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            spBegin.commit();
            dmr.setData("");
            dmr.setStatus(true);

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmr.setErrorMessage(e.getMessage());
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

        return dmr;
    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValues() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended lstTemp = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

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
}
