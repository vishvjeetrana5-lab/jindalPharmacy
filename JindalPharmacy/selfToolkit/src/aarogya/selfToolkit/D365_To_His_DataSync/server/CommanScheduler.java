package aarogya.selfToolkit.D365_To_His_DataSync.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.Bill;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.CancelIPDBill_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.DepartmentMaster_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.RevenueDetail_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.RevenueHeader_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.PharmacySaleBillDetail_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.PharmacySaleBillHeader_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.Settlement;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.StockTransferLine_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.StockTransfer_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.TpaSettlementReceipt_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommanScheduler {

    public static void syncDepartmentMaster(Connection cnTemp, Connection stagingCnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", departmentSubcode = "", filterNature = "", acDepartmentCode = "";
        try {

            /*===============fetch new data and updated data from staging===============*/
            List<DepartmentMaster_Mdl> newDepartmentMasterList = new ArrayList<>();
            List<DepartmentMaster_Mdl> updateDepartmentMasterList = new ArrayList<>();

            sqlQuery = "SELECT * FROM (\n"
                    + "               SELECT *, ROW_NUMBER() OVER (\n"
                    + "                PARTITION BY code ORDER BY D365_Timestamp DESC\n"
                    + "                ) AS rn\n"
                    + "                FROM D365_Department\n"
                    + "                WHERE D365_status IN ('Update','New')\n"
                    + "                AND his_TimeStamp IS NULL\n"
                    + "                 ) t WHERE rn = 1";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                DepartmentMaster_Mdl list = new DepartmentMaster_Mdl();
                list.setCode(rsTemp.getString("code"));
                list.setSharedKeyFieldName(rsTemp.getString("name"));
                list.setShortName(rsTemp.getString("shortname"));
                list.setNatureName(rsTemp.getString("natureName"));
                if ("New".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    newDepartmentMasterList.add(list);
                } else if ("Update".equalsIgnoreCase(rsTemp.getString("D365_status"))) {
                    updateDepartmentMasterList.add(list);
                }
                list.setSharedKeyFieldCode(DmServer.getSerialNoBigInt("1", "subGroup", cnTemp));
            }
            psTemp.close();
            rsTemp.close();

            acDepartmentCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupDepartmentCode", cnTemp);

            filterNature = DmServer.getCodeViaManualCode("DP", "manualCode", "subgroupNature", "Code", cnTemp);

            for (DepartmentMaster_Mdl listTemp : newDepartmentMasterList) {
                /*===============Insert subGroup data into His===============*/
                departmentSubcode = DmShared.xNull(DmServer.executeScalar("select subCode from subgroup where D365_code = '" + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp));

                if (departmentSubcode.equals("")) {

                    sqlQuery = "insert into subGroup(subcode, name, displayname, nature, groupcode, filterNature, manualCode, site_Code, preparedBy, u_EntDt,d365_code) "
                            + " values (:subcode, :name, :displayname, :nature, :groupcode, :filterNature, :manualCode, :sitecode, :uName, :uEntDt,:d365_code) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", listTemp.getSharedKeyFieldCode());
                    psTemp.setString("name", listTemp.getSharedKeyFieldName());
                    psTemp.setString("displayname", listTemp.getSharedKeyFieldName());
                    psTemp.setString("nature", DmServer.executeScalar("select code from subGroupNature where name = '" + listTemp.getNatureName() + "'", cnTemp));
                    psTemp.setString("groupcode", acDepartmentCode);
                    psTemp.setString("manualCode", listTemp.getShortName());
                    psTemp.setString("filterNature", filterNature);
                    psTemp.setString("sitecode", "1");
                    psTemp.setString("uName", "Scheduler");
                    psTemp.setString("d365_code", listTemp.getCode());
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "insert into departmentMast(subCode, nature, drugLicenceNo) "
                            + " values (:subcode, :nature, :drugLicenceNo) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", listTemp.getSharedKeyFieldCode());
                    psTemp.setString("nature", listTemp.getNatureName());
                    psTemp.setString("drugLicenceNo", listTemp.getDrugLicenceNo());
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "insert into subGroupSiteList(subCode, site_Code) "
                            + " values (:subcode, :siteCode);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", DmShared.foriegnKey(listTemp.getSharedKeyFieldCode()));
                    psTemp.setString("siteCode", DmShared.foriegnKey("1"));
                    psTemp.executeUpdate();
                    psTemp.close();

                }
                sqlQuery = "UPDATE D365_Department\n"
                        + "SET his_TimeStamp = GETDATE(),\n"
                        + "his_Status = 'Success',\n"
                        + "his_code = :his_code\n"
                        + "WHERE code = :code "
                        + "and his_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("his_code", DmShared.xNull(listTemp.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();
            }
            for (DepartmentMaster_Mdl listTemp : updateDepartmentMasterList) {
                /*===============Check code hai ki nahi===============*/
                departmentSubcode = DmShared.xNull(DmServer.executeScalar("select subcode from subgroup where D365_code = '" + DmShared.xNull(listTemp.getCode()) + "' ", cnTemp));
                /*===============Insert subGroup data into His===============*/
                if (departmentSubcode.equals("")) {

                    sqlQuery = "insert into subGroup(subcode, name, displayname, nature, groupcode, filterNature, manualCode, site_Code, preparedBy, u_EntDt,d365_code) "
                            + " values (:subcode, :name, :displayname, :nature, :groupcode, :filterNature, :manualCode, :sitecode, :uName, :uEntDt,:d365_code) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", listTemp.getSharedKeyFieldCode());
                    psTemp.setString("name", listTemp.getSharedKeyFieldName());
                    psTemp.setString("displayname", listTemp.getSharedKeyFieldName());
                    psTemp.setString("nature", listTemp.getNatureCode());
                    psTemp.setString("groupcode", acDepartmentCode);
                    psTemp.setString("manualCode", listTemp.getShortName());
                    psTemp.setString("filterNature", filterNature);
                    psTemp.setString("sitecode", "1");
                    psTemp.setString("uName", "Scheduler");
                    psTemp.setString("d365_code", listTemp.getCode());
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();

                    sqlQuery = "insert into departmentMast(subCode, nature, drugLicenceNo) "
                            + " values (:subcode, :nature, :drugLicenceNo) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", listTemp.getSharedKeyFieldCode());
                    psTemp.setString("nature", listTemp.getNatureName());
                    psTemp.setString("drugLicenceNo", listTemp.getDrugLicenceNo());
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "insert into subGroupSiteList(subCode, site_Code) "
                            + " values (:subcode, :siteCode);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", DmShared.foriegnKey(listTemp.getSharedKeyFieldCode()));
                    psTemp.setString("siteCode", DmShared.foriegnKey("1"));
                    psTemp.executeUpdate();
                    psTemp.close();
                    psTemp.close();

                } else {
                    sqlQuery = "update subGroup set "
                            + " name = :name, "
                            + " displayname = :displayname, "
                            + " nature = :nature, "
                            + " manualCode = :shortName, "
                            + " u_Name = :uName, "
                            + " u_EntDt = :uEntDt "
                            + " Where subcode = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("name", listTemp.getSharedKeyFieldName());
                    psTemp.setString("displayname", listTemp.getDisplayName());
                    psTemp.setString("nature", listTemp.getNatureCode());
                    psTemp.setString("shortName", listTemp.getShortName());
                    psTemp.setString("uName", "Scheduler");
                    psTemp.setString("code", listTemp.getSharedKeyFieldCode());
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "update departmentMast set "
                            + " nature = :nature, "
                            + " drugLicenceNo = :drugLicenceNo "
                            + " Where subcode = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("nature", listTemp.getNatureName());
                    psTemp.setString("code", listTemp.getSharedKeyFieldCode());
                    psTemp.setString("drugLicenceNo", listTemp.getDrugLicenceNo());
                    psTemp.executeUpdate();
                    psTemp.close();

                    //================< update subGroupSiteList >=================================
                    sqlQuery = "update subGroupSiteList set "
                            + " drugLicenseNo = :drugLicenseNo\n"
                            + " Where subcode = :code\n"
                            + " and site_Code = :siteCode";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", listTemp.getSharedKeyFieldCode());
                    psTemp.setString("drugLicenseNo", DmShared.xNull(listTemp.getDrugLicenceNo()));
                    psTemp.setString("siteCode", DmShared.foriegnKey("1"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
                sqlQuery = "UPDATE D365_Department\n"
                        + "SET his_TimeStamp = GETDATE(),\n"
                        + "his_Status = 'Success',\n"
                        + "his_code = :his_code\n"
                        + "WHERE code = :code "
                        + "and his_TimeStamp is null";
                psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(listTemp.getCode()));
                psTemp.setString("his_code", DmShared.xNull(listTemp.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();
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
    }

    public static void postHisPaymentDetails(Connection cnTemp, Connection stagingCnTemp, String aarogyaDB, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "";

        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            condition = "prc.v_Date = '" + dateValue + "'";

            sqlQuery = "insert into HIS_PaymentDetail(    LegalEntity,\n"
                    + "BusinessUnit,\n"
                    + "BusinessUnitName,\n"
                    + "Episode,\n"
                    + "HISDocumentType,\n"
                    + "HISDocumentTypeCode,\n"
                    + "AdvanceReceiptNo,\n"
                    + "ReceiptDate,\n"
                    + "UHID,\n"
                    + "PatientName,\n"
                    + "EncounterNo,\n"
                    + "Department,\n"
                    + "departmentCode,\n"
                    + "FacilityID,\n"
                    + "Amount,\n"
                    + "PaymentReferrence,\n"
                    + "DuplicateKeyCheck,\n"
                    + "HISUserID,\n"
                    + "HISUserName,\n"
                    + "PaymentModeCode,\n"
                    + "ModeOfPayment,\n"
                    + "SponsorCode,\n"
                    + "SponsorName,\n"
                    + "PayorName,\n"
                    + "PayorCategory,\n"
                    + "RRNumber,\n"
                    + "PaymentModeAccountName,\n"
                    + "GLCode) (\n"
                    + "select tmp.* from ( select sm.LegalEntity,sm.BusinessUnit,sm.businessunitname, \n"
                    + " CASE \n"
                    + "        WHEN CHARINDEX(':', lm.manualCode) > 0 \n"
                    + "        THEN \n"
                    + "		(\n"
                    + "		case when \n"
                    + "		SUBSTRING(lm.manualCode, CHARINDEX(':', lm.manualCode) + 1, LEN(lm.manualCode))='I'  \n"
                    + "		then 'IPD'\n"
                    + "		when \n"
                    + "		SUBSTRING(lm.manualCode, CHARINDEX(':', lm.manualCode) + 1, LEN(lm.manualCode))='O'  \n"
                    + "		then 'OPD'\n"
                    + "		when \n"
                    + "		SUBSTRING(lm.manualCode, CHARINDEX(':', lm.manualCode) + 1, LEN(lm.manualCode))='W'  \n"
                    + "		then 'Walk-In'\n"
                    + "\n"
                    + "		 end \n"
                    + "		)\n"
                    + "\n"
                    + "		 ELSE 'UHID'\n"
                    + "       \n"
                    + "    END as Episode\n"
                    + " ,vtprc.description as HISDocumentType, vtprc.v_Type as HISDocumentTypeCode ,prc.recId as AdvanceReceiptNo,\n"
                    + " prc.v_Date as ReceiptDate  , lm.manualCode as UHID , lm.displayName as PatientName ,\n"
                    + " LEFT(lm.manualCode, CHARINDEX(':', lm.manualCode + ':') - 1) as EncounterNo ,case when dpt.name is null then 'Hospital' else dpt.name  end  as Department ,case when dpt.d365_code is null then 'DEPT-0214' else dpt.d365_code end as departmentCode,'' as FacilityID ,cl.amtDr as Amount\n"
                    + " ,cl.cardNo as PaymentReferrence , CONCAT(cast(prc.docId as varchar(30)),sm.BusinessUnit,cast(ctm.code as varchar(30))) as DuplicateKeyCheck\n"
                    + " ,prc.preparedBy as HISUserID, prc.preparedBy as HISUserName ,ctm.code as paymentModeCode ,ctm.name as ModeofPayment\n"
                    + ",isnull(om.subCode,'') as SponsorCode ,isnull(org.name,'') as SponsorName , isnull(org.name,'') as PayorName,\n"
                    + " case when om.isTpa=1 then 'TPA' else 'General' end as PayorCategory, isnull(pos.rrNumber,'')rrNumber ,\n"
                    + " ctmGL.name as paymentModeAccountName , isnull(ctmGL.d365_code,'') as   glCode \n"
                    + " from " + aarogyaDB + ".dbo.patientReceipt prc \n"
                    + " left join " + aarogyaDB + ".dbo.ledgerMast lm on lm.code=prc.lmCode\n"
                    + "  left join " + aarogyaDB + ".dbo.voucher_Type vtprc on vtprc.v_Type = prc.v_Type   \n"
                    + "  left join " + aarogyaDB + ".dbo.patientRegistration pr on pr.regDocId=prc.regDocId\n"
                    + "left join " + aarogyaDB + ".dbo.saleBill sb on sb.docId=prc.refDocId\n"
                    + "left join " + aarogyaDB + ".dbo.subGroup2 as sg4 on sg4.subCode=sb.consultantCode \n"
                    + "left join " + aarogyaDB + ".dbo.subGroup as dpt on dpt.subCode= \n"
                    + "case \n"
                    + "when "
                    + " \n"
                    + "vtprc.description IN ( \n"
                    + "	 'Pharmacy Bill Receipt',\n"
                    + "	 'Pharmacy Bill Return Receipt'\n"
                    + ")\n"
                    + " \n"
                    + "then sg4.departmentCode\n"
                    + " \n"
                    + "end   left join " + aarogyaDB + ".dbo.siteMast sm on sm.code=prc.site_Code\n"
                    + "  \n"
                    + " left join " + aarogyaDB + ".dbo.voucher_Type vtpr on vtpr.v_Type=pr.v_Type\n"
                    + " left join " + aarogyaDB + ".dbo.patientUHID pu on pu.docId=pr.uhid\n"
                    + " left join " + aarogyaDB + ".dbo.cardLedger cl on cl.docId =prc.docId\n"
                    + " left join " + aarogyaDB + ".dbo.cardTypeMast ctm on ctm.code=cl.cardTypeCode\n"
                    + " left join " + aarogyaDB + ".dbo.organizationMast om on om.subCode =pr.organizationCode\n"
                    + " left join " + aarogyaDB + ".dbo.posPayment pos on pos.docId = cl.posPaymentDocid\n"
                    + " left join " + aarogyaDB + ".dbo.subGroup org on org.subCode=om.subCode \n"
                    + " left join " + aarogyaDB + ".dbo.subGroup ctmGL on ctmGL.subCode =cl.subCode\n"
                    + "\n"
                    + " where " + condition + " and isnull(prc.isSyncToD365,0)=0   and prc.isCancelled = 0  \n"
                    + " and vtprc.description IN (\n"
                    + "   \n"
                    + "    'Pharmacy Bill Receipt',\n"
                    + "    'Pharmacy Bill Return Receipt'\n"
                    + " )    ) tmp  "
                    + " \n"
                    + " )";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "UPDATE  prc SET \n"
                    + "prc.isSyncToD365 = 1 \n"
                    + "from patientReceipt prc \n"
                    + "WHERE " + condition + " "
                    + "AND prc.isCancelled = 0 "
                    + "AND ISNULL(prc.isSyncToD365,0) = 0";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void postUnbilledIPDServiceBill(Connection cnTemp, Connection stagingCnTemp,
            String aarogyaDB, String postingFromDate, String regDocid) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "";

        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);

            condition = regDocid.equalsIgnoreCase("") ? "psd.v_Date = '" + dateValue + "'" : "psd.regDocid = '" + regDocid + "'";

            sqlQuery = "INSERT INTO HIS_unBilledIPDService\n"
                    + "(\n"
                    + "    EntryNo,\n"
                    + "    serviceType,\n"
                    + "    GLAccountName,\n"
                    + "    GLAccountNo,\n"
                    + "    Department,\n"
                    + "    DepartmentCode,\n"
                    + "    ServiceCategory,\n"
                    + "    ServiceItemID,\n"
                    + "    ServiceItemName,\n"
                    + "    ServiceItemCode,\n"
                    + "    Quantity,\n"
                    + "    Rate,\n"
                    + "    GrossAmount,\n"
                    + "    MOUDiscount,\n"
                    + "    AddOnDiscount,\n"
                    + "    NetAmount,\n"
                    + "    LocationCode,\n"
                    + "    LocationName,\n"
                    + "    CreatedBy,\n"
                    + "    CreatedDateTime,\n"
                    + "    PayorCode,\n"
                    + "    PayorCategory,\n"
                    + "    PayorName,\n"
                    + "    ServiceLineNo,\n"
                    + "    DepartmentId,\n"
                    + "    FacilityID,\n"
                    + "    HeaderId,\n"
                    + "    netPayableAmount,\n"
                    + "    ValidationHISKey\n"
                    + ")\n"
                    + "   select  ps.recId as EntryNo ,\n"
                    + "vtps.description as serviceType  ,sgsa.name as GLAccountName,sgsa.d365_code as GLAccountNo,dpt.name as Department,\n"
                    + "dpt.d365_code as DepartmentCode  , pscm.name as ServiceCategory,psm.code as ServiceItemID ,psm.name as ServiceItemName,\n"
                    + "psm.shortName as ServiceItemCode ,psd.quantity as Quantity,psd.rate as Rate ,psd.quantity*psd.rate as GrossAmount,\n"
                    + "isNull(psd.otherDiscount,0) as MOUDiscount ,isNull(psd.Discount,0) as AddOnDiscount,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(psd.Discount,0) NetAmount ,\n"
                    + "dpt.d365_code as LocationCode ,dpt.name as LocationName ,ps.preparedBy as CreatedBy , ps.u_EntDt as CreatedDateTime,\n"
                    + "om.subCode as PayorCode ,CASE WHEN om.isTpa = 1 THEN 'TPA' ELSE 'General' END as	PayorCategory ,\n"
                    + "sgOrg.name as	PayorName , psd.v_SNo as ServiceLineNo ,dpt.subCode as DepartmentId,sm.BusinessUnit as FacilityID,\n"
                    + "ps.docId as HeaderId,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(psd.Discount,0) as netPayableAmount,\n"
                    + " (CONCAT(CAST(psd.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(om.subCode AS VARCHAR(50)))) AS ValidationHISKey\n"
                    + "\n"
                    + "from " + aarogyaDB + ".dbo.patientServiceDetail as psd\n"
                    + "left join " + aarogyaDB + ".dbo.patientRegistration pr on pr.regDocId=psd.regDocId\n"
                    + "left join " + aarogyaDB + ".dbo.voucher_Type vtpr on vtpr.v_Type=pr.v_Type\n"
                    + "left join " + aarogyaDB + ".dbo.patientUHID pu on pu.docId=pr.uhid\n"
                    + "left join " + aarogyaDB + ".dbo.consultantMast cons on cons.subCode=pr.consultantCode\n"
                    + "left join " + aarogyaDB + ".dbo.consultantSpeciMast csm on csm.code=cons.consultantSpeciCode \n"
                    + "left join " + aarogyaDB + ".dbo.organizationMast om on om.subCode=pr.organizationCode\n"
                    + "left join " + aarogyaDB + ".dbo.subGroup sgOrg on sgOrg.subCode=om.subCode\n"
                    + "left join " + aarogyaDB + ".dbo.siteMast sm on sm.code=psd.site_Code\n"
                    + "left join " + aarogyaDB + ".dbo.patientRegistration prOld on prold.regDocId=pr.regDocIdOld\n"
                    + "left join " + aarogyaDB + ".dbo.voucher_Type vtprold on vtprold.v_Type=prOld.v_Type\n"
                    + "left join " + aarogyaDB + ".dbo.subGroup as dpt on dpt.subCode=pr.departmentCode\n"
                    + "left join " + aarogyaDB + ".dbo.voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type\n"
                    + "left join " + aarogyaDB + ".dbo.patientServiceMast as psm on psd.serviceCode = psm.code \n"
                    + "left join " + aarogyaDB + ".dbo.patientServiceCatMast pscm on pscm.code=psm.categoryCode\n"
                    + "left join " + aarogyaDB + ".dbo.subGroup sgsa on psm.serviceAc = sgsa.subcode\n"
                    + "left join " + aarogyaDB + ".dbo.patientService as ps on ps.docId=psd.docId\n"
                    + "left join " + aarogyaDB + ".dbo.voucher_Type vtps on vtps.v_Type=psd.v_Type\n"
                    + "where psd.isCancelled=0 and  psd.billingNature in (0,1) \n"
                    + "and  vtpsd.category not in ('OBSSD')\n"
                    + "and  vtpr.short_Name in ('IPD') \n"
                    + "and  isnull(psd.isSyncToD365,0)=0 \n"
                    + "and vtps.category in  ('IPDSV','ISRVC','LAB','LABCN','OTSRV','PHBIL','PHBRT')\n"
                    + "and psd.sbDocId is  null\n"
                    + "and " + condition + "";
            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "UPDATE psd SET \n"
                    + "psd.isSyncToD365 = 1 \n"
                    + "FROM " + aarogyaDB + ".dbo.patientServiceDetail psd \n"
                    + "left join " + aarogyaDB + ".dbo.patientRegistration pr on pr.regDocId=psd.regDocId \n"
                    + "left join " + aarogyaDB + ".dbo.voucher_Type vtpr on vtpr.v_Type=pr.v_Type \n"
                    + "left join " + aarogyaDB + ".dbo.voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type \n"
                    + "left join " + aarogyaDB + ".dbo.voucher_Type vtps on vtps.v_Type=psd.v_Type \n"
                    + "WHERE " + condition + " \n"
                    + "AND psd.isCancelled=0 and psd.billingNature in (0,1) \n"
                    + "AND vtpsd.category not in ('OBSSD') \n"
                    + "AND vtpr.short_Name in ('IPD') \n"
                    + "AND vtps.category in ('IPDSV','ISRVC','LAB','LABCN','OTSRV','PHBIL','PHBRT') \n"
                    + "AND psd.sbDocId is null \n"
                    + "AND ISNULL(psd.isSyncToD365,0) = 0";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void exportRevenue(Connection cnTemp, Connection stagingCnTemp, String postingFromDate, String callFrom) throws Exception {
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTemp2 = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        List<RevenueHeader_Mdl> RevenueHeaderList = new ArrayList<>();
        List<RevenueDetail_Mdl> RevenueDetailsList = new ArrayList<>();

        try {
            if (callFrom.equalsIgnoreCase("ipdRevenu")) {
                RevenueHeaderList = getipdRevenueheader(cnTemp, postingFromDate);
                RevenueDetailsList = getipdRevenueheaderDetails(cnTemp, postingFromDate);
            } else if (callFrom.equalsIgnoreCase("opdRevenu")) {
                RevenueHeaderList = getOpdRevenueheader(cnTemp, postingFromDate);
                RevenueDetailsList = getOpdRevenueheaderDetails(cnTemp, postingFromDate);
            }

            // ===================== HIS_Revenue_Header (Batch) =====================
            sqlQuery = "insert into HIS_Revenue_Header ("
                    + " regDocId, regNo, entryNo, billNo, billDate, patientClass, patientName, UHID,"
                    + " specialityCode, speciality, sponsorCode, sponsorName, payorCode, payorCategory,"
                    + " payorName, registrationNo, admissionDate, admissionTime, dischargeDate, dischargeTime,"
                    + " externalDocumentNo, facilityID, validationHISKey, admissionSource, packagePatient,"
                    + " admissionBedCategory, dischargeBedCategory, departmentNameOrLocationName,"
                    + " departmentCodeOrLocationCode, systemCreatedAt, systemCreatedBy, grossAmount,"
                    + " mouDiscount, addOnDiscount, netAmount, patientPayable, payorPayable) "
                    + " values (:regDocId, :regNo, :entryNo, :billNo, :billDate, :patientClass, :patientName, :uhid,"
                    + " :specialityCode, :speciality, :sponsorCode, :sponsorName, :payorCode, :payorCategory,"
                    + " :payorName, :registrationNo, :admissionDate, :admissionTime, :dischargeDate, :dischargeTime,"
                    + " :externalDocumentNo, :facilityID, :validationHISKey, :admissionSource, :packagePatient,"
                    + " :admissionBedCategory, :dischargeBedCategory, :departmentNameOrLocationName,"
                    + " :departmentCodeOrLocationCode, :systemCreatedAt, :systemCreatedBy, :grossAmount,"
                    + " :mouDiscount, :addOnDiscount, :netAmount, :patientPayable, :payorPayable) ";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);

            sqlQuery = " update salebill set "
                    + " isSyncToD365 = 1 "
                    + " where docid = :docid ";
            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            for (RevenueHeader_Mdl listTemp : RevenueHeaderList) {

                psTemp.setString("regDocId", DmShared.xNull(listTemp.getRegDocId()));
                psTemp.setString("regNo", DmShared.xNull(listTemp.getRegNo()));
                psTemp.setString("entryNo", DmShared.xNull(listTemp.getEntryNo()));
                psTemp.setString("billNo", DmShared.xNull(listTemp.getBillNo()));
                psTemp.setString("billDate", DmShared.dNull(listTemp.getBillDate()));
                psTemp.setString("patientClass", DmShared.xNull(listTemp.getPatientClass()));
                psTemp.setString("patientName", DmShared.xNull(listTemp.getPatientName()));
                psTemp.setString("uhid", DmShared.xNull(listTemp.getUhid()));
                psTemp.setString("specialityCode", DmShared.xNull(listTemp.getSpecialityCode()));
                psTemp.setString("speciality", DmShared.xNull(listTemp.getSpeciality()));
                psTemp.setString("sponsorCode", DmShared.xNull(listTemp.getSponsorCode()));
                psTemp.setString("sponsorName", DmShared.xNull(listTemp.getSponsorName()));
                psTemp.setString("payorCode", DmShared.xNull(listTemp.getPayorCode()));
                psTemp.setString("payorCategory", DmShared.xNull(listTemp.getPayorCategory()));
                psTemp.setString("payorName", DmShared.xNull(listTemp.getPayorName()));
                psTemp.setString("registrationNo", DmShared.xNull(listTemp.getRegistrationNo()));
                psTemp.setString("admissionDate", DmShared.dNull(listTemp.getAdmissionDate()));
                psTemp.setString("admissionTime", listTemp.getAdmissionTime());
                psTemp.setString("dischargeDate", DmShared.dNull(listTemp.getDischargeDate()));
                psTemp.setString("dischargeTime", listTemp.getDischargeTime());
                psTemp.setString("externalDocumentNo", DmShared.xNull(listTemp.getExternalDocumentNo()));
                psTemp.setString("facilityID", DmShared.xNull(listTemp.getFacilityID()));
                psTemp.setString("validationHISKey", DmShared.xNull(listTemp.getValidationHISKey()));
                psTemp.setString("admissionSource", DmShared.xNull(listTemp.getAdmissionSource()));
                psTemp.setString("packagePatient", DmShared.xNull(listTemp.getPackagePatient()));
                psTemp.setString("admissionBedCategory", DmShared.xNull(listTemp.getAdmissionBedCategory()));
                psTemp.setString("dischargeBedCategory", DmShared.xNull(listTemp.getDischargeBedCategory()));
                psTemp.setString("departmentNameOrLocationName", DmShared.xNull(listTemp.getDepartmentNameOrLocationName()));
                psTemp.setString("departmentCodeOrLocationCode", DmShared.xNull(listTemp.getDepartmentCodeOrLocationCode()));
                psTemp.setString("systemCreatedAt", DmShared.xNull(listTemp.getSystemCreatedAt()));
                psTemp.setString("systemCreatedBy", DmShared.xNull(listTemp.getSystemCreatedBy()));
                psTemp.setString("grossAmount", DmServer.vNull(listTemp.getGrossAmount()));
                psTemp.setString("mouDiscount", DmServer.vNull(listTemp.getMouDiscount()));
                psTemp.setString("addOnDiscount", DmServer.vNull(listTemp.getAddOnDiscount()));
                psTemp.setString("netAmount", DmServer.vNull(listTemp.getNetAmount()));
                psTemp.setString("patientPayable", DmServer.vNull(listTemp.getPatientPayable()));
                psTemp.setString("payorPayable", DmServer.vNull(listTemp.getPayorPayable()));
                psTemp.addBatch();

                psTemp2.setString("docid", DmShared.xNull(listTemp.getEntryNo()));
                psTemp2.addBatch();
            }

            psTemp.executeBatch();
            psTemp.close();

            psTemp2.executeBatch();
            psTemp2.close();

            // ===================== HIS_Revenue_Detail (Batch) =====================
            sqlQuery = "insert into HIS_Revenue_Detail ("
                    + " entryNo, billNo, serviceType, GLAccountName, GLAccountNo, department, departmentCode,"
                    + " serviceCategory, serviceItemID, serviceItemName, serviceItemCode, quantity, rate,"
                    + " grossAmount, mouDiscount, addOnDiscount, netAmount, locationCode, locationName,"
                    + " createdBy, createdDateTime, payorCode, payorCategory, payorName, serviceLineNo,"
                    + " departmentId, facilityID, headerId, netPayableAmount, validationHISKey) "
                    + " values (:entryNo, :billNo, :serviceType, :glAccountName, :glAccountNo, :department, :departmentCode,"
                    + " :serviceCategory, :serviceItemID, :serviceItemName, :serviceItemCode, :quantity, :rate,"
                    + " :grossAmount, :mouDiscount, :addOnDiscount, :netAmount, :locationCode, :locationName,"
                    + " :createdBy, :createdDateTime, :payorCode, :payorCategory, :payorName, :serviceLineNo,"
                    + " :departmentId, :facilityID, :headerId, :netPayableAmount, :validationHISKey) ";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sqlQuery);

            for (RevenueDetail_Mdl listTemp : RevenueDetailsList) {

                psTemp.setString("entryNo", DmShared.xNull(listTemp.getEntryNo()));
                psTemp.setString("billNo", DmShared.xNull(listTemp.getBillNo()));
                psTemp.setString("serviceType", DmShared.xNull(listTemp.getServiceType()));
                psTemp.setString("glAccountName", DmShared.xNull(listTemp.getGlAccountName()));
                psTemp.setString("glAccountNo", DmShared.xNull(listTemp.getGlAccountNo()));
                psTemp.setString("department", DmShared.xNull(listTemp.getDepartment()));
                psTemp.setString("departmentCode", DmShared.xNull(listTemp.getDepartmentCode()));
                psTemp.setString("serviceCategory", DmShared.xNull(listTemp.getServiceCategory()));
                psTemp.setString("serviceItemID", DmServer.vNull(listTemp.getServiceItemID()));
                psTemp.setString("serviceItemName", DmShared.xNull(listTemp.getServiceItemName()));
                psTemp.setString("serviceItemCode", DmShared.xNull(listTemp.getServiceItemCode()));
                psTemp.setString("quantity", DmServer.vNull(listTemp.getQuantity()));
                psTemp.setString("rate", DmServer.vNull(listTemp.getRate()));
                psTemp.setString("grossAmount", DmServer.vNull(listTemp.getGrossAmount()));
                psTemp.setString("mouDiscount", DmServer.vNull(listTemp.getMouDiscount()));
                psTemp.setString("addOnDiscount", DmServer.vNull(listTemp.getAddOnDiscount()));
                psTemp.setString("netAmount", DmServer.vNull(listTemp.getNetAmount()));
                psTemp.setString("locationCode", DmShared.xNull(listTemp.getLocationCode()));
                psTemp.setString("locationName", DmShared.xNull(listTemp.getLocationName()));
                psTemp.setString("createdBy", DmShared.xNull(listTemp.getCreatedBy()));
                psTemp.setString("createdDateTime", DmShared.dNull(listTemp.getCreatedDateTime()));
                psTemp.setString("payorCode", DmServer.vNull(listTemp.getPayorCode()));
                psTemp.setString("payorCategory", DmShared.xNull(listTemp.getPayorCategory()));
                psTemp.setString("payorName", DmShared.xNull(listTemp.getPayorName()));
                psTemp.setString("serviceLineNo", DmShared.xNull(listTemp.getServiceLineNo()));
                psTemp.setString("departmentId", DmShared.xNull(listTemp.getDepartmentId()));
                psTemp.setString("facilityID", DmShared.xNull(listTemp.getFacilityID()));
                psTemp.setString("headerId", DmShared.xNull(listTemp.getHeaderId()));
                psTemp.setString("netPayableAmount", DmServer.vNull(listTemp.getNetPayableAmount()));
                psTemp.setString("validationHISKey", DmShared.xNull(listTemp.getValidationHISKey()));
                psTemp.addBatch();

            }

            psTemp.executeBatch();
            psTemp.close();
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
    }

    public static void exportCancelbill(Connection cnTemp, Connection stagingCnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTemp2 = null;
        ResultSet rsTemp = null;
        String sql = "";
        List<CancelIPDBill_Mdl> CancelHeaderList = new ArrayList<>();
        List<CancelIPDBill_Mdl> CancelDetailsList = new ArrayList<>();
        try {

            CancelHeaderList = getCancelIpdBillheader(cnTemp, postingFromDate);
            CancelDetailsList = getCancelIpdBillLine(cnTemp, postingFromDate);
            if (CancelHeaderList == null || CancelHeaderList.isEmpty()) {
                throw new Exception("No cancel bills data found to post for the selected date.");
            }

            sql = "INSERT INTO Cancel_IPDBillHeader "
                    + "(regNo, entryNo, billNo, billDate, CancellBillDocumentNo, CancellBillDocumentDate, "
                    + "patientClass, patientName, UHID, specialityCode, speciality, sponsorCode, sponsorName, "
                    + "payorCode, payorCategory, payorName, registrationNo, admissionDate, admissionTime, "
                    + "dischargeDate, dischargeTime, externalDocumentNo, facilityID, validationHISKey, "
                    + "admissionSource, packagePatient, admissionBedCategory, dischargeBedCategory, "
                    + "departmentNameOrLocationName, departmentCodeOrLocationCode, systemCreatedAt, "
                    + "systemCreatedBy, grossAmount, mouDiscount, netAmount) "
                    + "VALUES "
                    + "(:regNo, :entryNo, :billNo, :billDate, :cancelDocNo, :cancelDocDate, "
                    + ":patientClass, :patientName, :uhid, :specialityCode, :speciality, :sponsorCode, :sponsorName, "
                    + ":payorCode, :payorCategory, :payorName, :registrationNo, :admissionDate, :admissionTime, "
                    + ":dischargeDate, :dischargeTime, :externalDocumentNo, :facilityID, :validationHISKey, "
                    + ":admissionSource, :packagePatient, :admissionBedCategory, :dischargeBedCategory, "
                    + ":deptName, :deptCode, :systemCreatedAt, "
                    + ":systemCreatedBy, :grossAmount, :mouDiscount, :netAmount)";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sql);

            sql = " update salebill set "
                    + " isSyncToD365 = 1 "
                    + " where docid = :docid ";
            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sql);

            for (CancelIPDBill_Mdl h : CancelHeaderList) {
                psTemp.setString("regNo", DmShared.xNull(h.getRegNo()));
                psTemp.setString("entryNo", h.getEntryNo());
                psTemp.setString("billNo", DmShared.xNull(h.getBillNo()));
                psTemp.setString("billDate", DmShared.dNull(h.getBillDate()));
                psTemp.setString("cancelDocNo", DmShared.xNull(h.getCancellBillDocumentNo()));
                psTemp.setString("cancelDocDate", DmShared.dNull(h.getCancellBillDocumentDate()));
                psTemp.setString("patientClass", DmShared.xNull(h.getPatientClass()));
                psTemp.setString("patientName", DmShared.xNull(h.getPatientName()));
                psTemp.setString("uhid", DmShared.xNull(h.getUHID()));
                psTemp.setString("specialityCode", DmServer.vNull(h.getSpecialityCode()));
                psTemp.setString("speciality", DmShared.xNull(h.getSpeciality()));
                psTemp.setString("sponsorCode", h.getSponsorCode());
                psTemp.setString("sponsorName", DmShared.xNull(h.getSponsorName()));
                psTemp.setString("payorCode", h.getPayorCode());
                psTemp.setString("payorCategory", DmShared.xNull(h.getPayorCategory()));
                psTemp.setString("payorName", DmShared.xNull(h.getPayorName()));
                psTemp.setString("registrationNo", DmShared.xNull(h.getRegistrationNo()));
                psTemp.setString("admissionDate", DmShared.dNull(h.getAdmissionDate()));
                psTemp.setString("admissionTime", DmServer.vNull(h.getAdmissionTime()));
                psTemp.setString("dischargeDate", DmShared.dNull(h.getDischargeDate()));
                psTemp.setString("dischargeTime", DmServer.vNull(h.getDischargeTime()));
                psTemp.setString("externalDocumentNo", DmShared.xNull(h.getExternalDocumentNo()));
                psTemp.setString("facilityID", DmShared.xNull(h.getFacilityID()));
                psTemp.setString("validationHISKey", DmShared.xNull(h.getValidationHISKey()));
                psTemp.setString("admissionSource", DmShared.xNull(h.getAdmissionSource()));
                psTemp.setString("packagePatient", DmShared.xNull(h.getPackagePatient()));
                psTemp.setString("admissionBedCategory", DmShared.xNull(h.getAdmissionBedCategory()));
                psTemp.setString("dischargeBedCategory", DmShared.xNull(h.getDischargeBedCategory()));
                psTemp.setString("deptName", DmShared.xNull(h.getDepartmentNameOrLocationName()));
                psTemp.setString("deptCode", h.getDepartmentCodeOrLocationCode());
                psTemp.setString("systemCreatedAt", DmShared.xNull(h.getSystemCreatedAt()));
                psTemp.setString("systemCreatedBy", DmShared.xNull(h.getSystemCreatedBy()));
                psTemp.setString("grossAmount", DmServer.vNull(h.getGrossAmount()));
                psTemp.setString("mouDiscount", DmServer.vNull(h.getMouDiscount()));
                psTemp.setString("netAmount", DmServer.vNull(h.getNetAmount()));

                psTemp.addBatch();

                psTemp2.setString("docid", DmShared.xNull(h.getEntryNo()));
                psTemp2.addBatch();
            }
            psTemp.executeBatch();
            psTemp.close();

            psTemp2.executeBatch();
            psTemp2.close();

            sql = "INSERT INTO Cancel_IPDBillLine "
                    + "(EntryNo, BillNo, BillDate, CancellBillDocumentNo, CancellBillDocumentDate, serviceType, "
                    + "GLAccountName, GLAccountNo, Department, DepartmentCode, ServiceCategory, ServiceItemID, "
                    + "ServiceItemName, ServiceItemCode, Quantity, Rate, GrossAmount, MOUDiscount, AddOnDiscount, "
                    + "NetAmount, LocationCode, LocationName, CreatedBy, CreatedDateTime, PayorCode, PayorCategory, "
                    + "PayorName, ServiceLineNo, DepartmentId, FacilityID, HeaderId, netPayableAmount, ValidationHISKey) "
                    + "VALUES "
                    + "(:entryNo, :billNo, :billDate, :cancelDocNo, :cancelDocDate, :serviceType, "
                    + ":glAccountName, :glAccountNo, :department, :departmentCode, :serviceCategory, :serviceItemID, "
                    + ":serviceItemName, :serviceItemCode, :quantity, :rate, :grossAmount, :mouDiscount, :addOnDiscount, "
                    + ":netAmount, :locationCode, :locationName, :createdBy, :createdDateTime, :payorCode, :payorCategory, "
                    + ":payorName, :serviceLineNo, :departmentId, :facilityID, :headerId, :netPayableAmount, :validationHISKey)";

            psTemp = DmPreparedStatement.prepareStatement(stagingCnTemp, sql);
            for (CancelIPDBill_Mdl d : CancelDetailsList) {

                psTemp.setString("entryNo", DmShared.xNull(d.getEntryNo()));
                psTemp.setString("billNo", DmShared.xNull(d.getBillNo()));
                psTemp.setString("billDate", DmShared.dNull(d.getBillDate()));
                psTemp.setString("cancelDocNo", DmShared.xNull(d.getCancellBillDocumentNo()));
                psTemp.setString("cancelDocDate", DmShared.dNull(d.getCancellBillDocumentDate()));
                psTemp.setString("serviceType", DmShared.xNull(d.getServiceType()));
                psTemp.setString("glAccountName", DmShared.xNull(d.getGlAccountName()));
                psTemp.setString("glAccountNo", DmShared.xNull(d.getGlAccountNo()));
                psTemp.setString("department", DmShared.xNull(d.getDepartmentNameOrLocationName()));
                psTemp.setString("departmentCode", DmShared.xNull(d.getDepartmentCodeOrLocationCode()));
                psTemp.setString("serviceCategory", DmShared.xNull(d.getServiceCategory()));
                psTemp.setString("serviceItemID", DmShared.xNull(d.getServiceItemID()));
                psTemp.setString("serviceItemName", DmShared.xNull(d.getServiceItemName()));
                psTemp.setString("serviceItemCode", DmShared.xNull(d.getServiceItemCode()));
                psTemp.setString("quantity", DmServer.vNull(d.getQuantity()));
                psTemp.setString("rate", DmServer.vNull(d.getRate()));
                psTemp.setString("grossAmount", DmServer.vNull(d.getGrossAmount()));
                psTemp.setString("mouDiscount", DmServer.vNull(d.getMouDiscount()));
                psTemp.setString("addOnDiscount", DmServer.vNull(d.getAddOnDiscount()));
                psTemp.setString("netAmount", DmServer.vNull(d.getNetAmount()));
                psTemp.setString("locationCode", DmShared.xNull(d.getDepartmentCodeOrLocationCode()));
                psTemp.setString("locationName", DmShared.xNull(d.getDepartmentNameOrLocationName()));
                psTemp.setString("createdBy", DmShared.xNull(d.getSystemCreatedBy()));
                psTemp.setString("createdDateTime", DmShared.dNull(d.getSystemCreatedAt()));
                psTemp.setString("payorCode", DmShared.xNull(d.getPayorCode()));
                psTemp.setString("payorCategory", DmShared.xNull(d.getPayorCategory()));
                psTemp.setString("payorName", DmShared.xNull(d.getPayorName()));
                psTemp.setString("serviceLineNo", DmShared.xNull(d.getServiceLineNo()));
                psTemp.setString("departmentId", DmShared.xNull(d.getDepartmentId()));
                psTemp.setString("facilityID", DmShared.xNull(d.getFacilityID()));
                psTemp.setString("headerId", DmShared.xNull(d.getHeaderId()));
                psTemp.setString("netPayableAmount", DmServer.vNull(d.getNetAmount()));
                psTemp.setString("validationHISKey", DmShared.xNull(d.getValidationHISKey()));

                psTemp.addBatch();
            }
            psTemp.executeBatch();
            psTemp.close();
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
    }

    public static List<RevenueHeader_Mdl> getipdRevenueheader(Connection cnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        List<RevenueHeader_Mdl> list = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            condition = "sb.v_Date = '" + dateValue + "'";

            sqlQuery = "select * from (\n"
                    + "select billDetail.*,prc.prcAmount as PatientPayable ,cto.orgApproval as PayorPayable\n"
                    + "from \n"
                    + "(\n"
                    + "select  \n"
                    + "\n"
                    + " pr.regDocId AS regDocId,max(pr.recid) as RegNo,\n"
                    + "    MAX(sb.docId) AS EntryNo,\n"
                    + "    MAX(sb.recId) AS BillNo,Max(sb.v_date) as BillDate,\n"
                    + "    MAX(vtpr.short_Name) AS PatientClass,\n"
                    + "    MAX(pu.patientName) AS PatientName,\n"
                    + "    MAX(pu.recId) AS UHID,\n"
                    + "    MAX(csm.code) AS SpecialityCode,\n"
                    + "    MAX(csm.name) AS Speciality,\n"
                    + "    MAX(om.subCode) AS SponsorCode,\n"
                    + "    MAX(sgOrg.name) AS SponsorName,\n"
                    + "    MAX(om.subCode) AS PayorCode,\n"
                    + "    MAX(CASE WHEN om.isTpa = 1 THEN 'TPA' ELSE 'General' END) AS PayorCategory,\n"
                    + "    MAX(sgOrg.name) AS PayorName,\n"
                    + "    MAX(pr.recid) AS RegistrationNo,\n"
                    + "    MAX(pr.v_Date) AS AdmissionDate,\n"
                    + "    MAX(pr.regTime) AS AdmissionTime,\n"
                    + "    MAX(pd.dischargeDate) AS dischargeDate,\n"
                    + "    MAX(pd.dischargeTime) AS dischargeTime,\n"
                    + "    MAX(sb.recId) AS ExternalDocumentNo,\n"
                    + "    MAX(sm.BusinessUnit) AS FacilityID, \n"
                    + "    MAX(CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(om.subCode AS VARCHAR(50)))) AS ValidationHISKey,\n"
                    + "    MAX(vtprold.short_Name) AS AdmissionSource,\n"
                    + "    'False' AS PackagePatient , \n"
                    + "	min(rcmad.name) AdmissionBedCategory,max(rcmdis.name) as DischargeBedCategory ,\n"
                    + "	max(dpt.name) as departmentNameOrLocationName , \n"
                    + "	 max(dpt.subCode) as departmentCodeOrLocationCode,max(pr.u_EntDt) SystemCreatedAt,\n"
                    + "	 max(pr.preparedBy) as SystemCreatedBy , sum(psd.quantity*psd.rate) as GrossAmount ,\n"
                    + "	 sum(isNull(psd.otherDiscount,0)) as MOUDiscount ,\n"
                    + "	  sum(isNull(psd.Discount,0)) as AddOnDiscount,max(sb.oh_Amt_Net) NetAmount\n"
                    + "from saleBill sb\n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "left join patientRegistration pr on pr.regDocId=sb.regDocId\n"
                    + "left join voucher_Type vtpr on vtpr.v_Type=pr.v_Type\n"
                    + "left join patientUHID pu on pu.docId=pr.uhid\n"
                    + "left join consultantMast cons on cons.subCode=pr.consultantCode\n"
                    + "left join consultantSpeciMast csm on csm.code=cons.consultantSpeciCode \n"
                    + "left join organizationMast om on om.subCode=pr.organizationCode\n"
                    + "left join subGroup sgOrg on sgOrg.subCode=om.subCode\n"
                    + "left join patientDischarge pd on pd.regDocId=pr.regDocId\n"
                    + "left join siteMast sm on sm.code=sb.site_Code\n"
                    + "left join patientRegistration prOld on prold.regDocId=pr.regDocIdOld\n"
                    + "left join voucher_Type vtprold on vtprold.v_Type=prOld.v_Type\n"
                    + "left join subGroup as dpt on dpt.subCode=pr.departmentCode\n"
                    + "left join patientServiceDetail as psd on psd.sbDocId=sb.docId\n"
                    + "left join voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type\n"
                    + "left join patientServiceMast as psm on psd.serviceCode = psm.code \n"
                    + "left join subGroup sgsa on psm.serviceAc = sgsa.subcode\n"
                    + "left join roomAllotmentHistory rmh on rmh.regDocId=pr.regDocId and rmh.isSystemDefine=1 and rmh.status='A'\n"
                    + "left join roomMast rmad on rmad.code=rmh.roomCode\n"
                    + "left join roomCatMast rcmad on rcmad.code=rmad.roomCategoryCode\n"
                    + "left join roomMast rmdis on rmdis.code=pr.roomCode\n"
                    + "left join roomCatMast rcmdis on rcmdis.code=rmdis.roomCategoryCode\n"
                    + " \n"
                    + "\n"
                    + "\n"
                    + "\n"
                    + "where psm.shortName<>'ROFF' and psd.isCancelled=0 and  psd.billingNature in (0,1)"
                    + " and  vtpsd.category not in ('OBSSD') and isnull(sb.isSyncToD365,0)=0\n"
                    + "and " + condition + " "
                    + "and  isNull(sb.approvedBy,'') <> '' "
                    + " and  vt.short_Name in ('IPDBL')"
                    + " and isnull(sb.isCancelled,0)=0 \n"
                    + "  \n"
                    + " group by pr.regDocId\n"
                    + " ) as billDetail \n"
                    + "\n"
                    + " left join (\n"
                    + " select prc.regDocid ,sum(prc.amount) as prcAmount\n"
                    + " from patientReceipt as prc \n"
                    + " left join voucher_Type vt on vt.v_Type=prc.v_Type\n"
                    + " where prc.isCancelled=0 and vt.category in ('IPDRC','IPDRF')\n"
                    + " group by prc.regDocId\n"
                    + " \n"
                    + " ) as prc on prc.regDocid=billDetail.regDocId\n"
                    + "\n"
                    + "  left join (\n"
                    + " select pla.regDocid ,sum(pla.amount) as orgApproval\n"
                    + " from patientLimitApproval as pla \n"
                    + " \n"
                    + " where pla.isCancelled=0 and    pla.nature = 50\n"
                    + " group by pla.regDocId\n"
                    + " \n"
                    + " ) as cto on cto.regDocid=billDetail.regDocId\n"
                    + "\n"
                    + " ) as tmp";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                RevenueHeader_Mdl mdl = new RevenueHeader_Mdl();

                mdl.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mdl.setRegNo(DmShared.xNull(rsTemp.getString("RegNo")));
                mdl.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                mdl.setBillNo(DmShared.xNull(rsTemp.getString("BillNo")));
                mdl.setBillDate(DmShared.dNull(rsTemp.getDate("BillDate")));

                mdl.setPatientClass(DmShared.xNull(rsTemp.getString("PatientClass")));
                mdl.setPatientName(DmShared.xNull(rsTemp.getString("PatientName")));
                mdl.setUhid(DmShared.xNull(rsTemp.getString("UHID")));

                mdl.setSpecialityCode(DmShared.xNull(rsTemp.getString("SpecialityCode")));
                mdl.setSpeciality(DmShared.xNull(rsTemp.getString("Speciality")));

                mdl.setSponsorCode(DmShared.xNull(rsTemp.getString("SponsorCode")));
                mdl.setSponsorName(DmShared.xNull(rsTemp.getString("SponsorName")));

                mdl.setPayorCode(DmShared.xNull(rsTemp.getString("PayorCode")));
                mdl.setPayorCategory(DmShared.xNull(rsTemp.getString("PayorCategory")));
                mdl.setPayorName(DmShared.xNull(rsTemp.getString("PayorName")));

                mdl.setRegistrationNo(DmShared.xNull(rsTemp.getString("RegistrationNo")));

                mdl.setAdmissionDate(DmShared.dNull(rsTemp.getDate("AdmissionDate")));
                mdl.setAdmissionTime(DmShared.xNull(rsTemp.getString("AdmissionTime")));

                mdl.setDischargeDate(DmShared.dNull(rsTemp.getDate("dischargeDate")));
                mdl.setDischargeTime(DmShared.xNull(rsTemp.getString("dischargeTime")));

                mdl.setExternalDocumentNo(DmShared.xNull(rsTemp.getString("ExternalDocumentNo")));
                mdl.setFacilityID(DmShared.xNull(rsTemp.getString("FacilityID")));

                mdl.setValidationHISKey(DmShared.xNull(rsTemp.getString("ValidationHISKey")));
                mdl.setAdmissionSource(DmShared.xNull(rsTemp.getString("AdmissionSource")));
                mdl.setPackagePatient(DmShared.xNull(rsTemp.getString("PackagePatient")));

                mdl.setAdmissionBedCategory(DmShared.xNull(rsTemp.getString("AdmissionBedCategory")));
                mdl.setDischargeBedCategory(DmShared.xNull(rsTemp.getString("DischargeBedCategory")));

                mdl.setDepartmentNameOrLocationName(DmShared.xNull(rsTemp.getString("departmentNameOrLocationName")));
                mdl.setDepartmentCodeOrLocationCode(DmShared.xNull(rsTemp.getString("departmentCodeOrLocationCode")));

                mdl.setSystemCreatedAt(DmShared.xNull(rsTemp.getString("SystemCreatedAt")));
                mdl.setSystemCreatedBy(DmShared.xNull(rsTemp.getString("SystemCreatedBy")));

                mdl.setGrossAmount(DmShared.xNull(rsTemp.getString("GrossAmount")));
                mdl.setMouDiscount(DmShared.xNull(rsTemp.getString("MOUDiscount")));
                mdl.setAddOnDiscount(DmShared.xNull(rsTemp.getString("AddOnDiscount")));
                mdl.setNetAmount(DmShared.xNull(rsTemp.getString("NetAmount")));

                mdl.setPatientPayable(DmShared.xNull(rsTemp.getString("PatientPayable")));
                mdl.setPayorPayable(DmShared.xNull(rsTemp.getString("PayorPayable")));

                list.add(mdl);
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
        return list;
    }

    public static List<RevenueDetail_Mdl> getipdRevenueheaderDetails(Connection cnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        List<RevenueDetail_Mdl> list = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            condition = "sb.v_Date = '" + dateValue + "'";
            sqlQuery = " \n"
                    + "select  ps.recId as EntryNo ,sb.recId as BillNo,vtpsd.description as serviceType  ,sgsa.name as GLAccountName,sgsa.d365_code as GLAccountNo,dpt.name as Department,\n"
                    + "dpt.d365_code as DepartmentCode  , pscm.name as ServiceCategory,psm.code as ServiceItemID ,psm.name as ServiceItemName,\n"
                    + "psm.shortName as ServiceItemCode ,psd.quantity as Quantity,psd.rate as Rate ,psd.quantity*psd.rate as GrossAmount,\n"
                    + "isNull(psd.otherDiscount,0) as MOUDiscount ,isNull(psd.Discount,0) as AddOnDiscount,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(psd.Discount,0) NetAmount ,\n"
                    + "dpt.d365_code as LocationCode ,dpt.name as LocationName ,ps.preparedBy as CreatedBy , ps.u_EntDt as CreatedDateTime,\n"
                    + "om.subCode as PayorCode ,CASE WHEN om.isTpa = 1 THEN 'TPA' ELSE 'General' END as	PayorCategory ,\n"
                    + "sgOrg.name as	PayorName , psd.v_SNo as ServiceLineNo ,dpt.subCode as DepartmentId,sm.BusinessUnit as FacilityID,\n"
                    + "sb.docId as HeaderId,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(psd.Discount,0) as netPayableAmount,\n"
                    + " (CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(om.subCode AS VARCHAR(50)))) AS ValidationHISKey\n"
                    + "\n"
                    + "from saleBill sb\n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "left join patientRegistration pr on pr.regDocId=sb.regDocId\n"
                    + "left join voucher_Type vtpr on vtpr.v_Type=pr.v_Type\n"
                    + "left join patientUHID pu on pu.docId=pr.uhid\n"
                    + "left join consultantMast cons on cons.subCode=pr.consultantCode\n"
                    + "left join consultantSpeciMast csm on csm.code=cons.consultantSpeciCode \n"
                    + "left join organizationMast om on om.subCode=pr.organizationCode\n"
                    + "left join subGroup sgOrg on sgOrg.subCode=om.subCode\n"
                    + "left join patientDischarge pd on pd.regDocId=pr.regDocId\n"
                    + "left join siteMast sm on sm.code=sb.site_Code\n"
                    + "left join patientRegistration prOld on prold.regDocId=pr.regDocIdOld\n"
                    + "left join voucher_Type vtprold on vtprold.v_Type=prOld.v_Type\n"
                    + "left join subGroup as dpt on dpt.subCode=pr.departmentCode\n"
                    + "left join patientServiceDetail as psd on psd.sbDocId=sb.docId\n"
                    + "left join patientService as ps on ps.docId=psd.docId\n"
                    + "left join voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type\n"
                    + "left join patientServiceMast as psm on psd.serviceCode = psm.code \n"
                    + "left join patientServiceCatMast pscm on pscm.code=psm.categoryCode\n"
                    + "left join subGroup sgsa on pscm.serviceAc = sgsa.subcode\n"
                    + "left join roomAllotmentHistory rmh on rmh.regDocId=pr.regDocId and rmh.isSystemDefine=1 and rmh.status='A'\n"
                    + "left join roomMast rmad on rmad.code=rmh.roomCode\n"
                    + "left join roomCatMast rcmad on rcmad.code=rmad.roomCategoryCode\n"
                    + "left join roomMast rmdis on rmdis.code=pr.roomCode\n"
                    + "left join roomCatMast rcmdis on rcmdis.code=rmdis.roomCategoryCode\n"
                    + "left join voucher_Type vtps on vtps.v_Type=ps.v_Type\n"
                    + "\n"
                    + "where psm.shortName<>'ROFF' and psd.isCancelled=0 and  psd.billingNature in (0,1)"
                    + " and  vtpsd.category not in ('OBSSD') and isnull(sb.isSyncToD365,0)=0 \n"
                    + "and " + condition + "\n"
                    + " and  isNull(sb.approvedBy,'') <> '' "
                    + "and  vt.short_Name in ('IPDBL') "
                    + "and isnull(sb.isCancelled,0)=0 "
                    + " and sb.v_prefix > 24 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                RevenueDetail_Mdl mdl = new RevenueDetail_Mdl();

                mdl.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                mdl.setBillNo(DmShared.xNull(rsTemp.getString("BillNo")));
                mdl.setServiceType(DmShared.xNull(rsTemp.getString("serviceType")));
                mdl.setGlAccountName(DmShared.xNull(rsTemp.getString("GLAccountName")));
                mdl.setGlAccountNo(DmShared.xNull(rsTemp.getString("GLAccountNo")));

                mdl.setDepartment(DmShared.xNull(rsTemp.getString("Department")));
                mdl.setDepartmentCode(DmShared.xNull(rsTemp.getString("DepartmentCode")));

                mdl.setServiceCategory(DmShared.xNull(rsTemp.getString("ServiceCategory")));
                mdl.setServiceItemID(DmShared.xNull(rsTemp.getString("ServiceItemID")));
                mdl.setServiceItemName(DmShared.xNull(rsTemp.getString("ServiceItemName")));
                mdl.setServiceItemCode(DmShared.xNull(rsTemp.getString("ServiceItemCode")));

                mdl.setQuantity(DmShared.xNull(rsTemp.getString("Quantity")));
                mdl.setRate(DmShared.xNull(rsTemp.getString("Rate")));

                mdl.setGrossAmount(DmShared.xNull(rsTemp.getString("GrossAmount")));
                mdl.setMouDiscount(DmShared.xNull(rsTemp.getString("MOUDiscount")));
                mdl.setAddOnDiscount(DmShared.xNull(rsTemp.getString("AddOnDiscount")));
                mdl.setNetAmount(DmShared.xNull(rsTemp.getString("NetAmount")));

                mdl.setLocationCode(DmShared.xNull(rsTemp.getString("LocationCode")));
                mdl.setLocationName(DmShared.xNull(rsTemp.getString("LocationName")));

                mdl.setCreatedBy(DmShared.xNull(rsTemp.getString("CreatedBy")));
                mdl.setCreatedDateTime(DmShared.xNull(rsTemp.getString("CreatedDateTime")));

                mdl.setPayorCode(DmShared.xNull(rsTemp.getString("PayorCode")));
                mdl.setPayorCategory(DmShared.xNull(rsTemp.getString("PayorCategory")));
                mdl.setPayorName(DmShared.xNull(rsTemp.getString("PayorName")));

                mdl.setServiceLineNo(DmShared.xNull(rsTemp.getString("ServiceLineNo")));
                mdl.setDepartmentId(DmShared.xNull(rsTemp.getString("DepartmentId")));

                mdl.setFacilityID(DmShared.xNull(rsTemp.getString("FacilityID")));
                mdl.setHeaderId(DmShared.xNull(rsTemp.getString("HeaderId")));

                mdl.setNetPayableAmount(DmShared.xNull(rsTemp.getString("netPayableAmount")));
                mdl.setValidationHISKey(DmShared.xNull(rsTemp.getString("ValidationHISKey")));

                list.add(mdl);
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
        return list;
    }

    public static List<RevenueHeader_Mdl> getOpdRevenueheader(Connection cnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        List<RevenueHeader_Mdl> list = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            condition = "sb.v_Date = '" + dateValue + "'";

            sqlQuery = "select * from (\n"
                    + "select billDetail.*, 0 as PatientPayable , billDetail.NetAmount as PayorPayable\n"
                    + "from \n"
                    + "(\n"
                    + "select  \n"
                    + "\n"
                    + " '' AS regDocId,'' as RegNo,\n"
                    + "    MAX(sb.docId) AS EntryNo,\n"
                    + "    MAX(sb.recId) AS BillNo,Max(sb.v_date) as BillDate,\n"
                    + "   'OPD' AS PatientClass,\n"
                    + "    MAX(pu.patientName) AS PatientName,\n"
                    + "    MAX(pu.recId) AS UHID,\n"
                    + "   '' AS SpecialityCode,\n"
                    + "    '' AS Speciality,\n"
                    + "    MAX(om.subCode) AS SponsorCode,\n"
                    + "    MAX(sgOrg.name) AS SponsorName,\n"
                    + "    MAX(om.subCode) AS PayorCode,\n"
                    + "    MAX(CASE WHEN om.isTpa = 1 THEN 'TPA' ELSE 'General' END) AS PayorCategory,\n"
                    + "    MAX(sgOrg.name) AS PayorName,\n"
                    + "    '' AS RegistrationNo,\n"
                    + "    '' AS AdmissionDate,\n"
                    + "    '' AS AdmissionTime,\n"
                    + "    '' AS dischargeDate,\n"
                    + "    '' AS dischargeTime,\n"
                    + "    MAX(sb.recId) AS ExternalDocumentNo,\n"
                    + "    MAX(sm.BusinessUnit) AS FacilityID, \n"
                    + "    MAX(CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(om.subCode AS VARCHAR(50)))) AS ValidationHISKey,\n"
                    + "    '' AS AdmissionSource,\n"
                    + "    'False' AS PackagePatient , \n"
                    + "	'' AdmissionBedCategory,'' as DischargeBedCategory ,\n"
                    + "	'' as departmentNameOrLocationName , \n"
                    + "	 '' as departmentCodeOrLocationCode,max(sb.u_EntDt) SystemCreatedAt,\n"
                    + "	 '' as SystemCreatedBy , sum(psd.quantity*psd.rate) as GrossAmount ,\n"
                    + "	 sum(isNull(psd.otherDiscount,0)) as MOUDiscount ,\n"
                    + "	  sum(isNull(psd.Discount,0)) as AddOnDiscount,max(sb.oh_Amt_Net) NetAmount\n"
                    + "from saleBill sb\n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "left join patientUHID pu on pu.docId=sb.uhid\n"
                    + "left join organizationMast om on om.subCode=sb.subCode\n"
                    + "left join subGroup sgOrg on sgOrg.subCode=om.subCode\n"
                    + "left join siteMast sm on sm.code=sb.site_Code\n"
                    + "left join patientServiceDetail as psd on psd.sbDocId=sb.docId\n"
                    + "left join voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type\n"
                    + "left join patientServiceMast as psm on psd.serviceCode = psm.code \n"
                    + "left join subGroup sgsa on psm.serviceAc = sgsa.subcode\n"
                    + " \n"
                    + "\n"
                    + "where isnull(sb.isSyncToD365,0)=0 and  psm.shortName<>'ROFF'\n"
                    + "and  vt.short_Name in ('OBOPD') and isnull(sb.isCancelled,0)=0  \n"
                    + "and psd.isCancelled=0 and  psd.billingNature in (0,1) "
                    + "and  vtpsd.category not in ('OBSSD') and isnull(sb.isSyncToD365,0)=0 \n"
                    + "and " + condition + " \n"
                    + "  \n"
                    + " group by sb.docId\n"
                    + " ) as billDetail \n"
                    + "\n"
                    + " ) as tmp";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                RevenueHeader_Mdl mdl = new RevenueHeader_Mdl();

                mdl.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mdl.setRegNo(DmShared.xNull(rsTemp.getString("RegNo")));
                mdl.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                mdl.setBillNo(DmShared.xNull(rsTemp.getString("BillNo")));
                mdl.setBillDate(DmShared.dNull(rsTemp.getDate("BillDate")));

                mdl.setPatientClass(DmShared.xNull(rsTemp.getString("PatientClass")));
                mdl.setPatientName(DmShared.xNull(rsTemp.getString("PatientName")));
                mdl.setUhid(DmShared.xNull(rsTemp.getString("UHID")));

                mdl.setSpecialityCode(DmShared.xNull(rsTemp.getString("SpecialityCode")));
                mdl.setSpeciality(DmShared.xNull(rsTemp.getString("Speciality")));

                mdl.setSponsorCode(DmShared.xNull(rsTemp.getString("SponsorCode")));
                mdl.setSponsorName(DmShared.xNull(rsTemp.getString("SponsorName")));

                mdl.setPayorCode(DmShared.xNull(rsTemp.getString("PayorCode")));
                mdl.setPayorCategory(DmShared.xNull(rsTemp.getString("PayorCategory")));
                mdl.setPayorName(DmShared.xNull(rsTemp.getString("PayorName")));

                mdl.setRegistrationNo(DmShared.xNull(rsTemp.getString("RegistrationNo")));

//                mdl.setAdmissionDate(DmShared.dNull(rsTemp.getString("AdmissionDate")));
                mdl.setAdmissionTime(DmShared.xNull(rsTemp.getString("AdmissionTime")));

//                mdl.setDischargeDate(DmShared.dNull(rsTemp.getString("dischargeDate")));
                mdl.setDischargeTime(DmShared.xNull(rsTemp.getString("dischargeTime")));

                mdl.setExternalDocumentNo(DmShared.xNull(rsTemp.getString("ExternalDocumentNo")));
                mdl.setFacilityID(DmShared.xNull(rsTemp.getString("FacilityID")));

                mdl.setValidationHISKey(DmShared.xNull(rsTemp.getString("ValidationHISKey")));
                mdl.setAdmissionSource(DmShared.xNull(rsTemp.getString("AdmissionSource")));
                mdl.setPackagePatient(DmShared.xNull(rsTemp.getString("PackagePatient")));

                mdl.setAdmissionBedCategory(DmShared.xNull(rsTemp.getString("AdmissionBedCategory")));
                mdl.setDischargeBedCategory(DmShared.xNull(rsTemp.getString("DischargeBedCategory")));

                mdl.setDepartmentNameOrLocationName(DmShared.xNull(rsTemp.getString("departmentNameOrLocationName")));
                mdl.setDepartmentCodeOrLocationCode(DmShared.xNull(rsTemp.getString("departmentCodeOrLocationCode")));

                mdl.setSystemCreatedAt(DmShared.xNull(rsTemp.getString("SystemCreatedAt")));
                mdl.setSystemCreatedBy(DmShared.xNull(rsTemp.getString("SystemCreatedBy")));

                mdl.setGrossAmount(DmShared.xNull(rsTemp.getString("GrossAmount")));
                mdl.setMouDiscount(DmShared.xNull(rsTemp.getString("MOUDiscount")));
                mdl.setAddOnDiscount(DmShared.xNull(rsTemp.getString("AddOnDiscount")));
                mdl.setNetAmount(DmShared.xNull(rsTemp.getString("NetAmount")));

                mdl.setPatientPayable(DmShared.xNull(rsTemp.getString("PatientPayable")));
                mdl.setPayorPayable(DmShared.xNull(rsTemp.getString("PayorPayable")));

                list.add(mdl);
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
        return list;
    }

    public static List<CancelIPDBill_Mdl> getCancelIpdBillheader(Connection cnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        List<CancelIPDBill_Mdl> list = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            condition = "sb.v_Date = '" + dateValue + "'";

            sqlQuery = "select * from (\n"
                    + "\n"
                    + "select billDetail.*\n"
                    + "from \n"
                    + "(\n"
                    + "select \n"
                    + " max(pr.recid) as RegNo,\n"
                    + "    MAX(sb.docId) AS EntryNo,\n"
                    + "    MAX(sbm.recId) AS BillNo,\n"
                    + "    Max(sbm.v_date) as BillDate,\n"
                    + "	MAX(sb.recId) AS CancellBillDocumentNo,\n"
                    + "	Max(sb.v_date) as CancellBillDocumentDate,\n"
                    + "    MAX(vtpr.short_Name) AS PatientClass,\n"
                    + "    MAX(pu.patientName) AS PatientName,\n"
                    + "    MAX(pu.recId) AS UHID,\n"
                    + "    MAX(csm.code) AS SpecialityCode,\n"
                    + "    MAX(csm.name) AS Speciality,\n"
                    + "    MAX(om.subCode) AS SponsorCode,\n"
                    + "    MAX(sgOrg.name) AS SponsorName,\n"
                    + "    MAX(om.subCode) AS PayorCode,\n"
                    + "    MAX(CASE WHEN om.isTpa = 1 THEN 'TPA' ELSE 'General' END) AS PayorCategory,\n"
                    + "    MAX(sgOrg.name) AS PayorName,\n"
                    + "    MAX(pr.recid) AS RegistrationNo,\n"
                    + "    MAX(pr.v_Date) AS AdmissionDate,\n"
                    + "    MAX(pr.regTime) AS AdmissionTime,\n"
                    + "    MAX(pd.dischargeDate) AS dischargeDate,\n"
                    + "    MAX(pd.dischargeTime) AS dischargeTime,\n"
                    + "    MAX(sb.recId) AS ExternalDocumentNo,\n"
                    + "    MAX(sm.BusinessUnit) AS FacilityID, \n"
                    + "    MAX(CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(om.subCode AS VARCHAR(50)))) AS ValidationHISKey,\n"
                    + "    MAX(vtprold.short_Name) AS AdmissionSource,\n"
                    + "    'False' AS PackagePatient , \n"
                    + "	min(rcmad.name) AdmissionBedCategory,max(rcmdis.name) as DischargeBedCategory ,\n"
                    + "	max(dpt.name) as departmentNameOrLocationName , \n"
                    + "	 max(dpt.subCode) as departmentCodeOrLocationCode,max(pr.u_EntDt) SystemCreatedAt,\n"
                    + "	 max(pr.preparedBy) as SystemCreatedBy , sum(psd.quantity*psd.rate) as GrossAmount ,\n"
                    + "	 sum(isNull(psd.otherDiscount,0)) as MOUDiscount ,\n"
                    + "	  sum(isNull(csd.Discount,0)) as AddOnDiscount,abs(max(sb.oh_Amt_Net)) NetAmount\n"
                    + "from saleBill sb\n"
                    + "left join saleBill sbm on sbm.docId=sb.cancellAgainstSbDocId\n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "left join patientRegistration pr on pr.regDocId=sb.regDocId\n"
                    + "left join voucher_Type vtpr on vtpr.v_Type=pr.v_Type\n"
                    + "left join patientUHID pu on pu.docId=pr.uhid\n"
                    + "left join consultantMast cons on cons.subCode=pr.consultantCode\n"
                    + "left join consultantSpeciMast csm on csm.code=cons.consultantSpeciCode \n"
                    + "left join organizationMast om on om.subCode=sb.subCode\n"
                    + "left join subGroup sgOrg on sgOrg.subCode=om.subCode\n"
                    + "left join patientDischarge pd on pd.regDocId=pr.regDocId\n"
                    + "left join siteMast sm on sm.code=sb.site_Code\n"
                    + "left join patientRegistration prOld on prold.regDocId=pr.regDocIdOld\n"
                    + "left join voucher_Type vtprold on vtprold.v_Type=prOld.v_Type\n"
                    + "left join subGroup as dpt on dpt.subCode=pr.departmentCode\n"
                    + "left join cancellBillService as csd on csd.sbDocId=sb.cancellAgainstSbDocId\n"
                    + "left join patientServiceDetail as psd on psd.DocId=csd.psdDocId and  psd.v_SNo=csd.psdV_Sno\n"
                    + "left join voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type\n"
                    + "left join patientServiceMast as psm on psd.serviceCode = psm.code \n"
                    + "left join subGroup sgsa on psm.serviceAc = sgsa.subcode\n"
                    + "left join roomAllotmentHistory rmh on rmh.regDocId=pr.regDocId and rmh.isSystemDefine=1 and rmh.status='A'\n"
                    + "left join roomMast rmad on rmad.code=rmh.roomCode\n"
                    + "left join roomCatMast rcmad on rcmad.code=rmad.roomCategoryCode\n"
                    + "left join roomMast rmdis on rmdis.code=pr.roomCode\n"
                    + "left join roomCatMast rcmdis on rcmdis.code=rmdis.roomCategoryCode\n"
                    + " \n"
                    + "where  psd.isCancelled=0 and  \n"
                    + "psd.billingNature in (0,1) and  vtpsd.category not in ('OBSSD')\n"
                    + "and  vt.short_Name in ('IPCBL') and isnull(sb.isSyncToD365,0)=0 \n"
                    + "and " + condition + "  \n"
                    + "\n"
                    + " group by sb.docId\n"
                    + "\n"
                    + " ) as billDetail \n"
                    + " \n"
                    + " ) as tmp";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                CancelIPDBill_Mdl mdl = new CancelIPDBill_Mdl();

                mdl.setRegNo(DmShared.xNull(rsTemp.getString("RegNo")));
                mdl.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                mdl.setBillNo(DmShared.xNull(rsTemp.getString("BillNo")));
                mdl.setBillDate(DmShared.dNull(rsTemp.getDate("BillDate")));

                mdl.setCancellBillDocumentNo(DmShared.xNull(rsTemp.getString("CancellBillDocumentNo")));

                mdl.setCancellBillDocumentDate(DmShared.dNull(rsTemp.getDate("CancellBillDocumentDate")));

                mdl.setPatientClass(DmShared.xNull(rsTemp.getString("PatientClass")));

                mdl.setPatientName(DmShared.xNull(rsTemp.getString("PatientName")));

                mdl.setUHID(DmShared.xNull(rsTemp.getString("UHID")));

                mdl.setSpecialityCode(DmShared.xNull(rsTemp.getString("SpecialityCode")));

                mdl.setSpeciality(DmShared.xNull(rsTemp.getString("Speciality")));

                mdl.setSponsorCode(DmShared.xNull(rsTemp.getString("SponsorCode")));

                mdl.setSponsorName(DmShared.xNull(rsTemp.getString("SponsorName")));

                mdl.setPayorCode(DmShared.xNull(rsTemp.getString("PayorCode")));

                mdl.setPayorCategory(DmShared.xNull(rsTemp.getString("PayorCategory")));

                mdl.setPayorName(
                        DmShared.xNull(rsTemp.getString("PayorName"))
                );

                mdl.setRegistrationNo(
                        DmShared.xNull(rsTemp.getString("RegistrationNo"))
                );

                mdl.setAdmissionDate(
                        DmShared.dNull(rsTemp.getDate("AdmissionDate"))
                );

                mdl.setAdmissionTime(
                        DmShared.xNull(rsTemp.getString("AdmissionTime"))
                );

                mdl.setDischargeDate(
                        DmShared.dNull(rsTemp.getDate("dischargeDate"))
                );

                mdl.setDischargeTime(
                        DmShared.xNull(rsTemp.getString("dischargeTime"))
                );

                mdl.setExternalDocumentNo(
                        DmShared.xNull(rsTemp.getString("ExternalDocumentNo"))
                );

                mdl.setFacilityID(
                        DmShared.xNull(rsTemp.getString("FacilityID"))
                );

                mdl.setValidationHISKey(
                        DmShared.xNull(rsTemp.getString("ValidationHISKey"))
                );

                mdl.setAdmissionSource(
                        DmShared.xNull(rsTemp.getString("AdmissionSource"))
                );

                mdl.setPackagePatient(
                        DmShared.xNull(rsTemp.getString("PackagePatient"))
                );

                mdl.setAdmissionBedCategory(
                        DmShared.xNull(rsTemp.getString("AdmissionBedCategory"))
                );

                mdl.setDischargeBedCategory(
                        DmShared.xNull(rsTemp.getString("DischargeBedCategory"))
                );

                mdl.setDepartmentNameOrLocationName(
                        DmShared.xNull(rsTemp.getString("departmentNameOrLocationName"))
                );

                mdl.setDepartmentCodeOrLocationCode(
                        DmShared.xNull(rsTemp.getString("departmentCodeOrLocationCode"))
                );

                mdl.setSystemCreatedAt(
                        DmShared.xNull(rsTemp.getString("SystemCreatedAt"))
                );

                mdl.setSystemCreatedBy(
                        DmShared.xNull(rsTemp.getString("SystemCreatedBy"))
                );

                mdl.setGrossAmount(
                        DmShared.xNull(rsTemp.getString("GrossAmount"))
                );

                mdl.setMouDiscount(
                        DmShared.xNull(rsTemp.getString("MOUDiscount"))
                );

                mdl.setAddOnDiscount(
                        DmShared.xNull(rsTemp.getString("AddOnDiscount"))
                );

                mdl.setNetAmount(
                        DmShared.xNull(rsTemp.getString("NetAmount"))
                );

                list.add(mdl);
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
        return list;
    }

    public static List<CancelIPDBill_Mdl> getCancelIpdBillLine(Connection cnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        List<CancelIPDBill_Mdl> list = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            condition = "sb.v_Date = '" + dateValue + "'";

            sqlQuery = "select  ps.recId as EntryNo ,\n"
                    + "\n"
                    + "    sbm.recId AS BillNo,\n"
                    + "    sbm.v_date as BillDate,\n"
                    + "	sb.recId AS CancellBillDocumentNo,\n"
                    + "	sb.v_date as CancellBillDocumentDate,\n"
                    + "vtps.description as serviceType  ,sgsa.name as GLAccountName,sgsa.d365_code as GLAccountNo,dpt.name as Department,\n"
                    + "dpt.d365_code as DepartmentCode  , pscm.name as ServiceCategory,psm.code as ServiceItemID ,psm.name as ServiceItemName,\n"
                    + "psm.shortName as ServiceItemCode ,psd.quantity as Quantity,psd.rate as Rate ,psd.quantity*psd.rate as GrossAmount,\n"
                    + "isNull(psd.otherDiscount,0) as MOUDiscount ,isNull(csd.Discount,0) as AddOnDiscount,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(csd.Discount,0) NetAmount ,\n"
                    + "dpt.d365_code as LocationCode ,dpt.name as LocationName ,ps.preparedBy as CreatedBy , ps.u_EntDt as CreatedDateTime,\n"
                    + "om.subCode as PayorCode ,CASE WHEN om.isTpa = 1 THEN 'TPA' ELSE 'General' END as	PayorCategory ,\n"
                    + "sgOrg.name as	PayorName , psd.v_SNo as ServiceLineNo ,dpt.subCode as DepartmentId,sm.BusinessUnit as FacilityID,\n"
                    + "sb.docId as HeaderId,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(csd.Discount,0) as netPayableAmount,\n"
                    + " (CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(om.subCode AS VARCHAR(50)))) AS ValidationHISKey\n"
                    + "\n"
                    + "from saleBill sb\n"
                    + "left join saleBill sbm on sbm.docId=sb.cancellAgainstSbDocId\n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "left join patientRegistration pr on pr.regDocId=sb.regDocId\n"
                    + "left join voucher_Type vtpr on vtpr.v_Type=pr.v_Type\n"
                    + "left join patientUHID pu on pu.docId=pr.uhid\n"
                    + "left join consultantMast cons on cons.subCode=pr.consultantCode\n"
                    + "left join consultantSpeciMast csm on csm.code=cons.consultantSpeciCode \n"
                    + "left join organizationMast om on om.subCode=sb.subCode\n"
                    + "left join subGroup sgOrg on sgOrg.subCode=om.subCode\n"
                    + "left join patientDischarge pd on pd.regDocId=pr.regDocId\n"
                    + "left join siteMast sm on sm.code=sb.site_Code\n"
                    + "left join patientRegistration prOld on prold.regDocId=pr.regDocIdOld\n"
                    + "left join voucher_Type vtprold on vtprold.v_Type=prOld.v_Type\n"
                    + "left join subGroup as dpt on dpt.subCode=pr.departmentCode\n"
                    + "left join cancellBillService as csd on csd.sbDocId=sb.cancellAgainstSbDocId\n"
                    + "left join patientServiceDetail as psd on psd.DocId=csd.psdDocId and  psd.v_SNo=csd.psdV_Sno\n"
                    + "left join voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type\n"
                    + "left join patientServiceMast as psm on psd.serviceCode = psm.code \n"
                    + "left join patientServiceCatMast pscm on pscm.code=psm.categoryCode\n"
                    + "left join subGroup sgsa on psm.serviceAc = sgsa.subcode\n"
                    + "left join roomAllotmentHistory rmh on rmh.regDocId=pr.regDocId and rmh.isSystemDefine=1 and rmh.status='A'\n"
                    + "left join roomMast rmad on rmad.code=rmh.roomCode\n"
                    + "left join roomCatMast rcmad on rcmad.code=rmad.roomCategoryCode\n"
                    + "left join roomMast rmdis on rmdis.code=pr.roomCode\n"
                    + "left join roomCatMast rcmdis on rcmdis.code=rmdis.roomCategoryCode\n"
                    + "left join patientService as ps on ps.docId=psd.docId\n"
                    + "left join voucher_Type vtps on vtps.v_Type=psd.v_Type\n"
                    + "\n"
                    + "\n"
                    + "where psd.isCancelled=0 and  \n"
                    + "psd.billingNature in (0,1) and  vtpsd.category not in ('OBSSD')\n"
                    + "and  vt.short_Name in ('IPCBL') and isnull(sb.isSyncToD365,0)=0 \n"
                    + "and " + condition + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                CancelIPDBill_Mdl mdl = new CancelIPDBill_Mdl();

                mdl.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                mdl.setBillNo(DmShared.xNull(rsTemp.getString("BillNo")));
                mdl.setBillDate(DmShared.dNull(rsTemp.getDate("BillDate")));

                mdl.setCancellBillDocumentNo(DmShared.xNull(rsTemp.getString("CancellBillDocumentNo")));
                mdl.setCancellBillDocumentDate(DmShared.dNull(rsTemp.getDate("CancellBillDocumentDate")));

                mdl.setPayorCode(DmShared.xNull(rsTemp.getString("PayorCode")));
                mdl.setPayorCategory(DmShared.xNull(rsTemp.getString("PayorCategory")));
                mdl.setPayorName(DmShared.xNull(rsTemp.getString("PayorName")));

                mdl.setFacilityID(DmShared.xNull(rsTemp.getString("FacilityID")));
                mdl.setValidationHISKey(DmShared.xNull(rsTemp.getString("ValidationHISKey")));

                mdl.setDepartmentNameOrLocationName(DmShared.xNull(rsTemp.getString("Department")));
                mdl.setDepartmentCodeOrLocationCode(DmShared.xNull(rsTemp.getString("DepartmentCode")));

                mdl.setSystemCreatedAt(DmShared.xNull(rsTemp.getString("CreatedDateTime")));
                mdl.setSystemCreatedBy(DmShared.xNull(rsTemp.getString("CreatedBy")));

                mdl.setGrossAmount(DmShared.xNull(rsTemp.getString("GrossAmount")));
                mdl.setMouDiscount(DmShared.xNull(rsTemp.getString("MOUDiscount")));
                mdl.setAddOnDiscount(DmShared.xNull(rsTemp.getString("AddOnDiscount")));
                mdl.setNetAmount(DmShared.xNull(rsTemp.getString("NetAmount")));

                mdl.setServiceType(DmShared.xNull(rsTemp.getString("serviceType")));
                mdl.setGlAccountName(DmShared.xNull(rsTemp.getString("GLAccountName")));
                mdl.setGlAccountNo(DmShared.xNull(rsTemp.getString("GLAccountNo")));
                mdl.setServiceCategory(DmShared.xNull(rsTemp.getString("ServiceCategory")));
                mdl.setServiceItemID(DmShared.xNull(rsTemp.getString("ServiceItemID")));
                mdl.setServiceItemName(DmShared.xNull(rsTemp.getString("ServiceItemName")));
                mdl.setServiceItemCode(DmShared.xNull(rsTemp.getString("ServiceItemCode")));
                mdl.setQuantity(DmShared.xNull(rsTemp.getString("Quantity")));
                mdl.setRate(DmShared.xNull(rsTemp.getString("Rate")));
                mdl.setServiceLineNo(DmShared.xNull(rsTemp.getString("ServiceLineNo")));
                mdl.setDepartmentId(DmShared.xNull(rsTemp.getString("DepartmentId")));
                mdl.setHeaderId(DmShared.xNull(rsTemp.getString("HeaderId")));
                mdl.setNetAmount(DmShared.xNull(rsTemp.getString("netPayableAmount")));

                list.add(mdl);
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
        return list;
    }

    public static List<RevenueDetail_Mdl> getOpdRevenueheaderDetails(Connection cnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        List<RevenueDetail_Mdl> list = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            condition = "sb.v_Date = '" + dateValue + "'";

            sqlQuery = "select  ps.recId as EntryNo ,sb.recId as BillNo,vtpsd.description as serviceType  ,sgsa.name as GLAccountName,sgsa.d365_code as GLAccountNo,dpt.name as Department,\n"
                    + "dpt.d365_code as DepartmentCode  , pscm.name as ServiceCategory,psm.code as ServiceItemID ,psm.name as ServiceItemName,\n"
                    + "                    psm.shortName as ServiceItemCode ,psd.quantity as Quantity,psd.rate as Rate ,psd.quantity*psd.rate as GrossAmount,\n"
                    + "                    isNull(psd.otherDiscount,0) as MOUDiscount ,isNull(psd.Discount,0) as AddOnDiscount,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(psd.Discount,0) NetAmount ,\n"
                    + "                    dpt.d365_code as LocationCode ,dpt.name as LocationName ,ps.preparedBy as CreatedBy , ps.u_EntDt as CreatedDateTime,\n"
                    + "                    om.subCode as PayorCode ,CASE WHEN om.isTpa = 1 THEN 'TPA' ELSE 'General' END as	PayorCategory ,\n"
                    + "                    sgOrg.name as	PayorName , psd.v_SNo as ServiceLineNo ,dpt.subCode as DepartmentId,sm.BusinessUnit as FacilityID,\n"
                    + "                    sb.docId as HeaderId,(psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(psd.Discount,0) as netPayableAmount,\n"
                    + "                     (CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(om.subCode AS VARCHAR(50)))) AS ValidationHISKey\n"
                    + "                    from saleBill sb\n"
                    + "                    left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "                    left join patientServiceDetail as psd on psd.sbDocId=sb.docId\n"
                    + "					left join voucher_Type as vtpsd on vtpsd.v_Type = psd.v_Type\n"
                    + "                    left join patientRegistration pr on pr.regDocId=psd.regDocId\n"
                    + "                    left join voucher_Type vtpr on vtpr.v_Type=pr.v_Type\n"
                    + "                    left join patientUHID pu on pu.docId=pr.uhid\n"
                    + "                    left join consultantMast cons on cons.subCode=pr.consultantCode\n"
                    + "                    left join consultantSpeciMast csm on csm.code=cons.consultantSpeciCode \n"
                    + "					left join subGroup2 ref on ref.subCode=pr.referredBy\n"
                    + "                    left join subGroup as dpt on dpt.subCode=\n"
                    + "					\n"
                    + "					case when vtpsd.description in (\n"
                    + "					'Lab Registration',\n"
                    + "					'Lab Cancelation'\n"
                    + "					)\n"
                    + "					then ref.departmentCode\n"
                    + "					else \n"
                    + "					pr.departmentCode\n"
                    + "\n"
                    + "					end \n"
                    + "\n"
                    + "                    left join organizationMast om on om.subCode=sb.subCode\n"
                    + "                    left join subGroup sgOrg on sgOrg.subCode=om.subCode\n"
                    + "                    left join siteMast sm on sm.code=sb.site_Code\n"
                    + "                    left join patientService as ps on ps.docId=psd.docId\n"
                    + "                   \n"
                    + "                    left join patientServiceMast as psm on psd.serviceCode = psm.code \n"
                    + "                    left join patientServiceCatMast pscm on pscm.code=psm.categoryCode\n"
                    + "                    left join subGroup sgsa on pscm.serviceAc = sgsa.subcode\n"
                    + "                    left join voucher_Type vtps on vtps.v_Type=ps.v_Type\n"
                    + "                    where psm.shortName<>'ROFF' and  vt.short_Name in ('OBOPD')  \n"
                    + "                    and isnull(sb.isCancelled,0)=0  and  (psd.isCancelled = 0)\n"
                    + "                    and psd.isCancelled=0 and  psd.billingNature in (0,1)\n"
                    + "                    and  vtpsd.category not in ('OBSSD') "
                    + "                    and isnull(sb.isSyncToD365,0)=0 "
                    + "                    and sb.v_Prefix > 24\n"
                    + "                    and " + condition + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                RevenueDetail_Mdl mdl = new RevenueDetail_Mdl();

                mdl.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                mdl.setBillNo(DmShared.xNull(rsTemp.getString("BillNo")));
                mdl.setServiceType(DmShared.xNull(rsTemp.getString("serviceType")));
                mdl.setGlAccountName(DmShared.xNull(rsTemp.getString("GLAccountName")));
                mdl.setGlAccountNo(DmShared.xNull(rsTemp.getString("GLAccountNo")));

                mdl.setDepartment(DmShared.xNull(rsTemp.getString("Department")));
                mdl.setDepartmentCode(DmShared.xNull(rsTemp.getString("DepartmentCode")));

                mdl.setServiceCategory(DmShared.xNull(rsTemp.getString("ServiceCategory")));
                mdl.setServiceItemID(DmShared.xNull(rsTemp.getString("ServiceItemID")));
                mdl.setServiceItemName(DmShared.xNull(rsTemp.getString("ServiceItemName")));
                mdl.setServiceItemCode(DmShared.xNull(rsTemp.getString("ServiceItemCode")));

                mdl.setQuantity(DmShared.xNull(rsTemp.getString("Quantity")));
                mdl.setRate(DmShared.xNull(rsTemp.getString("Rate")));

                mdl.setGrossAmount(DmShared.xNull(rsTemp.getString("GrossAmount")));
                mdl.setMouDiscount(DmShared.xNull(rsTemp.getString("MOUDiscount")));
                mdl.setAddOnDiscount(DmShared.xNull(rsTemp.getString("AddOnDiscount")));
                mdl.setNetAmount(DmShared.xNull(rsTemp.getString("NetAmount")));

                mdl.setLocationCode(DmShared.xNull(rsTemp.getString("LocationCode")));
                mdl.setLocationName(DmShared.xNull(rsTemp.getString("LocationName")));

                mdl.setCreatedBy(DmShared.xNull(rsTemp.getString("CreatedBy")));
                mdl.setCreatedDateTime(DmShared.xNull(rsTemp.getString("CreatedDateTime")));

                mdl.setPayorCode(DmShared.xNull(rsTemp.getString("PayorCode")));
                mdl.setPayorCategory(DmShared.xNull(rsTemp.getString("PayorCategory")));
                mdl.setPayorName(DmShared.xNull(rsTemp.getString("PayorName")));

                mdl.setServiceLineNo(DmShared.xNull(rsTemp.getString("ServiceLineNo")));
                mdl.setDepartmentId(DmShared.xNull(rsTemp.getString("DepartmentId")));

                mdl.setFacilityID(DmShared.xNull(rsTemp.getString("FacilityID")));
                mdl.setHeaderId(DmShared.xNull(rsTemp.getString("HeaderId")));

                mdl.setNetPayableAmount(DmShared.xNull(rsTemp.getString("netPayableAmount")));
                mdl.setValidationHISKey(DmShared.xNull(rsTemp.getString("ValidationHISKey")));

                list.add(mdl);
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
        return list;
    }

    public static List<StockTransfer_Mdl> getStockTransferData(Connection cnTemp, String postingFromDate) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTempLine = null;
        DmPreparedStatement psTempLine = null;
        String sqlQuery = "", lineQuery = "";
        List<StockTransfer_Mdl> headerList = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);

            // ===== HEADER QUERY =====
            sqlQuery = "select vt.parentVoucher as EntryType \n"
                    + ",CONCAT(CAST(sh.docId AS VARCHAR(50)), \n"
                    + "sm.BusinessUnit) as EntryNumber, sh.docId as docId, sh.v_Date as EntryDate\n"
                    + ",vt.description as DocumentType, sh.recId as DocumentNumber,\n"
                    + "sm.businessUnit ,sm.LegalEntity\n"
                    + "from stockhead sh\n"
                    + "left join voucher_Type vt on vt.v_Type=sh.v_Type\n"
                    + "left join siteMast sm on sm.code=sh.site_Code\n"
                    + " left join stockHeadDetail shd on shd.docId=sh.docId "
                    + " where  vt.parentVoucher in ('Stock Transfer')\n"
                    + "and sh.v_date = :vDate "
                    + " and  isnull(sh.isSyncToD365,0)=0  "
                    + " and shd.departmentToCode is not null ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vDate", dateValue);
            rsTemp = psTemp.executeQuery();

            // ===== ONE HEADER ROW AT A TIME, WITH ITS OWN LINE FETCH =====
            while (rsTemp.next()) {
                StockTransfer_Mdl stockTransfer_Mdl = new StockTransfer_Mdl();
                stockTransfer_Mdl.setDocId(rsTemp.getString("docId"));
                stockTransfer_Mdl.setEntryType(rsTemp.getString("EntryType"));
                stockTransfer_Mdl.setEntryNumber(rsTemp.getString("EntryNumber"));
                stockTransfer_Mdl.setEntryDate(DmShared.dNull(rsTemp.getDate("EntryDate")));
                stockTransfer_Mdl.setDocumentType(rsTemp.getString("DocumentType"));
                stockTransfer_Mdl.setDocumentNumber(rsTemp.getString("DocumentNumber"));
                stockTransfer_Mdl.setBusinessUnit(rsTemp.getString("businessUnit"));
                stockTransfer_Mdl.setLegalEntity(rsTemp.getString("LegalEntity"));

                // ===== LINE QUERY FOR THIS HEADER'S docId =====
                List<StockTransferLine_Mdl> lineList = new ArrayList<>();
                lineQuery = "select shd.v_SNo as LineNumber, \n"
                        + "dptFrm.D365_code as D365FromDepartmentCode,\n"
                        + "dptFrm.name as D365FromDepartmentName,\n"
                        + "dptTo.D365_code as D365ToDepartmentCode,\n"
                        + "dptTo.name as D365ToDepartmentName ,\n"
                        + "im.D365_code as D365ItemCode ,im.name as itemName ,\n"
                        + "itmt.name as itemType, ib.batchNo, shd.quantity, \n"
                        + "unt.D365_code as D365UnitCode, unt.unit as unitName\n"
                        + "from stockHeadDetail shd\n"
                        + "left join subGroup as dptFrm on dptFrm.subCode=shd.departmentCode\n"
                        + "left join subGroup as dptTo on dptTo.subCode=shd.departmentToCode\n"
                        + "left join itemMast im on im.code=shd.itemCode\n"
                        + "left join itemBatch ib on ib.code=shd.ibCode\n"
                        + "left join unitMast as unt on unt.code=im.sku\n"
                        + "left join itemType itmt on itmt.code=im.filterItemType\n"
                        + "where shd.docId = :DocumentNumber \n"
                        + "order by shd.v_SNo";

                psTempLine = DmPreparedStatement.prepareStatement(cnTemp, lineQuery);
                psTempLine.setString("DocumentNumber", rsTemp.getString("docId"));
                rsTempLine = psTempLine.executeQuery();
                while (rsTempLine.next()) {
                    StockTransferLine_Mdl line = new StockTransferLine_Mdl();
                    line.setLineNumber(rsTempLine.getString("LineNumber"));
                    line.setD365FromDepartmentCode(rsTempLine.getString("D365FromDepartmentCode"));
                    line.setD365FromDepartmentName(rsTempLine.getString("D365FromDepartmentName"));
                    line.setD365ToDepartmentCode(rsTempLine.getString("D365ToDepartmentCode"));
                    line.setD365ToDepartmentName(rsTempLine.getString("D365ToDepartmentName"));
                    line.setD365ItemCode(rsTempLine.getString("D365ItemCode"));
                    line.setItemName(rsTempLine.getString("itemName"));
                    line.setItemType(rsTempLine.getString("itemType"));
                    line.setBatchNo(rsTempLine.getString("batchNo"));
                    line.setQuantity(rsTempLine.getString("quantity"));
                    line.setD365UnitCode(rsTempLine.getString("D365UnitCode"));
                    line.setUnitName(rsTempLine.getString("unitName"));
                    lineList.add(line);
                }
                rsTempLine.close();
                psTempLine.close();

                stockTransfer_Mdl.setLineDetails(lineList);
                headerList.add(stockTransfer_Mdl);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (rsTempLine != null) {
                    rsTempLine.close();
                }
                if (psTempLine != null) {
                    psTempLine.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return headerList;
    }

    public static List<StockTransfer_Mdl> getStockcomsumptionData(Connection cnTemp, String postingFromDate) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTempLine = null;
        DmPreparedStatement psTempLine = null;
        String sqlQuery = "", lineQuery = "";
        List<StockTransfer_Mdl> headerList = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);

            // ===== HEADER QUERY =====
            sqlQuery = "select  max(vt.parentVoucher) as EntryType ,\n"
                    + "CONCAT(CAST(sh.docId AS VARCHAR(50)),\n"
                    + "max(sm.BusinessUnit)) as EntryNumber,sh.docId as shdocId,max(sh.v_Date) as EntryDate\n"
                    + ",max(vt.description) as DocumentType,max(sh.recId) as DocumentNumber,\n"
                    + "max(sm.businessUnit) businessUnit ,\n"
                    + "max(sm.LegalEntity) LegalEntity \n"
                    + " \n"
                    + "from stockhead sh\n"
                    + "left join voucher_Type vt on vt.v_Type=sh.v_Type\n"
                    + "left join siteMast sm on sm.code=sh.site_Code\n"
                    + "left join stockHeadDetail shd on shd.docId=sh.docId\n"
                    + "\n"
                    + "where  vt.parentVoucher in ('Stock Transfer')\n"
                    + "and shd.departmentToCode is null \n"
                    + "and sh.v_date = :vDate and  isnull(sh.isSyncToD365,0)=0\n"
                    + " group by  sh.docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vDate", dateValue);
            rsTemp = psTemp.executeQuery();

            // ===== ONE HEADER ROW AT A TIME, WITH ITS OWN LINE FETCH =====
            while (rsTemp.next()) {
                StockTransfer_Mdl stockTransfer_Mdl = new StockTransfer_Mdl();
                stockTransfer_Mdl.setDocId(rsTemp.getString("shdocId"));
                stockTransfer_Mdl.setEntryType(rsTemp.getString("EntryType"));
                stockTransfer_Mdl.setEntryNumber(rsTemp.getString("EntryNumber"));
                stockTransfer_Mdl.setEntryDate(DmShared.dNull(rsTemp.getDate("EntryDate")));
                stockTransfer_Mdl.setDocumentType(rsTemp.getString("DocumentType"));
                stockTransfer_Mdl.setDocumentNumber(rsTemp.getString("DocumentNumber"));
                stockTransfer_Mdl.setBusinessUnit(rsTemp.getString("businessUnit"));
                stockTransfer_Mdl.setLegalEntity(rsTemp.getString("LegalEntity"));

                // ===== LINE QUERY FOR THIS HEADER'S docId =====
                List<StockTransferLine_Mdl> lineList = new ArrayList<>();
                lineQuery = "select  vt.parentVoucher as EntryType ,CONCAT(CAST(sh.docId AS VARCHAR(50)), sm.BusinessUnit) as EntryNumber,sh.v_Date as EntryDate\n"
                        + ",vt.description as DocumentType,sh.recId as DocumentNumber,\n"
                        + "sm.businessUnit ,sm.LegalEntity ,shd.v_SNo as LineNumber , \n"
                        + "dptFrm.D365_code as D365FromDepartmentCode,dptFrm.name as D365FromDepartmentName,\n"
                        + "dptTo.D365_code as D365ToDepartmentCode,dptTo.name as D365ToDepartmentName ,\n"
                        + "im.D365_code as D365ItemCode ,im.name as itemName ,itmt.name as itemType,ib.batchNo,shd.quantity, \n"
                        + "unt.D365_code as D365UnitCode,unt.unit as unitName\n"
                        + "\n"
                        + "from stockhead sh\n"
                        + "left join voucher_Type vt on vt.v_Type=sh.v_Type\n"
                        + "left join siteMast sm on sm.code=sh.site_Code\n"
                        + "left join stockHeadDetail shd on shd.docId=sh.docId\n"
                        + "left join subGroup as dptFrm on dptFrm.subCode=shd.departmentCode\n"
                        + "left join subGroup as dptTo on dptTo.subCode=shd.departmentToCode\n"
                        + "left join itemMast im on im.code=shd.itemCode\n"
                        + "left join itemBatch ib on ib.code=shd.ibCode\n"
                        + "left join unitMast as unt on unt.code=im.sku\n"
                        + "left join itemType itmt on itmt.code=im.filterItemType\n"
                        + "\n"
                        + "\n"
                        + "where  vt.parentVoucher in ('Stock Transfer') \n"
                        + "and shd.departmentToCode is null and \n"
                        + "shd.docId = :DocumentNumber \n"
                        + "order by shd.docId,shd.v_SNo";

                psTempLine = DmPreparedStatement.prepareStatement(cnTemp, lineQuery);
                psTempLine.setString("DocumentNumber", rsTemp.getString("shdocId"));
                rsTempLine = psTempLine.executeQuery();
                while (rsTempLine.next()) {
                    StockTransferLine_Mdl line = new StockTransferLine_Mdl();
                    line.setLineNumber(rsTempLine.getString("LineNumber"));
                    line.setD365FromDepartmentCode(rsTempLine.getString("D365FromDepartmentCode"));
                    line.setD365FromDepartmentName(rsTempLine.getString("D365FromDepartmentName"));
                    line.setD365ToDepartmentCode(rsTempLine.getString("D365ToDepartmentCode"));
                    line.setD365ToDepartmentName(rsTempLine.getString("D365ToDepartmentName"));
                    line.setD365ItemCode(rsTempLine.getString("D365ItemCode"));
                    line.setItemName(rsTempLine.getString("itemName"));
                    line.setItemType(rsTempLine.getString("itemType"));
                    line.setBatchNo(rsTempLine.getString("batchNo"));
                    line.setQuantity(rsTempLine.getString("quantity"));
                    line.setD365UnitCode(rsTempLine.getString("D365UnitCode"));
                    line.setUnitName(rsTempLine.getString("unitName"));
                    lineList.add(line);
                }
                rsTempLine.close();
                psTempLine.close();

                stockTransfer_Mdl.setLineDetails(lineList);
                headerList.add(stockTransfer_Mdl);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (rsTempLine != null) {
                    rsTempLine.close();
                }
                if (psTempLine != null) {
                    psTempLine.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return headerList;
    }

    public static void commanPostStockDataToD365(List<StockTransfer_Mdl> mrList, Connection cnTempStaging, Connection cnTemp, String callFrom) throws Exception {

        for (StockTransfer_Mdl mrTemp : mrList) {
            if (callFrom.equalsIgnoreCase("Transfer")) {
                postStockDataToD365(mrTemp, cnTempStaging, cnTemp, "His_stockTransferHeader", "His_stockTrasferLine");
            } else if (callFrom.equalsIgnoreCase("Consumption")) {
                postStockDataToD365(mrTemp, cnTempStaging, cnTemp, "His_stockConsumptionHeader", "His_stockConsumptionLine");
            }
        }
    }

    public static void postStockDataToD365(StockTransfer_Mdl mrTemp, Connection cnTempStaging, Connection cnTemp, String headerTable, String lineTable) throws Exception {
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {

            // ===== HEADER INSERT =====
            sqlQuery = "INSERT INTO " + headerTable + " ("
                    + "EntryType, EntryNumber, v_Date, DocumentType, DocumentNumber, "
                    + "BusinessUnitCode, LegalEntity"
                    + ") VALUES ("
                    + ":EntryType, :EntryNumber, :v_Date, :DocumentType, :DocumentNumber, "
                    + ":BusinessUnitCode, :LegalEntity"
                    + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sqlQuery);
            psTemp.setString("EntryType", DmShared.xNull(mrTemp.getEntryType()));
            psTemp.setString("EntryNumber", DmShared.xNull(mrTemp.getEntryNumber()));
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getEntryDate()));
            psTemp.setString("DocumentType", DmShared.xNull(mrTemp.getDocumentType()));
            psTemp.setString("DocumentNumber", DmShared.xNull(mrTemp.getDocumentNumber()));
            psTemp.setString("BusinessUnitCode", DmShared.xNull(mrTemp.getBusinessUnit()));
            psTemp.setString("LegalEntity", DmShared.xNull(mrTemp.getLegalEntity()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "UPDATE  shd SET \n"
                    + "shd.isSyncToD365 = 1 \n"
                    + "from stockhead shd \n"
                    + " WHERE  docid = :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", DmShared.xNull(mrTemp.getDocId()));
            psTemp.executeUpdate();
            psTemp.close();

            // ===== CLEAR EXISTING LINES (table has no docId, key off EntryNumber) =====
            sqlQuery = "DELETE FROM " + lineTable + " WHERE EntryNumber = :EntryNumber  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sqlQuery);
            psTemp.setString("EntryNumber", DmShared.xNull(mrTemp.getEntryNumber()));
            psTemp.executeUpdate();
            psTemp.close();

            // ===== LINE INSERT (flat - header values repeated on every row) =====
            for (int i = 0; i < mrTemp.getLineDetails().size(); i++) {
//                if (!DmShared.xNull(mrTemp.getLineDetails().get(i).getD365ItemCode()).equalsIgnoreCase("")) {
                StockTransferLine_Mdl detail = mrTemp.getLineDetails().get(i);

                sqlQuery = "INSERT INTO " + lineTable + " ("
                        + "EntryType, EntryNumber, EntryDate, DocumentType, DocumentNumber, businessUnit, LegalEntity, "
                        + "LineNumber, D365FromDepartmentCode, D365FromDepartmentName, D365ToDepartmentCode, D365ToDepartmentName, "
                        + "D365ItemCode, itemName, itemType, batchNo, quantity, D365UnitCode, unitName"
                        + ") VALUES ("
                        + ":EntryType, :EntryNumber, :EntryDate, :DocumentType, :DocumentNumber, :businessUnit, :LegalEntity, "
                        + ":LineNumber, :D365FromDepartmentCode, :D365FromDepartmentName, :D365ToDepartmentCode, :D365ToDepartmentName, "
                        + ":D365ItemCode, :itemName, :itemType, :batchNo, :quantity, :D365UnitCode, :unitName"
                        + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sqlQuery);
                // ---- header values, repeated on every line row ----
                psTemp.setString("EntryType", DmShared.xNull(mrTemp.getEntryType()));
                psTemp.setString("EntryNumber", DmShared.xNull(mrTemp.getEntryNumber()));
                psTemp.setDate("EntryDate", DmServer.convertDate(mrTemp.getEntryDate()));
                psTemp.setString("DocumentType", DmShared.xNull(mrTemp.getDocumentType()));
                psTemp.setString("DocumentNumber", DmShared.xNull(mrTemp.getDocumentNumber()));
                psTemp.setString("businessUnit", DmShared.xNull(mrTemp.getBusinessUnit()));
                psTemp.setString("LegalEntity", DmShared.xNull(mrTemp.getLegalEntity()));
                // ---- line values ----
                psTemp.setInt("LineNumber", DmShared.iNull(detail.getLineNumber()).intValue());
                psTemp.setString("D365FromDepartmentCode", DmShared.xNull(detail.getD365FromDepartmentCode()));
                psTemp.setString("D365FromDepartmentName", DmShared.xNull(detail.getD365FromDepartmentName()));
                psTemp.setString("D365ToDepartmentCode", DmShared.xNull(detail.getD365ToDepartmentCode()));
                psTemp.setString("D365ToDepartmentName", DmShared.xNull(detail.getD365ToDepartmentName()));
                psTemp.setString("D365ItemCode", DmShared.xNull(detail.getD365ItemCode()));
                psTemp.setString("itemName", DmShared.xNull(detail.getItemName()));
                psTemp.setString("itemType", DmShared.xNull(detail.getItemType()));
                psTemp.setString("batchNo", DmShared.xNull(detail.getBatchNo()));

                // quantity column is FLOAT - parse safely and bind as a double
                // (DmShared.dNull() is the Date-null helper, not numeric - it doesn't fit a String quantity)
                double qtyVal = 0d;
                String qtyStr = detail.getQuantity();
                if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                    try {
                        qtyVal = Double.parseDouble(qtyStr.trim());
                    } catch (NumberFormatException nfe) {
                        qtyVal = 0d;
                    }
                }
                psTemp.setDouble("quantity", qtyVal);

                psTemp.setString("D365UnitCode", DmShared.xNull(detail.getD365UnitCode()));
                psTemp.setString("unitName", DmShared.xNull(detail.getUnitName()));
                psTemp.executeUpdate();
                psTemp.close();

//                }
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
    }

    public static void postPharmacySaleBillDataToD365(List<PharmacySaleBillHeader_Mdl> mrList, Connection cnTempStaging, Connection cnTemp) throws Exception {

        for (PharmacySaleBillHeader_Mdl mrTemp : mrList) {
            postPharmacySaleBillDataToD365(mrTemp, cnTempStaging, cnTemp);
        }
    }

    public static void postPharmacySaleBillDataToD365(PharmacySaleBillHeader_Mdl mrTemp, Connection cnTempStaging, Connection cnTemp) throws Exception {
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {

            // ===== HEADER INSERT =====
            sqlQuery = "INSERT INTO His_PharmacySaleBillHeader ("
                    + "EntryType, EntryNumber, DocumentType, DocumentNumber, "
                    + "D365DepartmentCode, DepartmentName, LocationCode, LocationName, "
                    + "BusinessUnit, LegalEntity, ConsultantName, Speciality, "
                    + "PatientUHID, patientName , patientMobile ,  BillNo, BillDate , billTime,oh_Amt_Gross,oh_Amt_Discount,oh_Amt_Taxable,oh_Amt_CGST,oh_Amt_SGST,oh_Amt_UGST,\n"
                    + " oh_Amt_IGST,oh_Amt_RoundOff,oh_Amt_Net,preparedBy "
                    + " ) VALUES ("
                    + ":EntryType, :EntryNumber, :DocumentType, :DocumentNumber, "
                    + ":D365DepartmentCode, :DepartmentName, :LocationCode, :LocationName, "
                    + ":BusinessUnit, :LegalEntity, :ConsultantName, :Speciality, "
                    + ":PatientUHID, :patientName , :patientMobile ,  :BillNo, :BillDate  , :billTime , :oh_Amt_Gross , :oh_Amt_Discount , :oh_Amt_Taxable , :oh_Amt_CGST , :oh_Amt_SGST , :oh_Amt_UGST ,\n"
                    + " :oh_Amt_IGST ,:oh_Amt_RoundOff ,:oh_Amt_Net ,:preparedBy "
                    + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sqlQuery);
            psTemp.setString("EntryType", DmShared.xNull(mrTemp.getEntryType()));
            psTemp.setString("EntryNumber", DmShared.xNull(mrTemp.getEntryNumber()));
            psTemp.setString("DocumentType", DmShared.xNull(mrTemp.getDocumentType()));
            psTemp.setString("DocumentNumber", DmShared.xNull(mrTemp.getDocumentNumber()));
            psTemp.setString("D365DepartmentCode", DmShared.xNull(mrTemp.getD365DepartmentCode()));
            psTemp.setString("DepartmentName", DmShared.xNull(mrTemp.getDepartmentName()));
            psTemp.setString("LocationCode", DmShared.xNull(mrTemp.getLocationCode()));
            psTemp.setString("LocationName", DmShared.xNull(mrTemp.getLocationName()));
            psTemp.setString("BusinessUnit", DmShared.xNull(mrTemp.getBusinessUnit()));
            psTemp.setString("LegalEntity", DmShared.xNull(mrTemp.getLegalEntity()));
            psTemp.setString("ConsultantName", DmShared.xNull(mrTemp.getConsultantName()));
            psTemp.setString("Speciality", DmShared.xNull(mrTemp.getSpeciality()));
            psTemp.setString("PatientUHID", DmShared.xNull(mrTemp.getPatientUHID()));
            psTemp.setString("patientName", DmShared.xNull(mrTemp.getPatientName()));
            psTemp.setString("patientMobile", DmShared.xNull(mrTemp.getPatientMobile()));
            psTemp.setString("BillNo", DmShared.xNull(mrTemp.getBillNo()));
            psTemp.setDate("BillDate", DmServer.convertDate(mrTemp.getBillDate()));

            psTemp.setString("billTime", DmShared.xNull(mrTemp.getBillTime()));
            psTemp.setString("oh_Amt_Gross", DmShared.xNull(mrTemp.getOh_Amt_Gross()));
            psTemp.setString("oh_Amt_Discount", DmShared.xNull(mrTemp.getOh_Amt_Discount()));
            psTemp.setString("oh_Amt_Taxable", DmShared.xNull(mrTemp.getOh_Amt_Taxable()));
            psTemp.setString("oh_Amt_CGST", DmShared.xNull(mrTemp.getOh_Amt_CGST()));
            psTemp.setString("oh_Amt_SGST", DmShared.xNull(mrTemp.getOh_Amt_SGST()));
            psTemp.setString("oh_Amt_UGST", DmShared.xNull(mrTemp.getOh_Amt_UGST()));
            psTemp.setString("oh_Amt_IGST", DmShared.xNull(mrTemp.getOh_Amt_IGST()));
            psTemp.setString("oh_Amt_RoundOff", DmShared.xNull(mrTemp.getOh_Amt_RoundOff()));
            psTemp.setString("oh_Amt_Net", DmShared.xNull(mrTemp.getOh_Amt_Net()));
            psTemp.setString("preparedBy", DmShared.xNull(mrTemp.getPreparedBy()));

            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "UPDATE  shd SET \n"
                    + "shd.isSyncToD365 = 1 \n"
                    + "from salebill shd \n"
                    + " WHERE  docid = :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", DmShared.xNull(mrTemp.getDocId()));
            psTemp.executeUpdate();
            psTemp.close();

            // ===== CLEAR EXISTING LINES (table has no docId, key off EntryNumber) =====
            sqlQuery = "DELETE FROM His_PharmacySaleBillDetail WHERE EntryNumber = :EntryNumber  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sqlQuery);
            psTemp.setString("EntryNumber", DmShared.xNull(mrTemp.getEntryNumber()));
            psTemp.executeUpdate();
            psTemp.close();

            // ===== LINE INSERT (flat - header values repeated on every row) =====
            for (int i = 0; i < mrTemp.getLineDetails().size(); i++) {
//                if (!DmShared.xNull(mrTemp.getLineDetails().get(i).getD365ItemCode()).equalsIgnoreCase("")) {
                PharmacySaleBillDetail_Mdl detail = mrTemp.getLineDetails().get(i);
                sqlQuery = "INSERT INTO His_PharmacySaleBillDetail  ("
                        + "EntryType, EntryNumber,lineNumber, DocumentType, DocumentNumber, "
                        + "D365DepartmentCode, DepartmentName, LocationCode, LocationName, BusinessUnit, "
                        + "ConsultantName, Speciality, PatientUHID,  "
                        + "D365ItemCode, ItemName, BatchNo, Quantity, D365UnitCode, UnitName, "
                        + "BillNo, BillDate  , oh_Amt_Gross,oh_Amt_Discount,oh_Amt_Taxable,oh_Amt_CGST,oh_Amt_SGST,oh_Amt_UGST,oh_Amt_IGST,oh_Amt_Net,billTime,preparedBy "
                        + " ) VALUES ("
                        + ":EntryType, :EntryNumber,:lineNumber, :DocumentType, :DocumentNumber, "
                        + ":D365DepartmentCode, :DepartmentName, :LocationCode, :LocationName, :BusinessUnit, "
                        + ":ConsultantName, :Speciality, :PatientUHID,  "
                        + ":D365ItemCode, :ItemName, :BatchNo, :Quantity, :D365UnitCode, :UnitName, "
                        + ":BillNo, :BillDate ,:oh_Amt_Gross,:oh_Amt_Discount,:oh_Amt_Taxable,:oh_Amt_CGST,:oh_Amt_SGST,:oh_Amt_UGST,:oh_Amt_IGST,:oh_Amt_Net , :billTime , :preparedBy "
                        + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sqlQuery);
                // ---- header values, repeated on every line row ----
                psTemp.setString("EntryType", DmShared.xNull(mrTemp.getEntryType()));
                psTemp.setString("EntryNumber", DmShared.xNull(mrTemp.getEntryNumber()));
                psTemp.setString("lineNumber", DmShared.xNull(detail.getLineNumber()));
                psTemp.setString("DocumentType", DmShared.xNull(mrTemp.getDocumentType()));
                psTemp.setString("DocumentNumber", DmShared.xNull(mrTemp.getDocumentNumber()));
                psTemp.setString("D365DepartmentCode", DmShared.xNull(mrTemp.getD365DepartmentCode()));
                psTemp.setString("DepartmentName", DmShared.xNull(mrTemp.getDepartmentName()));
                psTemp.setString("LocationCode", DmShared.xNull(mrTemp.getLocationCode()));
                psTemp.setString("LocationName", DmShared.xNull(mrTemp.getLocationName()));
                psTemp.setString("BusinessUnit", DmShared.xNull(mrTemp.getBusinessUnit()));
                psTemp.setString("ConsultantName", DmShared.xNull(mrTemp.getConsultantName()));
                psTemp.setString("Speciality", DmShared.xNull(mrTemp.getSpeciality()));
                psTemp.setString("PatientUHID", DmShared.xNull(mrTemp.getPatientUHID()));

                // ---- line values ----
                psTemp.setString("D365ItemCode", DmShared.xNull(detail.getD365ItemCode()));
                psTemp.setString("ItemName", DmShared.xNull(detail.getItemName()));
                psTemp.setString("BatchNo", DmShared.xNull(detail.getBatchNo()));

                psTemp.setString("oh_Amt_Gross", DmShared.xNull(detail.getOh_Amt_Gross()));
                psTemp.setString("oh_Amt_Discount", DmShared.xNull(detail.getOh_Amt_Discount()));
                psTemp.setString("oh_Amt_Taxable", DmShared.xNull(detail.getOh_Amt_Taxable()));
                psTemp.setString("oh_Amt_CGST", DmShared.xNull(detail.getOh_Amt_CGST()));
                psTemp.setString("oh_Amt_SGST", DmShared.xNull(detail.getOh_Amt_SGST()));
                psTemp.setString("oh_Amt_UGST", DmShared.xNull(detail.getOh_Amt_UGST()));
                psTemp.setString("oh_Amt_IGST", DmShared.xNull(detail.getOh_Amt_IGST()));
                psTemp.setString("oh_Amt_Net", DmShared.xNull(detail.getOh_Amt_Net()));

                psTemp.setString("billTime", DmShared.xNull(mrTemp.getBillTime()));
                psTemp.setString("preparedBy", DmShared.xNull(mrTemp.getPreparedBy()));

                // Quantity column is DECIMAL(18,4) - parse safely and bind as a double
                // (DmShared.dNull() is the Date-null helper, not numeric - it doesn't fit a String quantity)
                double qtyVal = 0d;
                String qtyStr = detail.getQuantity();
                if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                    try {
                        qtyVal = Double.parseDouble(qtyStr.trim());
                    } catch (NumberFormatException nfe) {
                        qtyVal = 0d;
                    }
                }
                psTemp.setDouble("Quantity", qtyVal);

                psTemp.setString("D365UnitCode", DmShared.xNull(detail.getD365UnitCode()));
                psTemp.setString("UnitName", DmShared.xNull(detail.getUnitName()));
                psTemp.setString("BillNo", DmShared.xNull(mrTemp.getBillNo()));
                psTemp.setDate("BillDate", DmServer.convertDate(mrTemp.getBillDate()));
                psTemp.executeUpdate();
                psTemp.close();
//                }
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
    }

    public static List<TpaSettlementReceipt_Mdl> getTpaSettlementReceipt(Connection cnTemp, String postingFromDate) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        List<TpaSettlementReceipt_Mdl> list = new ArrayList<>();
        try {
            String[] postingDate = DmServer.getServerDateTime();
            String dateValue = postingFromDate.equals("")
                    ? DmShared.dNull(postingDate[0])
                    : DmShared.dNull(postingFromDate);
            sqlQuery = "select * from D365_TPABillSettlement_Receipt"
                    + " where dmstatus is null"
                    + " and D365_ReceiptDate = :D365_ReceiptDate";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vDate", dateValue);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                TpaSettlementReceipt_Mdl mdl = new TpaSettlementReceipt_Mdl();

                mdl.setBillNo(DmShared.xNull(rsTemp.getString("billNo")));
                mdl.setBillDate(DmShared.xNull(rsTemp.getString("billDate")));
                mdl.setBillType(DmShared.xNull(rsTemp.getString("billType")));
                mdl.setBillAmount(DmShared.xNull(rsTemp.getString("billAmount")));
                mdl.setBusinessUnit(DmShared.xNull(rsTemp.getString("BusinessUnit")));

                mdl.setD365_ReceiptNo(DmShared.xNull(rsTemp.getString("D365_ReceiptNo")));
                mdl.setD365_ReceiptDate(DmShared.xNull(rsTemp.getString("D365_ReceiptDate")));
                mdl.setAmount(DmShared.xNull(rsTemp.getString("Amount")));
                mdl.setInstrumentType(DmShared.xNull(rsTemp.getString("instrumentType")));
                mdl.setIntrumentNo(DmShared.xNull(rsTemp.getString("intrumentNo")));

                mdl.setD365_BankOrCashAcCode(DmShared.xNull(rsTemp.getString("D365_BankOrCashAcCode")));
                list.add(mdl);
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
        return list;
    }

    public static void TpaBillSettlement(List<TpaSettlementReceipt_Mdl> mrList, Connection cnTempStaging, Connection cnTemp, String postingFromDate) throws Exception {
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {

            for (TpaSettlementReceipt_Mdl mrTemp : mrList) {
                sqlQuery = "select  psd.regdocid,max(sb.site_Code) as siteCode,\n"
                        + "sum((psd.quantity*psd.rate)-isNull(psd.otherDiscount,0)-isNull(psd.Discount,0)) as billAmt \n"
                        + "from saleBill sb  \n"
                        + "left join patientServiceDetail psd on psd.sbDocId=sb.docId \n"
                        + "where sb.recId=:recId \n"
                        + "group by psd.regDocId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("recId", mrTemp.getBillNo());
                rsTemp = psTemp.executeQuery();
                List<Bill> bills = new ArrayList<>();
                while (rsTemp.next()) {
                    Bill b = new Bill();
                    b.setRegDocid(DmShared.xNull(rsTemp.getString("regdocid")));
                    b.setBillAmount(rsTemp.getBigDecimal("billAmt"));
                    bills.clear();
                    bills.add(b);
                }
                settleReceipt(BigDecimal.valueOf(DmShared.vNull(mrTemp.getAmount()).doubleValue()), bills, cnTemp, mrTemp);

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
    }

    public static void settleReceipt(BigDecimal totalReceiptAmount, List<Bill> bills, Connection cnTemp, TpaSettlementReceipt_Mdl mrTemp) throws Exception {

        try {
            List<Settlement> settlements = new ArrayList<>();

            BigDecimal remainingReceipt = totalReceiptAmount;

            for (Bill bill : bills) {

                if (remainingReceipt.compareTo(BigDecimal.ZERO) <= 0) {
//                settlements.add(
//                    new Settlement(
//                        bill.getRegDocid(),
//                        bill.getBillAmount(),
//                        BigDecimal.ZERO
//                    )
//                );
                    continue;
                }

                BigDecimal settlementAmount;

                // If receipt amount is greater than or equal to bill amount
                if (remainingReceipt.compareTo(bill.getBillAmount()) >= 0) {
                    settlementAmount = bill.getBillAmount();
                } else {
                    // Partial settlement
                    settlementAmount = remainingReceipt;
                }
//                        settlements.add(
//                            new Settlement(
//                                bill.getRegDocid(),
//                                bill.getBillAmount(),
//                                settlementAmount
//                            )
//            );

                remainingReceipt
                        = remainingReceipt.subtract(settlementAmount);
            }

            System.out.println("---------------------------------------");
            System.out.println("Receipt Amount : " + totalReceiptAmount);
            System.out.println("Unallocated    : " + remainingReceipt);
            System.out.println("---------------------------------------");
//            insertTpaSettlementReceipt(cnTemp, , "");
        } catch (Exception e) {
            throw e;
        } finally {
            try {

            } catch (Exception e) {
            }
        }

    }

    public static void insertTpaSettlementReceipt(Connection cnTemp, TpaSettlementReceipt_Mdl mrTemp, String siteCode) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull("172"), mrTemp.getVoucherPrefix(), siteCode, cnTemp));
                mrTemp.setRecIdReceipt(DmServer.getRecId("patientReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdReceipt(), mrTemp.getD365_ReceiptDate(), "", "172", mrTemp.getVoucherPrefix(), siteCode, "", "", cnTemp, true, null));
            }
            sqlQuery = "INSERT INTO patientReceipt(DocId,v_Type,recId,v_No,v_Date,v_Prefix,site_Code,preparedBy,u_EntDt,regDocId,amount,remark,recIdPrefix ,v_Time) "
                    + " VALUES (:docId,:v_Type,:recID,:v_No,:v_Date,:v_Prefix,:siteCode,:preparedBy, :u_EntDt,:regDocId,:amount,:remark,:recIdPrefix , :v_Time) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            psTemp.setString("v_Type", mrTemp.getCardPayment().getReceiptTypeCode());
//            psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdReceipt().getRecIdValue()));
//            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdReceipt().getPrefix()));
//            psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdReceipt().getCounter()).toString());
//            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtVoucher()));
//            psTemp.setString("v_Prefix", voucherPrefix);
//            psTemp.setString("siteCode", siteCode);
//            psTemp.setString("preparedBy", userName);
//            psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getPatientDetail().getDocIdIPD()));
//            psTemp.setString("amount", mrTemp.getAmount());
//            psTemp.setString("remark", mrTemp.getRemark());
//            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
//            mrTemp.setSharedPreparedBy(userName);

            psTemp.setString("v_Time", DmServer.vNull(DmServer.getServerDateTime()[1], "0.00"));

            psTemp.executeUpdate();
            psTemp.close();

            psTemp.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void insertPharmacySale(Connection cnTempStaging, Connection cnTemp, String billDate, String aarogyaDB) throws Exception {
        insertPharmacySaleBillHeader(cnTempStaging, cnTemp, billDate, aarogyaDB);
        insertPharmacySaleBillDetail(cnTempStaging, cnTemp, billDate, aarogyaDB);
    }

    public static void insertPharmacySaleBillHeader(Connection cnTempStaging, Connection cnTemp, String billDate, String aarogyaDB) throws Exception {
        DmPreparedStatement psTemp = null;
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        String pharmacyVCat = "'" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_WHOLESALE_CREDIT + "','" + pvc.WHOLE_SALE_RETURN_CREDIT + "'";

        try {
            String sql = "INSERT INTO His_PharmacySaleBillHeader "
                    + "(EntryType, EntryNumber, DocumentType, DocumentNumber, D365DepartmentCode, DepartmentName, "
                    + "LocationCode, LocationName, businessUnit, LegalEntity, consultantName, speciality, "
                    + "patientUHID, patientName, patientMobile, billNo, billDate, billTime, "
                    + "oh_Amt_Gross, oh_Amt_Discount, oh_Amt_Taxable, oh_Amt_CGST, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_IGST, "
                    + "oh_Amt_RoundOff, oh_Amt_Net, preparedBy) "
                    + "SELECT "
                    + "vt.parentVoucher as EntryType, "
                    + "CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit) as EntryNumber, "
                    + "vt.description as DocumentType, "
                    + "sb.recId as DocumentNumber, "
                    + "dpt.D365_code as D365DepartmentCode, "
                    + "dpt.name as DepartmentName, "
                    + "dpt.D365_code as LocationCode, "
                    + "dpt.name as LocationName, "
                    + "sm.businessUnit, "
                    + "sm.LegalEntity, "
                    + "cons.name as consultantName, "
                    + "speci.name as speciality, "
                    + "lm.manualCode as patientUHID, "
                    + "lm.displayName as patientName, "
                    + "lm.mobile as patientMobile, "
                    + "sb.recId as billNo, "
                    + "sb.v_Date as billDate, "
                    + "sb.v_Time as billTime, "
                    + "sb.oh_Amt_Gross, "
                    + "sb.oh_Amt_Discount, "
                    + "sb.oh_Amt_Taxable, "
                    + "sb.oh_Amt_CGST, "
                    + "sb.oh_Amt_SGST, "
                    + "sb.oh_Amt_UGST, "
                    + "isnull(sb.oh_Amt_IGST,0) oh_Amt_IGST, "
                    + "sb.oh_Amt_RoundOff, "
                    + "sb.oh_Amt_Net, "
                    + "sb.preparedBy "
                    + "FROM " + aarogyaDB + ".dbo.saleBill sb "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.ledgerMast lm on lm.code=sb.lmCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.voucher_Type vt on vt.v_Type=sb.v_Type "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.siteMast sm on sm.code=sb.site_Code "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.subGroup as dpt on dpt.subCode=sb.departmentCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.subGroup as cons on cons.subCode=sb.consultantCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.consultantMast as cm on cm.subCode=sb.consultantCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.consultantSpeciMast as speci on speci.code=cm.consultantSpeciCode "
                    + " WHERE vt.category in (" + pharmacyVCat + ") "
                    //                    + "AND vt.description <> 'Whole Sale Bill Credit OP' "
                    + "AND sb.v_date = :billDate "
                    + "AND isnull(sb.isSyncToD365,0)=0";

            psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sql);
            psTemp.setString("billDate", billDate);
            psTemp.executeUpdate();
            psTemp.close();

            sql = "UPDATE  sb SET \n"
                    + "sb.isSyncToD365 = 1 \n"
                    + "from salebill sb \n"
                    + "LEFT JOIN voucher_Type vt on vt.v_Type=sb.v_Type "
                    + "WHERE vt.parentVoucher in ('sale','sale Return') "
                    + "AND vt.description <> 'Whole Sale Bill Credit OP' "
                    + "AND sb.v_date = :billDate "
                    + "AND isnull(sb.isSyncToD365,0)=0";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("billDate", billDate);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void insertPharmacySaleBillDetail(Connection cnTempStaging, Connection cnTemp, String billDate, String aarogyaDB) throws Exception {
        DmPreparedStatement psTemp = null;
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        String pharmacyVCat = "'" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_WHOLESALE_CREDIT + "','" + pvc.WHOLE_SALE_RETURN_CREDIT + "'";

        try {
            String sql = "INSERT INTO His_PharmacySaleBillDetail "
                    + "(EntryType, EntryNumber, lineNumber, DocumentType, DocumentNumber, D365DepartmentCode, DepartmentName, "
                    + "LocationCode, LocationName, businessUnit, consultantName, speciality, patientUHID, "
                    + "D365ItemCode, ItemName, batchNo, quantity, D365UnitCode, unitName, "
                    + "billNo, billDate, billTime, "
                    + "oh_Amt_Gross, oh_Amt_Discount, oh_Amt_Taxable, oh_Amt_CGST, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_IGST, "
                    + "oh_Amt_Net, preparedBy) "
                    + "SELECT "
                    + "vt.parentVoucher as EntryType, "
                    + "CONCAT(CAST(sb.docId AS VARCHAR(50)), sm.BusinessUnit, CAST(sbd.v_SNo AS VARCHAR(10))) as EntryNumber, "
                    + "sbd.v_SNo as lineNumber, "
                    + "vt.description as DocumentType, "
                    + "sb.recId as DocumentNumber, "
                    + "dpt.D365_code as D365DepartmentCode, "
                    + "dpt.name as DepartmentName, "
                    + "dpt.D365_code as LocationCode, "
                    + "dpt.name as LocationName, "
                    + "sm.businessUnit, "
                    + "cons.name as consultantName, "
                    + "speci.name as speciality, "
                    + "lm.manualCode as patientUHID, "
                    + "im.D365_code as D365ItemCode, "
                    + "im.name as ItemName, "
                    + "ib.batchNo, "
                    + "case when vt.parentVoucher = 'sale' then abs(sbd.quantity) "
                    + "     when vt.parentVoucher = 'sale return' then -abs(sbd.quantity) "
                    + "end as quantity, "
                    + "unt.D365_code as D365UnitCode, "
                    + "unt.unit as unitName, "
                    + "sb.recId as billNo, "
                    + "sb.v_Date as billDate, "
                    + "sb.v_Time as billTime, "
                    + "sbd.oh_Amt_Gross, "
                    + "sbd.oh_Amt_Discount, "
                    + "sbd.oh_Amt_Taxable, "
                    + "sbd.oh_Amt_CGST, "
                    + "sbd.oh_Amt_SGST, "
                    + "sbd.oh_Amt_UGST, "
                    + "isnull(sbd.oh_Amt_IGST,0) oh_Amt_IGST, "
                    + "sbd.oh_Amt_Net, "
                    + "sb.preparedBy "
                    + "FROM " + aarogyaDB + ".dbo.saleBillDetail sbd "
                    + "LEFT join " + aarogyaDB + ".dbo.saleBill sb on sb.docid=sbd.docid "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.ledgerMast lm on lm.code=sb.lmCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.voucher_Type vt on vt.v_Type=sb.v_Type "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.siteMast sm on sm.code=sb.site_Code "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.subGroup as dpt on dpt.subCode=sb.departmentCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.unitMast as unt on unt.code=sbd.saleUnitCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.subGroup as cons on cons.subCode=sb.consultantCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.consultantMast as cm on cm.subCode=sb.consultantCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.consultantSpeciMast as speci on speci.code=cm.consultantSpeciCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.itemMast as im on im.code=sbd.itemCode "
                    + "LEFT JOIN " + aarogyaDB + ".dbo.itemBatch ib on ib.code=sbd.ibCode "
                    //                    + " WHERE vt.parentVoucher in ('sale','sale Return') "
                    + " WHERE vt.category in (" + pharmacyVCat + ") "
                    //                    + "AND vt.description <> 'Whole Sale Bill Credit OP' "
                    + "AND sb.v_date = :billDate "
                    + "AND isnull(sb.isSyncToD365,0)=0 "
                    + "AND sbd.isFooter=0";

            psTemp = DmPreparedStatement.prepareStatement(cnTempStaging, sql);
            psTemp.setString("billDate", billDate);
            psTemp.executeUpdate();
            psTemp.close();

//            sql = "UPDATE  sbd SET \n"
//                    + "sbd.isSyncToD365 = 1 \n"
//                    + "from saleBillDetail sbd \n"
//                    + "LEFT join saleBill sb on sb.docid=sbd.docid "
//                    + "LEFT JOIN voucher_Type vt on vt.v_Type=sb.v_Type "
//                    + "WHERE vt.parentVoucher in ('sale','sale Return') "
//                    + "AND vt.description <> 'Whole Sale Bill Credit OP' "
//                    + "AND sb.v_date = :billDate "
//                    + "AND isnull(sb.isSyncToD365,0)=0";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
//            psTemp.setString("billDate", billDate);
//            psTemp.executeUpdate();
//            psTemp.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }
}
