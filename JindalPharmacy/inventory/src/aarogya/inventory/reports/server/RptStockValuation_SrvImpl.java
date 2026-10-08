package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptStockValuation_Vld;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import aarogya.inventory.reports.shared.RptStockValuation_Srv;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RptStockValuation_SrvImpl extends DmRSSPrivate implements RptStockValuation_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = "", dateCondition = "";
        //=======================
        FileWriter fileWriter = null;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";
        try {
            RptStockValuation_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                //==================CSV=======================
                String fileName = widgetName.replaceAll(" ", "");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmssSS");
                String dateTimeValue = sdf.format(cal.getTime());

                String directoryPath = DmServer.Path.sessions + "/" + sessionId;
                String serverFileName = fileName + "_" + dateTimeValue + ".csv";
                String fullFilePath = directoryPath + "/" + serverFileName;

                File sFile = new File(directoryPath);
                if (!sFile.exists()) {
                    sFile.mkdirs();
                }
                File nFile = new File(fullFilePath);
                if (nFile.exists()) {
                    nFile.delete();
                }

                keyField.setSharedKeyFieldCode(directoryPath);
                keyField.setSharedKeyFieldName(serverFileName);
                fileWriter = new FileWriter(fullFilePath, true);
            }

            //=============================================
            if (DmShared.xNull(filter.get("inclOp").getText()).trim().equalsIgnoreCase("Yes")) {

                dateCondition = "   <= :toDate  ";
            } else if (DmShared.xNull(filter.get("inclOp").getText()).trim().equalsIgnoreCase("No")) {

                dateCondition = "   between :fromDate and :toDate  ";
            }

            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.Code in (" + DmServer.getParamList(":itemCode", filter.get("item").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup in (" + DmServer.getParamList(":itemGroup", filter.get("itemGroup").getTag()) + " )";
            }

            if (DmShared.xNull(filter.get("itemCat").getText()).trim().equalsIgnoreCase("Material")) {

                sqlQuery = "  select   im.name itemName,max(ic.name) as categoryName, max(ig.name) as groupName, \n"
                        + " format( sum(tmp.openingValue) ,'0.00') openingValue ,\n"
                        + "format( sum(tmp.purchaseValue) ,'0.00') purchaseValue ,format(sum(tmp.purchaseChallan),'0.00') purchaseChallan ,format(sum(tmp.purchaseReturnValue),'0.00')  purchaseReturnValue,\n"
                        + "format(sum(tmp.ProjectConsumptionMaterial),'0.00')  ProjectConsumptionMaterial,\n"
                        + "format(sum(tmp.DurableConsumptionMaterial) ,'0.00')  DurableConsumptionMaterial,format(sum(tmp.DepartmentConsumptionService) ,'0.00')  DepartmentConsumptionService \n"
                        + "\n"
                        + ",format(sum(tmp.DepartmentConsumptionMaterial) ,'0.00') as DepartmentConsumptionMaterial ,\n"
                        + " \n"
                        + "   format(   \n"
                        + "   cast( abs(sum(tmp.openingValue))  as numeric(18,3) )+   \n"
                        + "   cast( abs(sum(tmp.purchaseValue))  as numeric(18,3)) +  cast( abs(sum(tmp.purchaseChallan))  as numeric(18,3))  \n"
                        + "   - cast(abs(sum(tmp.purchaseReturnValue) ) as numeric(18,3)) \n"
                        + "   - cast(abs(sum(tmp.ProjectConsumptionMaterial) ) as numeric(18,3))  - cast(abs(sum(tmp.DurableConsumptionMaterial))as numeric(18,3)) -\n"
                        + "   cast (abs(sum(tmp.DepartmentConsumptionService)) as numeric(18,3)) -cast (abs(sum(tmp.DepartmentConsumptionMaterial)) as numeric(18,3))    ,'0.00') as balanceValue ,\n"
                        + " \n"
                        + " --------\n"
                        + "\n"
                        + "			cast (abs(sum(tmp.openingQty)) as numeric(18,3)) as openingQty ,\n"
                        + "			cast (abs(sum(tmp.purchaseQty)) as numeric(18,3))    as purchaseQty,\n"
                        + "			cast (abs(sum(tmp.purchaseChallanQty)) as numeric(18,3))  as  purchaseChallanQty,\n"
                        + "			cast (abs(sum(tmp.purchaseReturnQty)) as numeric(18,3))   as purchaseReturnQty , \n"
                        + "			cast (abs(sum(tmp.ProjectConsumptionMaterialQty)) as numeric(18,3))  as ProjectConsumptionMaterialQty  , \n"
                        + "			cast (abs(sum(tmp.DurableConsumptionMaterialQty)) as numeric(18,3))  as DurableConsumptionMaterialQty,\n"
                        + "			cast (abs(sum(tmp.DepartmentConsumptionServiceQty)) as numeric(18,3))   as DepartmentConsumptionServiceQty ,\n"
                        + "			cast (abs(sum(tmp.DepartmentConsumptionMaterialQty)) as numeric(18,3))   as DepartmentConsumptionMaterialQty\n"
                        + "\n"
                        + "				,	cast (abs(sum(tmp.openingQty)) as numeric(18,3)) \n"
                        + "			+ cast (abs(sum(tmp.purchaseQty)) as numeric(18,3))   \n"
                        + "			+cast (abs(sum(tmp.purchaseChallanQty)) as numeric(18,3))  \n"
                        + "			-cast (abs(sum(tmp.purchaseReturnQty)) as numeric(18,3))   \n"
                        + "			-cast (abs(sum(tmp.ProjectConsumptionMaterialQty)) as numeric(18,3))   \n"
                        + "			-cast (abs(sum(tmp.DurableConsumptionMaterialQty)) as numeric(18,3))   \n"
                        + "			-cast (abs(sum(tmp.DepartmentConsumptionServiceQty)) as numeric(18,3))   \n"
                        + "			-cast (abs(sum(tmp.DepartmentConsumptionMaterialQty)) as numeric(18,3))   as balanceQty\n"
                        + "from (\n"
                        + "\n"
                        + " \n"
                        + "-------------------------------------1\n"
                        + " select  stk.ibCode,  stk.itemCode, isnull(stk.landedSkuValue,0) as openingValue ,\n"
                        + " \n"
                        + " 0   as purchaseValue, 0 as  purchaseChallan,  0  as purchaseReturnValue , 0 as ProjectConsumptionMaterial  , 0  as DurableConsumptionMaterial, 0  as DepartmentConsumptionService \n"
                        + "\n"
                        + " , 0 as DepartmentConsumptionMaterial \n"
                        + "   ----------\n"
                        + " ,   stk.recQty as openingQty , 0   as purchaseQty,0 as  purchaseChallanQty, 0  as purchaseReturnQty , 0 as ProjectConsumptionMaterialQty  , 0  as DurableConsumptionMaterialQty,\n"
                        + "     0  as DepartmentConsumptionServiceQty ,0  as DepartmentConsumptionMaterialQty \n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "  \n"
                        + "\n"
                        + " and vt.description ='Item Opening' and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + " -----------------------------------------2\n"
                        + " union all \n"
                        + "\n"
                        + " \n"
                        + "select  stk.ibCode,  stk.itemCode, 0 as openingValue ,\n"
                        + " \n"
                        + " isnull(stk.landedSkuValue,0)   as purchaseValue,0 as  purchaseChallan,  0  as purchaseReturnValue , 0 as ProjectConsumptionMaterial  , 0  as DurableConsumptionMaterial, 0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial \n"
                        + "   ----------\n"
                        + " ,   0 as openingQty , stk.recQty   as purchaseQty,0 as  purchaseChallanQty, 0  as purchaseReturnQty , 0 as ProjectConsumptionMaterialQty  , 0  as DurableConsumptionMaterialQty,\n"
                        + "     0  as DepartmentConsumptionServiceQty ,0  as DepartmentConsumptionMaterialQty \n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date  " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Purchase Bill Direct','Purchase Bill Direct (U)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "  ----------------------------------------3\n"
                        + "  union all\n"
                        + "\n"
                        + "  select  stk.ibCode,  stk.itemCode, 0 as openingValue ,\n"
                        + " \n"
                        + " 0   as purchaseValue,isnull(stk.landedSkuValue,0) as  purchaseChallan,  0  as purchaseReturnValue , 0 as ProjectConsumptionMaterial  , 0  as DurableConsumptionMaterial, 0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial \n"
                        + "    ----------\n"
                        + " ,   0 as openingQty , 0   as purchaseQty,stk.recQty as  purchaseChallanQty, 0  as purchaseReturnQty , 0 as ProjectConsumptionMaterialQty  , 0  as DurableConsumptionMaterialQty,\n"
                        + "     0  as DepartmentConsumptionServiceQty ,0  as DepartmentConsumptionMaterialQty \n"
                        + "\n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date  " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Purchase Challan','Purchase Challan (U)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "\n"
                        + "-----------------------------------4\n"
                        + " union all \n"
                        + " \n"
                        + "select  stk.ibCode,  stk.itemCode, 0 as openingValue ,\n"
                        + " \n"
                        + "  0   as purchaseValue,0 as  purchaseChallan, isnull(stk.landedSkuValue,0)  as purchaseReturnValue , 0 as ProjectConsumptionMaterial  , 0  as DurableConsumptionMaterial, 0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial \n"
                        + "    ----------\n"
                        + " ,   0 as openingQty , 0   as purchaseQty,0 as  purchaseChallanQty, stk.recQty as purchaseReturnQty , 0 as ProjectConsumptionMaterialQty  , 0  as DurableConsumptionMaterialQty,\n"
                        + "     0  as DepartmentConsumptionServiceQty ,0  as DepartmentConsumptionMaterialQty \n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date  " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Purchase Return') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "--------------------------------------5\n"
                        + "  union all\n"
                        + "select  stk.ibCode,  stk.itemCode, 0 as openingValue ,\n"
                        + " \n"
                        + "  0   as purchaseValue,0 as  purchaseChallan, 0  as purchaseReturnValue , isnull(stk.landedSkuValue,0)  as ProjectConsumptionMaterial  , 0  as DurableConsumptionMaterial, 0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial \n"
                        + "    ----------\n"
                        + " ,   0 as openingQty , 0   as purchaseQty,0 as  purchaseChallanQty, 0  as purchaseReturnQty , stk.isuQty as ProjectConsumptionMaterialQty  , 0  as DurableConsumptionMaterialQty,\n"
                        + "     0  as DepartmentConsumptionServiceQty ,0  as DepartmentConsumptionMaterialQty \n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date  " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Project Consumption(Material)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "  ---------------------------------------6\n"
                        + "  union all\n"
                        + "\n"
                        + "select   stk.ibCode, stk.itemCode, 0 as openingValue ,\n"
                        + " \n"
                        + "  0   as purchaseValue,0 as  purchaseChallan, 0  as purchaseReturnValue , 0  as ProjectConsumptionMaterial  , isnull(stk.landedSkuValue,0)  as DurableConsumptionMaterial, 0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial \n"
                        + "    ----------\n"
                        + " ,   0 as openingQty , 0   as purchaseQty,0 as  purchaseChallanQty, 0  as purchaseReturnQty , 0 as ProjectConsumptionMaterialQty  , stk.isuQty  as DurableConsumptionMaterialQty,\n"
                        + "     0  as DepartmentConsumptionServiceQty ,0  as DepartmentConsumptionMaterialQty \n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date  " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Durable Consumption(Material)') and vt.nCat   in ('Inventory')\n"
                        + "  \n"
                        + "  -------------------------------7\n"
                        + "\n"
                        + "   union all\n"
                        + "\n"
                        + "select  stk.ibCode,  stk.itemCode, 0 as openingValue ,\n"
                        + " \n"
                        + "  0   as purchaseValue,0 as  purchaseChallan, 0  as purchaseReturnValue , 0  as ProjectConsumptionMaterial  , 0 as DurableConsumptionMaterial, isnull(stk.landedSkuValue,0)   as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial \n"
                        + "    ----------\n"
                        + " ,   0 as openingQty , 0   as purchaseQty,0 as  purchaseChallanQty, 0  as purchaseReturnQty , 0 as ProjectConsumptionMaterialQty  , 0  as DurableConsumptionMaterialQty,\n"
                        + "     stk.isuQty  as DepartmentConsumptionServiceQty ,0  as DepartmentConsumptionMaterialQty \n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date  " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Department Consumption(Service)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "  -------------------------------8\n"
                        + "\n"
                        + "   union all\n"
                        + "\n"
                        + "   select  stk.ibCode,  stk.itemCode, 0 as openingValue ,\n"
                        + " \n"
                        + "  0   as purchaseValue,0 as  purchaseChallan, 0  as purchaseReturnValue , 0 as ProjectConsumptionMaterial  , 0  as DurableConsumptionMaterial, 0  as DepartmentConsumptionService , isnull(stk.landedSkuValue,0)  as DepartmentConsumptionMaterial \n"
                        + "\n"
                        + "  ----------\n"
                        + " ,   0 as openingQty , 0   as purchaseQty,0 as  purchaseChallanQty, 0  as purchaseReturnQty , 0 as ProjectConsumptionMaterialQty  , 0  as DurableConsumptionMaterialQty,\n"
                        + "     0  as DepartmentConsumptionServiceQty ,stk.isuQty  as DepartmentConsumptionMaterialQty \n"
                        + "\n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "       \n"
                        + "\n"
                        + "  and vt.description  in ('Department Consumption(Material)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "\n"
                        + " )  as tmp\n"
                        + "\n"
                        + " left join itemMast as im on im.code=tmp.itemCode\n"
                        + " LEFT JOIN itemCat ic on ic.code=im.itemCategoryCode\n"
                        + " left join subGroup sg on sg.subCode=im.subCode\n"
                        + " left join itemGroup as ig on ig.code=im.itemGroup\n"
                        + "\n"
                        + "\n"
                        + " where im.filterItemType= :filterItemType and ic.name not in ('SERVICE','ASSET') and sg.name='GENERAL INVENTORY A/C'\n"
                        + condition
                        + "  group by im.name  \n"
                        + "  order by im.name    ";
            } else if (DmShared.xNull(filter.get("itemCat").getText()).trim().equalsIgnoreCase("Service")) {

                sqlQuery = " select  \n"
                        + " \n"
                        + " im.name itemName,max(ic.name) as categoryName, max(ig.name) as groupName,\n"
                        + "   \n"
                        + "format( sum(tmp.servicePurchaseValue) ,'0.00') servicePurchaseValue ,format(sum(tmp.ServiceChallan),'0.00') ServiceChallan ,format(sum(tmp.servicePurchaseReturnValue),'0.00')  servicePurchaseReturnValue,\n"
                        + "format(sum(tmp.ProjectConsumptionService),'0.00')  ProjectConsumptionService,\n"
                        + " format(sum(tmp.DepartmentConsumptionService) ,'0.00')  DepartmentConsumptionService \n"
                        + "\n"
                        + ",format(sum(tmp.DepartmentConsumptionMaterial) ,'0.00') as DepartmentConsumptionMaterial  \n"
                        + " \n"
                        + "   , \n"
                        + "   format(cast( abs(sum(tmp.servicePurchaseValue))  as numeric(18,3)) +  cast( abs(sum(tmp.ServiceChallan))  as numeric(18,3))  \n"
                        + "   - cast(abs(sum(tmp.servicePurchaseReturnValue) ) as numeric(18,3)) \n"
                        + "   - cast(abs(sum(tmp.ProjectConsumptionService) ) as numeric(18,3))    -\n"
                        + "   cast (abs(sum(tmp.DepartmentConsumptionService)) as numeric(18,3)) -cast (abs(sum(tmp.DepartmentConsumptionMaterial)) as numeric(18,3))    ,'0.00') as balanceValue ,\n"
                        + " \n"
                        + "   cast(  sum( tmp.servicePurchaseQty ) as numeric(18,3))  servicePurchaseQty, cast(  sum( tmp.ServiceChallanQty) as numeric(18,3))  ServiceChallanQty,  cast(  sum( tmp.servicePurchaseReturnQty) as numeric(18,3))  servicePurchaseReturnQty ,\n"
                        + "  cast(   sum( tmp.ProjectConsumptionServiceQty) as numeric(18,3))  ProjectConsumptionServiceQty ,  cast(  sum( tmp.DepartmentConsumptionServiceQty) as numeric(18,3))  DepartmentConsumptionServiceQty  , \n"
                        + "   cast(  sum( tmp.DepartmentConsumptionMaterialQty) as numeric(18,3))  DepartmentConsumptionMaterialQty,\n"
                        + "\n"
                        + "\n"
                        + "   cast( abs(sum( tmp.servicePurchaseQty ) )  as numeric(18,3)) +cast( abs(sum( tmp.ServiceChallanQty) )  as numeric(18,3)) +  cast( abs(sum( tmp.servicePurchaseReturnQty) )  as numeric(18,3)) +\n"
                        + "  - cast( abs(sum( tmp.ProjectConsumptionServiceQty) )  as numeric(18,3))-cast( abs(sum( tmp.DepartmentConsumptionServiceQty) )  as numeric(18,3))  -\n"
                        + "   cast( abs(sum( tmp.DepartmentConsumptionMaterialQty) )  as numeric(18,3)) as balanceQty\n"
                        + "\n"
                        + "from (\n"
                        + "\n"
                        + " \n"
                        + "-------------------------------------1\n"
                        + " \n"
                        + "select  stk.ibCode,  stk.itemCode, \n"
                        + " \n"
                        + " isnull(stk.landedSkuValue,0)   as servicePurchaseValue,0 as  ServiceChallan,  0  as servicePurchaseReturnValue , 0 as ProjectConsumptionService  ,  0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial ,\n"
                        + "\n"
                        + "  stk.recQty as  servicePurchaseQty,0 as  ServiceChallanQty,  0  as servicePurchaseReturnQty , 0 as ProjectConsumptionServiceQty  ,  0  as DepartmentConsumptionServiceQty , 0 as DepartmentConsumptionMaterialQty\n"
                        + " \n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + " \n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Purchase Bill Direct','Purchase Bill Direct (U)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "  ----------------------------------------2\n"
                        + "  union all\n"
                        + "\n"
                        + "  select  stk.ibCode,  stk.itemCode,  \n"
                        + " \n"
                        + " 0   as servicePurchaseValue,isnull(stk.landedSkuValue,0) as  ServiceChallan,  0  as servicePurchaseReturnValue , 0 as ProjectConsumptionService  ,   0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial ,\n"
                        + "\n"
                        + "  0 as  servicePurchaseQty,   stk.recQty as  ServiceChallanQty,  0  as servicePurchaseReturnQty , 0 as ProjectConsumptionServiceQty  ,  0  as DepartmentConsumptionServiceQty , 0 as DepartmentConsumptionMaterialQty\n"
                        + "  \n"
                        + "\n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Service Challan','Service Challan (U)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "\n"
                        + "-----------------------------------3\n"
                        + " union all \n"
                        + " \n"
                        + "select  stk.ibCode,  stk.itemCode, \n"
                        + " \n"
                        + "  0   as servicePurchaseValue,0 as  ServiceChallan, isnull(stk.landedSkuValue,0)  as servicePurchaseReturnValue , 0 as ProjectConsumptionService  ,  0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial ,\n"
                        + "\n"
                        + "  0 as  servicePurchaseQty,0 as  ServiceChallanQty,    stk.recQty  as servicePurchaseReturnQty , 0 as ProjectConsumptionServiceQty  ,  0  as DepartmentConsumptionServiceQty , 0 as DepartmentConsumptionMaterialQty\n"
                        + "\n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Purchase Return') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "--------------------------------------4\n"
                        + "  union all\n"
                        + "select  stk.ibCode,  stk.itemCode,  \n"
                        + " \n"
                        + "  0   as servicePurchaseValue,0 as  ServiceChallan, 0  as servicePurchaseReturnValue , isnull(stk.landedSkuValue,0)  as ProjectConsumptionService  ,   0  as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial ,\n"
                        + "\n"
                        + "  0 as  servicePurchaseQty,0 as  ServiceChallanQty,  0  as servicePurchaseReturnQty ,  stk.isuQty as ProjectConsumptionServiceQty  ,  0  as DepartmentConsumptionServiceQty , 0 as DepartmentConsumptionMaterialQty\n"
                        + "\n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Project Consumption(Service)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "  ---------------------------------------5\n"
                        + "   \n"
                        + "\n"
                        + "   union all\n"
                        + "\n"
                        + "select  stk.ibCode,  stk.itemCode,  \n"
                        + " \n"
                        + "  0   as servicePurchaseValue,0 as  ServiceChallan, 0  as servicePurchaseReturnValue , 0  as ProjectConsumptionService  ,  isnull(stk.landedSkuValue,0)   as DepartmentConsumptionService , 0 as DepartmentConsumptionMaterial ,\n"
                        + "\n"
                        + "  0 as  servicePurchaseQty,0 as  ServiceChallanQty,  0  as servicePurchaseReturnQty , 0 as ProjectConsumptionServiceQty  ,   stk.isuQty  as DepartmentConsumptionServiceQty , 0 as DepartmentConsumptionMaterialQty\n"
                        + "\n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Department Consumption(Service)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "  ------------------------------6\n"
                        + "\n"
                        + "   union all\n"
                        + "\n"
                        + "   select  stk.ibCode,  stk.itemCode,  \n"
                        + " \n"
                        + "  0   as servicePurchaseValue,0 as  ServiceChallan, 0  as servicePurchaseReturnValue , 0 as ProjectConsumptionService  ,  0  as DepartmentConsumptionService , isnull(stk.landedSkuValue,0)  as DepartmentConsumptionMaterial ,\n"
                        + " \n"
                        + "  0 as  servicePurchaseQty,0 as  ServiceChallanQty,  0  as servicePurchaseReturnQty , 0 as ProjectConsumptionServiceQty  ,  0  as DepartmentConsumptionServiceQty ,  stk.isuQty as DepartmentConsumptionMaterialQty\n"
                        + "\n"
                        + " \n"
                        + "\n"
                        + "from stock stk \n"
                        + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                        + "\n"
                        + "\n"
                        + " where stk.site_Code=:siteCode  and stk.v_Date " + dateCondition + "\n"
                        + "\n"
                        + "  and vt.description  in ('Department Consumption(Material)') and vt.nCat   in ('Inventory')\n"
                        + "\n"
                        + "\n"
                        + " )  as tmp\n"
                        + "\n"
                        + " left join itemMast as im on im.code=tmp.itemCode\n"
                        + " LEFT JOIN itemCat ic on ic.code=im.itemCategoryCode\n"
                        + " \n"
                        + " left join subGroup sg on sg.subCode=im.subCode\n"
                        + " left join itemGroup as ig on ig.code=im.itemGroup\n"
                        + "\n"
                        + "\n"
                        + " where im.filterItemType= :filterItemType  and ic.name   in ('SERVICE') and sg.name='SERVICE INVENTORY A/C'\n"
                        + condition
                        + " group by im.name   ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            //===================================================Export Data to CSV=====================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "";

                if (DmShared.xNull(filter.get("itemCat").getText()).trim().equalsIgnoreCase("Material")) {
                    FILE_HEADER = " Item Name,Category Name, Group Name,  "
                            + "Opening Value ,Purchase Value ,Purchase Challan Value,Purchase Return Value, "
                            + "Project Consumption Material, Durable Consumption Material,  Department Consumption Service  "
                            + ",Department Consumption Material , Balance Value , Opening Qty , Purchase Qty, Ppurchase Challan Qty, "
                            + "Purchase Return Qty ,  ProjectConsumption Material Qty  , DurableConsumption Material Qty,  "
                            + "Department Consumption Service Qty ,  Department Consumption Material Qty,BalanceQty ";
                } else if (DmShared.xNull(filter.get("itemCat").getText()).trim().equalsIgnoreCase("Service")) {
                    FILE_HEADER = "  Item Name ,	Category Name	,Group Name	,Service Purchase Value,	"
                            + "Service Challan Value ,	Service Purchase Return Value ,	Project Consumption Service Value ,	"
                            + "Department Consumption Service Value ,	Department Consumption Material Value ,	Balance Value, "
                            + "	Service Purchase Qty ,	Service Challan Qty	, Service Purchase Return Qty ,	Project Consumption Service Qty ,"
                            + "	Department Consumption Service Qty ,	Department Consumption Material Qty ,	Balance Qty ";
                }
                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {

                    String itemName = rsTemp.getString("itemName");
                    String backSlash = "";
                    if (itemName.contains("\"")) {
                        backSlash = "\"";
                    }

                    lineData = "\"" + DmShared.xNull(rsTemp.getString("itemName")) + backSlash + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("categoryName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("groupName")) + "\"" + COMMA_DELIMITER;

                    if (DmShared.xNull(filter.get("itemCat").getText()).trim().equalsIgnoreCase("Material")) {
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("openingValue")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseValue")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseChallan")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseReturnValue")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("ProjectConsumptionMaterial")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DurableConsumptionMaterial")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionService")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionMaterial")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceValue")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("openingQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseChallanQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseReturnQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("ProjectConsumptionMaterialQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DurableConsumptionMaterialQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionServiceQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionMaterialQty")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceQty")) + "\"" + COMMA_DELIMITER;
                    } else if (DmShared.xNull(filter.get("itemCat").getText()).trim().equalsIgnoreCase("Service")) {

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("servicePurchaseValue")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("ServiceChallan")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("servicePurchaseReturnValue")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("ProjectConsumptionService")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionService")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionMaterial")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceValue")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("servicePurchaseQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("ServiceChallanQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("servicePurchaseReturnQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("ProjectConsumptionServiceQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionServiceQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("DepartmentConsumptionMaterialQty")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceQty")) + "\"" + COMMA_DELIMITER;
                    }

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();
            }
            //=========================================================================================
            rstTemp.setData(keyField);

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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {

            if (DmShared.xNull(filter.get("inclOp").getText()).trim().equalsIgnoreCase("Yes")) {

                if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                    psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
                }
            } else if (DmShared.xNull(filter.get("inclOp").getText()).trim().equalsIgnoreCase("No")) {
                if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                    psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
                }
                if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                    psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
                }
            }

            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(filter.get("item").getTag()));
            }

            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemGroup", DmShared.xNull(filter.get("itemGroup").getTag()));
            }

        } catch (Exception e) {
        }
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
            sqlWithParam.setSQL("SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode"
                    + " FROM itemMast im"
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCat(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Material");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Service");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemGroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
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

}
