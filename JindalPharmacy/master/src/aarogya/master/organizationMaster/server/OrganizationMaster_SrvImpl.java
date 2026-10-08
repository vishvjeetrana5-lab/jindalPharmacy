package aarogya.master.organizationMaster.server;

import aarogya.selfToolkit.stImportData.server.ImportOrganization;
import aarogya.master.organizationMaster.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrganizationMaster_SrvImpl extends DmRSSPrivate implements OrganizationMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(boolean isOrganization) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String condition = "";
        try {
            if (isOrganization) {
                condition = " where isTPA in (0,1)";
            } else {
                condition = " where isTPA = 2";
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select om.subCode from organizationmast om Left Join subGroup sg on om.subCode=sg.subCode " + condition + " order by sg.name;");
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountGivenBy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "  select sg.subCode as CODE, sg.name \n"
                    + "from subGroup sg \n"
                    + "left join subGroupNature sgn on sgn.code = sg.nature\n"
                    + "where sgn.manualCode in ('LG','BK','CN','BR','OT','EM','AG')";
            sqlWithParam.setSQL(sqlQuery);
           // sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            //    sqlWithParam.setString("v_Type", voucherType);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select code, name from discountReasonMast ";
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
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select sg.name From organizationmast om Left Join subGroup sg on om.subCode=sg.subCode  ");
            helper.addSort("name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select shortName From organizationmast ");
            helper.addSort("shortName", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResult<OrganizationMaster_Mdl> getMoveRec(String strKey) {
        DmResult<OrganizationMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        OrganizationMaster_Mdl mrTemp = new OrganizationMaster_Mdl();
        DiscountPercent_Mdl discountMdl = new DiscountPercent_Mdl();
        GrdContactPerson_Mdl contact_Mdl;
        GrdRateList_Mdl nodeRateList_Mdl;
        GrdDiscountPercent_Mdl discountPercent_Mdl = new GrdDiscountPercent_Mdl();
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  om.subCode, sg.name, sg.manualCode, sg.gsTin, om.isTPA, sg.eMail, sg.mobile, sg.remark, om.billingPer, sg.isActive, "
                    + "		om.agreementDate, om.agreementNo, om.revisionDate, om.tariffRevisionDate, om.tdsAc, tds.name as tdsAcName, sg.u_Name, "
                    + "	        sg.preparedBy, rlm.name rateListName, rlm.code "
                    + "		rateListCode, rlm.rateListType, pspl.ApplicableFromDate, om.medicineSaleOn, om.pharmacyDiscountPer, om.isPharmacyDiscount, "
                    + "		pspl.sNo, sg.guiConfigCode , om.tpaControlAc , om.excessecurityAc ,om.discountReason,om.discountGivenBy,sgDis.name as discountGivenByText,"
                    + "         tpaCAc.name as tpaControlAcName , excsAc.name as excessecurityAcName   "
                    + "		from organizationMast om "
                    + "		left join subGroup sg on sg.subCode = om.subCode "
                    + "		left join subGroup tds on tds.subCode = om.tdsAc "
                    + "		left join subGroup tpaCAc on tpaCAc.subCode = om.tpaControlAc "
                    + "		left join subGroup excsAc on excsAc.subCode = om.excessecurityAc "
                    + "		left join subGroup sgDis on sgDis.subCode = om.discountGivenBy "
                    + "		left join rateListMast rlm on rlm.code =  om.rateListCode\n"
                    + "		left join organizationToRateListMapping pspl on pspl.rateListCode = om.rateListCode and om.subCode = pspl.organizationCode\n"
                    + "		where om.subCode = :code "
                    + "         order by pspl.applicableFromDate desc";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setGstinNo(DmShared.xNull(rsTemp.getString("gsTin")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setTarrifRevisionDate(DmShared.dNull(rsTemp.getDate("tariffRevisionDate")));
                mrTemp.setIsNatureTag(DmShared.xNull(rsTemp.getString("isTPA")));
                mrTemp.setIsNatureText(DmShared.xNull(rsTemp.getString("isTPA")).equalsIgnoreCase("0") ? "General"
                        : (DmShared.xNull(rsTemp.getString("isTPA")).equalsIgnoreCase("1") ? "TPA"
                        : (DmShared.xNull(rsTemp.getString("isTPA")).equalsIgnoreCase("2") ? "Collection Center" : "")));
                mrTemp.setActiveTag(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setActiveText(DmShared.xNull(rsTemp.getString("isActive")).equalsIgnoreCase("1") ? "Yes" : "No");

                mrTemp.setTdsAcTag(DmShared.xNull(rsTemp.getString("tdsAc")));
                mrTemp.setTdsAcText(DmShared.xNull(rsTemp.getString("tdsAcName")));

                mrTemp.setTpaControlAcTag(DmShared.xNull(rsTemp.getString("tpaControlAc")));
                mrTemp.setTpaControlAcText(DmShared.xNull(rsTemp.getString("tpaControlAcName")));

                mrTemp.setExcessecurityAcTag(DmShared.xNull(rsTemp.getString("excessecurityAc")));
                mrTemp.setExcessecurityAcText(DmShared.xNull(rsTemp.getString("excessecurityAcName")));
                mrTemp.setDiscountReason(DmShared.xNull(rsTemp.getString("discountReason")));
                mrTemp.setDiscountGivenByTag(DmShared.xNull(rsTemp.getString("discountGivenBy")));
                mrTemp.setDiscountGivenByText(DmShared.xNull(rsTemp.getString("discountGivenByText")));

                mrTemp.setAgreementNo(DmShared.xNull(rsTemp.getString("agreementNo")));
                mrTemp.setAgreementDate(DmShared.dNull(rsTemp.getDate("agreementDate")));
                mrTemp.setRevisionDate(DmShared.dNull(rsTemp.getDate("revisionDate")));
                mrTemp.setBiilingPer(DmServer.vNull(rsTemp.getString("billingPer"), "0.00"));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));

//                mrTemp.setOrganizationGroup(DmShared.xNull(rsTemp.getString("orgGroupName")));
//                mrTemp.setOrganizationGroupCode(DmShared.iNull(rsTemp.getString("orgGroupCode")).toString());
                mrTemp.setRateListName(DmShared.xNull(rsTemp.getString("rateListName")));
                mrTemp.setRateListCode(DmShared.iNull(rsTemp.getString("rateListCode")).toString());
                mrTemp.setRateList_Sno(DmShared.xNull(rsTemp.getString("sno")));
                mrTemp.setApplicableDate(DmShared.dNull(rsTemp.getDate("ApplicableFromDate")));
                discountMdl.setPharmacyDiscountAllow(DmShared.iNull(rsTemp.getString("isPharmacyDiscount")).intValue() == 1);
                discountMdl.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));
                discountMdl.setPharmacyDiscountPercentage(DmServer.vNull(rsTemp.getString("pharmacyDiscountPer"), "0.00"));

                mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(DmShared.iNull(rsTemp.getString("guiConfigCode")).toString(), DmShared.iNull(rsTemp.getString("subcode")).toString(), DmServer.getSsnSiteCode(this), cnTemp));
                mrTemp.setGuiConfigCode(DmShared.iNull(rsTemp.getString("guiConfigCode")).toString());

            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select organizationCode from patientRegistration pr where pr.organizationCode = :CODE ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("CODE", strKey);
            rsTemp = psTemp.executeQuery();
            mrTemp.setTPA(rsTemp.next());
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select serviceNumber,percentage from OperationPricePercent where organizationCode = :organizationCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", strKey);
            rsTemp = psTemp.executeQuery();
            discountPercent_Mdl.setOrganizationCode(strKey);
            while (rsTemp.next()) {
                GrdPercentage_Mdl percentage = new GrdPercentage_Mdl();
                percentage.setServiceNo(DmShared.xNull(rsTemp.getString("serviceNumber")));
                percentage.setPercentage(DmShared.xNull(rsTemp.getString("percentage")));
                discountPercent_Mdl.getPercentages().add(percentage);
            }
            mrTemp.setDiscountPercentMdl(discountPercent_Mdl);
            rsTemp.close();
            psTemp.close();

            /*=======================================< CONTACT DETAIL >=========================================*/
            sqlQuery = "SELECT code,contactPerson,mobile,phone AS landLineNo,designation,isDefault FROM addressBook "
                    + " where subCode= :code "
                    + "order by contactPerson ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                contact_Mdl = new GrdContactPerson_Mdl();
                contact_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                contact_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("contactPerson")));
                contact_Mdl.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));
                contact_Mdl.setLandlineNo(DmShared.xNull(rsTemp.getString("landLineNo")));
                contact_Mdl.setDesignation(DmShared.xNull(rsTemp.getString("designation")));
                contact_Mdl.setIsChecked(DmShared.xNull(rsTemp.getString("isDefault")));
                mrTemp.getListContectPerson().add(contact_Mdl);
            }
            rsTemp.close();
            psTemp.close();
            /*=======================================< Rate List Mapping DETAIL >=========================================*/
            sqlQuery = "select  rlm.name rateListName, rlm.rateListType, orlm.ApplicableFromDate, orlm.sNo, orlm.rateListCode "
                    + "         from organizationToRateListMapping orlm "
                    + "         left join rateListMast rlm on rlm.code = orlm.rateListcode"
                    + "         where orlm.organizationCode =:organizationCode "
                    + "         order by ApplicableFromDate Desc, rateListName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                nodeRateList_Mdl = new GrdRateList_Mdl();
                nodeRateList_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("rateListCode")));
                nodeRateList_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("rateListName")));
                nodeRateList_Mdl.setApplicableFrom(DmShared.dNull(rsTemp.getDate("ApplicableFromDate")));
                nodeRateList_Mdl.setRateListType(DmShared.xNull(RateListType.getViaKey(rsTemp.getInt("rateListType")).getText()));
                mrTemp.getLstRateList().add(nodeRateList_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            serviceGrdDetails(cnTemp, mrTemp.getLstService(), strKey, mrTemp.getRateListCode(), mrTemp.getApplicableDate());
            discountMdl.getLstCategoryDiscount().addAll(getServiceWiseDiscount(cnTemp, strKey, mrTemp.getRateListCode()));

            mrTemp.setDiscountMdl(discountMdl);

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

    private List<GrdServices_Mdl> serviceGrdDetails(Connection cnTemp, List<GrdServices_Mdl> mrTemp, String orgCode, String rateListCode, String applcableDate) {
        String previousServiceCode = "", sqlQuery = " ";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        GrdServices_Mdl service = new GrdServices_Mdl();
        GrdRoomCategory_Mdl roomCat = new GrdRoomCategory_Mdl();
        sqlQuery = "  select  max(pspl.serviceCode) as serviceCode, max(psm.name) as service, max(pspl.rate) as rate, pspl.roomCatCode,          "
                + "           max(pscm.code) servicecatCode, max(pscm.name) serivceCatName,  max(pspl.discount) discount, max(pspl.discountPer) discountPer, max(pspl.printSNo) printSno,          "
                + "           max(rcm.name) as roomCat, max(pspl.organizationServiceCode)organizationServiceCode, max(orgSM.organizationServiceName) orgServiceName  "
                + "             from patientservicepricelist pspl  "
                + "             left join patientservicemast psm ON pspl.serviceCode = psm.code  "
                + "             left join patientServiceCatmast pscm on pscm.code = psm.categoryCode"
                + "             left join siteMast AS sm ON sm.code=pspl.site_Code"
                + "             left join roomcatmast rcm ON pspl.roomCatCode = rcm.code  "
                + "             left Join organizationServiceMast orgSM on orgSM.code = pspl.organizationServiceCode "
                + "      where pspl.organizationCode = :organizationCode and pspl.site_code = :siteCode and pspl.EntryFrom = :entryFrom "
                + "             and pspl.entryFromNature = '0' and pspl.applicableFrom = :applicableFrom and pspl.ratelistCode=:rateListCode  "
                + "      group by pspl.serviceCode, pspl.roomCatCode  "
                + "      order by max(psm.name), max(rcm.name) ";
        try {
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(orgCode).toString());
            psTemp.setString("rateListCode", DmShared.iNull(rateListCode).toString());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.ORGANIZATION_MASTER.toInt()).trim());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applcableDate));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("serviceCode")).equalsIgnoreCase(previousServiceCode)) {
                    service = new GrdServices_Mdl();
                    mrTemp.add(service);
                }

                service.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                service.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("service")));
                service.setServiceCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                service.setCategoryCode(DmShared.xNull(rsTemp.getString("servicecatCode")));
                service.setCategoryText(DmShared.xNull(rsTemp.getString("serivceCatName")));
                service.setOrganizationServiceCode(DmShared.xNull(rsTemp.getString("organizationServiceCode")));
                service.setOrganizationServiceName(DmShared.xNull(rsTemp.getString("orgServiceName")));

                if (DmShared.xNull(rsTemp.getString("roomCatCode")).trim().equalsIgnoreCase("")) {
                    service.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                    service.setPrintSNo(DmShared.iNull(rsTemp.getString("printSno")).toString());
                    service.setDiscountAmount(DmServer.vNull(rsTemp.getString("discount"), "0.00"));
                    service.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                } else {
                    roomCat = new GrdRoomCategory_Mdl();

                    roomCat.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                    roomCat.setDiscountAmount(DmServer.vNull(rsTemp.getString("discount"), "0.00"));
                    roomCat.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                    roomCat.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCatCode")));
                    roomCat.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomCat")));
                    service.getLstRoomCategory().add(roomCat);
                }
                previousServiceCode = DmShared.xNull(rsTemp.getString("serviceCode"));
            }
            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return mrTemp;
    }

    @Override
    public DmResult<String> saveRecord(OrganizationMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, blnMapp = true;
        String sqlQuery, filterNature = "", acGroupOrganizationCode = "", tpaMessage = "";
        ResultSet rsTemp = null, rsTemp1 = null;
        String siteCode = DmServer.getSsnSiteCode(this);
        dmrTemp.setData("");
        try {
            OrganizationMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setShortName(DmShared.xNull(mrTemp.getShortName()).trim().replace(" ", ""));
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setAgreementNo(DmShared.removeSpace(mrTemp.getAgreementNo()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "SubGroup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(*) As Cnt From SubGroup Where Name = :name and SubCode<> :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", mrTemp.getSharedKeyFieldName());
            psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Name Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select Count(*) As Cnt From organizationmast Where shortName =:shortName and subcode <>:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select om.isTPA from organizationMast om where om.subCode = :Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isTPA")).intValue() != DmShared.iNull(mrTemp.getIsNatureTag()).intValue()) {
                    sqlQuery = "select organizationCode from patientRegistration pr where pr.organizationCode = :Code and pr.isCancelled = 0 ";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    rsTemp1 = psTemp1.executeQuery();
                    if (rsTemp1.next()) {
                        mrTemp.setIsNatureTag(DmShared.xNull(rsTemp.getString("isTPA")));
                        tpaMessage = " 'Nature' value not change beacause organization in use.";
                    }
                    rsTemp1.close();
                    psTemp1.close();
                }
            }
            rsTemp.close();
            psTemp.close();

            acGroupOrganizationCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupOrganizationCode", cnTemp);

            filterNature = DmServer.getCodeViaManualCode("OR", "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                sqlQuery = "insert into subGroup(subCode, name, groupCode, manualCode, nature, filterNature, u_EntDt, site_Code, displayName, preparedBy, "
                        + "         isActive, gsTin, guiConfigCode) "
                        + " values (:subCode, :name, :groupCode, :manualCode, :nature, :filterNature, :uEntDt, :siteCode, :displayName, :userName, "
                        + "         :isActive, :gsTin, :guiConfigCode) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getGstinNo()));
                psTemp.setString("groupCode", DmShared.xNull(acGroupOrganizationCode));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("filterNature", DmShared.xNull(filterNature));
                psTemp.setString("nature", DmShared.xNull(filterNature));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("isActive", DmShared.xNull(mrTemp.getActiveTag()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into organizationMast(shortName, subCode, remark, isTPA, tdsAc, agreementNo, agreementDate, "
                        + "         revisionDate, tariffRevisionDate, billingPer, mobileNo, email,  rateListCode , tpaControlAc , excessecurityAc,discountReason,discountGivenBy ) "
                        + " Values (:shortName, :subCode, :remark, :isTPA, :tdsAc, :agreementNo, :agreementDate, :revisionDate, "
                        + "         :tariffRevisionDate, :billingPer, :mobileNo, :email, :rateListCode  , :tpaControlAc , :excessecurityAc,:discountReason,:discountGivenBy  ) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark().trim()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("isTPA", DmShared.xNull(mrTemp.getIsNatureTag()));

                psTemp.setString("tdsAc", DmShared.foriegnKey(mrTemp.getTdsAcTag()));

                psTemp.setString("tpaControlAc", DmShared.foriegnKey(mrTemp.getTpaControlAcTag()));
                psTemp.setString("excessecurityAc", DmShared.foriegnKey(mrTemp.getExcessecurityAcTag()));
                psTemp.setString("discountReason", DmShared.xNull(mrTemp.getDiscountReason()));
                psTemp.setString("discountGivenBy", DmShared.foriegnKey(mrTemp.getDiscountGivenByTag()));

                psTemp.setString("agreementNo", DmShared.xNull(mrTemp.getAgreementNo()).trim());
                psTemp.setDate("agreementDate", DmServer.convertDate(mrTemp.getAgreementDate()));
                psTemp.setDate("tariffRevisionDate", DmServer.convertDate(mrTemp.getTarrifRevisionDate()));
                psTemp.setDate("revisionDate", DmServer.convertDate(mrTemp.getRevisionDate()));
                psTemp.setString("billingPer", DmServer.vNull(mrTemp.getBiilingPer(), "0.00"));
                psTemp.setString("mobileNo", DmShared.xNull(mrTemp.getMobileNo()));
                psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("rateListCode", DmShared.foriegnKey(mrTemp.getRateListCode()));

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into subGroupSiteList (subCode, site_Code) "
                        + " values (:subCode, :siteCode) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(siteCode));
                psTemp.executeUpdate();
                psTemp.close();
            } else {

                sqlQuery = "Update SubGroup Set "
                        + " Name = :name, "
                        + " ManualCode = :ManualCode, "
                        + " gsTin = :gsTin, "
                        + " u_Name = :userName, "
                        + " displayName = :displayName, "
                        + " guiConfigCode = :guiConfigCode, "
                        + " isActive = :isActive, "
                        + " u_EntDt = :uEntDt "
                        + " Where SubCode = :SubCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getGstinNo()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("isActive", DmShared.xNull(mrTemp.getActiveTag()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "Update organizationmast Set "
                        + "shortName = :shortName, "
                        + "remark = :remark, "
                        + "isTPA = :isTPA,"
                        + "tdsAc = :tdsAc, "
                        + "tpaControlAc = :tpaControlAc , "
                        + "excessecurityAc = :excessecurityAc ,"
                        + "discountReason = :discountReason,"
                        + "discountGivenBy = :discountGivenBy, "
                        + "agreementNo = :agreementNo, "
                        + "agreementDate = :agreementDate, "
                        + "tariffRevisionDate= :tariffRevisionDate, "
                        + "revisionDate = :revisionDate, "
                        + " billingPer = :billingPer ,"
                        + " mobileNo = :mobileNo, "
                        + " email = :email, "
                        //                        + " organizationGroup = :organizationGroup,"
                        + " rateListCode = :rateListCode,"
                        + " medicineSaleOn = :medicineSaleOn,"
                        + " pharmacyDiscountPer = :pharmacyDiscountPer,"
                        + " isPharmacyDiscount = :isPharmacyDiscount"
                        + " Where subcode = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark().trim()));
                psTemp.setString("isTPA", DmShared.xNull(mrTemp.getIsNatureTag()));
                psTemp.setString("tdsAc", DmShared.foriegnKey(mrTemp.getTdsAcTag()));

                psTemp.setString("tpaControlAc", DmShared.foriegnKey(mrTemp.getTpaControlAcTag()));
                psTemp.setString("excessecurityAc", DmShared.foriegnKey(mrTemp.getExcessecurityAcTag()));
                psTemp.setString("discountReason", DmShared.xNull(mrTemp.getDiscountReason()));
                psTemp.setString("discountGivenBy", DmShared.foriegnKey(mrTemp.getDiscountGivenByTag()));

                psTemp.setString("agreementNo", DmShared.xNull(mrTemp.getAgreementNo()));
                psTemp.setDate("agreementDate", DmServer.convertDate(mrTemp.getAgreementDate()));
                psTemp.setDate("revisionDate", DmServer.convertDate(mrTemp.getRevisionDate()));
                psTemp.setDate("tariffRevisionDate", DmServer.convertDate(mrTemp.getTarrifRevisionDate()));
                psTemp.setString("billingPer", DmServer.vNull(mrTemp.getBiilingPer(), "0.00"));
                psTemp.setString("mobileNo", DmShared.xNull(mrTemp.getMobileNo()));
                psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
//                psTemp.setString("organizationGroup", DmShared.foriegnKey(mrTemp.getOrganizationGroupCode()));
                psTemp.setString("rateListCode", DmShared.foriegnKey(mrTemp.getRateListCode()));
                psTemp.setInt("isPharmacyDiscount", mrTemp.getDiscountMdl().isPharmacyDiscountAllow() ? 1 : 0);
                psTemp.setString("pharmacyDiscountPer", DmServer.vNull(mrTemp.getDiscountMdl().getPharmacyDiscountPercentage(), "0.00"));
                psTemp.setInt("medicineSaleOn", mrTemp.getDiscountMdl().getMedicineSaleOn().getId());

                psTemp.executeUpdate();
                psTemp.close();
            }
            //===============================<<Rate list Mapping >>==================================================
            //                                << Check is last ratelistCode is same >>
            sqlQuery = "select  rateListCode\n"
                    + "		from organizationToRateListMapping\n"
                    + "		where organizationCode = :code and rateListCode = :rateListCode and applicableFromDate = :applicableDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("rateListcode", DmShared.foriegnKey(mrTemp.getRateListCode()));
            psTemp.setDate("applicableDate", DmServer.convertDate(mrTemp.getApplicableDate()));
            rsTemp = psTemp.executeQuery();
            blnMapp = !rsTemp.next();
            rsTemp.close();
            psTemp.close();

            if (blnMapp) {
                String sNo = DmServer.getCodeViaManualCode(mrTemp.getSharedKeyFieldCode(), "organizationCode", "organizationToRateListMapping", "isnull(max(sNo), 0)+1", cnTemp);
                sqlQuery = "insert into organizationToRateListMapping (organizationCode, sNo, rateListcode, ApplicableFromDate)  "
                        + "values (:organizationCode, :sNo, :rateListcode, :ApplicableFromDate) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("sNo", sNo);
                psTemp.setString("rateListcode", DmShared.foriegnKey(mrTemp.getRateListCode()));
                psTemp.setDate("ApplicableFromDate", DmServer.convertDate(mrTemp.getApplicableDate()));
                psTemp.executeUpdate();
                psTemp.close();

            }

            saveService(mrTemp.getLstService(), mrTemp.getRateListCode(), mrTemp.getApplicableDate(), mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*============================< Delete records from patientServiceCatDiscount  >==============================*/
            sqlQuery = "delete from patientServiceCatDiscount where organizationCode = :organizationCode and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("site_Code", siteCode);
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdCategoryDiscount_Mdl node : mrTemp.getDiscountMdl().getLstCategoryDiscount()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0 && DmShared.vNull(node.getDiscountPercentage()).doubleValue() != 0) {
                    sqlQuery = "insert into patientServiceCatDiscount (rateListcode, organizationCode, serviceCategoryCode, site_code, discountPer) "
                            + " values (:rateListcode, :organizationCode, :serviceCategoryCode, :site_code, :discountPer) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("rateListcode", DmShared.foriegnKey(mrTemp.getRateListCode()));
                    psTemp.setString("site_Code", siteCode);
                    psTemp.setString("discountPer", DmServer.vNull(node.getDiscountPercentage(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            sqlQuery = " delete from OperationPricePercent where organizationCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdPercentage_Mdl discountPercent : mrTemp.getDiscountPercentMdl().getPercentages()) {
                if (discountPercent.getServiceNo() != null) {
                    sqlQuery = " insert into OperationPricePercent (organizationCode,serviceNumber,percentage) values ( :code, :serviceNo, :percentage) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("serviceNo", DmShared.xNull(discountPercent.getServiceNo()));
                    psTemp.setString("percentage", DmShared.xNull(discountPercent.getPercentage()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }

            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmServer.getSsnUserName(this), cnTemp);

            dmrTemp.setValue1(tpaMessage);
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
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
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
    public DmResult<String> deleteRecord(String strKey, String guiConfigCode) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select  count(*) as cnt "
                    + "         from ( "
                    + "             select applicableFrom "
                    + " FROM patientservicepricelist "
                    + " WHERE organizationCode= :code "
                    + " and entryFrom = :entryFrom "
                    + " GROUP BY applicableFrom"
                    + ")AS maintTB ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.ORGANIZATION_MASTER.toInt()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 1) {
                throw new UnsupportedOperationException("Either Record have multiple price List or price List from different site. You Can't delete this Record.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select shortName From organizationmast Where subCode =:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getString("shortName").equalsIgnoreCase("CP")) {
                throw new UnsupportedOperationException("System generated value you can not edit/delete.");
            } else {

                sqlQuery = "Delete From patientservicepricelist  "
                        + "Where organizationCode = :code  "
                        + "and entryFrom = :entryFrom  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", strKey);
                psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.ORGANIZATION_MASTER.toInt()));
                psTemp.executeUpdate();

                sqlQuery = "Delete From patientServiceCatDiscount  "
                        + "Where organizationCode = :code  ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", strKey);
                psTemp.executeUpdate();

                DmGUI_Server.deleteViaConfigCode(guiConfigCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);

                sqlQuery = "Delete From subGroup2 Where SubCode = :SubCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("SubCode", strKey);
                psTemp.executeUpdate();

                sqlQuery = "Delete From subGroupSiteList Where subCode= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", strKey);
                psTemp.executeUpdate();

                sqlQuery = "Delete From organizationmast Where subCode= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", strKey);
                psTemp.executeUpdate();

                sqlQuery = "Delete From subGroup Where subCode = :SubCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("SubCode", strKey);
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile, boolean isOrganization) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        String sqlQuery, condition = "";
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (isOrganization) {
                condition = " where isTPA in (0,1)";
            } else {
                condition = " where isTPA = 2";
            }

            sqlQuery = "select  om.subCode, sg.name, sg.manualCode, sg.gsTin, tds.name as tdsAc, sg.eMail, sg.mobile, "
                    + "		case om.isTPA when 0 then 'General' when 1 then 'TPA' when 2 then 'Collection Center' end as nature, "
                    + "		case when sg.isActive = 1 then 'Yes' else 'No' end as active"
                    + "		from organizationMast om "
                    + "		left join subGroup sg on sg.subCode = om.subCode "
                    + "		left join subGroup tds on tds.subCode = om.tdsAc " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setActive(DmShared.xNull(rsTemp.getString("active")));
                listNode.setGsTin(DmShared.xNull(rsTemp.getString("gsTin")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));

                listNode.setTdsAc(DmShared.xNull(rsTemp.getString("tdsAc")));

                listNode.setOrgEMail(DmShared.xNull(rsTemp.getString("eMail")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile, boolean isOrganization) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            if (isOrganization) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("General");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("TPA");
                lstTemp.add(listNode);
            } else {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("2");
                listNode.setSharedKeyFieldName("Collection Center");
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSAc(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT dm.subcode AS code, dm.name FROM subgroup AS dm  ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = DmImportData_Server.getCSVFormats(new ImportOrganization().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "organizationMaster";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  sg.name, om.shortName, om.feesDiscountPer, om.testDiscountPer, om.pharmacyDiscountPer, tds.name as tdsAc, "
                    + "		sg.gsTin, om.serviceDiscountPer, case when om.isTPA = 1 then 'TPA' else 'General' end as tpa, "
                    + "		case when om.medicineSaleOn = " + MedicineSaleOn.MRP.getId() + " then '" + MedicineSaleOn.MRP.getValue() + "' "
                    + "             else '" + MedicineSaleOn.SALE_RATE.getValue() + "' end as medicineSaleOn, "
                    + "		case when om.isPharmacyDiscount = 0 then 'No' else 'Yes' end as pharmacyDiscount "
                    + "		from organizationMast om  "
                    + "		left join subGroup sg on sg.subCode = om.subCode  "
                    + "		left join subGroup tds on tds.subCode = om.tdsAc  "
                    + "		where om.isTPA in (0,1) "
                    + "		order by (case when om.shortName = 'CP' then 0 else 1 end), sg.name, om.shortName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("shortName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("serviceDiscountPer")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("testDiscountPer")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("pharmacyDiscountPer")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("gsTin")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("tpa")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("medicineSaleOn")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("pharmacyDiscount")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("tdsAc")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

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

    private void saveService(List<GrdServices_Mdl> lstGrdService, String rateListCode, String applicableDate, String organizationCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String siteCode = DmServer.getSsnSiteCode(this);
        ResultSet rsTemp = null;
        try {
            List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
            for (GrdServices_Mdl mrTemp : lstGrdService) {

                if (DmShared.vNull(mrTemp.getRate()).doubleValue() > 0 || (DmShared.vNull(mrTemp.getDiscountAmount()).doubleValue() > 0) || (DmShared.vNull(mrTemp.getDiscountPercentage()).doubleValue() > 0)) {
                    lstServicePrice.add(getPriceListMdl(rateListCode, applicableDate, mrTemp.getServiceCode(),
                            "", mrTemp.getRate(), mrTemp.getDiscountAmount(), mrTemp.getDiscountPercentage(), mrTemp.getPrintSNo(), organizationCode));
                }
                for (GrdRoomCategory_Mdl categoryMdl : mrTemp.getLstRoomCategory()) {
                    if (DmShared.vNull(categoryMdl.getRate()).doubleValue() > 0 || (DmShared.vNull(categoryMdl.getDiscountAmount()).doubleValue() > 0) || (DmShared.vNull(categoryMdl.getDiscountPercentage()).doubleValue() > 0)) {
                        lstServicePrice.add(getPriceListMdl(rateListCode, applicableDate, mrTemp.getServiceCode(),
                                categoryMdl.getSharedKeyFieldCode(), categoryMdl.getRate(), categoryMdl.getDiscountAmount(),
                                categoryMdl.getDiscountPercentage(), "", organizationCode));
                    }
                }

            }
//            if (lstServicePrice.size() > 0) {
            STServer.savePriceList(applicableDate, PriceListEntryFrom.ORGANIZATION_MASTER, organizationCode, rateListCode, siteCode, lstServicePrice, cnTemp);
//            }

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
                throw new DmException(0, e.getMessage());
            }
        }

    }

    private ServicePriceList_Mdl getPriceListMdl(String keyCode, String applicableDate, String serviceCode, String roomCategoryCode,
            String rate, String discountAmount, String discountPercent, String printSno, String organizationCode) {
        ServicePriceList_Mdl priceList;
        priceList = new ServicePriceList_Mdl();
        priceList.setRateListCode(keyCode);
        priceList.setDate(applicableDate);
        priceList.setPriceListEntryFrom(PriceListEntryFrom.ORGANIZATION_MASTER);
        priceList.setPriceListNature(PriceListNature.None);
        priceList.setRate(DmServer.vNull(rate, "0.00"));
        priceList.setDiscAmt(DmServer.vNull(discountAmount, "0.00"));
        priceList.setDiscPercentage(DmServer.vNull(discountPercent, "0.00"));
        priceList.setPrintSNo(printSno);
        priceList.setRoomCatCode(roomCategoryCode);
        priceList.setServiceCode(serviceCode);
        priceList.setSiteCode(DmServer.getSsnSiteCode(this));
        priceList.setEntryFromNature("0");
        priceList.setOrganizationCode(organizationCode);
        return priceList;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, String serviceCatCode, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            if (DmShared.iNull(serviceCatCode).longValue() != 0) {
                condition = " and pscm.code=:serviceCatCode";
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT psm.code, psm.name, psm.shortName, pscm.code catCode, pscm.name catName  "
                    + " FROM patientServiceMast psm "
                    + " left join patientServiceSiteList as pssl on pssl.code=psm.code "
                    + " left join patientServicecatMast pscm  on pscm.code=psm.categoryCode "
                    + " WHERE psm.serviceNature <> '" + ServiceNature.OVERHEAD_SERVICE.getId() + "' AND psm.isActive = 1 " + condition
                    + " and pssl.site_Code= :siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(serviceCatCode).longValue() != 0) {
                sqlWithParam.setString("serviceCatCode", DmShared.iNull(serviceCatCode).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("catCode")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("catName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCat(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT pscm.code, pscm.name  "
                    + " FROM patientServicecatMast pscm "
                    + " WHERE pscm.Nature <> " + ServiceNature.OVERHEAD_SERVICE.getId()
                    + "");
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
    public DmResult<List<GrdCategoryDiscount_Mdl>> getOrganizationPer(String organizationCode, String rateListCode) {
        DmResult<List<GrdCategoryDiscount_Mdl>> rstTemp = new DmResult<>();
        List<GrdCategoryDiscount_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getServiceWiseDiscount(cnTemp, organizationCode, rateListCode);
            rstTemp.setData(lstTemp);
            rstTemp.setStatus(true);
            cnTemp.close();
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

    private List<GrdCategoryDiscount_Mdl> getServiceWiseDiscount(Connection cnTemp, String organizationCode, String rateListCode) {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        GrdCategoryDiscount_Mdl node = new GrdCategoryDiscount_Mdl();
        List<GrdCategoryDiscount_Mdl> listNode = new ArrayList<>();
        try {

            sqlQuery = "select  pscm.code, pscm.name, pscd.discountPer, pscd.rateListCode, pscd.organizationCode "
                    + "		from patientServiceCatMast pscm "
                    + "		left join patientServiceCatDiscount pscd on pscd.serviceCategoryCode = pscm.code and pscd.rateListCode = :rateListCode "
                    + "         and pscd.site_code = :siteCode and organizationCode = :organization"
                    + "		where isnull(pscm.filterNature, 0) <> 4 "
                    + "		order by pscm.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organization", DmShared.iNull(organizationCode).toString());
            psTemp.setString("rateListCode", DmShared.iNull(rateListCode).toString());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdCategoryDiscount_Mdl();
                node.setDiscountPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                node.setOrgnaizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                node.setRateListCode(DmShared.xNull(rsTemp.getString("rateListCode")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setColorChange(DmShared.vNull(rsTemp.getString("discountPer")).doubleValue() > 0);
                listNode.add(node);
            }

            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return listNode;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultVal(String entryPoint) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, guiConfigCode = "";
        DmGUI_Mdl dmguiMdl = null;
        GrdRoomCategory_Mdl categoryNode;
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get default Voucher Type >====================================*/
//            sqlQuery = " Select  guiConfigCode from GuiMapping where entrypoint =:entrypoint";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("entrypoint", entryPoint);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                guiConfigCode = (DmShared.iNull(rsTemp.getString("guiConfigCode")).toString());
//            }
//
//            rsTemp.close();
//            psTemp.close();
            sqlQuery = "SELECT rcm.code AS roomCatCode, rcm.name, '0.00' AS rate  "
                    + "FROM roomcatmast rcm  "
                    + "ORDER BY rcm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                categoryNode = new GrdRoomCategory_Mdl();
                categoryNode.setRate(rsTemp.getString("rate"));
                categoryNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCatCode")));
                categoryNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                dvm.getLstCategory().add(categoryNode);
            }

            dvm.setSiteCode(DmServer.getSsnSiteCode(this));
            dvm.setCurrentDate(DmServer.getServerDateTime()[0]);

            guiConfigCode = DmServer.getCodeViaManualCode(entryPoint, "entrypoint", "GuiMapping", "guiConfigCode", cnTemp);

            if (DmShared.iNull(guiConfigCode).longValue() != 0l) {
                dvm.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(DmShared.iNull(guiConfigCode).toString(), "", DmServer.getSsnSiteCode(this), cnTemp));
            }
            dmrTemp.setData(dvm);
            dmrTemp.setValue1(guiConfigCode);
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
}
