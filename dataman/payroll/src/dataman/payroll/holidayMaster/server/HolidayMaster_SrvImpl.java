package dataman.payroll.holidayMaster.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.holidayMaster.shared.Find_Mdl;
import dataman.payroll.holidayMaster.shared.Holiday_Mdl;
import dataman.payroll.holidayMaster.shared.Holiday_Srv;
import dataman.payroll.self.shared.PREnum.HOLIDAY_TYPE;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HolidayMaster_SrvImpl extends DmRSSPrivate implements Holiday_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("Select Date  From holidayMast ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Date")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<String> saveRecord(Holiday_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean add = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.dNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                add = true;
                mrTemp.setSharedKeyFieldCode(mrTemp.getHolidayDate());
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*============================<Check Duplicate data>=================================*/
            if (add) {
                sqlQuery = "Select count(Date) as cnt from holidayMast where Date = :Date ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("Date", DmServer.convertDate(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Date already exists. ");
                }
                rsTemp.close();
                psTemp.close();
            }

            /*============================< Delete records from HolidayMaster >==============================*/
            sqlQuery = " Delete From holidayMast Where Date = :Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("Date", DmServer.convertDate(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into holidayMast (Date, holidayName, u_Name, u_EntDt, TypeCode )values "
                    + " (:Date, :holidayName, :u_Name, :uEntDt, :TypeCode) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("Date", DmServer.convertDate(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("holidayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("u_Name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("TypeCode", DmShared.iNull(mrTemp.getHolidayType().getId()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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

        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From holidayMast Where Date= :Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("Date", DmServer.convertDate(strKey));
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select Date, holidayName  as name , typeCode from holidayMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("Date")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHoliday(DmShared.xNull(rsTemp.getString("name")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("Date")));
                listNode.setHolidayType(HOLIDAY_TYPE.getViaKey(DmShared.iNull(rsTemp.getString("typeCode")).intValue()).getValue());

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
                rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            }

        }
        return rstTemp;
    }

    @Override
    public DmResult<Holiday_Mdl> getMoveRec(String strKey) {
        DmResult<Holiday_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Holiday_Mdl mrTemp = new Holiday_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  hm.Date, hm.holidayName, hm.u_Name, hm.TypeCode "
                    + "         From holidayMast as hm "
                    + "         where hm.Date = :Date";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Date", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("Date")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("holidayName")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setHolidayType(HOLIDAY_TYPE.getViaKey(DmShared.iNull(rsTemp.getString("TypeCode")).intValue()));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
           DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            
          
            for (HOLIDAY_TYPE status : HOLIDAY_TYPE.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(status.getId()).toString());
                listNode.setSharedKeyFieldName(status.getValue());
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

}
