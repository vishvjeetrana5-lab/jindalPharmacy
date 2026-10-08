package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptProfitabilityReport_Srv;
import aarogya.pharmacy.reports.shared.RptProfitabilityReport_Vld;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.pmTPA;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptProfitabilityReport_SrvImpl extends DmRSSPrivate implements RptProfitabilityReport_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String filterCategory = "'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "','"
            + pvc.PHARMACY_PURCHASE_CHALLAN + "','" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "','" + pvc.MEDICINE_OPENING + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = "", saleType = "", saleTypeReturn = "", returnFilter = "", site = "";
        DmEnum.DmExportType rptExportType = null;
        String fileName = "", directoryPath = "", serverFileName = "", fullFilePath;
        File sFile, nFile;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String filePath = "";
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        int rowIndex = 0;

        try {
            RptProfitabilityReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rptExportType = reportProperty.getExportType();

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")
                    || !DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += "and sb.v_Date between :fromDate and :toDate ";
            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.departmentCode in (" + DmServer.getParamList(":department ", filter.get("department").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.site_Code in (" + DmServer.getParamList(":site ", filter.get("site").getTag()) + " )";
                site += " and stk.site_Code in (" + DmServer.getParamList(":site ", filter.get("site").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemGroup in (" + DmServer.getParamList(":medicineGroup ", filter.get("medicineGroup").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sbd.itemCode in  (" + DmServer.getParamList(":medicine ", filter.get("medicine").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode in  (" + DmServer.getParamList(":medicineCat ", filter.get("medicineCat").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pu.organizationCode IN (" + DmServer.getParamList(":organization", filter.get("organization").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("returnInclude").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.iNull(filter.get("returnInclude").getTag()).intValue() == 1 && DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("Cash Sale")) {
                    returnFilter = "'" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "'";
                } else if (DmShared.iNull(filter.get("returnInclude").getTag()).intValue() == 1 && DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("Credit Sale")) {
                    returnFilter = "'" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "'";
                } else if (DmShared.iNull(filter.get("returnInclude").getTag()).intValue() == 0 && DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("Cash Sale")) {
                    returnFilter = "'" + pvc.PHARMACY_BILL_CASH + "'";
                } else {
                    returnFilter = "'" + pvc.PHARMACY_BILL + "'";
                }
            }

            if (!DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("Cash Sale")) {
                    saleType = "'" + DmShared.xNull(filter.get("saleType").getTag()) + "'";
                    saleTypeReturn = "'PHCSR'";
                } else if (DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("Credit Sale")) {
                    saleType = "'" + DmShared.xNull(filter.get("saleType").getTag()) + "'";
                    saleTypeReturn = "'PHBRT'";
                } else {
                    saleType = "'" + DmShared.xNull(filter.get("saleType").getTag()).replace(",", "','") + "'";
                    saleTypeReturn = "'PHCSR','PHBRT'";
                }

            }

            sqlQuery
                    //<editor-fold defaultstate="collapsed" desc="oldQuery_commented">
                    //----------------------------------------------------------------old Query 20221012  ----------------------------------------- 
                    //                    = "select \n"
                    //                    + "sum(tmp.pMrp) pbdMrp,\n"
                    //                    + "sum(tmp.pbdRate) pbdRate,\n"
                    //                    + "sum(tmp.purchaseValuewithoutTaxWithoutCd) purchaseValuewithoutTaxWithoutCd,\n"
                    //                    + "sum(lessCd) lessCd, \n"
                    //                    + "sum(tmp.purchaseValueWithoutTaxWithCd) purchaseValueWithoutTaxWithCd,\n"
                    //                    + "sum(tmp.purchaseTax2) purchaseTax2,\n"
                    //                    + "sum(tmp.purchaseValueWithGstWithCd) purchaseValueWithGstWithCd,\n"
                    //                    + "sum(tmp.purchaseValuewithoutTaxWithoutCd) + sum(tmp.purchaseTax2) as purchaseValueWithGstWithoutCd,\n"
                    //                    + "sum(tmp.sbdTaxable) sbdTaxable,\n"
                    //                    + "isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0)  as taxOnSale,\n"
                    //                    + "sum(tmp.sbdTaxable) + (isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0)) as newSale,\n"
                    //                    + "\n"
                    //                    + "isNull(sum(tmp.sbdTaxable),0) - isNull(sum(tmp.purchaseValueWithoutTaxWithCd),0) as profitWithoutTaxWithCd2,\n"
                    //                    + "isNull(sum(tmp.sbdTaxable),0) - isNull(sum(tmp.purchaseValuewithoutTaxWithoutCd),0) as profitWithoutTaxWithoutCd2,\n"
                    //                    + "isNull(sum(tmp.sbdTaxable),0) + isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0) - sum(tmp.purchaseValueWithGstWithCd) as profitWithTaxWithCd2,\n"
                    //                    + "isNull(sum(tmp.sbdTaxable),0) + isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0) -(sum(tmp.purchaseValuewithoutTaxWithoutCd) + sum(tmp.purchaseTax2)) as profitWithTaxWithoutCd2,\n"
                    //                    + "\n"
                    //                    + "sum(tmp.sGross) sbGross,sum(tmp.pbTaxable) pbTaxable,\n"
                    //                    + "sum(tmp.sbdTaxable) sbTaxable,"
                    //                    + "max(tmp.itemCat) as itemCat ,max(tmp.itemName) as itemName, max(tmp.itemCode) as itemCode,\n"
                    //                    + "sum(tmp.SaleQty) as SaleQty,\n"
                    //                    + "sum(tmp.purchaseValueForSale) as purchaseValueForSale,\n"
                    //                    + "sum(tmp.SaleValue) as SaleValue,\n"
                    //                    + "max(tmp.itemCatCode) as itemCatCode,\n"
                    //                    + "max(tmp.reportType) as reportType,\n"
                    //                    + "sum(tmp.purchaseValueForSale)-sum(tmp.purchaseValueForReturn) as netPurchaseValue,\n"
                    //                    + "sum(tmp.SaleValue)-sum(tmp.ReturnValue) as netSale,\n"
                    //                    + "((max(tmp.mrp)/max(tmp.purchaseUnitConversionRate))*sum(tmp.SaleQty-tmp.ReturnQty)) as mrp,\n"
                    //                    + "sum(tmp.pGross) pbGross,\n"
                    //                    + "isNull(sum(tmp.pCgst),0) + isNull(sum(tmp.pSgst),0)+ isNull(sum(tmp.pIgst),0) +isNull(sum(tmp.pUgst),0) purchaseTax \n "
                    //                    + " from\n"
                    //                    + "( \n"
                    //                    + "select \n"
                    //                    + "max(pbd.Rate)/ max(pbd.skuQtyForRate) pbdRate,\n"
                    //                    + "isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0) - \n"
                    //                    + "isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0)) as purchaseValuewithoutTaxWithoutCd,\n"
                    //                    + "\n"
                    //                    + "isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0) - \n"
                    //                    + "isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0))  * (isNull(max(pbd.oh_At_Discount),0) / 100)  as lessCd,\n"
                    //                    + "\n"
                    //                    + "(isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0) -\n"
                    //                    + "isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0)))- (isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * \n"
                    //                    + "(isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0)- isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0))  * \n"
                    //                    + "(isNull(max(pbd.oh_At_Discount),0) / 100) ) purchaseValueWithoutTaxWithCd,\n"
                    //                    + "\n"
                    //                    + "(isNull(max(pbd.oh_at_CGST),0)+isNull(max(pbd.oh_at_IGST),0)+isNull(max(pbd.oh_at_SGST),0)+isNull(max(pbd.oh_at_UGST),0)) * \n"
                    //                    + "((isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0) - \n"
                    //                    + "isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0)))- ((isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * \n"
                    //                    + "(isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0)- isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0))  * \n"
                    //                    + "(isNull(max(pbd.oh_At_Discount),0) / 100))  )) / 100 purchaseTax2,\n"
                    //                    + "\n"
                    //                    + "(isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0) - isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0))) + \n"
                    //                    + "((isNull(max(pbd.oh_at_CGST),0)+isNull(max(pbd.oh_at_IGST),0)+isNull(max(pbd.oh_at_SGST),0)+isNull(max(pbd.oh_at_UGST),0)) * \n"
                    //                    + "((isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0) - \n"
                    //                    + "isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0)))- (isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * \n"
                    //                    + "(isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0)- isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0))  * (max(pbd.oh_At_Discount) / 100) )) / 100) \n"
                    //                    + "- (isNull(max(pbd.Rate)/ max(pbd.skuQtyForRate),0) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0)- \n"
                    //                    + "isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0))  * (isNull(max(pbd.oh_At_Discount),0) / 100))  as purchaseValueWithGstWithCd,\n"
                    //                    + "\n"
                    //                    + "max(ic.name) as itemCat,max(im.name) as itemName, max(ic.code) as itemCatCode,max(im.code) as itemCode,\n"
                    //                    + "max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end) as SaleQty,\n"
                    //                    + "max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end) as ReturnQty,\n"
                    //                    + "max(case when vt.category in ('PHBCS') then sbd.oh_Amt_Net else 0 end) as SaleValue,\n"
                    //                    + "max(case when vt.category in ('PHCSR') then sbd.oh_Amt_Net else 0 end) as ReturnValue\n"
                    //                    + ",max((case when vt.category in ('PHBCS') then sbd.quantity else 0 end)*a.skuRate) as purchaseValueForSale\n"
                    //                    + ",max((case when vt.category in ('PHCSR') then sbd.quantity else 0 end)*a.skuRate) as purchaseValueForReturn,\n"
                    //                    + " max(a.skuRate)  as purchaseRate,max(im.purchaseUnitConversionRate) as purchaseUnitConversionRate,\n"
                    //                    + "max('D') as reportType,max(ib.mrp) as mrp,sum(pbd.oh_Amt_Gross) as  pGross,max(pbd.oh_Amt_CGST) pCgst,max(pbd.oh_Amt_SGST) as pSgst,sum(pbd.oh_Amt_Net) as pbNet,\n"
                    //                    + "max(pbd.oh_at_CGST) as oh_at_CGST,max(pbd.oh_at_SGST) as oh_at_SGST,sum(pbd.oh_Amt_Gross) as pbdGross,"
                    //                    + "max(pbd.skuMrp) * (isNull(max(case when vt.category in ('PHBCS') then sbd.quantity else 0 end),0)- isNull(max(case when vt.category in ('PHCSR') then sbd.quantity else 0 end),0)) as pMrp,"
                    //                    + "sum(pbd.oh_Amt_Taxable) as pbTaxable,\n"
                    //</editor-fold>

                    = "select\n"
                    + "isNull(sum(tmp.pMrp),0) pbdMrp,\n"
                    + "sum(tmp.pbdRate) pbdRate,\n"
                    + "sum(tmp.purchaseValuewithoutTaxWithoutCd) purchaseValuewithoutTaxWithoutCd,\n"
                    + "sum(lessCd) lessCd, \n"
                    + "sum(tmp.purchaseValueWithoutTaxWithCd) purchaseValueWithoutTaxWithCd,\n"
                    + "sum(tmp.purchaseTax2) purchaseTax2,\n"
                    + "sum(tmp.purchaseValueWithGstWithCd) purchaseValueWithGstWithCd,\n"
                    + "sum(tmp.purchaseValuewithoutTaxWithoutCd) + sum(tmp.purchaseTax2) as purchaseValueWithGstWithoutCd,\n"
                    + "sum(tmp.sbdTaxable) sbdTaxable,\n"
                    + "isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0)  as taxOnSale,\n"
                    + "sum(tmp.sbdTaxable) + (isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0)) as newSale,\n"
                    + "\n"
                    + "isNull(sum(tmp.sbdTaxable),0) - isNull(sum(tmp.purchaseValueWithoutTaxWithCd),0) as profitWithoutTaxWithCd2,\n"
                    + "isNull(sum(tmp.sbdTaxable),0) - isNull(sum(tmp.purchaseValuewithoutTaxWithoutCd),0) as profitWithoutTaxWithoutCd2,\n"
                    + "isNull(sum(tmp.sbdTaxable),0) + isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0) - sum(tmp.purchaseValueWithGstWithCd) as profitWithTaxWithCd2,\n"
                    + "isNull(sum(tmp.sbdTaxable),0) + isNull(sum(tmp.sCgst),0)  + isNull(sum(tmp.sSgst),0)+ isNull(sum(tmp.sIgst),0) + isNull(sum(tmp.sUgst),0) -(sum(tmp.purchaseValuewithoutTaxWithoutCd) + sum(tmp.purchaseTax2)) as profitWithTaxWithoutCd2,\n"
                    + "\n"
                    + "sum(tmp.sGross) sbGross,\n"
                    + "sum(tmp.pbTaxable) pbTaxable,\n"
                    + "sum(tmp.sbdTaxable) sbTaxable,max(tmp.itemCat) as itemCat ,max(tmp.itemName) as itemName, max(tmp.itemCode) as itemCode,\n"
                    + "max(tmp.itemCatCode) as itemCatCode,\n"
                    + "max(tmp.reportType) as reportType,\n"
                    + "sum(tmp.pGross) pbGross,\n"
                    + "isNull(sum(tmp.pCgst),0) + isNull(sum(tmp.pSgst),0)+ isNull(sum(tmp.pIgst),0) +isNull(sum(tmp.pUgst),0) purchaseTax \n"
                    + "  from(\n"
                    + "\n"
                    + "select tmp2.pbdRate ,\n"
                    + "tmp2.pbdRate * tmp2.netQty as purchaseValuewithoutTaxWithoutCd,\n"
                    + "tmp2.pbdRate* tmp2.netQty * cdPer  as lessCd,\n"
                    + "(tmp2.pbdRate * tmp2.netQty)- (tmp2.pbdRate* tmp2.netQty * cdPer) purchaseValueWithoutTaxWithCd,\n"
                    + "totalGst * ((tmp2.pbdRate * tmp2.netQty)- (tmp2.pbdRate* tmp2.netQty * cdPer)) / 100 purchaseTax2,\n"
                    + "(tmp2.pbdRate * tmp2.netQty)- (tmp2.pbdRate* tmp2.netQty * cdPer) + (totalGst * ((tmp2.pbdRate * tmp2.netQty)- (tmp2.pbdRate* tmp2.netQty * cdPer)) / 100) as purchaseValueWithGstWithCd,\n"
                    + "tmp2.itemCat,tmp2.itemName, tmp2.itemCatCode,tmp2.itemCode,\n"
                    + "tmp2.reportType,tmp2.mrp,tmp2.pGross,tmp2.pCgst,tmp2.pSgst,tmp2.pbNet,\n"
                    + "tmp2.oh_at_CGST,tmp2.oh_at_SGST,tmp2.pbdGross,\n"
                    + "\n"
                    + "tmp2.pbdSkuMrp * tmp2.netQty as pMrp,tmp2.pbTaxable,tmp2.sGross,tmp2.sbdTaxable,tmp2.sCgst,tmp2.sSgst,tmp2.sIgst,\n"
                    + "tmp2.sUgst,tmp2.pIgst,\n"
                    + "tmp2.pUgst,tmp2.oh_at_IGST,tmp2.oh_at_UGST\n"
                    + "\n"
                    + "from(\n"
                    + "select\n"
                    + "\n"
                    + "max(pbd.Rate)/ max(pbd.skuQtyForRate) pbdRate,\n"
                    + "(isNull(max(case when vt.category in (" + saleType + ") then sbd.quantity else 0 end),0) - isNull(max(case when vt.category in (" + saleTypeReturn + ") then sbd.quantity else 0 end),0)) as netQty,\n"
                    + "(isNull(max(pbd.oh_At_Discount),0) / 100) cdPer,\n"
                    + "(isNull(max(pbd.oh_at_CGST),0)+isNull(max(pbd.oh_at_IGST),0)+isNull(max(pbd.oh_at_SGST),0)+isNull(max(pbd.oh_at_UGST),0)) as totalGst,\n"
                    + "max(pbd.skuMrp) as pbdSkuMrp,\n"
                    + "\n"
                    + "max(ic.name) as itemCat,max(im.name) as itemName, max(ic.code) as itemCatCode,max(im.code) as itemCode,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.quantity else 0 end) as SaleQty,\n"
                    + "max(case when vt.category in (" + saleTypeReturn + ") then sbd.quantity else 0 end) as ReturnQty,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.oh_Amt_Net else 0 end) as SaleValue,\n"
                    + "max(case when vt.category in (" + saleTypeReturn + ") then sbd.oh_Amt_Net else 0 end) as ReturnValue\n"
                    + ",max((case when vt.category in (" + saleType + ") then sbd.quantity else 0 end)*a.skuRate) as purchaseValueForSale\n"
                    + ",max((case when vt.category in (" + saleTypeReturn + ") then sbd.quantity else 0 end)*a.skuRate) as purchaseValueForReturn,\n"
                    + " max(a.skuRate)  as purchaseRate,max(im.purchaseUnitConversionRate) as purchaseUnitConversionRate,\n"
                    + "\n"
                    + "'" + DmShared.xNull(filter.get("reportType").getTag()) + "' as reportType,max(ib.mrp) as mrp,sum(pbd.oh_Amt_Gross) as  pGross,max(pbd.oh_Amt_CGST) pCgst,max(pbd.oh_Amt_SGST) as pSgst,sum(pbd.oh_Amt_Net) as pbNet,\n"
                    + "max(pbd.oh_at_CGST) as oh_at_CGST,max(pbd.oh_at_SGST) as oh_at_SGST,sum(pbd.oh_Amt_Gross) as pbdGross,sum(pbd.oh_Amt_Taxable) as pbTaxable,"
                    + "max(sbd.skuMrp) as skuMrp,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.oh_Amt_Gross when vt.category in (" + saleTypeReturn + ") then -sbd.oh_Amt_Gross  end)as sGross,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.oh_Amt_Taxable when vt.category in (" + saleTypeReturn + ") then -sbd.oh_Amt_Taxable  end)as sbdTaxable,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.oh_Amt_CGST when vt.category in (" + saleTypeReturn + ") then -sbd.oh_Amt_CGST  end)as sCgst,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.oh_Amt_SGST when vt.category in (" + saleTypeReturn + ") then -sbd.oh_Amt_SGST  end)as sSgst,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.oh_Amt_IGST when vt.category in (" + saleTypeReturn + ") then -sbd.oh_Amt_IGST  end)as sIgst,\n"
                    + "max(case when vt.category in (" + saleType + ") then sbd.oh_Amt_UGST when vt.category in (" + saleTypeReturn + ") then -sbd.oh_Amt_UGST  end)as sUgst,\n"
                    + "max(sbd.skuQuantity) as  skuQuantity,max(pbd.oh_Amt_IGST) as pIgst,max(pbd.oh_at_IGST) as oh_at_IGST,max(pbd.oh_Amt_UGST) as pUgst,max(pbd.oh_at_UGST) as oh_at_UGST\n"
                    + "from saleBill sb\n"
                    + "left join saleBillDetail sbd on sbd.docid=sb.docid\n"
                    + "left join pBillDetail pbd on pbd.itemCode = sbd.itemCode and pbd.ibCode = sbd.ibCode and  pbd.batchNo = sbd.batchNo\n"
                    + "and pbd.skuMrp = sbd.skuMrp and pbd.skuQtyForRate = sbd.skuQtyForRate\n"
                    + "left join pBill pb on pb.docid = pbd.docid\n"
                    + "left join itemMast im on im.code = sbd.itemCode\n"
                    + " left join " + STServer.getSsnParentDateBase(this) + ".dbo.patientUHID pu on pu.docid = sb.lmCode\n"
                    + " left join " + STServer.getSsnParentDateBase(this) + ".dbo.subGroup sgOrg on sgOrg.subCode = pu.organizationCode \n"
                    + "left join itemBatch ib on sbd.ibCode = ib.code\n"
                    + "left join itemCat ic on ic.code=im.itemCategoryCode\n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "left join (				\n"
                    + "	select sum(stk.landedSkuValue)/sum(stk.recQty) as skuRate,max(stk.ibCode) as ibCode\n"
                    + "	from stock stk\n"
                    + "	left join itemMast im on stk.itemCode=im.code\n"
                    + "	left join voucher_Type vts on vts.v_Type=stk.v_Type\n"
                    + "	where  vts.category in(" + filterCategory + ") \n"
                    + "	and stk.recQty > 0 " + site
                    + "	group by stk.ibCode\n"
                    + "	) a on a.ibCode =sbd.ibCode \n"
                    + "	where sb.isCancelled=0 and vt.category in(" + returnFilter + ")\n"
                    + "	and sbd.isFooter = 0 " + condition
                    + " \n group by sbd.docid,sbd.itemCode,sbd.ibCode"
                    + ""
                    + " ) tmp2	\n"
                    + ")tmp	\n"
                    + " group by itemCode "
                    + " order by itemCat,itemName	";
            //<editor-fold defaultstate="collapsed" desc="oldQuery_commented">
//===================================< Old Query >==============================================================
//            sqlQuery = "select max(tmp.mrp) pbdMrp,\n"
//                    + "max(tmp.itemCat) as itemCat ,max(tmp.itemName) as itemName, max(tmp.itemCode) as itemCode,sum(tmp.SaleQty) as SaleQty,\n"
//                    + "sum(tmp.purchaseValueForSale) as purchaseValueForSale,sum(tmp.SaleValue) as SaleValue,max(tmp.itemCatCode) as itemCatCode,max(tmp.reportType) as reportType,\n"
//                    + "sum(tmp.purchaseValueForSale)-sum(tmp.purchaseValueForReturn) as netPurchaseValue,\n"
//                    + "sum(tmp.SaleValue)-sum(tmp.ReturnValue) as netSale,((max(tmp.mrp)/max(tmp.purchaseUnitConversionRate))*sum(tmp.SaleQty-tmp.ReturnQty)) as mrp,"
//                    + "max(tmp.pGross) pbGross,max(tmp.pCgst) pbCgstAmt,max(tmp.pSgst) pbSgstAmt,max(tmp.pbNet) as pbNet,\n"
//                    + "max(tmp.oh_at_CGST) pbCgst,max(tmp.oh_at_SGST) pbSgst,max(tmp.pbdGross) pbdGross,max(tmp.pMrp) pbMrp,\n"
//                    + "max(tmp.sGross) sbGross,max(tmp.pbTaxable) pbTaxable,max(lessCd) lessCd,\n"
//                    + "max(tmp.sbdTaxable) sbTaxable,max(tmp.sCgst) sbCgst ,max(tmp.sSgst) sbSgst,max(tmp.skuMrp) estMrp,max(tmp.skuQuantity) estQuantity,\n"
//                    + "max(tmp.estGross) estGross,max(lessCd) lessCd  "
//                    + " from\n"
//                    + "("
//                    + "select ic.name as itemCat,im.name as itemName, ic.code as itemCatCode,im.code as itemCode,\n"
//                    + "			case when vt.category in (" + saleType + ") then sbd.quantity else 0 end as SaleQty,\n"
//                    + "			case when vt.category in (" + saleTypeReturn + ") then sbd.quantity else 0 end as ReturnQty,\n"
//                    + "			case when vt.category in (" + saleType + ") then sbd.oh_Amt_Net else 0 end as SaleValue,\n"
//                    + "			case when vt.category in (" + saleTypeReturn + ") then sbd.oh_Amt_Net else 0 end as ReturnValue\n"
//                    + "			,(case when vt.category in (" + saleType + ") then sbd.quantity else 0 end)*a.skuRate as purchaseValueForSale\n"
//                    + "			,(case when vt.category in (" + saleTypeReturn + ") then sbd.quantity else 0 end)*a.skuRate as purchaseValueForReturn,\n"
//                    + " a.skuRate  as purchaseRate,im.purchaseUnitConversionRate,\n"
//                    + "'" + DmShared.xNull(filter.get("reportType").getTag()) + "' as reportType,ib.mrp,"
//                    + "pbd.oh_Amt_Gross as  pGross,pbd.oh_Amt_CGST pCgst,pbd.oh_Amt_SGST as pSgst,pbd.oh_Amt_Net as pbNet,\n"
//                    + "pbd.oh_at_CGST,pbd.oh_at_SGST,pbd.oh_Amt_Gross as pbdGross,pbd.mrp as pMrp,pbd.oh_Amt_Taxable as pbTaxable,pbd.oh_Amt_Discount as lessCd,\n"
//                    + "sbd.oh_Amt_Gross as sGross,\n"
//                    + "sbd.oh_Amt_Taxable sbdTaxable,sbd.oh_Amt_CGST sCgst,sbd.oh_Amt_SGST as sSgst,sbd.skuMrp,sbd.skuQuantity,sbd.oh_Amt_Gross as estGross\n"
//                    + "	from saleBill sb\n"
//                    + "	left join saleBillDetail sbd on sbd.docid=sb.docid\n"
//                    + "left join pBillDetail pbd on pbd.itemCode = sbd.itemCode and pbd.ibCode = sbd.ibCode and  pbd.batchNo = sbd.batchNo\n"
//                    + "and pbd.skuMrp = sbd.skuMrp and pbd.skuQtyForRate = sbd.skuQtyForRate and pbd.skuSaleRate = sbd.skuRate\n"
//                    + "left join pBill pb on pb.docid = pbd.docid\n"
//                    + " left join itemMast im on im.code = sbd.itemCode\n"
//                    + " left join itemBatch ib on sbd.ibCode = ib.code\n"
//                    + " left join itemCat ic on ic.code=im.itemCategoryCode\n"
//                    + "	left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
//                    + "	left join (				\n"
//                    + "	select sum(stk.landedSkuValue)/sum(stk.recQty) as skuRate,max(stk.ibCode) as ibCode\n"
//                    + "	from stock stk\n"
//                    + "	left join itemMast im on stk.itemCode=im.code\n"
//                    + "	left join voucher_Type vts on vts.v_Type=stk.v_Type\n"
//                    + "	where  vts.category in(" + filterCategory + ") \n"
//                    + "	and stk.recQty > 0 " + site
//                    + "	group by stk.ibCode\n"
//                    + "	) a on a.ibCode =sbd.ibCode \n"
//                    + "	where sb.isCancelled=0 and vt.category in(" + returnFilter + ")\n"
//                    + "			and sbd.isFooter = 0 " + condition
//                    + ")tmp	\n"
//                    + " group by itemCode "
//                    + " order by itemCat,itemName	";
            //</editor-fold>
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);         
            rsTemp = psTemp.executeQuery();
            //============================< Excel >=========================================
            if (rptExportType == DmEnum.DmExportType.EXCEL) {
                fileName = widgetName.replace(" ", "");
                if (!DmSession.getSessions().containsKey(sessionId)) {
                    throw new DmException(0, "Invalid request.");
                }
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
                directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId;
                serverFileName = fileName + ".xls";
                fullFilePath = directoryPath + File.separatorChar + serverFileName;

                sFile = new File(directoryPath);
                if (!sFile.exists()) {
                    sFile.mkdirs();
                }
                nFile = new File(fullFilePath);
                if (nFile.exists()) {
                    nFile.delete();
                }
                keyField.setSharedKeyFieldCode(directoryPath);
                keyField.setSharedKeyFieldName(serverFileName);

                filePath = DmBaseServer.Path.sessions + "/" + getLocalRequest().getSession().getId().trim();
                fileName = UUID.randomUUID().toString() + ".xls";
                DmXlsWriter xlsWriter = new DmXlsWriter(directoryPath, serverFileName);
                xlsWriter = xlsWriter.setWorkSheetCreationCallback(new DmXlsWriter.IXlsWriterWorkSheetCreationCallback() {
                    @Override
                    public boolean onWorkSheetCreating(DmXlsSheet workSheet) {
                        return true;
                    }

                    @Override
                    public String getDefaultFontName() {
                        return "Arial";
                    }

                    @Override
                    public int getDefaultFontSize() {
                        return 10;
                    }

                    @Override
                    public Alignment getDefaultHorizontalAlignment() {
                        return Alignment.LEFT;
                    }

                    @Override
                    public VerticalAlignment getDefaultVerticalAlignment() {
                        return VerticalAlignment.CENTRE;
                    }
                }).build(new pmPharmacy().PROFITABILITY_REPORT.caption);

                DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
                workSheet.setDefaultFontName(fileName);
                workSheet.getWorkSheet().getSettings().setShowGridLines(false);

                rowIndex = createXLHeader(workSheet, rowIndex, rsTemp, filter);
                rowIndex += 5;
                rowIndex = createColumnHeader(workSheet, rowIndex, rsTemp);
                rowIndex = createDataRow(workSheet, rowIndex, rsTemp);

                //===========================================<Printed By On>=========================================================
                rowIndex++;
                workSheet.mergeCells(1, rowIndex, 11, rowIndex);

                cellFormat = setFontGeneral(11, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);

                lblHeader = new Label(1, rowIndex, "", cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

                //===========================================<END OF REPORT>=========================================================
                rowIndex++;
                workSheet.mergeCells(1, rowIndex, 11, rowIndex);

                cellFormat = setFontGeneral(11, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);

                lblHeader = new Label(1, rowIndex, "", cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

                xlsWriter.commit();
                xlsWriter.close();

            } //=======================For PDF ===================================//
            else {

//                if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("D")) {
                jasperReport = DmServer.compileJasper("Pharmacy_ProfitabilityReport");
                jasperPrint = DmServer.fillJasper(this, "Profitability Report", jasperReport, null, filter, rsTemp);
                keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
//                } 
//                else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
//                    jasperReport = DmServer.compileJasper("Pharmacy_POReleaseAmountWithCDSummary");
//                    jasperPrint = DmServer.fillJasper(this, "PO Release Amount With CD Summary", jasperReport, null, filter, rsTemp);
//                    keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
//                }

            }

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

    private int createXLHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp, LinkedHashMap<String, FieldUDT_Mdl> filter) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        String filterString = "";
        int colCount = 0;
        try {
            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            colCount = rsTemp.getMetaData().getColumnCount() + 1;
            //===========================================<HOSPITAL SITE  Name> =========================================================
//            rowIndex = 0;
            workSheet.mergeCells(0, rowIndex, colCount, rowIndex);
            workSheet.getWorkSheet().setRowView(rowIndex, 300);

            //============================================< set column width>================================================================
            workSheet.getWorkSheet().setColumnView(0, 3);
            workSheet.getWorkSheet().setColumnView(1, 40);
//            workSheet.getWorkSheet().setColumnView(2, 11);
//            workSheet.getWorkSheet().setColumnView(3, 11);
//            workSheet.getWorkSheet().setColumnView(4, 11);

            lblHeader = new Label(0, rowIndex, DmServer.getSsnSiteDisplayName(this), cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            //===========================================<REPORT NAME>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex);

//            cellFormat = setFontGeneral(11, true, Alignment.LEFT, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            lblHeader = new Label(1, rowIndex, new pmTPA().TPA_IPD_SECURITY_AND_BILABLE_REFUND_REPORT.caption, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);
            //===========================================<Filters>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex + 4);

            if (filter != null) {
                Set<String> keySet = filter.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        FieldUDT_Mdl filter_Mdl = filter.get(key);
                        if (filter_Mdl != null) {
                            if (!DmShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
                                filterString += DmShared.xNull(filter_Mdl.getFieldCaption()) + ":" + DmShared.xNull(filter_Mdl.getText()) + "; ";
                            }
                        }
                    }
                }
            }

            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
            setBorder(cellFormat, false, false, false, false);
            lblHeader = new Label(1, rowIndex, filterString, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private int createColumnHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        try {

            rowIndex++;
            for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                String colName = rsTemp.getMetaData().getColumnName(colIndex);
                int collen = colName.length();
//                if (colName.equalsIgnoreCase("consultant") || colName.equalsIgnoreCase("total")) {
//                    cellFormat = setFontGeneral(true, Alignment.LEFT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
//                } else if (colName.equalsIgnoreCase("total")) {
//                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
//                } else
                {
                    workSheet.getWorkSheet().setColumnView(colIndex, 6 + collen);
                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                }

                setBorder(cellFormat, false, false, true, false);

                lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(colName), cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private void setBorder(WritableCellFormat cellFormat, boolean isTop, boolean isRight, boolean isBottom, boolean isLeft) {
        try {
            if (isBottom) {
                cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            }
            if (isTop) {
                cellFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
            }
            if (isLeft) {
                cellFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
            }
            if (isRight) {
                cellFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

    }

    private int createDataRow(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        jxl.write.Number number;
        jxl.write.DateTime date;
        try {
            while (rsTemp.next()) {

                rowIndex++;
                for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                    String coldata = rsTemp.getString(colIndex);//float//date//varchar
                    cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                    setBorder(cellFormat, false, false, false, false);

                    if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("float")) {

                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } //                    else if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("date")) {
                    //                        date = new jxl.write.DateTime(colIndex++, rowIndex, DmServer.convertDate(DmShared.dNull(coldata)), cellFormat);
                    //                        workSheet.getWorkSheet().addCell(date);
                    //                    }
                    else {
                        lblHeader = new Label(colIndex++, rowIndex, (DmShared.xNull(coldata)), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);
                    }
                    /*
                    if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("consultant")) {

                        cellFormat = setFontGeneral(false, Alignment.LEFT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        setBorder(cellFormat, false, true, false, true);
                        lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);

                    } else if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("Total")) {
                        cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                        setBorder(cellFormat, false, true, false, false);
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);

                    } else {
                        cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        setBorder(cellFormat, false, false, false, false);
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);

                    }
                     */
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    public static WritableCellFormat setFontGeneral(int fontSize, boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

    public static WritableCellFormat setFontGeneral(boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site", DmShared.xNull(filter.get("site").getTag()));
            }

            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }

            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCat", DmShared.xNull(filter.get("medicineCat").getTag()));
            }

            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicine", DmShared.xNull(filter.get("medicine").getTag()));
            }

            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("organization", DmShared.xNull(filter.get("organization").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getOrganization(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select om.subCode, sg.name "
                    + "	from organizationMast om"
                    + "	left join subGroup sg on sg.subCode = om.subCode"
                    + "	left join subGroupNature sgn on sgn.code = sg.nature"
                    + "	where sgn.manualCode ='OR' and sg.isActive = 1");
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
            sqlWithParam.setSQL("select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PH') and sg.isActive = 1 ");
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
            rsTemp.close();
            psTemp.close();

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
            sqlWithParam.setSQL("SELECT "
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode "
                    + " FROM itemMast AS im "
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
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
                    + "FROM itemCat ic"
                    + " WHERE (ic.filterItemType = :filterItemType OR ic.isCommon = 1)");
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
                    + "   WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1)");;
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
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSaleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHBIL");
            listNode.setSharedKeyFieldName("Credit Sale");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHBCS");
            listNode.setSharedKeyFieldName("Cash Sale");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReturnIncluded(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Detail");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Summary");
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

}
