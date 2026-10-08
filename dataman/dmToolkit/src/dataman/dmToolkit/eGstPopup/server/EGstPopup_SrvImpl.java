package dataman.dmToolkit.eGstPopup.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.eGstPopup.shared.*;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import dataman.dmToolkit.gst.shared.EGstToken_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EGstPopup_SrvImpl extends DmRSSPrivate implements EGstPopup_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransportMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.TransportMode transportMode : DmEnum.TransportMode.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(transportMode.getId())));
                listNode.setSharedKeyFieldName(DmShared.xNull(transportMode.getValue()));
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
    public DmResultPageing<List<TxtHTranporter_Mdl>> getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTranporter_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTranporter_Mdl> lstTemp = new ArrayList<>();
        TxtHTranporter_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select sg.subCode as code, sg.name , sg.legalName ,sg.manualCode , \n"
                    + " sg.tradeName ,sg.add1,sg.add2,sg.gstin,sg.gstinDate ,st.code stateCode,st.name as stateName, \n"
                    + " sg.pin as pinCode ,st.numericCode as stateNumericCode, sg.transporterId, \n"
                    + " c.cityCode as cityCode, c.cityName as cityName ,sg.mobile,sg.email \n"
                    + " from subGroup sg \n"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode \n"
                    + " LEFT JOIN city c on c.cityCode = sg.cityCode \n"
                    + " LEFT JOIN stateMast st on st.code = c.stateCode \n"
                    + " where sg.isActive = 1 \n"
                    + " AND sgsl.site_Code = :site_Code \n"
                    + " AND sg.nature = :nature \n";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("nature", DmServer.getCodeViaManualCode("TP", "manualCode", "subGroupNature", "code", cnTemp));

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHTranporter_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                listNode.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                //<editor-fold defaultstate="collapsed" desc="Transporter id">
                String transporterId = "";
                if (!DmShared.xNull(rsTemp.getString("gstin")).trim().equalsIgnoreCase("")) {
                    transporterId = DmShared.xNull(rsTemp.getString("gstin"));
                } else if (!DmShared.xNull(rsTemp.getString("transporterId")).trim().equalsIgnoreCase("")) {
                    transporterId = DmShared.xNull(rsTemp.getString("transporterId"));
                }
                listNode.setTransporterId(DmShared.xNull(transporterId));
                //</editor-fold>
                listNode.setLegalName(DmShared.xNull(rsTemp.getString("legalName")));
                listNode.setTradeName(DmShared.xNull(rsTemp.getString("tradeName")));
                listNode.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                listNode.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                listNode.setPinCode(DmShared.xNull(rsTemp.getString("pinCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setStateNumericCode(DmShared.xNull(rsTemp.getString("stateNumericCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));

                //<editor-fold defaultstate="collapsed" desc="Validate Phone and Email">
                String mobile = DmShared.xNull(rsTemp.getString("mobile"));
                String email = DmShared.xNull(rsTemp.getString("email"));
                try {
                    if (mobile.length() > 10) {
                        mobile = mobile.substring(mobile.length() - 10, mobile.length());
                    }
                    DmShared.isValidMobile(mobile, 0);
                } catch (Exception e) {
                    mobile = "";
                }
                try {
                    DmShared.isValidEMail(email, 0);
                } catch (Exception e) {
                    email = "";
                }
                listNode.setPhone(DmShared.xNull(mobile));
                listNode.setEmail(DmShared.xNull(email));
                //</editor-fold>
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getHVehicleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT vt.code AS code , vt.name AS name, vt.gstVehicleType "
                    + " FROM vehicleTypeMast vt ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("gstVehicleType")));
                lstTemp.add(listNode);
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
    public DmResultPageing<List<KeyFieldExtended>> getHEwaySubSupplyType(String supplyType, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.Eway_SubSupplyType subSupplyType : DmEnum.Eway_SubSupplyType.values()) {
                if (DmShared.xNull(subSupplyType.getSupplyType()).equalsIgnoreCase(supplyType)
                        || DmShared.xNull(subSupplyType.getSupplyType()).equalsIgnoreCase(DmEnum.Eway_SupplyType.BOTH_INWARD_OUTWARD.getCode())) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(subSupplyType.getCode())));
                    listNode.setSharedKeyFieldName(DmShared.xNull(subSupplyType.getName()));
                    lstTemp.add(listNode);
                }
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
    public DmResultPageing<List<KeyFieldExtended>> getHEwayDocumentType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.Eway_DocType docType : DmEnum.Eway_DocType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(docType.getCode())));
                listNode.setSharedKeyFieldName(DmShared.xNull(docType.getName()));
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
    public DmResultPageing<List<KeyFieldExtended>> getHEwayTransactionType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.Eway_TransactionType docType : DmEnum.Eway_TransactionType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(docType.getCode())));
                listNode.setSharedKeyFieldName(DmShared.xNull(docType.getValue()));
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
    public DmResult<EGstToken_Mdl> getAuthToken(int actionCode) {
        DmResult<EGstToken_Mdl> mrTemp = new DmResult<EGstToken_Mdl>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            EGstToken_Mdl eGstToken_Mdl = Gst_Server.getToken(cnTemp, this);
            if ((eGstToken_Mdl == null)
                    || (DmShared.xNull(eGstToken_Mdl.getTokenExpiry()).trim().equalsIgnoreCase(""))) {
                eGstToken_Mdl = new EGstToken_Mdl();
                eGstToken_Mdl = Gst_Server.getAuthToken(actionCode, cnTemp, this);
            }
            mrTemp.setData(eGstToken_Mdl);
            cnTemp.close();
            mrTemp.setStatus(true);
        } catch (Exception e) {
            mrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return mrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> print(String ewayNo, int actionCode) {
        DmResultPageing<KeyFieldExtended> rstTemp = new DmResultPageing<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        rstTemp.setData(keyField);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            keyField = Gst_Server.printEWayBill(ewayNo, actionCode, cnTemp, this);
            rstTemp.setData(keyField);
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCancelReason(int actionCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            if (actionCode == DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getCode()) {
                for (DmEnum.EwayCancelReason cancelReason : DmEnum.EwayCancelReason.values()) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(cancelReason.getCode())));
                    listNode.setSharedKeyFieldName(DmShared.xNull(cancelReason.getReason()));
                    lstTemp.add(listNode);
                }
            } else if (actionCode == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
                for (DmEnum.EinvCancelReason cancelReason : DmEnum.EinvCancelReason.values()) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(cancelReason.getCode())));
                    listNode.setSharedKeyFieldName(DmShared.xNull(cancelReason.getReason()));
                    lstTemp.add(listNode);
                }
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
    public DmResultPageing<List<KeyFieldExtended>> getHEinvSupplyType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.Einv_SupplyTypeCode supplyType : DmEnum.Einv_SupplyTypeCode.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(supplyType.getCode())));
                listNode.setSharedKeyFieldName(DmShared.xNull(supplyType.getValue()));
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
    public DmResultPageing<List<KeyFieldExtended>> getHEinvDocumentType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (DmEnum.Einv_DocTypeCode docType : DmEnum.Einv_DocTypeCode.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(docType.getCode()));
                listNode.setSharedKeyFieldName(DmShared.xNull(docType.getValue()));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.GstDocumentType type : DmEnum.GstDocumentType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(type.getShortName());
                listNode.setSharedKeyFieldName(type.getValue());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDocumentNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.GstDocumentNature nature : DmEnum.GstDocumentNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(nature.getShortName());
                listNode.setSharedKeyFieldName(nature.getValue());
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
    public DmResultPageing<List<TxtHIrpInfo_Mdl>> getTxtHIrp(int irpTypeCode, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHIrpInfo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHIrpInfo_Mdl> lstTemp = new ArrayList<>();
        TxtHIrpInfo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT  irm.code, irm.name as irpName \n"
                    + " , irm.url \n"
                    + "	FROM irpMast irm \n"
                    + " WHERE irm.type = :irpType \n"
                    + " AND irm.isActive = 1");
            sqlWithParam.setInteger("irpType", irpTypeCode);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHIrpInfo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("irpName")));
                listNode.setUrl(DmShared.xNull(rsTemp.getString("url")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResult<EGstParam_Mdl> getEgstParamSessionDetail() {
        DmResult<EGstParam_Mdl> dmrTemp = new DmResult();
        EGstParam_Mdl mrTemp = new EGstParam_Mdl();
        dmrTemp.setData(mrTemp);
        try {
            //<editor-fold defaultstate="collapsed" desc="getEgstParamDetails">
            mrTemp = (EGstParam_Mdl) DmBaseServer.getSsnData(this, "eGstParam");
            //</editor-fold>
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;

    }

    @Override
    public DmResult<String> setEgstParamSessionDetail(EGstParam_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        dmrTemp.setData("");
        try {
            //<editor-fold defaultstate="collapsed" desc="getEgstParamDetails">
            DmServer.setSsnData(this, "eGstParam", mrTemp);
            //</editor-fold>
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;

    }

    @Override
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(String gstin, String stateNumericCode, String subCode, String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        String sqlQuery = "", tempStateNumericCode = "", condition = "";
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(gstin).trim().equalsIgnoreCase("") && !DmShared.xNull(gstin).equalsIgnoreCase("URP")) {
                tempStateNumericCode = DmShared.xNull(gstin).substring(0, 2);
            } else if (!DmShared.xNull(stateNumericCode).trim().equalsIgnoreCase("")) {
                tempStateNumericCode = DmShared.xNull(stateNumericCode);
            } else if (!DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                tempStateNumericCode = DmServer.getCodeViaManualCode(DmShared.xNull(siteCode), "code", "siteMast", "stateNumericCode", cnTemp);
            } else if (!DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                sqlQuery = "SELECT sm.numericCode \n"
                        + "FROM subGroup sg \n"
                        + "LEFT JOIN city c ON c.cityCode = sg.cityCode \n"
                        + "LEFT JOIN stateMast sm On sm.code = c.stateCode \n"
                        + "WHERE sg.subCode = :subCode \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    tempStateNumericCode = DmShared.xNull(rsTemp.getString("numericCode"));
                }
                rsTemp.close();
                psTemp.close();
            }
            if (!DmShared.xNull(tempStateNumericCode).trim().equalsIgnoreCase("")) {
                condition += " AND sm.numericCode = :stateNumericCode \n";
            }
            sqlWithParam.setSQL(" select ct.cityCode,ct.cityName,ct.stateCode,sm.name as stateName,sm.numericCode, \n"
                    + " ct.countryCode,ctr.name as countryName, ctr.gstCode AS countryGstCode, ct.districtCode, cd.cityName AS districtName \n"
                    + " FROM city ct \n"
                    + " LEFT JOIN stateMast sm on ct.stateCode = sm.code \n"
                    + " LEFT JOIN country ctr on ct.countryCode = ctr.code \n"
                    + " LEFT JOIN city cd on cd.cityCode = ct.districtCode \n"
                    + " WHERE 1=1 \n" + condition);
            if (!DmShared.xNull(tempStateNumericCode).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("stateNumericCode", tempStateNumericCode);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setDistrictCode(DmShared.xNull(rsTemp.getString("districtCode")));
                listNode.setDistrictName(DmShared.xNull(rsTemp.getString("districtName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setStateNumericCode(DmShared.xNull(rsTemp.getString("numericCode")));
                listNode.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
                listNode.setGstCountryCode(DmShared.xNull(rsTemp.getString("countryGstCode")));
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
