package dataman.dmToolkit.accountPosting.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.accountPosting.shared.AccountPosting_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class AccountPosting_Server {
    
    public static void saveAccountPosting(List<AccountPosting_Mdl> lstAcPost, String keyFieldName, String keyFieldValue, RecId recId,
            String vType, String vPrefix, String siteCode, String vDate, String commonNarration, String refNo, String refDate,
            String chequeNo, String chequeDate, boolean isPosted, boolean isSystemGenerated, Connection cnTemp) throws Exception {
        try {
            saveAccountPosting(lstAcPost, keyFieldName, keyFieldValue, recId, vType, vPrefix, siteCode, vDate, commonNarration, refNo, refDate, chequeNo, chequeDate, isPosted, isSystemGenerated, cnTemp, "ledger");
        } catch (Exception e) {
            throw e;
        }
    }
    
    public static void saveAccountPosting(List<AccountPosting_Mdl> lstAcPost, String keyFieldName, String keyFieldValue, RecId recId,
            String vType, String vPrefix, String siteCode, String vDate, String commonNarration, String refNo, String refDate,
            String chequeNo, String chequeDate, boolean isPosted, boolean isSystemGenerated, Connection cnTemp, String ledgerTableName) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String contraText = "", credit = "", debit = "";
        
        try {
            /*Below line commented By Avinash to handle delete event in transaction forms due to double posting for manage Approval in Purchase Bill Direct and Purchase Return */
//            deleteAccountPosting(keyFieldValue, cnTemp, ledgerTableName);         

            int maxV_Sno = DmShared.vNull(DmServer.executeScalar("select isNull(max(v_Sno),0)+1 from ledger where docid=" + keyFieldValue, cnTemp)).intValue();
            //Ledger Posting Start
            for (int i = 0; i < lstAcPost.size(); i++) {
                credit = "";
                debit = "";
                contraText = "";
                
                if (DmShared.vNull(lstAcPost.get(i).getAmtDr()) > 0) {
                    debit = lstAcPost.get(i).getAmtDr();
                    if (!DmShared.xNull(lstAcPost.get(i).getContraText()).trim().equalsIgnoreCase("")) {
                        contraText = DmServer.prepareContraText(lstAcPost.get(i).getContraText(), DmShared.vNull(debit), "Cr");
                    }
                } else if (DmShared.vNull(lstAcPost.get(i).getAmtCr()) < 0) {
//                    credit = String.valueOf(Math.abs(DmShared.vNull(lstAcPost.get(i).getAmtCr())));
                    credit = DmServer.vNull(Math.abs(DmShared.vNull(lstAcPost.get(i).getAmtCr())),"0.00"); 
                    if (!DmShared.xNull(lstAcPost.get(i).getContraText()).trim().equalsIgnoreCase("")) {
                        contraText = DmServer.prepareContraText(lstAcPost.get(i).getContraText(), DmShared.vNull(credit), "Dr");
                    }
                }
                
                commonNarration = DmShared.xNull(commonNarration).trim();
                if (!commonNarration.equalsIgnoreCase("")) {
                    commonNarration = commonNarration.substring(1, commonNarration.length());
                }
                
                sqlQuery = "Insert Into " + ledgerTableName + "(DocId,Site_Code,V_No,RecIdPrefix,RecId,V_Sno,Narration,V_Type,V_Prefix, "
                        + "SubCode,ContraSub,ContraText,AmtDr,AmtCr,V_Date,RefNo,RefDate,Chq_No,Chq_Date,isPosted,isSystem_Generated) Values("
                        + ":DocId,:Site_Code,:V_No,:RecIdPrefix,:RecId,:V_Sno,:Narration,:V_Type,:V_Prefix, "
                        + ":SubCode,:ContraSub,:ContraText,:AmtDr,:AmtCr,:V_Date,:RefNo,:RefDate,:Chq_No,:Chq_Date,:isPosted, :isSystem_Generated) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", keyFieldValue);
                psTemp.setString("Site_Code", siteCode);
                psTemp.setString("RecId", recId.getRecIdValue());
                psTemp.setString("recIdPrefix", DmShared.xNull(recId.getPrefix()));
                psTemp.setLong("V_No", DmShared.iNull(recId.getCounter()).longValue());
                psTemp.setInt("V_Sno", maxV_Sno + i);
                psTemp.setString("Narration", DmShared.foriegnKey(commonNarration));
                psTemp.setString("V_Type", vType);
                psTemp.setString("V_Prefix", vPrefix);
                psTemp.setString("SubCode", lstAcPost.get(i).getSharedKeyFieldCode());
                psTemp.setString("ContraSub", DmShared.foriegnKey(lstAcPost.get(i).getContraCode()));
                psTemp.setString("ContraText", DmShared.foriegnKey(contraText));
                psTemp.setString("AmtDr", DmShared.foriegnKey(debit));
                psTemp.setString("AmtCr", DmShared.foriegnKey(credit));
                psTemp.setDate("V_Date", DmServer.convertDate(vDate));
                psTemp.setString("RefNo", DmShared.foriegnKey(refNo));
                psTemp.setDate("RefDate", DmServer.convertDate(refDate));
                psTemp.setString("Chq_No", DmShared.foriegnKey(chequeNo));
                psTemp.setDate("Chq_Date", DmServer.convertDate(chequeDate));
                
                if (isSystemGenerated){
                    psTemp.setString("isPosted", "1" );
                }else {
                    psTemp.setString("isPosted", isPosted ? "1" : "0");
                }
                
                psTemp.setString("isSystem_Generated", isSystemGenerated ? "1" : "0");
                
                psTemp.executeUpdate();
                psTemp.close();
            }
            //Ledger Posting End
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
    }
    
    public static void deleteAccountPosting(String keyFieldValue, Connection cnTemp) {
        deleteAccountPosting(keyFieldValue, cnTemp, "ledger");
    }
    
    public static void deleteAccountPosting(String keyFieldValue, Connection cnTemp, String ledgerTableName) {
        DmPreparedStatement psTemp = null;
        
        try {
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "delete from " + ledgerTableName + " where docId = :docId;");
            psTemp.setString("docId", keyFieldValue);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
                
            }
        }
        
    }
    
}
