package aarogya.selfToolkit.stStatic.server;

import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemStatus;
import dataman.dmBase.dmSession.server.DmRSSPrivate;

public class STApprovals {

    /*=====================<< For requisition Approvals >>======================*/
    public static String requisitionApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {

            STServer.chkIfDocumentUsed("requisitionDocId", "indentDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("requisitionDocId", "stockHeadDetail", keyCode, cnTemp);

            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from Requisition where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update Requisition set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update Requisition set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For enquiry Approvals >>======================*/
    public static String enquiryApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from enquiry where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update enquiry set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update enquiry set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For indent Approvals >>======================*/
    public static String indentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            ////////////////////////////////////////////////////
            STServer.chkIfDocumentUsed("indentDocId", "pOrderIndentDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("indentDocId", "enquiryIndentDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("indentDocId", "quotationIndentDetail", keyCode, cnTemp);
//////////////////////////////////////////////////////
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from indent where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update indent set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = "select docId "
                                    + "from indentDetail "
                                    + " where docId = :docId "
                                    + " and (isNull(orderedQty,0) <> 0 OR assignedTo IS NOT NULL )";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new Exception("Document is in use. you can not unapprove.");
                            }
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update indent set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For pOrder Approvals >>======================*/
    public static String purchaseOrderApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp, DmRSSPrivate servletInstance) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";
        String v_Type = "";
        try {

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("poDocId", "pChallanDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("poDocId", "pBillDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("poDocId", "gateAdviceDetail", keyCode, cnTemp);
            /*==============================<< *************************** >>====================================*/

            strSQL = "select approvedBy,approvalDateTime,v_Type,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from pOrder where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                v_Type = DmShared.xNull(rsTemp.getString("v_Type"));
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update pOrder set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                        STServer.sendMail(DmShared.iNull(keyCode).toString(), v_Type, servletInstance);
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = "select docId "
                                    + "from pOrderDetail "
                                    + "where docId = :docId and isNull(challanedQty,0) <> 0 ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new Exception("Document is in use. you can not unapprove.");
                            }
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update pOrder set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For GRN Approvals >>======================*/
    public static String grnApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pcDocId", "pBillDetail", keyCode, cnTemp);

            sqlQuery = "select approvedBy, approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from pChallan where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = " Update pChallan set "
                                + " u_EntDt_LatestLine=:uEntDt"
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :docId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = "select docId"
                                    + " from pChallanDetail"
                                    + " where docId = :docId and ISNULL(billedQty,0) <> 0 ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new Exception("Document is in use. you can not unapprove.");
                            }
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update pChallan set"
                                    + " u_EntDt_LatestLine=:uEntDt"
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = case when isSystem_Generated=0 then 0 when isSystem_Generated=1 then 1 end "
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.executeUpdate();
                            psTemp.close();

                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For indent Approvals >>======================*/
    public static String itemTransferApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from stockHead where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update stockHead set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update stockHead set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=========================<< For Item Activation >>============================*/
    public static String itemActivation(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            sqlQuery = "select statusChangedBy,statusChangedDate,isActive,"
                    + "(case when :userName IN (ISNULL(statusChangedBy,''),'SA','SUPER') then 1 else 0 end) canActive "
                    + "from itemMast where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("code", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (!DmShared.xNull(rsTemp.getString("isActive")).trim().equalsIgnoreCase(DmShared.xNull(String.valueOf(ItemStatus.ACTIVE.getId())))) {

                        rsTemp.updateString("statusChangedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("statusChangedDate", DmServer.getServerTimestamp());
                        rsTemp.updateString("isActive", "1");
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = " Update itemMast set "
                                + " statusChangedDate=:statusChangedDate "
                                + " where code=:code ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("statusChangedDate", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Item already active.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("statusChangedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canActive")).longValue() == 1) {

                            rsTemp.updateString("statusChangedBy", DmShared.xNull(userName).trim());
                            rsTemp.updateTimestamp("statusChangedDate", DmServer.getServerTimestamp());
                            rsTemp.updateString("isActive", "0");
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update itemMast set "
                                    + " statusChangedDate=:statusChangedDate "
                                    + " where code=:code ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("code", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("statusChangedDate", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to inactive item.");
                        }
                    } else {
                        throw new Exception("Item already inactive.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Sale Order Approvals >>======================*/
    public static String proformaInvoiceApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from proformaInvoice where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update proformaInvoice set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            /*==============================<< Check if document is in use >>====================================*/
//                            strSQL = "select docId "
//                                    + "from proformaInvoiceDetail "
//                                    + "where docId = :docId "
//                                    + "and (isNull(dispatchedQty,0) <> 0 OR isNull(woQty,0) <> 0) ";
//                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
//                            rsTemp = psTemp.executeQuery();
//                            if (rsTemp.next()) {
//                                throw new Exception("Document is in use. you can not unapprove.");
//                            }
//                            rsTemp.close();
//                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/
                            strSQL = " Update proformaInvoice set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Sale Order Approvals >>======================*/
    public static String saleOrderApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from sOrder where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update sOrder set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            /*==============================<< Check if document is in use >>====================================*/
                            strSQL = "select docId "
                                    + "from sOrderDetail "
                                    + "where docId = :docId "
                                    + "and (isNull(dispatchedQty,0) <> 0 OR isNull(woQty,0) <> 0) ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new Exception("Document is in use. you can not unapprove.");
                            }
                            rsTemp.close();
                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/

                            strSQL = " Update sOrder set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Work Order Approvals >>======================*/
    public static String workOrderApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from wOrder where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update wOrder set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            /*==============================<< Check if document is in use >>====================================*/
                            strSQL = " select COUNT(*) AS cnt "
                                    + " from wOrderDetail wod "
                                    + " left join wOrderSODetail wosod ON wosod.docId = wod.docId AND wosod.v_Sno = wod.v_Sno "
                                    + " where wod.docId = :docId "
                                    + " and isNull(wosod.producedQty,0) <> 0 ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                                    throw new Exception("Document is in use. you can not unapprove.");
                                }
                            }
                            rsTemp.close();
                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/

                            strSQL = " Update wOrder set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    public static boolean checkPBillLocationAccounts(String pBillDocid, Connection cnTemp) {

        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        boolean isLocationAccounts = false;

        try {

            sqlQuery = "  select                                \n"
                    + "     env.isBillH_Approval, bhlm.name,  "
                    + "     isNull(bh.approvedBy,'') approvedBY     \n"
                    + "     from pBill pb                       \n"
                    + "     left join billHandleLocationMaster  bhlm on bhlm.code = pb.lastloc   \n"
                    + "     left join enviro env on env.site_Code = pb.site_Code            \n"
                    + "     left join billHandleDetail bhd on bhd.modDocId = pb.docid   "
                    + "     left join billHandle bh on bh.docId = bhd.docId             "
                    + "     where pb.docid =:pBillDocid  and isnull(retransfer,0)=0     ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("pBillDocid", DmShared.iNull(pBillDocid).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();

            if (DmShared.iNull(rsTemp.getString("isBillH_Approval")).longValue() == 1) {

                if ((!DmShared.xNull(rsTemp.getString("Name")).equalsIgnoreCase("Accounts"))) {
                    throw new DmException(421, "P.Bill location is not accounts.");
                }
                if ((DmShared.xNull(rsTemp.getString("approvedBY")).equalsIgnoreCase(""))) {
                    throw new DmException(420, "P.Bill location accounts not approved.");
                }

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
        return isLocationAccounts;
    }

    /*=====================<< For Purchase Bill Approvals >>======================*/
    public static String purchaseBillApproval(boolean isAutoApprove, WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {
            if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                try {
                    checkPBillLocationAccounts(keyCode, cnTemp);
                } catch (DmException excp) {
                    if (!isAutoApprove) {
                        throw excp;
                    } else {
                        return null;
                    }
                }
            }

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("pbDocId", "finPayReqEnt", keyCode, cnTemp);

            sqlQuery = "    select approvedBy,approvalDateTime,"
                    + " (case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + " from pBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        //============= Ledger posting will be from Purchase Finalization ====================//
                        sqlQuery = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :docId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = " Update pBill set"
                                + " u_EntDt_LatestLine=:uEntDt"
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = case when isSystem_Generated=0 then 0 when isSystem_Generated=1 then 1 end "
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = " Update pBill set"
                                    + " u_EntDt_LatestLine=:uEntDt"
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();

                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
            manageGstTransRecord(keyCode, walkThroughMove == WalkThroughEnum.ACCEPT, cnTemp);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For lab Approvals >>======================*/
    public static String labApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {

//            STServer.chkIfDocumentUsed("requisitionDocId", "indentDetail", keyCode, cnTemp);
//            STServer.chkIfDocumentUsed("requisitionDocId", "stockHeadDetail", keyCode, cnTemp);
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from lab where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update lab set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = "SELECT COUNT(usedDocId) as cnt "
                                    + " FROM lab "
                                    + " WHERE docId = :docId "
                                    + " AND usedDocId IS NOT NULL ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                                    throw new Exception("Document is in use. you can not unapprove.");
                                }
                            }
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update lab set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Price list Approvals >>======================*/
    public static String priceListApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("plDocId", "sOrder", keyCode, cnTemp);

            strSQL = " select approvedBy,approvalDateTime,"
                    + " (case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + " from priceList where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update priceList set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            /*==============================<< Check if document is in use >>====================================*/
//                            strSQL = " select pld.docId "     //TBC
//                                    + " from priceListDetail pld "
//                                    + " left join sOrder so on so.plDocId = pld.docid "
//                                    + " left join sOrderDetail sod on so.docid = sod.docid"
//                                    + " where pld.docId = :docId "
//                                    + " and (isNull(sod.dispatchedQty,0) <> 0 OR isNull(sod.woQty,0) <> 0)  ";
//                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
//                            rsTemp = psTemp.executeQuery();
//                            if (rsTemp.next()) {
//                                throw new Exception("Document is in use. you can not unapprove.");
//                            }
//                            rsTemp.close();
//                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/
                            strSQL = " Update priceList set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Order Transfer Approvals >>======================*/
    public static String orderTransferApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from orderTransfer where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update orderTransfer set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                    manageSOQty(keyCode, false, cnTemp);
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            /*==============================<< Check if document is in use >>====================================*/
//                            strSQL = "select docId "
//                                    + "from orderTransferDetail "
//                                    + "where docId = :docId "
//                                    + "and isNull(quantity,0) <> 0 ";
//                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
//                            rsTemp = psTemp.executeQuery();
//                            if (rsTemp.next()) {
//                                throw new Exception("Document is in use. you can not unapprove.");
//                            }
//                            rsTemp.close();
//                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/
                            strSQL = " Update orderTransfer set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                    manageSOQty(keyCode, true, cnTemp);
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    private static void manageSOQty(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        try {
            manageSOTransfer(docId, isDelete, cnTemp);
            manageSORecieve(docId, isDelete, cnTemp);
        } catch (Exception e) {
            throw e;
        } finally {
            try {

            } catch (Exception e) {
            }
        }
    }

    private static void manageSOTransfer(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String strSQL, sOrderDocId = "";
        double orderTransIsuQty, soAprTrnsIsuQty, soTransferQty;
        int isStatus;

        try {
            strSQL = "SELECT so.docId AS sOrderDocId"
                    + "    , sod.v_SNo AS sOrderVSno"
                    + "    , woSod.ordTransferIsuQty "
                    + "    , sod.skuQty as sOrderSKUQty"
                    + "    , sod.transferIsuQty"
                    + "    , sod.aprtransferIsuQty"
                    + "    , sod.isStatus"
                    + "    , so.approvedBy"
                    + "    , so.recId AS sOrderNo"
                    + "    FROM sOrderDetail sod"
                    + "    LEFT JOIN sOrder so ON so.docId = sod.docId"
                    + "    INNER JOIN ("
                    + "			select otid.fromSoDocId, otid.fromSoV_SNo, SUM(iid.quantity) AS ordTransferIsuQty "
                    + "				from orderTransferItemId otid"
                    + "				LEFT JOIN itemId iid ON iid.code = otid.itemId"
                    + "				where otid.docId=:otDocId "
                    + "				Group by otid.fromSoDocId,otid.fromSoV_SNo"
                    + "    ) woSod ON sod.docId = woSod.fromSoDocId AND sod.v_Sno = woSod.fromSoV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("otDocId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Transfer Sale Order is not Approved");
                }
                if (isStatus == 1) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Transfer Sale Order is Cancelled");
                }
//                if (isStatus == 2) {
//                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Sale Order is Dispatched");
//                }

                orderTransIsuQty = rsTemp.getDouble("ordTransferIsuQty");
                soTransferQty = rsTemp.getDouble("transferIsuQty");
                soAprTrnsIsuQty = rsTemp.getDouble("aprtransferIsuQty");

                if (isDelete) {
                    soAprTrnsIsuQty -= orderTransIsuQty;
                } else {
                    soAprTrnsIsuQty += orderTransIsuQty;
                }

                if (soAprTrnsIsuQty > soTransferQty && !isDelete) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Transfer Sales Order overflows with Qty." + DmServer.vNull(soAprTrnsIsuQty - soTransferQty));
                }
                if (soAprTrnsIsuQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Transfer Sales Order overflows with Qty. " + DmServer.vNull(soAprTrnsIsuQty));
                }

                if (!sOrderDocId.equalsIgnoreCase("")) {
                    sOrderDocId += ",";
                }

                sOrderDocId += DmShared.iNull(rsTemp.getString("sOrderDocId")).toString();

                strSQL = "UPDATE sOrderDetail SET"
                        + " aprTransferIsuQty =:soTransferQty"
                        + " where docId =:docId AND v_SNo =:v_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("sOrderDocId")).toString());
                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("sOrderVSno")).toString());
                psTemp1.setString("soTransferQty", DmServer.vNull(soAprTrnsIsuQty));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!sOrderDocId.equalsIgnoreCase("")) {
                strSQL = "update sOrder set u_EntDt_LatestLine = :uEntDt where docId in (" + sOrderDocId + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void manageSORecieve(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String strSQL, sOrderDocId = "";
        double orderTransRecvQty, soAprTransRecQty, soRecieveQty;
        Integer isStatus;

        try {
            strSQL = "SELECT so.docId AS sOrderDocId"
                    + "    , sod.v_SNo AS sOrderVSno"
                    + "    , woSod.ordTransferRecvQty "
                    + "    , sod.skuQty as sOrderSKUQty"
                    + "    ,sod.transferRecvQty"
                    + "    ,sod.aprtransferRecvQty"
                    + "    , sod.isStatus"
                    + "    , so.approvedBy"
                    + "    , so.recId AS sOrderNo"
                    + "    FROM sOrderDetail sod"
                    + "    LEFT JOIN sOrder so ON so.docId = sod.docId"
                    + "    INNER JOIN ("
                    + "			select otd.soDocId, otd.soV_SNo, SUM(otd.quantity) AS ordTransferRecvQty "
                    + "				from orderTransferDetail otd"
                    + "				where otd.docId=:otDocId "
                    + "				Group by otd.soDocId, otd.soV_SNo"
                    + "    ) woSod ON sod.docId = woSod.soDocId AND sod.v_Sno = woSod.soV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("otDocId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Recieve Sale Order is not Approved");
                }
                if (isStatus == 1) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Recieve Sale Order is Cancelled");
                }
                if (isStatus.intValue() == 2) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Sale Order is Dispatched");
                }
                orderTransRecvQty = rsTemp.getDouble("ordTransferRecvQty");
                soRecieveQty = rsTemp.getDouble("transferRecvQty");
                soAprTransRecQty = rsTemp.getDouble("aprtransferRecvQty");

                if (isDelete) {
                    soAprTransRecQty -= orderTransRecvQty;
                } else {
                    soAprTransRecQty += orderTransRecvQty;
                }

                if (soAprTransRecQty > soRecieveQty) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Transfer Sales Order overflows with Qty." + DmServer.vNull(soAprTransRecQty - soRecieveQty));
                }
                if (soAprTransRecQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sOrderDocId")), DmShared.xNull(rsTemp.getString("sOrderVSno")), "Transfer Sales Order overflows with Qty." + DmServer.vNull(soAprTransRecQty));
                }

                if (!sOrderDocId.equalsIgnoreCase("")) {
                    sOrderDocId += ",";
                }

                sOrderDocId += DmShared.iNull(rsTemp.getString("sOrderDocId")).toString();

                strSQL = "UPDATE sOrderDetail SET"
                        + " aprTransferRecvQty =:soRecQty"
                        + " where docId = :docId "
                        + " AND v_SNo = :v_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("sOrderDocId")).toString());
                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("sOrderVSno")).toString());
                psTemp1.setString("soRecQty", DmServer.vNull(soAprTransRecQty));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!sOrderDocId.equalsIgnoreCase("")) {
                strSQL = "update sOrder set u_EntDt_LatestLine = :uEntDt where docId in (" + sOrderDocId + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /*=====================<< For Sales Invoice Approvals >>======================*/
    public static String salesInvoiceApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from sBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        /*==============================<< ACCOUNT POSTING >>====================================*/
                        strSQL = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :docId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.executeUpdate();
                        psTemp.close();
                        /*==============================<< ************************** >>====================================*/

                        strSQL = " Update sBill set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

//                            /*==============================<< ACCOUNT POSTING >>====================================*/
                            strSQL = "UPDATE ledger SET"
                                    + " isPosted = 0"
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.executeUpdate();
                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/
                            strSQL = " Update sBill set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Commission Finalization Approvals >>======================*/
    public static String commissionFinalizationApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from sCommission where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update sCommission set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update sCommission set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Sales Target Approvals >>======================*/
    public static String salesTargetApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from sTarget where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update sTarget set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update sTarget set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Purchase Bill Approvals >>======================*/
    public static String purchaseReturnApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", keyCode, cnTemp);

            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from pBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :docId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = " Update pBill set"
                                + " u_EntDt_LatestLine=:uEntDt"
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = case when isSystem_Generated=0 then 0 when isSystem_Generated=1 then 1 end "
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = " Update pBill set"
                                    + " u_EntDt_LatestLine=:uEntDt"
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Deckle Order Approvals >>======================*/
    public static String deckleOrderApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from dOrder where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update dOrder set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            /*==============================<< Check if document is in use >>====================================*/
                            strSQL = " select COUNT(*) AS cnt "
                                    + " from dOrderDetail wod "
                                    + " left join wOrderSODetail wosod ON wosod.docId = wod.docId AND wosod.v_Sno = wod.v_Sno "
                                    + " where wod.docId = :docId "
                                    + " and isNull(wosod.producedQty,0) <> 0 ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                                    throw new Exception("Document is in use. you can not unapprove.");
                                }
                            }
                            rsTemp.close();
                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/

                            strSQL = " Update dOrder set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Contractual Sale Order Approvals >>======================*/
    public static String contractualSaleOrderApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from csOrder where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update csOrder set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            /*==============================<< Check if document is in use >>====================================*/
                            strSQL = "select docId "
                                    + "from csOrderDetail "
                                    + "where docId = :docId "
                                    + "and isNull(orderedSKUQty,0) <> 0 ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new Exception("Document is in use. you can not unapprove.");
                            }
                            rsTemp.close();
                            psTemp.close();
                            /*==============================<< ************************** >>====================================*/
                            strSQL = " Update csOrder set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    /*=====================<< For Purchase Finalization Approvals >>======================*/
    public static String purchaseFinalizationApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", pbDocId = "";

        try {
            sqlQuery = "SELECT TOP 1 pfd.pbDocId \n"
                    + " FROM pFinalizationDetail pfd \n"
                    + " WHERE pfd.docId = :docId "
                    + " AND pfd.pbDocId IS NOT NULL ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                pbDocId = DmShared.xNull(rsTemp.getString("pbDocId"));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check if document is in use >>====================================*/
            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from pFinalization where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        //=========== Ledger Posting of Purchase Bill ================//
                        sqlQuery = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :pbDocId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("pbDocId", DmShared.xNull(pbDocId));
                        psTemp.executeUpdate();
                        psTemp.close();

                        //=========== Ledger Posting of Purchase Finalization ================//
                        sqlQuery = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :docId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = " Update pFinalization set"
                                + " u_EntDt_LatestLine=:uEntDt"
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            //=========== Ledger Posting of Purchase Bill ================//
                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = 0"
                                    + " WHERE docId = :pbDocId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("pbDocId", DmShared.xNull(pbDocId));
                            psTemp.executeUpdate();
                            psTemp.close();

                            //=========== Ledger Posting of Purchase Finalization ================//
                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = 0 "
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = " Update pFinalization set"
                                    + " u_EntDt_LatestLine=:uEntDt"
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    public static String assetMasterApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from assetStockHead where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update assetStockHead set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update assetStockHead set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    public static String assetTransferApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL = "";

        try {
            strSQL = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from assetStockHead where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        strSQL = " Update assetStockHead set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            strSQL = " Update assetStockHead set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
        return null;
    }

    public static void manageGstTransRecord(String docid, boolean isApprove, Connection cnTemp) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        try {
            if (DmShared.iNull(docid).longValue() > 0) {
                /*==============================<< SAVE IN TRANSPORTER TRANS >>====================================*/

                sqlQuery = " UPDATE \n"
                        + " gstTrans \n"
                        + " SET isStatus = :isStatus \n"
                        + " WHERE docid = :docid \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", docid);
                psTemp.setInt("isStatus", isApprove ? 0 : 1);
                psTemp.executeUpdate();
                psTemp.close();

            }

            /*==============================<< ************************ >>====================================*/
        } catch (Exception e) {
            throw e;
        }
    }
}
