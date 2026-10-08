/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.selfToolkit.mi.server;

import aarogya.projectMenu.mi.jvm.MIExtendedStartupParams;
import aarogya.projectMenu.mi.jvm.MiPersistenceModel;
import aarogya.projectMenu.mi.shared.MIConfigEnum;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author abhishek
 */
public class MachineInterfaceConfiguration_Srvlt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        boolean bool = false;
        String responseJsonString = null;
        try {
            String userAgent = DmShared.xNull(req.getHeader("user-agent")).trim();
            String remoteHost = DmShared.xNull(req.getRemoteHost()).trim();

            StringBuilder sb = new StringBuilder();
            byte[] BUFFEREDBYTES = new byte[1024 * 1024];
            int readLength = req.getInputStream().read(BUFFEREDBYTES);
            while (readLength != -1) {
                String readString = new String(BUFFEREDBYTES, 0, readLength);
                sb.append(readString);
                readLength = req.getInputStream().read(BUFFEREDBYTES);
            }

            List<String> listMachineNames = DmServer.convertFromJson(sb.toString(), new TypeToken<List<String>>() {
            });
            if (listMachineNames == null) {
                listMachineNames = new ArrayList<>();
            }
            List<MIExtendedStartupParams> listStartupParams = new ArrayList<>();

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            String sql = "SELECT * FROM MACHINEINTERFACECONFIG WHERE (MACHINENAME=:machineName)";
            for (int i = 0; i < listMachineNames.size(); i++) {
                String machineName = listMachineNames.get(i);
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
                psTemp.setString("machineName", DmShared.xNull(machineName).trim());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    MIExtendedStartupParams startupParam = new MIExtendedStartupParams();
                    startupParam.setManualMachineCode(machineName);
                    MIConfigEnum configEnum = MIConfigEnum.getConfiguration(DmShared.xNull(rsTemp.getString("machineCode")).trim());
                    if (configEnum != null) {
                        startupParam.setMachineCode(configEnum.getConfigurationName());
                        if (configEnum.getCommunicationMode().equalsIgnoreCase("tcp")) {
                            startupParam.setMachineCommunicationPort(DmShared.xNull(rsTemp.getString("ipAddress")).trim() + ":" + DmShared.xNull(rsTemp.getString("portNo")).trim());
                        } else if (configEnum.getCommunicationMode().equalsIgnoreCase("serial")) {
                            startupParam.setBaudRate(DmShared.xNull(rsTemp.getString("baudRate")).trim());
                            startupParam.setDataBits(DmShared.xNull(rsTemp.getString("dataBit")).trim());
                            startupParam.setHardwareControl(DmShared.xNull(rsTemp.getString("hardwareControl")).trim());
                            startupParam.setMachineCommunicationPort(DmShared.xNull(rsTemp.getString("comPort")).trim());
                            startupParam.setParity(DmShared.xNull(rsTemp.getString("parity")).trim());
                            startupParam.setStopBits(DmShared.xNull(rsTemp.getString("stopBit")).trim());
                        }
                        listStartupParams.add(startupParam);
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            responseJsonString = DmServer.convertToJson(listStartupParams, new TypeToken<List<MIExtendedStartupParams>>() {
            });

        } catch (Exception ex) {
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
            if (!DmShared.xNull(responseJsonString).equals("")) {
                resp.getOutputStream().write(responseJsonString.getBytes(StandardCharsets.UTF_8));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
