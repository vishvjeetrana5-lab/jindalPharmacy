package dataman.payroll.shiftMaster.server;

import dataman.payroll.shiftMaster.server.*;
import dataman.payroll.shiftMaster.shared.*;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.payroll.self.shared.PREnum.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShiftMaster_SrvImpl extends DmRSSPrivate implements ShiftMaster_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();

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
            rsTemp = cnTemp.createStatement().executeQuery("select code from shiftMast ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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

            sqlQuery = "delete from shiftMast where code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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
    public DmResult<ShiftMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ShiftMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ShiftMaster_Mdl mrTemp = new ShiftMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select sm.code,sm.shiftName,sm.shortName,sm.fromTime,\n"
                    + "sm.toTime,sm.duration,sm.inTimeGrace,sm.outTimeGrace,sm.includesNextDay\n"
                    + "from shiftMast sm\n"
                    + "where code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shiftName")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setFromTime(DmServer.vNull(rsTemp.getDouble("fromTime"), "0.00"));
                mrTemp.setToTime(DmServer.vNull(rsTemp.getDouble("toTime"), "0.00"));
                mrTemp.setIncludesNexDay(DmShared.xNull(rsTemp.getString("includesNextDay")));
                mrTemp.setIncludesNexDayName(DmShared.xNull(rsTemp.getString("includesNextDay")).trim().equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setDuration(DmShared.xNull(rsTemp.getString("duration")));
                mrTemp.setInTimeGrace(DmServer.vNull(rsTemp.getDouble("inTimeGrace"), "0.00"));
                mrTemp.setOutTimeGrace(DmServer.vNull(rsTemp.getDouble("outTimeGrace"), "0.00"));

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
    public String[] getServerDateTime() {
        String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }

    @Override

    public DmResult<String> getDuration(String fromTime, String toTime, String includesNextDay) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        String duration = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (!DmShared.xNull(fromTime).equalsIgnoreCase("") && !DmShared.xNull(toTime).equalsIgnoreCase("")) {

                if (DmShared.iNull(includesNextDay).intValue()==0){
                    sqlQuery = "select DATEDIFF(minute, cast(Convert(date, getdate())  as varchar(10))+space(1)+:fromTime, cast(Convert(date, getdate())  as varchar(10))+space(1)+:toTime) duration";
                }else{
                    sqlQuery = "select DATEDIFF(minute, cast(Convert(date, getdate())  as varchar(10))+space(1)+:fromTime, cast(Convert(date, dateadd(d,1,getdate()))  as varchar(10))+space(1)+:toTime) duration";
                }
                
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("fromTime", fromTime);
                psTemp.setString("toTime", toTime);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    duration = rsTemp.getString("duration");
                    if (DmShared.iNull(duration).intValue() < 0) {
                        duration = DmShared.iNull(1440 - DmShared.iNull(duration).intValue()).toString();
                    }
                }

                psTemp.close();

                spBegin.commit();
                cnTemp.close();
            }

            dmrTemp.setStatus(true);
            dmrTemp.setData(duration);

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
    public DmResult<String> saveRecord(ShiftMaster_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        try {

            ShiftMaster_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "shiftMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /**
             * ****************Checking previous entry for same
             * shiftName********************
             */
            sqlQuery = "select code from shiftMast \n"
                    + "where shiftName = :name and code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getShiftName()));
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("code")).longValue() != 0) {
                    throw new DmException(100, "Shift Name already exist.");
                }
            }
            rsTemp.close();
            psTemp.close();
            /**
             * ****************************************************************************************
             */

            /**
             * ****************Checking previous entry for same
             * shorttName********************
             */
            sqlQuery = "select code from shiftMast \n"
                    + "where shortName = :name and code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("code")).longValue() != 0) {
                    throw new DmException(100, "Short Name already exist.");
                }
            }
            rsTemp.close();
            psTemp.close();
            /**
             * ****************************************************************************************
             */

            if (blnAdd) {
                sqlQuery = "insert into shiftMast(code,shortName,shiftName\n"
                        + " ,fromTime,toTime,duration,inTimeGrace,outTimeGrace,u_Name,u_EntDt,includesNextDay)  "
                        + " values(:code,:shortName,:shiftName\n"
                        + " ,:fromTime,:toTime,:duration,:inTime,:outTime,:u_Name,:u_EntDt, :includesNextDay)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("shiftName", DmShared.xNull(mrTemp.getShiftName()));
                psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime()));
                psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime()));
                psTemp.setString("includesNextDay", DmShared.xNull(mrTemp.getIncludesNexDay()));
                psTemp.setString("duration", DmServer.vNull(mrTemp.getDuration()));
                psTemp.setString("inTime", DmServer.vNull(mrTemp.getInTimeGrace()));
                psTemp.setString("outTime", DmServer.vNull(mrTemp.getOutTimeGrace()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "update shiftMast set"
                        + " shortName = :shortName,"
                        + "shiftName = :shiftName,"
                        + "fromTime = :fromTime,"
                        + "toTime = :toTime,"
                        + "includesNextDay= :includesNextDay,"
                        + "duration = :duration,"
                        + "inTimeGrace = :inTimeGrace,"
                        + "outTimeGrace = :outTimeGrace"
                        + " where code = :code";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("shiftName", DmShared.xNull(mrTemp.getShiftName()));
                psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime()));
                psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime()));
                psTemp.setString("includesNextDay", DmShared.xNull(mrTemp.getIncludesNexDay()));
                psTemp.setString("duration", DmServer.vNull(mrTemp.getDuration()));
                psTemp.setString("inTimeGrace", DmServer.vNull(mrTemp.getInTimeGrace()));
                psTemp.setString("outTimeGrace", DmServer.vNull(mrTemp.getOutTimeGrace()));

                psTemp.executeUpdate();
                psTemp.close();
            }

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
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select sm.code,sm.shiftName,sm.shortName,sm.fromTime,\n"
                    + "sm.toTime,sm.inTimeGrace,sm.outTimeGrace\n"
                    + "from shiftMast sm";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shiftName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setFromTime(DmShared.xNull(rsTemp.getString("fromTime")));
                listNode.setToTime(DmShared.xNull(rsTemp.getString("toTime")));
                listNode.setInTimeGrace(DmShared.xNull(rsTemp.getString("inTimeGrace")));
                listNode.setOutTimeGrace(DmShared.xNull(rsTemp.getString("outTimeGrace")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludesNextDaye(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT 0 as CODE, 'No' as NAME union all SELECT 1 as CODE, 'Yes' as NAME");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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

}
