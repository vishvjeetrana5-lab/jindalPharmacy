package aarogya.pharmacy.procurementPlannerDisplay.server;

import aarogya.pharmacy.procurementPlannerDisplay.shared.*;
import aarogya.pharmacy.procurementPlannerDisplay.shared.SearchValue_Mdl;
import aarogya.pharmacy.procurementPlannerDisplay.shared.TxtHMedicine_Mdl;
import aarogya.pharmacy.procurementPlannerDisplay.shared.Mdl_Main;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
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
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_Srv;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ProcurementPlannerDisplay_SrvImpl extends DmRSSPrivate implements ProcurementPlannerDisplay_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    String vCatPROC = "";// + pvc.PROCUREMENT_PLANNER_DISPLAY + "";
    String moduleName;
    @Override
    public DmResult<List<ProcurementPlannerDisplay_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl) {
        DmResultPageing<List<ProcurementPlannerDisplay_Mdl>> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, conditionDept = "", conditionItmMst = "";
        ProcurementPlannerDisplay_Mdl node;
        List<ProcurementPlannerDisplay_Mdl> lstTemp = new ArrayList<>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                conditionItmMst +=" where im.filterItemType=11 \n ";
            }else{
                conditionItmMst +=" where im.filterItemType=5 \n ";
            }
            if (!DmShared.xNull(searchValueMdl.getDepartmentCode()).trim().equals("")) {
                conditionDept += " AND  trn.departmentCode IN (" + DmServer.getParamList(":departmentCode", searchValueMdl.getDepartmentCode()) + " ) ";
            } else if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).trim().equalsIgnoreCase("")) {
                conditionDept += "and trn.departmentCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
//                conditionItmStk = "and departmentCode in (" + STServer.getSsnDepartmentCodeList(this) + "," + STServer.getSsnDepartmentCode(this) + ")";
            }

            if (!DmShared.xNull(searchValueMdl.getCompanyCode()).trim().equals("")) {
                conditionItmMst += " AND im.itemmakeCode IN (" + DmServer.getParamList(":medMake", searchValueMdl.getCompanyCode()) + " )";
            }

            if (!DmShared.xNull(searchValueMdl.getGroupCode()).trim().equals("")) {
                conditionItmMst += " AND im.itemGroup IN (" + DmServer.getParamList(":medGroup", searchValueMdl.getGroupCode()) + " )";
            }

            if (!DmShared.xNull(searchValueMdl.getCategoryCode()).trim().equals("")) {
                conditionItmMst += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":medCat", searchValueMdl.getCategoryCode()) + " )";
            }

            if (!DmShared.xNull(searchValueMdl.getMedicineCode()).trim().equals("")) {
                conditionItmMst += " AND im.code IN (" + DmServer.getParamList(":medicineCode", searchValueMdl.getMedicineCode()) + " )";
            }

            if (searchValueMdl.isOnlyShortForDept()) {
                conditionItmMst += " and (((dCnp.cnpDept/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + "))*" + DmShared.iNull(searchValueMdl.getProjectionDays()) + ")-dstk.currStkDept-(isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0)) >0 ";
            }

            if (searchValueMdl.isOnlyShortForSite()) {
                conditionItmMst += " and (((sCnp.cnpSite/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + "))*" + DmShared.iNull(searchValueMdl.getProjectionDays()) + ")-sstk.currStkSite-(isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0)) >0 ";
            }

            if (DmShared.iNull(searchValueMdl.getStockRunDaysDept()).intValue() > 0) {
                conditionItmMst += "and (case when (dCnp.cnpDept/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ")>0 then dstk.currStkDept/((dCnp.cnpDept/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ")) else 0 end) <=" + DmShared.iNull(searchValueMdl.getStockRunDaysDept()).intValue();
            }

            if (DmShared.iNull(searchValueMdl.getStockRunDaysSite()).intValue() > 0) {
                conditionItmMst += "and (case when (sCnp.cnpSite/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ") > 0 then sstk.currStkSite/((sCnp.cnpSite/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ")) else 0 end)<= " + DmShared.iNull(searchValueMdl.getStockRunDaysSite()).intValue();
            }

            sqlQuery = "select im.name as itemName,im.code as itemCode,im.purchaseUnitConversionRate,im.purchaseRate,um.code as skuCode,um.unit as skuName,dstk.currStkDept,sStk.currStkSite,dCnp.cnpDept,sCnp.cnpSite \n"
                    + "         ,(dstk.currStkDept-((dCnp.cnpDept/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + "))*" + DmShared.iNull(searchValueMdl.getProjectionDays()) + ") as exc_Srt_Dept \n"
                    + "         ,(sstk.currStkSite-((sCnp.cnpSite/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + "))*" + DmShared.iNull(searchValueMdl.getProjectionDays()) + ") as exc_Srt_Site \n"
                    + "		,case when dCnp.cnpDept>0 then dstk.currStkDept/((dCnp.cnpDept/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ")) else 0 end as stkRunDaysDept \n"
                    + "		,case when sCnp.cnpSite>0 then sstk.currStkSite/((sCnp.cnpSite/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ")) else 0 end as stkRunDaysSite \n"
                    + "		,isnull(drp.deptReqQty,0) as deptReqQty,isNull(srp.siteReqQty,0) as siteReqQty \n"
                    + "		,isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0) as pendingPoIndent \n"
                    + "		,(((dCnp.cnpDept/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + "))*" + DmShared.iNull(searchValueMdl.getProjectionDays()) + ")-dstk.currStkDept-(isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0)) as recommendedQtyDept \n"
                    + "		,(((sCnp.cnpSite/" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + "))*" + DmShared.iNull(searchValueMdl.getProjectionDays()) + ")-sstk.currStkSite-(isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0)) as recommendedQtySite \n"
                    + "         	from itemMast im \n"
                    + "                 /*++++++++++++++++++++++++ Department wise stock +++++++++++++++++++++++*/ \n"
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(balanceQty) as currStkDept \n"
                    + "                             from itemBatchStock trn \n"
                    + "                             where trn.site_Code= :site_Code \n"
                    + "                            " + conditionDept
                    + "                             group by trn.itemCode  \n"
                    + "                         ) dStk on im.code=dStk.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Site wise stock +++++++++++++++++++++++*/"
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(balanceQty) as currStkSite \n"
                    + "                             from itemBatchStock trn \n"
                    + "                             where trn.site_Code= :site_Code group by trn.itemCode \n"
                    + "                         ) sstk on im.code=sStk.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Department wise consumption +++++++++++++++++++++++*/ "
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(trn.isuQty) as cnpDept \n"
                    + "                             from stock trn \n"
                    + "                             left join voucher_Type vt on trn.v_Type=vt.v_Type \n"
                    + "                             where trn.site_Code= :site_Code  \n"
                    + "                            " + conditionDept
                    + "                             and trn.v_Date between DATEADD(d,-" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ",:asOnDate) and :asOnDate \n"
                    + "                             and vt.category not in('MTRWS','MEDTR','ITMTR')  \n"
                    + "                             group by trn.itemCode \n"
                    + "                         ) dCnp on im.code=dCnp.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Site wise consumption +++++++++++++++++++++++*/ "
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(trn.isuQty) as cnpSite \n"
                    + "                             from stock trn \n"
                    + "                             left join voucher_Type vt on trn.v_Type=vt.v_Type \n"
                    + "                             where trn.site_Code= :site_Code  \n"
                    + "                             and trn.v_Date between DATEADD(d,-" + DmShared.iNull(searchValueMdl.getConsumptionDays()) + ",:asOnDate) and :asOnDate \n"
                    + "                             and vt.category not in('MTRWS','MEDTR','ITMTR')  \n"
                    + "                             group by trn.itemCode \n"
                    + "                         ) sCnp on im.code=sCnp.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Department wise pending requisition +++++++++++++++++++++++*/ "
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         select rqd.itemCode,sum(isnull(rqd.reqQty,0)-isnull(rqd.issueQty,0)-isnull(rqd.indentQty,0)) as deptReqQty \n"
                    + "                             from requisition trn \n"
                    + "                             left join requisitionDetail rqd on trn.docid=rqd.docid \n"
                    + "                             where trn.site_Code =:site_Code and isnull(trn.approvedBy,'') <> '' and rqd.isStatus=0 \n"
                    + "                            " + conditionDept
                    + "                             group by rqd.itemcode \n"
                    + "                         ) As dRp on dRp.ItemCode=im.Code \n"
                    + "                 /*++++++++++++++++++++++++ Site wise pending requisition +++++++++++++++++++++++*/"
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         select rqd.itemCode,sum(isnull(rqd.reqQty,0)-isnull(rqd.issueQty,0)-isnull(rqd.indentQty,0)) as siteReqQty \n"
                    + "                             from requisition rs \n"
                    + "                             left join requisitionDetail rqd on rs.docid=rqd.docid \n"
                    + "                             where rs.site_Code = :site_Code and isnull(rs.approvedBy,'') <> '' and rqd.isStatus=0 \n"
                    + "                             group by rqd.itemcode \n"
                    + "                         ) As sRp on sRp.ItemCode=im.Code \n"
                    + "                 /*++++++++++++++++++++++++ Pending indent +++++++++++++++++++++++*/"
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         SELECT rqd.itemCode, sum(isnull(rqd.aprQty,0)-isNUll(rqd.orderedQty,0)) as sitePendingIndent \n"
                    + "                             FROM indent as rq \n"
                    + "                             left join indentDetail rqd on rq.docid=rqd.docid \n"
                    + "                             WHERE  rq.site_Code=:site_Code  \n"
                    + "                             and rqd.isStatus=0  \n"
                    + "                             group by rqd.itemCode \n"
                    + "                         ) as spi on spi.itemCode=im.code \n"
                    + "                 /*++++++++++++++++++++++++ Pending pOrder +++++++++++++++++++++++*/"
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         SELECT rqd.itemCode, sum(isnull(rqd.quantity,0)-isNUll(rqd.challanedQty,0)) as PendingPO \n"
                    + "                             FROM pOrder as rq \n"
                    + "                             left join pOrderDetail rqd on rq.docid=rqd.docid \n"
                    + "                             WHERE  rq.site_Code=:site_Code  \n"
                    + "                             and rqd.isStatus=0  \n"
                    + "                             group by rqd.itemCode \n"
                    + "                         ) as pord on pord.itemCode=im.code \n"
                    + "                 left join unitMast um on im.sku=um.code  \n" + conditionItmMst
                    + "		order by itemName \n";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            psTemp.setString("asOnDate", DmShared.xNull(DmServer.getServerDateTime()[0]));

            if (!DmShared.xNull(searchValueMdl.getDepartmentCode()).trim().equals("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(DmShared.xNull(searchValueMdl.getDepartmentCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getCompanyCode()).trim().equals("")) {
                psTemp.setStringList("medMake", DmShared.xNull(DmShared.xNull(searchValueMdl.getCompanyCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getGroupCode()).trim().equals("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(DmShared.xNull(searchValueMdl.getGroupCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getCategoryCode()).trim().equals("")) {
                psTemp.setStringList("medCat", DmShared.xNull(DmShared.xNull(searchValueMdl.getCategoryCode())));
            }

            if (!DmShared.xNull(searchValueMdl.getMedicineCode()).trim().equals("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(DmShared.xNull(searchValueMdl.getMedicineCode())));
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new ProcurementPlannerDisplay_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setCurrentStkDept(DmServer.vNull(rsTemp.getString("currStkDept"), "0.00"));
                node.setCurrentStkSite(DmServer.vNull(rsTemp.getString("currStkSite"), "0.00"));
                node.setConsumptionDept(DmServer.vNull(rsTemp.getString("cnpDept"), "0.00"));
                node.setConsumptionSite(DmServer.vNull(rsTemp.getString("cnpSite"), "0.00"));
                node.setSurplusDeficitDept(DmServer.vNull(rsTemp.getString("exc_Srt_Dept"), "0.00"));
                node.setSurplusDeficitSite(DmServer.vNull(rsTemp.getString("exc_Srt_Site"), "0.00"));
                node.setStkRunDaysDept(DmServer.vNull(rsTemp.getString("stkRunDaysDept"), "0"));
                node.setStkRunDaysSite(DmServer.vNull(rsTemp.getString("stkRunDaysSite"), "0"));
                node.setRequiredQtyDept(DmServer.vNull(rsTemp.getString("deptReqQty"), "0.00"));
                node.setRequiredQtySite(DmServer.vNull(rsTemp.getString("siteReqQty"), "0.00"));
                node.setPendingPOIndentQty(DmServer.vNull(rsTemp.getString("pendingPoIndent"), "0.00"));
                node.setRecommendedQtyDept(DmServer.vNull(rsTemp.getString("recommendedQtyDept"), "0.00"));
                node.setRecommendedQtySite(DmServer.vNull(rsTemp.getString("recommendedQtySite"), "0.00"));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate"), "0.00"));
                node.setPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));

                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
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
    public DmResult<ProcurementPlannerDisplay_Mdl> getRequisitionDetail(String department) {
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

            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                condition = " and sgn.manualCode in ('CM','PH') ";
            }else {
                condition = " and sgn.manualCode in ('CM','PR') ";
            }
            sqlWithParam.setSQL("select	dm.subCode, sg.name"
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "		where sg.isActive = 1 " + condition);

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
    public DmResult<KeyFieldExtended> getDefaultValues(String moduleName) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended lstTemp = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                vCatPROC = "" + pvc.PROCUREMENT_PLANNER_DISPLAY + "";
            }else {
                vCatPROC = "" + pvc.PROCUREMENT_PLANNER_DISPLAY_INVENTORY + "";
            }
            this.moduleName=DmShared.xNull(moduleName).trim();
            lstTemp.setSharedModifiedBy(DmShared.xNull(moduleName));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT imm.code, imm.companyName AS name"
                    + " FROM itemMakeMast imm"
                    + " WHERE imm.filterItemType = :filterItemType");
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            }else {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            }
            
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name"
                    + " FROM itemgroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon=1)");
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            }else {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            }
            
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " FROM itemcat ic "
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            }else {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            }
            
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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT im.code, im.name, im.manualCode "
                    + " FROM itemMast im"
                    + " LEFT JOIN unitmast um ON im.sku = um.code "
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon=1) and im.isActive = 1");
            if (DmShared.xNull(moduleName).equalsIgnoreCase(new pmPharmacy().moduleName)) {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            }else {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            }
            
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResult<KeyFieldExtended> saveRecord(SearchValue_Mdl mrTemp, List<ProcurementPlannerDisplay_Mdl> gridMdl, String voucherPrefix) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;

        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
//        DBCollection dBCollection;
//        String filter_Mdl = "", grid_Mdl = "";
        String sqlQuery;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select v_Type from  voucher_Type where category = :category ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("category", DmShared.xNull(vCatPROC).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setVoucherTypeTag(DmShared.xNull(rsTemp.getString("v_Type")));
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setVoucherDate(DmServer.getServerDateTime()[0]);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdProcurementPlanner(DmServer.getRecId("procurementPlanner", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdProcurementPlanner(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdProcurementPlanner().getRecIdValue()));
            }

            sqlQuery = "INSERT INTO procurementPlanner (\n"
                    + "       docID , v_No , v_Type, recID,recIdPrefix , v_Prefix, v_Date, site_Code, preparedBy , preparedDate\n"
                    + "       ,departmentName,departmentCode,medicineName,medicineCode,categoryName,categoryCode,companyName,companyCode\n"
                    + "       ,groupName,groupCode,projectionDays,consumptionDays,stockRunDaysDept,stockRunDaysSite\n"
                    + " ) Values (\n"
                    + "       :docId, :v_No, :v_Type, :recID,:recIdPrefix, :v_Prefix, :v_Date, :site_Code, :preparedBy, :preparedDate\n"
                    + "       ,:departmentName,:departmentCode,:medicineName,:medicineCode,:categoryName,:categoryCode,:companyName,:companyCode\n"
                    + "       ,:groupName,:groupCode,:projectionDays,:consumptionDays,:stockRunDaysDept,:stockRunDaysSite)";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdProcurementPlanner().getCounter()).toString());
            psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeTag()));
            psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdProcurementPlanner().getPrefix()));
            psTemp.setString("v_Prefix", DmShared.xNull(voucherPrefix));
            psTemp.setString("v_Date", DmShared.xNull(mrTemp.getVoucherDate()));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
            psTemp.setString("departmentName", DmShared.xNull(mrTemp.getDepartmentName()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            psTemp.setString("medicineName", DmShared.xNull(mrTemp.getMedicineName()));
            psTemp.setString("medicineCode", DmShared.xNull(mrTemp.getMedicineCode()));
            psTemp.setString("categoryName", DmShared.xNull(mrTemp.getCategoryName()));
            psTemp.setString("categoryCode", DmShared.xNull(mrTemp.getCategoryCode()));
            psTemp.setString("companyName", DmShared.xNull(mrTemp.getCompanyName()));
            psTemp.setString("companyCode", DmShared.xNull(mrTemp.getCompanyCode()));
            psTemp.setString("groupName", DmShared.xNull(mrTemp.getGroupName()));
            psTemp.setString("groupCode", DmShared.xNull(mrTemp.getGroupCode()));
            psTemp.setString("projectionDays", DmServer.vNull(mrTemp.getProjectionDays()));
            psTemp.setString("consumptionDays", DmServer.vNull(mrTemp.getConsumptionDays()));
            psTemp.setString("stockRunDaysDept", DmServer.vNull(mrTemp.getStockRunDaysDept()));
            psTemp.setString("stockRunDaysSite", DmServer.vNull(mrTemp.getStockRunDaysSite()));

            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from procurementPlannerDetail >>====================================*/
            sqlQuery = "Delete From procurementPlannerDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            /*==============================<< insert into procurementPlannerDetail >>====================================*/

            for (int i = 0; i < gridMdl.size(); i++) {
                sqlQuery = "INSERT INTO procurementPlannerDetail (\n"
                        + "       docID , itemCode, skuCode, currentStkDept,currentStkSite, consumptionDept, consumptionSite, surplusDeficitDept\n"
                        + "       ,surplusDeficitSite,stkRunDaysDept,stkRunDaysSite,requiredQtyDept,requiredQtySite,pendingPOIndentQty\n"
                        + "       ,recommendedQtyDept,recommendedQtySite,finalIndentedQty,remark,requiredDate,purchaseUnitConversionRate\n"
                        + "       ,purchaseRate,estimatedCost\n"
                        + " ) Values (\n"
                        + "       :docID , :itemCode, :skuCode, :currentStkDept,:currentStkSite, :consumptionDept, :consumptionSite, :surplusDeficitDept\n"
                        + "       ,:surplusDeficitSite,:stkRunDaysDept,:stkRunDaysSite,:requiredQtyDept,:requiredQtySite,:pendingPOIndentQty\n"
                        + "       ,:recommendedQtyDept,:recommendedQtySite,:finalIndentedQty,:remark,:requiredDate,:purchaseUnitConversionRate\n"
                        + "       ,:purchaseRate,:estimatedCost)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("itemCode", DmShared.foriegnKey(gridMdl.get(i).getItemCode()));
                psTemp.setString("skuCode", DmShared.foriegnKey(gridMdl.get(i).getSkuCode()));
                psTemp.setString("currentStkDept", DmServer.vNull(gridMdl.get(i).getCurrentStkDept(),"0.00"));
                psTemp.setString("currentStkSite", DmServer.vNull(gridMdl.get(i).getCurrentStkSite(),"0.00"));
                psTemp.setString("consumptionDept", DmServer.vNull(gridMdl.get(i).getConsumptionDept(),"0.00"));
                psTemp.setString("consumptionSite", DmServer.vNull(gridMdl.get(i).getConsumptionSite(),"0.00"));
                psTemp.setString("surplusDeficitDept", DmServer.vNull(gridMdl.get(i).getSurplusDeficitDept(),"0.00"));
                psTemp.setString("surplusDeficitSite", DmServer.vNull(gridMdl.get(i).getSurplusDeficitSite(),"0.00"));
                psTemp.setString("stkRunDaysDept", DmServer.vNull(gridMdl.get(i).getStkRunDaysDept(),"0.00"));
                psTemp.setString("stkRunDaysSite", DmServer.vNull(gridMdl.get(i).getStkRunDaysSite(),"0.00"));
                psTemp.setString("requiredQtyDept", DmServer.vNull(gridMdl.get(i).getRequiredQtyDept(),"0.00"));
                psTemp.setString("requiredQtySite", DmServer.vNull(gridMdl.get(i).getRequiredQtySite(),"0.00"));
                psTemp.setString("pendingPOIndentQty", DmServer.vNull(gridMdl.get(i).getPendingPOIndentQty(),"0.00"));
                psTemp.setString("recommendedQtyDept", DmServer.vNull(gridMdl.get(i).getRecommendedQtyDept(),"0.00"));
                psTemp.setString("recommendedQtySite", DmServer.vNull(gridMdl.get(i).getRecommendedQtySite(),"0.00"));
                psTemp.setString("finalIndentedQty", DmServer.vNull(gridMdl.get(i).getFinalIndentedQty(),"0.00"));
                psTemp.setString("remark", DmShared.xNull(gridMdl.get(i).getRemark()));
                psTemp.setString("requiredDate", DmShared.xNull(gridMdl.get(i).getRequiredDate()));
                psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(gridMdl.get(i).getPurchaseUnitConversionRate(),"0.00"));
                psTemp.setString("purchaseRate", DmServer.vNull(gridMdl.get(i).getPurchaseRate(),"0.00"));
                psTemp.setString("estimatedCost", DmServer.vNull(gridMdl.get(i).getEstimatedCost(),"0.00"));

                psTemp.executeUpdate();
                psTemp.close();
            }

//            filter_Mdl = DmServer.convertToJson(mrTemp, new TypeToken<SearchValue_Mdl>() {
//            });
//            grid_Mdl = DmServer.convertToJson(gridMdl, new TypeToken<List<ProcurementPlannerDisplay_Mdl>>() {
//            });

//            dBCollection = DmServer.getMdbCollection(this, "PROCUREMENT_PLAN", true);
//            Map<String, Object> map;
//            BasicDBObject document;
//
//            /*===============================< Insert record into PROCUREMENT_PLAN (mongoDB)>==============================*/
//            map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
//            map.put("docId".toLowerCase(), DmShared.xNull(mrTemp.getSharedKeyFieldCode().trim()));
//            map.put("filter_Mdl".toLowerCase(), DmShared.xNull(filter_Mdl).trim());
//            map.put("grid_Mdl".toLowerCase(), DmShared.xNull(grid_Mdl).trim());
//            document = new BasicDBObject(map);
//            dBCollection.insert(document);

            dmrTemp.setValue1(mrTemp.getSharedKeyFieldCode());
            dmrTemp.setValue2(mrTemp.getSharedKeyFieldName());
//            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception ex) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (ex instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) ex).getErrorNo());
                    dmrTemp.setValue1(((DmException) ex).getErrorSource());
                    dmrTemp.setValue2(((DmException) ex).getErrorSourceCode());
                }
            } catch (Exception e) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
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

    ;
    
    @Override
    public DmResult<Mdl_Main> getMoveRec(String strKey) {
        DmResult<Mdl_Main> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Mdl_Main mrTemp = new Mdl_Main();
        dmrTemp.setData(mrTemp);
//        BasicDBObject filters;
//        DBCursor dBCursor;
//        String filterJSON = "", gridJSON = "";
//        List<ProcurementPlannerDisplay_Mdl> gridMdl;
        ProcurementPlannerDisplay_Mdl node;
        List<ProcurementPlannerDisplay_Mdl> lstTemp = new ArrayList<>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT pp.docID as code, pp.recID as name , pp.v_No , pp.v_Type,pp.recIdPrefix , pp.v_Prefix, pp.v_Date \n"
                    + " ,pp.indentDocID,ind.recID as indentRecId,pp.departmentName,pp.departmentCode,pp.medicineName,pp.medicineCode \n"
                    + " ,pp.categoryName,pp.categoryCode,pp.companyName,pp.companyCode,pp.groupName,pp.groupCode,pp.projectionDays\n"
                    + " ,pp.consumptionDays,pp.stockRunDaysDept,pp.stockRunDaysSite\n"
                    + " FROM procurementPlanner as pp \n"
                    + " left join indent ind on pp.indentDocID=ind.docID \n"
                    + " WHERE pp.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getString("v_Date")));
                mrTemp.setIndentDocID(DmShared.xNull(rsTemp.getString("indentDocID")));
                mrTemp.setIndentRecID(DmShared.xNull(rsTemp.getString("indentRecId")));
                mrTemp.getFilterMdl().setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.getFilterMdl().setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.getFilterMdl().setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.getFilterMdl().setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.getFilterMdl().setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.getFilterMdl().setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
                mrTemp.getFilterMdl().setMedicineCode(DmShared.xNull(rsTemp.getString("medicineCode")));
                mrTemp.getFilterMdl().setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                mrTemp.getFilterMdl().setCategoryCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                mrTemp.getFilterMdl().setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                mrTemp.getFilterMdl().setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                mrTemp.getFilterMdl().setCompanyName(DmShared.xNull(rsTemp.getString("companyName")));
                mrTemp.getFilterMdl().setCompanyCode(DmShared.xNull(rsTemp.getString("companyCode")));

                mrTemp.getFilterMdl().setConsumptionDays(DmShared.xNull(rsTemp.getString("consumptionDays")));
                mrTemp.getFilterMdl().setProjectionDays(DmShared.xNull(rsTemp.getString("projectionDays")));
                mrTemp.getFilterMdl().setStockRunDaysDept(DmShared.xNull(rsTemp.getString("stockRunDaysDept")));
                mrTemp.getFilterMdl().setStockRunDaysSite(DmShared.xNull(rsTemp.getString("stockRunDaysSite")));

            }

            psTemp.close();
            rsTemp.close();

            sqlQuery = "select ppd.docId,ppd.itemCode,im.name as itemName,um.unit as skuName,ppd.skuCode,ppd.currentStkDept,ppd.currentStkSite\n"
                    + " ,ppd.consumptionDept,ppd.consumptionSite,ppd.surplusDeficitDept,ppd.surplusDeficitSite,ppd.stkRunDaysDept\n"
                    + " ,ppd.stkRunDaysSite,ppd.requiredQtyDept,ppd.requiredQtySite,ppd.pendingPOIndentQty\n"
                    + " ,ppd.recommendedQtyDept,ppd.recommendedQtySite,ppd.finalIndentedQty,ppd.remark\n"
                    + " ,ppd.requiredDate,ppd.purchaseUnitConversionRate,ppd.purchaseRate,ppd.estimatedCost\n"
                    + " 	from procurementPlannerDetail ppd\n"
                    + " 	left join itemMast im on ppd.itemCode =im.code\n"
                    + " 	left join unitMast um on ppd.skuCode=um.code\n"
                    + "         where ppd.docid=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new ProcurementPlannerDisplay_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setCurrentStkDept(DmServer.vNull(rsTemp.getString("currentStkDept"), "0.00"));
                node.setCurrentStkSite(DmServer.vNull(rsTemp.getString("currentStkSite"), "0.00"));
                node.setConsumptionDept(DmServer.vNull(rsTemp.getString("consumptionDept"), "0.00"));
                node.setConsumptionSite(DmServer.vNull(rsTemp.getString("consumptionSite"), "0.00"));
                node.setSurplusDeficitDept(DmServer.vNull(rsTemp.getString("surplusDeficitDept"), "0.00"));
                node.setSurplusDeficitSite(DmServer.vNull(rsTemp.getString("surplusDeficitSite"), "0.00"));
                node.setStkRunDaysDept(DmServer.vNull(rsTemp.getString("stkRunDaysDept"), "0"));
                node.setStkRunDaysSite(DmServer.vNull(rsTemp.getString("stkRunDaysSite"), "0"));
                node.setRequiredQtyDept(DmServer.vNull(rsTemp.getString("requiredQtyDept"), "0.00"));
                node.setRequiredQtySite(DmServer.vNull(rsTemp.getString("requiredQtySite"), "0.00"));
                node.setPendingPOIndentQty(DmServer.vNull(rsTemp.getString("pendingPOIndentQty"), "0.00"));
                node.setRecommendedQtyDept(DmServer.vNull(rsTemp.getString("recommendedQtyDept"), "0.00"));
                node.setRecommendedQtySite(DmServer.vNull(rsTemp.getString("recommendedQtySite"), "0.00"));
                node.setFinalIndentedQty(DmServer.vNull(rsTemp.getString("finalIndentedQty"), "0.00"));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate"), "0.00"));
                node.setPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
                node.setEstimatedCost(DmServer.vNull(rsTemp.getString("estimatedCost"), "0.00"));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setRequiredDate(DmShared.dNull(rsTemp.getDate("requiredDate")));

                lstTemp.add(node);
            }
            mrTemp.getGrdItemDetails().addAll(lstTemp);
            psTemp.close();
            rsTemp.close();

//            DBCollection dBCollection = DmServer.getMdbCollection(this, "PROCUREMENT_PLAN", false);
//
//            filters = new BasicDBObject();
//            filters.put("docID".toLowerCase(), DmShared.xNull(mrTemp.getSharedKeyFieldCode().trim()));
//
////            orderBydBo.append("logdate".toLowerCase(), -1);
//            dBCursor = dBCollection.find(filters);
//
//            if (dBCursor.hasNext()) {
//                DBObject dBObject = dBCursor.next();
//                filterJSON = String.valueOf(dBObject.get("filter_Mdl".toLowerCase()));
//                gridJSON = String.valueOf(dBObject.get("grid_Mdl".toLowerCase()));
//            }
//
//            mrTemp.setFilterMdl(DmServer.convertFromJson(filterJSON, new TypeToken<SearchValue_Mdl>() {
//            }));
//            mrTemp.setGrdItemDetails(DmServer.convertFromJson(gridJSON, new TypeToken<List<ProcurementPlannerDisplay_Mdl>>() {
//            }));
            cnTemp.close();
            dmrTemp.setStatus(true);

//            savetoSql();
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
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT docid From procurementPlanner mid "
                    + "         LEFT JOIN voucher_Type vt ON vt.v_Type=mid.v_Type "
                    + "         where vt.category=" + vCatPROC + " and mid.site_Code = :site_Code and mid.v_prefix = :vPrefix ";

            sqlQuery += " Order By mid.v_Date Desc,vt.short_Name,mid.v_No Desc,mid.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                lstNavigator.add(lstNavigatorTmp);
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT	prcpl.docID,prcpl.recID as name,prcpl.v_Date,prcpl.preparedBy  "
                    + "        FROM procurementPlanner prcpl     "
                    + "        LEFT JOIN voucher_Type vt ON vt.v_Type = prcpl.v_Type     "
                    + "        where vt.category='" + vCatPROC + "' and prcpl.site_Code =  :site_Code and prcpl.v_prefix=:v_prefix  ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setPrparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
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
    public DmResult<String> execute(SearchValue_Mdl filterMdl, List<ProcurementPlannerDisplay_Mdl> gridMdl, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", tableName = "";
        long orderIndex;
        FieldUDT_Mdl filterTemp;
        LinkedHashMap<String, FieldUDT_Mdl> filter = new LinkedHashMap<String, FieldUDT_Mdl>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            tableName = getTable(this);
            sqlQuery = "IF OBJECT_ID('tempdb.." + tableName + "') IS NOT NULL "
                    + " DROP TABLE " + tableName + " ;"
                    + " create  table " + tableName + " (orderIndex bigint, departmentCode bigInt, departmentName varchar(160), itemCode bigInt,"
                    + " itemName varchar(160), skuCode  bigInt, skuName varchar(160), currentStkDept float, currentStkSite float,"
                    + " consumptionDept  float, consumptionSite float, surplusDeficitDept float, surplusDeficitSite float,"
                    + " stkRunDaysDept float, stkRunDaysSite float,"
                    + " requiredQtyDept float, requiredQtySite float, pendingPOIndentQty float, recommendedQtyDept float,"
                    + " recommendedQtySite float, finalIndentedQty float, remark varchar(200), "
                    + " requiredDate date, purchaseUnitConversionRate float, purchaseRate float, estimatedCost float, site_Name varchar(100))";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            orderIndex = 0;
            for (int i = 0; i < gridMdl.size(); i++) {
                orderIndex++;
                sqlQuery = " insert into " + tableName + " (orderIndex, departmentCode, departmentName, itemCode,"
                        + " itemName, skuCode, skuName, currentStkDept, currentStkSite,"
                        + " consumptionDept, consumptionSite, surplusDeficitDept, surplusDeficitSite,"
                        + " stkRunDaysDept, stkRunDaysSite,"
                        + " requiredQtyDept, requiredQtySite, pendingPOIndentQty, recommendedQtyDept,"
                        + " recommendedQtySite, finalIndentedQty, remark, "
                        + " requiredDate, purchaseUnitConversionRate, purchaseRate, estimatedCost, site_Name) "
                        + " values (:orderIndex, :departmentCode, :departmentName, :itemCode,"
                        + " :itemName, :skuCode, :skuName, :currentStkDept, :currentStkSite,"
                        + " :consumptionDept, :consumptionSite, :surplusDeficitDept, :surplusDeficitSite,"
                        + " :stkRunDaysDept, :stkRunDaysSite,"
                        + " :requiredQtyDept, :requiredQtySite, :pendingPOIndentQty, :recommendedQtyDept,"
                        + " :recommendedQtySite, :finalIndentedQty, :remark, "
                        + " :requiredDate, :purchaseUnitConversionRate, :purchaseRate, :estimatedCost, :site_Name)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("orderIndex", String.valueOf(orderIndex));
                psTemp.setString("departmentCode", DmShared.iNull(gridMdl.get(i).getDepartmentCode()).toString());
                psTemp.setString("departmentName", DmShared.xNull(gridMdl.get(i).getDepartmentName()));
                psTemp.setString("itemCode", DmShared.iNull(gridMdl.get(i).getItemCode()).toString());
                psTemp.setString("itemName", DmShared.xNull(gridMdl.get(i).getItemName()));
                psTemp.setString("skuCode", DmShared.iNull(gridMdl.get(i).getSkuCode()).toString());
                psTemp.setString("skuName", DmShared.xNull(gridMdl.get(i).getSkuName()));
                psTemp.setString("currentStkDept", DmServer.vNull(gridMdl.get(i).getCurrentStkDept(), "0.00"));
                psTemp.setString("currentStkSite", DmServer.vNull(gridMdl.get(i).getCurrentStkSite(), "0.00"));
                psTemp.setString("consumptionDept", DmServer.vNull(gridMdl.get(i).getConsumptionDept(), "0.00"));
                psTemp.setString("consumptionSite", DmServer.vNull(gridMdl.get(i).getConsumptionSite(), "0.00"));
                psTemp.setString("surplusDeficitDept", DmServer.vNull(gridMdl.get(i).getSurplusDeficitDept(), "0.00"));
                psTemp.setString("surplusDeficitSite", DmServer.vNull(gridMdl.get(i).getSurplusDeficitSite(), "0.00"));
                psTemp.setString("stkRunDaysDept", DmServer.vNull(gridMdl.get(i).getStkRunDaysDept(), "0.00"));
                psTemp.setString("stkRunDaysSite", DmServer.vNull(gridMdl.get(i).getStkRunDaysSite(), "0.00"));
                psTemp.setString("requiredQtyDept", DmServer.vNull(gridMdl.get(i).getRequiredQtyDept(), "0.00"));
                psTemp.setString("requiredQtySite", DmServer.vNull(gridMdl.get(i).getRequiredQtySite(), "0.00"));
                psTemp.setString("pendingPOIndentQty", DmServer.vNull(gridMdl.get(i).getPendingPOIndentQty(), "0.00"));
                psTemp.setString("recommendedQtyDept", DmServer.vNull(gridMdl.get(i).getRecommendedQtyDept(), "0.00"));
                psTemp.setString("recommendedQtySite", DmServer.vNull(gridMdl.get(i).getRecommendedQtySite(), "0.00"));
                psTemp.setString("finalIndentedQty", DmServer.vNull(gridMdl.get(i).getFinalIndentedQty(), "0.00"));
                psTemp.setString("remark", DmShared.xNull(gridMdl.get(i).getRemark()));
                psTemp.setString("requiredDate", DmShared.dNull(gridMdl.get(i).getRequiredDate()));
                psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(gridMdl.get(i).getPurchaseUnitConversionRate(), "0.00"));
                psTemp.setString("purchaseRate", DmServer.vNull(gridMdl.get(i).getPurchaseRate(), "0.00"));
                psTemp.setString("estimatedCost", DmServer.vNull(gridMdl.get(i).getEstimatedCost(), "0.00"));
                psTemp.setString("site_Name", DmServer.getSsnSiteName(this));
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = "SELECT temp.departmentCode, temp.departmentName, temp.itemCode,"
                    + " temp.itemName, temp.skuCode, temp.skuName, temp.currentStkDept, temp.currentStkSite,"
                    + " temp.consumptionDept, temp.consumptionSite, temp.surplusDeficitDept, temp.surplusDeficitSite,"
                    + " temp.stkRunDaysDept, temp.stkRunDaysSite,"
                    + " temp.requiredQtyDept, temp.requiredQtySite, temp.pendingPOIndentQty, temp.recommendedQtyDept,"
                    + " temp.recommendedQtySite, temp.finalIndentedQty, temp.remark, "
                    + " temp.requiredDate, temp.purchaseUnitConversionRate, temp.purchaseRate, temp.estimatedCost, temp.site_Name "
                    + " FROM " + tableName + " AS temp "
                    + " order by temp.orderIndex";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("Pharmacy_ProcurementPlannerDisplay");

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Entry Date");
            filterTemp.setText(filterMdl.getVoucherDate());
            filter.put("entryDate", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Entry No.");
            filterTemp.setText(filterMdl.getSharedKeyFieldName());
            filter.put("entryNo", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Department Name");
            filterTemp.setTag(filterMdl.getDepartmentCode());
            filterTemp.setText(filterMdl.getDepartmentName());
            filter.put("departmentName", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Company Name");
            filterTemp.setTag(filterMdl.getCompanyCode());
            filterTemp.setText(filterMdl.getCompanyName());
            filter.put("companyName", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Group Name");
            filterTemp.setTag(filterMdl.getGroupCode());
            filterTemp.setText(filterMdl.getGroupName());
            filter.put("groupName", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Category Name");
            filterTemp.setTag(filterMdl.getCategoryCode());
            filterTemp.setText(filterMdl.getCategoryName());
            filter.put("categoryName", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Medicine Name");
            filterTemp.setTag(filterMdl.getMedicineCode());
            filterTemp.setText(filterMdl.getMedicineName());
            filter.put("medicineName", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Consumption Days");
            filterTemp.setText(filterMdl.getConsumptionDays());
            filter.put("consumptionDays", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Projection Days");
            filterTemp.setText(filterMdl.getProjectionDays());
            filter.put("projectionDays", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Only Short For Dept");
            filterTemp.setText(filterMdl.isOnlyShortForDept() ? "Yes" : "No");
            filter.put("onlyShortForDept", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Only Short For Site");
            filterTemp.setText(filterMdl.isOnlyShortForSite() ? "Yes" : "No");
            filter.put("onlyShortForSite", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Stock Run Days Dept");
            filterTemp.setText(filterMdl.getStockRunDaysDept());
            filter.put("stockRunDaysDept", filterTemp);

            filterTemp = new FieldUDT_Mdl();
            filterTemp.setFieldCaption("Stock Run Days Site");
            filterTemp.setText(filterMdl.getStockRunDaysSite());
            filter.put("stockRunDaysSite", filterTemp);

            jasperPrint = DmServer.fillJasper(this, "Procurement Planner Display", jasperReport, null, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
            cnTemp.close();

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

    private String getTable(DmRSSPrivate rssPrivate) {
        String sessionName;
        sessionName = (rssPrivate.getLocalRequest().getSession().getId()).replaceAll("-", "_");
        return "##t" + sessionName + "_" + "ProcurementPlannerDisplay";
    }

    private void savetoSql() {
        DmResult<Mdl_Main> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempSave = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Mdl_Main mrTemp = new Mdl_Main();
        dmrTemp.setData(mrTemp);
        BasicDBObject filters;
        DBCursor dBCursor;
        String filterJSON = "", gridJSON = "", planDocID = "";
        List<ProcurementPlannerDisplay_Mdl> gridMdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT pp.docID as code, pp.recID as name , pp.v_No , pp.v_Type,pp.recIdPrefix , pp.v_Prefix, pp.v_Date \n"
                    + " ,pp.indentDocID,ind.recID as indentRecId \n"
                    + " FROM procurementPlanner as pp \n"
                    + " left join indent ind on pp.indentDocID=ind.docID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
//                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                planDocID = DmShared.xNull(rsTemp.getString("code"));
                mrTemp = new Mdl_Main();
                DBCollection dBCollection = DmServer.getMdbCollection(this, "PROCUREMENT_PLAN", false);

                filters = new BasicDBObject();
                filters.put("docID".toLowerCase(), planDocID);

//            orderBydBo.append("logdate".toLowerCase(), -1);
                dBCursor = dBCollection.find(filters);

                if (dBCursor.hasNext()) {
                    DBObject dBObject = dBCursor.next();
                    filterJSON = String.valueOf(dBObject.get("filter_Mdl".toLowerCase()));
                    gridJSON = String.valueOf(dBObject.get("grid_Mdl".toLowerCase()));
                }

                mrTemp.setFilterMdl(DmServer.convertFromJson(filterJSON, new TypeToken<SearchValue_Mdl>() {
                }));

                if (mrTemp.getFilterMdl() != null) {
                    sqlQuery = "update procurementPlanner set "
                            + "departmentName=:departmentName, "
                            + "departmentCode=:departmentCode, "
                            + "medicineName=:medicineName, "
                            + "medicineCode=:medicineCode, "
                            + "categoryName=:categoryName, "
                            + "categoryCode=:categoryCode, "
                            + "companyName=:companyName, "
                            + "companyCode=:companyCode, "
                            + "groupName=:groupName, "
                            + "groupCode=:groupCode, "
                            + "projectionDays=:projectionDays, "
                            + "consumptionDays=:consumptionDays, "
                            + "stockRunDaysDept=:stockRunDaysDept, "
                            + "stockRunDaysSite=:stockRunDaysSite "
                            + " where docId=:docID ";

                    psTempSave = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                    psTempSave.setString("docId", planDocID);

                    psTempSave.setString("departmentName", DmShared.xNull(mrTemp.getFilterMdl().getDepartmentName()));
                    psTempSave.setString("departmentCode", DmShared.xNull(mrTemp.getFilterMdl().getDepartmentCode()));
                    psTempSave.setString("medicineName", DmShared.xNull(mrTemp.getFilterMdl().getMedicineName()));
                    psTempSave.setString("medicineCode", DmShared.xNull(mrTemp.getFilterMdl().getMedicineCode()));
                    psTempSave.setString("categoryName", DmShared.xNull(mrTemp.getFilterMdl().getCategoryName()));
                    psTempSave.setString("categoryCode", DmShared.xNull(mrTemp.getFilterMdl().getCategoryCode()));
                    psTempSave.setString("companyName", DmShared.xNull(mrTemp.getFilterMdl().getCompanyName()));
                    psTempSave.setString("companyCode", DmShared.xNull(mrTemp.getFilterMdl().getCompanyCode()));
                    psTempSave.setString("groupName", DmShared.xNull(mrTemp.getFilterMdl().getGroupName()));
                    psTempSave.setString("groupCode", DmShared.xNull(mrTemp.getFilterMdl().getGroupCode()));
                    psTempSave.setString("projectionDays", DmShared.iNull(mrTemp.getFilterMdl().getProjectionDays()).toString());
                    psTempSave.setString("consumptionDays", DmShared.iNull(mrTemp.getFilterMdl().getConsumptionDays()).toString());
                    psTempSave.setString("stockRunDaysDept", DmShared.iNull(mrTemp.getFilterMdl().getStockRunDaysDept()).toString());
                    psTempSave.setString("stockRunDaysSite", DmShared.iNull(mrTemp.getFilterMdl().getStockRunDaysSite()).toString());

                    psTempSave.executeUpdate();
                    psTempSave.close();

                    mrTemp.setGrdItemDetails(DmServer.convertFromJson(gridJSON, new TypeToken<List<ProcurementPlannerDisplay_Mdl>>() {
                    }));

                    for (ProcurementPlannerDisplay_Mdl node : mrTemp.getGrdItemDetails()) {
                        if (node != null) {
                            sqlQuery = "INSERT INTO procurementPlannerDetail ("
                                    + "     docId, itemCode, skuCode, currentStkDept, currentStkSite, consumptionDept,"
                                    + "     consumptionSite, surplusDeficitDept, surplusDeficitSite, stkRunDaysDept, stkRunDaysSite, requiredQtyDept,"
                                    + "     requiredQtySite, pendingPOIndentQty, recommendedQtyDept,recommendedQtySite,finalIndentedQty,"
                                    + "     remark, requiredDate, purchaseUnitConversionRate,purchaseRate,estimatedCost"
                                    + " ) VALUES ("
                                    + "     :docId, :itemCode, :skuCode, :currentStkDept, :currentStkSite, :consumptionDept,"
                                    + "     :consumptionSite, :surplusDeficitDept, :surplusDeficitSite, :stkRunDaysDept, :stkRunDaysSite, :requiredQtyDept,"
                                    + "     :requiredQtySite, :pendingPOIndentQty, :recommendedQtyDept,:recommendedQtySite,:finalIndentedQty,"
                                    + "     :remark, :requiredDate, :purchaseUnitConversionRate,:purchaseRate,:estimatedCost"
                                    + " )";
                            psTempSave = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTempSave.setString("docId", planDocID);
                            psTempSave.setString("itemCode", DmShared.foriegnKey(node.getItemCode()));
                            psTempSave.setString("skuCode", DmShared.foriegnKey(node.getSkuCode()));

                            psTempSave.setString("currentStkDept", DmServer.vNull(node.getCurrentStkDept()));
                            psTempSave.setString("currentStkSite", DmServer.vNull(node.getCurrentStkSite()));
                            psTempSave.setString("consumptionDept", DmServer.vNull(node.getConsumptionDept()));
                            psTempSave.setString("consumptionSite", DmServer.vNull(node.getConsumptionSite()));
                            psTempSave.setString("surplusDeficitDept", DmServer.vNull(node.getSurplusDeficitDept()));
                            psTempSave.setString("surplusDeficitSite", DmServer.vNull(node.getSurplusDeficitSite()));
                            psTempSave.setString("stkRunDaysDept", DmServer.vNull(node.getStkRunDaysDept()));
                            psTempSave.setString("stkRunDaysSite", DmServer.vNull(node.getStkRunDaysSite()));
                            psTempSave.setString("requiredQtyDept", DmServer.vNull(node.getRequiredQtyDept()));
                            psTempSave.setString("requiredQtySite", DmServer.vNull(node.getRequiredQtySite()));
                            psTempSave.setString("pendingPOIndentQty", DmServer.vNull(node.getPendingPOIndentQty()));
                            psTempSave.setString("recommendedQtyDept", DmServer.vNull(node.getRecommendedQtyDept()));
                            psTempSave.setString("recommendedQtySite", DmServer.vNull(node.getRecommendedQtySite()));
                            psTempSave.setString("finalIndentedQty", DmServer.vNull(node.getFinalIndentedQty()));
                            psTempSave.setString("remark", DmShared.xNull(node.getRemark()));
                            psTempSave.setDate("requiredDate", DmServer.convertDate(node.getRequiredDate()));
                            psTempSave.setString("purchaseUnitConversionRate", DmServer.vNull(node.getPurchaseUnitConversionRate()));
                            psTempSave.setString("purchaseRate", DmServer.vNull(node.getPurchaseRate()));
                            psTempSave.setString("estimatedCost", node.getEstimatedCost());
                            psTempSave.executeUpdate();
                            psTempSave.close();
                        }
                    }
                }
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

    }
}
