package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptStockValuation_Vld;
import aarogya.pharmacy.reports.shared.TxtHItem_Mdl;
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
import aarogya.pharmacy.reports.shared.RptStockValuation_Srv;
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
            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.Code in (" + DmServer.getParamList(":itemCode", filter.get("item").getTag()) + " )";
            }

            sqlQuery = "  declare @date date\n"
                    + " set @date= :toDate \n"
                    + "\n"
                    + "---------------------------------Stock------------------------------------ \n"
                    + "select --cast(im.code as varchar(50))code,\n"
                    + "max(im.name) as medicineName,format(abs(sum(mStk.purchase)) ,'0.00')purchase , format(abs(sum(mStk.purchaseReturn)) ,'0.00')purchaseReturn , \n"
                    + "\n"
                    + "format(abs(sum(mStk.medicineIssue)),'0.00') medicineIssue,format(abs(sum(mStk.sale)) ,'0.00')sale,format(-(sum(mStk.saleReturn)),'0.00') saleReturn , \n"
                    + "\n"
                    + "format( -(sum(mStk.saleReturn))-abs(sum(mStk.medicineIssue))-abs(sum(mStk.sale)) +abs(sum(mStk.purchase))-abs(sum(mStk.purchaseReturn)) , '0.00') balance \n"
                    + "\n"
                    + " , format( abs(sum(mStk.purchaseQty)) ,'0.00')as purchaseQty,format(abs(sum(mStk.purchaseReturnQty)),'0.00') as purchaseReturnQty,format(abs(sum(mStk.saleQty)),'0.00') as saleQty,\n"
                    + " format(abs(sum(mStk.saleReturnQty)),'0.00')as saleReturnQty,format(abs(sum(mStk.medicineIsuQty)) ,'0.00')as medicineIsuQty,\n"
                    + " format( abs(sum(mStk.purchaseQty)) -abs(sum(mStk.purchaseReturnQty))+abs(sum(mStk.saleReturnQty))-abs(sum(mStk.saleQty))-abs(sum(mStk.medicineIsuQty)),'0.00') as balanceQty\n"
                    + "from (\n"
                    + "---1\n"
                    + "select stk.itemcode, stk.grossPurchaseRate as medicineIssue,0 as sale  , 0  as saleReturn ,0 as purchase,0 as purchaseReturn ,\n"
                    + "0 as purchaseQty,0 as purchaseReturnQty,0 as saleQty,0 as saleReturnQty,stk.isuQty as medicineIsuQty \n"
                    + "from stock stk \n"
                    + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                    + " \n"
                    + "where  vt.description='Medicine Issue' and stk.v_Date<=@date and stk.site_Code=:siteCode\n"
                    + "\n"
                    + "  union all\n"
                    + "  --2\n"
                    + "select stk.itemcode, 0 as medicineIssue , stk.grossPurchaseRate as sale ,0  as saleReturn ,0 as purchase,0 as purchaseReturn\n"
                    + " ,0 as purchaseQty,0 as purchaseReturnQty,stk.isuQty as saleQty,0 as saleReturnQty,0 as medicineIsuQty\n"
                    + "from stock stk \n"
                    + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                    + " \n"
                    + "where  vt.parentVoucher='Sale' and stk.v_Date<=@date and stk.site_Code=:siteCode\n"
                    + "\n"
                    + "union all\n"
                    + "--3\n"
                    + "select stk.itemcode, 0 as medicineIssue , 0 as sale , stk.grossPurchaseRate as saleReturn ,0 as purchase,0 as purchaseReturn \n"
                    + " ,0 as purchaseQty,0 as purchaseReturnQty,0 as saleQty,stk.isuQty as saleReturnQty,0 as medicineIsuQty\n"
                    + "from stock stk \n"
                    + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                    + " \n"
                    + "where  vt.parentVoucher='Sale Return' and stk.v_Date<=@date and stk.site_Code=:siteCode\n"
                    + "\n"
                    + "union all\n"
                    + "--4\n"
                    + "select stk.itemcode, 0 as medicineIssue , 0 as sale , 0 as saleReturn ,stk.grossPurchaseRate as purchase,0 as purchaseReturn \n"
                    + " ,stk.recQty as purchaseQty,0 as purchaseReturnQty,0 as saleQty,0 as saleReturnQty,0 as medicineIsuQty\n"
                    + "from stock stk \n"
                    + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                    + " \n"
                    + "where  vt.parentVoucher='purchase' and stk.v_Date<=@date and stk.site_Code=:siteCode\n"
                    + "\n"
                    + "union all\n"
                    + "--5\n"
                    + "select stk.itemcode, 0 as medicineIssue , 0 as sale , 0 as saleReturn ,0 as purchase, stk.grossPurchaseRate as purchaseReturn\n"
                    + " ,0 as purchaseQty,stk.recQty as purchaseReturnQty,0 as saleQty,0 as saleReturnQty,0 as medicineIsuQty\n"
                    + "from stock stk \n"
                    + "left join voucher_Type vt on vt.v_Type=stk.v_Type\n"
                    + " \n"
                    + "where  vt.parentVoucher='purchase Return' and stk.v_Date<=@date and stk.site_Code=:siteCode\n"
                    + " \n"
                    + ") as mStk \n"
                    + "\n"
                    + "left join itemMast im on im.code=mStk.itemCode\n"
                    + "\n"
                    + "where im.filterItemType=11  --and im.name='TAB-NEBILONG'\n"
                    + "\n"
                    + "group by im.code  "
                    + "order by im.code   ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
             
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            //===================================================Export Data to CSV=====================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "";

            
                    FILE_HEADER = " MedicineName,	Purchase Value ,	PurchaseReturn  Value ,	MedicineIssue Value ,	Sale Value ,	SaleReturn Value ,	Balance Value ,	"
                            + "purchaseQty,	purchaseReturnQty,	saleQty	,saleReturnQty,	medicineIsuQty,	balanceQty ";
                
                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {

                    String itemName = rsTemp.getString("medicineName");
                    String backSlash = "";
                    if (itemName.contains("\"")) {
                        backSlash = "\"";
                    }

                    lineData = "\"" + DmShared.xNull(rsTemp.getString("medicineName")) + backSlash + "\"" + COMMA_DELIMITER;
                   
                    
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchase")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("PurchaseReturn")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("MedicineIssue")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("Sale")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("SaleReturn")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("Balance")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseReturnQty")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("saleQty")) + "\"" + COMMA_DELIMITER;

                        lineData += "\"" + DmShared.xNull(rsTemp.getString("saleReturnQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("medicineIsuQty")) + "\"" + COMMA_DELIMITER;
                        lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceQty")) + "\"" + COMMA_DELIMITER;
                        

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

            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(filter.get("item").getTag()));
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
