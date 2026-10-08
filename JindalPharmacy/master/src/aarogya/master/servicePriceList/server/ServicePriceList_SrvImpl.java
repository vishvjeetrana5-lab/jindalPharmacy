package aarogya.master.servicePriceList.server;

import aarogya.master.servicePriceList.shared.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ServicePriceList_SrvImpl extends DmRSSPrivate implements ServicePriceList_Srv {

    class CSV_Mdl {

        private String organizationName, serviceName, serviceCode, serviceCategory, serviceCategoryCode, roomCategory, roomCategoryCode,
                organizationServiceName, organizationServiceCode, rate, discount, discountPercentage;

        public CSV_Mdl() {
        }

        public String getOrganizationName() {
            return organizationName;
        }

        public void setOrganizationName(String organizationName) {
            this.organizationName = organizationName;
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

        public String getOrganizationServiceName() {
            return organizationServiceName;
        }

        public void setOrganizationServiceName(String organizationServiceName) {
            this.organizationServiceName = organizationServiceName;
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

        public String getOrganizationServiceCode() {
            return organizationServiceCode;
        }

        public void setOrganizationServiceCode(String organizationServiceCode) {
            this.organizationServiceCode = organizationServiceCode;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Objects.hashCode(this.organizationName);
            hash = 29 * hash + Objects.hashCode(this.serviceName);
            hash = 29 * hash + Objects.hashCode(this.serviceCategory);
            hash = 29 * hash + Objects.hashCode(this.roomCategory);
            hash = 29 * hash + Objects.hashCode(this.organizationServiceName);
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
            if (!Objects.equals(this.organizationName, other.organizationName)) {
                return false;
            }
            if (!Objects.equals(this.serviceName, other.serviceName)) {
                return false;
            }
            if (!Objects.equals(this.serviceCategory, other.serviceCategory)) {
                return false;
            }
            if (!Objects.equals(this.roomCategory, other.roomCategory)) {
                return false;
            }
            if (!Objects.equals(this.organizationServiceName, other.organizationServiceName)) {
                return false;
            }
            return true;
        }
    }

    @Override
    public DmResult<ServicePriceMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ServicePriceMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ServicePriceMaster_Mdl mrTemp = new ServicePriceMaster_Mdl();
        GrdCategoryDiscount_Mdl node = null;
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  om.subCode, sg.name, om.shortName, pspl.applicableFrom, om.medicineSaleOn, om.serviceDiscountPer, om.testDiscountPer, "
                    + "		om.feesDiscountPer, om.pharmacyDiscountPer, om.isPharmacyDiscount, sg.isActive, om.isTPA "
                    + "		from organizationMast om "
                    + "		left join subGroup sg on sg.subCode = om.subCode "
                    + "		left join ( "
                    + "			select  max(organizationCode) as organizationCode, max(applicableFrom) as applicableFrom "
                    + "				from patientServicePriceList pspl "
                    + "				where entryFrom = :entryFrom and site_code = :siteCode and organizationCode = :code "
                    + "				and applicableFrom <= cast(:uEntDt as date) "
                    + "				group by organizationCode "
                    + "		) pspl on pspl.organizationCode = om.subCode  "
                    + "		where om.subCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.ORGANIZATION_MASTER.toInt()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setOrganizationCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setActive(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 1);
                mrTemp.setAllowPharmacyDiscount(DmShared.iNull(rsTemp.getString("isPharmacyDiscount")).intValue() == 1);
                mrTemp.setOrgnizationType(DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 0 ? "General"
                        : (DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 1 ? "TPA"
                        : (DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 2 ? "Collection Center" : "")));
                mrTemp.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));
                mrTemp.setFeesDiscountPer(DmServer.vNull(rsTemp.getString("feesDiscountPer"), "0.00"));
                mrTemp.setPharmacyDiscountPer(DmServer.vNull(rsTemp.getString("pharmacyDiscountPer"), "0.00"));
                mrTemp.setServiceDiscountPer(DmServer.vNull(rsTemp.getString("ServiceDiscountPer"), "0.00"));
                mrTemp.setTestDiscountPer(DmServer.vNull(rsTemp.getString("TestDiscountPer"), "0.00"));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setApplicableDate(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                mrTemp.setSiteCode(DmServer.getSsnSiteCode(this));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  pscm.code, pscm.name, pscd.discountPer, pscd.organizationCode "
                    + "		from patientServiceCatMast pscm "
                    + "		left join patientServiceCatDiscount pscd on pscd.serviceCategoryCode = pscm.code "
                    + "		where isnull(pscm.filterNature, 0) <> 4 and isnull(pscd.organizationCode, :organization) = :organization "
                    + "		and isnull(pscd.site_code, :siteCode) = :siteCode "
                    + "		order by pscm.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organization", strKey);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdCategoryDiscount_Mdl();
                node.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                node.setOrgnaizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
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
    public DmResult<String> savePopup(ServicePriceMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery, siteCode = DmServer.getSsnSiteCode(this);
        dmrTemp.setData("");
        try {
            ServicePriceList_Vld.chkSavePercent(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (mrTemp.getOrganizationCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "update organizationMast set "
                    + "	medicineSaleOn = :medicineSaleOn, "
                    + "	serviceDiscountPer = :serviceDiscountPer, "
                    + "	testDiscountPer = :testDiscountPer, "
                    + "	feesDiscountPer = :feesDiscountPer, "
                    + "	pharmacyDiscountPer = :pharmacyDiscountPer, "
                    + "	isPharmacyDiscount = :isPharmacyDiscount "
                    + "	where subCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getOrganizationCode()));
            psTemp.setInt("isPharmacyDiscount", mrTemp.isAllowPharmacyDiscount() ? 1 : 0);
            psTemp.setString("serviceDiscountPer", DmServer.vNull(mrTemp.getServiceDiscountPer(), "0.00"));
            psTemp.setString("testDiscountPer", DmServer.vNull(mrTemp.getTestDiscountPer(), "0.00"));
            psTemp.setString("feesDiscountPer", DmServer.vNull(mrTemp.getFeesDiscountPer(), "0.00"));
            psTemp.setString("pharmacyDiscountPer", DmServer.vNull(mrTemp.getPharmacyDiscountPer(), "0.00"));
            psTemp.setInt("medicineSaleOn", mrTemp.getMedicineSaleOn().getId());
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Delete records from patientServiceCatDiscount  >==============================*/
            sqlQuery = "delete from patientServiceCatDiscount where organizationCode = :organizationCode and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getOrganizationCode()).toString());
            psTemp.setString("site_Code", siteCode);
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdCategoryDiscount_Mdl node : mrTemp.getLstServiceCategory()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0 && DmShared.vNull(node.getDiscountPercentage()).doubleValue() != 0) {
                    sqlQuery = "insert into patientServiceCatDiscount (organizationCode, serviceCategoryCode, site_code, discountPer) "
                            + " values (:organizationCode, :serviceCategoryCode, :site_code, :discountPer) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("organizationCode", DmShared.xNull(mrTemp.getOrganizationCode()));
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
            ServicePriceList_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(param.getOrganizationCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "update patientServicePriceList set discountPer = :discountPer "
                    + " where (entryFrom = :entryFrom) and (entryFromNature = 0) and (site_code = :siteCode) and (organizationCode = :organization) "
                    + "	and (applicableFrom = :applicableDate) and (rate > 0) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("organization", DmShared.xNull(param.getOrganizationCode()));
            psTemp.setDate("applicableDate", DmServer.convertDate(param.getApplicableDate()));
            psTemp.setInt("entryFrom", PriceListEntryFrom.ORGANIZATION_MASTER.toInt());
            psTemp.setDouble("discountPer", percentValue);
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(param.getOrganizationCode());
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
        ResultSet rsTemp = null;
        String siteCode = DmServer.getSsnSiteCode(this);
        dmrTemp.setData("");

        try {
            ServicePriceList_Vld.chkSave(param);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(serviceCode).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            STServer.deletePriceList(param.getApplicableDate(), PriceListEntryFrom.ORGANIZATION_MASTER, serviceCode, param.getOrganizationCode(), siteCode, cnTemp);
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
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        try {
            ServicePriceList_Vld.chkSave(param);

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
                lstServicePrice.add(getPriceListMdl(param.getOrganizationCode(), param.getApplicableDate(), param.getServiceCode(),
                        "", mrTemp.getOrganizationServiceCode(), mrTemp.getRate(), mrTemp.getDiscountAmount(), mrTemp.getDiscountPercentage(), mrTemp.getPrintSNo()));
            }
            for (GrdRoomCategory_Mdl categoryMdl : mrTemp.getLstRoomCategory()) {
                if (DmShared.vNull(categoryMdl.getRate()).doubleValue() > 0 || (DmShared.vNull(categoryMdl.getDiscountAmount()).doubleValue() > 0) || (DmShared.vNull(categoryMdl.getDiscountPercentage()).doubleValue() > 0)) {
                    lstServicePrice.add(getPriceListMdl(param.getOrganizationCode(), param.getApplicableDate(), mrTemp.getServiceCode(),
                            categoryMdl.getSharedKeyFieldCode(), mrTemp.getOrganizationServiceCode(), categoryMdl.getRate(), categoryMdl.getDiscountAmount(),
                            categoryMdl.getDiscountPercentage(), ""));
                }
            }

            consFeesService = DmServer.getCodeViaManualCode("CF", "shortName", "patientservicemast", "code", cnTemp);
            if (DmShared.iNull(consFeesService).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid 'CF' code. Create 'CF' Code");
            }

            if (lstServicePrice.size() > 0) {
                STServer.savePriceList(param.getApplicableDate(), PriceListEntryFrom.ORGANIZATION_MASTER, mrTemp.getSharedKeyFieldCode(), param.getOrganizationCode(), siteCode, lstServicePrice, cnTemp);
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
            String organizationServiceCode, String rate, String discountAmount, String discountPercent, String printSno) {
        ServicePriceList_Mdl priceList;
        priceList = new ServicePriceList_Mdl();
        priceList.setOrganizationCode(keyCode);
        priceList.setDate(applicableDate);
        priceList.setPriceListEntryFrom(PriceListEntryFrom.ORGANIZATION_MASTER);
        priceList.setPriceListNature(PriceListNature.None);
        priceList.setRate(DmServer.vNull(rate, "0.00"));
        priceList.setDiscAmt(DmServer.vNull(discountAmount, "0.00"));
        priceList.setDiscPercentage(DmServer.vNull(discountPercent, "0.00"));
        priceList.setPrintSNo(printSno);
        priceList.setRoomCatCode(roomCategoryCode);
        priceList.setServiceCode(serviceCode);
        priceList.setOrganizationServiceCode(organizationServiceCode);
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

            sqlQuery = "select  om.subCODE, sg.name, om.shortName, pspl.applicableFrom, om.serviceDiscountPer, om.testDiscountPer, "
                    + "         om.feesDiscountPer, om.insurance, case SG.isActive when '1' then 'Yes' else 'No' end as isActive "
                    + "         from organizationmast om "
                    + "         left join subgroup sg on om.subcode = sg.subCode "
                    + "         left join ( "
                    + "             select  max(organizationCode) as organizationCode, max(applicableFrom) as applicableFrom "
                    + "                     from patientservicepricelist "
                    + "                     where entryFrom = :entryFrom and site_code = :siteCode and applicableFrom <= cast(:uEntDt as date) "
                    + "                     group by organizationCode "
                    + "         ) pspl on om.subcode = pspl.organizationCode ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("entryFrom", String.valueOf(PriceListEntryFrom.ORGANIZATION_MASTER.toInt()));
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setInsurance(DmShared.xNull(rsTemp.getString("insurance")));
                listNode.setServiceDiscountPer(DmServer.vNull(rsTemp.getString("serviceDiscountPer"), "0.00"));
                listNode.setTestDiscountPer(DmServer.vNull(rsTemp.getString("testDiscountPer"), "0.00"));
                listNode.setFeesDiscountPer(DmServer.vNull(rsTemp.getString("feesDiscountPer"), "0.00"));
                listNode.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
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
    public DmResultPageing<List<TxtHServiceTest_Mdl>> getTxtHServiceTest(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHServiceTest_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHServiceTest_Mdl> lstTemp = new ArrayList<>();
        TxtHServiceTest_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select  "
                    + "	psm.Code AS serviceCode,psm.Name AS serviceName,pscm.name AS serviceCategoryName, "
                    + "	psm.categoryCode AS ServiceCategoryCode,psm.serviceNature, "
                    + "	CASE psm.serviceNature "
                    + "	WHEN 0 THEN '" + ServiceNature.SERVICE.getValue() + "' WHEN 1 THEN '" + ServiceNature.PATHOLOGY_TEST.getValue() + "' "
                    + " WHEN 2 THEN '" + ServiceNature.OPERATION.getValue() + "'WHEN 3 THEN '" + ServiceNature.RADIOLOGY_TEST.getValue() + "' "
                    + " ELSE '' END as serviceNatureName    "
                    + "	from patientservicemast psm "
                    + "	left join patientservicecatmast pscm on pscm.code = psm.categoryCode"
                    + " left join patientServiceSiteList as pssl on pssl.code=psm.code "
                    + " where psm.isActive = 1 and pssl.site_Code=:siteCode ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHServiceTest_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceName")));
                listNode.setNature(ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue()));
                listNode.setNatureName(listNode.getNature().getValue());
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

            sqlWithParam.setSQL("select  serviceCode, max(name) as name, max(organizationServiceName) as organizationServiceName, max(name) as category, "
                    + "		max(printSNo) as printSNo, max(rate) as rate, max(discount) as discount, max(discountPer) as discountPer, "
                    + "		max(colorChange) as colorChange, max(serviceType) as serviceType, max(organizationServiceCode) as organizationServiceCode "
                    + "		from ("
                    + "			select  pspl.serviceCode, psm.name, osm.organizationServiceName, pscm.name as category, pspl.printSNo, "
                    + "				pspl.rate, pspl.discount, pspl.discountPer, 1 as colorChange, case psm.serviceNature "
                    + "                         when " + ServiceNature.SERVICE.getId() + " then '" + ServiceNature.SERVICE.getValue() + "'"
                    + "                         when " + ServiceNature.OPERATION.getId() + " then '" + ServiceNature.OPERATION.getValue() + "'"
                    + "                         when " + ServiceNature.PATHOLOGY_TEST.getId() + " then '" + ServiceNature.PATHOLOGY_TEST.getValue() + "'"
                    + "                         when " + ServiceNature.RADIOLOGY_TEST.getId() + " then '" + ServiceNature.RADIOLOGY_TEST.getValue() + "'"
                    + "                         end as serviceType, pspl.organizationServiceCode "
                    + "				from ("
                    + "					select  pspl.serviceCode, pspl.organizationServiceCode, max(pspl.printSNo) as printSNo,"
                    + "						max(case when roomCatCode is null then pspl.rate else 0 end) as rate, "
                    + "						max(case when roomCatCode is null then pspl.discount else 0 end) as discount, "
                    + "						max(case when roomCatCode is null then pspl.discountPer else 0 end) as discountPer"
                    + "						from patientServicePriceList pspl "
                    + "						where organizationCode = :organization and entryFrom = :entryFrom and "
                    + "						site_Code = :siteCode and applicableFrom = :applicableDate "
                    + "						group by pspl.organizationServiceCode, pspl.serviceCode "
                    + "				) as pspl "
                    + "				left join patientServiceSiteList pssl on pssl.code = pspl.serviceCode "
                    + "				left join organizationServiceMast osm on osm.code = pspl.organizationServiceCode "
                    + "				left join patientServiceMast psm on psm.code = pspl.serviceCode "
                    + "				left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "				where pssl.site_Code = :siteCode and psm.isActive = 1 and psm.serviceNature <> " + ServiceNature.OVERHEAD_SERVICE.getId() + condition
                    + "			union all "
                    + "			select  psm.code as serviceCode, psm.name, osm.organizationServiceName, pscm.name as category, psm.printSNo, "
                    + "				0.00 as rate, 0.00 as discount, 0.00 as discountPer, 0 as colorChange, case psm.serviceNature"
                    + "                         when " + ServiceNature.SERVICE.getId() + " then '" + ServiceNature.SERVICE.getValue() + "'"
                    + "                         when " + ServiceNature.OPERATION.getId() + " then '" + ServiceNature.OPERATION.getValue() + "'"
                    + "                         when " + ServiceNature.PATHOLOGY_TEST.getId() + " then '" + ServiceNature.PATHOLOGY_TEST.getValue() + "'"
                    + "                         when " + ServiceNature.RADIOLOGY_TEST.getId() + " then '" + ServiceNature.RADIOLOGY_TEST.getValue() + "'"
                    + "                         end as serviceType, osm.organizationServiceCode "
                    + "				from patientServiceMast psm "
                    + "				left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "				left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "				left join (	"
                    + "					select  osd.organizationServiceCode, osm.organizationServiceName, osd.serviceCode "
                    + "						from organizationServiceMast osm "
                    + "						left join organizationServiceMapping osd on osd.organizationServiceCode = osm.code "
                    + "						where osm.organizationCode = :organization "
                    + "				) osm on osm.serviceCode = psm.code "
                    + "				where pssl.site_Code = :siteCode and psm.isActive = 1 and psm.serviceNature <> " + ServiceNature.OVERHEAD_SERVICE.getId() + condition
                    + "		) as tbl"
                    + "		group by tbl.serviceCode, tbl.organizationServiceName");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("organization", DmShared.xNull(param.getOrganizationCode()));
            sqlWithParam.setDate("applicableDate", DmServer.convertDate(param.getApplicableDate()));
            sqlWithParam.setInteger("entryFrom", PriceListEntryFrom.ORGANIZATION_MASTER.toInt());
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
                listNode.setOrganizationServiceCode(DmShared.xNull(rsTemp.getString("organizationServiceCode")));
                listNode.setOrganizationServiceName(DmShared.xNull(rsTemp.getString("organizationServiceName")));
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
                    + "				where pspl.organizationCode = :organization and pspl.entryFrom = :entryFrom and pspl.roomCatCode is not null"
                    + "				and pspl.site_code = :siteCode and pspl.applicableFrom = :applicableDate and pspl.serviceCode = :service "
                    + "		) pspl on pspl.roomCatCode = rcm.code "
                    + "		order by rcm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("organization", DmShared.xNull(param.getOrganizationCode()));
            psTemp.setString("service", DmShared.xNull(param.getServiceCode()));
            psTemp.setDate("applicableDate", DmServer.convertDate(param.getApplicableDate()));
            psTemp.setInt("entryFrom", PriceListEntryFrom.ORGANIZATION_MASTER.toInt());
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
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        File sessionDir;
        String fieldValue, sqlQuery, organizationName = "";
        String directoryPath, lineNoDisplay = "";
        DmCsvReader csvReader = null;
        CSV_Mdl csvMdl = null;
        DmHandelFile handelFileLog = null;
        File tempFile = null;
        boolean flag = false;
        int index = 0, noOfBug = 0;
        List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
        List<ServicePriceList_Mdl> lstServicePriceTemp = new ArrayList<ServicePriceList_Mdl>();
        HashMap<String, List<ServicePriceList_Mdl>> hmRateList = new HashMap<String, List<ServicePriceList_Mdl>>();
        try {
            directoryPath = serverDirectory + File.separatorChar + "Organization_PriceList_Log";

            /*===========================< Make directory if not exits >===========================*/
            tempFile = new File(directoryPath);
            tempFile.delete();
            tempFile.mkdirs();
            tempFile = null;

            handelFileLog = new DmHandelFile(directoryPath + File.separatorChar + "Organization_Price_List.log", true);

            sessionDir = new File(serverDirectory + File.separatorChar + serverFileName);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            organizationName = DmShared.xNull(DmServer.getCodeViaManualCode(keyValue, "subCode", "subGroup", "name", cnTemp));
            if (sessionDir.exists()) {
                csvReader = new DmCsvReader(sessionDir);
                csvReader.readLines();
                DmImportData_Server.checkHeader(new String[]{"organizationName", "serviceName", "serviceCategory", "serviceNature", "organizationServiceName", "roomCategory",
                    "rate", "discountPer", "discount"}, csvReader, handelFileLog);
                index = 0;

                for (DmDynamicGridRow ddgr : csvReader.getRows()) {
                    csvMdl = new CSV_Mdl();
                    index++;
                    lineNoDisplay = "line No. " + DmShared.iNull(index).toString() + " :";
                    for (String columnName : csvReader.getHeaders()) {
                        fieldValue = ddgr.getCells().get(columnName.toLowerCase()) + "";
                        if (columnName.equalsIgnoreCase("organizationName")) {
                            flag = validateField(columnName, fieldValue, true, false, false, lineNoDisplay, handelFileLog);
                            if (DmShared.xNull(fieldValue).trim().equals("")) {
                                flag = false;
                                handelFileLog.writeLine(lineNoDisplay + " not inserted. Organization Name(" + DmShared.xNull(fieldValue).trim() + ") not found.");
                            } else {
                                csvMdl.setOrganizationName(DmShared.xNull(fieldValue));
                                if (!organizationName.trim().equalsIgnoreCase(csvMdl.getOrganizationName().trim())) {
                                    throw new DmException(0, lineNoDisplay + "Organization not matched. [" + csvMdl.getOrganizationName().trim() + "]");
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
                                csvMdl.setServiceCategoryCode(DmServer.getCodeViaManualCode(DmShared.removeSpace(fieldValue), "name", "patientServiceCatMast", "code", cnTemp));
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
                                csvMdl.setServiceCode(DmServer.getCodeViaManualCode(DmShared.removeSpace(fieldValue), "name", "patientServiceMast", "code", cnTemp));

                            }
                            if (!flag) {
                                break;
                            }
                        } else if (columnName.equalsIgnoreCase("organizationServiceName")) {
                            flag = validateField(columnName, fieldValue, false, false, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setOrganizationServiceName(DmShared.xNull(fieldValue));
                            sqlQuery = "select  code"
                                    + "		from organizationServiceMast "
                                    + "		where organizationCode = :organization and organizationServiceName = :serviceName ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("organization", keyValue);
                            psTemp.setString("serviceName", DmShared.xNull(fieldValue));
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                csvMdl.setOrganizationServiceCode(DmShared.xNull(rsTemp.getString("code")));
                            }
                            rsTemp.close();
                            psTemp.close();
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
                    if (flag) {
                        lstServicePrice.add(getPriceListMdl(keyValue, applicableDate, csvMdl.getServiceCode(), csvMdl.getRoomCategoryCode(),
                                csvMdl.getOrganizationServiceCode(), csvMdl.getRate(), csvMdl.getDiscount(), csvMdl.getDiscountPercentage(), ""));
                        sqlQuery = "delete from patientServicePriceList "
                                + "where applicableFrom = :applicableFrom "
                                + "and entryFrom = :entryFrom "
                                + "and site_Code = :site_Code "
                                + "and organizationCode = :organizationCode "
                                + "and serviceCode = :serviceCode ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                        psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.ORGANIZATION_MASTER.toInt()));
                        psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                        psTemp.setString("organizationCode", keyValue);
                        psTemp.setString("serviceCode", csvMdl.getServiceCode());
                        psTemp.executeUpdate();
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
                        STServer.savePriceList(applicableDate, PriceListEntryFrom.ORGANIZATION_MASTER, serviceCode, keyValue, DmServer.getSsnSiteCode(this), hmRateList.get(serviceCode), cnTemp);
                    }
                }
            }
            dmrTemp.setData(keyValue);
            dmrTemp.setStatus(true);
            dmrTemp.setValue1(directoryPath);
            dmrTemp.setValue2("Organization_Price_List.log");
            dmrTemp.setErrorNumber(noOfBug);
            spBegin.commit();
            cnTemp.close();
        } catch (Exception e) {
            try {
                spBegin.rollBack();
                handelFileLog.writeLine(e.getMessage());
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            } catch (Exception ex) {
                dmrTemp.setStatus(false);
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            }
        } finally {
            try {
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
}
