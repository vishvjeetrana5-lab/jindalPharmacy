package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptGroupWiseSaleRegister_Srv;
import aarogya.pharmacy.reports.shared.TxtHCompany_Mdl;
import aarogya.pharmacy.reports.shared.TxtHDivision_Mdl;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.pharmacy.reports.shared.TxtHSaleBillNo_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptGroupWiseSaleRegister_SrvImpl extends DmRSSPrivate implements RptGroupWiseSaleRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String category) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = " ";

        String group1Code = "", group2Code = "", group1Name = "", group2Name = "";

        try {
            validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  sb.v_Date >= :fromDate ";

            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  sb.v_Date <= :toDate ";
            }

            if (!DmShared.xNull(filter.get("saleType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("saleType").getTag()) + "   ) ";
            }

            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + "   )";
            }
            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultantName").getTag()) + "   )";

            }

            if (!DmShared.xNull(filter.get("medCom").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemMakeCode IN (" + DmServer.getParamList(":medCom ", filter.get("medCom").getTag()) + "   )";
            }

            if (!DmShared.xNull(filter.get("medicineName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and  sbd.itemCode  in (" + DmServer.getParamList(":medicineCode", filter.get("medicineName").getTag()) + "   )";

            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup in (" + DmServer.getParamList(":medicineGroup", filter.get("medicineGroup").getTag()) + "   )";

            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode in (" + DmServer.getParamList(":medicineCat", filter.get("medicineCat").getTag()) + "   )";

            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + "   )";

            }
            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vtpr.category = ('" + pvc.OPD + "  ') and lm.regDocId IS NOT NULL ";

                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vtpr.category = ('" + pvc.IPD + "  ') and lm.regDocId IS NOT NULL ";

                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vtpr.category = ('" + pvc.EMERGENCY + "  ') and lm.regDocId IS NOT NULL ";

                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vtpr.category = ('" + pvc.DAY_CARE + "  ') and lm.regDocId IS NOT NULL ";

                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("L")) {
                    condition += " and lm.subCode IS NOT NULL ";

                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("W")) {
                    condition += " and lm.subCode IS NULL and lm.regDocId IS NULL ";

                }
            }

            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  lm.code IN (" + DmServer.getParamList(":phAccount", filter.get("phAccount").getTag()) + "   ) ";

            }

            if (DmShared.xNull(filter.get("group1").getText()).trim().equalsIgnoreCase("Patient")) {
                group1Code = "sb.lmCode";
                group1Name = "lm.name";
            } else if (DmShared.xNull(filter.get("group1").getText()).trim().equalsIgnoreCase("Company")) {
                group1Code = " im.itemMakeCode ";
                group1Name = " imm.companyName ";
            } else if (DmShared.xNull(filter.get("group1").getText()).trim().equalsIgnoreCase("Division")) {
                group1Code = " im.divisionCode ";
                group1Name = " sgd.name ";
            } else if (DmShared.xNull(filter.get("group1").getText()).trim().equalsIgnoreCase("Organization")) {
                group1Code = " pr.organizationCode ";
                group1Name = " sgo.name ";
            }
            if (DmShared.xNull(filter.get("group2").getText()).trim().equalsIgnoreCase("Patient")) {
                group2Code = "sb.lmCode";
                group2Name = "lm.name";
            } else if (DmShared.xNull(filter.get("group2").getText()).trim().equalsIgnoreCase("Company")) {
                group2Code = " im.itemMakeCode ";
                group2Name = " imm.companyName ";
            } else if (DmShared.xNull(filter.get("group2").getText()).trim().equalsIgnoreCase("Division")) {
                group2Code = " im.divisionCode ";
                group2Name = " sgd.name ";
            } else if (DmShared.xNull(filter.get("group2").getText()).trim().equalsIgnoreCase("Organization")) {
                group2Code = " pr.organizationCode ";
                group2Name = " sgo.name ";
            } else {
                group2Code = " 0 ";
                group2Name = " '' ";
            }

            sqlQuery = " SELECT "
                    + " sbd.itemCode "
                    + " ,MAX(" + group1Code + ") as group1Code "
                    + " ,MAX(" + group1Name + ") as group1Name "
                    + " ,MAX(" + group2Code + ") as group2Code "
                    + " ,MAX(" + group2Name + ") as group2Name "
                    + "  ,MAX(im.name) as itemName  ,"
                    + "   MAX(im.itemPacking) as itemPacking  ,"
                    + "   MAX(um.unit) as unitName  ,"
                    + "   MAX(sbd.saleUnitCode) as unitCode  ,"
                    + "   SUM(sbd.quantity) as qty  ,"
                    + "   SUM(sbd.freeQty) as freeQty  ,"
                    + "   SUM(sbd.oh_AMt_Net) as netAmt  ,"
                    + "   MAX(im.itemMakeCode) as companyCode  ,"
                    + "   MAX(imm.companyName) as companyName  ,"
                    + "   MAX(im.divisionCode) as divisionCode  ,MAX(sgd.name) as divisionName ,"
                    + "   Max(sb.lmCode) as patientCode, MAX(lm.displayName) as patientName ,"
                    + "   MAX(pr.organizationCode) AS organizationCode ,"
                    + "   MAX(sgo.name) AS organizationName "
                    + "   from saleBillDetail as sbd"
                    + "  left join saleBill as sb on sb.docId=sbd.docId"
                    + "  left join ledgerMast as lm on sb.lmCode=lm.Code"
                    + "  left join voucher_Type as vt on sb.v_Type=vt.v_Type"
                    + "  left JOIN itemMast im ON im.code=sbd.itemCode"
                    + "  LEFT JOIN unitMast um ON um.code=sbd.saleUnitCode"
                    + "  LEFT JOIN subGroup sgd ON sgd.subCode=im.divisionCode"
                    + "  LEFT JOIN patientregistration pr ON sb.regDocid = pr.regDocid"
                    + "  LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type"
                    + "  LEFT JOIN itemmakemast imm ON imm.code=im.itemMakeCode"
                    + "  LEFT JOIN subGroup sgo ON sgo.subCode=pr.organizationCode"
                    + "  where sbd.isFooter=0   "
                    + "  AND  vt.category IN (  '" + category + "  ' )"
                    + "  AND sb.isCancelled = 0 " + condition
                    + "  GROUP BY sbd.itemCode "
                    + "  ORDER BY MAX(" + group1Name + "),MAX(" + group2Name + "),MAX(im.name)";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();
            hm.put("group1Name", filter.get("group1").getText());
            hm.put("group2Name", filter.get("group2").getText());
            jasperReport = DmServer.compileJasper("Pharmacy_GroupWiseSaleRegister");
            jasperPrint = DmServer.fillJasper(this, "Group Wise Sale Register ", jasperReport, hm, filter, rsTemp);

            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
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

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "To Date is mandatory.");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }

        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 300);

        DmShared.isValidText(hashMapFilter.get("group1").getTag(), "Group By (First)", 500);
//        DmShared.isValidText(hashMapFilter.get("group2").getTag(), "Group By (Second)", 600);

        if (hashMapFilter.get("group1").getTag().equalsIgnoreCase(hashMapFilter.get("group2").getTag())) {
            throw new DmException(400, "Group By (First) and  Group By (Second) cannot be same.");
        }
    }

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
//            if (!DmShared.xNull(recIdPrefixFrom).toString().trim().equals("") && !DmShared.xNull(recIdPrefixTo).toString().trim().equals("")) {
//                if (DmShared.xNull(recIdPrefixFrom).toString().trim().equalsIgnoreCase(recIdPrefixTo)) {
//                    psTemp.setString("recIdPrefix", DmShared.xNull(recIdPrefixFrom).toString());
//
//                    if (DmShared.iNull(vNoFrom).intValue() < DmShared.iNull(vNoTo).intValue()) {
//                        psTemp.setString("vNoFrom", DmShared.iNull(vNoFrom).toString());
//                        psTemp.setString("vNoTo", DmShared.iNull(vNoTo).toString());
//                    }
//                }
//            }
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("saleType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("saleType").getTag()));
            }
//            if (!DmShared.xNull(filter.get("billNo").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("docId", DmShared.xNull(filter.get("billNo").getTag()));
//            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("departmentName").getTag()));
            }
            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultantCode", DmShared.xNull(filter.get("consultantName").getTag()));
            }
            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("phAccount", DmShared.xNull(filter.get("phAccount").getTag()));
            }
//            if (!DmShared.xNull(filter.get("operation").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setString("serviceCode", DmShared.xNull(filter.get("operation").getTag()));
//            }

            if (!DmShared.xNull(filter.get("medCom").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCom", DmShared.xNull(filter.get("medCom").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicineName").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCat", DmShared.xNull(filter.get("medicineCat").getTag()));
            }
//            if (!DmShared.xNull(filter.get("restrictGroup").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("restrictGroup", DmShared.xNull(filter.get("restrictGroup").getTag()));
//            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }

            if (!DmShared.xNull(filter.get("division").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("division", DmShared.xNull(filter.get("division").getTag()));
            }

        } catch (Exception e) {
        }
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
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select dm.subCode, sg.name "
                    + "  	from departmentMast dm"
                    + "  	left join subGroup sg on sg.subCode = dm.subCode"
                    + "  	left join subGroupNature sgn on sgn.code = sg.nature"
                    + "  	where sgn.manualCode in ('PH','CM') and sg.isActive = 1");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	cm.subCode as code, sg.name  "
                    + "  		from consultantMast as cm  "
                    + "  		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
                    + "  		left join subGroup as sg on cm.subCode = sg.subCode  "
                    + "  		left join subgroupNature as sgn on sg.nature = sgn.code  "
                    + "  		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT   "
                    + "    im.Code,im.name,im.manualCode  "
                    + "   FROM itemMast im "
                    + "   WHERE (im.filterItemType = :filterItemType or im.isCommon=1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCat(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ic.code, ic.name "
                    + "  FROM itemCat ic"
                    + "   WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemgroup ig"
                    + "     WHERE (ig.filterItemType = :filterItemType or isCommon=1) ");;
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + "   FROM siteMast sm"
                    + "   where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + "  )");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(String category, DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT"
                    + "      vt.v_Type AS code"
                    + "   , vt.description AS name"
                    + "   FROM voucher_Type vt"
                    + "   WHERE vt.category IN ('" + category + "  ')");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  sm.code , sm.name"
                    + "   FROM siteMast sm"
                    + "   where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.IPD);
            listNode.setSharedKeyFieldName(pvc.IPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD);
            listNode.setSharedKeyFieldName(pvc.OPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Ledger");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("W");
            listNode.setSharedKeyFieldName("Walk-In");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHLMCode_Mdl>> getTxtHLMCode(String type, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLMCode_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLMCode_Mdl> lstTemp = new ArrayList<>();
        TxtHLMCode_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(type).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vt.category = ('" + pvc.OPD + "  ') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vt.category = ('" + pvc.IPD + "  ') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vt.category = ('" + pvc.EMERGENCY + "  ') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vt.category = ('" + pvc.DAY_CARE + "  ') and isNull(lm.regDocId,'')<>'' ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("L")) {
                    condition += " and isNull(lm.subCode,'')<>''  ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("W")) {
                    condition += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
                }
            }
            sqlWithParam.setSQL("select lm.code,lm.name,lm.manualCode,c.cityName,pr.recid as regNo,sg.name as acName"
                    + "   from ledgerMast as lm"
                    + "   left join city as c on lm.cityCode=c.cityCode"
                    + "   left join patientRegistration as pr on lm.regDocId=pr.regDocId"
                    + "   left join voucher_Type as vt on vt.v_Type=pr.v_type"
                    + "   left join subGroup as sg on lm.subCode=sg.subCode"
                    + "   where 1=1 " + condition);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLMCode_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
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
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT CODE, companyName, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "     WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPurchaseDisPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer"), "0.00"));
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
    public DmResultPageing<List<TxtHDivision_Mdl>> getTxtHDivision(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHDivision_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHDivision_Mdl> lstTemp = new ArrayList<>();
        TxtHDivision_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String filterNature = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            filterNature = DmServer.getCodeViaManualCode("DV", "manualCode", "subgroupNature", "code", cnTemp);

            sqlWithParam.setSQL(" select sg.subCode,sg.name,ct.cityName,sg.manualCode,concat(add1,add2) as address,sg.filterNature  from subGroup as sg "
                    + "   left join city as ct on sg.cityCode = ct.cityCode where filterNature = :filterNature ");
            sqlWithParam.setString("filterNature", filterNature);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHDivision_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
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
    public DmResultPageing<List<KeyFieldExtended>> getGroupName(String excludeGroup, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("Division")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Division");
                listNode.setSharedKeyFieldName("Division");
                lstTemp.add(listNode);
            }
            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("Company")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Company");
                listNode.setSharedKeyFieldName("Company");
                lstTemp.add(listNode);
            }
            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("Organization")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Organization");
                listNode.setSharedKeyFieldName("Organization");
                lstTemp.add(listNode);
            }
            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("Patient")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Patient");
                listNode.setSharedKeyFieldName("Patient");
                lstTemp.add(listNode);
            }

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
