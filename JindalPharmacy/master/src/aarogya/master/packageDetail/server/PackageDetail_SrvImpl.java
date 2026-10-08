package aarogya.master.packageDetail.server;

import aarogya.master.packageDetail.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;

public class PackageDetail_SrvImpl extends DmRSSPrivate implements PackageDetail_Srv {

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*====================< Delete records from packageServiceDetail (Room Category Wise) >=====================*/
            sqlQuery = "Delete	psd "
                    + "		from packageDetail as pd "
                    + "		left join packageDetail as det on pd.packageCode = det.packageCode and pd.organizationCode = det.organizationCode "
                    + "                                     and pd.site_Code = det.site_Code and pd.fromDate = det.fromDate and pd.toDate = det.toDate and det.roomCategoryCode is not null "
                    + "		left join packageServiceDetail as psd on det.code = psd.code "
                    + "		where pd.code = :code and pd.roomCategoryCode is null ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================< Delete records from packageItemDetail (Room Category Wise) >=====================*/
            sqlQuery = "Delete	pid "
                    + "		from packageDetail as pd "
                    + "		left join packageDetail as det on pd.packageCode = det.packageCode and pd.organizationCode = det.organizationCode "
                    + "                                     and pd.site_Code = det.site_Code and pd.fromDate = det.fromDate and pd.toDate = det.toDate and det.roomCategoryCode is not null "
                    + "		left join packageItemDetail as pid on det.code = pid.code "
                    + "		where pd.code = :code and pd.roomCategoryCode is null ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================< Delete records from packageDetail (Room Category Wise) >=====================*/
            sqlQuery = "Delete	det "
                    + "		from packageDetail as pd "
                    + "		left join packageDetail as det on pd.packageCode = det.packageCode and pd.organizationCode = det.organizationCode "
                    + "                                     and pd.site_Code = det.site_Code and pd.fromDate = det.fromDate and pd.toDate = det.toDate and det.roomCategoryCode is not null "
                    + "		where pd.code = :code and pd.roomCategoryCode is null ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Delete records from packageServiceDetail >==============================*/
            sqlQuery = "Delete From packageServiceDetail Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Delete records from packageItemDetail >==============================*/
            sqlQuery = "Delete From packageItemDetail Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Delete records from packageDetail >==============================*/
            sqlQuery = "Delete From packageDetail Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResult<String> saveRecord(PackageDetail_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            PackageDetail_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setOrgCode(mrTemp.getOrgCode().replace(" ", "").trim());

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "packageDetail", cnTemp));
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==========================< Check whether name already exist or not >============================*/
            sqlQuery = "Select  pd.code "
                    + "         from packageDetail as pd "
                    + "         where pd.code <> :code and pd.packageCode = :packageCode and pd.organizationCode = :organizationCode "
                    + "         and pd.site_Code = :site_Code and pd.isActive = 1 and isNull(pd.roomCategoryCode,0) = :roomCategoryCode "
                    + "         and ((pd.fromDate <= :fromDate and pd.toDate >= :fromDate) or "
                    + "         (pd.fromDate <= :toDate and pd.toDate >= :toDate) or (pd.fromDate >= :fromDate and pd.fromDate <= :toDate)) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("packageCode", DmShared.iNull(mrTemp.getPackageCode()).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getOrganizationCode()).toString());
            psTemp.setString("roomCategoryCode", DmShared.iNull(mrTemp.getRoomCategoryCode()).toString());
            psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
            psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Package already exist with given from date and to date.");
            }
            rsTemp.close();
            psTemp.close();

            /*============================< Delete records from packageServiceDetail >==============================*/
            sqlQuery = "Delete From packageServiceDetail Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Delete records from packageItemDetail >==============================*/
            sqlQuery = "Delete From packageItemDetail Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Update organizationManualCode, patientScheduleCode, isActive, fromDate and toDate to packageDetail >====================================*/
            sqlQuery = "Update	det "
                    + "		set det.organizationManualCode = :organizationManualCode, det.isActive = :isActive, "
                    + "         det.patientScheduleCode = :patientScheduleCode, det.fromDate = :fromDate, det.toDate = :toDate "
                    + "		from packageDetail as pd "
                    + "		left join packageDetail as det on pd.packageCode = det.packageCode and pd.organizationCode = det.organizationCode "
                    + "                                     and det.roomCategoryCode is not null and pd.fromDate = det.fromDate "
                    + "                                     and pd.toDate = det.toDate and pd.site_Code = det.site_Code "
                    + "		where pd.code = :code and pd.roomCategoryCode is null ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("organizationManualCode", DmShared.xNull(mrTemp.getOrgCode()).trim());
            psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
            psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
            psTemp.setString("patientScheduleCode", DmShared.foriegnKey(mrTemp.getScheduleCode()));
            psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert/Update into packageDetail >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert into packageDetail(code, packageCode, organizationCode, roomCategoryCode, organizationManualCode, fromDate, toDate, "
                        + " actualValue, packageValue, patientScheduleCode, isActive, preparedBy, site_Code, u_EntDt, transferDt) "
                        + " values (:code, :packageCode, :organizationCode, :roomCategoryCode, :organizationManualCode, :fromDate, :toDate, "
                        + " :actualValue, :packageValue, :patientScheduleCode, :isActive, :preparedBy, :site_Code, :u_EntDt, :transferDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("packageCode", DmShared.foriegnKey(mrTemp.getPackageCode()));
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrganizationCode()));
                psTemp.setString("roomCategoryCode", DmShared.foriegnKey(mrTemp.getRoomCategoryCode()));
                psTemp.setString("organizationManualCode", DmShared.xNull(mrTemp.getOrgCode()).trim());
                psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("actualValue", DmServer.vNull(mrTemp.getActualValue(), "0.00"));
                psTemp.setString("packageValue", DmServer.vNull(mrTemp.getPackageAmount(), "0.00"));
                psTemp.setString("patientScheduleCode", DmShared.foriegnKey(mrTemp.getScheduleCode()));
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveCode()).toString());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "Update packageDetail set "
                        + " packageCode = :packageCode, "
                        + " organizationCode = :organizationCode, "
                        + " roomCategoryCode = :roomCategoryCode, "
                        + " organizationManualCode = :organizationManualCode, "
                        + " fromDate = :fromDate, "
                        + " toDate = :toDate, "
                        + " actualValue = :actualValue, "
                        + " packageValue = :packageValue, "
                        + " patientScheduleCode = :patientScheduleCode, "
                        + " isActive = :isActive, "
                        + " u_EntDt = :u_EntDt, "
                        + " u_Name = :u_Name, "
                        + " transferDt = :transferDt "
                        + " where code = :code ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("packageCode", DmShared.foriegnKey(mrTemp.getPackageCode()));
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrganizationCode()));
                psTemp.setString("roomCategoryCode", DmShared.foriegnKey(mrTemp.getRoomCategoryCode()));
                psTemp.setString("organizationManualCode", DmShared.xNull(mrTemp.getOrgCode()).trim());
                psTemp.setDate("fromDate", DmServer.convertDate(mrTemp.getFromDate()));
                psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getToDate()));
                psTemp.setString("actualValue", DmServer.vNull(mrTemp.getActualValue(), "0.00"));
                psTemp.setString("packageValue", DmServer.vNull(mrTemp.getPackageAmount(), "0.00"));
                psTemp.setString("patientScheduleCode", DmShared.foriegnKey(mrTemp.getScheduleCode()));
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveCode()).toString());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*=============================< Insert records into packageServiceDetail (Service) >==============================*/
            for (GrdServiceMaster_Mdl node : mrTemp.getListService()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    sqlQuery = "Insert into packageServiceDetail (code, serviceCode, nature, qty, rate, amount, pkgRate, pkgAmt) "
                            + " values (:code, :serviceCode, :nature, :qty, :rate, :amount, :pkgRate, :pkgAmt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("serviceCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("nature", "0");
                    psTemp.setString("qty", DmServer.vNull(node.getQuantity(), "0.00"));
                    psTemp.setString("rate", DmServer.vNull(node.getRate(), "0.00"));
                    psTemp.setString("amount", DmServer.vNull(node.getAmount(), "0.00"));
                    psTemp.setString("pkgRate", DmServer.vNull(node.getPkgRate(), "0.00"));
                    psTemp.setString("pkgAmt", DmServer.vNull(node.getPkgAmount(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*=============================< Insert records into packageServiceDetail (Investigation) >==============================*/
            for (GrdServiceMaster_Mdl node : mrTemp.getListInvestigation()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    sqlQuery = "Insert into packageServiceDetail (code, serviceCode, nature, qty, rate, amount) "
                            + " values (:code, :serviceCode, :nature, :qty, :rate, :amount) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("serviceCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("nature", "1");
                    psTemp.setString("qty", DmServer.vNull(node.getQuantity(), "0.00"));
                    psTemp.setString("rate", DmServer.vNull(node.getRate(), "0.00"));
                    psTemp.setString("amount", DmServer.vNull(node.getAmount(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*=============================< Insert records into packageItemDetail >==============================*/
            for (GrdServiceMaster_Mdl node : mrTemp.getListMedicineItem()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    sqlQuery = "Insert into packageItemDetail (code, itemCode, qty, rate, amount) "
                            + " values (:code, :itemCode, :qty, :rate, :amount) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("itemCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("qty", DmServer.vNull(node.getQuantity(), "0.00"));
                    psTemp.setString("rate", DmServer.vNull(node.getRate(), "0.00"));
                    psTemp.setString("amount", DmServer.vNull(node.getAmount(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            if (DmShared.iNull(mrTemp.getRoomCategoryCode()).longValue() == 0) {
                /*==========================< Check whether service name used in room category or not >============================*/
                sqlQuery = "Select  psd.serviceCode, psm.name as serviceName "
                        + "         from ( "
                        + "             select	psd.serviceCode "
                        + "                     from packageServiceDetail psd "
                        + "			where psd.qty <> 0 and psd.nature = 0 and psd.code in ( "
                        + "                             select  pd1.code "
                        + "                                     from packageDetail pd "
                        + "					left join packageDetail pd1 on pd.packageCode = pd1.packageCode "
                        + "                                     and pd.organizationCode = pd1.organizationCode and pd1.roomCategoryCode is not null "
                        + "                                     and pd.fromDate = pd1.fromDate and pd.toDate = pd1.toDate "
                        + "					where pd.code = :code and pd1.code is not null and pd.roomCategoryCode is null "
                        + "                             ) "
                        + "                             group by psd.serviceCode "
                        + "         ) psd "
                        + "         left join ( "
                        + "             select	psd.serviceCode "
                        + "                     from packageServiceDetail psd "
                        + "			where psd.code = :code and psd.nature = 0 "
                        + "         ) as psdMain on psd.serviceCode = psdMain.serviceCode "
                        + "         left join patientServiceMast psm on psd.serviceCode = psm.code "
                        + "         where psdMain.serviceCode is null ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException(DmShared.xNull(rsTemp.getString("serviceName")) + " is used. You can not delete it.");
                }
                rsTemp.close();
                psTemp.close();

                /*============================< Delete records from packageServiceDetail (Room Category Wise) >==============================*/
                sqlQuery = "Delete  psd "
                        + "         from ( "
                        + "             select	psd.serviceCode "
                        + "                     from packageServiceDetail psd "
                        + "			where psd.nature = 0 and psd.code in ( "
                        + "                         select  pd1.code "
                        + "                         from packageDetail pd "
                        + "                         left join packageDetail pd1 on pd.packageCode = pd1.packageCode and pd.organizationCode = pd1.organizationCode "
                        + "                         and pd1.roomCategoryCode is not null "
                        + "                         and pd.fromDate = pd1.fromDate and pd.toDate = pd1.toDate "
                        + "                         where pd.code = :code and pd1.code is not null and pd.roomCategoryCode is null "
                        + "			) "
                        + "         ) as psd "
                        + "         left join ( "
                        + "             select  psd.serviceCode "
                        + "                     from packageServiceDetail psd "
                        + "                     where psd.code = :code and psd.nature = 0 "
                        + "         ) as psdMain on psd.serviceCode = psdMain.serviceCode "
                        + "         left join patientServiceMast psm on psd.serviceCode = psm.code "
                        + "         where psdMain.serviceCode is null ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                /*=============================< Insert records into packageServiceDetail (Service in room category wise)  >==============================*/
                sqlQuery = "Insert into packageServiceDetail (code, nature, serviceCode, qty, rate, amount, pkgRate, pkgAmt) "
                        + " Select	p.code, psdMain.nature, psdMain.serviceCode, 0 as qty, psdMain.rate, 0 as amount, 0 as pkgRate, 0 as pkgAmt "
                        + "		from ( "
                        + "                 select	psd.code, psd.nature, psd.serviceCode, 0 as qty, psd.rate, 0 as amount, 0 as pkgRate, 0 as pkgAmt "
                        + "                             from packageServiceDetail psd "
                        + "				where psd.code = :code and psd.nature = 0 "
                        + "		) as psdMain "
                        + "		left join ( "
                        + "                 select	psd.serviceCode "
                        + "                             from packageServiceDetail psd "
                        + "				where psd.nature = 0 and psd.code in ( "
                        + "				select pd1.code "
                        + "				from packageDetail pd "
                        + "				left join packageDetail pd1 on pd.packageCode = pd1.packageCode and pd.organizationCode = pd1.organizationCode "
                        + "				and pd1.roomCategoryCode is not null "
                        + "				and pd.fromDate = pd1.fromDate and pd.toDate = pd1.toDate "
                        + "				where pd.code = :code and pd1.code is not null and pd.roomCategoryCode is null) "
                        + "		) as psd on psd.serviceCode = psdMain.serviceCode "
                        + "		left join ( "
                        + "                     select  pd1.code "
                        + "                             from packageDetail pd "
                        + "                             left join packageDetail pd1 on pd.packageCode = pd1.packageCode and pd.organizationCode = pd1.organizationCode "
                        + "                             and pd1.roomCategoryCode is not null "
                        + "                             and pd.fromDate = pd1.fromDate and pd.toDate = pd1.toDate "
                        + "                             where pd.code = :code and pd1.code is not null and pd.roomCategoryCode is null "
                        + "		) as p on 1 = 1 "
                        + "		where psd.serviceCode is null and isNull(p.code,0) <> 0 ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                /*==========================< Check whether service name used in room category or not >============================*/
                sqlQuery = "Select  temp.serviceCode, psm.name as serviceName "
                        + "         from ( "
                        + "             Select	psd.serviceCode "
                        + "                     from packageServiceDetail as psd "
                        + "			where psd.code = :code and nature = 0 "
                        + "			and psd.serviceCode not in ( "
                        + "                                     select  psd.serviceCode "
                        + "                                             from packageDetail pd "
                        + "						left join packageDetail pd1 on pd.packageCode = pd1.packageCode and "
                        + "                                             pd.organizationCode = pd1.organizationCode and pd1.roomCategoryCode is null and "
                        + "						pd.fromDate = pd1.fromDate and pd.toDate = pd1.toDate "
                        + "						left join packageServiceDetail as psd on pd1.code = psd.code and psd.nature = 0 "
                        + "						where pd.code = :code and pd1.code is not null and pd.roomCategoryCode is not null and psd.code is not null "
                        + "			) "
                        + "		) as temp "
                        + "		left join patientServiceMast psm on temp.serviceCode = psm.code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException(DmShared.xNull(rsTemp.getString("serviceName")) + " is not exist in parent package. Please refresh and then modify.");
                }
                rsTemp.close();
                psTemp.close();
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
    public DmResult<PackageDetail_Mdl> getPackageDetail(String code, String packageCode, String organizationCode, String roomCategoryCode) {
        DmResult<PackageDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PackageDetail_Mdl mrTemp = new PackageDetail_Mdl();
        ServicePriceList_Mdl priceListMdl = new ServicePriceList_Mdl();

        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Default Organization detail >>===============================*/
            sqlQuery = "Select	om.subCode, sg.name, rcm.code as roomCategoryCode, rcm.name as roomCategoryName "
                    + "		from organizationMast as om "
                    + "		left join subGroup as sg on om.subCode = sg.subCode "
                    + "         left join roomCatMast as rcm on rcm.code = :roomCategoryCode "
                    + "		where om.shortName = 'CP' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("roomCategoryCode", DmShared.iNull(roomCategoryCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDefaultOrganizationCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setDefaultOrganizationName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setDefaultRoomCategoryCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                mrTemp.setDefaultRoomCategoryName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Package detail >>===============================*/
            sqlQuery = "Select	pm.code as packageCode, pm.name as packageName, pm.shortName, pm.nature, "
                    + "		dbo.getTaggedRateList(case when pd.code is null then :organizationCode else pd.organizationCode end,:vrDate) as rateListCode, "
                    + "		(case when pd.code is null then :organizationCode else pd.organizationCode end) as organizationCode, "
                    + "		(case when pd.code is null then :roomCategoryCode else pd.roomCategoryCode end) as roomCategoryCode, "
                    + "		org.name as organizationName, rcm.name as roomCategoryName, pd.patientScheduleCode, psm.name as patientScheduleName, "
                    + "		pd.code, pd.isActive, pd.organizationManualCode, pd.fromDate, pd.toDate, pd.packageValue "
                    + "		from packageMast as pm "
                    + "		left join packageDetail as pd on pm.code = pd.packageCode and pd.code = :code and pd.site_Code = :site_Code "
                    + "		left join patientScheduleMast as psm on pd.patientScheduleCode = psm.code "
                    + "		left join subGroup as org on org.subCode = (case when pd.code is null then :organizationCode else pd.organizationCode end) "
                    + "		left join roomCatMast as rcm on rcm.code = (case when pd.code is null then :roomCategoryCode else pd.roomCategoryCode end) "
                    + "		where pm.code = :packageCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(code).toString());
            psTemp.setString("packageCode", DmShared.iNull(packageCode).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("vrDate", DmServer.convertDate(DmServer.getServerDateTime()[0]));

            if (DmShared.iNull(organizationCode).longValue() == 0) {
                psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getDefaultOrganizationCode()).toString());
            } else {
                psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            }
            psTemp.setString("roomCategoryCode", DmShared.iNull(roomCategoryCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setPackageCode(DmShared.xNull(rsTemp.getString("packageCode")));
                mrTemp.setPackageName(DmShared.xNull(rsTemp.getString("packageName")));
                mrTemp.setPackageShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setOrganizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
                mrTemp.setRoomCategoryCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                mrTemp.setRoomCategoryName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                mrTemp.setPackageNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setPackageNatureName(DmShared.iNull(rsTemp.getString("nature")).intValue() == 0 ? "Hospital"
                        : DmShared.iNull(rsTemp.getString("nature")).intValue() == 1 ? "Day Care" : "");
                mrTemp.setOrgCode(DmShared.xNull(rsTemp.getString("organizationManualCode")));
                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                mrTemp.setScheduleCode(DmShared.xNull(rsTemp.getString("patientScheduleCode")));
                mrTemp.setScheduleName(DmShared.xNull(rsTemp.getString("patientScheduleName")));
                mrTemp.setActiveCode(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setActiveName(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 1 ? "Yes" : "No");
                mrTemp.setPackageAmount(DmServer.vNull(rsTemp.getString("packageValue"), "0.00"));
                mrTemp.setRateListCode(DmShared.xNull(rsTemp.getString("rateListCode")));

            }
            rsTemp.close();
            psTemp.close();
            if (DmShared.iNull(mrTemp.getRateListCode()).intValue() > 0) {
                priceListMdl.setRateListCode(mrTemp.getRateListCode());
                priceListMdl.setOrganizationCode(mrTemp.getOrganizationCode());
                priceListMdl.setServiceCode(mrTemp.getPackageCode());
                priceListMdl.setRoomCatCode(mrTemp.getRoomCategoryCode());
                priceListMdl.setSiteCode(DmServer.getSsnSiteCode(this));
                priceListMdl.setDate(DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("") ? DmServer.getServerDateTime()[0] : mrTemp.getFromDate());
                priceListMdl = STServer.getServicePriceBestFit(priceListMdl, cnTemp);
                mrTemp.setRateListPrice(priceListMdl.getRate());
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
                /*==============================< Get Service Detail >==============================*/
                mrTemp.getListService().addAll(getServiceTestDetail(mrTemp.getSharedKeyFieldCode(), "0", cnTemp));

                /*==============================< Get Investigation Detail >==============================*/
                mrTemp.getListInvestigation().addAll(getServiceTestDetail(mrTemp.getSharedKeyFieldCode(), "1", cnTemp));

                /*==============================< Get Item Detail >==============================*/
                mrTemp.getListMedicineItem().addAll(getItemDetail(mrTemp.getSharedKeyFieldCode(), cnTemp));
            }

            dmrTemp.setData(mrTemp);
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
    public DmResult<PackageDetail_Mdl> getCopyPackage(String code) {
        DmResult<PackageDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        PackageDetail_Mdl mrTemp = new PackageDetail_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(code).longValue() != 0) {
                mrTemp.setSharedKeyFieldCode(code);

                /*==============================< Get Service Detail >==============================*/
                mrTemp.getListService().addAll(getServiceTestDetail(mrTemp.getSharedKeyFieldCode(), "0", cnTemp));

                /*==============================< Get Investigation Detail >==============================*/
                mrTemp.getListInvestigation().addAll(getServiceTestDetail(mrTemp.getSharedKeyFieldCode(), "1", cnTemp));

                /*==============================< Get Item Detail >==============================*/
                mrTemp.getListMedicineItem().addAll(getItemDetail(mrTemp.getSharedKeyFieldCode(), cnTemp));
            }

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<List<GrdRoomCategory_Mdl>> getRoomCategoryDetail(String packageCode, String organizationCode, String fromDate, String toDate) {
        DmResult<List<GrdRoomCategory_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdRoomCategory_Mdl> listNode = new ArrayList<GrdRoomCategory_Mdl>();
        GrdRoomCategory_Mdl node;
        dmrTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Room Category detail >>===============================*/
            sqlQuery = "Select	rcm.code as roomCategoryCode, rcm.name as roomCategoryName, "
                    + "		pd.code as packageCode, pd.fromDate, pd.toDate, pd.packageValue, pd.isActive "
                    + "		from roomCatMast as rcm "
                    + "		left join packageDetail as pd on pd.packageCode = :packageCode and pd.organizationCode = :organizationCode "
                    + "                             and rcm.code = pd.roomCategoryCode and pd.fromDate = :fromDate and pd.toDate = :toDate and pd.site_Code = :site_Code "
                    + "		order by rcm.name, pd.fromDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("packageCode", DmShared.iNull(packageCode).toString());
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            psTemp.setDate("fromDate", DmServer.convertDate(fromDate));
            psTemp.setDate("toDate", DmServer.convertDate(toDate));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdRoomCategory_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                node.setPackageCode(DmShared.xNull(rsTemp.getString("packageCode")));

                if (DmShared.iNull(node.getPackageCode()).longValue() != 0) {
                    node.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                    node.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                    node.setActive(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 1 ? "Yes" : "No");
                    node.setPackageAmount(DmServer.vNull(rsTemp.getString("packageValue"), "0.00"));
                }

                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(listNode);
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

    private List<GrdServiceMaster_Mdl> getServiceTestDetail(String packageCode, String nature, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdServiceMaster_Mdl> nodeList = new ArrayList<GrdServiceMaster_Mdl>();
        GrdServiceMaster_Mdl node;

        try {
            sqlQuery = "Select	psd.serviceCode, psm.name as serviceName, psd.qty, psd.rate, psd.amount, "
                    + "		psd.pkgRate, psd.pkgAmt "
                    + "		from packageServiceDetail as psd "
                    + "		left join patientServiceMast as psm on psd.serviceCode = psm.code "
                    + "		where psd.code = :code and psd.nature = :nature "
                    + "		order by psm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(packageCode).toString());
            psTemp.setString("nature", DmShared.iNull(nature).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdServiceMaster_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("serviceCode")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceName")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("qty"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                node.setPkgRate(DmServer.vNull(rsTemp.getString("pkgRate"), "0.00"));
                node.setPkgAmount(DmServer.vNull(rsTemp.getString("pkgAmt"), "0.00"));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();
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

        return nodeList;
    }

    private List<GrdServiceMaster_Mdl> getItemDetail(String packageCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdServiceMaster_Mdl> nodeList = new ArrayList<GrdServiceMaster_Mdl>();
        GrdServiceMaster_Mdl node;

        try {
            sqlQuery = "Select	pid.itemCode, im.name as itemName, pid.qty, pid.rate, pid.amount "
                    + "		from packageItemDetail as pid "
                    + "		left join itemMast as im on pid.itemCode = im.code "
                    + "		where pid.code = :code "
                    + "		order by im.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(packageCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdServiceMaster_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("itemCode")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("qty"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();
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

        return nodeList;
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSchedule(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select code, name from patientScheduleMast ");
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

    @Override
    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicineName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicineName_Mdl> lstTemp = new ArrayList<TxtHMedicineName_Mdl>();
        TxtHMedicineName_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  mm.code, mm.name, mm.sku as skuCode, um.unit as skuName "
                    + "		from itemMast mm  "
                    + "         left join itemsiteList as isl on isl.code=mm.code"
                    + "		left join unitmast um on mm.sku = um.code "
                    + "         where  mm.isActive = 1 and mm.filterItemType = :filterItemType and isl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHMedicineName_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));

                lstTemp.add(node);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select rcm.name as category, rcm.code from roomCatMast rcm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Category")));
                lstTemp.add(listNode);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(boolean isInvestigation, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (isInvestigation) {
                condition = " and psm.serviceNature in (" + ServiceNature.PATHOLOGY_TEST.getId() + "," + ServiceNature.RADIOLOGY_TEST.getId() + ") ";
            }

            sqlQuery = "Select  psm.code, psm.name "
                    + "         from patientServiceMast as psm"
                    + "         left join patientServiceSiteList as pssl on pssl.code = psm.code "
                    + "         where psm.isActive = 1 and pssl.site_Code = :site_Code " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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

    @Override
    public DmResultPageing<List<TxtHPackage_Mdl>> getTxtHPackage(boolean isApplicable, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPackage_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPackage_Mdl> lstTemp = new ArrayList<TxtHPackage_Mdl>();
        TxtHPackage_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (isApplicable) {
                condition = " and pd.roomCategoryCode is null ";
            }

            sqlQuery = "Select	pd.code, pd.packageCode, pm.name as packageName, pm.shortName, pd.fromDate, pd.toDate, "
                    + "		case pm.nature when 0 then 'Hospital' when 1 then 'Day Care' end as nature, pd.organizationManualCode, "
                    + "		org.name as organizationName, rcm.name as roomCategoryName, pd.packageValue as packageAmount "
                    + "		from packageDetail as pd "
                    + "		left join packageMast as pm on pd.packageCode = pm.code "
                    + "		left join subGroup as org on pd.organizationCode = org.subCode "
                    + "		left join roomCatMast as rcm on pd.roomCategoryCode = rcm.code "
                    + "		where pd.isActive = 1 and pm.nature in (0,1) " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHPackage_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("packageName")));
                node.setPackageCode(DmShared.xNull(rsTemp.getString("packageCode")));
                node.setPackageName(DmShared.xNull(rsTemp.getString("packageName")));
                node.setPackageShortName(DmShared.xNull(rsTemp.getString("shortName")));
                node.setOrganizationManualCode(DmShared.xNull(rsTemp.getString("organizationManualCode")));
                node.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
                node.setRoomCategoryName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                node.setPackageNature(DmShared.xNull(rsTemp.getString("nature")));
                node.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                node.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                node.setPackageAmount(DmServer.vNull(rsTemp.getString("packageAmount"), "0.00"));
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

    @Override
    public DmResult<List<ServicePriceList_Mdl>> getServicePriceBestFit(ServicePriceList_Mdl servicePrice_Mdl) {
        DmResult<List<ServicePriceList_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            servicePrice_Mdl.setSiteCode(DmServer.getSsnSiteCode(this));

            List<ServicePriceList_Mdl> listServicePrice = new ArrayList<ServicePriceList_Mdl>();
            listServicePrice.add(STServer.getServicePriceBestFit(servicePrice_Mdl, cnTemp));

            dmrTemp.setData(listServicePrice);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> saveApplicable(String code, String fromDate, String toDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery, newCode = "", userName;
        dmrTemp.setData("");

        try {
            if (DmShared.iNull(code).longValue() == 0) {
                throw new UnsupportedOperationException("Please select package.");
            }
            if (DmShared.dNull(fromDate).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("From Date is mandatory.");
            }
            if (DmShared.dNull(toDate).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("To Date is mandatory.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(this);

            /*====================================< Get all packages with room wise detail >====================================*/
            sqlQuery = "Select	pdt.code, pdt.packageCode, pdt.organizationCode, pdt.roomCategoryCode, pdt.organizationManualCode, pdt.patientScheduleCode, pdt.fromDate, pdt.toDate, "
                    + "		pdt.actualValue, pdt.packageValue "
                    + "		from packageDetail as pd "
                    + "		left join packageDetail as pdt on pd.packageCode = pdt.packageCode and pd.organizationCode = pdt.organizationCode "
                    + "					and pd.fromDate = pdt.fromDate and pd.toDate = pdt.toDate and pdt.isActive = 1 and pd.site_Code = pdt.site_Code "
                    + "		where pd.code = :code "
                    + "		order by pdt.roomCategoryCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(code).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                newCode = DmShared.iNull(DmServer.getSerialNoInteger(this, "packageDetail", cnTemp)).toString();

                /*==========================< Check whether name already exist or not >============================*/
                sqlQuery = "Select  pd.code "
                        + "         from packageDetail as pd "
                        + "         where pd.code <> :code and pd.packageCode = :packageCode and pd.organizationCode = :organizationCode "
                        + "         and pd.site_Code = :site_Code and pd.isActive = 1 and isNull(pd.roomCategoryCode,0) = :roomCategoryCode "
                        + "         and ((pd.fromDate <= :fromDate and pd.toDate >= :fromDate) or "
                        + "         (pd.fromDate <= :toDate and pd.toDate >= :toDate) or (pd.fromDate >= :fromDate and pd.fromDate <= :toDate)) ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("code", newCode);
                psTemp1.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp1.setString("packageCode", rsTemp.getString("packageCode"));
                psTemp1.setString("organizationCode", rsTemp.getString("organizationCode"));
                psTemp1.setString("roomCategoryCode", rsTemp.getString("roomCategoryCode"));
                psTemp1.setDate("fromDate", DmServer.convertDate(fromDate));
                psTemp1.setDate("toDate", DmServer.convertDate(toDate));
                rsTemp1 = psTemp1.executeQuery();
                if (rsTemp1.next()) {
                    throw new UnsupportedOperationException("Package already exist with given from date and to date.");
                }
                rsTemp1.close();
                psTemp1.close();

                /*====================================< Insert into packageDetail >====================================*/
                sqlQuery = "Insert into packageDetail(code, packageCode, organizationCode, roomCategoryCode, organizationManualCode, patientScheduleCode, "
                        + " fromDate, toDate, actualValue, packageValue, isActive, preparedBy, site_Code, u_EntDt, transferDt) "
                        + " values (:code, :packageCode, :organizationCode, :roomCategoryCode, :organizationManualCode, :patientScheduleCode, "
                        + " :fromDate, :toDate, :actualValue, :packageValue, :isActive, :preparedBy, :site_Code, :u_EntDt, :transferDt)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("code", newCode);
                psTemp1.setString("packageCode", rsTemp.getString("packageCode"));
                psTemp1.setString("organizationCode", rsTemp.getString("organizationCode"));
                psTemp1.setString("roomCategoryCode", rsTemp.getString("roomCategoryCode"));
                psTemp1.setString("organizationManualCode", rsTemp.getString("organizationManualCode"));
                psTemp1.setString("patientScheduleCode", rsTemp.getString("patientScheduleCode"));
                psTemp1.setDate("fromDate", DmServer.convertDate(fromDate));
                psTemp1.setDate("toDate", DmServer.convertDate(toDate));
                psTemp1.setString("actualValue", DmServer.vNull(rsTemp.getString("actualValue"), "0.00"));
                psTemp1.setString("packageValue", DmServer.vNull(rsTemp.getString("packageValue"), "0.00"));
                psTemp1.setString("isActive", "1");
                psTemp1.setString("preparedBy", userName);
                psTemp1.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp1.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp1.setString("transferDt", null);
                psTemp1.executeUpdate();
                psTemp1.close();

                /*============================< Delete records from packageServiceDetail >==============================*/
                sqlQuery = "Delete From packageServiceDetail Where code = :code ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("code", newCode);
                psTemp1.executeUpdate();
                psTemp1.close();

                /*============================< Delete records from packageItemDetail >==============================*/
                sqlQuery = "Delete From packageItemDetail Where code = :code ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("code", newCode);
                psTemp1.executeUpdate();
                psTemp1.close();

                /*=============================< Insert records into packageServiceDetail (Service & Investigation) >==============================*/
                sqlQuery = "Insert into packageServiceDetail (code, serviceCode, nature, qty, rate, amount, pkgRate, pkgAmt) "
                        + " Select :newCode, serviceCode, nature, qty, rate, amount, pkgRate, pkgAmt from packageServiceDetail where code = :code ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("code", DmShared.iNull(code).toString());
                psTemp1.setString("newCode", newCode);
                psTemp1.executeUpdate();
                psTemp1.close();

                /*=============================< Insert records into packageItemDetail >==============================*/
                sqlQuery = "Insert into packageItemDetail (code, itemCode, qty, rate, amount) "
                        + " Select :newCode, itemCode, qty, rate, amount from packageItemDetail where code = :code ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("code", DmShared.iNull(code).toString());
                psTemp1.setString("newCode", newCode);
                psTemp1.executeUpdate();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(newCode);
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
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }
}
