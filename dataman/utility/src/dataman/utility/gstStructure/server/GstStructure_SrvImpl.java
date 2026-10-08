package dataman.utility.gstStructure.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.gstStructure.shared.GstStructure_Mdl;

import java.util.List;
import dataman.utility.gstStructure.shared.GstStructure_Vld;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import dataman.utility.gstStructure.shared.GstStructure_Srv;

public class GstStructure_SrvImpl extends DmRSSPrivate implements GstStructure_Srv {
    
    

    @Override
    public DmResultPageing<List<GstStructure_Mdl>> getFillRecords(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GstStructure_Mdl>> rstTemp = new DmResultPageing<>();
        List<GstStructure_Mdl> lstTemp = new ArrayList<>();
        GstStructure_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT vBase.locationCode, vBase.locationName\n"
                    + "	, vBase.gstTypeCode, vBase.gstTypeName, vBase.gstTypeSNo\n"
                    + "	, vBase.gstHeadCode, vBase.gstHeadName, vBase.gstHeadSNo\n"
                    + "	, gsts.atRate,  gsts.purchaseSalesAc, sg.name as purchaseSalesAcName\n"
                    + "	, gsts.inputTaxAc, sgi.name as inputTaxAcName, gsts.outputTaxAc, sgo.name as outputTaxAcName, gsts.rcmAc\n" 
                    + " , sgr.name as rcmAcName, vBase.isTaxHead\n"
                    + "FROM (\n"
                    + "	SELECT l.code as locationCode, l.name as locationName\n"
                    + "	, t.code as gstTypeCode, t.name as gstTypeName, t.printSrNo AS gstTypeSNo\n"
                    + "	, h.code as gstHeadCode, h.name as gstHeadName, h.printSrNo AS gstHeadSNo, h.istaxHead\n"
                    + "	FROM (\n"
                    + "		SELECT "+DmEnum.DmLocation.IntraState.getId()+" as code, '"+DmEnum.DmLocation.IntraState.getValue()+"' AS name\n"
                    + "		UNION\n"
                    + "		SELECT "+DmEnum.DmLocation.InterState.getId()+" as code, '"+DmEnum.DmLocation.InterState.getValue()+"' AS name\n"
                    + "		UNION\n"
                    + "		SELECT "+DmEnum.DmLocation.UnionTerritory.getId()+" as code, '"+DmEnum.DmLocation.UnionTerritory.getValue()+"' AS name\n"
                    + "		UNION\n"
                    + "		SELECT "+DmEnum.DmLocation.International.getId()+" as code, '"+DmEnum.DmLocation.International.getValue()+"' AS name\n"
                    + "		) l\n"
                    + "		, (SELECT code, name, printSrNo FROM gstType) t\n"
                    + "		, (SELECT code, name, printSrNo, isTaxHead FROM gstHead) h\n"
                    + "	) vBase\n"
                    + "LEFT JOIN gsts ON gsts.locationCode = vBase.locationCode\n"
                    + "				AND gsts.gstTypeCode = vBase.gstTypeCode\n"
                    + "				AND gsts.gstHeadCode = vBase.gstHeadCode\n"
                    + "left join subGroup sg on sg.subcode = gsts.purchaseSalesAc\n"
                    + "left join subGroup sgi on sgi.subcode = gsts.inputTaxAc\n"
                    + "left join subGroup sgo on sgo.subcode = gsts.outputTaxAc\n"
                    + "left join subGroup sgr on sgr.subcode = gsts.rcmAc\n";
//                    + "ORDER BY locationCode, gstTypeSNo, gstTypeName, gstHeadSNo, gstHeadName ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GstStructure_Mdl();
                listNode.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
                listNode.setLocationName(DmShared.xNull(rsTemp.getString("locationName")));
                listNode.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                listNode.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                listNode.setGstHeadCode(DmShared.xNull(rsTemp.getString("gstHeadCode")));
                listNode.setGstHeadName(DmShared.xNull(rsTemp.getString("gstHeadName")));
                listNode.setAmount(DmShared.xNull(rsTemp.getString("atRate")));
                listNode.setPurchaseSalesAc(DmShared.xNull(rsTemp.getString("purchaseSalesAc")));
                listNode.setPurchaseSalesAcName(DmShared.xNull(rsTemp.getString("purchaseSalesAcName")));
                listNode.setInputTaxAc(DmShared.xNull(rsTemp.getString("inputTaxAc")));
                listNode.setInputTaxAcName(DmShared.xNull(rsTemp.getString("inputTaxAcName")));
                listNode.setOutputTaxAc(DmShared.xNull(rsTemp.getString("outputTaxAc")));
                listNode.setOutputTaxAcName(DmShared.xNull(rsTemp.getString("outputTaxAcName")));
                listNode.setRcmAc(DmShared.xNull(rsTemp.getString("rcmAc")));
                listNode.setRcmAcName(DmShared.xNull(rsTemp.getString("rcmAcName")));
                listNode.setIsTaxHead(DmShared.xNull(rsTemp.getString("isTaxHead")));
                listNode.setGstTypeSNo(DmShared.xNull(rsTemp.getString("gstTypeSNo")));
                listNode.setGstHeadSNo(DmShared.xNull(rsTemp.getString("gstHeadSNo")));
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
    public DmResult<String> saveRecord(GstStructure_Mdl grdItemTaxStructure) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSavePoint spBegin = null;
        try {
            GstStructure_Vld.chkSave(grdItemTaxStructure);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            
            sqlQuery = "Delete From gsts where locationCode= :locationCode and gstTypeCode= :gstTypeCode and gstHeadCode= :gstHeadCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("locationCode", grdItemTaxStructure.getLocationCode());
            psTemp.setString("gstTypeCode", grdItemTaxStructure.getGstTypeCode());
            psTemp.setString("gstHeadCode", grdItemTaxStructure.getGstHeadCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " INSERT INTO gsts (locationCode, gsttypeCode, gstHeadCode, atRate, purchaseSalesAc, inputTaxAc,outputTaxAc,rcmAc, u_Name, u_EntDt) "
                    + "VALUES (:locationCode, :gsttypeCode, :gstHeadCode, :atRate, :purchaseSalesAc, :inputTaxAc,:outputTaxAc,:rcmAc, :u_Name, :u_EntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("locationCode", DmShared.foriegnKey(grdItemTaxStructure.getLocationCode()));
            psTemp.setString("gsttypeCode", DmShared.foriegnKey(grdItemTaxStructure.getGstTypeCode()));
            psTemp.setString("gstHeadCode", DmShared.foriegnKey(grdItemTaxStructure.getGstHeadCode()));
            psTemp.setString("atRate", DmServer.vNull(grdItemTaxStructure.getAmount(), "0.00"));
            psTemp.setString("purchaseSalesAc", DmShared.foriegnKey(grdItemTaxStructure.getPurchaseSalesAc()));
            psTemp.setString("inputTaxAc", DmShared.foriegnKey(grdItemTaxStructure.getInputTaxAc()));
            psTemp.setString("outputTaxAc", DmShared.foriegnKey(grdItemTaxStructure.getOutputTaxAc()));
            psTemp.setString("rcmAc", DmShared.foriegnKey(grdItemTaxStructure.getRcmAc()));
            psTemp.setString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)).trim());
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(grdItemTaxStructure.getLocationCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            String sqlQuery = " select sg.subCode as code, sg.name as name from subGroup sg ";
            sqlWithParam.setSQL(sqlQuery);
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
            cnTemp.close();
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

}
