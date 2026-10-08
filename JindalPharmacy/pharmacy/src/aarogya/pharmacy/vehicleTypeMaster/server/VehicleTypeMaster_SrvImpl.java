package aarogya.pharmacy.vehicleTypeMaster.server;

import com.google.gson.reflect.TypeToken;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import aarogya.pharmacy.vehicleTypeMaster.shared.Find_Mdl;
import aarogya.pharmacy.vehicleTypeMaster.shared.VehicleTypeMaster_Mdl;
import aarogya.pharmacy.vehicleTypeMaster.shared.VehicleTypeMaster_Vld;
import aarogya.pharmacy.vehicleTypeMaster.shared.VehicleTypeMaster_Srv;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.projectMenu.self.shared.pmPharmacy;

public class VehicleTypeMaster_SrvImpl extends DmRSSPrivate implements VehicleTypeMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT code FROM vehicleTypeMast ORDER BY name ");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<String>> getSuggestionVehicleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select name From vehicleTypeMast");
            helper.addSort("name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<VehicleTypeMaster_Mdl> getMoveRec(String strKey) {
        DmResult<VehicleTypeMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String[] cases = {"", ""};
        VehicleTypeMaster_Mdl mrTemp = new VehicleTypeMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT vtm.code,vtm.name,vtm.gstVehicleType  "
                    + " ,vtm.preparedBy, vtm.u_name, vtm.preparedDt, vtm.preparedByIP, vtm.u_EntDt "
                    + " FROM vehicleTypeMast vtm "
                    + " Where vtm.code= :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                if (!DmShared.xNull(rsTemp.getString("gstVehicleType")).trim().equalsIgnoreCase("")) {
                    mrTemp.setGstVehicleTypeCode(DmShared.xNull(rsTemp.getString("gstVehicleType")));
                    mrTemp.setGstVehicleTypeName(DmShared.xNull(DmEnum.GstVehicleType.getViaCode(DmShared.xNull(rsTemp.getString("gstVehicleType")))));
                }
                mrTemp.setSharedPreparedBy(DmServer.getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDt")));
                mrTemp.setSharedPreparedDate(DmServer.formatTimestamp(rsTemp.getTimestamp("preparedDt")));
                mrTemp.setSharedPreparedIP(DmShared.xNull(rsTemp.getString("preparedByIP")));
                mrTemp.setSharedModifyDate(DmServer.formatTimestamp(rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setSharedModifiedBy(DmServer.getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
            }
            psTemp.close();
            rsTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);
            DmServer.setSsnOldData(getClass().getName(), mrTemp, this);
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
    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From vehicleTypeMast Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
            saveLog(strKey, modifyReasonMdl, null, DmEnum.DmTransactionMode.DELETE, null);
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
    public DmResult<String> saveRecord(VehicleTypeMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        boolean blnAdd = false;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            VehicleTypeMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "vehicleTypeMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT COUNT(*) cnt FROM vehicleTypeMast WHERE name = :name AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new UnsupportedOperationException("Vehicle Type already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            Timestamp timestamp = DmServer.getServerTimestamp();
            if (blnAdd) {
                mrTemp.setSharedPreparedDate(DmServer.formatTimestamp(timestamp));
                mrTemp.setSharedPreparedBy(DmServer.getUserWithDateTime(DmServer.getSsnUserName(this), timestamp));
                mrTemp.setSharedPreparedIP(DmServer.getLocalIPAddresses(this));
            } else {
                mrTemp.setSharedModifyDate(DmServer.formatTimestamp(timestamp));
                mrTemp.setSharedModifiedBy(DmServer.getUserWithDateTime(DmServer.getSsnUserName(this), timestamp));
            }
            if (blnAdd) {
                sqlQuery = "Insert Into vehicleTypeMast (code , name, gstVehicleType "
                        + " ,preparedBy, preparedByIP, preparedDt) Values  "
                        + "(:code, :name, :gstVehicleType, :preparedBy, :preparedByIP, :preparedDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("gstVehicleType", DmShared.xNull(mrTemp.getGstVehicleTypeCode()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDt", timestamp);
                psTemp.setString("preparedByIP", DmServer.getLocalIPAddresses(this));
            } else {
                sqlQuery = " Update vehicleTypeMast Set "
                        + " name = :name, "
                        + " gstVehicleType = :gstVehicleType, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :uEntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("gstVehicleType", DmShared.xNull(mrTemp.getGstVehicleTypeCode()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", timestamp);
            }
            psTemp.executeUpdate();
            psTemp.close();
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
            saveLog(mrTemp.getSharedKeyFieldCode(), mrTemp.getModifyReasonMdl(), mrTemp, blnAdd ? DmEnum.DmTransactionMode.NEW : DmEnum.DmTransactionMode.EDIT, null);
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
            sqlWithParam.setSQL(" SELECT vtm.code,vtm.name,vtm.gstVehicleType \n"
                    + " FROM vehicleTypeMast vtm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setGstType(DmShared.xNull(rsTemp.getString("gstVehicleType")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGSTUnit(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (DmEnum.GstVehicleType gstVehicleType : DmEnum.GstVehicleType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(gstVehicleType.getCode()));
                listNode.setSharedKeyFieldName(DmShared.xNull(gstVehicleType.getName()));
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

    private void saveLog(String strKey, ModifyReason_Mdl modifyReasonMdl, VehicleTypeMaster_Mdl mrTemp, DmEnum.DmTransactionMode transactionMode, DmEnum.DmTransactionMode controlText) {
        String recId = "", description = "", shortName = "";
        boolean blnLog = false;
        String strSQL = "";
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //<editor-fold defaultstate="collapsed" desc="Validate Key">
            if (!DmShared.xNull(strKey).trim().equalsIgnoreCase("")) {
                if (mrTemp != null) {
                    if (!DmShared.xNull(strKey).equalsIgnoreCase(DmShared.xNull(mrTemp.getSharedKeyFieldCode()))) {
                        throw new DmException(0, "Key is not vaild.");
                    }
                }
            } else {
                throw new DmException(0, "Key is requied for saving log.");
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Validate ModifyReasonMdl">
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.PRINT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.APPROVE.getValue())) {
                blnLog = true;
                modifyReasonMdl = new ModifyReason_Mdl();
                modifyReasonMdl.setSharedKeyFieldCode(DmShared.xNull(DmServer.getCodeViaManualCode(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName(), "name", "modifyReasonMast", "code", cnTemp)));
                modifyReasonMdl.setSharedKeyFieldName(DmShared.xNull(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName()));
            } else {
                if (modifyReasonMdl == null) {
                    if (mrTemp != null) {
                        modifyReasonMdl = mrTemp.getModifyReasonMdl();
                    }
                }
                if (modifyReasonMdl != null) {
                    if (mrTemp != null) {
                        if (!DmShared.xNull(modifyReasonMdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getModifyReasonMdl().getSharedKeyFieldCode()))) {
                            throw new DmException(0, "ModifyReasonMdl is not vaild.");
                        }
                    }
                }
            }
            if (modifyReasonMdl == null) {
                throw new DmException(0, "ModifyReasonMdl is requied for saving log.");
            }
            //</editor-fold>
            modifyReasonMdl.setMode(DmShared.xNull(transactionMode.getValue()));

            //<editor-fold defaultstate="collapsed" desc="Save Log">
            VehicleTypeMaster_Mdl oldMdl = null;
            //create new reason mode in case of print, new
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                //check if latest record's old data is saved in moverec
                oldMdl = (VehicleTypeMaster_Mdl) DmServer.getSsnOldData(getClass().getName(), this);
                if (oldMdl == null || !DmShared.xNull(oldMdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(strKey))) {
                    throw new DmException(0, "Old Model data is invalid");
                } else {
                    if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                        mrTemp.setSharedPreparedBy(DmShared.xNull(oldMdl.getSharedPreparedBy()));
                        mrTemp.setSharedPreparedDate(DmShared.xNull(oldMdl.getSharedPreparedDate()));
                        mrTemp.setSharedPreparedIP(DmShared.xNull(oldMdl.getSharedPreparedIP()));
                    }
                    blnLog = true;
                }
            }
            if (blnLog) {
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    recId = DmShared.xNull(oldMdl.getSharedKeyFieldName());
                } else {
                    recId = DmShared.xNull(mrTemp.getSharedKeyFieldName());
                }
                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                modifyData_Mdl.setKeyId_1(DmShared.xNull(strKey));
                modifyData_Mdl.setKeyName_1(DmShared.xNull(recId));
                modifyData_Mdl.setKeyDescription_1(DmShared.xNull(recId));
                modifyData_Mdl.setModuleName(new pmMaster().moduleName);
                modifyData_Mdl.setMenuName(new pmPharmacy().VEHICLE_TYPE_MASTER.caption);
                if (controlText != null) {
                    modifyData_Mdl.setControlText(controlText.getValue());
                }
                modifyData_Mdl.setModelClassName(VehicleTypeMaster_Mdl.class.getName());
                modifyData_Mdl.setVrTypeDescription("");
                modifyData_Mdl.setVrTypeSName("");
                modifyData_Mdl.setTableName("vehicleTypeMast");
                //saving old record for edit, delete
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    modifyData_Mdl.setOldData(DmServer.convertToJson(oldMdl, new TypeToken<VehicleTypeMaster_Mdl>() {
                    }));
                }
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                    modifyData_Mdl.setNewData(DmServer.convertToJson(mrTemp, new TypeToken<VehicleTypeMaster_Mdl>() {
                    }));
                }
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
