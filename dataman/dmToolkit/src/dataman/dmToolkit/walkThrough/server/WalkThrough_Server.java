package dataman.dmToolkit.walkThrough.server;

import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughApplicable_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public class WalkThrough_Server {
    
    public static void deleteWalkThrough(String wtaCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "delete from walkThroughFootPrint where wtaCode = :wtaCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("wtaCode", DmShared.iNull(wtaCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from WalkThroughApplicable where wtaCode = :wtaCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("wtaCode", DmShared.iNull(wtaCode).toString());
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
    
    public static String applyWalkThrough(WalkThroughDefault_Mdl walkThroughFor, String userName, String v_Type, String docId, Connection cnTemp, DmRSS rss) throws DmException {
        String sqlQuery, sqlCondition = "", walkThroughCode = "", wtaCode = "";
        String userNameClause = "", siteCode = "", departmentCode = "", v_TypeClause = "", valueUpto = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<WalkThroughApplicable_Mdl> walkThroughApplicableList = new ArrayList<>();
        WalkThroughApplicable_Mdl walkThroughApplicable;
        
        try {
            
            if (walkThroughFor != null) {
                
                sqlCondition += "and (wtd.userName in ('' ";
                if (!DmShared.xNull(walkThroughFor.getUserName()).trim().equalsIgnoreCase("")) {
                    userNameClause = walkThroughFor.getUserName();
                    sqlCondition += ",:userName";
                }
                sqlCondition += ") or wtd.userName is null) ";
                
                sqlCondition += "and (";
                if (!DmShared.xNull(walkThroughFor.getSiteCode()).trim().equalsIgnoreCase("")) {
                    siteCode = walkThroughFor.getSiteCode();
                    sqlCondition += "wtd.site_code = :site_code or ";
                }
                sqlCondition += "wtd.site_code is null) ";
                
                sqlCondition += "and (";
                if (!DmShared.xNull(walkThroughFor.getvType()).trim().equalsIgnoreCase("")) {
                    v_TypeClause = walkThroughFor.getvType();
                    sqlCondition += "wtd.v_Type = :v_Type or ";
                }
                sqlCondition += "wtd.v_Type is null) ";
                
                sqlCondition += "and (";
                if (!DmShared.xNull(walkThroughFor.getDepartmentCode()).trim().equalsIgnoreCase("")) {
                    departmentCode = walkThroughFor.getDepartmentCode();
                    sqlCondition += "wtd.departmentCode = :departmentCode or ";
                }
                sqlCondition += "wtd.departmentCode is null) ";
                
                sqlCondition += "and (";
                if (DmShared.vNull(walkThroughFor.getValueUpto()).doubleValue() != 0) {
                    valueUpto = walkThroughFor.getValueUpto();
                    sqlCondition += "wtd.valueUpto >= :valueUpto or ";
                }
                sqlCondition += "wtd.valueUpto = 0) ";
                
                sqlQuery = "select  wtd.code,wtd.userName, wtd.site_code,wtd.v_Type,wtd.departmentCode,wtd.valueUpto "
                        + "from walkThroughDefault wtd "
                        + "where 1 = 1 "
                        + sqlCondition;
                
                sqlQuery = "select top 1 * "
                        + "from ( "
                        + sqlQuery
                        + ") as ord "
                        + "order by  "
                        + "(case when userName is null then 0 else 1 end) + "
                        + "(case when site_code is null then 0 else 1 end) + "
                        + "(case when v_Type is null then 0 else 1 end) + "
                        + "(case when departmentCode is null then 0 else 1 end) + "
                        + "(case when valueUpto = 0 then 0 else 1 end) desc, "
                        + "valueUpto ";
                
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                
                if (!DmShared.xNull(userNameClause).trim().equalsIgnoreCase("")) {
                    psTemp.setString("userName", userNameClause);
                }
                if (!DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                    psTemp.setString("site_code", siteCode);
                }
                if (!DmShared.xNull(v_TypeClause).trim().equalsIgnoreCase("")) {
                    psTemp.setString("v_Type", v_TypeClause);
                }
                if (!DmShared.xNull(departmentCode).trim().equalsIgnoreCase("")) {
                    psTemp.setString("departmentCode", departmentCode);
                }
                if (!DmShared.xNull(valueUpto).trim().equalsIgnoreCase("")) {
                    psTemp.setString("valueUpto", DmServer.vNull(valueUpto, "0.00"));
                }
                
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    walkThroughCode = rsTemp.getString("code");
                }
                rsTemp.close();
                psTemp.close();
                
                sqlQuery = "select	wtd.authority,wtd.linkedUser  "
                        + "from walkThroughDetail wtd "
                        + "where wtd.code  = :code "
                        + "order by wtd.v_SNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(walkThroughCode).toString());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    walkThroughApplicable = new WalkThroughApplicable_Mdl();
                    walkThroughApplicable.setAuthority(DmShared.xNull(rsTemp.getString("authority")));
                    walkThroughApplicable.setLinkUser(DmShared.xNull(rsTemp.getString("linkedUser")));
                    walkThroughApplicableList.add(walkThroughApplicable);
                }
                rsTemp.close();
                psTemp.close();
                wtaCode = applyWalkThrough(walkThroughApplicableList, userName, v_Type, docId, cnTemp, rss);
            }
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
        
        return wtaCode;
    }
    
    public static String applyWalkThrough(List<WalkThroughApplicable_Mdl> walkThroughApplicableList, String userName, String v_Type, String docId, Connection cnTemp, DmRSS rss) throws DmException {
        String sqlQuery, code = "";
        DmPreparedStatement psTemp = null;
        WalkThroughApplicable_Mdl walkThroughApplicable;
        int vSNo = 0;
        
        try {
            
            if (walkThroughApplicableList != null) {
                
                for (int i = 0; i < walkThroughApplicableList.size(); i++) {
                    walkThroughApplicable = walkThroughApplicableList.get(i);
                    
                    if (walkThroughApplicable != null) {
                        if (!(DmShared.xNull(walkThroughApplicable.getAuthority()).trim().equalsIgnoreCase("")
                                || DmShared.xNull(walkThroughApplicable.getLinkUser()).trim().equalsIgnoreCase(""))) {
                            
                            vSNo++;
                            if (DmShared.xNull(code).trim().equalsIgnoreCase("")) {
                                code = DmServer.getSerialNoBigInt(DmServer.getSsnSiteCode(rss), DmServer.getSsnVoucherPrefix(rss), "WalkThroughApplicable", cnTemp);
                                sqlQuery = "insert into WalkThroughFootPrint(wtaCode,v_SNo,wtfTimeStamp,linkV_Type,linkDocId,authority,linkedUser) "
                                        + "values (:wtaCode,:v_SNo,:wtfTimeStamp,:linkV_Type,:linkDocId,:authority,:linkedUser) ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("wtaCode", DmShared.iNull(code).toString());
                                psTemp.setString("v_SNo", DmShared.iNull(vSNo).toString());
                                psTemp.setString("wtfTimeStamp", DmShared.iNull(new java.util.Date().getTime()).toString());
                                psTemp.setString("linkV_Type", DmShared.iNull(v_Type).toString());
                                psTemp.setString("linkDocId", DmShared.iNull(docId).toString());
                                psTemp.setString("authority", DmShared.xNull(walkThroughApplicable.getAuthority()));
                                psTemp.setString("linkedUser", DmShared.xNull(walkThroughApplicable.getLinkUser()));
                                psTemp.executeUpdate();
                                psTemp.close();
                            }
                            
                            sqlQuery = "insert into WalkThroughApplicable(wtaCode,v_SNo,authority,linkedUser) "
                                    + "values (:wtaCode,:v_SNo,:authority,:linkedUser) ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("wtaCode", DmShared.iNull(code).toString());
                            psTemp.setString("v_SNo", DmShared.iNull(vSNo).toString());
                            psTemp.setString("authority", DmShared.xNull(walkThroughApplicable.getAuthority()));
                            psTemp.setString("linkedUser", DmShared.xNull(walkThroughApplicable.getLinkUser()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        
        return code;
    }
    
    public static void walkThrough(WalkThroughEnum walkThroughMove, String wtaCode, String wtaV_SNo, String wtfTimeStamp, String userName, String remark, Connection cnTemp, IWalkThrough walkThroughCompleted) throws DmException {
        String sqlQuery;
        boolean changeStatus = false;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String insWtaV_SNo = "", insLinkV_Type = "", insLinkDocId = "", insAuthority = "", insLinkedUser = "", insStatus = "";
        
        try {
            
            sqlQuery = "select  wtf.wtaCode,wtf.authority,wtf.linkedUser,wtf.remark,wtf.u_Name,wtf.u_EntDt,wtf.isStatus,wtf.linkV_Type,wtf.linkDocId, "
                    + "wtaNext.v_SNo as nextV_SNo,wtaNext.authority as nextAuthority,wtaNext.linkedUser as nextLinkedUser, "
                    + "wtaPrv.v_SNo as prvV_SNo,wtaPrv.authority as prvAuthority,wtaPrv.linkedUser as prvLinkedUser "
                    + "from walkThroughFootPrint wtf "
                    + "left join walkThroughApplicable wtaNext on wtf.wtaCode = wtaNext.wtaCode and wtaNext.v_SNo = (:v_SNo + 1) "
                    + "left join walkThroughApplicable wtaPrv on wtf.wtaCode = wtaPrv.wtaCode and wtaPrv.v_SNo = (:v_SNo - 1) "
                    + "where wtf.wtaCode = :wtaCode and wtf.v_SNo = :v_SNo and wtf.wtfTimeStamp = :wtfTimeStamp ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("wtaCode", DmShared.iNull(wtaCode).toString());
            psTemp.setString("v_SNo", DmShared.iNull(wtaV_SNo).toString());
            psTemp.setString("wtfTimeStamp", DmShared.iNull(wtfTimeStamp).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String validUser = DmShared.xNull(rsTemp.getString("linkedUser")).toLowerCase();
                
                if (validUser.contains(userName.trim().toLowerCase() + ",")) {
                    if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 0) {
                        insLinkDocId = DmShared.iNull(rsTemp.getString("linkDocId")).toString();
                        insLinkV_Type = DmShared.iNull(rsTemp.getString("linkV_Type")).toString();
                        if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                            insStatus = "1";
                            if (DmShared.iNull(rsTemp.getString("nextV_SNo")).longValue() == 0) {
                                changeStatus = true;
                            } else {
                                insWtaV_SNo = DmShared.iNull(rsTemp.getString("nextV_SNo")).toString();
                                insAuthority = DmShared.xNull(rsTemp.getString("nextAuthority"));
                                insLinkedUser = DmShared.xNull(rsTemp.getString("nextLinkedUser"));
                            }
                        } else {
                            insStatus = "2";
                            if (DmShared.iNull(rsTemp.getString("prvV_SNo")).longValue() == 0) {
                                throw new Exception("Invalid move.");
                            } else {
//                            if (DmShared.iNull(rsTemp.getString("nextV_SNo")).longValue() == 0) {
//                                changeStatus = true;
//                            }
                                insWtaV_SNo = DmShared.iNull(rsTemp.getString("prvV_SNo")).toString();
                                insAuthority = DmShared.xNull(rsTemp.getString("prvAuthority"));
                                insLinkedUser = DmShared.xNull(rsTemp.getString("prvLinkedUser"));
                            }
                        }
                        
                        rsTemp.updateString("remark", DmShared.xNull(remark));
                        rsTemp.updateString("u_Name", DmShared.xNull(userName));
                        rsTemp.updateTimestamp("u_EntDt", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateString("isStatus", DmShared.iNull(insStatus).toString());
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();
                    } else {
                        if (walkThroughMove == WalkThroughEnum.DECLINE) {
                            if (DmShared.iNull(rsTemp.getString("nextV_SNo")).longValue() == 0) {
                                insLinkDocId = DmShared.iNull(rsTemp.getString("linkDocId")).toString();
                                insLinkV_Type = DmShared.iNull(rsTemp.getString("linkV_Type")).toString();
                                insAuthority = DmShared.xNull(rsTemp.getString("authority"));
                                insLinkedUser = DmShared.xNull(rsTemp.getString("linkedUser"));
                                rsTemp.close();
                                psTemp.close();

                                //if it is last record then only execute else by pass.
                                sqlQuery = "select	wtf.wtaCode "
                                        + "from walkThroughFootPrint wtf "
                                        + "where wtf.wtaCode =  :wtaCode and "
                                        + "wtf.v_SNo = :v_SNo and "
                                        + "wtf.wtfTimeStamp > :wtfTimeStamp ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("wtaCode", DmShared.iNull(wtaCode).toString());
                                psTemp.setString("v_SNo", DmShared.iNull(wtaV_SNo).toString());
                                psTemp.setString("wtfTimeStamp", DmShared.iNull(wtfTimeStamp).toString());
                                rsTemp = psTemp.executeQuery();
                                if (!rsTemp.next()) {
                                    changeStatus = true;
                                    insWtaV_SNo = wtaV_SNo;
                                }
                                rsTemp.close();
                                psTemp.close();
                            }
                        }
                    }
                    
                    if (DmShared.iNull(insWtaV_SNo).longValue() != 0) {
                        sqlQuery = "insert into walkThroughFootPrint(wtaCode,v_SNo,wtfTimeStamp,linkV_Type,linkDocId,authority,linkedUser) "
                                + "values (:wtaCode, :v_SNo, :wtfTimeStamp,:linkV_Type, :linkDocId, :authority, :linkedUser) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("wtaCode", DmShared.iNull(wtaCode).toString());
                        psTemp.setString("v_SNo", DmShared.iNull(insWtaV_SNo).toString());
                        psTemp.setString("wtfTimeStamp", DmShared.iNull(new java.util.Date().getTime()).toString());
                        psTemp.setString("linkV_Type", DmShared.iNull(insLinkV_Type).toString());
                        psTemp.setString("linkDocId", DmShared.iNull(insLinkDocId).toString());
                        psTemp.setString("authority", DmShared.xNull(insAuthority));
                        psTemp.setString("linkedUser", DmShared.xNull(insLinkedUser));
                        psTemp.executeUpdate();
                    }
                    
                    if (changeStatus) {
                        walkThroughCompleted.changeStatus(walkThroughMove, insLinkDocId, userName, cnTemp);
                    }
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
    }
    
    public static void walkThrough(WalkThrough_Mdl walkThrough, IWalkThrough iWalkThrough, DmRSS rss) throws Exception {
        Connection cnTemp = null;
        String userName;
        DmSavePoint spBegin = null;
        
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(rss);
            
            if (walkThrough.isAccept()) {
                WalkThrough_Server.walkThrough(WalkThroughEnum.ACCEPT, DmShared.xNull(walkThrough.getWtaCode()), DmShared.xNull(walkThrough.getWtaV_SNo()), DmShared.xNull(walkThrough.getWtfTimeStamp()), userName, DmShared.xNull(walkThrough.getRemark()), cnTemp, iWalkThrough);
            } else {
                WalkThrough_Server.walkThrough(WalkThroughEnum.DECLINE, DmShared.xNull(walkThrough.getWtaCode()), DmShared.xNull(walkThrough.getWtaV_SNo()), DmShared.xNull(walkThrough.getWtfTimeStamp()), userName, DmShared.xNull(walkThrough.getRemark()), cnTemp, iWalkThrough);
            }
            spBegin.commit();
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
                throw new DmException(0, e.getMessage());
            }
        }
    }
    
}
