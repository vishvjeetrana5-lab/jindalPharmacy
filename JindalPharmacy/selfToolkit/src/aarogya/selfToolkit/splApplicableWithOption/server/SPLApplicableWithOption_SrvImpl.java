package aarogya.selfToolkit.splApplicableWithOption.server;

import aarogya.selfToolkit.splApplicableWithOption.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SPLApplicableWithOption_SrvImpl extends DmRSSPrivate implements SPLApplicableWithOption_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getApplicableDateList(PriceListEntryFrom priceListEntryFrom, String keyCode, String rateListCode, String site_Code) {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", sqlCondition = "";
        DmPreparedStatement psTemp = null;

        try {

            switch (priceListEntryFrom) {
                case CONSULTANT_MASTER:
                    sqlCondition = "and consultantCode = :keyCode ";
                    break;
                case RATE_LIST_MASTER:
                    sqlCondition = "and rateListCode = :keyCode ";
                    break;
                default:
                    throw new DmException(0, "Price List: Entry From not found.");
            }

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select applicableFrom,applicableTo "
                    + "from patientServicePriceList "
                    + "where entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and rateListCode = :rateListCode "
                    + sqlCondition
                    + "group by applicableFrom,applicableTo "
                    + "order by applicableFrom desc ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("rateListCode", rateListCode);
            psTemp.setString("keyCode", keyCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableTo")));
                lstTemp.add(listNode);
            }
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
    public DmResult<String> deleteApplicableDate(String applicableDate, PriceListEntryFrom priceListEntryFrom, String keyCode, String rateListCode, String site_Code) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            STServer.deletePriceList(applicableDate, priceListEntryFrom, keyCode, rateListCode, site_Code, cnTemp);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCopyRateDate(PriceListEntryFrom priceListEntryFrom, String keyCode, String rateListCode, String site_Code, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", sqlCondition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            if (rateListCode.equalsIgnoreCase("")) {
                throw new DmException(0, "Please Select Rate List.");
            }
            if (site_Code.equalsIgnoreCase("")) {
                throw new DmException(0, "Please Select Site.");
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select applicableFrom,applicableTo "
                    + "from patientServicePriceList "
                    + "where entryFrom = '" + String.valueOf(priceListEntryFrom.toInt()) + "' "
                    + "and site_Code = '" + site_Code + "' "
                    + "and rateListCode = '" + rateListCode + "' "
                    + sqlCondition
                    + "group by applicableFrom,applicableTo ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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

//   @Override
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
    public DmResult<DefaultValue_Mdl> getDefaultValue(PriceListEntryFrom priceListEntryFrom, String site_Code, String rateListCode) {
        DmResult<DefaultValue_Mdl> rstTemp = new DmResult<>();
        DefaultValue_Mdl lstTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        boolean flag = false, cpstatusFlag = false;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT COUNT(*) AS Cnt "
                    + "FROM ("
                    + "SELECT applicableFrom  "
                    + "FROM patientservicepricelist "
                    + "WHERE rateListCode= :code "
                    + "and entryFrom = :entryFrom "
                    + "GROUP BY applicableFrom"
                    + ")AS maintTB ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", rateListCode);
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                flag = true;
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select Count(AppFormDate) as cnt from ( "
                    + "SELECT MAX(PSPL.applicableFrom) AS AppFormDate  "
                    + "FROM rateListMast OM    "
                    + "LEFT JOIN patientservicepricelist PSPL ON OM.code=PSPL.rateListCode  "
                    + "WHERE PSPL.EntryFrom = " + priceListEntryFrom.toInt() + " AND PSPL.site_code = '" + site_Code + "' AND PSPL.applicableFrom <= :uEntDt) as tab1     ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                cpstatusFlag = true;
            }
            rsTemp.close();
            psTemp.close();

            if (flag) {
                sqlQuery = "SELECT MAX(OM.CODE) AS CODE, MAX(OM.NAME) AS NAME,MAX(PSPL.applicableFrom) AS AppFormDate "
                        + " FROM rateListMast OM    "
                        + " LEFT JOIN patientservicepricelist PSPL ON OM.code=PSPL.rateListCode "
                        //                        + " Left Join subGroup sg on om.subCode=sg.subCode "
                        + " WHERE PSPL.EntryFrom = '" + String.valueOf(priceListEntryFrom.toInt()) + "' AND PSPL.site_code = '" + site_Code + "'  AND  om.CODE='" + rateListCode + "' ";
            } else {
                if (cpstatusFlag) {
                    sqlQuery = "SELECT MAX(OM.CODE) AS CODE, MAX(OM.NAME) AS NAME,MAX(PSPL.applicableFrom) AS AppFormDate "
                            + " FROM rateListMast OM    "
                            + " LEFT JOIN patientservicepricelist PSPL ON OM.code=PSPL.organizationCode "
                            //                            + " Left Join subGroup sg on om.subCode=sg.subCode "
                            + " WHERE PSPL.EntryFrom = '" + String.valueOf(priceListEntryFrom.toInt()) + "' AND PSPL.site_code = '" + site_Code + "' AND PSPL.applicableFrom <= :applicableDate ";
//                            + " AND  om.shortName='CP'     ";
                }
            }
            if (flag || cpstatusFlag) {
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (cpstatusFlag && !flag) {
                    psTemp.setDate("applicableDate", DmServer.convertDate(DmServer.getServerDateTime()[0]));
                }
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    lstTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                    lstTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                    lstTemp.setCopyRateDate(DmShared.dNull(rsTemp.getDate("AppFormDate")));
                }
                rsTemp.close();
                psTemp.close();
                sqlQuery = "select sm.code,sm.name AS site from siteMast sm where code = :site_Code  ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {

                    lstTemp.setSiteCode(DmShared.xNull(rsTemp.getString("code")));
                    lstTemp.setSiteName(DmShared.xNull(rsTemp.getString("site")));

                }
                rsTemp.close();
                psTemp.close();
            }
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
    public DmResult<String> orgCurrentApplicableDate(PriceListEntryFrom priceListEntryFrom, String site_Code, String rateListMast) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        boolean flag = false;
        try {
            rstTemp.setData("");
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT  "
                    + "    MAX(OM.CODE) AS CODE, "
                    + "    MAX(OM.NAME) AS NAME, "
                    + "    MAX(PSPL.applicableFrom) AS AppFormDate  "
                    + "    FROM "
                    + "    rateListMast OM  "
                    + "    LEFT JOIN patientservicepricelist PSPL  "
                    + "    ON OM.code = PSPL.rateListCode "
                    + "    WHERE  PSPL.EntryFrom = :EntryFrom  "
                    + "    AND PSPL.site_code = :site_code  "
                    + "    AND OM.CODE = :rateListMast  "
                    + "    GROUP BY OM.CODE";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("rateListMast", rateListMast);
            psTemp.setString("site_code", site_Code);
            psTemp.setString("EntryFrom", String.valueOf(priceListEntryFrom.toInt()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                rstTemp.setData(DmShared.dNull(rsTemp.getDate("AppFormDate")));
            }

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(PriceListEntryFrom priceListEntryFrom, String rateListCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
//            if (rateListCode.equalsIgnoreCase("")) {
//                throw new DmException(0, "Please Select Rate List.");
//            }

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select pspl.site_Code,MAX(sm.name)AS name "
                    + " from patientServicePriceList pspl"
                    + " LEFT JOIN siteMast AS sm ON sm.code=pspl.site_Code"
                    + " where entryFrom = '" + String.valueOf(priceListEntryFrom.toInt()) + "' "
                    + " group by site_Code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_Code")));
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
    public DmResult<String> downloadCSV(List<KeyFieldExtended> lstOrganization, String siteCode) {
        DmResult<String> dmrTemp = new DmResult<String>();
        String downloadFileName, downloadDirectoryPath, fileFullPath, sessionId, sqlQuery;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "", fileHeader = "";
        FileWriter fileWriter = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String filterServiceNature = "", organizations = "", organizationCode = "", applicableDate = "", condition = "";
        String osmQuery = "", psplQuery = "";
        boolean flag = false;
        try {
            for (KeyFieldExtended grdMdl : lstOrganization) {
                flag = false;
                if (DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (organizations.equalsIgnoreCase("")) {
                        organizations = DmShared.iNull(grdMdl.getSharedKeyFieldCode()).toString();
                        organizationCode = DmShared.iNull(grdMdl.getSharedKeyFieldCode()).toString();
                        applicableDate = DmShared.dNull(grdMdl.getSharedModifiedBy());
                    } else {
                        organizations += "," + DmShared.iNull(grdMdl.getSharedKeyFieldCode()).toString();
                        flag = true;
                    }
                    if (flag) {
                        osmQuery += " union all "
                                + "   select  max(osm.organizationCode) as organizationCode, osp.serviceCode, osm.code as orgServiceCode, "
                                + "           max(osm.organizationServiceName) as organizationServiceName "
                                + "           from organizationServiceMast osm "
                                + "           left join organizationServiceMapping osp on osp.organizationServiceCode = osm.code "
                                + "           where osm.organizationCode =  " + DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue()
                                + "           group by osp.serviceCode, osm.code ";

                        psplQuery += " union all "
                                + "    select pspl.rateListCode as organizationCode, pspl.serviceCode, isNull(pspl.organizationServiceCode,0) as organizationServiceCode, "
                                + "           isNull(pspl.roomCatCode,-1) as roomCatCode, pspl.rate, pspl.discountPer, pspl.discount "
                                + "           from patientServicePriceList pspl "
                                + "           where pspl.entryFrom = :entryFrom and pspl.rateListCode = " + DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue()
                                + "             and pspl.site_Code = :siteCode and pspl.applicableFrom = '" + DmServer.convertDate(grdMdl.getSharedModifiedBy()) + "' ";
                    }
                }
            }
            if (!organizations.equalsIgnoreCase("")) {
                condition = " and rlm.Code in (" + organizations + ")";
            }
            filterServiceNature = DmShared.iNull(ServiceNature.PATHOLOGY_TEST.getId()).toString()
                    + "," + DmShared.iNull(ServiceNature.RADIOLOGY_TEST.getId()).toString()
                    + "," + DmShared.iNull(ServiceNature.SERVICE.getId()).toString()
                    + "," + DmShared.iNull(ServiceNature.PATHOLOGY_PANEL.getId()).toString()
                    + "," + DmShared.iNull(ServiceNature.PACKAGE.getId()).toString()
                    + "," + DmShared.iNull(ServiceNature.OPERATION.getId()).toString();
            downloadFileName = "RateList.csv";
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            downloadDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "downloads";

            new File(downloadDirectoryPath).mkdirs();
            fileFullPath = downloadDirectoryPath + File.separatorChar + downloadFileName;

            fileWriter = new FileWriter(fileFullPath);
            fileHeader = "rateList,serviceName,serviceCategory,serviceNature,roomCategory,rate,discountPer,discount,ServiceShortName,ServiceGroupName";
            fileWriter.append(fileHeader);
            fileWriter.append(NEW_LINE_SEPARATOR);

            sqlQuery = "select  sr.serviceName, sr.roomCatName roomCategory, pscm.name serviceCategory, sr.rateList, pspl.rate, pspl.discountPer, "
                    + "		pspl.discount, sr.serviceNature , sr.ServiceShortName ,psg.name as Service_Group "
                    + "		from ("
                    + "			select  psm.code as serviceCode,psm.name as serviceName,rcm.roomCatCode,rcm.roomCatName, "
                    + "				psm.categoryCode serviceCategoryCode, rcm.rateListCode, rcm.rateList, psm.serviceNature ,"
                    + " psm.shortName as  ServiceShortName"
                    + "				from patientServiceMast psm "
                    + "				left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "				left join ("
                    + "					select  rcm.roomCatCode, rcm.roomCatName, rlm.rateListCode, rlm.rateList"
                    + "						from ("
                    + "							select  rcm.code as roomCatCode,rcm.name as roomCatName "
                    + "								from roomCatMast rcm where isNull(rcm.isSystemdefine,0)=0	"
                    + "							union all "
                    + "							select -1 as roomCatCode,'' as roomCatName"
                    + "						) rcm "
                    + "						left join ("
                    + "							select  rlm.code as rateListCode, rlm.name as rateList "
                    + "								from rateListMast rlm "
                    + "								where 1 = 1  " + condition
                    + "						) rlm on 1 = 1"
                    + "				) as rcm on 1 = 1 "
                    + "				where psm.isActive = 1 and psm.serviceNature in (" + filterServiceNature + ") and pssl.site_Code = :siteCode "
                    + "		) as sr "
                    + "		left join patientServiceCatMast pscm on pscm.code = sr.serviceCategoryCode "
                    + "         left join serviceGroup as psg on psg.code=sr.serviceCode      "
                    + "		left join ("
                    + "			select	pspl.rateListCode, pspl.serviceCode, isNull(pspl.roomCatCode,-1) as roomCatCode, pspl.rate, pspl.discountPer, pspl.discount "
                    + "				from patientServicePriceList pspl "
                    + "				where pspl.entryFrom = :entryFrom and pspl.rateListCode = :organizationCode and pspl.site_Code = :siteCode "
                    + "                             and pspl.applicableFrom = :applicableDate " + psplQuery
                    + "		) as pspl on pspl.serviceCode = sr.serviceCode and pspl.roomCatCode = sr.roomCatCode and isnull(sr.rateListCode, 0) = isnull(pspl.rateListCode, 0) "
                    + "		order by sr.serviceName, sr.roomCatName, sr.rateList ";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableDate", DmServer.convertDate(applicableDate));
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setInt("entryFrom", PriceListEntryFrom.RATE_LIST_MASTER.toInt());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("rateList")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("serviceName")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("serviceCategory")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue()).getValue() + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + DmShared.xNull(rsTemp.getString("organizationServiceName")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("roomCategory")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("rate")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("discountPer")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("discount")).trim() + "\""+ COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ServiceShortName")).trim() + "\""+ COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("Service_Group")).trim() + "\"";
                fileWriter.append(lineData);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            rsTemp.close();
            psTemp.close();

            fileWriter.flush();
            fileWriter.close();
            fileWriter = null;

            dmrTemp.setData(downloadFileName);
            dmrTemp.setValue1(downloadDirectoryPath);
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

}
