package aarogya.pharmacy.medicineGroupMaster.server;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.medicineGroupMaster.shared.*;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stImportData.server.MedicineGroup;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineGroupMaster_SrvImpl extends DmRSSPrivate implements MedicineGroupMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT CODE FROM itemGroup WHERE filterItemType = :filterItemType ORDER BY NAME ");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
            sqlWithParam.setSQL("Select Name From itemGroup");
            helper.addSort("Name", DmEnum.DmSortType.ASC);
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
    public DmResult<MedicineGroupMaster_Mdl> getMoveRec(String strKey) {
        DmResult<MedicineGroupMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        MedicineGroupMaster_Mdl mrTemp = new MedicineGroupMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select ig.code, ig.name,ig.saleDiscount, ig.preparedBy, ig.u_name,ig.isCommon, "
                    + " ig.purchaseAc AS purchaseAcCode ,ig.purchaseReturnAc AS purchaseReturnAcCode ,ig.salesAc as salesAcCode, ig.salesReturnAc as salesReturnAcCode,\n"
                    + " ig.subCode as inventoryAc,sgp.name as purchaseAcName, sgpr.name as purchaseReturnAcName,sgs.name as salesAcName, sgsr.name as salesReturnAcName,sgi.name as inventoryAcName,\n"
                    + " ig.grIrAc AS grIrAcCode ,ig.consumptionAc AS consumptionAcCode ,ig.cogsAc as cogsAcCode, ig.pidAc as pidAcCode,ig.inTransitAc as inTransitAcCode,\n"
                    + " sgIrGr.name as irGrAcName, sgC.name as consumptionAcName,sgCOG.name as cogsAcName, sgPID.name as pidAcName,sgTrn.name as inTransitAcName \n"
                    + " From itemGroup as ig "
                    + " left join subgroup AS sgp ON ig.purchaseAc = sgp.subCode"
                    + " left join subgroup AS sgpr ON ig.purchaseReturnAc = sgpr.subCode"
                    + " left join subgroup AS sgs ON ig.salesAc = sgs.subCode"
                    + " left join subgroup AS sgsr ON ig.salesReturnAc = sgsr.subCode"
                    + " left join subgroup AS sgi ON ig.subCode = sgi.subCode"
                    + " left join subgroup AS sgIrGr ON ig.grIrAc = sgIrGr.subCode"
                    + " left join subgroup AS sgC ON ig.consumptionAc = sgC.subCode"
                    + " left join subgroup AS sgCOG ON ig.cogsAc = sgCOG.subCode"
                    + " left join subgroup AS sgPID ON ig.pidAc = sgPID.subCode"
                    + " left join subgroup AS sgTrn ON ig.inTransitAc = sgTrn.subCode"
                    + " Where ig.code= :code and ig.filterItemType = :filterItemType  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));

            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSaleDiscount(DmServer.vNull(rsTemp.getString("saleDiscount"), "0.00"));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setIsCommonTag(DmShared.xNull(rsTemp.getString("isCommon")));
                if (DmShared.iNull(rsTemp.getString("isCommon")).intValue() == 0) {
                    mrTemp.setIsCommonText("No");
                } else if (DmShared.iNull(rsTemp.getString("isCommon")).intValue() == 1) {
                    mrTemp.setIsCommonText("Yes");
                }
                mrTemp.setInventoryAcTag(DmShared.xNull(rsTemp.getString("inventoryAc")));
                mrTemp.setInventoryAcText(DmShared.xNull(rsTemp.getString("inventoryAcName")));
                mrTemp.setPurchaseAcTag(DmShared.xNull(rsTemp.getString("purchaseAcCode")));
                mrTemp.setPurchaseAcText(DmShared.xNull(rsTemp.getString("purchaseAcName")));
                mrTemp.setSalesAcTag(DmShared.xNull(rsTemp.getString("salesAcCode")));
                mrTemp.setSalesAcText(DmShared.xNull(rsTemp.getString("salesAcName")));
                mrTemp.setPurchaseReturnAcTag(DmShared.xNull(rsTemp.getString("purchaseReturnAcCode")));
                mrTemp.setPurchaseReturnAcText(DmShared.xNull(rsTemp.getString("purchaseReturnAcName")));
                mrTemp.setSalesReturnAcTag(DmShared.xNull(rsTemp.getString("salesReturnAcCode")));
                mrTemp.setSalesReturnAcText(DmShared.xNull(rsTemp.getString("salesReturnAcName")));

                mrTemp.setGrIrAcTag(DmShared.xNull(rsTemp.getString("grIrAcCode")));
                mrTemp.setGrIrAcText(DmShared.xNull(rsTemp.getString("irGrAcName")));
                mrTemp.setConsumptionAcTag(DmShared.xNull(rsTemp.getString("consumptionAcCode")));
                mrTemp.setConsumptionAcText(DmShared.xNull(rsTemp.getString("consumptionAcName")));
                mrTemp.setCogsAcTag(DmShared.xNull(rsTemp.getString("cogsAcCode")));
                mrTemp.setCogsAcText(DmShared.xNull(rsTemp.getString("cogsAcName")));
                mrTemp.setPidAcTag(DmShared.xNull(rsTemp.getString("pidAcCode")));
                mrTemp.setPidAcText(DmShared.xNull(rsTemp.getString("pidAcName")));
                mrTemp.setInTransitAcTag(DmShared.xNull(rsTemp.getString("inTransitAcCode")));
                mrTemp.setInTransitAcText(DmShared.xNull(rsTemp.getString("inTransitAcName")));
            }
            psTemp.close();
            rsTemp.close();
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
    public DmResult<String> deleteRecord(String strKey) {
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

            sqlQuery = "Delete From itemGroup Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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
    public DmResult<String> saveRecord(MedicineGroupMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterItemType = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            MedicineGroupMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "itemGroup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            filterItemType = DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp);
            sqlQuery = "  SELECT "
                    + "   name "
                    + " , code "
                    + " , filterItemType "
                    + " FROM itemGroup "
                    + " WHERE LOWER(name) = :name "
                    + " AND code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("name")).equalsIgnoreCase("")) {
                    if (DmShared.xNull(rsTemp.getString("filterItemType")).equalsIgnoreCase(filterItemType)) {
                        throw new UnsupportedOperationException("Medicine Group already exists.");
                    } else {
                        mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                        mrTemp.setIsCommonTag("1");
                        blnAdd = false;
                    }
                }
            }

            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "Insert Into itemGroup(code ,name,saleDiscount, "
                        + " preparedBy, u_EntDt, filterItemType,isCommon,purchaseAc,salesAc,subCode, purchaseReturnAc , salesReturnAc,"
                        + " grIrAc,consumptionAc,cogsAc,pidAc,inTransitAc) Values "
                        + "(:code ,:name, :saleDiscount, "
                        + ":preparedBy, :uEntDt, :filterItemType,:isCommon,:purchaseAc,:salesAc,:subCode, :purchaseReturnAc,:salesReturnAc, "
                        + ":grIrAc,:consumptionAc,:cogsAc,:pidAc,:inTransitAc ) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("isCommon", DmShared.xNull(mrTemp.getIsCommonTag()));
                psTemp.setString("saleDiscount", DmServer.vNull(mrTemp.getSaleDiscount(), "0.00"));
                psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));

                psTemp.setString("grIrAc", DmShared.foriegnKey(mrTemp.getGrIrAcTag()));
                psTemp.setString("consumptionAc", DmShared.foriegnKey(mrTemp.getConsumptionAcTag()));
                psTemp.setString("cogsAc", DmShared.foriegnKey(mrTemp.getCogsAcTag()));
                psTemp.setString("pidAc", DmShared.foriegnKey(mrTemp.getPidAcTag()));
                psTemp.setString("inTransitAc", DmShared.foriegnKey(mrTemp.getInTransitAcTag()));
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = " Update itemGroup Set "
                        + " name = :name, "
                        + " saleDiscount = :saleDiscount, "
                        + " U_Name = :u_Name, "
                        + " filterItemType = :filterItemType, "
                        + " isCommon = :isCommon, "
                        + " U_EntDt = :uEntDt, "
                        + " purchaseAc = :purchaseAc, "
                        + " salesAc = :salesAc, "
                        + " subCode = :subCode, "
                        + " purchaseReturnAc= :purchaseReturnAc, "
                        + " salesReturnAc = :salesReturnAc, "
                        + " grIrAc = :grIrAc, "
                        + " consumptionAc = :consumptionAc, "
                        + " cogsAc = :cogsAc, "
                        + " pidAc= :pidAc, "
                        + " inTransitAc = :inTransitAc "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("saleDiscount", DmServer.vNull(mrTemp.getSaleDiscount(), "0.00"));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("isCommon", DmShared.xNull(mrTemp.getIsCommonTag()));
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));

                psTemp.setString("grIrAc", DmShared.foriegnKey(mrTemp.getGrIrAcTag()));
                psTemp.setString("consumptionAc", DmShared.foriegnKey(mrTemp.getConsumptionAcTag()));
                psTemp.setString("cogsAc", DmShared.foriegnKey(mrTemp.getCogsAcTag()));
                psTemp.setString("pidAc", DmShared.foriegnKey(mrTemp.getPidAcTag()));
                psTemp.setString("inTransitAc", DmShared.foriegnKey(mrTemp.getInTransitAcTag()));
                psTemp.executeUpdate();
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT "
                    + "   code"
                    + " , name"
                    + " , saleDiscount "
                    + " , CASE WHEN isCommon = 1 then 'Yes' ELSE 'No' END as commonText"
                    + " , isCommon "
                    + " FROM itemGroup "
                    + " WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setIsCommon(DmShared.xNull(rsTemp.getString("isCommon")));
                listNode.setIsCommonText(DmShared.xNull(rsTemp.getString("commonText")));
                listNode.setSaleDiscount(DmServer.vNull(rsTemp.getString("saleDiscount"), "0.00"));
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = ImportData_Server.getCSVFormats(new MedicineGroup().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "Medicine Group Master";
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
            sqlQuery = "Select ig.name as medicineGroup, ig.saleDiscount, "
                    + "         case ig.isCommon when 1 then 'Yes' else 'No' end as isCommon "
                    + " From itemGroup as ig "
                    + " WHERE ig.filterItemType = :filterItemType  "
                    + " order by ig.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("medicineGroup")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmServer.vNull(rsTemp.getString("saleDiscount"), "0.00") + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isCommon")) + "\"";
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCode(String subgroupNature, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String Condition = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                Condition = " and sg.nature = :nature ";
            }

            sqlWithParam.setSQL(" SELECT sg.subCode, sg.name, sg.manualCode "
                    + " FROM subGroup sg where sg.isActive = 1 "
                    + Condition);
            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("nature", DmShared.xNull(DmServer.getCodeViaManualCode(subgroupNature, "manualCode", "subGroupNature", "code", cnTemp)));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
