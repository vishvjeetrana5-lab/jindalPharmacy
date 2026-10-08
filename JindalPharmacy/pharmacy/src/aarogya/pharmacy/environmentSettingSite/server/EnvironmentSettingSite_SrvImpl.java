package aarogya.pharmacy.environmentSettingSite.server;

import aarogya.pharmacy.environmentSettingSite.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.AlphabetCase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentSettingSite_SrvImpl extends DmRSSPrivate implements EnvironmentSettingSite_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> saveRecord(EnvironmentSettingSite_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            EnvironmentSettingSite_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "enviro", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            String readFilePath = DmShared.xNull(mrTemp.getServerDirectory()) + "/" + DmShared.xNull(mrTemp.getServerFileName());
            if (readFilePath.equalsIgnoreCase("/")) {
                getDeleteLogo(mrTemp);
            } else {
                getSaveImage(mrTemp);
            }

            sqlQuery = "SELECT  site_Code FROM enviro where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                sqlQuery = " INSERT INTO enviro(site_Code) VALUES(:site_Code) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "update enviro  SET "
                    + " alphCase_Patient = :alphCase_Patient,"
                    + " alphaCase_Address = :alphaCase_Address,"
                    + " alphaCase_Medicine = :alphaCase_Medicine,"
                    + " u_Name = :userName,"
                    + " u_EntDt = :u_EntDt,"
                    + " pharmacyControlAc = :pharmacyControlAc, "
                    + " pharmacyAllowReturnWithoutSale= :pharmacyAllowReturnWithoutSale,  "
                    + " isIncludeTaxInCost= :isIncludeTaxInCost, "
                    + " pharmacyHospitalAc =:pharmacyHospitalAc,"
                    + " pharmacySelfAc=:pharmacySelfAc,"
                    + " medicineSaleOn= :medicineSaleOn, "
                    + " expiryAlertBeforeDays= :expiryAlertBeforeDays, "
                    + " pharmacyAllowPatientForSite= :pharmacyAllowPatientForSite, "
                    + " pharmacyAllowSaleBillForSite= :pharmacyAllowSaleBillForSite, "
                    + " pharmacyFinalDiscountOHCode= :pharmacyFinalDiscountOHCode, "
                    + " pharmacyFinalDiscountAcCode= :pharmacyFinalDiscountAcCode, "
                    + " pharmacyReturnDays= :pharmacyReturnDays,"
                    + " pharmacyBarcodeActive  = :pharmacyBarcodeActive, "
                    + " isAutoInTransitByMedicineTransfer  = :isAutoInTransitByMedicineTransfer, "
                    + " ph_SRateEditInPurchase = :ph_SRateEditInPurchase, "
                    + " defaultPharmacyServiceCode  = :defaultPharmacyServiceCode, "
                    + " pharmacyCentralStore  = :pharmacyCentralStore "
                    + " where site_Code= :site_Code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("alphCase_Patient", DmShared.xNull(mrTemp.getPatientAlphabateCaseCode()));
            psTemp.setString("alphaCase_Address", DmShared.xNull(mrTemp.getAddressAlphabateCaseCode()));
            psTemp.setString("alphaCase_Medicine", DmShared.xNull(mrTemp.getMedicineAlphabateCaseCode()));
            psTemp.setString("pharmacyAllowReturnWithoutSale", DmShared.iNull(mrTemp.getAllowReturnWithoutSaleCode()).toString());
            psTemp.setString("isIncludeTaxInCost", DmShared.iNull(mrTemp.getIsIncludeTaxInCostTag()).toString());
            psTemp.setString("pharmacyControlAc", DmShared.foriegnKey(mrTemp.getPharmacyControlAcTag()));
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            psTemp.setString("pharmacyHospitalAc", DmShared.foriegnKey(mrTemp.getPharmacyHospitalAcTag()));
            psTemp.setString("pharmacySelfAc", DmShared.foriegnKey(mrTemp.getPharmacySelfAcTag()));
            psTemp.setString("pharmacyAllowPatientForSite", DmShared.xNull(mrTemp.getPhAllowPatientForSiteTag()));
            psTemp.setString("pharmacyAllowSaleBillForSite", DmShared.xNull(mrTemp.getPhAllowSaleBillForSiteTag()));
            psTemp.setString("expiryAlertBeforeDays", DmShared.iNull(mrTemp.getExpAlertBeforeDays()).toString());
            psTemp.setString("medicineSaleOn", DmShared.xNull(mrTemp.getMedicineSaleOnTag()));
            psTemp.setString("pharmacyFinalDiscountOHCode", DmShared.foriegnKey(mrTemp.getPharmacyFinalDiscountOHCode()));
            psTemp.setString("pharmacyFinalDiscountAcCode", DmShared.foriegnKey(mrTemp.getPharmacyFinalDiscountAcCode()));
            psTemp.setString("defaultPharmacyServiceCode", DmShared.foriegnKey(mrTemp.getDefaultServiceTag()));
            psTemp.setString("pharmacyReturnDays", DmShared.xNull(mrTemp.getReturnDays()));
            psTemp.setString("pharmacyBarcodeActive", DmShared.xNull(mrTemp.getPharmacyBarcodeActive()));
            psTemp.setString("isAutoInTransitByMedicineTransfer", DmShared.xNull(mrTemp.getAutoInTransitByMedicineTransfer()));
            psTemp.setString("ph_SRateEditInPurchase", DmShared.xNull(mrTemp.getPh_SRateEditInPurchase()));
            psTemp.setString("pharmacyCentralStore", DmShared.xNull(mrTemp.getPharmacyCentralStoreTag()));

            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            psTemp.executeUpdate();
            psTemp.close();

            ////////////////////////////////////Department Site Link Grid ////////////////////////////////////////////////
            sqlQuery = "Delete From enviroDepartmentSiteLink where site_Code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            if (mrTemp.getGrdLinkDept() != null) {
                for (GrdLinkDepartment_Mdl grdDept_Mdl : mrTemp.getGrdLinkDept()) {
                    if (DmShared.iNull(grdDept_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                        sqlQuery = " Insert Into enviroDepartmentSiteLink(site_Code,departmentCode, "
                                + " link_Site_Code) Values  "
                                + "(:site_Code,:departmentCode,:link_Site_Code) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("departmentCode", DmShared.foriegnKey(grdDept_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("link_Site_Code", DmShared.foriegnKey(grdDept_Mdl.getLinkSiteCode()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            spBegin.commit();
            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sm.code AS CODE,sm.name AS Name "
                    + "FROM sitemast sm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAddAlphaCase(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("lower");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Proper");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Sentence");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("UPPER");
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

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(String siteCode, DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "SELECT cm.subcode as code,sg.name "
                    + "FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " WHERE CSL.site_Code=:site_Code AND sg.isActive='1' AND sgn.manualCode='CN' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmShared.xNull(siteCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNationality(DmHelper helper, boolean blnToEndOfFile) {

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
            sqlWithParam.setSQL("select code, name from nationalityMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
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
    public DmResult<EnvironmentSettingSite_Mdl> getMoveRec(String strKey) {
        DmResult<EnvironmentSettingSite_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, phAllowPatientSiteCode = "", phAllowSaleBillSiteCode = "", phSiteName = "", phSaleBillSiteName = "";
        String[] phAllowPatientSiteCodeArray, phAllowSaleBillSiteCodeArray;
        EnvironmentSettingSite_Mdl mrTemp = new EnvironmentSettingSite_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  sm.code AS site_code, sm.name AS site_name, env.alphCase_Patient, env.alphaCase_Address, env.u_Name, "
                    + "         format(env.u_EntDt,'dd-MMM-yyyy HH:mm') AS u_EntDt, env.pharmacyControlAc, env.alphaCase_Medicine, "
                    + "         env.pharmacyHospitalAc, sgPhHos.name as sgPHHospAc, env.pharmacySelfAc, sgPhSf.name as sgPhSelfAc, "
                    + "         env.disallowType, env.expiryAlertBeforeDays, env.medicineSaleOn, env.pharmacyAllowPatientForSite, "
                    + "         env.defaultPharmacyServiceCode, env.pharmacyAllowSaleBillForSite, env.pharmacyFinalDiscountAcCode, "
                    + "         psmPharmacyDefaultService.name as defaultPharmacyServiceName, env.pharmacyFinalDiscountOHCode, "
                    + "         ohm.name as pharmacyFinalDiscountOHName, env.pharmacyReturnDays, env.defaultOpdPharmacySiteCode, "
                    + "         env.pharmacyAllowReturnWithoutSale, env.isIncludeTaxInCost, env.pharmacyBarcodeActive, env.isAutoInTransitByMedicineTransfer, "
                    + "         sgPharmCon.name as sgPharmCon, sgPharmFinalDisAc.name as pharmacyFinalDiscountAcName,env.ph_SRateEditInPurchase, \n"
                    + "         env.pharmacyCentralStore AS pharmacyCentralStoreCode, sgPharmCenS.name as pharmacyCentralStoreName "
                    + "		from enviro AS env "
                    + "		left join sitemast sm on (env.site_Code = sm.code) "
                    + "		left join subgroup sgPhHos on (env.pharmacyHospitalAc = sgPhHos.subcode) "
                    + "		left join subgroup sgPhSf on (env.pharmacySelfAc = sgPhSf.subcode) "
                    + "		left join subgroup sgPharmCon on (env.pharmacyControlAc = sgPharmCon.subcode) "
                    + "		left join subgroup sgPharmCenS on (env.pharmacyCentralStore = sgPharmCenS.subcode) "
                    + "		left join subgroup sgPharmFinalDisAc on (env.pharmacyFinalDiscountAcCode = sgPharmFinalDisAc.subcode) "
                    + "		left join overHeadMast ohm on (env.pharmacyFinalDiscountOHCode = ohm.code) "
                    + "		left join patientServiceMast psmPharmacyDefaultService on psmPharmacyDefaultService.code = env.defaultPharmacyServiceCode "
                    + "         WHERE env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("site_name")));

                mrTemp.setDefaultServiceTag(rsTemp.getString("defaultPharmacyServiceCode"));
                mrTemp.setDefaultServiceText(rsTemp.getString("defaultPharmacyServiceName"));

                mrTemp.setPharmacyControlAcTag(DmShared.xNull(rsTemp.getString("pharmacyControlAc")));
                mrTemp.setPharmacyControlAcText(DmShared.xNull(rsTemp.getString("sgPharmCon")));

                mrTemp.setAddressAlphabateCaseCode(DmShared.xNull(rsTemp.getString("alphaCase_Address")));
                mrTemp.setAddressAlphabateCaseName(AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphaCase_Address"))) == null
                        ? AlphabetCase.PROPER_CASE.getValue() : AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphaCase_Address"))).getValue());

                mrTemp.setPatientAlphabateCaseCode(DmShared.xNull(rsTemp.getString("alphCase_Patient")));
                mrTemp.setPatientAlphabateCaseName(AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphCase_Patient"))) == null
                        ? AlphabetCase.PROPER_CASE.getValue() : AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphCase_Patient"))).getValue());

                mrTemp.setMedicineAlphabateCaseCode(DmShared.xNull(rsTemp.getString("alphaCase_Medicine")));
                mrTemp.setMedicineAlphabateCaseName(AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphaCase_Medicine"))) == null
                        ? AlphabetCase.PROPER_CASE.getValue() : AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphaCase_Medicine"))).getValue());

                mrTemp.setuName(DmShared.xNull(rsTemp.getString("u_Name")));

                mrTemp.setPharmacyHospitalAcTag(DmShared.xNull(rsTemp.getString("pharmacyHospitalAc")));
                mrTemp.setPharmacyHospitalAcText(DmShared.xNull(rsTemp.getString("sgPHHospAc")));
                mrTemp.setPharmacySelfAcTag(DmShared.xNull(rsTemp.getString("pharmacySelfAc")));
                mrTemp.setPharmacySelfAcText(DmShared.xNull(rsTemp.getString("sgPhSelfAc")));

                mrTemp.setPharmacyFinalDiscountAcCode(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountAcCode")));
                mrTemp.setPharmacyFinalDiscountAcName(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountAcName")));
                mrTemp.setPharmacyFinalDiscountOHCode(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountOHCode")));
                mrTemp.setPharmacyFinalDiscountOHName(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountOHName")));

                mrTemp.setPharmacyCentralStoreTag(DmShared.xNull(rsTemp.getString("pharmacyCentralStoreCode")));
                mrTemp.setPharmacyCentralStoreText(DmShared.xNull(rsTemp.getString("pharmacyCentralStoreName")));

                if (!DmShared.xNull(rsTemp.getString("u_EntDt")).equalsIgnoreCase("")) {
                    String date = DmShared.xNull(rsTemp.getString("u_EntDt"));
                    String[] a = date.split(" ");
                    mrTemp.setDate(a[0]);
                    mrTemp.setTime(DmShared.xNull(a[1]));
                } else {
                    mrTemp.setDate("");
                    mrTemp.setTime("");
                }

                if (DmShared.xNull(rsTemp.getString("pharmacyAllowReturnWithoutSale")).equalsIgnoreCase("1")) {
                    mrTemp.setAllowReturnWithoutSaleCode("1");
                    mrTemp.setAllowReturnWithoutSaleName("Yes");
                } else {
                    mrTemp.setAllowReturnWithoutSaleCode("0");
                    mrTemp.setAllowReturnWithoutSaleName("No");
                }

                if (DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")).equalsIgnoreCase("1")) {
                    mrTemp.setIsIncludeTaxInCostTag("1");
                    mrTemp.setIsIncludeTaxInCostText("Yes");
                } else {
                    mrTemp.setIsIncludeTaxInCostTag("0");
                    mrTemp.setIsIncludeTaxInCostText("No");
                }

                if (DmShared.xNull(rsTemp.getString("pharmacyBarcodeActive")).equalsIgnoreCase("1")) {
                    mrTemp.setPharmacyBarcodeActive("1");
                    mrTemp.setPharmacyBarcodeActiveName("Yes");
                } else {
                    mrTemp.setPharmacyBarcodeActive("0");
                    mrTemp.setPharmacyBarcodeActiveName("No");
                }

                if (DmShared.xNull(rsTemp.getString("isAutoInTransitByMedicineTransfer")).equalsIgnoreCase("1")) {
                    mrTemp.setAutoInTransitByMedicineTransfer("1");
                    mrTemp.setAutoInTransitByMedicineTransferName("Yes");
                } else {
                    mrTemp.setAutoInTransitByMedicineTransfer("0");
                    mrTemp.setAutoInTransitByMedicineTransferName("No");
                }

                if (DmShared.xNull(rsTemp.getString("ph_SRateEditInPurchase")).equalsIgnoreCase("1")) {
                    mrTemp.setPh_SRateEditInPurchase("1");
                    mrTemp.setPh_SRateEditInPurchasetext("Yes");
                } else {
                    mrTemp.setPh_SRateEditInPurchase("0");
                    mrTemp.setPh_SRateEditInPurchasetext("No");
                }

                mrTemp.setMedicineSaleOnTag(DmShared.iNull(rsTemp.getString("medicineSaleOn")).toString());
                mrTemp.setMedicineSaleOnText(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()).getValue());
                mrTemp.setExpAlertBeforeDays(DmShared.xNull(rsTemp.getString("expiryAlertBeforeDays")));
                mrTemp.setReturnDays(DmShared.xNull(rsTemp.getString("pharmacyReturnDays")));

                mrTemp.setPhAllowPatientForSiteTag(DmShared.xNull(rsTemp.getString("pharmacyAllowPatientForSite")));
                mrTemp.setPhAllowSaleBillForSiteTag(DmShared.xNull(rsTemp.getString("pharmacyAllowSaleBillForSite")));

                //--------------------------- Pharamcy Allow Patient For Site ---------------------------------------------
                phAllowPatientSiteCode = DmShared.xNull(mrTemp.getPhAllowPatientForSiteTag());
                if (!DmShared.xNull(phAllowPatientSiteCode).equalsIgnoreCase("")) {
                    phAllowPatientSiteCodeArray = phAllowPatientSiteCode.split(",");
                    if (phAllowPatientSiteCodeArray != null) {
                        for (String siteCode : phAllowPatientSiteCodeArray) {
                            if (DmShared.iNull(siteCode).intValue() > 0) {
                                phSiteName += getSiteName(siteCode, cnTemp);
                                phSiteName += ", ";

                            }
                        }
                    }
                }

                if (phSiteName.endsWith(", ")) {
                    phSiteName = phSiteName.substring(0, phSiteName.lastIndexOf(","));
                }
                //--------------------------- END ---------------------------------------------
                //--------------------------- Pharamcy Allow Sale Bill For Site ---------------------------------------------
                phAllowSaleBillSiteCode = DmShared.xNull(mrTemp.getPhAllowSaleBillForSiteTag());
                if (!DmShared.xNull(phAllowSaleBillSiteCode).equalsIgnoreCase("")) {
                    phAllowSaleBillSiteCodeArray = phAllowSaleBillSiteCode.split(",");
                    if (phAllowSaleBillSiteCodeArray != null) {
                        for (String siteCode : phAllowSaleBillSiteCodeArray) {
                            if (DmShared.iNull(siteCode).intValue() > 0) {
                                phSaleBillSiteName += getSiteName(siteCode, cnTemp);
                                phSaleBillSiteName += ", ";

                            }
                        }
                    }
                }

                if (phSaleBillSiteName.endsWith(", ")) {
                    phSaleBillSiteName = phSaleBillSiteName.substring(0, phSaleBillSiteName.lastIndexOf(","));
                }
                //--------------------------- END ---------------------------------------------
//                //--------------------------- Diagnostic Allow Patient For Site ---------------------------------------------

                mrTemp.setPhAllowPatientForSiteText(DmShared.xNull(phSiteName));
                mrTemp.setPhAllowSaleBillForSiteText(DmShared.xNull(phSaleBillSiteName));

            }

            rsTemp.close();
            psTemp.close();

            cnTemp.setCatalog(STServer.getSsnParentDateBase(this));
            mrTemp.setDefaultServiceText(DmShared.xNull(DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getDefaultServiceTag()), "code", "patientserviceMast", "name", cnTemp)));
            cnTemp.setCatalog(DmServer.getSsnDBName(this));

            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< LINK DEPARTMENT GRID  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            sqlQuery = " select eDSL.departmentCode,sg.name as deptName,edSL.link_Site_Code,sm.name as siteName"
                    + " from enviroDepartmentSiteLink as eDSL"
                    + " left join enviro as env on env.site_Code=eDSL.site_Code"
                    + " left join subGroup as sg on eDSl.departmentCode=sg.subCode"
                    + " left join siteMast as sm on eDSL.link_Site_Code=sm.code "
                    + " WHERE env.site_Code = :site_Code"
                    + " ORDER BY sg.name,sm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdLinkDepartment_Mdl objListDept = new GrdLinkDepartment_Mdl();
                objListDept.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("deptName")));
                objListDept.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                objListDept.setLinkSiteCode(DmShared.xNull(rsTemp.getString("link_Site_Code")));
                objListDept.setLinkSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                mrTemp.getGrdLinkDept().add(objListDept);
            }
            rsTemp.close();
            psTemp.close();

            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Check whether File Uploaded or not >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            sqlQuery = "select count(*) as cnt"
                    + "  from linkFileCollection "
                    + " WHERE docId= :docId and sNo=:sNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.setString("sNo", "1");
            //  psTemp.setString("ruleCode", DmShared.xNull(ruleCode));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                mrTemp.setFileUploaded(true);
            } else {
                mrTemp.setFileUploaded(false);
            }
            rsTemp.close();
            psTemp.close();

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

    private String getSiteName(String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", siteName = "";
        try {
            sqlQuery = "SELECT name as siteName"
                    + " FROM siteMast  "
                    + " where  code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                siteName = DmShared.xNull(rsTemp.getString("siteName"));
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

        return siteName;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String siteCode, sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            siteCode = DmServer.getSsnSiteCode(this);
            sqlQuery = "SELECT sm.name AS site_name from sitemast sm "
                    + " WHERE sm.Code=:site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", siteCode);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(siteCode));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("site_name")));
                mrTemp.setImagePath(DmServer.Path.images);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTemplate(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT Code,Name FROM guiTemplateMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
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
            sqlWithParam.setSQL("select sg.subCode, sg.name "
                    + "  from departmentMast dep"
                    + "  left join subgroup sg on sg.subCode = dep.subCode"
                    + "  left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "  left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode"
                    + "  where sg.isActive = 1 and sgsl.site_Code = :siteCode and sgn.manualCode in ('PH')");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
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

    private void getSaveImage(EnvironmentSettingSite_Mdl mrTemp) {
        try {

            String readFilePath = DmShared.xNull(mrTemp.getServerDirectory()) + "/" + DmShared.xNull(mrTemp.getServerFileName());
            String writeDirectoryPath = DmServer.Path.images + "/" + mrTemp.getDestSiteCode();
            String writelFilePath = writeDirectoryPath + "/" + EnvironmentSettingSite_Mdl.getReceiptLogo();

            if (readFilePath.equalsIgnoreCase(writelFilePath)) {
                return;
            }
            File rFile = new File(readFilePath);
            if (!rFile.exists()) {
                getDeleteLogo(mrTemp);
            } else {
                File wFile = new File(writeDirectoryPath);
                if (!wFile.exists()) {
                    wFile.mkdirs();
                }
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(readFilePath);
                    fos = new FileOutputStream(writelFilePath);
                    byte[] bytes = new byte[2 * 1024 * 1024];
                    int readLen = fis.read(bytes);

                    while (readLen != -1) {
                        if (readLen >= bytes.length) {
                            fos.write(bytes);
                        } else if (readLen < bytes.length) {
                            fos.write(bytes, 0, readLen);
                        }
                        readLen = fis.read(bytes);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                } finally {
                    if (fis != null) {
                        fis.close();
                        fis = null;
                    }
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getDeleteLogo(EnvironmentSettingSite_Mdl mrTemp) {

        try {
            String deleteDirectoryPath = DmServer.Path.images + "/" + DmShared.iNull(mrTemp.getSharedKeyFieldCode());
            String deleteFilePath = deleteDirectoryPath + "/" + EnvironmentSettingSite_Mdl.getReceiptLogo();
            File dFile = new File(deleteFilePath);
            if (dFile.exists()) {
                dFile.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT c.cityCode, c.cityName, c.stateCode, c.countryCode, con.name as CountryName, sm.name as stateName, "
                    + "         CONCAT(c.cityName,', ',sm.name,', ',con.name) as cityDetails "
                    + "         FROM city c "
                    + "         Left Join country con on c.countryCode=con.code "
                    + "         Left Join stateMast sm on c.stateCode=sm.code ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityDetails")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("CountryName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCountry(DmHelper helper, boolean blnToEndOfFile) {

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
            sqlWithParam.setSQL("select code, name from country ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInfantUnit(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code, unit from unitmast");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getServices(DmHelper helper, boolean blnToEndOfFile, ServiceNature[] serviceNatures, String serviceCategory) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "", filterNature = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            if (serviceNatures != null) {
                if (serviceNatures.length > 0) {
                    for (ServiceNature nature : serviceNatures) {
                        if (filterNature.equalsIgnoreCase("")) {
                            filterNature = DmShared.iNull(nature.getId()).toString();
                        } else {
                            filterNature = "," + DmShared.iNull(nature.getId()).toString();
                        }
                    }
                }
            }
            rstTemp.setData(lstTemp);
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTemp = DmServer.getConnection(STServer.getSsnParentDateBase(this));
            if (!filterNature.equalsIgnoreCase("")) {
                condition += " and psm.serviceNature in (" + filterNature + ")";
            }
            if (DmShared.iNull(serviceCategory).longValue() > 0) {
                condition += " and psm.categoryCode = :categoryCode";
            }
            sqlWithParam.setSQL("select  psm.code, psm.name "
                    + "		from patientServiceMast psm "
                    + "		left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "		where pssl.site_Code = :siteCode and psm.isActive = 1" + condition);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(serviceCategory).longValue() > 0) {
                sqlWithParam.setString("categoryCode", serviceCategory);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCardType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select code,name from cardTypeMast where isActive=1  ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDisallowType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Disallow (Reg. wise)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Disallow (Service wise)");
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
    public DmResultPageing<List<KeyFieldExtended>> getPharmacyFinalDiscountOH(String siteCode, DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = " SELECT "
                    + "   ohm.code "
                    + " , ohm.name "
                    + " FROM overHeadMast ohm ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPackage(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select	pm.code, pm.name, pm.shortName "
                    + "		from packageMast pm "
                    + "         left join patientServiceSiteList psl on psl.code = pm.code"
                    + "		where pm.nature = 0 and psl.site_Code = :siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("shortName")));

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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

    private void saveLinkFileCollection(FileUpload_Mdl upload_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmFileManagerMongo mongoCon = null;
        String sqlQuery;

        try {
            mongoCon = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            if (DmShared.xNull(upload_Mdl.getDocId()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid DocId.");
            }

            if (DmShared.xNull(upload_Mdl.getRuleCode()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid Rule Code.");
            }

            if (DmShared.xNull(upload_Mdl.getFileTypeName()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid File Type.");
            }

            sqlQuery = "SELECT fileLink,docId,sNo,ruleCode FROM LINKFILECOLLECTION WHERE (docId=:docId) and (sNo=:sNo) and (ruleCode=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", upload_Mdl.getDocId());
            psTemp.setString("sNo", DmServer.vNull(upload_Mdl.getsNo()));
            psTemp.setString("ruleCode", upload_Mdl.getRuleCode());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                String mongoId = DmShared.xNull(rsTemp.getString("fileLink"));

                if (!mongoCon.deleteFile(upload_Mdl.getRuleCode(), mongoId)) {
                    throw new Exception("Unable to Delete File Link.");
                }
            }
            rsTemp.close();
            psTemp.close();
            sqlQuery = "DELETE FROM linkFileCollection WHERE (docId=:docId) and (sNo=:sNo) and (ruleCode=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", upload_Mdl.getDocId());
            psTemp.setString("sNo", DmServer.vNull(upload_Mdl.getsNo()));
            psTemp.setString("ruleCode", upload_Mdl.getRuleCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert into linkFileCollection (fileLink, docId, sNo, ruleCode, fileType, fileName, fileSize, remarks, "
                    + " u_Name, u_EntDt, tempDocId, sessionId) "
                    + " values (:fileLink, :docId, :sNo, :ruleCode,  :fileType, :fileName, :fileSize, :remarks, "
                    + " :u_name, :u_EntDt, :tempDocId, :sessionId)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fileLink", upload_Mdl.getFileLink());
            psTemp.setString("docid", DmShared.foriegnKey(upload_Mdl.getDocId()));
            psTemp.setString("rulecode", upload_Mdl.getRuleCode());
            psTemp.setString("sNo", DmServer.vNull(upload_Mdl.getsNo()));
            psTemp.setString("filesize", DmServer.vNull(upload_Mdl.getFileSize(), "0"));
            psTemp.setString("filename", upload_Mdl.getFileName());
            psTemp.setString("filetype", upload_Mdl.getFileTypeName());
            psTemp.setString("remarks", "");
            psTemp.setString("u_name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("tempDocId", DmShared.foriegnKey(""));
            psTemp.setString("sessionId", DmShared.foriegnKey(upload_Mdl.getSessionId()));

            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCentralStore(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select sg.name AS Name , sg.subCode AS Code "
                    + "           from subGroup  sg \n"
                    + "           left join subGroupNature sgn on sgn.code = sg.nature \n"
                    + "		  left join subGroupNature sgn1 on sgn1.code = sg.filterNature\n"
                    + "           where sgn1.manualCode='DP' and   sgn.manualCode in ('PH','CM')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
