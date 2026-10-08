package aarogya.master.priceListMaster.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import aarogya.master.priceListMaster.shared.DefaultValue_Mdl;
import aarogya.master.priceListMaster.shared.Find_Mdl;
import aarogya.master.priceListMaster.shared.GrdItemDetail_Mdl;
import aarogya.master.priceListMaster.shared.PriceListMaster_Srv;
import aarogya.master.priceListMaster.shared.PriceListMaster_Vld;
import aarogya.master.priceListMaster.shared.PriceList_Mdl;
import aarogya.master.priceListMaster.shared.SearchValue_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.ItemPriceList_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListEntryFrom;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListType;

public class PriceListMaster_SrvImpl extends DmRSSPrivate implements PriceListMaster_Srv {

    @Override
    public DmResultPageing<List<PriceList_Mdl>> getTxtHPriceList(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<PriceList_Mdl>> rstTemp = new DmResultPageing<>();
        List<PriceList_Mdl> lstTemp = new ArrayList<>();
        PriceList_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT name, code, priceListType, applicableOn "
                    + " FROM priceListMast ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new PriceList_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setPriceListTypeTag(DmShared.xNull(rsTemp.getString("priceListType")));
                if (rsTemp.getString("priceListType").equalsIgnoreCase("1")) {
                    listNode.setPriceListTypeName(DmShared.xNull(STEnum.PriceListType.FIX.getValue()));
                }
                listNode.setApplicableOn(DmShared.xNull(rsTemp.getString("applicableOn")));
                if (DmShared.xNull(rsTemp.getString("applicableOn")).trim().equalsIgnoreCase("1")) {
                    listNode.setApplicableOnName(DmShared.xNull(STEnum.PriceListApplicableOn.PHARMACY.getValue()));
                } else if (DmShared.xNull(rsTemp.getString("applicableOn")).trim().equalsIgnoreCase("2")) {
                    listNode.setApplicableOnName(DmShared.xNull(STEnum.PriceListApplicableOn.INVENTORY.getValue()));
                } else {
                    listNode.setApplicableOnName(DmShared.xNull(STEnum.PriceListApplicableOn.OTHER.getValue()));
                }

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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        try {
            mrTemp.setSiteCode(DmServer.getSsnSiteCode(this));
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHApplicableOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);
        try {
            for (STEnum.PriceListApplicableOn plan : STEnum.PriceListApplicableOn.values()) {

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(plan.getId()).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(plan.getValue()));
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

    class CSV_Mdl {

        private String itemName, itemCode, unitCode, unitName,
                rate, discount, discountPercentage, priceListName;

        public CSV_Mdl() {
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDiscountPercentage() {
            return discountPercentage;
        }

        public void setDiscountPercentage(String discountPercentage) {
            this.discountPercentage = discountPercentage;
        }

        public String getPriceListName() {
            return priceListName;
        }

        public void setPriceListName(String priceListName) {
            this.priceListName = priceListName;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + Objects.hashCode(this.itemName);
            hash = 37 * hash + Objects.hashCode(this.unitName);
            hash = 37 * hash + Objects.hashCode(this.priceListName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CSV_Mdl other = (CSV_Mdl) obj;
            if (!Objects.equals(this.itemName, other.itemName)) {
                return false;
            }
            if (!Objects.equals(this.unitName, other.unitName)) {
                return false;
            }
            if (!Objects.equals(this.priceListName, other.priceListName)) {
                return false;
            }
            return true;
        }

    }

    @Override
    public DmResult<String> applyDiscountPercent(double percentValue, SearchValue_Mdl param) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            PriceListMaster_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(param.getRateListCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "update priceList set discountPer = :discountPer "
                    + " where (entryFrom = :entryFrom) "
                    + "and (entryFromNature = 0) "
                    + "and (site_code = :siteCode) "
                    + "and (applicableFrom = :applicableDate) "
                    + "and (priceListcode = :priceListcode) "
                    + "and (ISNULL(rate,0) > 0) "
                    + "and (ISNULL(discount,0) = 0) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("applicableDate", DmServer.convertDate(param.getApplicableDate()));
            psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
            psTemp.setInt("priceListcode", DmShared.iNull(param.getRateListCode()).intValue());
            psTemp.setDouble("discountPer", percentValue);
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(param.getRateListCode());
            spBegin.commit();
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
    public DmResult<String> savePriceList(PriceList_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        dmrTemp.setData("");
        try {

            //===========================<< CHECK VALIDATION >>===================================================
            DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "Name", 100);
            DmShared.isValidText(DmShared.xNull(mrTemp.getPriceListTypeTag()), "Price List Type", 200);
//            DmShared.isValidText(DmShared.xNull(mrTemp.getApplicableOn()), "List Type", 300);

            //  =======================================================================================================
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select  count(*) as cnt "
                    + "         from priceListMast "
                    + "         where Name =:name "
                    + " and priceListType= :priceListType";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("priceListType", DmShared.xNull(mrTemp.getPriceListTypeTag()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(100, "PriceList already exist.");
                }
            }
            //===========================<< INSERT to priceListMast >>===================================================
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "priceListMast", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            sqlQuery = "Insert Into priceListMast(code, name, priceListType, applicableOn, preparedBy, preparedDt) Values  "
                    + "(:code,:name,:priceListType "
                    + ", :applicableOn, :preparedBy,:preparedDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("priceListType", DmShared.xNull(mrTemp.getPriceListTypeTag()));
            psTemp.setString("applicableOn", DmShared.foriegnKey(mrTemp.getApplicableOn()));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("preparedDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            dmrTemp.setValue1(mrTemp.getApplicableOn());
            spBegin.commit();
            dmrTemp.setStatus(true);
            //  =======================================================================================================
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
    public DmResult<String> deleteRecord(String itemCode, SearchValue_Mdl param) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String siteCode = DmServer.getSsnSiteCode(this);
        dmrTemp.setData("");

        try {
            PriceListMaster_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(itemCode).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            STServer.deletePriceList(param.getApplicableDate(), PriceListEntryFrom.PRICELIST_MASTER, itemCode, param.getRateListCode(),
                    siteCode, cnTemp);
            dmrTemp.setData(itemCode);
            spBegin.commit();
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
    public DmResult<String> saveRecord(GrdItemDetail_Mdl mrTemp, SearchValue_Mdl param) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            PriceListMaster_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (param.getRateListCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            dmrTemp.setData(saveItem(mrTemp, param, cnTemp));
            spBegin.commit();
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

    private String saveItem(GrdItemDetail_Mdl mrTemp, SearchValue_Mdl param, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String siteCode = DmServer.getSsnSiteCode(this);
        ResultSet rsTemp = null;
        List<ItemPriceList_Mdl> lstItemPrice = new ArrayList<ItemPriceList_Mdl>();
        try {

            if (!DmShared.xNull(mrTemp.getItemCode()).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(mrTemp.getUnitCode()).trim().equalsIgnoreCase("")
                    && DmShared.vNull(mrTemp.getRate()).doubleValue() > 0) {
                lstItemPrice.add(getPriceListMdl(param.getRateListCode(), param.getApplicableDate(),
                        mrTemp.getItemCode(), mrTemp.getItemName(), mrTemp.getUnitCode(), mrTemp.getUnitName(),
                        mrTemp.getRate(), mrTemp.getDiscountPercentage(), mrTemp.getDiscountAmount(), mrTemp.getPrintSNo()
                ));
            }
            if (lstItemPrice.size() > 0) {
                STServer.savePriceListItem(param.getApplicableDate(), PriceListEntryFrom.PRICELIST_MASTER, mrTemp.getItemCode(), param.getRateListCode(), siteCode, lstItemPrice, cnTemp);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return mrTemp.getSharedKeyFieldCode();
    }

    private ItemPriceList_Mdl getPriceListMdl(String keyCode, String applicableDate,
            String itemCode, String itemName,
            String unitCode, String unitName,
            String rate, String discountPercentage, String discountAmount, String printSno) {
        ItemPriceList_Mdl priceList;
        priceList = new ItemPriceList_Mdl();
        priceList.setPriceListCode(DmShared.xNull(keyCode));
        priceList.setItemCode(DmShared.xNull(itemCode));
        priceList.setItemName(DmShared.xNull(itemName));
        priceList.setUnitCode(DmShared.xNull(unitCode));
        priceList.setUnitName(DmShared.xNull(unitName));
        priceList.setRate(DmShared.xNull(rate));
        priceList.setDiscountPercentage(DmShared.xNull(discountPercentage));
        priceList.setDiscountAmount(DmShared.xNull(discountAmount));
        priceList.setDate(DmShared.dNull(applicableDate));
        priceList.setPrintSNo(DmShared.xNull(printSno));
        priceList.setEntryFrom((PriceListEntryFrom.PRICELIST_MASTER));
        priceList.setEntryFromNature(DmShared.iNull(PriceListEntryFrom.NONE.toInt()).toString());

        return priceList;
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        String sqlQuery;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  plm.code, plm.name as priceListName "
                    // + ", pl.applicableFrom"
                    + ", plm.priceListType"
                    + ", case plm.priceListType when " + PriceListType.FIX.getId() + " then '" + PriceListType.FIX.getValue() + "' end priceListTypeName "
                    + "	FROM priceListMast plm "
                    + "	INNER JOIN (\n"
                    + "     SELECT  Distinct pl.priceListCode\n"
                    + "     FROM priceList pl\n"
                    + "     Where pl.site_Code = :siteCode   \n"
                    + "     and entryFrom = :entryFrom  \n"
                    + "	) vPl on vPl.priceListCode = plm.code  ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("entryFrom", String.valueOf(PriceListEntryFrom.PRICELIST_MASTER.toInt()));
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setPriceListName(DmShared.xNull(rsTemp.getString("priceListName")));
                listNode.setPriceListType(DmShared.xNull(rsTemp.getString("priceListTypeName")));

                listNode.setPriceListTypeTag(DmShared.xNull(rsTemp.getString("priceListType")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<GrdItemDetail_Mdl>> getItemDetailGrid(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdItemDetail_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdItemDetail_Mdl> lstTemp = new ArrayList<>();
        GrdItemDetail_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String conditionIPL = "", conditionMast = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            conditionIPL = " WHERE pl.entryFrom = :entryFrom \n"
//                    + " and  pl.applicableFrom = :applicableDate and (im.filterItemType = :filterItemType OR im.isCommon = 1) \n"
                    + " and  pl.applicableFrom = :applicableDate and (im.filterItemType in(select code from itemType where manualCode in('SM','PH')) OR im.isCommon = 1) \n"
                    + " and sl.site_Code = :site_Code \n"
                    + " and pl.site_Code = :site_Code \n";

//            conditionMast = " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)"
            conditionMast = " WHERE (im.filterItemType in(select code from itemType where manualCode in('SM','PH')) OR im.isCommon = 1)"
                    + "and im.isActive = '1' "
                    + "and sl.site_Code = :site_Code \n"
                    + "and im.code not in(select itemCode from priceList) ";

            if (DmShared.iNull(param.getRateListCode()).longValue() > 0) {
                conditionIPL += " AND pl.priceListCode = :priceListCode ";
            } else {
                conditionIPL += " AND 1=2 ";
                conditionMast += " AND 1=2 ";
            }

            sqlWithParam.setSQL("SELECT \n"
                    + "im.name as itemName, pl.itemCode, pl.unitCode, um.unit AS unitName \n"
                    + ", ic.code as itemCatCode, ic.name as itemCatName\n"
                    + ", pl.rate, pl.discount, pl.discountPer \n"
                    + ", pl.printSno, plm.applicableOn, 2 as recordType \n"
                    + "FROM priceList pl\n"
                    + "LEFT JOIN itemMast im on im.code = pl.itemCode\n"
                    + "LEFT JOIN unitMast um ON um.code = pl.unitCode\n"
                    + "LEFT JOIN itemCat ic on ic.code = im.itemCategoryCode\n"
                    + "LEFT JOIN priceListMast plm on plm.code = pl.priceListCode\n"
                    + "LEFT JOIN itemSiteList sl ON sl.code = im.code\n" + conditionIPL
                    + "union all \n"
                    + "SELECT \n"
                    + " im.name as itemName,im.code as itemCode, im.saleUnit AS unitCode, um.unit AS unitName \n"
                    + ", ic.code as itemCatCode, ic.name as itemCatName\n"
                    + ", 0.00 as rate, 0.00 as discount, 0.00 as discountPer   \n "
                    + ", im.printSrNo as printSno, null as applicableOn , 1 as recordType \n"
                    + "FROM itemMast im\n"
                    + "LEFT JOIN unitMast um ON um.code = im.purchaseUnit\n"
                    + "LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode\n"
                    + "LEFT JOIN itemSiteList sl ON sl.code = im.code\n" + conditionMast);

//            if (DmShared.xNull(param.getApplicableOn()).trim().equalsIgnoreCase("1")) {
//                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
//            } else if (DmShared.xNull(param.getApplicableOn()).trim().equalsIgnoreCase("2")) {
//                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
//            }

            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(param.getRateListCode()).longValue() > 0) {
                sqlWithParam.setString("priceListCode", DmShared.xNull(param.getRateListCode()));
            }
            sqlWithParam.setString("applicableDate", DmShared.dNull(DmServer.convertDate(param.getApplicableDate())));
            sqlWithParam.setString("entryFrom", DmShared.iNull(PriceListEntryFrom.PRICELIST_MASTER.toInt()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdItemDetail_Mdl();
                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setItemCatCode(DmShared.xNull(rsTemp.getString("itemCatCode")));
                listNode.setItemCatName(DmShared.xNull(rsTemp.getString("itemCatName")));
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setDiscountAmount(DmServer.vNull(rsTemp.getString("discount"), "0.00"));
                listNode.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer")));
                listNode.setPrintSNo(DmShared.xNull(rsTemp.getString("printSNo")));
                listNode.setApplicableOn(DmShared.xNull(rsTemp.getString("applicableOn")));
                listNode.setRecordType(DmShared.xNull(rsTemp.getString("recordType")));

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
    public DmResult<String> getListFromCSV(String keyValue, String applicableDate, String serverDirectory, String serverFileName) {
        DmResult<String> dmrTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        File sessionDir;
        String fieldValue, sqlQuery, priceListName = "";
        String directoryPath, lineNoDisplay = "", siteCode = "", maxApplicableDate = "", applicableTo = "31-Dec-9999";
        DmCsvReader csvReader = null;
        CSV_Mdl csvMdl = null;
        DmHandelFile handelFileLog = null;
        File tempFile = null;
        boolean flag = false;
        int index = 0, noOfBug = 0, count = 0;
        List<ItemPriceList_Mdl> lstItemPrice = new ArrayList<ItemPriceList_Mdl>();
        List<ItemPriceList_Mdl> lstItemPriceTemp = new ArrayList<ItemPriceList_Mdl>();
        HashMap<String, List<ItemPriceList_Mdl>> hmRateList = new HashMap<String, List<ItemPriceList_Mdl>>();
        try {
            directoryPath = serverDirectory + File.separatorChar + "RateList_Log";

            /*===========================< Make directory if not exits >===========================*/
            tempFile = new File(directoryPath);
            tempFile.delete();
            tempFile.mkdirs();
            tempFile = null;

            handelFileLog = new DmHandelFile(directoryPath + File.separatorChar + "Price_List.log", true);

            sessionDir = new File(serverDirectory + File.separatorChar + serverFileName);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            priceListName = DmShared.xNull(DmServer.getCodeViaManualCode(keyValue, "code", "priceListMast", "name", cnTemp));
            if (sessionDir.exists()) {
                csvReader = new DmCsvReader(sessionDir);
                csvReader.readLines();

                DmImportData_Server.checkHeader(new String[]{"priceList", "itemName",
                    "rate", "discountPer", "discount"}, csvReader, handelFileLog);
                index = 0;

                for (DmDynamicGridRow ddgr : csvReader.getRows()) {
                    csvMdl = new CSV_Mdl();
                    index++;
                    lineNoDisplay = "line No. " + DmShared.iNull(index).toString() + " :";
                    for (String columnName : csvReader.getHeaders()) {
                        fieldValue = ddgr.getCells().get(columnName.toLowerCase()) + "";
                        if (columnName.equalsIgnoreCase("priceList")) {
                            flag = validateField(columnName, fieldValue, true, false, false, lineNoDisplay, handelFileLog);
                            if (DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                                flag = false;
                                handelFileLog.writeLine(lineNoDisplay + " not inserted. rateList Name(" + DmShared.xNull(fieldValue).trim() + ") not found.");
                            } else {
                                csvMdl.setPriceListName(DmShared.xNull(fieldValue));
                                if (!priceListName.trim().equalsIgnoreCase(csvMdl.getPriceListName().trim())) {
                                    throw new DmException(0, lineNoDisplay + "Price List not matched. [" + csvMdl.getPriceListName().trim() + "]");
                                }
                            }
                            if (!flag) {
                                break;
                            }
                        } else if (columnName.equalsIgnoreCase("itemName")) {
                            flag = validateField(columnName, fieldValue, true, false, false, lineNoDisplay, handelFileLog);
                            if (DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                                flag = false;
                                handelFileLog.writeLine(lineNoDisplay + " not inserted. Item Name(" + DmShared.xNull(fieldValue).trim() + ") not found.");
                            } else {
                                String itemCode = "", unitCode = "", unitName = "";
                                sqlQuery = "SELECT im.code as itemCode, im.saleUnit AS unitCode, um.unit AS unitName \n"
                                        + "FROM itemMast im \n"
                                        + "LEFT JOIN unitMast um ON um.code = im.saleUnit\n"
                                        + "where name = :name \n";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("name", fieldValue);
                                rsTemp = psTemp.executeQuery();
                                if (rsTemp.next()) {
                                    itemCode = DmShared.xNull(rsTemp.getString("itemCode"));
                                    unitCode = DmShared.xNull(rsTemp.getString("unitCode"));
                                    unitName = DmShared.xNull(rsTemp.getString("unitName"));
                                }
                                rsTemp.close();
                                psTemp.close();

                                if (DmShared.iNull(itemCode).longValue() == 0) {
                                    throw new DmException(0, lineNoDisplay + "Item name not matched. [" + csvMdl.getItemName().trim() + "]");
                                } else {
                                    csvMdl.setItemName(DmShared.xNull(fieldValue));
                                    csvMdl.setItemCode(itemCode);
                                    csvMdl.setUnitCode(unitCode);
                                    csvMdl.setUnitName(unitName);
                                }
                            }
                            if (!flag) {
                                break;
                            }
                        } else if (columnName.equalsIgnoreCase("rate")) {
                            flag = validateField(columnName, fieldValue, true, true, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setRate(DmShared.xNull(fieldValue));
                        } else if (columnName.equalsIgnoreCase("discount")) {
                            flag = validateField(columnName, fieldValue, false, true, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setDiscount(DmShared.xNull(fieldValue));
                        } else if (columnName.equalsIgnoreCase("discountPer")) {
                            flag = validateField(columnName, fieldValue, false, true, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setDiscountPercentage(DmShared.xNull(fieldValue));
                        }
                    }
                    if (DmShared.vNull(csvMdl.getDiscount()).doubleValue() > 0 && DmShared.vNull(csvMdl.getDiscountPercentage()).doubleValue() > 0) {
                        flag = false;
                        handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getItemName() + " not inserted. Either give discount or discount percentage.");
                    }
                    if (DmShared.vNull(csvMdl.getDiscount()).doubleValue() > 0 || DmShared.vNull(csvMdl.getDiscountPercentage()).doubleValue() > 0) {
                        if (DmShared.vNull(csvMdl.getRate()).doubleValue() <= 0) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getItemName() + " not inserted. Rate is mandatory when discount given.");
                        }
                        if (DmShared.vNull(csvMdl.getDiscountPercentage()).doubleValue() > 100) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getItemName() + " not inserted. Discount percentage cannot be greater than 100.");
                        }
                        if (DmShared.vNull(csvMdl.getDiscount()).doubleValue() > 0 && DmShared.vNull(csvMdl.getDiscount()).doubleValue() > DmShared.vNull(csvMdl.getRate()).doubleValue()) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getItemName() + " not inserted. Discount cannot be greater than rate.");
                        }

                    }
                    if (flag) {
                        GrdItemDetail_Mdl mrTemp = new GrdItemDetail_Mdl();
                        mrTemp.setItemCode(DmShared.xNull(csvMdl.getItemCode()));
                        mrTemp.setItemName(DmShared.xNull(csvMdl.getItemName()));
                        mrTemp.setUnitCode(DmShared.xNull(csvMdl.getUnitCode()));
                        mrTemp.setUnitName(DmShared.xNull(csvMdl.getUnitName()));
                        mrTemp.setRate(DmShared.xNull(csvMdl.getRate()));
                        mrTemp.setDiscountPercentage(DmShared.xNull(csvMdl.getDiscountPercentage()));
                        mrTemp.setDiscountAmount(DmShared.xNull(csvMdl.getDiscount()));
                        lstItemPrice.add(getPriceListMdl(keyValue, applicableDate,
                                csvMdl.getItemCode(), csvMdl.getItemName(),
                                csvMdl.getUnitCode(), csvMdl.getUnitName(),
                                csvMdl.getRate(), csvMdl.getDiscountPercentage(), csvMdl.getDiscount(), ""));
                        if (count == 0) {
                            sqlQuery = "select Max(applicableFrom) as applicableFrom,Min(applicableFrom) as applicableFromMin  "
                                    + "from priceList "
                                    + "where entryFrom = :entryFrom "
                                    + "and site_Code = :site_Code "
                                    + "and priceListCode = :priceListCode";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
                            psTemp.setString("site_Code", siteCode);
                            psTemp.setString("priceListCode", keyValue);
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
                            }
                            rsTemp.close();
                            psTemp.close();

                            if (!maxApplicableDate.equalsIgnoreCase("")) {
                                if (DmServer.convertDate(applicableDate).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                                    sqlQuery = "select applicableFrom,applicableTo "
                                            + "from priceList "
                                            + "where entryFrom = :entryFrom "
                                            + "and site_Code = :site_Code "
                                            + "and priceListCode = :priceListCode "
                                            + "and applicableFrom < :applicableFrom"
                                            + "group by applicableFrom,applicableTo "
                                            + "order by applicableFrom desc "
                                            + "offset 0 rows fetch next 1 rows only ";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                                    psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
                                    psTemp.setString("site_Code", siteCode);
                                    psTemp.setString("priceListCode", keyValue);
                                    rsTemp = psTemp.executeQuery();
                                    if (rsTemp.next()) {
                                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                                        sqlQuery = "update priceList set "
                                                + "applicableTo = :applicableTo "
                                                + "where applicableFrom = :applicableFrom "
                                                + "and entryFrom = :entryFrom "
                                                + "and site_Code = :site_Code "
                                                + "and priceListCode = :priceListCode ";

                                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                        psTemp1.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableDate))));
                                        psTemp1.setDate("applicableFrom", rsTemp.getDate("ApplicableFrom"));
                                        psTemp1.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
                                        psTemp1.setString("site_Code", siteCode);
                                        psTemp1.setString("priceListCode", keyValue);
                                        psTemp1.executeUpdate();
                                        psTemp1.close();

                                    } else {
                                        sqlQuery = "select applicableFrom,applicableTo "
                                                + "FROM priceList "
                                                + "where applicableFrom > :applicableFrom "
                                                + "and entryFrom = :entryFrom "
                                                + "and site_Code = :site_Code "
                                                + "and priceListCode = :priceListCode"
                                                + "group by applicableFrom,applicableTo "
                                                + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                        psTemp1.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                                        psTemp1.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
                                        psTemp1.setString("site_Code", siteCode);
                                        psTemp1.setString("priceListCode", keyValue);

                                        rsTemp1 = psTemp1.executeQuery();
                                        if (rsTemp1.next()) {
                                            applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));
                                        }
                                        psTemp1.close();
                                        rsTemp1.close();
                                    }
                                    psTemp.close();
                                    rsTemp.close();
                                } else {
                                    sqlQuery = "update priceList set "
                                            + "applicableTo = :applicableTo "
                                            + "where applicableFrom = :applicableFrom "
                                            + "and entryFrom = :entryFrom "
                                            + "and site_Code = :site_Code "
                                            + "and priceListCode = :priceListCode";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setDate("applicableFrom", DmServer.convertDate(maxApplicableDate));
                                    psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableDate))));
                                    psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
                                    psTemp.setString("site_Code", siteCode);
                                    psTemp.setString("priceListCode", keyValue);
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                            count++;
                        }
                        sqlQuery = "delete from priceList "
                                + "where applicableFrom = :applicableFrom "
                                + "and entryFrom = :entryFrom "
                                + "and site_Code = :site_Code "
                                + "and priceListCode = :priceListCode "
                                + "and itemCode = :itemCode "
                                + "and unitCode = :unitCode ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                        psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.PRICELIST_MASTER.toInt()));
                        psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                        psTemp.setString("priceListCode", keyValue);
                        psTemp.setString("itemCode", csvMdl.getItemCode());
                        psTemp.setString("unitCode", csvMdl.getUnitCode());
                        int result = psTemp.executeUpdate();
                        psTemp.close();
                        if (!hmRateList.containsKey(csvMdl.getItemCode())) {
                            lstItemPriceTemp = new ArrayList<ItemPriceList_Mdl>();
                            lstItemPriceTemp.add(lstItemPrice.get(lstItemPrice.size() - 1));
                            hmRateList.put(csvMdl.getItemCode(), lstItemPriceTemp);
                            lstItemPrice = new ArrayList<ItemPriceList_Mdl>();
                        } else if (hmRateList.containsKey(csvMdl.getItemCode())) {
                            lstItemPriceTemp = hmRateList.get(csvMdl.getItemCode());
                            lstItemPriceTemp.addAll(lstItemPrice);
                            hmRateList.replace(csvMdl.getItemCode(), hmRateList.get(csvMdl.getItemCode()), lstItemPriceTemp);
                            lstItemPrice = new ArrayList<ItemPriceList_Mdl>();
                        }
                    } else {
                        noOfBug++;
                    }
                }
                if (!hmRateList.isEmpty()) {
                    for (String itemCode : hmRateList.keySet()) {
                        STServer.savePriceListItem(applicableDate, PriceListEntryFrom.PRICELIST_MASTER, itemCode, keyValue, DmServer.getSsnSiteCode(this), hmRateList.get(itemCode), cnTemp);
                    }
                }
            }

            spBegin.commit();
            dmrTemp.setData(keyValue);
            dmrTemp.setStatus(true);
            dmrTemp.setValue1(directoryPath);
            dmrTemp.setValue2("Price_List.log");
            dmrTemp.setErrorNumber(noOfBug);

            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                handelFileLog.writeLine(e.getMessage());
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            } catch (Exception ex) {
                dmrTemp.setStatus(false);
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            }
        } finally {
            try {
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception ex) {

            }
        }
        return dmrTemp;
    }

    private static boolean validateField(String fieldName, String fieldValue, boolean isMandatory,
            boolean isNumeric, Boolean isNegative, String lineNoDisplay, DmHandelFile handelFile) {
        boolean flag = true;

        try {
            if (isMandatory) {
                if (isNumeric) {
                    if (DmShared.vNull(fieldValue).doubleValue() == 0) {
                        flag = false;
                        handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is mandatory.");
                    }
                } else if (DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                    flag = false;
                    handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is mandatory.");
                }
            }

            if (isNumeric) {
                if (isNegative != null) {
                    if (isNegative) {
                        if (DmShared.vNull(fieldValue).doubleValue() > 0) {
                            flag = false;
                            handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " positive not allowed.");
                        }
                    } else {
                        if (DmShared.vNull(fieldValue).doubleValue() < 0) {
                            flag = false;
                            handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is negative not allowed.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                flag = false;
                handelFile.writeLine(e.getMessage());
            } catch (Exception ex) {
            }
        }

        return flag;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPriceListType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (PriceListType type : PriceListType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(type.getId()).toString());
                listNode.setSharedKeyFieldName(type.getValue());

                lstTemp.add(listNode);
            }
            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
