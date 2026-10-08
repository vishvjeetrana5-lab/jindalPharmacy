package aarogya.selfToolkit.organizationPopup.server;

import aarogya.selfToolkit.organizationPopup.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrganizationPopup_SrvImpl extends DmRSSPrivate implements OrganizationPopup_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRelation(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Self");
            listNode.setSharedKeyFieldName("Self");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Brother");
            listNode.setSharedKeyFieldName("Brother");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Sister");
            listNode.setSharedKeyFieldName("Sister");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Mother");
            listNode.setSharedKeyFieldName("Mother");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Father");
            listNode.setSharedKeyFieldName("Father");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Wife");
            listNode.setSharedKeyFieldName("Wife");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Husband");
            listNode.setSharedKeyFieldName("Husband");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Son");
            listNode.setSharedKeyFieldName("Son");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Daughter");
            listNode.setSharedKeyFieldName("Daughter");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Grandson");
            listNode.setSharedKeyFieldName("Grandson");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Granddaughter");
            listNode.setSharedKeyFieldName("Granddaughter");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Grandmother");
            listNode.setSharedKeyFieldName("Grandmother");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Grandfather");
            listNode.setSharedKeyFieldName("Grandfather");
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
    public DmResultPageing<List<TxtHCorporateEmployee_Mdl>> getTxtHCorporate(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCorporateEmployee_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCorporateEmployee_Mdl> lstTemp = new ArrayList<>();
        TxtHCorporateEmployee_Mdl listNode;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select cem.code, empName, empCode, cem.policyNo, cardHolderName, "
                    + "                 relationShip, rank, cem.corporateCode, cm.name corporate "
                    + "                 from corporateEmployeeMast cem "
                    + "                 left join corporateMast cm on cm.code = cem.corporateCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCorporateEmployee_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("empName")));
                listNode.setCardHolderName(DmShared.xNull(rsTemp.getString("cardHolderName")));
                listNode.setPolicyNo(DmShared.xNull(rsTemp.getString("policyNo")));
                listNode.setRank(DmShared.xNull(rsTemp.getString("rank")));
                listNode.setRelationShip(DmShared.xNull(rsTemp.getString("relationShip")));
                listNode.setCorporateName(DmShared.xNull(rsTemp.getString("corporate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInsuranceCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> listNode = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        String sqlQuery = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from insuranceCompanyMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
}
