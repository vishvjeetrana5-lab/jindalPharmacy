package aarogya.localSetting.server;

import aarogya.localSetting.shared.GrdLabTestCodification_Mdl;
import aarogya.localSetting.shared.LabMachineConfiguration_Mdl;
import aarogya.localSetting.shared.LocalSetting_Srv;
import aarogya.localSetting.shared.TxtHMachineSpecification_Mdl;
import aarogya.localSetting.shared.TxtHMenuText_Mdl;
import aarogya.projectMenu.mi.shared.ExtendedLocalSettings;
import aarogya.projectMenu.mi.shared.MIConfigDetailsParams;
import aarogya.projectMenu.mi.shared.MIConfigEnum;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STLocalSetting_Mdl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmToolkit.dmStatic.server.DmServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LocalSetting_SrvImpl extends DmRSSPrivate implements LocalSetting_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> convertPrintServicesModel(DmHelper helper, String printerJsonString) {
        DmResultPageing<List<KeyFieldExtended>> result = new DmResultPageing<>();
        List<KeyFieldExtended> lstModels = new ArrayList<>();

        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
            };
            List<String> lstPrinters = gson.fromJson(printerJsonString, typeToken.getType());
            for (int i = 0; i < lstPrinters.size(); i++) {
                KeyFieldExtended kf = new KeyFieldExtended(lstPrinters.get(i), lstPrinters.get(i));
                lstModels.add(kf);
            }

            lstModels = DmShared.filterKeyFieldList(helper, lstModels);

            result.setData(lstModels);
            result.setPaging(helper.getPaging());
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public DmResult<String> getJsonOfSettings(STLocalSetting_Mdl settingModel) {
        DmResult<String> result = new DmResult<>();
        try {

            JVMLocalSetting_Mdl newSettingModel = new JVMLocalSetting_Mdl();

            newSettingModel.setReportMapSettings(settingModel.getDmLocalSettingPrintMap_Mdls());
            newSettingModel.setPrinterSettings(settingModel.getDmLocalSettingPrinter_Mdls());
            newSettingModel.setExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString(), settingModel.getExtendedLocalSettingJsonString());

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            TypeToken<JVMLocalSetting_Mdl> typeToken = new TypeToken<JVMLocalSetting_Mdl>() {
            };
            String encodedString = gson.toJson(newSettingModel, typeToken.getType());//new String(java.util.Base64.getEncoder().encode(gson.toJson(newSettingModel, typeToken.getType()).getBytes("UTF-8")));
            result.setData(encodedString);
            result.setStatus(true);
        } catch (Exception ex) {
            result.setErrorMessage(ex.getMessage());
            result.setStatus(false);
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public DmResult<STLocalSetting_Mdl> getSettingsFromJson(String jsonString) {
        DmResult<STLocalSetting_Mdl> result = new DmResult<>();
        try {
            TypeToken<JVMLocalSetting_Mdl> typeToken = new TypeToken<JVMLocalSetting_Mdl>() {
            };
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            JVMLocalSetting_Mdl settings = gson.fromJson(jsonString, typeToken.getType());

            STLocalSetting_Mdl settingsModel = new STLocalSetting_Mdl();

            settingsModel.setDmLocalSettingPrintMap_Mdls(settings.getReportMapSettings());
            settingsModel.setDmLocalSettingPrinter_Mdls(settings.getPrinterSettings());
            settingsModel.setExtendedLocalSettingJsonString(settings.getExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString()));

            result.setData(settingsModel);
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public DmResultPageing<List<TxtHMenuText_Mdl>> getTxtHMenuText(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMenuText_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMenuText_Mdl> lstTemp = new ArrayList<>();
        TxtHMenuText_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select MnuText + 'Ã¾' + MnuName + 'Ã¾' + MnuModule As Code, MnuText, MnuName, MnuModule "
                    + "                 From User_Permission_Web UP Where UserName='SA'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMenuText_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("MnuText")));

                listNode.setMenuModuleName(DmShared.xNull(rsTemp.getString("MnuModule")));
                listNode.setMenuText(DmShared.xNull(rsTemp.getString("MnuText")));
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
    public DmResultPageing<List<TxtHMachineSpecification_Mdl>> getTxtHMachineSpecification(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMachineSpecification_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMachineSpecification_Mdl> lstTemp = new ArrayList<>();
        TxtHMachineSpecification_Mdl listNode;

        try {

            MIConfigEnum[] values = MIConfigEnum.values();
            for (int i = 0; i < values.length; i++) {
                MIConfigEnum config = values[i];
                listNode = new TxtHMachineSpecification_Mdl();
                listNode.setSharedKeyFieldCode(config.getConfigurationName());
                listNode.setSharedKeyFieldName(config.getMachineModel());
                listNode.setCompanyName(config.getMachineCompany());
                listNode.setCommunicationMode(config.getCommunicationMode());
                listNode.setTransmissionMode(config.getTransmissionMode());
                listNode.setProtocoll(config.getProtocol());

                lstTemp.add(listNode);
            }

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
    public DmResult<LabMachineConfiguration_Mdl> parseMachineDetails(String jsonString) {
        DmResult<LabMachineConfiguration_Mdl> result = new DmResult<>();
        try {
            Map<String, String> machineDetails = DmServer.convertFromJson(jsonString, new TypeToken<HashMap<String, String>>() {
            });

            LabMachineConfiguration_Mdl machineModel = new LabMachineConfiguration_Mdl();

            machineModel.setMachineCode(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.ConfigName.toString())).trim());
            machineModel.setCompanyName(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.MachineName.toString())).trim());
            machineModel.setSharedKeyFieldCode(machineModel.getMachineCode());
            machineModel.setSharedKeyFieldName(machineModel.getMachineName());
            machineModel.setModelNo(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.MachineModel.toString())).trim());
            machineModel.setProtocoll(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.Protocol.toString())).trim());
            machineModel.setCommunicationMode(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.CommunicationMode.toString())).trim());

            if (machineDetails.get(MIConfigDetailsParams.CommunicationMode.toString()).equalsIgnoreCase("serial")) {
                if (machineDetails.containsKey(MIConfigDetailsParams.BaudRate.toString())) {
                    machineModel.setBaudRate(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.BaudRate.toString())).trim());
                }
                if (machineDetails.containsKey(MIConfigDetailsParams.StopBits.toString())) {
                    machineModel.setStopBit(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.StopBits.toString())).trim());
                }
                if (machineDetails.containsKey(MIConfigDetailsParams.DataBits.toString())) {
                    machineModel.setDataBit(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.DataBits.toString())).trim());
                }
                if (machineDetails.containsKey(MIConfigDetailsParams.Parity.toString())) {
                    machineModel.setParity(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.Parity.toString())).trim());
                }
                if (machineDetails.containsKey(MIConfigDetailsParams.HardwareControl.toString())) {
                    machineModel.setHardwareControl(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.HardwareControl.toString())).trim());
                }
                if (machineDetails.containsKey(MIConfigDetailsParams.CommunicationPort.toString())) {
                    machineModel.setComPortNo(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.CommunicationPort)).trim());
                }
            } else if (machineDetails.get(MIConfigDetailsParams.CommunicationMode.toString()).equalsIgnoreCase("tcp")) {
                machineModel.setPortNo(DmShared.xNull(machineDetails.get(MIConfigDetailsParams.CommunicationPort.toString())).trim());
            }

            if (machineDetails.containsKey(MIConfigDetailsParams.ObservableTestList.toString())) {
                String labTestJson = DmShared.xNull(machineDetails.get(MIConfigDetailsParams.ObservableTestList.toString())).trim();
                if (!labTestJson.equals("")) {
                    List<String> listTests = DmServer.convertFromJson(labTestJson, new TypeToken<List<String>>() {
                    });
                    for (int i = 0; i < listTests.size(); i++) {
                        GrdLabTestCodification_Mdl testModel = new GrdLabTestCodification_Mdl();
                        testModel.setMachineTestName(listTests.get(i));
                    }
                }
            }

            result.setData(machineModel);

            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public DmResult<String> getJsonOfLabMachineConfigurations(List<LabMachineConfiguration_Mdl> labMachineConfigurations) {
        DmResult<String> result = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = "select count(machineName) as cnt from machineInterfaceConfig  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(STServer.getSsnNoOfMachine(this)).intValue() >= 0) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > DmShared.iNull(STServer.getSsnNoOfMachine(this)).intValue()) {
                        throw new DmException(111, "Invalid License Key for No. of Machine Interface.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            if (labMachineConfigurations.size() > 0) {
                List<String> listMachineNames = new ArrayList<>();
                for (int i = 0; i < labMachineConfigurations.size(); i++) {
                    LabMachineConfiguration_Mdl macModel = labMachineConfigurations.get(i);
                    String machineCode = DmShared.xNull(macModel.getMachineCode()).trim();
                    MIConfigEnum miConfig = MIConfigEnum.getConfiguration(machineCode);
                    if ((miConfig != null) && (!DmShared.xNull(macModel.getMachineName()).trim().equals(""))) {
                        String machineName = DmShared.xNull(macModel.getMachineName()).trim();
                        listMachineNames.add(machineName);
                    }
                }
                result.setData(DmServer.convertToJson(listMachineNames, new TypeToken<List<String>>() {
                }));
            }
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp = null;
                }
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    @Override
    public DmResult<List<LabMachineConfiguration_Mdl>> getLabMachineModelsFromJsonOfSavedConfigs(String jsonString) {
        DmResult<List<LabMachineConfiguration_Mdl>> result = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            if (!DmShared.xNull(jsonString).equals("")) {
                List<LabMachineConfiguration_Mdl> listLabModels = new ArrayList<>();
                List<String> listMachineNames = DmServer.convertFromJson(jsonString, new TypeToken<List<String>>() {
                });

                if (listMachineNames.size() > 0) {

                    String sql = "select * from machineInterfaceConfig where (1 = 1)";

                    Map<String, String> sqlMap = new HashMap<>();

                    for (int i = 0; i < listMachineNames.size(); i++) {
                        String machineName = DmShared.xNull(listMachineNames.get(i)).trim();
                        if (!machineName.equals("")) {
                            if (i == 0) {
                                sql += " AND (";
                            }
                            String key = "machineName" + i;
                            sql += "(machineName=:" + key + ") OR ";
                            sqlMap.put(key, machineName);
                            LabMachineConfiguration_Mdl labModel = new LabMachineConfiguration_Mdl();
                            labModel.setMachineName(machineName);
                            labModel.setSharedKeyFieldName(machineName);
                            labModel.setUiHashCode(listLabModels.size());
                            listLabModels.add(labModel);
                        }
                    }

                    if (sql.endsWith(") OR ")) {
                        sql = sql.substring(0, sql.length() - 3).trim();
                        if (sqlMap.size() > 0) {
                            sql += ")";
                        }
                    }

                    cnTemp = DmServer.getConnection(DmServer.dbCompany);
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
                    if (sqlMap.size() > 0) {
                        Iterator<String> keys = sqlMap.keySet().iterator();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = sqlMap.get(key);
                            psTemp.setString(key, value);
                        }
                    }
                    rsTemp = psTemp.executeQuery();
                    while (rsTemp.next()) {
                        String machineName = DmShared.xNull(rsTemp.getString("machineName")).trim();
                        LabMachineConfiguration_Mdl tempModel = new LabMachineConfiguration_Mdl();
                        tempModel.setSharedKeyFieldName(machineName);
                        int indexOfMachine = listLabModels.indexOf(tempModel);
                        if ((!machineName.equals("")) && (indexOfMachine >= 0)) {
                            MIConfigEnum machineConfig = MIConfigEnum.getConfiguration(DmShared.xNull(rsTemp.getString("machineCode")).trim());
                            if (machineConfig != null) {
                                LabMachineConfiguration_Mdl labModel = listLabModels.get(indexOfMachine);

                                labModel.setMachineCode(machineConfig.getConfigurationName());
                                labModel.setMachineName(DmShared.xNull(rsTemp.getString("machineName")).trim());
                                labModel.setSharedKeyFieldCode(labModel.getMachineCode());
                                labModel.setCommunicationMode(machineConfig.getCommunicationMode());
                                labModel.setProtocoll(machineConfig.getProtocol());
                                labModel.setCompanyName(machineConfig.getMachineCompany());
                                labModel.setModelNo(machineConfig.getMachineModel());
                                labModel.setTransmissionMode(machineConfig.getTransmissionMode());
                                if (machineConfig.getCommunicationMode().equalsIgnoreCase("tcp")) {
                                    labModel.setIpAddress(DmShared.xNull(rsTemp.getString("ipAddress")).trim());
                                    labModel.setPortNo(DmShared.xNull(rsTemp.getString("portNo")).trim());
                                } else if (machineConfig.getCommunicationMode().equalsIgnoreCase("serial")) {
                                    labModel.setBaudRate(DmShared.xNull(rsTemp.getString("baudRate")).trim());
                                    labModel.setComPortNo(DmShared.xNull(rsTemp.getString("comPort")).trim());
                                    labModel.setDataBit(DmShared.xNull(rsTemp.getString("dataBit")).trim());
                                    labModel.setHardwareControl(DmShared.xNull(rsTemp.getString("hardwareControl")).trim());
                                    labModel.setParity(DmShared.xNull(rsTemp.getString("parity")).trim());
                                    labModel.setStopBit(DmShared.xNull(rsTemp.getString("stopBit")).trim());
                                }
                            }
                        }
                    }

//                    cnTemp = DmServer.getConnection(DmServer.dbCompany);
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
//                    if (sqlMap.size() > 0) {
//                        Iterator<String> keys = sqlMap.keySet().iterator();
//                        while (keys.hasNext()) {
//                            String key = keys.next();
//                            String value = sqlMap.get(key);
//                            psTemp.setString(key, value);
//                        }
//                    }
//                    rsTemp = psTemp.executeQuery();
//                    while (rsTemp.next()) {
//                        MIConfigEnum machineConfig = MIConfigEnum.getConfiguration(DmShared.xNull(rsTemp.getString("machineCode")).trim());
//                        if (machineConfig != null) {
//                            LabMachineConfiguration_Mdl labModel = new LabMachineConfiguration_Mdl();
//                            labModel.setMachineCode(machineConfig.getConfigurationName());
//                            labModel.setMachineName(DmShared.xNull(rsTemp.getString("machineName")).trim());
//                            labModel.setSharedKeyFieldCode(labModel.getMachineCode());
//                            labModel.setCommunicationMode(machineConfig.getCommunicationMode());
//                            labModel.setProtocoll(machineConfig.getProtocol());
//                            labModel.setCompanyName(machineConfig.getMachineCompany());
//                            labModel.setModelNo(machineConfig.getMachineModel());
//                            labModel.setTransmissionMode(machineConfig.getTransmissionMode());
//                            if (machineConfig.getCommunicationMode().equalsIgnoreCase("tcp")) {
//                                labModel.setIpAddress(DmShared.xNull(rsTemp.getString("ipAddress")).trim());
//                                labModel.setPortNo(DmShared.xNull(rsTemp.getString("portNo")).trim());
//                            } else if (machineConfig.getCommunicationMode().equalsIgnoreCase("serial")) {
//                                labModel.setBaudRate(DmShared.xNull(rsTemp.getString("baudRate")).trim());
//                                labModel.setComPortNo(DmShared.xNull(rsTemp.getString("comPort")).trim());
//                                labModel.setDataBit(DmShared.xNull(rsTemp.getString("dataBit")).trim());
//                                labModel.setHardwareControl(DmShared.xNull(rsTemp.getString("hardwareControl")).trim());
//                                labModel.setParity(DmShared.xNull(rsTemp.getString("parity")).trim());
//                                labModel.setStopBit(DmShared.xNull(rsTemp.getString("stopBit")).trim());
//                            }
//                            listLabModels.add(labModel);
//                        }
//                    }
                }

                result.setData(listLabModels);
            }
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(true);
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp = null;
                }
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    @Override
    public DmResult<String> saveRecordToSQL(LabMachineConfiguration_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            userName = DmServer.getSsnUserName(this);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getMode()).trim().equalsIgnoreCase("Create")) {
                blnAdd = true;
            }

            if (blnAdd) {
                //===================================Chk Duplicate Name ================================================
                sqlQuery = "SELECT machineName FROM machineInterfaceConfig WHERE machineName = :machineName ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("machineName", DmShared.removeSpace(DmShared.xNull(mrTemp.getMachineName().trim())));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Machine Name already exist in Database.");
                }
                rsTemp.close();
                psTemp.close();
            }

            //==========================Delete From machineInterfaceConfig=====================================
            sqlQuery = "Delete From machineInterfaceConfig Where machineName= :machineName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("machineName", DmShared.xNull(mrTemp.getMachineName()));
            psTemp.executeUpdate();
            psTemp.close();

            //==========================Delete From machineSubTestMapping=====================================
            sqlQuery = "Delete From machineSubTestMapping Where machineName = :machineName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("machineName", DmShared.xNull(mrTemp.getMachineName()));
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert into machineInterfaceConfig >====================================*/
            sqlQuery = "INSERT INTO machineInterfaceConfig  (machineName,machineCode,ipAddress,portNo,comPort,baudRate,stopBit,dataBit,parity,hardwareControl,u_EntDt,preparedBy)  "
                    + " VALUES ( :machineName, :machineCode, :ipAddress, :portNo, :comPort, :baudRate, :stopBit, :dataBit, :parity, :hardwareControl, :u_EntDt, :preparedBy)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("machineName", DmShared.removeSpace(DmShared.xNull(mrTemp.getMachineName().trim())));
            psTemp.setString("machineCode", DmShared.removeSpace(DmShared.xNull(mrTemp.getMachineCode().trim())));
            psTemp.setString("ipAddress", DmShared.removeSpace(DmShared.xNull(mrTemp.getIpAddress().trim())));
            psTemp.setString("portNo", DmShared.removeSpace(DmShared.xNull(mrTemp.getPortNo().trim())));
            psTemp.setString("comPort", DmShared.removeSpace(DmShared.xNull(mrTemp.getComPortNo().trim())));
            psTemp.setString("baudRate", DmShared.removeSpace(DmShared.xNull(mrTemp.getBaudRate().trim())));
            psTemp.setString("stopBit", DmShared.removeSpace(DmShared.xNull(mrTemp.getStopBit().trim())));
            psTemp.setString("dataBit", DmShared.removeSpace(DmShared.xNull(mrTemp.getDataBit().trim())));
            psTemp.setString("parity", DmShared.removeSpace(DmShared.xNull(mrTemp.getParity().trim())));
            psTemp.setString("hardwareControl", DmShared.removeSpace(DmShared.xNull(mrTemp.getHardwareControl().trim())));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("preparedBy", userName);
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdData() != null) {
                for (GrdLabTestCodification_Mdl grdMdl : mrTemp.getGrdData()) {
                    if (grdMdl.getManualName() != null) {
                        /*====================================< Insert into dietInstructionDetail >====================================*/
                        sqlQuery = "INSERT INTO machineSubTestMapping (machineName,machineSubTestName,ourSubTestName) "
                                + " VALUES(:machineName, :machineSubTestName, :ourSubTestName )";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("machineName", DmShared.removeSpace(DmShared.xNull(mrTemp.getMachineName().trim())));
                        psTemp.setString("machineSubTestName", DmShared.removeSpace(DmShared.xNull(grdMdl.getMachineTestName().trim())));
                        psTemp.setString("ourSubTestName", DmShared.removeSpace(DmShared.xNull(grdMdl.getManualName().trim())));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMachineName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("select machineName as code , machineName as name from machineInterfaceConfig");

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
    public DmResult<LabMachineConfiguration_Mdl> getData(String strKey) {
        DmResult<LabMachineConfiguration_Mdl> dmrTemp = new DmResult();

        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        LabMachineConfiguration_Mdl mrTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "SELECT machineCode,ipAddress,portNo,comPort,baudRate,stopBit,dataBit,parity,hardwareControl "
                    + " FROM dbo.machineInterfaceConfig  as mic  "
                    + " where mic.machineName  = :machineName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("machineName", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new LabMachineConfiguration_Mdl();
                mrTemp.setMachineName(DmShared.xNull(strKey));
                mrTemp.setMachineCode(DmShared.xNull(rsTemp.getString("machineCode")));
                mrTemp.setSharedKeyFieldCode(mrTemp.getMachineCode());
                mrTemp.setSharedKeyFieldName(mrTemp.getMachineName());
                mrTemp.setIpAddress(DmShared.xNull(rsTemp.getString("ipAddress")));
                mrTemp.setPortNo(DmShared.xNull(rsTemp.getString("portNo")));
                mrTemp.setComPortNo(DmShared.xNull(rsTemp.getString("comPort")));
                mrTemp.setBaudRate(DmShared.xNull(rsTemp.getString("baudRate")));
                mrTemp.setStopBit(DmShared.xNull(rsTemp.getString("stopBit")));
                mrTemp.setDataBit(DmShared.xNull(rsTemp.getString("dataBit")));
                mrTemp.setParity(DmShared.xNull(rsTemp.getString("parity")));
                mrTemp.setHardwareControl(DmShared.xNull(rsTemp.getString("hardwareControl")));

            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdData().clear();

            if (!DmShared.xNull(strKey).equalsIgnoreCase("")) {
                mrTemp.getGrdData().addAll(getFooterGridData(strKey, cnTemp));
            }

            cnTemp.close();

            dmrTemp.setData(mrTemp);
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

    private List<GrdLabTestCodification_Mdl> getFooterGridData(String strKey, Connection cnTemp) {

        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        GrdLabTestCodification_Mdl grdData;

        List<GrdLabTestCodification_Mdl> footerGrdDataList = new ArrayList<GrdLabTestCodification_Mdl>();

        try {

            sqlQuery = " SELECT machineSubTestName,ourSubTestName FROM machineSubTestMapping mstm "
                    + "where mstm.machineName = :machineName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("machineName", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                grdData = new GrdLabTestCodification_Mdl();

                grdData.setMachineTestName(DmShared.xNull(rsTemp.getString("machineSubTestName")));
                grdData.setManualName(DmShared.xNull(rsTemp.getString("ourSubTestName")));

                footerGrdDataList.add(grdData);
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
        return footerGrdDataList;
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
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("select machineName as name from machineInterfaceConfig ");
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
    public DmResult<String> deleteRecordFromSQL(String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTempUpdate = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            //==========================Delete From machineInterfaceConfig=====================================
            sqlQuery = "Delete From machineInterfaceConfig Where machineName= :machineName ";
            psTempUpdate = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTempUpdate.setString("machineName", DmShared.xNull(keyCode));
            psTempUpdate.executeUpdate();
            psTempUpdate.close();

            //==========================Delete From machineSubTestMapping=====================================
            sqlQuery = "Delete From machineSubTestMapping Where machineName= :machineName ";
            psTempUpdate = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTempUpdate.setString("machineName", DmShared.xNull(keyCode));
            psTempUpdate.executeUpdate();
            psTempUpdate.close();

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
                if (psTempUpdate != null) {
                    psTempUpdate.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getSubTest(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("  select psm.name as testName, tsc.name as subTest, psm.code from patientServiceMast psm "
                    + "  left join testSubCategory tsc on psm.code=tsc.code "
                    + "  WHERE psm.serviceNature = " + STEnum.ServiceNature.PATHOLOGY_TEST.getId() + " ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("testName")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("subTest")));
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
