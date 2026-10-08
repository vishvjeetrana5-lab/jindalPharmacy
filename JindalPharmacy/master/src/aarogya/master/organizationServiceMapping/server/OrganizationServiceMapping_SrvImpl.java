package aarogya.master.organizationServiceMapping.server;

import aarogya.master.organizationServiceMapping.shared.*;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListEntryFrom;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListNature;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.*;
import dataman.dmBase.self.server.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OrganizationServiceMapping_SrvImpl extends DmRSSPrivate implements OrganizationServiceMapping_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code from organizationPriceList order by u_EntDt ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResult<OrganizationServiceMapping_Mdl> getMoveRec(String strKey, String organizationCode) {
        DmResult<OrganizationServiceMapping_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        OrganizationServiceMapping_Mdl mrTemp = new OrganizationServiceMapping_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdService_Mdl> listServiceDetail = new ArrayList<GrdService_Mdl>();
        GrdService_Mdl service_Mdl;
        List<GrdRoomCategory_Mdl> listRoomCategory = new ArrayList<GrdRoomCategory_Mdl>();
        GrdRoomCategory_Mdl roomCategory_Mdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Header Row >====================================*/
            sqlQuery = "Select	opl.code "
                    + "		from organizationPriceList as opl "
                    + "		where opl.organizationCode = :organizationCode "
                    + "         order by opl.applicableFromDate desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                strKey = DmShared.xNull(rsTemp.getString("code"));
            }
            psTemp.close();
            rsTemp.close();

            /*====================================< Header Row >====================================*/
            sqlQuery = "select	opl.code, opl.organizationCode, org.name as organizationName, opl.applicableFromDate, opl.applicableToDate, "
                    + "         opl.preparedBy, opl.u_Name "
                    + "		from organizationPriceList as opl "
                    + "		left join subGroup as org on opl.organizationCode = org.subCode "
                    + "		where opl.code = :code "
                    + "         order by opl.applicableFromDate,org.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setOrganizationTag(DmShared.xNull(rsTemp.getString("organizationCode")));
                mrTemp.setOrganizationText(DmShared.xNull(rsTemp.getString("organizationName")));
                mrTemp.setApplicableDate(DmShared.dNull(rsTemp.getDate("applicableFromDate")));
                mrTemp.setApplicableTo(DmShared.dNull(rsTemp.getDate("applicableToDate")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
            }
            psTemp.close();
            rsTemp.close();

            /*====================================<Get Room Category Grid >====================================*/
            sqlQuery = "select	oprr.roomCategoryCode, rcm.name as roomCategoryName, oprr.ratePercentage "
                    + "		from organizationPriceListRoomRate as oprr "
                    + "		left join roomCatMast as rcm on oprr.roomCategoryCode = rcm.code "
                    + "         where oprr.oplCode = :code "
                    + "		order by rcm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                roomCategory_Mdl = new GrdRoomCategory_Mdl();
                roomCategory_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                roomCategory_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                roomCategory_Mdl.setRatePercent(DmServer.vNull(rsTemp.getString("ratePercentage"), "0.00"));
                listRoomCategory.add(roomCategory_Mdl);
            }
            psTemp.close();
            rsTemp.close();

            mrTemp.getListRoomCategory().addAll(listRoomCategory);

            /*====================================< Get Service Grid Detail >====================================*/
            sqlQuery = "select	osm.code, osm.organizationServiceGroupName, osm.organizationServiceName, osm.organizationManualCode, oprd.rate "
                    + "		from organizationPriceList as opl "
                    + "		right join organizationServiceMast as osm on opl.organizationCode = osm.organizationCode "
                    + "		left join organizationPriceListRoomDetail as oprd on opl.code = oprd.oplCode and osm.code = oprd.organizationServiceCode "
                    + "		where opl.code = :code "
                    + "		order by osm.printSNo, osm.organizationServiceName, osm.organizationServiceGroupName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                service_Mdl = new GrdService_Mdl();
                service_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                service_Mdl.setOrgGroupName(DmShared.xNull(rsTemp.getString("organizationServiceGroupName")));
                service_Mdl.setOrgServiceCode(DmShared.xNull(rsTemp.getString("organizationManualCode")));
                service_Mdl.setOrgServiceName(DmShared.xNull(rsTemp.getString("organizationServiceName")));
                service_Mdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));

                /*====================================< Get Mapped Services >====================================*/
                String mapService[] = getMappedServices(DmShared.xNull(service_Mdl.getSharedKeyFieldCode()), cnTemp);
                service_Mdl.setMappedServiceCode(DmShared.xNull(mapService[0]));
                service_Mdl.setMappedServiceName(DmShared.xNull(mapService[1]));
                listServiceDetail.add(service_Mdl);
            }
            psTemp.close();
            rsTemp.close();

            mrTemp.getListServiceDetail().addAll(listServiceDetail);
            /*====================================< Get Organization Detail >====================================*/
            mrTemp.getListOrganizationDetail().addAll(getOrganizationDetail(DmShared.xNull(mrTemp.getOrganizationTag()), DmShared.xNull(strKey), cnTemp));

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
    public DmResult<String> deleteRecord(String applicableFrom, String organizationCode, String code) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery, siteCode;
        // String applicableToDate = "31-Dec-9999";
        String applicableTo = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(code);
        siteCode = DmServer.getSsnSiteCode(this);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*====================================< Delete records from organizationPriceListRoomDetail >====================================*/
            sqlQuery = "delete from organizationPriceListRoomDetail Where oplCode= :oplCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("oplCode", DmShared.iNull(code).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from OrganizationPriceListRoomRate >====================================*/
            sqlQuery = "delete from OrganizationPriceListRoomRate Where oplCode= :oplCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("oplCode", DmShared.iNull(code).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Managing Applicablefrom date and ApplicableTo date in OrganizationPriceListRoomRate >==========*/
            sqlQuery = "select applicableFromDate,applicableToDate "
                    + "from organizationPriceList "
                    + "where applicableFromDate = :applicableFromDate "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + "order by applicableFromDate desc offset 0 rows fetch next 1 rows only ";
            //+ "limit 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFromDate", DmServer.convertDate(applicableFrom));
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("organizationCode", organizationCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                applicableTo = DmShared.dNull(rsTemp.getDate("applicableToDate"));
            }

            rsTemp.close();
            psTemp.close();

            sqlQuery = "select applicableFromDate,applicableToDate "
                    + "from organizationPriceList "
                    + "where applicableFromDate < :applicableFromDate "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + "group by applicableFromDate,applicableToDate "
                    + "order by applicableFromDate desc offset 0 rows fetch next 1 rows only ";
            //+ "limit 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFromDate", DmServer.convertDate(applicableFrom));
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("organizationCode", organizationCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                sqlQuery = "update organizationPriceList set "
                        + "applicableToDate = :applicableToDate "
                        + "where applicableFromDate = :applicableFromDate "
                        + "and site_Code = :site_Code "
                        + "and organizationCode = :organizationCode ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setDate("applicableToDate", DmServer.convertDate(applicableTo));
                psTemp1.setDate("applicableFromDate", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("applicableFromDate"))));
                psTemp1.setString("site_Code", siteCode);
                psTemp1.setString("organizationCode", organizationCode);
                psTemp1.executeUpdate();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Delete records from organizationPriceList >====================================*/
            sqlQuery = "delete from organizationPriceList "
                    + "where applicableFromDate = :applicableFromDate "
                    + "and site_Code = :site_Code "
                    + "and code = :code "
                    + "and organizationCode = :organizationCode ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(code).toString());
            psTemp.setDate("applicableFromDate", DmServer.convertDate(applicableFrom));
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("organizationCode", organizationCode);
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< End >====================================*/
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
    public DmResult<String> saveRecord(OrganizationServiceMapping_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null, rsTemp1 = null;;
        String applicableTo = "31-Dec-9999", maxApplicableDate = "";
        dmrTemp.setData("");
        int count = 0;
        GrdService_Mdl node = null;
        try {
            OrganizationServiceMapping_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            siteCode = DmServer.getSsnSiteCode(this);
            userName = DmServer.getSsnUserName(this);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "organizationPriceList", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*====================================< Managing Applicablefrom date and ApplicableTo date in OrganizationPriceListRoomRate >==========*/
            sqlQuery = "select  code "
                    + "         from organizationPriceList "
                    + "         where site_Code = :site_Code and organizationCode = :organizationCode and applicableFromDate = :applicableFromDate "
                    + "         and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getOrganizationTag()).toString());
            psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Already exist.");
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Delete records from OrganizationPriceListRoomRate >====================================*/
            sqlQuery = "Delete from OrganizationPriceListRoomRate Where oplCode= :oplCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("oplCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from OrganizationPriceListRoomDetail >================================*/
            sqlQuery = "Delete from OrganizationPriceListRoomDetail Where oplCode = :oplCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("oplCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Managing Applicablefrom date and ApplicableTo date in OrganizationPriceListRoomRate >==========*/
            sqlQuery = "select  Max(applicableFromDate) as applicableFrom,Min(applicableFromDate) as applicableFromMin  "
                    + "         from organizationPriceList "
                    + "         where site_Code = :site_Code and organizationCode = :organizationCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getOrganizationTag()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
            }
            rsTemp.close();
            psTemp.close();

            if (!maxApplicableDate.equalsIgnoreCase("")) {
                if (DmServer.convertDate(mrTemp.getApplicableDate()).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                    sqlQuery = "Select  applicableFromDate, applicableToDate "
                            + "         from organizationPriceList "
                            + "         where site_Code = :site_Code and organizationCode = :organizationCode and applicableFromDate < :applicableFromDate "
                            + "         group by applicableFromDate, applicableToDate "
                            + "         order by applicableFromDate desc "
                            + "         offset 0 rows fetch next 1 rows only ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableDate()));
                    psTemp.setString("site_Code", siteCode);
                    psTemp.setString("organizationCode", DmShared.xNull(mrTemp.getOrganizationTag()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableToDate"));
                        sqlQuery = "Update  organizationPriceList set "
                                + "         applicableToDate = :applicableToDate "
                                + "         where applicableFromDate = :applicableFromDate and site_Code = :site_Code and organizationCode = :organizationCode ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableDate()))));
                        psTemp1.setDate("applicableFromDate", rsTemp.getDate("applicableFromDate"));
                        psTemp1.setString("site_Code", siteCode);
                        psTemp1.setString("organizationCode", DmShared.xNull(mrTemp.getOrganizationTag()));
                        psTemp1.executeUpdate();
                        psTemp1.close();

                    } else {
                        sqlQuery = "Select  applicableFromDate,applicableToDate "
                                + "         FROM organizationPriceList "
                                + "         where applicableFromDate > :applicableFromDate and site_Code = :site_Code and organizationCode = :organizationCode "
                                + "         group by applicableFromDate,applicableToDate "
                                + "         order by applicableFromDate asc offset 0 rows fetch next 1 rows only ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableDate()));
                        psTemp1.setString("site_Code", siteCode);
                        psTemp1.setString("organizationCode", DmShared.xNull(mrTemp.getOrganizationTag()).toString());
                        rsTemp1 = psTemp1.executeQuery();
                        if (rsTemp1.next()) {
                            applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFromDate")));

                        }
                        psTemp1.close();
                        rsTemp1.close();
                    }

                    psTemp.close();
                    rsTemp.close();
                } else {
                    sqlQuery = "Update  organizationPriceList set "
                            + "         applicableToDate = :applicableToDate "
                            + "         where applicableFromDate = :applicableFromDate and site_Code = :site_Code and organizationCode = :organizationCode ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFromDate", DmServer.convertDate(maxApplicableDate));
                    psTemp.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableDate()))));
                    psTemp.setString("site_Code", siteCode);
                    psTemp.setString("organizationCode", DmShared.xNull(mrTemp.getOrganizationTag()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*====================================< Insert / Update records into organizationPriceList(Header Table) >====================================*/
            if (blnAdd) {
                sqlQuery = "insert into organizationPriceList(code, organizationCode, applicableFromDate, applicableToDate, "
                        + " site_Code, preparedBy, u_EntDt, transferDt)  "
                        + " VALUES (:code, :organizationCode, :applicableFromDate, :applicableToDate, "
                        + " :site_Code, :preparedBy, :u_EntDt, :transferDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrganizationTag()));
                psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableDate()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                if (DmShared.dNull(mrTemp.getApplicableTo()).trim().equalsIgnoreCase("")) {
                    /*====================================< Select Query to check whether the record available or not>=======================*/
                    sqlQuery = " SELECT applicableFromDate from organizationPriceList "
                            + "  WHERE organizationCode=:organizationCode AND applicableFromDate>:applicableFromDate"
                            + "  ORDER BY applicableFromDate offset 0 rows fetch next 1 rows only ";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrganizationTag()));
                    psTemp1.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableDate()));
                    rsTemp1 = psTemp1.executeQuery();
                    if (rsTemp1.next()) {
                        applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFromDate")));
                    }
                    rsTemp1.close();
                    psTemp1.close();
                    /*=============================================================================================================================================*/
                    psTemp.setDate("applicableToDate", DmServer.convertDate(applicableTo));
                } else {
                    psTemp.setDate("applicableToDate", DmServer.convertDate(mrTemp.getApplicableTo()));
                }
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "Update organizationPriceList set "
                        + " organizationCode = :organizationCode, "
                        + " applicableFromDate = :applicableFromDate, "
                        + " applicableToDate = :applicableToDate, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :u_EntDt, "
                        + " transferDt = :transferDt "
                        + " where code = :code ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrganizationTag()));
                psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableDate()));
                psTemp.setDate("applicableToDate", DmServer.convertDate(mrTemp.getApplicableTo()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            }
            psTemp.executeUpdate();
            psTemp.close();

            List<GrdRoomCategory_Mdl> listRoomCategory = mrTemp.getListRoomCategory();
            /*====================================< Insert records into organizationPriceListRoomRate >====================================*/
            for (GrdRoomCategory_Mdl category_Mdl : listRoomCategory) {
                if (DmShared.iNull(category_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    sqlQuery = "Insert into organizationPriceListRoomRate(oplCode, roomCategoryCode, ratePercentage) "
                            + " VALUES (:oplCode, :roomCategoryCode, :ratePercentage) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("oplCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("roomCategoryCode", DmShared.foriegnKey(category_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("ratePercentage", DmServer.vNull(category_Mdl.getRatePercent(), "0"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            List<GrdService_Mdl> listServiceDetail = mrTemp.getListServiceDetail();

            for (GrdService_Mdl service_Mdl : listServiceDetail) {
                if (!DmShared.xNull(service_Mdl.getOrgServiceCode()).trim().equalsIgnoreCase("")) {
                    ++count;
                    node = service_Mdl;
                    if (blnAdd) {
                        String code = "";
                        boolean flag = false;
                        /*====================================< Check whether record already exist or not >====================================*/
                        sqlQuery = "Select  code from organizationServiceMast where organizationCode = :organizationCode and organizationServiceName = :organizationServiceName ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getOrganizationTag()).toString());
                        psTemp.setString("organizationServiceName", DmShared.xNull(service_Mdl.getOrgServiceName()).trim());
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            code = DmShared.iNull(rsTemp.getString("code")).toString();
                        } else {
                            flag = true;
                            code = DmShared.iNull(DmServer.getSerialNoInteger(this, "organizationServiceMast", cnTemp)).toString();
                        }
                        rsTemp.close();
                        psTemp.close();

                        service_Mdl.setSharedKeyFieldCode(code);

                        if (flag) {
                            /*====================================<< Insert into organizationServiceMast >>=============================*/
                            sqlQuery = "Insert into organizationServiceMast(code, organizationServiceName, organizationManualCode, organizationServiceGroupName, "
                                    + " organizationCode, printSNo, preparedBy, u_EntDt) "
                                    + " values (:code,  :organizationServiceName, :organizationManualCode, :organizationServiceGroupName, "
                                    + " :organizationCode, :printSNo, :userName,:u_EntDt) ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("code", DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).toString());
                            psTemp.setString("organizationServiceName", DmShared.xNull(service_Mdl.getOrgServiceName()));
                            psTemp.setString("organizationManualCode", DmShared.xNull(service_Mdl.getOrgServiceCode()));
                            psTemp.setString("organizationServiceGroupName", DmShared.xNull(service_Mdl.getOrgGroupName()));
                            psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrganizationTag()));
                            psTemp.setString("printSNo", DmShared.xNull(service_Mdl.getPrintSNo()));
                            psTemp.setString("userName", userName);
                            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }

                    if (DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                        /*====================================< Insert records into organizationPriceListRoomDetail >====================================*/
                        sqlQuery = "Insert into organizationPriceListRoomDetail(oplCode, organizationServiceCode, rate) "
                                + " VALUES (:oplCode, :organizationServiceCode, :rate) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("oplCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("organizationServiceCode", DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).toString());
                        psTemp.setString("rate", DmServer.vNull(service_Mdl.getRate(), "0.00"));
                        psTemp.executeUpdate();
                        psTemp.close();

                        if (!DmShared.xNull(service_Mdl.getMappedServiceCode()).trim().equalsIgnoreCase("")) {
                            /*====================================< Insert record into organizationServiceMapping >====================================*/
                            saveMappedServices(DmShared.xNull(service_Mdl.getSharedKeyFieldCode()), DmShared.xNull(service_Mdl.getMappedServiceCode()), cnTemp);
                        }
                    }
                }
            }

            /*====================================< Save into patientServicePriceList >====================================*/
            savePriceList(DmShared.xNull(mrTemp.getOrganizationTag()), mrTemp.getRateListCode(), cnTemp);

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
            System.out.println(node);
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	opl.code, opl.organizationCode, sg.name as OrganizationName, "
                    + "         opl.applicableFromDate, opl.applicableToDate "
                    + "		from organizationPriceList opl "
                    + "		left join subGroup sg on sg.subCode =opl.organizationCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setOrganizationTag(DmShared.xNull(rsTemp.getString("organizationCode")));
                listNode.setOrganizationText(DmShared.xNull(rsTemp.getString("OrganizationName")));
                listNode.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFromDate")));
                listNode.setApplicableTo(DmShared.dNull(rsTemp.getDate("applicableToDate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = " select code, name from patientServiceMast where isActive = 1 ";
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrganization(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "select  om.subCode, sg.name, om.shortname, dbo.getTaggedRateList(om.subCode, :vrDate) as rateListCode "
                    + "         from organizationMast om "
                    + "         left join SubGroup sg on om.SubCode = sg.SubCode ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setDate("vrDate", DmServer.convertDate(DmServer.getServerDateTime()[0]));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("rateListCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from roomCatMast " + condition;
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
    public DmResultPageing<List<TxtHServiceTest_Mdl>> getTxtHServiceTest(String orgCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHServiceTest_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHServiceTest_Mdl> lstTemp = new ArrayList<TxtHServiceTest_Mdl>();
        TxtHServiceTest_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	osm.code, osm.organizationServiceName, osm.organizationManualCode, sm.serviceCode, psm.name as serviceName "
                    + "		from organizationServiceMast as osm "
                    + "		left join organizationServiceMapping as sm on osm.code = sm.organizationServiceCode "
                    + "		left join patientServiceMast as psm on sm.serviceCode = psm.code "
                    + "		where osm.organizationCode = :organizationCode ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("organizationCode", DmShared.iNull(orgCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHServiceTest_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("organizationServiceName")));
                listNode.setOrgServiceCode(DmShared.xNull(rsTemp.getString("organizationManualCode")));
                listNode.setServiceCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                listNode.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

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
    public DmResult<List<GrdService_Mdl>> getImportData(String dataFilePath) {
        DmResult<List<GrdService_Mdl>> dmrTemp = new DmResult<List<GrdService_Mdl>>();
        dmrTemp.setData(null);
        String logDirectoryPath, sessionId;
        ImportData_Mdl mrTemp = new ImportOrganizationRateList().getImportDataModel();
        try {
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();

            logDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "importData";
            logDirectoryPath = logDirectoryPath + File.separatorChar + "logs";
            new File(logDirectoryPath).mkdirs();

            mrTemp.setDataFilePath(dataFilePath);
            mrTemp.setLogFileName(logDirectoryPath + File.separatorChar + ImportOrganizationRateList.class.getSimpleName() + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".log");

            dmrTemp.setData(ImportOrganizationRateList.importData(this, mrTemp));

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    public static void savePriceList(String organizationCode, String rateListCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Delete records from patientServicePriceList >====================================*/
            sqlQuery = "Delete from patientServicePriceList where organizationCode = :organizationCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert record into patientServicePriceList >====================================*/
            sqlQuery = "Insert into patientServicePriceList (entryFrom, entryFromNature, nature,dayNameCode, rateListCode,"
                    + "			organizationCode, roomCatCode, serviceCode, organizationServiceCode,site_code, applicableFrom, applicableTo, rate, transferDt)  "
                    + " select	:entryFrom, '0', :nature, '0', :rateListCode, "
                    + "		temp.organizationCode, temp.roomCategoryCode, temp.serviceCode,temp.organizationServiceCode, temp.site_Code, temp.applicableFromDate, temp.applicableToDate, temp.rate, null "
                    + "		from ( "
                    + "			select	opl.organizationCode, map.serviceCode,map.organizationServiceCode, oprr.roomCategoryCode, opl.applicableFromDate, opl.applicableToDate, "
                    + "				opl.site_Code, (oplrd.rate + (oplrd.rate * oprr.ratePercentage) / 100.0) as rate "
                    + "				from organizationServiceMast as osm "
                    + "				left join organizationPriceList as opl on osm.organizationCode = opl.organizationCode "
                    + "				left join organizationServiceMapping as map on osm.code = map.organizationServiceCode "
                    + "				left join organizationPriceListRoomRate as oprr on opl.code = oprr.oplCode "
                    + "				left join organizationPriceListRoomDetail as oplrd on opl.code = oplrd.oplCode and osm.code = oplrd.organizationServiceCode "
                    + "				where (osm.organizationCode = :organizationCode) and (map.serviceCode is not null) and (oprr.roomCategoryCode is not null) "
                    + "			union all "
                    + "			select	opl.organizationCode, map.serviceCode,map.organizationServiceCode, null as roomCategoryCode, opl.applicableFromDate, opl.applicableToDate,  "
                    + "				opl.site_Code, oplrd.rate "
                    + "				from organizationServiceMast as osm "
                    + "				left join organizationPriceList as opl on osm.organizationCode = opl.organizationCode "
                    + "				left join organizationServiceMapping as map on osm.code = map.organizationServiceCode "
                    + "				left join organizationPriceListRoomDetail as oplrd on opl.code = oplrd.oplCode and osm.code = oplrd.organizationServiceCode "
                    + "				where (osm.organizationCode = :organizationCode) and (map.serviceCode is not null) "
                    + " ) as temp "
                    + " order by temp.organizationCode, temp.roomCategoryCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            psTemp.setString("rateListCode", DmShared.foriegnKey(rateListCode));
            psTemp.setString("entryFrom", DmShared.iNull(PriceListEntryFrom.ORGANIZATION_MASTER.toInt()).toString());
            psTemp.setString("nature", DmShared.iNull(PriceListNature.None.getId()).toString());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public static void saveMappedServices(String organizationServiceCode, String serviceCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Delete records from organizationServiceMapping >====================================*/
            sqlQuery = "Delete from organizationServiceMapping Where organizationServiceCode = :organizationServiceCode ";
//            sqlQuery = "Delete from organizationServiceMapping Where organizationServiceCode = :organizationServiceCode and serviceCode in (" + serviceCode + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationServiceCode", DmShared.iNull(organizationServiceCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            String[] codes = DmShared.xNull(serviceCode).trim().split(",", -1);

            for (String code : codes) {
                if (DmShared.iNull(code.trim()).longValue() != 0) {
                    /*====================================< Insert record into organizationServiceMapping >====================================*/
                    sqlQuery = "Insert into organizationServiceMapping (organizationServiceCode, serviceCode) values (:organizationServiceCode, :serviceCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("organizationServiceCode", DmShared.iNull(organizationServiceCode.trim()).toString());
                    psTemp.setString("serviceCode", DmShared.iNull(code.trim()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public static String[] getMappedServices(String organizationServiceCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String serivceCode = "", serviceName = "";
        String[] services = {"", ""};

        try {
            sqlQuery = "select	osm.serviceCode, psm.name as serviceName "
                    + "		from organizationServiceMapping as osm "
                    + "		left join patientServiceMast as psm on osm.serviceCode = psm.code "
                    + "		where osm.organizationServiceCode = :organizationServiceCode "
                    + "		order by psm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationServiceCode", DmShared.iNull(organizationServiceCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                serivceCode += DmShared.iNull(rsTemp.getString("serviceCode")).toString() + ",";
                serviceName += DmShared.xNull(rsTemp.getString("serviceName")).trim() + ",";
            }
            psTemp.close();
            rsTemp.close();

            if (serivceCode.trim().contains(",")) {
                serivceCode = serivceCode.substring(0, (serivceCode.length() - 1));
            }
            if (serviceName.trim().contains(",")) {
                serviceName = serviceName.substring(0, (serviceName.length() - 1));
            }

            services[0] = serivceCode;
            services[1] = serviceName;
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return services;
    }

    @Override
    public DmResult<List<GrdOrganizationDetail_Mdl>> getOrganizationDetail(String organizationCode) {
        DmResult<List<GrdOrganizationDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        List<GrdOrganizationDetail_Mdl> nodeList = new ArrayList<GrdOrganizationDetail_Mdl>();
        dmrTemp.setData(nodeList);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            nodeList = getOrganizationDetail(DmShared.iNull(organizationCode).toString(), "", cnTemp);

            dmrTemp.setData(nodeList);
            cnTemp.close();
            dmrTemp.setStatus(true);
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

    private List<GrdOrganizationDetail_Mdl> getOrganizationDetail(String organizationCode, String oplCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdOrganizationDetail_Mdl> nodeList = new ArrayList<GrdOrganizationDetail_Mdl>();
        GrdOrganizationDetail_Mdl node;

        try {
            sqlQuery = "select	opl.code, opl.applicableFromDate, opl.applicableToDate  "
                    + "		from OrganizationPriceList opl "
                    + "		where opl.organizationCode = :organizationCode and opl.code <> :oplCode "
                    + "		order by opl.applicableFromDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            psTemp.setString("oplCode", DmShared.iNull(oplCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdOrganizationDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFromDate")));
                node.setApplicableTo(DmShared.dNull(rsTemp.getDate("applicableToDate")));
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
    public DmResult<KeyFieldExtended> downloadCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        KeyFieldExtended field = null;
        String directoryPath, serverFileName = "", fullFilePath;
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handelFile = null;
        char separator = File.separatorChar;
        ImportData_Mdl mrTemp = new ImportOrganizationRateList().getImportDataModel();
        HashMap<String, String> hmCSV = null;
        ZipUtils zipUtils = null;
        try {
            fileName = "organizationRateList";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            directoryPath = DmServer.Path.sessions + separator + sessionId + separator + "organizationRateList";
            new File(directoryPath).mkdir();
            hmCSV = ImportData_Server.getCSVFormats(mrTemp.getHmCSV());

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  opl.organizationCode, max(org.name) as organization, max(opl.applicableFromDate) as applicableFromDate "
                    + "		from organizationPriceList opl "
                    + "		left join subGroup org on org.subCode = opl.organizationCode "
                    + "		where opl.applicableFromDate <= cast(:uEntDt as date)"
                    + "		group by opl.organizationCode "
                    + "		order by max(org.name), max(opl.applicableFromDate) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                fileName = DmShared.xNull(rsTemp.getString("organization")).trim().replace(" ", "_") + "_RateList";
                serverFileName = fileName + ".csv";
                fullFilePath = directoryPath + separator + serverFileName;
                handelFile = new DmHandelFile(fullFilePath, true);
                handelFile.writeLine(hmCSV.get("header"));

                sqlQuery = "select  osm.organizationServiceName, osm.organizationManualCode, osm.organizationServiceGroupName, "
                        + "         ord.rate, psm.name as mapService "
                        + "         from organizationPriceList opl "
                        + "         left join organizationServiceMast osm on osm.organizationCode = opl.organizationCode "
                        + "         left join organizationServiceMapping map on map.organizationServiceCode = osm.code "
                        + "         left join patientServiceMast psm on psm.code = map.serviceCode "
                        + "         left join organizationPriceListRoomDetail ord on ord.organizationServiceCode = osm.code "
                        + "         where opl.organizationCode = :organization and opl.applicableFromDate = :applicableFromDate "
                        + "         order by  osm.organizationServiceName, psm.name ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("organization", DmShared.xNull(rsTemp.getString("organizationCode")));
                psTemp1.setDate("applicableFromDate", rsTemp.getDate("applicableFromDate"));
                rsTemp1 = psTemp1.executeQuery();
                lineData = "";
                while (rsTemp1.next()) {
                    lineData = "\"" + DmShared.xNull(rsTemp1.getString("organizationServiceName")) + "\"";
                    lineData += commaDelimiter;
                    lineData += "\"" + DmShared.xNull(rsTemp1.getString("organizationManualCode")) + "\"";
                    lineData += commaDelimiter;
                    lineData += "\"" + DmShared.xNull(rsTemp1.getString("organizationServiceGroupName")) + "\"";
                    lineData += commaDelimiter;
                    lineData += "\"" + DmShared.vNull(rsTemp1.getString("rate")).doubleValue() + "\"";
                    lineData += commaDelimiter;
                    lineData += "\"" + DmShared.xNull(rsTemp1.getString("mapService")) + "\"";
                    handelFile.writeLine(lineData);
                }
                psTemp1.close();
                rsTemp1.close();
                handelFile.close();
            }
            psTemp.close();
            rsTemp.close();

            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            if (DmShared.xNull(lineData).trim().equalsIgnoreCase("") && hmCSV != null) {
                serverFileName = fileName + ".csv";
                fullFilePath = directoryPath + separator + serverFileName;
                handelFile = new DmHandelFile(fullFilePath, true);
                handelFile.writeLine(hmCSV.get("header"));
                handelFile.writeLine(hmCSV.get("sample"));
                handelFile.writeLine("");
                handelFile.writeLine("");
                handelFile.writeLine("");
                handelFile.writeLine(hmCSV.get("instruction"));
                handelFile.close();
                field.setSharedKeyFieldName(serverFileName);
            } else {
                zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "organizationRateList.zip");
                zipUtils.zipIt();
                field.setSharedKeyFieldName("organizationRateList.zip");
            }

            dmrTemp.setData(field);
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
                if (handelFile != null) {
                    handelFile.close();
                }
                if (zipUtils != null) {
                    zipUtils = null;
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }
}
