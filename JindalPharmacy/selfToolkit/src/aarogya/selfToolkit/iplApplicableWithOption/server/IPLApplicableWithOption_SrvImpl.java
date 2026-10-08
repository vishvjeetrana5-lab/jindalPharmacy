package aarogya.selfToolkit.iplApplicableWithOption.server;

import aarogya.selfToolkit.iplApplicableWithOption.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.iplApplicableWithOption.shared.RateList_Mdl;
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
import aarogya.selfToolkit.iplApplicableWithOption.shared.IPLApplicableWithOption_Srv;

public class IPLApplicableWithOption_SrvImpl extends DmRSSPrivate implements IPLApplicableWithOption_Srv {

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
                case PRICELIST_MASTER:
                    sqlCondition = "and priceListCode = :priceListCode ";
                    break;
                default:
                    throw new DmException(0, "Price List: Entry From not found.");
            }

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select applicableFrom,applicableTo "
                    + "from priceList "
                    + "where entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + sqlCondition
                    + "group by applicableFrom,applicableTo "
                    + "order by applicableFrom desc ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("priceListCode", rateListCode);
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
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, keyFieldName = "";

        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            switch (priceListEntryFrom) {
                case PARTY_PRICELIST:
                    keyFieldName = "subCode";
                    break;
                case PRICELIST_MASTER:
                    keyFieldName = "itemCode";
                    break;
                default:
                    throw new DmException(0, "Price List: Entry From not found.");
            }

            if (DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) {
                sqlQuery = "select DISTINCT " + keyFieldName + " AS keyCode \n"
                        + "from priceList \n"
                        + "where applicableFrom = :applicableFrom \n"
                        + "and entryFrom = :entryFrom \n"
                        + "and site_Code = :site_Code \n"
                        + "and priceListCode = :priceListCode \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
                psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                psTemp.setString("site_Code", site_Code);
                psTemp.setString("priceListCode", rateListCode);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    STServer.deletePriceList(applicableDate, priceListEntryFrom, rsTemp.getString("keyCode"), rateListCode, site_Code, cnTemp);
                }
                rsTemp.close();
                psTemp.close();
            } else {
                STServer.deletePriceList(applicableDate, priceListEntryFrom, keyCode, rateListCode, site_Code, cnTemp);
            }

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
            if (site_Code.equalsIgnoreCase("")) {
                throw new DmException(0, "Please Select Site.");
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select applicableFrom,applicableTo "
                    + "from priceList "
                    + "where entryFrom = '" + String.valueOf(priceListEntryFrom.toInt()) + "' "
                    + "and site_Code = '" + site_Code + "' "
                    + "and priceListCode = '" + rateListCode + "' "
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
            sqlWithParam.setSQL("SELECT name, code, priceListType FROM priceListMast ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new RateList_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setRateListTypeEnum(PriceListType.getViaKey(rsTemp.getInt("priceListType")));
                listNode.setRateListType(PriceListType.getViaKey(rsTemp.getInt("priceListType")).getValue());
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
                    + "FROM priceList "
                    + "WHERE priceListCode= :code "
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
                    + "SELECT MAX(pl.applicableFrom) AS AppFormDate  "
                    + "FROM priceListMast plm    "
                    + "LEFT JOIN priceList pl ON plm.code=pl.priceListCode  "
                    + "WHERE pl.EntryFrom = " + priceListEntryFrom.toInt() + " AND pl.site_code = '" + site_Code + "' AND pl.applicableFrom <= :uEntDt) as tab1     ";
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
                sqlQuery = "SELECT MAX(plm.CODE) AS CODE, MAX(plm.NAME) AS NAME,MAX(pl.applicableFrom) AS AppFormDate "
                        + " FROM priceListMast plm   "
                        + " LEFT JOIN priceList pl ON plm.code=pl.priceListCode "
                        //                       + " Left Join subGroup sg on om.subCode=sg.subCode "
                        + " WHERE pl.EntryFrom = '" + String.valueOf(priceListEntryFrom.toInt()) + "' AND pl.site_code = '" + site_Code + "'  AND  plm.CODE='" + rateListCode + "' ";
            } else {
                if (cpstatusFlag) {
                    sqlQuery = "SELECT MAX(plm.CODE) AS CODE, MAX(plm.NAME) AS NAME,MAX(pl.applicableFrom) AS AppFormDate "
                            + " FROM priceListMast plm    "
                            + " LEFT JOIN priceList pl ON plm.code=pl.priceListCode "
                            //                            + " Left Join subGroup sg on om.subCode=sg.subCode "
                            + " WHERE pl.EntryFrom = '" + String.valueOf(priceListEntryFrom.toInt()) + "' AND pl.site_code = '" + site_Code + "' AND pl.applicableFrom <= :applicableDate ";
//                            + "AND  om.shortName='CP'     ";
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
//                throw new DmException(0, "Please Select Rate List");
//            }

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select pl.site_Code,MAX(sm.name)AS name "
                    + " from priceList pl"
                    + " LEFT JOIN siteMast AS sm ON sm.code=pl.site_Code"
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
    public DmResult<String> downloadCSV(List<KeyFieldExtended> lstPriceList, String siteCode) {
        DmResult<String> dmrTemp = new DmResult<String>();
        String downloadFileName, downloadDirectoryPath, fileFullPath, sessionId, sqlQuery, sqlQueryPriceList = "", joinPriceList = "";
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "", fileHeader = "";
        FileWriter fileWriter = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String priceListCodeCsv = "", condition = "";
        boolean flag = false;
        try {
            for (KeyFieldExtended grdMdl : lstPriceList) {
                if (DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (!priceListCodeCsv.trim().equalsIgnoreCase("")) {
                        priceListCodeCsv += ",";
                    }
                    priceListCodeCsv += DmShared.iNull(grdMdl.getSharedKeyFieldCode()).toString();

                    if (!DmShared.dNull(grdMdl.getSharedModifiedBy()).equalsIgnoreCase("")) {
                        flag = true;
                        if (!sqlQueryPriceList.trim().equalsIgnoreCase("")) {
                            sqlQueryPriceList += " UNION ALL \n";
                        }
                        sqlQueryPriceList += "			SELECT	pl.priceListCode, pl.itemCode, pl.rate, pl.discountPer, pl.discount\n"
                                + "			FROM priceList pl\n"
                                + "			WHERE pl.entryFrom = :entryFrom \n"
                                + "				AND pl.site_Code = :siteCode \n"
                                + "				AND pl.priceListCode = " + DmShared.iNull(grdMdl.getSharedKeyFieldCode()).toString() + " \n"
                                + "				AND pl.applicableFrom = '" + DmServer.convertDate(grdMdl.getSharedModifiedBy()) + "' \n";
                    }
                }
            }
            if (!priceListCodeCsv.trim().equalsIgnoreCase("")) {
                condition = " and plm.code in (" + priceListCodeCsv + ")";
            }

            if (!flag) {
                sqlQueryPriceList = "			SELECT	NULL AS priceListCode, NULL AS itemCode, 0 AS rate, 0 AS discountPer, 0 AS discount\n";
            }

            if (!sqlQueryPriceList.trim().equalsIgnoreCase("")) {
                joinPriceList = "LEFT JOIN (\n" + sqlQueryPriceList
                        + "		) as vPl on vPl.itemCode = ipl.itemCode AND vPl.priceListCode = ipl.priceListCode\n";
            }

            downloadFileName = "PriceList.csv";
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            downloadDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "downloads";

            new File(downloadDirectoryPath).mkdirs();
            fileFullPath = downloadDirectoryPath + File.separatorChar + downloadFileName;

            fileWriter = new FileWriter(fileFullPath);

            fileHeader = "priceList,itemName,rate,discountPer,discount";
            fileWriter.append(fileHeader);
            fileWriter.append(NEW_LINE_SEPARATOR);

            sqlQuery = "SELECT ipl.itemCode, ipl.itemName\n"
                    + "	, ipl.priceListCode, ipl.priceListName\n"
                    + "	, vPl.rate, vPl.discountPer, vPl.discount\n"
                    + "FROM (\n"
                    + "	SELECT im.code AS itemCode, im.name as itemName\n"
                    + "		, vPlm.priceListCode, vPlm.priceListName\n"
                    + "	FROM itemMast im\n"
                    + "	LEFT JOIN itemSiteList isl ON isl.code = im.code\n"
                    + "	LEFT JOIN (\n"
                    + "				SELECT  plm.code as priceListCode, plm.name as priceListName\n"
                    + "				FROM priceListMast plm\n"
                    + "				WHERE 1 = 1   \n" + condition
                    + "			) vPlm ON 1 = 1\n"
                    + "	WHERE im.isActive = 1 \n"
                    + "		AND (im.filterItemType = :filterItemType OR im.isCommon = 1) and isl.site_Code = :siteCode \n"
                    + "	) ipl\n" + joinPriceList
                    + "ORDER BY ipl.itemName, ipl.priceListName ";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            if (flag) {
                psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("priceListName")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemName")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("rate")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("discountPer")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("discount")).trim() + "\"";
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
