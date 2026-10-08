package aarogya.selfToolkit.labApproval.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.labApproval.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.labApproval.shared.GrdLabApproval_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHSampleNo_Mdl;
import aarogya.selfToolkit.stStatic.server.STApprovals;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.labApproval.shared.LabApproval_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHLabNo_Mdl;
import aarogya.selfToolkit.labApproval.shared.LabApproval_Srv;
import aarogya.selfToolkit.labApproval.shared.TxtHItem_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHTechnician_Mdl;

public class LabApproval_SrvImpl extends DmRSSPrivate implements LabApproval_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.LAB + "'";

    @Override
    public DmResultPageing<List<GrdLabApproval_Mdl>> getFillRecords(LabApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdLabApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdLabApproval_Mdl> lstTemp = new ArrayList<>();
        GrdLabApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "", join = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  lab.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  lab.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getLabCode()).equalsIgnoreCase("")) {
                condition += " AND lab.docId IN(" + DmServer.getParamList(":labDocId", param.getLabCode()) + " )";
            }
            if (!DmShared.xNull(param.getTechnicianCode()).equalsIgnoreCase("")) {
                condition += " AND lab.technician IN(" + DmServer.getParamList(":technicianCode", param.getTechnicianCode()) + " )";
            }
            if (!DmShared.xNull(param.getTestTemplateCode()).equalsIgnoreCase("")) {
                condition += " AND tmm.code IN(" + DmServer.getParamList(":testTemplateCode", param.getTestTemplateCode()) + " )";
            }
            if (!DmShared.xNull(param.getSampleCode()).equalsIgnoreCase("")) {
                condition += " AND smp.docId IN(" + DmServer.getParamList(":sampleDocId", param.getSampleCode()) + " )";
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                condition += " AND im.code IN(" + DmServer.getParamList(":itemCode", param.getSampleCode()) + " )";
            }

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(lab.approvedBy,'') = '' ";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = lab.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(lab.approvedBy,'') <> '' ";
            }

            condition += " AND lab.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";

            sqlQuery = "  select lab.docId, lab.v_No, lab.v_Type, vt.short_Name, lab.v_Prefix, "
                    + "                      lab.v_Date,lab.v_Time, lab.recId, lab.recIdPrefix, lab.technician as technicianCode, "
                    + "                      sg.name as technicianName,lab.labSampleDocId,smp.recId as sampleRecId, "
                    + "                      lab.sampleConsumedQty, lab.preparedBy, lab.preparedDate, "
                    + "                      lab.u_Name, lab.u_EntDt, lab.approvedBy, lab.approvalDateTime, "
                    + "                      lab.u_EntDt_LatestLine, lab.wtaCode, lab.site_Code , "
                    + "                      tmm.description as testTemplateName , im.name as itemName, um.unit "
                    + "                      from lab lab "
                    + "                      LEFT JOIN voucher_Type vt ON vt.v_Type = lab.v_Type "
                    + "                      LEFT JOIN subGroup sg ON sg.subCode = lab.technician "
                    + "                      LEFT JOIN labsample smp ON smp.docId = lab.labSampleDocId   "
                    + "                      LEFT JOIN testTemplateMast tmm ON tmm.code = smp.testTemplateCode   "
                    + "                      LEFT JOIN itemMast im ON im.code = smp.itemCode   "
                    + "                      LEFT JOIN unitMast um ON um.code = smp.sampleUnit   "
                    + join
                    + " where vt.category = " + vCat + " " + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getLabCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("labDocId", DmShared.xNull(param.getLabCode()));
            }

            if (!DmShared.xNull(param.getTechnicianCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("technicianCode", DmShared.xNull(param.getTechnicianCode()));
            }
            if (!DmShared.xNull(param.getTestTemplateCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("testTemplateCode", DmShared.xNull(param.getTestTemplateCode()));
            }

            if (!DmShared.xNull(param.getSampleCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("sampleDocId", DmShared.xNull(param.getSampleCode()));
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(param.getItemCode()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdLabApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setLabType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setLabDateTime(DmShared.dNull(rsTemp.getDate("v_Date")) + "  " + DmServer.vNull(rsTemp.getString("v_Time"), "00.00"));
                listNode.setTechnician(DmShared.xNull(rsTemp.getString("technicianName")));
                listNode.setTestTemplate(DmShared.xNull(rsTemp.getString("testTemplateName")));
                listNode.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));

                listNode.setSampleNo(DmShared.xNull(rsTemp.getString("sampleRecId")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setSampleConsumeQty(DmServer.vNull(rsTemp.getString("sampleConsumedQty")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");
                } else {
                    listNode.setIsChecked("0");
                }
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo) {
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
            sqlQuery = "select approvedBy from lab where docId = :docId";
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

            sqlQuery = " Update lab set "
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
    public DmResultPageing<List<TxtHLabNo_Mdl>> getTxtHLabNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLabNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLabNo_Mdl> lstTemp = new ArrayList<>();
        TxtHLabNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  "
                    + " lab.docId, lab.recId as labRecId, lab.v_Date as labDate, lab.v_Time as labTime, vt.category "
                    + " , tmm.description as testTemplateName , im.name as itemName "
                    + " FROM lab AS lab"
                    + " left join voucher_Type as vt on vt.v_Type = lab.v_Type "
                    + " LEFT JOIN labsample smp ON smp.docId = lab.labSampleDocId "
                    + "	LEFT JOIN testTemplateMast tmm ON tmm.code = smp.testTemplateCode  "
                    + " LEFT JOIN itemMast im ON im.code = smp.itemCode   "
                    + " where lab.site_Code = '" + DmServer.getSsnSiteCode(this) + "' AND vt.category= " + vCat + " ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLabNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("labRecId")));
                listNode.setLabType(DmShared.xNull(rsTemp.getString("category")));
                listNode.setLabDate(DmShared.dNull(rsTemp.getDate("labDate")));
                listNode.setLabTime(DmServer.vNull(rsTemp.getString("labTime"), "00.00"));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setTestTemplate(DmShared.xNull(rsTemp.getString("testTemplateName")));
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
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                STApprovals.labApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                STApprovals.labApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
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

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    STApprovals.labApproval(walkThroughEnum, keyCode, userName, cnTemp);
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

//    @Override                                 <Avinash>
//    public DmResult<CardDetail_Mdl> getCardDetail() {
//        DmResult<CardDetail_Mdl> dmrTemp = new DmResult();
//        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
//        String sqlQuery = "";
//        dmrTemp.setData(mrTemp);
//
//        try {
//
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//
//            sqlQuery = " Select count(lab.docId) as cnt, "
//                    + " sum(case when DATEDIFF(day, lab.v_Date, CAST(getdate() AS date)) <= " + STEnum.PriorityDays.LOW_PRIORITYDAYS.getDays() + " then 1 else 0  end)  as low, "
//                    + " sum(case when DATEDIFF(day, lab.v_Date, CAST(getdate() AS date)) > " + STEnum.PriorityDays.LOW_PRIORITYDAYS.getDays() + "  and DATEDIFF(day, lab.v_Date, CAST(getdate() AS date)) <= " + STEnum.PriorityDays.MEDIUM_PRIORITYDAYS.getDays() + " then 1 else 0  end)  as medium, "
//                    + " sum(case when DATEDIFF(day, lab.v_Date, CAST(getdate() AS date)) >= " + STEnum.PriorityDays.HIGH_PRIORITYDAYS.getDays() + " then 1 else 0  end)  as high "
//                    + " from lab as lab "
//                    + " left join voucher_type as vt on lab.v_Type = vt.v_Type "
//                    + " left join( "
//                    + " select lab.docId from lab as lab "
//                    + "	group by lab.docId "
//                    + " ) as undt on undt.docId = lab.docId "
//                    + " LEFT JOIN walkThroughFootPrint wtfp on wtfp.wtaCode = lab.wtaCode and wtfp.isStatus = 0 "
//                    + " where undt.docId is not null "
//                    + " and isNull(lab.approvedBy,'') = '' "
//                    + " and vt.category = ('" + pvc.LAB + "') "
//                    + " AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'"
//                    + " and lab.site_Code = :site_Code ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                mrTemp.setCount(DmShared.iNull(rsTemp.getString("cnt")).toString());
//                mrTemp.setLowPriorityDays(DmShared.iNull(rsTemp.getString("low")).toString());
//                mrTemp.setMediumPriorityDays(DmShared.iNull(rsTemp.getString("medium")).toString());
//                mrTemp.setHighPriorityDays(DmShared.iNull(rsTemp.getString("high")).toString());
//            }
//            rsTemp.close();
//            psTemp.close();
//
//            dmrTemp.setStatus(true);
//            cnTemp.close();
//        } catch (Exception e) {
//            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return dmrTemp;
//
//    }

    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);

            dmrTemp.setData(defaultValue);
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

    @Override
    public DmResultPageing<List<TxtHSampleNo_Mdl>> getTxtHSampleNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSampleNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSampleNo_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TxtHSampleNo_Mdl listNode;
        String condition = "";
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = ("select smp.docId, smp.recId, smp.v_Date , smp.v_Time, "
                    + " tmm.description as testTemplateName , im.name as itemName "
                    + " from labsample smp "
                    + "	LEFT JOIN testTemplateMast tmm ON tmm.code = smp.testTemplateCode "
                    + "	LEFT JOIN itemMast im ON im.code = smp.itemCode ");
            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {

                listNode = new TxtHSampleNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setSampleDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setSampleTime(DmServer.vNull(rsTemp.getString("v_Time"), "00.00"));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setTestTemplate(DmShared.xNull(rsTemp.getString("testTemplateName")));
                lstTemp.add(listNode);

            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHTechnician_Mdl>> getTxtHTechnician(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTechnician_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTechnician_Mdl> lstTemp = new ArrayList<>();
        TxtHTechnician_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select sg.name, sg.subcode,  sg.manualCode "
                    + " from enviro env "
                    + " left join designationMast dm ON dm.code = env.lab_ChemistCode "
                    + " left join subGroup2 sg2 ON sg2.DesignationCode = dm.code "
                    + " left join subGroup sg ON sg.subcode = sg2.subCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTechnician_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subcode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT im.code,im.name as itemName, im.manualCode  "
                    + " FROM itemMast im "
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " WHERE im.isActive = 1 "
                    + " AND (im.filterItemType <> :filterItemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("OI", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTestTemplate(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(
                    " select code, description from testTemplateMast "
                    + " where active = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                lstTemp.add(listNode);
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
}
