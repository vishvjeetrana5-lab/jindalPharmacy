package aarogya.master.prescriptionGuiMapping.server;

import aarogya.master.prescriptionGuiMapping.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionGuiMapping_SrvImpl extends DmRSSPrivate implements PrescriptionGuiMapping_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (MappingNature mappingNature : MappingNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(mappingNature.getId()).toString());
                listNode.setSharedKeyFieldName(mappingNature.getValue());
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

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select  natureCode, max(case natureCode "
                    + "		when " + MappingNature.NABH_INDICATOR_1.getId() + " then '" + MappingNature.NABH_INDICATOR_1.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_2.getId() + " then '" + MappingNature.NABH_INDICATOR_2.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_3.getId() + " then '" + MappingNature.NABH_INDICATOR_3.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_4.getId() + " then '" + MappingNature.NABH_INDICATOR_4.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_5.getId() + " then '" + MappingNature.NABH_INDICATOR_5.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_6.getId() + " then '" + MappingNature.NABH_INDICATOR_6.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_7.getId() + " then '" + MappingNature.NABH_INDICATOR_7.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_8.getId() + " then '" + MappingNature.NABH_INDICATOR_8.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_9.getId() + " then '" + MappingNature.NABH_INDICATOR_9.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_10.getId() + " then '" + MappingNature.NABH_INDICATOR_10.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_11.getId() + " then '" + MappingNature.NABH_INDICATOR_11.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_12.getId() + " then '" + MappingNature.NABH_INDICATOR_12.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_13.getId() + " then '" + MappingNature.NABH_INDICATOR_13.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_14.getId() + " then '" + MappingNature.NABH_INDICATOR_14.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_15.getId() + " then '" + MappingNature.NABH_INDICATOR_15.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_16.getId() + " then '" + MappingNature.NABH_INDICATOR_16.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_17.getId() + " then '" + MappingNature.NABH_INDICATOR_17.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_18.getId() + " then '" + MappingNature.NABH_INDICATOR_18.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_19.getId() + " then '" + MappingNature.NABH_INDICATOR_19.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_20.getId() + " then '" + MappingNature.NABH_INDICATOR_20.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_21.getId() + " then '" + MappingNature.NABH_INDICATOR_21.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_22.getId() + " then '" + MappingNature.NABH_INDICATOR_22.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_23.getId() + " then '" + MappingNature.NABH_INDICATOR_23.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_24.getId() + " then '" + MappingNature.NABH_INDICATOR_24.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_25.getId() + " then '" + MappingNature.NABH_INDICATOR_25.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_26.getId() + " then '" + MappingNature.NABH_INDICATOR_26.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_27.getId() + " then '" + MappingNature.NABH_INDICATOR_27.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_28.getId() + " then '" + MappingNature.NABH_INDICATOR_28.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_29.getId() + " then '" + MappingNature.NABH_INDICATOR_29.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_30.getId() + " then '" + MappingNature.NABH_INDICATOR_30.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_31.getId() + " then '" + MappingNature.NABH_INDICATOR_31.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_32.getId() + " then '" + MappingNature.NABH_INDICATOR_32.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_33.getId() + " then '" + MappingNature.NABH_INDICATOR_33.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_34.getId() + " then '" + MappingNature.NABH_INDICATOR_34.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_35.getId() + " then '" + MappingNature.NABH_INDICATOR_35.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_36.getId() + " then '" + MappingNature.NABH_INDICATOR_36.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_37.getId() + " then '" + MappingNature.NABH_INDICATOR_37.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_38.getId() + " then '" + MappingNature.NABH_INDICATOR_38.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_39.getId() + " then '" + MappingNature.NABH_INDICATOR_39.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_40.getId() + " then '" + MappingNature.NABH_INDICATOR_40.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_41.getId() + " then '" + MappingNature.NABH_INDICATOR_41.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_42.getId() + " then '" + MappingNature.NABH_INDICATOR_42.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_43.getId() + " then '" + MappingNature.NABH_INDICATOR_43.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_44.getId() + " then '" + MappingNature.NABH_INDICATOR_44.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_45.getId() + " then '" + MappingNature.NABH_INDICATOR_45.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_46.getId() + " then '" + MappingNature.NABH_INDICATOR_46.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_47.getId() + " then '" + MappingNature.NABH_INDICATOR_47.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_48.getId() + " then '" + MappingNature.NABH_INDICATOR_48.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_49.getId() + " then '" + MappingNature.NABH_INDICATOR_49.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_50.getId() + " then '" + MappingNature.NABH_INDICATOR_50.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_51.getId() + " then '" + MappingNature.NABH_INDICATOR_51.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_52.getId() + " then '" + MappingNature.NABH_INDICATOR_52.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_53.getId() + " then '" + MappingNature.NABH_INDICATOR_53.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_54.getId() + " then '" + MappingNature.NABH_INDICATOR_54.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_55.getId() + " then '" + MappingNature.NABH_INDICATOR_55.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_56.getId() + " then '" + MappingNature.NABH_INDICATOR_56.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_57.getId() + " then '" + MappingNature.NABH_INDICATOR_57.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_58.getId() + " then '" + MappingNature.NABH_INDICATOR_58.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_59.getId() + " then '" + MappingNature.NABH_INDICATOR_59.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_60.getId() + " then '" + MappingNature.NABH_INDICATOR_60.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_61.getId() + " then '" + MappingNature.NABH_INDICATOR_61.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_62.getId() + " then '" + MappingNature.NABH_INDICATOR_62.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_63.getId() + " then '" + MappingNature.NABH_INDICATOR_63.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_64.getId() + " then '" + MappingNature.NABH_INDICATOR_64.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_65.getId() + " then '" + MappingNature.NABH_INDICATOR_65.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_66.getId() + " then '" + MappingNature.NABH_INDICATOR_66.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_67.getId() + " then '" + MappingNature.NABH_INDICATOR_67.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_68.getId() + " then '" + MappingNature.NABH_INDICATOR_68.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_69.getId() + " then '" + MappingNature.NABH_INDICATOR_69.getValue() + "' "
                    + "		when " + MappingNature.NABH_INDICATOR_70.getId() + " then '" + MappingNature.NABH_INDICATOR_70.getValue() + "' "
//                    + "		when " + MappingNature.NURSING_ROUTINE.getId() + " then '" + MappingNature.NURSING_ROUTINE.getValue() + "' "
//                    + "		when " + MappingNature.OT_ANESTHESIA.getId() + " then '" + MappingNature.OT_ANESTHESIA.getValue() + "' "
//                    + "		when " + MappingNature.OT_OPERATION.getId() + " then '" + MappingNature.OT_OPERATION.getValue() + "' "
//                    + "		when " + MappingNature.OT_OPERATION_START.getId() + " then '" + MappingNature.OT_OPERATION_START.getValue() + "' "
//                    + "		when " + MappingNature.OT_POST_ANESTHESIA.getId() + " then '" + MappingNature.OT_POST_ANESTHESIA.getValue() + "' "
//                    + "		when " + MappingNature.OT_POST_OPERATION.getId() + " then '" + MappingNature.OT_POST_OPERATION.getValue() + "' "
//                    + "		when " + MappingNature.OT_PRE_ANESTHESIA.getId() + " then '" + MappingNature.OT_PRE_ANESTHESIA.getValue() + "' "
//                    + "		when " + MappingNature.OT_PRE_OPERATION.getId() + " then '" + MappingNature.OT_PRE_OPERATION.getValue() + "' "
//                    + "		when " + MappingNature.OT_STERILIZATION.getId() + " then '" + MappingNature.OT_STERILIZATION.getValue() + "' "
                    + "		when " + MappingNature.POINT_Of_Care.getId() + " then '" + MappingNature.POINT_Of_Care.getValue() + "' "
                    + "		end) as nature, "
                    + "		stuff(("
                    + "			select  ',' + gtm.name "
                    + "					from patientPrescriptionGUIMapping "
                    + "					left join guiTemplateMast gtm on gtm.code = guiTemplateCode "
                    + "					where natureCode = ppgm.natureCode "
                    + "					order by gtm.name for xml path(''), type).value('.[1]', 'nvarchar(max)'), 1, 1, '') as guiname"
                    + "		from patientPrescriptionGUIMapping ppgm "
                    + "		group by natureCode");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("natureCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setGuiTemplateName(DmShared.xNull(rsTemp.getString("guiname")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGUITempName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select code, name  from guitemplatemast");
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
    public DmResult<String> saveRecord(PrescriptionGuiMapping_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        int vno = 0;
        dmrTemp.setData("");
        try {
            PrescriptionGuiMapping_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "delete from patientPrescriptionGUIMapping where natureCode= :natureCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("natureCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GridDescription_Mdl node : mrTemp.getDescrip_Mdl()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() > 0) {
                    vno++;
                    sqlQuery = "insert into patientPrescriptionGUIMapping(natureCode,v_No,guiTemplateCode) "
                            + " values (:natureCode, :vno, :guicode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("natureCode", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("vno", DmServer.vNull(vno, "0"));
                    psTemp.setString("guicode", DmShared.xNull(node.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<PrescriptionGuiMapping_Mdl> getMoveRec(String strKey) {
        DmResult<PrescriptionGuiMapping_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PrescriptionGuiMapping_Mdl mrTemp = new PrescriptionGuiMapping_Mdl();
        GridDescription_Mdl node;
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  ppgm.natureCode, ppgm.guiTemplateCode, gtm.name "
                    + "		from patientPrescriptionGUIMapping ppgm "
                    + "		left join guiTemplateMast gtm on gtm.code = ppgm.guiTemplateCode "
                    + "         where ppgm.natureCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("natureCode")));
                mrTemp.setSharedKeyFieldName(MappingNature.getNatureId(DmShared.iNull(rsTemp.getString("natureCode")).intValue()).getValue());
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  ppgm.guiTemplateCode, gtm.name "
                    + "         from patientPrescriptionGUIMapping ppgm "
                    + "         left join guiTemplateMast gtm on gtm.code = ppgm.guiTemplateCode "
                    + "         where ppgm.natureCode = :natureCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("natureCode", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GridDescription_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.getDescrip_Mdl().add(node);
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
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

    PrescriptionGuiMapping_Mdl getMoveRec(String seartKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PrescriptionGuiMapping_Mdl mrTemp = new PrescriptionGuiMapping_Mdl();
        GridDescription_Mdl node;
        try {

            sqlQuery = "select  ppgm.natureCode, ppgm.guiTemplateCode, gtm.name "
                    + "		from patientPrescriptionGUIMapping ppgm "
                    + "		left join guiTemplateMast gtm on gtm.code = ppgm.guiTemplateCode "
                    + "         where ppgm.natureCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", seartKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("natureCode")));
                mrTemp.setSharedKeyFieldName(MappingNature.getNatureId(DmShared.iNull(rsTemp.getString("natureCode")).intValue()).getValue());
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  ppgm.guiTemplateCode, gtm.name "
                    + "         from patientPrescriptionGUIMapping ppgm "
                    + "         left join guiTemplateMast gtm on gtm.code = ppgm.guiTemplateCode "
                    + "         where ppgm.natureCode = :natureCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("natureCode", seartKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GridDescription_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("guicode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("guiname")));
                mrTemp.getDescrip_Mdl().add(node);
            }
            psTemp.close();
            rsTemp.close();
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
        return mrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportJson(String natureCode, String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<>();
        String fileName = "";
        char separator = File.separatorChar;
        String[] listNatureCode;
        Connection cnTemp = null;
        List<PrescriptionGuiMapping_Mdl> lstModel = new ArrayList<PrescriptionGuiMapping_Mdl>();
        try {

            listNatureCode = natureCode.split(",");
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            fileName = "PrescriptionGUI";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".json";
            String fullFilePath = directoryPath + separator + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }

            for (String nature : listNatureCode) {
                lstModel.add(getMoveRec(nature, cnTemp));
            }

            KeyFieldExtended field = new KeyFieldExtended();

            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName(serverFileName);

            Gson gson = new Gson();
            String json = gson.toJson(lstModel, new TypeToken<List<PrescriptionGuiMapping_Mdl>>() {
            }.getType());
            FileWriter writer = new FileWriter(fullFilePath);
            writer.write(json);
            writer.close();
            dmrTemp.setStatus(true);
            dmrTemp.setData(field);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<PrescriptionGuiMapping_Mdl> importJson(String filePath, String fileName) {
        DmResult<PrescriptionGuiMapping_Mdl> dmrTemp = new DmResult<>();
        PrescriptionGuiMapping_Mdl mrTemp = new PrescriptionGuiMapping_Mdl();
        dmrTemp.setData(mrTemp);
        try {
            Gson gson = new Gson();
            String readDirectoryPath = filePath + "\\" + fileName;
            BufferedReader br = new BufferedReader(new FileReader(readDirectoryPath));
            mrTemp = gson.fromJson(br, PrescriptionGuiMapping_Mdl.class);
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }

}
