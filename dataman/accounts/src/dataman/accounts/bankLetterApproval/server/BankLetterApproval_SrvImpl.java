package dataman.accounts.bankLetterApproval.server;

import dataman.accounts.bankLetterApproval.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BankLetterApproval_SrvImpl extends DmRSSPrivate implements BankLetterApproval_Srv {

    private String v_typeShortName = new LibraryVoucherCategory().BANL_LETTER_CREATION;

    @Override
    public DmResultPageing<List<GrdBankLetterApproval_Mdl>> getFillRecords(DefaultValue_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdBankLetterApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdBankLetterApproval_Mdl> lstTemp = new ArrayList<>();
        GrdBankLetterApproval_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";
            join = " ";

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  bnk.v_Date >= :fromDate ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  bnk.v_Date <= :toDate ";
            }

            if (DmShared.xNull(param.getBillStatusCode()).equalsIgnoreCase("All")) {
                condition += "  ";

            } else if (DmShared.xNull(param.getBillStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(bnk.approvedBy,'') = '' ";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = bnk.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getBillStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(bnk.approvedBy,'') <> '' ";
            }

            condition += " AND bnk.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "  select bnk.v_No, bnk.docid,bnk.recid,bnk.v_Date,bnk.fromFinalPaymentDate,bnk.toFinalPaymentDate ,\n"
                    + "       bnk.approvedBy,bnk.preparedby,bnk.u_Name as modifiedBy ,bnk.wtaCode , FORMAT(bnk.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime\n"
                    + "from bankLetter as bnk "
                    + "left join voucher_Type vt on vt.v_Type=bnk.v_Type "
                    + "   " + join
                    + "        WHERE bnk.site_Code =:site_Code and bnk.v_prefix = :vPrefix  and vt.category IN ('" + v_typeShortName + "') and ISNULL(bnk.isDiscarded,0) = 0 " + condition;

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromDate", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("toDate", DmServer.convertDate(param.getToDate()));
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdBankLetterApproval_Mdl();

                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));

                node.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedby")));
                node.setModifiedBy(DmShared.xNull(rsTemp.getString("modifiedBy")));
                node.setBulkPaymentLerrterDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setFromFinalPaymentDate(DmShared.dNull(rsTemp.getDate("fromFinalPaymentDate")));
                node.setToFinalPaymentDate(DmShared.dNull(rsTemp.getDate("toFinalPaymentDate")));
                node.setApprovalDateTime(DmShared.xNull(rsTemp.getString("approvalDateTime")));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));

                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    node.setIsChecked("1");
                } else {
                    node.setIsChecked("0");
                }
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("All");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

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
    public DmResult<DefaultValue_Mdl> getDefalutValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        boolean flag = true;
        String sqlQuery = "", condition = "";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        try {
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//         
//         
//            
//            cnTemp.close();

            LocalDate date = LocalDate.parse(DmServer.convertDate(DmServer.getServerDateTime()[0]).toString());
            dvm.setFromDate((date.minusDays(30).format(dateFormatter)));
            dvm.setToDate(DmServer.getServerDateTime()[0]);

            dvm.setBillStatusCode("P");
            dvm.setBillStatusName("Pending");

            dmrTemp.setData(dvm);
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

}
