package aarogya.selfToolkit.D365_To_His_DataSync.server;

import aarogya.selfToolkit.D365_To_His_DataSync.shared.GrnHeaderModel;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.GrnLineModel;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.stock.server.Stock_Server;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StockReciveSync {

    public static String saveStockReciveSync(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        try {

            Connection cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc ", cnTempCompany);
            cnTempCompany.close();

            return saveStockReceive(getGrnHeaderLinedetail(stagingCnTemp, transactioDB), cnTemp, stagingCnTemp);

        } catch (Exception e) {
            throw e;
        } finally {
            try {

            } catch (Exception e) {
            }
        }
    }

    public static String saveStockIssueSync(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        try {
            Connection cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc ", cnTempCompany);
            cnTempCompany.close();

            return saveStockIssue(getGrnConsumptionHeaderLinedetail(stagingCnTemp, transactioDB), cnTemp, stagingCnTemp);

        } catch (Exception e) {
            throw e;
        } finally {
            try {

            } catch (Exception e) {
            }
        }
    }

    public static CompareProperties_Mdl iniItemBatchProperties(GrnHeaderModel mrTempCurrent, GrnHeaderModel mrTempPrevious, String siteCode) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();
        lstItemBatch_MdlCurnt.clear();
        for (GrnLineModel detail_MdlCurrent : mrTempCurrent.getLinedata()) {
            ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlCurrent.getDmItemCode()).longValue() != 0) {
                batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getDmItemCode()));
                batch_Mdl.getIpm().setBatchNo(DmShared.xNull(detail_MdlCurrent.getBatchNo()));
                batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(detail_MdlCurrent.getExpiryDate()));
                batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlCurrent.getManufacturingDate()));
                batch_Mdl.getIpm().setMrp(DmShared.xNull(detail_MdlCurrent.getMrp()));
                batch_Mdl.getIpm().setSkuMrp(DmShared.xNull(detail_MdlCurrent.getSkuMrp()));

                batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                batch_Mdl.setSaleRate(DmShared.xNull(detail_MdlCurrent.getSaleRate()));
                batch_Mdl.setSkuSaleRate(DmShared.xNull(detail_MdlCurrent.getSkuSaleRate()));
                batch_Mdl.setStaffSaleRate(DmShared.xNull(detail_MdlCurrent.getStaffSaleRate()));
                batch_Mdl.setSkuStaffSaleRate(DmShared.xNull(detail_MdlCurrent.getSkuStaffSaleRate()));
                batch_Mdl.setReceiveQty(DmShared.xNull(detail_MdlCurrent.getIndentSKUQty()));
                batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getvDate()));

                batch_Mdl.setStockSkuValue(DmServer.vNull(detail_MdlCurrent.getOhAmtNet(), "0.00"));
                batch_Mdl.setStockLandedSkuValue(DmServer.vNull(detail_MdlCurrent.getLandedSkuValue()));
                batch_Mdl.setStockV_Sno(detail_MdlCurrent.getvSNo());
                batch_Mdl.setStockRefChallanNo(DmShared.xNull(mrTempCurrent.getD365PChallanNo()));
                batch_Mdl.setStockRefChallanDate(DmShared.dNull(mrTempCurrent.getD365PChallanDate()));
                batch_Mdl.setBarcode(DmShared.xNull(detail_MdlCurrent.getBarcode()));
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));

                lstItemBatch_MdlCurnt.add(batch_Mdl);
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrnLineModel detail_MdlPrev : mrTempPrevious.getLinedata()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getDmItemCode()).longValue() != 0) {
                    batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getDmItemCode()));
                    batch_MdlPrev.getIpm().setBatchNo(DmShared.xNull(detail_MdlPrev.getBatchNo()));
                    batch_MdlPrev.getIpm().setExpiryDate(DmShared.dNull(detail_MdlPrev.getExpiryDate()));
                    batch_MdlPrev.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlPrev.getManufacturingDate()));
                    batch_MdlPrev.getIpm().setMrp(DmShared.xNull(detail_MdlPrev.getMrp()));
                    batch_MdlPrev.getIpm().setSkuMrp(DmShared.xNull(detail_MdlPrev.getSkuMrp()));

                    batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                    batch_MdlPrev.setSaleRate(DmShared.xNull(detail_MdlPrev.getSaleRate()));
                    batch_MdlPrev.setSkuSaleRate(DmShared.xNull(detail_MdlPrev.getSkuSaleRate()));
                    batch_MdlPrev.setStaffSaleRate(DmShared.xNull(detail_MdlPrev.getStaffSaleRate()));
                    batch_MdlPrev.setSkuStaffSaleRate(DmShared.xNull(detail_MdlPrev.getSkuStaffSaleRate()));
                    batch_MdlPrev.setReceiveQty(DmShared.xNull(detail_MdlPrev.getIndentSKUQty()));
                    batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getvDate()));

                    batch_MdlPrev.setStockSkuValue(DmServer.vNull(detail_MdlPrev.getOhAmtNet(), "0.00"));
                    batch_MdlPrev.setStockLandedSkuValue(DmServer.vNull(detail_MdlPrev.getLandedSkuValue()));
                    batch_MdlPrev.setStockV_Sno(detail_MdlPrev.getvSNo());
                    batch_MdlPrev.setStockRefChallanNo(DmShared.xNull(mrTempPrevious.getD365PChallanNo()));
                    batch_MdlPrev.setStockRefChallanDate(DmShared.dNull(mrTempPrevious.getD365PChallanDate()));
                    batch_MdlPrev.setBarcode(DmShared.xNull(detail_MdlPrev.getBarcode()));
                    batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));

                    lstItemBatch_MdlPrev.add(batch_MdlPrev);
                }
            }
        }

        if (mrTempCurrent != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdGRN().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdGRN().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getvType()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getvDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getSupplierSubCode()));

        } else if (mrTempPrevious != null) { //Delete Case
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempPrevious.getRecIdGRN().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempPrevious.getRecIdGRN().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempPrevious.getvType()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempPrevious.getvDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempPrevious.getDepartmentCode()));

        }
        compareProperties_Mdl.setSite_Code(siteCode);
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getvDate()));
        }
        return compareProperties_Mdl;
    }

    public static List<GrnHeaderModel> getGrnHeaderLinedetail(Connection cnTemp, String transactioDB) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlHeader = "";
        String sqlDetail = "";
        List<GrnHeaderModel> finalList = new ArrayList<>();
        try {
            // ================= HEADER QUERY =================
            sqlHeader = "  SELECT "
                    + "       grn.d365_docid,\n"
                    + "    grn.v_type,\n"
                    + "    grn.v_prefix,\n"
                    + "    grn.v_date,\n"
                    + "    grn.d365_departmentCode,\n"
                    + "    grn.departmentName,\n"
                    + "    grn.d365_supplier_subCode,\n"
                    + "    grn.placeOfSupply,\n"
                    + "    grn.remark,\n"
                    + "    grn.d365_pChallanNo,\n"
                    + "    grn.d365_pChallanDate,\n"
                    + "    grn.oh_Amt_Gross,\n"
                    + "    grn.oh_Amt_Discount,\n"
                    + "    grn.oh_Amt_Taxable,\n"
                    + "    grn.oh_Amt_CGST,\n"
                    + "    grn.oh_Amt_SGST,\n"
                    + "    grn.oh_Amt_IGST,\n"
                    + "    grn.oh_Amt_UGST,\n"
                    + "    grn.oh_Amt_Total,\n"
                    + "    grn.oh_at_FinalDiscount,\n"
                    + "    grn.oh_Amt_FinalDiscount,\n"
                    + "    grn.oh_Amt_RoundOff,\n"
                    + "    grn.oh_Amt_Net,\n"
                    + "    grn.oh_Amt_LandedValue,\n"
                    + "    grn.d365_TimeStamp,\n"
                    + "    grn.preparedBy,\n"
                    + "    grn.preparedDate,\n"
                    + "    grn.approvedBy,\n"
                    + "    grn.approvalDateTime,\n"
                    + "    grn.businessUnitCode,\n"
                    + "    grn.businessUnitName,\n"
                    + "    grn.rcmApplicable,\n"
                    + "    grn.partyType,\n"
                    + "    grn.gsTin,\n"
                    + "    grn.eWayBillNo,\n"
                    + "    grn.eWayBillDt,\n"
                    + "    grn.lrNo,\n"
                    + "    grn.lrDate,\n"
                    + "    grn.GSTlocation "
                    + "FROM D365_GRN_Header grn"
                    + " Left join " + transactioDB + ".dbo.siteMast sm on sm.BusinessUnit collate database_default = grn.businessUnitCode collate database_default "
                    + "  where grn.dm_Status is null and sm.BusinessUnit is not null ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlHeader);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrnHeaderModel header = new GrnHeaderModel();
                header.setD365DocId(DmShared.xNull(rsTemp.getString("d365_docid")));
                header.setvType(DmShared.xNull(rsTemp.getString("v_type")));
                header.setvPrefix(DmShared.xNull(rsTemp.getString("v_prefix")));
                header.setvDate(DmShared.dNull((rsTemp.getDate("v_date"))));

                header.setD365DepartmentCode(DmShared.xNull(rsTemp.getString("d365_departmentCode")));
                header.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                header.setD365SupplierSubCode(DmShared.xNull(rsTemp.getString("d365_supplier_subCode")));
                header.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));

                header.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                header.setD365PChallanNo(DmShared.xNull(rsTemp.getString("d365_pChallanNo")));
                header.setD365PChallanDate(DmShared.dNull((rsTemp.getDate("d365_pChallanDate"))));

                header.setOhAmtGross(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                header.setOhAmtDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                header.setOhAmtTaxable(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));

                header.setOhAmtCGST(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                header.setOhAmtSGST(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                header.setOhAmtUGST(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                header.setOhAmtIGST(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                header.setOhAmtIGST(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));

                header.setOhAmtTotal(DmServer.vNull(rsTemp.getString("oh_Amt_Total"), "0.00"));
                header.setOhAtFinalDiscount(DmServer.vNull(rsTemp.getString("oh_at_FinalDiscount"), "0.00"));
                header.setOhAmtFinalDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_FinalDiscount"), "0.00"));
                header.setOhAmtRoundOff(DmServer.vNull(rsTemp.getString("oh_Amt_RoundOff"), "0.00"));
                header.setOhAmtLandedValue(DmServer.vNull(rsTemp.getString("oh_Amt_LandedValue"), "0.00"));
                header.setOhAmtNet(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                header.setBusinessUnitCode(DmShared.xNull(rsTemp.getString("businessUnitCode")));
                header.setBusinessUnitName(DmShared.xNull(rsTemp.getString("businessUnitName")));

                header.setRcmApplicable(DmShared.xNull(rsTemp.getString("RcmApplicable")));
                header.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                header.setGsTin(DmShared.xNull(rsTemp.getString("GSTIN")));

                header.seteWayBillNo(DmShared.xNull(rsTemp.getString("eWayBillNo")));
                header.seteWayBillDt(DmShared.dNull(rsTemp.getDate("EWayBillDt")));
                header.setLrNo(DmShared.xNull(rsTemp.getString("LrNo")));
                header.setGstLocation(DmShared.xNull(rsTemp.getString("GSTlocation")));
                header.setLrDate(DmShared.dNull(rsTemp.getDate("LrDate")));

                // ================= DETAIL QUERY =================
                sqlDetail = "SELECT \n"
                        + "    D365_DocId,\n"
                        + "    v_SNo,\n"
                        + "    IndentDocId,\n"
                        + "    indentV_SNo,\n"
                        + "    D365_itemCode,\n"
                        + "    dm_ItemCode,\n"
                        + "    itemName,\n"
                        + "    D365_departmentCode,\n"
                        + "   sg.subcode as dm_departmentCode,\n"
                        + "    departmentName,\n"
                        + "    D365_unitCode,\n"
                        + "    D365_hsnCode,\n"
                        + "    indentSKUQty,\n"
                        + "    recSKUQty,\n"
                        + "    rate,\n"
                        + "    oh_Amt_Gross,\n"
                        + "    oh_Amt_Discount,\n"
                        + "    oh_at_Discount,\n"
                        + "    oh_Amt_Taxable,\n"
                        + "    oh_at_CGST,\n"
                        + "    oh_Amt_CGST,\n"
                        + "    oh_at_SGST,\n"
                        + "    oh_Amt_SGST,\n"
                        + "    oh_at_IGST,\n"
                        + "    oh_Amt_IGST,\n"
                        + "    oh_at_UGST,\n"
                        + "    oh_Amt_UGST,\n"
                        + "    oh_at_FinalDiscount,\n"
                        + "    oh_Amt_FinalDiscount,\n"
                        + "    oh_Amt_Net,\n"
                        + "    landedSkuValue,\n"
                        + "    landedSkuRate,\n"
                        + "    dgl.remark,\n"
                        + "    mrp,\n"
                        + "    skuMrp,\n"
                        + "    saleRate,\n"
                        + "    skuSaleRate,\n"
                        + "    staffSaleRate,\n"
                        + "    skuStaffSaleRate,\n"
                        + "    barcode,\n"
                        + "    batchNo,\n"
                        + "    manufacturingDate,\n"
                        + "    expiryDate,\n"
                        + "    dgl.itemGSTNature ,\n"
                        + " um.code as dm_unitCode ,\n"
                        + " hm.code as dm_hsnCode \n"
                        + " FROM D365_GRN_Line dgl "
                        + " Left join " + transactioDB + ".dbo.unitMast um on um.d365_code = dgl.D365_unitCode collate database_default "
                        + " Left join " + transactioDB + ".dbo.hsnmast hm on hm.d365_code = dgl.D365_hsnCode collate database_default "
                        + " Left join " + transactioDB + ".dbo.Subgroup sg on sg.d365_code = dgl.D365_departmentCode collate database_default "
                        + " WHERE D365_DocId = '" + header.getD365DocId() + "'";

                DmPreparedStatement psDetail = DmPreparedStatement.prepareStatement(cnTemp, sqlDetail);
                ResultSet rsDetail = psDetail.executeQuery();
                List<GrnLineModel> lineList = new ArrayList<>();
                while (rsDetail.next()) {
                    GrnLineModel line = new GrnLineModel();

                    line.setD365DocId(rsDetail.getString("D365_DocId"));
                    line.setvSNo(rsDetail.getString("v_SNo"));
                    line.setIndentDocId(rsDetail.getString("IndentDocId"));
                    line.setIndentVSNo(rsDetail.getString("indentV_SNo"));

                    line.setD365ItemCode(rsDetail.getString("D365_itemCode"));
                    line.setDmItemCode(rsDetail.getString("dm_ItemCode"));
                    line.setItemName(rsDetail.getString("itemName"));

                    line.setD365DepartmentCode(rsDetail.getString("D365_departmentCode"));
                    line.setDmDepartmentCode(rsDetail.getString("dm_departmentCode"));
                    line.setDepartmentName(rsDetail.getString("departmentName"));

                    line.setD365UnitCode(rsDetail.getString("D365_unitCode"));
                    line.setDmUnitCode(rsDetail.getString("dm_unitCode"));

                    line.setD365HsnCode(rsDetail.getString("D365_hsnCode"));
                    line.setDmHsnCode(rsDetail.getString("dm_hsnCode"));

//                    line.setIndentSKUQty(rsDetail.getString("indentSKUQty"));
//                    line.setRecSKUQty(rsDetail.getString("recSKUQty"));
                    line.setIndentSKUQty(rsDetail.getString("recSKUQty"));
                    line.setRecSKUQty(rsDetail.getString("indentSKUQty"));

                    line.setRate(rsDetail.getString("rate"));

                    line.setOhAmtGross(rsDetail.getString("oh_Amt_Gross"));
                    line.setOhAmtDiscount(rsDetail.getString("oh_Amt_Discount"));
                    line.setOhAtDiscount(rsDetail.getString("oh_at_Discount"));
                    line.setOhAmtTaxable(rsDetail.getString("oh_Amt_Taxable"));

                    line.setOhAtCGST(rsDetail.getString("oh_at_CGST"));
                    line.setOhAmtCGST(rsDetail.getString("oh_Amt_CGST"));
                    line.setOhAtSGST(rsDetail.getString("oh_at_SGST"));
                    line.setOhAmtSGST(rsDetail.getString("oh_Amt_SGST"));
                    line.setOhAtIGST(rsDetail.getString("oh_at_IGST"));
                    line.setOhAmtIGST(rsDetail.getString("oh_Amt_IGST"));
                    line.setOhAtUGST(rsDetail.getString("oh_at_UGST"));
                    line.setOhAmtUGST(rsDetail.getString("oh_Amt_UGST"));

                    line.setOhAtFinalDiscount(rsDetail.getString("oh_at_FinalDiscount"));
                    line.setOhAmtFinalDiscount(rsDetail.getString("oh_Amt_FinalDiscount"));
                    line.setOhAmtNet(rsDetail.getString("oh_Amt_Net"));

                    line.setLandedSkuValue(rsDetail.getString("landedSkuValue"));
                    line.setLandedSkuRate(rsDetail.getString("landedSkuRate"));

                    line.setRemark(rsDetail.getString("remark"));

                    line.setMrp(rsDetail.getString("mrp"));
                    line.setSkuMrp(rsDetail.getString("skuMrp"));
                    line.setSaleRate(rsDetail.getString("saleRate"));
                    line.setSkuSaleRate(rsDetail.getString("skuSaleRate"));
                    line.setStaffSaleRate(rsDetail.getString("staffSaleRate"));
                    line.setSkuStaffSaleRate(rsDetail.getString("skuStaffSaleRate"));

                    line.setBarcode(rsDetail.getString("barcode"));
                    line.setBatchNo(rsDetail.getString("batchNo"));
                    line.setManufacturingDate(rsDetail.getString("manufacturingDate"));
                    line.setExpiryDate(DmShared.dNull(rsDetail.getDate("expiryDate")));

//                    line.setItemMakeCode(DmShared.xNull(rsDetail.getString("itemMakeCode")));
//                    line.setGstTypeCode(rsDetail.getString("gstTypeCode"));
                    line.setItemGSTNature(rsDetail.getString("itemGSTNature"));
                    lineList.add(line);
                }
                header.setLinedata(lineList);
                finalList.add(header);
                rsDetail.close();
                psDetail.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return finalList;
    }

    public static List<GrnHeaderModel> getGrnConsumptionHeaderLinedetail(Connection cnTemp, String transactioDB) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlHeader = "";
        String sqlDetail = "";
        List<GrnHeaderModel> finalList = new ArrayList<>();
        try {
            // ================= HEADER QUERY =================
            sqlHeader = "  SELECT\n"
                    + "    grn.d365_docid,\n"
                    + "    grn.v_type,\n"
                    + "    grn.v_prefix,\n"
                    + "    grn.v_date,\n"
                    + "    grn.d365_departmentCode,\n"
                    + "    grn.departmentName,\n"
                    + "    grn.d365_supplier_subCode,\n"
                    + "    grn.placeOfSupply,\n"
                    + "    grn.remark,\n"
                    + "    grn.d365_pChallanNo,\n"
                    + "    grn.d365_pChallanDate,\n"
                    + "    grn.oh_Amt_Gross,\n"
                    + "    grn.oh_Amt_Discount,\n"
                    + "    grn.oh_Amt_Taxable,\n"
                    + "    grn.oh_Amt_CGST,\n"
                    + "    grn.oh_Amt_SGST,\n"
                    + "    grn.oh_Amt_IGST,\n"
                    + "    grn.oh_Amt_UGST,\n"
                    + "    grn.oh_Amt_Total,\n"
                    + "    grn.oh_at_FinalDiscount,\n"
                    + "    grn.oh_Amt_FinalDiscount,\n"
                    + "    grn.oh_Amt_RoundOff,\n"
                    + "    grn.oh_Amt_Net,\n"
                    + "    grn.oh_Amt_LandedValue,\n"
                    + "    grn.d365_TimeStamp,\n"
                    + "    grn.preparedBy,\n"
                    + "    grn.preparedDate,\n"
                    + "    grn.approvedBy,\n"
                    + "    grn.approvalDateTime,\n"
                    + "    grn.businessUnitCode,\n"
                    + "    grn.businessUnitName,\n"
                    + "    grn.rcmApplicable,\n"
                    + "    grn.partyType,\n"
                    + "    grn.gsTin,\n"
                    + "    grn.eWayBillNo,\n"
                    + "    grn.eWayBillDt,\n"
                    + "    grn.lrNo,\n"
                    + "    grn.lrDate,\n"
                    + "    grn.GSTlocation \n"
                    + "FROM D365_GRN_ConsumptionHeader grn "
                    + "  Left join " + transactioDB + ".dbo.siteMast sm on sm.BusinessUnit = grn.businessUnitCode collate database_default "
                    + " where grn.dm_Status is null and grn.dm_TimeStamp is null  and sm.BusinessUnit is not null  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlHeader);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrnHeaderModel header = new GrnHeaderModel();
                header.setD365DocId(DmShared.xNull(rsTemp.getString("d365_docid")));
                header.setvType(DmShared.xNull(rsTemp.getString("v_type")));
                header.setvPrefix(DmShared.xNull(rsTemp.getString("v_prefix")));
                header.setvDate(DmShared.dNull((rsTemp.getDate("v_date"))));

                header.setD365DepartmentCode(DmShared.xNull(rsTemp.getString("d365_departmentCode")));
                header.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                header.setD365SupplierSubCode(DmShared.xNull(rsTemp.getString("d365_supplier_subCode")));
                header.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));

                header.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                header.setD365PChallanNo(DmShared.xNull(rsTemp.getString("d365_pChallanNo")));
                header.setD365PChallanDate(DmShared.dNull((rsTemp.getDate("d365_pChallanDate"))));

                header.setOhAmtGross(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                header.setOhAmtDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                header.setOhAmtTaxable(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));

                header.setOhAmtCGST(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                header.setOhAmtSGST(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                header.setOhAmtUGST(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                header.setOhAmtIGST(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                header.setOhAmtIGST(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));

                header.setOhAmtTotal(DmServer.vNull(rsTemp.getString("oh_Amt_Total"), "0.00"));
                header.setOhAtFinalDiscount(DmServer.vNull(rsTemp.getString("oh_at_FinalDiscount"), "0.00"));
                header.setOhAmtFinalDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_FinalDiscount"), "0.00"));
                header.setOhAmtRoundOff(DmServer.vNull(rsTemp.getString("oh_Amt_RoundOff"), "0.00"));
                header.setOhAmtLandedValue(DmServer.vNull(rsTemp.getString("oh_Amt_LandedValue"), "0.00"));
                header.setOhAmtNet(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                header.setBusinessUnitCode(DmShared.xNull(rsTemp.getString("businessUnitCode")));
                header.setBusinessUnitName(DmShared.xNull(rsTemp.getString("businessUnitName")));

                header.setRcmApplicable(DmShared.xNull(rsTemp.getString("RcmApplicable")));
                header.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                header.setGsTin(DmShared.xNull(rsTemp.getString("GSTIN")));

                header.seteWayBillNo(DmShared.xNull(rsTemp.getString("eWayBillNo")));
                header.seteWayBillDt(DmShared.dNull(rsTemp.getDate("EWayBillDt")));
                header.setLrNo(DmShared.xNull(rsTemp.getString("LrNo")));
                header.setGstLocation(DmShared.xNull(rsTemp.getString("GSTlocation")));
                header.setLrDate(DmShared.dNull(rsTemp.getDate("LrDate")));

                // ================= DETAIL QUERY =================
                sqlDetail = "SELECT \n"
                        + "    D365_DocId,\n"
                        + "    v_SNo,\n"
                        + "    D365_itemCode,\n"
                        + "    itemName,\n"
                        + "    D365_departmentCode,\n"
                        + "    sg.subcode as dm_departmentCode,\n"
                        + "    departmentName,\n"
                        + "    D365_unitCode,\n"
                        + "    D365_hsnCode,\n"
                        + "    indentSKUQty,\n"
                        + "    issQty,\n"
                        + "    rate,\n"
                        + "    oh_Amt_Gross,\n"
                        + "    oh_Amt_Discount,\n"
                        + "    oh_at_Discount,\n"
                        + "    oh_Amt_Taxable,\n"
                        + "    oh_at_CGST,\n"
                        + "    oh_Amt_CGST,\n"
                        + "    oh_at_SGST,\n"
                        + "    oh_Amt_SGST,\n"
                        + "    oh_at_IGST,\n"
                        + "    oh_Amt_IGST,\n"
                        + "    oh_at_UGST,\n"
                        + "    oh_Amt_UGST,\n"
                        + "    oh_at_FinalDiscount,\n"
                        + "    oh_Amt_FinalDiscount,\n"
                        + "    oh_Amt_Net,\n"
                        + "    landedSkuValue,\n"
                        + "    landedSkuRate,\n"
                        + "    dgl.remark,\n"
                        + "    mrp,\n"
                        + "    skuMrp,\n"
                        + "    saleRate,\n"
                        + "    skuSaleRate,\n"
                        + "    staffSaleRate,\n"
                        + "    skuStaffSaleRate,\n"
                        + "    barcode,\n"
                        + "    batchNo,\n"
                        + "    manufacturingDate,\n"
                        + "    expiryDate,\n"
                        + "    itemMakeCode,\n"
                        + "    gstTypeCode,\n"
                        + "    dgl.itemGSTNature, \n"
                        + " um.code as dm_unitCode ,\n"
                        + " hm.code as dm_hsnCode \n"
                        + " FROM D365_GRN_ConsumptionLine dgl "
                        + " Left join " + transactioDB + ".dbo.unitMast um on um.d365_code = dgl.D365_unitCode collate database_default "
                        + " Left join " + transactioDB + ".dbo.hsnmast hm on hm.d365_code = dgl.D365_hsnCode collate database_default "
                        + " Left join " + transactioDB + ".dbo.Subgroup sg on sg.d365_code = dgl.D365_departmentCode collate database_default "
                        + " WHERE D365_DocId = '" + header.getD365DocId() + "'";

                DmPreparedStatement psDetail = DmPreparedStatement.prepareStatement(cnTemp, sqlDetail);
                ResultSet rsDetail = psDetail.executeQuery();
                List<GrnLineModel> lineList = new ArrayList<>();
                while (rsDetail.next()) {
                    GrnLineModel line = new GrnLineModel();

                    line.setD365DocId(rsDetail.getString("D365_DocId"));
                    line.setvSNo(rsDetail.getString("v_SNo"));

                    line.setD365ItemCode(rsDetail.getString("D365_itemCode"));
                    line.setItemName(rsDetail.getString("itemName"));

                    line.setD365DepartmentCode(rsDetail.getString("D365_departmentCode"));
                    line.setDmDepartmentCode(rsDetail.getString("dm_departmentCode"));
                    line.setDepartmentName(rsDetail.getString("departmentName"));

                    line.setD365UnitCode(rsDetail.getString("D365_unitCode"));
                    line.setDmUnitCode(rsDetail.getString("dm_unitCode"));
                    line.setD365HsnCode(rsDetail.getString("D365_hsnCode"));
                    line.setDmHsnCode(rsDetail.getString("dm_hsnCode"));
                    line.setIndentSKUQty(rsDetail.getString("indentSKUQty"));
                    line.setRecSKUQty(rsDetail.getString("issQty"));

                    line.setRate(rsDetail.getString("rate"));

                    line.setOhAmtGross(rsDetail.getString("oh_Amt_Gross"));
                    line.setOhAmtDiscount(rsDetail.getString("oh_Amt_Discount"));
                    line.setOhAtDiscount(rsDetail.getString("oh_at_Discount"));
                    line.setOhAmtTaxable(rsDetail.getString("oh_Amt_Taxable"));

                    line.setOhAtCGST(rsDetail.getString("oh_at_CGST"));
                    line.setOhAmtCGST(rsDetail.getString("oh_Amt_CGST"));
                    line.setOhAtSGST(rsDetail.getString("oh_at_SGST"));
                    line.setOhAmtSGST(rsDetail.getString("oh_Amt_SGST"));
                    line.setOhAtIGST(rsDetail.getString("oh_at_IGST"));
                    line.setOhAmtIGST(rsDetail.getString("oh_Amt_IGST"));
                    line.setOhAtUGST(rsDetail.getString("oh_at_UGST"));
                    line.setOhAmtUGST(rsDetail.getString("oh_Amt_UGST"));

                    line.setOhAtFinalDiscount(rsDetail.getString("oh_at_FinalDiscount"));
                    line.setOhAmtFinalDiscount(rsDetail.getString("oh_Amt_FinalDiscount"));
                    line.setOhAmtNet(rsDetail.getString("oh_Amt_Net"));

                    line.setLandedSkuValue(rsDetail.getString("landedSkuValue"));
                    line.setLandedSkuRate(rsDetail.getString("landedSkuRate"));

                    line.setRemark(rsDetail.getString("remark"));

                    line.setMrp(rsDetail.getString("mrp"));
                    line.setSkuMrp(rsDetail.getString("skuMrp"));
                    line.setSaleRate(rsDetail.getString("saleRate"));
                    line.setSkuSaleRate(rsDetail.getString("skuSaleRate"));
                    line.setStaffSaleRate(rsDetail.getString("staffSaleRate"));
                    line.setSkuStaffSaleRate(rsDetail.getString("skuStaffSaleRate"));

                    line.setBarcode(rsDetail.getString("barcode"));
                    line.setBatchNo(rsDetail.getString("batchNo"));
                    line.setManufacturingDate(rsDetail.getString("manufacturingDate"));
                    line.setExpiryDate(DmShared.dNull(rsDetail.getDate("expiryDate")));

                    line.setItemMakeCode(DmShared.xNull(rsDetail.getString("itemMakeCode")));
                    line.setGstTypeCode(rsDetail.getString("gstTypeCode"));
                    line.setItemGSTNature(rsDetail.getString("itemGSTNature"));
                    lineList.add(line);
                }
                header.setLinedata(lineList);
                finalList.add(header);
                rsDetail.close();
                psDetail.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return finalList;
    }

    private static void saveChallanHeader(GrnHeaderModel detail_Mdl, Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            detail_Mdl.setD365vType(detail_Mdl.getvType());

            detail_Mdl.setPlaceOfSupplyCode(DmServer.executeScalar(
                    "select code From stateMast where textCode = '" + detail_Mdl.getPlaceOfSupply() + "'", cnTemp));
            detail_Mdl.setSiteCode(DmServer.executeScalar(
                    "select code from siteMast where businessUnit = '" + detail_Mdl.getBusinessUnitCode() + "'", cnTemp));
            detail_Mdl.setDepartmentCode(DmServer.executeScalar(
                    "select SubCode from subgroup where d365_code = '" + detail_Mdl.getD365DepartmentCode() + "'", cnTemp));
            detail_Mdl.setSupplierSubCode(DmServer.executeScalar(
                    "select SubCode from subgroup where d365_code = '" + detail_Mdl.getD365SupplierSubCode() + "'", cnTemp));
            detail_Mdl.setvType(DmServer.executeScalar(
                    "select v_Type from voucher_Type where description = '" + detail_Mdl.getvType() + "'", cnTemp));

            if (DmShared.xNull(detail_Mdl.getvDate()).equals("")) {
                throw new Exception("Voucher Date cannot be blank.");
            }
            if (DmShared.convertDate(DmShared.dNull(DmServer.getServerDateTime()[0])).getTime() < DmShared.convertDate(DmShared.dNull(detail_Mdl.getvDate())).getTime()) {
                throw new Exception("Voucher Date cannot be a future date.");
            }
            if (DmShared.vNull(detail_Mdl.getOhAmtNet()).equals(0)) {
                throw new Exception("Net Amount can not blank.");
            }
            if (DmShared.xNull(detail_Mdl.getD365PChallanDate()).equals("")) {
                throw new Exception("Party Bill Date can not blank.");
            }
            if (DmShared.xNull(detail_Mdl.getD365PChallanNo()).equals("")) {
                throw new Exception("Party Bill No. can not blank.");
            }

            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                detail_Mdl.setSharedKeyFieldCode(DmServer.getDocId(
                        DmShared.xNull(detail_Mdl.getvType()), detail_Mdl.getvPrefix(), detail_Mdl.getSiteCode(), cnTemp));
                detail_Mdl.setRecIdGRN(DmServer.getRecId("pChallan", "docId",
                        DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()), "recId", detail_Mdl.getRecIdGRN(),
                        detail_Mdl.getvDate(), "", DmShared.xNull(detail_Mdl.getvType()), detail_Mdl.getvPrefix(),
                        detail_Mdl.getSiteCode(), "", "", cnTemp, true, null));
                detail_Mdl.setSharedKeyFieldName(DmShared.xNull(detail_Mdl.getRecIdGRN().getRecIdValue()));
            }

            sqlQuery = "INSERT INTO pChallan ( \n"
                    + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, \n"
                    + "     pChallanNo, pChallanDate, subCode, consignee, partyType, gsTin, gstinDate, \n"
                    + "     rcmApplicable, placeOfSupply, remark, departmentcode,\n"
                    + "     site_Code, preparedBy, preparedDate, u_EntDt_LatestLine, transferDt, \n"
                    + "     EWayBillNo, EWayBillDt, LRNo, LRDate, \n"
                    + "     oh_Amt_GrossGoods, oh_Amt_GrossService, oh_Amt_Gross, oh_Amt_Discount, oh_Amt_TaxableGoods, oh_Amt_TaxableService, \n"
                    + "     oh_Amt_Taxable, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_CGST, oh_Amt_IGST, \n"
                    + "     oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Total, \n"
                    + "     oh_Amt_RoundOff, oh_Amt_Net, oh_Amt_LandedValue, locationCode) \n"
                    + " VALUES ( \n"
                    + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, \n"
                    + "     :pChallanNo, :pChallanDate, :subCode, :consignee, :partyType, :gsTin, :gstinDate, \n"
                    + "     :rcmApplicable, :placeOfSupply, :remark,:departmentcode, \n"
                    + "     :site_Code, :preparedBy, :preparedDate, :uEntDt, :transferDt, \n"
                    + "     :EWayBillNo, :EWayBillDt, :LRNo, :LRDate, \n"
                    + "     :oh_Amt_GrossGoods, :oh_Amt_GrossService, :oh_Amt_Gross, :oh_Amt_Discount, :oh_Amt_TaxableGoods, :oh_Amt_TaxableService, \n"
                    + "     :oh_Amt_Taxable, :oh_Amt_SGST, :oh_Amt_UGST, :oh_Amt_CGST, :oh_Amt_IGST, \n"
                    + "     :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Total, \n"
                    + "     :oh_Amt_RoundOff, :oh_Amt_Net, :oh_Amt_LandedValue, :locationCode) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            psTemp.setString("recId", DmShared.xNull(detail_Mdl.getSharedKeyFieldName()));
            psTemp.setString("recIdPrefix", DmShared.xNull(detail_Mdl.getRecIdGRN().getPrefix()));
            psTemp.setString("v_Type", DmShared.foriegnKey(detail_Mdl.getvType()));
            psTemp.setString("v_No", DmShared.iNull(detail_Mdl.getRecIdGRN().getCounter()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(detail_Mdl.getvDate()));
            psTemp.setString("v_Prefix", detail_Mdl.getvPrefix());
            psTemp.setString("pChallanNo", DmShared.xNull(detail_Mdl.getD365PChallanNo()));
            psTemp.setDate("pChallanDate", DmServer.convertDate(detail_Mdl.getD365PChallanDate()));
            psTemp.setString("subCode", DmShared.foriegnKey(detail_Mdl.getSupplierSubCode()));
            psTemp.setString("consignee", DmShared.foriegnKey(detail_Mdl.getSupplierSubCode()));
            psTemp.setString("partyType", DmShared.xNull(detail_Mdl.getPartyType()).substring(0, 1));
            psTemp.setString("gsTin", DmShared.xNull(detail_Mdl.getGsTin()));
            psTemp.setDate("gstinDate", DmServer.convertDate(detail_Mdl.getvDate()));
            psTemp.setString("rcmApplicable", DmShared.iNull(detail_Mdl.getRcmApplicable()).toString());
            psTemp.setString("placeOfSupply", DmShared.foriegnKey(detail_Mdl.getPlaceOfSupplyCode()));
            psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
            psTemp.setString("departmentCode", DmShared.foriegnKey(detail_Mdl.getDepartmentCode()));
            psTemp.setString("site_Code", DmShared.foriegnKey(detail_Mdl.getSiteCode()));
            psTemp.setString("preparedby", DmShared.xNull("Scheduler"));
            psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("EWayBillNo", DmShared.xNull(detail_Mdl.geteWayBillNo()));
            psTemp.setDate("EWayBillDt", DmServer.convertDate(detail_Mdl.geteWayBillDt()));
            psTemp.setString("LRNo", DmShared.xNull(detail_Mdl.getLrNo()));
            psTemp.setDate("LRDate", DmServer.convertDate(detail_Mdl.getLrDate()));
            psTemp.setString("oh_Amt_GrossGoods", DmServer.vNull(detail_Mdl.getOhAmtGross(), "0.00"));
            psTemp.setString("oh_Amt_GrossService", DmServer.vNull(0, "0.00"));
            psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getOhAmtGross(), "0.00"));
            psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getOhAmtDiscount(), "0.00"));
            psTemp.setString("oh_Amt_TaxableGoods", DmServer.vNull(detail_Mdl.getOhAmtTaxable(), "0.00"));
            psTemp.setString("oh_Amt_TaxableService", DmServer.vNull(0, "0.00"));
            psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getOhAmtTaxable(), "0.00"));
            psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getOhAmtSGST(), "0.00"));
            psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getOhAmtUGST(), "0.00"));
            psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getOhAmtCGST(), "0.00"));
            psTemp.setString("oh_Amt_IGST", DmServer.vNull(detail_Mdl.getOhAmtIGST(), "0.00"));
            psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(detail_Mdl.getOhAmtTotal(), "0.00"));
            psTemp.setString("oh_at_FinalDiscount", DmServer.vNull(detail_Mdl.getOhAtFinalDiscount(), "0.00"));
            psTemp.setString("oh_Amt_FinalDiscount", DmServer.vNull(detail_Mdl.getOhAmtFinalDiscount(), "0.00"));
            psTemp.setString("oh_Amt_Total", DmServer.vNull(detail_Mdl.getOhAmtTotal(), "0.00"));
            psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(detail_Mdl.getOhAmtRoundOff(), "0.00"));
            psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getOhAmtNet(), "0.00"));
            psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(detail_Mdl.getOhAmtLandedValue(), "0.00"));
            psTemp.setString("locationCode", DmShared.foriegnKey(detail_Mdl.getGstLocation()));
            psTemp.setString("transferDt", null);

            psTemp.executeUpdate();
            psTemp.close();

            String updateTableName = "";
            if (DmShared.xNull(detail_Mdl.getD365vType()).equalsIgnoreCase("D365 Purchase Return") || DmShared.xNull(detail_Mdl.getD365vType()).equalsIgnoreCase("D365 PH Purchase Return") || DmShared.xNull(detail_Mdl.getD365vType()).equalsIgnoreCase("D365Wholesale")) {
                updateTableName = "D365_GRN_ConsumptionHeader";
            } else {
                updateTableName = "D365_GRN_Header";
            }

            String sqlQueryupdt = "UPDATE  " + updateTableName + "  "
                    + " SET dm_TimeStamp = GETDATE() , "
                    + " dm_Status = :dm_Status ,dm_docid = :dm_docid "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQueryupdt);
            psTemp.setString("docId", DmShared.xNull(detail_Mdl.getD365DocId()));
            psTemp.setString("dm_Status", DmShared.xNull("Success"));
            psTemp.setString("dm_docid", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From pChallanDetail Where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {

            try {

                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ignored) {
            }

        }
    }

    private static void syncIndentLineStatus(GrnHeaderModel detail_Mdl, GrnLineModel itemList, Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String indentLineStatus = "";

        try {
            sqlQuery = "select status from D365_IndentLineStatus "
                    + "where docid = :docid and v_SNo= :v_SNo "
                    + "and itemCode = :itemCode and BusinessUnitCode = :BusinessUnitCode ";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            psTemp.setString("v_SNo", DmShared.xNull(itemList.getvSNo()));
            psTemp.setString("itemCode", DmShared.xNull(itemList.getDmItemCode()));
            psTemp.setString("BusinessUnitCode", DmShared.xNull(detail_Mdl.getBusinessUnitCode()));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                indentLineStatus = DmShared.xNull(rsTemp.getString("status"));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "update id set"
                    + " id.DynIndent_Status = :DynIndent_Status "
                    + " from indentDetail id "
                    + " left join indent i on i.docid = id.docid "
                    + " left join sitemast sm on sm.code = i.site_code "
                    + " where id.docid = :docid and id.v_SNo= :v_SNo "
                    + " and id.itemCode = :itemCode and sm.BusinessUnit = :BusinessUnitCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DynIndent_Status", DmShared.xNull(indentLineStatus));
            psTemp.setString("docId", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            psTemp.setString("v_SNo", DmShared.xNull(itemList.getvSNo()));
            psTemp.setString("BusinessUnitCode", DmShared.xNull(detail_Mdl.getBusinessUnitCode()));
            psTemp.setString("itemCode", DmShared.xNull(itemList.getDmItemCode()));
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception ex) {

        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    private static void saveChallanLines(GrnHeaderModel detail_Mdl, Connection cnTemp, Connection stagingCnTemp, boolean syncIndent, boolean saveStock, String ncat) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            for (GrnLineModel itemList : detail_Mdl.getLinedata()) {

                itemList.setDmItemCode(DmServer.executeScalar(
                        "select code from itemmast where d365_code = '" + itemList.getD365ItemCode() + "'", cnTemp));

                String purchaseUnitConversionRate = DmServer.executeScalar(
                        " select purchaseUnitConversionRate from itemmast where d365_code = '" + itemList.getD365ItemCode() + "'", cnTemp);

                if (DmShared.iNull(itemList.getDmItemCode()).longValue() == 0) {
                    throw new Exception("Item is not Sync.");
                }
                if (DmShared.iNull(itemList.getDmDepartmentCode()).longValue() == 0) {
                    throw new Exception("Department is not Sync.");
                }
                if (DmShared.iNull(itemList.getDmUnitCode()).longValue() == 0) {
                    throw new Exception("Unit is not Sync.");
                }
                if (DmShared.xNull(itemList.getBatchNo()).equals("")
                        && DmShared.xNull(ncat).equals("Pharmacy")) {
                    throw new Exception("Batch No. can not blank.");
                }
                if (DmShared.xNull(itemList.getExpiryDate()).equals("")
                        && DmShared.xNull(ncat).equals("Pharmacy")) {
                    throw new Exception("Expiry Date can not blank.");
                }
                if (DmShared.vNull(itemList.getMrp()).equals(0)
                        && DmShared.xNull(ncat).equals("Pharmacy")) {
                    throw new Exception("MRP can not blank.");
                }
                if (DmShared.vNull(itemList.getSaleRate()).equals(0)
                        && DmShared.xNull(ncat).equals("Pharmacy")) {
                    throw new Exception("Sale Rate can not blank.");
                }
                if (DmShared.vNull(itemList.getRate()).equals(0)
                        && DmShared.xNull(ncat).equals("Pharmacy")) {
                    throw new Exception("Rate can not blank.");
                }
                if (DmShared.vNull(itemList.getRecSKUQty()).equals(0)) {
                    throw new Exception("Accept Qty can not blank.");
                }
                if (DmShared.vNull(itemList.getIndentSKUQty()).equals(0)) {
                    throw new Exception("Sku Qty can not blank.");
                }
                if (DmShared.vNull(itemList.getOhAmtNet()).equals(0)) {
                    throw new Exception("Amount can not blank.");
                }

                sqlQuery = "INSERT INTO pChallanDetail ("
                        + "     docId, v_SNo,  itemCode, departmentCode, hsnCode, itemGSTNature, batchNo, manufacturingDate, expiryDate,\n"
                        + "      mrp, skuMrp, unitCode, challanQty, recQty, acceptQty, skuQty, rate,\n"
                        + "     landedSkuValue, landedSkuRate, skuQtyForRate, skuRate, remark, isStatus,\n"
                        + "     oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                        + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                        + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Net\n"
                        + "     ,saleRate,skusalerate,staffsalerate,skustaffsalerate)\n"
                        + " Values (\n"
                        + "     :docId, :v_SNo, :itemCode, :departmentCode, :hsnCode, :itemGSTNature, :batchNo, :manufacturingDate, :expiryDate,\n"
                        + "      :mrp, :skuMrp, :unitCode, :challanQty, :recQty, :acceptQty, :skuQty, :rate,\n"
                        + "     :landedSkuValue, :landedSkuRate, :skuQtyForRate, :skuRate, :remark, 0,\n"
                        + "     :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                        + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                        + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Net\n"
                        + "     ,:saleRate,:skusalerate,:staffsalerate,:skustaffsalerate)\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                psTemp.setString("v_SNo", DmShared.xNull(itemList.getvSNo()));
//                psTemp.setString("poDocId", DmShared.foriegnKey(itemList.getIndentDocId()));
//                psTemp.setString("poV_SNo", DmShared.foriegnKey(itemList.getIndentVSNo()));
                psTemp.setString("itemCode", DmShared.foriegnKey(itemList.getDmItemCode()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(itemList.getDmDepartmentCode()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(itemList.getDmHsnCode()));
                psTemp.setString("itemGSTNature", DmShared.xNull(itemList.getItemGSTNature()));
                psTemp.setString("batchNo", DmShared.xNull(itemList.getBatchNo()));
                psTemp.setDate("manufacturingDate", DmServer.convertDate(itemList.getManufacturingDate()));
                psTemp.setDate("expiryDate", DmServer.convertDate(itemList.getExpiryDate()));
//                psTemp.setString("itemMakeCode", DmShared.foriegnKey(itemList.getItemMakeCode()));
                psTemp.setString("mrp", DmServer.vNull(itemList.getMrp(), "0.00"));
                psTemp.setString("skuMrp", DmServer.vNull(itemList.getSkuMrp()));
                psTemp.setString("unitCode", DmShared.foriegnKey(itemList.getDmUnitCode()));
                psTemp.setString("challanQty", DmServer.vNull(itemList.getRecSKUQty()));
                psTemp.setString("recQty", DmServer.vNull(itemList.getRecSKUQty()));
                psTemp.setString("acceptQty", DmServer.vNull(itemList.getRecSKUQty()));
                psTemp.setString("skuQty", DmServer.vNull(itemList.getIndentSKUQty()));
                psTemp.setString("rate", DmServer.vNull(itemList.getRate(), "0.00"));
                psTemp.setString("landedSkuValue", DmShared.vNull(itemList.getLandedSkuValue()).toString());
                psTemp.setString("landedSkuRate", DmServer.vNull(itemList.getLandedSkuRate()));
                psTemp.setString("skuQtyForRate", DmServer.vNull(purchaseUnitConversionRate));
                psTemp.setString("skuRate", DmServer.vNull(itemList.getSkuSaleRate()));
                psTemp.setString("remark", DmShared.xNull(itemList.getRemark()));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(itemList.getOhAmtGross(), "0.00"));
                psTemp.setString("oh_at_Discount", DmServer.vNull(itemList.getOhAtDiscount(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(itemList.getOhAmtDiscount(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(itemList.getOhAmtTaxable(), "0.00"));
                psTemp.setString("oh_at_SGST", DmServer.vNull(itemList.getOhAtSGST(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(itemList.getOhAmtSGST(), "0.00"));
                psTemp.setString("oh_at_UGST", DmServer.vNull(itemList.getOhAtUGST(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(itemList.getOhAmtUGST(), "0.00"));
                psTemp.setString("oh_at_CGST", DmServer.vNull(itemList.getOhAtCGST(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(itemList.getOhAmtCGST(), "0.00"));
                psTemp.setString("oh_at_IGST", DmServer.vNull(itemList.getOhAtIGST(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(itemList.getOhAmtIGST(), "0.00"));
                psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(itemList.getOhAmtNet(), "0.00"));
                psTemp.setString("oh_at_FinalDiscount", DmServer.vNull(itemList.getOhAtFinalDiscount(), "0.00"));
                psTemp.setString("oh_Amt_FinalDiscount", DmServer.vNull(itemList.getOhAmtFinalDiscount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(itemList.getOhAmtNet(), "0.00"));
                psTemp.setString("salerate", DmServer.vNull(itemList.getSaleRate(), "0.00"));
                psTemp.setString("skusalerate", DmServer.vNull(itemList.getSkuSaleRate(), "0.00"));
                psTemp.setString("staffsalerate", DmServer.vNull(itemList.getStaffSaleRate(), "0.00"));
                psTemp.setString("skustaffsalerate", DmServer.vNull(itemList.getSkuStaffSaleRate(), "0.00"));
//                psTemp.setString("gstTypeCode", DmShared.foriegnKey(itemList.getGstTypeCode()));
                psTemp.executeUpdate();
                psTemp.close();

                String updateTableName = "";
                if (DmShared.xNull(detail_Mdl.getD365vType()).equalsIgnoreCase("D365 Purchase Return") || DmShared.xNull(detail_Mdl.getD365vType()).equalsIgnoreCase("D365 PH Purchase Return") || DmShared.xNull(detail_Mdl.getD365vType()).equalsIgnoreCase("D365Wholesale")) {
                    updateTableName = "D365_GRN_ConsumptionLine";
                } else {
                    updateTableName = "D365_GRN_Line";
                }

                String sqlQueryupdt = "UPDATE  " + updateTableName + "  "
                        + " SET dm_TimeStamp = GETDATE() , "
                        + " dm_Status = :dm_Status ,dm_docid = :dm_docid "
                        + " WHERE D365_DocId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQueryupdt);
                psTemp.setString("docId", DmShared.xNull(detail_Mdl.getD365DocId()));
                psTemp.setString("dm_Status", DmShared.xNull("Success"));
                psTemp.setString("dm_docid", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();

                if (syncIndent) {
                    syncIndentLineStatus(detail_Mdl, itemList, cnTemp, stagingCnTemp);
                }

                if (saveStock) {
                    /*----------------------------------------------------------Stock Saving--------------------------------------------------------------*/
                    ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
                    batch_Mdl.getIpm().setItemCode(DmShared.xNull(itemList.getDmItemCode()));
                    batch_Mdl.setBarcode(DmShared.xNull(itemList.getBarcode()));
                    batch_Mdl.getIpm().setBatchNo(DmShared.xNull(itemList.getBatchNo()));
                    batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(itemList.getManufacturingDate()));
                    batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(itemList.getExpiryDate()));
                    batch_Mdl.getIpm().setMrp(DmShared.xNull(itemList.getMrp()));
                    batch_Mdl.getIpm().setSkuMrp(DmShared.xNull(itemList.getSkuMrp()));
                    batch_Mdl.setCode(DmShared.xNull(itemList.getIbCode()));
                    batch_Mdl.setSaleRate(DmShared.xNull(itemList.getSaleRate()));
                    batch_Mdl.setSkuSaleRate(DmShared.xNull(itemList.getSkuSaleRate()));
                    batch_Mdl.setStaffSaleRate(DmShared.xNull(itemList.getStaffSaleRate()));
                    batch_Mdl.setSkuStaffSaleRate(DmShared.xNull(itemList.getSkuStaffSaleRate()));
                    batch_Mdl.setReceiveQty(DmShared.xNull(itemList.getIndentSKUQty()));

                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    stock_Mdl.setSharedKeyFieldName(itemList.getvSNo());
                    stock_Mdl.setSite_Code(DmShared.xNull(detail_Mdl.getSiteCode()));
                    stock_Mdl.setV_Type(DmShared.xNull(detail_Mdl.getvType()));
                    stock_Mdl.setV_No(DmShared.iNull(detail_Mdl.getRecIdGRN().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(detail_Mdl.getRecIdGRN().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(detail_Mdl.getvDate()));
                    stock_Mdl.setRefChallanNo(DmShared.xNull(detail_Mdl.getD365PChallanNo()));
                    stock_Mdl.setRefChallanDate(DmShared.dNull(detail_Mdl.getD365PChallanDate()));
                    stock_Mdl.setDepartmentCode(DmShared.xNull(detail_Mdl.getDepartmentCode()));
                    stock_Mdl.setStockContraSubCode(DmShared.xNull(detail_Mdl.getSupplierSubCode()));
                    stock_Mdl.setRemark(DmShared.xNull(detail_Mdl.getRemark()) + "\n" + DmShared.xNull(detail_Mdl.getRemark()));
                    stock_Mdl.setItemCode(DmShared.xNull(itemList.getDmItemCode()));
                    stock_Mdl.setIsuQty(DmServer.vNull(itemList.getIndentSKUQty()));
                    stock_Mdl.setIbCode(DmShared.xNull(ItemBatch_Server.getItemBatchForAutoIssue(batch_Mdl,
                            stock_Mdl.getSite_Code(), stock_Mdl.getDepartmentCode(), stock_Mdl.getIsuQty(), cnTemp)));

                    Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.OTHER, cnTemp);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (psTemp != null) {
                try {
                    psTemp.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static void finalizeStockAndBatch(GrnHeaderModel detail_Mdl, Connection cnTemp, String nCat) throws Exception {
        DmPreparedStatement psTemp = null;
        try {
//            ItemBatch_Server.compareProperties(
//                    iniItemBatchProperties(detail_Mdl, detail_Mdl, detail_Mdl.getSiteCode()), cnTemp,
//                    detail_Mdl.getSiteCode(), detail_Mdl.getvPrefix());

            ItemBatch_Server.compareProperties(DmShared.xNull(nCat).equals("Pharmacy")
                    ? iniItemBatchProperties(detail_Mdl, detail_Mdl, detail_Mdl.getSiteCode())
                    : iniItemBatchPropertiesForInventory(detail_Mdl, detail_Mdl, detail_Mdl.getSiteCode()), cnTemp,
                    detail_Mdl.getSiteCode(), detail_Mdl.getvPrefix());

            /*==============================<< Update ibCode From Stock Table >>====================================*/
            String sqlQuery = "UPDATE pChallanDetail"
                    + " SET pChallanDetail.ibCode = stock.ibCode"
                    + " FROM stock"
                    + " WHERE stock.docid = pChallanDetail.docId"
                    + " AND stock.v_SNo = pChallanDetail.v_SNo"
                    + " AND pChallanDetail.docid = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
        } finally {
            if (psTemp != null) {
                try {
                    psTemp.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static CompareProperties_Mdl iniItemBatchPropertiesForInventory(GrnHeaderModel mrTempCurrent, GrnHeaderModel mrTempPrevious, String siteCode) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();
        lstItemBatch_MdlCurnt.clear();
        for (GrnLineModel detail_MdlCurrent : mrTempCurrent.getLinedata()) {
            ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlCurrent.getDmItemCode()).longValue() > 0) {
                batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getDmItemCode()));
                batch_Mdl.getIpm().setBatchNo("");
                batch_Mdl.getIpm().setLotNo(DmShared.xNull(""));
                batch_Mdl.getIpm().setMrp("");
                batch_Mdl.getIpm().setSkuMrp("");
                batch_Mdl.getIpm().setColourCode(DmShared.xNull(""));
                batch_Mdl.getIpm().setSizeCode(DmShared.xNull(""));
                batch_Mdl.getIpm().setItemMakeCode("");
                batch_Mdl.setCode("");
                batch_Mdl.setReceiveQty(DmShared.xNull(detail_MdlCurrent.getIndentSKUQty()));
                batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getvDate()));

                batch_Mdl.setStockSkuValue(DmServer.vNull(detail_MdlCurrent.getOhAmtNet(), "0.00"));
                batch_Mdl.setStockLandedSkuValue(DmServer.vNull(detail_MdlCurrent.getLandedSkuValue()));
                batch_Mdl.setStockV_Sno(detail_MdlCurrent.getvSNo());
                batch_Mdl.setStockRefChallanNo(DmShared.xNull(mrTempCurrent.getD365PChallanNo()));
                batch_Mdl.setStockRefChallanDate(DmShared.dNull(mrTempCurrent.getD365PChallanDate()));
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));

                lstItemBatch_MdlCurnt.add(batch_Mdl);

            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrnLineModel detail_MdlPrev : mrTempPrevious.getLinedata()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getDmItemCode()).longValue() > 0) {

                    batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getDmItemCode()));
                    batch_MdlPrev.getIpm().setBatchNo("");
                    batch_MdlPrev.getIpm().setLotNo(DmShared.xNull(""));
                    batch_MdlPrev.getIpm().setMrp("");
                    batch_MdlPrev.getIpm().setSkuMrp("");
                    batch_MdlPrev.getIpm().setColourCode(DmShared.xNull(""));
                    batch_MdlPrev.getIpm().setSizeCode(DmShared.xNull(""));
                    batch_MdlPrev.getIpm().setItemMakeCode("");
                    batch_MdlPrev.setCode("");
                    batch_MdlPrev.setReceiveQty(DmShared.xNull(detail_MdlPrev.getIndentSKUQty()));
                    batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getvDate()));

                    batch_MdlPrev.setStockSkuValue(DmServer.vNull(detail_MdlPrev.getOhAmtNet(), "0.00"));
                    batch_MdlPrev.setStockLandedSkuValue(DmServer.vNull(detail_MdlPrev.getLandedSkuValue()));
                    batch_MdlPrev.setStockV_Sno(detail_MdlPrev.getvSNo());
                    batch_MdlPrev.setStockRefChallanNo(DmShared.xNull(mrTempPrevious.getD365PChallanNo()));
                    batch_MdlPrev.setStockRefChallanDate(DmShared.dNull(mrTempPrevious.getD365PChallanDate()));
                    batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempPrevious.getDepartmentCode()));

                    lstItemBatch_MdlPrev.add(batch_MdlPrev);

                }
            }
        }

        if (mrTempCurrent != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdGRN().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdGRN().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getvType()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getvDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getSupplierSubCode()));

        } else if (mrTempPrevious != null) { //Delete Case
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempPrevious.getRecIdGRN().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempPrevious.getRecIdGRN().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempPrevious.getvType()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempPrevious.getvDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempPrevious.getSupplierSubCode()));

        }
        compareProperties_Mdl.setSite_Code(siteCode);
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getvDate()));
        }
        return compareProperties_Mdl;
    }

    private static void updateGrnReceiveErrorStatus(Connection stagingCnTemp, String docId, String errorMsg) throws Exception {
        DmPreparedStatement psTemp = null;
        try {
            String sqlHeader = "UPDATE D365_GRN_Header "
                    + " SET dm_errorMsg = :errorMsg "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlHeader);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("errorMsg", DmShared.xNull(errorMsg));
            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;

            String sqlLine = "UPDATE D365_GRN_Line "
                    + " SET dm_errorMsg = :errorMsg "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlLine);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("errorMsg", DmShared.xNull(errorMsg));
            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;

            String sqlHeader1 = "UPDATE D365_GRN_Header "
                    + " SET dm_status = null , dm_docid=null,dm_TimeStamp=null "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlHeader1);
            psTemp.setString("docId", DmShared.xNull(docId));

            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;

            String sqlLine1 = "UPDATE D365_GRN_Line "
                    + " SET dm_status = null , dm_docid=null , dm_TimeStamp=null "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlLine1);
            psTemp.setString("docId", DmShared.xNull(docId));

            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;
        } finally {
            if (psTemp != null) {
                try {
                    psTemp.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static void updateGrnIssueErrorStatus(List<GrnHeaderModel> mrTempCurrent, Connection stagingCnTemp, String docId, String errorMsg) throws Exception {
        DmPreparedStatement psTemp = null;
        try {
            String sqlHeader = "UPDATE D365_GRN_ConsumptionHeader "
                    + " SET dm_errorMsg = :errorMsg "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlHeader);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("errorMsg", DmShared.xNull(errorMsg));
            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;

            String sqlLine = "UPDATE D365_GRN_ConsumptionLine "
                    + " SET dm_errorMsg = :errorMsg "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlLine);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("errorMsg", DmShared.xNull(errorMsg));
            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;

//            if (mrTempCurrent != null) {
//                for (GrnHeaderModel grnHeaderModel : mrTempCurrent) {
            String sqlHeader1 = "UPDATE D365_GRN_ConsumptionHeader "
                    + " SET dm_status = null , dm_docid=null,dm_TimeStamp=null "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlHeader1);
            psTemp.setString("docId", DmShared.xNull(docId));

            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;

            String sqlLine1 = "UPDATE D365_GRN_ConsumptionLine "
                    + " SET dm_status = null , dm_docid=null , dm_TimeStamp=null "
                    + " WHERE D365_DocId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlLine1);
            psTemp.setString("docId", DmShared.xNull(docId));

            psTemp.executeUpdate();
            psTemp.close();
            psTemp = null;

//                }
//            }
        } finally {
            if (psTemp != null) {
                try {
                    psTemp.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static String saveStockReceive(List<GrnHeaderModel> mrTempCurrent, Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;

        StringBuilder failedDocIds = new StringBuilder();
        for (GrnHeaderModel detail_Mdl : mrTempCurrent) {
            java.sql.Savepoint spHeader = null;
            String ncat = "";
            try {
                cnTemp.setAutoCommit(false);
                spHeader = cnTemp.setSavepoint();
                ncat = DmServer.executeScalar(
                        "select ncat from voucher_Type where description = '" + detail_Mdl.getvType() + "'", cnTemp);

                saveChallanHeader(detail_Mdl, cnTemp, stagingCnTemp);
                saveChallanLines(detail_Mdl, cnTemp, stagingCnTemp, false, false, ncat);
                finalizeStockAndBatch(detail_Mdl, cnTemp, ncat);
                /*==============================<< Success: clear errorMsg >>====================================*/
//                updateGrnReceiveErrorStatus(stagingCnTemp, DmShared.xNull(detail_Mdl.getD365DocId()), "");
            } catch (Exception e) {
                try {
                    if (spHeader != null) {
                        cnTemp.rollback(spHeader);
                    }
                } catch (Exception rollbackEx) {
                }
                String docId = DmShared.xNull(detail_Mdl.getD365DocId());
                /*==============================<< Failure: record errorMsg >>====================================*/
                updateGrnReceiveErrorStatus(stagingCnTemp, DmShared.xNull(detail_Mdl.getD365DocId()), DmShared.xNull(e.getMessage()));
                if (failedDocIds.length() > 0) {
                    failedDocIds.append(",");
                }
                failedDocIds.append(docId);

            }
            cnTemp.commit();
        }

        return failedDocIds.toString();

    }

    public static String saveStockIssue(List<GrnHeaderModel> mrTempCurrent, Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        StringBuilder failedDocIds = new StringBuilder();
        for (GrnHeaderModel detail_Mdl : mrTempCurrent) {
            java.sql.Savepoint spHeader = null;
            String ncat = "";
            try {
                cnTemp.setAutoCommit(false);
                spHeader = cnTemp.setSavepoint();
                ncat = DmServer.executeScalar(
                        "select ncat from voucher_Type where description = '" + detail_Mdl.getvType() + "'", cnTemp);
                saveChallanHeader(detail_Mdl, cnTemp, stagingCnTemp);
                saveChallanLines(detail_Mdl, cnTemp, stagingCnTemp, false, true, ncat);

                /*==============================<< Success: clear errorMsg >>====================================*/
//                updateGrnIssueErrorStatus(null, stagingCnTemp, DmShared.xNull(detail_Mdl.getD365DocId()), "");
            } catch (Exception e) {
                try {
                    if (spHeader != null) {
                        cnTemp.rollback(spHeader);
                    }
                } catch (Exception rollbackEx) {
                }
                String docId = DmShared.xNull(detail_Mdl.getD365DocId());
                /*==============================<< Failure: record errorMsg >>====================================*/
                updateGrnIssueErrorStatus(mrTempCurrent, stagingCnTemp, DmShared.xNull(detail_Mdl.getD365DocId()), DmShared.xNull(e.getMessage()));
                if (failedDocIds.length() > 0) {
                    failedDocIds.append(",");
                }
                failedDocIds.append(docId);
            }
            cnTemp.commit();
        }
        return failedDocIds.toString();

    }

}
