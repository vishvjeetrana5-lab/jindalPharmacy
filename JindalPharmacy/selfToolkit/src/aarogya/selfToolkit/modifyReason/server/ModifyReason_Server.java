package aarogya.selfToolkit.modifyReason.server;

import aarogya.selfToolkit.modifyReason.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifyReason_Server {

    public static void delete(String docId, Connection cnTemp) throws Exception {
        deleteModifyReason(docId, 1, cnTemp);
    }

    public static void delete(String docId, int sNo, Connection cnTemp) throws Exception {
        deleteModifyReason(docId, sNo, cnTemp);
    }

    private static void deleteModifyReason(String docId, int sNo, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";

        try {

            if (DmShared.iNull(sNo).longValue() != 0) {
                condition = " and sNo = " + sNo + " ";
            }
            sqlQuery = "delete from modifyReason where docId = :docId " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();
        } catch (SQLException | DmException e) {
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

    public static void save(ModifyReason_Mdl modifyReason, String docId, String userName, Connection cnTemp) throws Exception {
        saveModifyReason(modifyReason, docId, userName, 1, cnTemp);
    }

    public static void save(ModifyReason_Mdl modifyReason, String docId, String userName, int sNo, Connection cnTemp) throws Exception {
        saveModifyReason(modifyReason, docId, userName, sNo, cnTemp);
    }

    private static void saveModifyReason(ModifyReason_Mdl modifyReason, String docId, String userName, int sNo, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, vSNo = "", uniqueKey = "";
        boolean changeFound = false;
        boolean manageGridLog = false;

        String gridStatus = "";

        int maxGridSize = 0;
        int vNo = 0;
        try {
            if (modifyReason != null) {

                if (modifyReason.getLogPrevious().isEmpty() && modifyReason.getLogCurrent().isEmpty()) {
                    inserIntoModifyReason("1", "", "", "", modifyReason, docId, userName, sNo, psTemp, cnTemp);
                } else {

                    //==================Get All Keys and Configuration============================================
                    List<String> totalFieldList = new ArrayList<>();
                    HashMap<String, String> logConfigurationHmList = new HashMap<>();
                    sqlQuery = "select fieldName as totalFields, sendMailOnEdit from logConfigurationSetting where mnuModule='" + modifyReason.getModuleName() + "' and mnuName='" + modifyReason.getMenuName() + "'  ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    rsTemp = psTemp.executeQuery();
                    while (rsTemp.next()) {
                        if (DmShared.iNull(rsTemp.getInt("sendMailOnEdit")).intValue() == 1) {
                            logConfigurationHmList.put(DmShared.xNull(rsTemp.getString("totalFields").trim()), "1");
                        }
                        totalFieldList.add(DmShared.xNull(rsTemp.getString("totalFields").trim()));

                        if(DmShared.xNull(rsTemp.getString("totalFields")).equalsIgnoreCase("Grid Detail") && DmShared.iNull(rsTemp.getInt("sendMailOnEdit")).intValue() == 1) {
                            manageGridLog = true;
                        }
                    }
                    psTemp.close();
                    rsTemp.close();

                    //==================Header Comparision Algo.============================================
                    for (int i = 0; i < totalFieldList.size(); i++) {
                        if (modifyReason.getLogPrevious().containsKey(totalFieldList.get(i)) && modifyReason.getLogCurrent().containsKey(totalFieldList.get(i))) {
                            if (!DmShared.removeSpace(modifyReason.getLogCurrent().get(totalFieldList.get(i))).equalsIgnoreCase(DmShared.removeSpace(modifyReason.getLogPrevious().get(totalFieldList.get(i))))) {
                                changeFound = true;
                                if (logConfigurationHmList.containsKey(totalFieldList.get(i))) {
                                    uniqueKey = modifyReason.getUniqueKey();
                                } else {
                                    uniqueKey = "";
                                }

                                //==================Insert Header Log============================================
                                vNo++;
                                modifyReason.setPreChangeValue(modifyReason.getLogPrevious().get(totalFieldList.get(i)));
                                modifyReason.setPostChangeValue(modifyReason.getLogCurrent().get(totalFieldList.get(i)));

                                inserIntoModifyReason("3", uniqueKey, totalFieldList.get(i), modifyReason.getMenuName(), modifyReason, docId, userName, vNo, psTemp, cnTemp);
                            }
                        }
                    }
                    //====================================Manage grid Log===========================================
                    if (manageGridLog) {

                        uniqueKey = modifyReason.getUniqueKey();
                        String gridPrevious = "";
                        String gridCurrent = "";

                        //==================Grid Comparision Algo.============================================
                        for (int i = 0; i < modifyReason.getGridPreviousKeysList().size(); i++) {

                            String Key = modifyReason.getGridPreviousKeysList().get(i);
                          
//                            if (!modifyReason.getGridLogCurrent().containsKey(Key)) {
//
////                                gridPrevious += " <tr> <td>Deleted </td> " + "" + DmShared.xNull(modifyReason.getGridLogPrevious().get(Key)) + "</tr>";
//                                gridCurrent += " <tr> <td>Deleted </td> " + "" + DmShared.xNull(modifyReason.getGridLogPrevious().get(Key)) + "</tr>";
//
//                            }
                            if (modifyReason.getGridLogCurrent().containsKey(Key)) {
                                if (!modifyReason.getGridLogCurrent().get(Key).equalsIgnoreCase(modifyReason.getGridLogPrevious().get(Key))) {

                                    //==================Diffrence Detection Algo.============================================
                                    String prevGrdLineItm = DmShared.xNull(modifyReason.getGridLogPrevious().get(Key));
                                    String[] prevGrdLine = prevGrdLineItm.replace("<td>", "").replace("</td>", ",").split(",");

                                    String currentGrdLineItm = DmShared.xNull(modifyReason.getGridLogCurrent().get(Key));

                                    String[] curntGrdLine = currentGrdLineItm.replace("<td>", "").replace("</td>", ",").split(",");
                                    List<String> diff = new ArrayList<>();
                                    
                                    for (int k = 0; k < prevGrdLine.length; k++) {
                                        if (!DmShared.removeSpace(prevGrdLine[k]).trim().equalsIgnoreCase(DmShared.removeSpace(curntGrdLine[k]).trim())) {
                                            diff.add(curntGrdLine[k]);
                                        }
                                    }

                                    //==================Mark Difference============================================
                                    String currRow = DmShared.xNull(modifyReason.getGridLogCurrent().get(Key));
                                    for (int m = 0; m < diff.size(); m++) {
                                        currRow = currRow.replace(diff.get(m), "<span style=\"background-color: #cdff69;\">" + diff.get(m) + "</span>");
                                    }
                                    //==================Set Data============================================
//                                    gridCurrent += " <tr> <td>Update </td> " + "" + currRow + "</tr>";
//                                    gridPrevious += " <tr> <td>Previous</td> " + "" + DmShared.xNull(modifyReason.getGridLogPrevious().get(Key)) + "</tr>";

                                    gridCurrent += " <tr> <td>Previous</td> " + "" + DmShared.xNull(modifyReason.getGridLogPrevious().get(Key)) + "</tr>";

                                    gridCurrent += "<b> <tr> <td>Updated </td> " + "" + currRow + "</tr> </b>";

                                }
                            }

                        }
                        
                          for (int i = 0; i < modifyReason.getGridPreviousKeysList().size(); i++) {

                            String Key = modifyReason.getGridPreviousKeysList().get(i);
                            if (!modifyReason.getGridLogCurrent().containsKey(Key)) {

//                                gridPrevious += " <tr> <td>Deleted </td> " + "" + DmShared.xNull(modifyReason.getGridLogPrevious().get(Key)) + "</tr>";
                                gridCurrent += "<tr> <td>Deleted </td> <strike> " + "" + DmShared.xNull(modifyReason.getGridLogPrevious().get(Key)) + "</strike></tr>";

                            }
                          }
                        for (int i = 0; i < modifyReason.getGridCurrentKeysList().size(); i++) {

                            String Key = modifyReason.getGridCurrentKeysList().get(i);
                            if (!modifyReason.getGridLogPrevious().containsKey(Key)) {

                                gridCurrent += " <tr> <td>Added </td> " + "" + DmShared.xNull(modifyReason.getGridLogCurrent().get(Key)) + "</tr>";

                            }

                        }

                        //==================Insert Grid Log.============================================
                        if (!gridPrevious.trim().equalsIgnoreCase("")) {
                            modifyReason.setPreChangeValue("<table  style=\"text-align:center\"  cellpadding=\"2\" cellspacing=\"0\" border=\"1\" bordercolor=\"#90d4fc\">" + modifyReason.getGridHeader() + gridPrevious + "</table>");

                        }
                        if (!gridCurrent.trim().equalsIgnoreCase("")) {
                            modifyReason.setPostChangeValue("<table  style=\"text-align:center\"  cellpadding=\"2\" cellspacing=\"0\" border=\"1\" bordercolor=\"#90d4fc\">" + modifyReason.getGridHeader() + gridCurrent + "</table>");

                        }

                        if (!gridPrevious.trim().equalsIgnoreCase("") || !gridCurrent.trim().equalsIgnoreCase("")) {
                            inserIntoModifyReason("3", uniqueKey, "Grid Detail", modifyReason.getMenuName(), modifyReason, docId, userName, vNo, psTemp, cnTemp);
                        }
                    }

                }
            } else {
                throw new DmException(0, "Reason can't empty.");
            }

        } catch (SQLException | DmException e) {
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
    }

    private static void inserIntoModifyReason(String status, String uniqueKey, String fieldName, String entryPointName, ModifyReason_Mdl modifyReason, String docId, String userName, int sNo, DmPreparedStatement psTemp, Connection cnTemp) {
        String sqlQuery = "";

        try {
            if (status.equalsIgnoreCase("1")) {
                sqlQuery = "INSERT INTO modifyReason (docId, modifyReasonCode, modifyReason, u_Name, u_EntDt, v_Sno, mode, sNo,"
                        + "v_Type, recId, v_Date, postChangeValue, "
                        + "preChangeValue, preU_Name, preU_EntDt) "
                        + "VALUES (:docId, :modifyReasonCode, :modifyReason, :u_Name, :u_EntDt, :v_Sno, :mode, :sNo,"
                        + ":v_Type, :recId, :v_Date, :postChangeValue, "
                        + ":preChangeValue, :preU_Name, :preU_EntDt) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("modifyReasonCode", DmShared.foriegnKey(modifyReason.getSharedKeyFieldCode()));
                psTemp.setString("modifyReason", DmShared.foriegnKey(modifyReason.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("v_Sno", "1");
                psTemp.setString("mode", DmShared.xNull(modifyReason.getMode()));
                psTemp.setString("sNo", "1");
                psTemp.setString("v_Type", DmShared.foriegnKey(modifyReason.getV_type()));
                psTemp.setString("recId", DmShared.xNull(modifyReason.getRecId()));
                psTemp.setDate("v_Date", DmServer.convertDate(modifyReason.getV_date()));
                psTemp.setString("postChangeValue", DmShared.xNull(modifyReason.getPostChangeValue()));
                psTemp.setString("preChangeValue", DmShared.xNull(modifyReason.getPreChangeValue()));
                psTemp.setString("preU_Name", DmShared.xNull(modifyReason.getPreU_Name()));
                psTemp.setTimestamp("preU_EntDt", DmShared.iNull(modifyReason.getPreU_EntDt()).longValue() > 0 ? new Timestamp(modifyReason.getPreU_EntDt()) : null);

                psTemp.executeUpdate();
                psTemp.close();
            } else if (status.equalsIgnoreCase("3")) {
                sqlQuery = "INSERT INTO modifyReason (docId, modifyReasonCode, modifyReason, u_Name, u_EntDt, v_Sno, mode, sNo,"
                        + "v_Type, recId, v_Date, postChangeValue, "
                        + "preChangeValue, preU_Name, preU_EntDt,fieldName,uniqueKey,entryPointName) "
                        + "VALUES (:docId, :modifyReasonCode, :modifyReason, :u_Name, :u_EntDt, :v_Sno, :mode, :sNo,"
                        + ":v_Type, :recId, :v_Date, :postChangeValue, "
                        + ":preChangeValue, :preU_Name, :preU_EntDt,:fieldName,:uniqueKey,:entryPointName) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("fieldName", fieldName);
                psTemp.setString("uniqueKey", uniqueKey);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("entryPointName", entryPointName);
                psTemp.setString("modifyReasonCode", DmShared.foriegnKey(modifyReason.getSharedKeyFieldCode()));
                psTemp.setString("modifyReason", DmShared.foriegnKey(modifyReason.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("v_Sno", DmShared.iNull(sNo).toString());
                psTemp.setString("mode", DmShared.xNull(modifyReason.getMode()));
                psTemp.setString("sNo", DmShared.iNull(sNo).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(modifyReason.getV_type()));
                psTemp.setString("recId", DmShared.xNull(modifyReason.getRecId()));
                psTemp.setDate("v_Date", DmServer.convertDate(modifyReason.getV_date()));
                psTemp.setString("postChangeValue", DmShared.xNull(modifyReason.getPostChangeValue()).trim());
                psTemp.setString("preChangeValue", DmShared.xNull(modifyReason.getPreChangeValue()).trim());
                psTemp.setString("preU_Name", DmShared.xNull(modifyReason.getPreU_Name()));
                psTemp.setTimestamp("preU_EntDt", DmShared.iNull(modifyReason.getPreU_EntDt()).longValue() > 0 ? new Timestamp(modifyReason.getPreU_EntDt()) : null);

                psTemp.executeUpdate();
                psTemp.close();
            }

            modifyReason.setPreChangeValue("");
            modifyReason.setPostChangeValue("");

        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());

        }
    }

}
