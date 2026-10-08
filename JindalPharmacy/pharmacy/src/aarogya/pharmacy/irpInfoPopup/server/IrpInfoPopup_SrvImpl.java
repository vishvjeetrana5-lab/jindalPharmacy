package aarogya.pharmacy.irpInfoPopup.server;

import com.google.gson.reflect.TypeToken;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Srv;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Vld;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IrpInfoPopup_SrvImpl extends DmRSSPrivate implements IrpInfoPopup_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsActive(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIrpType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.IRP_Type irpType : DmEnum.IRP_Type.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(irpType.getCode())));
                listNode.setSharedKeyFieldName(DmShared.xNull(irpType.getValue()));
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
    public DmResultPageing<List<IrpInfoPopup_Mdl>> getTxtHIrpName(String irpTypeCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<IrpInfoPopup_Mdl>> rstTemp = new DmResultPageing<>();
        List<IrpInfoPopup_Mdl> lstTemp = new ArrayList<>();
        IrpInfoPopup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT  irm.code, irm.name \n"
                    + " , irm.isActive \n"
                    + " , CASE WHEN irm.isActive = 1 THEN 'Yes' ELSE 'No' END AS isActiveName \n"
                    + " , irm.url \n"
                    + "	FROM irpMast irm "
                    + " WHERE irm.type = :irpType \n");
            sqlWithParam.setString("irpType", irpTypeCode);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new IrpInfoPopup_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setUrl(DmShared.xNull(rsTemp.getString("url")));
                listNode.setIsActiveCode(DmShared.xNull(rsTemp.getString("isActive")));
                listNode.setIsActiveText(DmShared.xNull(rsTemp.getString("isActiveName")));
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

    @Override
    public DmResult<String> deleteIrpInfo(String code) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        String sqlQuery = "";

        try {
            DmShared.isValidText(code, "IRP Info", 100);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            IrpInfoPopup_Mdl prevRecord = getMoveRec(code, cnTemp);

            sqlQuery = "DELETE FROM irpMast \n"
                    + "where code = :code \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", code);
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(code);
            spBegin.commit();
            dmrTemp.setStatus(true);

            saveIrpInfoLog(code, prevRecord, null, DmEnum.DmTransactionMode.DELETE);

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
    public DmResult<String> saveIrpInfo(IrpInfoPopup_Mdl prevMrTemp, IrpInfoPopup_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        dmrTemp.setData("");
        boolean blnAdd = false;
        try {
            //  =======================================================================================================
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            IrpInfoPopup_Vld.chkSave(mrTemp);

            sqlQuery = " SELECT COUNT(*) AS CNT \n"
                    + " FROM irpMast \n"
                    + " WHERE name = :name \n"
                    + " AND type = :irpType \n";
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                sqlQuery += " AND code <> :code \n";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("irpType", DmShared.xNull(mrTemp.getIrpTypeTag()));
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(100, "Name already exist in this IRP Type.");
                }
            }

            sqlQuery = " SELECT COUNT(*) AS CNT \n"
                    + " FROM irpMast \n"
                    + " WHERE url = :url \n"
                    + " AND type = :irpType \n";
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                sqlQuery += " AND code <> :code \n";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("url", DmShared.xNull(mrTemp.getUrl()));
            psTemp.setString("irpType", DmShared.xNull(mrTemp.getIrpTypeTag()));
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(100, "URL already exist in this IRP Type.");
                }
            }

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(DmShared.xNull(mrTemp.getSharedKeyFieldName())));
            mrTemp.setUrl(DmShared.removeSpace(DmShared.xNull(mrTemp.getUrl())));
            //===========================<< INSERT to irpMast >>===================================================
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.executeScalar("Select isNull(max(code),0) + 1 As Cnt From irpMast irm ", cnTemp));
                blnAdd = true;
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            if (blnAdd) {
                sqlQuery = "INSERT INTO irpMast( \n"
                        + " code, name, type, url, isActive, \n"
                        + " preparedBy, preparedDt, preparedByIP \n"
                        + " ) VALUES ( \n"
                        + ":code, :name, :type \n"
                        + ", :url, :isActive, :preparedBy, :preparedDt, :preparedByIP) \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("url", DmShared.xNull(mrTemp.getUrl()));
                psTemp.setString("type", DmShared.foriegnKey(mrTemp.getIrpTypeTag()));
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getIsActiveCode()).toString());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDt", DmServer.getServerTimestamp());
                psTemp.setString("preparedByIP", DmServer.getLocalIPAddresses(this));
            } else {
                sqlQuery = " UPDATE irpMast SET \n"
                        + "  name = :name \n"
                        + " , url = :url \n"
                        + " , isActive = :isActive \n"
                        + " , transferDt = :transferDt "
                        + " , u_EntDt = :uEntDt \n"
                        + " , u_Name = :u_name \n"
                        + " WHERE code = :code \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("url", DmShared.xNull(mrTemp.getUrl()));
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getIsActiveCode()).toString());
                psTemp.setString("transferDt", null);
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            //  =======================================================================================================
            saveIrpInfoLog(mrTemp.getSharedKeyFieldCode(), blnAdd ? null : prevMrTemp, mrTemp, blnAdd ? DmEnum.DmTransactionMode.NEW : DmEnum.DmTransactionMode.EDIT);

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
    public DmResult<IrpInfoPopup_Mdl> getMoveRec(String irpCode) {
        DmResult<IrpInfoPopup_Mdl> dmrTemp = new DmResult<IrpInfoPopup_Mdl>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(getMoveRec(irpCode, cnTemp));
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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

    private IrpInfoPopup_Mdl getMoveRec(String irpCode, Connection cnTemp) throws Exception {
        IrpInfoPopup_Mdl mrTemp = new IrpInfoPopup_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        try {
            sqlQuery = "SELECT  irm.code, irm.name as irpName \n"
                    + " , irm.type as irpTypeTag \n"
                    + ", irm.url, irm.isActive \n "
                    + ", irm.preparedBy, irm.u_name, irm.preparedDt, irm.preparedByIP, irm.u_EntDt \n"
                    + "	FROM irpMast irm \n"
                    + " WHERE irm.code = :irpCode \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("irpCode", DmShared.xNull(irpCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("irpName")));
                mrTemp.setSharedPreparedBy(DmServer.getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDt")));
                mrTemp.setSharedPreparedDate(DmServer.formatTimestamp(rsTemp.getTimestamp("preparedDt")));
                mrTemp.setSharedPreparedIP(DmShared.xNull(rsTemp.getString("preparedByIP")));
                mrTemp.setSharedModifyDate(DmServer.formatTimestamp(rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setSharedModifiedBy(DmServer.getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                
                if (!DmShared.xNull(rsTemp.getString("irpTypeTag")).trim().equalsIgnoreCase("")
                        && DmEnum.IRP_Type.getViaCode(DmShared.iNull(rsTemp.getString("irpTypeTag")).intValue()) != null) {
                    mrTemp.setIrpTypeTag(DmShared.xNull(rsTemp.getString("irpTypeTag")));
                    mrTemp.setIrpTypeText(DmEnum.IRP_Type.getViaCode(DmShared.iNull(rsTemp.getString("irpTypeTag")).intValue()).getValue());
                }
                mrTemp.setUrl(DmShared.xNull(rsTemp.getString("url")));
                if (!DmShared.xNull(rsTemp.getString("isActive")).trim().equalsIgnoreCase("")) {
                    mrTemp.setIsActiveCode(DmShared.xNull(rsTemp.getString("isActive")));
                    mrTemp.setIsActiveText(DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 1 ? "Yes" : "No");
                }

            }
            rsTemp.close();
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
        return mrTemp;
    }

    private void saveIrpInfoLog(String keyCode, IrpInfoPopup_Mdl prevRecord, IrpInfoPopup_Mdl newRecord, DmEnum.DmTransactionMode transactionMode) {
        boolean blnLog = true;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        ModifyReason_Mdl modifyReasonMdl;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //<editor-fold defaultstate="collapsed" desc="Validate ModifyReasonMdl">
            modifyReasonMdl = new ModifyReason_Mdl();
            modifyReasonMdl.setSharedKeyFieldCode(DmShared.xNull(DmServer.getCodeViaManualCode(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName(), "name", "modifyReasonMast", "code", cnTemp)));
            modifyReasonMdl.setSharedKeyFieldName(DmShared.xNull(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName()));
            modifyReasonMdl.setMode(DmShared.xNull(transactionMode.getValue()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Save Log">
            if (blnLog) {
                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                String keyName = "";
                if (prevRecord != null) {
                    modifyData_Mdl.setOldData(DmServer.convertToJson(prevRecord, new TypeToken<IrpInfoPopup_Mdl>() {
                    }));
                    keyName = DmShared.xNull(prevRecord.getSharedKeyFieldName());
                }
                if (newRecord != null) {
                    modifyData_Mdl.setNewData(DmServer.convertToJson(newRecord, new TypeToken<IrpInfoPopup_Mdl>() {
                    }));
                    keyName = DmShared.xNull(newRecord.getSharedKeyFieldName());
                }
                modifyData_Mdl.setKeyId_1(DmShared.xNull(keyCode));
                modifyData_Mdl.setKeyName_1(DmShared.xNull(keyName));
                modifyData_Mdl.setKeyDescription_1(DmShared.xNull(keyName));

                modifyData_Mdl.setControlText(transactionMode.getValue());
                modifyData_Mdl.setModelClassName(IrpInfoPopup_Mdl.class.getName());
                modifyData_Mdl.setVrTypeDescription("");
                modifyData_Mdl.setVrTypeSName("");
                modifyData_Mdl.setTableName("irpMast");
                //saving old record for edit, delete

                dataman.dmToolkit.modifyReason.server.ModifyReason_Server.saveNew(modifyData_Mdl, modifyReasonMdl, this);
            }
            cnTemp.close();
            //</editor-fold>
        } catch (Exception e) {
            System.out.println("saveLog: Exception - " + this.getClass());
            e.printStackTrace();
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
    }

}
