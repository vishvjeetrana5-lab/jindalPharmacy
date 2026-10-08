package aarogya.master.rateListMaster.server;

import aarogya.master.rateListMaster.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
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

public class RateListMaster_SrvImpl extends DmRSSPrivate implements RateListMaster_Srv {

    @Override
    public DmResultPageing<List<RateList_Mdl>> getTxtHRateList(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<RateList_Mdl>> rstTemp = new DmResultPageing<>();
        List<RateList_Mdl> lstTemp = new ArrayList<>();
        RateList_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT name, code, rateListType FROM rateListMast ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new RateList_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setRateListTypeEnum(RateListType.getViaKey(rsTemp.getInt("rateListType")));
                listNode.setRateListType(RateListType.getViaKey(rsTemp.getInt("rateListType")).getText());
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

    class CSV_Mdl {

        private String serviceName, serviceCode, serviceCategory, serviceCategoryCode, roomCategory, roomCategoryCode,
                rate, discount, discountPercentage, rateList;

        public CSV_Mdl() {
        }

        public String getRateList() {
            return rateList;
        }

        public void setRateList(String rateList) {
            this.rateList = rateList;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceCategory() {
            return serviceCategory;
        }

        public void setServiceCategory(String serviceCategory) {
            this.serviceCategory = serviceCategory;
        }

        public String getRoomCategory() {
            return roomCategory;
        }

        public void setRoomCategory(String roomCategory) {
            this.roomCategory = roomCategory;
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

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getServiceCategoryCode() {
            return serviceCategoryCode;
        }

        public void setServiceCategoryCode(String serviceCategoryCode) {
            this.serviceCategoryCode = serviceCategoryCode;
        }

        public String getRoomCategoryCode() {
            return roomCategoryCode;
        }

        public void setRoomCategoryCode(String roomCategoryCode) {
            this.roomCategoryCode = roomCategoryCode;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + Objects.hashCode(this.serviceName);
            hash = 37 * hash + Objects.hashCode(this.serviceCategory);
            hash = 37 * hash + Objects.hashCode(this.roomCategory);
            hash = 37 * hash + Objects.hashCode(this.rateList);
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
            if (!Objects.equals(this.serviceName, other.serviceName)) {
                return false;
            }
            if (!Objects.equals(this.serviceCategory, other.serviceCategory)) {
                return false;
            }
            if (!Objects.equals(this.roomCategory, other.roomCategory)) {
                return false;
            }
            if (!Objects.equals(this.rateList, other.rateList)) {
                return false;
            }
            return true;
        }

    }

    @Override
    public DmResult<RateListMaster_Mdl> getMoveRec(String strKey) {
        DmResult<RateListMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        RateListMaster_Mdl mrTemp = new RateListMaster_Mdl();
        GrdCategoryDiscount_Mdl node = null;
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  om.Code, om.name, pspl.applicableFrom, om.medicineSaleOn, "
                    + "		 om.pharmacyDiscountPer, om.isPharmacyDiscount,om.rateListType"
                    + "		from rateListmast om "
                    + "		left join ( "
                    + "			select  top 1 rateListCode, applicableFrom "
                    + "				from patientServicePriceList pspl "
                    + "				where entryFrom = :entryFrom and site_code = :siteCode and rateListCode = :code "
                    + "				and applicableFrom <= cast(:uEntDt as date) and organizationCode is null "
                    + "				order by applicableFrom desc"
                    + "		) pspl on pspl.rateListCode = om.Code  "
                    + "		where om.Code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.RATE_LIST_MASTER.toInt()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRateListCode(DmShared.xNull(rsTemp.getString("Code")));
                mrTemp.setRateListName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setAllowPharmacyDiscount(DmShared.iNull(rsTemp.getString("isPharmacyDiscount")).intValue() == 1);
//                mrTemp.setOrgnizationType(DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 0 ? "General"
//                        : (DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 1 ? "TPA"
//                        : (DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 2 ? "Collection Center" : "")));
                mrTemp.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));
                mrTemp.setRateListTypeCode(DmShared.xNull(rsTemp.getString("rateListType")));
                mrTemp.setRateLisTypeName(RateListType.getViaKey(rsTemp.getInt("rateListType")).getText());
//                mrTemp.setFeesDiscountPer(DmServer.vNull(rsTemp.getString("feesDiscountPer"), "0.00"));
                mrTemp.setPharmacyDiscountPer(DmServer.vNull(rsTemp.getString("pharmacyDiscountPer"), "0.00"));
//                mrTemp.setServiceDiscountPer(DmServer.vNull(rsTemp.getString("ServiceDiscountPer"), "0.00"));
//                mrTemp.setTestDiscountPer(DmServer.vNull(rsTemp.getString("TestDiscountPer"), "0.00"));
//                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setApplicableDate(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                mrTemp.setSiteCode(DmServer.getSsnSiteCode(this));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  pscm.code, pscm.name, pscd.discountPer, pscd.rateListCode "
                    + "		from patientServiceCatMast pscm "
                    + "		left join patientServiceCatDiscount pscd on pscd.serviceCategoryCode = pscm.code and pscd.rateListCode = :organization "
                    + "             and pscd.site_code = :siteCode and pscd.organizationCode is null"
                    + "		where isnull(pscm.filterNature, 0) <> 4 "
                    + "		order by pscm.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organization", strKey);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdCategoryDiscount_Mdl();
                node.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                node.setOrgnaizationCode(DmShared.xNull(rsTemp.getString("rateListCode")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setColorChange(DmShared.vNull(rsTemp.getString("discountPer")).doubleValue() > 0);
                mrTemp.getLstServiceCategory().add(node);
            }

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
    public DmResult<String> savePopup(RateListMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery, siteCode = DmServer.getSsnSiteCode(this);
        dmrTemp.setData("");
        try {
            RateListMaster_Vld.chkSavePercent(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (mrTemp.getRateListCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "update rateListmast set "
                    + "	medicineSaleOn = :medicineSaleOn, "
                    + "	pharmacyDiscountPer = :pharmacyDiscountPer, "
                    + "	isPharmacyDiscount = :isPharmacyDiscount "
                    + "	where code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getRateListCode()));
            psTemp.setInt("isPharmacyDiscount", mrTemp.isAllowPharmacyDiscount() ? 1 : 0);
//            psTemp.setString("serviceDiscountPer", DmServer.vNull(mrTemp.getServiceDiscountPer(), "0.00"));
//            psTemp.setString("testDiscountPer", DmServer.vNull(mrTemp.getTestDiscountPer(), "0.00"));
//            psTemp.setString("feesDiscountPer", DmServer.vNull(mrTemp.getFeesDiscountPer(), "0.00"));
            psTemp.setString("pharmacyDiscountPer", DmServer.vNull(mrTemp.getPharmacyDiscountPer(), "0.00"));
            psTemp.setInt("medicineSaleOn", mrTemp.getMedicineSaleOn().getId());
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Delete records from patientServiceCatDiscount  >==============================*/
            sqlQuery = "delete from patientServiceCatDiscount where rateListCode = :rateListCode and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("rateListCode", DmShared.iNull(mrTemp.getRateListCode()).toString());
            psTemp.setString("site_Code", siteCode);
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdCategoryDiscount_Mdl node : mrTemp.getLstServiceCategory()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0 && DmShared.vNull(node.getDiscountPercentage()).doubleValue() != 0) {
                    sqlQuery = "insert into patientServiceCatDiscount (rateListCode, serviceCategoryCode, site_code, discountPer) "
                            + " values (:rateListCode, :serviceCategoryCode, :site_code, :discountPer) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("rateListCode", DmShared.xNull(mrTemp.getRateListCode()));
                    psTemp.setString("site_Code", siteCode);
                    psTemp.setString("discountPer", DmServer.vNull(node.getDiscountPercentage(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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
    public DmResult<String> applyDiscountPercent(double percentValue, SearchValue_Mdl param) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            RateListMaster_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(param.getRateListCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "update patientServicePriceList set discountPer = :discountPer "
                    + " where (entryFrom = :entryFrom) and (entryFromNature = 0) and (site_code = :siteCode) and (organizationCode = :organization) "
                    + "	and (applicableFrom = :applicableDate) and (rate > 0) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("organization", DmShared.xNull(param.getRateListCode()));
            psTemp.setDate("applicableDate", DmServer.convertDate(param.getApplicableDate()));
            psTemp.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
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
    public DmResult<String> saveRateList(RateList_Mdl mrTemp) {
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
            DmShared.isValidText(DmShared.xNull(mrTemp.getRateListTypeEnum().getText()), "Rate List Type", 200);

            //  =======================================================================================================
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select  * "
                    + "         from rateListMast "
                    + "         where Name =:name and rateListType= :rateListType";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("rateListType", DmShared.iNull(mrTemp.getRateListTypeEnum().getId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(100, "RateList already exist.");
            }
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "rateListMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            sqlQuery = "Insert Into rateListMast(code, name, rateListType, preparedBy, u_EntDt) Values  "
                    + "(:code,:name,:rateListType,:preparedBy,:u_EntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("rateListType", DmShared.iNull(mrTemp.getRateListTypeEnum().getId()).toString());
            psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<String> deleteRecord(String serviceCode, SearchValue_Mdl param) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String consFeesService = "";
        ResultSet rsTemp = null;
        List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
        String siteCode = DmServer.getSsnSiteCode(this);
        dmrTemp.setData("");

        try {
            RateListMaster_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(serviceCode).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            STServer.deletePriceList(param.getApplicableDate(), PriceListEntryFrom.RATE_LIST_MASTER, serviceCode, param.getRateListCode(), siteCode, cnTemp);
            dmrTemp.setData(serviceCode);
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
    public DmResult<String> saveRecord(GrdServices_Mdl mrTemp, SearchValue_Mdl param) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String consFeesService = "";
        ResultSet rsTemp = null;
        List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
        String siteCode = DmServer.getSsnSiteCode(this);
        dmrTemp.setData("");

        try {
            RateListMaster_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            dmrTemp.setData(saveService(mrTemp, param, cnTemp));
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

    private String saveService(GrdServices_Mdl mrTemp, SearchValue_Mdl param, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String consFeesService = "", siteCode = DmServer.getSsnSiteCode(this);
        ResultSet rsTemp = null;
        List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
        try {

            if (DmShared.vNull(mrTemp.getRate()).doubleValue() > 0 || (DmShared.vNull(mrTemp.getDiscountAmount()).doubleValue() > 0) || (DmShared.vNull(mrTemp.getDiscountPercentage()).doubleValue() > 0)) {
                lstServicePrice.add(getPriceListMdl(param.getRateListCode(), param.getApplicableDate(), param.getServiceCode(),
                        "", mrTemp.getRate(), mrTemp.getDiscountAmount(), mrTemp.getDiscountPercentage(), mrTemp.getPrintSNo()));
            }
            for (GrdRoomCategory_Mdl categoryMdl : mrTemp.getLstRoomCategory()) {
                if (DmShared.vNull(categoryMdl.getRate()).doubleValue() > 0 || (DmShared.vNull(categoryMdl.getDiscountAmount()).doubleValue() > 0) || (DmShared.vNull(categoryMdl.getDiscountPercentage()).doubleValue() > 0)) {
                    lstServicePrice.add(getPriceListMdl(param.getRateListCode(), param.getApplicableDate(), param.getServiceCode(),
                            categoryMdl.getSharedKeyFieldCode(), categoryMdl.getRate(), categoryMdl.getDiscountAmount(),
                            categoryMdl.getDiscountPercentage(), ""));
                }
            }

            consFeesService = DmServer.getCodeViaManualCode("CF", "shortName", "patientservicemast", "code", cnTemp);
            if (DmShared.iNull(consFeesService).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid 'CF' code. Create 'CF' Code");
            }

            if (lstServicePrice.size() > 0) {
                STServer.savePriceList(param.getApplicableDate(), PriceListEntryFrom.RATE_LIST_MASTER, mrTemp.getSharedKeyFieldCode(), param.getRateListCode(), siteCode, lstServicePrice, cnTemp);
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

    private ServicePriceList_Mdl getPriceListMdl(String keyCode, String applicableDate, String serviceCode, String roomCategoryCode,
            String rate, String discountAmount, String discountPercent, String printSno) {
        ServicePriceList_Mdl priceList;
        priceList = new ServicePriceList_Mdl();
        priceList.setRateListCode(keyCode);
        priceList.setDate(applicableDate);
        priceList.setPriceListEntryFrom(PriceListEntryFrom.RATE_LIST_MASTER);
        priceList.setPriceListNature(PriceListNature.None);
        priceList.setRate(DmServer.vNull(rate, "0.00"));
        priceList.setDiscAmt(DmServer.vNull(discountAmount, "0.00"));
        priceList.setDiscPercentage(DmServer.vNull(discountPercent, "0.00"));
        priceList.setPrintSNo(printSno);
        priceList.setRoomCatCode(roomCategoryCode);
        priceList.setServiceCode(serviceCode);
        priceList.setSiteCode(DmServer.getSsnSiteCode(this));
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

            sqlQuery = "select  rlm.code, rlm.name, pspl.applicableFrom, case rlm.rateListType when " + RateListType.CASH.getId()
                    + "		then '" + RateListType.CASH.getText() + "' when " + RateListType.FIX.getId()
                    + "		then '" + RateListType.FIX.getText() + "' end rateListType  "
                    + "		from rateListMast rlm "
                    + "		left join ("
                    + "			select  max(rateListCode) as rateListCode, max(applicableFrom) as applicableFrom "
                    + "				from patientservicepricelist "
                    + "				where entryFrom = :entryFrom and site_code = :siteCode and applicableFrom <= cast(:uEntDt as date) "
                    + "				and (organizationCode is null or consultantCode is null)"
                    + "				group by rateListCode "
                    + "		) pspl on rlm.code = pspl.rateListCode";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("entryFrom", String.valueOf(PriceListEntryFrom.RATE_LIST_MASTER.toInt()));
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("rateListType")));
                listNode.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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

    public DmResult<List<GrdRoomCategory_Mdl>> getRoomCategory() {
        DmResult<List<GrdRoomCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdRoomCategory_Mdl> lstTemp = new ArrayList<>();
        GrdRoomCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT CODE , NAME,'0.00' AS Rate FROM roomcatmast ORDER BY NAME";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdRoomCategory_Mdl();

                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setRate(DmShared.xNull(rsTemp.getString("Rate")));
                listNode.setDiscountAmount(DmShared.xNull("0.00"));
                listNode.setDiscountPercentage(DmShared.xNull("0.00"));
                lstTemp.add(listNode);
            }
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
    public DmResultPageing<List<KeyFieldExtended>> getServiceNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (ServiceNature nature : ServiceNature.values()) {
                if (nature != ServiceNature.OVERHEAD_SERVICE) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(DmShared.iNull(nature.getId()).toString());
                    listNode.setSharedKeyFieldName(nature.getValue());
                    lstTemp.add(listNode);
                }
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlQuery = " select pscm.code ,pscm.name from patientServiceCatMast pscm where isnull(pscm.filterNature, 0) <> 4 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
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
    public DmResultPageing<List<GrdServices_Mdl>> getServiceAndInvestigation(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdServices_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdServices_Mdl> lstTemp = new ArrayList<>();
        GrdServices_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            if (!DmShared.xNull(param.getServiceNature()).equalsIgnoreCase("")) {
                condition = " and psm.serviceNature in (" + DmServer.getParamList(":serviceNature", param.getServiceNature()) + ")";
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  serviceCode, max(name) as name, max(category) as category, max(printSNo) as printSNo, max(rate) as rate, "
                    + "		max(discount) as discount, max(discountPer) as discountPer, max(colorChange) as colorChange, "
                    + "		max(serviceType) as serviceType "
                    + "		from ("
                    + "			select  pspl.serviceCode, psm.name, pscm.name as category, pspl.printSNo, "
                    + "				pspl.rate, pspl.discount, pspl.discountPer, 1 as colorChange, case psm.serviceNature "
                    + "                         when " + ServiceNature.SERVICE.getId() + " then '" + ServiceNature.SERVICE.getValue() + "'"
                    + "                         when " + ServiceNature.OPERATION.getId() + " then '" + ServiceNature.OPERATION.getValue() + "'"
                    + "                         when " + ServiceNature.PATHOLOGY_TEST.getId() + " then '" + ServiceNature.PATHOLOGY_TEST.getValue() + "'"
                    + "                         when " + ServiceNature.RADIOLOGY_TEST.getId() + " then '" + ServiceNature.RADIOLOGY_TEST.getValue() + "'"
                    + "                         end as serviceType "
                    + "				from ("
                    + "					select  pspl.serviceCode, max(pspl.organizationServiceCode)organizationServiceCode, max(pspl.printSNo) as printSNo,"
                    + "						max(case when roomCatCode is null then pspl.rate else 0 end) as rate, "
                    + "						max(case when roomCatCode is null then pspl.discount else 0 end) as discount, "
                    + "						max(case when roomCatCode is null then pspl.discountPer else 0 end) as discountPer"
                    + "						from patientServicePriceList pspl "
                    + "						where rateListCode = :rateListCode and entryFrom = :entryFrom and "
                    + "						site_Code = :siteCode and applicableFrom = :applicableDate "
                    + "						group by  pspl.serviceCode "
                    + "				) as pspl "
                    + "				left join patientServiceSiteList pssl on pssl.code = pspl.serviceCode "
                    + "				left join organizationServiceMast osm on osm.code = pspl.organizationServiceCode "
                    + "				left join patientServiceMast psm on psm.code = pspl.serviceCode "
                    + "				left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "				where pssl.site_Code = :siteCode and psm.isActive = 1 and psm.serviceNature <> " + ServiceNature.OVERHEAD_SERVICE.getId() + condition
                    + "			union all "
                    + "			select  psm.code as serviceCode, psm.name, pscm.name as category, psm.printSNo, "
                    + "				0.00 as rate, 0.00 as discount, 0.00 as discountPer, 0 as colorChange, case psm.serviceNature"
                    + "                         when " + ServiceNature.SERVICE.getId() + " then '" + ServiceNature.SERVICE.getValue() + "'"
                    + "                         when " + ServiceNature.OPERATION.getId() + " then '" + ServiceNature.OPERATION.getValue() + "'"
                    + "                         when " + ServiceNature.PATHOLOGY_TEST.getId() + " then '" + ServiceNature.PATHOLOGY_TEST.getValue() + "'"
                    + "                         when " + ServiceNature.RADIOLOGY_TEST.getId() + " then '" + ServiceNature.RADIOLOGY_TEST.getValue() + "'"
                    + "                         end as serviceType "
                    + "				from patientServiceMast psm "
                    + "				left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "				left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "				where pssl.site_Code = :siteCode and psm.isActive = 1 and psm.serviceNature <> " + ServiceNature.OVERHEAD_SERVICE.getId() + condition
                    + "		) as tbl"
                    + "		group by tbl.serviceCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("rateListCode", DmShared.xNull(param.getRateListCode()));
            sqlWithParam.setDate("applicableDate", DmServer.convertDate(param.getApplicableDate()));
            sqlWithParam.setInteger("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
            if (!DmShared.xNull(param.getServiceNature()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("serviceNature", param.getServiceNature());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdServices_Mdl();
                listNode.setCategoryText(DmShared.xNull(rsTemp.getString("category")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                listNode.setServiceCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setDiscountAmount(DmServer.vNull(rsTemp.getString("discount"), "0.00"));
                listNode.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                listNode.setServiceType(DmShared.xNull(rsTemp.getString("serviceType")));
                listNode.setPrintSNo(DmShared.xNull(rsTemp.getString("printSNo")));
                listNode.setColorChange(DmShared.iNull(rsTemp.getString("colorChange")).intValue() == 1);
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<GrdRoomCategory_Mdl>> getRoomCategoryViaService(SearchValue_Mdl param) {
        DmResultPageing<List<GrdRoomCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdRoomCategory_Mdl> lstTemp = new ArrayList<>();
        GrdRoomCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  rcm.code, rcm.name, pspl.rate, pspl.discount, pspl.discountPer "
                    + "		from roomCatMast rcm "
                    + "		left join ("
                    + "			select  pspl.roomCatCode, pspl.discountPer, pspl.rate, pspl.discount"
                    + "				from patientServicePriceList pspl "
                    + "				where pspl.rateListCode = :rateListCode and pspl.entryFrom = :entryFrom and pspl.roomCatCode is not null"
                    + "				and pspl.site_code = :siteCode and pspl.applicableFrom = :applicableDate and pspl.serviceCode = :service "
                    + "		) pspl on pspl.roomCatCode = rcm.code "
                    + "		order by rcm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("rateListCode", DmShared.xNull(param.getRateListCode()));
            psTemp.setString("service", DmShared.xNull(param.getServiceCode()));
            psTemp.setDate("applicableDate", DmServer.convertDate(param.getApplicableDate()));
            psTemp.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdRoomCategory_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setDiscountAmount(DmServer.vNull(rsTemp.getString("discount"), "0.00"));
                listNode.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
        String fieldValue, sqlQuery, rateListName = "";
        String directoryPath, lineNoDisplay = "", siteCode = "", maxApplicableDate = "", applicableTo = "31-Dec-9999";
        DmCsvReader csvReader = null;
        CSV_Mdl csvMdl = null;
        DmHandelFile handelFileLog = null;
        File tempFile = null;
        boolean flag = false;
        int index = 0, noOfBug = 0, count = 0;
        List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
        List<ServicePriceList_Mdl> lstServicePriceTemp = new ArrayList<ServicePriceList_Mdl>();
        HashMap<String, List<ServicePriceList_Mdl>> hmRateList = new HashMap<String, List<ServicePriceList_Mdl>>();
        try {
            directoryPath = serverDirectory + File.separatorChar + "RateList_Log";

            /*===========================< Make directory if not exits >===========================*/
            tempFile = new File(directoryPath);
            tempFile.delete();
            tempFile.mkdirs();
            tempFile = null;

            handelFileLog = new DmHandelFile(directoryPath + File.separatorChar + "Rate_List.log", true);

            sessionDir = new File(serverDirectory + File.separatorChar + serverFileName);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            rateListName = DmShared.xNull(DmServer.getCodeViaManualCode(keyValue, "code", "rateListMast", "name", cnTemp));
            if (sessionDir.exists()) {
                csvReader = new DmCsvReader(sessionDir);
                csvReader.readLines();
                DmImportData_Server.checkHeader(new String[]{"rateList", "serviceName", "serviceCategory", "serviceNature", "roomCategory",
                    "rate", "discountPer", "discount"}, csvReader, handelFileLog);
                index = 0;

                for (DmDynamicGridRow ddgr : csvReader.getRows()) {
                    csvMdl = new CSV_Mdl();
                    index++;
                    lineNoDisplay = "line No. " + DmShared.iNull(index).toString() + " :";
                    for (String columnName : csvReader.getHeaders()) {
                        fieldValue = ddgr.getCells().get(columnName.toLowerCase()) + "";
                        if (columnName.equalsIgnoreCase("rateList")) {
                            flag = validateField(columnName, fieldValue, true, false, false, lineNoDisplay, handelFileLog);
                            if (DmShared.xNull(fieldValue).trim().equals("")) {
                                flag = false;
                                handelFileLog.writeLine(lineNoDisplay + " not inserted. rateList Name(" + DmShared.xNull(fieldValue).trim() + ") not found.");
                            } else {
                                csvMdl.setRateList(DmShared.xNull(fieldValue));
                                if (!rateListName.trim().equalsIgnoreCase(csvMdl.getRateList().trim())) {
                                    throw new DmException(0, lineNoDisplay + "Rate List not matched. [" + csvMdl.getRateList().trim() + "]");
                                }
                            }
                            if (!flag) {
                                break;
                            }
                        } else if (columnName.equalsIgnoreCase("serviceCategory")) {
                            flag = validateField(columnName, fieldValue, true, false, false, lineNoDisplay, handelFileLog);
                            if (DmShared.xNull(fieldValue).trim().equals("")) {
                                flag = false;
                                handelFileLog.writeLine(lineNoDisplay + " not inserted. Service Category(" + DmShared.xNull(fieldValue).trim() + ") not found.");
                            } else {
                                csvMdl.setServiceCategory(DmShared.xNull(fieldValue));
                                csvMdl.setServiceCategoryCode(DmServer.getCodeViaManualCode(DmShared.xNull(fieldValue), "name", "patientServiceCatMast", "code", cnTemp));
                            }
                            if (!flag) {
                                break;
                            }
                        } else if (columnName.equalsIgnoreCase("serviceName")) {
                            flag = validateField(columnName, fieldValue, true, false, false, lineNoDisplay, handelFileLog);
                            if (DmShared.xNull(fieldValue).trim().equals("")) {
                                flag = false;
                                handelFileLog.writeLine(lineNoDisplay + " not inserted. Service (" + DmShared.xNull(fieldValue).trim() + ") not found.");
                            } else {
                                csvMdl.setServiceName(DmShared.xNull(fieldValue));
                                csvMdl.setServiceCode(DmServer.getCodeViaManualCode(DmShared.xNull(fieldValue), "name", "patientServiceMast", "code", cnTemp));
                            }
                            if (!flag) {
                                break;
                            }
                        } else if (columnName.equalsIgnoreCase("roomCategory")) {
                            flag = validateField(columnName, fieldValue, false, false, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setRoomCategory(DmShared.xNull(fieldValue));
                            csvMdl.setRoomCategoryCode(DmServer.getCodeViaManualCode(DmShared.xNull(fieldValue), "name", "roomCatMast", "code", cnTemp));
                        } else if (columnName.equalsIgnoreCase("rate")) {
                            flag = validateField(columnName, fieldValue, false, false, false, lineNoDisplay, handelFileLog);
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
                        handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getServiceName() + " not inserted. Either give discount or discount percentage.");
                    }
                    if (DmShared.vNull(csvMdl.getDiscount()).doubleValue() > 0 || DmShared.vNull(csvMdl.getDiscountPercentage()).doubleValue() > 0) {
                        if (DmShared.vNull(csvMdl.getRate()).doubleValue() <= 0) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getServiceName() + " not inserted. Rate is mandatory when discount given.");
                        }
                        if (DmShared.vNull(csvMdl.getDiscountPercentage()).doubleValue() > 100) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getServiceName() + " not inserted. Discount percentage should not be greater than 100.");
                        }
                        if (DmShared.vNull(csvMdl.getDiscount()).doubleValue() > 0 && DmShared.vNull(csvMdl.getDiscount()).doubleValue() > DmShared.vNull(csvMdl.getRate()).doubleValue()) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getServiceName() + " not inserted. Discount should not be greater than rate.");
                        }

                    }
                    if (DmShared.iNull(csvMdl.getRate()).longValue() == 0) {
                        continue;
                    }
                    if (flag) {
                        lstServicePrice.add(getPriceListMdl(keyValue, applicableDate, csvMdl.getServiceCode(), csvMdl.getRoomCategoryCode(),
                                csvMdl.getRate(), csvMdl.getDiscount(), csvMdl.getDiscountPercentage(), ""));
                        if (count == 0) {
                            sqlQuery = "select Max(applicableFrom) as applicableFrom,Min(applicableFrom) as applicableFromMin  "
                                    + "from patientServicePriceList "
                                    + "where entryFrom = :entryFrom "
                                    + "and site_Code = :site_Code "
                                    + "and rateListCode = :rateListCode and (organizationCode is null or consultantCode is null)";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
                            psTemp.setString("site_Code", siteCode);
                            psTemp.setString("rateListCode", keyValue);
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
                            }
                            rsTemp.close();
                            psTemp.close();

                            if (!maxApplicableDate.equalsIgnoreCase("")) {
                                if (DmServer.convertDate(applicableDate).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                                    sqlQuery = "select applicableFrom,applicableTo "
                                            + "from patientServicePriceList "
                                            + "where entryFrom = :entryFrom "
                                            + "and site_Code = :site_Code "
                                            + "and rateListCode = :rateListCode "
                                            + "and applicableFrom < :applicableFrom and (organizationCode is null or consultantCode is null)"
                                            + "group by applicableFrom,applicableTo "
                                            + "order by applicableFrom desc "
                                            + "offset 0 rows fetch next 1 rows only ";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                                    psTemp.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
                                    psTemp.setString("site_Code", siteCode);
                                    psTemp.setString("rateListCode", keyValue);
                                    rsTemp = psTemp.executeQuery();
                                    if (rsTemp.next()) {
                                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                                        sqlQuery = "update patientServicePriceList set "
                                                + "applicableTo = :applicableTo "
                                                + "where applicableFrom = :applicableFrom "
                                                + "and entryFrom = :entryFrom "
                                                + "and site_Code = :site_Code "
                                                + "and rateListCode = :rateListCode ";

                                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                        psTemp1.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableDate))));
                                        psTemp1.setDate("applicableFrom", rsTemp.getDate("ApplicableFrom"));
                                        psTemp1.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
                                        psTemp1.setString("site_Code", siteCode);
                                        psTemp1.setString("rateListCode", keyValue);
                                        psTemp1.executeUpdate();
                                        psTemp1.close();

                                    } else {
                                        sqlQuery = "select applicableFrom,applicableTo "
                                                + "FROM patientServicePriceList "
                                                + "where applicableFrom > :applicableFrom "
                                                + "and entryFrom = :entryFrom "
                                                + "and site_Code = :site_Code "
                                                + "and rateListCode = :rateListCode and (organizationCode is null or consultantCode is null)"
                                                + "group by applicableFrom,applicableTo "
                                                + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                        psTemp1.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                                        psTemp1.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
                                        psTemp1.setString("site_Code", siteCode);
                                        psTemp1.setString("rateListCode", keyValue);

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
                                    sqlQuery = "update patientServicePriceList set "
                                            + "applicableTo = :applicableTo "
                                            + "where applicableFrom = :applicableFrom "
                                            + "and entryFrom = :entryFrom "
                                            + "and site_Code = :site_Code "
                                            + "and rateListCode = :rateListCode and (organizationCode is null or consultantCode is null)";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setDate("applicableFrom", DmServer.convertDate(maxApplicableDate));
                                    psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableDate))));
                                    psTemp.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
                                    psTemp.setString("site_Code", siteCode);
                                    psTemp.setString("rateListCode", keyValue);
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                            count++;
                        }
                        sqlQuery = "delete from patientServicePriceList "
                                + "where applicableFrom = :applicableFrom "
                                + "and entryFrom = :entryFrom "
                                + "and site_Code = :site_Code "
                                + "and rateListCode = :rateListCode "
                                + "and (organizationCode is null or consultantCode is null)"
                                + "and serviceCode = :serviceCode ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                        psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.RATE_LIST_MASTER.toInt()));
                        psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                        psTemp.setString("rateListCode", keyValue);
//                        psTemp.setString("organizationCode", keyValue);
                        psTemp.setString("serviceCode", csvMdl.getServiceCode());
                        int result = psTemp.executeUpdate();
                        psTemp.close();
                        if (!hmRateList.containsKey(csvMdl.getServiceCode())) {
                            lstServicePriceTemp = new ArrayList<ServicePriceList_Mdl>();
                            lstServicePriceTemp.add(lstServicePrice.get(lstServicePrice.size() - 1));
                            hmRateList.put(csvMdl.getServiceCode(), lstServicePriceTemp);
                            lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
                        } else if (hmRateList.containsKey(csvMdl.getServiceCode())) {
                            lstServicePriceTemp = hmRateList.get(csvMdl.getServiceCode());
                            lstServicePriceTemp.addAll(lstServicePrice);
                            hmRateList.replace(csvMdl.getServiceCode(), hmRateList.get(csvMdl.getServiceCode()), lstServicePriceTemp);
                            lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
                        }
                    } else {
                        noOfBug++;
                    }
                }
                if (!hmRateList.isEmpty()) {
                    for (String serviceCode : hmRateList.keySet()) {
                        STServer.savePriceList(applicableDate, PriceListEntryFrom.RATE_LIST_MASTER, serviceCode, keyValue, DmServer.getSsnSiteCode(this), hmRateList.get(serviceCode), cnTemp);
                    }
                }
            }

            spBegin.commit();
            dmrTemp.setData(keyValue);
            dmrTemp.setStatus(true);
            dmrTemp.setValue1(directoryPath);
            dmrTemp.setValue2("Rate_List.log");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineSaleOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (MedicineSaleOn medSale : MedicineSaleOn.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(medSale.getId()).toString());
                listNode.setSharedKeyFieldName(medSale.getValue());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRateListType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (RateListType type : RateListType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(type.getId()).toString());
                listNode.setSharedKeyFieldName(type.getText());
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
